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
public class ICallBackReceptionTest {

	/**
	 *
	 */
	public ICallBackReceptionTest() {
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
	 * Test of received method, of class ICallBackReception.
	 */
	@Test
	public void testReceived() {
		System.out.println("received");
		String clientId = "";
		String login = "";
		String msg = "";
		ICallBackReception instance = new ICallBackReceptionImpl();
		instance.received(clientId, login, msg);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of disconnected method, of class ICallBackReception.
	 */
	@Test
	public void testDisconnected() {
		System.out.println("disconnected");
		ClientInfo clientInfo = null;
		String clientId = "";
		ICallBackReception instance = new ICallBackReceptionImpl();
		instance.disconnected(clientInfo, clientId);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of connected method, of class ICallBackReception.
	 */
	@Test
	public void testConnected() {
		System.out.println("connected");
		Client client = null;
		ICallBackReception instance = new ICallBackReceptionImpl();
		instance.connected(client);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 *
	 */
	public class ICallBackReceptionImpl implements ICallBackReception {

		/**
		 *
		 * @param clientId
		 * @param login
		 * @param msg
		 */
		public void received(String clientId, String login, String msg) {
		}

		/**
		 *
		 * @param clientInfo
		 * @param clientId
		 */
		public void disconnected(ClientInfo clientInfo, String clientId) {
		}

		/**
		 *
		 * @param client
		 */
		public void connected(Client client) {
		}
	}

}
