/*
 * Copyright (C) 2019 phramusca ( https://github.com/phramusca/ )
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
package jamuz;

import jamuz.database.DbConnJaMuz;
import jamuz.process.check.FolderInfo;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;
import test.helpers.TestUnitSettings;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
@RunWith(MockitoJUnitRunner.class)
public class DbConnJaMuzTest {

//	@Mock
//    private Jamuz mockJamuz;
	
	@Mock
    private Machine mockMachine;

	@InjectMocks
    private static DbConnJaMuz dbConnJaMuz;

    public DbConnJaMuzTest() {
		mockStatic(Jamuz.class);
        when(Jamuz.getMachine()).thenReturn(mockMachine);
//		Machine machine = Jamuz.getMachine();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
		
		
        dbConnJaMuz = TestUnitSettings.createTempDatabase();
    }

    @AfterClass
    public static void tearDownClass() {
//		new File(dbConnJaMuz.getDbConn().getInfo().getLocationOri()).delete();
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testConcurrency() throws InterruptedException {

        
		
//        when(mockMachine.getOptionValue("location.library")).thenReturn("mockedLocation");
		when(mockMachine.getOption(anyString())).thenReturn(new Option("location.library", "mockedLocation", -1, -1, ""));


        ExecutorService executorService = Executors.newFixedThreadPool(150);

        for (int i = 0; i < 1; i++) {
            final int index = i;

            executorService.submit(() -> {
                System.out.println("file insert " + index);
                int[] keyPath = new int[1];
                dbConnJaMuz.path().lock().insert("4file/insert" + index, new Date(), FolderInfo.CheckedFlag.UNCHECKED, "mmmbbbiiidd", keyPath);
                int[] key = new int[1];
                FileInfoInt fileInfoInt = new FileInfoInt("file/insert" + index, "/root/file/insert" + index);
                fileInfoInt.setIdPath(keyPath[0]);
                dbConnJaMuz.file().lock().insert(fileInfoInt, key);
                System.out.println("file inserted " + index);
            });

            executorService.submit(() -> {
                System.out.println("file update " + index);
                int[] keyPath = new int[1];
                dbConnJaMuz.path().lock().insert("4file/update" + index, new Date(), FolderInfo.CheckedFlag.UNCHECKED, "mmmbbbiiidd", keyPath);
                int[] key = new int[1];
                FileInfoInt fileInfoInt = new FileInfoInt("file/update" + index, "/root/file/update" + index);
                fileInfoInt.setIdPath(keyPath[0]);
                dbConnJaMuz.file().lock().insert(fileInfoInt, key);
                FileInfoInt file = dbConnJaMuz.file().getFile(key[0], "");
                file.setGenre("Updated");
                dbConnJaMuz.file().lock().update(file);
                System.out.println("file updated " + index);
            });
//
//			executorService.submit(() -> {
//				System.out.println("genre" + index);

//				dbConnJaMuz.genre().insert("genre" + index);
//			});
            executorService.submit(() -> {
                System.out.println("path insert " + index);
                int[] key = new int[1];
                dbConnJaMuz.path().lock().insert("path/insert" + index, new Date(), FolderInfo.CheckedFlag.UNCHECKED, "mmmbbbiiidd", key);
                System.out.println("path inserted " + index);
            });

            executorService.submit(() -> {
                System.out.println("path update " + index);
                final String path = "path/update" + index;
                int[] key = new int[1];
                dbConnJaMuz.path().lock().insert(path, new Date(), FolderInfo.CheckedFlag.UNCHECKED, "mmmbbbiiidd", key);
                int idPath = dbConnJaMuz.path().getIdPath(path);
                dbConnJaMuz.path().lock().update(idPath, new Date(), FolderInfo.CheckedFlag.OK_WARNING, path, "MbIdd + " + idPath);
                System.out.println("path updated " + index);
            });

//			executorService.submit(() -> {
//				System.out.println("tag" + index);
//				dbConnJaMuz.tag().insert("tag" + index);
//			});
        }
        executorService.shutdown();
        if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
            System.err.println("Pool did not terminate");
        }
//        while (!executorService.isTerminated()) {
//            Thread.sleep(1000); // Sleep for a short duration
//        }

        System.out.println("-- END --");
    }

}
