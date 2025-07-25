/*
 * Copyright (C) 2019 phramusca ( https://github.com/phramusca/ )
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
package jamuz.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import test.helpers.TestSettings;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class FileSystemTest {

	File source;
	File destination;

	/**
	 *
	 */
	public FileSystemTest() {
	}

	/**
	 *
	 */
	@BeforeClass
	public static void setUpClass() {
	}

	/**
	 *
	 */
	@AfterClass
	public static void tearDownClass() {
	}

	/**
	 *
	 */
	@Before
	public void setUp() {
		source = new File(FilenameUtils.concat(TestSettings.getResourcesPath(),
				FilenameUtils.concat("audioFiles", "1min.mp3")));

		destination = new File(FilenameUtils.concat(TestSettings.getAppFolder(),
				FilenameUtils.concat("temp-should_Be_DeletTED_Damned", "1min (copie).mp3")));
	}

	/**
	 *
	 */
	@After
	public void tearDown() {
		destination.delete();
		destination.getParentFile().delete();
	}

	// TODO: Add negative cases
	/**
	 * Test of moveFile method, of class FileSystem.
	 *
	 * @throws java.io.IOException
	 */
	@Test
	public void testMoveFile() throws IOException {
		System.out.println("moveFile");
		// Given
		File moveSource = new File(FilenameUtils.concat(TestSettings.getAppFolder(), "tempShouldBeDeleted.mp3"));
		FileSystem.copyFile(source, moveSource);
		assertTrue(moveSource.exists());
		assertTrue(moveSource.length() == 961029);
		assertTrue(!destination.getParentFile().exists());
		assertTrue(!destination.exists());

		// When
		FileSystem.moveFile(moveSource, destination);

		// Then
		assertTrue(source.exists());
		assertTrue(source.length() == 961029);
		assertTrue(destination.exists());
		assertTrue(destination.length() == 961029);
		assertTrue(!moveSource.exists());
	}

	// TODO: Add negative cases
	/**
	 * Test of copyFile method, of class FileSystem.
	 *
	 * @throws java.lang.Exception
	 */
	@Test
	public void testCopyFile() throws Exception {
		System.out.println("copyFile");

		// Given
		assertTrue(source.exists());
		assertTrue(source.length() == 961029);
		assertTrue(!destination.getParentFile().exists());
		assertTrue(!destination.exists());

		// When
		FileSystem.copyFile(source, destination);

		// Then
		assertTrue(source.exists());
		assertTrue(source.length() == 961029);
		assertTrue(destination.exists());
		assertTrue(destination.length() == 961029);
	}

	@Test
	public void testReplaceHome_String() {
		testReplaceHome("~/toto/~tem/oh~/top.mp9");
	}
	
	@Test
	public void testReplaceHome_File() {
		testReplaceHome(new File("~/toto/~tem/oh~/top.mp9"));
	}
	
	private void testReplaceHome(Object input) {
		boolean isWindows = false;
		boolean isLinux = false;
		if (OS.detect()) {
			isWindows = OS.isWindows();
			isLinux = OS.isUnix();
		} else {
			throw new UnsupportedOperationException("Operating system detection failed");
		}
		File file = (input instanceof File) ? (File) input : new File((String) input);
		File result = FileSystem.replaceHome(file);
		String expectedPath = "/toto/~tem/oh~/top.mp9";
		if (isWindows) {
			expectedPath = expectedPath.replace("/", "\\");
			assertThat(result.getAbsolutePath(), Matchers.endsWith(expectedPath.replace("/", "\\")));
		} else if (isLinux) {
			assertThat(result.getAbsolutePath(), Matchers.endsWith(expectedPath));
		}
		if (isLinux) {
			assertThat(result.getAbsolutePath(), Matchers.startsWith("/home/"));
		} else if (isWindows) {
			assertThat(result.getAbsolutePath(), Matchers.startsWith("C:\\Users\\"));
		}
	}

	/**
	 * Test of size method, of class FileSystem.
	 */
	@Test
	public void testSize() {
		System.out.println("size");
		File file = new File(FilenameUtils.concat(TestSettings.getResourcesPath(), "audioFiles"));
		long result = FileSystem.size(file.toPath());
		assertEquals(7011134, result);
	}

}
