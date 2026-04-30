package jamuz;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ICallBackVersionCheckTest {

    @Test
    void shouldExposeExpectedCallbackMethods() {
        assertTrue(ICallBackVersionCheck.class.isInterface());
        Set<String> names = Arrays.stream(ICallBackVersionCheck.class.getDeclaredMethods())
                .map(m -> m.getName())
                .collect(Collectors.toSet());
        assertTrue(names.containsAll(Set.of(
                "onNewVersion", "onCheck", "onCheckResult", "onUnzipCount",
                "onUnzipStart", "onUnzipProgress", "onDownloadRequest",
                "onDownloadStart", "onDownloadProgress")));
    }
}
