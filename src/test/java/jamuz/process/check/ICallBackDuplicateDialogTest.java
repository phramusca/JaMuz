package jamuz.process.check;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ICallBackDuplicateDialogTest {

    @Test
    void shouldDefineDuplicateDecisionCallbacks() {
        assertTrue(ICallBackDuplicateDialog.class.isInterface());
        Set<String> names = Arrays.stream(ICallBackDuplicateDialog.class.getDeclaredMethods())
                .map(m -> m.getName())
                .collect(Collectors.toSet());
        assertEquals(Set.of("notAduplicate", "delete"), names);
    }
}
