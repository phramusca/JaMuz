package jamuz.acoustid;

import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    private Result resultWithScore(String scoreStr) throws Exception {
        Result result = new Result();
        Field score = Result.class.getDeclaredField("score");
        score.setAccessible(true);
        score.set(result, scoreStr);
        return result;
    }

    @Test
    void getScore_parsesValidDouble() throws Exception {
        assertEquals(0.87, resultWithScore("0.87").getScore(), 0.0001);
    }

    @Test
    void getScore_parsesIntegerScore() throws Exception {
        assertEquals(1.0, resultWithScore("1").getScore(), 0.0001);
    }

    @Test
    void getScore_withInvalidString_throwsNumberFormatException() throws Exception {
        assertThrows(NumberFormatException.class, () -> resultWithScore("not-a-number").getScore());
    }
}
