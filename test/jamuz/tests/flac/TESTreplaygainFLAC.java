/*
 * Copyright (C) 2016 phramusca ( https://github.com/phramusca/JaMuz/ )
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
package jamuz.tests.flac;

import jamuz.FileInfoInt;
import jamuz.process.check.FileInfoDisplay;
import jamuz.tests.flac.dk.stigc.javatunes.audioplayer.other.Track;
import jamuz.tests.flac.dk.stigc.javatunes.audioplayer.tagreader.TagReaderManager;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class TESTreplaygainFLAC {
    /**
	 * Main program.
	 * @param args
	 */
	public static void main(String[] args) {

			File path = new File("/home/raph/Bureau/ReplayGain/FLAC-not set/");
			File[] files = path.listFiles();
			if (files != null) {
				for (File file : files) {
					if (!file.isDirectory()) {
						if(file.getAbsolutePath().toLowerCase().endsWith("flac")) {
//							Track track = new TagReaderManager().read(file);
//							System.out.println(track.toString());
//							System.out.println("Replaygain: "+track.replaygain+" // "+track.replaygainAlbumMode);
//							System.out.println("------------------------------------------------");
							FileInfoInt fileInfoInt = new FileInfoInt(file.getAbsolutePath(), "");
							System.out.println("readReplayGainFromFlac: "+fileInfoInt.readReplayGainFromFlac());
						}
					}
				}
			}
//			File file = new File("/home/raph/Bureau/ReplayGain/FLAC/Jacky Galou - Chansons de France/01 - Mes vieilles chansons.flac");
//			Track track = new TagReaderManager().read(file);
//			System.out.println("Replaygain: "+track.replaygain+" // "+track.replaygainAlbumMode);
//			file = new File("/home/raph/Bureau/ReplayGain/Musique-copy/King Yellowman/01 Jamaica Nice_Take Me Home Country Roads.mp3");
//			track = new TagReaderManager().read(file);
//			System.out.println("Replaygain: "+track.replaygain+" // "+track.replaygainAlbumMode);
//			FileInfoInt fileInfo = new FileInfoInt("ReplayGain/Musique-copy/King Yellowman/01 Jamaica Nice_Take Me Home Country Roads.mp3", "/home/raph/Bureau/");
//			System.out.println(fileInfo.readReplayGainFromID3());
		
		
    } 
}
