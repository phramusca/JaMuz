package jamuz.database;

import jamuz.Playlist;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoPlaylist}. */
class DaoPlaylistTest {

    private static DbConnJaMuz dbConnJaMuz;

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    void shouldExposeWriteLock() {
        assertNotNull(dbConnJaMuz.playlist().lock());
    }

    @Test
    void shouldInsertAndReadPlaylists() {
        Playlist p = new Playlist(0, "PlaylistRead", false, 0, Playlist.LimitUnit.Gio, false,
                Playlist.Type.Songs, Playlist.Match.All, false, "ext");
        assertTrue(dbConnJaMuz.playlist().lock().insert(p));

        HashMap<Integer, Playlist> playlists = new HashMap<>();
        assertTrue(dbConnJaMuz.playlist().get(playlists));
        assertFalse(playlists.isEmpty());
    }
}
