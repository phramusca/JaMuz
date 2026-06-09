package jamuz.process.merge;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ICallBackMergeTest {

    @Test
    void shouldDefineCoreAndDefaultCallbacks() {
        assertTrue(ICallBackMerge.class.isInterface());
        Set<String> names = Arrays.stream(ICallBackMerge.class.getDeclaredMethods())
                .map(m -> m.getName())
                .collect(Collectors.toSet());
        assertTrue(names.containsAll(Set.of("completed", "refresh", "showInfo", "showWarning", "showError")));
    }
}
