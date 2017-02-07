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
package jamuz.tests;
import jamuz.AlbumBuffer;
import jamuz.gui.PanelMain;
import jamuz.process.check.ProcessCheck;
import jamuz.ProcessHelper;
import jamuz.Settings;
import jamuz.process.check.PanelCheck;
import java.io.File;
import junit.framework.TestCase;
import org.junit.Test;
import jamuz.utils.Inter;
import jamuz.utils.Swing;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Merge1Test extends TestCase {

    @Test
    public void test() throws Exception {
        Settings.startGUI("Label.Check"); //Mandatory
        
        //Create an album, 
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest1_Creation").create();
        
        //Scan library
        ProcessHelper.scanLibraryQuick();
        checkNumberScanned(1);
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest1_Creation").checkAfterScan(); //Note that there are no results as folder is not analyzed: expected for now
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest2_DB").checkDbAndFS(false);
        
        PanelMain.selectTab(Inter.get("Label.Merge"));
        ProcessHelper.merge();
        //TODO: Click OK button in merge results popup OR disable popup (after each merge)
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest3").checkJaMuz();
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest3").checkStatSource(1, false);
        
        //Change stats in stat source
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest4_1").setAndCheckStatsInStatSource(1, false); // Guayadeque
        //Change stats in JamuZ
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest4_JaMuz").setAndCheckStatsInJamuzDb();

        //Merge again and check merge ok
        ProcessHelper.merge();
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest4_New").checkJaMuz();
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest4_New").checkStatSource(1, false);
        
        assertTrue("Not valid test. Shall no pass yet !", false);

    }

    private void checkNumberScanned(int expected){
        assertEquals("number of checked folders", expected, PanelCheck.tableModelCheck.getFolders().size());
    }

    public Merge1Test(String testMethodName) {
        super(testMethodName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Settings.setupApplication();
        
        Settings.addStatSource(
            "guayadeque.db", 
            1, 
            Settings.getMusicFolder() + "Archive" + File.separator, 
            -1
        );
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
