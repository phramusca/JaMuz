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
import jamuz.Playlist;
import jamuz.process.check.FolderInfo;
import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import jamuz.process.sync.SyncStatus;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/**
 * Tests sur {@link DaoDeviceFileWrite} (insert batch, upsert, delete).
 */
class DaoDeviceFileWriteTest {

    private static DbConnJaMuz dbConnJaMuz;
    private static DaoDeviceFileWrite writer;
    private static int deviceId = 1;
    private static int pathId;

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        writer = new DaoDeviceFileWrite(dbConnJaMuz.getDbConn());
        seedPlaylistMachineDeviceAndPath();
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @BeforeEach
    void clearDeviceFileRows() throws SQLException {
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "DELETE FROM deviceFile")) {
            st.executeUpdate();
        }
    }

    private static void seedPlaylistMachineDeviceAndPath() throws SQLException {
        Playlist playlist = new Playlist(0, "plDeviceFileWrite", false, 0, Playlist.LimitUnit.Gio, false,
                Playlist.Type.Albums, Playlist.Match.All, false, "ext");
        dbConnJaMuz.playlist().lock().insert(playlist);

        String hostname = "DaoDeviceFileWriteHost";
        dbConnJaMuz.machine().lock().getOrInsert(hostname, new StringBuilder(), true);

        Device device = new Device(-1, "dev", "src", "dst", 1, hostname, true);
        device.setIdPlaylist(1);
        dbConnJaMuz.device().lock().insertOrUpdate(device);

        StatSource statSource = new StatSource(hostname);
        dbConnJaMuz.statSource().lock().insertOrUpdate(statSource);

        int[] keyPath = new int[1];
        dbConnJaMuz.path().lock().insert("rel/base/", new Date(), FolderInfo.CheckedFlag.UNCHECKED, "mbid", keyPath);
        pathId = keyPath[0];
    }

    private static FileInfoInt insertFileRow(String filenameUnderBase) throws SQLException {
        FileInfoInt file = new FileInfoInt("rel/base/" + filenameUnderBase, "/root");
        file.setIdPath(pathId);
        int[] keyFile = new int[1];
        dbConnJaMuz.file().lock().insert(file, keyFile);
        file.setIdFile(keyFile[0]);
        file.setRating(0);
        file.setStatus(SyncStatus.NEW);
        return file;
    }

    private static int countDeviceFiles(int idDev) throws SQLException {
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "SELECT COUNT(*) FROM deviceFile WHERE idDevice = ?")) {
            st.setInt(1, idDev);
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                return rs.getInt(1);
            }
        }
    }

    private static String statusInDb(int idFile, int idDev) throws SQLException {
        try (PreparedStatement st = dbConnJaMuz.getDbConn().getConnection().prepareStatement(
                "SELECT status FROM deviceFile WHERE idFile = ? AND idDevice = ?")) {
            st.setInt(1, idFile);
            st.setInt(2, idDev);
            try (ResultSet rs = st.executeQuery()) {
                assertTrue(rs.next());
                return rs.getString("status");
            }
        }
    }

    @Test
    void shouldReturnEmptyWhenInsertOrIgnoreWithNoFiles() {
        assertTrue(writer.insertOrIgnore(new ArrayList<>(), deviceId).isEmpty());
    }

    @Test
    void shouldInsertOrIgnoreOneRowAndExposeNewStatus() throws SQLException {
        FileInfoInt a = insertFileRow("one.ext");
        ArrayList<FileInfoInt> batch = new ArrayList<>();
        batch.add(a);

        ArrayList<FileInfoInt> inserted = writer.insertOrIgnore(batch, deviceId);
        assertEquals(1, inserted.size());
        assertEquals(1, countDeviceFiles(deviceId));
        assertEquals("NEW", statusInDb(a.getIdFile(), deviceId));
    }

    @Test
    void shouldNotDuplicateRowWhenInsertOrIgnoreCalledTwice() throws SQLException {
        FileInfoInt f = insertFileRow("dup.ext");
        ArrayList<FileInfoInt> batch = new ArrayList<>();
        batch.add(f);

        assertEquals(1, writer.insertOrIgnore(batch, deviceId).size());
        assertTrue(writer.insertOrIgnore(batch, deviceId).isEmpty());
        assertEquals(1, countDeviceFiles(deviceId));
    }

    @Test
    void shouldUpdateStatusOnInsertOrUpdateAfterInitialInsert() throws SQLException {
        FileInfoInt f = insertFileRow("up.ext");
        ArrayList<FileInfoInt> batch = new ArrayList<>();
        batch.add(f);
        assertFalse(writer.insertOrIgnore(batch, deviceId).isEmpty());

        f.setStatus(SyncStatus.INFO);
        ArrayList<FileInfoInt> updateBatch = new ArrayList<>();
        updateBatch.add(f);
        assertFalse(writer.insertOrUpdate(updateBatch, deviceId).isEmpty());
        assertEquals("INFO", statusInDb(f.getIdFile(), deviceId));
    }

    @Test
    void shouldRemoveAllDeviceFileRowsWhenDelete() throws SQLException {
        FileInfoInt f1 = insertFileRow("del1.ext");
        FileInfoInt f2 = insertFileRow("del2.ext");
        ArrayList<FileInfoInt> batch = new ArrayList<>();
        batch.add(f1);
        batch.add(f2);
        writer.insertOrIgnore(batch, deviceId);
        assertEquals(2, countDeviceFiles(deviceId));

        assertTrue(writer.delete(deviceId));
        assertEquals(0, countDeviceFiles(deviceId));
    }
}
