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

    @Test
    public void testClient() {

        System.out.println("testClient");
        String hostname = "testClientMachine";
        dbConnJaMuz.machine().lock().getOrInsert(hostname, new StringBuilder(), true);
        Playlist playlist = new Playlist(0, "whateverName",false, 0, Playlist.LimitUnit.Gio, false, Playlist.Type.Albums, Playlist.Match.All, false, "exttt");
        dbConnJaMuz.playlist().lock().insert(playlist);
        Device device = new Device(-1, "login", "source", "clientLogin", 1, hostname, true);
        device.setIdPlaylist(1);
        dbConnJaMuz.device().lock().insertOrUpdate(device);
        StatSource statSource = new StatSource(hostname);
        dbConnJaMuz.statSource().lock().insertOrUpdate(statSource);
        ClientInfo clientInfo = new ClientInfo("login", "pwd", "rootPath", "name", true);
        clientInfo.setDevice(new Device(1, hostname, "", "", 1, hostname, true));
        statSource.setId(1);
        clientInfo.setStatSource(statSource);
        Jamuz.setDb(dbConnJaMuz);
        Jamuz.readPlaylists(); //in case of an error in below method
        
        //When
        dbConnJaMuz.client().lock().insertOrUpdate(clientInfo);

        //Then
        //FIXME TEST: Assert
        
        // FIXME TEST Check all those methods
        
        
        clientInfo.setConnected(true);

        clientInfo = new ClientInfo(1, clientInfo.getLogin(), clientInfo.getName(), clientInfo.getPwd(), clientInfo.getDevice(), clientInfo.getStatSource(), true);
        dbConnJaMuz.client().lock().insertOrUpdate(clientInfo);

        dbConnJaMuz.client().get("login");

        LinkedHashMap<Integer, ClientInfo> clients = new LinkedHashMap<>();

        dbConnJaMuz.client().get(clients);

        // FIXME TEST Check other constraints
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
