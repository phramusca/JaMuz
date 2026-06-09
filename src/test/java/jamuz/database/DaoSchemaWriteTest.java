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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/**
 * Exercises {@link DaoSchemaWrite#update(int)} in the no-op case (already at the target version) to
 * avoid running GUI-driven upgrade paths in unit tests.
 */
class DaoSchemaWriteTest {

    private static DbConnJaMuz dbConnJaMuz;
    private static int currentSchemaVersion;

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "SELECT version FROM versionHistory ORDER BY version DESC LIMIT 1")) {
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                currentSchemaVersion = rs.getInt(1);
            }
        }
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    void shouldReportNoOpWhenDatabaseAlreadyAtRequestedVersion() {
        assertTrue(dbConnJaMuz.schema().lock().update(currentSchemaVersion));
    }
}
