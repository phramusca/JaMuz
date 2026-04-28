package jamuz.acoustid;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AcoustIdResultTest {

    @Test
    void shouldExposeArtistTitleAndScore() {
        AcoustIdResult result = new AcoustIdResult("Artist", "Title");
        result.setScore("0.95");

        assertEquals("Artist", result.getArtist());
        assertEquals("Title", result.getTitle());
        assertEquals("0.95", result.getScore());
    }
}
