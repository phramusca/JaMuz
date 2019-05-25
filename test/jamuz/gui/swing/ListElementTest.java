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

import jamuz.FileInfoInt;
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
public class ListElementTest {
	
	public ListElementTest() {
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
	 * Test of toString method, of class ListElement.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		ListElement instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getValue method, of class ListElement.
	 */
	@Test
	public void testGetValue() {
		System.out.println("getValue");
		ListElement instance = null;
		String expResult = "";
		String result = instance.getValue();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setDisplay method, of class ListElement.
	 */
	@Test
	public void testSetDisplay() {
		System.out.println("setDisplay");
		String display = "";
		ListElement instance = null;
		instance.setDisplay(display);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFile method, of class ListElement.
	 */
	@Test
	public void testGetFile() {
		System.out.println("getFile");
		ListElement instance = null;
		FileInfoInt expResult = null;
		FileInfoInt result = instance.getFile();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of equals method, of class ListElement.
	 */
	@Test
	public void testEquals() {
		System.out.println("equals");
		Object obj = null;
		ListElement instance = null;
		boolean expResult = false;
		boolean result = instance.equals(obj);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of hashCode method, of class ListElement.
	 */
	@Test
	public void testHashCode() {
		System.out.println("hashCode");
		ListElement instance = null;
		int expResult = 0;
		int result = instance.hashCode();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of clone method, of class ListElement.
	 */
	@Test
	public void testClone() throws Exception {
		System.out.println("clone");
		ListElement instance = null;
		ListElement expResult = null;
		ListElement result = instance.clone();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
