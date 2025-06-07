/*
 * Copyright (C) 2017 phramusca <phramusca@gmail.com>
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

import java.util.EventListener;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
//TODO: Use that kind of stuff in every gui
//http://rom.developpez.com/java-listeners/

public interface MPlaybackListener extends EventListener {

	/**
	 *
	 * @param volume
	 */
	void volumeChanged(float volume);

	/**
	 *
	 */
	void playbackFinished();

	/**
	 *
	 * @param position
	 * @param length
	 */
	void positionChanged(int position, int length);
}

