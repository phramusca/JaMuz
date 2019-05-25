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
package jamuz.process.merge;

import jamuz.FileInfo;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class ICallBackMergeTest {
	
	public ICallBackMergeTest() {
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
	 * Test of completed method, of class ICallBackMerge.
	 */
	@Test
	public void testCompleted() {
		System.out.println("completed");
		ArrayList<FileInfo> errorList = null;
		ArrayList<FileInfo> completedList = null;
		String popupMsg = "";
		String mergeReport = "";
		ICallBackMerge instance = new ICallBackMergeImpl();
		instance.completed(errorList, completedList, popupMsg, mergeReport);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of refresh method, of class ICallBackMerge.
	 */
	@Test
	public void testRefresh() {
		System.out.println("refresh");
		ICallBackMerge instance = new ICallBackMergeImpl();
		instance.refresh();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	public class ICallBackMergeImpl implements ICallBackMerge {

		public void completed(ArrayList<FileInfo> errorList, ArrayList<FileInfo> completedList, String popupMsg, String mergeReport) {
		}

		public void refresh() {
		}
	}
	
}