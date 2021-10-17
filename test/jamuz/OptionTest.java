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
package jamuz;

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class OptionTest {
	
	public OptionTest() {
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
	 * Test of getIdMachine method, of class Option.
	 */
	@Test
	public void testGetIdMachine() {
		System.out.println("getIdMachine");
		Option instance = null;
		int expResult = 0;
		int result = instance.getIdMachine();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getIdOptionType method, of class Option.
	 */
	@Test
	public void testGetIdOptionType() {
		System.out.println("getIdOptionType");
		Option instance = null;
		int expResult = 0;
		int result = instance.getIdOptionType();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getComment method, of class Option.
	 */
	@Test
	public void testGetComment() {
		System.out.println("getComment");
		Option instance = null;
		String expResult = "";
		String result = instance.getComment();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getValue method, of class Option.
	 */
	@Test
	public void testGetValue() {
		System.out.println("getValue");
		Option instance = null;
		String expResult = "";
		String result = instance.getValue();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getId method, of class Option.
	 */
	@Test
	public void testGetId() {
		System.out.println("getId");
		Option instance = null;
		String expResult = "";
		String result = instance.getId();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setValue method, of class Option.
	 */
	@Test
	public void testSetValue() {
		System.out.println("setValue");
		String value = "";
		Option instance = null;
		instance.setValue(value);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getType method, of class Option.
	 */
	@Test
	public void testGetType() {
		System.out.println("getType");
		Option instance = null;
		String expResult = "";
		String result = instance.getType();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class Option.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		Option instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
