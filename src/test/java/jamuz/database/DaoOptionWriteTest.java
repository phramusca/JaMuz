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

import jamuz.Jamuz;
import jamuz.Machine;
import jamuz.Option;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoOptionWrite}. */
class DaoOptionWriteTest {

    private static final String HOST = "OptWriteTestHost";
    private static DbConnJaMuz dbConnJaMuz;
    private static DaoOptionWrite writer;

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        writer = new DaoOptionWrite(dbConnJaMuz.getDbConn());
        dbConnJaMuz.machine().lock().getOrInsert(HOST, new StringBuilder(), false);
        Jamuz.setDb(dbConnJaMuz);
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    void shouldUpdateAllOptionsForMachine() {
        Machine m = new Machine(HOST);
        assertTrue(m.read());
        String newValue = m.getOption(0).getValue() + "-x";
        m.getOption(0).setValue(newValue);
        assertTrue(writer.update(m));
    }

    @Test
    void shouldUpdateSingleOptionValue() throws SQLException {
        Machine m = new Machine(HOST);
        assertTrue(m.read());
        // Use the 'library.isMaster' option (type=bool) to avoid path-normalisation side effects
        // Option index 1 corresponds to idOptionType=2 (library.isMaster)
        Option boolOption = m.getOption(1);
        String value = "true";
        assertTrue(writer.update(boolOption, value));
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "SELECT value FROM option WHERE idMachine = ? AND idOptionType = ?")) {
            st.setInt(1, boolOption.getIdMachine());
            st.setInt(2, boolOption.getIdOptionType());
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                assertEquals(value, rs.getString(1));
            }
        }
    }
}
