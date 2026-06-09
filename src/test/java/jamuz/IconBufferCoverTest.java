package jamuz;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IconBufferCoverTest {

    @Test
    void getCoverIconSize_is50() {
        assertEquals(50, IconBufferCover.getCoverIconSize());
    }

    @Test
    void getCoverIcon_withNullFile_throwsNPE() {
        assertThrows(NullPointerException.class,
                () -> IconBufferCover.getCoverIcon(null, false));
    }
}
