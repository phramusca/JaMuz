/*
 * Copyright (C) 2016 phramusca ( https://github.com/phramusca/JaMuz/ )
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
package jamuz.process.video;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class VideoRating {
    private final int rating;
    private final String display;

    public VideoRating(int rating, String display) {
        this.rating = rating;
        this.display = display;
    }

    public int getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return display;
    }
}
