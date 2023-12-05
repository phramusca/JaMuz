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
package jamuz.process.sync;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public interface ICallBackSync {

	/**
	 *
	 */
	public void refresh();

	/**
	 *
	 */
	public void enable();

	/**
	 *
	 * @param enable
	 */
	public void enableButton(boolean enable);

	/**
	 *
	 * @param file
	 * @param idIcon
	 */
	public void addRow(String file, int idIcon);

	/**
	 *
	 * @param file
	 * @param msg
	 */
	public void addRow(String file, String msg);
}
