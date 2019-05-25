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
package jamuz.process.video;

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
public class ProcessVideoTest {
	
	public ProcessVideoTest() {
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
	 * Test of export method, of class ProcessVideo.
	 */
	@Test
	public void testExport() {
		System.out.println("export");
		ProcessVideo instance = null;
		instance.export();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of listDb method, of class ProcessVideo.
	 */
	@Test
	public void testListDb() {
		System.out.println("listDb");
		boolean move = false;
		boolean getDb = false;
		boolean search = false;
		ProcessVideo instance = null;
		instance.listDb(move, getDb, search);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTableModel method, of class ProcessVideo.
	 */
	@Test
	public void testGetTableModel() {
		System.out.println("getTableModel");
		ProcessVideo instance = null;
		TableModelVideo expResult = null;
		TableModelVideo result = instance.getTableModel();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
