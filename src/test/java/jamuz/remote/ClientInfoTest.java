package jamuz.remote;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClientInfoTest {
    @Test
    void shouldExposeCoreClientFields() {
        ClientInfo info = new ClientInfo("login", "pwd", "/r", "name", true);
        assertEquals("login", info.getLogin());
        assertEquals("pwd", info.getPwd());
        assertEquals("name", info.getName());
        assertTrue(info.isEnabled());
        assertNotNull(info.getProgressBar());
    }
}
