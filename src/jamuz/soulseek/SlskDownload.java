/*
 * Copyright (C) 2023 raph
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
package jamuz.soulseek;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import jamuz.utils.FileSystem;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author raph
 */
public class SlskDownload {
	
	@Expose
	public String query;
	@Expose
	public String username;
	@Expose
	public String path;
	@Expose
	public int nbOfFiles;
	@Expose
	public int nbDownloaded;
	
	private File jsonFile;
	private File folder;

	/**
	 *
	 * @param query
	 * @param nbOfFiles
	 * @param path
	 * @param username
	 * @param destination
	 */
	public SlskDownload(String query, int nbOfFiles, String path, String username, String destination) {
		this.query = query;
		this.nbOfFiles = nbOfFiles;
		this.path = path;
		this.username = username;
		setFiles(destination);
	}

	private void setFiles(String destination) {
		String folderName = username + "--" + path.replaceAll("\\\\", "--");
		this.jsonFile = new File(FilenameUtils.concat(destination, folderName + ".json"));
		this.folder = new File(FilenameUtils.concat(destination, folderName));
	}
	
	public String getPath() {
		return path;
	}

	public String getUsername() {
		return username;
	}

	public int getNbOfFiles() {
		return nbOfFiles;
	}
		
	private void write() {
		try {
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			FileSystem.writeTextFile(jsonFile, gson.toJson(this));
		} catch (IOException ex) {
			Logger.getLogger(SlskDownload.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	SlskDownload read() {
		final Gson gson = new Gson();
		SlskDownload fromJson = null;
		if(jsonFile.exists()) {
			try {
				String readJson = FileSystem.readTextFile(jsonFile);
				if (!readJson.equals("")) {
					fromJson = gson.fromJson(readJson, SlskDownload.class);
				}
			} catch (IOException ex) {
				Logger.getLogger(SlskDownload.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return fromJson;
	}

	void fileDownloaded() {
		 nbDownloaded++;
		 write();
	}

	void cleanup(String destination) {
		setFiles(destination);
		if(jsonFile.exists()) {
			jsonFile.delete();
		}
		if(folder.exists()) {
			folder.delete();
		}
	}
}
