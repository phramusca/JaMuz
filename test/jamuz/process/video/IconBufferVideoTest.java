/*
 * Copyright (C) 2018 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import javax.swing.ImageIcon;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class IconBufferVideoTest {
	
	public IconBufferVideoTest() {
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
	 * Test of getCoverIcon method, of class IconBufferVideo.
	 */
	@Test
	public void testGetCoverIcon() {
		System.out.println("getCoverIcon");
		String url = "http://thetvdb.com/banners/posters/273181-9.jpg";
//		Redirects to url=		 "https://www.thetvdb.com/banners/posters/273181-9.jpg";
		boolean readIfNotFound = true;
		ImageIcon result = IconBufferVideo.getCoverIcon(url, readIfNotFound);
		assertNotNull("because image should be retrieved", result);
	}
	
}
