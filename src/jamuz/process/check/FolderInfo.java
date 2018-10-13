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

import jamuz.IconBufferCover;
import jamuz.gui.PanelMain;
import jamuz.utils.Popup;
import jamuz.utils.Inter;
import de.umass.lastfm.Caller;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import jamuz.FileInfo;
import jamuz.process.check.Cover.CoverType;
import jamuz.process.check.FileInfoDisplay.TableValue;
import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.process.check.ProcessCheck.Action;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import jamuz.process.check.ReleaseMatch.Track;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import org.apache.commons.io.FilenameUtils;
import jamuz.gui.swing.ProgressBar;
import jamuz.gui.swing.TableModelCheckTracks;
import jamuz.process.check.ReplayGain.GainValues;
import jamuz.utils.DateTime;
import jamuz.utils.FileSystem;
import jamuz.utils.StringManager;
import java.awt.Color;

/**
 * Folder information
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class FolderInfo implements java.lang.Comparable {
	
	/**
	 * Id in database
	 */
	protected int idPath=-1;
	/**
	 * Modification date
	 */
	protected Date modifDate;
	/**
	 * Number of files in folder
	 */
	protected int nbFiles;
	private boolean deleted;
	private CheckedFlag checkedFlag;
	
	private String fullPath;
	private String relativePath;
	private String rootPath;
	private ArrayList <FileInfoInt> filesDb;
    //TODO: Why filesAudio AND filesAudioTableModel ?
    private List <FileInfoDisplay> filesAudio;
    private TableModelCheckTracks filesAudioTableModel;

	private List <FileInfoInt> filesOther;
	private List <FileInfoInt> filesImage;
	private List <FileInfoInt> filesConvertible;
	private Map<String, String> filesConvertibleExtensions;
	private Map<String, FolderInfoResult> results;
    private String mbId;
    private Map<String, List<ReleaseMatch>> matches;
	private List<ReleaseMatch> originals;
	private Map<String, List<Cover>> coversInternet;
	private List<Cover> coversTag;
	
    private BufferedImage newImage=null;
	//TODO: ReplayGain: 
	// - Store information in database by file (so we can check this when checking existing library)
	// - Store information in file too (the real replaygain tags)
	private boolean isReplayGainDone = false;
	
    /**
     * Action (move to OK, Delete, save, ...)
     */
    public Action action;

	/**
	 *
	 */
	public boolean isActionDone;

	/**
	 *
	 */
	public boolean isLast=false;
    private ProcessCheck.ScanType scanType = ProcessCheck.ScanType.SCAN; 

	/**
	 *
	 * @return
	 */
	public ProcessCheck.ScanType getScanType() {
        return scanType;
    }

	/**
	 *
	 * @param scanType
	 */
	public void setScanType(ProcessCheck.ScanType scanType) {
        this.scanType = scanType;
    }
    
	/**
	 * Rating
	 */
	protected String rating;
    private String searchKey=null;

	/**
	 * Sets path and filenameDisplay
	 * @param rootPath
	 * @param relativePath
	 */
	public void setPath(String rootPath, String relativePath) {
		this.rootPath = rootPath;
		this.relativePath = relativePath;
		fullPath = rootPath+relativePath;
	}
	
	/**
	 *
	 * @param isLast
	 */
	private FolderInfo(boolean isLast) {
		filesAudio = new ArrayList<>();
		filesDb = new ArrayList<>();
        filesAudioTableModel = new TableModelCheckTracks();
		filesOther = new ArrayList<>();
		filesImage = new ArrayList<>();
		filesConvertible = new ArrayList<>();
        coversInternet = new LinkedHashMap<>(); // Linked to preserver order
		matches = new LinkedHashMap<>(); // Linked to preserver order
        action = Action.ANALYZING;
        isActionDone=false;
        results = new HashMap<>();
        this.isLast = isLast;
	}
    
	/**
	 * LAST_FOLDER
	 */
	public FolderInfo() {
        this(true);
    }
	
	/**
	 * Used when getting from library
	 * @param id
	 * @param relativePath
	 * @param modifDate 
	 * @param deleted 
	 * @param checkedFlag  
	 */
	public FolderInfo(int id, String relativePath, Date modifDate, boolean deleted, CheckedFlag checkedFlag) {
		this(false);
		idPath=id;
		this.modifDate=(Date) modifDate.clone();
		this.deleted=deleted;
		this.checkedFlag=checkedFlag;
		
		rootPath = Jamuz.getMachine().getOptionValue("location.library");  //NOI18N
		this.relativePath = relativePath;
		fullPath = rootPath+relativePath;
	}
	
	/**
	 * Used when getting from filesystem
	 * @param fullPath
	 * @param rootPath
	 */
	public FolderInfo(String fullPath, String rootPath) {
		this(false);
		
		try {
			checkedFlag=CheckedFlag.UNCHECKED;
			this.fullPath = fullPath;
			this.rootPath = rootPath;
			relativePath = fullPath.substring(rootPath.length());
			
			File folder = new File(fullPath);
			//Count only files, NOT directories
			nbFiles=folder.listFiles((File file) -> file.isFile()).length;
			modifDate = new Date(folder.lastModified());
		}
		catch (Exception ex) {
			Popup.error(ex);
		}
	}
    
    /**
	 * Clone object instance
	 * @return
     * @throws java.lang.CloneNotSupportedException
	 */
	@Override
	public FolderInfo clone() throws CloneNotSupportedException {
		return (FolderInfo) super.clone();
	}
    
	//TODO: Use a HashMap instead ...
	private int searchInFileInfoDbList(String relativeFullPath) {
		for(int i = 0; i < filesDb.size(); i++) {
			FileInfo myFileInfo = filesDb.get(i);
            if(myFileInfo.getRelativeFullPath().equals(relativeFullPath)) { return i; }
		}
		return -1;
	}
	
    /**
	 * Check folder again
	 */
	public void reCheck() {
		Thread t = new Thread("Thread.FolderInfo.reCheck") {
			@Override
			public void run() {
                browse(false, true, DialogCheck.progressBar, true);
                if(isCheckingMasterLibrary()) {
                    scan(true, DialogCheck.progressBar);
                }
                try {
                    analyse(DialogCheck.progressBar);
                } catch (CloneNotSupportedException ex) {
                    Popup.error(ex); //Should never happen as Cloneable
                }
                DialogCheck.displayFolder();
			}
		};
		t.start();
	}
      
	/**
	 * Scan folder for new or modified files
	 * @param full
     * @param progressBar
	 * @return
	 */
	public boolean scan(boolean full, ProgressBar progressBar) {
		boolean scanDeletedFiles=true;
		//We cannot set or retrieve anything from database if path is a new insertion (idPath=-1)
		//Anyway, all files on that folder are not (supposed to be) in databsase yet, so will be inserted
		if(idPath>0) {
			progressBar.setIndeterminate(Inter.get("Msg.Check.Scan.Setup")); //NOI18N
			//Get list of files from library including deleted
			if(!Jamuz.getDb().getFiles(filesDb, idPath, true)) {
				return false;
			}
		}
		
		//Loop on files from filesystem
		progressBar.setup(filesAudio.size());
		for (FileInfoInt fileFS : filesAudio) {
			int idFileDb = searchInFileInfoDbList(fileFS.getRelativeFullPath());
			if(idFileDb>=0) {
				FileInfoInt fileDb = filesDb.get(idFileDb);
				//Date comparison may not work: compare formatted strings instead !!! 
				//to compare with same formatDisplay as within database
				if(fileDb.isDeleted() || full || !fileFS.getFormattedModifDate().equals(fileDb.getFormattedModifDate())) {
					fileFS.readTags(true); //TODO: Use returned boolean ! (shall we ?)
					fileFS.setIdFile(fileDb.getIdFile());
					fileFS.setIdPath(fileDb.getIdPath());
					//Update file in database (deleted=0 and tags)
					if(!fileFS.updateTagsInDb()) {
						fileFS.unsetCover(); //To prevent memory errors
                        return false;
					}
                    fileFS.unsetCover(); //To prevent memory errors
				}
			}
			else {
                scanDeletedFiles=false; //No need to search for deleted files if path is a new insertion
				fileFS.readTags(true); //TODO: Use returned boolean ! (shall we ?)
				fileFS.setIdPath(idPath);
				if(!fileFS.insertTagsInDb()) {
					fileFS.unsetCover(); //To prevent memory errors
                    return false;
				}
                fileFS.unsetCover(); //To prevent memory errors
			}
			progressBar.progress(fileFS.getRelativePath());
		}
		
		if(scanDeletedFiles) {
			progressBar.setIndeterminate(Inter.get("Msg.Check.Scan.Deleted")); //NOI18N
            if(!scanDeletedFiles(progressBar)) {
				return false;
			}
		}
		
		progressBar.reset();
		
		return true;
	}
	
	private boolean scanDeletedFiles(ProgressBar progressBar) {
		
//        progressBar.setIndeterminate("Listing files"); //TODO: Inter
        //Get list of files from library exluding the one(s) already set as deleted
		if(!Jamuz.getDb().getFiles(filesDb, idPath, false)) {
			return false;
		}

		//Loop on those files
        progressBar.setup(filesDb.size());
		for (FileInfoInt fileDB : filesDb) {
			if(!fileDB.scanDeleted()) {
				return false;
			}
            progressBar.progress("");
		}
		progressBar.reset();
		return true;
	}
	
	/**
	 * Scans for deleted files
     * @param progressBar
	 * @return
	 */
	public boolean scanDeleted(ProgressBar progressBar) {
		//Check if folder exist
		File path = new File(fullPath);
		if(!path.exists()) {
			//Path does not exist. Set path and associated files as deleted in database
			if(!Jamuz.getDb().setPathDeleted(idPath)) {
				return false;
			}
		}
		else {
			//Path does exist. Check if files have been deleted
			scanDeletedFiles(progressBar);
		}
		return true;
	}
	
	/**
	 * Insert or update in database
	 * @return
	 */
	private boolean insertOrUpdateInDb(CheckedFlag checkedFlag) {
		File folder = new File(fullPath);
		modifDate = new Date(folder.lastModified());
		
		//If idPath is not known, check if it exists
		if(idPath<0) {
			idPath = Jamuz.getDb().getIdPath(relativePath);
		}

//		if(isWarning()) {
//			checkedFlag=CheckedFlag.OK_WARNING;
//		}
//		else {
//			checkedFlag=CheckedFlag.OK;
//		}

		if(idPath>=0) {
			return updateInDb(checkedFlag);
		}
		else {
			return insertInDb(checkedFlag);
		}
	}
	
	/**
	 * Inserts in database
	 * @param checkedFlag
	 * @return
	 */
	public boolean insertInDb(CheckedFlag checkedFlag) {
		int [] key = new int[1]; //Hint: Using a int table a cannot pass a simple integer by reference
		boolean result = Jamuz.getDb().insertPath(relativePath, modifDate, checkedFlag, mbId, key);
		idPath=key[0]; //Get insertion key
		return result;
	}
	
	/**
	 * Updates in database
	 * @param checkedFlag
	 * @return
	 */
	public boolean updateInDb(CheckedFlag checkedFlag) {
		return Jamuz.getDb().updatePath(idPath, modifDate, checkedFlag, relativePath, mbId);
	}

    /**
     *
	 * @param recalculateGain
     * @param readTags
     * @param progressBar
     * @return
     */
    public boolean browse(boolean recalculateGain, boolean readTags, ProgressBar progressBar) {
        return browse(recalculateGain, readTags, progressBar, true);
    }
    
	/**
	 * Browse folder
	 * @param readTags 
     * @param progressBar 
	 * @return
	 */
	private boolean browse(boolean recalculateGain, boolean readTags, ProgressBar progressBar, boolean transcode) {

		//TODO: Manage potential parsing errors when options are not valid csv
		List <String> filesAudioExtensions = new ArrayList(
				Arrays.asList(Jamuz.getMachine().getOptionValue("files.audio")
						.split(","))); //NOI18N
        List <String> filesImageExtensions = new ArrayList(
				Arrays.asList(Jamuz.getMachine().getOptionValue("files.image")
						.split(","))); //NOI18N
        List <String> filesDeletableExtensions = new ArrayList(
				Arrays.asList(Jamuz.getMachine().getOptionValue("files.delete")
						.split(","))); //NOI18N
		filesConvertibleExtensions = new HashMap<>();
		for(String string : Jamuz.getMachine().getOptionValue("files.convert")
				.split(",")) { //NOI18N
            String[] strings = string.split(":"); //NOI18N
			filesConvertibleExtensions.put(strings[0], strings[1]);
        }
		
		filesAudio.clear();
        filesAudioTableModel.clear();
		filesOther.clear();
		filesImage.clear();
		filesConvertible.clear();
		File path = new File(fullPath);
		File[] files = path.listFiles();
		if (files != null) {
			progressBar.setup(files.length);
			progressBar.setMsgMax(30);
			for (File file : files) {
				String absolutePath=file.getAbsolutePath();
				String relativeFullPath=absolutePath.substring(rootPath.length());

				if (!file.isDirectory()) {
					FileInfoInt myFileInfoNew = new FileInfoDisplay(relativeFullPath, rootPath);
					if(filesAudioExtensions.contains(myFileInfoNew.getExt())) {
						if(readTags) {
							myFileInfoNew.readTags(false);//TODO: Use returned boolean ! (shall we ?)
						} 
                        FileInfoDisplay fileInfoDisplay = (FileInfoDisplay) myFileInfoNew;
                        fileInfoDisplay.initDisplay();
						filesAudio.add(fileInfoDisplay);
					}
					else if(filesImageExtensions.contains(myFileInfoNew.getExt())) {
						filesImage.add(myFileInfoNew);
					}
					else if(filesConvertibleExtensions.containsKey(myFileInfoNew.getExt())) {
						if(readTags) {
							myFileInfoNew.readTags(false);
						}
						filesConvertible.add(myFileInfoNew);
					}
					else if(filesDeletableExtensions.contains(myFileInfoNew.getExt())) {
                        //Direct deletion if
                        file.delete();
					}
					else {
						filesOther.add(myFileInfoNew);
					}
				}
//				progressBar.progress(": "+file.getName());  //NOI18N
                progressBar.progress(": "+relativeFullPath);  //NOI18N
			}
			progressBar.reset();
			Collections.sort(filesAudio);
			
			//Delete image files if no audio, no convertible 
			// and no other (not know has being deletable - so deleted) files have been found
			if(filesAudio.size()<=0) {
				if(filesConvertible.size()<=0) {
					if(filesOther.size()<=0){
						deleteList(filesImage, null);
						return true;
					}
				}
			}
			
			//Transcode convertible files
			if(transcode && filesConvertible.size()>0) {
				transcode(progressBar);
			}

			//FIXME PLAYER ReplayGain. Complete and test, then use (and on remote too)
			if(!isReplayGainDone || recalculateGain) {
				//http://www.bobulous.org.uk/misc/Replay-Gain-in-Linux.html				
				//http://id3.org/id3v2.3.0#User_defined_text_information_frame
				
				//Get ReplayGain values from files
				boolean isValid=true;
				for(FileInfoDisplay fileInfoDisplay : filesAudio) {
					GainValues gv = fileInfoDisplay.getReplayGain(false);
					System.out.println("ReplayGain: "+gv+" // "+gv.isValid()+" // " + fileInfoDisplay.getFullPath());
					if(!gv.isValid()) {
						isValid=false;
						break; //No need to read others
					}
				}
				if(!isValid || recalculateGain) {
					//Compute replaygain for MP3 files (if any)
					MP3gain mP3gain = new MP3gain(recalculateGain, rootPath, 
							relativePath, progressBar);
					if(mP3gain.process()) {
						filesAudio.stream().forEach((fileInfoDisplay) -> {
							GainValues gv = fileInfoDisplay.getReplayGain(true);
							System.out.println("ReplayGain: "+gv+" // "+gv.isValid()+" // " + fileInfoDisplay.getFullPath());
							fileInfoDisplay.saveReplayGainToID3(gv);
						});
					}
					//Compute Replaygain for FLAC files (if any)
					progressBar.setIndeterminate("Computing ReplayGain for FLAC ...");
					MetaFlac metaFlac = new MetaFlac(getFullPath());
					if(metaFlac.process()) {
						//Youpi :)
					}
					//TODO: Compute ReplayGain for OGG
				}
				progressBar.reset();
				isReplayGainDone=true;
			}

			return true;
		}
		else {
			return false;
		}
	}
    
	/**
	 * Transcode convertible files  
     * @param progressBar
	 */
	public void transcode(ProgressBar progressBar) {
		File source;
		File target;
		String basePath;
		progressBar.setup(filesConvertible.size());
		for (FileInfoInt file : filesConvertible) {
			try {							
				String destExt=filesConvertibleExtensions.get(file.getExt());
				progressBar.progress(MessageFormat.format(Inter.get("Msg.Check.Transcoding"), destExt));  //NOI18N
				
				basePath=file.getFullPath().getAbsolutePath();
				source = new File(FilenameUtils.concat(basePath, file.getFilename()));
				
				AudioAttributes audio = new AudioAttributes();
				audio.setBitRate(192000);
				audio.setChannels(2);
				audio.setSamplingRate(44100);
				EncodingAttributes attrs = new EncodingAttributes();
				switch (destExt) {
					case "ogg": //NOI18N
						//libvorbis works on windows but fails on linux mint
						//vorbis works on both :)
						audio.setCodec("vorbis");  //NOI18N
						attrs.setFormat("ogg");  //NOI18N
						break;
					case "mp3": //NOI18N
					default: //Encoding to MP3 by default
						destExt="mp3"; //NOI18N
						audio.setCodec("libmp3lame");  //NOI18N
						attrs.setFormat("mp3");  //NOI18N
						break;
				}
				target = new File(FilenameUtils.concat(basePath, FilenameUtils.getBaseName(file.getFilename())+"."+destExt));  //NOI18N
				attrs.setAudioAttributes(audio);
				//Encode
				Encoder encoder = new Encoder();
				encoder.encode(source, target, attrs);
				//Apply previously read tags to new 
				file.restoreTags(destExt);
                //Delete source now that it hs been transcoded
                source.delete();

			} catch (IllegalArgumentException | EncoderException ex) {
				Popup.error(ex);
			}
		}
		progressBar.reset();
		browse(false, true, progressBar, false); //last false as we do not want to transcode in loop in case of an error
	}
	
    /**
	 * Save tags
	 * @param deleteComment
     * @param progressBar
	 */
	public void save(final boolean deleteComment, ProgressBar progressBar) {
        File folder = new File(getFullPath());
        //TODO: use return bool from below
        saveTags(deleteComment, progressBar);
        //If checking master library (ie: not new nor not a master library)
        if(isCheckingMasterLibrary()) {
            //Change folder modification date in database
            modifDate = new Date(folder.lastModified());
            updateInDb(getCheckedFlag());
            //Scan to update database
            scan(true, progressBar);
        }
	}
    
    /**
     * Get first cover from tag
     * @return
     */
    public BufferedImage getFirstCoverFromTags() {
        BufferedImage myImage=null;
        for(FileInfoInt myFileInfoInt : getFilesAudio()) {
            if(myFileInfoInt.getNbCovers()>0) {
                myImage = myFileInfoInt.getCoverImage();
                if(myImage!=null) {
                    break;
                }
            }
        }
        return myImage;
    }
    
	/**
	 * Save tags to files
	 * @param tableModel
	 * @param deleteComment
	 * @return  
	 */
	private boolean saveTags(boolean deleteComment, ProgressBar progressBar) {
		try {
            progressBar.setup(filesAudioTableModel.getFiles().size());

            //Save tags
			for(FileInfoDisplay file : filesAudioTableModel.getFiles()) {
                if(file.isAudioFile) {
                    //If newImage is null, it will not be saved
                    file.saveTags(newImage, deleteComment);
                }
                progressBar.progress(Inter.get("Msg.Check.SavingTags"));  //NOI18N
			}
			browse(false, true, progressBar);
			return true;

		} catch (Exception ex) {
			Popup.error(ex);
			return false;
		}
	}
	
	/**
	 * Deletes all files in folder
	 */
	private void deleteAllFiles(ProgressBar progressBar) {
		progressBar.setup(filesAudio.size()
				+getFilesOther().size()
				+getFilesConvertible().size()
				+getFilesImage().size());
		deleteList(filesAudio, progressBar);
		deleteList(getFilesOther(), progressBar);
		deleteList(getFilesConvertible(), progressBar);
		deleteList(getFilesImage(), progressBar);
        
        //TODO: If no more files in folder, remove the folder too
	}

	private void deleteList(List<? extends FileInfoInt> myList, ProgressBar progressBar) {
		for (FileInfoInt myFileInfo : myList) {
			File myFile = new File(rootPath + File.separator + myFileInfo.getRelativeFullPath()); 
            myFile.delete();
			if(progressBar!=null) {
				progressBar.progress(Inter.get("Msg.Check.DeletingFiles"));  //NOI18N
			}
		}
        if(progressBar!=null) {
            progressBar.reset();
        }
	}

    /**
     * delete all files in folder
     */
    private void delete(ProgressBar progressBar) {
        deleteAllFiles(progressBar);
        browse(false, false, progressBar);
        if(isCheckingMasterLibrary()) {
            scanDeleted(progressBar);
        }
	}
    
    /**
	 * Queue folder to player
	 */
	public void queueAll() {
        for (FileInfoInt myFileInfo : filesAudio) {
            PanelMain.addToQueue(myFileInfo, ProcessCheck.getRootLocation().getValue()); 
        }
        PanelMain.playSelected(false);
	}
    
	//TODO: Move this to a dedicated class
	/**
	 *
	 * @param list
	 * @param groupBy
	 * @return
	 */
    public static ArrayList<String> group(List list, String groupBy){
		ArrayList<String> myGroupList = new ArrayList<>();

		for(Object obj : list){
			Class<?> klass = obj.getClass();

			try {
				// dynamic method invocation
				Method m = klass.getMethod(groupBy);
				Object result = m.invoke(obj);
				String resultAsKey = result.toString();

				if(!myGroupList.contains(resultAsKey)) {
					myGroupList.add(resultAsKey);
				}

			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
				Jamuz.getLogger().log(Level.SEVERE, "", ex);  //NOI18N
				return myGroupList;
			} 
		}
		//Sort the list
		Collections.sort(myGroupList);
		return myGroupList;
	}
	
	/**
	 * Analyse folder
     * @param progressBar
     * @throws java.lang.CloneNotSupportedException
	 */
	public void analyse(ProgressBar progressBar) throws CloneNotSupportedException {
		
        progressBar.setIndeterminate(Inter.get("Msg.Check.AnalyzingFolder")); //NOI18N
        
		coversTag = new ArrayList<>();
         //Needed here even if used in analyseMatch only as causes a bug in getCoverList (called before) otherwise
		originals = new ArrayList<>();
		
        filesAudioTableModel.clear();
		
		results = new HashMap<>();

		results.put("nbFiles", new FolderInfoResult());  //NOI18N
		results.put("hasID3v1", new FolderInfoResult());		  //NOI18N
        results.put("isReplayGainDone", new FolderInfoResult());  //NOI18N
		//TODO: Analyse ReplayGain (to be read first of course)
		results.put("cover", new FolderInfoResult());  //NOI18N
		int meanBitRate=0;
        results.put("bitRate", new FolderInfoResult());  //NOI18N
        results.put("length", new FolderInfoResult());  //NOI18N
		results.put("size", new FolderInfoResult());  //NOI18N
        results.put("format", new FolderInfoResult());  //NOI18N
        results.put("discNoFull", new FolderInfoResult());  //NOI18N
		results.put("trackNoFull", new FolderInfoResult());  //NOI18N
        results.put("comment", new FolderInfoResult());  //NOI18N
		results.put("artist", new FolderInfoResult());  //NOI18N
		String searchArtist="";  //NOI18N
		results.put("title", new FolderInfoResult());  //NOI18N
		results.put("bpm", new FolderInfoResult());  //NOI18N
		results.put("year", new FolderInfoResult());  //NOI18N
		results.put("genre", new FolderInfoResult());  //NOI18N
		results.put("albumArtist", new FolderInfoResult());  //NOI18N
		results.put("album", new FolderInfoResult());  //NOI18N
		String searchAlbum="";  //NOI18N
        results.put("duplicates", new FolderInfoResult());  //NOI18N

		String userAgent="tst"; //TODO: What does this userAgent means ??  //NOI18N
		Caller.getInstance().setUserAgent(userAgent);

		Proxy proxy = Jamuz.getProxy();
		if(proxy!=null) {
			Caller.getInstance().setProxy(proxy);
		}
	
		results.get("nbFiles").value=String.valueOf(filesAudio.size());  //NOI18N
		results.get("nbFiles").tooltip=Inter.get("Tooltip.NumberOfFiles");  //NOI18N
		
		int nbNonAudioFiles = filesOther.size();
		nbNonAudioFiles += filesImage.size();
		nbNonAudioFiles += filesConvertible.size();
		
		if(nbNonAudioFiles<=0 && filesAudio.size()<=0) {
			results.get("nbFiles").setKO();  //NOI18N
			results.get("nbFiles").tooltip=Inter.get("Tooltip.NoFilesFound");  //NOI18N
		}
		else if(filesAudio.size()<=0) {
			results.get("nbFiles").setKO();  //NOI18N
			results.get("nbFiles").tooltip=Inter.get("Tooltip.NoSupportedAudioFiles");  //NOI18N
            
            for(FileInfoInt fileInfo : getFilesConvertible()) {
                addRowTag(FolderInfoResult.colorField(fileInfo.getFilename(), 0));
            }
            for(FileInfoInt fileInfo : getFilesImage()) {
                addRowTag(FolderInfoResult.colorField(fileInfo.getFilename(), 1));
            }
            for(FileInfoInt fileInfo : getFilesOther()) {
                addRowTag(FolderInfoResult.colorField(fileInfo.getFilename(), 2));
            }
		}
		else {
			//Analyse hasID3v1
			ArrayList<String> hasID3v1List = group(filesAudio, "hasID3v1");  //NOI18N
			if(hasID3v1List.contains("true")) {  //NOI18N
				results.get("hasID3v1").value=Inter.get("Label.Yes");  //NOI18N
				results.get("hasID3v1").setKO();  //NOI18N
			}
			else {
				results.get("hasID3v1").value=Inter.get("Label.No");  //NOI18N
			}
			
			//Analyse isReplayGainDone
			if(!isReplayGainDone) {
				results.get("isReplayGainDone").value=Inter.get("Label.No");  //NOI18N
				results.get("isReplayGainDone").setKO();  //NOI18N
			}
			else {
				results.get("isReplayGainDone").value=Inter.get("Label.Yes");  //NOI18N
			}
			
			//Analyse number of covers
			double mean=0;
			for(FileInfoInt audioFile : filesAudio) {
				mean+=audioFile.getNbCovers();
			}
			mean=mean/filesAudio.size();
			if(mean<1) {
				//Some files do not have a cover
				results.get("cover").setKO();  //NOI18N
				results.get("cover").value=Inter.get("Label.Check.MissingCover"); //NOI18N
			}
			else if(mean>1) {
				results.get("cover").setKO();  //NOI18N
				results.get("cover").value=Inter.get("Label.Check.ExtraCover"); //NOI18N
			}
			else { //mean==1
				//All files have only one cover
				//Analyse cover Hash
				for(FileInfoInt audioFile : filesAudio) { //Need to read image from tag to be able to read hash
					audioFile.getCoverImage();
				}
				ArrayList<String> coverHashList = group(filesAudio, "getCoverHash");  //NOI18N
				if(coverHashList.contains("")) { //NOI18N
					results.get("cover").setKO();  //NOI18N
					results.get("cover").value=Inter.get("Label.Check.HashIssue"); //NOI18N
				}
				else if(coverHashList.size()!=1) {  //NOI18N
					results.get("cover").setKO();  //NOI18N
					results.get("cover").value=String.valueOf(coverHashList.size())+" &ne;"; // "&ne;" => "≠" //NOI18N
				}
				else {
					BufferedImage myImage=filesAudio.get(0).getCoverImage();
					results.get("cover").value = Inter.get("Label.All")+" "+myImage.getWidth()+"x"+myImage.getHeight(); //NOI18N
					if(myImage.getWidth()<200 || myImage.getHeight()<200) {
						results.get("cover").setWarning(); //NOI18N
					}
					else if(myImage.getWidth()<100 || myImage.getHeight()<100) {
						results.get("cover").setKO(); //NOI18N
					}
				}
			}
            
			//Get YEAR, if all the same and valid
			ArrayList<String> yearList = group(filesAudio, "getYear");  //NOI18N
			if(yearList.size()==1) {
				if(yearList.get(0).equals("")) {  //NOI18N
					results.get("year").value="{Empty}";  //NOI18N
					results.get("year").setWarning(); //NOI18N
				}
				else if(yearList.get(0).matches("\\d{4}")) {  //NOI18N
					results.get("year").value=yearList.get(0);  //NOI18N
				}
				else {
					results.get("year").value="{Error}";  //NOI18N
					results.get("year").setKO(); //NOI18N
				}
			}
			else {
				results.get("year").value="{Multi}";  //NOI18N
				results.get("year").setWarning(); //NOI18N
			}

			//Get GENRE, if all the same and valid
			ArrayList<String> genreList = group(filesAudio, "getGenre");  //NOI18N
			if(genreList.size()==1) {
                //TODO: Use genre cache (in some combo or else, not to query db each time !!)
				if(Jamuz.getDb().checkGenre(genreList.get(0))) {
					results.get("genre").value=genreList.get(0);  //NOI18N
				}
			}
			
			//Get artistDisplay for matches search. Can be Album Artist if all the same and not empty
			ArrayList<String> artistList = group(filesAudio, "getArtist");  //NOI18N
			if(!artistList.contains("")){  //NOI18N
				if(artistList.size()>1) {
					searchArtist="Various Artists";  //NOI18N
				}
				else {
					searchArtist=artistList.get(0);
				}
			}
			//Get ALBUMARTIST
			ArrayList<String> albumArtistList = group(filesAudio, "getAlbumArtist");  //NOI18N
			if(!albumArtistList.contains("")) {  //NOI18N
				if(albumArtistList.size()==1){
					results.get("albumArtist").value=albumArtistList.get(0);  //NOI18N
					searchArtist=albumArtistList.get(0);
				}
				else {
					results.get("albumArtist").value="{Multi}";  //NOI18N
					results.get("albumArtist").setKO();  //NOI18N
				}
			}
			else {
				results.get("albumArtist").value="{Empty}";  //NOI18N
				results.get("albumArtist").setKO();  //NOI18N
			}
			
			//Analyse ALBUM
			ArrayList<String> albumList = group(filesAudio, "getAlbum");  //NOI18N
			if(!albumList.contains("")) {  //NOI18N
				if(albumList.size()==1){
					results.get("album").value=albumList.get(0);  //NOI18N
					searchAlbum=albumList.get(0);
				}
				else {
					results.get("album").value="{Multi}";  //NOI18N
					results.get("album").setKO();  //NOI18N
				}
			}
			else {
				results.get("album").value="{Empty}";  //NOI18N
				results.get("album").setKO();  //NOI18N
			}

			//TODO: Make this configurable in options
			ArrayList supportedFormats = new ArrayList<>();
			supportedFormats.add("MPEG-1 Layer 3"); //NOI18N
			supportedFormats.add("MPEG-2 Layer 3"); //NOI18N
			supportedFormats.add("Ogg Vorbis v1"); //NOI18N
            supportedFormats.add("FLAC 16 bits"); //NOI18N
			
			//FILE BY FILE ANALYSIS
            progressBar.setup(filesAudio.size());
			for(FileInfoDisplay audioFile : filesAudio) {
                
                //COVER
                if(audioFile.getNbCovers()>0) {
                    if(!containsCoverHash(audioFile.getCoverHash())) {
                        coversTag.add(new Cover(
								audioFile.getFilename(), 
								audioFile.getCoverImage(), 
								audioFile.getCoverHash()));
                    }
                }

                //Analyze BITRATE
                if(audioFile.getBitRate().equals("")) {  //NOI18N
                    results.get("bitRate").setKO();  //NOI18N
                    audioFile.bitRateDisplay=FolderInfoResult
							.colorField(audioFile.getBitRate(), 2);
                }
                else {
                    String tempBitRate = audioFile.getBitRate();
                    if(tempBitRate.startsWith("~")) {  //NOI18N
                        tempBitRate=tempBitRate.substring(1);
                    }
//                    int tempBitRateInt=Integer.parseInt(tempBitRate);
                    double tempBitRateDouble = Double.parseDouble(tempBitRate);
                    meanBitRate+=tempBitRateDouble;
                    if(tempBitRateDouble<128) {
                        results.get("bitRate").setWarning();  //NOI18N
                        audioFile.bitRateDisplay=FolderInfoResult
								.colorField(audioFile.getBitRate(), 1);
                    }
                    else {
                        audioFile.bitRateDisplay=FolderInfoResult
								.colorField(audioFile.getBitRate(), 0);
                    }
                }

                //Analyze LENGTH
                if(audioFile.getLength() <= 0) {
                    results.get("length").setKO();  //NOI18N
                    audioFile.setLengthDisplay(FolderInfoResult.colorField(audioFile.getLengthDisplay(), 2));
                }
                else if(audioFile.getLength() < 30) {
                    results.get("length").setWarning();  //NOI18N
                    audioFile.setLengthDisplay(FolderInfoResult.colorField(audioFile.getLengthDisplay(), 1));
                }
                else {
                    audioFile.setLengthDisplay(FolderInfoResult.colorField(audioFile.getLengthDisplay(), 0));
                }

                //Analyze SIZE
				//FIXME SCAN We should delete files with (audioFile.getSize() <= 0) as:
				//makes writing tags crashing
				//kodi does not include those in library so they end up not found during merge
				//not valid anyway and cause other problems
				//=> BETTER to do this at scan level
				// Mean time, use :
				//	find . -size 0 -print0 | xargs -0 rm
				//to delete all 0o files recursively in current folder and below
                if(audioFile.getSize() <= 0) {
                    results.get("size").setKO();  //NOI18N
                    audioFile.setSizeDisplay(FolderInfoResult.colorField(audioFile.getSizeDisplay(), 2));
                }
                else if(audioFile.getSize() < 400000) {
                    results.get("size").setWarning();  //NOI18N
                    audioFile.setSizeDisplay(FolderInfoResult.colorField(audioFile.getSizeDisplay(), 1));
                }
                else {
                    audioFile.setSizeDisplay(FolderInfoResult.colorField(audioFile.getSizeDisplay(), 0));
                }

                //Analyse FORMAT
                if(audioFile.getFormat().equals("")) {  //NOI18N
                    //This should never happen
                    results.get("format").setKO();  //NOI18N
                    audioFile.formatDisplay=FolderInfoResult.colorField(audioFile.getFormat(), 2);
                }
                else if(!supportedFormats.contains(audioFile.getFormat())) {  //NOI18N
                    results.get("format").setKO();  //NOI18N
                    audioFile.formatDisplay=FolderInfoResult.colorField(audioFile.getFormat(), 2);
                }
                else {
                    audioFile.formatDisplay=FolderInfoResult.colorField(audioFile.getFormat(), 0);
                }

                filesAudioTableModel.addRow((FileInfoDisplay) audioFile.clone());
                
                progressBar.progress(Inter.get("Msg.Check.AnalyzingFolder")+ audioFile.getFilename());
			}
			progressBar.setIndeterminate(Inter.get("Msg.Check.AnalyzingFolder")); //NOI18N
            
			//Analyse mean BITRATE
			meanBitRate = meanBitRate / filesAudio.size();
			//TODO: Looks like (to be further analysed) that ogg have lower bitrates
			//at least with my test convertion of WMA 128, which results to a mean bitrate of 82 (74 to 93)
			if(meanBitRate<128) {
				results.get("bitRate").setKO();  //NOI18N
			}
			results.get("bitRate").value=String.valueOf(meanBitRate);  //NOI18N
			
			//Searching matches on MusicBrainz and Last.fm
			//(Only if a valid artistDisplay could be retrieved)
			if(!searchArtist.equals("")) {  //NOI18N
				int discNo=1;
				int discTotal=1;
				ArrayList<String> discNoList = group(filesAudio, "getDiscNo");  //NOI18N
				if(!discNoList.contains("") && discNoList.size()==1) {
					discNo=Integer.valueOf(discNoList.get(0));
				}
				ArrayList<String> discTotalList = group(filesAudio, "getDiscTotal");  //NOI18N
				if(!discTotalList.contains("") && discTotalList.size()==1){
					discTotal=Integer.valueOf(discTotalList.get(0));
				}
				searchMatches(searchAlbum, searchArtist, discNo, discTotal, progressBar);
			}
			progressBar.setIndeterminate(Inter.get("Msg.Check.AnalyzingFolder")); //NOI18N
            
			//Add original(s) artistDisplay/album/year to originals list
			ArrayList<String> releaseList = group(filesAudio, "getRelease");  //NOI18N
			int i=1;
			for (String myRelease : releaseList) {
				String[] split = myRelease.split("X7IzQsi3");  //NOI18N //TODO: Use something nicer than this bad coding
				addToOriginals(Inter.get("Label.original")+i, split[0], split[1], split[2], Integer.parseInt(split[3]), idPath);  //NOI18N
			}
			
			//Add path parts to originals list (may be usefull when no tags are available)
			String path=filesAudio.get(0).getRelativePath();
			File file = new File(path);
			
			if(file.getPath().contains(File.separator)) {
				if(file.getParent().equals("")) {  //NOI18N
					addToOriginals(Inter.get("Label.File"), file.getName(), "", "", 0, idPath);  //NOI18N
				}
				else {
					addToOriginals(Inter.get("Label.File"), file.getParentFile().getPath(), file.getName(), "", 0, idPath);  //NOI18N
				}
			}
			else {
				addToOriginals(Inter.get("Label.File"), file.getPath(), "", "", 0, idPath);  //NOI18N
			}
			
		}
	}
	
    private boolean containsCoverHash(String hash) {
		for(Cover cover : coversTag) {
			if(cover.getHash().equals(hash)) {
                return true;
            }
		}
		return false;
	}
    
	/**
	 * Gets given match
	 * @param matchId
	 * @return
	 */
	public ReleaseMatch getMatch(int matchId) {
        //TODO: Why matchesL and not matches directly ???
        List<ReleaseMatch> matchesL = getMatches();
        if(matchesL==null) return null;
        
        if(matchId < matchesL.size()) {
            return matchesL.get(matchId);
        }
        else {
            matchId=matchId-matchesL.size();
            if(matchId < originals.size()) {
                return originals.get(matchId);
            }
            else {
                return null;
            }
        }
	}
	
	/**
	 * Analyse match againts folder information
     * @param matchId
     * @param progressBar
	 * @throws java.lang.CloneNotSupportedException
	 */
	public void analyseMatch(int matchId, ProgressBar progressBar) throws CloneNotSupportedException {
        
        ReleaseMatch match = getMatch(matchId);
        if(match==null) {
            return;
        }
		//Assume no duplicates at first
		results.get("duplicates").setOK();  //NOI18N
		if(match.isIsErrorDuplicate()) {
			results.get("duplicates").setKO();  //NOI18N
		}
		if(match.isIsWarningDuplicate()) {
			results.get("duplicates").setWarning();  //NOI18N
		}
		
		results.get("nbFiles").restoreFolderErrorLevel(); //NOI18N
		//Get match tracks
		List<Track> tracks=match.getTracks(progressBar);
        progressBar.setIndeterminate(Inter.get("Msg.Scan.SearchingMatches"));  //NOI18N
		if(match.isOriginal()) {
			results.get("nbFiles").tooltip=Inter.get("Tooltip.OriginalMatch");  //NOI18N
			results.get("nbFiles").setWarning();  //NOI18N
		}
		else {
			if(tracks.size()<=0) {
				results.get("nbFiles").tooltip=Inter.get("Tooltip.MatchHasNoTracks");  //NOI18N
				results.get("nbFiles").setWarning();  //NOI18N
			}
			else {
				if(tracks.size() != filesAudio.size()) {
					results.get("nbFiles").tooltip=Inter.get("Tooltip.NumberOfTracksDiffer");  //NOI18N
					results.get("nbFiles").setKO();  //NOI18N
				}
				else {
					//Note: If "nbFiles" has errorlevel set >0 during folder analysis
					//then we will not look for matches as no supported audio files found to search for
					//So we can change resultsMap.get("nbFiles") as we like without interference
					results.get("nbFiles").tooltip=Inter.get("Tooltip.NumberOfFiles");  //NOI18N
					results.get("nbFiles").setOK();  //NOI18N
				}
			}
		}
        
        //Analyse if match has a year
        results.get("year").restoreFolderErrorLevel(); //NOI18N
        results.get("year").tooltip=null; //NOI18N
        if(match.getYear().equals("")) { //NOI18N
            results.get("year").tooltip=Inter.get("Tooltip.MatchHasNoYear"); //NOI18N
            results.get("year").setWarning(true);  //NOI18N
        }
        
        ReleaseMatch.Track track;
        int i=0;
        filesAudioTableModel.clear();
        for(FileInfoInt fileAudio : filesAudio) {
            if(i<tracks.size()) {
                track=tracks.get(i);
            }
            else {
                track = new ReleaseMatch.Track(fileAudio.getDiscNo(), fileAudio.getDiscTotal(), 
                        fileAudio.getTrackNo(), fileAudio.getTrackTotal(), 
                        fileAudio.getArtist(), fileAudio.getTitle(), 
                        Long.valueOf(0), "");  //NOI18N
            }
//            FileInfoDisplay fileInfoDisplay = new FileInfoDisplay(fileAudio);
            FileInfoDisplay fileInfoDisplay = (FileInfoDisplay) fileAudio.clone();
//            fileInfoDisplay.init();
            //Set new values from existing file as not available in match
//            fileInfoDisplay.filename=fileAudio.filename;
//            fileInfoDisplay.setGenre(fileAudio.genre);
//            fileInfoDisplay.BPM=fileAudio.BPM;
            //Set new values from match
            fileInfoDisplay.setTrack(track);
            if(!match.getYear().equals("")) { //NOI18N
                fileInfoDisplay.setYear(match.getYear());
            }
            else {
                fileInfoDisplay.setYear(fileAudio.getYear());
            }   

            fileInfoDisplay.setAlbumArtist(match.getArtist());
            fileInfoDisplay.setAlbum(match.getAlbum());

            fileInfoDisplay.isAudioFile=true;
            fileInfoDisplay.coverIconDisplay=IconBufferCover.getCoverIcon(fileAudio, true);
            addRowTag(fileInfoDisplay);
            i++;
        }
        //Add potential extra titles from match
        for (int j=i; j<tracks.size(); j++) {
            track=tracks.get(j);
            //TODO: Pass match too to set new fields artist, album, albumArtist, year
            //bpm=""
            //THEN UDPDATE TableModelCheck.moveRow function accordingly (refer to title which is properly set)
            addRowTag(track);
        }
	}
	
    private void addRowTag(FileInfoDisplay fileInfoDisplay) {
        //Genre
		fileInfoDisplay.setGenre(PanelMain.getGenre(fileInfoDisplay.getGenre()));
		filesAudioTableModel.addRow(fileInfoDisplay);
	}
	
	private void addRowTag(ReleaseMatch.Track track) {
		FileInfoDisplay fileInfoDisplay = new FileInfoDisplay(track);
		filesAudioTableModel.addRow(fileInfoDisplay);
	}
	
	private void addRowTag(String filename) {
		FileInfoDisplay fileInfoDisplay = new FileInfoDisplay(filename);
		filesAudioTableModel.addRow(fileInfoDisplay);
	}
    
	/**
	 * analyse match tracks
	 */
	public void analyseMatchTracks() {
        //TODO: Get this list from tableModel
		//Restore Folder error levels
        List<Integer> editableColumns = new ArrayList<>();
        editableColumns.add(1);
        editableColumns.add(3);
        editableColumns.add(5);
        editableColumns.add(7);
        editableColumns.add(9);
        editableColumns.add(11);
        editableColumns.add(19);
        editableColumns.add(21);
        editableColumns.add(24);
        for(int colId : editableColumns) {
            results.get(FolderInfo.getField(colId)).restoreFolderErrorLevel();  //NOI18N
        }
        editableColumns.add(13);//Add year, not before as we do not want to restore error level;
        //Analyse tracks
		for(int rowId=0; rowId < filesAudioTableModel.getRowCount(); rowId++) {
			for(int colId : editableColumns) {
                analyseMatchTrack(rowId, colId);
            }
		}
	}
	
	//TODO: Move this to TableModel

	/**
	 *
	 * @param colId
	 * @return
	 */
    public static String getField(int colId) {
        String field; //NOI18N
        switch (colId) {
			case 1: field="discNoFull";	break; //NOI18N
			case 3: field="trackNoFull"; break; //NOI18N
			case 5: field="artist";	break; //NOI18N
			case 7: field="title"; break; //NOI18N
			case 9: field="genre"; break; //NOI18N
			case 11: field="album"; break; //NOI18N
			case 13: field="year"; break; //NOI18N
			case 19: field="albumArtist"; break; //NOI18N
			case 21: field="comment"; break; //NOI18N
			case 24: field="bpm"; break; //NOI18N
            default: field=""; //NOI18N
		}
        return field;
    }

	/**
	 *
	 * @param colId
	 */
	public void analyseMatchTracks(int colId) {
        
        results.get(FolderInfo.getField(colId)).restoreFolderErrorLevel();  //NOI18N
		
		for(int rowId=0; rowId < filesAudioTableModel.getRowCount(); rowId++) {
			analyseMatchTrack(rowId, colId);
		}
    }
    
	private void analyseMatchTrack(int rowId, int colId) {
		String field=getField(colId);
        TableValue tagValue = (TableValue) filesAudioTableModel.getValueAt(rowId, colId+1);
        
        Object newValueObject = filesAudioTableModel.getValueAt(rowId, colId);
        //TODO: use polymorphism instead
        if (newValueObject instanceof String) {
            String newValue = (String) newValueObject;
            tagValue.setDisplay(results.get(field).analyseTrack(tagValue.getValue(), newValue, field));
        } else if (newValueObject instanceof Float) { //This is for BPM
            Float newValue = (Float) newValueObject;
            Float tagValueFloat;
            try {
                tagValueFloat=Float.parseFloat(tagValue.getValue());
            }
            catch(java.lang.NumberFormatException ex) {
                tagValueFloat=Float.valueOf(0);
            }
            tagValue.setDisplay(results.get(field).analyseTrackBpm(tagValueFloat, newValue));
        } else {
            Popup.error("Unknown class");
        }
	}
	
	//Used in :
	//analyse()
	private void addToOriginals(String source, String artist, String album, String year, int trackTotal, int idPath) {
		ReleaseMatch myMatch = new ReleaseMatch(-1, source, artist, album, year, trackTotal, idPath);
		originals.add(myMatch);
	}
	
	/**
	 * Search matches
	 * @param album
	 * @param artist
	 * @param discNo
	 * @param discTotal
     * @param progressBar
	 * @return
	 */
	public boolean searchMatches(String album, 
			String artist,
			int discNo,
			int discTotal, 
			ProgressBar progressBar) {
        progressBar.setIndeterminate(Inter.get("Label.Searching"));  //NOI18N
        searchKey = album+artist;
        ReleaseMB releaseMB = new ReleaseMB(progressBar);
        ReleaseLastFm releaseLastFm = new ReleaseLastFm();
        if(matches.containsKey(searchKey)) {
            if(matches.get(searchKey)==null) {
                //Remove map entry if entry was null (connexion issue) so it can be retried
                matches.remove(searchKey);
            }
        }
        if(!matches.containsKey(searchKey)) {
            //Query MusicBrainz (better results, usually including yearDisplay and tracks)
            //Query Last.fm (in case no luck with MusicBrainz)
            
            List<ReleaseMatch> releases = releaseMB.search(
					artist, 
					album, 
					filesAudio.size(), 
					idPath, discNo, discTotal);
            progressBar.setIndeterminate(Inter.get("Label.Searching"));  //NOI18N
            if(releases!=null) {
                releases.addAll(releaseLastFm.search(artist, album, idPath));
                matches.put(searchKey, releases);
            }
            else {
                matches.put(searchKey, null); //Meaning search issue
            }
        }
        if(!coversInternet.containsKey(searchKey)) {
            //Adding Last.fm covers first (usually more results)
            List<Cover> searchedCovers = releaseLastFm.getCoverList();
            //Then adding MusicBrainz's covers (from covertartarchive)
            searchedCovers.addAll(releaseMB.getCoverList());
            coversInternet.put(searchKey, searchedCovers);
        }

		//TODO: Not always return true !
		return true;
	}
	
    private boolean isCheckingMasterLibrary() {
        //idPath = -1 when checking location.add (new files)
        return (idPath>0 
				&& Jamuz.getMachine().
						getOptionValue("library.isMaster").equals("true")); //NOI18N
    }
    
    /**
	 * Insert folder in database with given checkeFlag
	 */
	private void moveToLibrary(ProgressBar progressBar, CheckedFlag checkedFlag, boolean useMask) {
        //TODO: Check duplicates at this point again, as a previous OK (resulting in db insertion) may have inserted a duplicate !
        
        boolean updateDatabase=false;
        boolean checkDestination=false;
        if(isCheckingMasterLibrary()) {
            updateDatabase=true;
        }
        else {
            if(FilenameUtils.equalsNormalizedOnSystem(
						ProcessCheck.getDestinationLocation().getValue(), 
						Jamuz.getMachine().getOptionValue("location.library"))
					&& Jamuz.getMachine().getOptionValue("library.isMaster")
							.equals("true")) {  //NOI18N
                updateDatabase=true;
            }
            checkDestination=true;
        }
        //TODO: We can end up with duplicate strPath in path table
        //(at least) when a folder is renamed manually, then scan, and move to OK that renames the folder back:
        //need to check if file exist before inserting it !!
        moveList(filesAudio, 
				useMask, 
				MessageFormat.format(
						Inter.get("Msg.Check.MovingToOK"), 
						ProcessCheck.getDestinationLocation().getValue()), 
                ProcessCheck.getDestinationLocation().getValue(), 
				checkDestination, 
				progressBar);  //NOI18N

        //Change folder path
        //TODO: Find a better way (using getDestination on path only)
        String rootPathOri = getRootPath();
        String relativePathOri = getRelativePath();
        setPath(ProcessCheck.getDestinationLocation().getValue(), 
				filesAudio.get(0).getRelativePath());

        if(updateDatabase) {
            //Insert or update path in database
            progressBar.setIndeterminate(MessageFormat.format(
					Inter.get("Msg.Check.UpdatingFolderInDb"), 
					getRelativePath())); //NOI18N
            //insertOrUpdateInDb(checkedFlag); 
				//=> Cannot move insertOrUpdateInDb(checkedFlag); here 
				//   as isCheckingMasterLibrary() would always return true
            if(isCheckingMasterLibrary()) {
                insertOrUpdateInDb(checkedFlag);
                progressBar.setup(filesAudio.size());
                for(FileInfoInt fileInfo : filesAudio) {
                    fileInfo.updateInDb();
                    progressBar.progress(MessageFormat.format(
							Inter.get("Msg.Check.UpdatingFileInDb"), 
							fileInfo.getFilename())); //NOI18N
                }
            }
            else {
                insertOrUpdateInDb(checkedFlag);
                //Scan files for insertion of new files
                scan(true, progressBar);
            }
            progressBar.reset();
        }
        //Change path back so that source path is browsed (and scanned for deleted as required)
        setPath(rootPathOri, relativePathOri);
	}
    
	private void KO(ProgressBar progressBar) {
        if(isCheckingMasterLibrary()) {
            //TODO: Rename path and files as for OK
            //ONLY IF all needed tags are valid for location.mask
            //ie: same album artist and same album (for the path) as instance
            Jamuz.getDb().setCheckedFlag(idPath, FolderInfo.CheckedFlag.KO);
        }
        else {
            moveList(getAllFiles(), 
					false, 
					MessageFormat.format(
						Inter.get("Msg.Check.MovingToKO"), 
						ProcessCheck.getKoLocation().getValue()), 
					ProcessCheck.getKoLocation().getValue(), 
					true, progressBar);	  //NOI18N
        }
	}
    
    private boolean Manual(ProgressBar progressBar) {
        if(isCheckingMasterLibrary()) {
            Jamuz.getDb().setCheckedFlag(idPath, FolderInfo.CheckedFlag.UNCHECKED);
            return false;
        }
        else {
            moveList(getAllFiles(), 
					false, 
					MessageFormat.format(
						Inter.get("Msg.Check.MovingToManual"), 
						ProcessCheck.getManualLocation().getValue()), 
					ProcessCheck.getManualLocation().getValue(), 
					true, progressBar);	  //NOI18N
            return true;
        }
	}
    
	private List<FileInfoInt> getAllFiles() {
		List<FileInfoInt> merged = filesOther;
			merged.addAll(filesAudio);
            merged.addAll(filesConvertible);
            merged.addAll(filesImage);
		return merged;
	}
	
    private void moveList(
			List<? extends FileInfoInt> myList, 
			boolean useMask, 
			String msg, 
			String destination, 
			boolean checkDest, 
			ProgressBar progressBar) {
		File originalFile;
		String destinationRelativePath;
		File destinationFile;
		
		boolean doMove=true;
		if(checkDest && !checkDestination(myList, useMask, destination, progressBar)) {
			doMove=false;
		}
		if(doMove) {
			progressBar.setup(myList.size());
			for (FileInfoInt fileInfo : myList) {
				originalFile = new File(FilenameUtils.concat(
						ProcessCheck.getRootLocation().getValue(), 
						fileInfo.getRelativeFullPath())); 
				destinationRelativePath = getDestination(fileInfo, useMask);
				destinationFile = new File(FilenameUtils.concat(
						destination, 
						destinationRelativePath));
				if(!FilenameUtils.equalsNormalizedOnSystem(
						destinationFile.getAbsolutePath(), 
						originalFile.getAbsolutePath())) {
                    if(FileSystem.moveFile(originalFile, destinationFile)) {
                        fileInfo.setRootPath(destination);
                        fileInfo.setPath(destinationRelativePath);
                    }
                }
				progressBar.progress(msg);
			}
			progressBar.reset();
		}
	}
    
	/**
	 *
	 * @param progressBar
	 * @return
	 */
	public boolean doAction(ProgressBar progressBar) {
        if(!isActionDone) {
            switch(action) {
                case OK:
					moveToLibrary(progressBar, CheckedFlag.OK, true);
                    break;
                case WARNING:
                    moveToLibrary(progressBar, CheckedFlag.OK_WARNING, true);
                    break;
                case KO:
                    KO(progressBar);
                    break;
                case KO_LIBRARY:
					//FIXME CHECK Rename too, but need to insure that filename and path are valid
					//=> Move to an "Issues" folder and replace bad tags by "Missing artist", "Empty album" ...
                    moveToLibrary(progressBar, CheckedFlag.KO, false);
                    break;
                case MANUAL:
                    isActionDone = Manual(progressBar);
                    break;
                case DEL:
                    delete(progressBar);
                    break;
                case SAVE:
                    //TODO: Add an option for "delete comment ?"
                    save(true, progressBar);
                    break;
    //                case SEARCHING:
    //                    Nothing
            }
            if(action!=Action.MANUAL) {
                isActionDone=true;
            }
            if(isActionDone) {
                //Deleting potentially huge items, to prevent memory issues
                coversInternet=null;
                coversTag=null;
                filesAudio=null;
                filesAudioTableModel=null;
                filesConvertible=null;
                filesConvertibleExtensions=null;
                filesDb=null;
                filesImage=null;
                filesOther=null;
//                matches=null; //Used in toString, cannot remove
                newImage=null;
                originals=null;
//                results=null; //Used in toString, cannot remove
            }
        }
        return isActionDone;
    }

	/**
	 *
	 * @param isReplayGainDone
	 */
	public void setIsReplayGainDone(boolean isReplayGainDone) {
        this.isReplayGainDone = isReplayGainDone;
    }
    
    private boolean checkDestination(List<? extends FileInfoInt> myList, 
			boolean useMask, String destination, ProgressBar progressBar) {
		File destinationFile;
		progressBar.setIndeterminate(
				Inter.get("Msg.Check.CheckingDestinationFiles"));  //NOI18N
		for (FileInfoInt myFileInfo : myList) {
			destinationFile = new File(FilenameUtils.concat(
					destination, getDestination(myFileInfo, useMask))); 
			if (destinationFile.exists()) {
                Popup.warning(MessageFormat.format(
						Inter.get("Error.DestinationExist"), 
						destinationFile.toString()));  //NOI18N
				return false;
			}
		}
		return true;
	}
    
    private String getDestination(FileInfoInt fileInfo, boolean useMask) {
		if(useMask) {
			String destination = fileInfo.computeMask(
					Jamuz.getMachine().getOptionValue("location.mask"));  //NOI18N
			if(destination.length()>150) {
				destination=StringManager.Left(destination, 150)+"(_)";  //NOI18N
			}
			destination=destination+"."+fileInfo.getExt();  //NOI18N
			return destination; 
		}
		else {
			return fileInfo.getRelativeFullPath();
		}
	}
    
	/**
	 * Get list of covers
	 * @return
	 */
	public List<Cover> getCoverList() {
		ArrayList<Cover> coversList=new ArrayList<>();
		for (FileInfoInt myFileInfo : filesImage) {
			coversList.add(new Cover(CoverType.FILE, 
					myFileInfo.getFullPath().getAbsolutePath(), 
					myFileInfo.getFilename()));  //NOI18N
		}
		coversList.addAll(coversTag);
		if(searchKey!=null && coversInternet.containsKey(searchKey)) {
			coversList.addAll(coversInternet.get(searchKey));
		}
		return coversList;
	}
	
	/**
	 * Return list of audio files
	 * @return
	 */
	public List<FileInfoDisplay> getFilesAudio() {
		return filesAudio;
	}
	
	/**
	 * Return list of other files
	 * @return
	 */
	public List<FileInfoInt> getFilesOther() {
		return filesOther;
	}

	/**
	 * Return list of image files
	 * @return
	 */
	public List<FileInfoInt> getFilesImage() {
		return filesImage;
	}

	/**
	 * Return list of convertible files
	 * @return
	 */
	public List<FileInfoInt> getFilesConvertible() {
		Collections.sort(filesConvertible);
		return filesConvertible;
	}
	
	/**
	 * Return analysis results
	 * @return
	 */
	public Map<String, FolderInfoResult> getResults() {
		return results;
	}

	/**
	 * Return relative path
	 * @return
	 */
	public String getRelativePath() {
		return relativePath;
	}
	
	/**
	 * Return full path
	 * @return
	 */
	public String getFullPath() {
		return fullPath;
	}
	
	/**
	 * Return if folder is valid or not
	 * @return
	 */
	public boolean isValid() {
		for(FolderInfoResult result : results.values()) {
			if(result.isKO()) {
				return false;
			}
		}
		return true;
	}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String result;
        builder.append("<html><b>");
        builder.append(relativePath);
        builder.append("</b><BR/> | ");
        for (Map.Entry<String, FolderInfoResult> entry : results.entrySet()) {
            if(entry.getValue().errorLevel>0) {
                result=entry.getKey();
                if(!entry.getValue().value.equals("")) result+=":"+entry.getValue().value;
                builder.append(FolderInfoResult.colorField(result, entry.getValue().errorLevel, false));
				builder.append(" | "); //NOI18N
			}
        }
        List<ReleaseMatch> matchesL = getMatches();
        if(matchesL!=null) {
            if(matchesL.size()>0) {
                builder.append("<BR/>");
                builder.append(matchesL.get(0).toString());
                builder.append("<BR/>");
            }
        }
        builder.append("</html>");
        return builder.toString();
    }

    @Override
	public int compareTo(Object o) {
        //ORDER BY action
		if (action.getOrder() < ((FolderInfo) o).action.getOrder()) return -1;
		if (action.getOrder() > ((FolderInfo) o).action.getOrder()) return 1;
        return 0;
	}

    @Override
    public boolean equals(Object obj) {
		if(this == obj) {
            return true;
        }
		
		if (obj instanceof FolderInfo) {
			return super.equals(obj);
		}
		return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

	/**
	 * Return if folder has warnings
	 * @return
	 */
	public boolean isWarning() {
		for(FolderInfoResult result : results.values()) {
			if(result.isWarning()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Return checked flag
	 * @return
	 */
	public CheckedFlag getCheckedFlag() {
		return checkedFlag;
	}
	
	/**
	 * Return list of matches
	 * @return
	 */
	public List<ReleaseMatch> getMatches() {
        if(searchKey==null) {
            return new ArrayList<>();
        }
		return matches.get(searchKey);
	}
	
	/**
	 * Return list of originals
	 * @return
	 */
	public List<ReleaseMatch> getOriginals() {
		return originals;
	}

	/**
	 * Return modification date in SQL formatDisplay
	 * @return
	 */
	public String getModifDate() {
		//Returning a formatted String so that it is comparable
		//Filesystem may include ms whereas we store in below formatDisplay in database
		return DateTime.formatUTCtoSqlUTC(modifDate);
	}

	/**
	 * Return deleted flag
	 * @return
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * Return root path
	 * @return
	 */
	public String getRootPath() {
		return rootPath;
	}

    /**
     * Return audio files table model
     * @return
     */
    public TableModelCheckTracks getFilesAudioTableModel() {
        return filesAudioTableModel;
    }

	/**
	 *
	 * @return
	 */
	public BufferedImage getNewImage() {
        return newImage;
    }

	/**
	 *
	 * @param newImage
	 */
	public void setNewImage(BufferedImage newImage) {
        this.newImage = newImage;
    }
    
	/**
	 *
	 * @param text
	 */
	public void setNewGenre(String text) {
        filesAudioTableModel.setValueAt(text, 9);
    }

    void setNewYear(String text) {
        filesAudioTableModel.setValueAt(text, 13);
    }

    void setNewAlbum(String text) {
        filesAudioTableModel.setValueAt(text, 11);
    }

    void setNewArtist(String text) {
        filesAudioTableModel.setValueAt(text, 5);
    }
    
    void setNewAlbumArtist(String text) {
        filesAudioTableModel.setValueAt(text, 19);
    }

	/**
	 *
	 * @param mbId
	 */
	public void setMbId(String mbId) {
        this.mbId = mbId;
    }
	
	/**
	 * Checked Flag
	 */
    public enum CheckedFlag {
		/**
		 * Not yet checked
		 */
		UNCHECKED(Inter.get("Check.Unchecked"), 0, Color.WHITE),  //NOI18N

		/**
		 * KO: Something wrong
		 */
        KO(Inter.get("Check.KO"), 1, Color.RED), //NOI18N

		/**
		 * OK but with a warning
		 */
        OK_WARNING(Inter.get("Check.OK.Warning"), 2, Color.ORANGE), //NOI18N

		/**
		 * All tests OK, no duplicate, a "match" from internet matching all tracks and album info
		 */
        OK(Inter.get("Check.OK"), 3, new Color(0, 128, 0));  //NOI18N

		private final String display;
		private final int value;
        private final Color color;
        
		private CheckedFlag(String display, int value, Color color) {
			this.display = display;
			this.value = value;
            this.color = color;
		}
		@Override
		public String toString() {
			return display;
		}

		/**
		 * Return value
		 * @return
		 */
		public int getValue() {
			return value;
		}

		/**
		 * return color
		 * @return
		 */
		public Color getColor() {
            return color;
        }
	}
}