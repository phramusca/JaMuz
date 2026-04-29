package jamuz.gui.swing;

import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ListRendererCoverTest {
    @Test
    void shouldBeConcreteRendererClass() {
        assertFalse(Modifier.isAbstract(ListRendererCover.class.getModifiers()));
        assertTrue(ListRendererCover.class.getDeclaredConstructors().length > 0);
    }
}
