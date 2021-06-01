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

import javax.swing.JFrame;
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
public class PanelCheckTest {
	
	public PanelCheckTest() {
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
	 * Test of setThreadPanels method, of class PanelCheck.
	 */
	@Test
	public void testSetThreadPanels() {
		System.out.println("setThreadPanels");
		ProcessCheck.CheckType checkType = null;
		PanelCheck.setThreadPanels(checkType);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of enableRowSorter method, of class PanelCheck.
	 */
	@Test
	public void testEnableRowSorter() {
		System.out.println("enableRowSorter");
		boolean enable = false;
		PanelCheck.enableRowSorter(enable);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addToActionQueue method, of class PanelCheck.
	 */
	@Test
	public void testAddToActionQueue() {
		System.out.println("addToActionQueue");
		FolderInfo folder = null;
		PanelCheck instance = new PanelCheck(new JFrame());
		instance.addToActionQueue(folder);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of stopActions method, of class PanelCheck.
	 */
	@Test
	public void testStopActions() {
		System.out.println("stopActions");
		boolean enableDoActions = false;
		PanelCheck.stopActions(enableDoActions);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of check method, of class PanelCheck.
	 */
	@Test
	public void testCheck() {
		System.out.println("check");
		int idPath = 0;
		PanelCheck.check(null, idPath);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setOptions method, of class PanelCheck.
	 */
	@Test
	public void testSetOptions() {
		System.out.println("setOptions");
		PanelCheck.setOptions();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of enableCheck method, of class PanelCheck.
	 */
	@Test
	public void testEnableCheck() {
		System.out.println("enableCheck");
		boolean enable = false;
		PanelCheck.enableCheck(enable);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of enableCheckButtons method, of class PanelCheck.
	 */
	@Test
	public void testEnableCheckButtons() {
		System.out.println("enableCheckButtons");
		boolean enabled = false;
		PanelCheck.enableCheckButtons(enabled);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of enableAbortButton method, of class PanelCheck.
	 */
	@Test
	public void testEnableAbortButton() {
		System.out.println("enableAbortButton");
		PanelCheck.enableAbortButton();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
