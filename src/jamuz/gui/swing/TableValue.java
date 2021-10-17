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
package jamuz.gui.swing;

/**
* Used for displaying items in table html formatted
* while keeping raw data value available
*/
public class TableValue {

	private final String value;
	private String display;

	/**
	 *
	 */
	public static String na = "<html><font color=\"red\">N/A</font></html>";

	/**
	 * Create a new TableValue
	 * @param value
	 */
	public TableValue(String value) {
		this.value=value;
		this.display=value;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return display;
	}

	/**
	 * Get value
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set value to be displayed
	 * @param display
	 */
	public void setDisplay(String display) {
		this.display = display;
	}
}
