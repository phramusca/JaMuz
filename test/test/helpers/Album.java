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

import jamuz.FileInfo;
import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.process.check.FolderInfo;
import jamuz.process.check.FolderInfoResult;
import jamuz.process.check.PanelCheck;
import jamuz.process.sync.Device;
import jamuz.process.check.ReleaseMatch;
import jamuz.process.merge.StatSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.junit.Assert;
import org.apache.commons.io.FileExistsException;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import org.musicbrainz.MBWS2Exception;
import jamuz.utils.DateTime;
import jamuz.utils.Inter;
import jamuz.utils.StringManager;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public final class Album {
    
    private final String mbId;
	private String version;
	
    private final ArrayList<TrackTag> tracks;

	/**
	 *
	 * @return
	 */
	public int getNbTracks() {
        return tracks.size();
    }
    
    private final Map<String, FolderInfoResult> results;
    private int idPath=-1;
    private int idFirstFile=-1;
    private String location="";
  
	/**
	 *
	 */
	public int index=-1;  
    
    private Album(String mbId) throws IOException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
        this.mbId=mbId;
        tracks = new ArrayList<>();
        results = new HashMap<>();
		results.put("nbFiles", new FolderInfoResult());  //NOI18N
		results.put("hasID3v1", new FolderInfoResult());		  //NOI18N
        results.put("isReplayGainDone", new FolderInfoResult());  //NOI18N
		results.put("cover", new FolderInfoResult());  //NOI18N
        results.put("bitRate", new FolderInfoResult());  //NOI18N
        results.put("length", new FolderInfoResult());  //NOI18N
		results.put("size", new FolderInfoResult());  //NOI18N
        results.put("format", new FolderInfoResult());  //NOI18N
        results.put("discNoFull", new FolderInfoResult());  //NOI18N
		results.put("trackNoFull", new FolderInfoResult());  //NOI18N
        results.put("comment", new FolderInfoResult());  //NOI18N
		results.put("artist", new FolderInfoResult());  //NOI18N
		results.put("title", new FolderInfoResult());  //NOI18N
		results.put("bpm", new FolderInfoResult());  //NOI18N
		results.put("year", new FolderInfoResult());  //NOI18N
		results.put("genre", new FolderInfoResult());  //NOI18N
		results.put("albumArtist", new FolderInfoResult());  //NOI18N
		results.put("album", new FolderInfoResult());  //NOI18N
        results.put("duplicates", new FolderInfoResult());  //NOI18N
    }
    
	/**
	 *
	 * @param mbId
	 * @param version
	 * @throws IOException
	 * @throws CannotReadException
	 * @throws TagException
	 * @throws ReadOnlyFileException
	 * @throws InvalidAudioFrameException
	 */
	public Album(String mbId, String version) throws IOException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
        this(mbId);
		this.version=version;
        readFromFile(version);
    }

	public String getMbId() {
		return mbId;
	}

	public ArrayList<TrackTag> getTracks() {
		return tracks;
	}

	public Map<String, FolderInfoResult> getResults() {
		return results;
	}

	public int getIdPath() {
		return idPath;
	}

	public int getIdFirstFile() {
		return idFirstFile;
	}

	public String getLocation() {
		return location;
	}

	public int getIndex() {
		return index;
	}

    private void readFromFile(String version) throws IOException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
        
		File file=getFile();
		if(!file.exists()) {
			return;
		}
        SpreadSheet spreadSheet = SpreadSheet.createFromFile(file);
        Sheet sheet = spreadSheet.getSheet(version);
		if(sheet==null) {
			return;
		}
        int nRowCount = sheet.getRowCount();
        int nColCount = sheet.getColumnCount();
        tracks.clear();
        for(int nRowIndex = 0; nRowIndex < nRowCount; nRowIndex++)
        {
            Row row = new Row(sheet, nRowIndex);
            String key;
            switch(row.getArg(0)) {
                case "track": 
                    String sourceFile; String relativePath; String filename; 
                        float BPM; String album; String albumArtist; String artist; String comment; 
                        int discNo; int discTotal; String genre; int nbCovers; String title; int trackNo; int trackTotal; 
                        String year; int playCounter; int rating; String addedDate; String lastPlayed; 
                        boolean deleted; FolderInfo.CheckedFlag checkedFlag; boolean ignore=false;
                    
                    if(nColCount>21) {
                        ignore = row.getValue(21).equals("{IGNORE}");
                    }
                    else {
                        ignore=false;
                    }
                    
                    tracks.add(new TrackTag(
                            sourceFile=row.getValue(5),
                            relativePath=mbId+File.separator,
                            filename="Track"+nRowIndex+".mp3",
                            BPM=Float.valueOf(row.getValue(12)), 
                            album=row.getValue(3), 
                            albumArtist=row.getValue(2), 
                            artist=row.getValue(1), 
                            comment=row.getValue(14), 
                            discNo=Integer.valueOf(row.getValue(6)), 
                            discTotal=Integer.valueOf(row.getValue(7)), 
                            genre=row.getValue(11), 
                            nbCovers=Integer.valueOf(row.getValue(13)), 
                            title=row.getValue(4), 
                            trackNo=Integer.valueOf(row.getValue(8)), 
                            trackTotal=Integer.valueOf(row.getValue(9)), 
                            year=row.getValue(10), 
                            playCounter=Integer.valueOf(row.getValue(17)), 
                            rating=Integer.valueOf(row.getValue(18)), 
                            addedDate=row.getValue(19), 
                            lastPlayed=row.getValue(20), 
                            deleted=Boolean.parseBoolean(row.getValue(15)), 
                            checkedFlag=FolderInfo.CheckedFlag.valueOf(row.getValue(16)),
                            ignore
                    ));
                    break;
                case "result":
                    key = row.getValue(1);
                    setResultValue(key, row.getValue(3));   
                    
                    switch(row.getArg(2)) {
                        case "warning":
                            setWarning(key);
                            break;
                        case "ko":
                            setKO(key);
                            break;
                        case "ok":
                            setOK(key);
                            break;
                    }
                    break;
                case "process":
                    String value = row.getValue(2); 
                    switch(row.getArg(1)) {
                        case "index":
                            this.index=Integer.valueOf(value);
                            break;
                        case "idpath":
                            this.idPath=Integer.valueOf(value);
                            break;
                        case "idfirstfile":
                            this.idFirstFile=Integer.valueOf(value);
                            break;
                        case "location":
                            this.location=value;
                            break;
                    }
                    break;
            }
        }
    }
    
    private class Row {
        private final Sheet sheet;
        private final int index;

        public Row(Sheet sheet, int index) {
            this.sheet = sheet;
            this.index = index;
        }
        
        public String getArg(int colIndex) {
            return sheet.getCellAt(colIndex, this.index).getValue().toString().toLowerCase();
        }
        
        public String getValue(int colIndex) {
            return sheet.getCellAt(colIndex, this.index).getValue().toString();
        }
    }
    
	/**
	 *
	 * @param mbId
	 * @throws MBWS2Exception
	 * @throws IOException
	 * @throws CannotReadException
	 * @throws TagException
	 * @throws ReadOnlyFileException
	 * @throws InvalidAudioFrameException
	 */
	public static void createFile(String mbId) throws MBWS2Exception, IOException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException {

        //Get info from MusicBrainz
        ReleaseMatch match = new ReleaseMatch(mbId);
        match.getTracks(null);

        int i=0;
        String[] columns = new String[21];
        columns[i++] = "";
        columns[i++] = "artist";
        columns[i++] = "albumArtist";
        columns[i++] = "album";
        columns[i++] = "title";
        columns[i++] = "sourceFile";
        columns[i++] = "discNo";
        columns[i++] = "discTotal";
        columns[i++] = "trackNo";
        columns[i++] = "trackTotal";
        columns[i++] = "year";
        columns[i++] = "genre";
        columns[i++] = "BPM";
        columns[i++] = "nbCovers";
        columns[i++] = "comment";
        columns[i++] = "deleted";
        columns[i++] = "checkedFlag";
        columns[i++] = "playCounter";
        columns[i++] = "rating";
        columns[i++] = "addedDate";
        columns[i++] = "lastPlayed";

        // Save the data to an ODS file and open it.
        TableModel model = new DefaultTableModel(getData(match), columns);

        if(getFile(mbId).exists()) {
            throw new FileExistsException(getFile(mbId));
        }

        SpreadSheet spreadSheet = SpreadSheet.createEmpty(model);
        spreadSheet.getFirstSheet().setName("MusicBrainz_REFERENCE_DO_NOT_MODIFY"); //TODO: Protect this sheet (original data)

		i=0;
        spreadSheet.getFirstSheet().copy(i++, "CheckTest1_KO");         //Created and scan KO (several modifications)
        spreadSheet.getFirstSheet().copy(i++, "CheckTest2_OK");         //Modify genre, cover and SAVE
        spreadSheet.getFirstSheet().copy(i++, "CheckTest3_DbOk");       //Set OK
        
        spreadSheet.getFirstSheet().copy(i++, "MergeTest1_Creation");
        spreadSheet.getFirstSheet().copy(i++, "MergeTest2_DB");         //Same as above with rating 0 instead of -1 (as inserted in Db)
        spreadSheet.getFirstSheet().copy(i++, "MergeTest3");            //Added date changes after initial merge
        
        spreadSheet.getFirstSheet().copy(i++, "MergeTest4_1");          //New statistics to set on Guayadeque 	(Linux)
//        spreadSheet.getFirstSheet().copy(i++, "MergeTest4_2");          //New statistics to set on Kodi 	(Linux/Windows)
//        spreadSheet.getFirstSheet().copy(i++, "MergeTest4_3");          //New statistics to set on MediaMonkey (Windows)
//        spreadSheet.getFirstSheet().copy(i++, "MergeTest4_4");          //New statistics to set on Mixxx 	(Linux/Windows)
//        spreadSheet.getFirstSheet().copy(i++, "MergeTest4_5");          //New statistics to set on MyTunes 	(Android)
        spreadSheet.getFirstSheet().copy(i++, "MergeTest4_JaMuz");      //New statistics to set on JaMuz 	(Linux/Windows)
        
        spreadSheet.getFirstSheet().copy(i++, "MergeTest4_New");        //Expected statistics after merge

        spreadSheet.getFirstSheet().copy(i++, "MergeDevice1_KO");       //Created KO (so we can set it OK between 2 merges)
        spreadSheet.getFirstSheet().copy(i++, "MergeDevice2_DB");       //Same as above with rating 0 instead of -1 (as inserted in Db)
        spreadSheet.getFirstSheet().copy(i++, "MergeDevice3_JaMuz");    //Set some ratings=4 for device sync (playlist)
        spreadSheet.getFirstSheet().copy(i++, "MergeDevice4_1stMerge"); //Added date changes after initial merge
        
        spreadSheet.getFirstSheet().copy(i++, "MergeDevice5_1");        //New statistics to set on Guayadeque 	(Linux)
        spreadSheet.getFirstSheet().copy(i++, "MergeDevice5_2");        //New statistics to set on Kodi 	(Linux/Windows)
        spreadSheet.getFirstSheet().copy(i++, "MergeDevice5_3");        //New statistics to set on MediaMonkey (Windows)
        spreadSheet.getFirstSheet().copy(i++, "MergeDevice5_4");        //New statistics to set on Mixxx 	(Linux/Windows)
        spreadSheet.getFirstSheet().copy(i++, "MergeDevice5_5");        //New statistics to set on MyTunes 	(Android)
        spreadSheet.getFirstSheet().copy(i++, "MergeDevice5_JaMuz");    //New statistics to set on JaMuz 	(Linux/Windows)
        spreadSheet.getFirstSheet().copy(i++, "MergeDevice6_New");      //Expected statistics after merge

        spreadSheet.getFirstSheet().copy(i++, "MergeDevice7_KO");       //Scan library unchecked => KO
        spreadSheet.getFirstSheet().copy(i++, "MergeDevice8_OK");       //Modify genre, cover and SAVE 
        spreadSheet.getFirstSheet().copy(i++, "MergeDevice9_DbOk");     //Set OK
		
		//TODO TEST Create files according to latest changes to tests
        spreadSheet.getFirstSheet().copy(i++, "MergeDevice10_NoSync");  //Merge without updating stat sources
        spreadSheet.getFirstSheet().copy(i++, "MergeDevice11_Sync");    //Merge after updating stat sources
		
		//Example, changing location process value
//		setValue(spreadSheet, "MergeTest1_Creation", "C", getRow(match, "location"), "location.library");
		//Example changing title of 6th track
//		setValue(spreadSheet, "MergeTest1_Creation", getCol("title"), 6, "CHANGED TITLE");
		
//		setValue(spreadSheet, "MergeDevice1_KO", "C", getRow(match, "location"), "location.library");

        spreadSheet.saveAs(getFile(mbId));
//        OOUtils.open(getFile(mbId));
    }
    
	private static String getCol(String key) {
		String value="A";
		switch(key) {
			case "artist": value="B"; break;
			case "albumArtist": value="C"; break;
			case "album": value="D"; break;
			case "title": value="E"; break;
			case "sourceFile": value="F"; break;
			case "discNo": value="G"; break;
			case "discTotal": value="H"; break;
			case "trackNo": value="I"; break;
			case "trackTotal": value="J"; break;
			case "year": value="K"; break;
			case "genre": value="L"; break;
			case "BPM": value="M"; break;
			case "nbCovers": value="N"; break;
			case "comment": value="O"; break;
			case "deleted": value="P"; break;
			case "checkedFlag": value="Q"; break;
			case "playCounter": value="R"; break;
			case "rating": value="S"; break;
			case "addedDate": value="T"; break;
			case "lastPlayed": value="U"; break;
		}
		return value;
	}
	
	private static int getRow(ReleaseMatch match, String key) {
		int index=0;
		switch(key) {
			case "nbFiles": index=1;
				break;
			case "hasID3v1": index=2;
				break;
			case "isReplayGainDone": index=3;
				break;
			case "cover": index=4;
				break;
			case "bitRate": index=5;
				break;
			case "length": index=6;
				break;
			case "size": index=7;
				break;
			case "format": index=8;
				break;
			case "discNoFull": index=9;
				break;
			case "trackNoFull": index=10;
				break;
			case "comment": index=11;
				break;
			case "artist": index=12;
				break;
			case "title": index=13;
				break;
			case "bpm": index=14;
				break;
			case "year": index=15;
				break;
			case "genre": index=16;
				break;
			case "albumArtist": index=17;
				break;
			case "album": index=18;
				break;
			case "duplicates": index=19;
				break;
			case "index": index=21;
				break;
			case "idPath": index=22;
				break;
			case "idFirstFile": index=23;
				break;
			case "location": index=24;
				break;
		}
		return match.getTracks(null).size()+index+2;
	}
	
	private static void setValue(SpreadSheet spreadSheet, String sheetName, String col, int index, String value) {
		spreadSheet.getSheet(sheetName).getCellAt(col+index).setValue(value);
	}
	
	private static Object[][] getData(ReleaseMatch match) {
		int i=0;
        final Object[][] data = new Object[match.getTracks(null).size()+25][21];
        String trackSource="1min.mp3";
        //Add tracks information
        for(ReleaseMatch.Track track : match.getTracks(null)) {
            data[i++] = new Object[] { 
                "track",                    //0
                track.getArtist(),          //1
                match.getArtist(),          //2
                match.getAlbum(),           //3
                track.getTitle(),           //4
                trackSource,                //5: sourceFile
                track.getDiscNo(),          //6
                track.getDiscTotal(),       //7
                track.getTrackNo(),         //8
                track.getTrackTotal(),      //9
                match.getYear(),            //10
                "",                         //11: genre
                "0",                        //12: BPM
                "0",                        //13: nbCovers
                "",                         //14: comment
                "0",                        //15: deleted
                "UNCHECKED",                //16: checkedFlag
                "0",                        //17: playCounter
                "-1",                       //18: rating
                "1970-01-01 00:00:00",      //19: addedDate
                "1970-01-01 00:00:00",      //20: lastPlayed
            };
        }
        data[i++] = new Object[] { "", "key", "result", "value" };
        
        //Add expected results
        data[i++] = new Object[] { "result", "nbFiles", "ok", match.getTracks(null).size() };
        data[i++] = new Object[] { "result", "hasID3v1", "ok", Inter.get("Label.No") };
        data[i++] = new Object[] { "result", "isReplayGainDone", "ok", Inter.get("Label.Yes") };
        data[i++] = new Object[] { "result", "cover", "ko", "{IGNORE}" }; //TODO TEST Set expected value
        data[i++] = new Object[] { "result", "bitRate", "ok", "{IGNORE}" }; //TODO TEST Set expected value (mean bitRate)
        data[i++] = new Object[] { "result", "length", "ok", "{N/A}" };
        data[i++] = new Object[] { "result", "size", "ok", "{N/A}" }; 
        data[i++] = new Object[] { "result", "format", "ok", "{N/A}" };
        data[i++] = new Object[] { "result", "discNoFull", "ok", "{N/A}" };
        data[i++] = new Object[] { "result", "trackNoFull", "ok", "{N/A}" };
        data[i++] = new Object[] { "result", "comment", "ok", "{N/A}" };
        data[i++] = new Object[] { "result", "artist", "ok", "{N/A}" };
        data[i++] = new Object[] { "result", "title", "ok", "{N/A}" };
        data[i++] = new Object[] { "result", "bpm", "ok", "{N/A}" };
        data[i++] = new Object[] { "result", "year", "ok", match.getYear() }; //May not be ok though, but mostly
        data[i++] = new Object[] { "result", "genre", "ko", "" };
        data[i++] = new Object[] { "result", "albumArtist", "ok", match.getArtist() };
        data[i++] = new Object[] { "result", "album", "ok", match.getAlbum() };
        data[i++] = new Object[] { "result", "duplicates", "ok" };

        data[i++] = new Object[] { "", "key", "value" };
        data[i++] = new Object[] { "process", "index", "-1" };
        data[i++] = new Object[] { "process", "idPath", "-1" };
        data[i++] = new Object[] { "process", "idFirstFile", "-1" };
        data[i++] = new Object[] { "process", "location", "location.add" };
		
		return data;
	}
	
    private static File getFile(String mbId) {
        return new File(Settings.getRessourcesPath()+"albumFiles"+File.separator+mbId+".ods");
    }
    
    private File getFile() {
        return getFile(this.mbId);
    }

	/**
	 *
	 * @return
	 */
    public FolderInfo getCheckedFolder() {
        return PanelCheck.tableModelActionQueue.getFolders().get(index);
    }
    
    /**
	 * setResult: Set expected result.
     * Error Level:
	 * 0: OK
	 * 1: Warning
	 * 2: KO
     * @param resultKey
     * @param value
     * @param errorLevel
	 */
    private void setResultValue(String resultKey, String value) {
        this.results.get(resultKey).setValue(value);
    }

    private void setKO(String resultKey) {
        this.results.get(resultKey).setKO(true);
    }
    
    private void setWarning(String resultKey) {
        this.results.get(resultKey).setWarning(true);
    }
    
    private void setOK(String resultKey) {
        this.results.get(resultKey).setOK();
    }

	/**
	 *
	 * @throws CannotReadException
	 * @throws IOException
	 * @throws TagException
	 * @throws ReadOnlyFileException
	 * @throws InvalidAudioFrameException
	 */
	public void create() throws CannotReadException, IOException, TagException, 
			ReadOnlyFileException, InvalidAudioFrameException {
        for(TrackTag track : tracks) {
            track.create(location);
        }
    }
 
    private void checkDb() throws CannotReadException, IOException, TagException, 
			ReadOnlyFileException, InvalidAudioFrameException {
        FolderInfo folder = Jamuz.getDb().getFolder(idPath);
        Assert.assertTrue("Could not retrieve idPath="+idPath, folder!=null);
        
        ArrayList<FileInfoInt> files = new ArrayList<>();
        Jamuz.getDb().getFiles(files, idPath, true);
        
        compare(files);
    }
      
    private ArrayList<FileInfo> getFiles() {
        ArrayList<FileInfo> files = new ArrayList<>();
        int idFile = idFirstFile;
        for(TrackTag trackTag : tracks) {
            files.add(new FileInfo(idFile, idPath, 
                    trackTag.getRelativeFullPath(), 
                    trackTag.getRating(), 
                    trackTag.getFormattedLastPlayed(), 
                    trackTag.getFormattedAddedDate(), 
                    trackTag.getPlayCounter(), "", -1, 
                    trackTag.getBPM(), "", "", "", ""));
            idFile++;
        }
        return files;
    }
    
	/**
	 *
	 * @param idStatSource
	 * @param isDevice
	 * @throws IOException
	 * @throws CannotReadException
	 * @throws TagException
	 * @throws ReadOnlyFileException
	 * @throws InvalidAudioFrameException
	 */
	public void setAndCheckStatsInStatSource(int idStatSource, boolean isDevice) throws IOException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
        StatSource statSource = Jamuz.getMachine().getStatSource(idStatSource);
        statSource.getSource().getSource(System.getProperty("java.io.tmpdir")+File.separator);
        statSource.getSource().setUp();
        statSource.getSource().updateStatistics(getFiles());
        statSource.getSource().tearDown();
        statSource.getSource().sendSource(System.getProperty("java.io.tmpdir")+File.separator);
        checkStatSource(idStatSource, isDevice, false);
    }

	/**
	 *
	 * @throws IOException
	 * @throws CannotReadException
	 * @throws TagException
	 * @throws ReadOnlyFileException
	 * @throws InvalidAudioFrameException
	 */
	public void checkJaMuz() throws IOException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
        checkDb();
    }
    
	/**
	 *
	 * @throws CannotReadException
	 * @throws IOException
	 * @throws TagException
	 * @throws ReadOnlyFileException
	 * @throws InvalidAudioFrameException
	 */
	public void checkActionsTableModel() throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
        //Check tracks
        compare((ArrayList<FileInfoInt>)(List<?>)getCheckedFolder().getFilesAudio());

        //Check results
        for (Map.Entry<String, FolderInfoResult> entry : results.entrySet()) {
            FolderInfoResult resultActual = getCheckedFolder().getResults().get(entry.getKey());
            FolderInfoResult resultExpected = entry.getValue();
            if(!resultExpected.getValue().startsWith("{")) {
                Assert.assertEquals(entry.getKey()+" "+resultActual, resultExpected, resultActual);
            }
        }
    }
    
	/**
	 *
	 * @throws IOException
	 * @throws CannotReadException
	 * @throws TagException
	 * @throws ReadOnlyFileException
	 * @throws InvalidAudioFrameException
	 */
	public void setAndCheckStatsInJamuzDb() throws IOException, 
			CannotReadException, TagException, 
			ReadOnlyFileException, InvalidAudioFrameException {
        Jamuz.getDb().updateStatistics(getFiles());
        checkDb();
    }
    
	/**
	 *
	 * @param idStatSource
	 * @param isDevice
	 * @param renamed
	 */
	public void checkStatSource(int idStatSource, 
			boolean isDevice, 
			boolean renamed) {
        
        StatSource statSource = Jamuz.getMachine().getStatSource(idStatSource);
        String msg = " (idStatement="+statSource.getIdStatement()
							+", "+this.version
							+", "+this.mbId+")";
        ArrayList<FileInfo> statSourcesFiles = new ArrayList<>();
        statSource.getSource().getSource(
				System.getProperty("java.io.tmpdir")+File.separator);
        statSource.getSource().setUp();
        statSource.getSource().getStatistics(statSourcesFiles);
        statSource.getSource().tearDown();
        statSource.getSource().sendSource(
				System.getProperty("java.io.tmpdir")+File.separator);

        int nbExpected=0;
        for (TrackTag albumFile : tracks) {
            if(!(isDevice && albumFile.ignore)) {
                nbExpected++;
				
				String fileName = getFilename(albumFile, renamed);
                
                FileInfo statSourceFile=searchInStatsList(fileName, statSourcesFiles);
                
                Assert.assertTrue("Search "+fileName+msg, statSourceFile!=null);
				Assert.assertEquals("rating "+fileName+msg, 
						albumFile.getRating(), 
						statSourceFile.getRating());
				if(statSource.getSource().isUpdatePlayCounter()) {
					Assert.assertEquals("playCounter "+fileName+msg, 
							albumFile.getPlayCounter(), 
							statSourceFile.getPlayCounter());
				}
                if(statSource.getSource().isUpdateAddedDate()) {
                    Assert.assertEquals("addedDate "+fileName+msg, 
							albumFile.getFormattedAddedDate(), 
							statSourceFile.getFormattedAddedDate());
                }
                if(statSource.getSource().isUpdateLastPlayed()) {
                    Assert.assertEquals("lastPlayed "+fileName+msg, 
							albumFile.getFormattedLastPlayed(), 
							statSourceFile.getFormattedLastPlayed()); //Default is 1970-01-01 00:00:00
                }
                if(statSource.getSource().isUpdateBPM()) {
                    Assert.assertEquals("BPM "+fileName+msg, 
							albumFile.getBPM(), 
							statSourceFile.getBPM(), 0.0f);
                }
				//TODO: Test tags mege
			//				if(statSource.getSource().isUpdateTags()) {
			//                    Assert.assertEquals("Tags "+fileName+msg, albumFile.getTags(), statSourceFile.getTags(), 0.0f);
			//                }
			//TODO: Test genre mege
            }
        }
        
        //Count nb of files with same relativepath as current album in retrieved list from stat source
        int nbActual=0;
		String albumRelativePath=getPath(this.tracks.get(0), renamed);
        for(FileInfo statSourceFile : statSourcesFiles) {
            if(statSourceFile.getRelativePath().equals(albumRelativePath)) { 
                nbActual++;
            }
		}
        
        Assert.assertEquals("Number of files in album"+msg, nbExpected, nbActual);
    }
    
    //TODO TEST: Use a Map instead ...
    private FileInfo searchInStatsList(String relativeFullPath, ArrayList<FileInfo> statSourcesFiles) {
        for (FileInfo myFileInfo : statSourcesFiles) {
            if(myFileInfo.getRelativeFullPath().equals(relativeFullPath)) { return myFileInfo; }
        }
		return null;
	}
    
	/**
	 *
	 * @param renamed
	 * @throws IOException
	 * @throws CannotReadException
	 * @throws TagException
	 * @throws ReadOnlyFileException
	 * @throws InvalidAudioFrameException
	 */
	public void checkDbAndFS(boolean renamed) throws IOException, CannotReadException, CannotReadException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
        checkDb();
        checkFS(location, renamed);
    }
    
	/**
	 *
	 * @param device
	 * @param renamed
	 */
	public void checkFSdevice(Device device, boolean renamed) {
        for(TrackTag track: this.tracks) {
            File file = new File(device.getDestination()+getFilename(track, renamed));
            if(track.ignore) {
                Assert.assertTrue(file + " exists but expected it does not.", !file.exists());
            }
            else {
                Assert.assertTrue(file + " does not exists but expected it does.", file.exists());
            }
        }
        //TODO TEST: Browse FS to check that there are no other files
    }
    
    private void checkFS(String option, boolean renamed) {
        for(TrackTag track: this.tracks) {
            File file = new File(Jamuz.getMachine().getOptionValue(option)+getFilename(track, renamed));
            Assert.assertTrue(file + " exists?: ", file.exists());
        }
    }
    
    private String getFilename(TrackTag track, boolean renamed) {
        String filename;
            
        if(renamed) {
            //"%albumartist%/%album%/%track% %title%"
            filename = StringManager.removeIllegal(
					track.getAlbumArtist())+File.separator
                        + StringManager.removeIllegal(
								track.getAlbum())+File.separator;

            String trackStr="";  //NOI18N
            if(track.getDiscTotal()>1 && track.getDiscNo()>0) {
                trackStr="["+track.getDiscNo()+"-"+track.getDiscTotal()+"] - ";  //NOI18N
            }
            if(track.getTrackNo()>0) {
                trackStr=trackStr+FolderInfoResult.formatNumber(track.getTrackNo());
            }

            filename += trackStr+" ";

            String titleStr="";  //NOI18N
            if(!track.getArtist().equals(track.getAlbumArtist())) {
                titleStr=track.getArtist() + " - ";  //NOI18N
            }
            titleStr+=track.getTitle();
            filename += StringManager.removeIllegal(titleStr)+".mp3";
        }
        else {
            filename = track.getRelativeFullPath();
        }
        return filename;
    }
	
	private String getPath(FileInfoInt file, boolean renamed) {
        String path;
            
        if(renamed) {
            //"%albumartist%/%album%/%track% %title%"
            path = StringManager.removeIllegal(file.getAlbumArtist())+File.separator
                        + StringManager.removeIllegal(file.getAlbum())+File.separator;
        }
        else {
            path = file.getRelativePath();
        }
        return path;
    }

    private void compare(ArrayList<FileInfoInt> files) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
        Assert.assertEquals("Nb files " + mbId, tracks.size(), files.size());
        int indexTrack=0;
        String msg;
        for (FileInfoInt file : files) {
            msg = " ("+file.getFilename()+", "+version+", " + mbId+")";
            Assert.assertEquals("artist"+msg, tracks.get(indexTrack).getArtist(), file.getArtist());
            Assert.assertEquals("albumArtist"+msg, tracks.get(indexTrack).getAlbumArtist(), file.getAlbumArtist());
            Assert.assertEquals("album"+msg, tracks.get(indexTrack).getAlbum(), file.getAlbum());
            Assert.assertEquals("title"+msg, tracks.get(indexTrack).getTitle(), file.getTitle());
            Assert.assertEquals("trackNb"+msg, tracks.get(indexTrack).getTrackNo(), file.getTrackNo());
            Assert.assertEquals("trackTotal"+msg, tracks.get(indexTrack).getTrackTotal(), file.getTrackTotal());
            Assert.assertEquals("discNo"+msg, tracks.get(indexTrack).getDiscNo(), file.getDiscNo());
            Assert.assertEquals("discTotal"+msg, tracks.get(indexTrack).getDiscTotal(), file.getDiscTotal());
            Assert.assertEquals("year"+msg, tracks.get(indexTrack).getYear(), file.getYear());
            Assert.assertEquals("genre"+msg, tracks.get(indexTrack).getGenre(), format(file.getGenre()));
            Assert.assertEquals("BPM"+msg, tracks.get(indexTrack).getBPM(), file.getBPM(), 0.01f);
            Assert.assertEquals("nbCovers"+msg, tracks.get(indexTrack).getNbCovers(), file.getNbCovers());
            Assert.assertEquals("comment"+msg, tracks.get(indexTrack).getComment(), format(file.getComment()));
            
            Assert.assertEquals("deleted"+msg, tracks.get(indexTrack).isDeleted(), file.isDeleted());
            Assert.assertEquals("checkedFlag"+msg, tracks.get(indexTrack).getCheckedFlag(), file.getCheckedFlag());
            
            Assert.assertEquals("playCounter"+msg, tracks.get(indexTrack).getPlayCounter(), file.getPlayCounter());
            Assert.assertEquals("rating"+msg, tracks.get(indexTrack).getRating(), file.getRating());
            
            Date expected = DateTime.parseSqlUtc(tracks.get(indexTrack).getFormattedAddedDate());
            long diffInSeconds = (expected.getTime() - file.getAddedDate().getTime()) / 1000;
            
            Assert.assertTrue("AddedDate. Expected: " + tracks.get(indexTrack).getFormattedAddedDate() + " +/- 30s, Actual: " + file.getFormattedAddedDate()+msg, diffInSeconds < 30);
            Assert.assertEquals("lastPlayed"+msg, tracks.get(indexTrack).getFormattedLastPlayed(), 
					file.getFormattedLastPlayed()); //Default is 1970-01-01 00:00:00
            
            Assert.assertEquals("bitRate"+msg, TrackSourceRepo.get(tracks.get(indexTrack).sourceFile).bitRate, 
					file.getBitRate());
            Assert.assertEquals("length"+msg, TrackSourceRepo.get(tracks.get(indexTrack).sourceFile).length, 
					file.getLength());
            Assert.assertEquals("format"+msg, TrackSourceRepo.get(tracks.get(indexTrack).sourceFile).format, 
					file.getFormat());
            //Since size varies (increase) after replaygain or tag saving, 
            //only checking it is above initial size and not increased by more than around 5K
            long minimumSize = TrackSourceRepo.get(tracks.get(indexTrack).sourceFile).size;
            long maximumSize = minimumSize + 5000;
            Assert.assertTrue("size. Expected: between "+minimumSize+" and "+maximumSize+", Actual:"+file.getSize()+msg, 
                    file.getSize() >= minimumSize && file.getSize() <= maximumSize);
            //This requires to read tracks.get(index).size after each process => not much usefull too
//            assertEquals("size", tracks.get(index).size, file.size); 
        
            indexTrack++;
        }
    }
    
    private String format(String value) {
        return value.replace("{Empty}", "");
    }
}
