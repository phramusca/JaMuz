/*
 * Copyright (C) 2023 raph
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

import jamuz.FileInfoInt;
import jamuz.StatItem;
import jamuz.process.check.FolderInfo;
import java.awt.Color;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;
import org.junit.Assert;
import test.helpers.FileInfoIntComparator;

/**
 *
 * @author raph
 */
public class DaoFileTest {

	public DaoFileTest() {
	}

	private static DbConnJaMuz dbConnJaMuz;

	@BeforeClass
	public static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
		dbConnJaMuz = TestUnitSettings.createTempDatabase();
	}

	@AfterClass
	public static void tearDownClass() {
		TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testFile() {

		System.out.println("testFile");

		String rootPath = "/root/path/";
		dbConnJaMuz.file().setLocationLibrary(rootPath);
		
		int index = 1;
		int[] keyPath = new int[1];
		dbConnJaMuz.path().lock().insert("relative/path" + index + "/", new Date(), FolderInfo.CheckedFlag.UNCHECKED, "mmmbbbiiidd", keyPath);
		FileInfoInt fileInfoInt = new FileInfoInt("relative/path" + index + "/file" + index + ".ext",  rootPath);
		fileInfoInt.setIdPath(keyPath[0]);
		int[] keyFile = new int[1];
		dbConnJaMuz.file().lock().insert(fileInfoInt, keyFile);
		fileInfoInt.setIdFile(keyFile[0]);

		FileInfoInt file = dbConnJaMuz.file().getFile(fileInfoInt.getIdFile(), "");

		assertEquals(fileInfoInt, file);

		FileInfoIntComparator fileInfoIntComparator = new FileInfoIntComparator();

		// Use the custom comparator directly in the assertion
		if (fileInfoIntComparator.compare(fileInfoInt, file) != 0) {
			Assert.fail("Objects are not equal");
		}

		//FIXME TEST Review FileInfoIntComparator (commented values)
		
		//FIXME TEST Negative cases
		//FIXME TEST Check other constraints
	}

//	class FileInfoIntComparator implements Comparator<FileInfoInt> {
//
//		@Override
//		public int compare(FileInfoInt t, FileInfoInt t1) {
//			throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//		}
//
////		@Override
////		public int compare(FileInfoInt o1, FileInfoInt o2) {
////			//FIXME TEST ! Compare other fields !!!!
////			return o1.getFilename().compareTo(o2.getFilename());
////		}
//		
//		
//	}

	/**
	 * Test of setLocationLibrary method, of class DaoFile.
	 */
	@Test
	public void testSetLocationLibrary() {
		System.out.println("setLocationLibrary");
		String locationLibrary = "";
		DaoFile instance = null;
		instance.setLocationLibrary(locationLibrary);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of lock method, of class DaoFile.
	 */
	@Test
	public void testLock() {
		System.out.println("lock");
		DaoFile instance = null;
		DaoFileWrite expResult = null;
		DaoFileWrite result = instance.lock();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getYear method, of class DaoFile.
	 */
	@Test
	public void testGetYear() {
		System.out.println("getYear");
		String maxOrMin = "";
		DaoFile instance = null;
		double expResult = 0.0;
		double result = instance.getYear(maxOrMin);
		assertEquals(expResult, result, 0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getStatItem method, of class DaoFile.
	 */
	@Test
	public void testGetStatItem() {
		System.out.println("getStatItem");
		String field = "";
		String value = "";
		String table = "";
		String label = "";
		Color color = null;
		boolean[] selRatings = null;
		DaoFile instance = null;
		StatItem expResult = null;
		StatItem result = instance.getStatItem(field, value, table, label, color, selRatings);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getSelectionList4Stats method, of class DaoFile.
	 */
	@Test
	public void testGetSelectionList4Stats() {
		System.out.println("getSelectionList4Stats");
		ArrayList<StatItem> stats = null;
		String field = "";
		boolean[] selRatings = null;
		DaoFile instance = null;
		instance.getSelectionList4Stats(stats, field, selRatings);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPercentRatedForStats method, of class DaoFile.
	 */
	@Test
	public void testGetPercentRatedForStats() {
		System.out.println("getPercentRatedForStats");
		ArrayList<StatItem> stats = null;
		DaoFile instance = null;
		instance.getPercentRatedForStats(stats);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFiles method, of class DaoFile.
	 */
	@Test
	public void testGetFiles_11args() {
		System.out.println("getFiles");
		ArrayList<FileInfoInt> myFileInfoList = null;
		String selGenre = "";
		String selArtist = "";
		String selAlbum = "";
		boolean[] selRatings = null;
		boolean[] selCheckedFlag = null;
		int yearFrom = 0;
		int yearTo = 0;
		float bpmFrom = 0.0F;
		float bpmTo = 0.0F;
		int copyRight = 0;
		DaoFile instance = null;
		boolean expResult = false;
		boolean result = instance.getFiles(myFileInfoList, selGenre, selArtist, selAlbum, selRatings, selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo, copyRight);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFile method, of class DaoFile.
	 */
	@Test
	public void testGetFile() {
		System.out.println("getFile");
		int idFile = 0;
		String destExt = "";
		DaoFile instance = null;
		FileInfoInt expResult = null;
		FileInfoInt result = instance.getFile(idFile, destExt);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFiles method, of class DaoFile.
	 */
	@Test
	public void testGetFiles_ArrayList_int() {
		System.out.println("getFiles");
		ArrayList<FileInfoInt> files = null;
		int idPath = 0;
		DaoFile instance = null;
		boolean expResult = false;
		boolean result = instance.getFiles(files, idPath);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getFilesStats method, of class DaoFile.
	 */
	@Test
	public void testGetFilesStats_10args() {
		System.out.println("getFilesStats");
		String selGenre = "";
		String selArtist = "";
		String selAlbum = "";
		boolean[] selRatings = null;
		boolean[] selCheckedFlag = null;
		int yearFrom = 0;
		int yearTo = 0;
		float bpmFrom = 0.0F;
		float bpmTo = 0.0F;
		int copyRight = 0;
		DaoFile instance = null;
		String expResult = "";
		String result = instance.getFilesStats(selGenre, selArtist, selAlbum, selRatings, selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo, copyRight);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
