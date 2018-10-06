/*
 * Copyright (C) 2018 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class StringManagerTest {
	
	public StringManagerTest() {
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
	 * Test of removeIllegal method, of class StringManager.
	 */
	@Test
	public void testRemoveIllegal() {
		System.out.println("removeIllegal");
		String str = "";
		String expResult = "";
		String result = StringManager.removeIllegal(str);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getNullableText method, of class StringManager.
	 */
	@Test
	public void testGetNullableText() {
		System.out.println("getNullableText");
		String text = "";
		String expResult = "";
		String result = StringManager.getNullableText(text);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of humanReadableByteCount method, of class StringManager.
	 */
	@Test
	public void testHumanReadableByteCount() {
		System.out.println("humanReadableByteCount");
		long bytes = 0L;
		boolean si = false;
		String expResult = "";
		String result = StringManager.humanReadableByteCount(bytes, si);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of secondsToMMSS method, of class StringManager.
	 */
	@Test
	public void testSecondsToMMSS() {
		System.out.println("secondsToMMSS");
		int seconds = 0;
		String expResult = "";
		String result = StringManager.secondsToMMSS(seconds);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of secondsToHHMM method, of class StringManager.
	 */
	@Test
	public void testSecondsToHHMM() {
		System.out.println("secondsToHHMM");
		int seconds = 0;
		String expResult = "";
		String result = StringManager.secondsToHHMM(seconds);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of humanReadableSeconds method, of class StringManager.
	 */
	@Test
	public void testHumanReadableSeconds() {
		System.out.println("humanReadableSeconds");
		long seconds = 0L;
		String expResult = "";
		String result = StringManager.humanReadableSeconds(seconds);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of parseSlashList method, of class StringManager.
	 */
	@Test
	public void testParseSlashList() {
		System.out.println("parseSlashList");
		String string = "";
		List<String> expResult = null;
		List<String> result = StringManager.parseSlashList(string);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of Left method, of class StringManager.
	 */
	@Test
	public void testLeft() {
		System.out.println("Left");
		String text = "";
		int length = 0;
		String expResult = "";
		String result = StringManager.Left(text, length);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of Right method, of class StringManager.
	 */
	@Test
	public void testRight() {
		System.out.println("Right");
		String text = "";
		int length = 0;
		String expResult = "";
		String result = StringManager.Right(text, length);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of Mid method, of class StringManager.
	 */
	@Test
	public void testMid_3args() {
		System.out.println("Mid");
		String text = "";
		int start = 0;
		int end = 0;
		String expResult = "";
		String result = StringManager.Mid(text, start, end);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of Mid method, of class StringManager.
	 */
	@Test
	public void testMid_String_int() {
		System.out.println("Mid");
		String text = "";
		int start = 0;
		String expResult = "";
		String result = StringManager.Mid(text, start);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
