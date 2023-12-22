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
public class StatSourceAbstractTest {
    
    public StatSourceAbstractTest() {
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
     * Test of getRootPath method, of class StatSourceAbstract.
     */
    @Test
    public void testGetRootPath() {
        System.out.println("getRootPath");
        StatSourceAbstract instance = null;
        String expResult = "";
        String result = instance.getRootPath();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setRootPath method, of class StatSourceAbstract.
     */
    @Test
    public void testSetRootPath() {
        System.out.println("setRootPath");
        String rootPath = "";
        StatSourceAbstract instance = null;
        instance.setRootPath(rootPath);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLocation method, of class StatSourceAbstract.
     */
    @Test
    public void testGetLocation() {
        System.out.println("getLocation");
        StatSourceAbstract instance = null;
        String expResult = "";
        String result = instance.getLocation();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLocation method, of class StatSourceAbstract.
     */
    @Test
    public void testSetLocation() {
        System.out.println("setLocation");
        String location = "";
        StatSourceAbstract instance = null;
        instance.setLocation(location);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getName method, of class StatSourceAbstract.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        StatSourceAbstract instance = null;
        String expResult = "";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setName method, of class StatSourceAbstract.
     */
    @Test
    public void testSetName() {
        System.out.println("setName");
        String name = "";
        StatSourceAbstract instance = null;
        instance.setName(name);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isUpdateAddedDate method, of class StatSourceAbstract.
     */
    @Test
    public void testIsUpdateAddedDate() {
        System.out.println("isUpdateAddedDate");
        StatSourceAbstract instance = null;
        boolean expResult = false;
        boolean result = instance.isUpdateAddedDate();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isUpdateLastPlayed method, of class StatSourceAbstract.
     */
    @Test
    public void testIsUpdateLastPlayed() {
        System.out.println("isUpdateLastPlayed");
        StatSourceAbstract instance = null;
        boolean expResult = false;
        boolean result = instance.isUpdateLastPlayed();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isUpdateBPM method, of class StatSourceAbstract.
     */
    @Test
    public void testIsUpdateBPM() {
        System.out.println("isUpdateBPM");
        StatSourceAbstract instance = null;
        boolean expResult = false;
        boolean result = instance.isUpdateBPM();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isUpdateTags method, of class StatSourceAbstract.
     */
    @Test
    public void testIsUpdateTags() {
        System.out.println("isUpdateTags");
        StatSourceAbstract instance = null;
        boolean expResult = false;
        boolean result = instance.isUpdateTags();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isUpdateGenre method, of class StatSourceAbstract.
     */
    @Test
    public void testIsUpdateGenre() {
        System.out.println("isUpdateGenre");
        StatSourceAbstract instance = null;
        boolean expResult = false;
        boolean result = instance.isUpdateGenre();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isUpdatePlayCounter method, of class StatSourceAbstract.
     */
    @Test
    public void testIsUpdatePlayCounter() {
        System.out.println("isUpdatePlayCounter");
        StatSourceAbstract instance = null;
        boolean expResult = false;
        boolean result = instance.isUpdatePlayCounter();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStatistics method, of class StatSourceAbstract.
     */
    @Test
    public void testGetStatistics() {
        System.out.println("getStatistics");
        ArrayList<FileInfo> files = null;
        StatSourceAbstract instance = null;
        boolean expResult = false;
        boolean result = instance.getStatistics(files);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateFileStatistics method, of class StatSourceAbstract.
     */
    @Test
    public void testUpdateFileStatistics() {
        System.out.println("updateFileStatistics");
        ArrayList<? extends FileInfo> files = null;
        StatSourceAbstract instance = null;
        int[] expResult = null;
        int[] result = instance.updateFileStatistics(files);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTags method, of class StatSourceAbstract.
     */
    @Test
    public void testGetTags() {
        System.out.println("getTags");
        ArrayList<String> tags = null;
        FileInfo file = null;
        StatSourceAbstract instance = null;
        boolean expResult = false;
        boolean result = instance.getTags(tags, file);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setUp method, of class StatSourceAbstract.
     */
    @Test
    public void testSetUp() {
        System.out.println("setUp");
        boolean isRemote = false;
        StatSourceAbstract instance = null;
        boolean expResult = false;
        boolean result = instance.setUp(isRemote);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of tearDown method, of class StatSourceAbstract.
     */
    @Test
    public void testTearDown() {
        System.out.println("tearDown");
        StatSourceAbstract instance = null;
        boolean expResult = false;
        boolean result = instance.tearDown();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of check method, of class StatSourceAbstract.
     */
    @Test
    public void testCheck() {
        System.out.println("check");
        StatSourceAbstract instance = null;
        boolean expResult = false;
        boolean result = instance.check();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSource method, of class StatSourceAbstract.
     */
    @Test
    public void testGetSource() {
        System.out.println("getSource");
        String locationWork = "";
        StatSourceAbstract instance = null;
        boolean expResult = false;
        boolean result = instance.getSource(locationWork);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sendSource method, of class StatSourceAbstract.
     */
    @Test
    public void testSendSource() {
        System.out.println("sendSource");
        String locationWork = "";
        StatSourceAbstract instance = null;
        boolean expResult = false;
        boolean result = instance.sendSource(locationWork);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of backupSource method, of class StatSourceAbstract.
     */
    @Test
    public void testBackupSource() {
        System.out.println("backupSource");
        String locationWork = "";
        StatSourceAbstract instance = null;
        boolean expResult = false;
        boolean result = instance.backupSource(locationWork);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class StatSourceAbstract.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        StatSourceAbstract instance = null;
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class StatSourceAbstract.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        StatSourceAbstract instance = null;
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class StatSourceAbstractImpl extends StatSourceAbstract {

        public StatSourceAbstractImpl() {
            super("", "", "", false, false, false, false, false, false);
        }

        public boolean getStatistics(ArrayList<FileInfo> files) {
            return false;
        }

        public int[] updateFileStatistics(ArrayList<? extends FileInfo> files) {
            return null;
        }

        public boolean getTags(ArrayList<String> tags, FileInfo file) {
            return false;
        }

        public boolean setUp(boolean isRemote) {
            return false;
        }

        public boolean tearDown() {
            return false;
        }

        public boolean check() {
            return false;
        }

        public boolean getSource(String locationWork) {
            return false;
        }

        public boolean sendSource(String locationWork) {
            return false;
        }

        public boolean backupSource(String locationWork) {
            return false;
        }
    }

    public class StatSourceAbstractImpl extends StatSourceAbstract {

        public StatSourceAbstractImpl() {
            super("", "", "", false, false, false, false, false, false);
        }

        public boolean getStatistics(ArrayList<FileInfo> files) {
            return false;
        }

        public int[] updateFileStatistics(ArrayList<? extends FileInfo> files) {
            return null;
        }

        public boolean getTags(ArrayList<String> tags, FileInfo file) {
            return false;
        }

        public boolean setUp(boolean isRemote) {
            return false;
        }

        public boolean tearDown() {
            return false;
        }

        public boolean check() {
            return false;
        }

        public boolean getSource(String locationWork) {
            return false;
        }

        public boolean sendSource(String locationWork) {
            return false;
        }

        public boolean backupSource(String locationWork) {
            return false;
        }
    }

    public class StatSourceAbstractImpl extends StatSourceAbstract {

        public StatSourceAbstractImpl() {
            super("", "", "", false, false, false, false, false, false);
        }

        public boolean getStatistics(ArrayList<FileInfo> files) {
            return false;
        }

        public int[] updateFileStatistics(ArrayList<? extends FileInfo> files) {
            return null;
        }

        public boolean getTags(ArrayList<String> tags, FileInfo file) {
            return false;
        }

        public boolean setUp(boolean isRemote) {
            return false;
        }

        public boolean tearDown() {
            return false;
        }

        public boolean check() {
            return false;
        }

        public boolean getSource(String locationWork) {
            return false;
        }

        public boolean sendSource(String locationWork) {
            return false;
        }

        public boolean backupSource(String locationWork) {
            return false;
        }
    }
    
}
