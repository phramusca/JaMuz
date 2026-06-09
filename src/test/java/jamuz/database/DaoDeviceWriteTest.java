/*
 * Copyright (C) 2023 phramusca <phramusca@gmail.com>
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
import jamuz.process.sync.Device;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/**
 * Tests sur {@link DaoDeviceWrite}.
 */
class DaoDeviceWriteTest {

    private static DbConnJaMuz dbConnJaMuz;
    private static DaoDeviceWrite writer;
    private static final String HOST = "DaoDeviceWriteHost";

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        writer = new DaoDeviceWrite(dbConnJaMuz.getDbConn());

        Playlist playlist = new Playlist(0, "plDeviceWrite", false, 0, Playlist.LimitUnit.Gio, false,
                Playlist.Type.Albums, Playlist.Match.All, false, "ext");
        dbConnJaMuz.playlist().lock().insert(playlist);
        dbConnJaMuz.machine().lock().getOrInsert(HOST, new StringBuilder(), true);
        Jamuz.setDb(dbConnJaMuz);
        Jamuz.readPlaylists();
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @BeforeEach
    void wipeDevices() throws SQLException {
        try (Statement st = dbConnJaMuz.getDbConn().getConnection().createStatement()) {
            st.executeUpdate("DELETE FROM deviceFile");
            st.executeUpdate("DELETE FROM device");
            // Reset AUTOINCREMENT counter so IDs are predictable across test methods
            st.executeUpdate("DELETE FROM sqlite_sequence WHERE name='device'");
        }
    }

    private static String deviceNameInDb(int idDevice) throws SQLException {
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "SELECT name FROM device WHERE idDevice = ?")) {
            st.setInt(1, idDevice);
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                return rs.getString("name");
            }
        }
    }

    private static int deviceIdByName(String name) throws SQLException {
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "SELECT idDevice FROM device WHERE name = ?")) {
            st.setString(1, name);
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next(), "Device '" + name + "' should exist");
                return rs.getInt(1);
            }
        }
    }

    @Test
    void shouldInsertDeviceWhenIdIsUnset() throws SQLException {
        Device d = new Device(-1, "nIns", "sIns", "dIns", 1, HOST, false);
        assertTrue(writer.insertOrUpdate(d));
        assertEquals("nIns", deviceNameInDb(1));
    }

    @Test
    void shouldUpdateDeviceWhenIdIsSet() throws SQLException {
        Device d = new Device(-1, "nUpd", "sUpd", "dUpd", 1, HOST, true);
        assertTrue(writer.insertOrUpdate(d));
        int insertedId = deviceIdByName("nUpd");

        Device updated = new Device(insertedId, "nUpd2", "s2", "d2", 1, "", false);
        assertTrue(writer.insertOrUpdate(updated));
        assertEquals("nUpd2", deviceNameInDb(insertedId));
    }

    @Test
    void shouldDeleteDeviceById() throws SQLException {
        Device d = new Device(-1, "toDel", "s", "d", 1, HOST, true);
        assertTrue(writer.insertOrUpdate(d));
        int deviceId = deviceIdByName("toDel");
        assertTrue(writer.delete(deviceId));
        assertFalse(writer.delete(deviceId));
    }
}
