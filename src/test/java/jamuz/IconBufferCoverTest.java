package jamuz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IconBufferCoverTest {

    @Test
    void shouldExposeExpectedCoverIconSize() {
        assertEquals(50, IconBufferCover.getCoverIconSize());
    }
}
