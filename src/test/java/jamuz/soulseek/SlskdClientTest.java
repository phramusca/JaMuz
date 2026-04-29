package jamuz.soulseek;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SlskdClientTest {
    @Test
    void shouldExposeServerExceptionType() {
        SlskdClient.ServerException ex = new SlskdClient.ServerException("err");
        assertEquals("err", ex.getMessage());
    }
}
