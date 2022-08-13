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
package jamuz.process.merge;

import jamuz.FileInfo;
import java.sql.ResultSet;
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
public class StatSourceMixxxTest {

	public StatSourceMixxxTest() {
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
	 * Test of setUp method, of class StatSourceMixxx.
	 */
	@Test
	public void testSetUp() {
		System.out.println("setUp");
		StatSourceMixxx instance = null;
		boolean expResult = false;
		boolean isRemote = false;
		boolean result = instance.setUp(isRemote);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getStatistics method, of class StatSourceMixxx.
	 */
	@Test
	public void testGetStatistics() {
		System.out.println("getStatistics");
		ResultSet rs = null;
		StatSourceMixxx instance = null;
		FileInfo expResult = null;
		FileInfo result = instance.getFileStatistics(rs);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setUpdateStatisticsParameters method, of class StatSourceMixxx.
	 */
	@Test
	public void testSetUpdateStatisticsParameters() throws Exception {
		System.out.println("setUpdateStatisticsParameters");
		FileInfo file = null;
		StatSourceMixxx instance = null;
		instance.setUpdateStatisticsParameters(file);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of guessRootPath method, of class StatSourceMixxx.
	 */
	@Test
	public void testGuessRootPath() {
		System.out.println("guessRootPath");
		StatSourceMixxx instance = null;
		String expResult = "";
		String result = instance.guessRootPath();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTags method, of class StatSourceMixxx.
	 */
	@Test
	public void testGetTags() {
		System.out.println("getTags");
		ArrayList<String> tags = null;
		FileInfo file = null;
		StatSourceMixxx instance = null;
		boolean expResult = false;
		boolean result = instance.getTags(tags, file);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateStatistics method, of class StatSourceMixxx.
	 */
	@Test
	public void testUpdateStatistics() {
		System.out.println("updateStatistics");
		ArrayList<? extends FileInfo> files = null;
		StatSourceMixxx instance = null;
		int[] expResult = null;
		int[] result = instance.updateFileStatistics(files);
		assertArrayEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setTags method, of class StatSourceMixxx.
	 */
	@Test
	public void testSetTags() {
		System.out.println("setTags");
		ArrayList<? extends FileInfo> files = null;
		int[] results = null;
		StatSourceMixxx instance = null;
		int[] expResult = null;
		int[] result = instance.setTags(files, results);
		assertArrayEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
