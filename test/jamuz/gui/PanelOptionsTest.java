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
package jamuz.gui;

import javax.swing.JFrame;
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
public class PanelOptionsTest {
	
	public PanelOptionsTest() {
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
	 * Test of initExtended method, of class PanelOptions.
	 */
	@Test
	public void testInitExtended() {
		System.out.println("initExtended");
		PanelOptions instance = new PanelOptions(new JFrame());
		instance.initExtended();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of fillMachineList method, of class PanelOptions.
	 */
	@Test
	public void testFillMachineList() {
		System.out.println("fillMachineList");
		PanelOptions.fillMachineList();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of refreshListTagsModel method, of class PanelOptions.
	 */
	@Test
	public void testRefreshListTagsModel() {
		System.out.println("refreshListTagsModel");
		PanelOptions.refreshListTagsModel();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
