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
import java.io.IOException;
import java.sql.SQLException;
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
import test.helpers.TestUnitSettings;
import java.io.File;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoTagTest {

	private static DbConnJaMuz dbConnJaMuz;

	public DaoTagTest() {
	}

	@BeforeClass
	public static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
		dbConnJaMuz = TestUnitSettings.createTempDatabase();
		Jamuz.setDb(dbConnJaMuz);
	}

	@AfterClass
	public static void tearDownClass() {
		new File(dbConnJaMuz.getDbConn().getInfo().getLocationOri()).delete();
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
	 * Test of lock method, of class DaoTag.
	 */
	@Test
	@Ignore // Refer to testTag() above
	public void testLock() {
		System.out.println("lock");
		DaoTag instance = null;
		DaoTagWrite expResult = null;
		DaoTagWrite result = instance.lock();
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

}
