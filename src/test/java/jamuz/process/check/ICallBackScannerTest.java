package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ICallBackScannerTest {

    @Test
    void shouldDefineCompletedCallback() {
        assertTrue(ICallBackScanner.class.isInterface());
        assertEquals("completed", ICallBackScanner.class.getDeclaredMethods()[0].getName());
    }
}
