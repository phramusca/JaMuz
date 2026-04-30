package jamuz.database;

import jamuz.FileInfoInt;
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

/** Tests for {@link DaoFileTag}. */
class DaoFileTagTest {

    private static DbConnJaMuz dbConnJaMuz;

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    void shouldExposeWriteLock() {
        assertNotNull(dbConnJaMuz.fileTag().lock());
    }

    @Test
    void shouldReadTagsAfterFileTagUpdate() {
        assertTrue(dbConnJaMuz.tag().lock().insertIfMissing("filetag-a"));
        assertTrue(dbConnJaMuz.tag().lock().insertIfMissing("filetag-b"));

        int[] keyPath = new int[1];
        assertTrue(dbConnJaMuz.path().lock().insert("filetag/path", new Date(), FolderInfo.CheckedFlag.UNCHECKED, "", keyPath));
        dbConnJaMuz.file().setLocationLibrary("/root/tag/");
        FileInfoInt f = new FileInfoInt("filetag/path/t.mp3", "/root/tag/");
        f.setIdPath(keyPath[0]);
        f.setTags(new ArrayList<>());
        f.getTags().add("filetag-a");
        f.getTags().add("filetag-b");
        int[] keyFile = new int[1];
        assertTrue(dbConnJaMuz.file().lock().insert(f, keyFile));
        f.setIdFile(keyFile[0]);

        ArrayList<FileInfoInt> files = new ArrayList<>();
        files.add(f);
        dbConnJaMuz.fileTag().lock().update(files, null);

        ArrayList<String> tags = new ArrayList<>();
        assertTrue(dbConnJaMuz.fileTag().get(tags, f.getIdFile()));
        assertEquals(2, tags.size());
    }
}
