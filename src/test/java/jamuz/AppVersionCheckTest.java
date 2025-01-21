package jamuz;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.MockResponse;

public class AppVersionCheckTest {

    private MockWebServer mockWebServer;
    private ICallBackVersionCheck mockCallBack;
    private AppVersionCheck appVersionCheck;

    @Before
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        mockCallBack = mock(ICallBackVersionCheck.class);
        appVersionCheck = new AppVersionCheck(mockCallBack);
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void testCheckNewVersion_withLatestRelease() throws IOException {
        JsonObject releaseData = new JsonObject();
        releaseData.addProperty("tag_name", "v2.0.0");
        releaseData.add("assets", new JsonArray());
        mockWebServer.enqueue(new MockResponse().setBody(releaseData.toString()).setResponseCode(200));

        appVersionCheck.start(false);
        appVersionCheck.shutdownScheduler();

        verify(mockCallBack, times(1)).onCheck(any(AppVersion.class), eq("Checking for new version ..."));
        verify(mockCallBack, times(1)).onCheckResult(any(AppVersion.class), eq("You are running the latest version."));
    }

    @Test
    public void testCheckNewVersion_withPreRelease() throws IOException {
        JsonObject releaseData = new JsonObject();
        releaseData.addProperty("tag_name", "v2.0.0-beta");
        releaseData.add("assets", new JsonArray());
        JsonArray releasesArray = new JsonArray();
        releasesArray.add(releaseData);
        mockWebServer.enqueue(new MockResponse().setBody(releasesArray.toString()).setResponseCode(200));

        appVersionCheck.start(true);
        appVersionCheck.shutdownScheduler();

        verify(mockCallBack, times(1)).onCheck(any(AppVersion.class), eq("Checking for new version ..."));
        verify(mockCallBack, times(1)).onCheckResult(any(AppVersion.class), eq("You are running the latest version."));
    }

    @Test
    public void testDownloadAndProcessAsset() throws IOException {
        JsonObject asset = new JsonObject();
        asset.addProperty("browser_download_url", mockWebServer.url("/download").toString());
        asset.addProperty("name", "test-asset.zip");
        asset.addProperty("size", 1024);
        JsonArray assets = new JsonArray();
        assets.add(asset);
        JsonObject releaseData = new JsonObject();
        releaseData.addProperty("tag_name", "v2.0.0");
        releaseData.add("assets", assets);
        mockWebServer.enqueue(new MockResponse().setBody(releaseData.toString()).setResponseCode(200));
        okio.Buffer buffer = new okio.Buffer();
        buffer.write(new byte[1024]);
        mockWebServer.enqueue(new MockResponse().setBody(buffer).setResponseCode(200));

        appVersionCheck.start(false);
        appVersionCheck.shutdownScheduler();

        verify(mockCallBack, times(1)).onCheck(any(AppVersion.class), eq("Getting new version ..."));
        verify(mockCallBack, times(1)).onDownloadStart();
        verify(mockCallBack, atLeastOnce()).onDownloadProgress(any(AppVersion.class), anyInt());
        verify(mockCallBack, times(1)).onNewVersion(any(AppVersion.class));
    }
}
