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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author raph
 */
public class DaoGenreWriteTest {
    
    public DaoGenreWriteTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of insert method, of class DaoGenreWrite.
     */
    @Test
    @Ignore // Refer to DaoGenTest
    public void testInsert() {
        System.out.println("insert");
        String genre = "";
        DaoGenreWrite instance = null;
        boolean expResult = false;
        boolean result = instance.insert(genre);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class DaoGenreWrite.
     */
    @Test
    @Ignore // Refer to DaoGenTest
    public void testUpdate() {
        System.out.println("update");
        String oldGenre = "";
        String newGenre = "";
        DaoGenreWrite instance = null;
        boolean expResult = false;
        boolean result = instance.update(oldGenre, newGenre);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class DaoGenreWrite.
     */
    @Test
    @Ignore // Refer to DaoGenTest
    public void testDelete() {
        System.out.println("delete");
        String genre = "";
        DaoGenreWrite instance = null;
        boolean expResult = false;
        boolean result = instance.delete(genre);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
