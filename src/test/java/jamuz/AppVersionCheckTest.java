package jamuz;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link AppVersionCheck}.
 *
 * NOTE: the async path in {@link AppVersionCheck#start} is not covered here
 * because (1) the OkHttp base URL is hard-coded to GitHub so a MockWebServer
 * cannot intercept it, and (2) the scheduler is initialised on a background
 * thread, creating a race condition on {@link AppVersionCheck#shutdownScheduler}.
 * Full async coverage would require injecting the base URL (or the OkHttpClient)
 * as a dependency.
 */
class AppVersionCheckTest {

    @Test
    void constructor_callsOnCheckOnce() {
        ICallBackVersionCheck mockCallBack = mock(ICallBackVersionCheck.class);
        new AppVersionCheck(mockCallBack);
        verify(mockCallBack, times(1)).onCheck(any(AppVersion.class), any(String.class));
    }

    @Test
    void getAppVersion_returnsNonNull() {
        ICallBackVersionCheck mockCallBack = mock(ICallBackVersionCheck.class);
        AppVersionCheck checker = new AppVersionCheck(mockCallBack);
        assertNotNull(checker.getAppVersion());
    }

    @Disabled("Async test: start() scheduler is background-threaded; MockWebServer URL is not injected. See class Javadoc.")
    @Test
    void testCheckNewVersion_withLatestRelease() throws IOException {
        // TODO: refactor AppVersionCheck to accept injectable OkHttpClient or base URL
    }

    @Disabled("Async test: start() scheduler is background-threaded; MockWebServer URL is not injected. See class Javadoc.")
    @Test
    void testCheckNewVersion_withPreRelease() throws IOException {
        // TODO: refactor AppVersionCheck to accept injectable OkHttpClient or base URL
    }

    @Disabled("Async test: start() scheduler is background-threaded; MockWebServer URL is not injected. See class Javadoc.")
    @Test
    void testDownloadAndProcessAsset() throws IOException {
        // TODO: refactor AppVersionCheck to accept injectable OkHttpClient or base URL
    }
}
