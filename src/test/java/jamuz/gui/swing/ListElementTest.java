package jamuz.gui.swing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ListElementTest {

    @Test
    void shouldImplementDisplayEqualityAndClone() throws Exception {
        ListElement a = new ListElement("id", "display");
        ListElement b = new ListElement("id", "x");

        assertEquals("display", a.toString());
        a.setDisplay("shown");
        assertEquals("shown", a.toString());
        assertEquals("id", a.getValue());
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        ListElement clone = a.clone();
        assertEquals(a, clone);
        assertNotSame(a, clone);
    }
}
