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

import jamuz.process.sync.Device;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;

/**
 *
 * @author raph
 */
public class DaoDeviceTest {

    public DaoDeviceTest() {
    }

    private static DbConnJaMuz dbConnJaMuz;
    
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
	public void testxxxxxxxxxxxxxx() {

		System.out.println("testxxxxxxxxxxxxxx");

        //FIXME TEST Make unit test
		//FIXME TEST Negative cases
		//FIXME TEST Check other constraints
	}
    
    /**
     * Test of lock method, of class DaoDevice.
     */
    @Test
    public void testLock() {
        System.out.println("lock");
        DaoDevice instance = null;
        DaoDeviceWrite expResult = null;
        DaoDeviceWrite result = instance.lock();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class DaoDevice.
     */
    @Test
    public void testGet_String() {
        System.out.println("get");
        String login = "";
        DaoDevice instance = null;
        Device expResult = null;
        Device result = instance.get(login);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class DaoDevice.
     */
    @Test
    public void testGet_3args_1() {
        System.out.println("get");
        LinkedHashMap<Integer, Device> devices = null;
        String hostname = "";
        boolean hidden = false;
        DaoDevice instance = null;
        boolean expResult = false;
        boolean result = instance.get(devices, hostname, hidden);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class DaoDevice.
     */
    @Test
    public void testGet_3args_2() throws Exception {
        System.out.println("get");
        ResultSet rs = null;
        String hostname = "";
        boolean hidden = false;
        DaoDevice instance = null;
        Device expResult = null;
        Device result = instance.get(rs, hostname, hidden);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
