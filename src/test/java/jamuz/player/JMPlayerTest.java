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
package jamuz.player;

import java.io.File;
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
public class JMPlayerTest {

	public JMPlayerTest() {
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
	 * Test of getMPlayerPath method, of class JMPlayer.
	 */
	@Test
	public void testGetMPlayerPath() {
		System.out.println("getMPlayerPath");
		JMPlayer instance = new JMPlayer();
		String expResult = "";
		String result = instance.getMPlayerPath();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setMPlayerPath method, of class JMPlayer.
	 */
	@Test
	public void testSetMPlayerPath() {
		System.out.println("setMPlayerPath");
		String mplayerPath = "";
		JMPlayer instance = new JMPlayer();
		instance.setMPlayerPath(mplayerPath);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of open method, of class JMPlayer.
	 */
	@Test
	public void testOpen_File() throws Exception {
		System.out.println("open");
		File file = null;
		JMPlayer instance = new JMPlayer();
		instance.open(file);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of open method, of class JMPlayer.
	 */
	@Test
	public void testOpen_0args() throws Exception {
		System.out.println("open");
		JMPlayer instance = new JMPlayer();
		instance.open();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of close method, of class JMPlayer.
	 */
	@Test
	public void testClose() {
		System.out.println("close");
		JMPlayer instance = new JMPlayer();
		instance.close();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPlayingFile method, of class JMPlayer.
	 */
	@Test
	public void testGetPlayingFile() {
		System.out.println("getPlayingFile");
		JMPlayer instance = new JMPlayer();
		File expResult = null;
		File result = instance.getPlayingFile();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of togglePlay method, of class JMPlayer.
	 */
	@Test
	public void testTogglePlay() {
		System.out.println("togglePlay");
		JMPlayer instance = new JMPlayer();
		instance.togglePlay();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of isPlaying method, of class JMPlayer.
	 */
	@Test
	public void testIsPlaying() {
		System.out.println("isPlaying");
		JMPlayer instance = new JMPlayer();
		boolean expResult = false;
		boolean result = instance.isPlaying();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTimePosition method, of class JMPlayer.
	 */
	@Test
	public void testGetTimePosition() {
		System.out.println("getTimePosition");
		JMPlayer instance = new JMPlayer();
		long expResult = 0L;
		long result = instance.getTimePosition();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setTimePosition method, of class JMPlayer.
	 */
	@Test
	public void testSetTimePosition() {
		System.out.println("setTimePosition");
		long seconds = 0L;
		JMPlayer instance = new JMPlayer();
		instance.setTimePosition(seconds);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getTotalTime method, of class JMPlayer.
	 */
	@Test
	public void testGetTotalTime() {
		System.out.println("getTotalTime");
		JMPlayer instance = new JMPlayer();
		long expResult = 0L;
		long result = instance.getTotalTime();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getVolume method, of class JMPlayer.
	 */
	@Test
	public void testGetVolume() {
		System.out.println("getVolume");
		JMPlayer instance = new JMPlayer();
		float expResult = 0.0F;
		float result = instance.getVolume();
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setVolume method, of class JMPlayer.
	 */
	@Test
	public void testSetVolume() {
		System.out.println("setVolume");
		float volume = 0.0F;
		JMPlayer instance = new JMPlayer();
		instance.setVolume(volume);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getProperty method, of class JMPlayer.
	 */
	@Test
	public void testGetProperty() {
		System.out.println("getProperty");
		String name = "";
		JMPlayer instance = new JMPlayer();
		String expResult = "";
		String result = instance.getProperty(name);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPropertyAsLong method, of class JMPlayer.
	 */
	@Test
	public void testGetPropertyAsLong() {
		System.out.println("getPropertyAsLong");
		String name = "";
		JMPlayer instance = new JMPlayer();
		long expResult = 0L;
		long result = instance.getPropertyAsLong(name);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPropertyAsFloat method, of class JMPlayer.
	 */
	@Test
	public void testGetPropertyAsFloat() {
		System.out.println("getPropertyAsFloat");
		String name = "";
		JMPlayer instance = new JMPlayer();
		float expResult = 0.0F;
		float result = instance.getPropertyAsFloat(name);
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setProperty method, of class JMPlayer.
	 */
	@Test
	public void testSetProperty_String_String() {
		System.out.println("setProperty");
		String name = "";
		String value = "";
		JMPlayer instance = new JMPlayer();
		instance.setProperty(name, value);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setProperty method, of class JMPlayer.
	 */
	@Test
	public void testSetProperty_String_long() {
		System.out.println("setProperty");
		String name = "";
		long value = 0L;
		JMPlayer instance = new JMPlayer();
		instance.setProperty(name, value);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setProperty method, of class JMPlayer.
	 */
	@Test
	public void testSetProperty_String_float() {
		System.out.println("setProperty");
		String name = "";
		float value = 0.0F;
		JMPlayer instance = new JMPlayer();
		instance.setProperty(name, value);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of main method, of class JMPlayer.
	 */
	@Test
	public void testMain() throws Exception {
		System.out.println("main");
		String[] args = null;
		JMPlayer.main(args);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
