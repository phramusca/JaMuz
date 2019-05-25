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
package jamuz.gui.swing;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.AbstractListModel;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 * @param <T>
 */
public class SortedListModel<T> extends AbstractListModel<T> {

    // Define a SortedSet
    SortedSet model;

	/**
	 *
	 */
	public SortedListModel() {
      // Create a TreeSet
        // Store it in SortedSet variable
        model = new TreeSet();
    }

    // ListModel methods
    @Override
    public int getSize() {
        // Return the model size
        return model.size();
    }

    @Override
    public T getElementAt(int index) {
        return (T) model.toArray()[index];
    }

    // Other methods

	/**
	 *
	 * @param element
	 * @return 
	 */
    public boolean add(Object element) {
        return model.add(element);
    }

	/**
	 *
	 * @param elements
	 */
	public void addAll(Object elements[]) {
        Collection c = Arrays.asList(elements);
        model.addAll(c);
    }

	public void fire() {
		fireContentsChanged(this, 0, getSize()-1);
	}
	
	/**
	 *
	 */
	public void clear() {
        model.clear();
    }

	/**
	 *
	 * @param element
	 * @return
	 */
	public boolean contains(Object element) {
        return model.contains(element);
    }

	/**
	 *
	 * @return
	 */
	public Object firstElement() {
        // Return the appropriate element
        return model.first();
    }

	/**
	 *
	 * @return
	 */
	public Iterator iterator() {
        return model.iterator();
    }

	/**
	 *
	 * @return
	 */
	public Object lastElement() {
        // Return the appropriate element
        return model.last();
    }

	/**
	 *
	 * @param element
	 * @return
	 */
	public boolean removeElement(Object element) {
        return model.remove(element);
    }

}
