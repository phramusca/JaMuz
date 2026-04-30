package jamuz.utils;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestSettings;

class FileSystemTest {

    File source;
    File destination;

    @BeforeEach
    void setUp() {
        source = new File(FilenameUtils.concat(TestSettings.getResourcesPath(),
                FilenameUtils.concat("audioFiles", "1min.mp3")));
        destination = new File(FilenameUtils.concat(TestSettings.getAppFolder(),
                FilenameUtils.concat("temp-should_Be_DeletTED_Damned", "1min (copie).mp3")));
    }

    @AfterEach
    void tearDown() {
        destination.delete();
        destination.getParentFile().delete();
    }

    @Test
    void moveFile_movesSourceToDestinationAndRemovesSource() throws IOException {
        File moveSource = new File(FilenameUtils.concat(TestSettings.getAppFolder(), "tempShouldBeDeleted.mp3"));
        FileSystem.copyFile(source, moveSource);
        assertTrue(moveSource.exists());
        assertFalse(destination.exists());

        FileSystem.moveFile(moveSource, destination);

        assertTrue(source.exists());
        assertTrue(destination.exists());
        assertEquals(961029, destination.length());
        assertFalse(moveSource.exists());
    }

    @Test
    void copyFile_copiesSourceToDestinationWithoutRemovingSource() throws Exception {
        assertTrue(source.exists());
        assertFalse(destination.exists());

        FileSystem.copyFile(source, destination);

        assertTrue(source.exists());
        assertTrue(destination.exists());
        assertEquals(961029, destination.length());
    }

    @Test
    void replaceHome_withStringPath_expandsTildeToHomeDir() {
        File result = FileSystem.replaceHome(new File("~/toto/~tem/oh~/top.mp9"));
        assertTrue(result.getAbsolutePath().startsWith(System.getProperty("user.home")),
                "Should start with home dir");
        assertTrue(result.getAbsolutePath().endsWith(
                File.separator + "toto" + File.separator + "~tem" + File.separator + "oh~" + File.separator + "top.mp9"),
                "Should preserve the rest of the path");
    }

    @Test
    void size_ofAudioFilesDir_returnsExpectedTotalBytes() {
        File dir = new File(FilenameUtils.concat(TestSettings.getResourcesPath(), "audioFiles"));
        assertEquals(7011134, FileSystem.size(dir.toPath()));
    }
}
