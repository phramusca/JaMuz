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
package jamuz.process.sync;

import javax.swing.JFrame;
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
public class PanelSyncTest {
	
	public PanelSyncTest() {
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
	 * Test of initExtended method, of class PanelSync.
	 */
	@Test
	public void testInitExtended() {
		System.out.println("initExtended");
		PanelSync instance = new PanelSync(new JFrame());
		instance.initExtended();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setOptions method, of class PanelSync.
	 */
	@Test
	public void testSetOptions() {
		System.out.println("setOptions");
		PanelSync.setOptions();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addRowSync method, of class PanelSync.
	 */
	@Test
	public void testAddRowSync_String_int() {
		System.out.println("addRowSync");
		String file = "";
		int idIcon = 0;
		PanelSync.addRowSync(file, idIcon);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addRowSync method, of class PanelSync.
	 */
	@Test
	public void testAddRowSync_String_String() {
		System.out.println("addRowSync");
		String file = "";
		String msg = "";
		PanelSync.addRowSync(file, msg);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of enableSyncStartButton method, of class PanelSync.
	 */
	@Test
	public void testEnableSyncStartButton() {
		System.out.println("enableSyncStartButton");
		boolean enabled = false;
		PanelSync.enableSyncStartButton(enabled);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of enableSync method, of class PanelSync.
	 */
	@Test
	public void testEnableSync() {
		System.out.println("enableSync");
		boolean enable = false;
		PanelSync.enableSync(enable);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
