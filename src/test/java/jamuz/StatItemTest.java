package jamuz;

import java.awt.Color;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatItemTest {

    private StatItem item(String label) {
        return new StatItem(label, "v", 10, 3, 200, 1200, 4.2, Color.BLUE);
    }

    @Test
    void constructor_exposesAllFields() {
        StatItem i = item("lbl");
        assertEquals(10, i.getCountFile());
        assertEquals(3, i.getCountPath());
        assertEquals(200, i.getSize());
        assertEquals(1200, i.getLength());
        assertEquals(4.2, i.getRating());
        assertEquals("lbl", i.getLabel());
        assertEquals("v", i.getValue());
        assertEquals(Color.BLUE, i.getColor());
    }

    @Test
    void getPercentage_defaultIsMinusOne() {
        assertEquals(-1f, item("x").getPercentage());
    }

    @Test
    void setPercentage_updatesValue() {
        StatItem i = item("x");
        i.setPercentage(75.5f);
        assertEquals(75.5f, i.getPercentage());
    }

    @Test
    void setLabel_updatesLabel() {
        StatItem i = item("old");
        i.setLabel("new");
        assertEquals("new", i.getLabel());
    }

    @Test
    void getLabelForChart_replacesPercentWordWithSymbol() {
        // "percent" → "%"
        assertEquals("50 %", item("50 percent").getLabelForChart());
    }

    @Test
    void getLabelForChart_replacesPercentSymbolWithDash() {
        // "%" → "-"
        assertEquals("50-", item("50%").getLabelForChart());
    }

    @Test
    void getLabelForChart_mixedPercentSymbolAndWord() {
        // "%" replaced first with "-", then "percent" replaced with "%"
        assertEquals("50- %", item("50% percent").getLabelForChart());
    }

    @Test
    void getLabelForChart_withPlainLabel_returnsUnchanged() {
        assertEquals("rock", item("rock").getLabelForChart());
    }
}
