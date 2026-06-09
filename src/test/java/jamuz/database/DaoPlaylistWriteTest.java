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

import jamuz.Playlist;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoPlaylistWrite}. */
class DaoPlaylistWriteTest {

    private static DbConnJaMuz dbConnJaMuz;
    private static DaoPlaylistWrite writer;

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        writer = new DaoPlaylistWrite(dbConnJaMuz.getDbConn());
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    private Playlist basePlaylist(String name) {
        return new Playlist(0, name, false, 0, Playlist.LimitUnit.Gio, false,
                Playlist.Type.Albums, Playlist.Match.All, false, "mp3");
    }

    private int findPlaylistId(String name) throws SQLException {
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "SELECT idPlaylist FROM playlist WHERE name = ?")) {
            st.setString(1, name);
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }
        }
    }

    @Test
    void shouldInsertPlaylist() throws SQLException {
        Playlist p = basePlaylist("PlWriteInsert");
        assertTrue(writer.insert(p));
        assertTrue(findPlaylistId("PlWriteInsert") > 0);
    }

    @Test
    void shouldUpdatePlaylistMetadata() throws SQLException {
        Playlist inserted = basePlaylist("PlWriteUpdate");
        assertTrue(writer.insert(inserted));
        int id = findPlaylistId("PlWriteUpdate");

        Playlist loaded = new Playlist(id, "PlWriteUpdated", true, 10, Playlist.LimitUnit.Gio, true,
                Playlist.Type.Songs, Playlist.Match.All, true, "flac");
        assertTrue(writer.update(loaded));

        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "SELECT name, limitDo, random FROM playlist WHERE idPlaylist = ?")) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                assertEquals("PlWriteUpdated", rs.getString("name"));
                assertTrue(rs.getBoolean("limitDo"));
                assertTrue(rs.getBoolean("random"));
            }
        }
    }

    @Test
    void shouldDeletePlaylistWhenNotReferenced() throws SQLException {
        Playlist p = basePlaylist("PlWriteDelete");
        assertTrue(writer.insert(p));
        int id = findPlaylistId("PlWriteDelete");
        assertTrue(writer.delete(id));
        assertFalse(writer.delete(id));
    }
}
