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
package jamuz.process.check;

import java.util.List;
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
public class TableModelCheckTracksTest {
	
	public TableModelCheckTracksTest() {
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
	 * Test of getFiles method, of class TableModelCheckTracks.
	 */
	@Test
	public void testGetFiles() {
		System.out.println("getFiles");
		TableModelCheckTracks instance = new TableModelCheckTracks();
		List<FileInfoDisplay> expResult = null;
		List<FileInfoDisplay> result = instance.getFiles();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRowCount method, of class TableModelCheckTracks.
	 */
	@Test
	public void testGetRowCount() {
		System.out.println("getRowCount");
		TableModelCheckTracks instance = new TableModelCheckTracks();
		int expResult = 0;
		int result = instance.getRowCount();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getValueAt method, of class TableModelCheckTracks.
	 */
	@Test
	public void testGetValueAt() {
		System.out.println("getValueAt");
		int rowIndex = 0;
		int columnIndex = 0;
		TableModelCheckTracks instance = new TableModelCheckTracks();
		Object expResult = null;
		Object result = instance.getValueAt(rowIndex, columnIndex);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getColumnClass method, of class TableModelCheckTracks.
	 */
	@Test
	public void testGetColumnClass() {
		System.out.println("getColumnClass");
		int col = 0;
		TableModelCheckTracks instance = new TableModelCheckTracks();
		Class expResult = null;
		Class result = instance.getColumnClass(col);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setValueAt method, of class TableModelCheckTracks.
	 */
	@Test
	public void testSetValueAt_Object_int() {
		System.out.println("setValueAt");
		Object value = null;
		int col = 0;
		TableModelCheckTracks instance = new TableModelCheckTracks();
		instance.setValueAt(value, col);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setValueAt method, of class TableModelCheckTracks.
	 */
	@Test
	public void testSetValueAt_3args() {
		System.out.println("setValueAt");
		Object value = null;
		int row = 0;
		int col = 0;
		TableModelCheckTracks instance = new TableModelCheckTracks();
		instance.setValueAt(value, row, col);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of clear method, of class TableModelCheckTracks.
	 */
	@Test
	public void testClear() {
		System.out.println("clear");
		TableModelCheckTracks instance = new TableModelCheckTracks();
		instance.clear();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addRow method, of class TableModelCheckTracks.
	 */
	@Test
	public void testAddRow() {
		System.out.println("addRow");
		FileInfoDisplay file = null;
		TableModelCheckTracks instance = new TableModelCheckTracks();
		instance.addRow(file);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of moveRow method, of class TableModelCheckTracks.
	 */
	@Test
	public void testMoveRow() throws Exception {
		System.out.println("moveRow");
		int fromIndex = 0;
		int toIndex = 0;
		TableModelCheckTracks instance = new TableModelCheckTracks();
		instance.moveRow(fromIndex, toIndex);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
