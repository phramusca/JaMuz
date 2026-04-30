package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ICallBackDuplicatePanelTest {

    @Test
    void shouldDefineDuplicateCallback() {
        assertTrue(ICallBackDuplicatePanel.class.isInterface());
        assertEquals("duplicate", ICallBackDuplicatePanel.class.getDeclaredMethods()[0].getName());
    }
}
