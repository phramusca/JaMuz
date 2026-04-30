package jamuz.remote;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ICallBackServerTest {

    @Test
    void shouldDefineReceivedAndGetIdFileMethods() {
        assertTrue(ICallBackServer.class.isInterface());
        Set<String> names = Arrays.stream(ICallBackServer.class.getDeclaredMethods())
                .map(m -> m.getName())
                .collect(Collectors.toSet());
        assertEquals(Set.of("received", "getIdFile"), names);
    }
}
