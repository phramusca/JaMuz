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

import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import java.util.ArrayList;
import java.util.Collection;
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
public class MachineTest {
	
	public MachineTest() {
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
	 * Test of read method, of class Machine.
	 */
	@Test
	public void testRead() {
		System.out.println("read");
		Machine instance = null;
		boolean expResult = false;
		boolean result = instance.read();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getOption method, of class Machine.
	 */
	@Test
	public void testGetOption_int() {
		System.out.println("getOption");
		int index = 0;
		Machine instance = null;
		Option expResult = null;
		Option result = instance.getOption(index);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getOption method, of class Machine.
	 */
	@Test
	public void testGetOption_String() {
		System.out.println("getOption");
		String id = "";
		Machine instance = null;
		Option expResult = null;
		Option result = instance.getOption(id);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getOptionValue method, of class Machine.
	 */
	@Test
	public void testGetOptionValue() {
		System.out.println("getOptionValue");
		String id = "";
		Machine instance = null;
		String expResult = "";
		String result = instance.getOptionValue(id);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getOptions method, of class Machine.
	 */
	@Test
	public void testGetOptions() {
		System.out.println("getOptions");
		Machine instance = null;
		ArrayList<Option> expResult = null;
		ArrayList<Option> result = instance.getOptions();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getStatSources method, of class Machine.
	 */
	@Test
	public void testGetStatSources_0args() {
		System.out.println("getStatSources");
		Machine instance = null;
		Collection<StatSource> expResult = null;
		Collection<StatSource> result = instance.getStatSources();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getStatSources method, of class Machine.
	 */
	@Test
	public void testGetStatSources_boolean() {
		System.out.println("getStatSources");
		boolean force = false;
		Machine instance = null;
		Collection<StatSource> expResult = null;
		Collection<StatSource> result = instance.getStatSources(force);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getStatSource method, of class Machine.
	 */
	@Test
	public void testGetStatSource() {
		System.out.println("getStatSource");
		int id = 0;
		Machine instance = null;
		StatSource expResult = null;
		StatSource result = instance.getStatSource(id);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDevices method, of class Machine.
	 */
	@Test
	public void testGetDevices_0args() {
		System.out.println("getDevices");
		Machine instance = null;
		Collection<Device> expResult = null;
		Collection<Device> result = instance.getDevices();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDevices method, of class Machine.
	 */
	@Test
	public void testGetDevices_boolean() {
		System.out.println("getDevices");
		boolean force = false;
		Machine instance = null;
		Collection<Device> expResult = null;
		Collection<Device> result = instance.getDevices(force);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDevice method, of class Machine.
	 */
	@Test
	public void testGetDevice() {
		System.out.println("getDevice");
		int id = 0;
		Machine instance = null;
		Device expResult = null;
		Device result = instance.getDevice(id);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getName method, of class Machine.
	 */
	@Test
	public void testGetName() {
		System.out.println("getName");
		Machine instance = null;
		String expResult = "";
		String result = instance.getName();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDescription method, of class Machine.
	 */
	@Test
	public void testGetDescription() {
		System.out.println("getDescription");
		Machine instance = null;
		String expResult = "";
		String result = instance.getDescription();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
