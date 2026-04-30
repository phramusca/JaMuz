package jamuz.database;

import jamuz.process.check.DuplicateInfo;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoPathAlbum}. */
class DaoPathAlbumTest {

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
        assertNotNull(dbConnJaMuz.album().lock());
    }

    @Test
    void shouldReturnFalseOnBlankInputs() {
        ArrayList<DuplicateInfo> dups = new ArrayList<>();
        assertFalse(dbConnJaMuz.album().checkSimilar(dups, "", 1));
        assertFalse(dbConnJaMuz.album().checkExact(dups, "", 1));
        assertFalse(dbConnJaMuz.album().checkDuplicate(dups, ""));
        assertFalse(dbConnJaMuz.album().checkDuplicate(dups, "", "", 1));
        assertFalse(dbConnJaMuz.album().checkDuplicate(dups, "", "", 1, 1, 1));
    }
}
