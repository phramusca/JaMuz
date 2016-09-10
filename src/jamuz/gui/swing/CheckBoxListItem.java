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

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class CheckBoxListItem {
	private final Object object;
	private boolean isSelected = false;

	/**
	 * Creates a CheckBoxListItem
	 * @param object
	 */
	public CheckBoxListItem(Object object)
	{
	   this.object = object;
	   this.isSelected=true;
	}

	/**
	 * Is checkbox item selected ?
	 * @return
	 */
	public boolean isSelected()
	{
	   return isSelected;
	}

	/**
	 * Set checkbox selected
	 * @param isSelected
	 */
	public void setSelected(boolean isSelected)
	{
	   this.isSelected = isSelected;
	}

	@Override
	public String toString()
	{
	   return object.toString();
	}

	/**
	 * Return object stored
	 * @return
	 */
	public Object getObject() {
		return object;
	}
}
