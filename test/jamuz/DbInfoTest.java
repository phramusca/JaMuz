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

import jamuz.utils.Ftp;
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
public class DbInfoTest {
	
	public DbInfoTest() {
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
	 * Test of getFtp method, of class DbInfo.
	 */
	@Test
	public void testGetFtp() {
		System.out.println("getFtp");
		String localFolder = "";
		DbInfo instance = null;
		Ftp expResult = null;
		Ftp result = instance.getFtp(localFolder);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of check method, of class DbInfo.
	 */
	@Test
	public void testCheck() {
		System.out.println("check");
		DbInfo instance = null;
		boolean expResult = false;
		boolean result = instance.check();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of copyDB method, of class DbInfo.
	 */
	@Test
	public void testCopyDB() {
		System.out.println("copyDB");
		boolean receive = false;
		String locationWork = "";
		DbInfo instance = null;
		boolean expResult = false;
		boolean result = instance.copyDB(receive, locationWork);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of backupDB method, of class DbInfo.
	 */
	@Test
	public void testBackupDB() {
		System.out.println("backupDB");
		String destinationPath = "";
		DbInfo instance = null;
		boolean expResult = false;
		boolean result = instance.backupDB(destinationPath);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLocationOri method, of class DbInfo.
	 */
	@Test
	public void testGetLocationOri() {
		System.out.println("getLocationOri");
		DbInfo instance = null;
		String expResult = "";
		String result = instance.getLocationOri();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLocationWork method, of class DbInfo.
	 */
	@Test
	public void testGetLocationWork() {
		System.out.println("getLocationWork");
		DbInfo instance = null;
		String expResult = "";
		String result = instance.getLocationWork();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setLocationWork method, of class DbInfo.
	 */
	@Test
	public void testSetLocationWork() {
		System.out.println("setLocationWork");
		String locationWork = "";
		DbInfo instance = null;
		instance.setLocationWork(locationWork);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
