/*
 * Copyright (C) 2014 phramusca <phramusca@gmail.com>
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
//FIXME Z SYNC If file missing in source, it blocks transfert
//FIXME Z SYNC If tag issue (genre, only?), it blocks transfert (errors)
//FIXME Z SYNC Export file scrollabr freeze => threaded ?

import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.Playlist;
import jamuz.gui.swing.ProgressBar;
import jamuz.process.check.Location;
import jamuz.utils.Benchmark;
import jamuz.utils.FileSystem;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import jamuz.utils.ProcessAbstract;
import jamuz.utils.StringManager;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import ws.schild.jave.EncoderException;

/**
 * Sync process class
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class ProcessSync extends ProcessAbstract {

    private ArrayList<FileInfoInt> fileInfoSourceList;
    private ArrayList<FileInfoInt> fileInfoDestinationList;
    private final Device device;
    private ArrayList<FileInfoInt> toInsertInDeviceFiles;
    private final ProgressBar progressBar;
    private final ICallBackSync callback;

    /**
     * Creates a new sync process instance
     *
     * @param name
     * @param device
     * @param progressBar
     * @param callback
     */
    public ProcessSync(String name, Device device, ProgressBar progressBar,
            ICallBackSync callback) {
        super(name);
        this.fileInfoSourceList = new ArrayList<>();
        this.fileInfoDestinationList = new ArrayList<>();
        this.toInsertInDeviceFiles = new ArrayList<>();
        this.device = device;
        this.progressBar = progressBar;
        this.callback = callback;
    }

    /**
     * Starts file export process in a new thread
     */
    @Override
    public void run() {
        this.resetAbort();
        try {
            if (sync() && toInsertInDeviceFiles.size() <= 0) {
                progressBar.setup(fileInfoSourceList.size());
                progressBar.progress("Export complete.", fileInfoSourceList.size());
                callback.refresh();
            }
        } catch (InterruptedException ex) {
            Popup.info(Inter.get("Msg.Process.Aborted") //NOI18N
                    + "\nYou shall sync again if some files have been deleted on destination\n"
                    + "OR you will face some merge \"not found\" issues.");  //TODO: Inter
        } finally {
            //Updating database only if toInsertInDeviceFiles has items 
            //This prevents problems in case aborted before any change has been made 
            //BUT problem remains if some changes occur after abortion
            // (database will not reflect reality so merge "not found" errors would be raised)
            //TODO: Make a proper toInsertInDeviceFiles list in all cases:
            //  => Use fileInfoSourceList, fileInfoDestinationList and toInsertInDeviceFiles
            if (!toInsertInDeviceFiles.isEmpty()) {
                progressBar.setIndeterminate(Inter.get("Msg.Sync.UpdatingDb")); //NOI18N
                callback.refresh();
                Jamuz.getDb().deviceFile().lock().delete(device.getId());
                Jamuz.getDb().deviceFile().lock().insertOrIgnore(toInsertInDeviceFiles, device.getId());
                progressBar.setup(fileInfoSourceList.size());
                progressBar.progress("Export complete.", fileInfoSourceList.size());
                callback.refresh();
            }
            callback.enable();
        }
    }

    private boolean sync() throws InterruptedException {
        if (this.device.isHidden()) {
            return syncRemote();
        } else {
            return syncFS();
        }
    }

    private boolean syncRemote() throws InterruptedException {
        callback.enableButton(true);
        progressBar.reset();
        progressBar.setIndeterminate(Inter.get("Msg.Process.RetrievingList")); //NOI18N
        callback.refresh();

        //Get list of files to export
        ArrayList<FileInfoInt> filesDevicePlaylist = new ArrayList<>();
        Playlist playlist = device.getPlaylist();
        playlist.getFiles(filesDevicePlaylist);

        //GET list of files in deviceFile
        fileInfoSourceList = new ArrayList<>();
        Jamuz.getDb().file().getFiles(fileInfoSourceList, device);

        // Check if some files require to be transcoded and exit if so
        String destExt = playlist.getDestExt();
        if (!destExt.isBlank()) {
            Location location = new Location("location.transcoded");
            if (!location.check()) {
                return false;
            }
            String destPath = location.getValue();
            List<FileInfoInt> filesToMaybeTranscode = filesDevicePlaylist.stream()
                    .filter(f -> !f.getExt().equals(destExt))
                    .collect(Collectors.toList());
            for (FileInfoInt file : filesToMaybeTranscode) {
                try {
                    if (file.transcodeRequired(destPath, destExt)) {
                        Popup.warning("Some files requires transcoding but are not yet transcoded. Please use the Check tab to do so. ");
                        progressBar.reset();
                        callback.refresh();
                        return false;
                    }
                } catch (IllegalArgumentException | EncoderException | IOException ex) {
                    Jamuz.getLogger().severe(ex.toString());
                }
            }
        }

        //Set statuses in deviceFile
        progressBar.setup(fileInfoSourceList.size());
        callback.refresh();
        ArrayList<FileInfoInt> filesToInsertOrUpdate = new ArrayList<>();
        for (FileInfoInt fileTable : fileInfoSourceList) {
            if (filesDevicePlaylist.contains(fileTable)) {
                filesDevicePlaylist.remove(fileTable);
                fileTable.setStatus(SyncStatus.NEW);
            } else {
                fileTable.setStatus(SyncStatus.INFO);
            }
            filesToInsertOrUpdate.add(fileTable);
            callback.addRow(fileTable.getRelativeFullPath(), 1); //NOI18N
            progressBar.progress(fileTable.getTitle());
            callback.refresh();
        }
        filesToInsertOrUpdate.addAll(filesDevicePlaylist);
        Jamuz.getDb().deviceFile().lock().insertOrUpdate(filesToInsertOrUpdate, device.getId());
        fileInfoSourceList.addAll(filesDevicePlaylist); // To have a proper count in progressBar at the end
        return true;
    }

    private boolean syncFS() throws InterruptedException {
        //TODO: Use a pattern (separated from the one used for library)
        //Inspire from file tagger in check

        if (!new File(this.device.getDestination()).exists()) {
            Popup.warning(java.text.MessageFormat.format(
                    "<html>" + Inter.get("Msg.Sync.DestinationDoesNotExist") + "</html>",
                    new Object[]{this.device.getDestination()}));  //NOI18N
            return false;
        }

        //Allowing abort
        callback.enableButton(true);

        progressBar.reset();
        progressBar.setIndeterminate(Inter.get("Msg.Process.RetrievingList")); //NOI18N
        callback.refresh();

        //Get source files list (files to be sent)
        fileInfoSourceList = new ArrayList<>();
        Playlist playlist = this.device.getPlaylist();
        playlist.getFiles(fileInfoSourceList);
        this.checkAbort();

        //Get files currently on destination
        fileInfoDestinationList = new ArrayList<>();
        this.browseFS(new File(this.device.getDestination()));
        this.checkAbort();
        progressBar.setup(fileInfoSourceList.size() + fileInfoDestinationList.size());
        callback.refresh();
        //FIXME Z SYNC: Offer deletion as an option now that process is labeled "export" and not "sync" anymore !!!!
        this.toInsertInDeviceFiles = new ArrayList<>();
        //Remove files on destination
        for (FileInfoInt fileInfo : fileInfoDestinationList) {
            this.checkAbort();
            int idInSource = searchInSourceList(fileInfo.getRelativeFullPath());
            if (idInSource >= 0) {
//                    PanelSync.addRow(fileInfo.getRelativeFullPath(), Inter.get("Playlist.AlreadyOnDestination")); //NOI18N
                this.toInsertInDeviceFiles.add(
                        this.fileInfoSourceList.get(idInSource));
                //Remove from Source list as already on destination
                fileInfoSourceList.remove(idInSource);
                progressBar.setMaximum(progressBar.getMaximum() - 1);
                callback.refresh();
            } else {
                //Not a file to be copied, removing it on destination
                File file = fileInfo.getFullPath();
                file.delete();
                callback.addRow(fileInfo.getRelativeFullPath(), 0); //NOI18N
            }
            progressBar.progress(fileInfo.getTitle());
            callback.refresh();
        }

        //Copy files to destination
        Benchmark bench = new Benchmark(fileInfoSourceList.size());
        for (FileInfoInt fileInfo : fileInfoSourceList) {
            this.checkAbort();
            File source = new File(FilenameUtils.concat(
                    this.device.getSource(), fileInfo.getRelativeFullPath()));
            File destination = new File(FilenameUtils.concat(
                    this.device.getDestination(), fileInfo.getRelativeFullPath()));
            long startTime = System.currentTimeMillis();
            String format = "{0} \t ({1})";
            try {
                FileSystem.copyFile(source, destination);
                this.toInsertInDeviceFiles.add(fileInfo);
                callback.addRow(MessageFormat.format(format,
                        fileInfo.getRelativeFullPath(),
                        StringManager.humanReadableSeconds(
                                (System.currentTimeMillis() - startTime) / 1000)),
                         1); //NOI18N
            } catch (IOException ex) {
                callback.addRow(MessageFormat.format(format,
                        fileInfo.getRelativeFullPath(),
                        StringManager.humanReadableSeconds(
                                (System.currentTimeMillis() - startTime) / 1000)),
                        MessageFormat.format(Inter.get("Playlist.CopyFailed"),
                                ex.toString())); //NOI18N
            }
            progressBar.progress(bench.get());
            callback.refresh();
        }
        return true;
    }

    //TODO: Use a Map instead ...
    private int searchInSourceList(String relativeFullPath) throws InterruptedException {

        //FileSystem.copyFile preserves datetime
        //Unfortunatly on some devices it does not work
        //ex: Android (https://stackoverflow.com/questions/18677438/android-set-last-modified-time-for-the-file)
        //=> FIXME Z SYNC Make options of these, must be one or the other
        //to detect if file is different
        boolean doCheckLastModified = true; // Faster but does not work for android (JaMuz Remote is not concerned)
        boolean doCheckContent = false;		// Way Slower (especially over wifi) but more reliable

        for (int i = 0; i < this.fileInfoSourceList.size(); i++) {
            this.checkAbort();
            FileInfoInt file = this.fileInfoSourceList.get(i);

            //TODO: maybe support ignoreCase as an option
//			if(fileInfo.getRelativeFullPath().equalsIgnoreCase(relativeFullPath)) { return i; }
            //We want sync to be case sensitive
            if (file.getRelativeFullPath().equals(relativeFullPath)) {
                File fileSource = new File(FilenameUtils.concat(
                        this.device.getSource(), file.getRelativeFullPath()));
                File fileDestination = new File(FilenameUtils.concat(
                        this.device.getDestination(), relativeFullPath));

                if (fileSource.length() == fileDestination.length()) {
                    if (!doCheckLastModified
                            || fileSource.lastModified() == fileDestination.lastModified()) {
                        try {
                            if (!doCheckContent
                                    || FileUtils.contentEquals(fileSource,
                                            fileDestination)) {
                                return i;
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(ProcessSync.class.getName())
                                    .log(Level.SEVERE, null, ex);
                        }
                    }
                    return i;
                }
            }
        }
        return -1;
    }

    private void browseFS(File path) throws InterruptedException {
        Jamuz.getLogger().log(Level.FINE,
                "Browsing \"{0}\"", path.getAbsolutePath());  //NOI18N
        this.checkAbort();
        //Verifying we have a path and not a file
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            if (files != null) {
                if (files.length <= 0) {
                    if (!FilenameUtils.equalsNormalizedOnSystem(
                            this.device.getDestination(),
                            path.getAbsolutePath())) {
                        Jamuz.getLogger().log(Level.FINE,
                                "Deleted empty folder \"{0}\"",
                                path.getAbsolutePath());  //NOI18N
                        path.delete();
                    }
                } else {
                    for (File file : files) {
                        this.checkAbort();
                        if (file.isDirectory()) {
                            browseFS(file);
                        } else {
                            String absolutePath = file.getAbsolutePath();
                            String relativeFullPath = absolutePath.substring(
                                    this.device.getDestination().length());
                            FileInfoInt fileInfo = new FileInfoInt(
                                    relativeFullPath,
                                    this.device.getDestination());
                            this.fileInfoDestinationList.add(fileInfo);
                        }
                    }
                }
            }
        }
    }
}
