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
package jamuz.utils;

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
public class ClipboardTextTest {

	/**
	 *
	 */
	public ClipboardTextTest() {
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
	 * Test of lostOwnership method, of class ClipboardText.
	 */
	@Test
	public void testLostOwnership() {
		System.out.println("lostOwnership");
		//Not used, no need to test
	}

	/**
	 * Test of setClipboardContents AND getClipboardContents methods, of class
	 * ClipboardText.
	 */
	@Test
	public void testClipboardContents() {
		System.out.println("setClipboardContents");
		String aString = "the beautiful text I'm sending, wouah !";
		ClipboardText instance = new ClipboardText();
		instance.setClipboardContents(aString);
		String result = instance.getClipboardContents();
		assertEquals(aString, result);
	}

	/**
	 * Test of setClipboardContents method, of class ClipboardText.
	 */
	@Test
	public void testSetClipboardContents() {
		System.out.println("setClipboardContents");
		//Refer to testClipboardContents() above
	}

	/**
	 * Test of getClipboardContents method, of class ClipboardText.
	 */
	@Test
	public void testGetClipboardContents() {
		System.out.println("getClipboardContents");
		//Refer to testClipboardContents() above
	}

}
