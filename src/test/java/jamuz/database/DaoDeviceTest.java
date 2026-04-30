package jamuz.database;

import jamuz.Playlist;
import jamuz.process.sync.Device;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoDevice}. */
class DaoDeviceTest {

    private static DbConnJaMuz dbConnJaMuz;

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        String host = "DeviceReadHost";
        dbConnJaMuz.machine().lock().getOrInsert(host, new StringBuilder(), false);
        dbConnJaMuz.playlist().lock().insert(new Playlist(0, "plDeviceRead", false, 0, Playlist.LimitUnit.Gio, false,
                Playlist.Type.Albums, Playlist.Match.All, false, "ext"));
        Device d = new Device(-1, "DeviceRead", "src", "dst", 1, host, true);
        d.setIdPlaylist(1);
        dbConnJaMuz.device().lock().insertOrUpdate(d);
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    void shouldExposeWriteLock() {
        assertNotNull(dbConnJaMuz.device().lock());
    }

    @Test
    void shouldReadDeviceByNameLookup() {
        Device d = dbConnJaMuz.device().get("DeviceReadHost");
        assertEquals(1, d.getId());
        assertEquals("DeviceRead", d.getName());
    }

    @Test
    void shouldReadDevicesByHost() {
        LinkedHashMap<Integer, Device> devices = new LinkedHashMap<>();
        assertTrue(dbConnJaMuz.device().get(devices, "DeviceReadHost", true));
        assertEquals(1, devices.size());
    }
}
