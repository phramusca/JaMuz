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

import jamuz.utils.Ftp;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DbInfo}. */
class DbInfoTest {

    private static DbConnJaMuz dbConnJaMuz;

    @TempDir
    Path tempDir;

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    void checkReturnsTrueWhenSqliteFileExists() {
        DbInfo info = dbConnJaMuz.getDbConn().getInfo();
        assertTrue(info.check());
    }

    @Test
    void sqliteLocationsInitiallyMatchForTempDb() {
        DbInfo info = dbConnJaMuz.getDbConn().getInfo();
        assertEquals(info.getLocationOri(), info.getLocationWork());
        assertEquals(DbInfo.LibType.Sqlite, info.getLibType());
        assertTrue(info.getLocationWork().endsWith(".db"));
    }

    @Test
    void setLocationWorkUpdatesWorkingCopyPath() throws IOException {
        DbInfo info = TestUnitSettings.getTempDbInfo();
        Path file = tempDir.resolve("relocated.db");
        info.setLocationWork(file.toString());
        assertEquals(file.toString(), info.getLocationWork());
    }

    @Test
    void getFtpBuildsWrapperFromFtpUri() {
        // URI without port (DbInfo FTP parser does not handle explicit port numbers)
        DbInfo ftpLike = new DbInfo(DbInfo.LibType.Sqlite,
                "ftp://user:secret@remote-host.invalid/sub/music.db", "", "");
        Ftp ftp = ftpLike.getFtp("/tmp/");
        assertNotNull(ftp);
    }

    @Test
    void backupDbWritesCopyIntoDestinationFolder() throws IOException {
        DbInfo info = dbConnJaMuz.getDbConn().getInfo();
        Path destDir = tempDir.resolve("bak");
        destDir.toFile().mkdirs();
        assertTrue(info.backupDB(destDir.toString() + File.separator));
        File[] files = destDir.toFile().listFiles((dir, name) -> name.endsWith(".db"));
        assertNotNull(files);
        assertEquals(1, files.length);
        assertTrue(files[0].length() > 0L);
    }

    @Test
    void copyDbReceiveMovesWorkingPathIntoFolder() throws IOException {
        DbInfo info = dbConnJaMuz.getDbConn().getInfo();
        String savedWork = info.getLocationWork();
        try {
            Path dir = tempDir.resolve("recv");
            dir.toFile().mkdirs();
            assertTrue(info.copyDB(true, dir.toString() + File.separator));
            File copied = new File(info.getLocationWork());
            assertTrue(copied.isFile());
            assertEquals(copied.getParentFile().getCanonicalPath(), dir.toFile().getCanonicalPath());
        } finally {
            info.setLocationWork(savedWork);
        }
    }
}
