package jamuz.acoustid;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChromaPrintTest {

    @Test
    void shouldExposeConstructorValues() {
        ChromaPrint cp = new ChromaPrint("abc", "123");
        assertEquals("abc", cp.getChromaprint());
        assertEquals("123", cp.getDuration());
    }
}
