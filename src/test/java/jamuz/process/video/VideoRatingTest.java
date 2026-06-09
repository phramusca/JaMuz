package jamuz.process.video;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VideoRatingTest {

    @Test
    void constructor_exposesRatingAndDisplay() {
        VideoRating vr = new VideoRating(4, "****");
        assertEquals(4, vr.getRating());
        assertEquals("****", vr.toString());
    }

    @Test
    void getRating_withZero_returnsZero() {
        assertEquals(0, new VideoRating(0, "").getRating());
    }

    @Test
    void toString_withEmptyDisplay_returnsEmpty() {
        assertEquals("", new VideoRating(1, "").toString());
    }
}
