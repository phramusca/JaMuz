/*
 * Copyright (C) 2011 phramusca ( https://github.com/phramusca/JaMuz/ )
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.*;

/**
 * FTP class
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Ftp {

	/**
	 * Server
	 */
	protected String server;
	/**
	 * User name
	 */
	protected String username;
	/**
	 * Password
	 */
	protected String password;
	/**
	 * Local folder
	 */
	protected String localFolder;
	/**
	 * Remote folder
	 */
	protected String remoteFolder;
	/**
	 * File name
	 */
	protected String fileName;
	FTPClient ftp = new FTPClient();

	private static Logger logger=null;  //Can't be static. Why should it be (as netbeans says) ?
	
	/**
	 * Set logger
	 * @param logger
	 */
	public static void setLogger(Logger logger) {
		Ftp.logger = logger;
	}
	
	/**
	 * Create a new FTP instance
	 * @param server
	 * @param username
	 * @param password
	 * @param localFolder
	 * @param remoteFolder
	 * @param fileName
	 */
	public Ftp(String server, String username, String password, String localFolder, String remoteFolder, String fileName) {
		this.server = server;
		this.username = username;
		this.password = password;
		this.localFolder = localFolder;
		this.remoteFolder = remoteFolder;
		this.fileName = fileName;
	}

	private boolean connect() {
		try {
			this.ftp.connect(this.server);
			this.ftp.login(this.username, this.password);
			Ftp.logger.log(Level.FINE, "Connected to " + this.server + ".");
			Ftp.logger.log(Level.FINE, this.ftp.getReplyString());
			this.ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.changeWorkingDirectory(this.remoteFolder);
			FTPFile[] files = this.ftp.listFiles();
			Ftp.logger.log(Level.FINE, "Number of files in dir: " + files.length);
			return true;
			
		} catch (IOException ex) {
			Ftp.logger.log(Level.SEVERE, "Ftp.connect()", ex);  //NOI18N
			return false;
		}
	}

	private boolean disconnect() {
		try {
			this.ftp.logout();
			this.ftp.disconnect();
			return true;
		} catch (IOException ex) {
			Ftp.logger.log(Level.SEVERE, "Ftp.disconnect()", ex);  //NOI18N
			return false;
		}
	}

	/**
	 * Get a file from FTP server
	 * @return
	 */
	public boolean getFile() {
		if (this.connect()) {
			FileOutputStream fos = null;
			try {
				File file = new File(this.localFolder + File.separator + this.fileName);
				fos = new FileOutputStream(file);
                return this.ftp.retrieveFile(this.fileName, fos);
			} catch (IOException ex) {
				Ftp.logger.log(Level.SEVERE, "Ftp.getFile()", ex);  //NOI18N
				return false;
			} finally {
				try {
					this.disconnect();
					if(fos!=null) {
                        fos.close();
                    }
				} catch (IOException ex) {
					Ftp.logger.log(Level.SEVERE, "Ftp.getFile()", ex);  //NOI18N
				}
			}
		}
		else {
			return false;
		}
	}

	/**
	 * Send a file to FTP server
	 * @return
	 */
	public boolean sendFile() {
		if (this.connect()) {
			FileInputStream fis = null;
			try {
				File file = new File(this.localFolder + File.separator + this.fileName);
				fis = new FileInputStream(file);
                return this.ftp.storeFile(this.fileName, fis);
			} catch (IOException ex) {
				Ftp.logger.log(Level.SEVERE, "Ftp.sendFile()", ex);  //NOI18N
				return false;
			} finally {
				try {
					this.disconnect();
					if(fis!=null) {
                         fis.close();
                    }
				} catch (IOException ex) {
					Ftp.logger.log(Level.SEVERE, "Ftp.sendFile()", ex);  //NOI18N
					return false;
				}
			}
		}
		else {
			return false;
		}
		
	}

}
