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
import jamuz.Jamuz;
import jamuz.gui.PanelMain;
import test.helpers.TestProcessHelper;
import test.helpers.Settings;
import java.io.File;
import junit.framework.TestCase;
import org.junit.Test;
import jamuz.utils.Inter;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Merge1Test extends TestCase {

	/**
	 *
	 * @throws Exception
	 */
	@Test
    public void test() throws Exception {
		//FIXME TEST Do the same with multi-stat sources
		//=> Yes, similar to MergeNTest 
		//BUT without check process and no device
		
        Settings.startGUI("Label.Check"); //Mandatory
 
        /***********************************************************
		 * Create an album
		***********************************************************/
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest1_Creation").create();
        
        /***********************************************************
		 * Scan library quick
		***********************************************************/
        TestProcessHelper.scanLibraryQuick();
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest2_DB").checkDbAndFS(false);
        
		/***********************************************************
		 * Merge
		***********************************************************/
        PanelMain.selectTab(Inter.get("Label.Merge"));
        TestProcessHelper.merge();
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest3").checkJaMuz();
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest3").checkStatSource(1, false, false);

		/***********************************************************
		 * Change stats in stat source & JamuZ
		***********************************************************/
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest4_1").setAndCheckStatsInStatSource(1, false); // Guayadeque 
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest4_JaMuz").setAndCheckStatsInJamuzDb();

		/***********************************************************
		 * Merge again and check merge ok
		***********************************************************/
        TestProcessHelper.merge();
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest4_New").checkJaMuz();
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest4_New").checkStatSource(1, false, false);
        
		/***********************************************************
		 * Change stats in stat source & JamuZ, playCounter especially
		***********************************************************/
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest5_1").setAndCheckStatsInStatSource(1, false); // Guayadeque 
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest5_JaMuz").setAndCheckStatsInJamuzDb();

		/***********************************************************
		 * Merge again and check merge ok
		***********************************************************/
        TestProcessHelper.merge();
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest5_New").checkJaMuz();
        AlbumBuffer.getAlbum("9e097b10-8160-491e-a310-e26e54a86a10", "MergeTest5_New").checkStatSource(1, false, false);

    }

	/**
	 *
	 * @param testMethodName
	 */
	public Merge1Test(String testMethodName) {
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
        
        Settings.addStatSource(
            "guayadeque.db", 
            1, 
            Settings.getMusicFolder() + "Archive" + File.separator, 
            -1
        );
		
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
