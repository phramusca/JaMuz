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
package jamuz.process.check;

import java.util.List;
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
public class ReleaseMBTest {
	
	public ReleaseMBTest() {
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
	 * Test of search method, of class ReleaseMB.
	 */
	@Test
	public void testSearch() {
		System.out.println("search");
		String artist = "";
		String album = "";
		int nbAudioFiles = 0;
		int idPath = 0;
		int discNo = 0;
		int discTotal = 0;
		ReleaseMB instance = null;
		List<ReleaseMatch> expResult = null;
		List<ReleaseMatch> result = instance.search(artist, album, nbAudioFiles, idPath, discNo, discTotal);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of lookup method, of class ReleaseMB.
	 */
	@Test
	public void testLookup() {
		System.out.println("lookup");
		String mbId = "";
		boolean discPart = false;
		int discNb = 0;
		int discTotal = 0;
		ReleaseMB instance = null;
		List<ReleaseMatch.Track> expResult = null;
		List<ReleaseMatch.Track> result = instance.lookup(mbId, discPart, discNb, discTotal);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getCoverList method, of class ReleaseMB.
	 */
	@Test
	public void testGetCoverList() {
		System.out.println("getCoverList");
		ReleaseMB instance = null;
		List<Cover> expResult = null;
		List<Cover> result = instance.getCoverList();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
