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
package jamuz;

import java.io.File;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import org.apache.http.impl.client.DefaultHttpClient;
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
public class JamuzTest {
	
	/**
	 *
	 */
	public JamuzTest() {
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
	 * Test of configure method, of class Jamuz.
	 */
	@Test
	public void testConfigure() {
		System.out.println("configure");
		String appPath = "";
		boolean expResult = false;
		boolean result = Jamuz.configure(appPath);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getHttpClient method, of class Jamuz.
	 */
	@Test
	public void testGetHttpClient() {
		System.out.println("getHttpClient");
		DefaultHttpClient expResult = null;
		DefaultHttpClient result = Jamuz.getHttpClient();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getProxy method, of class Jamuz.
	 */
	@Test
	public void testGetProxy() {
		System.out.println("getProxy");
		Proxy expResult = null;
		Proxy result = Jamuz.getProxy();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFile method, of class Jamuz.
	 */
	@Test
	public void testGetFile() {
		System.out.println("getFile");
		String filename = "";
		String[] args = null;
		File expResult = null;
		File result = Jamuz.getFile(filename, args);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLogPath method, of class Jamuz.
	 */
	@Test
	public void testGetLogPath() {
		System.out.println("getLogPath");
		String expResult = "";
		String result = Jamuz.getLogPath();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDb method, of class Jamuz.
	 */
	@Test
	public void testGetDb() {
		System.out.println("getDb");
		DbConnJaMuz expResult = null;
		DbConnJaMuz result = Jamuz.getDb();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMachine method, of class Jamuz.
	 */
	@Test
	public void testGetMachine() {
		System.out.println("getMachine");
		Machine expResult = null;
		Machine result = Jamuz.getMachine();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getOptions method, of class Jamuz.
	 */
	@Test
	public void testGetOptions() {
		System.out.println("getOptions");
		Options expResult = null;
		Options result = Jamuz.getOptions();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getKeys method, of class Jamuz.
	 */
	@Test
	public void testGetKeys() {
		System.out.println("getKeys");
		Options expResult = null;
		Options result = Jamuz.getKeys();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of readGenres method, of class Jamuz.
	 */
	@Test
	public void testReadGenres() {
		System.out.println("readGenres");
		Jamuz.readGenres();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getGenres method, of class Jamuz.
	 */
	@Test
	public void testGetGenres() {
		System.out.println("getGenres");
		List<String> expResult = null;
		List<String> result = Jamuz.getGenres();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getGenreListModel method, of class Jamuz.
	 */
	@Test
	public void testGetGenreListModel() {
		System.out.println("getGenreListModel");
		DefaultListModel expResult = null;
		DefaultListModel result = Jamuz.getGenreListModel();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of readTags method, of class Jamuz.
	 */
	@Test
	public void testReadTags() {
		System.out.println("readTags");
		Jamuz.readTags();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTags method, of class Jamuz.
	 */
	@Test
	public void testGetTags() {
		System.out.println("getTags");
		ArrayList<String> expResult = null;
		ArrayList<String> result = Jamuz.getTags();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTagsModel method, of class Jamuz.
	 */
	@Test
	public void testGetTagsModel() {
		System.out.println("getTagsModel");
		DefaultListModel expResult = null;
		DefaultListModel result = Jamuz.getTagsModel();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of readPlaylists method, of class Jamuz.
	 */
	@Test
	public void testReadPlaylists() {
		System.out.println("readPlaylists");
		boolean expResult = false;
		boolean result = Jamuz.readPlaylists();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPlaylists method, of class Jamuz.
	 */
	@Test
	public void testGetPlaylists() {
		System.out.println("getPlaylists");
		List<Playlist> expResult = null;
		List<Playlist> result = Jamuz.getPlaylists();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPlaylistsVisible method, of class Jamuz.
	 */
	@Test
	public void testGetPlaylistsVisible() {
		System.out.println("getPlaylistsVisible");
		List<Playlist> expResult = null;
		List<Playlist> result = Jamuz.getPlaylistsVisible();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPlaylist method, of class Jamuz.
	 */
	@Test
	public void testGetPlaylist() {
		System.out.println("getPlaylist");
		int id = 0;
		Playlist expResult = null;
		Playlist result = Jamuz.getPlaylist(id);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLogger method, of class Jamuz.
	 */
	@Test
	public void testGetLogger() {
		System.out.println("getLogger");
		Logger expResult = null;
		Logger result = Jamuz.getLogger();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
