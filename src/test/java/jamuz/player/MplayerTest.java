package jamuz.player;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MplayerTest {
    @Test
    void shouldBeLoadable() {
        assertNotNull(Mplayer.class);
    }
}
