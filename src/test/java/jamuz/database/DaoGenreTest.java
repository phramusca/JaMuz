package jamuz.database;

import java.io.IOException;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoGenre}. */
class DaoGenreTest {

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
        assertNotNull(dbConnJaMuz.genre().lock());
    }

    @Test
    void shouldSupportCrudOnGenreValues() {
        assertTrue(dbConnJaMuz.genre().isSupported("Reggae"));
        assertTrue(dbConnJaMuz.genre().lock().update("Reggae", "ReggaeTest"));
        assertTrue(dbConnJaMuz.genre().isSupported("ReggaeTest"));
        assertTrue(dbConnJaMuz.genre().lock().delete("ReggaeTest"));
        assertFalse(dbConnJaMuz.genre().isSupported("ReggaeTest"));
        assertTrue(dbConnJaMuz.genre().lock().insert("Reggae"));
    }
}
