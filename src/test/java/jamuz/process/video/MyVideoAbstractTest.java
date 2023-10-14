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
public class MyVideoAbstractTest {

	/**
	 *
	 */
	public MyVideoAbstractTest() {
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
	 * Test of getId method, of class MyVideoAbstract.
	 */
	@Test
	public void testGetId() {
		System.out.println("getId");
		MyVideoAbstract instance = new MyVideoAbstractImpl();
		int expResult = 0;
		int result = instance.getId();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setMyVideoInCache method, of class MyVideoAbstract.
	 */
	@Test
	public void testSetMyVideoInCache() {
		System.out.println("setMyVideoInCache");
		MyVideoAbstract instance = new MyVideoAbstractImpl();
		instance.setMyVideoInCache();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getHomepage method, of class MyVideoAbstract.
	 */
	@Test
	public void testGetHomepage() {
		System.out.println("getHomepage");
		MyVideoAbstract instance = new MyVideoAbstractImpl();
		String expResult = "";
		String result = instance.getHomepage();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isIsFavorite method, of class MyVideoAbstract.
	 */
	@Test
	public void testIsIsFavorite() {
		System.out.println("isIsFavorite");
		MyVideoAbstract instance = new MyVideoAbstractImpl();
		boolean expResult = false;
		boolean result = instance.isIsFavorite();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isIsInWatchList method, of class MyVideoAbstract.
	 */
	@Test
	public void testIsIsInWatchList() {
		System.out.println("isIsInWatchList");
		MyVideoAbstract instance = new MyVideoAbstractImpl();
		boolean expResult = false;
		boolean result = instance.isIsInWatchList();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getUserRating method, of class MyVideoAbstract.
	 */
	@Test
	public void testGetUserRating() {
		System.out.println("getUserRating");
		MyVideoAbstract instance = new MyVideoAbstractImpl();
		VideoRating expResult = null;
		VideoRating result = instance.getUserRating();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setUserRating method, of class MyVideoAbstract.
	 */
	@Test
	public void testSetUserRating() {
		System.out.println("setUserRating");
		VideoRating userRating = null;
		MyVideoAbstract instance = new MyVideoAbstractImpl();
		instance.setUserRating(userRating);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setIsFavorite method, of class MyVideoAbstract.
	 */
	@Test
	public void testSetIsFavorite() {
		System.out.println("setIsFavorite");
		boolean isFavorite = false;
		MyVideoAbstract instance = new MyVideoAbstractImpl();
		instance.setIsFavorite(isFavorite);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setIsInWatchList method, of class MyVideoAbstract.
	 */
	@Test
	public void testSetIsInWatchList() {
		System.out.println("setIsInWatchList");
		boolean isInWatchList = false;
		MyVideoAbstract instance = new MyVideoAbstractImpl();
		instance.setIsInWatchList(isInWatchList);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getYear method, of class MyVideoAbstract.
	 */
	@Test
	public void testGetYear_0args() {
		System.out.println("getYear");
		MyVideoAbstract instance = new MyVideoAbstractImpl();
		int expResult = 0;
		int result = instance.getYear();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getYear method, of class MyVideoAbstract.
	 */
	@Test
	public void testGetYear_String() {
		System.out.println("getYear");
		String date = "";
		int expResult = 0;
		int result = MyVideoAbstract.getYear(date);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 *
	 */
	public class MyVideoAbstractImpl extends MyVideoAbstract {

		/**
		 *
		 * @return
		 */
		public int getId() {
			return 0;
		}

		/**
		 *
		 */
		public void setMyVideoInCache() {
		}

		/**
		 *
		 * @return
		 */
		public String getHomepage() {
			return "";
		}
	}

}
