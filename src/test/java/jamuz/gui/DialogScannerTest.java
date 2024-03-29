/*
 * Copyright (C) 2018 phramusca ( https://github.com/phramusca/JaMuz/ )
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
package jamuz.gui;

import jamuz.process.check.ICallBackScanner;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DialogScannerTest {

	/**
	 * Main program.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		//%b - %z %y/%n.%a - %t.mp3

		List<String> paths = new ArrayList<>();
		paths.add("the Grey CAT - SnaKee 1865/001.toto - INTRO.mp3");
		paths.add("the Grey CAT - SnaKee 1865/toto - INTRO.mp3");

		jamuz.process.check.DialogScanner.main(new JDialog(), paths, new ICallBackScanner() {
			@Override
			public void completed(String pattern) {
				System.out.println(".completed(" + pattern + ")");
			}
		});
	}
}
