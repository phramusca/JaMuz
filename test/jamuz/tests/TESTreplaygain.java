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

import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.gui.swing.ProgressBar;
import jamuz.process.check.FileInfoDisplay;
import jamuz.process.check.FolderInfo;
import jamuz.utils.Popup;

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
				System.out.println("-------------------------------------------");  //NOI18N
				System.out.println(file.getRelativeFullPath());
				//Save tags
//				file.isAudioFile=true;
//				file.saveTags(null, true);
				System.out.println("-------------------------------------------");  //NOI18N
				System.out.println("readReplayGainFromID3: "+file.readReplayGainFromID3());
				FileInfoInt.GainValues gv = file.readReplayGainFromAPE();
				System.out.println("readReplayGainFromAPE: "+gv);
				System.out.println("Save replaygain to ID3v2 tags");
				file.saveReplayGainToID3(gv);
				System.out.println("readReplayGainFromID3: "+file.readReplayGainFromID3());
				System.out.println("-------------------------------------------");  //NOI18N
			}
		} catch (Exception ex) {
			Popup.error(ex);
		}
    } 
}
