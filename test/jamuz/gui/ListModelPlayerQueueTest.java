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

import jamuz.gui.swing.ListElement;
import java.util.ArrayList;
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
public class ListModelPlayerQueueTest {
	
	public ListModelPlayerQueueTest() {
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
	 * Test of add method, of class ListModelPlayerQueue.
	 */
	@Test
	public void testAdd() {
		System.out.println("add");
		ListElement file = null;
		ListModelPlayerQueue instance = new ListModelPlayerQueue();
		instance.add(file);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of clear method, of class ListModelPlayerQueue.
	 */
	@Test
	public void testClear() {
		System.out.println("clear");
		ListModelPlayerQueue instance = new ListModelPlayerQueue();
		instance.clear();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of clearButLeave method, of class ListModelPlayerQueue.
	 */
	@Test
	public void testClearButLeave() {
		System.out.println("clearButLeave");
		int nbFilesPlayedToLeave = 0;
		ListModelPlayerQueue instance = new ListModelPlayerQueue();
		instance.clearButLeave(nbFilesPlayedToLeave);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of clearNotPlayed method, of class ListModelPlayerQueue.
	 */
	@Test
	public void testClearNotPlayed() {
		System.out.println("clearNotPlayed");
		ListModelPlayerQueue instance = new ListModelPlayerQueue();
		instance.clearNotPlayed();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setPlayingIndex method, of class ListModelPlayerQueue.
	 */
	@Test
	public void testSetPlayingIndex() {
		System.out.println("setPlayingIndex");
		int playingIndex = 0;
		ListModelPlayerQueue instance = new ListModelPlayerQueue();
		instance.setPlayingIndex(playingIndex);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPlayingIndex method, of class ListModelPlayerQueue.
	 */
	@Test
	public void testGetPlayingIndex() {
		System.out.println("getPlayingIndex");
		ListModelPlayerQueue instance = new ListModelPlayerQueue();
		int expResult = 0;
		int result = instance.getPlayingIndex();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of enablePreviousAndNext method, of class ListModelPlayerQueue.
	 */
	@Test
	public void testEnablePreviousAndNext() {
		System.out.println("enablePreviousAndNext");
		ListModelPlayerQueue instance = new ListModelPlayerQueue();
		instance.enablePreviousAndNext();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of next method, of class ListModelPlayerQueue.
	 */
	@Test
	public void testNext() {
		System.out.println("next");
		ListModelPlayerQueue instance = new ListModelPlayerQueue();
		instance.next();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of previous method, of class ListModelPlayerQueue.
	 */
	@Test
	public void testPrevious() {
		System.out.println("previous");
		ListModelPlayerQueue instance = new ListModelPlayerQueue();
		instance.previous();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addBullet method, of class ListModelPlayerQueue.
	 */
	@Test
	public void testAddBullet() {
		System.out.println("addBullet");
		ListModelPlayerQueue instance = new ListModelPlayerQueue();
		instance.addBullet();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeBullet method, of class ListModelPlayerQueue.
	 */
	@Test
	public void testRemoveBullet() {
		System.out.println("removeBullet");
		ListModelPlayerQueue instance = new ListModelPlayerQueue();
		instance.removeBullet();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getSize method, of class ListModelPlayerQueue.
	 */
	@Test
	public void testGetSize() {
		System.out.println("getSize");
		ListModelPlayerQueue instance = new ListModelPlayerQueue();
		int expResult = 0;
		int result = instance.getSize();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPlayingSong method, of class ListModelPlayerQueue.
	 */
	@Test
	public void testGetPlayingSong() {
		System.out.println("getPlayingSong");
		ListModelPlayerQueue instance = new ListModelPlayerQueue();
		ListElement expResult = null;
		ListElement result = instance.getPlayingSong();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getQueue method, of class ListModelPlayerQueue.
	 */
	@Test
	public void testGetQueue() {
		System.out.println("getQueue");
		ListModelPlayerQueue instance = new ListModelPlayerQueue();
		ArrayList<ListElement> expResult = null;
		ArrayList<ListElement> result = instance.getQueue();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getElementAt method, of class ListModelPlayerQueue.
	 */
	@Test
	public void testGetElementAt() {
		System.out.println("getElementAt");
		int index = 0;
		ListModelPlayerQueue instance = new ListModelPlayerQueue();
		Object expResult = null;
		Object result = instance.getElementAt(index);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of refreshPlayingFile method, of class ListModelPlayerQueue.
	 */
	@Test
	public void testRefreshPlayingFile() {
		System.out.println("refreshPlayingFile");
		ListModelPlayerQueue instance = new ListModelPlayerQueue();
		instance.refreshPlayingFile();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of moveRow method, of class ListModelPlayerQueue.
	 */
	@Test
	public void testMoveRow() throws Exception {
		System.out.println("moveRow");
		int fromIndex = 0;
		int toIndex = 0;
		ListModelPlayerQueue instance = new ListModelPlayerQueue();
		instance.moveRow(fromIndex, toIndex);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
