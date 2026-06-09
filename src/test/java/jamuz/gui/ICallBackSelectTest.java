package jamuz.gui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ICallBackSelectTest {

    @Test
    void shouldDefineRefreshCallback() throws Exception {
        assertTrue(ICallBackSelect.class.isInterface());
        assertEquals("refresh", ICallBackSelect.class.getDeclaredMethods()[0].getName());
    }
}
