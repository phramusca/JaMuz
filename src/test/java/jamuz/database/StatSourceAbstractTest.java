package jamuz.database;

import jamuz.FileInfo;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;

/** Tests generic getters/setters from {@link StatSourceAbstract}. */
public class StatSourceAbstractTest {

    private static final class DummyStatSource extends StatSourceSQL {
        DummyStatSource(DbInfo info) {
            super(info, "DummyName", "/root/", true, false, true, true, false, true);
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
    public void shouldExposeMutableAndFlagProperties() throws IOException {
        DummyStatSource src = new DummyStatSource(TestUnitSettings.getTempDbInfo());
        assertEquals("DummyName", src.getName());
        assertEquals("/root/", src.getRootPath());

        src.setName("NewName");
        src.setLocation("/tmp/source.db");
        src.setRootPath("/new/root/");

        assertEquals("NewName", src.getName());
        assertEquals("/tmp/source.db", src.getLocation());
        assertEquals("/new/root/", src.getRootPath());
        assertTrue(src.isUpdateAddedDate());
        assertFalse(src.isUpdateLastPlayed());
        assertTrue(src.isUpdateBPM());
        assertTrue(src.isUpdatePlayCounter());
        assertFalse(src.isUpdateTags());
        assertTrue(src.isUpdateGenre());
    }
}
