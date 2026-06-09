package jamuz.process.video;

import jamuz.database.DbInfo;
import jamuz.database.DbInfo.LibType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DbConnVideoTest {
    @Test
    void shouldInstantiateWithDbInfo() {
        DbInfo dbInfo = new DbInfo(LibType.Sqlite, ":memory:", "", "");
        assertNotNull(new DbConnVideo(dbInfo, "/"));
    }
}
