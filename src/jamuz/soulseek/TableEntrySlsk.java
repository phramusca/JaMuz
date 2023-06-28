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

import jamuz.utils.DateTime;

/**
 *
 * @author raph
 */
public class TableEntrySlsk {
	
	private String date;
	private Status status;
	private int nbOfFiles;
	private int nbDownloaded;
	private String path;
	private String fileInfo = "";
	private int id;
	private String key = "";
	private String user = "";
	private int bitrate = -1;
	private int size = -1;
	private int speed = -1;

	/**
	 * Soulseek result
	 * @param status
	 * @param id
	 * @param path
	 */
	public TableEntrySlsk(int id, Status status, String path) {
		this.id = id;
		this.path = path;
		this.status = status;
		this.date = DateTime.getCurrentLocal(DateTime.DateTimeFormat.HUMAN);
	}

	public TableEntrySlsk(String key, Status status, String path, String username, int nbOfFiles, int bitrate, int size, int speed) {
		this(-1, status, path);
		this.nbOfFiles = nbOfFiles;
		this.user = username;
		this.bitrate = bitrate;
		this.size = size;
		this.speed = speed;
		this.key = key;
	}
	
	public int getId() {
		return id;
	}

	public int getNbOfFiles() {
		return nbOfFiles;
	}

	public String getPath() {
		return path;
	}

	public String getUser() {
		return user;
	}
	
	public void setId(int rowToReplace) {
		id = rowToReplace;
	}

	public String getDate() {
		return date;
	}

	String getKey() {
		return key;
	}

	public String getFileInfo() {
		return fileInfo;
	}

	void setFileInfo(String fileInfo) {
		this.fileInfo = fileInfo;
	}

	public enum Status {
		Folder,
		Downloading,
		Received,
		Moved
	}
	
	Status getStatus() {
		return status;
	}
	
	public int getBitrate() {
		return bitrate;
	}

	public int getSize() {
		return size;
	}

	public int getSpeed() {
		return speed;
	}

	public int getNbDownloaded() {
		return nbDownloaded;
	}

	public void setNbDownloaded(int nbDownloaded) {
		this.nbDownloaded = nbDownloaded;
	}

	public void setStatus(Status status) {
		this.status = status;
		this.date = DateTime.getCurrentLocal(DateTime.DateTimeFormat.HUMAN);
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	@Override
	public String toString() {
		return path + " | " + user + " | " + nbOfFiles + " |" ;
	}
}
