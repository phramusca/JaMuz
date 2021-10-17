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
package jamuz.remote;

import java.util.Collection;
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
public class TableModelRemoteTest {

	/**
	 *
	 */
	public TableModelRemoteTest() {
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
	 * Test of setColumnNames method, of class TableModelRemote.
	 */
	@Test
	public void testSetColumnNames() {
		System.out.println("setColumnNames");
		TableModelRemote instance = new TableModelRemote();
		instance.setColumnNames();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getValueAt method, of class TableModelRemote.
	 */
	@Test
	public void testGetValueAt() {
		System.out.println("getValueAt");
		int rowIndex = 0;
		int columnIndex = 0;
		TableModelRemote instance = new TableModelRemote();
		Object expResult = null;
		Object result = instance.getValueAt(rowIndex, columnIndex);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isCellEditable method, of class TableModelRemote.
	 */
	@Test
	public void testIsCellEditable() {
		System.out.println("isCellEditable");
		int row = 0;
		int col = 0;
		TableModelRemote instance = new TableModelRemote();
		boolean expResult = false;
		boolean result = instance.isCellEditable(row, col);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isCellEnabled method, of class TableModelRemote.
	 */
	@Test
	public void testIsCellEnabled() {
		System.out.println("isCellEnabled");
		int row = 0;
		int col = 0;
		TableModelRemote instance = new TableModelRemote();
		boolean expResult = false;
		boolean result = instance.isCellEnabled(row, col);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getClients method, of class TableModelRemote.
	 */
	@Test
	public void testGetClients() {
		System.out.println("getClients");
		TableModelRemote instance = new TableModelRemote();
		Collection<String> expResult = null;
		Collection<String> result = instance.getClients();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getClient method, of class TableModelRemote.
	 */
	@Test
	public void testGetClient_String() {
		System.out.println("getClient");
		String login = "";
		TableModelRemote instance = new TableModelRemote();
		ClientInfo expResult = null;
		ClientInfo result = instance.getClient(login);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getClient method, of class TableModelRemote.
	 */
	@Test
	public void testGetClient_int() {
		System.out.println("getClient");
		int index = 0;
		TableModelRemote instance = new TableModelRemote();
		ClientInfo expResult = null;
		ClientInfo result = instance.getClient(index);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRowCount method, of class TableModelRemote.
	 */
	@Test
	public void testGetRowCount() {
		System.out.println("getRowCount");
		TableModelRemote instance = new TableModelRemote();
		int expResult = 0;
		int result = instance.getRowCount();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getColumnClass method, of class TableModelRemote.
	 */
	@Test
	public void testGetColumnClass() {
		System.out.println("getColumnClass");
		int col = 0;
		TableModelRemote instance = new TableModelRemote();
		Class expResult = null;
		Class result = instance.getColumnClass(col);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of clear method, of class TableModelRemote.
	 */
	@Test
	public void testClear() {
		System.out.println("clear");
		TableModelRemote instance = new TableModelRemote();
		instance.clear();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of add method, of class TableModelRemote.
	 */
	@Test
	public void testAdd() {
		System.out.println("add");
		ClientInfo client = null;
		TableModelRemote instance = new TableModelRemote();
		instance.add(client);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeClient method, of class TableModelRemote.
	 */
	@Test
	public void testRemoveClient() {
		System.out.println("removeClient");
		String login = "";
		TableModelRemote instance = new TableModelRemote();
		instance.removeClient(login);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of contains method, of class TableModelRemote.
	 */
	@Test
	public void testContains() {
		System.out.println("contains");
		String login = "";
		TableModelRemote instance = new TableModelRemote();
		boolean expResult = false;
		boolean result = instance.contains(login);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
