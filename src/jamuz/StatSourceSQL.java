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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.commons.io.FilenameUtils;
import jamuz.utils.Popup;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public abstract class StatSourceSQL extends StatSourceAbstract  {
    //TODO: Make a "StatSourceTheAudioDb" (See TheMovieDb class & website http://www.theaudiodb.com/)
    // => NEED TO FIND A JAVA LIB FIRST !
    
    //TODO: Make a "StatSourceMusicBrainz" (See ReleaseMB class and ManualTesting class - stop at ReleaseGroup level, ie not go to Medium level)
    protected final DbConn dbConn;

    public DbConn getDbConn() {
        return dbConn;
    }
    
    //TODO: Declare and setup where used
    protected PreparedStatement stSelectFileStatistics;
    protected PreparedStatement stUpdateFileStatistics;
    /**
     *
     * @param dbInfo
     * @param name
     * @param updateAddedDate
     * @param updateLastPlayed
     * @param rootPath
     * @param updateBPM
     */
    public StatSourceSQL(DbInfo dbInfo, String name, String rootPath, 
            boolean updateAddedDate, boolean updateLastPlayed, boolean updateBPM) {
        super(name, rootPath, updateAddedDate, updateLastPlayed, updateBPM, dbInfo.locationOri);
        dbConn = new DbConn(dbInfo);
    }

    @Override
    public boolean getStatistics(ArrayList<FileInfo> files) {
        ResultSet rs=null;
        try {
            FileInfo myFileInfo;
            rs = this.stSelectFileStatistics.executeQuery();
            while (rs.next()) {
				myFileInfo = getStats(rs);
				files.add(myFileInfo);
            }
            return true;
        } catch (SQLException ex) {
            Popup.error(ex);
			Jamuz.getLogger().log(Level.SEVERE, "getStatistics", ex);  //NOI18N
			return false;
        }
        finally {
            try {
                if (rs!=null) rs.close();
            } catch (SQLException ex) {
                Jamuz.getLogger().warning("Failed to close ResultSet");
            }
            
        }
    }
    
    protected FileInfo getStats(ResultSet rs) {
		try {
            String strfullPath = dbConn.getStringValue(rs, "fullPath");  //NOI18N
            String relativeFullPath = strfullPath.substring(getRootPath().length());
			int rating = rs.getInt("rating");  //NOI18N
			String lastPlayed = dbConn.getStringValue(rs, "lastplayed", "1970-01-01 00:00:00");  //NOI18N
			String addedDate = dbConn.getStringValue(rs, "addedDate", "1970-01-01 00:00:00");  //NOI18N
			int playCounter = rs.getInt("playCounter");  //NOI18N

			return new FileInfo(-1, -1, relativeFullPath, rating, lastPlayed, addedDate, playCounter, this.getName(), 0, Float.valueOf(0), "");
		} catch (SQLException ex) {
			Popup.error("getStats", ex);  //NOI18N
			return null;
		}
	}
    
    /**
	 * Update statistics
	 * @param files
	 * @return
	 */
    @Override
	public int[] updateStatistics(ArrayList<? extends FileInfo> files) {
		try {
			dbConn.connection.setAutoCommit(false);
			for(FileInfo file : files) {
                setUpdateStatisticsParameters(file);
			}
			long startTime = System.currentTimeMillis();
			int[] results = this.stUpdateFileStatistics.executeBatch();
			dbConn.connection.commit();
			long endTime = System.currentTimeMillis();
			Jamuz.getLogger().log(Level.FINEST, "updateStatistics // {0} // Total execution time: {1}ms", new Object[]{results.length, endTime-startTime});   //NOI18N
			dbConn.connection.setAutoCommit(true);
			return results;
		} catch (SQLException ex) {
			Popup.error(ex);
            return new int[0];
		}
	}
    
    abstract protected void setUpdateStatisticsParameters(FileInfo file) throws SQLException;

    // Some audio players do not store path in current OS style
    // Exemple: Mixxx store paths in linux style even running on Windows
    // Since rootPath must use the good separator, we can use it to do conversions
    protected String getPath(String path) {
		if(this.getRootPath().contains("/")) { //NOI18N
			path = FilenameUtils.separatorsToUnix(path);
		}
		else {
			path = FilenameUtils.separatorsToWindows(path);
		}
        return path;
    }
    
    @Override
    public boolean check() {
        return this.dbConn.info.check();
    }

    @Override
    public boolean getSource(String locationWork) {
        return this.dbConn.info.copyDB(true, locationWork);
    }

    @Override
    public boolean sendSource(String locationWork) {
        return this.dbConn.info.copyDB(false, locationWork);
    }

    @Override
    public boolean backupSource(String locationWork) {
        return this.dbConn.info.backupDB(locationWork);
    }
    
    @Override
    public boolean tearDown() {
        this.dbConn.disconnect();
        return true;
    }

}
