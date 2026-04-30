package jamuz.database;

import jamuz.Option;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoOption}. */
class DaoOptionTest {

    private static DbConnJaMuz dbConnJaMuz;

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        dbConnJaMuz.machine().lock().getOrInsert("OptReadHost", new StringBuilder(), false);
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    void shouldExposeWriteLock() {
        assertNotNull(dbConnJaMuz.option().lock());
    }

    @Test
    void shouldReadDefaultOptionsForKnownMachine() {
        ArrayList<Option> options = new ArrayList<>();
        assertTrue(dbConnJaMuz.option().get(options, "OptReadHost"));
        assertFalse(options.isEmpty());
    }
}
