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
package jamuz.gui.swing;

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
public class TableModelTest {

	/**
	 *
	 */
	public TableModelTest() {
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
	 * Test of setModel method, of class TableModel.
	 */
	@Test
	public void testSetModel() {
		System.out.println("setModel");
		String[] columnNames = null;
		Object[][] data = null;
		TableModel instance = new TableModel();
		instance.setModel(columnNames, data);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setEditable method, of class TableModel.
	 */
	@Test
	public void testSetEditable() {
		System.out.println("setEditable");
		Integer[] editableColumns = null;
		TableModel instance = new TableModel();
		instance.setEditable(editableColumns);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addRow method, of class TableModel.
	 */
	@Test
	public void testAddRow() {
		System.out.println("addRow");
		Object[] data = null;
		TableModel instance = new TableModel();
		instance.addRow(data);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of clear method, of class TableModel.
	 */
	@Test
	public void testClear() {
		System.out.println("clear");
		TableModel instance = new TableModel();
		instance.clear();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getList method, of class TableModel.
	 */
	@Test
	public void testGetList() {
		System.out.println("getList");
		int columnIndex = 0;
		TableModel instance = new TableModel();
		List expResult = null;
		List result = instance.getList(columnIndex);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeRow method, of class TableModel.
	 */
	@Test
	public void testRemoveRow() {
		System.out.println("removeRow");
		int position = 0;
		TableModel instance = new TableModel();
		instance.removeRow(position);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of moveRow method, of class TableModel.
	 */
	@Test
	public void testMoveRow() {
		System.out.println("moveRow");
		int fromIndex = 0;
		int toIndex = 0;
		int[] fixedColumns = null;
		TableModel instance = new TableModel();
		instance.moveRow(fromIndex, toIndex, fixedColumns);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRowCount method, of class TableModel.
	 */
	@Test
	public void testGetRowCount() {
		System.out.println("getRowCount");
		TableModel instance = new TableModel();
		int expResult = 0;
		int result = instance.getRowCount();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getColumnCount method, of class TableModel.
	 */
	@Test
	public void testGetColumnCount() {
		System.out.println("getColumnCount");
		TableModel instance = new TableModel();
		int expResult = 0;
		int result = instance.getColumnCount();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getValueAt method, of class TableModel.
	 */
	@Test
	public void testGetValueAt() {
		System.out.println("getValueAt");
		int rowIndex = 0;
		int columnIndex = 0;
		TableModel instance = new TableModel();
		Object expResult = null;
		Object result = instance.getValueAt(rowIndex, columnIndex);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getColumnName method, of class TableModel.
	 */
	@Test
	public void testGetColumnName() {
		System.out.println("getColumnName");
		int col = 0;
		TableModel instance = new TableModel();
		String expResult = "";
		String result = instance.getColumnName(col);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getColumnClass method, of class TableModel.
	 */
	@Test
	public void testGetColumnClass() {
		System.out.println("getColumnClass");
		int col = 0;
		TableModel instance = new TableModel();
		Class expResult = null;
		Class result = instance.getColumnClass(col);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setValueAt method, of class TableModel.
	 */
	@Test
	public void testSetValueAt() {
		System.out.println("setValueAt");
		Object value = null;
		int row = 0;
		int col = 0;
		TableModel instance = new TableModel();
		instance.setValueAt(value, row, col);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isCellEditable method, of class TableModel.
	 */
	@Test
	public void testIsCellEditable() {
		System.out.println("isCellEditable");
		int row = 0;
		int col = 0;
		TableModel instance = new TableModel();
		boolean expResult = false;
		boolean result = instance.isCellEditable(row, col);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
