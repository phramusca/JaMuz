package jamuz.database;

import jamuz.Jamuz;
import jamuz.Playlist;
import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import jamuz.remote.ClientInfo;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoClient}. */
class DaoClientTest {

    private static DbConnJaMuz dbConnJaMuz;

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        seedOneClient("client-read-1");
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    private static void seedOneClient(String login) {
        String host = "ClientReadHost";
        dbConnJaMuz.machine().lock().getOrInsert(host, new StringBuilder(), false);
        Playlist playlist = new Playlist(0, "plClientRead", false, 0, Playlist.LimitUnit.Gio, false,
                Playlist.Type.Albums, Playlist.Match.All, false, "ext");
        dbConnJaMuz.playlist().lock().insert(playlist);
        Device device = new Device(-1, "devClientRead", "src", "dest", 1, host, true);
        device.setIdPlaylist(1);
        dbConnJaMuz.device().lock().insertOrUpdate(device);
        StatSource statSource = new StatSource(host);
        dbConnJaMuz.statSource().lock().insertOrUpdate(statSource);

        ClientInfo ci = new ClientInfo(login, "pwd-" + login, "/root", "name-" + login, true);
        ci.setDevice(new Device(1, "devClientRead", "src", "dest", 1, "", true));
        statSource.setId(1);
        statSource.setHidden(true);
        ci.setStatSource(statSource);

        Jamuz.setDb(dbConnJaMuz);
        Jamuz.readPlaylists();
        assertTrue(dbConnJaMuz.client().lock().insertOrUpdate(ci));
    }

    @Test
    void shouldExposeWriteLock() {
        assertNotNull(dbConnJaMuz.client().lock());
    }

    @Test
    void shouldReadClientByLogin() {
        ClientInfo client = dbConnJaMuz.client().get("client-read-1");
        assertEquals("client-read-1", client.getLogin());
        assertTrue(client.isEnabled());
    }

    @Test
    void shouldReadAllClients() {
        LinkedHashMap<Integer, ClientInfo> clients = new LinkedHashMap<>();
        assertTrue(dbConnJaMuz.client().get(clients));
        assertFalse(clients.isEmpty());
    }
}
