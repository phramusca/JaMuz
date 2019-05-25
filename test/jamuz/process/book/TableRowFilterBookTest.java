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

import jamuz.gui.swing.TriStateCheckBox;
import javax.swing.RowFilter;
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
public class TableRowFilterBookTest {
	
	public TableRowFilterBookTest() {
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
	 * Test of displaySelected method, of class TableRowFilterBook.
	 */
	@Test
	public void testDisplaySelected() {
		System.out.println("displaySelected");
		TriStateCheckBox.State display = null;
		TableRowFilterBook instance = new TableRowFilterBook();
		instance.displaySelected(display);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayByRating method, of class TableRowFilterBook.
	 */
	@Test
	public void testDisplayByRating() {
		System.out.println("displayByRating");
		String rating = "";
		TableRowFilterBook instance = new TableRowFilterBook();
		instance.displayByRating(rating);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayByTag method, of class TableRowFilterBook.
	 */
	@Test
	public void testDisplayByTag() {
		System.out.println("displayByTag");
		String tag = "";
		TableRowFilterBook instance = new TableRowFilterBook();
		instance.displayByTag(tag);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayLocal method, of class TableRowFilterBook.
	 */
	@Test
	public void testDisplayLocal() {
		System.out.println("displayLocal");
		TriStateCheckBox.State display = null;
		TableRowFilterBook instance = new TableRowFilterBook();
		instance.displayLocal(display);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of include method, of class TableRowFilterBook.
	 */
	@Test
	public void testInclude() {
		System.out.println("include");
		RowFilter.Entry entry = null;
		TableRowFilterBook instance = new TableRowFilterBook();
		boolean expResult = false;
		boolean result = instance.include(entry);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
