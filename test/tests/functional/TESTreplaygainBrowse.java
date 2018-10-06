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
package tests.functional;

import jamuz.Jamuz;
import jamuz.gui.swing.ProgressBar;
import jamuz.process.check.FolderInfo;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class TESTreplaygainBrowse {
    /**
	 * Main program.
	 * @param args
	 */
	public static void main(String[] args) {

//		String pathStr = "/home/raph/Bureau/ReplayGain/Musique-copy/King Yellowman/";
		String pathStr = "/home/raph/Bureau/ReplayGain/FLAC-not set/";
		boolean recalculate = true;

		////////////////////////////////////////////////////////////////////////////
		
		//Configure application
		if(!Jamuz.configure("/home/raph/Bureau/ReplayGain/")) {
			System.exit(1);
		}
		FolderInfo folderInfo = new FolderInfo(
				pathStr, 
				"/home/raph/Bureau/");
		ProgressBar progressBar = new ProgressBar();
		System.out.println("Recomputing Replaygain ("+recalculate+"). Please wait ...");
		folderInfo.browse(recalculate, true, progressBar);
    } 
}
