package jamuz.database;

import jamuz.FileInfoInt;
import jamuz.process.check.FolderInfo;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoFileTag}. */
public class DaoFileTagTest {

    private static DbConnJaMuz dbConnJaMuz;

    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
    }

    @AfterClass
    public static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    public void shouldExposeWriteLock() {
        assertNotNull(dbConnJaMuz.fileTag().lock());
    }

    @Test
    public void shouldReadTagsAfterFileTagUpdate() {
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
