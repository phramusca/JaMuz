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
		new File(dbConnJaMuz.getDbConn().getInfo().getLocationOri()).delete();
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

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

		assertTrue("updateGenre", dbConnJaMuz.genre().lock().update("Reggae", "Toto"));
		expectedGenres.set(13, "Toto");
		checkGenreList(expectedGenres);

		assertTrue("checkGenre", dbConnJaMuz.genre().isSupported("Toto"));
		assertFalse("checkGenre negative", dbConnJaMuz.genre().isSupported("Reggae"));

		assertTrue("deleteGenre", dbConnJaMuz.genre().lock().delete("Toto"));
		expectedGenres.remove("Toto");
		checkGenreList(expectedGenres);

		assertFalse("checkGenre negative", dbConnJaMuz.genre().isSupported("Toto"));
		assertFalse("checkGenre negative", dbConnJaMuz.genre().isSupported("Reggae"));

		assertTrue("insertGenre", dbConnJaMuz.genre().lock().insert("Reggae"));
		expectedGenres.add("Reggae");
		checkGenreList(expectedGenres);

		assertTrue("checkGenre", dbConnJaMuz.genre().isSupported("Reggae"));

		// Negative cases
		assertFalse("updateGenre negative", dbConnJaMuz.genre().lock().update("NoSuchWeirdGenre", "Toto"));
		checkGenreList(expectedGenres);

		assertFalse("deleteGenre negative", dbConnJaMuz.genre().lock().delete("NoSuchWeirdGenre"));
		checkGenreList(expectedGenres);

		assertFalse("insertGenre negative", dbConnJaMuz.genre().lock().insert("Reggae")); // As duplicate
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
	 * Test of lock method, of class DaoGenre.
	 */
	@Test
	@Ignore // Refer to testGenre() above
	public void testLock() {
		System.out.println("lock");
		DaoGenre instance = null;
		DaoGenreWrite expResult = null;
		DaoGenreWrite result = instance.lock();
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

}
