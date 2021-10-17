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
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import test.helpers.Settings;

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
		source = new File(FilenameUtils.concat(Settings.getRessourcesPath(),
				FilenameUtils.concat("audioFiles", "1min.mp3")));

		destination = new File(FilenameUtils.concat(Settings.getAppFolder(),
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

	//TODO: Add negative cases
	/**
	 * Test of moveFile method, of class FileSystem.
	 *
	 * @throws java.io.IOException
	 */
	@Test
	public void testMoveFile() throws IOException {
		System.out.println("moveFile");
		//Given
		File moveSource = new File(FilenameUtils.concat(Settings.getAppFolder(), "tempShouldBeDeleted.mp3"));
		FileSystem.copyFile(source, moveSource);
		assertTrue(moveSource.exists());
		assertTrue(moveSource.length() == 961029);
		assertTrue(!destination.getParentFile().exists());
		assertTrue(!destination.exists());

		//When
		FileSystem.moveFile(moveSource, destination);

		//Then
		assertTrue(source.exists());
		assertTrue(source.length() == 961029);
		assertTrue(destination.exists());
		assertTrue(destination.length() == 961029);
		assertTrue(!moveSource.exists());
	}

	//TODO: Add negative cases
	/**
	 * Test of copyFile method, of class FileSystem.
	 *
	 * @throws java.lang.Exception
	 */
	@Test
	public void testCopyFile() throws Exception {
		System.out.println("copyFile");

		//Given
		assertTrue(source.exists());
		assertTrue(source.length() == 961029);
		assertTrue(!destination.getParentFile().exists());
		assertTrue(!destination.exists());

		//When
		FileSystem.copyFile(source, destination);

		//Then
		assertTrue(source.exists());
		assertTrue(source.length() == 961029);
		assertTrue(destination.exists());
		assertTrue(destination.length() == 961029);
	}

	/**
	 * Test of replaceHome method, of class FileSystem.
	 */
	@Test
	public void testReplaceHome_File() {
		System.out.println("replaceHome");
		File file = new File("~/toto/~tem/oh~/top.mp9");
		File result = FileSystem.replaceHome(file);
		assertTrue(result.getAbsolutePath().endsWith("/toto/~tem/oh~/top.mp9"));
		assertTrue(result.getAbsolutePath().startsWith("/home/"));
	}

	/**
	 * Test of replaceHome method, of class FileSystem.
	 */
	@Test
	public void testReplaceHome_String() {
		System.out.println("replaceHome");
		File result = FileSystem.replaceHome("~/toto/~tem/oh~/top.mp9");
		assertTrue(result.getAbsolutePath().endsWith("/toto/~tem/oh~/top.mp9"));
		assertTrue(result.getAbsolutePath().startsWith("/home/"));

		String filename = "/tmp/toto/~tem/oh~/top.mp9";
		result = FileSystem.replaceHome(filename);
		assertEquals(new File(filename), result);
	}

	/**
	 * Test of size method, of class FileSystem.
	 */
	@Test
	public void testSize() {
		System.out.println("size");
		File file = new File(FilenameUtils.concat(Settings.getRessourcesPath(), "audioFiles"));
		long result = FileSystem.size(file.toPath());
		assertEquals(7011134, result);
	}

}
