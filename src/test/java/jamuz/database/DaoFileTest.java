package jamuz.database;

import jamuz.FileInfoInt;
import jamuz.StatItem;
import jamuz.process.check.FolderInfo;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoFile}. */
class DaoFileTest {

    private static DbConnJaMuz dbConnJaMuz;
    private static int pathId;
    private static int fileId;

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        dbConnJaMuz.file().setLocationLibrary("/root/filetest/");
        int[] keyPath = new int[1];
        dbConnJaMuz.path().lock().insert("relative/filetest/", new Date(), FolderInfo.CheckedFlag.UNCHECKED, "", keyPath);
        pathId = keyPath[0];

        FileInfoInt f = new FileInfoInt("relative/filetest/a.mp3", "/root/filetest/");
        f.setIdPath(pathId);
        f.setGenre("Reggae");
        int[] keyFile = new int[1];
        dbConnJaMuz.file().lock().insert(f, keyFile);
        fileId = keyFile[0];
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    void shouldExposeWriteLock() {
        assertNotNull(dbConnJaMuz.file().lock());
    }

    @Test
    void shouldReadFileById() {
        FileInfoInt f = dbConnJaMuz.file().getFile(fileId, "");
        assertNotNull(f);
        assertEquals(fileId, f.getIdFile());
    }

    @Test
    void shouldReadFilesAndStats() {
        ArrayList<FileInfoInt> files = new ArrayList<>();
        assertTrue(dbConnJaMuz.file().getFiles(files, pathId));
        assertEquals(1, files.size());

        boolean[] ratings = new boolean[]{true, true, true, true, true, true};
        boolean[] checked = new boolean[]{true, true, true, true};
        String stats = dbConnJaMuz.file().getFilesStats("%", "%", "%", ratings, checked, 0, 9999, 0, 9999, -1);
        assertTrue(stats.contains("file(s)"));

        ArrayList<StatItem> statItems = new ArrayList<>();
        dbConnJaMuz.file().getSelectionList4Stats(statItems, "genre", ratings);
        assertFalse(statItems.isEmpty());
    }

    @Test
    void shouldReturnYearRange() {
        // The test file has no year set (defaults to 0); we only verify the query executes without error
        assertTrue(dbConnJaMuz.file().getYear("MIN") >= 0);
        assertTrue(dbConnJaMuz.file().getYear("MAX") >= 0);
    }
}
