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

import java.util.Iterator;
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
public class SortedListModelTest {
	
	public SortedListModelTest() {
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
	 * Test of getSize method, of class SortedListModel.
	 */
	@Test
	public void testGetSize() {
		System.out.println("getSize");
		SortedListModel instance = new SortedListModel();
		int expResult = 0;
		int result = instance.getSize();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getElementAt method, of class SortedListModel.
	 */
	@Test
	public void testGetElementAt() {
		System.out.println("getElementAt");
		int index = 0;
		SortedListModel instance = new SortedListModel();
		Object expResult = null;
		Object result = instance.getElementAt(index);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of add method, of class SortedListModel.
	 */
	@Test
	public void testAdd() {
		System.out.println("add");
		Object element = null;
		SortedListModel instance = new SortedListModel();
		boolean expResult = false;
		boolean result = instance.add(element);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addAll method, of class SortedListModel.
	 */
	@Test
	public void testAddAll() {
		System.out.println("addAll");
		Object[] elements = null;
		SortedListModel instance = new SortedListModel();
		instance.addAll(elements);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of fire method, of class SortedListModel.
	 */
	@Test
	public void testFire() {
		System.out.println("fire");
		SortedListModel instance = new SortedListModel();
		instance.fire();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of clear method, of class SortedListModel.
	 */
	@Test
	public void testClear() {
		System.out.println("clear");
		SortedListModel instance = new SortedListModel();
		instance.clear();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of contains method, of class SortedListModel.
	 */
	@Test
	public void testContains() {
		System.out.println("contains");
		Object element = null;
		SortedListModel instance = new SortedListModel();
		boolean expResult = false;
		boolean result = instance.contains(element);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of firstElement method, of class SortedListModel.
	 */
	@Test
	public void testFirstElement() {
		System.out.println("firstElement");
		SortedListModel instance = new SortedListModel();
		Object expResult = null;
		Object result = instance.firstElement();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of iterator method, of class SortedListModel.
	 */
	@Test
	public void testIterator() {
		System.out.println("iterator");
		SortedListModel instance = new SortedListModel();
		Iterator expResult = null;
		Iterator result = instance.iterator();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of lastElement method, of class SortedListModel.
	 */
	@Test
	public void testLastElement() {
		System.out.println("lastElement");
		SortedListModel instance = new SortedListModel();
		Object expResult = null;
		Object result = instance.lastElement();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeElement method, of class SortedListModel.
	 */
	@Test
	public void testRemoveElement() {
		System.out.println("removeElement");
		Object element = null;
		SortedListModel instance = new SortedListModel();
		boolean expResult = false;
		boolean result = instance.removeElement(element);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
