package jamuz.database;

import jamuz.FileInfo;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;

/** Tests for base behaviours in {@link StatSourceSQL}. */
public class StatSourceSQLTest {

    private static final class DummyStatSource extends StatSourceSQL {
        DummyStatSource(DbInfo info) {
            super(info, "Dummy", "/root/", false, false, false, false, false, false);
        }

        @Override
        protected void setUpdateStatisticsParameters(FileInfo file) throws SQLException {
        }

        @Override
        public boolean setUp(boolean isRemote) {
            return true;
        }

        @Override
        public boolean getTags(ArrayList<String> tags, FileInfo file) {
            return true;
        }
    }

    @Test
    public void shouldExposeDbConnAndBaseHelpers() throws IOException {
        DummyStatSource src = new DummyStatSource(TestUnitSettings.getTempDbInfo());
        assertNotNull(src.getDbConn());
        assertEquals("a/b", src.getPath("a\\b"));
        assertTrue(src.tearDown());
    }
}
