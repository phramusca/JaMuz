/*
 * Copyright (C) 2023 phramusca
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jamuz.database;

import jamuz.Jamuz;
import jamuz.Playlist;
import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import jamuz.remote.ClientInfo;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;

/** SQLite-backed tests for {@link DaoClientWrite} insert/update behaviour (same setup pattern as {@link DaoClientTest}). */
public class DaoClientWriteTest {

    private static DbConnJaMuz dbConnJaMuz;
    private static DaoClientWrite writer;

    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        writer = new DaoClientWrite(dbConnJaMuz.getDbConn(), dbConnJaMuz.device(), dbConnJaMuz.statSource());
        seedMachinePlaylistDeviceStatSource();
        Jamuz.setDb(dbConnJaMuz);
        Jamuz.readPlaylists();
    }

    @AfterClass
    public static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    private static void seedMachinePlaylistDeviceStatSource() throws SQLException {
        String hostname = "DaoClientWriteTestHost";
        dbConnJaMuz.machine().lock().getOrInsert(hostname, new StringBuilder(), true);

        Playlist playlist = new Playlist(0, "plDaoClientWrite", false, 0, Playlist.LimitUnit.Gio, false,
                Playlist.Type.Albums, Playlist.Match.All, false, "ext");
        dbConnJaMuz.playlist().lock().insert(playlist);

        Device device = new Device(-1, "devName", "source", "dest", 1, hostname, true);
        device.setIdPlaylist(1);
        dbConnJaMuz.device().lock().insertOrUpdate(device);

        StatSource statSource = new StatSource(hostname);
        dbConnJaMuz.statSource().lock().insertOrUpdate(statSource);
    }

    private static ClientInfo newClientForInsert(String login) {
        ClientInfo clientInfo = new ClientInfo(login, "pwd-" + login, "rootPath", "disp-" + login, true);
        Device device = new Device(1, "devName", "source", "dest", 1, "", true);
        StatSource statSource = new StatSource(login);
        statSource.setId(1);
        statSource.setHidden(true);
        clientInfo.setDevice(device);
        clientInfo.setStatSource(statSource);
        return clientInfo;
    }

    @Test
    public void shouldInsertClientWhenIdIsUnset() {
        ClientInfo clientInfo = newClientForInsert("ins-writer-1");
        assertTrue(writer.insertOrUpdate(clientInfo));

        ClientInfo fromDb = dbConnJaMuz.client().get("ins-writer-1");
        assertEquals("disp-ins-writer-1-ins-w", fromDb.getName());
        assertEquals("pwd-ins-writer-1", fromDb.getPwd());
        assertTrue(fromDb.isEnabled());
    }

    @Test
    public void shouldUpdateClientWhenIdIsSet() {
        ClientInfo inserted = newClientForInsert("upd-writer-1");
        assertTrue(writer.insertOrUpdate(inserted));

        ClientInfo loaded = dbConnJaMuz.client().get("upd-writer-1");
        Device device = new Device(1, loaded.getDevice().getName(), loaded.getDevice().getSource(),
                loaded.getDevice().getDestination(), loaded.getDevice().getIdPlaylist(), "", loaded.getDevice().isHidden());
        StatSource statSource = new StatSource(loaded.getLogin());
        statSource.setId(1);
        statSource.setHidden(true);

        ClientInfo updated = new ClientInfo(loaded.getId(), "upd-writer-2", "newName", "newPwd", device, statSource, false);
        assertTrue(writer.insertOrUpdate(updated));

        ClientInfo fromDb = dbConnJaMuz.client().get("upd-writer-2");
        assertEquals("newName-upd-w", fromDb.getName());
        assertEquals("newPwd", fromDb.getPwd());
        assertFalse(fromDb.isEnabled());
    }

    /** Regression: older code passed {@code java.sql.Types.INTEGER} (value 4) to {@link PreparedStatement#setInt}, persisting 4 instead of NULL. */
    @Test
    public void shouldPersistNullDeviceAndStatSourceWhenAbsentOnInsert() throws SQLException {
        ClientInfo bare = new ClientInfo("null-fk-client", "p", "/r", "BareName", true);
        assertTrue(writer.insertOrUpdate(bare));

        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "SELECT idDevice, idStatSource FROM client WHERE login = ?")) {
            st.setString(1, "null-fk-client");
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                assertNull(rs.getObject("idDevice"));
                assertNull(rs.getObject("idStatSource"));
            }
        }
    }
}
