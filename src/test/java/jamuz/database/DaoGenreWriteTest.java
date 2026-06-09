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
import java.sql.SQLException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/**
 * Tests sur {@link DaoGenreWrite}.
 */
class DaoGenreWriteTest {

    private static DbConnJaMuz dbConnJaMuz;
    private static DaoGenreWrite writer;

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        writer = new DaoGenreWrite(dbConnJaMuz.getDbConn(), dbConnJaMuz.genre());
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    void shouldRejectInsertWhenGenreAlreadyInDatabase() {
        assertFalse(writer.insert("Rock"));
    }

    @Test
    void shouldInsertUpdateAndDeleteCustomGenre() {
        String g = "UnitGenreWrite999";
        assertFalse(dbConnJaMuz.genre().isSupported(g));
        assertTrue(writer.insert(g));
        assertTrue(dbConnJaMuz.genre().isSupported(g));
        assertTrue(writer.update(g, g + "Ren"));
        assertTrue(dbConnJaMuz.genre().isSupported(g + "Ren"));
        assertFalse(dbConnJaMuz.genre().isSupported(g));
        assertTrue(writer.delete(g + "Ren"));
        assertFalse(dbConnJaMuz.genre().isSupported(g + "Ren"));
    }
}
