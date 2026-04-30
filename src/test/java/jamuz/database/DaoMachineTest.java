package jamuz.database;

import jamuz.Option;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DaoMachine} and related options flow. */
class DaoMachineTest {

    private static DbConnJaMuz dbConnJaMuz;

    @BeforeAll
    static void setUpClass() throws SQLException, ClassNotFoundException, IOException {
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
    }

    @AfterAll
    static void tearDownClass() {
        TestUnitSettings.cleanupTempDatabase(dbConnJaMuz);
    }

    @Test
    void shouldExposeWriteLock() {
        assertNotNull(dbConnJaMuz.machine().lock());
    }

    @Test
    void shouldCreateUpdateAndDeleteMachine() {
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
