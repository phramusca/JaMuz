package jamuz.database;

import jamuz.Option;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoOption}. */
public class DaoOptionTest {

    private static DbConnJaMuz dbConnJaMuz;

    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        dbConnJaMuz.machine().lock().getOrInsert("OptReadHost", new StringBuilder(), false);
    }

    @AfterClass
    public static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    public void shouldExposeWriteLock() {
        assertNotNull(dbConnJaMuz.option().lock());
    }

    @Test
    public void shouldReadDefaultOptionsForKnownMachine() {
        ArrayList<Option> options = new ArrayList<>();
        assertTrue(dbConnJaMuz.option().get(options, "OptReadHost"));
        assertFalse(options.isEmpty());
    }
}
