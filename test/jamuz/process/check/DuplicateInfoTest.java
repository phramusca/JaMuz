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
public class DuplicateInfoTest {
	
	public DuplicateInfoTest() {
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
	 * Test of getFolderInfo method, of class DuplicateInfo.
	 */
	@Test
	public void testGetFolderInfo() {
		System.out.println("getFolderInfo");
		DuplicateInfo instance = null;
		FolderInfo expResult = null;
		FolderInfo result = instance.getFolderInfo();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getAlbum method, of class DuplicateInfo.
	 */
	@Test
	public void testGetAlbum() {
		System.out.println("getAlbum");
		DuplicateInfo instance = null;
		String expResult = "";
		String result = instance.getAlbum();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setAlbum method, of class DuplicateInfo.
	 */
	@Test
	public void testSetAlbum() {
		System.out.println("setAlbum");
		String album = "";
		DuplicateInfo instance = null;
		instance.setAlbum(album);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getAlbumArtist method, of class DuplicateInfo.
	 */
	@Test
	public void testGetAlbumArtist() {
		System.out.println("getAlbumArtist");
		DuplicateInfo instance = null;
		String expResult = "";
		String result = instance.getAlbumArtist();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setAlbumArtist method, of class DuplicateInfo.
	 */
	@Test
	public void testSetAlbumArtist() {
		System.out.println("setAlbumArtist");
		String artist = "";
		DuplicateInfo instance = null;
		instance.setAlbumArtist(artist);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRating method, of class DuplicateInfo.
	 */
	@Test
	public void testGetRating() {
		System.out.println("getRating");
		DuplicateInfo instance = null;
		double expResult = 0.0;
		double result = instance.getRating();
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setRating method, of class DuplicateInfo.
	 */
	@Test
	public void testSetRating() {
		System.out.println("setRating");
		double rating = 0.0;
		DuplicateInfo instance = null;
		instance.setRating(rating);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getCheckedFlag method, of class DuplicateInfo.
	 */
	@Test
	public void testGetCheckedFlag() {
		System.out.println("getCheckedFlag");
		DuplicateInfo instance = null;
		FolderInfo.CheckedFlag expResult = null;
		FolderInfo.CheckedFlag result = instance.getCheckedFlag();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setCheckedFlag method, of class DuplicateInfo.
	 */
	@Test
	public void testSetCheckedFlag() {
		System.out.println("setCheckedFlag");
		FolderInfo.CheckedFlag checkedFlag = null;
		DuplicateInfo instance = null;
		instance.setCheckedFlag(checkedFlag);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getErrorLevel method, of class DuplicateInfo.
	 */
	@Test
	public void testGetErrorLevel() {
		System.out.println("getErrorLevel");
		DuplicateInfo instance = null;
		int expResult = 0;
		int result = instance.getErrorLevel();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setErrorLevel method, of class DuplicateInfo.
	 */
	@Test
	public void testSetErrorLevel() {
		System.out.println("setErrorLevel");
		int errorLevel = 0;
		DuplicateInfo instance = null;
		instance.setErrorLevel(errorLevel);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDiscNo method, of class DuplicateInfo.
	 */
	@Test
	public void testGetDiscNo() {
		System.out.println("getDiscNo");
		DuplicateInfo instance = null;
		int expResult = 0;
		int result = instance.getDiscNo();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setDiscNo method, of class DuplicateInfo.
	 */
	@Test
	public void testSetDiscNo() {
		System.out.println("setDiscNo");
		int discNo = 0;
		DuplicateInfo instance = null;
		instance.setDiscNo(discNo);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDiscTotal method, of class DuplicateInfo.
	 */
	@Test
	public void testGetDiscTotal() {
		System.out.println("getDiscTotal");
		DuplicateInfo instance = null;
		int expResult = 0;
		int result = instance.getDiscTotal();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setDiscTotal method, of class DuplicateInfo.
	 */
	@Test
	public void testSetDiscTotal() {
		System.out.println("setDiscTotal");
		int discTotal = 0;
		DuplicateInfo instance = null;
		instance.setDiscTotal(discTotal);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class DuplicateInfo.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		DuplicateInfo instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
