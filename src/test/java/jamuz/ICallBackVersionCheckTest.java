/*
 * Copyright (C) 2023 phramusca <phramusca@gmail.com>
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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class ICallBackVersionCheckTest {
    
    public ICallBackVersionCheckTest() {
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
     * Test of onNewVersion method, of class ICallBackVersionCheck.
     */
    @Test
    public void testOnNewVersion() {
        System.out.println("onNewVersion");
        AppVersion appVersion = null;
        ICallBackVersionCheck instance = new ICallBackVersionCheckImpl();
        instance.onNewVersion(appVersion);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of onCheck method, of class ICallBackVersionCheck.
     */
    @Test
    public void testOnCheck() {
        System.out.println("onCheck");
        AppVersion appVersion = null;
        String msg = "";
        ICallBackVersionCheck instance = new ICallBackVersionCheckImpl();
        instance.onCheck(appVersion, msg);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of onCheckResult method, of class ICallBackVersionCheck.
     */
    @Test
    public void testOnCheckResult() {
        System.out.println("onCheckResult");
        AppVersion appVersion = null;
        String msg = "";
        ICallBackVersionCheck instance = new ICallBackVersionCheckImpl();
        instance.onCheckResult(appVersion, msg);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of onUnzipCount method, of class ICallBackVersionCheck.
     */
    @Test
    public void testOnUnzipCount() {
        System.out.println("onUnzipCount");
        AppVersion appVersion = null;
        ICallBackVersionCheck instance = new ICallBackVersionCheckImpl();
        instance.onUnzipCount(appVersion);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of onUnzipStart method, of class ICallBackVersionCheck.
     */
    @Test
    public void testOnUnzipStart() {
        System.out.println("onUnzipStart");
        ICallBackVersionCheck instance = new ICallBackVersionCheckImpl();
        instance.onUnzipStart();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of onUnzipProgress method, of class ICallBackVersionCheck.
     */
    @Test
    public void testOnUnzipProgress() {
        System.out.println("onUnzipProgress");
        AppVersion appVersion = null;
        String filename = "";
        int percentComplete = 0;
        ICallBackVersionCheck instance = new ICallBackVersionCheckImpl();
        instance.onUnzipProgress(appVersion, filename, percentComplete);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of onDownloadRequest method, of class ICallBackVersionCheck.
     */
    @Test
    public void testOnDownloadRequest() {
        System.out.println("onDownloadRequest");
        AppVersion assetFile = null;
        ICallBackVersionCheck instance = new ICallBackVersionCheckImpl();
        instance.onDownloadRequest(assetFile);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of onDownloadStart method, of class ICallBackVersionCheck.
     */
    @Test
    public void testOnDownloadStart() {
        System.out.println("onDownloadStart");
        ICallBackVersionCheck instance = new ICallBackVersionCheckImpl();
        instance.onDownloadStart();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of onDownloadProgress method, of class ICallBackVersionCheck.
     */
    @Test
    public void testOnDownloadProgress() {
        System.out.println("onDownloadProgress");
        AppVersion appVersion = null;
        int progress = 0;
        ICallBackVersionCheck instance = new ICallBackVersionCheckImpl();
        instance.onDownloadProgress(appVersion, progress);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public class ICallBackVersionCheckImpl implements ICallBackVersionCheck {

        public void onNewVersion(AppVersion appVersion) {
        }

        public void onCheck(AppVersion appVersion, String msg) {
        }

        public void onCheckResult(AppVersion appVersion, String msg) {
        }

        public void onUnzipCount(AppVersion appVersion) {
        }

        public void onUnzipStart() {
        }

        public void onUnzipProgress(AppVersion appVersion, String filename, int percentComplete) {
        }

        public void onDownloadRequest(AppVersion assetFile) {
        }

        public void onDownloadStart() {
        }

        public void onDownloadProgress(AppVersion appVersion, int progress) {
        }
    }
    
}
