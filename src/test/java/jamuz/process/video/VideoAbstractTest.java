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
package jamuz.process.video;

import jamuz.FileInfo;
import jamuz.gui.swing.FileSizeComparable;
import jamuz.utils.SSH;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import javax.swing.ImageIcon;
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
public class VideoAbstractTest {

	/**
	 *
	 */
	public VideoAbstractTest() {
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
	 * Test of getRelativeFullPath method, of class VideoAbstract.
	 */
	@Test
	public void testGetRelativeFullPath() {
		System.out.println("getRelativeFullPath");
		VideoAbstract instance = null;
		String expResult = "";
		String result = instance.getRelativeFullPath();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of moveFilesAndSrt method, of class VideoAbstract.
	 */
	@Test
	public void testMoveFilesAndSrt() {
		System.out.println("moveFilesAndSrt");
		ProcessVideo.PathBuffer buffer = null;
		DbConnVideo conn = null;
		SSH myConn = null;
		VideoAbstract instance = null;
		instance.moveFilesAndSrt(buffer, conn, myConn);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getVideoSummary method, of class VideoAbstract.
	 */
	@Test
	public void testGetVideoSummary() {
		System.out.println("getVideoSummary");
		VideoAbstract instance = null;
		String expResult = "";
		String result = instance.getVideoSummary();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFilesToCleanup method, of class VideoAbstract.
	 */
	@Test
	public void testGetFilesToCleanup() {
		System.out.println("getFilesToCleanup");
		int nbSeasonToKeep = 0;
		int nbEpisodeToKeep = 0;
		boolean keepEnded = false;
		boolean keepCanceled = false;
		VideoAbstract instance = null;
		ArrayList<FileInfoVideo> expResult = null;
		ArrayList<FileInfoVideo> result = instance.getFilesToCleanup(nbSeasonToKeep, nbEpisodeToKeep, keepEnded, keepCanceled);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeFavorite method, of class VideoAbstract.
	 */
	@Test
	public void testRemoveFavorite() {
		System.out.println("removeFavorite");
		VideoAbstract instance = null;
		instance.removeFavorite();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addFavorite method, of class VideoAbstract.
	 */
	@Test
	public void testAddFavorite() {
		System.out.println("addFavorite");
		VideoAbstract instance = null;
		instance.addFavorite();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeFromWatchList method, of class VideoAbstract.
	 */
	@Test
	public void testRemoveFromWatchList() {
		System.out.println("removeFromWatchList");
		VideoAbstract instance = null;
		instance.removeFromWatchList();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addToWatchList method, of class VideoAbstract.
	 */
	@Test
	public void testAddToWatchList() {
		System.out.println("addToWatchList");
		VideoAbstract instance = null;
		instance.addToWatchList();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setRating method, of class VideoAbstract.
	 */
	@Test
	public void testSetRating() {
		System.out.println("setRating");
		VideoRating rating = null;
		VideoAbstract instance = null;
		instance.setRating(rating);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setMyVideo method, of class VideoAbstract.
	 */
	@Test
	public void testSetMyVideo_boolean() {
		System.out.println("setMyVideo");
		boolean search = false;
		VideoAbstract instance = null;
		instance.setMyVideo(search);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isLocal method, of class VideoAbstract.
	 */
	@Test
	public void testIsLocal() {
		System.out.println("isLocal");
		VideoAbstract instance = null;
		boolean expResult = false;
		boolean result = instance.isLocal();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isWatched method, of class VideoAbstract.
	 */
	@Test
	public void testIsWatched() {
		System.out.println("isWatched");
		VideoAbstract instance = null;
		boolean expResult = false;
		boolean result = instance.isWatched();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of moveFileAndSrt method, of class VideoAbstract.
	 */
	@Test
	public void testMoveFileAndSrt() {
		System.out.println("moveFileAndSrt");
		ProcessVideo.PathBuffer buffer = null;
		DbConnVideo conn = null;
		SSH myConn = null;
		FileInfo fileInfo = null;
		String newName = "";
		VideoAbstract instance = null;
		String expResult = "";
		String result = instance.moveFileAndSrt(buffer, conn, myConn, fileInfo, newName);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getStreamDetails4Filename method, of class VideoAbstract.
	 */
	@Test
	public void testGetStreamDetails4Filename() {
		System.out.println("getStreamDetails4Filename");
		FileInfoVideo file = null;
		VideoAbstract instance = null;
		String expResult = "";
		String result = instance.getStreamDetails4Filename(file);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setStatus method, of class VideoAbstract.
	 */
	@Test
	public void testSetStatus() {
		System.out.println("setStatus");
		String status = "";
		VideoAbstract instance = null;
		instance.setStatus(status);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMppaRating method, of class VideoAbstract.
	 */
	@Test
	public void testGetMppaRating() {
		System.out.println("getMppaRating");
		VideoAbstract instance = null;
		String expResult = "";
		String result = instance.getMppaRating();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getRating method, of class VideoAbstract.
	 */
	@Test
	public void testGetRating() {
		System.out.println("getRating");
		VideoAbstract instance = null;
		String expResult = "";
		String result = instance.getRating();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of parseURLStringList method, of class VideoAbstract.
	 */
	@Test
	public void testParseURLStringList_3args() {
		System.out.println("parseURLStringList");
		String string = "";
		String start = "";
		String end = "";
		VideoAbstract instance = null;
		List<String> expResult = null;
		List<String> result = instance.parseURLStringList(string, start, end);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of parseURLStringList method, of class VideoAbstract.
	 */
	@Test
	public void testParseURLStringList_String() {
		System.out.println("parseURLStringList");
		String string = "";
		VideoAbstract instance = null;
		List<String> expResult = null;
		List<String> result = instance.parseURLStringList(string);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTitle method, of class VideoAbstract.
	 */
	@Test
	public void testGetTitle() {
		System.out.println("getTitle");
		VideoAbstract instance = null;
		String expResult = "";
		String result = instance.getTitle();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFiles method, of class VideoAbstract.
	 */
	@Test
	public void testGetFiles() {
		System.out.println("getFiles");
		VideoAbstract instance = null;
		TreeMap<String, FileInfoVideo> expResult = null;
		TreeMap<String, FileInfoVideo> result = instance.getFiles();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTitleOri method, of class VideoAbstract.
	 */
	@Test
	public void testGetTitleOri() {
		System.out.println("getTitleOri");
		VideoAbstract instance = null;
		String expResult = "";
		String result = instance.getTitleOri();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getGenres method, of class VideoAbstract.
	 */
	@Test
	public void testGetGenres() {
		System.out.println("getGenres");
		VideoAbstract instance = null;
		List<String> expResult = null;
		List<String> result = instance.getGenres();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getYear method, of class VideoAbstract.
	 */
	@Test
	public void testGetYear() {
		System.out.println("getYear");
		VideoAbstract instance = null;
		String expResult = "";
		String result = instance.getYear();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getYearInt method, of class VideoAbstract.
	 */
	@Test
	public void testGetYearInt() {
		System.out.println("getYearInt");
		VideoAbstract instance = null;
		int expResult = 0;
		int result = instance.getYearInt();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getThumbnail method, of class VideoAbstract.
	 */
	@Test
	public void testGetThumbnail() {
		System.out.println("getThumbnail");
		boolean readIfNotFound = false;
		VideoAbstract instance = null;
		ImageIcon expResult = null;
		ImageIcon result = instance.getThumbnail(readIfNotFound);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTrailerURL method, of class VideoAbstract.
	 */
	@Test
	public void testGetTrailerURL() {
		System.out.println("getTrailerURL");
		VideoAbstract instance = null;
		String expResult = "";
		String result = instance.getTrailerURL();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getSynopsis method, of class VideoAbstract.
	 */
	@Test
	public void testGetSynopsis() {
		System.out.println("getSynopsis");
		VideoAbstract instance = null;
		String expResult = "";
		String result = instance.getSynopsis();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getImdbURI method, of class VideoAbstract.
	 */
	@Test
	public void testGetImdbURI() {
		System.out.println("getImdbURI");
		VideoAbstract instance = null;
		String expResult = "";
		String result = instance.getImdbURI();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getHomepage method, of class VideoAbstract.
	 */
	@Test
	public void testGetHomepage() {
		System.out.println("getHomepage");
		VideoAbstract instance = null;
		String expResult = "";
		String result = instance.getHomepage();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isSelected method, of class VideoAbstract.
	 */
	@Test
	public void testIsSelected() {
		System.out.println("isSelected");
		VideoAbstract instance = null;
		boolean expResult = false;
		boolean result = instance.isSelected();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setSelected method, of class VideoAbstract.
	 */
	@Test
	public void testSetSelected() {
		System.out.println("setSelected");
		boolean selected = false;
		VideoAbstract instance = null;
		instance.setSelected(selected);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setLength method, of class VideoAbstract.
	 */
	@Test
	public void testSetLength() {
		System.out.println("setLength");
		long length = 0L;
		VideoAbstract instance = null;
		instance.setLength(length);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getLength method, of class VideoAbstract.
	 */
	@Test
	public void testGetLength() {
		System.out.println("getLength");
		VideoAbstract instance = null;
		FileSizeComparable expResult = null;
		FileSizeComparable result = instance.getLength();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getMyMovieDb method, of class VideoAbstract.
	 */
	@Test
	public void testGetMyMovieDb() {
		System.out.println("getMyMovieDb");
		VideoAbstract instance = null;
		MyVideoAbstract expResult = null;
		MyVideoAbstract result = instance.getMyMovieDb();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isMovie method, of class VideoAbstract.
	 */
	@Test
	public void testIsMovie() {
		System.out.println("isMovie");
		VideoAbstract instance = null;
		boolean expResult = false;
		boolean result = instance.isMovie();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setMyVideo method, of class VideoAbstract.
	 */
	@Test
	public void testSetMyVideo_MyMovieDb() {
		System.out.println("setMyVideo");
		MyMovieDb myMovieDb = null;
		VideoAbstract instance = null;
		instance.setMyVideo(myMovieDb);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setMyVideo method, of class VideoAbstract.
	 */
	@Test
	public void testSetMyVideo_MyTvShow() {
		System.out.println("setMyVideo");
		MyTvShow myTvShow = null;
		VideoAbstract instance = null;
		instance.setMyVideo(myTvShow);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class VideoAbstract.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		VideoAbstract instance = null;
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isHD method, of class VideoAbstract.
	 */
	@Test
	public void testIsHD() {
		System.out.println("isHD");
		VideoAbstract instance = null;
		boolean expResult = false;
		boolean result = instance.isHD();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of compareTo method, of class VideoAbstract.
	 */
	@Test
	public void testCompareTo() {
		System.out.println("compareTo");
		Object o = null;
		VideoAbstract instance = null;
		int expResult = 0;
		int result = instance.compareTo(o);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 *
	 */
	public class VideoAbstractImpl extends VideoAbstract {

		/**
		 *
		 */
		public VideoAbstractImpl() {
			super("", "", 0, "", "", "", 0, "", 0, "", "", "", "", "", "", "");
		}

		/**
		 *
		 * @return
		 */
		public String getRelativeFullPath() {
			return "";
		}

		/**
		 *
		 * @param buffer
		 * @param conn
		 * @param myConn
		 */
		public void moveFilesAndSrt(ProcessVideo.PathBuffer buffer, DbConnVideo conn, SSH myConn) {
		}

		/**
		 *
		 * @return
		 */
		public String getVideoSummary() {
			return "";
		}

		/**
		 *
		 * @param nbSeasonToKeep
		 * @param nbEpisodeToKeep
		 * @param keepEnded
		 * @param keepCanceled
		 * @return
		 */
		public ArrayList<FileInfoVideo> getFilesToCleanup(int nbSeasonToKeep, int nbEpisodeToKeep, boolean keepEnded, boolean keepCanceled) {
			return null;
		}

		/**
		 *
		 */
		public void removeFavorite() {
		}

		/**
		 *
		 */
		public void addFavorite() {
		}

		/**
		 *
		 */
		public void removeFromWatchList() {
		}

		/**
		 *
		 */
		public void addToWatchList() {
		}

		/**
		 *
		 * @param rating
		 */
		public void setRating(VideoRating rating) {
		}

		/**
		 *
		 * @param search
		 */
		public void setMyVideo(boolean search) {
		}

		/**
		 *
		 * @return
		 */
		public boolean isLocal() {
			return false;
		}

		/**
		 *
		 * @return
		 */
		public boolean isWatched() {
			return false;
		}

		/**
		 *
		 * @return
		 */
		public boolean isMovie() {
			return false;
		}
	}

}
