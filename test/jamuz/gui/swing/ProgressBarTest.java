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
package jamuz.gui.swing;

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
public class ProgressBarTest {
	
	public ProgressBarTest() {
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
	 * Test of reset method, of class ProgressBar.
	 */
	@Test
	public void testReset() {
		System.out.println("reset");
		ProgressBar instance = new ProgressBar();
		instance.reset();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setup method, of class ProgressBar.
	 */
	@Test
	public void testSetup() {
		System.out.println("setup");
		int max = 0;
		ProgressBar instance = new ProgressBar();
		instance.setup(max);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setMsgMax method, of class ProgressBar.
	 */
	@Test
	public void testSetMsgMax() {
		System.out.println("setMsgMax");
		int msgMax = 0;
		ProgressBar instance = new ProgressBar();
		instance.setMsgMax(msgMax);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayAsPercent method, of class ProgressBar.
	 */
	@Test
	public void testDisplayAsPercent() {
		System.out.println("displayAsPercent");
		ProgressBar instance = new ProgressBar();
		instance.displayAsPercent();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of progress method, of class ProgressBar.
	 */
	@Test
	public void testProgress_String() {
		System.out.println("progress");
		String msg = "";
		ProgressBar instance = new ProgressBar();
		instance.progress(msg);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of progress method, of class ProgressBar.
	 */
	@Test
	public void testProgress_String_int() {
		System.out.println("progress");
		String msg = "";
		int index = 0;
		ProgressBar instance = new ProgressBar();
		instance.progress(msg, index);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setIndeterminate method, of class ProgressBar.
	 */
	@Test
	public void testSetIndeterminate() {
		System.out.println("setIndeterminate");
		String msg = "";
		ProgressBar instance = new ProgressBar();
		instance.setIndeterminate(msg);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
