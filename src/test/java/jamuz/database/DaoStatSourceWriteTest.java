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

import jamuz.process.merge.StatSource;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoStatSourceWrite}. */
public class DaoStatSourceWriteTest {

    private static final String MACHINE = "StatWriteTestHost";
    private static DbConnJaMuz dbConnJaMuz;
    private static DaoStatSourceWrite writer;

    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        writer = new DaoStatSourceWrite(dbConnJaMuz.getDbConn());
        dbConnJaMuz.machine().lock().getOrInsert(MACHINE, new StringBuilder(), false);
    }

    @AfterClass
    public static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Before
    public void wipeStatSourcesUnderPrefix() throws SQLException {
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "DELETE FROM statSource WHERE name LIKE ?")) {
            st.setString(1, "StatW_%");
            st.executeUpdate();
        }
    }

    private StatSource newSource(String name) {
        StatSource ss = new StatSource(MACHINE);
        ss.getSource().setLocation("/tmp/statsource-unit-" + name + ".db");
        ss.getSource().setName(name);
        ss.getSource().setRootPath("/root");
        return ss;
    }

    private int idForName(String name) throws SQLException {
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "SELECT idStatSource FROM statSource WHERE name = ?")) {
            st.setString(1, name);
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }
        }
    }

    @Test
    public void shouldInsertAndUpdateStatSource() throws SQLException {
        StatSource inserted = newSource("StatW_new");
        assertTrue(writer.insertOrUpdate(inserted));
        int id = idForName("StatW_new");

        StatSource updated = newSource("StatW_upd");
        updated.setId(id);
        assertTrue(writer.insertOrUpdate(updated));

        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "SELECT name FROM statSource WHERE idStatSource = ?")) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                assertEquals("StatW_upd", rs.getString("name"));
            }
        }
    }

    @Test
    public void shouldRefreshLastMergeDate() throws SQLException {
        StatSource ss = newSource("StatW_merge");
        assertTrue(writer.insertOrUpdate(ss));
        int id = idForName("StatW_merge");

        String after = writer.updateLastMergeDate(id);
        assertNotEquals("1970-01-01 00:00:00", after);
    }

    @Test
    public void shouldDeleteStatSource() throws SQLException {
        StatSource ss = newSource("StatW_del");
        assertTrue(writer.insertOrUpdate(ss));
        int id = idForName("StatW_del");
        assertTrue(writer.delete(id));
        assertFalse(writer.delete(id));
    }
}
