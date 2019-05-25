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
package jamuz.process.check;

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
public class ICallBackDuplicateDialogTest {
	
	public ICallBackDuplicateDialogTest() {
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
	 * Test of notAduplicate method, of class ICallBackDuplicateDialog.
	 */
	@Test
	public void testNotAduplicate() {
		System.out.println("notAduplicate");
		ICallBackDuplicateDialog instance = new ICallBackDuplicateDialogImpl();
		instance.notAduplicate();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of delete method, of class ICallBackDuplicateDialog.
	 */
	@Test
	public void testDelete() {
		System.out.println("delete");
		ICallBackDuplicateDialog instance = new ICallBackDuplicateDialogImpl();
		instance.delete();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	public class ICallBackDuplicateDialogImpl implements ICallBackDuplicateDialog {

		public void notAduplicate() {
		}

		public void delete() {
		}
	}
	
}
