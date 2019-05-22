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
import java.nio.file.Path;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class FileSystemTest {
	
	public FileSystemTest() {
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
	 * Test of moveFile method, of class FileSystem.
	 */
	@Test
	public void testMoveFile() {
		System.out.println("moveFile");
		File sourceFile = null;
		File destFile = null;
		boolean expResult = false;
		boolean result = FileSystem.moveFile(sourceFile, destFile);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of copyFile method, of class FileSystem.
	 */
	@Test
	public void testCopyFile() throws Exception {
		System.out.println("copyFile");
		File sourceFile = null;
		File destFile = null;
		FileSystem.copyFile(sourceFile, destFile);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of replaceHome method, of class FileSystem.
	 */
	@Test
	public void testReplaceHome_File() {
		System.out.println("replaceHome");
		File file = null;
		File expResult = null;
		File result = FileSystem.replaceHome(file);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of replaceHome method, of class FileSystem.
	 */
	@Test
	public void testReplaceHome_String() {
		System.out.println("replaceHome");
		String fileURL = "";
		File expResult = null;
		File result = FileSystem.replaceHome(fileURL);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of size method, of class FileSystem.
	 */
	@Test
	public void testSize() {
		System.out.println("size");
		Path path = null;
		long expResult = 0L;
		long result = FileSystem.size(path);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
