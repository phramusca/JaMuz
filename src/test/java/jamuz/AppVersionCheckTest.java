package jamuz;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link AppVersionCheck}.
 *
 * Async coverage is achieved by calling the package-private
 * {@link AppVersionCheck#checkNewVersion()} directly (bypassing the scheduler)
 * with an injected {@link OkHttpClient} pointed at a {@link MockWebServer}.
 */
class AppVersionCheckTest {

    private static final String CURRENT_VERSION = "v1.0.0";

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

    @Test
    void checkNewVersion_withLatestRelease_upToDate_reportsNoUpdate() throws IOException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse()
                .setBody("{\"tag_name\":\"" + CURRENT_VERSION + "\",\"assets\":[]}")
                .addHeader("Content-Type", "application/json"));
        server.start();

        AtomicReference<String> result = new AtomicReference<>();
        ICallBackVersionCheck cb = mock(ICallBackVersionCheck.class);
        doAnswer(inv -> { result.set(inv.getArgument(1)); return null; })
                .when(cb).onCheckResult(any(), anyString());

        AppVersionCheck checker = new AppVersionCheck(cb,
                new OkHttpClient(),
                server.url("/releases").toString().replaceAll("/$", ""),
                CURRENT_VERSION);

        checker.checkNewVersion();

        assertEquals("You are running the latest version.", result.get());
        server.shutdown();
    }

    @Test
    void checkNewVersion_withPreRelease_upToDate_reportsNoUpdate() throws IOException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse()
                .setBody("[{\"tag_name\":\"" + CURRENT_VERSION + "\",\"assets\":[]}]")
                .addHeader("Content-Type", "application/json"));
        server.start();

        AtomicReference<String> result = new AtomicReference<>();
        ICallBackVersionCheck cb = mock(ICallBackVersionCheck.class);
        doAnswer(inv -> { result.set(inv.getArgument(1)); return null; })
                .when(cb).onCheckResult(any(), anyString());

        AppVersionCheck checker = new AppVersionCheck(cb,
                new OkHttpClient(),
                server.url("/releases").toString().replaceAll("/$", ""),
                CURRENT_VERSION);
        checker.includePreRelease = true;

        checker.checkNewVersion();

        assertEquals("You are running the latest version.", result.get());
        server.shutdown();
    }

    @Test
    void checkNewVersion_withNewVersionButNoAssets_reportsNoAssetFound() throws IOException {
        MockWebServer server = new MockWebServer();
        server.enqueue(new MockResponse()
                .setBody("{\"tag_name\":\"v99.0.0\",\"assets\":[]}")
                .addHeader("Content-Type", "application/json"));
        server.start();

        AtomicReference<String> result = new AtomicReference<>();
        ICallBackVersionCheck cb = mock(ICallBackVersionCheck.class);
        doAnswer(inv -> { result.set(inv.getArgument(1)); return null; })
                .when(cb).onCheckResult(any(), anyString());

        AppVersionCheck checker = new AppVersionCheck(cb,
                new OkHttpClient(),
                server.url("/releases").toString().replaceAll("/$", ""),
                CURRENT_VERSION);

        checker.checkNewVersion();

        assertEquals("No asset found in release!", result.get());
        server.shutdown();
    }
}
