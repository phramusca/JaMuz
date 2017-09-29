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
package jamuz.tests;

import jamuz.Jamuz;
import jamuz.gui.swing.ProgressBar;
import jamuz.process.check.FileInfoDisplay;
import jamuz.process.check.FolderInfo;
import jamuz.utils.Popup;
import java.io.Console;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class TESTreplaygain {
    /**
	 * Main program.
	 * @param args
	 */
	public static void main(String[] args) {
//		try {
//			FileInfoInt fileInfo = new FileInfoInt(-1, -1, 
//					"/ReplayGain/Musique-copy/Miss Kittin/I Com/", 
//					"01 Professional Distortion.mp3", 
//					120, "format", "bitRate", 0, 0, "My Album", "My Album Artist", 
//					"My Artist", "My Comment", 1, 1,
//					"Pop", 0, "My Title", 1, 1, "1979", 9, 5, 
//					"1970-01-01 00:00:00", "1970-01-01 00:00:00", "1970-01-01 00:00:00", 
//					true, "", FolderInfo.CheckedFlag.OK, 0, 0, 0, 
//					"/home/raph/Bureau/");
//			File sourceFile = new File(Settings.getRessourcesPath()+"audioFiles"
//					+File.separator+"1min.mp3");
//			File testFile = new File(fileInfo.getRootPath()+File.separator
//					+fileInfo.getRelativeFullPath());
////			FileSystem.copyFile(sourceFile, testFile);
//			fileInfo.saveTags(false);
//			fileInfo.saveReplayGain();
//			fileInfo.readReplayGain();
//		} catch (IOException ex) {
//			Logger.getLogger(TESTreplaygain.class.getName()).log(Level.SEVERE, null, ex);
//		}



		//Configure application
		if(!Jamuz.configure("/home/raph/Bureau/ReplayGain/")) {
			System.exit(1);
		}

		FolderInfo folderInfo = new FolderInfo(
				"/home/raph/Bureau/ReplayGain/Musique-copy/King Yellowman/", 
				"/home/raph/Bureau/");
		ProgressBar progressBar = new ProgressBar();
		
		// This recomputes ReplayGain (rg in APE tags)
		System.out.println("Recomputing Replaygain. Please wait ...");
		folderInfo.browse(true, true, progressBar);

		try {
			for(FileInfoDisplay file : folderInfo.getFilesAudio()) {
				//Save tags
//				file.isAudioFile=true;
//				file.saveTags(null, true);
				
				//Read Replaygain tags in APE tags
				System.out.println(file.getReplayGainValues());
				
				//Try reading replaygain from ID3v2 tags
				file.readReplayGain();

				//Try saving replaygain to ID3v2 tags
				//FIXME: IndexOutOfBounds
//				file.saveReplayGain();
				//IF cannot, how to convert ID3V24 to ID3v23
			}
		} catch (Exception ex) {
			Popup.error(ex);
		}
		
		//This save tags
//		folderInfo.save(true, progressBar);

    } 
}
