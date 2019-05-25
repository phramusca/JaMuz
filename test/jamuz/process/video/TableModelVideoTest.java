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
public class TableModelVideoTest {
	
	public TableModelVideoTest() {
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
	 * Test of isCellEditable method, of class TableModelVideo.
	 */
	@Test
	public void testIsCellEditable() {
		System.out.println("isCellEditable");
		int row = 0;
		int col = 0;
		TableModelVideo instance = new TableModelVideo();
		boolean expResult = false;
		boolean result = instance.isCellEditable(row, col);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isCellEnabled method, of class TableModelVideo.
	 */
	@Test
	public void testIsCellEnabled() {
		System.out.println("isCellEnabled");
		int row = 0;
		int col = 0;
		TableModelVideo instance = new TableModelVideo();
		boolean expResult = false;
		boolean result = instance.isCellEnabled(row, col);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFiles method, of class TableModelVideo.
	 */
	@Test
	public void testGetFiles() {
		System.out.println("getFiles");
		TableModelVideo instance = new TableModelVideo();
		List<VideoAbstract> expResult = null;
		List<VideoAbstract> result = instance.getFiles();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFile method, of class TableModelVideo.
	 */
	@Test
	public void testGetFile() {
		System.out.println("getFile");
		int index = 0;
		TableModelVideo instance = new TableModelVideo();
		VideoAbstract expResult = null;
		VideoAbstract result = instance.getFile(index);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLengthAll method, of class TableModelVideo.
	 */
	@Test
	public void testGetLengthAll() {
		System.out.println("getLengthAll");
		TableModelVideo instance = new TableModelVideo();
		long expResult = 0L;
		long result = instance.getLengthAll();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLengthSelected method, of class TableModelVideo.
	 */
	@Test
	public void testGetLengthSelected() {
		System.out.println("getLengthSelected");
		TableModelVideo instance = new TableModelVideo();
		long expResult = 0L;
		long result = instance.getLengthSelected();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getNbSelected method, of class TableModelVideo.
	 */
	@Test
	public void testGetNbSelected() {
		System.out.println("getNbSelected");
		TableModelVideo instance = new TableModelVideo();
		int expResult = 0;
		int result = instance.getNbSelected();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRowCount method, of class TableModelVideo.
	 */
	@Test
	public void testGetRowCount() {
		System.out.println("getRowCount");
		TableModelVideo instance = new TableModelVideo();
		int expResult = 0;
		int result = instance.getRowCount();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getValueAt method, of class TableModelVideo.
	 */
	@Test
	public void testGetValueAt() {
		System.out.println("getValueAt");
		int rowIndex = 0;
		int columnIndex = 0;
		TableModelVideo instance = new TableModelVideo();
		Object expResult = null;
		Object result = instance.getValueAt(rowIndex, columnIndex);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setValueAt method, of class TableModelVideo.
	 */
	@Test
	public void testSetValueAt() {
		System.out.println("setValueAt");
		Object value = null;
		int row = 0;
		int col = 0;
		TableModelVideo instance = new TableModelVideo();
		instance.setValueAt(value, row, col);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of select method, of class TableModelVideo.
	 */
	@Test
	public void testSelect() {
		System.out.println("select");
		VideoAbstract fileInfoVideo = null;
		boolean selected = false;
		TableModelVideo instance = new TableModelVideo();
		instance.select(fileInfoVideo, selected);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getColumnClass method, of class TableModelVideo.
	 */
	@Test
	public void testGetColumnClass() {
		System.out.println("getColumnClass");
		int col = 0;
		TableModelVideo instance = new TableModelVideo();
		Class expResult = null;
		Class result = instance.getColumnClass(col);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of clear method, of class TableModelVideo.
	 */
	@Test
	public void testClear() {
		System.out.println("clear");
		TableModelVideo instance = new TableModelVideo();
		instance.clear();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addRow method, of class TableModelVideo.
	 */
	@Test
	public void testAddRow() {
		System.out.println("addRow");
		VideoAbstract file = null;
		TableModelVideo instance = new TableModelVideo();
		instance.addRow(file);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeRow method, of class TableModelVideo.
	 */
	@Test
	public void testRemoveRow() {
		System.out.println("removeRow");
		VideoAbstract file = null;
		TableModelVideo instance = new TableModelVideo();
		instance.removeRow(file);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of loadThumbnails method, of class TableModelVideo.
	 */
	@Test
	public void testLoadThumbnails() {
		System.out.println("loadThumbnails");
		TableModelVideo instance = new TableModelVideo();
		instance.loadThumbnails();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
