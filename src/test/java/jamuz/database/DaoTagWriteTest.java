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
package jamuz.database;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoTagWriteTest {
	
	public DaoTagWriteTest() {
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
	 * Test of insert method, of class DaoTagWrite.
	 */
	@Test
	@Ignore // Refer to DaoTagTes
	public void testInsert() {
		System.out.println("insert");
		String tag = "";
		DaoTagWrite instance = null;
		boolean expResult = false;
		boolean result = instance.insert(tag);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of insertIfMissing method, of class DaoTagWrite.
	 */
	@Test
	@Ignore // Refer to DaoTagTes
	public void testInsertIfMissing() {
		System.out.println("insertIfMissing");
		String tag = "";
		DaoTagWrite instance = null;
		boolean expResult = false;
		boolean result = instance.insertIfMissing(tag);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of update method, of class DaoTagWrite.
	 */
	@Test
	@Ignore // Refer to DaoTagTest
	public void testUpdate() {
		System.out.println("update");
		String oldTag = "";
		String newTag = "";
		DaoTagWrite instance = null;
		boolean expResult = false;
		boolean result = instance.update(oldTag, newTag);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of delete method, of class DaoTagWrite.
	 */
	@Test
	@Ignore // Refer to DaoTagTest
	public void testDelete() {
		System.out.println("delete");
		String tag = "";
		DaoTagWrite instance = null;
		boolean expResult = false;
		boolean result = instance.delete(tag);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
