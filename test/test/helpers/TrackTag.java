/*
 * Copyright (C) 2015 phramusca ( https://github.com/phramusca/JaMuz/ )
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

package test.helpers;

import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.process.check.FolderInfo;
import java.io.IOException;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import jamuz.utils.DateTime;
import jamuz.utils.FileSystem;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class TrackTag extends FileInfoInt {

	/**
	 *
	 */
	public String sourceFile;

	/**
	 *
	 * @param sourceFile
	 * @param relativePath
	 * @param filename
	 * @param BPM
	 * @param album
	 * @param albumArtist
	 * @param artist
	 * @param comment
	 * @param discNo
	 * @param discTotal
	 * @param genre
	 * @param nbCovers
	 * @param title
	 * @param trackNo
	 * @param trackTotal
	 * @param year
	 * @param playCounter
	 * @param rating
	 * @param addedDate
	 * @param lastPlayed
	 * @param deleted
	 * @param checkedFlag
	 * @param ignore
	 * @throws CannotReadException
	 * @throws IOException
	 * @throws TagException
	 * @throws ReadOnlyFileException
	 * @throws InvalidAudioFrameException
	 */
	public TrackTag(String sourceFile, String relativePath, String filename,  
            float BPM, String album, String albumArtist, String artist, String comment, int discNo, int discTotal, String genre, 
            int nbCovers, String title, int trackNo, int trackTotal, String year, int playCounter, int rating, String addedDate, 
            String lastPlayed, boolean deleted, FolderInfo.CheckedFlag checkedFlag, boolean ignore) throws CannotReadException, IOException, TagException, 
                ReadOnlyFileException, InvalidAudioFrameException {

        super(-1, -1, relativePath, filename, TrackSourceRepo.get(sourceFile).length, TrackSourceRepo.get(sourceFile).format, 
                TrackSourceRepo.get(sourceFile).bitRate, (int) TrackSourceRepo.get(sourceFile).size, BPM, album, albumArtist, artist, comment, 
                discNo, discTotal, genre, nbCovers, title, trackNo, trackTotal, year, playCounter, rating, addedDate, lastPlayed, 
                TrackSourceRepo.get(sourceFile).modifDate, deleted, "", checkedFlag, -1, 0, 0, "");

        oriAddedDate=addedDate;
        oriLastPlayed=lastPlayed;
        this.sourceFile = sourceFile;
        this.ignore = ignore;
    }

	/**
	 *
	 * @param option
	 * @throws CannotReadException
	 * @throws IOException
	 * @throws TagException
	 * @throws ReadOnlyFileException
	 * @throws InvalidAudioFrameException
	 */
	public void create(String option) throws CannotReadException, IOException, 
			TagException, ReadOnlyFileException, InvalidAudioFrameException {
        setRootPath(Jamuz.getMachine().getOptionValue(option));
        FileSystem.copyFile(TrackSourceRepo.get(sourceFile).getFile(), 
				getFullPath());
		saveTags(false);
    }
    
    private final String oriAddedDate;
    private final String oriLastPlayed;

	/**
	 *
	 */
	public boolean ignore = false;

    @Override
    public String getFormattedAddedDate() {
        if(oriAddedDate.equals("NOW")) {
            return DateTime.getCurrentUtcSql();
        }
        else {
            return super.getFormattedAddedDate();
        }
    }

    @Override
    public String getFormattedLastPlayed() {
        if(oriLastPlayed.equals("NOW")) {
            return DateTime.getCurrentUtcSql();
        }
        else {
            return super.getFormattedLastPlayed();
        }
    }
}
