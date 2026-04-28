package jamuz.database;

import jamuz.Jamuz;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoTag}. */
public class DaoTagTest {

    private static DbConnJaMuz dbConnJaMuz;

    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        Jamuz.setDb(dbConnJaMuz);
    }

    @AfterClass
    public static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    public void shouldExposeWriteLock() {
        assertNotNull(Jamuz.getDb().tag().lock());
    }

    @Test
    public void shouldInsertAndReadTags() {
        assertTrue(Jamuz.getDb().tag().lock().insert("TagReadA"));
        ArrayList<String> tags = Jamuz.getDb().tag().get();
        assertTrue(tags.contains("TagReadA"));
    }
}
