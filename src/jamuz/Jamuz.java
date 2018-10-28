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
import jamuz.utils.Ftp;
import jamuz.utils.Inter;
import jamuz.utils.OS;
import jamuz.utils.Popup;
import jamuz.utils.XML;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.DefaultListModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Jamuz {

	private static String appPath="";  //NOI18N
    private static String logPath="";  //NOI18N
    private static Options options;
	private static Keys keys;
    private static Machine machine;
    private static DbConnJaMuz db;
	private static HashMap <Integer, Playlist> playlists;

	/**
	 *
	 * @param appPath
	 * @return
	 */
	public static boolean configure(String appPath) {
        Jamuz.appPath = appPath;
        logPath = appPath + "logs" + File.separator;  //NOI18N //NOI18N //NOI18N

        if(!connectDatabase()) {
            return false;
        }
        readGenres(); 
        readTags();
		if(!readPlaylists()) {
			return false;
		}
        if(!getCurrentMachine())
        {
            return false;
        }
        if(!getMachine().read()) {
            return false;
        }
        if(!createLog()) {
            return false;
        }
        logger.info("JaMuz started"); //NOI18N
        logger.log(Level.CONFIG, "Current folder: {0}", appPath); //NOI18N

        if(!OS.detect()) {
            return false;
        }
        logger.log(Level.CONFIG, "OS: {0}", OS.getName()); //NOI18N

        String filename = appPath + "JaMuz.properties";
        if(new File(filename).exists()) {
			options = new Options(filename); 
			if(!options.read()) {
				return false;
			}
		} else {
			Popup.error("Missing JaMuz.properties file.");
            return false;
        }

		keys = new Keys("/jamuz/keys.properties");
        if(!keys.read()) {
			Popup.error("Missing keys.properties file from jar package.");
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
        db.setRootPath(getMachine().getOptionValue("location.library"));  //NOI18N
        
        return true;
    }

	private static void logConfig(String id) {
		logger.log(Level.CONFIG, id+": {0}", getMachine().getOptionValue(id)); //NOI18N
	}
	
	private static boolean setProxy() {
		String proxy = Jamuz.getMachine().getOptionValue("network.proxy");  //NOI18N
		if(!proxy.startsWith("{")) { // For {Empty}  //NOI18N
			String[] split = proxy.split(":");  //NOI18N
			System.setProperty("http.proxyHost", split[0]);  //NOI18N
			System.setProperty("http.proxyPort", split[1]);  //NOI18N
		}
		return true;
	}
	
	public static DefaultHttpClient getHttpClient() {
		DefaultHttpClient httpclient = null;
		if(machine!=null) {
			String proxy = machine.getOptionValue("network.proxy");  //NOI18N
			if(!proxy.startsWith("{")) { // For {Empty}  //NOI18N
				String[] split = proxy.split(":");  //NOI18N
				HttpHost httpHost = new HttpHost(split[0], Integer.parseInt(split[1]));
				httpclient = new DefaultHttpClient();
				httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, httpHost);
			}
		}
		return httpclient;
	}
	
	public static Proxy getProxy() {
		Proxy myProxy = null;
		if(machine!=null) {
			String proxy = machine.getOptionValue("network.proxy");  //NOI18N
			if(!proxy.startsWith("{")) { // For {Empty}  //NOI18N
				String[] split = proxy.split(":");  //NOI18N
				InetSocketAddress sa = new InetSocketAddress(split[0], Integer.parseInt(split[1]));
				myProxy = new Proxy(Proxy.Type.HTTP, sa);
			}
		}
		return myProxy;
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
		String JaMuzDbPath = appPath + "JaMuz.db";
		
		//Check XML config file presence (to override database location and name)
		String configFileName=appPath + "JaMuz.xml";  //NOI18N
        File f = new File(configFileName);
        if(f.exists()) {
			//Open application XML configuration file
			org.w3c.dom.Document docDatabase = XML.open(configFileName);
			if(docDatabase==null) {
				Popup.warning(Inter.get("Error.OpenXMLConfigFile")); //NOI18N
				return false;
			}

			//Check XML configuration file type and version
			String type = XML.getNodeValue(docDatabase, "header", "type");  //NOI18N
			String version = XML.getNodeValue(docDatabase, "header", "version");  //NOI18N
			if(!type.equals("jamuz") || !version.equals("1")) { //NOI18N
				Popup.warning(java.text.MessageFormat.format(Inter.get("Error.XMLConfigFileInvalid"), new Object[] {configFileName, type, version}));  //NOI18N
				return false;
			}

			//Get database path from XML configuration file
			JaMuzDbPath = XML.getNodeValue(docDatabase, "config", "database");  //NOI18N
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
			//mkdir() return false if folder already exist and no Exception is thrown
			//Need to use f.canRead(); et f.canWrite();
			
			//Create Logger
			String logFilePath = logPath + "log-%g.html";  //NOI18N
			FileHandler fh=new FileHandler(logFilePath, Integer.parseInt(getMachine().getOptionValue("log.limit")), Integer.parseInt(getMachine().getOptionValue("log.count"))); //NOI18N
			fh.setFormatter(new LogFormat());
			logger.addHandler(fh);
			logger.setLevel(Level.parse(getMachine().getOptionValue("log.level"))); //NOI18N
			
			return true;
		} catch (IOException ex) {
			Popup.error(Inter.get("Error.IOException")+" (create journal):\n"+ex.toString());  //NOI18N
			return false;
		}
	}
    
	/**
	 *
	 * @param filename
	 * @param args
	 * @return
	 */
	public static File getFile(String filename, String... args) {
        String file=appPath;
        for (String subFolder : args) {
            file = FilenameUtils.concat(file, subFolder); //NOI18N
			try {
				FileUtils.forceMkdir(new File(file));
			} catch (IOException ex) {
				Logger.getLogger(Jamuz.class.getName()).log(Level.SEVERE, null, ex);
			}
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

	/**
	 *
	 * @return
	 */
	public static Options getKeys() {
		return keys;
	}

	private static DefaultListModel genreListModel;
	
	/**
	 *
	 */
	public static void readGenres() {
        genreListModel = new DefaultListModel();
        getDb().getGenreListModel(genreListModel);
    }

	/**
	 *
	 * @return
	 */
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
	private static DefaultListModel tagsModel;
    public static void readTags() {
        tags = getDb().getTags();
		tagsModel = new DefaultListModel();
		for(String tag : tags) {
			tagsModel.addElement(tag);
		}
    }

	/**
	 *
	 * @return
	 */
	public static ArrayList<String> getTags() {
        return tags;
    }
	
	public static DefaultListModel getTagsModel() {
		return tagsModel;
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
    
	/**
	 *
	 * @return
	 */
	public static List<Playlist> getPlaylistsVisible() {
		return playlists.values().stream()
				.filter(playlist -> !playlist.isHidden())
				.sorted()
				.collect(Collectors.toList());
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

	/**
	 *
	 * @return
	 */
	public static Logger getLogger() {
        return logger;
    }
    
}