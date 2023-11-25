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

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

public class AppVersionTest {

    private AppVersion appVersion;

    @Before
    public void setUp() {
        appVersion = new AppVersion("v1.0.0", "v1.0.1");
    }

    @Test
    public void testIsNewVersion() {
        assertTrue(appVersion.isNewVersion());
        appVersion.setLatestVersion("v0.9.9");
        assertFalse(appVersion.isNewVersion());
    }

    @Test
    public void testSetAsset() {
        File assetFile = new File("testFile.txt");
        appVersion.setAsset(assetFile, 100);
        assertEquals(assetFile, appVersion.getAssetFile());
        assertEquals(100, appVersion.getAssetSize());
    }

    @Test
    public void testSetLatestVersion() {
        appVersion.setLatestVersion("v1.0.2");
        assertEquals("v1.0.2", appVersion.getLatestVersion());
    }

    @Test
    public void testCompareVersionStrings() {
        assertEquals(0, AppVersion.compareVersionStrings("v1.0.0", "v1.0.0"));
        assertTrue(AppVersion.compareVersionStrings("v1.0.1", "v1.0.0") > 0);
        assertTrue(AppVersion.compareVersionStrings("v1.0.0", "v1.0.1") < 0);
    }

    @Test
    public void testToString() {
        assertEquals("Current: v1.0.0. Latest: v1.0.1. ", appVersion.toString());
    }

    @Test
    public void testIsAssetValid() throws IOException {
        File tempFile = File.createTempFile("tempFile", null);
        appVersion.setAsset(tempFile, (int) tempFile.length());
        assertTrue(appVersion.isAssetValid());
    }

    @Test
    public void testIsUnzippedAssetValid() throws IOException {
        File tempFile = File.createTempFile("tempFile", null);
        appVersion.setAsset(tempFile, (int) tempFile.length());

        ICallBackVersionCheck mockCallback = Mockito.mock(ICallBackVersionCheck.class);

        assertTrue(appVersion.isUnzippedAssetValid());

        Mockito.verify(mockCallback, Mockito.times(0)).onUnzipCount(Mockito.any());
        Mockito.verify(mockCallback, Mockito.times(0)).onUnzipStart();
        Mockito.verify(mockCallback, Mockito.times(0)).onUnzipProgress(Mockito.any(), Mockito.any(), Mockito.anyInt());
    }

    @Test
    public void testUnzipAsset() throws IOException {
        File tempFile = File.createTempFile("tempFile", null);
        appVersion.setAsset(tempFile, (int) tempFile.length());

        ICallBackVersionCheck mockCallback = Mockito.mock(ICallBackVersionCheck.class);

        appVersion.unzipAsset(mockCallback);

        Mockito.verify(mockCallback, Mockito.times(1)).onUnzipCount(Mockito.any());
        Mockito.verify(mockCallback, Mockito.times(1)).onUnzipStart();
        Mockito.verify(mockCallback, Mockito.atLeastOnce()).onUnzipProgress(Mockito.any(), Mockito.any(), Mockito.anyInt());
    }
}

