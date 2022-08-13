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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class StatSourceSQLTest {

	public StatSourceSQLTest() {
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
	 * Test of getDbConn method, of class StatSourceSQL.
	 */
	@Test
	public void testGetDbConn() {
		System.out.println("getDbConn");
		StatSourceSQL instance = null;
		DbConn expResult = null;
		DbConn result = instance.getDbConn();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getStatistics method, of class StatSourceSQL.
	 */
	@Test
	public void testGetStatistics_ArrayList() {
		System.out.println("getStatistics");
		ArrayList<FileInfo> files = null;
		StatSourceSQL instance = null;
		boolean expResult = false;
		boolean result = instance.getStatistics(files);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getStatistics method, of class StatSourceSQL.
	 */
	@Test
	public void testGetStatistics_ResultSet() {
		System.out.println("getStatistics");
		ResultSet rs = null;
		StatSourceSQL instance = null;
		FileInfo expResult = null;
		FileInfo result = instance.getFileStatistics(rs);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateStatistics method, of class StatSourceSQL.
	 */
	@Test
	public void testUpdateStatistics() {
		System.out.println("updateStatistics");
		ArrayList<? extends FileInfo> files = null;
		StatSourceSQL instance = null;
		int[] expResult = null;
		int[] result = instance.updateFileStatistics(files);
		assertArrayEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setUpdateStatisticsParameters method, of class StatSourceSQL.
	 */
	@Test
	public void testSetUpdateStatisticsParameters() throws Exception {
		System.out.println("setUpdateStatisticsParameters");
		FileInfo file = null;
		StatSourceSQL instance = null;
		instance.setUpdateStatisticsParameters(file);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPath method, of class StatSourceSQL.
	 */
	@Test
	public void testGetPath() {
		System.out.println("getPath");
		String path = "";
		StatSourceSQL instance = null;
		String expResult = "";
		String result = instance.getPath(path);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of check method, of class StatSourceSQL.
	 */
	@Test
	public void testCheck() {
		System.out.println("check");
		StatSourceSQL instance = null;
		boolean expResult = false;
		boolean result = instance.check();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getSource method, of class StatSourceSQL.
	 */
	@Test
	public void testGetSource() {
		System.out.println("getSource");
		String locationWork = "";
		StatSourceSQL instance = null;
		boolean expResult = false;
		boolean result = instance.getSource(locationWork);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of sendSource method, of class StatSourceSQL.
	 */
	@Test
	public void testSendSource() {
		System.out.println("sendSource");
		String locationWork = "";
		StatSourceSQL instance = null;
		boolean expResult = false;
		boolean result = instance.sendSource(locationWork);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of backupSource method, of class StatSourceSQL.
	 */
	@Test
	public void testBackupSource() {
		System.out.println("backupSource");
		String locationWork = "";
		StatSourceSQL instance = null;
		boolean expResult = false;
		boolean result = instance.backupSource(locationWork);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of tearDown method, of class StatSourceSQL.
	 */
	@Test
	public void testTearDown() {
		System.out.println("tearDown");
		StatSourceSQL instance = null;
		boolean expResult = false;
		boolean result = instance.tearDown();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	public class StatSourceSQLImpl extends StatSourceSQL {

		public StatSourceSQLImpl() {
			super(null, "", "", false, false, false, false, false, false);
		}

		@Override
		public void setUpdateStatisticsParameters(FileInfo file) throws SQLException {
		}

		@Override
		public boolean getTags(ArrayList<String> tags, FileInfo file) {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}

		@Override
		public boolean setUp(boolean isRemote) {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}
	}

}
