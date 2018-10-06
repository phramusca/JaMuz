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

import jamuz.FileInfoInt;
import jamuz.process.check.ReplayGain;
import java.io.File;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class TESTreplaygainRead {
    /**
	 * Main program.
	 * @param args
	 */
	public static void main(String[] args) {

		
//		String pathStr = "/home/raph/Bureau/ReplayGain/Musique-copy/King Yellowman/";
		String pathStr = "/home/raph/Bureau/ReplayGain/FLAC-not set/";
		
		
		/////////////////////////////////////////////////////
		File path = new File(pathStr);
		File[] files = path.listFiles();
		if (files != null) {
			for (File file : files) {
				if (!file.isDirectory()) {
					FileInfoInt fileInfoInt = new FileInfoInt(file.getAbsolutePath(), "");
					ReplayGain.GainValues gv = fileInfoInt.getReplayGain(true);
					System.out.println("ReplayGain: "+gv+" // "+gv.isValid()+" // " + fileInfoInt.getFullPath());
					
				}
			}
		}
    } 
}
