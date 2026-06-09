package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ICallBackCheckPanelTest {

    @Test
    void shouldDefineQueueActionCallback() {
        assertTrue(ICallBackCheckPanel.class.isInterface());
        assertEquals("addToQueueAction", ICallBackCheckPanel.class.getDeclaredMethods()[0].getName());
    }
}
