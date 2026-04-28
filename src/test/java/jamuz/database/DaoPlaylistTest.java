package jamuz.database;

import jamuz.Playlist;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoPlaylist}. */
public class DaoPlaylistTest {

    private static DbConnJaMuz dbConnJaMuz;

    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
    }

    @AfterClass
    public static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    public void shouldExposeWriteLock() {
        assertNotNull(dbConnJaMuz.playlist().lock());
    }

    @Test
    public void shouldInsertAndReadPlaylists() {
        Playlist p = new Playlist(0, "PlaylistRead", false, 0, Playlist.LimitUnit.Gio, false,
                Playlist.Type.Songs, Playlist.Match.All, false, "ext");
        assertTrue(dbConnJaMuz.playlist().lock().insert(p));

        HashMap<Integer, Playlist> playlists = new HashMap<>();
        assertTrue(dbConnJaMuz.playlist().get(playlists));
        assertFalse(playlists.isEmpty());
    }
}
