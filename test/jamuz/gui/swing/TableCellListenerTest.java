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

import java.beans.PropertyChangeEvent;
import javax.swing.JTable;
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
public class TableCellListenerTest {
	
	public TableCellListenerTest() {
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
	 * Test of getColumn method, of class TableCellListener.
	 */
	@Test
	public void testGetColumn() {
		System.out.println("getColumn");
		TableCellListener instance = null;
		int expResult = 0;
		int result = instance.getColumn();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getNewValue method, of class TableCellListener.
	 */
	@Test
	public void testGetNewValue() {
		System.out.println("getNewValue");
		TableCellListener instance = null;
		Object expResult = null;
		Object result = instance.getNewValue();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getOldValue method, of class TableCellListener.
	 */
	@Test
	public void testGetOldValue() {
		System.out.println("getOldValue");
		TableCellListener instance = null;
		Object expResult = null;
		Object result = instance.getOldValue();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRow method, of class TableCellListener.
	 */
	@Test
	public void testGetRow() {
		System.out.println("getRow");
		TableCellListener instance = null;
		int expResult = 0;
		int result = instance.getRow();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTable method, of class TableCellListener.
	 */
	@Test
	public void testGetTable() {
		System.out.println("getTable");
		TableCellListener instance = null;
		JTable expResult = null;
		JTable result = instance.getTable();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of propertyChange method, of class TableCellListener.
	 */
	@Test
	public void testPropertyChange() {
		System.out.println("propertyChange");
		PropertyChangeEvent e = null;
		TableCellListener instance = null;
		instance.propertyChange(e);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of run method, of class TableCellListener.
	 */
	@Test
	public void testRun() {
		System.out.println("run");
		TableCellListener instance = null;
		instance.run();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
