package jamuz.player;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JMPlayerTest {
    @Test
    void shouldBeLoadable() {
        assertNotNull(JMPlayer.class);
    }
}
