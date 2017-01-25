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

//FIXME: Ant (build.xml):  copy configuration and other required files, make 7z package, obfuscate:
//BINARY CODE OBFUSCATION (TO PROTECT API KEYS) AND MAKE SURE THERE IS NO API KEY NOR (worse) PASSWORDS IN CODE TO BE COMMITED

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
//		- TODO: Support more formats (FLAC at first, then OGG and maybe more)
//		- TODO: transitions
//		- TODO: Choose playlist or "Select" tab for auto-fill queue mode
//	- Merge tab:
//		- TODO: Support Clementine. It is sqlite, cross-platform, amarok1.4 fork, BUT :
//			- filename is stored as blob and URL encoded (with %20 as space at least) :(
//			- cannot find addedDate
//		- TODO: Support JaJUK: sounds promising (DJ, ambiances,...) but XML database :(
//		- TODO: Support rating=-1:
//			Guayadeque default rating is -1, but cannnot be set back to -1 in GUI (only from 0 to 5)
//			MediaMonkey default is -1 (not rated), 0 is used to mark a file as "to be deleted" (bomb icon)
//			XBMC: ??
//			AmaroK: ??
//			Mixxx: ??
//	- Check tab:
//		- TODO: Add an import feature, copying only if no duplicate found
//  - Synchro tab
//      - //TODO: Check for other devices (whole library) ==> keskidit ??
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
//		- FIXME: Options
//                  - Do not display current machine in machines list (cannot be deleted anyway) 
//                  + "Options" button to select current options (as in all other tabs)
//					+ New Options GUI
//                  + Use some common options (among machines) 
//                      Ex: files.* and location.mask options should be global (all machines), 
//                          not fully mandatory as only one machine can be master, but far better if master machine changes !!!
//	- Videos tab:
//		- TODO: Enable export over SSH (on remote machine to an device plugged on that remote machine : Intel NUC to friend USB key/HDD for instance)
//          => Doing this, merge options to copy/move files b/w "Sync" and "Video" (export)
//TODO: List used librairies (source, version, how to compile if needed,...)
import jamuz.gui.PanelMain;
import jamuz.gui.PanelSelect;
import jamuz.utils.Popup;
import java.io.File;
import java.util.logging.Handler;

/**
 * JaMuz main class
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Main {
    
	/**
	 * Main program.
	 * @param args
	 */
	public static void main(String[] args) {
		try {

            //Get current application folder
			File f = new File(".");  //NOI18N
			Jamuz.getLogger().finest(f.getAbsolutePath());
			String appPath = f.getAbsolutePath();
			appPath = appPath.substring(0, appPath.length() - 1);

            //Configure application
            if(!Jamuz.configure(appPath)) {
                System.exit(1);
            }			

			//Add a listnerer on application exit
			Runtime.getRuntime().addShutdownHook(new Thread("Thread.Main.main.addShutdownHook")
			{
				@Override
				public void run()
				{
					if(Jamuz.getLogger()!=null) {
						//Close all handlers, if not already closed
						for(Handler hd : Jamuz.getLogger().getHandlers()) {
							hd.close();
							hd.flush();
						}
					}
					PanelMain.stopMplayer();
					PanelSelect.stopMplayer();
				}
			});

			PanelMain.main(null);
		}
		catch(Exception ex) {
			Popup.error(ex);
			System.exit(99);
		}
    }   

}
