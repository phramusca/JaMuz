/*
 * Copyright (C) 2015 raph
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
package jamuz.tests;
import jamuz.Album;
import jamuz.AlbumBuffer;
import jamuz.process.check.FolderInfo;
import jamuz.process.check.PanelCheck;
import jamuz.process.check.ProcessCheck;
import jamuz.process.check.ProcessCheck.Action;
import jamuz.ProcessHelper;
import jamuz.Settings;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author raph
 */
public class Check1Test extends TestCase {
   
    @Test
    public void test() throws Exception {
        //Should use NTest instead. This one is not up to date
        return;
        
//        Settings.startGUI("Label.Check"); //Mandatory
//        
//        //Create an album, 
//        //TODO: BPM does not seem to be saved: compare with banshee (and other tools)
//        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "CheckTest1_KO").create(); //Modified version with errors (compared to MusicBrainz)
//        
//        //Scan
//        ProcessHelper.scanNewFolder();
//        checkNumberScanned(1);
//        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "CheckTest1_KO").checkAfterScan();
//        
//        //Set genre, cover and SAVE action. Apply changes
//        //Note that MusiBrainz album should have been retrieved
//        FolderInfo folder = AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "CheckTest1_KO").getCheckedFolder();
//        folder.setNewGenre("Reggae");
//        folder.setNewImage(Settings.getTestCover());
//        folder.action=Action.SAVE;
//        PanelCheck.addToActionQueue(folder);
//        ProcessHelper.applyChanges();
//        
//        //Scan again 
//        ProcessHelper.scanNewFolder();
//        checkNumberScanned(1);
//        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "CheckTest2_OK").checkAfterScan(); //MusicBrainz + Reggae + cover => OK
//        
//        //OK should have been selected. Apply changes
//        ProcessHelper.applyChanges();
//        ProcessHelper.scanNewFolder();
//        checkNumberScanned(0);
//        
//        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "CheckTest3_DbOk").checkDbAndFS(true); // In DB and OK
//        
//        assertTrue("Not valid test. Shall no pass yet !", false);
    }

    private void checkNumberScanned(int expected){
        //TODO: Fix when check process change over
//        assertEquals("number of checked folders", expected, ProcessCheck.tableModelCheck.getFolders().size());
    }
    
    public Check1Test(String testMethodName) {
        super(testMethodName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Settings.setupApplication();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
