/*
 * Copyright (C) 2015 phramusca <phramusca@gmail.com>
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
package test.helpers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class AlbumBuffer {

	private static final Map<String, Album> albums = new HashMap<>();
	private static final ThreadLocal<String> testSuite = new ThreadLocal<>();

	public static void setTestSuite(String testSuite) {
		if (testSuite == null || testSuite.isBlank()) {
			throw new IllegalArgumentException("Test suite name must not be blank");
		}
		AlbumBuffer.testSuite.set(testSuite);
	}

	/**
	 * Get Album version
	 *
	 * @param mbId
	 * @param version
	 * @return
	 * @throws java.io.IOException
	 * @throws org.jaudiotagger.audio.exceptions.CannotReadException
	 * @throws org.jaudiotagger.tag.TagException
	 * @throws org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
	 * @throws org.jaudiotagger.audio.exceptions.ReadOnlyFileException
	 */
	public static Album getAlbum(String mbId, String version) throws IOException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		String suite = Objects.requireNonNull(testSuite.get(), "Test suite has not been set");
		String key = suite + "--" + mbId + "--" + version;

		if (albums.containsKey(key)) {
			return albums.get(key);
		}

		//Album not found
		Album album = new Album(suite, mbId, version);
		albums.put(key, album);

		return album;
	}
}
