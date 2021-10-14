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
public class ProcessCheckTest {
	
	public ProcessCheckTest() {
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
	 * Test of setMaxActionQueueSize method, of class ProcessCheck.
	 */
	@Test
	public void testSetMaxActionQueueSize() {
		System.out.println("setMaxActionQueueSize");
		int maxActionQueueSize = 0;
		ProcessCheck instance = null;
		instance.setMaxActionQueueSize(maxActionQueueSize);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMaxActionQueueSize method, of class ProcessCheck.
	 */
	@Test
	public void testGetMaxActionQueueSize() {
		System.out.println("getMaxActionQueueSize");
		ProcessCheck instance = null;
		int expResult = 0;
		int result = instance.getMaxActionQueueSize();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDestinationLocation method, of class ProcessCheck.
	 */
	@Test
	public void testGetDestinationLocation() {
		System.out.println("getDestinationLocation");
		Location expResult = null;
		Location result = ProcessCheck.getDestinationLocation();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRootLocation method, of class ProcessCheck.
	 */
	@Test
	public void testGetRootLocation() {
		System.out.println("getRootLocation");
		Location expResult = null;
		Location result = ProcessCheck.getRootLocation();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getKoLocation method, of class ProcessCheck.
	 */
	@Test
	public void testGetKoLocation() {
		System.out.println("getKoLocation");
		Location expResult = null;
		Location result = ProcessCheck.getKoLocation();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getManualLocation method, of class ProcessCheck.
	 */
	@Test
	public void testGetManualLocation() {
		System.out.println("getManualLocation");
		Location expResult = null;
		Location result = ProcessCheck.getManualLocation();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of startCheck method, of class ProcessCheck.
	 */
	@Test
	public void testStartCheck() {
		System.out.println("startCheck");
		ProcessCheck.CheckType checkType = null;
		int idPath = 0;
		int nbAnalysis = 0;
		int nbScan = 0;
		ProcessCheck instance = null;
		instance.startCheck(checkType, idPath, nbAnalysis, nbScan);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of stopCheck method, of class ProcessCheck.
	 */
	@Test
	public void testStopCheck() {
		System.out.println("stopCheck");
		ProcessCheck instance = null;
		instance.stopCheck();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isCheckAlive method, of class ProcessCheck.
	 */
	@Test
	public void testIsCheckAlive() {
		System.out.println("isCheckAlive");
		ProcessCheck instance = null;
		boolean expResult = false;
		boolean result = instance.isCheckAlive();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayActionQueue method, of class ProcessCheck.
	 */
	@Test
	public void testDisplayActionQueue() {
		System.out.println("displayActionQueue");
		ProcessCheck instance = null;
		instance.displayActionQueue();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of startActions method, of class ProcessCheck.
	 */
	@Test
	public void testStartActions() {
		System.out.println("startActions");
		boolean doKO = false;
		boolean doWarning = false;
		boolean doManual = false;
		ProcessCheck instance = null;
		instance.startActions(doKO, doWarning, doManual);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of stopActions method, of class ProcessCheck.
	 */
	@Test
	public void testStopActions() {
		System.out.println("stopActions");
		ProcessCheck instance = null;
		instance.stopActions();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
