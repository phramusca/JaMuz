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

import jamuz.process.check.FolderInfo;
import jamuz.process.check.ReplayGain;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
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
public class FileInfoIntTest {
	
	public FileInfoIntTest() {
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
	 * Test of getTrackNo method, of class FileInfoInt.
	 */
	@Test
	public void testGetTrackNo() {
		System.out.println("getTrackNo");
		FileInfoInt instance = null;
		int expResult = 0;
		int result = instance.getTrackNo();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTrackTotal method, of class FileInfoInt.
	 */
	@Test
	public void testGetTrackTotal() {
		System.out.println("getTrackTotal");
		FileInfoInt instance = null;
		int expResult = 0;
		int result = instance.getTrackTotal();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDiscNo method, of class FileInfoInt.
	 */
	@Test
	public void testGetDiscNo() {
		System.out.println("getDiscNo");
		FileInfoInt instance = null;
		int expResult = 0;
		int result = instance.getDiscNo();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getComment method, of class FileInfoInt.
	 */
	@Test
	public void testGetComment() {
		System.out.println("getComment");
		FileInfoInt instance = null;
		String expResult = "";
		String result = instance.getComment();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getNbCovers method, of class FileInfoInt.
	 */
	@Test
	public void testGetNbCovers() {
		System.out.println("getNbCovers");
		FileInfoInt instance = null;
		int expResult = 0;
		int result = instance.getNbCovers();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getCheckedFlag method, of class FileInfoInt.
	 */
	@Test
	public void testGetCheckedFlag() {
		System.out.println("getCheckedFlag");
		FileInfoInt instance = null;
		FolderInfo.CheckedFlag expResult = null;
		FolderInfo.CheckedFlag result = instance.getCheckedFlag();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getBitRate method, of class FileInfoInt.
	 */
	@Test
	public void testGetBitRate() {
		System.out.println("getBitRate");
		FileInfoInt instance = null;
		String expResult = "";
		String result = instance.getBitRate();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFormat method, of class FileInfoInt.
	 */
	@Test
	public void testGetFormat() {
		System.out.println("getFormat");
		FileInfoInt instance = null;
		String expResult = "";
		String result = instance.getFormat();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLength method, of class FileInfoInt.
	 */
	@Test
	public void testGetLength() {
		System.out.println("getLength");
		FileInfoInt instance = null;
		int expResult = 0;
		int result = instance.getLength();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setLengthDisplay method, of class FileInfoInt.
	 */
	@Test
	public void testSetLengthDisplay() {
		System.out.println("setLengthDisplay");
		String lengthDisplay = "";
		FileInfoInt instance = null;
		instance.setLengthDisplay(lengthDisplay);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getSize method, of class FileInfoInt.
	 */
	@Test
	public void testGetSize() {
		System.out.println("getSize");
		FileInfoInt instance = null;
		long expResult = 0L;
		long result = instance.getSize();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setSizeDisplay method, of class FileInfoInt.
	 */
	@Test
	public void testSetSizeDisplay() {
		System.out.println("setSizeDisplay");
		String sizeDisplay = "";
		FileInfoInt instance = null;
		instance.setSizeDisplay(sizeDisplay);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isFromLibrary method, of class FileInfoInt.
	 */
	@Test
	public void testIsFromLibrary() {
		System.out.println("isFromLibrary");
		FileInfoInt instance = null;
		boolean expResult = false;
		boolean result = instance.isFromLibrary();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getCopyRight method, of class FileInfoInt.
	 */
	@Test
	public void testGetCopyRight() {
		System.out.println("getCopyRight");
		FileInfoInt instance = null;
		int expResult = 0;
		FolderInfo.CopyRight result = instance.getCopyRight();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getAlbumRating method, of class FileInfoInt.
	 */
	@Test
	public void testGetAlbumRating() {
		System.out.println("getAlbumRating");
		FileInfoInt instance = null;
		double expResult = 0.0;
		double result = instance.getAlbumRating();
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPercentRated method, of class FileInfoInt.
	 */
	@Test
	public void testGetPercentRated() {
		System.out.println("getPercentRated");
		FileInfoInt instance = null;
		double expResult = 0.0;
		double result = instance.getPercentRated();
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setRootPath method, of class FileInfoInt.
	 */
	@Test
	public void testSetRootPath() {
		System.out.println("setRootPath");
		String rootPath = "";
		FileInfoInt instance = null;
		instance.setRootPath(rootPath);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFullPath method, of class FileInfoInt.
	 */
	@Test
	public void testGetFullPath() {
		System.out.println("getFullPath");
		FileInfoInt instance = null;
		File expResult = null;
		File result = instance.getFullPath();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLyrics method, of class FileInfoInt.
	 */
	@Test
	public void testGetLyrics() {
		System.out.println("getLyrics");
		FileInfoInt instance = null;
		String expResult = "";
		String result = instance.getLyrics();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of returnHex method, of class FileInfoInt.
	 */
	@Test
	public void testReturnHex() {
		System.out.println("returnHex");
		byte[] inBytes = null;
		String expResult = "";
		String result = FileInfoInt.returnHex(inBytes);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of readTags method, of class FileInfoInt.
	 */
	@Test
	public void testReadTags() {
		System.out.println("readTags");
		boolean readCover = false;
		FileInfoInt instance = null;
		boolean expResult = false;
		boolean result = instance.readMetadata(readCover);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of saveTags method, of class FileInfoInt.
	 */
	@Test
	public void testSaveTags_boolean() {
		System.out.println("saveTags");
		boolean deleteComment = false;
		FileInfoInt instance = null;
		boolean expResult = false;
		boolean result = instance.saveTags(deleteComment);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of saveTags method, of class FileInfoInt.
	 */
	@Test
	public void testSaveTags_15args() {
		System.out.println("saveTags");
		String artist = "";
		String albumArtist = "";
		String album = "";
		int trackNo = 0;
		int trackTotal = 0;
		int discNo = 0;
		int discTotal = 0;
		String genre = "";
		String year = "";
		BufferedImage image = null;
		boolean deleteComment = false;
		String comment = "";
		String title = "";
		float bpm = 0.0F;
		String lyrics = "";
		FileInfoInt instance = null;
		boolean expResult = false;
		boolean result = instance.saveMetadata(artist, albumArtist, album, trackNo, trackTotal, discNo, discTotal, genre, year, image, deleteComment, comment, title, bpm, lyrics);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of insertTagsInDb method, of class FileInfoInt.
	 */
	@Test
	public void testInsertTagsInDb() {
		System.out.println("insertTagsInDb");
		FileInfoInt instance = null;
		boolean expResult = false;
		boolean result = instance.insertTagsInDb();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateTagsInDb method, of class FileInfoInt.
	 */
	@Test
	public void testUpdateTagsInDb() {
		System.out.println("updateTagsInDb");
		FileInfoInt instance = null;
		boolean expResult = false;
		boolean result = instance.updateTagsInDb();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of saveBPMtoFileTags method, of class FileInfoInt.
	 */
	@Test
	public void testSaveBPMtoFileTags() {
		System.out.println("saveBPMtoFileTags");
		FileInfoInt instance = null;
		boolean expResult = false;
		boolean result = instance.saveMetadataBPM();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of saveTagLyrics method, of class FileInfoInt.
	 */
	@Test
	public void testSaveTagLyrics() {
		System.out.println("saveTagLyrics");
		String lyrics = "";
		FileInfoInt instance = null;
		boolean expResult = false;
		boolean result = instance.saveMetadataLyrics(lyrics);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateGenre method, of class FileInfoInt.
	 */
	@Test
	public void testUpdateGenre() {
		System.out.println("updateGenre");
		String genre = "";
		FileInfoInt instance = null;
		boolean expResult = false;
		boolean result = instance.updateGenre(genre);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateRating method, of class FileInfoInt.
	 */
	@Test
	public void testUpdateRating() {
		System.out.println("updateRating");
		String rating = "";
		FileInfoInt instance = null;
		boolean expResult = false;
		boolean result = instance.updateRating(rating);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateInDb method, of class FileInfoInt.
	 */
	@Test
	public void testUpdateInDb() {
		System.out.println("updateInDb");
		FileInfoInt instance = null;
		boolean expResult = false;
		boolean result = instance.updateInDb();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of scanDeleted method, of class FileInfoInt.
	 */
	@Test
	public void testScanDeleted() {
		System.out.println("scanDeleted");
		FileInfoInt instance = null;
		boolean expResult = false;
		boolean result = instance.scanDeleted();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of saveCoverToFile method, of class FileInfoInt.
	 */
	@Test
	public void testSaveCoverToFile() {
		System.out.println("saveCoverToFile");
		boolean overwrite = false;
		FileInfoInt instance = null;
		FileInfoInt expResult = null;
		FileInfoInt result = instance.saveCoverToFile(overwrite);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTrackNoFull method, of class FileInfoInt.
	 */
	@Test
	public void testGetTrackNoFull() {
		System.out.println("getTrackNoFull");
		FileInfoInt instance = null;
		String expResult = "";
		String result = instance.getTrackNoFull();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setTrackNoFull method, of class FileInfoInt.
	 */
	@Test
	public void testSetTrackNoFull() {
		System.out.println("setTrackNoFull");
		String trackNoFull = "";
		FileInfoInt instance = null;
		instance.setTrackNoFull(trackNoFull);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDiscNoFull method, of class FileInfoInt.
	 */
	@Test
	public void testGetDiscNoFull() {
		System.out.println("getDiscNoFull");
		FileInfoInt instance = null;
		String expResult = "";
		String result = instance.getDiscNoFull();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setDiscNoFull method, of class FileInfoInt.
	 */
	@Test
	public void testSetDiscNoFull() {
		System.out.println("setDiscNoFull");
		String discNoFull = "";
		FileInfoInt instance = null;
		instance.setDiscNoFull(discNoFull);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of computeMask method, of class FileInfoInt.
	 */
	@Test
	public void testComputeMask() {
		System.out.println("computeMask");
		String mask = "";
		String albumArtist = "";
		String album = "";
		String genre = "";
		FileInfoInt instance = null;
		String expResult = "";
		String result = instance.computeMask(mask, albumArtist, album, genre);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of compareTo method, of class FileInfoInt.
	 */
	@Test
	public void testCompareTo() {
		System.out.println("compareTo");
		Object o = null;
		FileInfoInt instance = null;
		int expResult = 0;
		int result = instance.compareTo(o);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of equals method, of class FileInfoInt.
	 */
	@Test
	public void testEquals() {
		System.out.println("equals");
		Object obj = null;
		FileInfoInt instance = null;
		boolean expResult = false;
		boolean result = instance.equals(obj);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of hashCode method, of class FileInfoInt.
	 */
	@Test
	public void testHashCode() {
		System.out.println("hashCode");
		FileInfoInt instance = null;
		int expResult = 0;
		int result = instance.hashCode();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTitle method, of class FileInfoInt.
	 */
	@Test
	public void testGetTitle() {
		System.out.println("getTitle");
		FileInfoInt instance = null;
		String expResult = "";
		String result = instance.getTitle();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getCoverImage method, of class FileInfoInt.
	 */
	@Test
	public void testGetCoverImage() {
		System.out.println("getCoverImage");
		FileInfoInt instance = null;
		BufferedImage expResult = null;
		BufferedImage result = instance.getCoverImage();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of unsetCover method, of class FileInfoInt.
	 */
	@Test
	public void testUnsetCover() {
		System.out.println("unsetCover");
		FileInfoInt instance = null;
		instance.unsetCover();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFormattedModifDate method, of class FileInfoInt.
	 */
	@Test
	public void testGetFormattedModifDate() {
		System.out.println("getFormattedModifDate");
		FileInfoInt instance = null;
		String expResult = "";
		String result = instance.getFormattedModifDate();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of hasID3v1 method, of class FileInfoInt.
	 */
	@Test
	public void testHasID3v1() {
		System.out.println("hasID3v1");
		FileInfoInt instance = null;
		boolean expResult = false;
		boolean result = instance.hasID3v1();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getCoverHash method, of class FileInfoInt.
	 */
	@Test
	public void testGetCoverHash() {
		System.out.println("getCoverHash");
		FileInfoInt instance = null;
		String expResult = "";
		String result = instance.getCoverHash();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setCoverHash method, of class FileInfoInt.
	 */
	@Test
	public void testSetCoverHash() {
		System.out.println("setCoverHash");
		String coverHash = "";
		FileInfoInt instance = null;
		instance.setCoverHash(coverHash);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getYear method, of class FileInfoInt.
	 */
	@Test
	public void testGetYear() {
		System.out.println("getYear");
		FileInfoInt instance = null;
		String expResult = "";
		String result = instance.getYear();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getArtist method, of class FileInfoInt.
	 */
	@Test
	public void testGetArtist() {
		System.out.println("getArtist");
		FileInfoInt instance = null;
		String expResult = "";
		String result = instance.getArtist();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getAlbumArtist method, of class FileInfoInt.
	 */
	@Test
	public void testGetAlbumArtist() {
		System.out.println("getAlbumArtist");
		FileInfoInt instance = null;
		String expResult = "";
		String result = instance.getAlbumArtist();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getAlbum method, of class FileInfoInt.
	 */
	@Test
	public void testGetAlbum() {
		System.out.println("getAlbum");
		FileInfoInt instance = null;
		String expResult = "";
		String result = instance.getAlbum();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDiscTotal method, of class FileInfoInt.
	 */
	@Test
	public void testGetDiscTotal() {
		System.out.println("getDiscTotal");
		FileInfoInt instance = null;
		int expResult = 0;
		int result = instance.getDiscTotal();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRelease method, of class FileInfoInt.
	 */
	@Test
	public void testGetRelease() {
		System.out.println("getRelease");
		FileInfoInt instance = null;
		String expResult = "";
		String result = instance.getRelease();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of sayRating method, of class FileInfoInt.
	 */
	@Test
	public void testSayRating() {
		System.out.println("sayRating");
		boolean sayRated = false;
		FileInfoInt instance = null;
		instance.sayRating(sayRated);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of playRessouce method, of class FileInfoInt.
	 */
	@Test
	public void testPlayRessouce() {
		System.out.println("playRessouce");
		String filename = "";
		FileInfoInt instance = null;
		instance.playRessouce(filename);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTags method, of class FileInfoInt.
	 */
	@Test
	public void testGetTags() {
		System.out.println("getTags");
		FileInfoInt instance = null;
		ArrayList<String> expResult = null;
		ArrayList<String> result = instance.getTags();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toggleTag method, of class FileInfoInt.
	 */
	@Test
	public void testToggleTag() {
		System.out.println("toggleTag");
		String value = "";
		FileInfoInt instance = null;
		instance.toggleTag(value);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toStringQueue method, of class FileInfoInt.
	 */
	@Test
	public void testToStringQueue() {
		System.out.println("toStringQueue");
		FileInfoInt instance = null;
		String expResult = "";
		String result = instance.toStringQueue();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toJson method, of class FileInfoInt.
	 */
	@Test
	public void testToJson() {
		System.out.println("toJson");
		FileInfoInt instance = null;
		String expResult = "";
		String result = instance.toJson();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toMap method, of class FileInfoInt.
	 */
	@Test
	public void testToMap() {
		System.out.println("toMap");
		FileInfoInt instance = null;
		Map expResult = null;
		Map result = instance.toMap();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getReplayGain method, of class FileInfoInt.
	 */
	@Test
	public void testGetReplayGain() {
		System.out.println("getReplayGain");
		boolean read = false;
		FileInfoInt instance = null;
		ReplayGain.GainValues expResult = null;
		ReplayGain.GainValues result = instance.getReplayGain(read);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of saveReplayGainToID3 method, of class FileInfoInt.
	 */
	@Test
	public void testSaveReplayGainToID3() {
		System.out.println("saveReplayGainToID3");
		ReplayGain.GainValues gv = null;
		FileInfoInt instance = null;
		instance.saveReplayGainToID3(gv);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
