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
public class CheckDisplayTest {
	
	public CheckDisplayTest() {
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
	 * Test of displayMatches method, of class CheckDisplay.
	 */
	@Test
	public void testDisplayMatches() {
		System.out.println("displayMatches");
		CheckDisplay instance = null;
		instance.displayMatches();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayMatchTracks method, of class CheckDisplay.
	 */
	@Test
	public void testDisplayMatchTracks_0args() {
		System.out.println("displayMatchTracks");
		CheckDisplay instance = null;
		instance.displayMatchTracks();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayMatchTracks method, of class CheckDisplay.
	 */
	@Test
	public void testDisplayMatchTracks_int() {
		System.out.println("displayMatchTracks");
		int colId = 0;
		CheckDisplay instance = null;
		instance.displayMatchTracks(colId);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayMatch method, of class CheckDisplay.
	 */
	@Test
	public void testDisplayMatch() {
		System.out.println("displayMatch");
		int matchId = 0;
		DuplicateInfo diToSelect = null;
		CheckDisplay instance = null;
		instance.displayMatch(matchId, diToSelect);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayFolder method, of class CheckDisplay.
	 */
	@Test
	public void testDisplayFolder() {
		System.out.println("displayFolder");
		CheckDisplay instance = null;
		instance.displayFolder();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of unSelectCheckBoxes method, of class CheckDisplay.
	 */
	@Test
	public void testUnSelectCheckBoxes() {
		System.out.println("unSelectCheckBoxes");
		CheckDisplay instance = null;
		instance.unSelectCheckBoxes();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayMatchColumns method, of class CheckDisplay.
	 */
	@Test
	public void testDisplayMatchColumns() {
		System.out.println("displayMatchColumns");
		Map<String, FolderInfoResult> results = null;
		CheckDisplay instance = null;
		instance.displayMatchColumns(results);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
