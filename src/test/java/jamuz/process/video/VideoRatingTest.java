package jamuz.process.video;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VideoRatingTest {

    @Test
    void shouldExposeRatingAndDisplayText() {
        VideoRating vr = new VideoRating(4, "****");
        assertEquals(4, vr.getRating());
        assertEquals("****", vr.toString());
    }
}
