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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoMachineWrite}. */
public class DaoMachineWriteTest {

    private static DbConnJaMuz dbConnJaMuz;
    private static DaoMachineWrite writer;

    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        writer = new DaoMachineWrite(dbConnJaMuz.getDbConn());
    }

    @AfterClass
    public static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Before
    public void removeMwTestMachines() throws SQLException {
        try (Statement st = dbConnJaMuz.getDbConn().getConnection().createStatement()) {
            st.executeUpdate("DELETE FROM machine WHERE name LIKE 'MwTest%'");
        }
    }

    private static int machineId(String hostname) throws SQLException {
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "SELECT idMachine FROM machine WHERE name = ?")) {
            st.setString(1, hostname);
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }
        }
    }

    @Test
    public void shouldInsertMachineWhenMissingAndFillDescriptionWhenPresent() {
        StringBuilder desc = new StringBuilder();
        assertTrue(writer.getOrInsert("MwTestHostIns", desc, false));
        assertTrue(writer.getOrInsert("MwTestHostIns", desc, false));
        assertFalse(desc.toString().isEmpty());
    }

    @Test
    public void shouldUpdateMachineDescription() throws SQLException {
        assertTrue(writer.getOrInsert("MwTestHostUp", new StringBuilder(), false));
        int id = machineId("MwTestHostUp");
        assertTrue(writer.update(id, "new-description"));
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "SELECT description FROM machine WHERE idMachine = ?")) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                assertEquals("new-description", rs.getString("description"));
            }
        }
    }

    @Test
    public void shouldDeleteMachineByName() throws SQLException {
        assertTrue(writer.getOrInsert("MwTestHostDel", new StringBuilder(), false));
        assertTrue(writer.delete("MwTestHostDel"));
        assertFalse(writer.delete("MwTestHostDel"));
    }
}
