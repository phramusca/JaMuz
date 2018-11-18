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
package tests.functional;
import test.helpers.AlbumBuffer;
import jamuz.process.sync.Device;
import jamuz.FileInfo;
import jamuz.process.check.FolderInfo;
import jamuz.Jamuz;
import jamuz.process.check.PanelCheck;
import jamuz.gui.PanelMain;
import jamuz.Playlist;
import jamuz.process.check.ProcessCheck.Action;
import test.helpers.TestProcessHelper;
import test.helpers.Settings;
import static test.helpers.Settings.getMusicFolder;
import jamuz.process.merge.StatSource;
import java.io.File;
import java.util.ArrayList;
import org.junit.Assert;
import junit.framework.TestCase;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import jamuz.utils.Inter;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class MergeCheckNTest extends TestCase {

	/**
	 *
	 * @throws Exception
	 */
	@Test
    public void test() throws Exception {
        
        Settings.startGUI("Label.Check"); //Mandatory
        
		/***********************************************************
		 * Create 8 Album audio files from album definition files
		***********************************************************/
        ArrayList<String> mbIds = new ArrayList<>();
        mbIds.add("9e097b10-8160-491e-a310-e26e54a86a10");
        mbIds.add("9dc7fe6a-3fa4-4461-8975-ecb7218b39a3");
        mbIds.add("c212b71b-848c-491c-8ae7-b62a993ae194");
        mbIds.add("8cfbb741-bd63-449f-9e48-4d234264c8d5");
        mbIds.add("be04bc1f-fc63-48f5-b1ca-2723f17d241d");
        mbIds.add("6cc35892-c44f-4aa7-bfee-5f63eca70821");
        mbIds.add("7598d527-bc8d-4282-a72c-874f335d05ac");
        mbIds.add("13ca98f6-1a9f-4d76-a3b3-a72a16d91916");
        for(String mbId : mbIds) {
            AlbumBuffer.getAlbum(mbId, "MergeDevice1_KO").create();
        }

		/***********************************************************
		 * Scan library quick to insert files in JaMuz
		 * Check Db and FS
		***********************************************************/
        TestProcessHelper.scanLibraryQuick();
        for(String mbId : mbIds) {
			//MergeDevice2_DB:
			// - Update rating -1 -> 0
			//=> Check database and filesystem
            AlbumBuffer.getAlbum(mbId, "MergeDevice2_DB").checkDbAndFS(false);
        }

		/***********************************************************
		 * Update some rating 0 -> 4 in JaMuz & check
		 * -> to have enough (but not too many) files 
		 * in test playlist to export to device MyTunes (Android)
		***********************************************************/
        for(String mbId : mbIds) {
			//MergeDevice3_JaMuz:
			// - Update rating 0 -> 4 for some files
			//=> Set and check stats in JaMuz
            AlbumBuffer.getAlbum(mbId, "MergeDevice3_JaMuz").setAndCheckStatsInJamuzDb();
        }

		/***********************************************************
		 * Sync to MyTunes (Android) and Check FS on device
		***********************************************************/
        PanelMain.selectTab(Inter.get("PanelMain.panelSync.TabConstraints.tabTitle"));
        TestProcessHelper.sync(); 
        for(StatSource statSource : Jamuz.getMachine().getStatSources()) {
            Device device = statSource.getDevice();
            Playlist playlist = device.getPlaylist();
            if(playlist.getId()>0) {
                for(String mbId : mbIds) {
                    AlbumBuffer.getAlbum(mbId, "MergeDevice3_JaMuz").checkFSdevice(device, false);
                }
            }
        }

		/***********************************************************
		 * 1st Merge & check
		 * Then change some stats in stat sources & JaMuz (+check)
		***********************************************************/
        PanelMain.selectTab(Inter.get("Label.Merge"));
        TestProcessHelper.merge();
        for(String mbId : mbIds) {
			//MergeDevice4_1stMerge:
			// - Change addedDate from "01/01/1970 00:00:00" to "05/05/2012 19:27:24"
			//=> Set and check stats in JaMuz
            AlbumBuffer.getAlbum(mbId, "MergeDevice4_1stMerge").checkJaMuz();
			
            for(StatSource statSource : Jamuz.getMachine().getStatSources()) {
                Device device = statSource.getDevice();
                Playlist playlist = device.getPlaylist(); 
                //Check merge
                AlbumBuffer
						.getAlbum(mbId, "MergeDevice4_1stMerge")
						.checkStatSource(
								statSource.getId(), 
								playlist.getId()>0, 
								false);
                //Change stats in stat source
                AlbumBuffer.getAlbum(mbId, 
						"MergeDevice5_"+statSource.getIdStatement())
							.setAndCheckStatsInStatSource(
									statSource.getId(), 
									playlist.getId()>0);
            }
            //Change stats in JamuZ
            AlbumBuffer.getAlbum(mbId, "MergeDevice5_JaMuz").setAndCheckStatsInJamuzDb();
        }

		/***********************************************************
		 * 2nd Merge & check
		***********************************************************/
        //Merge again and check merge ok
        TestProcessHelper.merge();
        for(String mbId : mbIds) {
            AlbumBuffer.getAlbum(mbId, "MergeDevice6_New").checkJaMuz();
            for(StatSource statSource : Jamuz.getMachine().getStatSources()) {
                Device device = statSource.getDevice();
                Playlist playlist = device.getPlaylist(); 
                AlbumBuffer.getAlbum(mbId, "MergeDevice6_New")
						.checkStatSource(
								statSource.getId(), 
								playlist.getId()>0, 
								false);
            }
        }
        
		//FIXME TEST MergeCheckNTest Mock it and test "check" process in another test class
		// to avoids issues, especially with MB/Mast.fm (resulting in merge differences) 
		/***********************************************************
		 * Check library in order to modify path and filenames
		 * Set genre, cover and SAVE action
		 * Apply changes
		***********************************************************/
        PanelMain.selectTab(Inter.get("Label.Check"));
        TestProcessHelper.checkLibrary();
        checkNumberScanned(mbIds.size());
        for(String mbId : mbIds) {
			//MergeDevice7_KO:
			// - Rating -1 et addedDate 01/01/1970 00:00:00
			//		AS statistics are not read during check process
            AlbumBuffer.getAlbum(mbId, "MergeDevice7_KO").checkActionsTableModel();
            //Set genre, cover and SAVE action. Apply changes
            //Note that MusiBrainz album should have been retrieved
            FolderInfo folder = AlbumBuffer.getAlbum(mbId, "MergeDevice7_KO").getCheckedFolder();
            folder.setNewGenre("Reggae");
            folder.setNewImage(Settings.getTestCover());
            folder.action=Action.SAVE;
            PanelCheck.addToActionQueue(folder);
        }
		TestProcessHelper.applyChanges();
		
		/***********************************************************
		 * Check library again and check changes have been applied
		 * Then apply changes (OK selected)
		***********************************************************/
        TestProcessHelper.checkLibrary();
        checkNumberScanned(mbIds.size());
        for(String mbId : mbIds) {
			//MergeDevice8_OK:
			// - MusicBrainz + Reggae + cover => OK
            AlbumBuffer.getAlbum(mbId, "MergeDevice8_OK").checkActionsTableModel(); 
        }
		//OK should have been selected. Apply changes
        TestProcessHelper.applyChanges();
		
		/***********************************************************
		 * Check libray again 
		 * and check there is nothing left unchecked
		***********************************************************/
        TestProcessHelper.checkLibrary();
        checkNumberScanned(0);
        for(String mbId : mbIds) {
            AlbumBuffer.getAlbum(mbId, "MergeDevice9_DbOk").checkDbAndFS(true); // In DB and OK
        }

		/***********************************************************
		 * 3rd Merge: stat sources are NOT upated:
		 * -> "not found files" issues after merge
		 * -> Except MyTunes (Android) as linked to a device 
		 *         so original path and filename is known
		 * THEN change some stats in JaMuz and MyTunes (Android)
		***********************************************************/
        PanelMain.selectTab(Inter.get("Label.Merge"));
        TestProcessHelper.merge();
        int nbErrorsExpected=0;
        for(String mbId : mbIds) {
            nbErrorsExpected+=AlbumBuffer.getAlbum(mbId, "MergeDevice9_DbOk").getNbTracks();
        }
		
		int nbSourcesWithoutErrors=1; // MyTunes only for now. Update nbNoErrors accordingly if changed
		int nbErrorsTotal=nbErrorsExpected*((Jamuz.getMachine().getStatSources().size()*2)-1)*2; //-1 because one source does only one round
		int nbNoErrors=nbErrorsExpected*nbSourcesWithoutErrors*2*2; //Unless it is the middle one done only one way ...
		nbErrorsExpected=nbErrorsTotal-nbNoErrors; 
        //  *2:         source vs jamuz and reverse side
        //  *2:         1st and second run
        ArrayList<FileInfo> errorList = TestProcessHelper.processMerge.getErrorList();
        assertEquals("Nb errors", nbErrorsExpected, errorList.size());
        //None expected completed (all missing on both sides, and MyTunes has no changes)
        ArrayList<FileInfo> completedList = TestProcessHelper.processMerge.getCompletedList();
        assertEquals("Nb completed", 0, completedList.size());
        //No change expected, so using the same album version finally
        for(String mbId : mbIds) {
            AlbumBuffer.getAlbum(mbId, "MergeDevice9_DbOk").checkJaMuz();            
            for(StatSource statSource : Jamuz.getMachine().getStatSources()) {
                Device device = statSource.getDevice();
                Playlist playlist = device.getPlaylist(); 
                AlbumBuffer.getAlbum(mbId, "MergeDevice9_DbOk").checkStatSource(statSource.getId(), playlist.getId()>0, false);
                //Change stats in stat source for MyTunes only
                if(playlist.getId()>0) {
                    //TODO TEST: Modify more albums, not only one as for now
					
					//MergeDevice10_xxx:
					// - Change some stats
					//=> Set and check stats in JaMuz & stat sources
                    AlbumBuffer.getAlbum(mbId, "MergeDevice10_"+statSource.getIdStatement()).setAndCheckStatsInStatSource(statSource.getId(), playlist.getId()>0);
                }
            }
            //Change stats in JamuZ
			
			//FIXME TEST MergeCheckNTest Continue here: 
			//Somehow genre for 1st track in "9e0"/"10_JaMuz" is empty (expected is 
			//	"Reggae", as it IS at this stage in JaMuz.db)
			
            AlbumBuffer.getAlbum(mbId, "MergeDevice10_JaMuz").setAndCheckStatsInJamuzDb();
        }
		
		/***********************************************************
		 * 4th Merge: Check only MyTunes is merged and properly
		***********************************************************/
        //Merge again
        PanelMain.selectTab(Inter.get("Label.Merge"));
        TestProcessHelper.merge();
        assertEquals("Nb errors", nbErrorsExpected, TestProcessHelper.processMerge.getErrorList().size());
        for(String mbId : mbIds) {
            AlbumBuffer.getAlbum(mbId, "MergeDevice10_New").checkJaMuz();            
            for(StatSource statSource : Jamuz.getMachine().getStatSources()) {
                Device device = statSource.getDevice();
                Playlist playlist = device.getPlaylist(); 
                if(playlist.getId()>0) {
                    //MyTunes has changed, so checking new values
                    AlbumBuffer.getAlbum(mbId, "MergeDevice10_New").checkStatSource(statSource.getId(), playlist.getId()>0, false);
                }
                else {
                    //Other stat sources are not changed. Check no updates.
                    AlbumBuffer.getAlbum(mbId, "MergeDevice9_DbOk").checkStatSource(statSource.getId(), playlist.getId()>0, false);
                }
            }
        }

		/***********************************************************
		 * 5th Merge: NOW stat sources HAVE BEEN upated:
		***********************************************************/
		//Replace stat source databases with the updated ones
		//(Manually made using real application, from previous state)
		Settings.copyStatSourceDatabase("guayadeque_Device_Updated.db", "guayadeque_Device.db");
		Settings.copyStatSourceDatabase("mixxxdb_Device_Updated.sqlite", "mixxxdb_Device.sqlite");
		Settings.copyStatSourceDatabase("MyMusic60_Device_Updated.db", "MyMusic32_Device.db");
		//Before merge, stats are as follows:
		//JaMuz & MyTunes:		MergeDevice10_New
		//Other stat sources:	MergeDevice9_DbOk
        TestProcessHelper.merge();
        for(String mbId : mbIds) {
            AlbumBuffer.getAlbum(mbId, "MergeDevice11_Sync").checkJaMuz();
            
            for(StatSource statSource : Jamuz.getMachine().getStatSources()) {
                Device device = statSource.getDevice();
                Playlist playlist = device.getPlaylist(); 
                
                AlbumBuffer.getAlbum(mbId, "MergeDevice11_Sync").checkStatSource(statSource.getId(), 
						playlist.getId()>0, !(playlist.getId()>0));
				//Note: last parameter means that paths have been renamed for all 
				//BUT MyTunes which has not been synced 
				//since checked and inserted OK in JaMuz
            }
        }
 
		/***********************************************************
		 * Sync again MyTunes and check device FS
		***********************************************************/
        //Sync and Check sync
        PanelMain.selectTab(Inter.get("PanelMain.panelSync.TabConstraints.tabTitle"));
        TestProcessHelper.sync(); 
        for(StatSource statSource : Jamuz.getMachine().getStatSources()) {
            Device device = statSource.getDevice();
            Playlist playlist = device.getPlaylist();
            if(playlist.getId()>0) {
                for(String mbId : mbIds) {
                    AlbumBuffer.getAlbum(mbId, "MergeDevice11_Sync2").checkFSdevice(device,  true);
                }
            }
        }
        Settings.copyStatSourceDatabase("MusicIndexDatabase_Device_Updated.db", "MusicIndexDatabase_Device.db");

        /***********************************************************
		 * Merge again and check merge is OK for all sources
		***********************************************************/
		TestProcessHelper.merge();
        for(String mbId : mbIds) {
            AlbumBuffer.getAlbum(mbId, "MergeDevice11_Sync2").checkJaMuz();
            
            for(StatSource statSource : Jamuz.getMachine().getStatSources()) {
                Device device = statSource.getDevice();
                Playlist playlist = device.getPlaylist(); 
                
                AlbumBuffer.getAlbum(mbId, "MergeDevice11_Sync2").checkStatSource(statSource.getId(), 
						playlist.getId()>0, true);
				//Note: last parameter means that ALL paths have been renamed for all 
				//includng MyTunes this time
            }
        }
		
    }

    private void checkNumberScanned(int expected){
       assertEquals("number of checked folders", expected, PanelCheck.tableModelActionQueue.getFolders().size());
    }

	/**
	 *
	 * @param testMethodName
	 */
	public MergeCheckNTest(String testMethodName) {
        super(testMethodName);
    }
    
	/**
	 *
	 * @throws Exception
	 */
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        Settings.setupApplication();
        
        //Create test playlist (genre=reggae) for merge test with a device
        Playlist playlist = new Playlist(0, "TestDevicePlaylist", false, 1, Playlist.LimitUnit.Gio, false,
            Playlist.Type.Songs, Playlist.Match.All, false);
        Assert.assertTrue("playlist creation", playlist.insert());
        
        //Getting playlist from library (so that id is set , so we can add filter)
        //Read created options
        Jamuz.readPlaylists();
        playlist = Jamuz.getPlaylist(1);
        
        Playlist.Filter filter = new Playlist.Filter(-1, Playlist.Field.RATING, Playlist.Operator.IS, "4"); //NOI18N
        playlist.addFilter(filter); 
        playlist.update();
        
        //Create test device
        //FIXME TEST create a device for each stat source
        Device device = new Device(-1, 
                "TestDevice", 
                FilenameUtils.normalizeNoEndSeparator(getMusicFolder() + "Archive")+File.separator, 
                FilenameUtils.normalizeNoEndSeparator(getMusicFolder() + "TestDevice")+File.separator, 
                1, //playlist.getId()is not set (only when retrieved from db)
                Jamuz.getMachine().getName(), false
        );
        assertTrue("Device creation", Jamuz.getDb().setDevice(device));
        
        //Set stat sources
        String rootPath; int idDevice; String name;
        int idStatement;
//        case 1: // Guayadeque 	(Linux)
//        case 2: // Kodi 	(Linux/Windows)
//        case 3: // MediaMonkey (Windows)
//        case 4: // Mixxx 	(Linux/Windows)
//        case 5: // MyTunes 	(Android)
        Settings.addStatSource(
            name = "guayadeque_Device.db", 
            idStatement=1, 
            rootPath=Settings.getMusicFolder() + "Archive" + File.separator, 
            idDevice = -1
        );
        Settings.addStatSource(
				//FIXME TEST WINDOWS Test on Windows, on a SSH box and a FTP box
                name = "MyMusic32_Device.db", //kodi
                idStatement=2, 
                rootPath=Settings.getMusicFolder() + "Archive" + File.separator, 
                idDevice = -1);
        //FIXME TEST WINDOWS Enable this when I have a Windows PC available
//        Settings.addStatSource(
//                name = "MediaMonkey source", 
//                idStatement=3, 
//                rootPath=Settings.getMusicFolder() + "Archive" + File.separator, 
//                idDevice = -1);
        Settings.addStatSource(
				//FIXME TEST WINDOWS Test on windows
                name = "mixxxdb_Device.sqlite", 
                idStatement=4, 
                rootPath=Settings.getMusicFolder() + "Archive" + File.separator, 
                idDevice = -1);
        Settings.addStatSource(
                name = "MusicIndexDatabase_Device.db", //MyTunes (could be removed ...) 
                idStatement=5, 
                rootPath="/storage/extSdCard/Musique/",
                idDevice = 1); //This one has a linked device
        
        //Read created options
        Jamuz.getMachine().read();
    }

	/**
	 *
	 * @throws Exception
	 */
	@Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
