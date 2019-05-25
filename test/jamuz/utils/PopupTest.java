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
package jamuz.utils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class PopupTest {
	
	public PopupTest() {
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
	 * Test of setLogger method, of class Popup.
	 */
	@Test
	public void testSetLogger() {
		System.out.println("setLogger");
//		Logger logger = null;
//		Popup.setLogger(logger);
		//Cannot assert.
	}

	/**
	 * Test of info method, of class Popup.
	 */
	@Test
	public void testInfo() {
		System.out.println("info");
//		String str = "Hello !";
//		Popup.info(str);
		//Cannot assert, manual test
	}

	/**
	 * Test of warning method, of class Popup.
	 */
	@Test
	public void testWarning() {
		System.out.println("warning");
//		String str = "";
//		Popup.warning(str);
		//Cannot assert, manual test
	}

	/**
	 * Test of error method, of class Popup.
	 */
	@Test
	public void testError_String() {
		System.out.println("error");
//		String str = "";
//		Popup.error(str);
		//Cannot assert, manual test
	}

	/**
	 * Test of error method, of class Popup.
	 */
	@Test
	public void testError_String_Exception() {
		System.out.println("error");
//		String str = "";
//		Exception ex = null;
//		Popup.error(str, ex);
		//Cannot assert, manual test
	}

	/**
	 * Test of error method, of class Popup.
	 */
	@Test
	public void testError_Exception() {
		System.out.println("error");
//		Exception ex = null;
//		Popup.error(ex);
		//Cannot assert, manual test
	}

	/**
	 * Test of error method, of class Popup.
	 */
	@Test
	public void testError_3args() {
		System.out.println("error");
//		String methodName = "";
//		String sql = "";
//		Exception ex = null;
//		Popup.error(methodName, sql, ex);
		//Cannot assert, manual test
	}
	
}
