package jamuz.database;

import jamuz.Jamuz;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoTag}. */
class DaoTagTest {

    private static DbConnJaMuz dbConnJaMuz;

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        Jamuz.setDb(dbConnJaMuz);
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    void shouldExposeWriteLock() {
        assertNotNull(Jamuz.getDb().tag().lock());
    }

    @Test
    void shouldInsertAndReadTags() {
        assertTrue(Jamuz.getDb().tag().lock().insert("TagReadA"));
        ArrayList<String> tags = Jamuz.getDb().tag().get();
        assertTrue(tags.contains("TagReadA"));
    }
}
