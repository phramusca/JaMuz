/*
 * Copyright (C) 2015 phramusca <phramusca@gmail.com>
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
package jamuz.database;

import jamuz.FileInfo;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public abstract class StatSourceAbstract {

	/**
	 * Root path
	 */
	private String rootPath;

	/**
	 *
	 * @return
	 */
	public String getRootPath() {
		return rootPath;
	}

	/**
	 *
	 * @param rootPath
	 */
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	/**
	 * Location
	 */
	private String location;

	/**
	 *
	 * @return
	 */
	public String getLocation() {
		return location;
	}

	/**
	 *
	 * @param location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Name
	 */
	private String name;

	/**
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	private boolean updateAddedDate = false;

	/**
	 *
	 * @return
	 */
	public boolean isUpdateAddedDate() {
		return updateAddedDate;
	}

	private boolean updateLastPlayed = false;

	/**
	 *
	 * @return
	 */
	public boolean isUpdateLastPlayed() {
		return updateLastPlayed;
	}

	private boolean updateBPM = false;

	/**
	 *
	 * @return
	 */
	public boolean isUpdateBPM() {
		return updateBPM;
	}

	private boolean updateTags = false;

	/**
	 *
	 * @return
	 */
	public boolean isUpdateTags() {
		return updateTags;
	}

	//TODO: Make this true by default as
	//present in all databases.
	//Problem is that it is in file tags too
	//so would be overwritten everytime selectedDb rescan
	//its library (because of genreModifDate and lastMergeDate)
	private boolean updateGenre = false;

	/**
	 *
	 * @return
	 */
	public boolean isUpdateGenre() {
		return updateGenre;
	}

	private boolean updatePlayCounter = false;

	/**
	 *
	 * @return
	 */
	public boolean isUpdatePlayCounter() {
		return updatePlayCounter;
	}

	/**
	 * Create a new Source
	 *
	 * @param name
	 * @param updateAddedDate
	 * @param updateLastPlayed
	 * @param rootPath
	 * @param updateBPM
	 * @param updatePlayCounter
	 * @param location
	 * @param updateTags
	 * @param updateGenre
	 */
	public StatSourceAbstract(String name, String rootPath, String location,
			boolean updateAddedDate, boolean updateLastPlayed,
			boolean updateBPM, boolean updatePlayCounter,
			boolean updateTags,
			boolean updateGenre) {
		this.updateAddedDate = updateAddedDate;
		this.updateLastPlayed = updateLastPlayed;
		this.name = name;
		this.rootPath = rootPath;
		this.updateBPM = updateBPM;
		this.updatePlayCounter = updatePlayCounter;
		this.location = location;
		this.updateTags = updateTags;
		this.updateGenre = updateGenre;
	}

	/**
	 * Get statistics (rating, last played, ...) MAKE SURE THAT: - rating is
	 * retrieved out of 5 (x/5) - dates are in "yyyy-MM-dd HH:mm:ss" format, UTC
	 *
	 * @param files
	 * @return
	 */
	public abstract boolean getStatistics(ArrayList<FileInfo> files);

	/**
	 * Update statistics (rating, last played, ...)
	 *
	 * @param files
	 * @return
	 */
	public abstract int[] updateFileStatistics(ArrayList<? extends FileInfo> files);

	/**
	 * Get user tags
	 *
	 * @param tags
	 * @param file
	 * @return
	 */
	public abstract boolean getTags(ArrayList<String> tags, FileInfo file);

	/**
	 * Connect database, ...or whatever is needed to setup before merge
	 *
	 * @param isRemote
	 * @return
	 */
	abstract public boolean setUp(boolean isRemote);

	/**
	 * DisConnect database, ... or whatever is needed to cleanup after merge
	 *
	 * @return
	 */
	abstract public boolean tearDown();

	/**
	 * Check source validity
	 *
	 * @return
	 */
	abstract public boolean check();

	//TODO: Make a class variable locationWork, for the 3 below
	/**
	 * Retrieve source to work location
	 *
	 * @param locationWork
	 * @return
	 */
	abstract public boolean getSource(String locationWork);

	/**
	 * Send back source to its original location (from work location)
	 *
	 * @param locationWork
	 * @return
	 */
	abstract public boolean sendSource(String locationWork);

	/**
	 * Backup source in work location
	 *
	 * @param locationWork
	 * @return
	 */
	abstract public boolean backupSource(String locationWork);

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 23 * hash + Objects.hashCode(this.rootPath);
		hash = 23 * hash + Objects.hashCode(this.location);
		hash = 23 * hash + Objects.hashCode(this.name);
		hash = 23 * hash + (this.updateAddedDate ? 1 : 0);
		hash = 23 * hash + (this.updateLastPlayed ? 1 : 0);
		hash = 23 * hash + (this.updateBPM ? 1 : 0);
		hash = 23 * hash + (this.updateTags ? 1 : 0);
		hash = 23 * hash + (this.updateGenre ? 1 : 0);
		hash = 23 * hash + (this.updatePlayCounter ? 1 : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final StatSourceAbstract other = (StatSourceAbstract) obj;
		if (this.updateAddedDate != other.updateAddedDate) {
			return false;
		}
		if (this.updateLastPlayed != other.updateLastPlayed) {
			return false;
		}
		if (this.updateBPM != other.updateBPM) {
			return false;
		}
		if (this.updateTags != other.updateTags) {
			return false;
		}
		if (this.updateGenre != other.updateGenre) {
			return false;
		}
		if (this.updatePlayCounter != other.updatePlayCounter) {
			return false;
		}
		if (!Objects.equals(this.rootPath, other.rootPath)) {
			return false;
		}
		if (!Objects.equals(this.location, other.location)) {
			return false;
		}
		return Objects.equals(this.name, other.name);
	}
}