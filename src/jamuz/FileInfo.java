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

package jamuz;

import java.util.Date;
import java.util.Locale;
import org.apache.commons.io.FilenameUtils;
import jamuz.utils.DateTime;
import jamuz.utils.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Audio file information
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class FileInfo implements java.lang.Comparable, Cloneable {

	/**
	 * file ID in database
	 */
	protected int idFile;


	/**
	 *
	 * @return
	 */
	public int getIdFile() {
        return idFile;
    }

	/**
	 *
	 * @param idFile
	 */
	public void setIdFile(int idFile) {
        this.idFile = idFile;
    }
    
	/**
	 * path ID in database
	 */
	protected int idPath;

	/**
	 *
	 * @return
	 */
	public int getIdPath() {
        return idPath;
    }

	/**
	 *
	 * @param idPath
	 */
	public void setIdPath(int idPath) {
        this.idPath = idPath;
    }
    
	/**
	 * Last Played datetime
	 */
	protected Date lastPlayed;

	/**
	 *
	 * @return
	 */
	public Date getLastPlayed() {
        return lastPlayed;
    }

	/**
	 *
	 * @param lastPlayed
	 */
	public void setLastPlayed(Date lastPlayed) {
        this.lastPlayed = lastPlayed;
    }
    
    //then  update stat sources accordingly
	/**
	 * Added datetime
	 */
	protected Date addedDate;

	/**
	 *
	 * @return
	 */
	public Date getAddedDate() {
        return addedDate;
    }

	/**
	 *
	 * @param addedDate
	 */
	public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }
    
	/**
	 * Relative full path (path + filename)
	 */
	protected String relativeFullPath;

	/**
	 *
	 * @param relativeFullPath
	 */
	public void setRelativeFullPath(String relativeFullPath) {
        this.relativeFullPath = relativeFullPath;
    }
    
	/**
	 * Relative path (without filename)
	 */
	protected String relativePath;

	/**
	 *
	 * @return
	 */
	public String getRelativePath() {
        return relativePath;
    }

	/**
	 *
	 * @param relativePath
	 */
	public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }
    
	/**
	 * File extension (lowercase)
	 */
	protected String ext;

	/**
	 *
	 * @return
	 */
	public String getExt() {
        return ext;
    }
    
	/**
	 * File name
	 */
	protected String filename;

	/**
	 *
	 * @return
	 */
	public String getFilename() {
        return filename;
    }

	/**
	 *
	 * @param filename
	 */
	public void setFilename(String filename) {
        this.filename = filename;
    }
    
	/**
	 * rating (0 to 5)
	 */
	protected int rating;

	/**
	 *
	 * @return
	 */
	public int getRating() {
        return rating;
    }

	/**
	 *
	 * @param rating
	 */
	public void setRating(int rating) {
        this.rating = rating;
    }
    
	/**
	 *
	 * @return
	 */
	public String getFormattedRatingModifDate() {
        return DateTime.formatUTCtoSqlUTC(ratingModifDate);
    }

	
	/**
	 *
	 */
	protected boolean updateRatingModifDate=false;
	
	/**
	 *
	 */
	protected Date ratingModifDate;

	/**
	 *
	 * @return
	 */
	public Date getRatingModifDate() {
        return ratingModifDate;
    }

	public void setRatingModifDate(Date ratingModifDate) {
		this.ratingModifDate = ratingModifDate;
	}

	/**
	 *
	 * @param updateRatingModifDate
	 */
	public void setUpdateRatingModifDate(boolean updateRatingModifDate) {
        this.updateRatingModifDate = updateRatingModifDate;
    }
	
	/**
	 *
	 */
	protected Date tagsModifDate;

	/**
	 *
	 * @return
	 */
	public Date getTagsModifDate() {
        return tagsModifDate;
    }

	public void setTagsModifDate(Date tagsModifDate) {
		this.tagsModifDate = tagsModifDate;
	}

	/**
	 *
	 * @return
	 */
	public String getFormattedTagsModifDate() {
        return DateTime.formatUTCtoSqlUTC(tagsModifDate);
    }
	
	protected Date genreModifDate=new Date();
	
	public String getFormattedGenreModifDate() {
        return DateTime.formatUTCtoSqlUTC(genreModifDate);
    }

	public Date getGenreModifDate() {
		return genreModifDate;
	}

	public void setGenreModifDate(Date genreModifDate) {
		this.genreModifDate = genreModifDate;
	}
	
	protected boolean updateGenreModifDate=false;
	
	/**
	 *
	 * @param updateGenreModifDate
	 */
	public void setUpdateGenreModifDate(boolean updateGenreModifDate) {
        this.updateGenreModifDate = updateGenreModifDate;
    }
	
	/**
	 * play counter
	 */
	protected int playCounter;

    /**
     * play counter
     * @return
     */
    public int getPlayCounter() {
        return playCounter;
    }

	/**
	 *
	 * @param playCounter
	 */
	public void setPlayCounter(int playCounter) {
        this.playCounter = playCounter;
    }

	/**
	 * Previous play counter
	 */
	protected int previousPlayCounter;

	/**
	 *
	 * @return
	 */
	public int getPreviousPlayCounter() {
        return previousPlayCounter;
    }

	public void setPreviousPlayCounter(int previousPlayCounter) {
		this.previousPlayCounter = previousPlayCounter;
	}
  
    /**
	 * Song BPM
	 */
	protected float BPM=0;  //NOI18N

	/**
	 *
	 * @return
	 */
	public float getBPM() {
        return BPM;
    }

	/**
	 *
	 * @param BPM
	 */
	public void setBPM(float BPM) {
        this.BPM = BPM;
    }

	ArrayList<String> tags = null;
	
	public ArrayList<String> getTags() {
        return tags;
    }
	
	/**
	 *
	 * @return
	 */
	public ArrayList<String> readTags() {
		ArrayList<String> out = new ArrayList<>();
		if(idFile>=0) {
			 Jamuz.getDb().getTags(out, idFile);
		}
		return out;
    }
	
	public String getTagsToString() {
		StringBuilder sb = new StringBuilder();
		if(tags!=null) {
			for(String tag : tags)
			{
				sb.append(tag);
				sb.append(" ");
			}
		} else {
			sb.append("Empty or all identical");
		}
		return(sb.toString());
	}
	
	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	
    /**
	 * Source name for scan and merge (StatSource.name)
	 */
	protected String sourceName;

	/**
	 *
	 * @return
	 */
	public String getSourceName() {
        return sourceName;
    }

	/**
	 *
	 * @param sourceName
	 */
	public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
    
	/**
	 * Deleted flag
	 */
	protected boolean deleted;

	/**
	 *
	 * @return
	 */
	public boolean isDeleted() {
        return deleted;
    }

	/**
	 * Used for scan statistics purposes 
	 * (statistics info got from databases to be merged)
	 * @param idFile 
	 * @param relativeFullPath
	 * @param idPath 
	 * @param rating
	 * @param lastPlayed
	 * @param addedDate 
	 * @param playCounter
	 * @param sourceName
	 * @param previousPlayCounter  
     * @param bpm  
	 * @param genre  
     * @param ratingModifDate  
	 * @param tagsModifDate  
	 * @param genreModifDate  
	 */
	public FileInfo(int idFile, int idPath, String relativeFullPath, int rating, 
            String lastPlayed, String addedDate, int playCounter, 
			String sourceName, int previousPlayCounter, float bpm, String genre, 
			String ratingModifDate, String tagsModifDate, String genreModifDate) {
		
		this.idFile=idFile;
		this.idPath=idPath;
		this.BPM=bpm;
		this.rating = rating;
		this.genre = genre;
        this.ratingModifDate = DateTime.parseSqlUtc(ratingModifDate);
		this.tagsModifDate = DateTime.parseSqlUtc(tagsModifDate);
		this.genreModifDate = DateTime.parseSqlUtc(genreModifDate);
        this.updateRatingModifDate=false;
		this.updateGenreModifDate=false;
		this.lastPlayed = DateTime.parseSqlUtc(lastPlayed);
		this.addedDate = DateTime.parseSqlUtc(addedDate);
		this.playCounter = playCounter;
		this.previousPlayCounter = previousPlayCounter;
		this.sourceName = sourceName;
		
		setPath(relativeFullPath);
	}
	
	/**
	 * Used for scan statistics purposes 
	 * (default values for one-side only file)
	 * @param sourceName 
	 */
	public FileInfo(String sourceName) {
		this(-1, -1, "", 0, "1970-01-01 00:00:00", "1970-01-01 00:00:00", 
				0, sourceName, 0, 0, "", "", "", "");  //NOI18N
	}
	
	/**
	 *
	 * @param sourceName
	 * @param file
	 */
	public FileInfo(String sourceName, JSONObject file) {
		this(sourceName);
		relativeFullPath = (String) file.get("path");
		rating = (int) (long) file.get("rating");
		addedDate = getDate(file, "addedDate");
		lastPlayed = getDate(file, "lastPlayed");
		playCounter = (int) (long) file.get("playCounter");
		genre = (String) file.get("genre");

		JSONArray jsonTags = (JSONArray) file.get("tags");
		tags = new ArrayList<>();
		for(int i=0; i<jsonTags.size(); i++) {
			String tag = (String) jsonTags.get(i);
			tags.add(tag);
		}
	}

	/**
     * @param jsonObject the one including date to get
     * @param id id where to get date from file
     * @return Date from jsonObject
     */
    private Date getDate(JSONObject jsonObject, String id) {
        String dateStr = (String) jsonObject.get(id);
        return DateTime.parseSqlUtc(dateStr);
    }
	
	/**
	 * Sets the path and filename
	 * @param relativeFullPath
	 */
	public final void setPath(String relativeFullPath) {
		this.relativeFullPath = FilenameUtils.separatorsToSystem(relativeFullPath);
		this.relativePath=FilenameUtils.getFullPath(this.relativeFullPath);
		this.filename=FilenameUtils.getName(this.relativeFullPath);
		this.ext=FilenameUtils.getExtension(this.filename).toLowerCase(Locale.ENGLISH);
	}

	/**
	 * Get the relative full path
	 * @return
	 */
	public String getRelativeFullPath() {
		return relativeFullPath;
	}
	
	/**
	 * Returns last played date in "yyyy-MM-dd HH:mm:ss" format
	 * @return
	 */
	public String getFormattedLastPlayed() {
		return DateTime.formatUTCtoSqlUTC(this.lastPlayed);
	}
    
	/**
	 * Returns last played date in "yyyy-MM-dd HH:mm:ss" format,
	 * translated to local time.
	 * @return
	 */
	public String getLastPlayedLocalTime() {
        return DateTime.formatUTCtoSqlLocal(this.lastPlayed);
    }
	
	/**
	 * Returns added date in "yyyy-MM-dd HH:mm:ss" format
	 * @return
	 */
	public String getFormattedAddedDate() {
		return DateTime.formatUTCtoSqlUTC(this.addedDate);
	}
	
	/**
	 * Returns added date date in "yyyy-MM-dd HH:mm:ss" format,
	 * translated to local time.
	 * @return
	 */
	public String getAddedDateLocalTime() {
        return DateTime.formatUTCtoSqlLocal(this.addedDate);
    }

	/**
	 * Song genre
	 */
	protected String genre="";  //NOI18N
	
	/**
	 * Set genre
	 * @param genre
	 */
	public void setGenre(String genre) {
        this.genre = genre;
    }
	
	/**
	 * Called dynamically by group function in FolderInfo
	 * WARNING: It may exists no usage of that function (dynamic call)
	 * DO NOT REMOVE before having done a "Find in Projects..." !    
	 * @return
	 */
	public String getGenre() {
		return this.genre;
	}
	
	/**
	 * Overring method for sorting by relativeFullPath, for comparison during scan
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo(Object o) {
		return (this.relativeFullPath.compareTo(((FileInfo) o).relativeFullPath));
	}

	/**
	 * Compare file statistics during merge
	 * @param thatFileInfo
	 * @return
	 */
	public boolean equalsStats(FileInfo thatFileInfo) {
        if(this == thatFileInfo) {
            return true;
        }

        boolean isEqual = (this.rating == thatFileInfo.rating);
        isEqual &= (this.playCounter == thatFileInfo.playCounter);
        isEqual &= (Math.abs(this.BPM - thatFileInfo.BPM) < 0.000001);
        isEqual &= (this.addedDate == thatFileInfo.addedDate 
				|| (this.addedDate != null 
					&& this.addedDate.equals(thatFileInfo.addedDate)));
        isEqual &= (this.lastPlayed == thatFileInfo.lastPlayed 
				|| (this.lastPlayed != null 
					&& this.lastPlayed.equals(thatFileInfo.lastPlayed)));
		isEqual &= (Utils.equalLists(tags, thatFileInfo.tags));
		isEqual &= this.genre.equals(thatFileInfo.genre);
        return isEqual;
    }
    
	/**
	 * Overring method for removing duplicates
	 * @param obj 
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
            return true;
        }
		
		if (obj instanceof FileInfo) {
			FileInfo thatFileInfo = (FileInfo) obj;
            //FIXME LOW MERGE Compare idFile instead of relativeFullPath
			// !! FIRST be sure it is always available during merge on both sides !!
            return this.relativeFullPath.equals(thatFileInfo.relativeFullPath);
		}
		return false;
	}
	
	/**
	 * Overring method as overrided equals()
	 * @return
	 */
	@Override
	public int hashCode() {
		  int hash = 7;
		  hash = 31 * hash + 
				  (null == this.relativeFullPath ? 0 
				  : this.relativeFullPath.hashCode());
		  return hash;
	}

	/**
	 * Clone object instance
	 * @return
     * @throws java.lang.CloneNotSupportedException
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

    
	/**
	 * Return object isntance as string
	 * @return
	 */
	@Override
	public String toString() {
		return relativeFullPath + "\n"
				+ "rating=" + rating + "\n"
				+ "ratingModifDate=" + DateTime.formatUTCtoSqlUTC(ratingModifDate) + "\n"
				+ "genre=" + genre + "\n"
				+ "genreModifDate=" + DateTime.formatUTCtoSqlUTC(genreModifDate) + "\n"
				+ "lastPlayed=" + getFormattedLastPlayed() + "\n"
				+ "addedDate=" + getFormattedAddedDate() + "\n"
				+ "playCounter=" + playCounter + "\n"
				+ "previousPlayCounter=" + this.previousPlayCounter + "\n"
				+ "BPM=" + BPM + "\n"
				+ "tags=" + getTagsToString() + "\n"
				+ "tagsModifDate=" + DateTime.formatUTCtoSqlUTC(tagsModifDate)+"\n";
	}
	
	public Map toMap() {
		Map jsonAsMap = new HashMap();
		jsonAsMap.put("path", relativeFullPath);
		jsonAsMap.put("idFile", idFile);
		jsonAsMap.put("rating", rating);
		jsonAsMap.put("addedDate", getFormattedAddedDate());
		jsonAsMap.put("lastPlayed", getFormattedLastPlayed());
		jsonAsMap.put("playCounter", playCounter);
		jsonAsMap.put("genre", genre);

		JSONArray tagsAsMap = new JSONArray();
		readTags().stream().forEach((tag) -> {
			tagsAsMap.add(tag);
		});
		jsonAsMap.put("tags", tagsAsMap);
		jsonAsMap.put("size", -1);
		return jsonAsMap;
	}
	
	
}
