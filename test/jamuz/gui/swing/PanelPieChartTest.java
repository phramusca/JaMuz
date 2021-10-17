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
package jamuz.gui.swing;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.logging.Logger;
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
public class PanelPieChartTest {
	
	public PanelPieChartTest() {
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
	 * Test of setLogger method, of class PanelPieChart.
	 */
	@Test
	public void testSetLogger() {
		System.out.println("setLogger");
		Logger logger = null;
		PanelPieChart.setLogger(logger);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of paint method, of class PanelPieChart.
	 */
	@Test
	public void testPaint() {
		System.out.println("paint");
		Graphics g = null;
		PanelPieChart instance = new PanelPieChart();
		instance.paint(g);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setPie method, of class PanelPieChart.
	 */
	@Test
	public void testSetPie() {
		System.out.println("setPie");
		String title = "";
		ArrayList<PanelPieChart.PieSlice> slices = null;
		PanelPieChart instance = new PanelPieChart();
		instance.setPie(title, slices);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
