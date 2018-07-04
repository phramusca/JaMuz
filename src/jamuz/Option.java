/*
 * Copyright (C) 2012 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import jamuz.utils.Inter;

/**
 * Option class
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Option {

	private final String id;
	private String value;
	private final String comment;
	private final int idMachine;
	private final int idOptionType;
	private final String type;

	/**
	 * Creates a new Option
	 * @param id
	 * @param value
	 * @param idMachine
	 * @param idOptionType
	 * @param type
	 */
	public Option(String id, String value, int idMachine, 
			int idOptionType, String type) {
		this.id = id;
		this.value = value;
		this.comment = ""
				+ "<html>"
				+ "<b>"
				+Inter.get("Options.Title."+id)
				+"</b> : "
				+Inter.get("Options.Comment."+id)
				+"</html>";  //NOI18N
		this.idMachine=idMachine;
		this.idOptionType=idOptionType;
		this.type=type;
	}

	/**
	 * Return machine ID
	 * @return
	 */
	public int getIdMachine() {
		return idMachine;
	}

	/**
	 * Return Option type ID
	 * @return
	 */
	public int getIdOptionType() {
		return idOptionType;
	}

	/**
	 * Return comment
	 * @return
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * Get the value
	 *
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Return ID
	 *
	 * @return ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets value
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Return type
	 * @return
	 */
	public String getType() {
		return type;
	}

    @Override
    public String toString() {
        return id;
    }
}