/*
 * Copyright (C) 2014 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import jamuz.FileInfoInt;
import jamuz.process.check.FileInfoDisplay;
import java.util.Objects;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */

public class ListElement implements Cloneable{
    private final String value;
    private String display;
    private FileInfoInt file;

    /**
     * Create a new list element
     * @param value
	 * @param display
     */
    public ListElement(String value, String display) {
        this.value=value;
		this.display=display;
    }
    
    /**
     * Create a new list element
     * @param value
     * @param file
     */
    public ListElement(String value, FileInfoInt file) {
        this(value, value);
        this.file=file;
    }

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

    /**
     * Return file
     * @return
     */
    public FileInfoInt getFile() {
        return file;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
		
		if (obj instanceof ListElement) {
			ListElement thatAlbum = (ListElement) obj;
            //TODO: Compare idFile instead (first be sure it is always available during merge on both sides)
            return this.value.equals(thatAlbum.value);
		}
		return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.value);
        return hash;
    }
  
    @Override
	public ListElement clone() throws CloneNotSupportedException {
		return (ListElement) super.clone();
	}
    
}
