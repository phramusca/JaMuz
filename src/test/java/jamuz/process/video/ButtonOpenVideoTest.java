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

import java.awt.Component;
import javax.swing.JTable;
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
public class ButtonOpenVideoTest {

	/**
	 *
	 */
	public ButtonOpenVideoTest() {
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
	 * Test of getTableCellEditorComponent method, of class ButtonOpenVideo.
	 */
	@Test
	public void testGetTableCellEditorComponent() {
		System.out.println("getTableCellEditorComponent");
		JTable table = null;
		Object value = null;
		boolean isSelected = false;
		int row = 0;
		int column = 0;
		ButtonOpenVideo instance = new ButtonOpenVideo();
		Component expResult = null;
		Component result = instance.getTableCellEditorComponent(table, value, isSelected, row, column);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getCellEditorValue method, of class ButtonOpenVideo.
	 */
	@Test
	public void testGetCellEditorValue() {
		System.out.println("getCellEditorValue");
		ButtonOpenVideo instance = new ButtonOpenVideo();
		Object expResult = null;
		Object result = instance.getCellEditorValue();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of stopCellEditing method, of class ButtonOpenVideo.
	 */
	@Test
	public void testStopCellEditing() {
		System.out.println("stopCellEditing");
		ButtonOpenVideo instance = new ButtonOpenVideo();
		boolean expResult = false;
		boolean result = instance.stopCellEditing();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of fireEditingStopped method, of class ButtonOpenVideo.
	 */
	@Test
	public void testFireEditingStopped() {
		System.out.println("fireEditingStopped");
		ButtonOpenVideo instance = new ButtonOpenVideo();
		instance.fireEditingStopped();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
