package jamuz.database;

import jamuz.process.check.DuplicateInfo;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoPathAlbum}. */
public class DaoPathAlbumTest {

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
        assertNotNull(dbConnJaMuz.album().lock());
    }

    @Test
    public void shouldReturnFalseOnBlankInputs() {
        ArrayList<DuplicateInfo> dups = new ArrayList<>();
        assertFalse(dbConnJaMuz.album().checkSimilar(dups, "", 1));
        assertFalse(dbConnJaMuz.album().checkExact(dups, "", 1));
        assertFalse(dbConnJaMuz.album().checkDuplicate(dups, ""));
        assertFalse(dbConnJaMuz.album().checkDuplicate(dups, "", "", 1));
        assertFalse(dbConnJaMuz.album().checkDuplicate(dups, "", "", 1, 1, 1));
    }
}
