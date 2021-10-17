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

import jamuz.FileInfoInt;
import jamuz.gui.swing.ProgressBar;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
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
public class FolderInfoTest {

	public FolderInfoTest() {
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
	 * Test of getScanType method, of class FolderInfo.
	 */
	@Test
	public void testGetScanType() {
		System.out.println("getScanType");
		FolderInfo instance = new FolderInfo();
		ProcessCheck.ScanType expResult = null;
		ProcessCheck.ScanType result = instance.getScanType();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setScanType method, of class FolderInfo.
	 */
	@Test
	public void testSetScanType() {
		System.out.println("setScanType");
		ProcessCheck.ScanType scanType = null;
		FolderInfo instance = new FolderInfo();
		instance.setScanType(scanType);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setPath method, of class FolderInfo.
	 */
	@Test
	public void testSetPath() {
		System.out.println("setPath");
		String rootPath = "";
		String relativePath = "";
		FolderInfo instance = new FolderInfo();
		instance.setPath(rootPath, relativePath);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of clone method, of class FolderInfo.
	 */
	@Test
	public void testClone() throws Exception {
		System.out.println("clone");
		FolderInfo instance = new FolderInfo();
		FolderInfo expResult = null;
		FolderInfo result = instance.clone();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of reCheck method, of class FolderInfo.
	 */
	@Test
	public void testReCheck() {
		System.out.println("reCheck");
		ICallBackReCheck callback = null;
		ProgressBar progressBar = null;
		FolderInfo instance = new FolderInfo();
		instance.reCheck(callback, progressBar);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of scan method, of class FolderInfo.
	 */
	@Test
	public void testScan() {
		System.out.println("scan");
		boolean full = false;
		ProgressBar progressBar = null;
		FolderInfo instance = new FolderInfo();
		boolean expResult = false;
		boolean result = instance.scan(full, progressBar);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of scanDeleted method, of class FolderInfo.
	 */
	@Test
	public void testScanDeleted() {
		System.out.println("scanDeleted");
		ProgressBar progressBar = null;
		FolderInfo instance = new FolderInfo();
		boolean expResult = false;
		boolean result = instance.scanDeleted(progressBar);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of insertInDb method, of class FolderInfo.
	 */
	@Test
	public void testInsertInDb() {
		System.out.println("insertInDb");
		FolderInfo.CheckedFlag checkedFlag = null;
		FolderInfo instance = new FolderInfo();
		boolean expResult = false;
		boolean result = instance.insertInDb(checkedFlag);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of updateInDb method, of class FolderInfo.
	 */
	@Test
	public void testUpdateInDb() {
		System.out.println("updateInDb");
		FolderInfo.CheckedFlag checkedFlag = null;
		FolderInfo instance = new FolderInfo();
		boolean expResult = false;
		boolean result = instance.updateInDb(checkedFlag);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of browse method, of class FolderInfo.
	 */
	@Test
	public void testBrowse() {
		System.out.println("browse");
		boolean recalculateGain = false;
		boolean readTags = false;
		ProgressBar progressBar = null;
		FolderInfo instance = new FolderInfo();
		boolean expResult = false;
		boolean result = instance.browse(recalculateGain, readTags, progressBar);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of transcode method, of class FolderInfo.
	 */
	@Test
	public void testTranscode() {
		System.out.println("transcode");
		ProgressBar progressBar = null;
		FolderInfo instance = new FolderInfo();
		instance.transcode(progressBar);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of save method, of class FolderInfo.
	 */
	@Test
	public void testSave() {
		System.out.println("save");
		boolean deleteComment = false;
		ProgressBar progressBar = null;
		FolderInfo instance = new FolderInfo();
		instance.save(deleteComment, progressBar);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFirstCoverFromTags method, of class FolderInfo.
	 */
	@Test
	public void testGetFirstCoverFromTags() {
		System.out.println("getFirstCoverFromTags");
		FolderInfo instance = new FolderInfo();
		BufferedImage expResult = null;
		BufferedImage result = instance.getFirstCoverFromTags();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of delete method, of class FolderInfo.
	 */
	@Test
	public void testDelete() {
		System.out.println("delete");
		ProgressBar progressBar = null;
		FolderInfo instance = new FolderInfo();
		instance.delete(progressBar);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of queueAll method, of class FolderInfo.
	 */
	@Test
	public void testQueueAll() {
		System.out.println("queueAll");
		FolderInfo instance = new FolderInfo();
		instance.queueAll();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of group method, of class FolderInfo.
	 */
	@Test
	public void testGroup() {
		System.out.println("group");
		List list = null;
		String groupBy = "";
		ArrayList<String> expResult = null;
		ArrayList<String> result = FolderInfo.group(list, groupBy);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of analyse method, of class FolderInfo.
	 */
	@Test
	public void testAnalyse() throws Exception {
		System.out.println("analyse");
		ProgressBar progressBar = null;
		FolderInfo instance = new FolderInfo();
		instance.analyse(progressBar);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMatch method, of class FolderInfo.
	 */
	@Test
	public void testGetMatch() {
		System.out.println("getMatch");
		int matchId = 0;
		FolderInfo instance = new FolderInfo();
		ReleaseMatch expResult = null;
		ReleaseMatch result = instance.getMatch(matchId);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of analyseMatch method, of class FolderInfo.
	 */
	@Test
	public void testAnalyseMatch() {
		System.out.println("analyseMatch");
		int matchId = 0;
		ProgressBar progressBar = null;
		FolderInfo instance = new FolderInfo();
		instance.analyseMatch(matchId, progressBar);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of analyseMatchTracks method, of class FolderInfo.
	 */
	@Test
	public void testAnalyseMatchTracks_0args() {
		System.out.println("analyseMatchTracks");
		FolderInfo instance = new FolderInfo();
		instance.analyseMatchTracks();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getField method, of class FolderInfo.
	 */
	@Test
	public void testGetField() {
		System.out.println("getField");
		int colId = 0;
		String expResult = "";
		String result = FolderInfo.getField(colId);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of analyseMatchTracks method, of class FolderInfo.
	 */
	@Test
	public void testAnalyseMatchTracks_int() {
		System.out.println("analyseMatchTracks");
		int colId = 0;
		FolderInfo instance = new FolderInfo();
		instance.analyseMatchTracks(colId);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of searchMatches method, of class FolderInfo.
	 */
	@Test
	public void testSearchMatches() {
		System.out.println("searchMatches");
		String album = "";
		String artist = "";
		int discNo = 0;
		int discTotal = 0;
		ProgressBar progressBar = null;
		FolderInfo instance = new FolderInfo();
		boolean expResult = false;
		boolean result = instance.searchMatches(album, artist, discNo, discTotal, progressBar);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of doAction method, of class FolderInfo.
	 */
	@Test
	public void testDoAction() {
		System.out.println("doAction");
		ProgressBar progressBar = null;
		FolderInfo instance = new FolderInfo();
		ActionResult expResult = null;
		ActionResult result = instance.doAction(progressBar);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setIsReplayGainDone method, of class FolderInfo.
	 */
	@Test
	public void testSetIsReplayGainDone() {
		System.out.println("setIsReplayGainDone");
		boolean isReplayGainDone = false;
		FolderInfo instance = new FolderInfo();
		instance.setIsReplayGainDone(isReplayGainDone);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getCoverList method, of class FolderInfo.
	 */
	@Test
	public void testGetCoverList() {
		System.out.println("getCoverList");
		FolderInfo instance = new FolderInfo();
		List<Cover> expResult = null;
		List<Cover> result = instance.getCoverList();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFilesAudio method, of class FolderInfo.
	 */
	@Test
	public void testGetFilesAudio() {
		System.out.println("getFilesAudio");
		FolderInfo instance = new FolderInfo();
		List<FileInfoDisplay> expResult = null;
		List<FileInfoDisplay> result = instance.getFilesAudio();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFilesOther method, of class FolderInfo.
	 */
	@Test
	public void testGetFilesOther() {
		System.out.println("getFilesOther");
		FolderInfo instance = new FolderInfo();
		List<FileInfoInt> expResult = null;
		List<FileInfoInt> result = instance.getFilesOther();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFilesImage method, of class FolderInfo.
	 */
	@Test
	public void testGetFilesImage() {
		System.out.println("getFilesImage");
		FolderInfo instance = new FolderInfo();
		List<FileInfoInt> expResult = null;
		List<FileInfoInt> result = instance.getFilesImage();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFilesConvertible method, of class FolderInfo.
	 */
	@Test
	public void testGetFilesConvertible() {
		System.out.println("getFilesConvertible");
		FolderInfo instance = new FolderInfo();
		List<FileInfoInt> expResult = null;
		List<FileInfoInt> result = instance.getFilesConvertible();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getResults method, of class FolderInfo.
	 */
	@Test
	public void testGetResults() {
		System.out.println("getResults");
		FolderInfo instance = new FolderInfo();
		Map<String, FolderInfoResult> expResult = null;
		Map<String, FolderInfoResult> result = instance.getResults();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRelativePath method, of class FolderInfo.
	 */
	@Test
	public void testGetRelativePath() {
		System.out.println("getRelativePath");
		FolderInfo instance = new FolderInfo();
		String expResult = "";
		String result = instance.getRelativePath();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFullPath method, of class FolderInfo.
	 */
	@Test
	public void testGetFullPath() {
		System.out.println("getFullPath");
		FolderInfo instance = new FolderInfo();
		String expResult = "";
		String result = instance.getFullPath();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isValid method, of class FolderInfo.
	 */
	@Test
	public void testIsValid() {
		System.out.println("isValid");
		FolderInfo instance = new FolderInfo();
		boolean expResult = false;
		boolean result = instance.isValid();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class FolderInfo.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		FolderInfo instance = new FolderInfo();
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of compareTo method, of class FolderInfo.
	 */
	@Test
	public void testCompareTo() {
		System.out.println("compareTo");
		Object o = null;
		FolderInfo instance = new FolderInfo();
		int expResult = 0;
		int result = instance.compareTo(o);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of equals method, of class FolderInfo.
	 */
	@Test
	public void testEquals() {
		System.out.println("equals");
		Object obj = null;
		FolderInfo instance = new FolderInfo();
		boolean expResult = false;
		boolean result = instance.equals(obj);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of hashCode method, of class FolderInfo.
	 */
	@Test
	public void testHashCode() {
		System.out.println("hashCode");
		FolderInfo instance = new FolderInfo();
		int expResult = 0;
		int result = instance.hashCode();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isWarning method, of class FolderInfo.
	 */
	@Test
	public void testIsWarning() {
		System.out.println("isWarning");
		FolderInfo instance = new FolderInfo();
		boolean expResult = false;
		boolean result = instance.isWarning();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getCheckedFlag method, of class FolderInfo.
	 */
	@Test
	public void testGetCheckedFlag() {
		System.out.println("getCheckedFlag");
		FolderInfo instance = new FolderInfo();
		FolderInfo.CheckedFlag expResult = null;
		FolderInfo.CheckedFlag result = instance.getCheckedFlag();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMatches method, of class FolderInfo.
	 */
	@Test
	public void testGetMatches() {
		System.out.println("getMatches");
		FolderInfo instance = new FolderInfo();
		List<ReleaseMatch> expResult = null;
		List<ReleaseMatch> result = instance.getMatches();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getOriginals method, of class FolderInfo.
	 */
	@Test
	public void testGetOriginals() {
		System.out.println("getOriginals");
		FolderInfo instance = new FolderInfo();
		List<ReleaseMatch> expResult = null;
		List<ReleaseMatch> result = instance.getOriginals();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getModifDate method, of class FolderInfo.
	 */
	@Test
	public void testGetModifDate() {
		System.out.println("getModifDate");
		FolderInfo instance = new FolderInfo();
		String expResult = "";
		String result = instance.getModifDate();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isDeleted method, of class FolderInfo.
	 */
	@Test
	public void testIsDeleted() {
		System.out.println("isDeleted");
		FolderInfo instance = new FolderInfo();
		boolean expResult = false;
		boolean result = instance.isDeleted();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRootPath method, of class FolderInfo.
	 */
	@Test
	public void testGetRootPath() {
		System.out.println("getRootPath");
		FolderInfo instance = new FolderInfo();
		String expResult = "";
		String result = instance.getRootPath();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFilesAudioTableModel method, of class FolderInfo.
	 */
	@Test
	public void testGetFilesAudioTableModel() {
		System.out.println("getFilesAudioTableModel");
		FolderInfo instance = new FolderInfo();
		TableModelCheckTracks expResult = null;
		TableModelCheckTracks result = instance.getFilesAudioTableModel();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getNewImage method, of class FolderInfo.
	 */
	@Test
	public void testGetNewImage() {
		System.out.println("getNewImage");
		FolderInfo instance = new FolderInfo();
		BufferedImage expResult = null;
		BufferedImage result = instance.getNewImage();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setNewImage method, of class FolderInfo.
	 */
	@Test
	public void testSetNewImage() {
		System.out.println("setNewImage");
		BufferedImage newImage = null;
		FolderInfo instance = new FolderInfo();
		instance.setNewImage(newImage);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setNewGenre method, of class FolderInfo.
	 */
	@Test
	public void testSetNewGenre() {
		System.out.println("setNewGenre");
		String text = "";
		FolderInfo instance = new FolderInfo();
		instance.setNewGenre(text);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setNewYear method, of class FolderInfo.
	 */
	@Test
	public void testSetNewYear() {
		System.out.println("setNewYear");
		String text = "";
		FolderInfo instance = new FolderInfo();
		instance.setNewYear(text);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setNewAlbum method, of class FolderInfo.
	 */
	@Test
	public void testSetNewAlbum() {
		System.out.println("setNewAlbum");
		String text = "";
		FolderInfo instance = new FolderInfo();
		instance.setNewAlbum(text);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setNewArtist method, of class FolderInfo.
	 */
	@Test
	public void testSetNewArtist() {
		System.out.println("setNewArtist");
		String text = "";
		FolderInfo instance = new FolderInfo();
		instance.setNewArtist(text);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setNewAlbumArtist method, of class FolderInfo.
	 */
	@Test
	public void testSetNewAlbumArtist() {
		System.out.println("setNewAlbumArtist");
		String text = "";
		FolderInfo instance = new FolderInfo();
		instance.setNewAlbumArtist(text);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setMbId method, of class FolderInfo.
	 */
	@Test
	public void testSetMbId() {
		System.out.println("setMbId");
		String mbId = "";
		FolderInfo instance = new FolderInfo();
		instance.setMbId(mbId);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getNewGenre method, of class FolderInfo.
	 */
	@Test
	public void testGetNewGenre() {
		System.out.println("getNewGenre");
		FolderInfo instance = new FolderInfo();
		String expResult = "";
		String result = instance.getNewGenre();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setDuplicateStatus method, of class FolderInfo.
	 */
	@Test
	public void testSetDuplicateStatus() {
		System.out.println("setDuplicateStatus");
		ReleaseMatch match = null;
		FolderInfo instance = new FolderInfo();
		instance.setDuplicateStatus(match);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setAction method, of class FolderInfo.
	 */
	@Test
	public void testSetAction() {
		System.out.println("setAction");
		FolderInfo instance = new FolderInfo();
		instance.setAction();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFilesDb method, of class FolderInfo.
	 */
	@Test
	public void testGetFilesDb() {
		System.out.println("getFilesDb");
		FolderInfo instance = new FolderInfo();
		List<FileInfoInt> expResult = null;
		List<FileInfoInt> result = instance.getFilesDb();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
