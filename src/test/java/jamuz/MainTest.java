package jamuz;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    void shouldExposeStaticMainMethod() throws Exception {
        Method m = Main.class.getDeclaredMethod("main", String[].class);
        assertTrue(Modifier.isStatic(m.getModifiers()));
    }
}
