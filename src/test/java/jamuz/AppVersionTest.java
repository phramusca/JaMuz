package jamuz;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AppVersionTest {

    private AppVersion appVersion;

    @BeforeEach
    void setUp() {
        appVersion = new AppVersion("v1.0.0", "v1.0.1");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void isNewVersion_returnsTrueWhenLatestIsNewer() {
        assertTrue(appVersion.isNewVersion());
        appVersion.setLatestVersion("v0.9.9");
        assertFalse(appVersion.isNewVersion());
    }

    @Test
    void setAsset_storesFileAndSize() {
        File assetFile = new File("testFile.txt");
        appVersion.setAsset(assetFile, 100);
        assertEquals(assetFile, appVersion.getAssetFile());
        assertEquals(100, appVersion.getAssetSize());
    }

    @Test
    void setLatestVersion_updatesVersion() {
        appVersion.setLatestVersion("v1.0.2");
        assertEquals("v1.0.2", appVersion.getLatestVersion());
    }

    @Test
    void compareVersionStrings_returnsCorrectOrdering() {
        assertEquals(0, AppVersion.compareVersionStrings("v1.0.0", "v1.0.0"));
        assertTrue(AppVersion.compareVersionStrings("v1.0.1", "v1.0.0") > 0);
        assertTrue(AppVersion.compareVersionStrings("v1.0.0", "v1.0.1") < 0);
    }

    @Test
    void toString_containsCurrentAndLatest() {
        assertEquals("Current: v1.0.0. Latest: v1.0.1. ", appVersion.toString());
    }

    @Test
    void isAssetValid_withExistingFile_returnsTrue() throws IOException {
        File tempFile = File.createTempFile("tempFile", null);
        appVersion.setAsset(tempFile, (int) tempFile.length());
        assertTrue(appVersion.isAssetValid());
    }

    @Test
    void isUnzippedAssetValid_afterUnzip_returnsTrue() throws IOException {
        File testAsset = createTestAsset();
        appVersion.setAsset(testAsset, (int) testAsset.length());
        appVersion.unzipAsset(Mockito.mock(ICallBackVersionCheck.class));
        assertTrue(appVersion.isUnzippedAssetValid());
    }

    @Test
    void unzipAsset_callsCallbackLifecycleMethods() throws IOException {
        File testAsset = createTestAsset();
        appVersion.setAsset(testAsset, (int) testAsset.length());

        ICallBackVersionCheck mockCallback = Mockito.mock(ICallBackVersionCheck.class);
        appVersion.unzipAsset(mockCallback);

        verify(mockCallback, times(1)).onUnzipCount(any());
        verify(mockCallback, times(1)).onUnzipStart();
        verify(mockCallback, atLeastOnce()).onUnzipProgress(any(), any(), anyInt());
    }

    @Test
    void getCurrentVersion_returnsConstructedVersion() {
        assertEquals("v1.0.0", appVersion.getCurrentVersion());
    }

    @Test
    void getLatestVersion_returnsConstructedLatest() {
        assertEquals("v1.0.1", appVersion.getLatestVersion());
    }

    @Test
    void getAssetFile_isNullByDefaultThenSetBySetAsset() {
        assertNull(appVersion.getAssetFile());
        File assetFile = new File("testFile.txt");
        appVersion.setAsset(assetFile, 100);
        assertEquals(assetFile, appVersion.getAssetFile());
    }

    @Test
    void getAssetSize_isZeroByDefaultThenSetBySetAsset() {
        assertEquals(0, appVersion.getAssetSize());
        File assetFile = new File("testFile.txt");
        appVersion.setAsset(assetFile, 100);
        assertEquals(100, appVersion.getAssetSize());
    }

    private File createTestAsset() throws IOException {
        File tempFile = File.createTempFile("tempFile", ".7z");
        try (SevenZOutputFile sevenZOutputFile = new SevenZOutputFile(tempFile)) {
            File tempEntry = File.createTempFile("tempEntry1", ".txt");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempEntry))) {
                writer.write("This is a sample text.");
                writer.flush();
            }
            sevenZOutputFile.putArchiveEntry(sevenZOutputFile.createArchiveEntry(tempEntry, tempEntry.getName()));
            sevenZOutputFile.write(Files.readAllBytes(tempEntry.toPath()));
            sevenZOutputFile.closeArchiveEntry();
        }
        return tempFile;
    }
}
