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

import jamuz.database.DbConnJaMuz;
import jamuz.gui.swing.ListElement;
import jamuz.process.check.DuplicateInfo;
import jamuz.process.check.FolderInfo;
import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import jamuz.process.sync.SyncStatus;
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
import test.helpers.TestUnitSettings;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class DbConnJaMuzTest {

	private static DbConnJaMuz dbConnJaMuz;
	
	// <editor-fold defaultstate="collapsed" desc="Test Setup & Cleanup">
	public DbConnJaMuzTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
//		TestSettings.setupApplication();
		dbConnJaMuz = TestUnitSettings.createTempDatabase();
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
			assertTrue(Jamuz.getDb().tag().insert(tag));
		}
		checkTagList(expectedTags);

		DefaultListModel myListModel = new DefaultListModel();
		Jamuz.getDb().listModel().getTagListModel(myListModel);
		assertArrayEquals(expectedTags.toArray(), myListModel.toArray());

		assertTrue("updateTag", Jamuz.getDb().tag().update("Normal", "Tutu"));
		expectedTags.set(2, "Tutu");
		checkTagList(expectedTags);

		assertTrue("deleteTag", Jamuz.getDb().tag().delete("Tutu"));
		expectedTags.remove("Tutu");
		checkTagList(expectedTags);

		assertTrue("insertTag", Jamuz.getDb().tag().insert("Normal"));
		expectedTags.add("Normal");
		checkTagList(expectedTags);

		//Negative cases
		assertFalse("updateTag negative", Jamuz.getDb().tag().update("NoSuchWeirdGenre", "Toto"));
		checkTagList(expectedTags);

		assertFalse("deleteTag negative", Jamuz.getDb().tag().delete("NoSuchWeirdGenre"));
		checkTagList(expectedTags);

		assertFalse("insertTag negative", Jamuz.getDb().tag().insert("Normal")); //As duplicate
		checkTagList(expectedTags);

		//FIXME TEST Check other constraints
	}

	private void checkTagList(ArrayList<String> expectedTags) {
		ArrayList<String> actualList = Jamuz.getDb().tag().get();
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
		boolean result = instance.tag().update(oldTag, newTag);
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
		boolean result = instance.tag().insert(tag);
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
		boolean result = instance.tag().delete(tag);
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
		instance.listModel().getTagListModel(myListModel);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	@Test
	@Ignore // Refer to testTag() above
	public void testGetTags_0args() {
		System.out.println("getTags");
		DbConnJaMuz instance = null;
		ArrayList<String> expResult = null;
		ArrayList<String> result = instance.tag().get();
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
		assertTrue("isMachine", Jamuz.getDb().machine().getOrInsert(machineName, zText, false));

		//Get machines
		DefaultListModel defaultListModel = new DefaultListModel();
		Jamuz.getDb().listModel().getMachineListModel(defaultListModel);
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
		assertTrue(Jamuz.getDb().machine().update(idMachine, description));
		zText = new StringBuilder();
		assertTrue("isMachine updated", Jamuz.getDb().machine().getOrInsert(machineName, zText, false));
		assertEquals(description, zText.toString());
		defaultListModel = new DefaultListModel();
		Jamuz.getDb().listModel().getMachineListModel(defaultListModel);
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
			Jamuz.getDb().option().update(expectedOption, newValue);
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
		assertTrue(Jamuz.getDb().option().update(machine));
		for (Option expectedOption : expectedOptions) {
			if (expectedOption.getType().equals("path")) {   //NOI18N
				expectedOption.setValue(FilenameUtils.normalizeNoEndSeparator(expectedOption.getValue().trim()) + File.separator);
			}
		}
		checkOptionList(machineName, expectedOptions);

		//Delete machine 
		assertTrue(Jamuz.getDb().machine().delete(machineName));
		defaultListModel = new DefaultListModel();
		Jamuz.getDb().listModel().getMachineListModel(defaultListModel);
		assertEquals(1, defaultListModel.size()); //Only current machine left
		element = (ListElement) defaultListModel.get(0);
		assertNull(element.getFile());
		assertNotSame(machineName, element.getValue());

		//FIXME TEST Negative cases
		//FIXME TEST Check other constraints
	}

	private void checkOptionList(String machineName, ArrayList<Option> expectedOptions) {
		ArrayList<Option> options = new ArrayList<>();
		assertTrue("getOptions", Jamuz.getDb().option().get(options, machineName));
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
		boolean result = instance.machine().getOrInsert(hostname, description, hidden);
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
		boolean result = instance.machine().update(idMachine, description);
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
		boolean result = instance.machine().delete(machineName);
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
		instance.listModel().getMachineListModel(myListModel);
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
		boolean result = instance.option().update(selOptions);
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
		boolean result = instance.option().update(myOption, value);
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
		boolean result = instance.option().get(myOptions, machineName);
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
	 * Test of updatePlaylist method, of class DbConnJaMuz.
	 */
	@Test
	@Ignore // Refer to testPlaylists() above
	public void testUpdatePlaylist() {
		System.out.println("updatePlaylist");
		Playlist playlist = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.playlist().update(playlist);
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
		boolean result = instance.playlist().insert(playlist);
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
		boolean result = instance.playlist().delete(id);
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
		boolean result = instance.playlist().get(playlists);
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
			assertTrue(Jamuz.getDb().statSource().insertOrUpdate(statSource));
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
			assertTrue(Jamuz.getDb().statSource().insertOrUpdate(statSource));
		}
		checkStatSourceList(expectedStatSources);
		
		//Delete and check list
		assertTrue(Jamuz.getDb().statSource().delete(2));
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
		Assert.assertEquals(statSource3, Jamuz.getDb().statSource().get(Jamuz.getMachine().getName()));
		
		//FIXME TEST Negative cases
		//FIXME TEST Check other constraints
	}
	
	private void checkStatSourceList(ArrayList<StatSource> expectedStatSources) {
		LinkedHashMap<Integer, StatSource> actualList = new LinkedHashMap<>();
		assertTrue(Jamuz.getDb().statSource().get(actualList, Jamuz.getMachine().getName(), false));
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
		boolean result = instance.statSource().get(statSources, hostname, hidden);
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
		boolean result = instance.statSource().insertOrUpdate(statSource);
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
		boolean result = instance.statSource().delete(id);
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
		StatSource result = instance.statSource().get(login);
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
		String result = instance.statSource().updateLastMergeDate(idStatSource);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	// </editor-fold>
}