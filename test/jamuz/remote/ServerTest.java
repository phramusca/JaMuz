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
import java.util.Map;
import org.json.simple.JSONObject;
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
public class ServerTest {
	
	public ServerTest() {
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
	 * Test of connect method, of class Server.
	 */
	@Test
	public void testConnect() {
		System.out.println("connect");
		Server instance = null;
		boolean expResult = false;
		boolean result = instance.connect();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setPort method, of class Server.
	 */
	@Test
	public void testSetPort() {
		System.out.println("setPort");
		int port = 0;
		Server instance = null;
		instance.setPort(port);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of close method, of class Server.
	 */
	@Test
	public void testClose() {
		System.out.println("close");
		Server instance = null;
		instance.close();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of closeClients method, of class Server.
	 */
	@Test
	public void testCloseClients() {
		System.out.println("closeClients");
		Server instance = null;
		instance.closeClients();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPort method, of class Server.
	 */
	@Test
	public void testGetPort() {
		System.out.println("getPort");
		Server instance = null;
		int expResult = 0;
		int result = instance.getPort();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of sendCover method, of class Server.
	 */
	@Test
	public void testSendCover() {
		System.out.println("sendCover");
		String clientId = "";
		FileInfoInt displayedFile = null;
		int maxWidth = 0;
		Server instance = null;
		instance.sendCover(clientId, displayedFile, maxWidth);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of sendFile method, of class Server.
	 */
	@Test
	public void testSendFile() {
		System.out.println("sendFile");
		String clientId = "";
		String login = "";
		int idFile = 0;
		Server instance = null;
		instance.sendFile(clientId, login, idFile);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of send method, of class Server.
	 */
	@Test
	public void testSend_Map_boolean() {
		System.out.println("send");
		Map jsonAsMap = null;
		boolean isRemote = false;
		Server instance = null;
		instance.send(jsonAsMap, isRemote);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isConnected method, of class Server.
	 */
	@Test
	public void testIsConnected() {
		System.out.println("isConnected");
		String clientId = "";
		Server instance = null;
		boolean expResult = false;
		boolean result = instance.isConnected(clientId);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of send method, of class Server.
	 */
	@Test
	public void testSend_String_JSONObject() {
		System.out.println("send");
		String clientId = "";
		JSONObject obj = null;
		Server instance = null;
		boolean expResult = false;
		boolean result = instance.send(clientId, obj);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of closeClient method, of class Server.
	 */
	@Test
	public void testCloseClient() {
		System.out.println("closeClient");
		String clientId = "";
		Server instance = null;
		instance.closeClient(clientId);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTableModel method, of class Server.
	 */
	@Test
	public void testGetTableModel() {
		System.out.println("getTableModel");
		Server instance = null;
		TableModelRemote expResult = null;
		TableModelRemote result = instance.getTableModel();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of fillClients method, of class Server.
	 */
	@Test
	public void testFillClients() {
		System.out.println("fillClients");
		Server instance = null;
		instance.fillClients();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
