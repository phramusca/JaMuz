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

import jamuz.FileInfoInt;
import jamuz.process.check.FolderInfo;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import test.helpers.TestUnitSettings;

/**
 *
 * @author raph
 */
public class DaoFileTagTest {

    public DaoFileTagTest() {
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
    public void testFileTag() {

        System.out.println("testFileTag");

        //Given
        dbConnJaMuz.tag().lock().insert("frefrfe");
        dbConnJaMuz.tag().lock().insert("mpomoipm");
        dbConnJaMuz.tag().lock().insert("5165vge");
        dbConnJaMuz.tag().lock().insert("wqs");

        Map<FileInfoInt, List<String>> expectedTags = new HashMap<>();

        FileInfoInt file1 = insertNewFile(1);
        ArrayList<String> tags1 = new ArrayList<>();
        tags1.add("mpomoipm");
        file1.setTags(tags1);
        expectedTags.put(file1, tags1);

        FileInfoInt file2 = insertNewFile(2);
        ArrayList<String> tags2 = new ArrayList<>();
        tags2.add("mpomoipm");
        tags2.add("5165vge");
        file2.setTags(tags2);
        expectedTags.put(file2, tags2);

        FileInfoInt file3 = insertNewFile(3);
        ArrayList<String> tags3 = new ArrayList<>();
        tags3.add("mpomoipm");
        tags3.add("frefrfe");
        tags3.add("wqs");
        file3.setTags(tags3);
        expectedTags.put(file3, tags3);

        //When
        dbConnJaMuz.fileTag().lock().update(new ArrayList<>(expectedTags.keySet()), null);

        //Then
        checkTags(file1, expectedTags);
        checkTags(file2, expectedTags);
        checkTags(file3, expectedTags);

        //FIXME TEST Negative cases
        //FIXME TEST Check other constraints
    }

    private void checkTags(FileInfoInt file, Map<FileInfoInt, List<String>> expectedTags) {
        ArrayList<String> tags = new ArrayList<>();
        dbConnJaMuz.fileTag().get(tags, file.getIdFile());
        List<String> expected = expectedTags.get(file);
        //fileTag().get returns in ids order, and expected is not sorted neither
        Collections.sort(expected);
        Collections.sort(tags);
        assertArrayEquals(expected.toArray(), tags.toArray());
    }

    private FileInfoInt insertNewFile(int index) {
        int[] keyPath = new int[1];
        dbConnJaMuz.path().lock().insert("4file/insert" + index, new Date(), FolderInfo.CheckedFlag.UNCHECKED, "mmmbbbiiidd", keyPath);
        FileInfoInt fileInfoInt = new FileInfoInt("file/insert" + index, "/root/file/insert" + index);
        fileInfoInt.setIdPath(keyPath[0]);
        int[] keyFile = new int[1];
        dbConnJaMuz.file().lock().insert(fileInfoInt, keyFile);
        fileInfoInt.setIdFile(keyFile[0]);
        return fileInfoInt;
    }

    private void setTags(ArrayList<String> tags) {
//        fileInfoInt.setTags(tags);
    }

    /**
     * Test of lock method, of class DaoFileTag.
     */
    @Test
    @Ignore
    public void testLock() {
        System.out.println("lock");
        DaoFileTag instance = null;
        DaoFileTagWrite expResult = null;
        DaoFileTagWrite result = instance.lock();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get method, of class DaoFileTag.
     */
    @Test
    @Ignore
    public void testGet() {
        System.out.println("get");
        ArrayList<String> tags = null;
        int idFile = 0;
        DaoFileTag instance = null;
        boolean expResult = false;
        boolean result = instance.get(tags, idFile);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
