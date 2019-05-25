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
package jamuz.gui;

import jamuz.FileInfoInt;
import java.util.ArrayList;
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
public class DialogTagTest {
	
	public DialogTagTest() {
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
	 * Test of main method, of class DialogTag.
	 */
	@Test
	public void testMain() {
		System.out.println("main");
		FileInfoInt file = null;
		DialogTag.main(file);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getHighlightedTags method, of class DialogTag.
	 */
	@Test
	public void testGetHighlightedTags() {
		System.out.println("getHighlightedTags");
		ArrayList<String> expResult = null;
		ArrayList<String> result = DialogTag.getHighlightedTags();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of highlightTags method, of class DialogTag.
	 */
	@Test
	public void testHighlightTags() {
		System.out.println("highlightTags");
		DialogTag.highlightTags();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of highlightTag method, of class DialogTag.
	 */
	@Test
	public void testHighlightTag() {
		System.out.println("highlightTag");
		String tag = "";
		boolean highlight = false;
		DialogTag.highlightTag(tag, highlight);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
