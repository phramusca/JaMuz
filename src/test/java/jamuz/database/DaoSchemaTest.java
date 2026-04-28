package jamuz.database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoSchema}. */
public class DaoSchemaTest {

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
        assertNotNull(dbConnJaMuz.schema().lock());
    }

    @Test
    public void shouldReadVersionHistory() {
        ArrayList<DbVersion> versions = new ArrayList<>();
        assertTrue(dbConnJaMuz.schema().getVersionHistory(versions));
        assertFalse(versions.isEmpty());
        assertTrue(versions.get(0).getVersion() > 0);
    }
}
