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

// http://code.google.com/p/musicbrainzws2-java/wiki/TableOfContents

package jamuz.gui;

import jamuz.process.check.*;
import jamuz.Jamuz;
import jamuz.gui.swing.ProgressBar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import org.apache.http.impl.client.DefaultHttpClient;
import org.musicbrainz.controller.Artist;
import org.musicbrainz.model.entity.ArtistWs2;
import org.musicbrainz.model.entity.ReleaseGroupWs2;
import org.musicbrainz.model.searchresult.ArtistResultWs2;
import org.musicbrainz.webservice.impl.HttpClientWebServiceWs2;

/**
 * MusicBrainz release class
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ArtistMB {
    private final ProgressBar progressBar;

	/**
	 *
	 * @param progressBar
	 */
	public ArtistMB(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

	private List<Cover> covers;
    
	/**
	 * Search artist albums
	 * @param artist
	 * @return
	 */
	public List<ArtistResultWs2> search(String artist) {
		artist = ReleaseMB.removeIllegal(artist);
		List<ArtistResultWs2> matches = new ArrayList<>();
		covers = new ArrayList<>();
		try {
			Artist artistController=getArtistController();
			artistController.search(artist);
			List<ArtistResultWs2> firstSearchResultPage = artistController.getFirstSearchResultPage();
			for (ArtistResultWs2 artistResult : firstSearchResultPage) {
				try {
					matches.add(artistResult);
				}
				catch (Exception ex) {
					Jamuz.getLogger().log(Level.SEVERE, "", ex);
					//Get next releaseWs2, no need to exit yet
				}
			}
			
//			//ORDER BY score DESC
//			Collections.sort(matches);
            
            return matches;
		}
		catch (Exception ex) {
			Jamuz.getLogger().log(Level.SEVERE, "", ex);
			return null;
		}
	}
	
		/**
	 * Search artist albums
	 * @param artistResult
	 * @return
	 */
	public List<ReleaseMatch> getReleaseGroups(ArtistResultWs2 artistResult) {
		List<ReleaseMatch> matches = new ArrayList<>();
		covers = new ArrayList<>();
		try {
			try {
					ArtistWs2 artist1 = artistResult.getArtist();
					Artist artistController=getArtistController();
					artistController.lookUp(artist1.getId());
					List<ReleaseGroupWs2> fullReleaseGroupList = artistController.getFullReleaseGroupList();
					if(fullReleaseGroupList!=null) {
						for (ReleaseGroupWs2 releaseWs2 : fullReleaseGroupList) {
							try {
								int score=artistResult.getScore();
								ReleaseMatch myMatch;
								myMatch = new ReleaseMatch(releaseWs2, score);
								matches.add(myMatch);
								covers.add(new CoverMB(Cover.CoverType.MB, myMatch.getId(), myMatch.toString()));  //NOI18N
							}
							catch (Exception ex) {
								Jamuz.getLogger().log(Level.SEVERE, "", ex);
								//Get next releaseWs2, no need to exit yet
							}
						}
					}
				}
				catch (Exception ex) {
					Jamuz.getLogger().log(Level.SEVERE, "", ex);
					//Get next releaseWs2, no need to exit yet
				}
			
			//ORDER BY score DESC
			Collections.sort(matches);
            
            return matches;
		}
		catch (Exception ex) {
			Jamuz.getLogger().log(Level.SEVERE, "", ex);
			return null;
		}
	}
    
	private Artist getArtistController() {
		Artist artistController =new Artist();
		artistController.getSearchFilter().setLimit((long)10);
		//TODO: Create getProxy() in OptionsEnv.options that directly returns the Proxy
		DefaultHttpClient httpclient = Jamuz.getHttpClient();
		if(httpclient!=null) {
			artistController.setQueryWs(new HttpClientWebServiceWs2(httpclient));
		}
		return artistController;
	}
	
	/**
	 * Return list of covers
	 * @return
	 */
	public List<Cover> getCoverList() {
		Collections.sort(covers);
		return covers;
	}
}