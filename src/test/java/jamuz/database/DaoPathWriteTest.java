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
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author raph
 */
public class DaoPathWriteTest {
    
    public DaoPathWriteTest() {
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
     * Test of insert method, of class DaoPathWrite.
     */
    @Test
    public void testInsert() {
        System.out.println("insert");
        String relativePath = "";
        Date modifDate = null;
        FolderInfo.CheckedFlag checkedFlag = null;
        String mbId = "";
        int[] key = null;
        DaoPathWrite instance = null;
        boolean expResult = false;
        boolean result = instance.insert(relativePath, modifDate, checkedFlag, mbId, key);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class DaoPathWrite.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        int idPath = 0;
        Date modifDate = null;
        FolderInfo.CheckedFlag checkedFlag = null;
        String path = "";
        String mbId = "";
        DaoPathWrite instance = null;
        boolean expResult = false;
        boolean result = instance.update(idPath, modifDate, checkedFlag, path, mbId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateCheckedFlag method, of class DaoPathWrite.
     */
    @Test
    public void testUpdateCheckedFlag() {
        System.out.println("updateCheckedFlag");
        int idPath = 0;
        FolderInfo.CheckedFlag checkedFlag = null;
        DaoPathWrite instance = null;
        boolean expResult = false;
        boolean result = instance.updateCheckedFlag(idPath, checkedFlag);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateCheckedFlagReset method, of class DaoPathWrite.
     */
    @Test
    public void testUpdateCheckedFlagReset() {
        System.out.println("updateCheckedFlagReset");
        FolderInfo.CheckedFlag checkedFlag = null;
        DaoPathWrite instance = null;
        boolean expResult = false;
        boolean result = instance.updateCheckedFlagReset(checkedFlag);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateCopyRight method, of class DaoPathWrite.
     */
    @Test
    public void testUpdateCopyRight() {
        System.out.println("updateCopyRight");
        int idPath = 0;
        int copyRight = 0;
        DaoPathWrite instance = null;
        boolean expResult = false;
        boolean result = instance.updateCopyRight(idPath, copyRight);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class DaoPathWrite.
     */
    @Test
    public void testDelete() {
        System.out.println("delete");
        int idPath = 0;
        DaoPathWrite instance = null;
        boolean expResult = false;
        boolean result = instance.delete(idPath);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
