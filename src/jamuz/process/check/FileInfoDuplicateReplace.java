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
import jamuz.gui.swing.TableValue;

/**
 * Similar as FileInfo class but for display only
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public final class FileInfoDuplicateReplace extends FileInfoInt {

	/**
	 * Is this an audio file ?
	 */
	public boolean isAudioFile=false;

	private FileInfoInt fileInfoInt;
	
	/**
	 *
	 */
	public int index;

	public TableValue filenameDisplay=new TableValue("");
	
	/**
	 * Artist
	 */
	public TableValue artistDisplay=new TableValue("");  //NOI18N

	/**
	 * Title
	 */
	public TableValue titleDisplay=new TableValue("");  //NOI18N
	
	/**
	 * Track # full formatDisplay (xx/yy)
	 */
	public TableValue trackNoFullDisplay=new TableValue("");  //NOI18N

	/**
	 * Disc # full formatDisplay (xx/yy)
	 */
	public TableValue discNoFullDisplay=new TableValue("");  //NOI18N

	/**
	 * Used for extra titles from match
	 * @param fileInfoDisplay
	 */
	public FileInfoDuplicateReplace(FileInfoDisplay fileInfoDisplay) {
        super("", ""); //NOI18N
		this.fileInfoInt=fileInfoDisplay;
		discNoFullDisplay=new TableValue(fileInfoDisplay.getDiscNoFull());
		trackNoFullDisplay=new TableValue(fileInfoDisplay.getTrackNoFull());
		artistDisplay=new TableValue(fileInfoDisplay.getArtist());
		titleDisplay=new TableValue(fileInfoDisplay.getTitle());
		filenameDisplay=new TableValue(fileInfoDisplay.getFilename());
		this.rating=fileInfoDisplay.getRating();
		filename=TableValue.na;
        artist=TableValue.na;
		title=TableValue.na;
	}
    
	public void setFileInfoDuplicate(FileInfoDuplicateReplace fileInfoDuplicateReplace) {
		discNoFullDisplay=new TableValue(fileInfoDuplicateReplace.discNoFullDisplay.getValue());
		trackNoFullDisplay=new TableValue(fileInfoDuplicateReplace.trackNoFullDisplay.getValue());
		artistDisplay=new TableValue(fileInfoDuplicateReplace.artistDisplay.getValue());
		titleDisplay=new TableValue(fileInfoDuplicateReplace.titleDisplay.getValue());
		filenameDisplay=new TableValue(fileInfoDuplicateReplace.filenameDisplay.getValue());
		rating=fileInfoDuplicateReplace.getRating();
		fileInfoInt=fileInfoDuplicateReplace.fileInfoInt;
	}
	
	/**
	 * Set file info
	 * @param fileInfoInt
	 */
	public void setFileInfoDisplay(FileInfoInt fileInfoInt) {
		this.discNo = fileInfoInt.getDiscNo();
		this.discTotal = fileInfoInt.getDiscTotal();
		this.trackNo = fileInfoInt.getTrackNo();
		this.trackTotal = fileInfoInt.getTrackTotal();
		this.artist=fileInfoInt.getArtist();
		this.title=fileInfoInt.getTitle();
		this.filename=fileInfoInt.getFilename();
	}

	@Override
	public boolean updateInDb() {
		if(fileInfoInt!=null) {
			fileInfoInt.setFilename(filename);
			return fileInfoInt.updateInDb();
		}
		return false;
	}
	
	/**
	 * Clone object instance
	 * @return
     * @throws java.lang.CloneNotSupportedException
	 */
	@Override
	public FileInfoDuplicateReplace clone() throws CloneNotSupportedException {
		return (FileInfoDuplicateReplace) super.clone();
	}

}
