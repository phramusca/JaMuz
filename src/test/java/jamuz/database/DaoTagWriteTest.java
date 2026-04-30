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

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoTagWrite}. */
class DaoTagWriteTest {

    private static DbConnJaMuz dbConnJaMuz;
    private static DaoTagWrite writer;

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        writer = new DaoTagWrite(dbConnJaMuz.getDbConn());
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @BeforeEach
    void wipeTagWriteRows() throws SQLException {
        try (Statement st = dbConnJaMuz.getDbConn().getConnection().createStatement()) {
            st.executeUpdate("DELETE FROM tagFile WHERE idTag IN (SELECT id FROM tag WHERE value LIKE 'TagW_%')");
            st.executeUpdate("DELETE FROM tag WHERE value LIKE 'TagW_%'");
        }
    }

    private boolean tagExists(String value) throws SQLException {
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "SELECT COUNT(*) FROM tag WHERE value = ?")) {
            st.setString(1, value);
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                return rs.getInt(1) > 0;
            }
        }
    }

    @Test
    void shouldInsertDistinctTags() {
        assertTrue(writer.insert("TagW_one"));
        assertFalse(writer.insert("TagW_one"));
        assertTrue(writer.insert("TagW_two"));
    }

    @Test
    void insertIfMissingCreatesOrAcknowledgesExistingTag() {
        assertTrue(writer.insertIfMissing("TagW_miss"));
        assertTrue(writer.insertIfMissing("TagW_miss"));
    }

    @Test
    void shouldRenameTagValue() throws SQLException {
        assertTrue(writer.insert("TagW_old"));
        assertTrue(writer.update("TagW_old", "TagW_new"));
        assertFalse(tagExists("TagW_old"));
        assertTrue(tagExists("TagW_new"));
    }

    @Test
    void shouldDeleteUnusedTag() {
        assertTrue(writer.insert("TagW_del"));
        assertTrue(writer.delete("TagW_del"));
        assertFalse(writer.delete("TagW_del"));
    }
}
