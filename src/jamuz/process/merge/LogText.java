/*
 * Copyright (C) 2012 phramusca
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

package jamuz.process.merge;

import jamuz.Jamuz;
import jamuz.utils.Popup;
import jamuz.utils.Inter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;

/**
 * Text log class
 * @author raph
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
	 * @param LogName
	 * @return
	 */
	public boolean createFile(String LogName) {
		try {
			//Create LOG file
			String logFilePath = this.logPath + LogName + ".txt";  //NOI18N
			File f = new File(logFilePath);
			f.createNewFile(); //Creates if not exist
			//Open file for writing
			FileWriter outFile = new FileWriter(logFilePath);
			this.logPrintWriter = new PrintWriter(outFile);
			return true;
		} catch (IOException ex) {
			Popup.error(Inter.get("Error.IOException")+" (create LogText file):\n"+ex.toString());  //NOI18N
			return false;
		}
	}
	
	/**
	 * Add a line to log
	 * @param str
	 */
	public void add(String str) {
		//TODO: Same should be done in createFile (instead of in caller one)
		if(Level.INFO.intValue()>=Jamuz.getLogger().getLevel().intValue()) {
			this.logPrintWriter.println(str);
			this.logPrintWriter.flush();
		}
        
    }
	
	//TODO: Call below function
	/**
	 * Closes log file
	 */
	public void close() {
		this.logPrintWriter.close();
	}
}
