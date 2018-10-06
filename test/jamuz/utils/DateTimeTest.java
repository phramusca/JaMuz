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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DateTimeTest {
	
	public DateTimeTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}
	
	/**
	 *
	 * @throws Exception
	 */
	@Test
    public void testConversions() throws Exception {

        assertEquals(new Date(0), DateTime.parseSqlUtc("1970-01-01 00:00:00"));
        System.out.println(DateTime.parseSqlUtc("1970-01-01 00:00:00"));

        assertEquals("1970-01-01 01:00:00", DateTime.formatUTC(new Date(0), DateTime.DateTimeFormat.SQL, true));
        System.out.println(DateTime.formatUTC(new Date(0), DateTime.DateTimeFormat.SQL, true));
        assertEquals("1970-01-01 00:00:00", DateTime.formatUTC(new Date(0), DateTime.DateTimeFormat.SQL, false));
        System.out.println(DateTime.formatUTC(new Date(0), DateTime.DateTimeFormat.SQL, false));
        
        assertEquals("1970-01-01 01:00:00", DateTime.formatUTC(new Date(0), "yyyy-MM-dd HH:mm:ss", true));
        System.out.println(DateTime.formatUTC(new Date(0), "yyyy-MM-dd HH:mm:ss", true));
        assertEquals("1970-01-01 00:00:00", DateTime.formatUTC(new Date(0), "yyyy-MM-dd HH:mm:ss", false));
        System.out.println(DateTime.formatUTC(new Date(0), "yyyy-MM-dd HH:mm:ss", false));
        
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
	 * Test of formatUTC method, of class DateTime.
	 */
	@Test
	public void testFormatUTC_3args_1() {
		System.out.println("formatUTC");
		Date date = null;
		String format = "";
		boolean toLocal = false;
		String expResult = "";
		String result = DateTime.formatUTC(date, format, toLocal);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of formatUTC method, of class DateTime.
	 */
	@Test
	public void testFormatUTC_3args_2() {
		System.out.println("formatUTC");
		Date date = null;
		DateTime.DateTimeFormat format = null;
		boolean toLocal = false;
		String expResult = "";
		String result = DateTime.formatUTC(date, format, toLocal);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of formatUTCtoSqlUTC method, of class DateTime.
	 */
	@Test
	public void testFormatUTCtoSqlUTC() {
		System.out.println("formatUTCtoSqlUTC");
		Date date = null;
		String expResult = "";
		String result = DateTime.formatUTCtoSqlUTC(date);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of formatUTCtoSqlLocal method, of class DateTime.
	 */
	@Test
	public void testFormatUTCtoSqlLocal() {
		System.out.println("formatUTCtoSqlLocal");
		Date date = null;
		String expResult = "";
		String result = DateTime.formatUTCtoSqlLocal(date);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getCurrentLocal method, of class DateTime.
	 */
	@Test
	public void testGetCurrentLocal() {
		System.out.println("getCurrentLocal");
		DateTime.DateTimeFormat format = null;
		String expResult = "";
		String result = DateTime.getCurrentLocal(format);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getCurrentUtcSql method, of class DateTime.
	 */
	@Test
	public void testGetCurrentUtcSql() {
		System.out.println("getCurrentUtcSql");
		String expResult = "";
		String result = DateTime.getCurrentUtcSql();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of parseSqlUtc method, of class DateTime.
	 */
	@Test
	public void testParseSqlUtc() {
		System.out.println("parseSqlUtc");
		String date = "";
		Date expResult = null;
		Date result = DateTime.parseSqlUtc(date);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
