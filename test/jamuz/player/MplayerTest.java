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

import java.util.ArrayList;
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
public class MplayerTest {
	
	/**
	 *
	 */
	public MplayerTest() {
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
	 * Test of addListener method, of class Mplayer.
	 */
	@Test
	public void testAddListener() {
		System.out.println("addListener");
		MPlaybackListener listener = null;
		Mplayer instance = new Mplayer();
		instance.addListener(listener);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getListeners method, of class Mplayer.
	 */
	@Test
	public void testGetListeners() {
		System.out.println("getListeners");
		Mplayer instance = new Mplayer();
		MPlaybackListener[] expResult = null;
		MPlaybackListener[] result = instance.getListeners();
		assertArrayEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of fireVolumeChanged method, of class Mplayer.
	 */
	@Test
	public void testFireVolumeChanged() {
		System.out.println("fireVolumeChanged");
		float volume = 0.0F;
		Mplayer instance = new Mplayer();
		instance.fireVolumeChanged(volume);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of firePlaybackFinished method, of class Mplayer.
	 */
	@Test
	public void testFirePlaybackFinished() {
		System.out.println("firePlaybackFinished");
		Mplayer instance = new Mplayer();
		instance.firePlaybackFinished();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of firePositionChanged method, of class Mplayer.
	 */
	@Test
	public void testFirePositionChanged() {
		System.out.println("firePositionChanged");
		int position = 0;
		int length = 0;
		Mplayer instance = new Mplayer();
		instance.firePositionChanged(position, length);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setAudioCard method, of class Mplayer.
	 */
	@Test
	public void testSetAudioCard() {
		System.out.println("setAudioCard");
		Mplayer.AudioCard audioCard = null;
		Mplayer instance = new Mplayer();
		instance.setAudioCard(audioCard);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getAudioCards method, of class Mplayer.
	 */
	@Test
	public void testGetAudioCards() {
		System.out.println("getAudioCards");
		Mplayer instance = new Mplayer();
		ArrayList<Mplayer.AudioCard> expResult = null;
		ArrayList<Mplayer.AudioCard> result = instance.getAudioCards();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of startMplayer method, of class Mplayer.
	 */
	@Test
	public void testStartMplayer() {
		System.out.println("startMplayer");
		Mplayer instance = new Mplayer();
		boolean expResult = false;
		boolean result = instance.startMplayer();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of pause method, of class Mplayer.
	 */
	@Test
	public void testPause() {
		System.out.println("pause");
		Mplayer instance = new Mplayer();
		instance.pause();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of stop method, of class Mplayer.
	 */
	@Test
	public void testStop() {
		System.out.println("stop");
		Mplayer instance = new Mplayer();
		boolean expResult = false;
		boolean result = instance.stop();
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of play method, of class Mplayer.
	 */
	@Test
	public void testPlay() {
		System.out.println("play");
		String filePath = "";
		boolean resume = false;
		Mplayer instance = new Mplayer();
		String expResult = "";
		String result = instance.play(filePath, resume);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setPosition method, of class Mplayer.
	 */
	@Test
	public void testSetPosition() {
		System.out.println("setPosition");
		int seconds = 0;
		Mplayer instance = new Mplayer();
		instance.setPosition(seconds);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPosition method, of class Mplayer.
	 */
	@Test
	public void testGetPosition() {
		System.out.println("getPosition");
		Mplayer instance = new Mplayer();
		double expResult = 0.0;
		double result = instance.getPosition();
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPropertyAsDouble method, of class Mplayer.
	 */
	@Test
	public void testGetPropertyAsDouble() {
		System.out.println("getPropertyAsDouble");
		String name = "";
		Mplayer instance = new Mplayer();
		double expResult = 0.0;
		double result = instance.getPropertyAsDouble(name);
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getPropertyAsFloat method, of class Mplayer.
	 */
	@Test
	public void testGetPropertyAsFloat() {
		System.out.println("getPropertyAsFloat");
		String name = "";
		Mplayer instance = new Mplayer();
		float expResult = 0.0F;
		float result = instance.getPropertyAsFloat(name);
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getProperty method, of class Mplayer.
	 */
	@Test
	public void testGetProperty() {
		System.out.println("getProperty");
		String name = "";
		Mplayer instance = new Mplayer();
		String expResult = "";
		String result = instance.getProperty(name);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setVolume method, of class Mplayer.
	 */
	@Test
	public void testSetVolume() {
		System.out.println("setVolume");
		float volume = 0.0F;
		Mplayer instance = new Mplayer();
		instance.setVolume(volume);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of getVolume method, of class Mplayer.
	 */
	@Test
	public void testGetVolume() {
		System.out.println("getVolume");
		Mplayer instance = new Mplayer();
		float expResult = 0.0F;
		float result = instance.getVolume();
		assertEquals(expResult, result, 0.0);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of run method, of class Mplayer.
	 */
	@Test
	public void testRun() {
		System.out.println("run");
		Mplayer instance = new Mplayer();
		instance.run();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
