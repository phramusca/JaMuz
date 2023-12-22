/*
 * Copyright (C) 2023 phramusca <phramusca@gmail.com>
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
package jamuz.database;

import jamuz.remote.ClientInfo;
import java.util.LinkedHashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoClientTest {
	
	public DaoClientTest() {
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
	 * Test of lock method, of class DaoClient.
	 */
	@Test
	public void testLock() {
		System.out.println("lock");
		DaoClient instance = null;
		DaoClientWrite expResult = null;
		DaoClientWrite result = instance.lock();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of get method, of class DaoClient.
	 */
	@Test
	public void testGet_String() {
		System.out.println("get");
		String login = "";
		DaoClient instance = null;
		ClientInfo expResult = null;
		ClientInfo result = instance.get(login);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of get method, of class DaoClient.
	 */
	@Test
	public void testGet_LinkedHashMap() {
		System.out.println("get");
		LinkedHashMap<Integer, ClientInfo> clients = null;
		DaoClient instance = null;
		boolean expResult = false;
		boolean result = instance.get(clients);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
