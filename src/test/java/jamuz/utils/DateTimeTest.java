/*
 * Copyright (C) 2018 phramusca <phramusca@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */
package jamuz.utils;

import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests for {@link DateTime}. */
public class DateTimeTest {

    @Test
    public void shouldFormatUtcWithPattern() {
        assertEquals("1970-01-01 01:00:00", DateTime.formatUTC(new Date(0), "yyyy-MM-dd HH:mm:ss", true));
        assertEquals("1970-01-01 00:00:00", DateTime.formatUTC(new Date(0), "yyyy-MM-dd HH:mm:ss", false));
    }

    @Test
    public void shouldFormatUtcWithEnum() {
        assertEquals("1970-01-01 01:00:00", DateTime.formatUTC(new Date(0), DateTime.DateTimeFormat.SQL, true));
        assertEquals("1970-01-01 00:00:00", DateTime.formatUTC(new Date(0), DateTime.DateTimeFormat.SQL, false));
    }

    @Test
    public void shouldFormatUtcToSqlLocalAndUtc() {
        assertEquals("1970-01-01 01:00:00", DateTime.formatUTCtoSqlLocal(new Date(0)));
        assertEquals("1970-01-01 00:00:00", DateTime.formatUTCtoSqlUTC(new Date(0)));
    }

    @Test
    public void shouldExposeCurrentDateHelpers() {
        assertEquals(DateTime.formatUTCtoSqlUTC(new Date()), DateTime.getCurrentUtcSql());
        assertEquals(DateTime.formatUTCtoSqlLocal(new Date()), DateTime.getCurrentLocal(DateTime.DateTimeFormat.SQL));
    }

    @Test
    public void shouldParseSqlUtc() {
        String date = "2019-05-23 11:04:38";
        Date result = DateTime.parseSqlUtc(date);
        assertEquals(date, DateTime.formatUTCtoSqlUTC(result));
    }
}
