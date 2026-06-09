package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ICallBackCoverTest {

    @Test
    void shouldDefineSetImageCallback() {
        assertTrue(ICallBackCover.class.isInterface());
        assertEquals("setImage", ICallBackCover.class.getDeclaredMethods()[0].getName());
    }
}
