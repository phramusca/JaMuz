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
package jamuz.process.check;

import java.awt.Color;
import javax.swing.JFrame;
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
public class DialogCheckTest {
	
	public DialogCheckTest() {
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
	 * Test of main method, of class DialogCheck.
	 */
	@Test
	public void testMain() {
		System.out.println("main");
		FolderInfo folder = null;
		ICallBackCheckPanel callback = null;
		DialogCheck.main(new JFrame(), folder, callback);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of applyPattern method, of class DialogCheck.
	 */
	@Test
	public void testApplyPattern() {
		System.out.println("applyPattern");
		String pattern = "";
		DialogCheck instance = null;
		instance.applyPattern(pattern);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of highlightGenre method, of class DialogCheck.
	 */
	@Test
	public void testHighlightGenre_boolean() {
		System.out.println("highlightGenre");
		boolean highlight = false;
		DialogCheck instance = null;
		instance.highlightGenre(highlight);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of highlightGenre method, of class DialogCheck.
	 */
	@Test
	public void testHighlightGenre_String_Color() {
		System.out.println("highlightGenre");
		String genre = "";
		Color selectedColor = null;
		DialogCheck instance = null;
		instance.highlightGenre(genre, selectedColor);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of clearCheckTab method, of class DialogCheck.
	 */
	@Test
	public void testClearCheckTab() {
		System.out.println("clearCheckTab");
		DialogCheck instance = null;
		instance.clearCheckTab();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
