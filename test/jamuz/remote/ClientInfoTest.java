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

import jamuz.Playlist;
import jamuz.gui.swing.ProgressBar;
import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
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
public class ClientInfoTest {
	
	public ClientInfoTest() {
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
	 * Test of getId method, of class ClientInfo.
	 */
	@Test
	public void testGetId() {
		System.out.println("getId");
		ClientInfo instance = null;
		int expResult = 0;
		int result = instance.getId();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getName method, of class ClientInfo.
	 */
	@Test
	public void testGetName() {
		System.out.println("getName");
		ClientInfo instance = null;
		String expResult = "";
		String result = instance.getName();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setName method, of class ClientInfo.
	 */
	@Test
	public void testSetName() {
		System.out.println("setName");
		String name = "";
		ClientInfo instance = null;
		instance.setName(name);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getStatus method, of class ClientInfo.
	 */
	@Test
	public void testGetStatus() {
		System.out.println("getStatus");
		ClientInfo instance = null;
		String expResult = "";
		String result = instance.getStatus();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setStatus method, of class ClientInfo.
	 */
	@Test
	public void testSetStatus() {
		System.out.println("setStatus");
		String status = "";
		ClientInfo instance = null;
		instance.setStatus(status);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getProgressBar method, of class ClientInfo.
	 */
	@Test
	public void testGetProgressBar() {
		System.out.println("getProgressBar");
		ClientInfo instance = null;
		ProgressBar expResult = null;
		ProgressBar result = instance.getProgressBar();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLogin method, of class ClientInfo.
	 */
	@Test
	public void testGetLogin() {
		System.out.println("getLogin");
		ClientInfo instance = null;
		String expResult = "";
		String result = instance.getLogin();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPwd method, of class ClientInfo.
	 */
	@Test
	public void testGetPwd() {
		System.out.println("getPwd");
		ClientInfo instance = null;
		String expResult = "";
		String result = instance.getPwd();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setPwd method, of class ClientInfo.
	 */
	@Test
	public void testSetPwd() {
		System.out.println("setPwd");
		String pwd = "";
		ClientInfo instance = null;
		instance.setPwd(pwd);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPlaylist method, of class ClientInfo.
	 */
	@Test
	public void testGetPlaylist() {
		System.out.println("getPlaylist");
		ClientInfo instance = null;
		Playlist expResult = null;
		Playlist result = instance.getPlaylist();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class ClientInfo.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		ClientInfo instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRootPath method, of class ClientInfo.
	 */
	@Test
	public void testGetRootPath() {
		System.out.println("getRootPath");
		ClientInfo instance = null;
		String expResult = "";
		String result = instance.getRootPath();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDevice method, of class ClientInfo.
	 */
	@Test
	public void testGetDevice() {
		System.out.println("getDevice");
		ClientInfo instance = null;
		Device expResult = null;
		Device result = instance.getDevice();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setDevice method, of class ClientInfo.
	 */
	@Test
	public void testSetDevice() {
		System.out.println("setDevice");
		Device device = null;
		ClientInfo instance = null;
		instance.setDevice(device);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getStatSource method, of class ClientInfo.
	 */
	@Test
	public void testGetStatSource() {
		System.out.println("getStatSource");
		ClientInfo instance = null;
		StatSource expResult = null;
		StatSource result = instance.getStatSource();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setStatSource method, of class ClientInfo.
	 */
	@Test
	public void testSetStatSource() {
		System.out.println("setStatSource");
		StatSource statSource = null;
		ClientInfo instance = null;
		instance.setStatSource(statSource);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of enable method, of class ClientInfo.
	 */
	@Test
	public void testEnable() {
		System.out.println("enable");
		boolean enable = false;
		ClientInfo instance = null;
		instance.enable(enable);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isEnabled method, of class ClientInfo.
	 */
	@Test
	public void testIsEnabled() {
		System.out.println("isEnabled");
		ClientInfo instance = null;
		boolean expResult = false;
		boolean result = instance.isEnabled();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setId method, of class ClientInfo.
	 */
	@Test
	public void testSetId() {
		System.out.println("setId");
		int id = 0;
		ClientInfo instance = null;
		instance.setId(id);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
