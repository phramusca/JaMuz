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

import jamuz.gui.swing.ProgressBar;
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
public class PanelDuplicateTest {
	
	public PanelDuplicateTest() {
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
	 * Test of init method, of class PanelDuplicate.
	 */
	@Test
	public void testInit_3args() {
		System.out.println("init");
		FolderInfo folderInfo = null;
		DuplicateInfo diToSelect = null;
		ICallBackDuplicatePanel callback = null;
		PanelDuplicate instance = new PanelDuplicate();
		instance.init(folderInfo, diToSelect, callback);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of reCheck method, of class PanelDuplicate.
	 */
	@Test
	public void testReCheck() {
		System.out.println("reCheck");
		FolderInfo folderInfo = null;
		ICallBackDuplicatePanel callback = null;
		PanelDuplicate instance = new PanelDuplicate();
		instance.reCheck(folderInfo, callback);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of init method, of class PanelDuplicate.
	 */
	@Test
	public void testInit_DuplicateInfo() {
		System.out.println("init");
		DuplicateInfo duplicateInfo = null;
		PanelDuplicate instance = new PanelDuplicate();
		instance.init(duplicateInfo);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getProgressBar method, of class PanelDuplicate.
	 */
	@Test
	public void testGetProgressBar() {
		System.out.println("getProgressBar");
		PanelDuplicate instance = new PanelDuplicate();
		ProgressBar expResult = null;
		ProgressBar result = instance.getProgressBar();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
