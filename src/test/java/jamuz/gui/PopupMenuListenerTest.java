package jamuz.gui;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PopupMenuListenerTest {

    @Test
    void shouldDefinePopupLifecycleCallbacks() {
        assertTrue(PopupMenuListener.class.isInterface());
        Set<String> names = Arrays.stream(PopupMenuListener.class.getDeclaredMethods())
                .map(m -> m.getName())
                .collect(Collectors.toSet());
        assertEquals(Set.of("deleteStarted", "deleteEnded", "refresh"), names);
    }
}
