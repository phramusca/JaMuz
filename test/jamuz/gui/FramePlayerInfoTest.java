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
package jamuz.gui;

import jamuz.FileInfoInt;
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
public class FramePlayerInfoTest {
	
	public FramePlayerInfoTest() {
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
	 * Test of displayFileInfo method, of class FramePlayerInfo.
	 */
	@Test
	public void testDisplayFileInfo() {
		System.out.println("displayFileInfo");
		FileInfoInt myFileInfo = null;
		FramePlayerInfo instance = null;
		instance.displayFileInfo(myFileInfo);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setSelected method, of class FramePlayerInfo.
	 */
	@Test
	public void testSetSelected() {
		System.out.println("setSelected");
		int index = 0;
		FramePlayerInfo instance = null;
		instance.setSelected(index);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of dispMP3progress method, of class FramePlayerInfo.
	 */
	@Test
	public void testDispMP3progress() {
		System.out.println("dispMP3progress");
		int currentPosition = 0;
		FramePlayerInfo instance = null;
		instance.dispMP3progress(currentPosition);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setMax method, of class FramePlayerInfo.
	 */
	@Test
	public void testSetMax() {
		System.out.println("setMax");
		int maximum = 0;
		FramePlayerInfo instance = null;
		instance.setMax(maximum);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
