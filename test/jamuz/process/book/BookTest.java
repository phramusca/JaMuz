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

import jamuz.gui.swing.FileSizeComparable;
import java.util.List;
import javax.swing.ImageIcon;
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
public class BookTest {
	
	/**
	 *
	 */
	public BookTest() {
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
	 * Test of getFormat method, of class Book.
	 */
	@Test
	public void testGetFormat() {
		System.out.println("getFormat");
		Book instance = null;
		String expResult = "";
		String result = instance.getFormat();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getThumbnail method, of class Book.
	 */
	@Test
	public void testGetThumbnail() {
		System.out.println("getThumbnail");
		boolean readIfNotFound = false;
		Book instance = null;
		ImageIcon expResult = null;
		ImageIcon result = instance.getThumbnail(readIfNotFound);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTags method, of class Book.
	 */
	@Test
	public void testGetTags() {
		System.out.println("getTags");
		Book instance = null;
		List<String> expResult = null;
		List<String> result = instance.getTags();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isSelected method, of class Book.
	 */
	@Test
	public void testIsSelected() {
		System.out.println("isSelected");
		Book instance = null;
		boolean expResult = false;
		boolean result = instance.isSelected();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setSelected method, of class Book.
	 */
	@Test
	public void testSetSelected() {
		System.out.println("setSelected");
		boolean selected = false;
		Book instance = null;
		instance.setSelected(selected);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTitle method, of class Book.
	 */
	@Test
	public void testGetTitle() {
		System.out.println("getTitle");
		Book instance = null;
		String expResult = "";
		String result = instance.getTitle();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getSort method, of class Book.
	 */
	@Test
	public void testGetSort() {
		System.out.println("getSort");
		Book instance = null;
		String expResult = "";
		String result = instance.getSort();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPubdate method, of class Book.
	 */
	@Test
	public void testGetPubdate() {
		System.out.println("getPubdate");
		Book instance = null;
		String expResult = "";
		String result = instance.getPubdate();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getAuthor_sort method, of class Book.
	 */
	@Test
	public void testGetAuthor_sort() {
		System.out.println("getAuthor_sort");
		Book instance = null;
		String expResult = "";
		String result = instance.getAuthor_sort();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getAuthor method, of class Book.
	 */
	@Test
	public void testGetAuthor() {
		System.out.println("getAuthor");
		Book instance = null;
		String expResult = "";
		String result = instance.getAuthor();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getUuid method, of class Book.
	 */
	@Test
	public void testGetUuid() {
		System.out.println("getUuid");
		Book instance = null;
		String expResult = "";
		String result = instance.getUuid();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRating method, of class Book.
	 */
	@Test
	public void testGetRating() {
		System.out.println("getRating");
		Book instance = null;
		String expResult = "";
		String result = instance.getRating();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRelativeFullPath method, of class Book.
	 */
	@Test
	public void testGetRelativeFullPath() {
		System.out.println("getRelativeFullPath");
		Book instance = null;
		String expResult = "";
		String result = instance.getRelativeFullPath();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFullPath method, of class Book.
	 */
	@Test
	public void testGetFullPath() {
		System.out.println("getFullPath");
		Book instance = null;
		String expResult = "";
		String result = instance.getFullPath();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setLength method, of class Book.
	 */
	@Test
	public void testSetLength() {
		System.out.println("setLength");
		long length = 0L;
		Book instance = null;
		instance.setLength(length);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLength method, of class Book.
	 */
	@Test
	public void testGetLength() {
		System.out.println("getLength");
		Book instance = null;
		FileSizeComparable expResult = null;
		FileSizeComparable result = instance.getLength();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isLocal method, of class Book.
	 */
	@Test
	public void testIsLocal() {
		System.out.println("isLocal");
		Book instance = null;
		boolean expResult = false;
		boolean result = instance.isLocal();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class Book.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		Book instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of compareTo method, of class Book.
	 */
	@Test
	public void testCompareTo() {
		System.out.println("compareTo");
		Object o = null;
		Book instance = null;
		int expResult = 0;
		int result = instance.compareTo(o);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
