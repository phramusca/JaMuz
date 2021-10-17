/*
 * Copyright (C) 2019 phramusca ( https://github.com/phramusca/ )
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
package jamuz;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class LogFormatTest {

	/**
	 *
	 */
	public LogFormatTest() {
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
	 * Test of format method, of class LogFormat.
	 */
	@Test
	public void testFormat() {
		System.out.println("format");
		LogRecord record = null;
		LogFormat instance = new LogFormat();
		String expResult = "";
		String result = instance.format(record);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getHead method, of class LogFormat.
	 */
	@Test
	public void testGetHead() {
		System.out.println("getHead");
		Handler h = null;
		LogFormat instance = new LogFormat();
		String expResult = "";
		String result = instance.getHead(h);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTail method, of class LogFormat.
	 */
	@Test
	public void testGetTail() {
		System.out.println("getTail");
		Handler h = null;
		LogFormat instance = new LogFormat();
		String expResult = "";
		String result = instance.getTail(h);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
