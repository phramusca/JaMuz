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

import javazoom.jl.player.advanced.PlaybackEvent;
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
public class PlayerMP3Test {

	/**
	 *
	 */
	public PlayerMP3Test() {
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
	 * Test of play method, of class PlayerMP3.
	 */
	@Test
	public void testPlay() {
		System.out.println("play");
		String filePath = "";
		boolean resume = false;
		PlayerMP3 instance = new PlayerMP3();
		String expResult = "";
		String result = instance.play(filePath, resume);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of pause method, of class PlayerMP3.
	 */
	@Test
	public void testPause() {
		System.out.println("pause");
		PlayerMP3 instance = new PlayerMP3();
		instance.pause();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of stop method, of class PlayerMP3.
	 */
	@Test
	public void testStop() {
		System.out.println("stop");
		PlayerMP3 instance = new PlayerMP3();
		instance.stop();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of forward method, of class PlayerMP3.
	 */
	@Test
	public void testForward() {
		System.out.println("forward");
		int seconds = 0;
		PlayerMP3 instance = new PlayerMP3();
		instance.forward(seconds);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of rewind method, of class PlayerMP3.
	 */
	@Test
	public void testRewind() {
		System.out.println("rewind");
		int seconds = 0;
		PlayerMP3 instance = new PlayerMP3();
		instance.rewind(seconds);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of setPosition method, of class PlayerMP3.
	 */
	@Test
	public void testSetPosition() {
		System.out.println("setPosition");
		int seconds = 0;
		PlayerMP3 instance = new PlayerMP3();
		instance.setPosition(seconds);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of playbackStarted method, of class PlayerMP3.
	 */
	@Test
	public void testPlaybackStarted() {
		System.out.println("playbackStarted");
		PlaybackEvent playbackEvent = null;
		PlayerMP3 instance = new PlayerMP3();
		instance.playbackStarted(playbackEvent);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of playbackFinished method, of class PlayerMP3.
	 */
	@Test
	public void testPlaybackFinished() {
		System.out.println("playbackFinished");
		PlaybackEvent playbackEvent = null;
		PlayerMP3 instance = new PlayerMP3();
		instance.playbackFinished(playbackEvent);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of run method, of class PlayerMP3.
	 */
	@Test
	public void testRun() {
		System.out.println("run");
		PlayerMP3 instance = new PlayerMP3();
		instance.run();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

}
