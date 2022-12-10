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
package jamuz.utils;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({jamuz.utils.PopupTest.class, jamuz.utils.EncryptionTest.class, jamuz.utils.OSTest.class, jamuz.utils.SwingTest.class, jamuz.utils.LogTextTest.class, jamuz.utils.FileSystemTest.class, jamuz.utils.FtpTest.class, jamuz.utils.QRCodeTest.class, jamuz.utils.SSHTest.class, jamuz.utils.ProcessAbstractTest.class, jamuz.utils.ClipboardTextTest.class, jamuz.utils.InterTest.class, jamuz.utils.ImageUtilsTest.class, jamuz.utils.XMLTest.class, jamuz.utils.DesktopTest.class, jamuz.utils.UtilsTest.class, jamuz.utils.ClipboardImageTest.class, jamuz.utils.DateTimeTest.class, jamuz.utils.BenchmarkTest.class})
public class UtilsSuite {

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

}
