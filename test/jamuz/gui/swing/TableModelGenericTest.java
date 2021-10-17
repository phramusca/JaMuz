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
public class TableModelGenericTest {
	
	/**
	 *
	 */
	public TableModelGenericTest() {
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
	 * Test of setColumnNames method, of class TableModelGeneric.
	 */
	@Test
	public void testSetColumnNames() {
		System.out.println("setColumnNames");
		String[] columnNames = null;
		TableModelGeneric instance = new TableModelGenericImpl();
		instance.setColumnNames(columnNames);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isCellEditable method, of class TableModelGeneric.
	 */
	@Test
	public void testIsCellEditable() {
		System.out.println("isCellEditable");
		int row = 0;
		int col = 0;
		TableModelGeneric instance = new TableModelGenericImpl();
		boolean expResult = false;
		boolean result = instance.isCellEditable(row, col);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getColumnCount method, of class TableModelGeneric.
	 */
	@Test
	public void testGetColumnCount() {
		System.out.println("getColumnCount");
		TableModelGeneric instance = new TableModelGenericImpl();
		int expResult = 0;
		int result = instance.getColumnCount();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getColumnName method, of class TableModelGeneric.
	 */
	@Test
	public void testGetColumnName() {
		System.out.println("getColumnName");
		int col = 0;
		TableModelGeneric instance = new TableModelGenericImpl();
		String expResult = "";
		String result = instance.getColumnName(col);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 *
	 */
	public class TableModelGenericImpl extends TableModelGeneric {

		/**
		 *
		 * @return
		 */
		@Override
		public int getRowCount() {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}

		/**
		 *
		 * @param rowIndex
		 * @param columnIndex
		 * @return
		 */
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		}
	}
	
}
