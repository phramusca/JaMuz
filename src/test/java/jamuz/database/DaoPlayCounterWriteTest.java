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
import jamuz.process.merge.StatSource;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoPlayCounterWrite}. */
class DaoPlayCounterWriteTest {

    private static DbConnJaMuz dbConnJaMuz;
    private static DaoPlayCounterWrite writer;
    private static DaoStatSourceWrite statWriter;
    private static DaoFileWrite fileWriter;
    private static final String MACHINE = "PlayCntWriteHost";
    private static final String ROOT = "/root/pcw/";
    private static int pathId;
    private static int idStatSource;

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        writer = new DaoPlayCounterWrite(dbConnJaMuz.getDbConn());
        statWriter = new DaoStatSourceWrite(dbConnJaMuz.getDbConn());
        fileWriter = dbConnJaMuz.file().lock();
        dbConnJaMuz.file().setLocationLibrary(ROOT);

        dbConnJaMuz.machine().lock().getOrInsert(MACHINE, new StringBuilder(), false);

        StatSource ss = new StatSource(MACHINE);
        ss.getSource().setLocation("/tmp/playcounter-unit.db");
        ss.getSource().setName("PlayCntSrc");
        ss.getSource().setRootPath("/music");
        assertTrue(statWriter.insertOrUpdate(ss));

        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "SELECT idStatSource FROM statSource WHERE name = ?")) {
            st.setString(1, "PlayCntSrc");
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                idStatSource = rs.getInt(1);
            }
        }

        int[] keyPath = new int[1];
        dbConnJaMuz.path().lock().insert("rel/pcw/", new Date(), FolderInfo.CheckedFlag.UNCHECKED, "", keyPath);
        pathId = keyPath[0];
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @BeforeEach
    void wipePlayCountersAndFiles() throws SQLException {
        try (Statement st = dbConnJaMuz.getDbConn().getConnection().createStatement()) {
            st.executeUpdate("DELETE FROM playCounter");
            st.executeUpdate("DELETE FROM file WHERE idPath = " + pathId);
        }
    }

    private FileInfoInt insertFile(String name, int counter) throws SQLException {
        FileInfoInt f = new FileInfoInt("rel/pcw/" + name, ROOT);
        f.setIdPath(pathId);
        int[] key = new int[1];
        assertTrue(fileWriter.insert(f, key));
        f.setIdFile(key[0]);
        f.setPlayCounter(counter);
        return f;
    }

    private int counterFor(int idFile) throws SQLException {
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "SELECT playCounter FROM playCounter WHERE idFile = ? AND idStatSource = ?")) {
            st.setInt(1, idFile);
            st.setInt(2, idStatSource);
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }
        }
    }

    @Test
    void shouldInsertPlayCounterWhenRowMissing() throws SQLException {
        FileInfoInt f = insertFile("a.mp3", 11);
        ArrayList<FileInfoInt> files = new ArrayList<>();
        files.add(f);
        assertTrue(writer.update(files, idStatSource));
        assertEquals(11, counterFor(f.getIdFile()));
    }

    @Test
    void shouldUpdateExistingPlayCounterRow() throws SQLException {
        FileInfoInt f = insertFile("b.mp3", 3);
        ArrayList<FileInfoInt> files = new ArrayList<>();
        files.add(f);
        assertTrue(writer.update(files, idStatSource));

        f.setPlayCounter(99);
        assertTrue(writer.update(files, idStatSource));
        assertEquals(99, counterFor(f.getIdFile()));
    }
}
