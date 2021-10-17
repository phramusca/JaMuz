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
import java.util.Date;
import java.util.Map;
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
public class FileInfoTest {
	
	public FileInfoTest() {
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
	 * Test of getIdFile method, of class FileInfo.
	 */
	@Test
	public void testGetIdFile() {
		System.out.println("getIdFile");
		FileInfo instance = null;
		int expResult = 0;
		int result = instance.getIdFile();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setIdFile method, of class FileInfo.
	 */
	@Test
	public void testSetIdFile() {
		System.out.println("setIdFile");
		int idFile = 0;
		FileInfo instance = null;
		instance.setIdFile(idFile);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getIdPath method, of class FileInfo.
	 */
	@Test
	public void testGetIdPath() {
		System.out.println("getIdPath");
		FileInfo instance = null;
		int expResult = 0;
		int result = instance.getIdPath();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setIdPath method, of class FileInfo.
	 */
	@Test
	public void testSetIdPath() {
		System.out.println("setIdPath");
		int idPath = 0;
		FileInfo instance = null;
		instance.setIdPath(idPath);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLastPlayed method, of class FileInfo.
	 */
	@Test
	public void testGetLastPlayed() {
		System.out.println("getLastPlayed");
		FileInfo instance = null;
		Date expResult = null;
		Date result = instance.getLastPlayed();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setLastPlayed method, of class FileInfo.
	 */
	@Test
	public void testSetLastPlayed() {
		System.out.println("setLastPlayed");
		Date lastPlayed = null;
		FileInfo instance = null;
		instance.setLastPlayed(lastPlayed);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getAddedDate method, of class FileInfo.
	 */
	@Test
	public void testGetAddedDate() {
		System.out.println("getAddedDate");
		FileInfo instance = null;
		Date expResult = null;
		Date result = instance.getAddedDate();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setAddedDate method, of class FileInfo.
	 */
	@Test
	public void testSetAddedDate() {
		System.out.println("setAddedDate");
		Date addedDate = null;
		FileInfo instance = null;
		instance.setAddedDate(addedDate);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setRelativeFullPath method, of class FileInfo.
	 */
	@Test
	public void testSetRelativeFullPath() {
		System.out.println("setRelativeFullPath");
		String relativeFullPath = "";
		FileInfo instance = null;
		instance.setRelativeFullPath(relativeFullPath);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRelativePath method, of class FileInfo.
	 */
	@Test
	public void testGetRelativePath() {
		System.out.println("getRelativePath");
		FileInfo instance = null;
		String expResult = "";
		String result = instance.getRelativePath();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setRelativePath method, of class FileInfo.
	 */
	@Test
	public void testSetRelativePath() {
		System.out.println("setRelativePath");
		String relativePath = "";
		FileInfo instance = null;
		instance.setRelativePath(relativePath);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getExt method, of class FileInfo.
	 */
	@Test
	public void testGetExt() {
		System.out.println("getExt");
		FileInfo instance = null;
		String expResult = "";
		String result = instance.getExt();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFilename method, of class FileInfo.
	 */
	@Test
	public void testGetFilename() {
		System.out.println("getFilename");
		FileInfo instance = null;
		String expResult = "";
		String result = instance.getFilename();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setFilename method, of class FileInfo.
	 */
	@Test
	public void testSetFilename() {
		System.out.println("setFilename");
		String filename = "";
		FileInfo instance = null;
		instance.setFilename(filename);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRating method, of class FileInfo.
	 */
	@Test
	public void testGetRating() {
		System.out.println("getRating");
		FileInfo instance = null;
		int expResult = 0;
		int result = instance.getRating();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setRating method, of class FileInfo.
	 */
	@Test
	public void testSetRating() {
		System.out.println("setRating");
		int rating = 0;
		FileInfo instance = null;
		instance.setRating(rating);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFormattedRatingModifDate method, of class FileInfo.
	 */
	@Test
	public void testGetFormattedRatingModifDate() {
		System.out.println("getFormattedRatingModifDate");
		FileInfo instance = null;
		String expResult = "";
		String result = instance.getFormattedRatingModifDate();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRatingModifDate method, of class FileInfo.
	 */
	@Test
	public void testGetRatingModifDate() {
		System.out.println("getRatingModifDate");
		FileInfo instance = null;
		Date expResult = null;
		Date result = instance.getRatingModifDate();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setRatingModifDate method, of class FileInfo.
	 */
	@Test
	public void testSetRatingModifDate() {
		System.out.println("setRatingModifDate");
		Date ratingModifDate = null;
		FileInfo instance = null;
		instance.setRatingModifDate(ratingModifDate);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setUpdateRatingModifDate method, of class FileInfo.
	 */
	@Test
	public void testSetUpdateRatingModifDate() {
		System.out.println("setUpdateRatingModifDate");
		boolean updateRatingModifDate = false;
		FileInfo instance = null;
		instance.setUpdateRatingModifDate(updateRatingModifDate);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTagsModifDate method, of class FileInfo.
	 */
	@Test
	public void testGetTagsModifDate() {
		System.out.println("getTagsModifDate");
		FileInfo instance = null;
		Date expResult = null;
		Date result = instance.getTagsModifDate();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setTagsModifDate method, of class FileInfo.
	 */
	@Test
	public void testSetTagsModifDate() {
		System.out.println("setTagsModifDate");
		Date tagsModifDate = null;
		FileInfo instance = null;
		instance.setTagsModifDate(tagsModifDate);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFormattedTagsModifDate method, of class FileInfo.
	 */
	@Test
	public void testGetFormattedTagsModifDate() {
		System.out.println("getFormattedTagsModifDate");
		FileInfo instance = null;
		String expResult = "";
		String result = instance.getFormattedTagsModifDate();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFormattedGenreModifDate method, of class FileInfo.
	 */
	@Test
	public void testGetFormattedGenreModifDate() {
		System.out.println("getFormattedGenreModifDate");
		FileInfo instance = null;
		String expResult = "";
		String result = instance.getFormattedGenreModifDate();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getGenreModifDate method, of class FileInfo.
	 */
	@Test
	public void testGetGenreModifDate() {
		System.out.println("getGenreModifDate");
		FileInfo instance = null;
		Date expResult = null;
		Date result = instance.getGenreModifDate();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setGenreModifDate method, of class FileInfo.
	 */
	@Test
	public void testSetGenreModifDate() {
		System.out.println("setGenreModifDate");
		Date genreModifDate = null;
		FileInfo instance = null;
		instance.setGenreModifDate(genreModifDate);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setUpdateGenreModifDate method, of class FileInfo.
	 */
	@Test
	public void testSetUpdateGenreModifDate() {
		System.out.println("setUpdateGenreModifDate");
		boolean updateGenreModifDate = false;
		FileInfo instance = null;
		instance.setUpdateGenreModifDate(updateGenreModifDate);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPlayCounter method, of class FileInfo.
	 */
	@Test
	public void testGetPlayCounter() {
		System.out.println("getPlayCounter");
		FileInfo instance = null;
		int expResult = 0;
		int result = instance.getPlayCounter();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setPlayCounter method, of class FileInfo.
	 */
	@Test
	public void testSetPlayCounter() {
		System.out.println("setPlayCounter");
		int playCounter = 0;
		FileInfo instance = null;
		instance.setPlayCounter(playCounter);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPreviousPlayCounter method, of class FileInfo.
	 */
	@Test
	public void testGetPreviousPlayCounter() {
		System.out.println("getPreviousPlayCounter");
		FileInfo instance = null;
		int expResult = 0;
		int result = instance.getPreviousPlayCounter();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setPreviousPlayCounter method, of class FileInfo.
	 */
	@Test
	public void testSetPreviousPlayCounter() {
		System.out.println("setPreviousPlayCounter");
		int previousPlayCounter = 0;
		FileInfo instance = null;
		instance.setPreviousPlayCounter(previousPlayCounter);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getBPM method, of class FileInfo.
	 */
	@Test
	public void testGetBPM() {
		System.out.println("getBPM");
		FileInfo instance = null;
		float expResult = 0.0F;
		float result = instance.getBPM();
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setBPM method, of class FileInfo.
	 */
	@Test
	public void testSetBPM() {
		System.out.println("setBPM");
		float BPM = 0.0F;
		FileInfo instance = null;
		instance.setBPM(BPM);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTags method, of class FileInfo.
	 */
	@Test
	public void testGetTags() {
		System.out.println("getTags");
		FileInfo instance = null;
		ArrayList<String> expResult = null;
		ArrayList<String> result = instance.getTags();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of readTags method, of class FileInfo.
	 */
	@Test
	public void testReadTags() {
		System.out.println("readTags");
		FileInfo instance = null;
		ArrayList<String> expResult = null;
		ArrayList<String> result = instance.readTags();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTagsToString method, of class FileInfo.
	 */
	@Test
	public void testGetTagsToString() {
		System.out.println("getTagsToString");
		FileInfo instance = null;
		String expResult = "";
		String result = instance.getTagsToString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setTags method, of class FileInfo.
	 */
	@Test
	public void testSetTags() {
		System.out.println("setTags");
		ArrayList<String> tags = null;
		FileInfo instance = null;
		instance.setTags(tags);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getSourceName method, of class FileInfo.
	 */
	@Test
	public void testGetSourceName() {
		System.out.println("getSourceName");
		FileInfo instance = null;
		String expResult = "";
		String result = instance.getSourceName();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setSourceName method, of class FileInfo.
	 */
	@Test
	public void testSetSourceName() {
		System.out.println("setSourceName");
		String sourceName = "";
		FileInfo instance = null;
		instance.setSourceName(sourceName);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isDeleted method, of class FileInfo.
	 */
	@Test
	public void testIsDeleted() {
		System.out.println("isDeleted");
		FileInfo instance = null;
		boolean expResult = false;
		boolean result = instance.isDeleted();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setPath method, of class FileInfo.
	 */
	@Test
	public void testSetPath() {
		System.out.println("setPath");
		String relativeFullPath = "";
		FileInfo instance = null;
		instance.setPath(relativeFullPath);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRelativeFullPath method, of class FileInfo.
	 */
	@Test
	public void testGetRelativeFullPath() {
		System.out.println("getRelativeFullPath");
		FileInfo instance = null;
		String expResult = "";
		String result = instance.getRelativeFullPath();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFormattedLastPlayed method, of class FileInfo.
	 */
	@Test
	public void testGetFormattedLastPlayed() {
		System.out.println("getFormattedLastPlayed");
		FileInfo instance = null;
		String expResult = "";
		String result = instance.getFormattedLastPlayed();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLastPlayedLocalTime method, of class FileInfo.
	 */
	@Test
	public void testGetLastPlayedLocalTime() {
		System.out.println("getLastPlayedLocalTime");
		FileInfo instance = null;
		String expResult = "";
		String result = instance.getLastPlayedLocalTime();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFormattedAddedDate method, of class FileInfo.
	 */
	@Test
	public void testGetFormattedAddedDate() {
		System.out.println("getFormattedAddedDate");
		FileInfo instance = null;
		String expResult = "";
		String result = instance.getFormattedAddedDate();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getAddedDateLocalTime method, of class FileInfo.
	 */
	@Test
	public void testGetAddedDateLocalTime() {
		System.out.println("getAddedDateLocalTime");
		FileInfo instance = null;
		String expResult = "";
		String result = instance.getAddedDateLocalTime();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setGenre method, of class FileInfo.
	 */
	@Test
	public void testSetGenre() {
		System.out.println("setGenre");
		String genre = "";
		FileInfo instance = null;
		instance.setGenre(genre);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getGenre method, of class FileInfo.
	 */
	@Test
	public void testGetGenre() {
		System.out.println("getGenre");
		FileInfo instance = null;
		String expResult = "";
		String result = instance.getGenre();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of compareTo method, of class FileInfo.
	 */
	@Test
	public void testCompareTo() {
		System.out.println("compareTo");
		Object o = null;
		FileInfo instance = null;
		int expResult = 0;
		int result = instance.compareTo(o);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of equalsStats method, of class FileInfo.
	 */
	@Test
	public void testEqualsStats() {
		System.out.println("equalsStats");
		FileInfo thatFileInfo = null;
		FileInfo instance = null;
		boolean expResult = false;
		boolean result = instance.equalsStats(thatFileInfo);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of equals method, of class FileInfo.
	 */
	@Test
	public void testEquals() {
		System.out.println("equals");
		Object obj = null;
		FileInfo instance = null;
		boolean expResult = false;
		boolean result = instance.equals(obj);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of hashCode method, of class FileInfo.
	 */
	@Test
	public void testHashCode() {
		System.out.println("hashCode");
		FileInfo instance = null;
		int expResult = 0;
		int result = instance.hashCode();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of clone method, of class FileInfo.
	 */
	@Test
	public void testClone() throws Exception {
		System.out.println("clone");
		FileInfo instance = null;
		Object expResult = null;
		Object result = instance.clone();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class FileInfo.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		FileInfo instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toMap method, of class FileInfo.
	 */
	@Test
	public void testToMap() {
		System.out.println("toMap");
		FileInfo instance = null;
		Map expResult = null;
		Map result = instance.toMap();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
