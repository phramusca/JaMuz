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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.Mockito;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;

public class AppVersionTest {

	private AppVersion appVersion;

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
		appVersion = new AppVersion("v1.0.0", "v1.0.1");
	}

	@After
	public void tearDown() throws Exception {
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
		File testAsset = getTestAsset();
		appVersion.setAsset(testAsset, (int) testAsset.length());

		ICallBackVersionCheck mockCallback = Mockito.mock(ICallBackVersionCheck.class);

		appVersion.unzipAsset(Mockito.mock(ICallBackVersionCheck.class));
		assertTrue(appVersion.isUnzippedAssetValid());

		Mockito.verify(mockCallback, Mockito.times(0)).onUnzipCount(Mockito.any());
		Mockito.verify(mockCallback, Mockito.times(0)).onUnzipStart();
		Mockito.verify(mockCallback, Mockito.times(0)).onUnzipProgress(Mockito.any(), Mockito.any(), Mockito.anyInt());
	}

	@Test
	public void testUnzipAsset() throws IOException {
		File testAsset = getTestAsset();
		appVersion.setAsset(testAsset, (int) testAsset.length());

		ICallBackVersionCheck mockCallback = Mockito.mock(ICallBackVersionCheck.class);

		appVersion.unzipAsset(mockCallback);

		Mockito.verify(mockCallback, times(1)).onUnzipCount(any());
		Mockito.verify(mockCallback, times(1)).onUnzipStart();
		Mockito.verify(mockCallback, atLeastOnce()).onUnzipProgress(any(), any(), anyInt());
	}

	private File getTestAsset() throws IOException {
		File tempFile = File.createTempFile("tempFile", ".7z");
		try (SevenZOutputFile sevenZOutputFile = new SevenZOutputFile(tempFile)) {
			File tempEntry = File.createTempFile("tempEntry1", ".txt");
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempEntry))) {
				writer.write("This is a sample text.");
				writer.flush();
			}
			sevenZOutputFile.putArchiveEntry(sevenZOutputFile.createArchiveEntry(tempEntry, tempEntry.getName()));
			sevenZOutputFile.write(Files.readAllBytes(tempEntry.toPath()));
			sevenZOutputFile.closeArchiveEntry();
		}
		return tempFile;
	}

	@Test
	public void testGetCurrentVersion() {
		assertEquals("v1.0.0", appVersion.getCurrentVersion());
	}

	@Test
	public void testGetLatestVersion() {
		assertEquals("v1.0.1", appVersion.getLatestVersion());
	}

	@Test
	public void testGetAssetFile() {
		assertNull(appVersion.getAssetFile());
		File assetFile = new File("testFile.txt");
		appVersion.setAsset(assetFile, 100);
		assertEquals(assetFile, appVersion.getAssetFile());
	}

	@Test
	public void testGetAssetSize() {
		assertEquals(0, appVersion.getAssetSize());
		File assetFile = new File("testFile.txt");
		appVersion.setAsset(assetFile, 100);
		assertEquals(100, appVersion.getAssetSize());
	}
}
