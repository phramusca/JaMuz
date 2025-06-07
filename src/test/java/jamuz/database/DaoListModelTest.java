/*
 * Copyright (C) 2023 raph
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
package jamuz.database;

import javax.swing.DefaultListModel;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;
import java.io.IOException;
import java.sql.SQLException;


/**
 *
 * @author raph
 */
public class DaoListModelTest {
    
    public DaoListModelTest() {
    }
    
   private static DbConnJaMuz dbConnJaMuz;
    
    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
    }

    @AfterClass
    public static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
	public void testxxxxxxxxxxxxxx() {

		System.out.println("testxxxxxxxxxxxxxx");

        //FIXME TEST Make unit test
		//FIXME TEST Negative cases
		//FIXME TEST Check other constraints
	}

    /**
     * Test of setLocationLibrary method, of class DaoListModel.
     */
    @Test
    public void testSetLocationLibrary() {
        System.out.println("setLocationLibrary");
        String locationLibrary = "";
        DaoListModel instance = null;
        instance.setLocationLibrary(locationLibrary);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getGenreListModel method, of class DaoListModel.
     */
    @Test
    public void testGetGenreListModel() {
        System.out.println("getGenreListModel");
        DefaultListModel myListModel = null;
        DaoListModel instance = null;
        instance.getGenreListModel(myListModel);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTagListModel method, of class DaoListModel.
     */
    @Test
    public void testGetTagListModel() {
        System.out.println("getTagListModel");
        DefaultListModel myListModel = null;
        DaoListModel instance = null;
        instance.getTagListModel(myListModel);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMachineListModel method, of class DaoListModel.
     */
    @Test
    public void testGetMachineListModel() {
        System.out.println("getMachineListModel");
        DefaultListModel myListModel = null;
        DaoListModel instance = null;
        instance.getMachineListModel(myListModel);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fillSelectorList method, of class DaoListModel.
     */
    @Test
    public void testFillSelectorList() {
        System.out.println("fillSelectorList");
        DefaultListModel myListModel = null;
        String field = "";
        String selGenre = "";
        String selArtist = "";
        String selAlbum = "";
        boolean[] selRatings = null;
        boolean[] selCheckedFlag = null;
        int yearFrom = 0;
        int yearTo = 0;
        float bpmFrom = 0.0F;
        float bpmTo = 0.0F;
        int copyRight = 0;
        String sqlOrder = "";
        DaoListModel instance = null;
        instance.fillSelectorList(myListModel, field, selGenre, selArtist, selAlbum, selRatings, selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo, copyRight, sqlOrder);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
