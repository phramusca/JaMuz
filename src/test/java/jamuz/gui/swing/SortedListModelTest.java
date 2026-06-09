package jamuz.gui.swing;

import java.util.Iterator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SortedListModelTest {

    @Test
    void shouldKeepElementsSortedAndSupportOperations() {
        SortedListModel<String> model = new SortedListModel<>();
        model.add("b");
        model.add("a");
        model.addAll(new Object[]{"c"});

        assertEquals(3, model.getSize());
        assertEquals("a", model.getElementAt(0));
        assertTrue(model.contains("b"));
        assertEquals("a", model.firstElement());
        assertEquals("c", model.lastElement());

        Iterator it = model.iterator();
        assertTrue(it.hasNext());
        assertTrue(model.removeElement("b"));
        model.clear();
        assertEquals(0, model.getSize());
    }
}
