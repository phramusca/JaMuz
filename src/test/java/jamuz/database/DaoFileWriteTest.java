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

import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;
import java.io.IOException;
import java.sql.SQLException;

import jamuz.FileInfo;
import jamuz.FileInfoInt;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author raph
 */
public class DaoFileWriteTest {
    
    public DaoFileWriteTest() {
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
     * Test of insert method, of class DaoFileWrite.
     */
    @Test
    public void testInsert() {
        System.out.println("insert");
        FileInfoInt fileInfo = null;
        int[] key = null;
        DaoFileWrite instance = null;
        boolean expResult = false;
        boolean result = instance.insert(fileInfo, key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class DaoFileWrite.
     */
    @Test
    public void testDelete() {
        System.out.println("delete");
        int idFile = 0;
        DaoFileWrite instance = null;
        boolean expResult = false;
        boolean result = instance.delete(idFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSaved method, of class DaoFileWrite.
     */
    @Test
    public void testSetSaved() {
        System.out.println("setSaved");
        int idFile = 0;
        DaoFileWrite instance = null;
        boolean expResult = false;
        boolean result = instance.setSaved(idFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class DaoFileWrite.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        FileInfoInt fileInfo = null;
        DaoFileWrite instance = null;
        boolean expResult = false;
        boolean result = instance.update(fileInfo);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateLastPlayedAndCounter method, of class DaoFileWrite.
     */
    @Test
    public void testUpdateLastPlayedAndCounter() {
        System.out.println("updateLastPlayedAndCounter");
        FileInfoInt fileInfo = null;
        DaoFileWrite instance = null;
        boolean expResult = false;
        boolean result = instance.updateLastPlayedAndCounter(fileInfo);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateRating method, of class DaoFileWrite.
     */
    @Test
    public void testUpdateRating() {
        System.out.println("updateRating");
        FileInfoInt fileInfo = null;
        DaoFileWrite instance = null;
        boolean expResult = false;
        boolean result = instance.updateRating(fileInfo);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateFileGenre method, of class DaoFileWrite.
     */
    @Test
    public void testUpdateFileGenre() {
        System.out.println("updateFileGenre");
        FileInfoInt fileInfo = null;
        DaoFileWrite instance = null;
        boolean expResult = false;
        boolean result = instance.updateFileGenre(fileInfo);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateIdPath method, of class DaoFileWrite.
     */
    @Test
    public void testUpdateIdPath() {
        System.out.println("updateIdPath");
        int idPath = 0;
        int newIdPath = 0;
        DaoFileWrite instance = null;
        boolean expResult = false;
        boolean result = instance.updateIdPath(idPath, newIdPath);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateModifDate method, of class DaoFileWrite.
     */
    @Test
    public void testUpdateModifDate_3args() {
        System.out.println("updateModifDate");
        int idFile = 0;
        Date modifDate = null;
        String name = "";
        DaoFileWrite instance = null;
        boolean expResult = false;
        boolean result = instance.updateModifDate(idFile, modifDate, name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateModifDate method, of class DaoFileWrite.
     */
    @Test
    public void testUpdateModifDate_String() {
        System.out.println("updateModifDate");
        String newTag = "";
        DaoFileWrite instance = null;
        boolean expResult = false;
        boolean result = instance.updateModifDate(newTag);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateModifDate method, of class DaoFileWrite.
     */
    @Test
    public void testUpdateModifDate_FileInfo() {
        System.out.println("updateModifDate");
        FileInfo fileInfo = null;
        DaoFileWrite instance = null;
        boolean expResult = false;
        boolean result = instance.updateModifDate(fileInfo);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
