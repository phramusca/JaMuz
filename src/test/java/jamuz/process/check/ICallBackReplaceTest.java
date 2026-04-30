package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ICallBackReplaceTest {

    @Test
    void shouldDefineReplacedCallback() {
        assertTrue(ICallBackReplace.class.isInterface());
        assertEquals("replaced", ICallBackReplace.class.getDeclaredMethods()[0].getName());
    }
}
