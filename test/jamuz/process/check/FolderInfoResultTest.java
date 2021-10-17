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

import java.awt.Color;
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
public class FolderInfoResultTest {
	
	public FolderInfoResultTest() {
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
	 * Test of getValue method, of class FolderInfoResult.
	 */
	@Test
	public void testGetValue() {
		System.out.println("getValue");
		FolderInfoResult instance = new FolderInfoResult();
		String expResult = "";
		String result = instance.getValue();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setValue method, of class FolderInfoResult.
	 */
	@Test
	public void testSetValue() {
		System.out.println("setValue");
		String value = "";
		FolderInfoResult instance = new FolderInfoResult();
		instance.setValue(value);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setKO method, of class FolderInfoResult.
	 */
	@Test
	public void testSetKO_0args() {
		System.out.println("setKO");
		FolderInfoResult instance = new FolderInfoResult();
		instance.setKO();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setKO method, of class FolderInfoResult.
	 */
	@Test
	public void testSetKO_boolean() {
		System.out.println("setKO");
		boolean match = false;
		FolderInfoResult instance = new FolderInfoResult();
		instance.setKO(match);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setOK method, of class FolderInfoResult.
	 */
	@Test
	public void testSetOK() {
		System.out.println("setOK");
		FolderInfoResult instance = new FolderInfoResult();
		instance.setOK();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setWarning method, of class FolderInfoResult.
	 */
	@Test
	public void testSetWarning_0args() {
		System.out.println("setWarning");
		FolderInfoResult instance = new FolderInfoResult();
		instance.setWarning();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setWarning method, of class FolderInfoResult.
	 */
	@Test
	public void testSetWarning_boolean() {
		System.out.println("setWarning");
		boolean match = false;
		FolderInfoResult instance = new FolderInfoResult();
		instance.setWarning(match);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of restoreFolderErrorLevel method, of class FolderInfoResult.
	 */
	@Test
	public void testRestoreFolderErrorLevel() {
		System.out.println("restoreFolderErrorLevel");
		FolderInfoResult instance = new FolderInfoResult();
		instance.restoreFolderErrorLevel();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isNotValid method, of class FolderInfoResult.
	 */
	@Test
	public void testIsNotValid() {
		System.out.println("isNotValid");
		FolderInfoResult instance = new FolderInfoResult();
		boolean expResult = false;
		boolean result = instance.isNotValid();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isKO method, of class FolderInfoResult.
	 */
	@Test
	public void testIsKO() {
		System.out.println("isKO");
		FolderInfoResult instance = new FolderInfoResult();
		boolean expResult = false;
		boolean result = instance.isKO();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isWarning method, of class FolderInfoResult.
	 */
	@Test
	public void testIsWarning() {
		System.out.println("isWarning");
		FolderInfoResult instance = new FolderInfoResult();
		boolean expResult = false;
		boolean result = instance.isWarning();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getErrorLevel method, of class FolderInfoResult.
	 */
	@Test
	public void testGetErrorLevel() {
		System.out.println("getErrorLevel");
		FolderInfoResult instance = new FolderInfoResult();
		int expResult = 0;
		int result = instance.getErrorLevel();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDisplayColor method, of class FolderInfoResult.
	 */
	@Test
	public void testGetDisplayColor() {
		System.out.println("getDisplayColor");
		FolderInfoResult instance = new FolderInfoResult();
		Color expResult = null;
		Color result = instance.getDisplayColor();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDisplayText method, of class FolderInfoResult.
	 */
	@Test
	public void testGetDisplayText() {
		System.out.println("getDisplayText");
		FolderInfoResult instance = new FolderInfoResult();
		String expResult = "";
		String result = instance.getDisplayText();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getDisplayToolTip method, of class FolderInfoResult.
	 */
	@Test
	public void testGetDisplayToolTip() {
		System.out.println("getDisplayToolTip");
		FolderInfoResult instance = new FolderInfoResult();
		String expResult = "";
		String result = instance.getDisplayToolTip();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of analyseTrack method, of class FolderInfoResult.
	 */
	@Test
	public void testAnalyseTrack() {
		System.out.println("analyseTrack");
		String tagValue = "";
		String matchValue = "";
		String field = "";
		FolderInfoResult instance = new FolderInfoResult();
		String expResult = "";
		String result = instance.analyseTrack(tagValue, matchValue, field);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of analyseTrackBpm method, of class FolderInfoResult.
	 */
	@Test
	public void testAnalyseTrackBpm() {
		System.out.println("analyseTrackBpm");
		Float tagValue = null;
		Float matchValue = null;
		FolderInfoResult instance = new FolderInfoResult();
		String expResult = "";
		String result = instance.analyseTrackBpm(tagValue, matchValue);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of colorField method, of class FolderInfoResult.
	 */
	@Test
	public void testColorField_String_int() {
		System.out.println("colorField");
		String text = "";
		int errorLevel = 0;
		String expResult = "";
		String result = FolderInfoResult.colorField(text, errorLevel);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of colorField method, of class FolderInfoResult.
	 */
	@Test
	public void testColorField_3args() {
		System.out.println("colorField");
		String text = "";
		int errorLevel = 0;
		boolean html = false;
		String expResult = "";
		String result = FolderInfoResult.colorField(text, errorLevel, html);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of formatNumber method, of class FolderInfoResult.
	 */
	@Test
	public void testFormatNumber() {
		System.out.println("formatNumber");
		int value = 0;
		String expResult = "";
		String result = FolderInfoResult.formatNumber(value);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of equals method, of class FolderInfoResult.
	 */
	@Test
	public void testEquals() {
		System.out.println("equals");
		Object obj = null;
		FolderInfoResult instance = new FolderInfoResult();
		boolean expResult = false;
		boolean result = instance.equals(obj);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of hashCode method, of class FolderInfoResult.
	 */
	@Test
	public void testHashCode() {
		System.out.println("hashCode");
		FolderInfoResult instance = new FolderInfoResult();
		int expResult = 0;
		int result = instance.hashCode();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toString method, of class FolderInfoResult.
	 */
	@Test
	public void testToString() {
		System.out.println("toString");
		FolderInfoResult instance = new FolderInfoResult();
		String expResult = "";
		String result = instance.toString();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
