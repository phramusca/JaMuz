/*
 * Copyright (C) 2023 phramusca <phramusca@gmail.com>
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
package jamuz.database;

import jamuz.Jamuz;
import jamuz.Playlist;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoPlaylistTest {
	
	public DaoPlaylistTest() {
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

	@Test
	public void testPlaylists() {

		System.out.println("testPlaylists");
		
		//Check no default playlists
		ArrayList<Playlist> expectedPlaylists = new ArrayList<>();
		checkPlaylistList(expectedPlaylists);	

		//Create some playlists and test insertion in db
		// Note : filters and orders are not inserted
		Playlist playlist1 = new Playlist(1, "Pl 1", true, 0, Playlist.LimitUnit.minutes, true, Playlist.Type.Songs, Playlist.Match.All, true, "/fou/barre");
		Playlist playlist2 = new Playlist(2, "Pl 2nd", false, 20, Playlist.LimitUnit.hours, false, Playlist.Type.Albums, Playlist.Match.Inde, false, "/another/path/to/somwhere/else/");
		Playlist playlist3 = new Playlist(3, "Pl 3è du nom", true, -1, Playlist.LimitUnit.Mio, true, Playlist.Type.Songs, Playlist.Match.All, true, "/fou/barre");
		Playlist playlist4 = new Playlist(4, "Pl IV le retour", true, 99999, Playlist.LimitUnit.files, false, Playlist.Type.Artists, Playlist.Match.One, false, "/car/en/barre");		
		expectedPlaylists.add(playlist1);
		expectedPlaylists.add(playlist2);
		expectedPlaylists.add(playlist3);
		expectedPlaylists.add(playlist4);
		for(Playlist playlist : expectedPlaylists) {
			assertTrue(Jamuz.getDb().playlist().insert(playlist));
		}
		checkPlaylistList(expectedPlaylists);

		//Add some filters and orders, then test db update
		playlist2.addFilter(new Playlist.Filter(1, Playlist.Field.TITLE, Playlist.Operator.CONTAINS, "toto"));
		playlist2.addFilter(new Playlist.Filter(2, Playlist.Field.FORMAT, Playlist.Operator.ISNOT, "tutu"));
		playlist2.addOrder(new Playlist.Order(1, Playlist.Field.LASTPLAYED, true));
		playlist2.addOrder(new Playlist.Order(2, Playlist.Field.TITLE, false));
		playlist3.addFilter(new Playlist.Filter(1, Playlist.Field.TITLE, Playlist.Operator.CONTAINS, "toto"));
		playlist3.addFilter(new Playlist.Filter(2, Playlist.Field.FORMAT, Playlist.Operator.ISNOT, "tutu"));
		playlist3.addOrder(new Playlist.Order(1, Playlist.Field.LASTPLAYED, true));
		playlist3.addOrder(new Playlist.Order(2, Playlist.Field.TITLE, false));
		for(Playlist playlist : expectedPlaylists) {
			assertTrue(Jamuz.getDb().playlist().update(playlist));
		}
		checkPlaylistList(expectedPlaylists);
		
		//Update playlists and check update in db
		playlist1.addFilter(new Playlist.Filter(1, Playlist.Field.ADDEDDATE, Playlist.Operator.DATEGREATERTHAN, "titi"));
		playlist1.addOrder(new Playlist.Order(2, Playlist.Field.ALBUMRATING, true));
		playlist1.setHidden(false);	
		playlist2.setLimit(true);
		playlist2.setLimitUnit(Playlist.LimitUnit.Mio);
		playlist2.setLimitValue(666);
		playlist2.removeFilter(0);
		playlist2.removeOrder(1);
		playlist3.setMatch(Playlist.Match.One);
		playlist3.setName("newName");
		playlist3.setRandom(false);
		playlist3.setFilter(1, new Playlist.Filter(2, Playlist.Field.COPYRIGHT, Playlist.Operator.NUMISNOT, "thrt"));
		playlist3.setOrder(0, new Playlist.Order(1, Playlist.Field.TRACKNO, false));
		playlist4.setTranscode(true);
		playlist4.setType(Playlist.Type.Songs);		
		for(Playlist playlist : expectedPlaylists) {
			assertTrue(Jamuz.getDb().playlist().update(playlist));
		}
		checkPlaylistList(expectedPlaylists);
		
		assertTrue(Jamuz.getDb().playlist().delete(2));
		expectedPlaylists.remove(playlist2);
		checkPlaylistList(expectedPlaylists);

		//FIXME TEST Negative cases
		//FIXME TEST Check other constraints
	}

	private void checkPlaylistList(ArrayList<Playlist> expectedPlaylists) {
		HashMap<Integer, Playlist> actualList = new HashMap<>();
		assertTrue(Jamuz.getDb().playlist().get(actualList));
		//Collections.sort(expectedPlaylists); // getPlaylists() return sorted ?
		assertArrayEquals(expectedPlaylists.toArray(), actualList.values().toArray());
	}
	
	/**
	 * Test of insert method, of class DaoPlaylist.
	 */
	@Test
	@Ignore // Refer to testPlaylists() above
	public void testInsert() {
		System.out.println("insert");
		Playlist playlist = null;
		DaoPlaylist instance = null;
		boolean expResult = false;
		boolean result = instance.insert(playlist);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of get method, of class DaoPlaylist.
	 */
	@Test
	@Ignore // Refer to testPlaylists() above
	public void testGet() {
		System.out.println("get");
		HashMap<Integer, Playlist> playlists = null;
		DaoPlaylist instance = null;
		boolean expResult = false;
		boolean result = instance.get(playlists);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of update method, of class DaoPlaylist.
	 */
	@Test
	@Ignore // Refer to testPlaylists() above
	public void testUpdate() {
		System.out.println("update");
		Playlist playlist = null;
		DaoPlaylist instance = null;
		boolean expResult = false;
		boolean result = instance.update(playlist);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of delete method, of class DaoPlaylist.
	 */
	@Test
	@Ignore // Refer to testPlaylists() above
	public void testDelete() {
		System.out.println("delete");
		int id = 0;
		DaoPlaylist instance = null;
		boolean expResult = false;
		boolean result = instance.delete(id);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
