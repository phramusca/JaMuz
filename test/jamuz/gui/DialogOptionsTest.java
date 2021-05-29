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
package jamuz.gui;

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
public class DialogOptionsTest {
	
	public DialogOptionsTest() {
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
	 * Test of displayOptions method, of class DialogOptions.
	 */
	@Test
	public void testDisplayOptions() {
		System.out.println("displayOptions");
		DialogOptions.displayOptions();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayStatSources method, of class DialogOptions.
	 */
	@Test
	public void testDisplayStatSources() {
		System.out.println("displayStatSources");
		DialogOptions.displayStatSources();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayDevices method, of class DialogOptions.
	 */
	@Test
	public void testDisplayDevices() {
		System.out.println("displayDevices");
		DialogOptions.displayDevices();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getReturnStatus method, of class DialogOptions.
	 */
	@Test
	public void testGetReturnStatus() {
		System.out.println("getReturnStatus");
		DialogOptions instance = null;
		int expResult = 0;
		int result = instance.getReturnStatus();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of main method, of class DialogOptions.
	 */
	@Test
	public void testMain() {
		System.out.println("main");
		String machineName = "";
		DialogOptions.main(new JFrame(), machineName);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
