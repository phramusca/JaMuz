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
import java.sql.ResultSet;
import java.util.ArrayList;
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
public class StatSourceSQLTest {
    
    public StatSourceSQLTest() {
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
     * Test of getDbConn method, of class StatSourceSQL.
     */
    @Test
    public void testGetDbConn() {
        System.out.println("getDbConn");
        StatSourceSQL instance = null;
        DbConn expResult = null;
        DbConn result = instance.getDbConn();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStatistics method, of class StatSourceSQL.
     */
    @Test
    public void testGetStatistics() {
        System.out.println("getStatistics");
        ArrayList<FileInfo> files = null;
        StatSourceSQL instance = null;
        boolean expResult = false;
        boolean result = instance.getStatistics(files);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFileStatistics method, of class StatSourceSQL.
     */
    @Test
    public void testGetFileStatistics() {
        System.out.println("getFileStatistics");
        ResultSet rs = null;
        StatSourceSQL instance = null;
        FileInfo expResult = null;
        FileInfo result = instance.getFileStatistics(rs);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateFileStatistics method, of class StatSourceSQL.
     */
    @Test
    public void testUpdateFileStatistics() {
        System.out.println("updateFileStatistics");
        ArrayList<? extends FileInfo> files = null;
        StatSourceSQL instance = null;
        int[] expResult = null;
        int[] result = instance.updateFileStatistics(files);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setUpdateStatisticsParameters method, of class StatSourceSQL.
     */
    @Test
    public void testSetUpdateStatisticsParameters() throws Exception {
        System.out.println("setUpdateStatisticsParameters");
        FileInfo file = null;
        StatSourceSQL instance = null;
        instance.setUpdateStatisticsParameters(file);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPath method, of class StatSourceSQL.
     */
    @Test
    public void testGetPath() {
        System.out.println("getPath");
        String path = "";
        StatSourceSQL instance = null;
        String expResult = "";
        String result = instance.getPath(path);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of check method, of class StatSourceSQL.
     */
    @Test
    public void testCheck() {
        System.out.println("check");
        StatSourceSQL instance = null;
        boolean expResult = false;
        boolean result = instance.check();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSource method, of class StatSourceSQL.
     */
    @Test
    public void testGetSource() {
        System.out.println("getSource");
        String locationWork = "";
        StatSourceSQL instance = null;
        boolean expResult = false;
        boolean result = instance.getSource(locationWork);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sendSource method, of class StatSourceSQL.
     */
    @Test
    public void testSendSource() {
        System.out.println("sendSource");
        String locationWork = "";
        StatSourceSQL instance = null;
        boolean expResult = false;
        boolean result = instance.sendSource(locationWork);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of backupSource method, of class StatSourceSQL.
     */
    @Test
    public void testBackupSource() {
        System.out.println("backupSource");
        String locationWork = "";
        StatSourceSQL instance = null;
        boolean expResult = false;
        boolean result = instance.backupSource(locationWork);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of tearDown method, of class StatSourceSQL.
     */
    @Test
    public void testTearDown() {
        System.out.println("tearDown");
        StatSourceSQL instance = null;
        boolean expResult = false;
        boolean result = instance.tearDown();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
   
}
