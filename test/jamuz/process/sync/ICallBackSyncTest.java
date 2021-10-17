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
public class ICallBackSyncTest {

	/**
	 *
	 */
	public ICallBackSyncTest() {
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
	 * Test of refresh method, of class ICallBackSync.
	 */
	@Test
	public void testRefresh() {
		System.out.println("refresh");
		ICallBackSync instance = new ICallBackSyncImpl();
		instance.refresh();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of enable method, of class ICallBackSync.
	 */
	@Test
	public void testEnable() {
		System.out.println("enable");
		ICallBackSync instance = new ICallBackSyncImpl();
		instance.enable();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of enableButton method, of class ICallBackSync.
	 */
	@Test
	public void testEnableButton() {
		System.out.println("enableButton");
		boolean enable = false;
		ICallBackSync instance = new ICallBackSyncImpl();
		instance.enableButton(enable);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addRow method, of class ICallBackSync.
	 */
	@Test
	public void testAddRow_String_int() {
		System.out.println("addRow");
		String file = "";
		int idIcon = 0;
		ICallBackSync instance = new ICallBackSyncImpl();
		instance.addRow(file, idIcon);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addRow method, of class ICallBackSync.
	 */
	@Test
	public void testAddRow_String_String() {
		System.out.println("addRow");
		String file = "";
		String msg = "";
		ICallBackSync instance = new ICallBackSyncImpl();
		instance.addRow(file, msg);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 *
	 */
	public class ICallBackSyncImpl implements ICallBackSync {

		/**
		 *
		 */
		public void refresh() {
		}

		/**
		 *
		 */
		public void enable() {
		}

		/**
		 *
		 * @param enable
		 */
		public void enableButton(boolean enable) {
		}

		/**
		 *
		 * @param file
		 * @param idIcon
		 */
		public void addRow(String file, int idIcon) {
		}

		/**
		 *
		 * @param file
		 * @param msg
		 */
		public void addRow(String file, String msg) {
		}
	}

}
