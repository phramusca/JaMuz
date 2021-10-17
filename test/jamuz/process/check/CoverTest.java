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

import java.awt.image.BufferedImage;
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
public class CoverTest {
	
	/**
	 *
	 */
	public CoverTest() {
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
	 * Test of getImage method, of class Cover.
	 */
	@Test
	public void testGetImage() {
		System.out.println("getImage");
		Cover instance = null;
		BufferedImage expResult = null;
		BufferedImage result = instance.getImage();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setImage method, of class Cover.
	 */
	@Test
	public void testSetImage() {
		System.out.println("setImage");
		BufferedImage image = null;
		Cover instance = null;
		instance.setImage(image);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of readImages method, of class Cover.
	 */
	@Test
	public void testReadImages() {
		System.out.println("readImages");
		Cover instance = null;
		instance.readImages();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getCoverArtArchiveList method, of class Cover.
	 */
	@Test
	public void testGetCoverArtArchiveList() {
		System.out.println("getCoverArtArchiveList");
		Cover instance = null;
		List<Cover.MbImage> expResult = null;
		List<Cover.MbImage> result = instance.getCoverArtArchiveList();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of listCovertArtArchive method, of class Cover.
	 */
	@Test
	public void testListCovertArtArchive() {
		System.out.println("listCovertArtArchive");
		Cover instance = null;
		instance.listCovertArtArchive();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getHash method, of class Cover.
	 */
	@Test
	public void testGetHash() {
		System.out.println("getHash");
		Cover instance = null;
		String expResult = "";
		String result = instance.getHash();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getName method, of class Cover.
	 */
	@Test
	public void testGetName() {
		System.out.println("getName");
		Cover instance = null;
		String expResult = "";
		String result = instance.getName();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getType method, of class Cover.
	 */
	@Test
	public void testGetType() {
		System.out.println("getType");
		Cover instance = null;
		Cover.CoverType expResult = null;
		Cover.CoverType result = instance.getType();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getValue method, of class Cover.
	 */
	@Test
	public void testGetValue() {
		System.out.println("getValue");
		Cover instance = null;
		String expResult = "";
		String result = instance.getValue();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of compareTo method, of class Cover.
	 */
	@Test
	public void testCompareTo() {
		System.out.println("compareTo");
		Object obj = null;
		Cover instance = null;
		int expResult = 0;
		int result = instance.compareTo(obj);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of equals method, of class Cover.
	 */
	@Test
	public void testEquals() {
		System.out.println("equals");
		Object obj = null;
		Cover instance = null;
		boolean expResult = false;
		boolean result = instance.equals(obj);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of hashCode method, of class Cover.
	 */
	@Test
	public void testHashCode() {
		System.out.println("hashCode");
		Cover instance = null;
		int expResult = 0;
		int result = instance.hashCode();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getSizeHTML method, of class Cover.
	 */
	@Test
	public void testGetSizeHTML() {
		System.out.println("getSizeHTML");
		Cover instance = null;
		String expResult = "";
		String result = instance.getSizeHTML();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class Cover.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		Cover instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
