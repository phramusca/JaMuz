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
@Suite.SuiteClasses({jamuz.gui.GuiSuite.class, jamuz.StatSourceSQLTest.class, jamuz.IconBufferCoverTest.class, jamuz.MainTest.class, jamuz.MachineTest.class, jamuz.IconBufferTest.class, jamuz.process.ProcessSuite.class, jamuz.DbConnTest.class, jamuz.DbConnJaMuzTest.class, jamuz.PlaylistTest.class, jamuz.DbInfoTest.class, jamuz.FileInfoTest.class, jamuz.ressources.RessourcesSuite.class, jamuz.FileInfoIntTest.class, jamuz.OptionTest.class, jamuz.OptionsTest.class, jamuz.JamuzTest.class, jamuz.remote.RemoteSuite.class, jamuz.utils.UtilsSuite.class, jamuz.KeysTest.class, jamuz.StatSourceAbstractTest.class, jamuz.player.PlayerSuite.class, jamuz.LogFormatTest.class})
public class JamuzSuite {

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
