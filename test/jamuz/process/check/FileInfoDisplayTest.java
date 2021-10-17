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
public class FileInfoDisplayTest {

	public FileInfoDisplayTest() {
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
	 * Test of setTrack method, of class FileInfoDisplay.
	 */
	@Test
	public void testSetTrack() {
		System.out.println("setTrack");
		ReleaseMatch.Track track = null;
		FileInfoDisplay instance = null;
		instance.setTrack(track);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of initDisplay method, of class FileInfoDisplay.
	 */
	@Test
	public void testInitDisplay() {
		System.out.println("initDisplay");
		FileInfoDisplay instance = null;
		instance.initDisplay();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setTrackNo method, of class FileInfoDisplay.
	 */
	@Test
	public void testSetTrackNo() {
		System.out.println("setTrackNo");
		int trackNo = 0;
		FileInfoDisplay instance = null;
		instance.setTrackNo(trackNo);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setTrackTotal method, of class FileInfoDisplay.
	 */
	@Test
	public void testSetTrackTotal() {
		System.out.println("setTrackTotal");
		int trackTotal = 0;
		FileInfoDisplay instance = null;
		instance.setTrackTotal(trackTotal);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setDiscNo method, of class FileInfoDisplay.
	 */
	@Test
	public void testSetDiscNo() {
		System.out.println("setDiscNo");
		int discNo = 0;
		FileInfoDisplay instance = null;
		instance.setDiscNo(discNo);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setDiscTotal method, of class FileInfoDisplay.
	 */
	@Test
	public void testSetDiscTotal() {
		System.out.println("setDiscTotal");
		int discTotal = 0;
		FileInfoDisplay instance = null;
		instance.setDiscTotal(discTotal);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLengthDisplay method, of class FileInfoDisplay.
	 */
	@Test
	public void testGetLengthDisplay() {
		System.out.println("getLengthDisplay");
		FileInfoDisplay instance = null;
		String expResult = "";
		String result = instance.getLengthDisplay();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getSizeDisplay method, of class FileInfoDisplay.
	 */
	@Test
	public void testGetSizeDisplay() {
		System.out.println("getSizeDisplay");
		FileInfoDisplay instance = null;
		String expResult = "";
		String result = instance.getSizeDisplay();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFilename method, of class FileInfoDisplay.
	 */
	@Test
	public void testGetFilename() {
		System.out.println("getFilename");
		FileInfoDisplay instance = null;
		String expResult = "";
		String result = instance.getFilename();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getComment method, of class FileInfoDisplay.
	 */
	@Test
	public void testGetComment() {
		System.out.println("getComment");
		FileInfoDisplay instance = null;
		String expResult = "";
		String result = instance.getComment();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getBPM method, of class FileInfoDisplay.
	 */
	@Test
	public void testGetBPM() {
		System.out.println("getBPM");
		FileInfoDisplay instance = null;
		float expResult = 0.0F;
		float result = instance.getBPM();
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setArtist method, of class FileInfoDisplay.
	 */
	@Test
	public void testSetArtist() {
		System.out.println("setArtist");
		String artist = "";
		FileInfoDisplay instance = null;
		instance.setArtist(artist);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setTitle method, of class FileInfoDisplay.
	 */
	@Test
	public void testSetTitle() {
		System.out.println("setTitle");
		String title = "";
		FileInfoDisplay instance = null;
		instance.setTitle(title);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setAlbum method, of class FileInfoDisplay.
	 */
	@Test
	public void testSetAlbum() {
		System.out.println("setAlbum");
		String album = "";
		FileInfoDisplay instance = null;
		instance.setAlbum(album);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setAlbumArtist method, of class FileInfoDisplay.
	 */
	@Test
	public void testSetAlbumArtist() {
		System.out.println("setAlbumArtist");
		String albumArtist = "";
		FileInfoDisplay instance = null;
		instance.setAlbumArtist(albumArtist);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setYear method, of class FileInfoDisplay.
	 */
	@Test
	public void testSetYear() {
		System.out.println("setYear");
		String year = "";
		FileInfoDisplay instance = null;
		instance.setYear(year);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setBPM method, of class FileInfoDisplay.
	 */
	@Test
	public void testSetBPM() {
		System.out.println("setBPM");
		float BPM = 0.0F;
		FileInfoDisplay instance = null;
		instance.setBPM(BPM);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setComment method, of class FileInfoDisplay.
	 */
	@Test
	public void testSetComment() {
		System.out.println("setComment");
		String comment = "";
		FileInfoDisplay instance = null;
		instance.setComment(comment);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of saveTags method, of class FileInfoDisplay.
	 */
	@Test
	public void testSaveTags() {
		System.out.println("saveTags");
		BufferedImage image = null;
		boolean deleteComment = false;
		FileInfoDisplay instance = null;
		boolean expResult = false;
		boolean result = instance.saveTags(image, deleteComment);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of clone method, of class FileInfoDisplay.
	 */
	@Test
	public void testClone() throws Exception {
		System.out.println("clone");
		FileInfoDisplay instance = null;
		FileInfoDisplay expResult = null;
		FileInfoDisplay result = instance.clone();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
