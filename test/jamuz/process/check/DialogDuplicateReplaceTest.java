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

import java.awt.Dimension;
import java.awt.Point;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class DialogDuplicateReplaceTest {
	
	public DialogDuplicateReplaceTest() {
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
	 * Test of displayMatchTracks method, of class DialogDuplicateReplace.
	 */
	@Test
	public void testDisplayMatchTracks_0args() {
		System.out.println("displayMatchTracks");
		DialogDuplicateReplace instance = null;
		instance.displayMatchTracks();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayMatchTracks method, of class DialogDuplicateReplace.
	 */
	@Test
	public void testDisplayMatchTracks_int() {
		System.out.println("displayMatchTracks");
		int colId = 0;
		DialogDuplicateReplace instance = null;
		instance.displayMatchTracks(colId);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of analyseMatchTracks method, of class DialogDuplicateReplace.
	 */
	@Test
	public void testAnalyseMatchTracks_0args() {
		System.out.println("analyseMatchTracks");
		DialogDuplicateReplace instance = null;
		instance.analyseMatchTracks();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getField method, of class DialogDuplicateReplace.
	 */
	@Test
	public void testGetField() {
		System.out.println("getField");
		int colId = 0;
		String expResult = "";
		String result = DialogDuplicateReplace.getField(colId);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of analyseMatchTracks method, of class DialogDuplicateReplace.
	 */
	@Test
	public void testAnalyseMatchTracks_int() {
		System.out.println("analyseMatchTracks");
		int colId = 0;
		DialogDuplicateReplace instance = null;
		instance.analyseMatchTracks(colId);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of main method, of class DialogDuplicateReplace.
	 */
	@Test
	public void testMain() {
		System.out.println("main");
		Dimension parentSize = null;
		Point parentLocation = null;
		FolderInfo folder = null;
		DuplicateInfo duplicateInfo = null;
		ICallBackReplace callback = null;
		DialogDuplicateReplace.main(parentSize, parentLocation, folder, duplicateInfo, callback);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
