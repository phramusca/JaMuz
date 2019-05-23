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

import jamuz.Playlist;
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
public class PanelPlaylistsTest {
	
	public PanelPlaylistsTest() {
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
	 * Test of initExtended method, of class PanelPlaylists.
	 */
	@Test
	public void testInitExtended() {
		System.out.println("initExtended");
		PanelPlaylists instance = new PanelPlaylists();
		instance.initExtended();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of fillPlayList method, of class PanelPlaylists.
	 */
	@Test
	public void testFillPlayList() {
		System.out.println("fillPlayList");
		PanelPlaylists.fillPlayList();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayFilters method, of class PanelPlaylists.
	 */
	@Test
	public void testDisplayFilters() {
		System.out.println("displayFilters");
		Playlist playlist = null;
		PanelPlaylists.displayFilters(playlist);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of displayOrders method, of class PanelPlaylists.
	 */
	@Test
	public void testDisplayOrders() {
		System.out.println("displayOrders");
		Playlist playlist = null;
		PanelPlaylists.displayOrders(playlist);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
