package jamuz.database;

import jamuz.FileInfoInt;
import jamuz.gui.swing.ListElement;
import jamuz.process.check.FolderInfo;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.DefaultListModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoListModel}. */
class DaoListModelTest {

    private static DbConnJaMuz dbConnJaMuz;

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
        dbConnJaMuz.machine().lock().getOrInsert("ListModelHost", new StringBuilder(), false);
        dbConnJaMuz.tag().lock().insertIfMissing("listmodel-tag");
        dbConnJaMuz.file().setLocationLibrary("/root/list/");
        dbConnJaMuz.listModel().setLocationLibrary("/root/list/");

        int[] keyPath = new int[1];
        dbConnJaMuz.path().lock().insert("list/path/", new Date(), FolderInfo.CheckedFlag.UNCHECKED, "", keyPath);
        FileInfoInt f = new FileInfoInt("list/path/a.mp3", "/root/list/");
        f.setIdPath(keyPath[0]);
        f.setGenre("Reggae");
        f.setAlbumArtist("Artist A");
        int[] keyFile = new int[1];
        dbConnJaMuz.file().lock().insert(f, keyFile);
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    void shouldReadGenreTagAndMachineModels() {
        DefaultListModel genres = new DefaultListModel();
        dbConnJaMuz.listModel().getGenreListModel(genres);
        assertTrue(genres.size() > 0);

        DefaultListModel tags = new DefaultListModel();
        dbConnJaMuz.listModel().getTagListModel(tags);
        assertTrue(tags.contains("listmodel-tag"));

        DefaultListModel machines = new DefaultListModel();
        dbConnJaMuz.listModel().getMachineListModel(machines);
        assertEquals(1, machines.size());
        assertEquals("ListModelHost", ((ListElement) machines.get(0)).getValue());
    }

    @Test
    void shouldFillSelectorLists() {
        boolean[] ratings = new boolean[]{true, true, true, true, true, true};
        boolean[] checked = new boolean[]{true, true, true, true};
        DefaultListModel artists = new DefaultListModel();
        dbConnJaMuz.listModel().fillSelectorList(artists, "artist", "%", "%", "%", ratings, checked,
                0, 9999, 0, 9999, -1, "albumArtist");
        assertTrue(artists.size() >= 1);
    }
}
