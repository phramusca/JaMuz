package jamuz.utils;

import java.util.Date;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DateTimeTest {

    @Test
    void shouldFormatUtcWithPattern() {
        assertEquals("1970-01-01 01:00:00", DateTime.formatUTC(new Date(0), "yyyy-MM-dd HH:mm:ss", true));
        assertEquals("1970-01-01 00:00:00", DateTime.formatUTC(new Date(0), "yyyy-MM-dd HH:mm:ss", false));
    }

    @Test
    void shouldFormatUtcWithEnum() {
        assertEquals("1970-01-01 01:00:00", DateTime.formatUTC(new Date(0), DateTime.DateTimeFormat.SQL, true));
        assertEquals("1970-01-01 00:00:00", DateTime.formatUTC(new Date(0), DateTime.DateTimeFormat.SQL, false));
    }

    @Test
    void shouldFormatUtcToSqlLocalAndUtc() {
        assertEquals("1970-01-01 01:00:00", DateTime.formatUTCtoSqlLocal(new Date(0)));
        assertEquals("1970-01-01 00:00:00", DateTime.formatUTCtoSqlUTC(new Date(0)));
    }

    @Test
    void shouldExposeCurrentDateHelpers() {
        assertEquals(DateTime.formatUTCtoSqlUTC(new Date()), DateTime.getCurrentUtcSql());
        assertEquals(DateTime.formatUTCtoSqlLocal(new Date()), DateTime.getCurrentLocal(DateTime.DateTimeFormat.SQL));
    }

    @Test
    void shouldParseSqlUtc() {
        String date = "2019-05-23 11:04:38";
        Date result = DateTime.parseSqlUtc(date);
        assertEquals(date, DateTime.formatUTCtoSqlUTC(result));
    }
}
