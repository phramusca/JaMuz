/*
 * Copyright (C) 2023 raph
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
import jamuz.Playlist;
import jamuz.process.check.FolderInfo;
import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import test.helpers.TestUnitSettings;
import java.time.Instant;
import java.time.Duration;

/**
 *
 * @author raph
 */
public class DaoDeviceFileTest {

    private static DbConnJaMuz dbConnJaMuz;

    public DaoDeviceFileTest() {
    }

    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
    }

    @AfterClass
    public static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDeviceFile() {

        System.out.println("testDeviceFile");

        //Given
        //Playlist needed by Device
        Playlist playlist = new Playlist(0, "whateverName", false, 0, Playlist.LimitUnit.Gio, false, Playlist.Type.Albums, Playlist.Match.All, false, "exttt");
        dbConnJaMuz.playlist().lock().insert(playlist);

        //Machine needed by Device
        String hostname = "testClientMachine";
        dbConnJaMuz.machine().lock().getOrInsert(hostname, new StringBuilder(), true);

        //Device need by deviceFile
        Device device = new Device(-1, "name", "source", "clientLogin", 1, hostname, true);
        device.setIdPlaylist(1);
        dbConnJaMuz.device().lock().insertOrUpdate(device);
        device = new Device(1, "name", "source", "clientLogin", 1, hostname, true);

        //StatSource to link to Device
        StatSource statSource = new StatSource(hostname);
        dbConnJaMuz.statSource().lock().insertOrUpdate(statSource);
        statSource.setId(1);
        

        //File need a path
        int[] keyPath = new int[1];
        dbConnJaMuz.path().lock().insert("relative/path/", new Date(), FolderInfo.CheckedFlag.UNCHECKED, "mbId", keyPath);
        
        //Inserting files
        ArrayList<FileInfoInt> expectedFiles = new ArrayList<>();
        FileInfoInt fileInfoInt = new FileInfoInt("relative/path/file.ext", "/root/path");
        fileInfoInt.setIdPath(keyPath[0]);
        expectedFiles.add(fileInfoInt);
        int[] keyFile = new int[1];
        dbConnJaMuz.file().lock().insert(fileInfoInt, keyFile);
        fileInfoInt.setIdFile(keyFile[0]);
		fileInfoInt.setRating(0); //forced to 0 on insertion

        //When
        dbConnJaMuz.deviceFile().lock().insertOrIgnore(expectedFiles, device.getId());

        //Then
        dbConnJaMuz.setUp(true);
        checkFilesList(expectedFiles, statSource);
		
        //FIXME TEST Test insertOrUpdate, delete, and maybe other get (select from deviceFile, there are a few others)
//		dbConnJaMuz.deviceFile().lock().insertOrUpdate(expectedFiles, device.getId());
//		dbConnJaMuz.deviceFile().lock().delete(idDevice);
		
        //FIXME TEST Negative cases
        //FIXME TEST Check other constraints
    }

    private void checkFilesList(ArrayList<FileInfoInt> expectedFiles, StatSource statSource) {
//		LinkedHashMap<Integer, ClientInfo> clients = new LinkedHashMap<>();
        ArrayList<FileInfo> files = new ArrayList<>();
		assertTrue(dbConnJaMuz.getStatistics(files, statSource));
		
		//Cannot use assertArrayEquals as equals in FileInfoInt is limited for other usage
//		Collections.sort(expectedClients);
//		assertArrayEquals(expectedFiles.toArray(), files.toArray());

		int i = 0;
		FileInfo actualFile;
		for (FileInfoInt expectedFile : expectedFiles) {
			actualFile = files.get(i);
			assertEquals(expectedFile.getIdFile(), actualFile.getIdFile());		
			assertEquals(expectedFile.getIdPath(), actualFile.getIdPath());
			assertEquals(expectedFile.getBPM(), actualFile.getBPM(), 0);
			assertEquals(expectedFile.getExt(), actualFile.getExt());
			assertEquals(expectedFile.getFilename(), actualFile.getFilename());
			assertEquals(expectedFile.getFormattedGenreModifDate(), actualFile.getFormattedGenreModifDate());
			assertEquals(expectedFile.getFormattedLastPlayed(), actualFile.getFormattedLastPlayed());
			assertEquals(expectedFile.getFormattedRatingModifDate(), actualFile.getFormattedRatingModifDate());
			assertEquals(expectedFile.getFormattedTagsModifDate(), actualFile.getFormattedTagsModifDate());
			assertEquals(expectedFile.getGenre(), actualFile.getGenre());
			assertEquals(expectedFile.getGenreModifDate(), actualFile.getGenreModifDate());
			assertEquals(expectedFile.getLastPlayed(), actualFile.getLastPlayed());
			assertEquals(expectedFile.getLastPlayedLocalTime(), actualFile.getLastPlayedLocalTime());
			assertEquals(expectedFile.getPlayCounter(), actualFile.getPlayCounter());
			assertEquals(expectedFile.getPreviousPlayCounter(), actualFile.getPreviousPlayCounter());
			assertEquals(expectedFile.getRating(), actualFile.getRating());
			assertEquals(expectedFile.getRatingModifDate(), actualFile.getRatingModifDate());
			assertEquals(expectedFile.getRelativeFullPath(), actualFile.getRelativeFullPath());
			assertEquals(expectedFile.getRelativePath(), actualFile.getRelativePath());
			
			//AddedDate is inserted with `datetime('now')` in db, so need to check it is close to current datetime
			long toleranceSeconds = 5;
			Instant currentInstant = Instant.now();
			assertTrue("addedDate is not close to the current date and time", Duration.between( currentInstant, actualFile.getAddedDate().toInstant()).abs().getSeconds() <= toleranceSeconds);
			//Not checking those convertions here. Refer to DateTimeUtils and DaoFile tests
//			assertEquals(expectedFile.getAddedDateLocalTime(), actualFile.getAddedDateLocalTime());
//			assertEquals(expectedFile.getFormattedAddedDate(), actualFile.getFormattedAddedDate());
			
			// Source differ as for one it is "file" and "Jamuz" for the other
//			assertEquals(expectedFile.getSourceName(), actualFile.getSourceName());
			//FileInfoInt reads tags from database but not FileInfo (only for stats)
//			Jamuz.setDb(dbConnJaMuz);
//			assertEquals(expectedFile.getTags(), actualFile.getTags());
			assertEquals(expectedFile.getTagsModifDate(), actualFile.getTagsModifDate());
			assertEquals(expectedFile.getTagsToString(), actualFile.getTagsToString());
		}
	}
    
    /**
     * Test of lock method, of class DaoDeviceFile.
     */
    @Test
    @Ignore
    public void testLock() {
        fail("The test case is a prototype.");
    }

}
