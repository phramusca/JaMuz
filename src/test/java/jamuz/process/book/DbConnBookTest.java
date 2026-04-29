package jamuz.process.book;

import jamuz.database.DbInfo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DbConnBookTest {
    @Test
    void shouldInstantiateWithDbInfo() {
        DbConnBook db = new DbConnBook(new DbInfo(DbInfo.LibType.Sqlite, "/tmp/a.db", "", ""));
        assertNotNull(db);
    }
}
