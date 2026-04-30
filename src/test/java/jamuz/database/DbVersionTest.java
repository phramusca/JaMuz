package jamuz.database;

import java.util.Date;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link DbVersion}. */
class DbVersionTest {

    @Test
    void shouldExposeVersionAndDates() {
        Date start = new Date(1000L);
        Date end = new Date(2000L);
        DbVersion v = new DbVersion(7, start, end);
        assertEquals(7, v.getVersion());
        assertEquals(start, v.getUpgradeStart());
        assertEquals(end, v.getUpgradeEnd());
    }
}
