package jamuz;

import java.awt.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatItemTest {

    @Test
    void shouldExposeStatValuesAndMutableLabelPercentage() {
        StatItem item = new StatItem("50 percent", "v", 10, 3, 200, 1200, 4.2, Color.BLUE);

        assertEquals(10, item.getCountFile());
        assertEquals(3, item.getCountPath());
        assertEquals(200, item.getSize());
        assertEquals(1200, item.getLength());
        assertEquals(4.2, item.getRating());
        assertEquals("50 percent", item.getLabel());
        assertEquals("50 %", item.getLabelForChart());
        assertEquals("v", item.getValue());
        assertEquals(Color.BLUE, item.getColor());

        item.setLabel("new");
        item.setPercentage(42.5f);
        assertEquals("new", item.getLabel());
        assertEquals(42.5f, item.getPercentage());
    }
}
