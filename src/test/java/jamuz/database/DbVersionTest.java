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

import java.util.Date;
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
public class DbVersionTest {
    
    public DbVersionTest() {
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
     * Test of getVersion method, of class DbVersion.
     */
    @Test
    public void testGetVersion() {
        System.out.println("getVersion");
        DbVersion instance = null;
        int expResult = 0;
        int result = instance.getVersion();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUpgradeStart method, of class DbVersion.
     */
    @Test
    public void testGetUpgradeStart() {
        System.out.println("getUpgradeStart");
        DbVersion instance = null;
        Date expResult = null;
        Date result = instance.getUpgradeStart();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUpgradeEnd method, of class DbVersion.
     */
    @Test
    public void testGetUpgradeEnd() {
        System.out.println("getUpgradeEnd");
        DbVersion instance = null;
        Date expResult = null;
        Date result = instance.getUpgradeEnd();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}