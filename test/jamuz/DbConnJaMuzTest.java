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

import jamuz.process.check.DuplicateInfo;
import jamuz.process.check.FolderInfo;
import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import jamuz.remote.ClientInfo;
import java.awt.Color;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
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
public class DbConnJaMuzTest {
	
	public DbConnJaMuzTest() {
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
	 * Test of setUp method, of class DbConnJaMuz.
	 */
	@Test
	public void testSetUp() {
		System.out.println("setUp");
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.setUp();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateGenre method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateGenre_String_String() {
		System.out.println("updateGenre");
		String oldGenre = "";
		String newGenre = "";
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateGenre(oldGenre, newGenre);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateTag method, of class DbConnJaMuz.
	 */
	@Test
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

	/**
	 * Test of updateMachine method, of class DbConnJaMuz.
	 */
	@Test
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
	 * Test of updatePlaylist method, of class DbConnJaMuz.
	 */
	@Test
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
	 * Test of deleteGenre method, of class DbConnJaMuz.
	 */
	@Test
	public void testDeleteGenre() {
		System.out.println("deleteGenre");
		String genre = "";
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.deleteGenre(genre);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of insertGenre method, of class DbConnJaMuz.
	 */
	@Test
	public void testInsertGenre() {
		System.out.println("insertGenre");
		String genre = "";
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.insertGenre(genre);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of insertTag method, of class DbConnJaMuz.
	 */
	@Test
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

	/**
	 * Test of deleteTag method, of class DbConnJaMuz.
	 */
	@Test
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

	/**
	 * Test of insertPlaylist method, of class DbConnJaMuz.
	 */
	@Test
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
	 * Test of setIdPath method, of class DbConnJaMuz.
	 */
	@Test
	public void testSetIdPath() {
		System.out.println("setIdPath");
		int idPath = 0;
		int newIdPath = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.setIdPath(idPath, newIdPath);
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
	 * Test of checkGenre method, of class DbConnJaMuz.
	 */
	@Test
	public void testCheckGenre() {
		System.out.println("checkGenre");
		String genre = "";
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.checkGenre(genre);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getGenreList method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetGenreList() {
		System.out.println("getGenreList");
		ArrayList<String> myList = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.getGenreList(myList);
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
	 * Test of updateCopyRight method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateCopyRight() {
		System.out.println("updateCopyRight");
		int idPath = 0;
		int copyRight = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateCopyRight(idPath, copyRight);
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
	 * Test of setPathDeleted method, of class DbConnJaMuz.
	 */
	@Test
	public void testSetPathDeleted() {
		System.out.println("setPathDeleted");
		int idPath = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.setPathDeleted(idPath);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setFileDeleted method, of class DbConnJaMuz.
	 */
	@Test
	public void testSetFileDeleted() {
		System.out.println("setFileDeleted");
		int idFile = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.setFileDeleted(idFile);
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
	 * Test of insertTags method, of class DbConnJaMuz.
	 */
	@Test
	public void testInsertTags() {
		System.out.println("insertTags");
		FileInfoInt fileInfo = null;
		int[] key = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.insertTags(fileInfo, key);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateTags method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateTags() {
		System.out.println("updateTags");
		FileInfoInt fileInfo = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateTags(fileInfo);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateLastPlayedAndCounter method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateLastPlayedAndCounter() {
		System.out.println("updateLastPlayedAndCounter");
		FileInfoInt file = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateLastPlayedAndCounter(file);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateGenre method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateGenre_FileInfoInt() {
		System.out.println("updateGenre");
		FileInfoInt fileInfo = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateGenre(fileInfo);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateRating method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateRating() {
		System.out.println("updateRating");
		FileInfoInt fileInfo = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateRating(fileInfo);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setCheckedFlag method, of class DbConnJaMuz.
	 */
	@Test
	public void testSetCheckedFlag() {
		System.out.println("setCheckedFlag");
		int idPath = 0;
		FolderInfo.CheckedFlag checkedFlag = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.setCheckedFlag(idPath, checkedFlag);
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

	/**
	 * Test of insertDeviceFiles method, of class DbConnJaMuz.
	 */
	@Test
	public void testInsertDeviceFiles() {
		System.out.println("insertDeviceFiles");
		ArrayList<FileInfoInt> files = null;
		int idDevice = 0;
		DbConnJaMuz instance = null;
		ArrayList<FileInfoInt> expResult = null;
		ArrayList<FileInfoInt> result = instance.insertDeviceFiles(files, idDevice);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of insertDeviceFile method, of class DbConnJaMuz.
	 */
	@Test
	public void testInsertDeviceFile() {
		System.out.println("insertDeviceFile");
		int idDevice = 0;
		FileInfoInt file = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.insertDeviceFile(idDevice, file);
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
	 * Test of setPreviousPlayCounter method, of class DbConnJaMuz.
	 */
	@Test
	public void testSetPreviousPlayCounter() {
		System.out.println("setPreviousPlayCounter");
		ArrayList<? super FileInfoInt> files = null;
		int idStatSource = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.setPreviousPlayCounter(files, idStatSource);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isMachine method, of class DbConnJaMuz.
	 */
	@Test
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
	 * Test of getStatSource method, of class DbConnJaMuz.
	 */
	@Test
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
	 * Test of getStatSources method, of class DbConnJaMuz.
	 */
	@Test
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
	 * Test of setStatSource method, of class DbConnJaMuz.
	 */
	@Test
	public void testSetStatSource() {
		System.out.println("setStatSource");
		StatSource statSource = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.setStatSource(statSource);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setDevice method, of class DbConnJaMuz.
	 */
	@Test
	public void testSetDevice() {
		System.out.println("setDevice");
		Device device = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.setDevice(device);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setClientInfo method, of class DbConnJaMuz.
	 */
	@Test
	public void testSetClientInfo() {
		System.out.println("setClientInfo");
		ClientInfo clientInfo = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.setClientInfo(clientInfo);
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

	/**
	 * Test of deleteStatSource method, of class DbConnJaMuz.
	 */
	@Test
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
	 * Test of updateLastMergeDate method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateLastMergeDate() {
		System.out.println("updateLastMergeDate");
		int idStatSource = 0;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.updateLastMergeDate(idStatSource);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

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
	 * Test of deletePlaylist method, of class DbConnJaMuz.
	 */
	@Test
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
	 * Test of deleteMachine method, of class DbConnJaMuz.
	 */
	@Test
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
	 * Test of getPlaylists method, of class DbConnJaMuz.
	 */
	@Test
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

	/**
	 * Test of setOptions method, of class DbConnJaMuz.
	 */
	@Test
	public void testSetOptions() {
		System.out.println("setOptions");
		Machine selOptions = null;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.setOptions(selOptions);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setOption method, of class DbConnJaMuz.
	 */
	@Test
	public void testSetOption() {
		System.out.println("setOption");
		Option myOption = null;
		String value = "";
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.setOption(myOption, value);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getOptions method, of class DbConnJaMuz.
	 */
	@Test
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
	 * Test of getGenreListModel method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetGenreListModel() {
		System.out.println("getGenreListModel");
		DefaultListModel myListModel = null;
		DbConnJaMuz instance = null;
		instance.getGenreListModel(myListModel);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTagListModel method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetTagListModel() {
		System.out.println("getTagListModel");
		DefaultListModel myListModel = null;
		DbConnJaMuz instance = null;
		instance.getTagListModel(myListModel);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTags method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetTags_0args() {
		System.out.println("getTags");
		DbConnJaMuz instance = null;
		ArrayList<String> expResult = null;
		ArrayList<String> result = instance.getTags();
		assertEquals(expResult, result);
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

	/**
	 * Test of updateStatistics method, of class DbConnJaMuz.
	 */
	@Test
	public void testUpdateStatistics() {
		System.out.println("updateStatistics");
		ArrayList<? extends FileInfo> files = null;
		DbConnJaMuz instance = null;
		int[] expResult = null;
		int[] result = instance.updateStatistics(files);
		assertArrayEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setTags method, of class DbConnJaMuz.
	 */
	@Test
	public void testSetTags() {
		System.out.println("setTags");
		ArrayList<? extends FileInfo> files = null;
		int[] results = null;
		DbConnJaMuz instance = null;
		int[] expResult = null;
		int[] result = instance.setTags(files, results);
		assertArrayEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMachineListModel method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetMachineListModel() {
		System.out.println("getMachineListModel");
		DefaultListModel myListModel = null;
		DbConnJaMuz instance = null;
		instance.getMachineListModel(myListModel);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of fillCombo method, of class DbConnJaMuz.
	 */
	@Test
	public void testFillCombo() {
		System.out.println("fillCombo");
		DefaultComboBoxModel myComboModel = null;
		String table = "";
		DbConnJaMuz instance = null;
		instance.fillCombo(myComboModel, table);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

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
	 * Test of getStatistics method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetStatistics_ArrayList_StatSource() {
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
	 * Test of getStatistics method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetStatistics_ResultSet() {
		System.out.println("getStatistics");
		ResultSet rs = null;
		DbConnJaMuz instance = null;
		FileInfo expResult = null;
		FileInfo result = instance.getStatistics(rs);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setUpdateStatisticsParameters method, of class DbConnJaMuz.
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
	 * Test of tearDown method, of class DbConnJaMuz.
	 */
	@Test
	public void testTearDown() {
		System.out.println("tearDown");
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.tearDown();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFiles method, of class DbConnJaMuz.
	 */
	@Test
	public void testGetFiles_3args_1() {
		System.out.println("getFiles");
		ArrayList<FileInfoInt> files = null;
		int idPath = 0;
		boolean getDeleted = false;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.getFiles(files, idPath, getDeleted);
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
		FileInfoInt result = instance.getFile(idFile);
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
	public void testGetFiles_3args_2() {
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
	public void testGetFolders_ConcurrentHashMap_boolean() {
		System.out.println("getFolders");
		ConcurrentHashMap<String, FolderInfo> folders = null;
		boolean getDeleted = false;
		DbConnJaMuz instance = null;
		boolean expResult = false;
		boolean result = instance.getFolders(folders, getDeleted);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
