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

import jamuz.utils.SSH;
import java.util.ArrayList;
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
public class VideoMovieTest {

	/**
	 *
	 */
	public VideoMovieTest() {
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
	 * Test of getVideoSummary method, of class VideoMovie.
	 */
	@Test
	public void testGetVideoSummary() {
		System.out.println("getVideoSummary");
		VideoMovie instance = null;
		String expResult = "";
		String result = instance.getVideoSummary();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of moveFilesAndSrt method, of class VideoMovie.
	 */
	@Test
	public void testMoveFilesAndSrt() {
		System.out.println("moveFilesAndSrt");
		ProcessVideo.PathBuffer buffer = null;
		DbConnVideo conn = null;
		SSH myConn = null;
		VideoMovie instance = null;
		instance.moveFilesAndSrt(buffer, conn, myConn);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isWatched method, of class VideoMovie.
	 */
	@Test
	public void testIsWatched() {
		System.out.println("isWatched");
		VideoMovie instance = null;
		boolean expResult = false;
		boolean result = instance.isWatched();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isLocal method, of class VideoMovie.
	 */
	@Test
	public void testIsLocal() {
		System.out.println("isLocal");
		VideoMovie instance = null;
		boolean expResult = false;
		boolean result = instance.isLocal();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setMyVideo method, of class VideoMovie.
	 */
	@Test
	public void testSetMyVideo() {
		System.out.println("setMyVideo");
		boolean search = false;
		VideoMovie instance = null;
		instance.setMyVideo(search);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setRating method, of class VideoMovie.
	 */
	@Test
	public void testSetRating() {
		System.out.println("setRating");
		VideoRating rating = null;
		VideoMovie instance = null;
		instance.setRating(rating);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addToWatchList method, of class VideoMovie.
	 */
	@Test
	public void testAddToWatchList() {
		System.out.println("addToWatchList");
		VideoMovie instance = null;
		instance.addToWatchList();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeFromWatchList method, of class VideoMovie.
	 */
	@Test
	public void testRemoveFromWatchList() {
		System.out.println("removeFromWatchList");
		VideoMovie instance = null;
		instance.removeFromWatchList();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addFavorite method, of class VideoMovie.
	 */
	@Test
	public void testAddFavorite() {
		System.out.println("addFavorite");
		VideoMovie instance = null;
		instance.addFavorite();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeFavorite method, of class VideoMovie.
	 */
	@Test
	public void testRemoveFavorite() {
		System.out.println("removeFavorite");
		VideoMovie instance = null;
		instance.removeFavorite();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRelativeFullPath method, of class VideoMovie.
	 */
	@Test
	public void testGetRelativeFullPath() {
		System.out.println("getRelativeFullPath");
		VideoMovie instance = null;
		String expResult = "";
		String result = instance.getRelativeFullPath();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isMovie method, of class VideoMovie.
	 */
	@Test
	public void testIsMovie() {
		System.out.println("isMovie");
		VideoMovie instance = null;
		boolean expResult = false;
		boolean result = instance.isMovie();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFilesToCleanup method, of class VideoMovie.
	 */
	@Test
	public void testGetFilesToCleanup() {
		System.out.println("getFilesToCleanup");
		int nbSeasonToKeep = 0;
		int nbEpisodeToKeep = 0;
		boolean keepEnded = false;
		boolean keepCanceled = false;
		VideoMovie instance = null;
		ArrayList<FileInfoVideo> expResult = null;
		ArrayList<FileInfoVideo> result = instance.getFilesToCleanup(nbSeasonToKeep, nbEpisodeToKeep, keepEnded, keepCanceled);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
