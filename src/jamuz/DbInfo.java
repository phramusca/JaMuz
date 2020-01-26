/*
 * Copyright (C) 2015 phramusca ( https://github.com/phramusca/JaMuz/ )
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

package jamuz;

import jamuz.remote.ClientCanal;
import jamuz.remote.PanelRemote;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.logging.Level;
import jamuz.utils.DateTime;
import jamuz.utils.FileSystem;
import jamuz.utils.Ftp;
import jamuz.utils.Inter;
import jamuz.utils.Popup;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DbInfo {

	/**
	 * FTP user
	 */
	private String ftpUser="";
	/**
	 * FTP password
	 */
	private String ftpPwd="";
	/**
	 * FTP server
	 */
	private String ftpServer="";
	/**
	 * FTP remote folder
	 */
	private String ftpRemoteFolder="";
	/**
	 * FTP filename
	 */
	private String ftpFileName="";
	/**
	 * Database user, if applicable
	 */
	protected final String user;
	/**
	 * Database password, if applicable
	 */
	protected final String pwd;
	
	private ClientCanal canal;
	
    /**
     *
     * @param libType
     * @param location
     * @param user
     * @param pwd
	 * @param canal
     */
    public DbInfo(LibType libType, String location, String user, String pwd, ClientCanal canal) {
		this.canal = ClientCanal.NONE;
        this.libType = libType;
        this.locationOri = location;
        this.locationWork = location;
        this.user = user;
        this.pwd = pwd;
        
        //Parse FTP information
		if (this.locationOri.startsWith("ftp://")) {  //NOI18N
			String[] split1 = this.locationOri.split("//");  //NOI18N
			String[] split2 = split1[1].split(":");  //NOI18N
			this.ftpUser = split2[0];
			String[] split3 = split2[1].split("@");  //NOI18N
			this.ftpPwd = split3[0];
			//	192.168.0.12/F/apps/XBMC_XBOXT_30832/XBMC/UserData/Database/MyMusic7.db
			int firstDelim = split3[1].indexOf("/");  //NOI18N
			int lastDelim = split3[1].lastIndexOf("/");  //NOI18N
			this.ftpServer = split3[1].substring(0, firstDelim);
			this.ftpRemoteFolder = split3[1].substring(firstDelim, lastDelim);
			this.ftpFileName = split3[1].substring(lastDelim + 1, split3[1].length());
		}
		this.canal = canal;
    }
	
	/**
     *
     * @param libType
     * @param location
     * @param user
     * @param pwd
     */
    public DbInfo(LibType libType, String location, String user, String pwd) {
		this(libType, location, user, pwd, ClientCanal.NONE);
	}

    /**
	 * Return Ftp instance
	 * @param localFolder 
	 * @return
	 */
	public Ftp getFtp(String localFolder) {
		return new Ftp(this.ftpServer, this.ftpUser, this.ftpPwd, localFolder, 
				this.ftpRemoteFolder, this.ftpFileName);
	}

	/**
	 * Check the stat source
	 * @return
	 */
	public boolean check() {
        switch (this.libType) {
            case Sqlite:
                if (locationOri.startsWith("ftp://")) {  //NOI18N
                    //TODO: Check FTP connect (and file ?)
                    return true;
                } else if (!canal.equals(ClientCanal.NONE)) { //For JaMuz Remote merge
					String clientId = this.locationOri+"-"+this.canal.name();
					if(!PanelRemote.isConnected(clientId)) {
						Popup.warning(java.text.MessageFormat.format(
								"<html>"+Inter.get("Msg.Sync.DestinationDoesNotExist")+"</html>", 
								new Object[] {clientId}));  //NOI18N
						return false;
					}
                    return true;
				}
                else {
                    //Checking if file exists
                    File myFile = FileSystem.replaceHome(locationOri);
                    if (myFile.exists()) {
                        return true;
                    }
                    else {
                        Popup.error(java.text.MessageFormat.format(
								Inter.get("Error.PathNotFound"), 
								new Object[] {locationOri})); //NOI18N
                        return false;
                    }
                }
            case MySQL:
                //TODO: Check database connect
                return true;
            default:
                Popup.error(java.text.MessageFormat.format(
						Inter.get("Error.DbTypeNotSupported"), 
						new Object[] {this.libType})); //NOI18N
                return false;
        }
	}
	
	/**
	 * Copy database from/to log sub path from/to original location
	 * @param receive
	 * @param locationWork
	 * @return
	 */
	public boolean copyDB(boolean receive, String locationWork) {
		switch (this.libType) {
			case Sqlite:  //NOI18N
				String fileName;  //NOI18N
				if (this.locationOri.startsWith("ftp://")) {  //NOI18N
					Ftp myFTP = this.getFtp(locationWork);
					if(receive) {
						fileName = this.ftpFileName;
						if (!(myFTP.getFile())) {
							Popup.error(MessageFormat.format(
									Inter.get("Error.DatabaseFileRetrieve"), 
									new Object[] {this.locationOri}));  //NOI18N
							return false;
						}
						this.locationWork=locationWork + fileName;
					}
					else {
						if (!(myFTP.sendFile())) {
							Popup.error(MessageFormat.format(
									Inter.get("Error.DataBaseFileSend"), 
									new Object[] {this.locationOri}));  //NOI18N
							return false;
						}
					}
					return true;
				} 
                else {
					File sourceFile;
					File destinationFile;
					if(receive) {
						sourceFile = FileSystem.replaceHome(this.locationOri);
						fileName = sourceFile.getName();
						destinationFile = new File(locationWork + fileName);
						this.locationWork=locationWork + fileName;
					}
					else {
						sourceFile = new File(this.locationWork);
						destinationFile = new File(this.locationOri);
					}
                    try {
                        FileSystem.copyFile(sourceFile, destinationFile);
                    } catch (IOException ex) {
						Popup.error("sourceFile="+sourceFile+", destinationFile="
								+destinationFile, ex);
                        return false;
                    }
				}			
			case MySQL:  //NOI18N
				//No need to retrieve a mysql database ...
				return true;
			default:
				Popup.error(MessageFormat.format(
						Inter.get("Error.DbTypeNotSupported"), 
						new Object[] {this.libType}));  //NOI18N
				return false;
		}
	}
    
	/**
	 *
	 * @param destinationPath
	 * @return
	 */
	public boolean backupDB(String destinationPath) {
		switch (this.libType) {
			case Sqlite:  //NOI18N
				//Create a backup of that database file
				File workingFile = new File(this.locationWork);
				File backupFile = new File(this.locationWork + ".bak");  //NOI18N
                try {
                    FileSystem.copyFile(workingFile, backupFile);
                } catch (IOException ex) {
                    Popup.error(ex);
                    return false;
                }
                return true;
			case MySQL:  //NOI18N
				String mysqlBackupFile = destinationPath + 
						this.locationOri.replace("/", "-") + "--" + DateTime.getCurrentLocal(DateTime.DateTimeFormat.FILE) + ".sql";  //NOI18N
				String myCmdLine = "mysqldump -u "+ this.user +" -p"+this.pwd
						+" --opt --compatible=mysql40 "
						+this.locationOri.substring(this.locationOri.indexOf("/")+1);  //NOI18N
				Jamuz.getLogger().finest(myCmdLine);
				Runtime runtime = Runtime.getRuntime();
				final Process process;
				final Process process2;
				final PrintWriter mysqlBackupWriter;

				try {
					//Create database backup file
					File f = new File(mysqlBackupFile);
					f.createNewFile(); //Creates if not exist
					//Open file for writing
					FileWriter outFile = new FileWriter(mysqlBackupFile);
					mysqlBackupWriter = new PrintWriter(outFile);

					process = runtime.exec(myCmdLine);

					//TODO: Return false if an error is detected 
					//(check rc from process and/or ErrorStream)

					// Consommation de la sortie standard de l'application externe dans un Thread separe
					new Thread("Thread.DbInfo.backupDB.mySQL.mysqldump.InputStream") {
						@Override
						public void run() {
							try {
								BufferedReader reader = new BufferedReader(
										new InputStreamReader(
												process.getInputStream()));
								String line;
								try {
									while((line = reader.readLine()) != null) {
									mysqlBackupWriter.println(line);
									mysqlBackupWriter.flush();
									}
								} finally {
									reader.close();
								}
							} catch(IOException ex) {
								Jamuz.getLogger().log(Level.SEVERE, "", ex);  //NOI18N
							}
						}
					}.start();

					// Consommation de la sortie d'erreur de l'application externe dans un Thread separe
					new Thread("Thread.DbInfo.backupDB.mySQL.mysqldump.ErrorStream") {
						@Override
						public void run() {
							try {
								BufferedReader reader = new BufferedReader(
										new InputStreamReader(
												process.getErrorStream()));
								String line;
								try {
									while((line = reader.readLine()) != null) {
										//Logging eventual errors from mysqldump process
										Jamuz.getLogger().severe(line);
									}
								} finally {
									reader.close();
								}
							} catch(IOException ex) {
								Jamuz.getLogger().log(Level.SEVERE, "", ex);  //NOI18N
							}
						}
					}.start();

					process.waitFor();

					//Bzip the file
					myCmdLine="bzip2 -fq "+mysqlBackupFile;  //NOI18N
					process2 = runtime.exec(myCmdLine);
					Jamuz.getLogger().finest(myCmdLine);

					// Consommation de la sortie d'erreur de l'application externe dans un Thread separe
					new Thread("Thread.DbInfo.backupDB.mySQL.bzip2.ErrorStream") {
						@Override
						public void run() {
							try {
								BufferedReader reader = new BufferedReader(
										new InputStreamReader(
												process2.getErrorStream()));
								String line;
								try {
								while((line = reader.readLine()) != null) {
									//Logging eventual errors from bzip2 process
									Jamuz.getLogger().severe(line);
								}
								} finally {
								reader.close();
								}
							} catch(IOException ex) {
								Jamuz.getLogger().log(Level.SEVERE, "", ex);  //NOI18N
							}
						}
					}.start();
					process.waitFor();
					return true;
					
				} catch (IOException | InterruptedException ex) {
					Popup.error(ex);
					return false; 
				}
			default:
				Popup.error(MessageFormat.format(Inter.get("Error.DbTypeNotSupported"), new Object[] {this.libType}));  //NOI18N
				return false;
		}
	}
    
	/**
	 *
	 */
	protected final LibType libType;
	
	/**
	 *
	 */
	public enum LibType {

		/**
		 *
		 */
		Sqlite,

		/**
		 *
		 */
		MySQL
	}
    /**
	 * Original location
	 */
	protected String locationOri;

	/**
	 *
	 * @return
	 */
	public String getLocationOri() {
        return locationOri;
    }
    
	/**
	 * Work location
	 */
	protected String locationWork;

	/**
	 *
	 * @return
	 */
	public String getLocationWork() {
        return locationWork;
    }
    
	/**
	 *
	 * @param locationWork
	 */
	public void setLocationWork(String locationWork) {
        this.locationWork = locationWork;
    }
    
	
}
