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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
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
public class ImageUtilsTest {
	
	public ImageUtilsTest() {
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
	 * Test of readIconFromInternet method, of class ImageUtils.
	 */
	@Test
	public void testReadIconFromInternet() {
		System.out.println("readIconFromInternet");
		String url = "";
		int height = 0;
		File file = null;
		ImageIcon expResult = null;
		ImageIcon result = ImageUtils.readIconFromInternet(url, height, file);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of readIconFromFile method, of class ImageUtils.
	 */
	@Test
	public void testReadIconFromFile() {
		System.out.println("readIconFromFile");
		String filename = "";
		int height = 0;
		File file = null;
		ImageIcon expResult = null;
		ImageIcon result = ImageUtils.readIconFromFile(filename, height, file);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of write method, of class ImageUtils.
	 */
	@Test
	public void testWrite_3args() {
		System.out.println("write");
		ImageIcon icon = null;
		File file = null;
		boolean overwrite = false;
		boolean expResult = false;
		boolean result = ImageUtils.write(icon, file, overwrite);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of write method, of class ImageUtils.
	 */
	@Test
	public void testWrite_ImageIcon_File() {
		System.out.println("write");
		ImageIcon icon = null;
		File file = null;
		boolean expResult = false;
		boolean result = ImageUtils.write(icon, file);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toBufferedImage method, of class ImageUtils.
	 */
	@Test
	public void testToBufferedImage_Image() {
		System.out.println("toBufferedImage");
		Image img = null;
		BufferedImage expResult = null;
		BufferedImage result = ImageUtils.toBufferedImage(img);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of toBufferedImage method, of class ImageUtils.
	 */
	@Test
	public void testToBufferedImage_ImageIcon() {
		System.out.println("toBufferedImage");
		ImageIcon icon = null;
		BufferedImage expResult = null;
		BufferedImage result = ImageUtils.toBufferedImage(icon);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getBorderedIfTooBig method, of class ImageUtils.
	 */
	@Test
	public void testGetBorderedIfTooBig() {
		System.out.println("getBorderedIfTooBig");
		BufferedImage image = null;
		ImageIcon expResult = null;
		ImageIcon result = ImageUtils.getBorderedIfTooBig(image);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of scaleImage method, of class ImageUtils.
	 */
	@Test
	public void testScaleImage() {
		System.out.println("scaleImage");
		BufferedImage image = null;
		int newWidth = 0;
		int newHeight = 0;
		BufferedImage expResult = null;
		BufferedImage result = ImageUtils.scaleImage(image, newWidth, newHeight);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of shrinkImage method, of class ImageUtils.
	 */
	@Test
	public void testShrinkImage() {
		System.out.println("shrinkImage");
		BufferedImage image = null;
		int maxIconSize = 0;
		BufferedImage expResult = null;
		BufferedImage result = ImageUtils.shrinkImage(image, maxIconSize);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getEmptyCover method, of class ImageUtils.
	 */
	@Test
	public void testGetEmptyCover() {
		System.out.println("getEmptyCover");
		BufferedImage expResult = null;
		BufferedImage result = ImageUtils.getEmptyCover();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTestCover method, of class ImageUtils.
	 */
	@Test
	public void testGetTestCover() {
		System.out.println("getTestCover");
		BufferedImage expResult = null;
		BufferedImage result = ImageUtils.getTestCover();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
