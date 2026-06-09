package jamuz.process.merge;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatSourceTest {

    @Test
    void constructor_machineName_exposesName() {
        StatSource source = new StatSource("machine");
        assertEquals("machine", source.getMachineName());
    }

    @Test
    void isIsSelected_isTrueByDefault() {
        assertTrue(new StatSource("m").isIsSelected());
    }

    @Test
    void setIsSelected_updatesFlag() {
        StatSource s = new StatSource("m");
        s.setIsSelected(true);
        assertTrue(s.isIsSelected());
    }

    @Test
    void setHidden_and_equals_considersHiddenField() {
        StatSource a = new StatSource("m");
        StatSource b = new StatSource("m");
        assertEquals(a, b);

        b.setHidden(true);
        assertNotEquals(a, b);
    }

    @Test
    void equals_withSameFields_returnsTrue() {
        StatSource a = new StatSource("host");
        StatSource b = new StatSource("host");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equals_withDifferentMachineName_returnsFalse() {
        assertNotEquals(new StatSource("a"), new StatSource("b"));
    }

    @Test
    void toString_returnsSourceName() {
        assertNotNull(new StatSource("m").toString());
    }
}
