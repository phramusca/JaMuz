/*
 * Copyright (C) 2014 phramusca
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

package jamuz.utils;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author phramusca
 */
public class FileSystem {

	/**
	 * Move a file
	 * @param sourceFile
	 * @param destFile
	 * @return
	 */
	public static boolean moveFile(File sourceFile, File destFile) {
		try {
			sourceFile=replaceHome(sourceFile);
			destFile=replaceHome(destFile);
			FileUtils.moveFile(sourceFile, destFile);
			return true;
		} catch(FileExistsException ex) {
            //This case will only occur in some particular circumstances
            //(exemple: moving a file (case modified only) under linux on a FAT system (not case sensitive)
            return false;
        } 
        catch (IOException ex) {
			Popup.error(ex);
			return false;
		}
	}
	
	/**
	 * Copy a file
	 * @param sourceFile
	 * @param destFile
     * @throws java.io.IOException
	 */
	public static void copyFile(File sourceFile, File destFile) throws IOException {
			sourceFile=replaceHome(sourceFile);
			destFile=replaceHome(destFile);
			
			FileUtils.copyFile(sourceFile, destFile, true);
    }
	//TODO: How to display progress (especially for videos)
    //http://stackoverflow.com/questions/11427303/progress-bar-with-apache-fileutils-copydirectory
    //https://netbeans.org/bugzilla/show_bug.cgi?id=95305
    //http://stackoverflow.com/questions/15805303/copying-a-file-using-fileutils-copyfile
////FOR DISPLAY progressbar IN A JTable cell: http://stackoverflow.com/questions/13753562/adding-progress-bar-to-each-table-cell-for-file-progress-java
    
	private static File replaceHome(File file) {
		//Replacing ~ by real home path, ONLY IF AT START !
		String fileURL=file.getPath();
		if(fileURL.startsWith("~")) {  //NOI18N
			fileURL=fileURL.replace("~", System.getProperty("user.home"));  //NOI18N
		}
		return new File(fileURL);
	}
}
