package jamuz.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Ftp is a thin Apache-Commons-Net wrapper requiring a live FTP server —
 * cannot be tested automatically. Tests cover the constructor contract only.
 */
class FtpTest {

    @Test
    void newInstance_canBeCreatedWithoutException() {
        assertDoesNotThrow(() -> new Ftp("ftp.example.invalid", "user", "pass",
                "/local", "/remote", "file.db"));
    }
}
