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

package jamuz.process.check;

//http://code.google.com/p/lastfm-java/downloads/list
import de.umass.lastfm.Album;
import de.umass.lastfm.Artist;
import de.umass.lastfm.ImageSize;
import jamuz.Options;
import jamuz.process.check.Cover.CoverType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import jamuz.process.check.ReleaseMatch.Track;

/**
 * Last.fm release class
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ReleaseLastFm {
	
	private final String lastFmKey = "";  //NOI18N //Obfuscate
	//FIXME: Use Options.readKey("LastFm") but not here as done too often, move to upper
	
	private List<ReleaseMatch> matches;
	private List<Cover> covers;
		
	/**
	 * Return list of covers
	 * @return
	 */
	public List<Cover> getCoverList() {
		return covers;
	}
	
	/**
	 * Search album
	 * @param artist
	 * @param album
	 * @param idPath
	 * @return
	 */
	public List<ReleaseMatch> search(String artist, String album, int idPath) {
		matches = new ArrayList<>();
		covers = new ArrayList<>();
		try {
			Collection<Album> albums = new ArrayList<>();
			if(album.equals("")) {  //NOI18N
				albums = Artist.getTopAlbums(artist, lastFmKey);
			}
			else {
                try {
                    
                    albums = Album.search(album, lastFmKey);
                } catch (NullPointerException ex) {
                    //TODO: Why those exception occur ???
                }
			}

			for (Album myAlbum : albums) {
				//Comparing in lowerCase as looks like last.fm is case insensitive
				if(myAlbum.getArtist().toLowerCase().equals(artist.toLowerCase()) && myAlbum.getName().toLowerCase().equals(album.toLowerCase())) {
					addToMatches(myAlbum, 100, idPath);

					//TODO: Check the titles (number and names) ...
					addURL(myAlbum, ImageSize.SMALL);
					addURL(myAlbum, ImageSize.MEDIUM);
					addURL(myAlbum, ImageSize.LARGE);
					addURL(myAlbum, ImageSize.LARGESQUARE);
					addURL(myAlbum, ImageSize.EXTRALARGE);
					addURL(myAlbum, ImageSize.HUGE);
					addURL(myAlbum, ImageSize.MEGA);
					addURL(myAlbum, ImageSize.ORIGINAL);

					//TODO: Exit if exact found. No need to search more
				}
				else if(myAlbum.getName().toLowerCase().equals(album.toLowerCase())) {
					addToMatches(myAlbum, 80, idPath);
				}
				else {
					addToMatches(myAlbum, 60, idPath);
				}
			}
		} catch (Exception ex) {
			//Sometimes LastFM bindings face a org.xml.sax.SAXParseException
			//Adding this so we can continue the analysis anyway
//			Popup.error("Search Last.FM", ex);
		}
		//ORDER BY score DESC
		Collections.sort(matches);
		
		return matches;
	}
	
	/**
	 * Return list of tracks of given album
	 * @param mbId
	 * @return
	 */
	public List<Track> lookup(String mbId) {
		
		try {
            List<Track> tracksOut = new ArrayList<>();
			if(!mbId.equals("")) {  //NOI18N
				Album myAlbum=Album.getInfo("", mbId, lastFmKey);  //NOI18N
				Collection<de.umass.lastfm.Track> tracks=myAlbum.getTracks();
				int i=1;
				for (de.umass.lastfm.Track track : tracks) {
					tracksOut.add(new Track(1, 1, i, tracks.size(), track.getArtist(), track.getName(), Long.valueOf(track.getDuration()), ""));  //NOI18N
					i++;
				}
			}
            return tracksOut;
		} 
		catch (Exception ex) {
			//Potential connection issues
			return null;
		}
	}
	
	private void addToMatches(Album album, int score, int idPath) {
		ReleaseMatch myMatch = new ReleaseMatch(album, score, idPath);
		matches.add(myMatch);
	}
		
	private void addURL(Album myAlbum, ImageSize size) {
		String url = myAlbum.getImageURL(size);
		//TODO: Some empty url are still listed, maybe need to check .equals("")
		if(url!=null) {
			if(!url.equals("null")) {  //NOI18N
				String display="Last.fm [100] ("+size+"): \""+myAlbum.getName()+"\" (\""+myAlbum.getArtist()+"\")"; //NOI18N
				covers.add(new Cover(CoverType.URL, url, display));  //NOI18N
			}
		}
	}
	
	//	private boolean checkLastFmArtist(String myArtistStr, String lastFmKey) {
//		if(!myArtistStr.equals("")) {
//			//Check myArtistStr on last.fm
//			Artist myArtist = Artist.getCorrection(myArtistStr, lastFmKey);
//			if(myArtist!=null) {
//				if(!myArtistStr.equals(myArtist.getName())) {
//					artist=myArtist.getName();
////					this.addToList(guessArtistList, 1, "Correction", artist);
//					this.addInfo(myArtist.getName());
//					return false;
//				}
//				else {
//					artist=myArtistStr;
////					this.addToList(guessArtistList, 2, "Valid", artist);
//					this.addOK("Last.fm: Artist \""+myArtistStr+"\" is valid.");
//					return true;
//				}
//			}
//			else {
//				this.addKO("Last.fm: Artist \""+myArtistStr+"\" NOT found");
//				//A correction might not be available but maybe (is this true ?) a search will return some possibilities
////				Collection<Artist> artists = new ArrayList<Artist>();
//				Collection<Artist> artists = Artist.search(myArtistStr, lastFmKey);
//				if(artists.size()>0) {
//					this.addInfo("Last.fm: Some possible artist alternative(s): ");
//					for (Artist myArtistSearch : artists) {
////						this.addToList(guessArtistList, 3, "Alternative", myArtistSearch.getName());
//						this.addInfo(myArtistSearch.getName());
//					}
//				}
//				artist=myArtistStr;
//				return false;
//			}
//		}
//		return false;
//	}
}

