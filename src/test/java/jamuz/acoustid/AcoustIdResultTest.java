package jamuz.acoustid;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AcoustIdResultTest {

    @Test
    void constructor_exposesArtistAndTitle() {
        AcoustIdResult result = new AcoustIdResult("Artist", "Title");
        assertEquals("Artist", result.getArtist());
        assertEquals("Title", result.getTitle());
    }

    @Test
    void score_isNullByDefault() {
        AcoustIdResult result = new AcoustIdResult("A", "T");
        assertNull(result.getScore());
    }

    @Test
    void setScore_updatesScore() {
        AcoustIdResult result = new AcoustIdResult("Artist", "Title");
        result.setScore("0.95");
        assertEquals("0.95", result.getScore());
    }

    @Test
    void setScore_canBeOverwritten() {
        AcoustIdResult result = new AcoustIdResult("A", "T");
        result.setScore("0.5");
        result.setScore("0.9");
        assertEquals("0.9", result.getScore());
    }
}
