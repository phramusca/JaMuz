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

import jamuz.gui.swing.ProgressBar;
import java.util.List;
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
public class ReleaseMatchTest {
	
	public ReleaseMatchTest() {
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
	 * Test of getDuplicates method, of class ReleaseMatch.
	 */
	@Test
	public void testGetDuplicates() {
		System.out.println("getDuplicates");
		ReleaseMatch instance = null;
		List<DuplicateInfo> expResult = null;
		List<DuplicateInfo> result = instance.getDuplicates();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isIsWarningDuplicate method, of class ReleaseMatch.
	 */
	@Test
	public void testIsIsWarningDuplicate() {
		System.out.println("isIsWarningDuplicate");
		ReleaseMatch instance = null;
		boolean expResult = false;
		boolean result = instance.isIsWarningDuplicate();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isIsErrorDuplicate method, of class ReleaseMatch.
	 */
	@Test
	public void testIsIsErrorDuplicate() {
		System.out.println("isIsErrorDuplicate");
		ReleaseMatch instance = null;
		boolean expResult = false;
		boolean result = instance.isIsErrorDuplicate();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTracks method, of class ReleaseMatch.
	 */
	@Test
	public void testGetTracks() {
		System.out.println("getTracks");
		ProgressBar progressBar = null;
		ReleaseMatch instance = null;
		List<ReleaseMatch.Track> expResult = null;
		List<ReleaseMatch.Track> result = instance.getTracks(progressBar);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getScore method, of class ReleaseMatch.
	 */
	@Test
	public void testGetScore() {
		System.out.println("getScore");
		ReleaseMatch instance = null;
		int expResult = 0;
		int result = instance.getScore();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class ReleaseMatch.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		ReleaseMatch instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of compareTo method, of class ReleaseMatch.
	 */
	@Test
	public void testCompareTo() {
		System.out.println("compareTo");
		Object o = null;
		ReleaseMatch instance = null;
		int expResult = 0;
		int result = instance.compareTo(o);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getArtist method, of class ReleaseMatch.
	 */
	@Test
	public void testGetArtist() {
		System.out.println("getArtist");
		ReleaseMatch instance = null;
		String expResult = "";
		String result = instance.getArtist();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getCountryId method, of class ReleaseMatch.
	 */
	@Test
	public void testGetCountryId() {
		System.out.println("getCountryId");
		ReleaseMatch instance = null;
		String expResult = "";
		String result = instance.getCountryId();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFormat method, of class ReleaseMatch.
	 */
	@Test
	public void testGetFormat() {
		System.out.println("getFormat");
		ReleaseMatch instance = null;
		String expResult = "";
		String result = instance.getFormat();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getId method, of class ReleaseMatch.
	 */
	@Test
	public void testGetId() {
		System.out.println("getId");
		ReleaseMatch instance = null;
		String expResult = "";
		String result = instance.getId();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getQuality method, of class ReleaseMatch.
	 */
	@Test
	public void testGetQuality() {
		System.out.println("getQuality");
		ReleaseMatch instance = null;
		String expResult = "";
		String result = instance.getQuality();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isOriginal method, of class ReleaseMatch.
	 */
	@Test
	public void testIsOriginal() {
		System.out.println("isOriginal");
		ReleaseMatch instance = null;
		boolean expResult = false;
		boolean result = instance.isOriginal();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTextLanguage method, of class ReleaseMatch.
	 */
	@Test
	public void testGetTextLanguage() {
		System.out.println("getTextLanguage");
		ReleaseMatch instance = null;
		String expResult = "";
		String result = instance.getTextLanguage();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTrackTotal method, of class ReleaseMatch.
	 */
	@Test
	public void testGetTrackTotal() {
		System.out.println("getTrackTotal");
		ReleaseMatch instance = null;
		int expResult = 0;
		int result = instance.getTrackTotal();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getYear method, of class ReleaseMatch.
	 */
	@Test
	public void testGetYear() {
		System.out.println("getYear");
		ReleaseMatch instance = null;
		String expResult = "";
		String result = instance.getYear();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getYearInt method, of class ReleaseMatch.
	 */
	@Test
	public void testGetYearInt() {
		System.out.println("getYearInt");
		ReleaseMatch instance = null;
		int expResult = 0;
		int result = instance.getYearInt();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getAlbum method, of class ReleaseMatch.
	 */
	@Test
	public void testGetAlbum() {
		System.out.println("getAlbum");
		ReleaseMatch instance = null;
		String expResult = "";
		String result = instance.getAlbum();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDiscTotal method, of class ReleaseMatch.
	 */
	@Test
	public void testGetDiscTotal() {
		System.out.println("getDiscTotal");
		ReleaseMatch instance = null;
		int expResult = 0;
		int result = instance.getDiscTotal();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of resetStatus method, of class ReleaseMatch.
	 */
	@Test
	public void testResetStatus() {
		System.out.println("resetStatus");
		ReleaseMatch instance = null;
		instance.resetStatus();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
