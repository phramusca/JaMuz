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

import java.util.ArrayList;
import java.util.List;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
@RunWith(JUnitParamsRunner.class)
public class StringManagerTest {

	/**
	 *
	 */
	public StringManagerTest() {
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
	 * Test of removeIllegal method, of class StringManager.
	 */
	@Test
	public void testRemoveIllegal() {
		System.out.println("removeIllegal");
		String str = "Those\\chars/are:not\"allowed*as?a<file>name|or.a!path";
		String expResult = "Those_chars_are_not_allowed_as_a_file_name_or_a_path";
		String result = StringManager.removeIllegal(str);
		assertEquals(expResult, result);
	}

	/**
	 * Test of getNullableText method, of class StringManager.
	 */
	@Test
	public void testGetNullableText() {
		System.out.println("getNullableText");
		assertEquals("null", StringManager.getNullableText(null));
		assertEquals("notnull", StringManager.getNullableText("notnull"));
	}

	/**
	 * Test of humanReadableByteCount method, of class StringManager.
	 * @param expResult
	 * @param bytes
	 * @param si
	 */
	@Test
	@Parameters
	public void testHumanReadableByteCount(String expResult, long bytes, boolean si) {
		System.out.println("humanReadableByteCount("+bytes+","+si+") = \""+expResult+"\"");
		String result = StringManager.humanReadableByteCount(bytes, si);
		assertEquals(expResult, result);
	}
	private Object parametersForTestHumanReadableByteCount() {
        return new Object[]{
			new Object[]{"1,0 ko", 1000, true}, 
			new Object[]{"1000 o", 1000, false},
			new Object[]{"1,0 ko", 1024, true},
			new Object[]{"1,0 Kio", 1024, false},
			new Object[]{"100 o", 100, true},
			new Object[]{"100 o", 100, false},
			new Object[]{"845,7 Po", 845651320698469846L, true},
			new Object[]{"751,1 Pio", 845651320698469846L, false},
			new Object[]{"0 o", 0, true},
			new Object[]{"0 o", 0, false},
			new Object[]{"6 o", 6, true},
			new Object[]{"6 o", 6, false},
			new Object[]{"2,5 ko", 2463, true},
			new Object[]{"2,4 Kio", 2463, false},
			new Object[]{"12,4 ko", 12432, true},
			new Object[]{"12,1 Kio", 12432, false}
		};
	}

	/**
	 * Test of secondsToMMSS method, of class StringManager.
	 * @param expResult
	 * @param seconds
	 */
	@Test
	@Parameters
	public void testSecondsToMMSS(String expResult, int seconds) {
		System.out.println("secondsToMMSS("+seconds+") = \""+expResult+"\"");
		String result = StringManager.secondsToMMSS(seconds);
		assertEquals(expResult, result);
	}
	private Object parametersForTestSecondsToMMSS() {
        return new Object[]{
			new Object[]{"00:00", 0}, 
			new Object[]{"--:--", -6},
			new Object[]{"00:06", 6},
			new Object[]{"00:54", 54},
			new Object[]{"00:59", 59},
			new Object[]{"01:00", 60},
			new Object[]{"01:01", 61},
			new Object[]{"02:17", 137},
			new Object[]{"26141646:38", 1568498798}
		};
	}

	/**
	 * Test of secondsToHHMM method, of class StringManager.
	 * @param expResult
	 * @param seconds
	 */
	@Test
	@Parameters
	public void testSecondsToHHMM(String expResult, int seconds) {
		System.out.println("secondsToHHMM("+seconds+") = \""+expResult+"\"");
		String result = StringManager.secondsToHHMM(seconds);
		assertEquals(expResult, result);
	}
	private Object parametersForTestSecondsToHHMM() {
        return new Object[]{
			new Object[]{"00 h 00", 0}, 
			new Object[]{"--:--", -6},
			new Object[]{"00 h 00", 6},
			new Object[]{"00 h 00", 54},
			new Object[]{"00 h 00", 59},
			new Object[]{"00 h 01", 60},
			new Object[]{"00 h 01", 61},
			new Object[]{"00 h 02", 137},
			new Object[]{"435694 h 06", 1568498798}
		};
	}

	/**
	 * Test of humanReadableSeconds method, of class StringManager.
	 * @param expResult
	 * @param seconds
	 */
	@Test
	@Parameters
	public void testHumanReadableSeconds(String expResult, long seconds) {
		System.out.println("humanReadableSeconds("+seconds+") = \""+expResult+"\"");
		String result = StringManager.humanReadableSeconds(seconds);
		assertEquals(expResult, result);
	}
	private Object parametersForTestHumanReadableSeconds() {
        return new Object[]{
			new Object[]{"-", 0}, 
			new Object[]{"-", -6},
			new Object[]{"06s", 6},
			new Object[]{"54s", 54},
			new Object[]{"59s", 59},
			new Object[]{"01m", 60},
			new Object[]{"01m 01s", 61},
			new Object[]{"01h 02m 03s", 3723},
			new Object[]{"02m 17s", 137},
			new Object[]{"03h", 10800},
			new Object[]{"22h 06m 38s", 79598},
			new Object[]{"18153d 22h 06m 38s", 1568498798}
				// 1568498798 s = 18153 d (1568419200 s) + 79598 s
				// 79598 s      = 22 h    (79200 s)      + 398 s
				// 398 s        = 6 m     (360 s)        + 38 s
		};
	}

	/**
	 * Test of parseSlashList method, of class StringManager.
	 */
	@Test
	public void testParseSlashList() {
		System.out.println("parseSlashList");
		String string = "a/b / c/ d /e  /f/  g  /  h / i/j/k / l//m // n / o";
		List<String> expResult;
		expResult = new ArrayList<>();
		expResult.add("a/b");
		expResult.add("c/ d /e  /f/  g ");
		expResult.add(" h");
		expResult.add("i/j/k");
		expResult.add("l//m // n");
		expResult.add("o");
		List<String> result = StringManager.parseSlashList(string);
		assertEquals(expResult, result);
	}

	/**
	 * Test of Left method, of class StringManager.
	 */
	@Test
	public void testLeft() {
		System.out.println("Left");
		String text = "Left part is NOT on the right";
		int length = 9;
		String expResult = "Left part";
		String result = StringManager.Left(text, length);
		assertEquals(expResult, result);
	}

	/**
	 * Test of Right method, of class StringManager.
	 */
	@Test
	public void testRight() {
		System.out.println("Right");
		String text = "Left part is NOT on the right";
		int length = 9;
		String expResult = "the right";
		String result = StringManager.Right(text, length);
		assertEquals(expResult, result);
	}
}
