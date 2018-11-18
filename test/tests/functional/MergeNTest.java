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
import jamuz.Jamuz;
import jamuz.gui.PanelMain;
import jamuz.Playlist;
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
public class MergeNTest extends TestCase {

	//FIXME TEST !!!!! Refer to "TestPlan.ods"
	
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
			//FIXME TEST !!!!!!!!!!! Update ratingModifDate & tagsModifDate 
			// as done in Jamuz (check that)
			//and update ratings in 6cc35892-c44f-4aa7-bfee-5f63eca70821.ods 
			//accordingly
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
		
    }
	/**
	 *
	 * @param testMethodName
	 */
	public MergeNTest(String testMethodName) {
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
