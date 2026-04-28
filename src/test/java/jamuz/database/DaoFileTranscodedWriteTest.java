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
 * Tests sur {@link DaoFileTranscodedWrite#insertOrUpdate}.
 */
public class DaoFileTranscodedWriteTest {

    private static DbConnJaMuz dbConnJaMuz;
    private static DaoFileTranscodedWrite writer;
    private static final String ROOT = "/root/tr/";
    private static int pathId;

    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        writer = new DaoFileTranscodedWrite(dbConnJaMuz.getDbConn());
        dbConnJaMuz.file().setLocationLibrary(ROOT);
        int[] keyPath = new int[1];
        dbConnJaMuz.path().lock().insert("rel/tr/", new Date(), FolderInfo.CheckedFlag.UNCHECKED, "mbid", keyPath);
        pathId = keyPath[0];
    }

    @AfterClass
    public static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    private static FileInfoInt insertSourceFile(String name) throws SQLException {
        FileInfoInt f = new FileInfoInt("rel/tr/" + name, ROOT);
        f.setIdPath(pathId);
        int[] key = new int[1];
        assertTrue(dbConnJaMuz.file().lock().insert(f, key));
        f.setIdFile(key[0]);
        f.setRating(0);
        return f;
    }

    private static String transcodedModifDate(int idFile, String ext) throws SQLException {
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "SELECT modifDate FROM fileTranscoded WHERE idFile = ? AND ext = ?")) {
            st.setInt(1, idFile);
            st.setString(2, ext);
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                return rs.getString("modifDate");
            }
        }
    }

    private static ArrayList<FileInfoInt> singleton(FileInfoInt f) {
        ArrayList<FileInfoInt> batch = new ArrayList<>();
        batch.add(f);
        return batch;
    }

    @Test
    public void shouldInsertThenUpsertFileTranscodedRow() throws SQLException {
        FileInfoInt base = insertSourceFile("src.ext");
        FileInfoInt row = dbConnJaMuz.file().getFile(base.getIdFile(), ROOT);
        row.setExt("opus");

        writer.insertOrUpdate(singleton(row));
        String firstModif = transcodedModifDate(row.getIdFile(), "opus");
        assertFalse(firstModif.isEmpty());

        Date far = new Date(200_000_000_000L);
        assertTrue(dbConnJaMuz.file().lock().updateModifDate(row.getIdFile(), far, row.getFilename()));
        FileInfoInt row2 = dbConnJaMuz.file().getFile(row.getIdFile(), ROOT);
        row2.setExt("opus");
        writer.insertOrUpdate(singleton(row2));

        String secondModif = transcodedModifDate(row.getIdFile(), "opus");
        assertFalse(firstModif.equals(secondModif));
    }
}
