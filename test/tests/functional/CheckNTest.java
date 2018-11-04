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
import jamuz.process.check.FolderInfo;
import jamuz.process.check.PanelCheck;
import jamuz.process.check.ProcessCheck.Action;
import test.helpers.TestProcessHelper;
import test.helpers.Settings;
import java.util.ArrayList;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class CheckNTest extends TestCase {

	/**
	 *
	 * @throws Exception
	 */
	@Test
    public void test() throws Exception {
        
        Settings.startGUI("Label.Check"); //Mandatory

        //Create somes albums
        ArrayList<String> mbIds = new ArrayList<>();
        mbIds.add("9e097b10-8160-491e-a310-e26e54a86a10");
        //FIXME TEST Apply the above changes to the below
//        mbIds.add("9dc7fe6a-3fa4-4461-8975-ecb7218b39a3");
//        mbIds.add("c212b71b-848c-491c-8ae7-b62a993ae194");
//        mbIds.add("8cfbb741-bd63-449f-9e48-4d234264c8d5");
//        mbIds.add("be04bc1f-fc63-48f5-b1ca-2723f17d241d");
//        mbIds.add("6cc35892-c44f-4aa7-bfee-5f63eca70821");
//        mbIds.add("7598d527-bc8d-4282-a72c-874f335d05ac");
//        mbIds.add("13ca98f6-1a9f-4d76-a3b3-a72a16d91916");
        for(String mbId : mbIds) {
            AlbumBuffer.getAlbum(mbId, "CheckTest1_KO").create();
        }
        
        //Scan
        TestProcessHelper.scanNewFolder();
        checkNumberScanned(mbIds.size());
        for(String mbId : mbIds) {
            AlbumBuffer.getAlbum(mbId, "CheckTest1_KO").checkActionsTableModel();
            
            //Set genre, cover and SAVE action. Apply changes
            //Note that MusiBrainz album should have been retrieved
            FolderInfo folder = AlbumBuffer.getAlbum(mbId, "CheckTest1_KO")
					.getCheckedFolder();
            folder.setNewGenre("Reggae");
            folder.setNewImage(Settings.getTestCover());
            folder.action=Action.SAVE;
            PanelCheck.addToActionQueue(folder);
            TestProcessHelper.applyChanges();
        }

        //Scan again 
        TestProcessHelper.scanNewFolder();
        checkNumberScanned(mbIds.size());
        
        for(String mbId : mbIds) {
            AlbumBuffer.getAlbum(mbId, "CheckTest2_OK").checkActionsTableModel(); //MusicBrainz + Reggae + cover => OK
        }

        //OK should have been selected. Apply changes
        TestProcessHelper.applyChanges();
        //Verifying there is nothing left in new folder
        TestProcessHelper.scanNewFolder();
        checkNumberScanned(0);
        
        for(String mbId : mbIds) {
            AlbumBuffer.getAlbum(mbId, "CheckTest3_DbOk").checkDbAndFS(true); // In DB and OK
        }
        
        assertTrue("Not valid test. Shall no pass yet !", false);
        
    }

    private void checkNumberScanned(int expected){
        assertEquals("number of checked folders", expected, 
				PanelCheck.tableModelActionQueue.getFolders().size());
    }
    
	/**
	 *
	 * @param testMethodName
	 */
	public CheckNTest(String testMethodName) {
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
