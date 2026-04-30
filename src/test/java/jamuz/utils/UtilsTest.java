package jamuz.utils;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void equalLists_bothNull_returnsTrue() {
        assertTrue(Utils.equalLists(null, null));
    }

    @Test
    void equalLists_sameElementsDifferentOrder_returnsTrue() {
        assertTrue(Utils.equalLists(List.of("a", "b"), List.of("b", "a")));
    }

    @Test
    void equalLists_differentSizes_returnsFalse() {
        assertFalse(Utils.equalLists(List.of("a"), List.of("a", "b")));
    }

    @Test
    void equalLists_oneNullOtherEmpty_returnsFalse() {
        assertFalse(Utils.equalLists(null, List.of()));
        assertFalse(Utils.equalLists(List.of(), null));
    }

    @Test
    void equalLists_bothEmpty_returnsTrue() {
        assertTrue(Utils.equalLists(List.of(), List.of()));
    }

    @Test
    void equalLists_sameElements_returnsTrue() {
        assertTrue(Utils.equalLists(List.of("x", "y"), List.of("x", "y")));
    }

    @Test
    void getInteger_withValidPositive_returnsInt() {
        assertEquals(42, Utils.getInteger("42"));
    }

    @Test
    void getInteger_withNegativeNumber_returnsNegative() {
        assertEquals(-5, Utils.getInteger("-5"));
    }

    @Test
    void getInteger_withZero_returnsZero() {
        assertEquals(0, Utils.getInteger("0"));
    }

    @Test
    void getInteger_withNonNumeric_returnsMinusOne() {
        assertEquals(-1, Utils.getInteger("x"));
    }

    @Test
    void getInteger_withEmptyString_returnsMinusOne() {
        assertEquals(-1, Utils.getInteger(""));
    }

    @Test
    void getInteger_withNull_returnsMinusOne() {
        assertEquals(-1, Utils.getInteger(null));
    }
}
