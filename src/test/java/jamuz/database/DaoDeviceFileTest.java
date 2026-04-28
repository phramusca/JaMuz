package jamuz.database;

import jamuz.FileInfoInt;
import jamuz.Playlist;
import jamuz.process.check.FolderInfo;
import jamuz.process.sync.Device;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoDeviceFile}. */
public class DaoDeviceFileTest {

    private static DbConnJaMuz dbConnJaMuz;

    @BeforeClass
    public static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
    }

    @AfterClass
    public static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    public void shouldExposeWriteLock() {
        assertNotNull(dbConnJaMuz.deviceFile().lock());
    }

    @Test
    public void shouldReadFilesLinkedToDeviceAfterInsertOrIgnore() {
        dbConnJaMuz.machine().lock().getOrInsert("DeviceFileHost", new StringBuilder(), false);
        dbConnJaMuz.playlist().lock().insert(new Playlist(0, "plDeviceFile", false, 0, Playlist.LimitUnit.Gio, false,
                Playlist.Type.Albums, Playlist.Match.All, false, "ext"));
        Device d = new Device(-1, "DeviceFileDev", "src", "dst", 1, "DeviceFileHost", true);
        d.setIdPlaylist(1);
        assertTrue(dbConnJaMuz.device().lock().insertOrUpdate(d));

        int[] keyPath = new int[1];
        assertTrue(dbConnJaMuz.path().lock().insert("devfile/path", new Date(), FolderInfo.CheckedFlag.UNCHECKED, "", keyPath));
        dbConnJaMuz.file().setLocationLibrary("/root/dev/");
        FileInfoInt f = new FileInfoInt("devfile/path/a.mp3", "/root/dev/");
        f.setIdPath(keyPath[0]);
        int[] keyFile = new int[1];
        assertTrue(dbConnJaMuz.file().lock().insert(f, keyFile));
        f.setIdFile(keyFile[0]);

        ArrayList<FileInfoInt> fileList = new ArrayList<>();
        fileList.add(f);
        dbConnJaMuz.deviceFile().lock().insertOrIgnore(fileList, 1);

        ArrayList<FileInfoInt> loaded = new ArrayList<>();
        assertTrue(dbConnJaMuz.file().getFiles(loaded, new Device(1, "n", "s", "d", 1, "DeviceFileHost", true)));
        assertEquals(1, loaded.size());
        assertEquals(keyFile[0], loaded.get(0).getIdFile());
    }
}
