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
public class TableModelCheckTest {

	public TableModelCheckTest() {
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
	 * Test of getRowCount method, of class TableModelCheck.
	 */
	@Test
	public void testGetRowCount() {
		System.out.println("getRowCount");
		TableModelCheck instance = new TableModelCheck();
		int expResult = 0;
		int result = instance.getRowCount();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getValueAt method, of class TableModelCheck.
	 */
	@Test
	public void testGetValueAt() {
		System.out.println("getValueAt");
		int rowIndex = 0;
		int columnIndex = 0;
		TableModelCheck instance = new TableModelCheck();
		Object expResult = null;
		Object result = instance.getValueAt(rowIndex, columnIndex);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getColumnClass method, of class TableModelCheck.
	 */
	@Test
	public void testGetColumnClass() {
		System.out.println("getColumnClass");
		int col = 0;
		TableModelCheck instance = new TableModelCheck();
		Class expResult = null;
		Class result = instance.getColumnClass(col);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of clear method, of class TableModelCheck.
	 */
	@Test
	public void testClear() {
		System.out.println("clear");
		TableModelCheck instance = new TableModelCheck();
		instance.clear();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addRow method, of class TableModelCheck.
	 */
	@Test
	public void testAddRow() {
		System.out.println("addRow");
		FolderInfo folder = null;
		TableModelCheck instance = new TableModelCheck();
		instance.addRow(folder);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFolders method, of class TableModelCheck.
	 */
	@Test
	public void testGetFolders() {
		System.out.println("getFolders");
		TableModelCheck instance = new TableModelCheck();
		List<FolderInfo> expResult = null;
		List<FolderInfo> result = instance.getFolders();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeRow method, of class TableModelCheck.
	 */
	@Test
	public void testRemoveRow() {
		System.out.println("removeRow");
		FolderInfo folder = null;
		TableModelCheck instance = new TableModelCheck();
		instance.removeRow(folder);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of moveRow method, of class TableModelCheck.
	 */
	@Test
	public void testMoveRow() throws Exception {
		System.out.println("moveRow");
		int fromIndex = 0;
		int toIndex = 0;
		TableModelCheck instance = new TableModelCheck();
		instance.moveRow(fromIndex, toIndex);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
