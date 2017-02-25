/*
 * Copyright (C) 2015 phramusca ( https://github.com/phramusca/JaMuz/ )
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

package jamuz;

import java.util.ArrayList;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
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
    
    
    /**
	 * Do we update added date ?
	 */
	protected boolean updateAddedDate = false;

	/**
	 *
	 * @return
	 */
	public boolean isUpdateAddedDate() {
        return updateAddedDate;
    }

	/**
	 *
	 * @return
	 */
	public boolean isUpdateLastPlayed() {
        return updateLastPlayed;
    }

	/**
	 *
	 * @return
	 */
	public boolean isUpdateBPM() {
        return updateBPM;
    }
    
	/**
	 *
	 * @return
	 */
	public boolean isUpdatePlayCounter() {
		//FIXME HIGH Fix problem with PlayCounter:
		//When playcounter is updated (inserted) in a merge, the next merge
		//play counters starts incrementing !!!!!
		//=> Seems to occur only when many statsources (more than 2 ?), to
		// be analyzed
		//Then, enable back
//		return true;
        return false;
    }
	
	/**
	 * Do we update last played ?
	 */
	protected boolean updateLastPlayed = false;

	/**
	 *
	 */
	protected boolean updateBPM = false;
    
    /**
     * Create a new Source for Statistics (rating, last played, ...)
     * @param name
     * @param updateAddedDate
     * @param updateLastPlayed
     * @param rootPath
     * @param updateBPM
     * @param location
     */
    public StatSourceAbstract(String name, String rootPath, 
            boolean updateAddedDate, boolean updateLastPlayed, boolean updateBPM, String location) {
        this.updateAddedDate = updateAddedDate;
		this.updateLastPlayed = updateLastPlayed;
        this.name = name;
        this.rootPath = rootPath;
        this.updateBPM = updateBPM;
        this.location = location;
    }
 
    /**
     * Get statistics (rating, last played, ...)
     * MAKE SURE THAT:
     *  - rating is retrieved out of 5 (x/5)
     *  - dates are in "yyyy-MM-dd HH:mm:ss" format, UTC
     *  - TODO: State other limitations if any
     * @param files
     * @return
     */
    public abstract boolean getStatistics(ArrayList<FileInfo> files);

    /**
     * Update statistics (rating, last played, ...)
     * @param files
     * @return
     */
    public abstract int[] updateStatistics(ArrayList<? extends FileInfo> files);
    
    /**
     * Connect database, prepare predefined SQL statements, ...
     * or whatever is needed to setup before merge
     * @return
     */
    abstract public boolean setUp();
    
    /**
     * DisConnect database, ...
     * or whatever is needed to cleanup after merge
     * @return
     */
    abstract public boolean tearDown();

    /**
     * Check source validity
     * @return
     */
    abstract public boolean check();
    
    //TODO: Make a class variable locationWork, for the 3 below
    
    /**
     * Retrieve source to work location
     * @param locationWork
     * @return
     */
    abstract public boolean getSource(String locationWork); 
    
    /**
     * Send back source to its original location (from work location)
     * @param locationWork
     * @return
     */
    abstract public boolean sendSource(String locationWork);
    
    /**
     * Backup source in work location
     * @param locationWork
     * @return
     */
    abstract public boolean backupSource(String locationWork);
}
