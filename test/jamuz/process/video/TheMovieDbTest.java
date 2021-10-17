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
public class TheMovieDbTest {
	
	/**
	 *
	 */
	public TheMovieDbTest() {
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
	 * Test of getAll method, of class TheMovieDb.
	 */
	@Test
	public void testGetAll() {
		System.out.println("getAll");
		TheMovieDb instance = null;
		instance.getAll();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getAllFromCache method, of class TheMovieDb.
	 */
	@Test
	public void testGetAllFromCache() {
		System.out.println("getAllFromCache");
		TheMovieDb instance = null;
		instance.getAllFromCache();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setRating method, of class TheMovieDb.
	 */
	@Test
	public void testSetRating() {
		System.out.println("setRating");
		int movieId = 0;
		int rating = 0;
		TheMovieDb instance = null;
		instance.setRating(movieId, rating);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setRatingTV method, of class TheMovieDb.
	 */
	@Test
	public void testSetRatingTV() {
		System.out.println("setRatingTV");
		int serieId = 0;
		int rating = 0;
		TheMovieDb instance = null;
		instance.setRatingTV(serieId, rating);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addFavorite method, of class TheMovieDb.
	 */
	@Test
	public void testAddFavorite() {
		System.out.println("addFavorite");
		int movieId = 0;
		TheMovieDb instance = null;
		instance.addFavorite(movieId);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addFavoriteTV method, of class TheMovieDb.
	 */
	@Test
	public void testAddFavoriteTV() {
		System.out.println("addFavoriteTV");
		int serieId = 0;
		TheMovieDb instance = null;
		instance.addFavoriteTV(serieId);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeFavorite method, of class TheMovieDb.
	 */
	@Test
	public void testRemoveFavorite() {
		System.out.println("removeFavorite");
		int movieId = 0;
		TheMovieDb instance = null;
		instance.removeFavorite(movieId);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeFavoriteTV method, of class TheMovieDb.
	 */
	@Test
	public void testRemoveFavoriteTV() {
		System.out.println("removeFavoriteTV");
		int movieId = 0;
		TheMovieDb instance = null;
		instance.removeFavoriteTV(movieId);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addToWatchList method, of class TheMovieDb.
	 */
	@Test
	public void testAddToWatchList() {
		System.out.println("addToWatchList");
		int movieId = 0;
		TheMovieDb instance = null;
		instance.addToWatchList(movieId);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addToWatchListTV method, of class TheMovieDb.
	 */
	@Test
	public void testAddToWatchListTV() {
		System.out.println("addToWatchListTV");
		int movieId = 0;
		TheMovieDb instance = null;
		instance.addToWatchListTV(movieId);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeFromWatchList method, of class TheMovieDb.
	 */
	@Test
	public void testRemoveFromWatchList() {
		System.out.println("removeFromWatchList");
		int movieId = 0;
		TheMovieDb instance = null;
		instance.removeFromWatchList(movieId);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeFromWatchListTV method, of class TheMovieDb.
	 */
	@Test
	public void testRemoveFromWatchListTV() {
		System.out.println("removeFromWatchListTV");
		int movieId = 0;
		TheMovieDb instance = null;
		instance.removeFromWatchListTV(movieId);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of searchFirst method, of class TheMovieDb.
	 */
	@Test
	public void testSearchFirst() {
		System.out.println("searchFirst");
		String name = "";
		int year = 0;
		TheMovieDb instance = null;
		MyMovieDb expResult = null;
		MyMovieDb result = instance.searchFirst(name, year);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of searchFirstTv method, of class TheMovieDb.
	 */
	@Test
	public void testSearchFirstTv() {
		System.out.println("searchFirstTv");
		String name = "";
		int year = 0;
		TheMovieDb instance = null;
		MyTvShow expResult = null;
		MyTvShow result = instance.searchFirstTv(name, year);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMyMovies method, of class TheMovieDb.
	 */
	@Test
	public void testGetMyMovies() {
		System.out.println("getMyMovies");
		TheMovieDb instance = null;
		Map<Integer, MyMovieDb> expResult = null;
		Map<Integer, MyMovieDb> result = instance.getMyMovies();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMyTvShows method, of class TheMovieDb.
	 */
	@Test
	public void testGetMyTvShows() {
		System.out.println("getMyTvShows");
		TheMovieDb instance = null;
		Map<Integer, MyTvShow> expResult = null;
		Map<Integer, MyTvShow> result = instance.getMyTvShows();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of get method, of class TheMovieDb.
	 */
	@Test
	public void testGet() {
		System.out.println("get");
		String name = "";
		int year = 0;
		TheMovieDb instance = null;
		MyMovieDb expResult = null;
		MyMovieDb result = instance.get(name, year);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTv method, of class TheMovieDb.
	 */
	@Test
	public void testGetTv() {
		System.out.println("getTv");
		String name = "";
		int year = 0;
		boolean search = false;
		TheMovieDb instance = null;
		MyTvShow expResult = null;
		MyTvShow result = instance.getTv(name, year, search);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
