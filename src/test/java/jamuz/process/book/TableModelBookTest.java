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
package jamuz.process.book;

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
public class TableModelBookTest {

	/**
	 *
	 */
	public TableModelBookTest() {
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
	 * Test of isCellEditable method, of class TableModelBook.
	 */
	@Test
	public void testIsCellEditable() {
		System.out.println("isCellEditable");
		int row = 0;
		int col = 0;
		TableModelBook instance = new TableModelBook();
		boolean expResult = false;
		boolean result = instance.isCellEditable(row, col);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isCellEnabled method, of class TableModelBook.
	 */
	@Test
	public void testIsCellEnabled() {
		System.out.println("isCellEnabled");
		int row = 0;
		int col = 0;
		TableModelBook instance = new TableModelBook();
		boolean expResult = false;
		boolean result = instance.isCellEnabled(row, col);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getBooks method, of class TableModelBook.
	 */
	@Test
	public void testGetBooks() {
		System.out.println("getBooks");
		TableModelBook instance = new TableModelBook();
		List<Book> expResult = null;
		List<Book> result = instance.getBooks();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFile method, of class TableModelBook.
	 */
	@Test
	public void testGetFile() {
		System.out.println("getFile");
		int index = 0;
		TableModelBook instance = new TableModelBook();
		Book expResult = null;
		Book result = instance.getFile(index);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLengthAll method, of class TableModelBook.
	 */
	@Test
	public void testGetLengthAll() {
		System.out.println("getLengthAll");
		TableModelBook instance = new TableModelBook();
		long expResult = 0L;
		long result = instance.getLengthAll();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getNbSelected method, of class TableModelBook.
	 */
	@Test
	public void testGetNbSelected() {
		System.out.println("getNbSelected");
		TableModelBook instance = new TableModelBook();
		int expResult = 0;
		int result = instance.getNbSelected();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRowCount method, of class TableModelBook.
	 */
	@Test
	public void testGetRowCount() {
		System.out.println("getRowCount");
		TableModelBook instance = new TableModelBook();
		int expResult = 0;
		int result = instance.getRowCount();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getValueAt method, of class TableModelBook.
	 */
	@Test
	public void testGetValueAt() {
		System.out.println("getValueAt");
		int rowIndex = 0;
		int columnIndex = 0;
		TableModelBook instance = new TableModelBook();
		Object expResult = null;
		Object result = instance.getValueAt(rowIndex, columnIndex);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setValueAt method, of class TableModelBook.
	 */
	@Test
	public void testSetValueAt() {
		System.out.println("setValueAt");
		Object value = null;
		int row = 0;
		int col = 0;
		TableModelBook instance = new TableModelBook();
		instance.setValueAt(value, row, col);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of select method, of class TableModelBook.
	 */
	@Test
	public void testSelect() {
		System.out.println("select");
		Book book = null;
		boolean selected = false;
		TableModelBook instance = new TableModelBook();
		instance.select(book, selected);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLengthSelected method, of class TableModelBook.
	 */
	@Test
	public void testGetLengthSelected() {
		System.out.println("getLengthSelected");
		TableModelBook instance = new TableModelBook();
		long expResult = 0L;
		long result = instance.getLengthSelected();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getColumnClass method, of class TableModelBook.
	 */
	@Test
	public void testGetColumnClass() {
		System.out.println("getColumnClass");
		int col = 0;
		TableModelBook instance = new TableModelBook();
		Class expResult = null;
		Class result = instance.getColumnClass(col);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of clear method, of class TableModelBook.
	 */
	@Test
	public void testClear() {
		System.out.println("clear");
		TableModelBook instance = new TableModelBook();
		instance.clear();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addRow method, of class TableModelBook.
	 */
	@Test
	public void testAddRow() {
		System.out.println("addRow");
		Book file = null;
		TableModelBook instance = new TableModelBook();
		instance.addRow(file);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeRow method, of class TableModelBook.
	 */
	@Test
	public void testRemoveRow() {
		System.out.println("removeRow");
		Book file = null;
		TableModelBook instance = new TableModelBook();
		instance.removeRow(file);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of loadThumbnails method, of class TableModelBook.
	 */
	@Test
	public void testLoadThumbnails() {
		System.out.println("loadThumbnails");
		TableModelBook instance = new TableModelBook();
		instance.loadThumbnails();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
