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


//FIXME !!!!! Put back wait() !!! But check it, seems to prevent process cancellation
package jamuz.process.check;

import jamuz.process.check.Cover.CoverType;
import jamuz.Jamuz;
import jamuz.gui.swing.ProgressBar;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jamuz.process.check.ReleaseMatch.Track;
import java.util.logging.Level;
import org.apache.http.impl.client.DefaultHttpClient;
import org.musicbrainz.controller.Release;
import org.musicbrainz.model.MediumListWs2;
import org.musicbrainz.model.MediumWs2;
import org.musicbrainz.model.TrackListWs2;
import org.musicbrainz.model.TrackWs2;
import org.musicbrainz.model.entity.RecordingWs2;
import org.musicbrainz.model.entity.ReleaseWs2;
import org.musicbrainz.model.searchresult.ReleaseResultWs2;
import org.musicbrainz.webservice.impl.HttpClientWebServiceWs2;

/**
 * MusicBrainz release class
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ReleaseMB {
    private final ProgressBar progressBar;

	/**
	 *
	 * @param progressBar
	 */
	public ReleaseMB(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

	private List<Cover> covers;
    
	/**
	 * Search album
	 * @param artist
	 * @param album
	 * @param nbAudioFiles
	 * @param idPath
	 * @param discNo
	 * @param discTotal
	 * @return
	 */
	public List<ReleaseMatch> search(String artist, 
			String album, 
			int nbAudioFiles, 
			int idPath,
			int discNo,
			int discTotal) {
		List<ReleaseMatch> matches = new ArrayList<>();
		covers = new ArrayList<>();
		try {
			Release release = new Release();
			release.getSearchFilter().setLimit((long)10);

			//TODO: Create getProxy() in OptionsEnv.options that directly returns the Proxy
			DefaultHttpClient httpclient = Jamuz.getHttpClient();
			if(httpclient!=null) {
				release.setQueryWs(new HttpClientWebServiceWs2(httpclient));
			}
			
			//Search for album/artist
			artist = removeIllegal(artist);
			if(!album.equals("")) {  //NOI18N
				album = removeIllegal(album);
				release.search("'"+album+"' AND artist:'"+artist+"'"); //same as above ?  //NOI18N
			}
			else {
				release.search("artist:'"+artist+"'");  //NOI18N

			}
            List<ReleaseResultWs2> releaseResultsWs2List = release.getFirstSearchResultPage();
			for (ReleaseResultWs2 releaseResultWs2 : releaseResultsWs2List) {
				try {
					ReleaseWs2 releaseWs2;
					releaseWs2 = releaseResultWs2.getRelease();
					int score;
					//TODO: Order by medium (reel-to-reel, vinyl, k7, CD), BUT before or after year order ?
					ReleaseMatch myMatch;
					MediumListWs2 mediumListWs2 = releaseWs2.getMediumList();
					List<MediumWs2> mediums = mediumListWs2.getMedia();
					int discTotalMatch = mediums.size();

					if(discTotalMatch>1) {
						int discNb = 1;
						for(MediumWs2 medium : mediums) {
							score=releaseResultWs2.getScore();
							if(medium.getTracksCount()!=nbAudioFiles) {
								score=score-20;
							}
							if((discNb>0 && discTotal>0)
									&& !(discNb==discNo && discTotal==discTotalMatch)) {
								score=score-20;
							}
							myMatch = new ReleaseMatch(releaseWs2, 
									score, 
									discNb, 
									discTotalMatch, 
									medium.getTracksCount(), 
									medium.getFormat(), 
									idPath);
							discNb++;
							matches.add(myMatch);
						}
					}

					score=releaseResultWs2.getScore();
					if(releaseWs2.getTracksCount()!=nbAudioFiles) {
						score=score-20;
					}

					myMatch = new ReleaseMatch(releaseWs2, score, discTotalMatch, idPath);
					matches.add(myMatch);
					covers.add(new CoverMB(CoverType.MB, myMatch.getId(), myMatch.toString()));  //NOI18N
				}
				catch (Exception ex) {
					Jamuz.getLogger().log(Level.SEVERE, "", ex);
					//Get next releaseWs2, no need to exit yet
				}
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
    
	private String removeIllegal(String str) {
		//+ - && || ! ( ) { } [ ] ^ " ~ * ? : \
		//TODO: Escape above list of chars instead of removing them
		//http://tickets.musicbrainz.org/browse/MBS-3988?page=com.atlassian.jira.plugin.system.issuetabpanels%3Aall-tabpanel
		String pattern = "[\\\\/:\"*?|\\+\\-\\&\\!\\(\\)\\[\\]^{}~]+";  //NOI18N
        return str.replaceAll(pattern, " ").trim();  //NOI18N
	}
    
	/**
	 * Get list of tracks for given album
	 * @param mbId
	 * @param discPart
	 * @param discNb
	 * @param discTotal
	 * @return
	 */
	public List<Track> lookup(String mbId, boolean discPart, int discNb, int discTotal) {
		List<Track> tracks = new ArrayList<>();
        
		try {
            Release release = new Release();
			DefaultHttpClient httpclient = Jamuz.getHttpClient();
            if(httpclient!=null) {
                release.setQueryWs(new HttpClientWebServiceWs2(httpclient));
            }
			//TODO: Only include useful relations
//			release.getIncludes().setReleaseRelations(true);
//			release.getIncludes().setUrlRelations(true);
//			release.getIncludes().setReleaseGroupRelations(true);
//			release.getIncludes().setArtistRelations(true);
//			release.getIncludes().setLabelRelations(true);
//			release.getIncludes().setRecordingLevelRelations(true);
//			release.getIncludes().setRecordingRelations(true);
//			release.getIncludes().setWorkRelations(true);
//			release.getIncludes().setWorkLevelRelations(true);

			//TODO: Getting 403 Forbidden with musicbrainzws2-java-v.3.0.0 (though search works)
			//No such issue with musicbrainzws2-java_1.01r31_20120103.rar
			ReleaseWs2 releaseWs2Full= release.lookUp(mbId);
			//Get relations
//			RelationListWs2 relationListWs2 = releaseWs2Full.getRelationList();
//			List<RelationWs2> relationWs2List = relationListWs2.getRelations();
//			for (RelationWs2 relationWs2 : relationWs2List) {
//				myMatch.addRelation(relationWs2.getDirection(), relationWs2.getTargetId(), relationWs2.getTargetType(),relationWs2.getType());
//			}
				
//			RatingsWs2 ratingsWs2 = releaseWs2Full.getRating();
//			System.out.println("\t getRating():"+ratingsWs2.getAverageRating()+" ("+ratingsWs2.getVotesCount()+")");
//			List<TagWs2> tagWs2List = releaseWs2Full.getTags();
//			for (TagWs2 tagWs2 : tagWs2List) {
//				System.out.println("\t getTags():"+tagWs2.getName()+" ("+tagWs2.getCount()+")");
//			}
				
			//Get tracks
			MediumListWs2 mediumListWs2 = releaseWs2Full.getMediumList();
			List<TrackWs2> trackWs2List;
			
			if(discPart) {
				//Getting tracks for a given discNb
				List<MediumWs2> mediums = mediumListWs2.getMedia();
				MediumWs2 medium = mediums.get(discNb-1);
				TrackListWs2 trackListWs2 = medium.getTrackList();
				trackWs2List = trackListWs2.getTracks();
				
				addTracks(tracks, trackWs2List, discNb, discTotal);
			}
			else {
				//Getting tracks for all discs
				List<MediumWs2> mediums = mediumListWs2.getMedia();
				for(MediumWs2 medium : mediums) {
					TrackListWs2 trackListWs2 = medium.getTrackList();
					trackWs2List = trackListWs2.getTracks();
					addTracks(tracks, trackWs2List, discNb, discTotal);
					discNb++;
				}
			}
			return tracks;
		}
		catch (Exception ex) {
            Jamuz.getLogger().log(Level.SEVERE, "", ex);
			return null;
		}
    }

	private void addTracks(List<Track> tracks, List<TrackWs2> trackWs2List, int discNo, int discTotal) {
		for (TrackWs2 trackWs2 : trackWs2List) {
				RecordingWs2 recordingWs2 = trackWs2.getRecording();
				tracks.add(new Track(discNo, discTotal, trackWs2.getPosition(), trackWs2List.size(), recordingWs2.getArtistCreditString(), recordingWs2.getTitle(), recordingWs2.getDurationInMillis(), trackWs2.getMediumStr()));
				
                //TODO: Get tags
//				RatingsWs2 ratingsWs2Track = recordingWs2.getRating();
//				System.out.println("\t\tgetRating():"+ratingsWs2Track.getAverageRating()+" ("+ratingsWs2Track.getVotesCount()+")");
//				List<TagWs2> tagWs2ListTrack = recordingWs2.getTags();
//				for (TagWs2 tagWs2 : tagWs2ListTrack) {
//					System.out.println("\t\t\tgetTags():"+tagWs2.getName()+" ("+tagWs2.getCount()+")");
//				}
			}
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