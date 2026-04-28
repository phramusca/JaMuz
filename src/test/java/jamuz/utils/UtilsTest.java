package jamuz.utils;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    void shouldCompareListsIgnoringOrder() {
        assertTrue(Utils.equalLists(List.of("a", "b"), List.of("b", "a")));
        assertFalse(Utils.equalLists(List.of("a"), List.of("a", "b")));
        assertTrue(Utils.equalLists(null, null));
    }

    @Test
    void shouldParseIntegerOrReturnMinusOne() {
        assertEquals(42, Utils.getInteger("42"));
        assertEquals(-1, Utils.getInteger("x"));
    }
}
