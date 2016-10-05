/*
 * Copyright (C) 2014 phramusca ( https://github.com/phramusca/JaMuz/ )
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

package jamuz.process.sync;

import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.Playlist;
import jamuz.utils.ProcessAbstract;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import jamuz.utils.Popup;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.commons.io.FilenameUtils;
import jamuz.utils.Benchmark;
import jamuz.utils.FileSystem;
import jamuz.utils.Inter;

/**
 * Sync process class
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ProcessSync extends ProcessAbstract {
	
	private ArrayList<FileInfoInt> fileInfoSourceList;
	private ArrayList<FileInfoInt> fileInfoDestinationList;
	private final Device device;
    private ArrayList<FileInfoInt> toInsertInDeviceFiles;
    
	/**
	 * Creates a new sync process instance  
     * @param name
     * @param device
	 */
	public ProcessSync(String name, Device device) {
        super(name);
		this.fileInfoSourceList = new ArrayList<>();
		this.fileInfoDestinationList = new ArrayList<>();
        this.toInsertInDeviceFiles = new ArrayList<>();
		this.device=device;
	}
	
	/**
	 * Starts file synchronisation process in a new thread
	 * Called by MainGUI
	 */
    @Override
	public void run() {
		this.resetAbort();

        try {
            sync();
        } catch (InterruptedException ex) {
            Popup.info(Inter.get("Msg.Process.Aborted") //NOI18N
            + "\nYou shall sync again if some files have been deleted on destination\n"
                    + "OR you will face some merge \"not found\" issues.");  //TODO: Inter
        }
        finally {
            PanelSync.progressBar.setIndeterminate(Inter.get("Msg.Sync.UpdatingDb")); //NOI18N
            
            //Updating database only if toInsertInDeviceFiles has items 
            //This prevents problems in case aborted before any change has been made 
            //BUT problem remains if some changes occur after abortion
            // (database will not reflect reality so merge "not found" errors would be raised)
            //TODO: Make a proper toInsertInDeviceFiles list in all cases:
            //  => Use fileInfoSourceList, fileInfoDestinationList and toInsertInDeviceFiles
            if(toInsertInDeviceFiles.size()>0) {
                Jamuz.getDb().deleteDeviceFiles(device.getId());
                Jamuz.getDb().insertDeviceFiles(toInsertInDeviceFiles, device.getId());
            }

            PanelSync.progressBar.reset();
            //enabling back buttons 				
            PanelSync.enableSync(true);
        }
	}
	
	private boolean sync() throws InterruptedException {
		//FIXME: Use a pattern (separated from the one used for library)
		//Inspire from file tagger in check
		
        if(!new File(this.device.getDestination()).exists()) {
            Popup.warning(java.text.MessageFormat.format(Inter.get("Msg.Sync.DestinationDoesNotExist"), new Object[] {this.device.getDestination()}));  //NOI18N
            return false;
        }

        //Allowing abort
        PanelSync.enableSyncStartButton(true);

        PanelSync.progressBar.reset();
        PanelSync.progressBar.setIndeterminate(Inter.get("Msg.Process.RetrievingList")); //NOI18N

        //Get source files list (files to be sent)
        fileInfoSourceList = new ArrayList<>();
        Playlist playlist = this.device.getPlaylist();
        playlist.getFiles(fileInfoSourceList);
        this.checkAbort();

        //Get files currently on destination
        fileInfoDestinationList = new ArrayList<>();
        this.browseFS(new File(this.device.getDestination()));
        this.checkAbort();
        PanelSync.progressBar.setup(fileInfoSourceList.size() + fileInfoDestinationList.size());

        this.toInsertInDeviceFiles = new ArrayList<>();
        //Remove files on destination
        for (FileInfoInt fileInfo : fileInfoDestinationList) {
            this.checkAbort();
            int idInSource = searchInSourceList(fileInfo.getRelativeFullPath());
            if(idInSource>=0) {
//                    PanelSync.addRowSync(fileInfo.getRelativeFullPath(), Inter.get("Playlist.AlreadyOnDestination")); //NOI18N
                this.toInsertInDeviceFiles.add(this.fileInfoSourceList.get(idInSource));
                //Remove from Source list as already on destination
                fileInfoSourceList.remove(idInSource);
                PanelSync.progressBar.setMaximum(PanelSync.progressBar.getMaximum()-1);
            }
            else {
                //Not a file to be copied, removing it on destination
                File file = new File(fileInfo.getRootPath()+fileInfo.getRelativeFullPath());
                file.delete();
                PanelSync.addRowSync(fileInfo.getRelativeFullPath(), 0); //NOI18N
            }
            PanelSync.progressBar.progress(fileInfo.getTitle());
        }

        //Copy files to destination
        Benchmark bench = new Benchmark(fileInfoSourceList.size());
        for (FileInfoInt fileInfo : fileInfoSourceList) {
            this.checkAbort();
            File source = new File(FilenameUtils.concat(this.device.getSource(), fileInfo.getRelativeFullPath()));
            File destination = new File(FilenameUtils.concat(this.device.getDestination(), fileInfo.getRelativeFullPath()));

            try {
                FileSystem.copyFile(source, destination);
                this.toInsertInDeviceFiles.add(fileInfo);
                PanelSync.addRowSync(fileInfo.getRelativeFullPath(), 1); //NOI18N
            } catch (IOException ex) {
                PanelSync.addRowSync(fileInfo.getRelativeFullPath(), MessageFormat.format(Inter.get("Playlist.CopyFailed"), ex.toString())); //NOI18N

            }

            PanelSync.progressBar.progress(bench.get());
        }
       
		return true;
	}
	
    //TODO: Use a Map instead ...
	private int searchInSourceList(String relativeFullPath) throws InterruptedException {
		for(int i = 0; i < this.fileInfoSourceList.size(); i++) {
			this.checkAbort();
			FileInfoInt file = this.fileInfoSourceList.get(i);
            
            //TODO: maybe support ignoreCase as an option
//			if(fileInfo.getRelativeFullPath().equalsIgnoreCase(relativeFullPath)) { return i; }
            //We want sync to be case sensitive
            //TODO: Check file size too, seen once a file on destination 0 kO in size
            if(file.getRelativeFullPath().equals(relativeFullPath)) { 
                File fileSource = new File(file.getRelativeFullPath());
                File fileDestination = new File(relativeFullPath);
                //Checking size //TODO: Make it an option as takes more time
                if(fileSource.length()==fileDestination.length()) {
                    //TODO: Check modification date too, as an option too 
                    return i; 
                }
            }
		}
		return -1;
	}
	
	private void browseFS(File path) throws InterruptedException {
		Jamuz.getLogger().log(Level.FINE, "Browsing \"{0}\"", path.getAbsolutePath());  //NOI18N
        this.checkAbort();
        //Verifying we have a path and not a file
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (files != null) {
                if(files.length<=0) {
                    if(!FilenameUtils.equalsNormalizedOnSystem(this.device.getDestination(), path.getAbsolutePath())) {
                        Jamuz.getLogger().log(Level.FINE, "Deleted empty folder \"{0}\"", path.getAbsolutePath());  //NOI18N
                        path.delete();
                    }
                }
                else {
                    for (File file : files) {
                        this.checkAbort();
                        if (file.isDirectory()) {
                            browseFS(file);
                        }
                        else {
                            String absolutePath=file.getAbsolutePath();
                            String relativeFullPath=absolutePath.substring(this.device.getDestination().length());
                            FileInfoInt fileInfo = new FileInfoInt(relativeFullPath, this.device.getDestination());
                            this.fileInfoDestinationList.add(fileInfo);
                        }
                    }
                }
            } 
        }
	}
}
