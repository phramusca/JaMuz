package jamuz.process.video;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TheMovieDbTest {
    @Test
    void shouldExposeExpectedConstructorShape() {
        assertNotNull(TheMovieDb.class);
        boolean hasThreeArgCtor = java.util.Arrays.stream(TheMovieDb.class.getDeclaredConstructors())
                .anyMatch(ctor -> ctor.getParameterCount() == 3);
        assertTrue(hasThreeArgCtor);
    }
}
