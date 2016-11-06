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

import jamuz.DbInfo.LibType;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import jamuz.utils.Ftp;
import jamuz.utils.Inter;
import jamuz.utils.OS;
import jamuz.utils.Popup;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.DefaultListModel;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Jamuz {

	private static String appPath="";  //NOI18N
    private static String logPath="";  //NOI18N
    private static Options options;
    private static Machine machine;
    private static DbConnJaMuz db;
	private static HashMap <Integer, Playlist> playlists;

    public static boolean configure(String appPath) {
        Jamuz.appPath = appPath;
        logPath = appPath + "logs" + File.separator;  //NOI18N //NOI18N //NOI18N
        
        //Get database connection info, connect an init
        if(!connectDatabase()) {
            return false;
        }
        
        readGenres(); 
        readTags();

		if(!readPlaylists()) {
			return false;
		}
        
        //Get current machine
        if(!getCurrentMachine())
        {
            return false;
        }
        
        //Get options for current machine
        if(!getMachine().read()) {
            return false;
        }

        //Create LOG
        if(!createLog()) {
            return false;
        }
        logger.info("JaMuz started"); //NOI18N
        logger.log(Level.CONFIG, "Current folder: {0}", appPath); //NOI18N
        
        //Detect OS and check it is supported
        if(!OS.detect()) {
            return false;
        }
        logger.log(Level.CONFIG, "OS: {0}", OS.getName()); //NOI18N
        
        //Read properties file (this is for Video)
        options = new Options(appPath + "JaMuz.properties"); //TODO: Manage potential errors
        if(!options.read()) {
            return false;
        }
        
		//TODO: Test (not used a proxy for long and not since it is setup here)
		//TODO: Reload when options changes
		if(!setProxy()) {
			return false;
		}
		
        //Set logger on utils classes
        Popup.setLogger(logger);
        Ftp.setLogger(logger);

        //Log options
        logger.log(Level.CONFIG, "JaMuz database: {0}", getDb().getDbConn().info.locationOri); //NOI18N
		logConfig("location.library");
		logConfig("library.isMaster");
		logConfig("location.add");
		logConfig("location.ok");
		logConfig("location.mask");
		logConfig("location.ko");
		logConfig("location.manual");
		logConfig("network.proxy");
		logConfig("log.level");
		logConfig("log.limit");
		logConfig("log.count");
		logConfig("files.audio");
		logConfig("files.image");
		logConfig("files.convert");
		logConfig("files.delete");

        //Set library location (JaMuz's rootPath)
        db.setRootPath(getMachine().getOption("location.library"));  //NOI18N
        
        return true;
    }

	private static void logConfig(String id) {
		logger.log(Level.CONFIG, id+": {0}", getMachine().getOption(id)); //NOI18N
	}
	
	private static boolean setProxy() {
		String proxy = Jamuz.getMachine().getOption("network.proxy");  //NOI18N
		if(!proxy.startsWith("{")) { // For {Empty}  //NOI18N
			String[] split = proxy.split(":");  //NOI18N
			System.setProperty("http.proxyHost", split[0]);  //NOI18N
			System.setProperty("http.proxyPort", split[1]);  //NOI18N
		}
		return true;
	}
	
    private static boolean getCurrentMachine() {
		try {
			String currentMachine = InetAddress.getLocalHost().getHostName();
            machine = new Machine(currentMachine);
			return true;
		} catch (UnknownHostException ex) {
			Popup.error(ex);
			return false;
		}
	}
    
    private static boolean connectDatabase() {
        
        //This is default database location and name
		String JaMuzDbPath = "JaMuz.db";
		
		//Check XML config file presence (to override database location and name)
		String configFileName=appPath + "JaMuz.xml";  //NOI18N
        File f = new File(configFileName);
        if(f.exists()) {
			//Open application XML configuration file
			org.w3c.dom.Document docDatabase = openXmlFile(configFileName);
			if(docDatabase==null) {
				Popup.warning(Inter.get("Error.OpenXMLConfigFile")); //NOI18N
				return false;
			}

			//Check XML configuration file type and version
			String type = getXmlNodeValue(docDatabase, "header", "type");  //NOI18N
			String version = getXmlNodeValue(docDatabase, "header", "version");  //NOI18N
			if(!type.equals("jamuz") || !version.equals("1")) { //NOI18N
				Popup.warning(java.text.MessageFormat.format(Inter.get("Error.XMLConfigFileInvalid"), new Object[] {configFileName, type, version}));  //NOI18N
				return false;
			}

			//Get database path from XML configuration file
			JaMuzDbPath = getXmlNodeValue(docDatabase, "config", "database");  //NOI18N
		}

        //Check JaMuz JaMuzDbPath file presence
        f = new File(JaMuzDbPath);
        if(!f.exists()) {
            Popup.warning(java.text.MessageFormat.format(Inter.get("Error.JaMuzDbNotFound"), JaMuzDbPath));  //NOI18N
            return false;
        }
        
        //Create and open connection to JaMuz JaMuzDbPath			
        db= new DbConnJaMuz(new DbInfo(LibType.Sqlite, JaMuzDbPath, "", ""));

        return db.setUp();
    }
    
    private static boolean createLog() {
		try {
			//Create process LOG folder if it does not yet exists
			File f = new File(logPath);
			f.mkdir();
			//TODO: Return an error if something goes wrong
			//mkdir() return false if folder already exist an no Exception is thrown
			//Need to use f.canRead(); et f.canWrite();
			
			//Create Logger
			String logFilePath = logPath + "log-%g.html";  //NOI18N
			FileHandler fh=new FileHandler(logFilePath, Integer.parseInt(getMachine().getOption("log.limit")), Integer.parseInt(getMachine().getOption("log.count"))); //NOI18N
			fh.setFormatter(new LogFormat());
			logger.addHandler(fh);
			logger.setLevel(Level.parse(getMachine().getOption("log.level"))); //NOI18N
			
			return true;
		} catch (IOException ex) {
			Popup.error(Inter.get("Error.IOException")+" (create journal):\n"+ex.toString());  //NOI18N
			return false;
		}
	}
    
    public static File getFile(String filename, String... args) {
        String file=appPath;
        for (String subFolder : args) {
            file = FilenameUtils.concat(file, subFolder); //NOI18N
			//TODO: Create the folders if missing
        } 
        file = FilenameUtils.concat(file, filename); //NOI18N
        return new File(file); //NOI18N
    }

    /**
	 * Get Application LOG path. Used to backup database files also
     * @return 
	 */
    public static String getLogPath() {
        return logPath;
    }
	
	/**
	 * Get Application database connection.
     * @return 
	 */
    public static DbConnJaMuz getDb() {
		return db;
    }
    
    /**
	 * Get Application current machine's options.
     * @return 
	 */
    public static Machine getMachine() {
        return machine;
    }

    /**
	 * Get Application options.
     * Currently only video options.
     * @return 
	 */
    public static Options getOptions() {
        return options;
    }

	private static DefaultListModel genreListModel;
	
    public static void readGenres() {
        genreListModel = new DefaultListModel();
        getDb().getGenreListModel(genreListModel);
    }

	public static List<String> getGenres() {
        return (List<String>)(List<?>) Arrays.asList(genreListModel.toArray());
    }
	
	/**
	 *
	 * @return
	 */
	public static DefaultListModel getGenreListModel() {
        return genreListModel;
    }
    
    private static ArrayList<String> tags;
    private static void readTags() {
        tags = new ArrayList<>();
        getDb().getTags(tags);
    }

    public static ArrayList<String> getTags() {
        return tags;
    }

    /**
	 * Reads playlists from database
	 * @return
	 */
	public static boolean readPlaylists() {
		playlists = new HashMap<>();
		return getDb().getPlaylists(playlists);
	}
    
	/**
	 * Return playlists as a Collection
	 * @return
	 */
	public static List<Playlist> getPlaylists() {
        List list = new ArrayList(playlists.values());
        Collections.sort(list);
		return list;
	}
    
	public static List<Playlist> getPlaylistsVisible() {
		return playlists.values().stream().filter(playlist -> !playlist.isHidden()).sorted().collect(Collectors.toList());
	}
	
	/**
	 * Return requested playlist
	 * @param id
	 * @return
	 */
	public static Playlist getPlaylist(int id) {
		if(id>0) {
			return playlists.get(id);
		}
		else {
			return new Playlist(0, Inter.get("Playlist.FullLibrary"), false, 1, Playlist.LimitUnit.Gio, false, Playlist.Type.Songs, Playlist.Match.All, false); //NOI18N
		}
	}
    
    /**
	 * Get LOG instance.
	 */
	private static final Logger logger = Logger.getLogger("JaMuz");  //NOI18N

    public static Logger getLogger() {
        return logger;
    }
    
    //TODO: Move below functions to a dedicated class
	private static org.w3c.dom.Document openXmlFile(String filename) {
		try {
			File file = new File(filename);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
			org.w3c.dom.Document doc = documentBuilder.parse(file);
			doc.getDocumentElement().normalize();
			return doc;
		} catch (ParserConfigurationException | SAXException | IOException ex) {
			//Proper error handling. filename is displayed in ex, so no need to add it again
			Popup.error(ex); 
			return null;
        }
	}

	private static String getXmlNodeValue(org.w3c.dom.Document doc, String TagNameLev1, String TagNameLev2) {
		NodeList nodeLst = doc.getElementsByTagName(TagNameLev1);
		Node fstNode = nodeLst.item(0);

		Element myElement = (Element) fstNode;
		NodeList myElementList = myElement.getElementsByTagName(TagNameLev2);
		Element mySubElement = (Element) myElementList.item(0);
		NodeList mySubElementList = mySubElement.getChildNodes();
		return ((Node) mySubElementList.item(0)).getNodeValue();
	}
}