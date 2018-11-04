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

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import jamuz.utils.DateTime;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class TrackSourceRepo {

    private static HashMap<String, TrackSource> sources = new HashMap<>();

	/**
	 *
	 * @param filename
	 * @return
	 * @throws CannotReadException
	 * @throws IOException
	 * @throws TagException
	 * @throws ReadOnlyFileException
	 * @throws InvalidAudioFrameException
	 */
	public static TrackSource get(String filename) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
        if(!sources.containsKey(filename)) {
            TrackSource source = new TrackSource("1min.mp3");
            sources.put(filename, source);
        }
        return sources.get(filename);
        
    }
    
	/**
	 *
	 */
	protected static final class TrackSource {

		/**
		 *
		 */
		public String filename;

		/**
		 *
		 */
		public int length;

		/**
		 *
		 */
		public String format;

		/**
		 *
		 */
		public String bitRate;

		/**
		 *
		 */
		public long size;

		/**
		 *
		 */
		public String modifDate;

		/**
		 *
		 * @param filename
		 * @param length
		 * @param format
		 * @param bitRate
		 * @param size
		 * @param modifDate
		 */
		public TrackSource(String filename, int length, String format, String bitRate, long size, String modifDate) {
            this.filename = filename;
            this.length = length;
            this.format = format;
            this.bitRate = bitRate;
            this.size = size;
            this.modifDate = modifDate;
        }

		/**
		 *
		 * @param pFilename
		 * @throws CannotReadException
		 * @throws IOException
		 * @throws TagException
		 * @throws ReadOnlyFileException
		 * @throws InvalidAudioFrameException
		 */
        public TrackSource(String pFilename) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
            filename = pFilename;

            File file = getFile();
            modifDate = DateTime.formatUTCtoSqlUTC(new Date(file.lastModified()));
            size = file.length();

            MP3File myMP3File = (MP3File)AudioFileIO.read(file);
            AudioHeader header = myMP3File.getAudioHeader();
            bitRate=header.getBitRate();
            format=header.getFormat();
            length=header.getTrackLength();
        }

		/**
		 *
		 * @return
		 */
		public File getFile() {
            return new File(Settings.getRessourcesPath()+"audioFiles"+File.separator+filename);
        }
    }
}
