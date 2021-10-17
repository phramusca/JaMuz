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
package jamuz.process.video;

import java.io.File;
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
public class FileInfoVideoTest {
	
	public FileInfoVideoTest() {
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
	 * Test of isHD method, of class FileInfoVideo.
	 */
	@Test
	public void testIsHD() {
		System.out.println("isHD");
		FileInfoVideo instance = new FileInfoVideo();
		boolean expResult = false;
		boolean result = instance.isHD();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getVideoStreamDetails method, of class FileInfoVideo.
	 */
	@Test
	public void testGetVideoStreamDetails() {
		System.out.println("getVideoStreamDetails");
		FileInfoVideo instance = new FileInfoVideo();
		String expResult = "";
		String result = instance.getVideoStreamDetails();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getAudioStreamDetails method, of class FileInfoVideo.
	 */
	@Test
	public void testGetAudioStreamDetails() {
		System.out.println("getAudioStreamDetails");
		FileInfoVideo instance = new FileInfoVideo();
		String expResult = "";
		String result = instance.getAudioStreamDetails();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getSubtitlesStreamDetails method, of class FileInfoVideo.
	 */
	@Test
	public void testGetSubtitlesStreamDetails() {
		System.out.println("getSubtitlesStreamDetails");
		FileInfoVideo instance = new FileInfoVideo();
		String expResult = "";
		String result = instance.getSubtitlesStreamDetails();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getVideoFile method, of class FileInfoVideo.
	 */
	@Test
	public void testGetVideoFile() {
		System.out.println("getVideoFile");
		FileInfoVideo instance = new FileInfoVideo();
		File expResult = null;
		File result = instance.getVideoFile();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getVideoPath method, of class FileInfoVideo.
	 */
	@Test
	public void testGetVideoPath() {
		System.out.println("getVideoPath");
		FileInfoVideo instance = new FileInfoVideo();
		String expResult = "";
		String result = instance.getVideoPath();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLibraryPath method, of class FileInfoVideo.
	 */
	@Test
	public void testGetLibraryPath() {
		System.out.println("getLibraryPath");
		FileInfoVideo instance = new FileInfoVideo();
		String expResult = "";
		String result = instance.getLibraryPath();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTitle method, of class FileInfoVideo.
	 */
	@Test
	public void testGetTitle() {
		System.out.println("getTitle");
		FileInfoVideo instance = new FileInfoVideo();
		String expResult = "";
		String result = instance.getTitle();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFormattedEpisodeNumber method, of class FileInfoVideo.
	 */
	@Test
	public void testGetFormattedEpisodeNumber() {
		System.out.println("getFormattedEpisodeNumber");
		FileInfoVideo instance = new FileInfoVideo();
		String expResult = "";
		String result = instance.getFormattedEpisodeNumber();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
