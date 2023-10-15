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
package jamuz.process.video;

import java.util.List;
import javax.swing.DefaultListModel;
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
public class PanelVideoTest {

	/**
	 *
	 */
	public PanelVideoTest() {
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
	 * Test of getModel method, of class PanelVideo.
	 */
	@Test
	public void testGetModel_List() {
		System.out.println("getModel");
		List<String> list = null;
		DefaultListModel expResult = null;
		DefaultListModel result = PanelVideo.getModel(list);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getModel method, of class PanelVideo.
	 */
	@Test
	public void testGetModel_List_boolean() {
		System.out.println("getModel");
		List<String> list = null;
		boolean sort = false;
		DefaultListModel expResult = null;
		DefaultListModel result = PanelVideo.getModel(list, sort);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of diplayLength method, of class PanelVideo.
	 */
	@Test
	public void testDiplayLength() {
		System.out.println("diplayLength");
		PanelVideo.diplayLength();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of export method, of class PanelVideo.
	 */
	@Test
	public void testExport() {
		System.out.println("export");
		PanelVideo.export();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of enableProcess method, of class PanelVideo.
	 */
	@Test
	public void testEnableProcess() {
		System.out.println("enableProcess");
		boolean enable = false;
		PanelVideo.enableProcess(enable);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
