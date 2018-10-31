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
package check.code;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Result {
	public int lineNb;
	public String line;

	public Result(int lineNb, String line) {
		this.lineNb = lineNb;
		this.line = line;
	}

	@Override
	public String toString() {
		return lineNb + ": " + line;
	}
}
