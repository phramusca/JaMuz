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

import jamuz.gui.swing.TriStateCheckBox;
import javax.swing.RowFilter;
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
public class TableRowFilterVideoTest {

	/**
	 *
	 */
	public TableRowFilterVideoTest() {
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
	 * Test of displaySelected method, of class TableRowFilterVideo.
	 */
	@Test
	public void testDisplaySelected() {
		System.out.println("displaySelected");
		TriStateCheckBox.State display = null;
		TableRowFilterVideo instance = new TableRowFilterVideo();
		instance.displaySelected(display);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayWatched method, of class TableRowFilterVideo.
	 */
	@Test
	public void testDisplayWatched() {
		System.out.println("displayWatched");
		TriStateCheckBox.State display = null;
		TableRowFilterVideo instance = new TableRowFilterVideo();
		instance.displayWatched(display);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayWatchList method, of class TableRowFilterVideo.
	 */
	@Test
	public void testDisplayWatchList() {
		System.out.println("displayWatchList");
		TriStateCheckBox.State display = null;
		TableRowFilterVideo instance = new TableRowFilterVideo();
		instance.displayWatchList(display);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayFavorite method, of class TableRowFilterVideo.
	 */
	@Test
	public void testDisplayFavorite() {
		System.out.println("displayFavorite");
		TriStateCheckBox.State display = null;
		TableRowFilterVideo instance = new TableRowFilterVideo();
		instance.displayFavorite(display);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayLocal method, of class TableRowFilterVideo.
	 */
	@Test
	public void testDisplayLocal() {
		System.out.println("displayLocal");
		TriStateCheckBox.State display = null;
		TableRowFilterVideo instance = new TableRowFilterVideo();
		instance.displayLocal(display);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayMovies method, of class TableRowFilterVideo.
	 */
	@Test
	public void testDisplayMovies() {
		System.out.println("displayMovies");
		TriStateCheckBox.State display = null;
		TableRowFilterVideo instance = new TableRowFilterVideo();
		instance.displayMovies(display);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayHD method, of class TableRowFilterVideo.
	 */
	@Test
	public void testDisplayHD() {
		System.out.println("displayHD");
		TriStateCheckBox.State display = null;
		TableRowFilterVideo instance = new TableRowFilterVideo();
		instance.displayHD(display);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayRated method, of class TableRowFilterVideo.
	 */
	@Test
	public void testDisplayRated() {
		System.out.println("displayRated");
		TriStateCheckBox.State display = null;
		TableRowFilterVideo instance = new TableRowFilterVideo();
		instance.displayRated(display);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayByMppaRating method, of class TableRowFilterVideo.
	 */
	@Test
	public void testDisplayByMppaRating() {
		System.out.println("displayByMppaRating");
		String mppaRating = "";
		TableRowFilterVideo instance = new TableRowFilterVideo();
		instance.displayByMppaRating(mppaRating);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayByRating method, of class TableRowFilterVideo.
	 */
	@Test
	public void testDisplayByRating() {
		System.out.println("displayByRating");
		String rating = "";
		TableRowFilterVideo instance = new TableRowFilterVideo();
		instance.displayByRating(rating);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayByGenre method, of class TableRowFilterVideo.
	 */
	@Test
	public void testDisplayByGenre() {
		System.out.println("displayByGenre");
		String genre = "";
		TableRowFilterVideo instance = new TableRowFilterVideo();
		instance.displayByGenre(genre);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of include method, of class TableRowFilterVideo.
	 */
	@Test
	public void testInclude() {
		System.out.println("include");
		RowFilter.Entry entry = null;
		TableRowFilterVideo instance = new TableRowFilterVideo();
		boolean expResult = false;
		boolean result = instance.include(entry);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
