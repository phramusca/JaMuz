package jamuz.database;

import java.io.IOException;
import java.sql.Connection;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestResultSet;
import test.helpers.TestUnitSettings;

/** Tests for {@link DbConn}. */
public class DbConnTest {

    @Test
    public void shouldConnectAndDisconnectSqliteDb() throws IOException {
        DbInfo info = TestUnitSettings.getTempDbInfo();
        DbConn conn = new DbConn(info);
        assertTrue(conn.connect());
        Connection jdbc = conn.getConnection();
        assertNotNull(jdbc);
        conn.disconnect();
    }

    @Test
    public void shouldExposeInfoObject() throws IOException {
        DbInfo info = TestUnitSettings.getTempDbInfo();
        DbConn conn = new DbConn(info);
        assertEquals(info, conn.getInfo());
    }

    @Test
    public void shouldReadStringValuesWithAndWithoutDefaults() {
        DbConn conn = new DbConn(new DbInfo(DbInfo.LibType.Sqlite, "location", "user", "pwd"));
        assertEquals("MyDefault", conn.getStringValue(TestResultSet.getResultSet("Value"), "GimmeEmpty", "MyDefault"));
        assertEquals("", conn.getStringValue(TestResultSet.getResultSet("Value"), "GimmeNull"));
        assertEquals("Value", conn.getStringValue(TestResultSet.getResultSet("Value"), "GimmeValue"));
    }
}
