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
package jamuz.remote;

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
public class EmissionTest {

	/**
	 *
	 */
	public EmissionTest() {
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
	 * Test of send method, of class Emission.
	 */
	@Test
	public void testSend() {
		System.out.println("send");
		String msg = "";
		SocketEmission instance = null;
		boolean expResult = false;
		boolean result = instance.send(msg);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of sendFile method, of class Emission.
	 */
	@Test
	public void testSendFile() {
		System.out.println("sendFile");
		String file = "";
		SocketEmission instance = null;
		instance.sendFile(file);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of run method, of class Emission.
	 */
	@Test
	public void testRun() {
		System.out.println("run");
		SocketEmission instance = null;
		instance.start();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
