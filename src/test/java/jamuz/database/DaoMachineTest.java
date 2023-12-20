/*
 * Copyright (C) 2023 phramusca <phramusca@gmail.com>
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
package jamuz.database;

import jamuz.Jamuz;
import jamuz.Machine;
import jamuz.Option;
import jamuz.gui.swing.ListElement;
import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoMachineTest {
	
	public DaoMachineTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}

		/**
	 * Test of machine and option methods, of class DbConnJaMuz.
	 */
	@Test
	public void testMachineAndOption() {

		System.out.println("testMachineAndOption");
		
		//Create a new machine
		StringBuilder zText = new StringBuilder();
		String machineName = "000aaaa000"; //Hoping this this will be id 0 when sorted
		assertTrue("isMachine", Jamuz.getDb().machine().lock().getOrInsert(machineName, zText, false));

		//Get machines
		DefaultListModel defaultListModel = new DefaultListModel();
		Jamuz.getDb().listModel().getMachineListModel(defaultListModel);
		ListElement element = (ListElement) defaultListModel.get(0);
		assertEquals(2, defaultListModel.size()); //The created one and current machine
		assertEquals("<html><b>" + machineName + "</b><BR/><i></i></html>", element.toString());
		assertEquals(machineName, element.getValue());
		assertNull(element.getFile());

		//Get new machine options
		ArrayList<Option> expectedOptions = new ArrayList<>();
		int idMachine = 2;
		expectedOptions.add(new Option("location.library", "", idMachine, 1, "path"));
		expectedOptions.add(new Option("library.isMaster", "false", idMachine, 2, "bool"));
		expectedOptions.add(new Option("location.add", "", idMachine, 3, "path"));
		expectedOptions.add(new Option("location.ok", "", idMachine, 4, "path"));
		expectedOptions.add(new Option("location.ko", "", idMachine, 5, "path"));
		expectedOptions.add(new Option("network.proxy", "", idMachine, 6, "proxy"));
		expectedOptions.add(new Option("location.mask", "%albumartist%/%album%/%track% %title%", idMachine, 7, "mask"));
		expectedOptions.add(new Option("log.level", "INFO", idMachine, 8, "list"));
		expectedOptions.add(new Option("log.limit", "5242880", idMachine, 9, "integer"));
		expectedOptions.add(new Option("log.count", "20", idMachine, 10, "integer"));
		expectedOptions.add(new Option("files.audio", "mp3,flac", idMachine, 11, "csv"));
		expectedOptions.add(new Option("files.image", "", idMachine, 12, "csv"));
		expectedOptions.add(new Option("files.convert", "", idMachine, 13, "csv"));
		expectedOptions.add(new Option("files.delete", "", idMachine, 14, "csv"));
		expectedOptions.add(new Option("location.manual", "", idMachine, 15, "path"));
		expectedOptions.add(new Option("location.transcoded", "", idMachine, 16, "csv"));
		expectedOptions.add(new Option("files.image.delete", "false", idMachine, 17, "csv"));
		
		checkOptionList(machineName, expectedOptions);

		//Set description
		String description = "Waouh the great description!";
		assertTrue(Jamuz.getDb().machine().lock().update(idMachine, description));
		zText = new StringBuilder();
		assertTrue("isMachine updated", Jamuz.getDb().machine().lock().getOrInsert(machineName, zText, false));
		assertEquals(description, zText.toString());
		defaultListModel = new DefaultListModel();
		Jamuz.getDb().listModel().getMachineListModel(defaultListModel);
		element = (ListElement) defaultListModel.get(0);
		assertEquals(2, defaultListModel.size()); //The created one and current machine
		assertEquals("<html><b>" + machineName + "</b><BR/><i>" + description + "</i></html>", element.toString());
		assertEquals(machineName, element.getValue());
		assertNull(element.getFile());

		//Set options (one by one)
		int i = 0;
		String newValue;
		for (Option expectedOption : expectedOptions) {
			newValue = "New value " + i;
			Jamuz.getDb().option().lock().update(expectedOption, newValue);
			if (expectedOption.getType().equals("path")) {   //NOI18N
				newValue = FilenameUtils.normalizeNoEndSeparator(newValue.trim()) + File.separator;
			}
			expectedOption.setValue(newValue);
		}
		checkOptionList(machineName, expectedOptions);

		//Set options
		i = 0;
		for (Option expectedOption : expectedOptions) {
			newValue = "New New value " + (i + 10);
			expectedOption.setValue(newValue);
		}
		Machine machine = new Machine(machineName);
		machine.setOptions(expectedOptions);
		assertTrue(Jamuz.getDb().option().lock().update(machine));
		for (Option expectedOption : expectedOptions) {
			if (expectedOption.getType().equals("path")) {   //NOI18N
				expectedOption.setValue(FilenameUtils.normalizeNoEndSeparator(expectedOption.getValue().trim()) + File.separator);
			}
		}
		checkOptionList(machineName, expectedOptions);

		//Delete machine 
		assertTrue(Jamuz.getDb().machine().lock().delete(machineName));
		defaultListModel = new DefaultListModel();
		Jamuz.getDb().listModel().getMachineListModel(defaultListModel);
		assertEquals(1, defaultListModel.size()); //Only current machine left
		element = (ListElement) defaultListModel.get(0);
		assertNull(element.getFile());
		assertNotSame(machineName, element.getValue());

		//FIXME TEST Negative cases
		//FIXME TEST Check other constraints
	}

	private void checkOptionList(String machineName, ArrayList<Option> expectedOptions) {
		ArrayList<Option> options = new ArrayList<>();
		assertTrue("getOptions", Jamuz.getDb().option().get(options, machineName));
		Option actualOption;
		int i = 0;
		for (Option expectedOption : expectedOptions) {
			actualOption = options.get(i);
			i++;
			assertEquals(expectedOption.getComment(), actualOption.getComment());
			assertEquals(expectedOption.getIdOptionType(), actualOption.getIdOptionType());
			assertEquals(expectedOption.getId(), actualOption.getId());
			assertEquals(expectedOption.getIdMachine(), actualOption.getIdMachine());
			assertEquals(expectedOption.getType(), actualOption.getType());
			assertEquals(expectedOption.getValue(), actualOption.getValue());
			assertEquals(expectedOption.toString(), actualOption.toString());
		}
	}
	
	/**
	 * Test of getOrInsert method, of class DaoMachine.
	 */
	@Test
	@Ignore // Refer to testMachineAndOption() above
	public void testGetOrInsert() {
		System.out.println("getOrInsert");
		String hostname = "";
		StringBuilder description = null;
		boolean hidden = false;
		DaoMachine instance = null;
		boolean expResult = false;
		boolean result = instance.lock().getOrInsert(hostname, description, hidden);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of update method, of class DaoMachine.
	 */
	@Test
	@Ignore // Refer to testMachineAndOption() above
	public void testUpdate() {
		System.out.println("update");
		int idMachine = 0;
		String description = "";
		DaoMachine instance = null;
		boolean expResult = false;
		boolean result = instance.lock().update(idMachine, description);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}

	/**
	 * Test of delete method, of class DaoMachine.
	 */
	@Test
	@Ignore // Refer to testMachineAndOption() above
	public void testDelete() {
		System.out.println("delete");
		String machineName = "";
		DaoMachine instance = null;
		boolean expResult = false;
		boolean result = instance.lock().delete(machineName);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	
}
