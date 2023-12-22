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

import jamuz.process.merge.StatSource;
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
public class DaoStatSourceWriteTest {
	
	public DaoStatSourceWriteTest() {
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
	 * Test of insertOrUpdate method, of class DaoStatSourceWrite.
	 */
	@Test
	@Ignore // Refer to DaoStatSourceTest
	public void testInsertOrUpdate() {
		System.out.println("insertOrUpdate");
		StatSource statSource = null;
		DaoStatSourceWrite instance = null;
		boolean expResult = false;
		boolean result = instance.insertOrUpdate(statSource);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateLastMergeDate method, of class DaoStatSourceWrite.
	 */
	@Test
	@Ignore // Refer to DaoStatSourceTest
	public void testUpdateLastMergeDate() {
		System.out.println("updateLastMergeDate");
		int idStatSource = 0;
		DaoStatSourceWrite instance = null;
		String expResult = "";
		String result = instance.updateLastMergeDate(idStatSource);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of delete method, of class DaoStatSourceWrite.
	 */
	@Test
	@Ignore // Refer to DaoStatSourceTest
	public void testDelete() {
		System.out.println("delete");
		int id = 0;
		DaoStatSourceWrite instance = null;
		boolean expResult = false;
		boolean result = instance.delete(id);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
