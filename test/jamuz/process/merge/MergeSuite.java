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
package jamuz.process.merge;

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
@Suite.SuiteClasses({jamuz.process.merge.ProcessMergeTest.class, jamuz.process.merge.StatSourceMediaMonkeyTest.class, jamuz.process.merge.StatSourceJaMuzRemoteTest.class, jamuz.process.merge.PanelMergeTest.class, jamuz.process.merge.StatSourceMixxxTest.class, jamuz.process.merge.ICallBackMergeTest.class, jamuz.process.merge.StatSourceKodiTest.class, jamuz.process.merge.StatSourceGuayadequeTest.class, jamuz.process.merge.StatSourceMyTunesTest.class, jamuz.process.merge.StatSourceTest.class})
public class MergeSuite {

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
