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
package jamuz.process.merge;

import jamuz.FileInfo;
import java.util.ArrayList;
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
public class StatSourceKodiTest {
	
	public StatSourceKodiTest() {
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
	 * Test of setUp method, of class StatSourceKodi.
	 */
	@Test
	public void testSetUp() {
		System.out.println("setUp");
		StatSourceKodi instance = null;
		boolean expResult = false;
		boolean result = instance.setUp();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setUpdateStatisticsParameters method, of class StatSourceKodi.
	 */
	@Test
	public void testSetUpdateStatisticsParameters() throws Exception {
		System.out.println("setUpdateStatisticsParameters");
		FileInfo file = null;
		StatSourceKodi instance = null;
		instance.setUpdateStatisticsParameters(file);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of guessRootPath method, of class StatSourceKodi.
	 */
	@Test
	public void testGuessRootPath() {
		System.out.println("guessRootPath");
		StatSourceKodi instance = null;
		String expResult = "";
		String result = instance.guessRootPath();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTags method, of class StatSourceKodi.
	 */
	@Test
	public void testGetTags() {
		System.out.println("getTags");
		ArrayList<String> tags = null;
		FileInfo file = null;
		StatSourceKodi instance = null;
		boolean expResult = false;
		boolean result = instance.getTags(tags, file);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
