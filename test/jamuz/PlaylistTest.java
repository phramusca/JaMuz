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

import java.util.ArrayList;
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
public class PlaylistTest {
	
	/**
	 *
	 */
	public PlaylistTest() {
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
	}
	
	/**
	 *
	 */
	@After
	public void tearDown() {
	}

	/**
	 * Test of isHidden method, of class Playlist.
	 */
	@Test
	public void testIsHidden() {
		System.out.println("isHidden");
		Playlist instance = null;
		boolean expResult = false;
		boolean result = instance.isHidden();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getId method, of class Playlist.
	 */
	@Test
	public void testGetId() {
		System.out.println("getId");
		Playlist instance = null;
		int expResult = 0;
		int result = instance.getId();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getName method, of class Playlist.
	 */
	@Test
	public void testGetName() {
		System.out.println("getName");
		Playlist instance = null;
		String expResult = "";
		String result = instance.getName();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setName method, of class Playlist.
	 */
	@Test
	public void testSetName() {
		System.out.println("setName");
		String name = "";
		Playlist instance = null;
		instance.setName(name);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isLimit method, of class Playlist.
	 */
	@Test
	public void testIsLimit() {
		System.out.println("isLimit");
		Playlist instance = null;
		boolean expResult = false;
		boolean result = instance.isLimit();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setLimit method, of class Playlist.
	 */
	@Test
	public void testSetLimit() {
		System.out.println("setLimit");
		boolean limit = false;
		Playlist instance = null;
		instance.setLimit(limit);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLimitValue method, of class Playlist.
	 */
	@Test
	public void testGetLimitValue() {
		System.out.println("getLimitValue");
		Playlist instance = null;
		int expResult = 0;
		int result = instance.getLimitValue();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setLimitValue method, of class Playlist.
	 */
	@Test
	public void testSetLimitValue() {
		System.out.println("setLimitValue");
		int limitValue = 0;
		Playlist instance = null;
		instance.setLimitValue(limitValue);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLimitUnit method, of class Playlist.
	 */
	@Test
	public void testGetLimitUnit() {
		System.out.println("getLimitUnit");
		Playlist instance = null;
		Playlist.LimitUnit expResult = null;
		Playlist.LimitUnit result = instance.getLimitUnit();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setLimitUnit method, of class Playlist.
	 */
	@Test
	public void testSetLimitUnit() {
		System.out.println("setLimitUnit");
		Playlist.LimitUnit limitUnit = null;
		Playlist instance = null;
		instance.setLimitUnit(limitUnit);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isRandom method, of class Playlist.
	 */
	@Test
	public void testIsRandom() {
		System.out.println("isRandom");
		Playlist instance = null;
		boolean expResult = false;
		boolean result = instance.isRandom();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setRandom method, of class Playlist.
	 */
	@Test
	public void testSetRandom() {
		System.out.println("setRandom");
		boolean random = false;
		Playlist instance = null;
		instance.setRandom(random);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getType method, of class Playlist.
	 */
	@Test
	public void testGetType() {
		System.out.println("getType");
		Playlist instance = null;
		Playlist.Type expResult = null;
		Playlist.Type result = instance.getType();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setType method, of class Playlist.
	 */
	@Test
	public void testSetType() {
		System.out.println("setType");
		Playlist.Type type = null;
		Playlist instance = null;
		instance.setType(type);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMatch method, of class Playlist.
	 */
	@Test
	public void testGetMatch() {
		System.out.println("getMatch");
		Playlist instance = null;
		Playlist.Match expResult = null;
		Playlist.Match result = instance.getMatch();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setMatch method, of class Playlist.
	 */
	@Test
	public void testSetMatch() {
		System.out.println("setMatch");
		Playlist.Match match = null;
		Playlist instance = null;
		instance.setMatch(match);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of update method, of class Playlist.
	 */
	@Test
	public void testUpdate() {
		System.out.println("update");
		Playlist instance = null;
		boolean expResult = false;
		boolean result = instance.update();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of insert method, of class Playlist.
	 */
	@Test
	public void testInsert() {
		System.out.println("insert");
		Playlist instance = null;
		boolean expResult = false;
		boolean result = instance.insert();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of delete method, of class Playlist.
	 */
	@Test
	public void testDelete() {
		System.out.println("delete");
		Playlist instance = null;
		boolean expResult = false;
		boolean result = instance.delete();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class Playlist.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		Playlist instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of compareTo method, of class Playlist.
	 */
	@Test
	public void testCompareTo() {
		System.out.println("compareTo");
		Object o = null;
		Playlist instance = null;
		int expResult = 0;
		int result = instance.compareTo(o);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of hashCode method, of class Playlist.
	 */
	@Test
	public void testHashCode() {
		System.out.println("hashCode");
		Playlist instance = null;
		int expResult = 0;
		int result = instance.hashCode();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of equals method, of class Playlist.
	 */
	@Test
	public void testEquals() {
		System.out.println("equals");
		Object obj = null;
		Playlist instance = null;
		boolean expResult = false;
		boolean result = instance.equals(obj);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setHidden method, of class Playlist.
	 */
	@Test
	public void testSetHidden() {
		System.out.println("setHidden");
		boolean hidden = false;
		Playlist instance = null;
		instance.setHidden(hidden);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFiles method, of class Playlist.
	 */
	@Test
	public void testGetFiles() {
		System.out.println("getFiles");
		ArrayList<FileInfoInt> fileInfoList = null;
		Playlist instance = null;
		boolean expResult = false;
		boolean result = instance.getFiles(fileInfoList);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setFilter method, of class Playlist.
	 */
	@Test
	public void testSetFilter() {
		System.out.println("setFilter");
		int filterIndex = 0;
		Playlist.Filter filter = null;
		Playlist instance = null;
		instance.setFilter(filterIndex, filter);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addFilter method, of class Playlist.
	 */
	@Test
	public void testAddFilter() {
		System.out.println("addFilter");
		Playlist.Filter filter = null;
		Playlist instance = null;
		instance.addFilter(filter);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeFilter method, of class Playlist.
	 */
	@Test
	public void testRemoveFilter() {
		System.out.println("removeFilter");
		int index = 0;
		Playlist instance = null;
		instance.removeFilter(index);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFilters method, of class Playlist.
	 */
	@Test
	public void testGetFilters() {
		System.out.println("getFilters");
		Playlist instance = null;
		ArrayList<Playlist.Filter> expResult = null;
		ArrayList<Playlist.Filter> result = instance.getFilters();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setOrder method, of class Playlist.
	 */
	@Test
	public void testSetOrder() {
		System.out.println("setOrder");
		int orderIndex = 0;
		Playlist.Order order = null;
		Playlist instance = null;
		instance.setOrder(orderIndex, order);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addOrder method, of class Playlist.
	 */
	@Test
	public void testAddOrder() {
		System.out.println("addOrder");
		Playlist.Order order = null;
		Playlist instance = null;
		instance.addOrder(order);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeOrder method, of class Playlist.
	 */
	@Test
	public void testRemoveOrder() {
		System.out.println("removeOrder");
		int index = 0;
		Playlist instance = null;
		instance.removeOrder(index);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getOrders method, of class Playlist.
	 */
	@Test
	public void testGetOrders() {
		System.out.println("getOrders");
		Playlist instance = null;
		ArrayList<Playlist.Order> expResult = null;
		ArrayList<Playlist.Order> result = instance.getOrders();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
