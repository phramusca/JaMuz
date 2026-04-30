package jamuz;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ICallBackVersionUpdateTest {

    @Test
    void shouldDefineUpdateCallbacks() {
        assertTrue(ICallBackVersionUpdate.class.isInterface());
        Set<String> names = Arrays.stream(ICallBackVersionUpdate.class.getDeclaredMethods())
                .map(m -> m.getName())
                .collect(Collectors.toSet());
        assertEquals(Set.of("onSetup", "onFileUpdated"), names);
    }
}
