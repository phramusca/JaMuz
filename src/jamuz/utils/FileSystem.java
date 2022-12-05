/*
 * Copyright (C) 2014 phramusca ( https://github.com/phramusca/JaMuz/ )
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
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
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
    
	/**
	 *
	 * @param file
	 * @return
	 */
	public static File replaceHome(File file) {
		return replaceHome(file.getPath());
	}
	
	/**
	 *
	 * @param fileURL
	 * @return
	 */
	public static File replaceHome(String fileURL) {
		//Replacing ~ by real home path, ONLY IF AT START !
		if(fileURL.startsWith("~")) {  //NOI18N
			fileURL=System.getProperty("user.home").concat(fileURL.substring(1));  //NOI18N
		}
		return new File(fileURL);
	}
	
	/**
	* Attempts to calculate the size of a file or directory.
	* 
	* <p>
	* Since the operation is non-atomic, the returned value may be inaccurate.
	* However, this method is quick and does its best.
	 * @param path
	 * @return 
	*/
   public static long size(Path path) {

	   final AtomicLong size = new AtomicLong(0);

	   try {
		   Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			   @Override
			   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

				   size.addAndGet(attrs.size());
				   return FileVisitResult.CONTINUE;
			   }

			   @Override
			   public FileVisitResult visitFileFailed(Path file, IOException exc) {

				   System.out.println("skipped: " + file + " (" + exc + ")");
				   // Skip folders that can't be traversed
				   return FileVisitResult.CONTINUE;
			   }

			   @Override
			   public FileVisitResult postVisitDirectory(Path dir, IOException exc) {

				   if (exc != null) {
					   System.out.println("had trouble traversing: " + dir + " (" + exc + ")");
				   }
				   // Ignore errors traversing a folder
				   return FileVisitResult.CONTINUE;
			   }
		   });
	   } catch (IOException e) {
		   throw new AssertionError("walkFileTree will not throw IOException if the FileVisitor does not");
	   }

	   return size.get();
   }
   
   	/**
	* Checks, whether the child directory is a subdirectory of the base 
	* directory.
	*
	* @param base the base directory.
	* @param child the suspected child directory.
	* @return true, if the child is a subdirectory of the base directory.
	* @throws IOException if an IOError occured during the test.
	*/
	public static boolean isSubDirectory(File base, File child) throws IOException {
		base = base.getCanonicalFile();
		child = child.getCanonicalFile();

		File parentFile = child;
		while (parentFile != null) {
			if (base.equals(parentFile)) {
				return true;
			}
			parentFile = parentFile.getParentFile();
		}
		return false;
	}
}
