package jamuz.acoustid;

import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    void shouldParseScoreAsDouble() throws Exception {
        Result result = new Result();
        Field score = Result.class.getDeclaredField("score");
        score.setAccessible(true);
        score.set(result, "0.87");

        assertEquals(0.87, result.getScore(), 0.0001);
    }
}
