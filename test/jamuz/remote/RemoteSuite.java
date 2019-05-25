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
package jamuz.remote;

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
@Suite.SuiteClasses({jamuz.remote.ClientInfoTest.class, jamuz.remote.ICallBackServerTest.class, jamuz.remote.EmissionTest.class, jamuz.remote.ReceptionTest.class, jamuz.remote.ServerTest.class, jamuz.remote.ProcessAbstractTest.class, jamuz.remote.DialogClientInfoTest.class, jamuz.remote.ICallBackReceptionTest.class, jamuz.remote.TableModelRemoteTest.class, jamuz.remote.PanelRemoteTest.class, jamuz.remote.ClientTest.class})
public class RemoteSuite {

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
