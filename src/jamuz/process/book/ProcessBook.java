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

package jamuz.process.book;

import jamuz.DbInfo;
import jamuz.Jamuz;
import java.io.File;
import jamuz.utils.Popup;
import org.apache.commons.io.FilenameUtils;
import jamuz.utils.FileSystem;
import jamuz.utils.Inter;
import jamuz.utils.ProcessAbstract;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Video process class
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ProcessBook extends ProcessAbstract {
    
    private final TableModelBook tableModel;
    private boolean getDb;

	/**
	 *
	 * @param name
	 */
	public ProcessBook(String name) {
        super(name);
        tableModel = new TableModelBook();
        getDb = false;
    }

    /**
     * Export selected files in a thred, updating PanelBook
     */
    public void export() {
        PanelBook.progressBar.setup(tableModel.getNbSelected());
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
				Popup.info("Export book complete");
                PanelBook.progressBar.reset();
                PanelBook.enableProcess(true);
            }
        }
    }
    
    private boolean exportFiles() throws InterruptedException {
        File sourceFile;
        File destinationFile;
        
        checkAbort();
        
		List<Book> filestoExport = tableModel.getBooks().stream().filter(
				video -> video.isSelected()).collect(Collectors.toList());
		
		//The following should never happen as it is checked in PanelBook already
		if(filestoExport.size()<=0) {
			Popup.warning("You should select some files to export first");
			return false;
		}
		
        for (Book book : filestoExport) {
			checkAbort();
			PanelBook.progressBar.progress(book.getTitle());
			sourceFile = new File(book.getFullPath());
			//FIXME LOW BOOK Use a template for export destination
			destinationFile = new File(FilenameUtils.concat(
					Jamuz.getOptions().get("book.destination"), 
					book.getRelativeFullPath()));
			if(sourceFile.exists() && !destinationFile.exists()) {
				checkAbort();
				PanelBook.progressBar.setIndeterminate(true);
				try {
					FileSystem.copyFile(sourceFile, destinationFile);
					tableModel.select(book, false);
				} catch (IOException ex) {
					Jamuz.getLogger().warning(MessageFormat.format(
							Inter.get("Msg.Video.ExportFailed"), 
							ex.toString()));
				}
			}
		}
        return true;
    }
    
	/**
	 *
	 * @param getDb
	 */
	public void listDb(boolean getDb) {
        this.tableModel.clear();
        this.getDb = getDb;
        
        ProcessListDb process = new ProcessListDb("Thread.ProcessBook.listDb");
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
                listDbfiles(getDb);
            } catch (InterruptedException ex) {
                Popup.error(Inter.get("Msg.Process.Aborted"), ex); //NOI18N
            }
            finally {
                PanelBook.progressBar.reset();
                PanelBook.enableProcess(true);
            }
        }
    }
 
	private boolean listDbfiles(boolean getDb) throws InterruptedException {
		PanelBook.progressBar.reset();
        PanelBook.progressBar.setIndeterminate(Inter.get("Msg.Process.RetrievingList")); //NOI18N

		//Connect to database
		String calibreDbFileName = 
				FilenameUtils.concat(Jamuz.getOptions().get("book.calibre"), 
				"metadata.db");
		
		DbConnBook connCalibre = new DbConnBook(new DbInfo(DbInfo.LibType.Sqlite, 
				calibreDbFileName, ".", "."));
        File calibreDbFile = FileSystem.replaceHome(calibreDbFileName);

		if (!calibreDbFile.exists()) {
			return false;
		}
		if(getDb) {
            //Retrieve Calibre database
            checkAbort();
            if(!connCalibre.getInfo().copyDB(true, Jamuz.getLogPath())) {
                return false;
            }
        }
        else {
            //It is changed when copying, but if we want to use previous local copy directly, need to change work location
            connCalibre.getInfo().setLocationWork(FilenameUtils.concat(Jamuz.getLogPath(), 
                    FilenameUtils.getName(calibreDbFileName))); //NOI18N
        }
        connCalibre.connect();
        
		//List books
		connCalibre.getBooks(tableModel.getBooks());
      
		//Get files length
		PanelBook.progressBar.setup(tableModel.getBooks().size()*2);
		for (Book book : tableModel.getBooks()) {
			File file = new File(book.getFullPath());
			if(file.exists()) {
				book.setLength(file.length());
			}
            //Display
            PanelBook.progressBar.progress(book.getTitle());
		}
        tableModel.loadThumbnails();
        PanelBook.diplayLength();
        if(getDb) {
            //Send Kodi dtabase back
            this.checkAbort();
            if(!connCalibre.getInfo().copyDB(false, Jamuz.getLogPath())) {
                return false;
            }
        }
		connCalibre.disconnect();
		return true;
	}

	/**
	 *
	 * @return
	 */
	public TableModelBook getTableModel() {
        return tableModel;
    }
	
	
	//TODO: Use this: need to think of options (override or derive StatSource
    private boolean listFS(String rootPath) throws InterruptedException {
        PanelBook.progressBar.setup(tableModel.getBooks().size());
        browseFS(new File(rootPath), rootPath);
//        for (FileInfoVideo fileInfoVideo : tableModel.getBooks()) {
//            tableModel.addRow(fileInfoVideo);
//            PanelBook.progressBar.progress(fileInfoVideo.getTitle());
//		}
        return true;
    }
	
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
                    PanelBook.progressBar.progress(": "+FilenameUtils.getBaseName(FilenameUtils.getPathNoEndSeparator(path.getAbsolutePath())));  //NOI18N
                }
			} 
		}
	}
    
	

}