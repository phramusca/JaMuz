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

import jamuz.process.check.FolderInfo;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;
import java.io.IOException;
import java.sql.SQLException;


/**
 *
 * @author raph
 */
public class DaoPathTest {
    
    public DaoPathTest() {
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
     * Test of lock method, of class DaoPath.
     */
    @Test
    public void testLock() {
        System.out.println("lock");
        DaoPath instance = null;
        DaoPathWrite expResult = null;
        DaoPathWrite result = instance.lock();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class DaoPath.
     */
    @Test
    public void testGet_ConcurrentHashMap_FolderInfoCheckedFlag() {
        System.out.println("get");
        ConcurrentHashMap<String, FolderInfo> folders = null;
        FolderInfo.CheckedFlag checkedFlag = null;
        DaoPath instance = null;
        boolean expResult = false;
        boolean result = instance.get(folders, checkedFlag);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class DaoPath.
     */
    @Test
    public void testGet_ConcurrentHashMap_int() {
        System.out.println("get");
        ConcurrentHashMap<String, FolderInfo> folders = null;
        int idPath = 0;
        DaoPath instance = null;
        boolean expResult = false;
        boolean result = instance.get(folders, idPath);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class DaoPath.
     */
    @Test
    public void testGet_int() {
        System.out.println("get");
        int idPath = 0;
        DaoPath instance = null;
        FolderInfo expResult = null;
        FolderInfo result = instance.get(idPath);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class DaoPath.
     */
    @Test
    public void testGet_ConcurrentHashMap() {
        System.out.println("get");
        ConcurrentHashMap<String, FolderInfo> folders = null;
        DaoPath instance = null;
        boolean expResult = false;
        boolean result = instance.get(folders);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIdPath method, of class DaoPath.
     */
    @Test
    public void testGetIdPath() {
        System.out.println("getIdPath");
        String path = "";
        DaoPath instance = null;
        int expResult = 0;
        int result = instance.getIdPath(path);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
