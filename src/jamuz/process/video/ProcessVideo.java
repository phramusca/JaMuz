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

import jamuz.process.video.VideoAbstract;
import jamuz.process.video.VideoMovie;
import jamuz.process.video.TheMovieDb;
import jamuz.process.video.VideoTvShow;
import info.movito.themoviedbapi.model.core.ResponseStatusException;
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
import java.util.logging.Level;

/**
 * Video process class
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ProcessVideo extends ProcessAbstract {
    
    private final TableModelVideo tableModel;
    private DbConnVideo conn;
	private SSH myConn;
    private PathBuffer buffer;

    private boolean move;
    private boolean getDb;

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
        
        for (VideoAbstract video : tableModel.getFiles()) {
            if(video.isSelected()) {
                checkAbort();
                PanelVideo.progressBar.progress(video.getTitle());
                
                for(FileInfoVideo fileInfoVideo : video.getFiles().values()) {
        
                    sourceFile = new File(FilenameUtils.concat(Jamuz.getOptions().get("video.source"), fileInfoVideo.getFilename()));
                    destinationFile = new File(FilenameUtils.concat(Jamuz.getOptions().get("video.destination"), fileInfoVideo.getFilename()));

                    //TODO: Allow export over SSH: merge with move option on list process

                    if(sourceFile.exists()) {
                        if(!destinationFile.exists()) {
                            checkAbort();
                            PanelVideo.progressBar.setIndeterminate(true);

                            try {
                                FileSystem.copyFile(sourceFile, destinationFile);
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
                
                tableModel.select(video, false);
            }
		}
        return true;
    }
    
    public void listDb(boolean move, boolean getDb) {
        this.tableModel.clear();
        this.buffer = new PathBuffer();
        this.move = move;
        this.getDb = getDb;
        
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
                listDbfiles(move, getDb);
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
    private boolean listFS() throws InterruptedException {
        PanelVideo.progressBar.setup(tableModel.getFiles().size());
        browseFS(new File(conn.rootPath));
//        for (FileInfoVideo fileInfoVideo : tableModel.getFiles()) {
//            tableModel.addRow(fileInfoVideo);
//            PanelVideo.progressBar.progress(fileInfoVideo.getTitle());
//		}
        return true;
    }
	
	private boolean listDbfiles(boolean move, boolean getDb) throws InterruptedException {
		PanelVideo.progressBar.reset();
        PanelVideo.progressBar.setIndeterminate(Inter.get("Msg.Process.RetrievingList")); //NOI18N
        
        //Connect to SSH for moving/renaming files
        if(move && Jamuz.getOptions().get("video.SSH.enabled").equals("true")) {
            myConn = new SSH(Jamuz.getOptions().get("video.SSH.IP"), Jamuz.getOptions().get("video.SSH.user"), Jamuz.getOptions().get("video.SSH.pwd")); //NOI18N
            if(!myConn.connect()) {
                return false;
            }
        }
        
		//Connect to database
		conn = new DbConnVideo(new DbInfo("sqlite", Jamuz.getOptions().get("video.dbLocation"), ".", "."), Jamuz.getOptions().get("video.rootPath"));
        if(getDb) {
            //Retrieve XBMC database
            checkAbort();
            if(!conn.getInfo().copyDB(true, Jamuz.getLogPath())) {
                return false;
            }
        }
        else {
            //It is changed when copying, but if we want to use previous local copy directly, need to change work location
            conn.getInfo().setLocationWork(FilenameUtils.concat(Jamuz.getLogPath(), 
                    FilenameUtils.getName(Jamuz.getOptions().get("video.dbLocation")))); //NOI18N
        }
        conn.connect();
		conn.prepareStatements();
        
		//List movies
		conn.getMovies(tableModel.getFiles(), conn.rootPath);
        
        //List TV Shows
        conn.getTvShows(tableModel.getFiles());

        //Get TheMovieDb user data
        themovieDb = new TheMovieDb(Jamuz.getOptions().get("video.themovieDb.user"), 
                Jamuz.getOptions().get("video.themovieDb.pwd"), 
                Jamuz.getOptions().get("video.themovieDb.language")); 
        
		//Move - if required - and display movies 
		PanelVideo.progressBar.setup(tableModel.getFiles().size()*2);
		for (VideoAbstract video : tableModel.getFiles()) {
            if(move) {
                //TODO: Tell user (doc, options, ?) that stream info is not 
                //always available. Kodi needs to read it first
                // and that happens only when you open files in a liste
                //So for series, you have to go through all or create an "All" playlist and open it
                //Then wait a while before processing again
                //FIXME: Warn that kodi needs cleanup/update when at least a file has been moved
                video.moveFilesAndSrt(buffer, conn, myConn); 
            }
            //Check if files exist locally, and get size if so
            long length = 0;
            for(FileInfoVideo fileInfoVideo : video.getFiles().values()) {
                File file = new File(FilenameUtils.concat(Jamuz.getOptions().get("video.source"), fileInfoVideo.getRelativeFullPath()));
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
//            try {
                fileInfoVideo.setMyVideo();
                tableModel.fireTableDataChanged();
				
				//IF this try/catch was somehow useful (commented 2016-04-06), then add it in fileInfoVideo.setMyVideo(); and return a boolean to break;
//            }
//            catch(ResponseStatusException ex) {
//                if(ex.getResponseStatus().getStatusCode()!=25) {
//                    Popup.error("Code "+ex.getResponseStatus().getStatusCode()+": "+ex.getResponseStatus().getStatusMessage());
//                }
//				else {
//					Jamuz.getLogger().log(Level.FINEST, "Code {0}: {1}", new Object[]{ex.getResponseStatus().getStatusCode(), ex.getResponseStatus().getStatusMessage()});
//				}
//                break;
//            }
        }
        for(MyMovieDb myMovieDb : themovieDb.getMyMovies().values()) {
            tableModel.addRow(new VideoMovie(myMovieDb));
        }
        for(MyTvShow myTvShow : themovieDb.getMyTvShows().values()) {
            tableModel.addRow(new VideoTvShow(myTvShow));
        }
        
        tableModel.loadThumbnails();
        
        PanelVideo.diplayLength();
        
        if(getDb) {
            //Send XBMC dtabase back
            this.checkAbort();
            if(!conn.getInfo().copyDB(false, Jamuz.getLogPath())) {
                return false;
            }
        }
		return true;
	}
    public static TheMovieDb themovieDb;
    
    private void browseFS(File path) throws InterruptedException {
		this.checkAbort();
		//Verifying we have a path and not a file
		if (path.isDirectory()) {
			File[] files = path.listFiles();
			if (files != null) {
                for (File file : files) {
                    this.checkAbort();
                    if (file.isDirectory()) {
                        browseFS(file);
                    }
                    else {
                        String absolutePath=file.getAbsolutePath();
                        String filename=file.getName();
                        String relativeFullPath=absolutePath.substring(this.conn.rootPath.length());
//                        FileInfoVideo fileInfo = new FileInfoVideo(filename, relativeFullPath);
//                        tableModel.addRow(fileInfo);
                    }
                    PanelVideo.progressBar.progress(": "+FilenameUtils.getBaseName(FilenameUtils.getPathNoEndSeparator(path.getAbsolutePath())));  //NOI18N
                }
			} 
		}
	}
    
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
         * @return
         */
        public int getId(String strPath) {
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