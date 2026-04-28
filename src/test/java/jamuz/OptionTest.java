package jamuz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OptionTest {

    @Test
    void shouldExposeOptionFieldsAndAllowValueUpdate() {
        Option option = new Option("location.library", "v1", 3, 7, "path");

        assertEquals("location.library", option.getId());
        assertEquals("v1", option.getValue());
        assertEquals(3, option.getIdMachine());
        assertEquals(7, option.getIdOptionType());
        assertEquals("path", option.getType());
        assertFalse(option.getComment().isBlank());

        option.setValue("v2");
        assertEquals("v2", option.getValue());
        assertEquals("location.library", option.toString());
    }
}
