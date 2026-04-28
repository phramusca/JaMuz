package jamuz.database;

import jamuz.Option;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoMachine} and related options flow. */
public class DaoMachineTest {

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
        assertNotNull(dbConnJaMuz.machine().lock());
    }

    @Test
    public void shouldCreateUpdateAndDeleteMachine() {
        String name = "MachineReadA";
        StringBuilder description = new StringBuilder();
        assertTrue(dbConnJaMuz.machine().lock().getOrInsert(name, description, false));

        DefaultListModel model = new DefaultListModel();
        dbConnJaMuz.listModel().getMachineListModel(model);
        assertEquals(1, model.size());

        ArrayList<Option> options = new ArrayList<>();
        assertTrue(dbConnJaMuz.option().get(options, name));
        assertFalse(options.isEmpty());

        assertTrue(dbConnJaMuz.machine().lock().update(1, "desc-machine"));
        assertTrue(dbConnJaMuz.machine().lock().delete(name));
    }
}
