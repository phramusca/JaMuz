/*
 * Copyright (C) 2015 phramusca ( https://github.com/phramusca/JaMuz/ )
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jamuz.unitary;

import java.util.Date;
import org.junit.Assert;
import org.junit.Test;
import jamuz.utils.DateTime;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DateTimeTest {

	/**
	 *
	 * @throws Exception
	 */
	@Test
    public void test() throws Exception {

        Assert.assertEquals(new Date(0), DateTime.parseSqlUtc("1970-01-01 00:00:00"));
        System.out.println(DateTime.parseSqlUtc("1970-01-01 00:00:00"));

        Assert.assertEquals("1970-01-01 01:00:00", DateTime.formatUTC(new Date(0), DateTime.DateTimeFormat.SQL, true));
        System.out.println(DateTime.formatUTC(new Date(0), DateTime.DateTimeFormat.SQL, true));
        Assert.assertEquals("1970-01-01 00:00:00", DateTime.formatUTC(new Date(0), DateTime.DateTimeFormat.SQL, false));
        System.out.println(DateTime.formatUTC(new Date(0), DateTime.DateTimeFormat.SQL, false));
        
        Assert.assertEquals("1970-01-01 01:00:00", DateTime.formatUTC(new Date(0), "yyyy-MM-dd HH:mm:ss", true));
        System.out.println(DateTime.formatUTC(new Date(0), "yyyy-MM-dd HH:mm:ss", true));
        Assert.assertEquals("1970-01-01 00:00:00", DateTime.formatUTC(new Date(0), "yyyy-MM-dd HH:mm:ss", false));
        System.out.println(DateTime.formatUTC(new Date(0), "yyyy-MM-dd HH:mm:ss", false));
        
        Assert.assertEquals("1970-01-01 01:00:00", DateTime.formatUTCtoSqlLocal(new Date(0)));
        System.out.println(DateTime.formatUTCtoSqlLocal(new Date(0)));
        Assert.assertEquals("1970-01-01 00:00:00", DateTime.formatUTCtoSqlUTC(new Date(0)));
        System.out.println(DateTime.formatUTCtoSqlUTC(new Date(0)));
        
        Assert.assertEquals(DateTime.formatUTCtoSqlUTC(new Date()), DateTime.getCurrentUtcSql());
        System.out.println("DateTime.getCurrentUtcSql(): "+DateTime.getCurrentUtcSql());
        Assert.assertEquals(DateTime.formatUTCtoSqlLocal(new Date()), DateTime.getCurrentLocal(DateTime.DateTimeFormat.SQL));
        System.out.println("DateTime.getCurrentLocal(DateTime.DateTimeFormat.SQL):"+DateTime.getCurrentLocal(DateTime.DateTimeFormat.SQL));
        
    }
}
