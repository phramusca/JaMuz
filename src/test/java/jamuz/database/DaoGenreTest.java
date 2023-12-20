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

import jamuz.database.DaoGenre;
import jamuz.database.DbConnJaMuz;
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

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoGenreTest {

	private static DbConnJaMuz dbConnJaMuz;

	public DaoGenreTest() {
	}

	@BeforeClass
	public static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
		dbConnJaMuz = TestUnitSettings.createTempDatabase();
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
	 * Test of genre methods, of class DbConnJaMuz.
	 */
	@Test
	public void testGenre() {

		System.out.println("testGenre");

		ArrayList<String> expectedGenres = new ArrayList<>();
		expectedGenres.add("Blues");
		expectedGenres.add("BO");
		expectedGenres.add("Chanson");
		expectedGenres.add("Dub");
		expectedGenres.add("Electro");
		expectedGenres.add("Folk");
		expectedGenres.add("Funk");
		expectedGenres.add("Jazz");
		expectedGenres.add("Latino");
		expectedGenres.add("Musical");
		expectedGenres.add("Pop");
		expectedGenres.add("Ragga");
		expectedGenres.add("Rap");
		expectedGenres.add("Reggae");
		expectedGenres.add("Reggaeton");
		expectedGenres.add("Rock");
		expectedGenres.add("Salsa");
		expectedGenres.add("Samba");
		expectedGenres.add("Ska");
		expectedGenres.add("Ska Punk");
		expectedGenres.add("Soul");
		expectedGenres.add("Trip Hop");
		checkGenreList(expectedGenres);

		assertTrue("updateGenre", dbConnJaMuz.genre().update("Reggae", "Toto"));
		expectedGenres.set(13, "Toto");
		checkGenreList(expectedGenres);

		assertTrue("deleteGenre", dbConnJaMuz.genre().delete("Toto"));
		expectedGenres.remove("Toto");
		checkGenreList(expectedGenres);

		assertTrue("insertGenre", dbConnJaMuz.genre().insert("Reggae"));
		expectedGenres.add("Reggae");
		checkGenreList(expectedGenres);

		assertTrue("checkGenre", dbConnJaMuz.genre().isSupported("Reggae"));

		// Negative cases
		assertFalse("updateGenre negative", dbConnJaMuz.genre().update("NoSuchWeirdGenre", "Toto"));
		checkGenreList(expectedGenres);

		assertFalse("deleteGenre negative", dbConnJaMuz.genre().delete("NoSuchWeirdGenre"));
		checkGenreList(expectedGenres);

		assertFalse("insertGenre negative", dbConnJaMuz.genre().insert("Reggae")); // As duplicate
		checkGenreList(expectedGenres);

		assertFalse("checkGenre negative", dbConnJaMuz.genre().isSupported("NoSuchWeirdGenre"));

		// FIXME TEST Check other constraints
	}

	private void checkGenreList(ArrayList<String> expectedGenres) {
		DefaultListModel myListModel = new DefaultListModel();
		dbConnJaMuz.listModel().getGenreListModel(myListModel);
		Collections.sort(expectedGenres);
		assertArrayEquals(expectedGenres.toArray(), myListModel.toArray());
	}

	/**
	 * Test of isGenreSupported method, of class DaoGenre.
	 */
	@Test
	@Ignore // Refer to testGenre() above
	public void testIsGenreSupported() {
		System.out.println("isGenreSupported");
		String genre = "";
		DaoGenre instance = null;
		boolean expResult = false;
		boolean result = instance.isSupported(genre);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of insert method, of class DaoGenre.
	 */
	@Test
	@Ignore // Refer to testGenre() above
	public void testInsert() {
		System.out.println("insert");
		String genre = "";
		DaoGenre instance = null;
		boolean expResult = false;
		boolean result = instance.insert(genre);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isSupported method, of class DaoGenre.
	 */
	@Test
	@Ignore // Refer to testGenre() above
	public void testIsSupported() {
		System.out.println("isSupported");
		String genre = "";
		DaoGenre instance = null;
		boolean expResult = false;
		boolean result = instance.isSupported(genre);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of update method, of class DaoGenre.
	 */
	@Test
	@Ignore // Refer to testGenre() above
	public void testUpdate() {
		System.out.println("update");
		String oldGenre = "";
		String newGenre = "";
		DaoGenre instance = null;
		boolean expResult = false;
		boolean result = instance.update(oldGenre, newGenre);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of delete method, of class DaoGenre.
	 */
	@Test
	@Ignore // Refer to testGenre() above
	public void testDelete() {
		System.out.println("delete");
		String genre = "";
		DaoGenre instance = null;
		boolean expResult = false;
		boolean result = instance.delete(genre);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
