package jamuz.process.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReleaseMatchTest {
    @Test
    void shouldExposeTrackNestedType() {
        assertNotNull(ReleaseMatch.Track.class);
    }
}
