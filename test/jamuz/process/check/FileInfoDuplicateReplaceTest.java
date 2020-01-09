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
package jamuz.process.check;

import jamuz.FileInfoInt;
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
public class FileInfoDuplicateReplaceTest {
	
	public FileInfoDuplicateReplaceTest() {
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
	 * Test of setFileInfoDuplicate method, of class FileInfoDuplicateReplace.
	 */
	@Test
	public void testSetFileInfoDuplicate() {
		System.out.println("setFileInfoDuplicate");
		FileInfoDuplicateReplace fileInfoDuplicateReplace = null;
		FileInfoDuplicateReplace instance = null;
		instance.setFileInfoDuplicate(fileInfoDuplicateReplace);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setFileInfoDisplay method, of class FileInfoDuplicateReplace.
	 */
	@Test
	public void testSetFileInfoDisplay() {
		System.out.println("setFileInfoDisplay");
		FileInfoInt fileInfoInt = null;
		FileInfoDuplicateReplace instance = null;
		instance.setFileInfoDisplay(fileInfoInt);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFilename method, of class FileInfoDuplicateReplace.
	 */
	@Test
	public void testGetFilename() {
		System.out.println("getFilename");
		FileInfoDuplicateReplace instance = null;
		String expResult = "";
		String result = instance.getFilename();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of clone method, of class FileInfoDuplicateReplace.
	 */
	@Test
	public void testClone() throws Exception {
		System.out.println("clone");
		FileInfoDuplicateReplace instance = null;
		FileInfoDuplicateReplace expResult = null;
		FileInfoDuplicateReplace result = instance.clone();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
