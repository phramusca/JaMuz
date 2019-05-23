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

import javax.swing.table.TableColumn;
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
public class TableColumnModelTest {
	
	public TableColumnModelTest() {
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
	 * Test of setColumnVisible method, of class TableColumnModel.
	 */
	@Test
	public void testSetColumnVisible() {
		System.out.println("setColumnVisible");
		TableColumn column = null;
		boolean visible = false;
		TableColumnModel instance = new TableColumnModel();
		instance.setColumnVisible(column, visible);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setAllColumnsVisible method, of class TableColumnModel.
	 */
	@Test
	public void testSetAllColumnsVisible() {
		System.out.println("setAllColumnsVisible");
		TableColumnModel instance = new TableColumnModel();
		instance.setAllColumnsVisible();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getColumnByModelIndex method, of class TableColumnModel.
	 */
	@Test
	public void testGetColumnByModelIndex() {
		System.out.println("getColumnByModelIndex");
		int modelColumnIndex = 0;
		TableColumnModel instance = new TableColumnModel();
		TableColumn expResult = null;
		TableColumn result = instance.getColumnByModelIndex(modelColumnIndex);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isColumnVisible method, of class TableColumnModel.
	 */
	@Test
	public void testIsColumnVisible() {
		System.out.println("isColumnVisible");
		TableColumn aColumn = null;
		TableColumnModel instance = new TableColumnModel();
		boolean expResult = false;
		boolean result = instance.isColumnVisible(aColumn);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addColumn method, of class TableColumnModel.
	 */
	@Test
	public void testAddColumn() {
		System.out.println("addColumn");
		TableColumn column = null;
		TableColumnModel instance = new TableColumnModel();
		instance.addColumn(column);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeColumn method, of class TableColumnModel.
	 */
	@Test
	public void testRemoveColumn() {
		System.out.println("removeColumn");
		TableColumn column = null;
		TableColumnModel instance = new TableColumnModel();
		instance.removeColumn(column);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of moveColumn method, of class TableColumnModel.
	 */
	@Test
	public void testMoveColumn() {
		System.out.println("moveColumn");
		int oldIndex = 0;
		int newIndex = 0;
		TableColumnModel instance = new TableColumnModel();
		instance.moveColumn(oldIndex, newIndex);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getColumnCount method, of class TableColumnModel.
	 */
	@Test
	public void testGetColumnCount() {
		System.out.println("getColumnCount");
		boolean onlyVisible = false;
		TableColumnModel instance = new TableColumnModel();
		int expResult = 0;
		int result = instance.getColumnCount(onlyVisible);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getColumnIndex method, of class TableColumnModel.
	 */
	@Test
	public void testGetColumnIndex() {
		System.out.println("getColumnIndex");
		Object identifier = null;
		boolean onlyVisible = false;
		TableColumnModel instance = new TableColumnModel();
		int expResult = 0;
		int result = instance.getColumnIndex(identifier, onlyVisible);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getColumn method, of class TableColumnModel.
	 */
	@Test
	public void testGetColumn() {
		System.out.println("getColumn");
		int columnIndex = 0;
		boolean onlyVisible = false;
		TableColumnModel instance = new TableColumnModel();
		TableColumn expResult = null;
		TableColumn result = instance.getColumn(columnIndex, onlyVisible);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
