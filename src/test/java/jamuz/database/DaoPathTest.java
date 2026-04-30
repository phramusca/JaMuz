package jamuz.database;

import jamuz.process.check.FolderInfo;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoPath}. */
class DaoPathTest {

    private static DbConnJaMuz dbConnJaMuz;
    private static int idPath;

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        int[] key = new int[1];
        dbConnJaMuz.path().lock().insert("daoPath/test", new Date(), FolderInfo.CheckedFlag.OK, "mbid", key);
        idPath = key[0];
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    void shouldExposeWriteLock() {
        assertNotNull(dbConnJaMuz.path().lock());
    }

    @Test
    void shouldReadPathsByDifferentSelectors() {
        ConcurrentHashMap<String, FolderInfo> byChecked = new ConcurrentHashMap<>();
        assertTrue(dbConnJaMuz.path().get(byChecked, FolderInfo.CheckedFlag.OK));
        assertFalse(byChecked.isEmpty());

        ConcurrentHashMap<String, FolderInfo> byId = new ConcurrentHashMap<>();
        assertTrue(dbConnJaMuz.path().get(byId, idPath));
        assertEquals(1, byId.size());

        FolderInfo folder = dbConnJaMuz.path().get(idPath);
        assertNotNull(folder);

        assertEquals(idPath, dbConnJaMuz.path().getIdPath("daoPath/test"));
    }
}
