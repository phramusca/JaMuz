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

import jamuz.FileInfoInt;
import jamuz.process.sync.SyncStatus;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class DaoDeviceFileWriteTest {
    
    public DaoDeviceFileWriteTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
    /**
     * Test of insertOrUpdate method, of class DaoDeviceFileWrite.
     */
    @Test
    public void testInsertOrUpdate() {
        System.out.println("insertOrUpdate");
        ArrayList<FileInfoInt> files = null;
        int idDevice = 0;
        DaoDeviceFileWrite instance = null;
        ArrayList<FileInfoInt> expResult = null;
        ArrayList<FileInfoInt> result = instance.insertOrUpdate(files, idDevice);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insertOrIgnore method, of class DaoDeviceFileWrite.
     */
    @Test
    public void testInsertOrIgnore_ArrayList_int() {
        System.out.println("insertOrIgnore");
        ArrayList<FileInfoInt> files = null;
        int idDevice = 0;
        DaoDeviceFileWrite instance = null;
        ArrayList<FileInfoInt> expResult = null;
        ArrayList<FileInfoInt> result = instance.insertOrIgnore(files, idDevice);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of insertOrIgnore method, of class DaoDeviceFileWrite.
     */
    @Test
    public void testInsertOrIgnore_int_FileInfoInt() {
        System.out.println("insertOrIgnore");
        int idDevice = 0;
        FileInfoInt file = null;
        DaoDeviceFileWrite instance = null;
        boolean expResult = false;
        boolean result = instance.insertOrIgnore(idDevice, file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class DaoDeviceFileWrite.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        SyncStatus status = null;
        int idFile = 0;
        int idDevice = 0;
        DaoDeviceFileWrite instance = null;
        boolean expResult = false;
        boolean result = instance.update(status, idFile, idDevice);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class DaoDeviceFileWrite.
     */
    @Test
    public void testDelete() {
        System.out.println("delete");
        int idDevice = 0;
        DaoDeviceFileWrite instance = null;
        boolean expResult = false;
        boolean result = instance.delete(idDevice);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
