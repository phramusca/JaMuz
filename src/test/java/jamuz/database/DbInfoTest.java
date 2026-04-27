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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DbInfo}. */
public class DbInfoTest {

    private static DbConnJaMuz dbConnJaMuz;

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
    }

    @AfterClass
    public static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    public void checkReturnsTrueWhenSqliteFileExists() {
        DbInfo info = dbConnJaMuz.getDbConn().getInfo();
        assertTrue(info.check());
    }

    @Test
    public void sqliteLocationsInitiallyMatchForTempDb() {
        DbInfo info = dbConnJaMuz.getDbConn().getInfo();
        assertEquals(info.getLocationOri(), info.getLocationWork());
        assertEquals(DbInfo.LibType.Sqlite, info.getLibType());
        assertTrue(info.getLocationWork().endsWith(".db"));
    }

    @Test
    public void setLocationWorkUpdatesWorkingCopyPath() throws IOException {
        DbInfo info = TestUnitSettings.getTempDbInfo();
        Path file = tempFolder.newFile("relocated.db").toPath();
        info.setLocationWork(file.toString());
        assertEquals(file.toString(), info.getLocationWork());
    }

    @Test
    public void getFtpBuildsWrapperFromFtpUri() {
        DbInfo ftpLike = new DbInfo(DbInfo.LibType.Sqlite,
                "ftp://user:secret@remote-host.invalid:21/sub/music.db", "", "");
        Ftp ftp = ftpLike.getFtp("/tmp/");
        assertNotNull(ftp);
    }

    @Test
    public void backupDbWritesCopyIntoDestinationFolder() throws IOException {
        DbInfo info = dbConnJaMuz.getDbConn().getInfo();
        Path destDir = tempFolder.newFolder("bak").toPath();
        assertTrue(info.backupDB(destDir.toString() + File.separator));
        File[] files = destDir.toFile().listFiles((dir, name) -> name.endsWith(".db"));
        assertNotNull(files);
        assertEquals(1, files.length);
        assertTrue(files[0].length() > 0L);
    }

    @Test
    public void copyDbReceiveMovesWorkingPathIntoFolder() throws IOException {
        DbInfo info = dbConnJaMuz.getDbConn().getInfo();
        String savedWork = info.getLocationWork();
        try {
            Path dir = tempFolder.newFolder("recv").toPath();
            assertTrue(info.copyDB(true, dir.toString() + File.separator));
            File copied = new File(info.getLocationWork());
            assertTrue(copied.isFile());
            assertEquals(copied.getParent(), dir.toFile());
        } finally {
            info.setLocationWork(savedWork);
        }
    }
}
