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
public class ProcessAbstractTest {
	
	public ProcessAbstractTest() {
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
	 * Test of abort method, of class ProcessAbstract.
	 */
	@Test
	public void testAbort() {
		System.out.println("abort");
//		ProcessAbstract instance = null;
//		instance.abort();
		//Cannot assert.
	}

	/**
	 * Test of resetAbort method, of class ProcessAbstract.
	 */
	@Test
	public void testResetAbort() {
		System.out.println("resetAbort");
//		ProcessAbstract instance = null;
//		instance.resetAbort();
		//Cannot assert.
	}

	/**
	 * Test of checkAbort method, of class ProcessAbstract.
	 */
	@Test
	public void testCheckAbort() throws Exception {
		System.out.println("checkAbort");
//		ProcessAbstract instance = null;
//		instance.checkAbort();
		//Cannot assert.
	}

	public class ProcessAbstractImpl extends ProcessAbstract {

		public ProcessAbstractImpl() {
			super("");
		}
	}
	
}
