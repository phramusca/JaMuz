package jamuz.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * OS detection is platform-dependent; we only verify API contracts.
 * Actual values are validated manually on each platform.
 */
class OSTest {

    @Test
    void detect_recognisesCurrentPlatform() {
        boolean recognised = OS.detect();
        assertTrue(recognised, "OS.detect() should recognise the CI/test platform");
    }

    @Test
    void getName_afterDetect_returnsNonNull() {
        OS.detect();
        assertNotNull(OS.getName());
    }

    @Test
    void isWindowsAndIsUnix_areExclusive() {
        OS.detect();
        assertFalse(OS.isWindows() && OS.isUnix(), "Cannot be both Windows and Unix");
    }

    @Test
    void isWindows_orIsUnix_afterDetect() {
        OS.detect();
        assertTrue(OS.isWindows() || OS.isUnix(),
                "After detect(), at least one platform flag should be set (Windows or Unix/Linux)");
    }
}
