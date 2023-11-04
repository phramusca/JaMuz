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
package jamuz;

//----------------------------------------------------------
// - General TODO:
//      - TODO: manage database backups in logs
//		- TODO: Other location for JaMuz Db:
//			- on a FTP server (should be easy, if not already possible)
//			- through a webservice ... maybe one day
//		- Add a "WHERE path LIKE '<rootPath>%' to prevent scanning/merging values that are not from the proper library
//			OR support multiple librairies :)
//TODO: Display duplicate files (artist/album)
// - Best way to display this ?
// - What will this be used for ?
//select t.*, albumArtist, album, idFile
//from file f
//join (
//    select artist, title, count(*) as qty
//    from file
//    group by artist, title
//    having count(*) > 1
//) t on f.artist = t.artist and f.title = t.title
// - Player:
//		- TODO: transitions
//	- Merge tab:
//		- TODO: Support rating=-1:
//			Guayadeque default rating is -1, but cannnot be set back to -1 in GUI (only from 0 to 5)
//			MediaMonkey default is -1 (not rated), 0 is used to mark a file as "to be deleted" (bomb icon)
//			Kodi: ??
//			AmaroK: ??
//			Mixxx: ??
//	- Check tab:
//		- TODO: Add an import feature, copying only if no duplicate found
//  - Export tab
//	- Select tab:
//		- TODO: Quick tag edition in JList (rating and genre) => not that usefull as can be done in player "tab"
//	- Playlist tab:
//		- TODO: Merge playlists among sources players
//      - TODO: Make all filters and merge filters with those in "Select" tab
//	- Lyrics tab:
//		- TODO: Search online
//	- Statistics tab:
//		- TODO: Review color choices
//	- Options tab:
//		- TODO: Options
//                  - Do not display current machine in machines list (cannot be deleted anyway) 
//                  + "Options" button to select current options (as in all other tabs)
//                  + Use some common options (among machines) 
//                      Ex: files.* and location.mask options should be global (all machines), 
//                          not fully mandatory as only one machine can be master, but far better if master machine changes !!!
//	- Videos tab:
//		- TODO: Enable export over SSH (on remote machine to an device plugged on that remote machine : Intel NUC to friend USB key/HDD for instance)
//          => Doing this, merge options to copy/move files b/w "Sync" and "Video" (export)
//TODO: List used librairies (source, version, how to compile if needed,...)

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jamuz.gui.PanelMain;
import jamuz.gui.PanelSelect;
import jamuz.utils.Popup;
import java.io.File;
import java.io.IOException;
import java.util.logging.Handler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

// TODO pom.xml essayer de remplacer les jar locaux par des maven

//FIXME Z https://github.com/phramusca/JaMuz/security/dependabot
//FIXME Z <https://docs.github.com/fr/actions/using-workflows/reusing-workflows>**

/**
 * JaMuz main class
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Main {

	/**
	 * Main program.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			// The path obtained might be URL-encoded, so you need to decode it
			try {
				jarPath = java.net.URLDecoder.decode(jarPath, "UTF-8");
			} catch (java.io.UnsupportedEncodingException ex) {
				Popup.error("get jar folder", ex);
			}
			File jarFile = new File(jarPath);
			File jarFolder = jarFile.getParentFile();
            String appPath = jarFolder.getAbsolutePath() + File.separator;
			System.out.println("Folder of the running JAR : " + appPath);
            
			//Get current application folder
			File f = new File(".");  //NOI18N
			Jamuz.getLogger().finest(f.getAbsolutePath());
			String dotPath = f.getAbsolutePath();
			dotPath = dotPath.substring(0, dotPath.length() - 1);
			System.out.println("Folder of . : " + dotPath);
            
			//Configure application
			if (!Jamuz.configure(appPath)) {
				System.exit(1);
			}

            //Add a listnerer on application exit
			Runtime.getRuntime().addShutdownHook(new Thread("Thread.Main.main.addShutdownHook") {
				@Override
				public void run() {
					if (Jamuz.getLogger() != null) {
						//Close all handlers, if not already closed
						for (Handler hd : Jamuz.getLogger().getHandlers()) {
							hd.close();
							hd.flush();
						}
					}
					PanelMain.stopMplayer();
					PanelSelect.stopMplayer();
                    
                    //TODO: Make this an option as server could be left running for dowloads ...
//                    Jamuz.getSlskdDocker().stop();
				}
			});

            String newVersionAssetName = checkNewVersion();
			//Start GUI
			PanelMain.main(newVersionAssetName);
            
		} catch (Exception ex) {
			Popup.error(ex);
			System.exit(99);
		}
	}
    
    private static String checkNewVersion() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.github.com/repos/phramusca/jamuz/releases/latest")
                    .build();
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            Gson gson = new Gson();
            JsonObject releaseData = gson.fromJson(responseBody, JsonObject.class);
            String latestVersion = releaseData.get("tag_name").getAsString();
            JsonArray assets = releaseData.getAsJsonArray("assets");
            if (!assets.isJsonNull() && assets.size() > 0) {
                String downloadURL = assets.get(0).getAsJsonObject().get("browser_download_url").getAsString();
                String assetName = assets.get(0).getAsJsonObject().get("name").getAsString();
                String version = Main.class.getPackage().getImplementationVersion();
                String currentVersion = "v"+version;
                currentVersion = "v0.5.61"; //FIXME ! Remove when done with tests
                File assetFile = Jamuz.getFile(assetName, "data", "system", "update");
                
                //FIXME ! Check versions first and log latestVersion
                if (!currentVersion.equals("vnull") 
                        && compareVersionStrings(latestVersion, currentVersion) > 0) {
                    if(!assetFile.exists() ) {
                        Request downloadRequest = new Request.Builder()
                            .url(downloadURL)
                            .build();
                        Response downloadResponse = client.newCall(downloadRequest).execute();
                        byte[] zipBytes = downloadResponse.body().bytes();
                        FileUtils.writeByteArrayToFile(assetFile, zipBytes);
                    }
                    return assetName;
                } else {
                    System.out.println("No updates available or already downloaded. Current version: " + currentVersion);
                }
            } else {
                System.out.println("No release assets found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    //FIXME TEST this one compareVersionStrings
    public static int compareVersionStrings(String version1, String version2) {
        String[] parts1 = version1.split("\\.");
        String[] parts2 = version2.split("\\.");

        int minLength = Math.min(parts1.length, parts2.length);

        for (int i = 0; i < minLength; i++) {
            int v1 = Integer.parseInt(parts1[i].replaceAll("\\D", ""));
            int v2 = Integer.parseInt(parts2[i].replaceAll("\\D", ""));

            if (v1 < v2) {
                return -1; // version1 is lower
            } else if (v1 > v2) {
                return 1; // version1 is higher
            }
        }

        // If all common parts are equal, the longer version is higher
        if (parts1.length < parts2.length) {
            return -1; // version1 is lower
        } else if (parts1.length > parts2.length) {
            return 1; // version1 is higher
        }

        return 0; // versions are equal
    }
}
