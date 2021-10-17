/*
 * Copyright (C) 2018 phramusca ( https://github.com/phramusca/JaMuz/ )
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
package jamuz.utils;

import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DateTimeTest {
	
	/**
	 *
	 */
	public DateTimeTest() {
	}
	
	/**
	 *
	 */
	@BeforeClass
	public static void setUpClass() {
	}
	
	/**
	 *
	 */
	@AfterClass
	public static void tearDownClass() {
	}
	
	/**
	 *
	 */
	@Before
	public void setUp() {
	}
	
	/**
	 *
	 */
	@After
	public void tearDown() {
	}
	
	/**
	 * Test of formatUTC method, of class DateTime.
	 */
	@Test
	public void testFormatUTC_3args_1() {
		System.out.println("formatUTC");
		assertEquals("1970-01-01 01:00:00", DateTime.formatUTC(new Date(0), "yyyy-MM-dd HH:mm:ss", true));
        System.out.println(DateTime.formatUTC(new Date(0), "yyyy-MM-dd HH:mm:ss", true));
        assertEquals("1970-01-01 00:00:00", DateTime.formatUTC(new Date(0), "yyyy-MM-dd HH:mm:ss", false));
        System.out.println(DateTime.formatUTC(new Date(0), "yyyy-MM-dd HH:mm:ss", false));	
	}

	/**
	 * Test of formatUTC method, of class DateTime.
	 */
	@Test
	public void testFormatUTC_3args_2() {
		System.out.println("formatUTC");
		assertEquals("1970-01-01 01:00:00", DateTime.formatUTC(new Date(0), DateTime.DateTimeFormat.SQL, true));
        System.out.println(DateTime.formatUTC(new Date(0), DateTime.DateTimeFormat.SQL, true));
        assertEquals("1970-01-01 00:00:00", DateTime.formatUTC(new Date(0), DateTime.DateTimeFormat.SQL, false));
        System.out.println(DateTime.formatUTC(new Date(0), DateTime.DateTimeFormat.SQL, false));
	}

	/**
	 * Test of formatUTCtoSqlUTC method, of class DateTime.
	 */
	@Test
	public void testFormatUTCtoSqlUTC() {
		System.out.println("formatUTCtoSqlUTC");
		//Refer to formatUtc() below
	}

	/**
	 * Test of formatUTCtoSqlLocal method, of class DateTime.
	 */
	@Test
	public void testFormatUTCtoSqlLocal() {
		System.out.println("formatUTCtoSqlLocal");
		//Refer to formatUtc() below
	}
	
	/**
	 *
	 * @throws Exception
	 */
	@Test
    public void formatUtc() throws Exception {
        assertEquals("1970-01-01 01:00:00", DateTime.formatUTCtoSqlLocal(new Date(0)));
        System.out.println(DateTime.formatUTCtoSqlLocal(new Date(0)));
        assertEquals("1970-01-01 00:00:00", DateTime.formatUTCtoSqlUTC(new Date(0)));
        System.out.println(DateTime.formatUTCtoSqlUTC(new Date(0)));
        
        assertEquals(DateTime.formatUTCtoSqlUTC(new Date()), DateTime.getCurrentUtcSql());
        System.out.println("DateTime.getCurrentUtcSql(): "+DateTime.getCurrentUtcSql());
        assertEquals(DateTime.formatUTCtoSqlLocal(new Date()), DateTime.getCurrentLocal(DateTime.DateTimeFormat.SQL));
        System.out.println("DateTime.getCurrentLocal(DateTime.DateTimeFormat.SQL):"+DateTime.getCurrentLocal(DateTime.DateTimeFormat.SQL));        
    }

	/**
	 * Test of getCurrentLocal method, of class DateTime.
	 */
	@Test
	public void testGetCurrentLocal() {
		System.out.println("getCurrentLocal");
		//Subset of formatUTC with current date: already tested
	}

	/**
	 * Test of getCurrentUtcSql method, of class DateTime.
	 */
	@Test
	public void testGetCurrentUtcSql() {
		System.out.println("getCurrentUtcSql");
		//Subset of formatUTC with current date: already tested
	}

	/**
	 * Test of parseSqlUtc method, of class DateTime.
	 */
	@Test
	public void testParseSqlUtc() {
		System.out.println("parseSqlUtc");
		String date = "2019-05-23 11:04:38";
		Date result = DateTime.parseSqlUtc(date);
		assertEquals(DateTime.formatUTCtoSqlUTC(result), date);
	}
	
}
