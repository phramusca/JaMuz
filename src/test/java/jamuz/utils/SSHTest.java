package jamuz.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * SSH is a thin jsch wrapper requiring a live server — cannot be tested
 * automatically. Tests cover the constructor contract only.
 */
class SSHTest {

    @Test
    void newInstance_canBeCreatedWithoutException() {
        // SSH.isConnected() would NPE before connect() is called (session is null);
        // we only verify that the constructor itself does not throw.
        assertDoesNotThrow(() -> new SSH("host", "user", "password"));
    }
}
