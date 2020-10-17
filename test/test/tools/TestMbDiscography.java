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
package test.tools;

import jamuz.gui.swing.ProgressBar;
import jamuz.process.check.ReleaseMatch;
import java.util.List;

/**
 *
 * @author raph
 */
public class TestMbDiscography {
	/**
	 * Main program.
	 * @param args
	 */
	public static void main(String[] args) {
		ArtistMB artistMB = new ArtistMB(new ProgressBar());
		List<ReleaseMatch> search = artistMB.search("Tash Sultana");
		System.out.println("------------------------------");
		System.out.println("Type\tYear\tAlbum\tArtist\tScore\tStatus");
		search.forEach(m -> System.out.println(m.getFormat()+"\t"+m.getYear()+"\t"+m.getAlbum()+"\t"+m.getArtist()+"\t"+m.getScore()));	
	}
}
