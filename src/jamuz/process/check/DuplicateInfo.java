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
package jamuz.process.check;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DuplicateInfo {

    /**
     *
     * @param album
     * @param albumArtist
     * @param rating
     * @param checkedFlag
     * @param errorLevel
	 * @param discNo
	 * @param discTotal
     */
    public DuplicateInfo(
			String album, 
			String albumArtist, 
			double rating, 
			FolderInfo.CheckedFlag checkedFlag, 
			int errorLevel, 
			int discNo, 
			int discTotal) {
        this.album = album;
        this.albumArtist = albumArtist;
        this.rating = rating;
        this.checkedFlag = checkedFlag;
        this.errorLevel = errorLevel;
		this.discNo = discNo;
		this.discTotal = discTotal;
    }

    private String album;

    /**
     * Get the value of album
     *
     * @return the value of album
     */
    public String getAlbum() {
        return album;
    }

    /**
     * Set the value of album
     *
     * @param album new value of album
     */
    public void setAlbum(String album) {
        this.album = album;
    }

    private String albumArtist;

    /**
     * Get the value of albumArtist
     *
     * @return the value of albumArtist
     */
    public String getAlbumArtist() {
        return albumArtist;
    }

    /**
     * Set the value of albumArtist
     *
     * @param artist new value of albumArtist
     */
    public void setAlbumArtist(String artist) {
        this.albumArtist = artist;
    }

        private double rating;

    /**
     * Get the value of rating
     *
     * @return the value of rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * Set the value of rating
     *
     * @param rating new value of rating
     */
    public void setRating(double rating) {
        this.rating = rating;
    }


    private FolderInfo.CheckedFlag checkedFlag;

    /**
     * Get the value of checkedFlag
     *
     * @return the value of checkedFlag
     */
    public FolderInfo.CheckedFlag getCheckedFlag() {
        return checkedFlag;
    }

    /**
     * Set the value of checkedFlag
     *
     * @param checkedFlag new value of checkedFlag
     */
    public void setCheckedFlag(FolderInfo.CheckedFlag checkedFlag) {
        this.checkedFlag = checkedFlag;
    }
    
        private int errorLevel;

    /**
     * Get the value of errorLevel
     *
     * @return the value of errorLevel
     */
    public int getErrorLevel() {
        return errorLevel;
    }

    /**
     * Set the value of errorLevel
     *
     * @param errorLevel new value of errorLevel
     */
    public void setErrorLevel(int errorLevel) {
        this.errorLevel = errorLevel;
    }

	private int discNo;

	/**
	 * Get the value of discNo
	 *
	 * @return the value of discNo
	 */
	public int getDiscNo() {
		return discNo;
	}

	/**
	 * Set the value of discNo
	 *
	 * @param discNo new value of discNo
	 */
	public void setDiscNo(int discNo) {
		this.discNo = discNo;
	}

	private int discTotal;
	
	public int getDiscTotal() {
		return discTotal;
	}

	public void setDiscTotal(int discTotal) {
		this.discTotal = discTotal;
	}

    @Override
    public String toString() {
        return "<html><b>" + FolderInfoResult.colorField("\"" + album + "\" "+" ["+ discNo + "/" + discTotal + "] "
						+"(\"" + albumArtist + "\")",  //NOI18N
                        errorLevel, false) + "</b> ["+checkedFlag.toString()+"] ["+rating+"]</html>";
    }
}
