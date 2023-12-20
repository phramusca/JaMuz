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

import jamuz.Jamuz;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.DefaultListModel;
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
public class DaoTagTest {
	
	public DaoTagTest() {
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
	 * Test of tag methods, of class DbConnJaMuz.
	 */
	@Test
	public void testTag() {

		System.out.println("testTag");
		
		ArrayList<String> expectedTags = new ArrayList<>();
		expectedTags.add("Calme");
		expectedTags.add("Normal");
		expectedTags.add("Joyeux");
		for (String tag : expectedTags) {
			assertTrue(Jamuz.getDb().tag().lock().insert(tag));
		}
		checkTagList(expectedTags);

		DefaultListModel myListModel = new DefaultListModel();
		Jamuz.getDb().listModel().getTagListModel(myListModel);
		assertArrayEquals(expectedTags.toArray(), myListModel.toArray());

		assertTrue("updateTag", Jamuz.getDb().tag().lock().update("Normal", "Tutu"));
		expectedTags.set(2, "Tutu");
		checkTagList(expectedTags);

		assertTrue("deleteTag", Jamuz.getDb().tag().lock().delete("Tutu"));
		expectedTags.remove("Tutu");
		checkTagList(expectedTags);

		assertTrue("insertTag", Jamuz.getDb().tag().lock().insert("Normal"));
		expectedTags.add("Normal");
		checkTagList(expectedTags);

		//Negative cases
		assertFalse("updateTag negative", Jamuz.getDb().tag().lock().update("NoSuchWeirdGenre", "Toto"));
		checkTagList(expectedTags);

		assertFalse("deleteTag negative", Jamuz.getDb().tag().lock().delete("NoSuchWeirdGenre"));
		checkTagList(expectedTags);

		assertFalse("insertTag negative", Jamuz.getDb().tag().lock().insert("Normal")); //As duplicate
		checkTagList(expectedTags);

		//FIXME TEST Check other constraints
	}

	private void checkTagList(ArrayList<String> expectedTags) {
		ArrayList<String> actualList = Jamuz.getDb().tag().get();
		Collections.sort(expectedTags); // getTags() return sorted
		assertArrayEquals(expectedTags.toArray(), actualList.toArray());
	}
	
	/**
	 * Test of insert method, of class DaoTag.
	 */
	@Test
	@Ignore // Refer to testTag() above
	public void testInsert() {
		System.out.println("insert");
		String tag = "";
		DaoTag instance = null;
		boolean expResult = false;
		boolean result = instance.lock().insert(tag);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of insertIfMissing method, of class DaoTag.
	 */
	@Test
	@Ignore // Refer to testTag() above
	public void testInsertIfMissing() {
		System.out.println("insertIfMissing");
		String tag = "";
		DaoTag instance = null;
		boolean expResult = false;
		boolean result = instance.lock().insertIfMissing(tag);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of get method, of class DaoTag.
	 */
	@Test
	@Ignore // Refer to testTag() above
	public void testGet() {
		System.out.println("get");
		DaoTag instance = null;
		ArrayList<String> expResult = null;
		ArrayList<String> result = instance.get();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of update method, of class DaoTag.
	 */
	@Test
	@Ignore // Refer to testTag() above
	public void testUpdate() {
		System.out.println("update");
		String oldTag = "";
		String newTag = "";
		DaoTag instance = null;
		boolean expResult = false;
		boolean result = instance.lock().update(oldTag, newTag);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of delete method, of class DaoTag.
	 */
	@Test
	@Ignore // Refer to testTag() above
	public void testDelete() {
		System.out.println("delete");
		String tag = "";
		DaoTag instance = null;
		boolean expResult = false;
		boolean result = instance.lock().delete(tag);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
