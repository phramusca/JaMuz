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
package test.helpers;

import test.helpers.AlbumBuffer;
import jamuz.process.check.FolderInfoResult;
import jamuz.utils.*;
import jamuz.utils.LogText;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

/**
 * Analyse all albums checked in:
 *  - CheckNTest
 *  - Merge1Test
 *  - MergeNTest
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class CompareOO {
	
	
    /**
	 * Main program.
	 * @param args
	 */
	public static void main(String[] args) {

		compareMerge1Test();
		
//		List<String> mbIds = new ArrayList<>();
//        mbIds.add("9e097b10-8160-491e-a310-e26e54a86a10");
//        mbIds.add("9dc7fe6a-3fa4-4461-8975-ecb7218b39a3");
//        mbIds.add("c212b71b-848c-491c-8ae7-b62a993ae194");
//        mbIds.add("8cfbb741-bd63-449f-9e48-4d234264c8d5");
//        mbIds.add("be04bc1f-fc63-48f5-b1ca-2723f17d241d");
//        mbIds.add("6cc35892-c44f-4aa7-bfee-5f63eca70821");
//        mbIds.add("7598d527-bc8d-4282-a72c-874f335d05ac");
//        mbIds.add("13ca98f6-1a9f-4d76-a3b3-a72a16d91916");
//		compareMergeNTest(mbIds);
		
    } 
	
	private static void compareMerge1Test() {
		
		List<String> versions = new ArrayList<>();
		
		versions.add("MergeTest1_Creation");
		versions.add("MergeTest2_DB");
		versions.add("MergeTest3");
		versions.add("MergeTest4_1");
		versions.add("MergeTest4_New");
		versions.add("MergeTest4_JaMuz");
		versions.add("MergeTest4_New");
		
		
		for(int i=0; i<(versions.size()-1); i++) {
			String firstTab = versions.get(i);
			String secondTab = versions.get(i+1);
			compareAlbums(
				"9e097b10-8160-491e-a310-e26e54a86a10",
				firstTab,
				secondTab, "Merge1Test");
		}
	}
	
	private static void compareMergeNTest(List<String> mbIds) {
		
		String testName = "MergeNTest";
		
		List<String> versions = new ArrayList<>();
		
//		versions.add("MergeDevice1_KO");
//		versions.add("MergeDevice2_DB");
//		versions.add("MergeDevice3_JaMuz");
//		versions.add("MergeDevice4_1stMerge");
		

//		versions.add("MergeDevice4_1stMerge");
//		versions.add("MergeDevice5_1");
//		versions.add("MergeDevice4_1stMerge");
//		versions.add("MergeDevice5_2");
//		versions.add("MergeDevice4_1stMerge");
//		versions.add("MergeDevice5_3");
//		versions.add("MergeDevice4_1stMerge");
//		versions.add("MergeDevice5_4");
//		versions.add("MergeDevice4_1stMerge");
//		versions.add("MergeDevice5_5");
//		versions.add("MergeDevice4_1stMerge");
//		versions.add("MergeDevice5_JaMuz");


//		versions.add("MergeDevice5_1");
//		versions.add("MergeDevice6_New");
//		versions.add("MergeDevice5_2");
//		versions.add("MergeDevice6_New");
//		versions.add("MergeDevice5_3");
//		versions.add("MergeDevice6_New");
//		versions.add("MergeDevice5_4");
//		versions.add("MergeDevice6_New");
//		versions.add("MergeDevice5_5");
//		versions.add("MergeDevice6_New");
//		versions.add("MergeDevice5_JaMuz");
//		versions.add("MergeDevice6_New");


//		versions.add("MergeDevice6_New");
//		versions.add("MergeDevice7_KO");
//		versions.add("MergeDevice8_OK");
//		versions.add("MergeDevice9_DbOk");


//		versions.add("MergeDevice9_DbOk");
//		versions.add("MergeDevice10_5");
//		versions.add("MergeDevice9_DbOk");
//		versions.add("MergeDevice10_JaMuz");

		
//		versions.add("MergeDevice10_5");
//		versions.add("MergeDevice10_New");
//		versions.add("MergeDevice10_JaMuz");
//		versions.add("MergeDevice10_New");


		versions.add("MergeDevice11_Sync");
		versions.add("MergeDevice11_Sync2");
		
        for(String mbId : mbIds) {
			for(int i=0; i<(versions.size()-1); i++) {
				String firstTab = versions.get(i);
				String secondTab = versions.get(i+1);
				compareAlbums(
					mbId,
					firstTab,
					secondTab, testName);
			}
        }
	}
	
	private static void compareAlbums(String mbId, String tab1, String tab2, String testName) {
		compareAlbums(mbId, mbId, tab1, tab2, testName);
	}
	
	private static void compareAlbums(String mbId1, String mbId2, String tab1, String tab2, String testName) {
		try {
			Album firstAlbum = AlbumBuffer.getAlbum(mbId1, tab1);
			Album secondAlbum = AlbumBuffer.getAlbum(mbId2, tab2);

			String pathLogs = Settings.getRessourcesPath()+"albumFiles_"+testName;
			File f = new File(pathLogs);
			f.mkdir();
			logFile = new LogText(pathLogs+File.separator);
			String logFileName = tab1+"-"+tab2+"-"+mbId1+".csv";;			
			if(!logFile.createFile(logFileName)) {
				Popup.error(MessageFormat.format(Inter.get("Error.Merge.CreatingLOG"), new Object[] {logFileName}));  //NOI18N
				System.exit(1);
			}
			logFile.add("File\tValue\t"+tab1+"\t"+tab2);

			compareAlbums(firstAlbum, secondAlbum);

			logFile.close();

//			Desktop.openFile(FilenameUtils.concat(pathLogs, logFileName));
			
		} catch (IOException | CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException ex) {
			Logger.getLogger(CompareOO.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private static void compareAlbums(Album album1, Album album2) {

		//Compare tracks
		for(int i=0; i<album1.getTracks().size(); i++) {
			TrackTag trackTag1 = album1.getTracks().get(i);
			TrackTag trackTag2 = album2.getTracks().get(i);
			compareFile(trackTag1, trackTag2);
		}
		
		//Compare results
		compareResult(album1, album2, "nbFiles");
		compareResult(album1, album2, "hasID3v1");
		compareResult(album1, album2, "isReplayGainDone");
		compareResult(album1, album2, "cover");
		compareResult(album1, album2, "bitRate");
		compareResult(album1, album2, "length");
		compareResult(album1, album2, "size");
		compareResult(album1, album2, "format");
		compareResult(album1, album2, "discNoFull");
		compareResult(album1, album2, "trackNoFull");
		compareResult(album1, album2, "comment");
		compareResult(album1, album2, "artist");
		compareResult(album1, album2, "title");
		compareResult(album1, album2, "bpm");
		compareResult(album1, album2, "year");
		compareResult(album1, album2, "genre");
		compareResult(album1, album2, "albumArtist");
		compareResult(album1, album2, "album");
		compareResult(album1, album2, "duplicates");
		
		if(!album1.getLocation().equals(album2.getLocation())) {
			addToLog("", "Location", album1.getLocation(), album2.getLocation());
		}
		if(album1.getIdFirstFile()!=album2.getIdFirstFile()) {
			addToLog("", "IdFirstFile", String.valueOf(album1.getIdFirstFile()), String.valueOf(album2.getIdFirstFile()));
		}
		if(album1.getIdPath()!=album2.getIdPath()) {
			addToLog("", "IdPath", String.valueOf(album1.getIdPath()), String.valueOf(album2.getIdPath()));
		}
		if(album1.getIndex()!=album2.getIndex()) {
			addToLog("", "Index", String.valueOf(album1.getIndex()), String.valueOf(album2.getIndex()));
		}

	}
	
	private static void compareFile(TrackTag file1, TrackTag file2) {

		if(!file1.getArtist().equals(file2.getArtist())) {
			addToLog(file1.getRelativeFullPath(), "artist", file1.getArtist(), file2.getArtist());
		}
		if(!file1.getAlbumArtist().equals(file2.getAlbumArtist())) {
			addToLog(file1.getRelativeFullPath(), "albumArtist", file1.getAlbumArtist(), file2.getAlbumArtist());
		}
		if(!file1.getAlbum().equals(file2.getAlbum())) {
			addToLog(file1.getRelativeFullPath(), "album", file1.getAlbum(), file2.getAlbum());
		}
		if(!file1.getTitle().equals(file2.getTitle())) {
			addToLog(file1.getRelativeFullPath(), "title", file1.getTitle(), file2.getTitle());
		}
		if(!file1.getFilename().equals(file2.getFilename())) {
			addToLog(file1.getRelativeFullPath(), "sourceFile", file1.getFilename(), file2.getFilename());
		}
		if(!file1.getGenre().equals(file2.getGenre())) {
			addToLog(file1.getRelativeFullPath(), "genre", file1.getGenre(), file2.getGenre());
		}
		if(!file1.getComment().equals(file2.getComment())) {
			addToLog(file1.getRelativeFullPath(), "comment", file1.getComment(), file2.getComment());
		}
		if(!file1.getYear().equals(file2.getYear())) {
			addToLog(file1.getRelativeFullPath(), "year", file1.getYear(), file2.getYear());
		}
		if(file1.getDiscNo()!=file2.getDiscNo()) {
			addToLog(file1.getRelativeFullPath(), "discNo", String.valueOf(file1.getDiscNo()), String.valueOf(file2.getDiscNo()));
		}
		if(file1.getDiscTotal()!=file2.getDiscTotal()) {
			addToLog(file1.getRelativeFullPath(), "discTotal", String.valueOf(file1.getDiscTotal()), String.valueOf(file2.getDiscTotal()));
		}
		if(file1.getTrackNo()!=file2.getTrackNo()) {
			addToLog(file1.getRelativeFullPath(), "trackNo", String.valueOf(file1.getTrackNo()), String.valueOf(file2.getTrackNo()));
		}
		if(file1.getTrackTotal()!=file2.getTrackTotal()) {
			addToLog(file1.getRelativeFullPath(), "trackTotal", String.valueOf(file1.getTrackTotal()), String.valueOf(file2.getTrackTotal()));
		}
		if(file1.getNbCovers()!=file2.getNbCovers()) {
			addToLog(file1.getRelativeFullPath(), "nbCovers", String.valueOf(file1.getNbCovers()), String.valueOf(file2.getNbCovers()));
		}
		if(file1.isDeleted()!=file2.isDeleted()) {
			addToLog(file1.getRelativeFullPath(), "deleted", String.valueOf(file1.isDeleted()), String.valueOf(file2.isDeleted()));
		}
		if(file1.getCheckedFlag()!=file2.getCheckedFlag()) {
			addToLog(file1.getRelativeFullPath(), "checkedFlag", String.valueOf(file1.getCheckedFlag()), String.valueOf(file2.getCheckedFlag()));
		}
		if(file1.getPlayCounter()!=file2.getPlayCounter()) {
			addToLog(file1.getRelativeFullPath(), "PlayCounter", String.valueOf(file1.getPlayCounter()), String.valueOf(file2.getPlayCounter()));
		}
		if(file1.getRating()!=file2.getRating()) {
			addToLog(file1.getRelativeFullPath(), "Rating", String.valueOf(file1.getRating()), String.valueOf(file2.getRating()));
		}
		if(!file1.getAddedDate().equals(file2.getAddedDate())) {
			addToLog(file1.getRelativeFullPath(), "AddedDate", 
					DateTime.formatUTC(file1.getAddedDate(), DateTime.DateTimeFormat.HUMAN, false), 
					DateTime.formatUTC(file2.getAddedDate(), DateTime.DateTimeFormat.HUMAN, false));
		}
		if(!file1.getLastPlayed().equals(file2.getLastPlayed())) {
			addToLog(file1.getRelativeFullPath(), "LastPlayed", 
					DateTime.formatUTC(file1.getLastPlayed(), DateTime.DateTimeFormat.HUMAN, false), 
					DateTime.formatUTC(file2.getLastPlayed(), DateTime.DateTimeFormat.HUMAN, false));
		}
		if(file1.getBPM()!=file2.getBPM()) {
			addToLog(file1.getRelativeFullPath(), "BPM", String.valueOf(file1.getBPM()), String.valueOf(file2.getBPM()));
		}
		
		if(file1.ignore!=file2.ignore) {
			addToLog(file1.getRelativeFullPath(), "ignore", String.valueOf(file1.ignore), String.valueOf(file2.ignore));
		}
	}
    
	private static void compareResult(Album album1, Album album2, String key) {
		FolderInfoResult folderInfoResult1 = album1.getResults().get(key);
		FolderInfoResult folderInfoResult2 = album2.getResults().get(key);
		
		if(folderInfoResult1.getErrorLevel()!=folderInfoResult2.getErrorLevel()) {
			addToLog("", "Result "+key, String.valueOf(folderInfoResult1.getErrorLevel()), String.valueOf(folderInfoResult2.getErrorLevel()));
		}
		
		compareResultValue(album1, album2, key);
	}
	
	private static void compareResultValue(Album album1, Album album2, String key) {
		FolderInfoResult folderInfoResult1 = album1.getResults().get(key);
		FolderInfoResult folderInfoResult2 = album2.getResults().get(key);
		
		if(!folderInfoResult1.getValue().equals(folderInfoResult2.getValue())) {
			addToLog("", "Value "+key, folderInfoResult1.getValue(), folderInfoResult2.getValue());
		}
	}
	
    private static LogText logFile;
    
    private static void addToLog(String file, String value, String dB1, String dB2) {
        String line = file+"\t"+value+"\t";
        line=line.concat(dB1+"\t"+dB2);
        logFile.add(line);
        System.out.println(line);
    }

}
