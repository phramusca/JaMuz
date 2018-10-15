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

import de.umass.lastfm.Album;
import jamuz.Jamuz;
import jamuz.gui.swing.ProgressBar;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.musicbrainz.MBWS2Exception;
import org.musicbrainz.controller.Release;
import org.musicbrainz.model.entity.ReleaseWs2;
import jamuz.utils.DateTime;
import org.musicbrainz.model.RatingsWs2;

/**
 * Match release class. Can be Last.fm, MusicBrainz or Original
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ReleaseMatch implements java.lang.Comparable {
	
	private String source;
	private static final String MUSICBRAINZ="MusicBrainz";  //NOI18N
	private static final String LASTFM="Last.fm";  //NOI18N
	private int score;
	private String artist;
	private String album;
	private String format;
	private int trackTotal;
	private int discNb=1;
	private int discTotal=1;
	private String countryId;
	private String textLanguage;
	private String quality;
	private String year;
	private String id;
//	private List<Relation> relations; //NOT usefull for now
	private List<Track> tracks;
	private boolean isLookup=false; //Specify if a lookup has been performed (to get tracks)
	private boolean isDiscPart=false;
	private boolean isOriginal=false;
	private List <DuplicateInfo> duplicates;
	private boolean isWarningDuplicate=false;
	private boolean isErrorDuplicate=false;
	private int idPath=-1;
	
    //TODO: Make a "ReleaseTheAudioDb" (See TheMovieDb class & website http://www.theaudiodb.com/)
    // => NEED TO FIND A JAVA LIB FIRST !
	private ReleaseMatch(String artist, String album, String year, int idPath) {
		this.artist = artist;
		this.album = album;
		this.year = year;
		this.tracks = new ArrayList<>();
		this.idPath = idPath;
	}
	
	/**
	 *
	 * @param mbId
	 * @throws MBWS2Exception
	 */
	public ReleaseMatch(String mbId) throws MBWS2Exception {
        this.id = mbId;
        //Read album info from MusicBrainz
        Release release = new Release();
        ReleaseWs2 releaseWs2= release.lookUp(mbId);
        this.artist = releaseWs2.getArtistCreditString();
		this.album = releaseWs2.getTitle();
		this.year = releaseWs2.getYear();
        this.score = -1;
        RatingsWs2 ratingsWs2 = releaseWs2.getUserRating();
		this.source=MUSICBRAINZ;
        this.tracks = new ArrayList<>();
    }
    
	/**
	 * Used for MusicBrainz only
	 * 
	 * @param releaseWs2
	 * @param score
	 * @param discTotal  
	 * @param idPath  
	 */
	public ReleaseMatch(ReleaseWs2 releaseWs2, int score, int discTotal, int idPath) {

		this(releaseWs2.getArtistCreditString(), releaseWs2.getTitle(), releaseWs2.getYear(), idPath);
		this.trackTotal = releaseWs2.getTracksCount();
		
		//Specific to MusicBrainz:
		String[] mbidStr = releaseWs2.getIdUri().split("/");  //NOI18N
		this.format = releaseWs2.getFormat();
		this.discTotal = discTotal;

		try {
			Locale l = new Locale(releaseWs2.getTextLanguage(), releaseWs2.getCountryId());
			this.countryId = l.getDisplayCountry();
			this.textLanguage = l.getDisplayLanguage();
		}
		catch(java.lang.NullPointerException ex) {
			this.countryId = releaseWs2.getCountryId() != null ? releaseWs2.getCountryId() : "";
            this.textLanguage = releaseWs2.getTextLanguage() != null ? releaseWs2.getTextLanguage() : "";
		}
		
		this.quality = releaseWs2.getQualityStr();
		this.id = mbidStr[mbidStr.length-1];
//		this.relations = new ArrayList<Relation>();
		this.score = score;
		this.source=MUSICBRAINZ;
	}
	
	/**
	 * Used for MusicBrainz only
	 * @param releaseWs2
	 * @param score
	 * @param discNb
	 * @param discTotal
	 * @param trackTotal
	 * @param format
	 * @param idPath
	 */
	public ReleaseMatch(ReleaseWs2 releaseWs2, int score, int discNb, 
			int discTotal, int trackTotal, String format, int idPath) {
		this(releaseWs2,  score, discTotal, idPath);
		this.discNb = discNb;
		this.trackTotal = trackTotal;
		this.format = format;
		this.isDiscPart = true;
	}

	/**
	 * Used for Last.fm only
	 * @param score 
	 * @param album
	 * @param idPath
	 */
	public ReleaseMatch(Album album, int score, int idPath) {
		
		this(album.getArtist(), album.getName(), "", idPath);  //NOI18N
		
		String myYear;
		if(album.getReleaseDate()!=null) {
			myYear=DateTime.formatUTC(album.getReleaseDate(), "yyyy", false);  //NOI18N
		}
		else {
			myYear="";  //NOI18N
		}
		
		this.year = myYear;
		this.id = album.getMbid();
		this.source=LASTFM;
		this.score = score;
	}
	
	/**
	 * Used to add originals
	 * @param score 
	 * @param source
	 * @param artist
	 * @param album
	 * @param year
	 * @param trackTotal  
	 * @param idPath  
	 */
	public ReleaseMatch(int score, String source, String artist, String album, String year, 
			int trackTotal, int idPath) {
		
		this(artist, album, year, idPath);
		
		this.score = score; //-1 for originals;
		if(score<0) {
			this.isOriginal = true;
		}
		
		this.source = source;
		this.trackTotal = trackTotal;
	}

	private boolean checkExactAlbum() {
		ArrayList<DuplicateInfo> myList = new ArrayList<>();
		Jamuz.getDb().checkAlbumExact(myList, album, idPath);
		if(myList.size()>0) {
			this.duplicates.addAll(myList);
			return true;
		}
		return false;
	}
	
	private boolean checkSimilarAlbum() {
		ArrayList<DuplicateInfo> myList = new ArrayList<>();
		Jamuz.getDb().checkAlbumSimilar(myList, album, idPath);
		if(myList.size()>0) {
			duplicates.addAll(myList);
			return true;
		}
		return false;
	}
	
	private boolean checkDuplicateMbId() {
		ArrayList<DuplicateInfo> myList = new ArrayList<>();
		Jamuz.getDb().checkAlbumDuplicate(myList, id);
		if(myList.size()>0) {
			duplicates.addAll(myList);
			return true;
		}
		return false;
	}
	
	private boolean checkDuplicateAlbumArtistNumber() {
		ArrayList<DuplicateInfo> myList = new ArrayList<>();
		Jamuz.getDb().checkAlbumDuplicate(myList, artist, album, idPath, discNb, discTotal);
		if(myList.size()>0) {
			duplicates.addAll(myList);
			return true;
		}
		return false;
	}
	
	private boolean checkSimilarAlbumArtist() {
		ArrayList<DuplicateInfo> myList = new ArrayList<>();
		Jamuz.getDb().checkAlbumDuplicate(myList, artist, album, idPath);
		if(myList.size()>0) {
			duplicates.addAll(myList);
			return true;
		}
		return false;
	}
	
	/**
	 * Return list of duplicates
	 * @return
	 */
	public List<DuplicateInfo> getDuplicates() {
		if(duplicates==null) {
			//Search for duplicates
        
			//FIXME CHECK Add type in displayed combo (exact duplicate, ...) if color not enough. 
			//At least mention "duplicate found: "
			// or " no duplicates"

			//FIXME CHECK Support duplicate can be from a various folder (check that original is the complete album)
			//FIXME CHECK Support check duplicate status (OK, KO,...) and offer to replace if new is better
			//FIXME CHECK: List all duplicates discNo/discTotal (ex: [1-3], [2-3])

			//FIXME !! CHECK Not searched soon enough it seems since status is wrong until selected again
			
			duplicates = new ArrayList<>();
			if(		checkDuplicateMbId() 
					|| (isDiscPart && checkDuplicateAlbumArtistNumber())
					|| (!isDiscPart && checkSimilarAlbumArtist())) {
				isErrorDuplicate=true;
			} else if( 
					(isDiscPart && checkSimilarAlbumArtist())
					|| checkSimilarAlbum()
					|| checkExactAlbum()) {
				isWarningDuplicate=true;
			}
		}
		return duplicates;
	}

	/**
	 * Is this a warning ?
	 * @return
	 */
	public boolean isIsWarningDuplicate() {
		return isWarningDuplicate;
	}

	/**
	 * Is this an error ?
	 * @return
	 */
	public boolean isIsErrorDuplicate() {
		return isErrorDuplicate;
	}

    //TODO: Maybe add a connection timeout ? or an abort button ?
	/**
	 * Return list of tracks
     * @param progressBar
	 * @return  
	 */
	public List<Track> getTracks(ProgressBar progressBar) {
		//Only lookup if not already done
		if(!this.isLookup) {
			if(this.source.equals(ReleaseMatch.MUSICBRAINZ)) {
				//MusicBrainz
                ReleaseMB releaseMB = new ReleaseMB(progressBar);
                List<Track> MBtracks = releaseMB.lookup(
						this.getId(), 
						this.isDiscPart, 
						this.discNb, 
						this.discTotal);
                if(MBtracks!=null) {
                    this.tracks.addAll(MBtracks);
                    this.isLookup=true;
                }
				//TODO: Use the relations
				//		jTextArea1.append("\n-------------------------------\n"); 		
				//		jTextArea1.append(myMatchTracks.toString() + "\n");  		
				//		for (Relation relation : myMatchTracks.getRelations()) { 			
				//			jTextArea1.append("\t getDirection:" + relation.getDirection() + "\n"); 			
				//			jTextArea1.append("\t getTargetId:" + relation.getTargetId() + "\n"); 			
				//			jTextArea1.append("\t getTargetType:" + relation.getTargetType() + "\n"); 			
				//			jTextArea1.append("\t getType:" + relation.getType() + "\n"); 		
				//		} 		
				//		jTextArea1.append("\n"); 
			}
			else if(this.source.equals(ReleaseMatch.LASTFM)) {
				//Last.fm
                ReleaseLastFm releaseLastFm = new ReleaseLastFm();
                List<Track> lastFmTracks = releaseLastFm.lookup(this.getId());
                if(lastFmTracks!=null) {
                    this.tracks.addAll(lastFmTracks);
                    this.isLookup=true;
                }
			}
		}
		//No need to sort, already done
		return tracks;
	}

	/**
	 *
	 * @return
	 */
	public int getScore() {
        return score;
    }
	
	/**
	 * Format release match to string.
	 * @return
	 */
	@Override
	public String toString() {
		
		String text = source;
		if(this.score>=0) {
			text += " ["+this.score+"]";  //NOI18N
		}
		text += " : \""+this.album+"\" (\""+this.artist+"\")";  //NOI18N
		if(!this.year.equals("")) {  //NOI18N
			text += " ["+this.year+"]";  //NOI18N
		}
		if(trackTotal>0) {
			text += " {"+this.trackTotal+" tracks}";  //NOI18N
		}

		if(this.source.equals(MUSICBRAINZ)) {
			if(isDiscPart) {
				text += " [Disc "+ this.discNb + "/" + this.discTotal + "]";   //NOI18N //NOI18N //NOI18N
			}
			if(this.format != null && !this.format.isEmpty()) {
				text += " ("+this.format+")";  //NOI18N
			}
			if(this.countryId != null && !this.countryId.isEmpty()) {
				text += " ("+this.countryId+")";  //NOI18N
			}
			if(this.textLanguage != null && !this.textLanguage.isEmpty()) {
				text += " ("+this.textLanguage+")";  //NOI18N
			}
			if(this.quality != null && !this.quality.isEmpty()) {
				text += " (Quality="+this.quality+")";  //NOI18N
			}
		}
		
		String display;
		if(score<=0) { //That is for duplicates and originals
			display="<html><b><font color=\"#ff0000\">"+text+"</font></b></html>";  //NOI18N
		}
		else if(score==100) {
			display="<b><font color=\"#005F00\">"+text+"</font></b>";  //NOI18N
		}
		else if(score > 70) {
			display="<font color=\"#3F4700\">"+text+"</font>";  //NOI18N
		}
		else if(score > 50) {
			display="<font color=\"#9F2300\">"+text+"</font>";  //NOI18N
		}
		else {
			display="<font color=\"#ff0000\">"+text+"</font>";  //NOI18N
		}
		
		return display;
	}
	
	@Override
	public int compareTo(Object o) {
		//ORDER BY score DESC,  year ASC
        
        if (this.score < ((ReleaseMatch) o).score) return 1;
		if (this.score > ((ReleaseMatch) o).score) return -1;
        
        if (this.getYearInt() < ((ReleaseMatch) o).getYearInt()) return -1;
		if (this.getYearInt() > ((ReleaseMatch) o).getYearInt()) return 1;
        
		return 0;
	}

	/**
	 * Return artist
	 * @return
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * Return country ID
	 * @return
	 */
	public String getCountryId() {
		return countryId;
	}

	/**
	 * Return format
	 * @return
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Return ID
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Return quality
	 * @return
	 */
	public String getQuality() {
		return quality;
	}

	/**
	 * Return if this is an original
	 * @return
	 */
	public boolean isOriginal() {
		return isOriginal;
	}

	/**
	 * 
	 * @return
	 */
//	public List<Relation> getRelations() {
//		return relations;
//	}

	/**
	 * Return text language
	 * @return
	 */
	public String getTextLanguage() {
		return textLanguage;
	}

	/**
	 * Return track total
	 * @return
	 */
	public int getTrackTotal() {
		return trackTotal;
	}

	/**
	 * Return year
	 * @return
	 */
	public String getYear() {
		return year;
	}

    /**
     * get year as an integer (for comparison)
     * @return
     */
    public int getYearInt() {
        try {
            return Integer.parseInt(year);
        }
        catch (NumberFormatException ex) {
            return Integer.MAX_VALUE;
        }
    }
    
	/**
	 * Return album
	 * @return
	 */
	public String getAlbum() {
		return album;
	}

	/**
	 * Return disc total
	 * @return
	 */
	public int getDiscTotal() {
		return discTotal;
	}

	
	/**
	 * Relation class
	 */
	public class Relation {
		private final String direction;
		private final String targetId;
		private final String targetType;
		private final String type;
		
		/**
		 * Creates a relation instance
		 * @param direction
		 * @param targetId
		 * @param targetType
		 * @param type
		 */
		public Relation(String direction, String targetId, String targetType, String type) {
			this.direction = direction;
			this.targetId = targetId;
			this.targetType = targetType;
			this.type = type;
		}

		/**
		 * Return relation direction
		 * @return
		 */
		public String getDirection() {
			return direction;
		}

		/**
		 * Return relation target ID
		 * @return
		 */
		public String getTargetId() {
			return targetId;
		}

		/**
		 * Return relation target type
		 * @return
		 */
		public String getTargetType() {
			return targetType;
		}

		/**
		 * Return relation type
		 * @return
		 */
		public String getType() {
			return type;
		}
	}
	
	/**
	 * Track class
	 */
	public static class Track {
		private final int trackNo;
		private final int discNo;
		private final int trackTotal;
		private final int discTotal;
		private final String artist;
		private final String title;
		private final Long duration;
		private final String medium;
		
		/**
		 * Creates a new track instance
		 * @param discNo 
		 * @param discTotal 
		 * @param trackNo 
		 * @param artist
		 * @param title
		 * @param trackTotal 
		 * @param duration
		 * @param medium
		 */
		public Track(int discNo, int discTotal, int trackNo, int trackTotal, String artist, String title, Long duration, String medium) {
			this.trackNo = trackNo;
			this.artist = artist;
			this.title = title;
			this.duration = duration;
			this.medium = medium;
			this.discNo = discNo;
			this.trackTotal = trackTotal;
			this.discTotal = discTotal;
		}

		/**
		 * Return track number in "xx/yy" format
		 * @return
		 */
		public String getTrackNoFull() {
			return FolderInfoResult.formatNumber(this.trackNo)+"/"+FolderInfoResult.formatNumber(this.trackTotal);  //NOI18N
		}

		/**
		 * Return disc number in "xx/yy" format
		 * @return
		 */
		public String getDiscNoFull() {
			return FolderInfoResult.formatNumber(this.discNo)+"/"+FolderInfoResult.formatNumber(this.discTotal);  //NOI18N
		}

		/**
		 * Return dic number
		 * @return
		 */
		public int getDiscNo() {
			return discNo;
		}

		/**
		 * Return disc total
		 * @return
		 */
		public int getDiscTotal() {
			return discTotal;
		}

		/**
		 * Return track number
		 * @return
		 */
		public int getTrackNo() {
			return trackNo;
		}

		/**
		 * Return track total
		 * @return
		 */
		public int getTrackTotal() {
			return trackTotal;
		}
		
		/**
		 * Return artist
		 * @return
		 */
		public String getArtist() {
			return this.artist;
		}

		/**
		 * Return duration
		 * @return
		 */
		public Long getDuration() {
			return this.duration;
		}

		/**
		 * Return medium
		 * @return
		 */
		public String getMedium() {
			return this.medium;
		}

		/**
		 * Return title
		 * @return
		 */
		public String getTitle() {
			return this.title;
		}
	}

}