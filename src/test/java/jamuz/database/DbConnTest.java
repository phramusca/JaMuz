package jamuz.database;

import java.io.IOException;
import java.sql.Connection;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestResultSet;
import test.helpers.TestUnitSettings;

/** Tests for {@link DbConn}. */
class DbConnTest {

    @Test
    void shouldConnectAndDisconnectSqliteDb() throws IOException {
        DbInfo info = TestUnitSettings.getTempDbInfo();
        DbConn conn = new DbConn(info);
        assertTrue(conn.connect());
        Connection jdbc = conn.getConnection();
        assertNotNull(jdbc);
        conn.disconnect();
    }

    @Test
    void shouldExposeInfoObject() throws IOException {
        DbInfo info = TestUnitSettings.getTempDbInfo();
        DbConn conn = new DbConn(info);
        assertEquals(info, conn.getInfo());
    }

    @Test
    void shouldReadStringValuesWithAndWithoutDefaults() {
        DbConn conn = new DbConn(new DbInfo(DbInfo.LibType.Sqlite, "location", "user", "pwd"));
        assertEquals("MyDefault", conn.getStringValue(TestResultSet.getResultSet("Value"), "GimmeEmpty", "MyDefault"));
        assertEquals("", conn.getStringValue(TestResultSet.getResultSet("Value"), "GimmeNull"));
        assertEquals("Value", conn.getStringValue(TestResultSet.getResultSet("Value"), "GimmeValue"));
    }
}
