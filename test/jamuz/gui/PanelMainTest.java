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

import jamuz.FileInfoInt;
import jamuz.gui.swing.TableColumnModel;
import jamuz.gui.swing.TableModel;
import java.util.List;
import javax.swing.ImageIcon;
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
public class PanelMainTest {

	/**
	 *
	 */
	public PanelMainTest() {
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
	 * Test of getQueueModel method, of class PanelMain.
	 */
	@Test
	public void testGetQueueModel() {
		System.out.println("getQueueModel");
		ListModelPlayerQueue expResult = null;
		ListModelPlayerQueue result = PanelMain.getQueueModel();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getComboGenre method, of class PanelMain.
	 */
	@Test
	public void testGetComboGenre() {
		System.out.println("getComboGenre");
		String[] expResult = null;
		String[] result = PanelMain.getComboGenre();
		assertArrayEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRatingIcon method, of class PanelMain.
	 */
	@Test
	public void testGetRatingIcon() {
		System.out.println("getRatingIcon");
		int rating = 0;
		ImageIcon expResult = null;
		ImageIcon result = PanelMain.getRatingIcon(rating);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addToQueue method, of class PanelMain.
	 */
	@Test
	public void testAddToQueue() {
		System.out.println("addToQueue");
		FileInfoInt fileInfo = null;
		String rootPath = "";
		PanelMain.addToQueue(fileInfo, rootPath);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of fillGenreLists method, of class PanelMain.
	 */
	@Test
	public void testFillGenreLists() {
		System.out.println("fillGenreLists");
		PanelMain.fillGenreLists();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setOptions method, of class PanelMain.
	 */
	@Test
	public void testSetOptions() {
		System.out.println("setOptions");
		boolean expResult = false;
		boolean result = PanelMain.setOptions();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of enablePreviousAndNext method, of class PanelMain.
	 */
	@Test
	public void testEnablePreviousAndNext() {
		System.out.println("enablePreviousAndNext");
		boolean previous = false;
		boolean next = false;
		PanelMain.enablePreviousAndNext(previous, next);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of fillQueue method, of class PanelMain.
	 */
	@Test
	public void testFillQueue() {
		System.out.println("fillQueue");
		PanelMain.fillQueue();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of playSelected method, of class PanelMain.
	 */
	@Test
	public void testPlaySelected() {
		System.out.println("playSelected");
		boolean resume = false;
		PanelMain.playSelected(resume);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of play method, of class PanelMain.
	 */
	@Test
	public void testPlay() {
		System.out.println("play");
		boolean resume = false;
		PanelMain.play(resume);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of selectTab method, of class PanelMain.
	 */
	@Test
	public void testSelectTab() {
		System.out.println("selectTab");
		String title = "";
		PanelMain.selectTab(title);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of stopMplayer method, of class PanelMain.
	 */
	@Test
	public void testStopMplayer() {
		System.out.println("stopMplayer");
		PanelMain.stopMplayer();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of pause method, of class PanelMain.
	 */
	@Test
	public void testPause() {
		System.out.println("pause");
		PanelMain.pause();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of editLocation method, of class PanelMain.
	 */
	@Test
	public void testEditLocation() {
		System.out.println("editLocation");
		String location = "";
		PanelMain.editLocation(location);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addRowSelect method, of class PanelMain.
	 */
	@Test
	public void testAddRowSelect() {
		System.out.println("addRowSelect");
		TableModel tableModel = null;
		FileInfoInt myFileInfo = null;
		PanelMain.addRowSelect(tableModel, myFileInfo);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of refreshHiddenQueue method, of class PanelMain.
	 */
	@Test
	public void testRefreshHiddenQueue() {
		System.out.println("refreshHiddenQueue");
		boolean wait = false;
		PanelMain.refreshHiddenQueue(wait);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of forward method, of class PanelMain.
	 */
	@Test
	public void testForward() {
		System.out.println("forward");
		PanelMain.forward();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of rewind method, of class PanelMain.
	 */
	@Test
	public void testRewind() {
		System.out.println("rewind");
		PanelMain instance = new PanelMain();
		instance.rewind();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of unsetToggleButtonPlayerInfo method, of class PanelMain.
	 */
	@Test
	public void testUnsetToggleButtonPlayerInfo() {
		System.out.println("unsetToggleButtonPlayerInfo");
		PanelMain.unsetToggleButtonPlayerInfo();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayFileInfo method, of class PanelMain.
	 */
	@Test
	public void testDisplayFileInfo() {
		System.out.println("displayFileInfo");
		PanelMain.displayFileInfo();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPlaylists method, of class PanelMain.
	 */
	@Test
	public void testGetPlaylists() {
		System.out.println("getPlaylists");
		List<String> expResult = null;
		List<String> result = PanelMain.getPlaylists();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getGenre method, of class PanelMain.
	 */
	@Test
	public void testGetGenre() {
		System.out.println("getGenre");
		String genre = "";
		String expResult = "";
		String result = PanelMain.getGenre(genre);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setBasicVisible method, of class PanelMain.
	 */
	@Test
	public void testSetBasicVisible() {
		System.out.println("setBasicVisible");
		TableColumnModel myXTableColumnModel = null;
		boolean state = false;
		PanelMain.setBasicVisible(myXTableColumnModel, state);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setFileVisible method, of class PanelMain.
	 */
	@Test
	public void testSetFileVisible() {
		System.out.println("setFileVisible");
		TableColumnModel myXTableColumnModel = null;
		boolean state = false;
		PanelMain.setFileVisible(myXTableColumnModel, state);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setExtraVisible method, of class PanelMain.
	 */
	@Test
	public void testSetExtraVisible() {
		System.out.println("setExtraVisible");
		TableColumnModel myXTableColumnModel = null;
		boolean state = false;
		PanelMain.setExtraVisible(myXTableColumnModel, state);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setRightsVisible method, of class PanelMain.
	 */
	@Test
	public void testSetRightsVisible() {
		System.out.println("setRightsVisible");
		TableColumnModel myXTableColumnModel = null;
		boolean state = false;
		PanelMain.setRightsVisible(myXTableColumnModel, state);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setStatsVisible method, of class PanelMain.
	 */
	@Test
	public void testSetStatsVisible() {
		System.out.println("setStatsVisible");
		TableColumnModel myXTableColumnModel = null;
		boolean state = false;
		PanelMain.setStatsVisible(myXTableColumnModel, state);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setColumnVisible method, of class PanelMain.
	 */
	@Test
	public void testSetColumnVisible_4args() {
		System.out.println("setColumnVisible");
		TableColumnModel myXTableColumnModel = null;
		int start = 0;
		int end = 0;
		boolean state = false;
		PanelMain.setColumnVisible(myXTableColumnModel, start, end, state);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setColumnVisible method, of class PanelMain.
	 */
	@Test
	public void testSetColumnVisible_3args_1() {
		System.out.println("setColumnVisible");
		TableColumnModel myXTableColumnModel = null;
		int[] columns = null;
		boolean state = false;
		PanelMain.setColumnVisible(myXTableColumnModel, columns, state);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setColumnVisible method, of class PanelMain.
	 */
	@Test
	public void testSetColumnVisible_3args_2() {
		System.out.println("setColumnVisible");
		TableColumnModel myXTableColumnModel = null;
		int index = 0;
		boolean state = false;
		PanelMain.setColumnVisible(myXTableColumnModel, index, state);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of main method, of class PanelMain.
	 */
	@Test
	public void testMain() {
		System.out.println("main");
		PanelMain.main();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
