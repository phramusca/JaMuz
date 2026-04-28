package jamuz.player;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MPlaybackListenerTest {

    @Test
    void shouldDefinePlaybackCallbacks() {
        assertTrue(MPlaybackListener.class.isInterface());
        Set<String> names = Arrays.stream(MPlaybackListener.class.getDeclaredMethods())
                .map(m -> m.getName())
                .collect(Collectors.toSet());
        assertEquals(Set.of("volumeChanged", "playbackFinished", "positionChanged"), names);
    }
}
