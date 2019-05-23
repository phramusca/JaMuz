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

import jamuz.FileInfo;
import jamuz.FileInfoInt;
import java.util.ArrayList;
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
public class PanelRemoteTest {
	
	public PanelRemoteTest() {
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
	 * Test of initExtended method, of class PanelRemote.
	 */
	@Test
	public void testInitExtended() {
		System.out.println("initExtended");
		ICallBackServer callback = null;
		PanelRemote instance = new PanelRemote();
		instance.initExtended(callback);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of send method, of class PanelRemote.
	 */
	@Test
	public void testSend_FileInfoInt() {
		System.out.println("send");
		FileInfoInt fileInfo = null;
		PanelRemote.send(fileInfo);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of sendCover method, of class PanelRemote.
	 */
	@Test
	public void testSendCover() {
		System.out.println("sendCover");
		String clientId = "";
		FileInfoInt displayedFile = null;
		int maxWidth = 0;
		PanelRemote.sendCover(clientId, displayedFile, maxWidth);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of send method, of class PanelRemote.
	 */
	@Test
	public void testSend_Map_boolean() {
		System.out.println("send");
		Map jsonAsMap = null;
		boolean isRemote = false;
		PanelRemote.send(jsonAsMap, isRemote);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of send method, of class PanelRemote.
	 */
	@Test
	public void testSend_String_JSONObject() {
		System.out.println("send");
		String clientId = "";
		JSONObject obj = null;
		boolean expResult = false;
		boolean result = PanelRemote.send(clientId, obj);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of send method, of class PanelRemote.
	 */
	@Test
	public void testSend_String_ArrayList() {
		System.out.println("send");
		String clientId = "";
		ArrayList<FileInfo> mergeListDbSelected = null;
		PanelRemote.send(clientId, mergeListDbSelected);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isConnected method, of class PanelRemote.
	 */
	@Test
	public void testIsConnected() {
		System.out.println("isConnected");
		String clientId = "";
		boolean expResult = false;
		boolean result = PanelRemote.isConnected(clientId);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of refreshList method, of class PanelRemote.
	 */
	@Test
	public void testRefreshList() {
		System.out.println("refreshList");
		PanelRemote.refreshList();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
