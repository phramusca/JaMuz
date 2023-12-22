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
import jamuz.process.merge.StatSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
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
public class DaoStatSourceTest {

	private static DbConnJaMuz dbConnJaMuz;
	private static final String testMachineName = "Wh@teverName123456";

	public DaoStatSourceTest() {
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
	public void testStatSources() {

		System.out.println("testStatSources");

		//Check no default stat sources
		ArrayList<StatSource> expectedStatSources = new ArrayList<>();
		checkStatSourceList(expectedStatSources);

		//Need a machine in db to insert a statSource because tables are linked
		dbConnJaMuz.machine().lock().getOrInsert(testMachineName, new StringBuilder(), true);

		//Create some stat sources and test insertion in db
		StatSource statSource1 = new StatSource(-1, "Numero Uno", 3, "ici et la", "moi meme", "BestPassword", "africa", testMachineName, 0, false, "What if not a date?", false);
		StatSource statSource2 = new StatSource(-1, "Numero Dos", 5, "ici et ailleurs", "", "", "europe", testMachineName, 0, true, "What if not a date?", false);
		StatSource statSource3 = new StatSource(-1, "Numero Tres", 2, "loin", "", "", "asie", testMachineName, 0, true, "What if not a date?", false);
		expectedStatSources.add(statSource1);
		expectedStatSources.add(statSource2);
		expectedStatSources.add(statSource3);
		for (StatSource statSource : expectedStatSources) {

			assertTrue(dbConnJaMuz.statSource().lock().insertOrUpdate(statSource));
		}
		// Needed in checkStatSourceList to get proper value
		// and later on too, to be able to update them
		statSource1.setId(1);
		statSource2.setId(2);
		statSource3.setId(3);
		checkStatSourceList(expectedStatSources);

		//Update some stat sources and test update in db
		//statSource1.setIdDevice(4569); // FIXME TEST Need to create a device to test this
		//statSource2.setIdStatement(1); // FIXME TEST Need to update source in statSource2 if changing IdStatement
		statSource3.setIsSelected(false);
		for (StatSource statSource : expectedStatSources) {
			assertTrue(dbConnJaMuz.statSource().lock().insertOrUpdate(statSource));
		}
		checkStatSourceList(expectedStatSources);

		//Delete and check list
		assertTrue(dbConnJaMuz.statSource().lock().delete(2));
		expectedStatSources.remove(statSource2);
		checkStatSourceList(expectedStatSources);

//		dbConnJaMuz.statSource().lock().updateLastMergeDate(3); //Called below + set statSource
		Jamuz.setDb(dbConnJaMuz);
		statSource3.updateLastMergeDate();
		checkStatSourceList(expectedStatSources);

		//getStatSource returns only the first stat source for given machine
		//But ORDER BY on name only, so there might be an order on some other fields
		//resulting in statSource3 to be returned finally
		//Also, getStatSource is made for device remote, so stat source is hidden
		statSource3.setHidden(true);
		Assert.assertEquals(statSource3, dbConnJaMuz.statSource().get(testMachineName));

		//FIXME TEST Negative cases
		//FIXME TEST Check other constraints
	}

	private void checkStatSourceList(ArrayList<StatSource> expectedStatSources) {
		LinkedHashMap<Integer, StatSource> actualList = new LinkedHashMap<>();
		assertTrue(dbConnJaMuz.statSource().get(actualList, testMachineName, false));
		for (StatSource value : actualList.values()) {
			Optional<StatSource> findFirst = expectedStatSources.stream().filter(s -> s.getId() == value.getId()).findFirst();
			assertTrue(findFirst.isPresent());
			assertEquals(value, findFirst.get());
		}
	}

	/**
	 * Test of lock method, of class DaoStatSource.
	 */
	@Test
	@Ignore // Refer to testStatSources() above
	public void testLock() {
		System.out.println("lock");
		DaoStatSource instance = null;
		DaoStatSourceWrite expResult = null;
		DaoStatSourceWrite result = instance.lock();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of get method, of class DaoStatSource.
	 */
	@Test
	@Ignore // Refer to testStatSources() above
	public void testGet_String() {
		System.out.println("get");
		String login = "";
		DaoStatSource instance = null;
		StatSource expResult = null;
		StatSource result = instance.get(login);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of get method, of class DaoStatSource.
	 */
	@Test
	@Ignore // Refer to testStatSources() above
	public void testGet_3args() {
		System.out.println("get");
		LinkedHashMap<Integer, StatSource> statSources = null;
		String hostname = "";
		boolean hidden = false;
		DaoStatSource instance = null;
		boolean expResult = false;
		boolean result = instance.get(statSources, hostname, hidden);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of get method, of class DaoStatSource.
	 */
	@Test
	@Ignore // Refer to testStatSources() above + `instance.get(rs, hidden)` is not public
	public void testGet_ResultSet_boolean() throws Exception {
		System.out.println("get");
		ResultSet rs = null;
		boolean hidden = false;
		DaoStatSource instance = null;
		StatSource expResult = null;
		StatSource result = instance.get(rs, hidden);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
