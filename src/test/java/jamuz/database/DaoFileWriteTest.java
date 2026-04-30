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

import jamuz.FileInfo;
import jamuz.FileInfoInt;
import jamuz.process.check.FolderInfo;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoFileWrite}. */
class DaoFileWriteTest {

    private static DbConnJaMuz dbConnJaMuz;
    private static DaoFileWrite writer;
    private static final String ROOT = "/root/fw/";
    private static int pathId;

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        writer = dbConnJaMuz.file().lock();
        dbConnJaMuz.file().setLocationLibrary(ROOT);
        int[] keyPath = new int[1];
        dbConnJaMuz.path().lock().insert("rel/fw/", new Date(), FolderInfo.CheckedFlag.UNCHECKED, "mbid", keyPath);
        pathId = keyPath[0];
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @BeforeEach
    void wipeFilesOnPath() throws SQLException {
        try (Statement st = dbConnJaMuz.getDbConn().getConnection().createStatement()) {
            st.executeUpdate("DELETE FROM tagFile WHERE idFile IN (SELECT idFile FROM file WHERE idPath = " + pathId + ")");
            st.executeUpdate("DELETE FROM fileTranscoded WHERE idFile IN (SELECT idFile FROM file WHERE idPath = " + pathId + ")");
            st.executeUpdate("DELETE FROM file WHERE idPath = " + pathId);
        }
    }

    private FileInfoInt insertSampleFile(String filename) throws SQLException {
        FileInfoInt f = new FileInfoInt("rel/fw/" + filename, ROOT);
        f.setIdPath(pathId);
        int[] key = new int[1];
        assertTrue(writer.insert(f, key));
        f.setIdFile(key[0]);
        f.setRating(0);
        return f;
    }

    private int intScalar(String sql, int... params) throws SQLException {
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                st.setInt(i + 1, params[i]);
            }
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }
        }
    }

    private void linkTagToFile(int idFile, String tagValue) throws SQLException {
        assertTrue(dbConnJaMuz.tag().lock().insertIfMissing(tagValue));
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "INSERT INTO tagFile (idFile, idTag) SELECT ?, id FROM tag WHERE value = ?")) {
            st.setInt(1, idFile);
            st.setString(2, tagValue);
            st.executeUpdate();
        }
    }

    @Test
    void shouldInsertThenDeleteFile() throws SQLException {
        FileInfoInt f = insertSampleFile("a.ext");
        assertTrue(writer.delete(f.getIdFile()));
        assertFalse(writer.delete(f.getIdFile()));
    }

    @Test
    void shouldSetSavedFlag() throws SQLException {
        FileInfoInt f = insertSampleFile("saved.ext");
        assertEquals(0, intScalar("SELECT saved FROM file WHERE idFile = ?", f.getIdFile()));
        assertTrue(writer.setSaved(f.getIdFile()));
        assertEquals(1, intScalar("SELECT saved FROM file WHERE idFile = ?", f.getIdFile()));
    }

    @Test
    void shouldUpdateMetadataFromFileInfo() throws SQLException {
        FileInfoInt f = insertSampleFile("upd.ext");
        f.setGenre("Genre2");
        assertTrue(writer.update(f));
        FileInfoInt fromDb = dbConnJaMuz.file().getFile(f.getIdFile(), "");
        // DaoFileWrite.update() updates audio metadata (genre, artist, etc.) but not the filename
        assertEquals("upd.ext", fromDb.getFilename());
        assertEquals("Genre2", fromDb.getGenre());
    }

    @Test
    void shouldUpdateLastPlayedAndCounter() throws SQLException {
        FileInfoInt f = insertSampleFile("lp.ext");
        f.setPlayCounter(3);
        assertTrue(writer.updateLastPlayedAndCounter(f));
        FileInfoInt fromDb = dbConnJaMuz.file().getFile(f.getIdFile(), "");
        assertEquals(4, fromDb.getPlayCounter());
    }

    @Test
    void shouldUpdateRating() throws SQLException {
        FileInfoInt f = insertSampleFile("rate.ext");
        f.setRating(4);
        assertTrue(writer.updateRating(f));
        assertEquals(4, intScalar("SELECT rating FROM file WHERE idFile = ?", f.getIdFile()));
    }

    @Test
    void shouldUpdateFileGenre() throws SQLException {
        FileInfoInt f = insertSampleFile("genre.ext");
        f.setGenre("Jazz");
        assertTrue(writer.updateFileGenre(f));
        assertEquals("Jazz", dbConnJaMuz.file().getFile(f.getIdFile(), "").getGenre());
    }

    @Test
    void shouldUpdateIdPathForAllFilesOnPath() throws SQLException {
        int[] keyPath2 = new int[1];
        dbConnJaMuz.path().lock().insert("rel/fw2/", new Date(), FolderInfo.CheckedFlag.UNCHECKED, "mbid2", keyPath2);
        int path2 = keyPath2[0];
        try {
            FileInfoInt f = new FileInfoInt("rel/fw2/move.ext", ROOT);
            f.setIdPath(pathId);
            int[] key = new int[1];
            assertTrue(writer.insert(f, key));
            f.setIdFile(key[0]);
            f.setRating(0);
            assertTrue(writer.updateIdPath(pathId, path2));
            assertEquals(path2, intScalar("SELECT idPath FROM file WHERE idFile = ?", f.getIdFile()));
        } finally {
            try (Statement st = dbConnJaMuz.getDbConn().getConnection().createStatement()) {
                st.executeUpdate("DELETE FROM file WHERE idPath = " + path2);
                st.executeUpdate("DELETE FROM path WHERE idPath = " + path2);
            }
        }
    }

    @Test
    void shouldUpdateModifDateAndNameById() throws SQLException {
        FileInfoInt f = insertSampleFile("mod.ext");
        Date d = new Date(150_000_000_000L);
        assertTrue(writer.updateModifDate(f.getIdFile(), d, "renamed.ext"));
        FileInfoInt fromDb = dbConnJaMuz.file().getFile(f.getIdFile(), "");
        assertEquals("renamed.ext", fromDb.getFilename());
    }

    @Test
    void shouldUpdateTagsModifDateByTagValue() throws SQLException {
        FileInfoInt f = insertSampleFile("tagmod.ext");
        linkTagToFile(f.getIdFile(), "modTagVal");
        assertTrue(writer.updateModifDate("modTagVal"));
    }

    @Test
    void shouldUpdateTagsModifDateByFileInfo() throws SQLException {
        FileInfoInt f = insertSampleFile("tagmod2.ext");
        FileInfo asFileInfo = dbConnJaMuz.file().getFile(f.getIdFile(), "");
        assertTrue(writer.updateModifDate(asFileInfo));
    }
}
