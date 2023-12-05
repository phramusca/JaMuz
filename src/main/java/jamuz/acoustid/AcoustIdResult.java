/*
 * Copyright (C) 2020 raph
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
package jamuz.acoustid;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class AcoustIdResult {
	
	private String score;
	private final String artist;
	private final String title;

	/**
	 *
	 * @param artist
	 * @param title
	 */
	public AcoustIdResult(String artist, String title) {
		this.artist = artist;
		this.title = title;
	}

	/**
	 *
	 * @param score
	 */
	protected void setScore(String score) {
		this.score = score;
	}	

	/**
	 *
	 * @return
	 */
	public String getScore() {
		return score;
	}
		
	/**
	 *
	 * @return
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 *
	 * @return
	 */
	public String getTitle() {
		return title;
	}
}
