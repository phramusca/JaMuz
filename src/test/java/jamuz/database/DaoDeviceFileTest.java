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
import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import test.helpers.TestUnitSettings;

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

        //Inserting files
        ArrayList<FileInfoInt> files = new ArrayList<>();
        FileInfoInt fileInfoInt = new FileInfoInt("/relative/path", "/root/path");
        files.add(fileInfoInt);
        int[] key = new int[1];
        dbConnJaMuz.file().lock().insert(fileInfoInt, key);

        //When
        dbConnJaMuz.deviceFile().lock().insertOrIgnore(files, device.getId());

        //Then
        dbConnJaMuz.setUp(true);
        ArrayList<FileInfo> filesGet = new ArrayList<>();
        dbConnJaMuz.getStatistics(filesGet, statSource);

        //FIXME TEST ! Assert insertion, then test others cases: other insertion, other gets, and update and delete too
        
        //FIXME TEST Negative cases
        //FIXME TEST Check other constraints
    }

    /**
     * Test of lock method, of class DaoDeviceFile.
     */
    @Test
    @Ignore
    public void testLock() {
        System.out.println("lock");
        DaoDeviceFile instance = null;
        DaoDeviceFileWrite expResult = null;
        DaoDeviceFileWrite result = instance.lock();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
