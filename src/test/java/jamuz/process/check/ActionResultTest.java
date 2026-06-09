package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ActionResultTest {

    @Test
    void shouldInitializeFromBooleanConstructor() {
        ActionResult res = new ActionResult(true);
        assertTrue(res.isPerformed);
        assertEquals("", res.status);
    }

    @Test
    void shouldInitializeFromStatusConstructor() {
        ActionResult res = new ActionResult("KO");
        assertFalse(res.isPerformed);
        assertEquals("KO", res.status);
    }
}
