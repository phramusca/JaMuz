package jamuz.database;

import java.io.IOException;
import java.sql.SQLException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoGenre}. */
public class DaoGenreTest {

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
        assertNotNull(dbConnJaMuz.genre().lock());
    }

    @Test
    public void shouldSupportCrudOnGenreValues() {
        assertTrue(dbConnJaMuz.genre().isSupported("Reggae"));
        assertTrue(dbConnJaMuz.genre().lock().update("Reggae", "ReggaeTest"));
        assertTrue(dbConnJaMuz.genre().isSupported("ReggaeTest"));
        assertTrue(dbConnJaMuz.genre().lock().delete("ReggaeTest"));
        assertFalse(dbConnJaMuz.genre().isSupported("ReggaeTest"));
        assertTrue(dbConnJaMuz.genre().lock().insert("Reggae"));
    }
}
