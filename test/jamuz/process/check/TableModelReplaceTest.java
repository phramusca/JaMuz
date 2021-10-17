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
public class TableModelReplaceTest {
	
	public TableModelReplaceTest() {
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
	 * Test of getFiles method, of class TableModelReplace.
	 */
	@Test
	public void testGetFiles() {
		System.out.println("getFiles");
		TableModelReplace instance = new TableModelReplace();
		List<FileInfoDuplicateReplace> expResult = null;
		List<FileInfoDuplicateReplace> result = instance.getFiles();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRowCount method, of class TableModelReplace.
	 */
	@Test
	public void testGetRowCount() {
		System.out.println("getRowCount");
		TableModelReplace instance = new TableModelReplace();
		int expResult = 0;
		int result = instance.getRowCount();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getValueAt method, of class TableModelReplace.
	 */
	@Test
	public void testGetValueAt() {
		System.out.println("getValueAt");
		int rowIndex = 0;
		int columnIndex = 0;
		TableModelReplace instance = new TableModelReplace();
		Object expResult = null;
		Object result = instance.getValueAt(rowIndex, columnIndex);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getColumnClass method, of class TableModelReplace.
	 */
	@Test
	public void testGetColumnClass() {
		System.out.println("getColumnClass");
		int col = 0;
		TableModelReplace instance = new TableModelReplace();
		Class expResult = null;
		Class result = instance.getColumnClass(col);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setValueAt method, of class TableModelReplace.
	 */
	@Test
	public void testSetValueAt() {
		System.out.println("setValueAt");
		Object value = null;
		int col = 0;
		TableModelReplace instance = new TableModelReplace();
		instance.setValueAt(value, col);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of clear method, of class TableModelReplace.
	 */
	@Test
	public void testClear() {
		System.out.println("clear");
		TableModelReplace instance = new TableModelReplace();
		instance.clear();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addRow method, of class TableModelReplace.
	 */
	@Test
	public void testAddRow() {
		System.out.println("addRow");
		FileInfoDuplicateReplace file = null;
		TableModelReplace instance = new TableModelReplace();
		instance.addRow(file);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of moveRow method, of class TableModelReplace.
	 */
	@Test
	public void testMoveRow() throws Exception {
		System.out.println("moveRow");
		int fromIndex = 0;
		int toIndex = 0;
		TableModelReplace instance = new TableModelReplace();
		instance.moveRow(fromIndex, toIndex);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
