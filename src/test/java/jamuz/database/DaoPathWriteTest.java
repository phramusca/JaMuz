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

import jamuz.process.check.FolderInfo;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoPathWrite}. */
public class DaoPathWriteTest {

    private static DbConnJaMuz dbConnJaMuz;
    private static DaoPathWrite writer;

    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        writer = new DaoPathWrite(dbConnJaMuz.getDbConn());
    }

    @AfterClass
    public static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Before
    public void wipePathWriteRows() throws SQLException {
        try (Statement st = dbConnJaMuz.getDbConn().getConnection().createStatement()) {
            st.executeUpdate("DELETE FROM path WHERE strPath LIKE 'pathWrite/%'");
        }
    }

    private int insertPath(String relPath) {
        int[] key = new int[1];
        assertTrue(writer.insert(relPath, new Date(), FolderInfo.CheckedFlag.UNCHECKED, "mbid-path", key));
        return key[0];
    }

    private int intScalar(String sql, Object... args) throws SQLException {
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) {
                st.setObject(i + 1, args[i]);
            }
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }
        }
    }

    @Test
    public void shouldInsertUpdateDeletePath() {
        int id = insertPath("pathWrite/ins/a");
        assertTrue(writer.update(id, new Date(), FolderInfo.CheckedFlag.OK,
                "pathWrite/upd/a", "mbid2"));
        assertEquals("pathWrite/upd/a",
                stringScalar("SELECT strPath FROM path WHERE idPath = ?", id));
        assertTrue(writer.delete(id));
        assertFalse(writer.delete(id));
    }

    private String stringScalar(String sql, Object arg) throws SQLException {
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(sql)) {
            st.setObject(1, arg);
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                return rs.getString(1);
            }
        }
    }

    @Test
    public void shouldUpdateCheckedFlagAndCopyRight() throws SQLException {
        int id = insertPath("pathWrite/chk");
        assertEquals(FolderInfo.CheckedFlag.UNCHECKED.getValue(),
                intScalar("SELECT checked FROM path WHERE idPath = ?", id));
        assertTrue(writer.updateCheckedFlag(id, FolderInfo.CheckedFlag.OK_WARNING));
        assertEquals(FolderInfo.CheckedFlag.OK_WARNING.getValue(),
                intScalar("SELECT checked FROM path WHERE idPath = ?", id));

        assertTrue(writer.updateCopyRight(id, 2));
        assertEquals(2, intScalar("SELECT copyRight FROM path WHERE idPath = ?", id));
    }

    @Test
    public void shouldResetCheckedFlagBatch() throws SQLException {
        int id = insertPath("pathWrite/reset");
        assertTrue(writer.updateCheckedFlag(id, FolderInfo.CheckedFlag.KO));
        assertEquals(FolderInfo.CheckedFlag.KO.getValue(),
                intScalar("SELECT checked FROM path WHERE idPath = ?", id));
        assertTrue(writer.updateCheckedFlagReset(FolderInfo.CheckedFlag.KO));
        assertEquals(FolderInfo.CheckedFlag.UNCHECKED.getValue(),
                intScalar("SELECT checked FROM path WHERE idPath = ?", id));
    }
}
