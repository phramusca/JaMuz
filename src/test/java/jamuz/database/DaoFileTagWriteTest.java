/*
 * Copyright (C) 2023 phramusca <phramusca@gmail.com>
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
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;

/**
 * Tests sur {@link DaoFileTagWrite#update}.
 */
public class DaoFileTagWriteTest {

    private static DbConnJaMuz dbConnJaMuz;
    private static DaoFileTagWrite writer;
    private static int pathId;

    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        writer = new DaoFileTagWrite(dbConnJaMuz.getDbConn(), dbConnJaMuz.tag(), dbConnJaMuz.file());
        dbConnJaMuz.file().setLocationLibrary("/root/tagwrite/");
        int[] keyPath = new int[1];
        dbConnJaMuz.path().lock().insert("rel/tag/", new Date(), FolderInfo.CheckedFlag.UNCHECKED, "mbid", keyPath);
        pathId = keyPath[0];
    }

    @AfterClass
    public static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    private static FileInfoInt insertBareFile(String filename) throws SQLException {
        FileInfoInt f = new FileInfoInt("rel/tag/" + filename, "/root/tagwrite/");
        f.setIdPath(pathId);
        int[] key = new int[1];
        assertTrue(dbConnJaMuz.file().lock().insert(f, key));
        f.setIdFile(key[0]);
        f.setRating(0);
        return f;
    }

    private static int countTagFilesForFile(int idFile) throws SQLException {
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "SELECT COUNT(*) FROM tagFile WHERE idFile = ?")) {
            st.setInt(1, idFile);
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }
        }
    }

    @Test
    public void shouldPersistTagsWhenUpdate() throws SQLException {
        FileInfoInt f = insertBareFile("tagged.ext");
        ArrayList<String> tags = new ArrayList<>();
        tags.add("tagWriteAlpha");
        tags.add("tagWriteBravo");
        f.setTags(tags);

        ArrayList<FileInfoInt> files = new ArrayList<>();
        files.add(f);
        int[] results = new int[]{1};
        assertArrayEquals(results, writer.update(files, results));
        assertEquals(2, countTagFilesForFile(f.getIdFile()));
    }
}
