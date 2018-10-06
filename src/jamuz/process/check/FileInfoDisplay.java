/*
 * Copyright (C) 2011 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import jamuz.FileInfoInt;
import jamuz.process.check.ReleaseMatch.Track;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import jamuz.utils.Inter;

/**
 * Similar as FileInfo class but for display only
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public final class FileInfoDisplay extends FileInfoInt {

	/**
	 * Is this an audio file ?
	 */
	public boolean isAudioFile=false;

	/**
	 *
	 */
	public int index;
	
	//Tags
	/**
	 * Artist
	 */
	public TableValue artistDisplay=new TableValue("");  //NOI18N
	/**
	 * Album
	 */
	public TableValue albumDisplay=new TableValue("");  //NOI18N
	/**
	 * Album's Artist
	 */
	public TableValue albumArtistDisplay=new TableValue("");  //NOI18N
	/**
	 * Title
	 */
	public TableValue titleDisplay=new TableValue("");  //NOI18N
	/**
	 * Track # full formatDisplay (xx/yy)
	 */
	public TableValue trackNoFullDisplay=new TableValue("");  //NOI18N

	/**
	 * Track # full (NEW)
	 */
//	private String trackNoFull="";  //NOI18N
	/**
	 * Disc # full formatDisplay (xx/yy)
	 */
	public TableValue discNoFullDisplay=new TableValue("");  //NOI18N

	/**
	 * Disc # full (NEW)
	 */
//	private String discNoFull="";  //NOI18N
	/**
	 * Genre
	 */
	public TableValue genreDisplay=new TableValue("");  //NOI18N
	/**
	 * Year
	 */
	public TableValue yearDisplay=new TableValue("");  //NOI18N
	/**
	 * BPMDisplay
	 */
	public TableValue BPMDisplay=new TableValue("");  //NOI18N
	/**
	 * Comment
	 */
	public TableValue commentDisplay=new TableValue("");  //NOI18N
	//File information
	/**
	 * Bit rate
	 */
	public String bitRateDisplay="";  //NOI18N
	/**
	 * Format
	 */
	public String formatDisplay="";  //NOI18N

	/**
	 * Cover Icon
	 */
	public ImageIcon coverIconDisplay;
    
	/**
	 * Set track
	 * @param track
	 */
	public void setTrack(Track track) {
		this.discNo = track.getDiscNo();
		this.discTotal = track.getDiscTotal();
		this.trackNo = track.getTrackNo();
		this.trackTotal = track.getTrackTotal();
		this.artist=track.getArtist();
		this.title=track.getTitle();
	}
	
	/**
	 * Used for extra titles from match
	 * @param track
	 */
	public FileInfoDisplay(Track track) {
        super("", ""); //NOI18N
		this.discNo = track.getDiscNo();
		this.discTotal = track.getDiscTotal();
		this.trackNo = track.getTrackNo();
		this.trackTotal = track.getTrackTotal();
		//Do not put "/" (ex: "N/A"= in below String naNumber as discNoFull and trackNoFull are splitted by "/" 
		String naNumber = "-- --";  //NOI18N
		this.discNoFullDisplay=new TableValue(naNumber);
		this.trackNoFullDisplay=new TableValue(naNumber);
		String na = "<html><font color=\"red\">N/A</font></html>";  //NOI18N
		this.filename=na;
        this.BPM=0;
		this.BPMDisplay=new TableValue(na);
        this.album=na;
		this.albumDisplay=new TableValue(na);
        this.albumArtist=na;
		this.albumArtistDisplay=new TableValue(na);
        this.artist=na;
		this.artistDisplay=new TableValue(na);
		this.title=track.getTitle();
		this.titleDisplay=new TableValue(na);
        this.year=na;
		this.yearDisplay=new TableValue(na);
		this.genre=Inter.get("Label.SelectOne"); //NOI18N
		this.genreDisplay=new TableValue(Inter.get("Label.SelectOne")); //NOI18N
	}
    
	/**
	 * Used for non-audio files when there is no audio files in folder
	 * @param filename
	 */
	public FileInfoDisplay(String filename) {
        super("", ""); //NOI18N
		this.filename=filename;
	}
	
    /**
     * Used for original audio files list
     * @param relativeFullPath
     * @param rootPath
     */
    public FileInfoDisplay(String relativeFullPath, String rootPath) {
        super(relativeFullPath, rootPath);
    }

    /**
     *
     */
    public void initDisplay() {
        this.trackNoFullDisplay= new TableValue(this.getTrackNoFull());
		this.discNoFullDisplay=new TableValue(this.getDiscNoFull());
		this.commentDisplay=new TableValue(this.comment);
		this.yearDisplay=new TableValue(this.year);
		this.genreDisplay=new TableValue(this.genre);
		this.albumArtistDisplay = new TableValue(this.albumArtist);
		this.albumDisplay = new TableValue(this.album);
		this.artistDisplay=new TableValue(this.artist);
		this.titleDisplay=new TableValue(this.title);
		this.BPMDisplay=new TableValue(Float.toString(this.BPM));
    }
    
	/**
	 *
	 * @param trackNo
	 */
	public void setTrackNo(int trackNo) {
        this.trackNo = trackNo;
    }

	/**
	 *
	 * @param trackTotal
	 */
	public void setTrackTotal(int trackTotal) {
        this.trackTotal = trackTotal;
    }

	/**
	 *
	 * @param discNo
	 */
	public void setDiscNo(int discNo) {
        this.discNo = discNo;
	}

	/**
	 *
	 * @param discTotal
	 */
	public void setDiscTotal(int discTotal) {
        this.discTotal = discTotal;
	}
    
	/**
	 * Return length display
	 * @return
	 */
	public String getLengthDisplay() {
        return lengthDisplay;
    }

	/**
	 * Return size display
	 * @return
	 */
	public String getSizeDisplay() {
        return sizeDisplay;
    }

	/**
	 * Return filename
	 * @return
	 */
	@Override
	public String getFilename() {
        return filename;
    }

	/**
	 * Return comment
	 * @return
	 */
	@Override
	public String getComment() {
		return comment;
	}

	/**
	 * Return BPM
	 * @return
	 */
	@Override
	public float getBPM() {
		return BPM;
	}
	
	/**
	 * Set artist 
	 * @param artist
	 */
	public void setArtist(String artist) {
        this.artist = artist;
    }

	/**
	 * Set title
	 * @param title
	 */
	public void setTitle(String title) {
        this.title = title;
    }

	/**
	 * Set album
	 * @param album
	 */
	public void setAlbum(String album) {
		this.album = album;
	}

	/**
	 * Set Album Artist
	 * @param albumArtist
	 */
	public void setAlbumArtist(String albumArtist) {
		this.albumArtist = albumArtist;
	}

	/**
	 * Set year
	 * @param year
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * Set BPM
	 * @param BPM
	 */
	@Override
	public void setBPM(float BPM) {
		this.BPM = BPM;
	}

	/**
	 * Set comment
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Save tags to file
     * @param image
	 * @param deleteComment
	 * @return
	 */
	public boolean saveTags(BufferedImage image, boolean deleteComment) {
        if(this.isAudioFile) {
			
			String genreNew=this.genre;
			if(genreNew.equals(Inter.get("Label.SelectOne"))) {  //NOI18N
				genreNew="";   //NOI18N
			}
			
            return this.saveTags(this.artist, this.albumArtist, this.album, this.trackNo, this.trackTotal, this.discNo, this.discTotal, 
                    genreNew, this.year, image, deleteComment, this.comment, this.title, this.BPM, this.lyrics);  //NOI18N
        }
        return true;
	}

	/**
	 * Clone object instance
	 * @return
     * @throws java.lang.CloneNotSupportedException
	 */
	@Override
	public FileInfoDisplay clone() throws CloneNotSupportedException {
		return (FileInfoDisplay) super.clone();
	}
	
    //TODO: Move to a dedicated class file in swingExtensions
	/**
	 * Used for displaying items in table html formatted
	 * while keeping raw data value available
	 */
	public static class TableValue{

		private final String value;
		private String display;
		
		/**
		 * Create a new TableValue
		 * @param value
		 */
		public TableValue(String value) {
			this.value=value;
			this.display=value;
		}

		@Override
		public String toString() {
			return display;
		}

		/**
		 * Get value
		 * @return
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Set value to be displayed
		 * @param display
		 */
		public void setDisplay(String display) {
			this.display = display;
		}
	}
}
