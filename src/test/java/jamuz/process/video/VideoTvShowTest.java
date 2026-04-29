package jamuz.process.video;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VideoTvShowTest {
    @Test
    void shouldBeSubtypeOfVideoAbstract() {
        assertTrue(VideoAbstract.class.isAssignableFrom(VideoTvShow.class));
    }
}
