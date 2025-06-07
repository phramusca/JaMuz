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

import jamuz.process.check.DuplicateInfo;
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
public class DaoPathAlbumTest {
    
    public DaoPathAlbumTest() {
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
     * Test of lock method, of class DaoPathAlbum.
     */
    @Test
    public void testLock() {
        System.out.println("lock");
        DaoPathAlbum instance = null;
        DaoPathAlbumWrite expResult = null;
        DaoPathAlbumWrite result = instance.lock();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkSimilar method, of class DaoPathAlbum.
     */
    @Test
    public void testCheckSimilar() {
        System.out.println("checkSimilar");
        ArrayList<DuplicateInfo> myList = null;
        String album = "";
        int idPath = 0;
        DaoPathAlbum instance = null;
        boolean expResult = false;
        boolean result = instance.checkSimilar(myList, album, idPath);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkExact method, of class DaoPathAlbum.
     */
    @Test
    public void testCheckExact() {
        System.out.println("checkExact");
        ArrayList<DuplicateInfo> myList = null;
        String album = "";
        int idPath = 0;
        DaoPathAlbum instance = null;
        boolean expResult = false;
        boolean result = instance.checkExact(myList, album, idPath);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkDuplicate method, of class DaoPathAlbum.
     */
    @Test
    public void testCheckDuplicate_ArrayList_String() {
        System.out.println("checkDuplicate");
        ArrayList<DuplicateInfo> myList = null;
        String mbId = "";
        DaoPathAlbum instance = null;
        boolean expResult = false;
        boolean result = instance.checkDuplicate(myList, mbId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkDuplicate method, of class DaoPathAlbum.
     */
    @Test
    public void testCheckDuplicate_6args() {
        System.out.println("checkDuplicate");
        ArrayList<DuplicateInfo> myList = null;
        String albumArtist = "";
        String album = "";
        int idPath = 0;
        int discNo = 0;
        int discTotal = 0;
        DaoPathAlbum instance = null;
        boolean expResult = false;
        boolean result = instance.checkDuplicate(myList, albumArtist, album, idPath, discNo, discTotal);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of checkDuplicate method, of class DaoPathAlbum.
     */
    @Test
    public void testCheckDuplicate_4args() {
        System.out.println("checkDuplicate");
        ArrayList<DuplicateInfo> myList = null;
        String albumArtist = "";
        String album = "";
        int idPath = 0;
        DaoPathAlbum instance = null;
        boolean expResult = false;
        boolean result = instance.checkDuplicate(myList, albumArtist, album, idPath);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
