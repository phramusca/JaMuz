package jamuz.database;

import jamuz.process.merge.StatSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoStatSource}. */
class DaoStatSourceTest {

    private static DbConnJaMuz dbConnJaMuz;
    private static final String HOST = "StatSourceReadHost";

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        dbConnJaMuz.machine().lock().getOrInsert(HOST, new StringBuilder(), false);
        StatSource ss = new StatSource(HOST);
        ss.getSource().setName("StatSourceReadName");
        ss.getSource().setLocation("/tmp/statsource-read.db");
        ss.getSource().setRootPath("/root");
        assertTrue(dbConnJaMuz.statSource().lock().insertOrUpdate(ss));
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    void shouldExposeWriteLock() {
        assertNotNull(dbConnJaMuz.statSource().lock());
    }

    @Test
    void shouldReadStatSourcesByHost() {
        LinkedHashMap<Integer, StatSource> map = new LinkedHashMap<>();
        assertTrue(dbConnJaMuz.statSource().get(map, HOST, false));
        assertEquals(1, map.size());
        assertEquals("StatSourceReadName", map.values().iterator().next().getSource().getName());
    }
}
