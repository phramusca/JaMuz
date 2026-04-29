package jamuz.remote;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ServerTest {
    @Test
    void shouldBeLoadable() {
        assertNotNull(Server.class);
    }
}
