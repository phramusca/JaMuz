/*
 * Copyright (C) 2015 phramusca ( https://github.com/phramusca/JaMuz/ )
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

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Keys extends Options {

	/**
	 *
	 * @param filename
	 */
	public Keys(String filename) {
		super();
		input = Keys.class.getResourceAsStream(filename);
    }
	
	/**
	 *
	 * @return
	 */
	@Override
	public boolean save() {
		//Not needed and unwanted
		return false;
	}
	
	/**
	 *
	 * @param key
	 * @param value
	 */
	@Override
	public void set(String key, String value) {
        //Not needed and unwanted
    }
}
