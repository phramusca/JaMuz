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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public class BenchmarkTest {
	
	/**
	 *
	 */
	public BenchmarkTest() {
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
	 * Test of get method, of class Benchmark.
	 * @throws java.lang.InterruptedException
	 */
	@Test
	public void testGet() throws InterruptedException {
		System.out.println("get");
		Benchmark instance = new Benchmark(16);
		String result = instance.get();
		assertEquals("Ecoulé: -, restant: -", result);
		Thread.sleep(300);
		result = instance.get();
		assertEquals("Ecoulé: -, restant: 02s", result);
		Thread.sleep(300);
		result = instance.get();
		assertEquals("Ecoulé: -, restant: 02s", result);
		Thread.sleep(300);
		result = instance.get();
		assertEquals("Ecoulé: -, restant: 02s", result);
		Thread.sleep(300);
		result = instance.get();
		assertEquals("Ecoulé: 01s, restant: 02s", result);
		Thread.sleep(300);
		result = instance.get();
		assertEquals("Ecoulé: 01s, restant: 02s", result);
		Thread.sleep(300);
		result = instance.get();
		assertEquals("Ecoulé: 01s, restant: 02s", result);
		Thread.sleep(300);
		result = instance.get();
		assertEquals("Ecoulé: 02s, restant: 02s", result);
		Thread.sleep(300);
		result = instance.get();
		assertEquals("Ecoulé: 02s, restant: 02s", result);
		Thread.sleep(300);
		result = instance.get();
		assertEquals("Ecoulé: 02s, restant: 01s", result);
		Thread.sleep(300);
		result = instance.get();
		assertEquals("Ecoulé: 03s, restant: 01s", result);
		Thread.sleep(300);
		result = instance.get();
		assertEquals("Ecoulé: 03s, restant: 01s", result);
		Thread.sleep(300);
		result = instance.get();
		assertEquals("Ecoulé: 03s, restant: 01s", result);
		Thread.sleep(300);
		result = instance.get();
		assertEquals("Ecoulé: 03s, restant: -", result);
		Thread.sleep(300);
		result = instance.get();
		assertEquals("Ecoulé: 04s, restant: -", result);
		//At this point we are out of initial benchmark size
		Thread.sleep(300);
		result = instance.get();
		assertEquals("Ecoulé: 04s, restant: -", result);
		Thread.sleep(300);
		result = instance.get();
		assertEquals("Ecoulé: 04s, restant: -", result);
		Thread.sleep(300);
		result = instance.get();
		assertEquals("Ecoulé: 05s, restant: -", result);	
	}

	/**
	 * Test of setSize method, of class Benchmark.
	 */
	@Test
	public void testSetSize() {
		System.out.println("setSize");
		//No sense to test this method
	}

	/**
	 * Test of mean method, of class Benchmark.
	 */
	@Test
	public void testMean() {
		System.out.println("mean");
		List<Long> numbers = new ArrayList<>(Arrays.asList(2L, 9L, 12L, 3L, 7L));
		long expResult = 7L;
		long result = Benchmark.mean(numbers);
		assertEquals(expResult, result);
	}

	/**
	 * Test of sum method, of class Benchmark.
	 */
	@Test
	public void testSum() {
		System.out.println("sum");
		List<Long> numbers = new ArrayList<>(Arrays.asList(2L, 9L, 12L, 3L, 7L));
		long expResult = 33L;
		long result = Benchmark.sum(numbers);
		assertEquals(expResult, result);
	}
	
}
