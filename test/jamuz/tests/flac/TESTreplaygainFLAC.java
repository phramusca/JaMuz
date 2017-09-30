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
import java.io.File;

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
							FileInfoInt fileInfoInt = new FileInfoInt(file.getAbsolutePath(), "");
							System.out.println("readReplayGainFromFlac: "+fileInfoInt.readReplayGainFromFlac());
						}
					}
				}
			}
    } 
}
