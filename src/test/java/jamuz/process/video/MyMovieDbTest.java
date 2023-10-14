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

import info.movito.themoviedbapi.model.MovieDb;
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
public class MyMovieDbTest {

	/**
	 *
	 */
	public MyMovieDbTest() {
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
	 * Test of getMovieDb method, of class MyMovieDb.
	 */
	@Test
	public void testGetMovieDb() {
		System.out.println("getMovieDb");
		MyMovieDb instance = null;
		MovieDb expResult = null;
		MovieDb result = instance.getMovieDb();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setMovieDb method, of class MyMovieDb.
	 */
	@Test
	public void testSetMovieDb() {
		System.out.println("setMovieDb");
		MovieDb movieDb = null;
		MyMovieDb instance = null;
		instance.setMovieDb(movieDb);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getId method, of class MyMovieDb.
	 */
	@Test
	public void testGetId() {
		System.out.println("getId");
		MyMovieDb instance = null;
		int expResult = 0;
		int result = instance.getId();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getHomepage method, of class MyMovieDb.
	 */
	@Test
	public void testGetHomepage() {
		System.out.println("getHomepage");
		MyMovieDb instance = null;
		String expResult = "";
		String result = instance.getHomepage();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setMyVideoInCache method, of class MyMovieDb.
	 */
	@Test
	public void testSetMyVideoInCache() {
		System.out.println("setMyVideoInCache");
		MyMovieDb instance = null;
		instance.setMyVideoInCache();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class MyMovieDb.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		MyMovieDb instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
