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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author raph
 */
public class SoulseekResult {
	
	private String line;
	private Status status;
	private int id;
	private int nbOfFiles;
	private String path="";
	private String username="";
	private String bitrate="";
	private String size="";
	private String speed="";
	private String date;
	private String attributes;
	private int nbDownloaded;

	/**
	 * Soulseek result
	 * @param line
	 * @param status
	 * @param id
	 * @param path
	 */
	public SoulseekResult(String line, Status status, int id, String path) {
		this.line = line;
		this.id = id;
		this.path = path;
		this.status = status;
		this.date = DateTime.getCurrentLocal(DateTime.DateTimeFormat.HUMAN);
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

	public String getUsername() {
		return username;
	}
	
	public void setId(int rowToReplace) {
		id = rowToReplace;
	}

	public String getDate() {
		return date;
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

	public boolean parseFolderAttributes() {
		Pattern patterner = Pattern.compile("^  ([0-9]+)\\)(.*) - (.*)(\\(([0-9]+) file.*)", Pattern.CASE_INSENSITIVE);
		Matcher matcher = patterner.matcher(line);
		boolean matchFound = matcher.find();
		if(matchFound) {
			id = Integer.parseInt(matcher.group(1));
			path = matcher.group(2).trim();
			username = matcher.group(3).trim();
			
			attributes = matcher.group(4);
			nbOfFiles = Integer.parseInt(matcher.group(5));
			attributes = attributes.substring(0, attributes.length()-1).substring(1);
			String[] split = attributes.split(",");		
			for (String string : split) {
				String[] split1 = string.split(":");
				if(split1.length > 1) {
					String key = split1[0].trim();
					String value = split1[1].trim();
					switch(key) {
						case "bitrate": bitrate = value; break;
						case "size": size = value; break;
						case "speed": speed = value; break;
					}
				}
			}
		}
		return matchFound;
	}
	
	public String getBitrate() {
		return bitrate;
	}

	public String getSize() {
		return size;
	}

	public String getSpeed() {
		return speed;
	}

	public int getNbDownloaded() {
		return nbDownloaded;
	}

	public void setNbDownloaded(int nbDownloaded) {
		this.nbDownloaded = nbDownloaded;
	}
	
	void append(String string) {
		this.line += string;
	}

	public void setStatus(Status status) {
		this.status = status;
		this.date = DateTime.getCurrentLocal(DateTime.DateTimeFormat.HUMAN);
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}
	
	@Override
	public String toString() {
		return path + " | " + username + " | " + attributes + " |" ;
	}
}
