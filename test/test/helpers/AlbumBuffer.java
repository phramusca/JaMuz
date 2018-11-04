/*
 * Copyright (C) 2015 phramusca ( https://github.com/phramusca/JaMuz/ )
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
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class AlbumBuffer {
    private static final Map<String, Album> albums = new HashMap<>();

    /**
     * Get Album version
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
		String key = mbId + "--" + version;
        
        if(albums.containsKey(key)) {
            return albums.get(key);
        }
        
        //Album not found
        Album album = new Album(mbId, version);        
        albums.put(key, album);
        
        return album;
	}
}
