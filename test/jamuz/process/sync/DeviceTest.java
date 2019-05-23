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

import jamuz.Playlist;
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
public class DeviceTest {
	
	public DeviceTest() {
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
	 * Test of getId method, of class Device.
	 */
	@Test
	public void testGetId() {
		System.out.println("getId");
		Device instance = new Device();
		int expResult = 0;
		int result = instance.getId();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getName method, of class Device.
	 */
	@Test
	public void testGetName() {
		System.out.println("getName");
		Device instance = new Device();
		String expResult = "";
		String result = instance.getName();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setName method, of class Device.
	 */
	@Test
	public void testSetName() {
		System.out.println("setName");
		String name = "";
		Device instance = new Device();
		instance.setName(name);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getSource method, of class Device.
	 */
	@Test
	public void testGetSource() {
		System.out.println("getSource");
		Device instance = new Device();
		String expResult = "";
		String result = instance.getSource();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setSource method, of class Device.
	 */
	@Test
	public void testSetSource() {
		System.out.println("setSource");
		String source = "";
		Device instance = new Device();
		instance.setSource(source);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDestination method, of class Device.
	 */
	@Test
	public void testGetDestination() {
		System.out.println("getDestination");
		Device instance = new Device();
		String expResult = "";
		String result = instance.getDestination();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setDestination method, of class Device.
	 */
	@Test
	public void testSetDestination() {
		System.out.println("setDestination");
		String destination = "";
		Device instance = new Device();
		instance.setDestination(destination);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getIdPlaylist method, of class Device.
	 */
	@Test
	public void testGetIdPlaylist() {
		System.out.println("getIdPlaylist");
		Device instance = new Device();
		int expResult = 0;
		int result = instance.getIdPlaylist();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setIdPlaylist method, of class Device.
	 */
	@Test
	public void testSetIdPlaylist() {
		System.out.println("setIdPlaylist");
		int idPlaylist = 0;
		Device instance = new Device();
		instance.setIdPlaylist(idPlaylist);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMachineName method, of class Device.
	 */
	@Test
	public void testGetMachineName() {
		System.out.println("getMachineName");
		Device instance = new Device();
		String expResult = "";
		String result = instance.getMachineName();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPlaylist method, of class Device.
	 */
	@Test
	public void testGetPlaylist() {
		System.out.println("getPlaylist");
		Device instance = new Device();
		Playlist expResult = null;
		Playlist result = instance.getPlaylist();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class Device.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		Device instance = new Device();
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isHidden method, of class Device.
	 */
	@Test
	public void testIsHidden() {
		System.out.println("isHidden");
		Device instance = new Device();
		boolean expResult = false;
		boolean result = instance.isHidden();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
