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

import info.movito.themoviedbapi.model.tv.TvSeries;
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
public class MyTvShowTest {

	/**
	 *
	 */
	public MyTvShowTest() {
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
	 * Test of getSerie method, of class MyTvShow.
	 */
	@Test
	public void testGetSerie() {
		System.out.println("getSerie");
		MyTvShow instance = null;
		TvSeries expResult = null;
		TvSeries result = instance.getSerie();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setSerie method, of class MyTvShow.
	 */
	@Test
	public void testSetSerie() {
		System.out.println("setSerie");
		TvSeries movieDb = null;
		MyTvShow instance = null;
		instance.setSerie(movieDb);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getId method, of class MyTvShow.
	 */
	@Test
	public void testGetId() {
		System.out.println("getId");
		MyTvShow instance = null;
		int expResult = 0;
		int result = instance.getId();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getHomepage method, of class MyTvShow.
	 */
	@Test
	public void testGetHomepage() {
		System.out.println("getHomepage");
		MyTvShow instance = null;
		String expResult = "";
		String result = instance.getHomepage();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class MyTvShow.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		MyTvShow instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setMyVideoInCache method, of class MyTvShow.
	 */
	@Test
	public void testSetMyVideoInCache() {
		System.out.println("setMyVideoInCache");
		MyTvShow instance = null;
		instance.setMyVideoInCache();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
