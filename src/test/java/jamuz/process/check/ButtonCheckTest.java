package jamuz.process.check;

import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ButtonCheckTest {
    @Test
    void shouldBeConcreteEditorClass() {
        assertFalse(Modifier.isAbstract(ButtonCheck.class.getModifiers()));
    }
}
