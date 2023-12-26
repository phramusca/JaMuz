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
import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import jamuz.remote.ClientInfo;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
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
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoClientTest {

    private static DbConnJaMuz dbConnJaMuz;

    public DaoClientTest() {
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

    //FIXME ZZZ Refactor client, device, statsource as too much hard-coded weird misusages that makes maintenance very hard
    @Test
    public void testClient() {

        System.out.println("testClient");
        
        //Check no default playlists
        ArrayList<ClientInfo> expectedClients = new ArrayList<>();
		checkClientList(expectedClients);
        
        //Machine needed by Device and StatSource
        String hostname = "testClientMachine";
        dbConnJaMuz.machine().lock().getOrInsert(hostname, new StringBuilder(), true);
        
        //Playlist needed by Device
        Playlist playlist = new Playlist(0, "whateverName",false, 0, Playlist.LimitUnit.Gio, false, Playlist.Type.Albums, Playlist.Match.All, false, "exttt");
        dbConnJaMuz.playlist().lock().insert(playlist);
        
        //Device needed by ClientInfo and StatSource
        Device device = new Device(-1, "logMoreThan5", "source", "clientLogin", 1, hostname, true);
        device.setIdPlaylist(1);
        dbConnJaMuz.device().lock().insertOrUpdate(device);
        device = new Device(1, device.getName(), device.getSource(), device.getDestination(), device.getIdPlaylist(), "", device.isHidden());
        
        //StatSource needed by ClientInfo
        StatSource statSource = new StatSource(hostname);
        dbConnJaMuz.statSource().lock().insertOrUpdate(statSource);
        
        //Creating a ClientInfo with device and statSource (with their id 1 in database)
        ClientInfo clientInfo = new ClientInfo("login", "pwd", "rootPath", "name", true);
        
        clientInfo.setDevice(device);
        statSource.setId(1);
        clientInfo.setStatSource(statSource);
        
        //in case of an error in `dbConnJaMuz.client().lock().insertOrUpdate(clientInfo)`
        Jamuz.setDb(dbConnJaMuz);
        Jamuz.readPlaylists(); 
        
        //When
        dbConnJaMuz.client().lock().insertOrUpdate(clientInfo);
        //Set id and name
        clientInfo = new ClientInfo(1, clientInfo.getLogin(), clientInfo.getName() + "-" + clientInfo.getLogin().substring(0, 5), clientInfo.getPwd(), clientInfo.getDevice(), clientInfo.getStatSource(), true);
        clientInfo.setDevice(device);
        statSource = new StatSource(clientInfo.getLogin());
        statSource.setId(1);
        statSource.setHidden(true);
        clientInfo.setStatSource(statSource);
        expectedClients.add(clientInfo);

        //Then
        checkClientList(expectedClients);
        
        //When 
        ClientInfo get = dbConnJaMuz.client().get(clientInfo.getLogin());
        
        //When using client().get with a login, device's machineName ends up to be set to login
        device = new Device(1, device.getName(), device.getSource(), device.getDestination(), device.getIdPlaylist(), clientInfo.getLogin(), device.isHidden());
        clientInfo.setDevice(device);
        
        //Then
        assertEquals(clientInfo, get);
 
        //When
        clientInfo = new ClientInfo(1, "new login", "new name", "new pwd", device, statSource, false);
        
        dbConnJaMuz.client().lock().insertOrUpdate(clientInfo);

        //Then
        device = new Device(1, device.getName(), device.getSource(), device.getDestination(), device.getIdPlaylist(), "", device.isHidden());
        clientInfo.setDevice(device);
        statSource = new StatSource(clientInfo.getLogin());
        statSource.setId(1);
        statSource.setHidden(true);
        clientInfo.setStatSource(statSource);
        expectedClients.set(0, clientInfo);
        checkClientList(expectedClients);

        // FIXME TEST Check other constraints
        // FIXME TEST Negative cases
    }

    private void checkClientList(ArrayList<ClientInfo> expectedClients) {
		LinkedHashMap<Integer, ClientInfo> clients = new LinkedHashMap<>();
		assertTrue(dbConnJaMuz.client().get(clients));
		//Collections.sort(expectedPlaylists); // getPlaylists() return sorted ?
        // Print arrays for visual inspection
        System.out.println("Actual: " + Arrays.toString(expectedClients.toArray()));
        System.out.println("Expected: " + Arrays.toString(clients.values().toArray()));
        
		assertArrayEquals(expectedClients.toArray(), clients.values().toArray());
	}
    
    /**
     * Test of lock method, of class DaoClient.
     */
    @Test
    @Ignore
    public void testLock() {
        System.out.println("lock");
        DaoClient instance = null;
        DaoClientWrite expResult = null;
        DaoClientWrite result = instance.lock();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class DaoClient.
     */
    @Test
    @Ignore
    public void testGet_String() {
        System.out.println("get");
        String login = "";
        DaoClient instance = null;
        ClientInfo expResult = null;
        ClientInfo result = instance.get(login);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class DaoClient.
     */
    @Test
    @Ignore
    public void testGet_LinkedHashMap() {
        System.out.println("get");
        LinkedHashMap<Integer, ClientInfo> clients = null;
        DaoClient instance = null;
        boolean expResult = false;
        boolean result = instance.get(clients);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
