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

import jamuz.gui.ArtistMB;
import jamuz.gui.swing.ProgressBar;
import jamuz.process.check.ReleaseMatch;
import java.util.List;
import org.musicbrainz.model.searchresult.ArtistResultWs2;

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
		List<ArtistResultWs2> artistResults = artistMB.search("10 Ft. Ganja Plant");
		if(artistResults.size()>0) {
			List<ReleaseMatch> releaseGroups = artistMB.getReleaseGroups(artistResults.get(0));	
			System.out.println("------------------------------");
			System.out.println("Type\tYear\tAlbum\tArtist\tScore\tStatus");
			releaseGroups.forEach(rg -> System.out.println(rg.getFormat()+"\t"+rg.getYear()+"\t"+rg.getAlbum()+"\t"+rg.getArtist()+"\t"+rg.getScore()));	
		} else {
			System.out.println("No arist match found :(");
		}
	}
}
