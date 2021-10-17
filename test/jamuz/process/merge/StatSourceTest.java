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

import jamuz.StatSourceAbstract;
import jamuz.process.sync.Device;
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
public class StatSourceTest {
	
	public StatSourceTest() {
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
	 * Test of updateLastMergeDate method, of class StatSource.
	 */
	@Test
	public void testUpdateLastMergeDate() {
		System.out.println("updateLastMergeDate");
		StatSource instance = null;
		instance.updateLastMergeDate();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getId method, of class StatSource.
	 */
	@Test
	public void testGetId() {
		System.out.println("getId");
		StatSource instance = null;
		int expResult = 0;
		int result = instance.getId();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getIdStatement method, of class StatSource.
	 */
	@Test
	public void testGetIdStatement() {
		System.out.println("getIdStatement");
		StatSource instance = null;
		int expResult = 0;
		int result = instance.getIdStatement();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setIdStatement method, of class StatSource.
	 */
	@Test
	public void testSetIdStatement() {
		System.out.println("setIdStatement");
		int idStatement = 0;
		StatSource instance = null;
		instance.setIdStatement(idStatement);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getSource method, of class StatSource.
	 */
	@Test
	public void testGetSource() {
		System.out.println("getSource");
		StatSource instance = null;
		StatSourceAbstract expResult = null;
		StatSourceAbstract result = instance.getSource();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMachineName method, of class StatSource.
	 */
	@Test
	public void testGetMachineName() {
		System.out.println("getMachineName");
		StatSource instance = null;
		String expResult = "";
		String result = instance.getMachineName();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isIsSelected method, of class StatSource.
	 */
	@Test
	public void testIsIsSelected() {
		System.out.println("isIsSelected");
		StatSource instance = null;
		boolean expResult = false;
		boolean result = instance.isIsSelected();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setIsSelected method, of class StatSource.
	 */
	@Test
	public void testSetIsSelected() {
		System.out.println("setIsSelected");
		boolean isSelected = false;
		StatSource instance = null;
		instance.setIsSelected(isSelected);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getIdDevice method, of class StatSource.
	 */
	@Test
	public void testGetIdDevice() {
		System.out.println("getIdDevice");
		StatSource instance = null;
		int expResult = 0;
		int result = instance.getIdDevice();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setIdDevice method, of class StatSource.
	 */
	@Test
	public void testSetIdDevice() {
		System.out.println("setIdDevice");
		int idDevice = 0;
		StatSource instance = null;
		instance.setIdDevice(idDevice);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDevice method, of class StatSource.
	 */
	@Test
	public void testGetDevice() {
		System.out.println("getDevice");
		StatSource instance = null;
		Device expResult = null;
		Device result = instance.getDevice();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class StatSource.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		StatSource instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isHidden method, of class StatSource.
	 */
	@Test
	public void testIsHidden() {
		System.out.println("isHidden");
		StatSource instance = null;
		boolean expResult = false;
		boolean result = instance.isHidden();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
