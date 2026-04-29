package jamuz.process.video;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MyMovieDbTest {
    @Test
    void shouldInheritFromMyVideoAbstract() {
        assertTrue(MyVideoAbstract.class.isAssignableFrom(MyMovieDb.class));
    }
}
