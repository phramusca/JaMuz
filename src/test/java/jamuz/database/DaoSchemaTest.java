package jamuz.database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoSchema}. */
class DaoSchemaTest {

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
        assertNotNull(dbConnJaMuz.schema().lock());
    }

    @Test
    void shouldReadVersionHistory() {
        ArrayList<DbVersion> versions = new ArrayList<>();
        assertTrue(dbConnJaMuz.schema().getVersionHistory(versions));
        assertFalse(versions.isEmpty());
        assertTrue(versions.get(0).getVersion() > 0);
    }
}
