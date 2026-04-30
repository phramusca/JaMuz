package jamuz.gui.swing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableValueTest {

    @Test
    void shouldKeepRawValueAndAllowDisplayOverride() {
        TableValue value = new TableValue("raw");

        assertEquals("raw", value.getValue());
        assertEquals("raw", value.toString());

        value.setDisplay("shown");
        assertEquals("shown", value.toString());
    }
}
