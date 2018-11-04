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

package jamuz.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.apache.commons.io.FilenameUtils;

/**
 * Text log class
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class LogText {
	
	private PrintWriter logPrintWriter;
	private final String logPath;

	/**
	 * Creates a new LogText instance
	 * @param logPath
	 */
	public LogText(String logPath) {
		this.logPath = logPath;
	}
	
	/**
	 * Creates a text log file
	 * @param LogFileName
	 * @return
	 */
	public boolean createFile(String LogFileName) {
		try {
			
			String ext = FilenameUtils.getExtension(LogFileName);
			String baseName = FilenameUtils.getBaseName(LogFileName);
			baseName=StringManager.removeIllegal(baseName);
			String logFilePath = FilenameUtils.concat(logPath, 
					baseName+(ext.equals("")?"":("."+ext)));  //NOI18N
			File f = new File(logFilePath);
			f.createNewFile(); //Creates if not exist
			//Open file for writing
			FileWriter outFile = new FileWriter(logFilePath);
			this.logPrintWriter = new PrintWriter(outFile);
			return true;
		} catch (IOException ex) {
			Popup.error(Inter.get("Error.IOException")
					+" (create LogText file):\n"+ex.toString());  //NOI18N
			return false;
		}
	}
	
	/**
	 * Add a line to log
	 * @param str
	 */
	public void add(String str) {
		this.logPrintWriter.println(str);
		this.logPrintWriter.flush();
    }
	
	//TODO: Call below function
	/**
	 * Closes log file
	 */
	public void close() {
		this.logPrintWriter.close();
	}
}
