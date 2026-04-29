package jamuz.process.check;

import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LocationTest {
    @Test
    void shouldBeConcreteClass() {
        assertFalse(Modifier.isAbstract(Location.class.getModifiers()));
        assertTrue(Location.class.getDeclaredConstructors().length > 0);
    }
}
