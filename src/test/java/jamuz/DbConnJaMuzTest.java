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

import jamuz.gui.swing.ListElement;
import jamuz.process.check.DuplicateInfo;
import jamuz.process.check.FolderInfo;
import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import jamuz.remote.ClientInfo;
import java.awt.Color;
import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.DefaultListModel;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import test.helpers.TestSettings;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class DbConnJaMuzTest {

	// <editor-fold defaultstate="collapsed" desc="Test Setup & Cleanup">
	public DbConnJaMuzTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		TestSettings.setupApplication();
//        Jamuz.getMachine().read();
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() {
	}

	// </editor-fold>
	
	// <editor-fold defaultstate="collapsed" desc="Setup">
	/**
	 * Test of setUp method, of class DbConnJaMuz.
	 */
	@Test
	@Ignore
	@Deprecated
	public void testSetUp() {
		System.out.println("setUp");
		//We would quickly know if this would not work. Not tested
	}

	/**
	 * Test of tearDown method, of class DbConnJaMuz.
	 */
	@Test
	@Ignore
	@Deprecated
	public void testTearDown() {
		System.out.println("tearDown");
		// This does nothing anyway. No test
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Genre">

	@Test
	@Ignore //Refer to DaoGenreTest.java
	public void testGetGenreListModel() {
		System.out.println("getGenreListModel");
		DefaultListModel myListModel = null;
		DbConnJaMuz instance = null;
		instance.getGenreListModel(myListModel);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Tag">
	/**
	 * Test of tag methods, of class DbConnJaMuz.
	 */
	@Test
	public void testTag() {

		System.out.println("testTag");
		
		ArrayList<String> expectedTags = new ArrayList<>();
		expectedTags.add("Calme");
		expectedTags.add("Normal");
		expectedTags.add("Joyeux");
		for (String tag : expectedTags) {
			assertTrue(Jamuz.getDb().insertTag(tag));
		}
		checkTagList(expectedTags);

		DefaultListModel myListModel = new DefaultListModel();
		Jamuz.getDb().getTagListModel(myListModel);
		assertArrayEquals(expectedTags.toArray(), myListModel.toArray());

		assertTrue("updateTag", Jamuz.getDb().updateTag("Normal", "Tutu"));
		expectedTags.set(2, "Tutu");
		checkTagList(expectedTags);

		assertTrue("deleteTag", Jamuz.getDb().deleteTag("Tutu"));
		expectedTags.remove("Tutu");
		checkTagList(expectedTags);

		assertTrue("insertTag", Jamuz.getDb().insertTag("Normal"));
		expectedTags.add("Normal");
		checkTagList(expectedTags);

		//Negative cases
		assertFalse("updateTag negative", Jamuz.getDb().updateTag("NoSuchWeirdGenre", "Toto"));
		checkTagList(expectedTags);

		assertFalse("deleteTag negative", Jamuz.getDb().deleteTag("NoSuchWeirdGenre"));
		checkTagList(expectedTags);

		assertFalse("insertTag negative", Jamuz.getDb().insertTag("Normal")); //As duplicate
		checkTagList(expectedTags);

		//FIXME TEST Check other constraints
	}

	private void checkTagList(ArrayList<String> expectedTags) {
		ArrayList<String> actualList = Jamuz.getDb().getTags();
		Collections.sort(expectedTags); // getTags() return sorted
		assertArrayEquals(expectedTags.toArray(), actualList.toArray());
	}

	@Test
	@Ignore // Refer to testTag() above
	public void testUpdateTag() {
		System.out.println("updateTag");
		String oldTag = "";
		String newTag = "";
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateTag(oldTag, newTag);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	@Test
	@Ignore // Refer to testTag() above
	public void testInsertTag() {
		System.out.println("insertTag");
		String tag = "";
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.insertTag(tag);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	@Test
	@Ignore // Refer to testTag() above
	public void testDeleteTag() {
		System.out.println("deleteTag");
		String tag = "";
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.deleteTag(tag);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	@Test
	@Ignore // Refer to testTag() above
	public void testGetTagListModel() {
		System.out.println("getTagListModel");
		DefaultListModel myListModel = null;
		DbConnJaMuz instance = null;
		instance.getTagListModel(myListModel);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	@Test
	@Ignore // Refer to testTag() above
	public void testGetTags_0args() {
		System.out.println("getTags");
		DbConnJaMuz instance = null;
		ArrayList<String> expResult = null;
		ArrayList<String> result = instance.getTags();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Machine & Option">
	/**
	 * Test of machine and option methods, of class DbConnJaMuz.
	 */
	@Test
	public void testMachineAndOption() {

		System.out.println("testMachineAndOption");
		
		//Create a new machine
		StringBuilder zText = new StringBuilder();
		String machineName = "000aaaa000"; //Hoping this this will be id 0 when sorted
		assertTrue("isMachine", Jamuz.getDb().isMachine(machineName, zText, false));

		//Get machines
		DefaultListModel defaultListModel = new DefaultListModel();
		Jamuz.getDb().getMachineListModel(defaultListModel);
		ListElement element = (ListElement) defaultListModel.get(0);
		assertEquals(2, defaultListModel.size()); //The created one and current machine
		assertEquals("<html><b>" + machineName + "</b><BR/><i></i></html>", element.toString());
		assertEquals(machineName, element.getValue());
		assertNull(element.getFile());

		//Get new machine options
		ArrayList<Option> expectedOptions = new ArrayList<>();
		int idMachine = 2;
		expectedOptions.add(new Option("location.library", "", idMachine, 1, "path"));
		expectedOptions.add(new Option("library.isMaster", "false", idMachine, 2, "bool"));
		expectedOptions.add(new Option("location.add", "", idMachine, 3, "path"));
		expectedOptions.add(new Option("location.ok", "", idMachine, 4, "path"));
		expectedOptions.add(new Option("location.ko", "", idMachine, 5, "path"));
		expectedOptions.add(new Option("network.proxy", "", idMachine, 6, "proxy"));
		expectedOptions.add(new Option("location.mask", "%albumartist%/%album%/%track% %title%", idMachine, 7, "mask"));
		expectedOptions.add(new Option("log.level", "INFO", idMachine, 8, "list"));
		expectedOptions.add(new Option("log.limit", "5242880", idMachine, 9, "integer"));
		expectedOptions.add(new Option("log.count", "20", idMachine, 10, "integer"));
		expectedOptions.add(new Option("files.audio", "mp3,flac", idMachine, 11, "csv"));
		expectedOptions.add(new Option("files.image", "", idMachine, 12, "csv"));
		expectedOptions.add(new Option("files.convert", "", idMachine, 13, "csv"));
		expectedOptions.add(new Option("files.delete", "", idMachine, 14, "csv"));
		expectedOptions.add(new Option("location.manual", "", idMachine, 15, "path"));
		expectedOptions.add(new Option("location.transcoded", "", idMachine, 16, "csv"));
		expectedOptions.add(new Option("files.image.delete", "false", idMachine, 17, "csv"));
		
		checkOptionList(machineName, expectedOptions);

		//Set description
		String description = "Waouh the great description!";
		assertTrue(Jamuz.getDb().updateMachine(idMachine, description));
		zText = new StringBuilder();
		assertTrue("isMachine updated", Jamuz.getDb().isMachine(machineName, zText, false));
		assertEquals(description, zText.toString());
		defaultListModel = new DefaultListModel();
		Jamuz.getDb().getMachineListModel(defaultListModel);
		element = (ListElement) defaultListModel.get(0);
		assertEquals(2, defaultListModel.size()); //The created one and current machine
		assertEquals("<html><b>" + machineName + "</b><BR/><i>" + description + "</i></html>", element.toString());
		assertEquals(machineName, element.getValue());
		assertNull(element.getFile());

		//Set options (one by one)
		int i = 0;
		String newValue;
		for (Option expectedOption : expectedOptions) {
			newValue = "New value " + i;
			Jamuz.getDb().updateOption(expectedOption, newValue);
			if (expectedOption.getType().equals("path")) {   //NOI18N
				newValue = FilenameUtils.normalizeNoEndSeparator(newValue.trim()) + File.separator;
			}
			expectedOption.setValue(newValue);
		}
		checkOptionList(machineName, expectedOptions);

		//Set options
		i = 0;
		for (Option expectedOption : expectedOptions) {
			newValue = "New New value " + (i + 10);
			expectedOption.setValue(newValue);
		}
		Machine machine = new Machine(machineName);
		machine.setOptions(expectedOptions);
		assertTrue(Jamuz.getDb().updateOptions(machine));
		for (Option expectedOption : expectedOptions) {
			if (expectedOption.getType().equals("path")) {   //NOI18N
				expectedOption.setValue(FilenameUtils.normalizeNoEndSeparator(expectedOption.getValue().trim()) + File.separator);
			}
		}
		checkOptionList(machineName, expectedOptions);

		//Delete machine 
		assertTrue(Jamuz.getDb().deleteMachine(machineName));
		defaultListModel = new DefaultListModel();
		Jamuz.getDb().getMachineListModel(defaultListModel);
		assertEquals(1, defaultListModel.size()); //Only current machine left
		element = (ListElement) defaultListModel.get(0);
		assertNull(element.getFile());
		assertNotSame(machineName, element.getValue());

		//FIXME TEST Negative cases
		//FIXME TEST Check other constraints
	}

	private void checkOptionList(String machineName, ArrayList<Option> expectedOptions) {
		ArrayList<Option> options = new ArrayList<>();
		assertTrue("getOptions", Jamuz.getDb().getOptions(options, machineName));
		Option actualOption;
		int i = 0;
		for (Option expectedOption : expectedOptions) {
			actualOption = options.get(i);
			i++;
			assertEquals(expectedOption.getComment(), actualOption.getComment());
			assertEquals(expectedOption.getIdOptionType(), actualOption.getIdOptionType());
			assertEquals(expectedOption.getId(), actualOption.getId());
			assertEquals(expectedOption.getIdMachine(), actualOption.getIdMachine());
			assertEquals(expectedOption.getType(), actualOption.getType());
			assertEquals(expectedOption.getValue(), actualOption.getValue());
			assertEquals(expectedOption.toString(), actualOption.toString());
		}
	}

	/**
	 * Test of isMachine method, of class DbConnJaMuz.
	 */
	@Test
	@Ignore // Refer to testMachineAndOption() above
	public void testIsMachine() {
		System.out.println("isMachine");
		String hostname = "";
		StringBuilder description = null;
		boolean hidden = false;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.isMachine(hostname, description, hidden);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateMachine method, of class DbConnJaMuz.
	 */
	@Test
	@Ignore // Refer to testMachineAndOption() above
	public void testUpdateMachine() {
		System.out.println("updateMachine");
		int idMachine = 0;
		String description = "";
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateMachine(idMachine, description);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of deleteMachine method, of class DbConnJaMuz.
	 */
	@Test
	@Ignore // Refer to testMachineAndOption() above
	public void testDeleteMachine() {
		System.out.println("deleteMachine");
		String machineName = "";
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.deleteMachine(machineName);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMachineListModel method, of class DbConnJaMuz.
	 */
	@Test
	@Ignore // Refer to testMachineAndOption() above
	public void testGetMachineListModel() {
		System.out.println("getMachineListModel");
		DefaultListModel myListModel = null;
		DbConnJaMuz instance = null;
		instance.getMachineListModel(myListModel);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateOptions method, of class DbConnJaMuz.
	 */
	@Test
	@Ignore // Refer to testMachineAndOption() above
	public void testUpdateOptions() {
		System.out.println("updateOptions");
		Machine selOptions = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateOptions(selOptions);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
	/**
	 * Test of updateOption method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateOption() {
		System.out.println("updateOption");
		Option myOption = null;
		String value = "";
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateOption(myOption, value);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getOptions method, of class DbConnJaMuz.
	 */
	@Test
	@Ignore // Refer to testMachineAndOption() above
	public void testGetOptions() {
		System.out.println("getOptions");
		ArrayList<Option> myOptions = null;
		String machineName = "";
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.getOptions(myOptions, machineName);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Playlist">

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
			assertTrue(Jamuz.getDb().insertPlaylist(playlist));
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
			assertTrue(Jamuz.getDb().updatePlaylist(playlist));
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
			assertTrue(Jamuz.getDb().updatePlaylist(playlist));
		}
		checkPlaylistList(expectedPlaylists);
		
		assertTrue(Jamuz.getDb().deletePlaylist(2));
		expectedPlaylists.remove(playlist2);
		checkPlaylistList(expectedPlaylists);

		//FIXME TEST Negative cases
		//FIXME TEST Check other constraints
	}

	private void checkPlaylistList(ArrayList<Playlist> expectedPlaylists) {
		HashMap<Integer, Playlist> actualList = new HashMap<>();
		assertTrue(Jamuz.getDb().getPlaylists(actualList));
		//Collections.sort(expectedPlaylists); // getPlaylists() return sorted ?
		assertArrayEquals(expectedPlaylists.toArray(), actualList.values().toArray());
	}
	
	/**
	 * Test of updatePlaylist method, of class DbConnJaMuz.
	 */
	@Test
	@Ignore // Refer to testPlaylists() above
	public void testUpdatePlaylist() {
		System.out.println("updatePlaylist");
		Playlist playlist = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updatePlaylist(playlist);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of insertPlaylist method, of class DbConnJaMuz.
	 */
	@Test
	@Ignore // Refer to testPlaylists() above
	public void testInsertPlaylist() {
		System.out.println("insertPlaylist");
		Playlist playlist = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.insertPlaylist(playlist);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of deletePlaylist method, of class DbConnJaMuz.
	 */
	@Test
	@Ignore // Refer to testPlaylists() above
	public void testDeletePlaylist() {
		System.out.println("deletePlaylist");
		int id = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.deletePlaylist(id);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPlaylists method, of class DbConnJaMuz.
	 */
	@Test
	@Ignore // Refer to testPlaylists() above
	public void testGetPlaylists() {
		System.out.println("getPlaylists");
		HashMap<Integer, Playlist> playlists = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.getPlaylists(playlists);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="StatSource">
	
	@Test
	public void testStatSources() {
	
		System.out.println("testStatSources");

		//Check no default stat sources
		ArrayList<StatSource> expectedStatSources = new ArrayList<>();
		checkStatSourceList(expectedStatSources);	

		//Create some stat sources and test insertion in db
		StatSource statSource1 = new StatSource(-1, "Numero Uno", 3, "ici et la", "moi meme", "BestPassword", "africa", Jamuz.getMachine().getName(), 0, false, "What if not a date?", false);
		StatSource statSource2 = new StatSource(-1, "Numero Dos", 5, "ici et ailleurs", "", "", "europe", Jamuz.getMachine().getName(), 0, true, "What if not a date?", false);
		StatSource statSource3 = new StatSource(-1, "Numero Tres", 2, "loin", "", "", "asie", Jamuz.getMachine().getName(), 0, true, "What if not a date?", false);
		expectedStatSources.add(statSource1);
		expectedStatSources.add(statSource2);
		expectedStatSources.add(statSource3);
		for(StatSource statSource : expectedStatSources) {
			assertTrue(Jamuz.getDb().insertOrUpdateStatSource(statSource));
		}
		// Needed in checkStatSourceList to get proper value
		// and later on too, to be able to update them
		statSource1.setId(1);
		statSource2.setId(2);
		statSource3.setId(3);
		checkStatSourceList(expectedStatSources);

		//Update some stat sources and test update in db
		//statSource1.setIdDevice(4569); // Need to create a device to test this
		//statSource2.setIdStatement(1); // Need to update source in statSource2 if changing IdStatement
		statSource3.setIsSelected(false);
		for(StatSource statSource : expectedStatSources) {
			assertTrue(Jamuz.getDb().insertOrUpdateStatSource(statSource));
		}
		checkStatSourceList(expectedStatSources);
		
		//Delete and check list
		assertTrue(Jamuz.getDb().deleteStatSource(2));
		expectedStatSources.remove(statSource2);
		checkStatSourceList(expectedStatSources);

		//Jamuz.getDb().updateStatSourceLastMergeDate(3); //Called below + set statSource
		statSource3.updateLastMergeDate();
		checkStatSourceList(expectedStatSources);
		
		//getStatSource returns only the first stat source for given machine
		//But ORDER BY on name only, so there might be an order on some other fields
		//resulting in statSource3 to be returned finally
		//Also, getStatSource is made for device remote, so stat source is hidden
		statSource3.setHidden(true);
		Assert.assertEquals(statSource3, Jamuz.getDb().getStatSource(Jamuz.getMachine().getName()));
		
		//FIXME TEST Negative cases
		//FIXME TEST Check other constraints
	}
	
	private void checkStatSourceList(ArrayList<StatSource> expectedStatSources) {
		LinkedHashMap<Integer, StatSource> actualList = new LinkedHashMap<>();
		assertTrue(Jamuz.getDb().getStatSources(actualList, Jamuz.getMachine().getName(), false));
		for (StatSource value : actualList.values()) {
			Optional<StatSource> findFirst = expectedStatSources.stream().filter(s -> s.getId()==value.getId()).findFirst();
			assertTrue(findFirst.isPresent());
			assertEquals(value, findFirst.get());
		}
	}
	
	/**
	 * Test of getStatSources method, of class DbConnJaMuz.
	 */
	@Test
	@Ignore // Refer to testStatSources() above
	public void testGetStatSources() {
		System.out.println("getStatSources");
		LinkedHashMap<Integer, StatSource> statSources = null;
		String hostname = "";
		boolean hidden = false;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.getStatSources(statSources, hostname, hidden);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of insertOrUpdateStatSource method, of class DbConnJaMuz.
	 */
	@Test
	@Ignore // Refer to testStatSources() above
	public void testInsertOrUpdateStatSource() {
		System.out.println("insertOrUpdateStatSource");
		StatSource statSource = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.insertOrUpdateStatSource(statSource);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of deleteStatSource method, of class DbConnJaMuz.
	 */
	@Test
	@Ignore // Refer to testStatSources() above
	public void testDeleteStatSource() {
		System.out.println("deleteStatSource");
		int id = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.deleteStatSource(id);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getStatSource method, of class DbConnJaMuz.
	 */
	@Test
	@Ignore // Refer to testStatSources() above
	public void testGetStatSource() {
		System.out.println("getStatSource");
		String login = "";
		DbConnJaMuz instance = null;
		StatSource expResult = null;
		StatSource result = instance.getStatSource(login);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateStatSourceLastMergeDate method, of class DbConnJaMuz.
	 */
	@Test
	@Ignore // Refer to testStatSources() above
	public void testUpdateStatSourceLastMergeDate() {
		System.out.println("updateStatSourceLastMergeDate");
		int idStatSource = 0;
		DbConnJaMuz instance = null;
		String expResult = "";
		String result = instance.updateStatSourceLastMergeDate(idStatSource);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	// </editor-fold>
	//FIXME TEST ! Continue from here
	// <editor-fold defaultstate="collapsed" desc="PlayCounter">
	
	/**
	 * Test of updatePreviousPlayCounter method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdatePreviousPlayCounter() {
		System.out.println("updatePreviousPlayCounter");
		ArrayList<? super FileInfoInt> files = null;
		int idStatSource = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updatePreviousPlayCounter(files, idStatSource);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="DeviceFile">
	
	/**
	 * Test of insertOrIgnoreDeviceFiles method, of class DbConnJaMuz.
	 */
	@Test
	public void testInsertOrIgnoreDeviceFiles() {
		System.out.println("insertOrIgnoreDeviceFiles");
		ArrayList<FileInfoInt> files = null;
		int idDevice = 0;
		DbConnJaMuz instance = null;
		ArrayList<FileInfoInt> expResult = null;
		ArrayList<FileInfoInt> result = instance.insertOrIgnoreDeviceFiles(files, idDevice);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of insertOrIgnoreDeviceFile method, of class DbConnJaMuz.
	 */
	@Test
	public void testInsertOrIgnoreDeviceFile() {
		System.out.println("insertOrIgnoreDeviceFile");
		int idDevice = 0;
		FileInfoInt file = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.insertOrIgnoreDeviceFile(idDevice, file);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of deleteDeviceFiles method, of class DbConnJaMuz.
	 */
	@Test
	public void testDeleteDeviceFiles() {
		System.out.println("deleteDeviceFiles");
		int idDevice = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.deleteDeviceFiles(idDevice);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of insertOrUpdateDeviceFiles method, of class DbConnJaMuz.
	 */
	@Test
	public void testInsertOrUpdateDeviceFiles() {
		System.out.println("insertOrUpdateDeviceFiles");
		ArrayList<FileInfoInt> files = null;
		int idDevice = 0;
		DbConnJaMuz instance = null;
		ArrayList<FileInfoInt> expResult = null;
		ArrayList<FileInfoInt> result = instance.insertOrUpdateDeviceFiles(files, idDevice);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
	/**
	 * Test of updateDeviceFileStatus method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateDeviceFileStatus() {
		System.out.println("updateDeviceFileStatus");
		DbConnJaMuz.SyncStatus status = null;
		int idFile = 0;
		int idDevice = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateDeviceFileStatus(status, idFile, idDevice);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
	/**
	 * Test of insertOrUpdateFilesTranslated method, of class DbConnJaMuz.
	 */
	@Test
	public void testInsertOrUpdateFilesTranslated() {
		System.out.println("insertOrUpdateFilesTranslated");
		ArrayList<FileInfoInt> files = null;
		DbConnJaMuz instance = null;
		instance.insertOrUpdateFilesTranslated(files);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Device">
	/**
	 * Test of deleteDevice method, of class DbConnJaMuz.
	 */
	@Test
	public void testDeleteDevice() {
		System.out.println("deleteDevice");
		int id = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.deleteDevice(id);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDevice method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetDevice() {
		System.out.println("getDevice");
		String login = "";
		DbConnJaMuz instance = null;
		Device expResult = null;
		Device result = instance.getDevice(login);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDevices method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetDevices() {
		System.out.println("getDevices");
		LinkedHashMap<Integer, Device> devices = null;
		String hostname = "";
		boolean hidden = false;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.getDevices(devices, hostname, hidden);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateDevice method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateDevice() {
		System.out.println("updateDevice");
		Device device = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateDevice(device);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Client">
	
	/**
	 * Test of updateClient method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateClient() {
		System.out.println("updateClient");
		ClientInfo clientInfo = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateClient(clientInfo);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getClient method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetClient() {
		System.out.println("getClient");
		String login = "";
		DbConnJaMuz instance = null;
		ClientInfo expResult = null;
		ClientInfo result = instance.getClient(login);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getClients method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetClients() {
		System.out.println("getClients");
		LinkedHashMap<Integer, ClientInfo> clients = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.getClients(clients);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="File">
	/**
	 * Test of getYear method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetYear() {
		System.out.println("getYear");
		String maxOrMin = "";
		DbConnJaMuz instance = null;
		double expResult = 0.0;
		double result = instance.getYear(maxOrMin);
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getStatItem method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetStatItem() {
		System.out.println("getStatItem");
		String field = "";
		String value = "";
		String table = "";
		String label = "";
		Color color = null;
		boolean[] selRatings = null;
		DbConnJaMuz instance = null;
		DbConnJaMuz.StatItem expResult = null;
		DbConnJaMuz.StatItem result = instance.getStatItem(field, value, table, label, color, selRatings);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of insert method, of class DbConnJaMuz.
	 */
	@Test
	public void testInsert() {
		System.out.println("insert");
		FileInfoInt fileInfo = null;
		int[] key = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.insert(fileInfo, key);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateFile method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateFile() {
		System.out.println("updateFile");
		FileInfoInt fileInfo = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateFile(fileInfo);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateFileLastPlayedAndCounter method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateFileLastPlayedAndCounter() {
		System.out.println("updateFileLastPlayedAndCounter");
		FileInfoInt file = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateFileLastPlayedAndCounter(file);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateFileRating method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateFileRating() {
		System.out.println("updateFileRating");
		FileInfoInt fileInfo = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateFileRating(fileInfo);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
	/**
	 * Test of updateFileGenre method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateFileGenre() {
		System.out.println("updateFileGenre");
		FileInfoInt fileInfo = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateFileGenre(fileInfo);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateFileIdPath method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateFileIdPath() {
		System.out.println("updateFileIdPath");
		int idPath = 0;
		int newIdPath = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateFileIdPath(idPath, newIdPath);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateFileModifDate method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateFileModifDate() {
		System.out.println("updateFileModifDate");
		int idFile = 0;
		Date modifDate = null;
		String name = "";
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateFileModifDate(idFile, modifDate, name);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setFileDeleted method, of class DbConnJaMuz.
	 */
	@Test
	public void testDeleteFile() {
		System.out.println("deleteFile");
		int idFile = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.deleteFile(idFile);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setFileSaved method, of class DbConnJaMuz.
	 */
	@Test
	public void testSetFileSaved() {
		System.out.println("setFileSaved");
		int idFile = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.setFileSaved(idFile);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getStatistics method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetStatistics() {
		System.out.println("getStatistics");
		ArrayList<FileInfo> files = null;
		StatSource statSource = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.getStatistics(files, statSource);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFileStatistics method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetFileStatistics() {
		System.out.println("getFileStatistics");
		ResultSet rs = null;
		DbConnJaMuz instance = null;
		FileInfo expResult = null;
		FileInfo result = instance.getFileStatistics(rs);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateFileStatistics method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateFileStatistics() {
		System.out.println("updateFileStatistics");
		ArrayList<? extends FileInfo> files = null;
		DbConnJaMuz instance = null;
		int[] expResult = null;
		int[] result = instance.updateFileStatistics(files);
		assertArrayEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setUpdateStatisticsParameters method, of class DbConnJaMuz.
	 *
	 * @throws java.lang.Exception
	 */
	@Test
	public void testSetUpdateStatisticsParameters() throws Exception {
		System.out.println("setUpdateStatisticsParameters");
		FileInfo file = null;
		DbConnJaMuz instance = null;
		instance.setUpdateStatisticsParameters(file);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFilesCount method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetFilesCount() {
		System.out.println("getFilesCount");
		String sql = "";
		DbConnJaMuz instance = null;
		Integer expResult = null;
		Integer result = instance.getFilesCount(sql);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Path">
	/**
	 * Test of getFolders method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetFolders_ConcurrentHashMap_FolderInfoCheckedFlag() {
		System.out.println("getFolders");
		ConcurrentHashMap<String, FolderInfo> folders = null;
		FolderInfo.CheckedFlag checkedFlag = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.getFolders(folders, checkedFlag);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFolder method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetFolder_ConcurrentHashMap_int() {
		System.out.println("getFolder");
		ConcurrentHashMap<String, FolderInfo> folders = null;
		int idPath = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.getFolder(folders, idPath);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFolder method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetFolder_int() {
		System.out.println("getFolder");
		int idPath = 0;
		DbConnJaMuz instance = null;
		FolderInfo expResult = null;
		FolderInfo result = instance.getFolder(idPath);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFolders method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetFolders_ConcurrentHashMap() {
		System.out.println("getFolders");
		ConcurrentHashMap<String, FolderInfo> folders = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.getFolders(folders);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getIdPath method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetIdPath() {
		System.out.println("getIdPath");
		String path = "";
		DbConnJaMuz instance = null;
		int expResult = 0;
		int result = instance.getIdPath(path);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of insertPath method, of class DbConnJaMuz.
	 */
	@Test
	public void testInsertPath() {
		System.out.println("insertPath");
		String relativePath = "";
		Date modifDate = null;
		FolderInfo.CheckedFlag checkedFlag = null;
		String mbId = "";
		int[] key = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.insertPath(relativePath, modifDate, checkedFlag, mbId, key);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updatePath method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdatePath() {
		System.out.println("updatePath");
		int idPath = 0;
		Date modifDate = null;
		FolderInfo.CheckedFlag checkedFlag = null;
		String path = "";
		String mbId = "";
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updatePath(idPath, modifDate, checkedFlag, path, mbId);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updatePathCopyRight method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdatePathCopyRight() {
		System.out.println("updatePathCopyRight");
		int idPath = 0;
		int copyRight = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updatePathCopyRight(idPath, copyRight);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updatePathChecked method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdatePathChecked() {
		System.out.println("updatePathChecked");
		int idPath = 0;
		FolderInfo.CheckedFlag checkedFlag = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updatePathChecked(idPath, checkedFlag);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setCheckedFlagReset method, of class DbConnJaMuz.
	 */
	@Test
	public void testSetCheckedFlagReset() {
		System.out.println("setCheckedFlagReset");
		FolderInfo.CheckedFlag checkedFlag = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.setCheckedFlagReset(checkedFlag);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="File & Path">
	/**
	 * Test of getSelectionList4Stats method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetSelectionList4Stats() {
		System.out.println("getSelectionList4Stats");
		ArrayList<DbConnJaMuz.StatItem> stats = null;
		String field = "";
		boolean[] selRatings = null;
		DbConnJaMuz instance = null;
		instance.getSelectionList4Stats(stats, field, selRatings);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPercentRatedForStats method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetPercentRatedForStats() {
		System.out.println("getPercentRatedForStats");
		ArrayList<DbConnJaMuz.StatItem> stats = null;
		DbConnJaMuz instance = null;
		instance.getPercentRatedForStats(stats);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFiles method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetFiles_11args() {
		System.out.println("getFiles");
		ArrayList<FileInfoInt> myFileInfoList = null;
		String selGenre = "";
		String selArtist = "";
		String selAlbum = "";
		boolean[] selRatings = null;
		boolean[] selCheckedFlag = null;
		int yearFrom = 0;
		int yearTo = 0;
		float bpmFrom = 0.0F;
		float bpmTo = 0.0F;
		int copyRight = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.getFiles(myFileInfoList, selGenre, selArtist, selAlbum, selRatings, selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo, copyRight);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFilesStats method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetFilesStats_10args() {
		System.out.println("getFilesStats");
		String selGenre = "";
		String selArtist = "";
		String selAlbum = "";
		boolean[] selRatings = null;
		boolean[] selCheckedFlag = null;
		int yearFrom = 0;
		int yearTo = 0;
		float bpmFrom = 0.0F;
		float bpmTo = 0.0F;
		int copyRight = 0;
		DbConnJaMuz instance = null;
		String expResult = "";
		String result = instance.getFilesStats(selGenre, selArtist, selAlbum, selRatings, selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo, copyRight);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFilesStats method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetFilesStats_String() {
		System.out.println("getFilesStats");
		String sql = "";
		DbConnJaMuz instance = null;
		String expResult = "";
		String result = instance.getFilesStats(sql);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setPathDeleted method, of class DbConnJaMuz.
	 */
	@Test
	public void testDeletePath() {
		System.out.println("deletePath");
		int idPath = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.deletePath(idPath);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of fillSelectorList method, of class DbConnJaMuz.
	 */
	@Test
	public void testFillSelectorList() {
		System.out.println("fillSelectorList");
		DefaultListModel myListModel = null;
		String field = "";
		String selGenre = "";
		String selArtist = "";
		String selAlbum = "";
		boolean[] selRatings = null;
		boolean[] selCheckedFlag = null;
		int yearFrom = 0;
		int yearTo = 0;
		float bpmFrom = 0.0F;
		float bpmTo = 0.0F;
		int copyRight = 0;
		String sqlOrder = "";
		DbConnJaMuz instance = null;
		instance.fillSelectorList(myListModel, field, selGenre, selArtist, selAlbum, selRatings, selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo, copyRight, sqlOrder);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFiles method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetFiles_ArrayList_int() {
		System.out.println("getFiles");
		ArrayList<FileInfoInt> files = null;
		int idPath = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.getFiles(files, idPath);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFile method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetFile() {
		System.out.println("getFile");
		int idFile = 0;
		DbConnJaMuz instance = null;
		FileInfoInt expResult = null;
		FileInfoInt result = instance.getFile(idFile, "mp3");
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFiles method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetFiles_ArrayList_String() {
		System.out.println("getFiles");
		ArrayList<FileInfoInt> myFileInfoList = null;
		String sql = "";
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.getFiles(myFileInfoList, sql);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFiles method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetFiles_3args() {
		System.out.println("getFiles");
		ArrayList<FileInfoInt> myFileInfoList = null;
		String sql = "";
		String rootPath = "";
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.getFiles(myFileInfoList, sql, rootPath);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of checkAlbumSimilar method, of class DbConnJaMuz.
	 */
	@Test
	public void testCheckAlbumSimilar() {
		System.out.println("checkAlbumSimilar");
		ArrayList<DuplicateInfo> myList = null;
		String album = "";
		int idPath = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.checkAlbumSimilar(myList, album, idPath);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of checkAlbumExact method, of class DbConnJaMuz.
	 */
	@Test
	public void testCheckAlbumExact() {
		System.out.println("checkAlbumExact");
		ArrayList<DuplicateInfo> myList = null;
		String album = "";
		int idPath = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.checkAlbumExact(myList, album, idPath);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of checkAlbumDuplicate method, of class DbConnJaMuz.
	 */
	@Test
	public void testCheckAlbumDuplicate_ArrayList_String() {
		System.out.println("checkAlbumDuplicate");
		ArrayList<DuplicateInfo> myList = null;
		String mbId = "";
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.checkAlbumDuplicate(myList, mbId);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of checkAlbumDuplicate method, of class DbConnJaMuz.
	 */
	@Test
	public void testCheckAlbumDuplicate_6args() {
		System.out.println("checkAlbumDuplicate");
		ArrayList<DuplicateInfo> myList = null;
		String albumArtist = "";
		String album = "";
		int idPath = 0;
		int discNo = 0;
		int discTotal = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.checkAlbumDuplicate(myList, albumArtist, album, idPath, discNo, discTotal);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of checkAlbumDuplicate method, of class DbConnJaMuz.
	 */
	@Test
	public void testCheckAlbumDuplicate_4args() {
		System.out.println("checkAlbumDuplicate");
		ArrayList<DuplicateInfo> myList = null;
		String albumArtist = "";
		String album = "";
		int idPath = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.checkAlbumDuplicate(myList, albumArtist, album, idPath);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="TagFile & File">

	/**
	 * Test of updateFileTags method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateFileTags() {
		System.out.println("updateFileTags");
		ArrayList<? extends FileInfo> files = null;
		int[] results = null;
		DbConnJaMuz instance = null;
		int[] expResult = null;
		int[] result = instance.updateFileTags(files, results);
		assertArrayEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTags method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetTags_ArrayList_int() {
		System.out.println("getTags");
		ArrayList<String> tags = null;
		int idFile = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.getTags(tags, idFile);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTags method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetTags_ArrayList_FileInfo() {
		System.out.println("getTags");
		ArrayList<String> tags = null;
		FileInfo file = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.getTags(tags, file);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	// </editor-fold>
	
	// <editor-fold defaultstate="collapsed" desc="Schema">
	
	/**
	 * Test of updateSchema method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateSchema() {
		System.out.println("updateSchema");
		int requestedVersion = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateSchema(requestedVersion);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
	// </editor-fold>

	
}