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
public class MPlaybackListenerTest {

	/**
	 *
	 */
	public MPlaybackListenerTest() {
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
	 * Test of volumeChanged method, of class MPlaybackListener.
	 */
	@Test
	public void testVolumeChanged() {
		System.out.println("volumeChanged");
		float volume = 0.0F;
		MPlaybackListener instance = new MPlaybackListenerImpl();
		instance.volumeChanged(volume);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of playbackFinished method, of class MPlaybackListener.
	 */
	@Test
	public void testPlaybackFinished() {
		System.out.println("playbackFinished");
		MPlaybackListener instance = new MPlaybackListenerImpl();
		instance.playbackFinished();
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of positionChanged method, of class MPlaybackListener.
	 */
	@Test
	public void testPositionChanged() {
		System.out.println("positionChanged");
		int position = 0;
		int length = 0;
		MPlaybackListener instance = new MPlaybackListenerImpl();
		instance.positionChanged(position, length);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 *
	 */
	public class MPlaybackListenerImpl implements MPlaybackListener {

		/**
		 *
		 * @param volume
		 */
		public void volumeChanged(float volume) {
		}

		/**
		 *
		 */
		public void playbackFinished() {
		}

		/**
		 *
		 * @param position
		 * @param length
		 */
		public void positionChanged(int position, int length) {
		}
	}

}
