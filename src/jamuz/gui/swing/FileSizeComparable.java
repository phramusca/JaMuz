/*
 * Copyright (C) 2017 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import jamuz.utils.StringManager;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class FileSizeComparable implements Comparable {
	private final Long length;

        /**
         *
         * @param length
         */
        public FileSizeComparable(Long length) {
            this.length = length;
        }

        /**
         *
         * @return
         */
        public Long getLength() {
            return length;
        }

        @Override
        public String toString() {
            return StringManager.humanReadableByteCount(length, false);
        }

        @Override
        public int compareTo(Object o) {
            return this.length.compareTo(((FileSizeComparable) o).length);
        }
}
