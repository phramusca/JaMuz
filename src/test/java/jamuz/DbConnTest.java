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
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import test.helpers.TestResultSet;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class DbConnTest {

	/**
	 *
	 */
	public DbConnTest() {
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
	 * Test of getConnnection method, of class DbConn.
	 */
	@Test
	@Ignore //Simple getter
	public void testGetConnnection() {
		System.out.println("getConnnection");
		DbConn instance = null;
		Connection expResult = null;
		Connection result = instance.getConnection();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getInfo method, of class DbConn.
	 */
	@Test
	@Ignore //Simple getter
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
	@Ignore //
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
	@Ignore //
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

		DbConn instance = new DbConn(new DbInfo(DbInfo.LibType.Sqlite, "location", "user", "pwd"));

		assertEquals("MyDefoltV@lue", instance.getStringValue(
				TestResultSet.getResultSet("MailleNiouValiou"),
				"GimmeEmpty",
				"MyDefoltV@lue"));

		assertEquals("MyDefoltV@lue", instance.getStringValue(
				TestResultSet.getResultSet("MailleNiouValiou"),
				"GimmeNull",
				"MyDefoltV@lue"));

		assertEquals("MailleNiouValiou", instance.getStringValue(
				TestResultSet.getResultSet("MailleNiouValiou"),
				"GimmeValue",
				"MyDefoltV@lue"));
	}

	/**
	 * Test of getStringValue method, of class DbConn.
	 */
	@Test
	public void testGetStringValue_ResultSet_String() {
		System.out.println("getStringValue");

		DbConn instance = new DbConn(new DbInfo(DbInfo.LibType.Sqlite, "location", "user", "pwd"));

		assertEquals("", instance.getStringValue(
				TestResultSet.getResultSet("MailleNiouValiou"),
				"GimmeEmpty"));

		assertEquals("", instance.getStringValue(
				TestResultSet.getResultSet("MailleNiouValiou"),
				"GimmeNull"));

		assertEquals("MailleNiouValiou", instance.getStringValue(
				TestResultSet.getResultSet("MailleNiouValiou"),
				"GimmeValue"));
	}

	/**
	 * Test of getStringValue method, of class DbConn.
	 */
	@Test
	public void testGetStringValue_3args_2() {
		System.out.println("getStringValue");

		DbConn instance = new DbConn(new DbInfo(DbInfo.LibType.Sqlite, "location", "user", "pwd"));

		assertEquals("", instance.getStringValue(
				TestResultSet.getResultSet("MailleNiouValiou"),
				"GimmeEmpty"));

		assertEquals("", instance.getStringValue(
				TestResultSet.getResultSet("MailleNiouValiou"),
				"GimmeNull"));

		assertEquals("MailleNiouValiou", instance.getStringValue(
				TestResultSet.getResultSet("MailleNiouValiou"),
				"GimmeValue"));
	}

}
