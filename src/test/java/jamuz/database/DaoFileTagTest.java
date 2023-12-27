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
import static org.junit.Assert.*;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import test.helpers.TestUnitSettings;

/**
 *
 * @author raph
 */
public class DaoFileTagTest {

    public DaoFileTagTest() {
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
    public void testFileTag() {

        System.out.println("testFileTag");
		
		//Given
		dbConnJaMuz.tag().lock().insert("frefrfe");
		dbConnJaMuz.tag().lock().insert("mpomoipm");
		dbConnJaMuz.tag().lock().insert("5165vge");
		dbConnJaMuz.tag().lock().insert("wqs:");
		

//		dbConnJaMuz.fileTag().get(tags, idFile);
		
		ArrayList<? extends FileInfo> files = new ArrayList<>();

		files.add(new FileInfoInt())
		
		dbConnJaMuz.fileTag().lock().update(files, results);
		
        //FIXME TEST Make unit test
        //FIXME TEST Negative cases
        //FIXME TEST Check other constraints
    }

    /**
     * Test of lock method, of class DaoFileTag.
     */
    @Test
    public void testLock() {
        System.out.println("lock");
        DaoFileTag instance = null;
        DaoFileTagWrite expResult = null;
        DaoFileTagWrite result = instance.lock();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class DaoFileTag.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        ArrayList<String> tags = null;
        int idFile = 0;
        DaoFileTag instance = null;
        boolean expResult = false;
        boolean result = instance.get(tags, idFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
