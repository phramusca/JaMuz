package jamuz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OptionTest {

    private static Option option() {
        return new Option("location.library", "v1", 3, 7, "path");
    }

    @Test
    void constructor_exposesAllFields() {
        Option o = option();
        assertEquals("location.library", o.getId());
        assertEquals("v1", o.getValue());
        assertEquals(3, o.getIdMachine());
        assertEquals(7, o.getIdOptionType());
        assertEquals("path", o.getType());
    }

    @Test
    void getComment_isNotBlank() {
        assertFalse(option().getComment().isBlank());
    }

    @Test
    void setValue_updatesValue() {
        Option o = option();
        o.setValue("v2");
        assertEquals("v2", o.getValue());
    }

    @Test
    void setValue_doesNotAffectOtherFields() {
        Option o = option();
        o.setValue("changed");
        assertEquals("location.library", o.getId());
        assertEquals(3, o.getIdMachine());
    }

    @Test
    void toString_returnsId() {
        assertEquals("location.library", option().toString());
    }
}
