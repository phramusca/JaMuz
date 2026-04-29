package jamuz.gui;

import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PanelCoverTest {
    @Test
    void shouldBeConcreteClass() {
        assertFalse(Modifier.isAbstract(PanelCover.class.getModifiers()));
    }
}
