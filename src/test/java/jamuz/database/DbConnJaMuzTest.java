package jamuz.database;

import jamuz.FileInfoInt;
import jamuz.process.check.FolderInfo;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import test.helpers.TestUnitSettings;

/** Tests for {@link DbConnJaMuz}. */
class DbConnJaMuzTest {

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
    void shouldExposeAllDaoAccessors() {
        assertNotNull(dbConnJaMuz.genre());
        assertNotNull(dbConnJaMuz.tag());
        assertNotNull(dbConnJaMuz.fileTag());
        assertNotNull(dbConnJaMuz.machine());
        assertNotNull(dbConnJaMuz.playlist());
        assertNotNull(dbConnJaMuz.device());
        assertNotNull(dbConnJaMuz.deviceFile());
        assertNotNull(dbConnJaMuz.client());
        assertNotNull(dbConnJaMuz.statSource());
        assertNotNull(dbConnJaMuz.schema());
        assertNotNull(dbConnJaMuz.file());
        assertNotNull(dbConnJaMuz.fileTranscoded());
        assertNotNull(dbConnJaMuz.path());
        assertNotNull(dbConnJaMuz.playCounter());
        assertNotNull(dbConnJaMuz.option());
        assertNotNull(dbConnJaMuz.album());
        assertNotNull(dbConnJaMuz.listModel());
    }

    @Test
    void shouldAllowConcurrentPathAndFileInsertions() throws InterruptedException {
        dbConnJaMuz.file().setLocationLibrary("/root/concurrency/");
        ExecutorService pool = Executors.newFixedThreadPool(8);
        for (int i = 0; i < 20; i++) {
            final int idx = i;
            pool.submit(() -> {
                int[] keyPath = new int[1];
                dbConnJaMuz.path().lock().insert("conc/path/" + idx, new Date(), FolderInfo.CheckedFlag.UNCHECKED, "", keyPath);
                FileInfoInt f = new FileInfoInt("conc/path/" + idx + "/f.mp3", "/root/concurrency/");
                f.setIdPath(keyPath[0]);
                int[] key = new int[1];
                dbConnJaMuz.file().lock().insert(f, key);
            });
        }
        pool.shutdown();
        assertTrue(pool.awaitTermination(30, TimeUnit.SECONDS));
    }
}
