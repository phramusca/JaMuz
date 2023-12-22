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
package jamuz.database;

import jamuz.FileInfo;
import jamuz.FileInfoInt;
import jamuz.process.check.FolderInfo;
import jamuz.process.merge.StatSource;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import test.helpers.TestUnitSettings;
import java.io.IOException;
import java.sql.SQLException;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class DbConnJaMuzTest {

	private static DbConnJaMuz dbConnJaMuz;

	public DbConnJaMuzTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
		dbConnJaMuz = TestUnitSettings.createTempDatabase();
	}

	@AfterClass
	public static void tearDownClass() {
		TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
	}

	@Before
	public void setUp() throws SQLException, ClassNotFoundException, IOException {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testConcurrency() throws InterruptedException {

		ExecutorService executorService = Executors.newFixedThreadPool(150);

		for (int i = 0; i < 10; i++) {
			final int index = i;

			//FIXME TEST Check other methods, and check if no exceptions (need to throw them)
			
			executorService.submit(() -> {
				System.out.println("file insert " + index);
				int[] keyPath = new int[1];
				dbConnJaMuz.path().lock().insert("4file/insert" + index, new Date(), FolderInfo.CheckedFlag.UNCHECKED, "mmmbbbiiidd", keyPath);
				int[] key = new int[1];
				FileInfoInt fileInfoInt = new FileInfoInt("file/insert" + index, "/root/file/insert" + index);
				fileInfoInt.setIdPath(keyPath[0]);
				dbConnJaMuz.file().lock().insert(fileInfoInt, key);
				System.out.println("file inserted " + index);
			});

			executorService.submit(() -> {
				System.out.println("file update " + index);
				int[] keyPath = new int[1];
				dbConnJaMuz.path().lock().insert("4file/update" + index, new Date(), FolderInfo.CheckedFlag.UNCHECKED, "mmmbbbiiidd", keyPath);
				int[] key = new int[1];
				FileInfoInt fileInfoInt = new FileInfoInt("file/update" + index, "/root/file/update" + index);
				fileInfoInt.setIdPath(keyPath[0]);
				dbConnJaMuz.file().lock().insert(fileInfoInt, key);
				FileInfoInt file = dbConnJaMuz.file().getFile(key[0], "");
				file.setGenre("Updated + " + key[0]);
				dbConnJaMuz.file().lock().update(file);
				System.out.println("file updated " + index);
			});

			executorService.submit(() -> {
				System.out.println("path insert " + index);
				int[] key = new int[1];
				dbConnJaMuz.path().lock().insert("path/insert" + index, new Date(), FolderInfo.CheckedFlag.UNCHECKED, "mmmbbbiiidd", key);
				System.out.println("path inserted " + index);
			});

			executorService.submit(() -> {
				System.out.println("path update " + index);
				final String path = "path/update" + index;
				int[] key = new int[1];
				dbConnJaMuz.path().lock().insert(path, new Date(), FolderInfo.CheckedFlag.UNCHECKED, "mmmbbbiiidd", key);
				int idPath = dbConnJaMuz.path().getIdPath(path);
				dbConnJaMuz.path().lock().update(idPath, new Date(), FolderInfo.CheckedFlag.OK_WARNING, path, "MbIdd + " + idPath);
				System.out.println("path updated " + index);
			});

		}
		executorService.shutdown();
		if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
			System.err.println("Pool did not terminate");
		}

		System.out.println("-- END --");
	}

    /**
     * Test of setLocationLibrary method, of class DbConnJaMuz.
     */
    @Test
    public void testSetLocationLibrary() {
        System.out.println("setLocationLibrary");
        String locationLibrary = "";
        DbConnJaMuz instance = null;
        instance.setLocationLibrary(locationLibrary);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of genre method, of class DbConnJaMuz.
     */
    @Test
    public void testGenre() {
        System.out.println("genre");
        DbConnJaMuz instance = null;
        DaoGenre expResult = null;
        DaoGenre result = instance.genre();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of tag method, of class DbConnJaMuz.
     */
    @Test
    public void testTag() {
        System.out.println("tag");
        DbConnJaMuz instance = null;
        DaoTag expResult = null;
        DaoTag result = instance.tag();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fileTag method, of class DbConnJaMuz.
     */
    @Test
    public void testFileTag() {
        System.out.println("fileTag");
        DbConnJaMuz instance = null;
        DaoFileTag expResult = null;
        DaoFileTag result = instance.fileTag();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of machine method, of class DbConnJaMuz.
     */
    @Test
    public void testMachine() {
        System.out.println("machine");
        DbConnJaMuz instance = null;
        DaoMachine expResult = null;
        DaoMachine result = instance.machine();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of playlist method, of class DbConnJaMuz.
     */
    @Test
    public void testPlaylist() {
        System.out.println("playlist");
        DbConnJaMuz instance = null;
        DaoPlaylist expResult = null;
        DaoPlaylist result = instance.playlist();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of device method, of class DbConnJaMuz.
     */
    @Test
    public void testDevice() {
        System.out.println("device");
        DbConnJaMuz instance = null;
        DaoDevice expResult = null;
        DaoDevice result = instance.device();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deviceFile method, of class DbConnJaMuz.
     */
    @Test
    public void testDeviceFile() {
        System.out.println("deviceFile");
        DbConnJaMuz instance = null;
        DaoDeviceFile expResult = null;
        DaoDeviceFile result = instance.deviceFile();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of client method, of class DbConnJaMuz.
     */
    @Test
    public void testClient() {
        System.out.println("client");
        DbConnJaMuz instance = null;
        DaoClient expResult = null;
        DaoClient result = instance.client();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of statSource method, of class DbConnJaMuz.
     */
    @Test
    public void testStatSource() {
        System.out.println("statSource");
        DbConnJaMuz instance = null;
        DaoStatSource expResult = null;
        DaoStatSource result = instance.statSource();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of schema method, of class DbConnJaMuz.
     */
    @Test
    public void testSchema() {
        System.out.println("schema");
        DbConnJaMuz instance = null;
        DaoSchema expResult = null;
        DaoSchema result = instance.schema();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of file method, of class DbConnJaMuz.
     */
    @Test
    public void testFile() {
        System.out.println("file");
        DbConnJaMuz instance = null;
        DaoFile expResult = null;
        DaoFile result = instance.file();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fileTranscoded method, of class DbConnJaMuz.
     */
    @Test
    public void testFileTranscoded() {
        System.out.println("fileTranscoded");
        DbConnJaMuz instance = null;
        DaoFileTranscoded expResult = null;
        DaoFileTranscoded result = instance.fileTranscoded();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of path method, of class DbConnJaMuz.
     */
    @Test
    public void testPath() {
        System.out.println("path");
        DbConnJaMuz instance = null;
        DaoPath expResult = null;
        DaoPath result = instance.path();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of playCounter method, of class DbConnJaMuz.
     */
    @Test
    public void testPlayCounter() {
        System.out.println("playCounter");
        DbConnJaMuz instance = null;
        DaoPlayCounter expResult = null;
        DaoPlayCounter result = instance.playCounter();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of option method, of class DbConnJaMuz.
     */
    @Test
    public void testOption() {
        System.out.println("option");
        DbConnJaMuz instance = null;
        DaoOption expResult = null;
        DaoOption result = instance.option();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of album method, of class DbConnJaMuz.
     */
    @Test
    public void testAlbum() {
        System.out.println("album");
        DbConnJaMuz instance = null;
        DaoPathAlbum expResult = null;
        DaoPathAlbum result = instance.album();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of listModel method, of class DbConnJaMuz.
     */
    @Test
    public void testListModel() {
        System.out.println("listModel");
        DbConnJaMuz instance = null;
        DaoListModel expResult = null;
        DaoListModel result = instance.listModel();
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
     * Test of setUp method, of class DbConnJaMuz.
     */
    @Test
    public void testSetUp() {
        System.out.println("setUp");
        boolean isRemote = false;
        DbConnJaMuz instance = null;
        boolean expResult = false;
        boolean result = instance.setUp(isRemote);
        assertEquals(expResult, result);
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
     * Test of getTags method, of class DbConnJaMuz.
     */
    @Test
    public void testGetTags() {
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

}
