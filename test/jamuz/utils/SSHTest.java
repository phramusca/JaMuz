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
package jamuz.utils;

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
public class SSHTest {
	
	public SSHTest() {
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
	 * Test of connect method, of class SSH.
	 */
	@Test
	public void testConnect() {
		System.out.println("connect");
		SSH instance = null;
		boolean expResult = false;
		boolean result = instance.connect();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isConnected method, of class SSH.
	 */
	@Test
	public void testIsConnected() {
		System.out.println("isConnected");
		SSH instance = null;
		boolean expResult = false;
		boolean result = instance.isConnected();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of disconnect method, of class SSH.
	 */
	@Test
	public void testDisconnect() {
		System.out.println("disconnect");
		SSH instance = null;
		instance.disconnect();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of moveFile method, of class SSH.
	 */
	@Test
	public void testMoveFile() {
		System.out.println("moveFile");
		String source = "";
		String destination = "";
		SSH instance = null;
		boolean expResult = false;
		boolean result = instance.moveFile(source, destination);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of sendAndReceive method, of class SSH.
	 */
	@Test
	public void testSendAndReceive() {
		System.out.println("sendAndReceive");
		String myCmd = "";
		StringBuilder result_2 = null;
		SSH instance = null;
		int expResult = 0;
		int result = instance.sendAndReceive(myCmd, result_2);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
