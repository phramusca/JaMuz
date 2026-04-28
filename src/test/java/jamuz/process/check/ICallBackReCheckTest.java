package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ICallBackReCheckTest {

    @Test
    void shouldDefineReCheckedCallback() {
        assertTrue(ICallBackReCheck.class.isInterface());
        assertEquals("reChecked", ICallBackReCheck.class.getDeclaredMethods()[0].getName());
    }
}
