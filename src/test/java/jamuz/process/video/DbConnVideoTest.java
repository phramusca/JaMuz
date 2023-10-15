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

import java.util.List;
import java.util.Map;
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
public class DbConnVideoTest {

	/**
	 *
	 */
	public DbConnVideoTest() {
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
	 * Test of getIdPath method, of class DbConnVideo.
	 */
	@Test
	public void testGetIdPath() {
		System.out.println("getIdPath");
		String strPath = "";
		DbConnVideo instance = null;
		int expResult = 0;
		int result = instance.getIdPath(strPath);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMovies method, of class DbConnVideo.
	 */
	@Test
	public void testGetMovies() {
		System.out.println("getMovies");
		List<VideoAbstract> myFileInfoList = null;
		String rootPath = "";
		DbConnVideo instance = null;
		boolean expResult = false;
		boolean result = instance.getMovies(myFileInfoList, rootPath);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTvShows method, of class DbConnVideo.
	 */
	@Test
	public void testGetTvShows() {
		System.out.println("getTvShows");
		List<VideoAbstract> myFileInfoList = null;
		DbConnVideo instance = null;
		boolean expResult = false;
		boolean result = instance.getTvShows(myFileInfoList);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTvShowsFromCache method, of class DbConnVideo.
	 */
	@Test
	public void testGetTvShowsFromCache() {
		System.out.println("getTvShowsFromCache");
		Map<Integer, MyTvShow> myTvShows = null;
		DbConnVideo instance = null;
		boolean expResult = false;
		boolean result = instance.getTvShowsFromCache(myTvShows);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setTvShowsInCache method, of class DbConnVideo.
	 */
	@Test
	public void testSetTvShowsInCache() {
		System.out.println("setTvShowsInCache");
		Map<Integer, MyTvShow> myTvShows = null;
		DbConnVideo instance = null;
		boolean expResult = false;
		boolean result = instance.setTvShowsInCache(myTvShows);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setTvShowInCache method, of class DbConnVideo.
	 */
	@Test
	public void testSetTvShowInCache() {
		System.out.println("setTvShowInCache");
		MyTvShow myTvShow = null;
		DbConnVideo instance = null;
		boolean expResult = false;
		boolean result = instance.setTvShowInCache(myTvShow);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMoviesFromCache method, of class DbConnVideo.
	 */
	@Test
	public void testGetMoviesFromCache() {
		System.out.println("getMoviesFromCache");
		Map<Integer, MyMovieDb> myMovies = null;
		DbConnVideo instance = null;
		boolean expResult = false;
		boolean result = instance.getMoviesFromCache(myMovies);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setMoviesInCache method, of class DbConnVideo.
	 */
	@Test
	public void testSetMoviesInCache() {
		System.out.println("setMoviesInCache");
		Map<Integer, MyMovieDb> myMovies = null;
		DbConnVideo instance = null;
		boolean expResult = false;
		boolean result = instance.setMoviesInCache(myMovies);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setMovieInCache method, of class DbConnVideo.
	 */
	@Test
	public void testSetMovieInCache() {
		System.out.println("setMovieInCache");
		MyMovieDb myMovie = null;
		DbConnVideo instance = null;
		boolean expResult = false;
		boolean result = instance.setMovieInCache(myMovie);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getBytesFromCache method, of class DbConnVideo.
	 */
	@Test
	public void testGetBytesFromCache() {
		System.out.println("getBytesFromCache");
		Map objects = null;
		String table = "";
		String idName = "";
		DbConnVideo instance = null;
		boolean expResult = false;
		boolean result = instance.getBytesFromCache(objects, table, idName);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setBytesInCache method, of class DbConnVideo.
	 */
	@Test
	public void testSetBytesInCache() {
		System.out.println("setBytesInCache");
		Map objects = null;
		String table = "";
		String idName = "";
		DbConnVideo instance = null;
		boolean expResult = false;
		boolean result = instance.setBytesInCache(objects, table, idName);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateFile method, of class DbConnVideo.
	 */
	@Test
	public void testUpdateFile() {
		System.out.println("updateFile");
		int idFile = 0;
		int newIdPath = 0;
		String newFilename = "";
		DbConnVideo instance = null;
		boolean expResult = false;
		boolean result = instance.updateFile(idFile, newIdPath, newFilename);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
