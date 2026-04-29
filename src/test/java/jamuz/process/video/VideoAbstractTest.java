package jamuz.process.video;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VideoAbstractTest {
    @Test
    void shouldDefineNestedStatusType() {
        assertNotNull(VideoAbstract.Status.class);
    }
}
