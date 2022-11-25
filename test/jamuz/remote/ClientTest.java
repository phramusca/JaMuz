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

import jamuz.FileInfoInt;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import org.json.simple.JSONObject;
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
public class ClientTest {

	public ClientTest() {
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
	 * Test of setPath method, of class Client.
	 */
	@Test
	public void testSetPath() {
		System.out.println("setPath");
		String locationWork = "";
		ClientSocket instance = null;
		instance.setPath(locationWork);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of login method, of class Client.
	 */
	@Test
	public void testLogin() {
		System.out.println("login");
		ClientSocket instance = null;
		boolean expResult = false;
		boolean result = instance.login();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of close method, of class Client.
	 */
	@Test
	public void testClose() {
		System.out.println("close");
		ClientSocket instance = null;
		instance.close();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of send method, of class Client.
	 */
	@Test
	public void testSend_String() {
		System.out.println("send");
		String msg = "";
		ClientSocket instance = null;
		instance.send(msg);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of send method, of class Client.
	 */
	@Test
	public void testSend_Map() {
		System.out.println("send");
		Map jsonAsMap = null;
		ClientSocket instance = null;
		instance.send(jsonAsMap);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of send method, of class Client.
	 */
	@Test
	public void testSend_JSONObject() {
		System.out.println("send");
		JSONObject obj = null;
		ClientSocket instance = null;
		instance.send(obj);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDatabase method, of class Client.
	 */
	@Test
	public void testGetDatabase() {
		System.out.println("getDatabase");
		ClientSocket instance = null;
		boolean expResult = false;
		boolean result = instance.getDatabase();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of sendCover method, of class Client.
	 */
	@Test
	public void testSendCover() {
		System.out.println("sendCover");
		FileInfoInt displayedFile = null;
		int maxWidth = 0;
		ClientSocket instance = null;
		boolean expResult = false;
		boolean result = instance.sendCover(displayedFile, maxWidth);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of sendFile method, of class Client.
	 */
	@Test
	public void testSendFile() {
		System.out.println("sendFile");
		FileInfoInt fileInfoInt = null;
		ClientSocket instance = null;
		boolean expResult = false;
		boolean result = instance.sendFile(fileInfoInt);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of copyStream method, of class Client.
	 */
	@Test
	public void testCopyStream() throws Exception {
		System.out.println("copyStream");
		InputStream input = null;
		OutputStream output = null;
		ClientSocket.copyStream(input, output);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class Client.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		ClientSocket instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getInfo method, of class Client.
	 */
	@Test
	public void testGetInfo() {
		System.out.println("getInfo");
		ClientSocket instance = null;
		ClientInfo expResult = null;
		ClientInfo result = instance.getInfo();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getClientId method, of class Client.
	 */
	@Test
	public void testGetClientId() {
		System.out.println("getClientId");
		ClientSocket instance = null;
		String expResult = "";
		String result = instance.getClientId();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
