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
package jamuz;

import java.io.File;
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
public class AppVersionTest {
    
    public AppVersionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of isNewVersion method, of class AppVersion.
     */
    @Test
    public void testIsNewVersion() {
        System.out.println("isNewVersion");
        AppVersion instance = null;
        boolean expResult = false;
        boolean result = instance.isNewVersion();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setAsset method, of class AppVersion.
     */
    @Test
    public void testSetAsset() {
        System.out.println("setAsset");
        File assetFile = null;
        int size = 0;
        AppVersion instance = null;
        instance.setAsset(assetFile, size);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setLatestVersion method, of class AppVersion.
     */
    @Test
    public void testSetLatestVersion() {
        System.out.println("setLatestVersion");
        String latestVersion = "";
        AppVersion instance = null;
        instance.setLatestVersion(latestVersion);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCurrentVersion method, of class AppVersion.
     */
    @Test
    public void testGetCurrentVersion() {
        System.out.println("getCurrentVersion");
        AppVersion instance = null;
        String expResult = "";
        String result = instance.getCurrentVersion();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLatestVersion method, of class AppVersion.
     */
    @Test
    public void testGetLatestVersion() {
        System.out.println("getLatestVersion");
        AppVersion instance = null;
        String expResult = "";
        String result = instance.getLatestVersion();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAssetFile method, of class AppVersion.
     */
    @Test
    public void testGetAssetFile() {
        System.out.println("getAssetFile");
        AppVersion instance = null;
        File expResult = null;
        File result = instance.getAssetFile();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class AppVersion.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        AppVersion instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isAssetValid method, of class AppVersion.
     */
    @Test
    public void testIsAssetValid() {
        System.out.println("isAssetValid");
        AppVersion instance = null;
        boolean expResult = false;
        boolean result = instance.isAssetValid();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isUnzippedAssetValid method, of class AppVersion.
     */
    @Test
    public void testIsUnzippedAssetValid() {
        System.out.println("isUnzippedAssetValid");
        AppVersion instance = null;
        boolean expResult = false;
        boolean result = instance.isUnzippedAssetValid();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of unzipAsset method, of class AppVersion.
     */
    @Test
    public void testUnzipAsset() throws Exception {
        System.out.println("unzipAsset");
        ICallBackVersionCheck callBackVersionCheck = null;
        AppVersion instance = null;
        instance.unzipAsset(callBackVersionCheck);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
