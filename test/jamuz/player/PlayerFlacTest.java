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

import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.kc7bfi.jflac.metadata.StreamInfo;
import org.kc7bfi.jflac.util.ByteData;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class PlayerFlacTest {
	
	public PlayerFlacTest() {
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
	 * Test of stop method, of class PlayerFlac.
	 */
	@Test
	public void testStop() {
		System.out.println("stop");
		PlayerFlac instance = new PlayerFlac();
		instance.stop();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of play method, of class PlayerFlac.
	 */
	@Test
	public void testPlay() {
		System.out.println("play");
		String filePath = "";
		PlayerFlac instance = new PlayerFlac();
		String expResult = "";
		String result = instance.play(filePath);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of addListener method, of class PlayerFlac.
	 */
	@Test
	public void testAddListener() {
		System.out.println("addListener");
		LineListener listener = null;
		PlayerFlac instance = new PlayerFlac();
		instance.addListener(listener);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of processStreamInfo method, of class PlayerFlac.
	 */
	@Test
	public void testProcessStreamInfo() {
		System.out.println("processStreamInfo");
		StreamInfo streamInfo = null;
		PlayerFlac instance = new PlayerFlac();
		instance.processStreamInfo(streamInfo);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of processPCM method, of class PlayerFlac.
	 */
	@Test
	public void testProcessPCM() {
		System.out.println("processPCM");
		ByteData pcm = null;
		PlayerFlac instance = new PlayerFlac();
		instance.processPCM(pcm);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of removeListener method, of class PlayerFlac.
	 */
	@Test
	public void testRemoveListener() {
		System.out.println("removeListener");
		LineListener listener = null;
		PlayerFlac instance = new PlayerFlac();
		instance.removeListener(listener);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of run method, of class PlayerFlac.
	 */
	@Test
	public void testRun() {
		System.out.println("run");
		PlayerFlac instance = new PlayerFlac();
		instance.run();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of update method, of class PlayerFlac.
	 */
	@Test
	public void testUpdate() {
		System.out.println("update");
		LineEvent event = null;
		PlayerFlac instance = new PlayerFlac();
		instance.update(event);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
