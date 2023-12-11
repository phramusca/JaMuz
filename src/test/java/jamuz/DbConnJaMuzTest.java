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
import jamuz.gui.swing.ListElement;
import jamuz.process.check.DuplicateInfo;
import jamuz.process.check.FolderInfo;
import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import jamuz.process.sync.SyncStatus;
import jamuz.remote.ClientInfo;
import java.awt.Color;
import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.swing.DefaultListModel;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import test.helpers.TestSettings;
import test.helpers.TestUnitSettings;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class DbConnJaMuzTest {

	private static DbConnJaMuz dbConnJaMuz;

	public DbConnJaMuzTest() {
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
		ExecutorService executorService = Executors.newFixedThreadPool(150);

		for (int i = 0; i < 10; i++) {
			final int index = i;

//			executorService.submit(() -> {
//				System.out.println("file" + index);
//				dbConnJaMuz.file().insert(new FileInfoInt("file" + index, "/root/path"), new int[1]);
//			});
//
//			executorService.submit(() -> {
//				System.out.println("genre" + index);
//				dbConnJaMuz.genre().insert("genre" + index);
//			});

			executorService.submit(() -> {
				System.out.println("path" + index);
				dbConnJaMuz.path().insert("path" + index, new Date(), FolderInfo.CheckedFlag.UNCHECKED, "mmmbbbiiidd", new int[1]);
			});

//			executorService.submit(() -> {
//				System.out.println("tag" + index);
//				dbConnJaMuz.tag().insert("tag" + index);
//			});
		}

		executorService.awaitTermination(30, TimeUnit.SECONDS);
	}

}

