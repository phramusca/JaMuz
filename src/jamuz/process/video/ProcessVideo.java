/*
 * Copyright (C) 2013 phramusca ( https://github.com/phramusca/JaMuz/ )
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

package jamuz.process.video;

import jamuz.DbInfo;
import jamuz.Jamuz;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import jamuz.utils.Popup;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import jamuz.utils.FileSystem;
import jamuz.utils.SSH;
import jamuz.utils.Inter;
import jamuz.utils.ProcessAbstract;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Video process class
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ProcessVideo extends ProcessAbstract {
    
    private final TableModelVideo tableModel;
	private SSH myConn;
    private PathBuffer buffer;

    private boolean move;
    private boolean getDb;
	private boolean doSearch;

	/**
	 *
	 * @param name
	 */
	public ProcessVideo(String name) {
        super(name);
        tableModel = new TableModelVideo();
        buffer = new PathBuffer();
        move = false;
        getDb = false;
    }
    
    
    /**
     * Export selected files in a thred, updating PanelVideo
     */
    public void export() {
        PanelVideo.progressBar.setup(tableModel.getNbSelected());
        ProcessExport process = new ProcessExport("Thread.ProcessVideo.export");
        process.start();
    }

    private class ProcessExport extends ProcessAbstract {

        public ProcessExport(String name) {
            super(name);
        }
        
        @Override
        public void run() {
            this.resetAbort();
            try {
                exportFiles();
            } catch (InterruptedException ex) {
                Popup.error(Inter.get("Msg.Process.Aborted"), ex); //NOI18N
            }
            finally {
                PanelVideo.progressBar.reset();
                PanelVideo.enableProcess(true);
            }
        }
    }
    
    private boolean exportFiles() throws InterruptedException {
        File sourceFile;
        File destinationFile;
        
        checkAbort();
        
		List<VideoAbstract> filestoExport = tableModel.getFiles().stream()
				.filter(video -> video.isSelected()).collect(Collectors.toList());
		
		if(Jamuz.getOptions().get("video.destination").startsWith("{")) {
			Popup.warning("Invalid destination folder.");
			return false;
		}
		
        for (VideoAbstract video : filestoExport) {
			checkAbort();
			PanelVideo.progressBar.progress(video.getTitle());

			for(FileInfoVideo fileInfoVideo : video.getFiles().values()) {

				sourceFile = fileInfoVideo.getVideoFile();
				destinationFile = new File(FilenameUtils.concat(Jamuz.getOptions().get("video.destination"), fileInfoVideo.getRelativeFullPath()));

				//TODO: Allow export over SSH: merge with move option on list process

				if(sourceFile.exists()) {
					if(!destinationFile.exists()) {
						checkAbort();
						PanelVideo.progressBar.setIndeterminate(true);

						try {
							FileSystem.copyFile(sourceFile, destinationFile);
							tableModel.select(video, false);
						} catch (IOException ex) {
							video.setStatus(MessageFormat.format(Inter.get("Msg.Video.ExportFailed"), ex.toString()));
						}
					}
					else {
						video.setStatus(Inter.get("Msg.Video.DestinationExist"));
					}
				}
				else {
					video.setStatus(Inter.get("Msg.Video.SourceFileMissing"));
				}
			}
		}
        return true;
    }
    
	/**
	 *
	 * @param move
	 * @param getDb
	 * @param search
	 */
	public void listDb(boolean move, boolean getDb, boolean search) {
        this.tableModel.clear();
        this.buffer = new PathBuffer();
        this.move = move;
        this.getDb = getDb;
		this.doSearch = search;
        
        ProcessListDb process = new ProcessListDb("Thread.ProcessVideo.listDb");
        process.start();
    }
    
    //TODO: Check if abort works and how in ProcessExport above.
    //If it does somehow, do the same for other classes extending ProcessAbstract
    private class ProcessListDb extends ProcessAbstract {

        public ProcessListDb(String name) {
            super(name);
        }
        @Override
        public void run() {
            this.resetAbort();
            try {
                listDbfiles(move, getDb, doSearch);
            } catch (InterruptedException ex) {
                Popup.error(Inter.get("Msg.Process.Aborted"), ex); //NOI18N
            }
            finally {
                PanelVideo.progressBar.reset();
                PanelVideo.enableProcess(true);
            }
        }
    }
 
    //TODO: Use this: need to think of options (override or derive StatSource
    private boolean listFS(String rootPath) throws InterruptedException {
        PanelVideo.progressBar.setup(tableModel.getFiles().size());
        browseFS(new File(rootPath), rootPath);
//        for (FileInfoVideo fileInfoVideo : tableModel.getFiles()) {
//            tableModel.addRow(fileInfoVideo);
//            PanelVideo.progressBar.progress(fileInfoVideo.getTitle());
//		}
        return true;
    }
	
	private boolean listDbfiles(boolean move, boolean getDb, boolean doSearch) throws InterruptedException {
		PanelVideo.progressBar.reset();
        PanelVideo.progressBar.setIndeterminate(Inter.get("Msg.Process.RetrievingList")); //NOI18N
        
        //Connect to SSH for moving/renaming files
        if(move && Boolean.parseBoolean(Jamuz.getOptions().get("video.library.remote"))
				&& Boolean.parseBoolean(Jamuz.getOptions().get("video.SSH.enabled"))) {
            myConn = new SSH(Jamuz.getOptions().get("video.SSH.IP"), 
					Jamuz.getOptions().get("video.SSH.user"), 
					Jamuz.getOptions().get("video.SSH.pwd")); //NOI18N
            if(!myConn.connect()) {
                return false;
            }
        }

		//Connect to database
		String dbLocation = Jamuz.getOptions().get("video.dbLocation");
		if (dbLocation == null || dbLocation.trim().equals("")) {
			return false; 
		}
		DbConnVideo connKodi = new DbConnVideo(new DbInfo(DbInfo.LibType.Sqlite, 
				Jamuz.getOptions().get("video.dbLocation"), ".", "."), 
				Jamuz.getOptions().get("video.rootPath"));
		if(getDb) {
//			//Check Kodi db file exists
//			File kodiDbFile = FileSystem.replaceHome(Jamuz.getOptions().get("video.dbLocation"));
//			//Not using connKodi.getInfo().check() as we don't want popup
//			if (!kodiDbFile.exists()) { 
//				//TODO: Display error in PanelVideo somehow
//				return false;
//			}
			if(!connKodi.getInfo().check()) {
				return false;
			}
            //Retrieve Kodi database
            checkAbort();
            if(!connKodi.getInfo().copyDB(true, Jamuz.getLogPath())) {
                return false;
            }
        }
        else {
			String kodiBackupFile = FilenameUtils.concat(Jamuz.getLogPath(), 
                    FilenameUtils.getName(Jamuz.getOptions().get("video.dbLocation")));
			//Check local file exists
			File kodiDbFile = FileSystem.replaceHome(kodiBackupFile);
			if (!kodiDbFile.exists()) {
				//TODO: Explain that backup kodi db is not available
				//and so user must first run video process with "Get Db" checked
				Popup.error(java.text.MessageFormat.format(Inter.get("Error.PathNotFound"), new Object[] {kodiBackupFile})); //NOI18N
				return false;
			} else if(kodiDbFile.isDirectory()) {
				return false;
			}
            connKodi.getInfo().setLocationWork(kodiBackupFile); //NOI18N
        }
        connKodi.connect();
        
		//List movies
		connKodi.getMovies(tableModel.getFiles(), connKodi.rootPath);
        
        //List TV Shows
        connKodi.getTvShows(tableModel.getFiles());

        //Get TheMovieDb user data
		themovieDb = new TheMovieDb(Jamuz.getOptions().get("video.themovieDb.user"), 
			Jamuz.getOptions().get("video.themovieDb.pwd"), 
			Jamuz.getOptions().get("video.themovieDb.language")); 
		if(doSearch) {
			themovieDb.getAll();
		}
		else {
			themovieDb.getAllFromCache();
		}
        
		//Move - if required - and display movies 
		PanelVideo.progressBar.setup(tableModel.getFiles().size()*2);
		for (VideoAbstract video : tableModel.getFiles()) {
            if(move) {
                //TODO: Tell user (doc, options, ?) that stream info is not 
                //always available. Kodi needs to read it first
                // and that happens only when you open files in a list
                //So for tv shows, you have to go through all or create an "All" playlist and open it
                //Then wait a while before processing again
                video.moveFilesAndSrt(buffer, connKodi, myConn); 
            }
            //Check if files exist, and get size if so
            long length = 0;
            for(FileInfoVideo fileInfoVideo : video.getFiles().values()) {
                File file = fileInfoVideo.getVideoFile();
                if(file.exists()) {
                    length += file.length();
                }
            }
            if(length<=0) {
                video.setStatus(Inter.get("Msg.Video.FileNotFound"));
            }
            video.setLength(length);
           
            //Display
            PanelVideo.progressBar.progress(video.getTitle());
		}
        
        //Load user data from themovieDb.org
        for(VideoAbstract fileInfoVideo : tableModel.getFiles()) {
            PanelVideo.progressBar.progress(fileInfoVideo.getTitle());
			fileInfoVideo.setMyVideo(doSearch); //This removes from MyMovies or MyTvShows
			tableModel.fireTableDataChanged();
        }
		//Now display remaining, not removed as attached to a VideoAbstract above
        for(MyMovieDb myMovieDb : themovieDb.getMyMovies().values()) {
            tableModel.addRow(new VideoMovie(myMovieDb));
        }
        for(MyTvShow myTvShow : themovieDb.getMyTvShows().values()) {
            tableModel.addRow(new VideoTvShow(myTvShow));
        }
        
        tableModel.loadThumbnails();
        
        PanelVideo.diplayLength();
        
        if(getDb) {
            //Send Kodi database back
            this.checkAbort();
            if(!connKodi.getInfo().copyDB(false, Jamuz.getLogPath())) {
                return false;
            }
        }
		if(doSearch) {
			DbConnVideo connCacheTheMovieDb = new DbConnVideo(new DbInfo(DbInfo.LibType.Sqlite, "myMovieDb.db", ".", "."), "");
			connCacheTheMovieDb.connect();
			//Get the TvShows from the video files, as removed from themovieDb.getMyTvShows() 
			//	when set in VideoAbstract leaving only the extra from theMovieDb
			Map<Integer, MyTvShow> myTvShows = new HashMap<>();
			Map<Integer, MyMovieDb> myMovies = new HashMap<>();
			for (VideoAbstract video : getTableModel().getFiles()) {
				if(video.isMovie()) {
					myMovies.put(video.getMyMovieDb().getId(), (MyMovieDb)video.getMyMovieDb());
				} else {
					myTvShows.put(video.getMyMovieDb().getId(), (MyTvShow)video.getMyMovieDb());
				}
			}
			connCacheTheMovieDb.setTvShowsInCache(myTvShows);
			connCacheTheMovieDb.setMoviesInCache(myMovies);
			//TODO: Add a cleanup somehow
			connCacheTheMovieDb.disconnect();
		}
		connKodi.disconnect();
		return true;
	}
	
	/**
	 *
	 */
	public static TheMovieDb themovieDb;
    
    private void browseFS(File path, String rootPath) throws InterruptedException {
		this.checkAbort();
		//Verifying we have a path and not a file
		if (path.isDirectory()) {
			File[] files = path.listFiles();
			if (files != null) {
                for (File file : files) {
                    this.checkAbort();
                    if (file.isDirectory()) {
                        browseFS(file, rootPath);
                    }
                    else {
                        String absolutePath=file.getAbsolutePath();
                        String filename=file.getName();
                        String relativeFullPath=absolutePath.substring(rootPath.length());
//                        FileInfoVideo fileInfo = new FileInfoVideo(filename, relativeFullPath);
//                        tableModel.addRow(fileInfo);
                    }
                    PanelVideo.progressBar.progress(": "+FilenameUtils.getBaseName(FilenameUtils.getPathNoEndSeparator(path.getAbsolutePath())));  //NOI18N
                }
			} 
		}
	}
    
	/**
	 *
	 * @return
	 */
	public TableModelVideo getTableModel() {
        return tableModel;
    }
    
    /**
     * Path buffer
     */
    public class PathBuffer {
        private final Map<String, Integer> ids = new HashMap<>();

        /**
         * get id
         * @param strPath
		 * @param conn
         * @return
         */
        public int getId(String strPath, DbConnVideo conn) {
            if(ids.containsKey(strPath)) {
                return ids.get(strPath);
            }
            int idPath = conn.getIdPath(strPath);
            //TODO: Check what id is returned if strPath not found and adapt below if statement accordingly
            if(idPath>0) {
                ids.put(strPath, idPath);
            }
            return idPath;
        }
    }

}