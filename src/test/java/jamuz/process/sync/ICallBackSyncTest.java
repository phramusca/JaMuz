package jamuz.process.sync;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ICallBackSyncTest {

    @Test
    void shouldDefineSyncCallbacksIncludingDefaultMessages() {
        assertTrue(ICallBackSync.class.isInterface());
        Set<String> names = Arrays.stream(ICallBackSync.class.getDeclaredMethods())
                .map(m -> m.getName())
                .collect(Collectors.toSet());
        assertTrue(names.containsAll(Set.of("refresh", "enable", "enableButton", "addRow", "showInfo", "showWarning", "showError")));
    }
}
