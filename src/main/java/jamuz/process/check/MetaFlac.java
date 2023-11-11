/*
 * Copyright (C) 2012 phramusca ( https://github.com/phramusca/JaMuz/ )
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

package jamuz.process.check;

import jamuz.utils.OS;
import jamuz.utils.Popup;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/**
 * MetaFlac class launches metaflac executable to compute ReplayGain on FLAC files
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class MetaFlac {
	
    private final String path;
	
    /**
     *
	 * @param path
     */  
    public MetaFlac(String path) {
		this.path = path;
    }

    //FIXME !! Delete JaMuz.sh, no more needed
    //FIXME !! Move metaflac.exe, mp3gain.exe and mplayer.exe to system/bin
    
	/**
	 * Compute ReplayGain for FLAC files
	 * @return  
	 */
	public boolean process() {
		File pathFile = new File(path);
		File[] files = pathFile.listFiles();
		if (files != null) {
			List<String> cmdArray = new ArrayList<>();
			if(OS.isWindows()) {
				cmdArray.add("data\\metaflac.exe");
			}
            else {
                //TODO: Test if it works in MacOS for instance
				cmdArray.add("metaflac");
			}
			cmdArray.add("--add-replay-gain");
			//From --help :
//			Calculates the title and album gains/peaks of the given
//			FLAC files as if all the files were part of one album,
//			then stores them in the VORBIS_COMMENT block.  The tags
//			are the same as those used by vorbisgain.  Existing
//			ReplayGain tags will be replaced.  If only one FLAC file
//			is given, the album and title gains will be the same.
//			Since this operation requires two passes, it is always
//			executed last, after all other operations have been
//			completed and written to disk.  All FLAC files specified
//			must have the same resolution, sample rate, and number
//			of channels.  The sample rate must be one of 8, 11.025,
//			12, 16, 22.05, 24, 32, 44.1, or 48 kHz.
			for (File myFile : files) {
                String absolutePath=myFile.getAbsolutePath();
                String fileExtension=FilenameUtils.getExtension(absolutePath);
                //TODO: Find a way to get mime type instead of file extension
                if(fileExtension.toLowerCase().equals("flac")) {  //NOI18N
                    cmdArray.add(absolutePath);
                }
            }
			Runtime runtime = Runtime.getRuntime();
			final Process process;
			try {
				String[] stockArr = new String[cmdArray.size()];
				stockArr = cmdArray.toArray(stockArr);
				process = runtime.exec(stockArr);
				process.waitFor();
				return true;
			} catch (IOException | InterruptedException ex) {
				Popup.error(ex);
				return false;
			}
		}
		else {
			Popup.error("No files found for metaflac in \""+path+"\"");  //NOI18N
			return false;
		}
	}

}