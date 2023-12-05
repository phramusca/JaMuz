/*
 * Copyright (C) 2021 raph
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
package jamuz.process.check;

import jamuz.Jamuz;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import java.io.File;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class Location {

	private final String optionId;
	private final String value;

	/**
	 * get value
	 *
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 * create a new location
	 *
	 * @param optionId
	 */
	public Location(String optionId) {
		this.optionId = optionId;
		this.value = Jamuz.getMachine().getOptionValue(this.optionId);
	}

	/**
	 * check if location exist
	 *
	 * @return
	 */
	public boolean check() {
		File checked = new File(this.value);
		if (!checked.exists()) {
			Popup.warning(java.text.MessageFormat.format(Inter.get("Error.Check.SourcePathNotFound"), this.value, Inter.get("Options.Title." + this.optionId)));  //NOI18N
			return false;
		}
		return true;
	}
}
