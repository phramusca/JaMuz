package jamuz.gui;

import jamuz.gui.swing.ListElement;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ListModelPlayerQueueTest {
    @Test
    void shouldAddElementsToQueue() {
        ListModelPlayerQueue queue = new ListModelPlayerQueue();
        queue.add(new ListElement("v", "d"));
        assertEquals(1, queue.getSize());
        assertNotNull(queue.getElementAt(0));
    }
}
