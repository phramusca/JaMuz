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

import java.sql.Connection;
import java.sql.ResultSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class DbConnTest {
	
	public DbConnTest() {
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
	 * Test of getConnnection method, of class DbConn.
	 */
	@Test
	public void testGetConnnection() {
		System.out.println("getConnnection");
		DbConn instance = null;
		Connection expResult = null;
		Connection result = instance.getConnnection();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getInfo method, of class DbConn.
	 */
	@Test
	public void testGetInfo() {
		System.out.println("getInfo");
		DbConn instance = null;
		DbInfo expResult = null;
		DbInfo result = instance.getInfo();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of connect method, of class DbConn.
	 */
	@Test
	public void testConnect() {
		System.out.println("connect");
		DbConn instance = null;
		boolean expResult = false;
		boolean result = instance.connect();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of disconnect method, of class DbConn.
	 */
	@Test
	public void testDisconnect() {
		System.out.println("disconnect");
		DbConn instance = null;
		instance.disconnect();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getStringValue method, of class DbConn.
	 */
	@Test
	public void testGetStringValue_3args_1() {
		System.out.println("getStringValue");
		ResultSet rs = null;
		String source = "";
		String defaultValue = "";
		DbConn instance = null;
		String expResult = "";
		String result = instance.getStringValue(rs, source, defaultValue);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getStringValue method, of class DbConn.
	 */
	@Test
	public void testGetStringValue_ResultSet_String() {
		System.out.println("getStringValue");
		ResultSet rs = null;
		String source = "";
		DbConn instance = null;
		String expResult = "";
		String result = instance.getStringValue(rs, source);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getStringValue method, of class DbConn.
	 */
	@Test
	public void testGetStringValue_3args_2() {
		System.out.println("getStringValue");
		ResultSet rs = null;
		String source = "";
		boolean replaceEmpty = false;
		DbConn instance = null;
		String expResult = "";
		String result = instance.getStringValue(rs, source, replaceEmpty);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
