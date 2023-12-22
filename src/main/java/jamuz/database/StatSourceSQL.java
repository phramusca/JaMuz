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
import jamuz.Jamuz;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public abstract class StatSourceSQL extends StatSourceAbstract {
	//TODO: Make a "StatSourceTheAudioDb" (See TheMovieDb class & website http://www.theaudiodb.com/)
	// => NEED TO FIND A JAVA LIB FIRST !

	//TODO: Make a "StatSourceMusicBrainz" (See ReleaseMB class and ManualTesting class - stop at ReleaseGroup level, ie not go to Medium level)
	/**
	 *
	 */
	protected final DbConn dbConn;

	/**
	 *
	 * @return
	 */
	public DbConn getDbConn() {
		return dbConn;
	}

	/**
	 *
	 */
	protected PreparedStatement stSelectFileStatistics;

	/**
	 *
	 */
	protected PreparedStatement stUpdateFileStatistics;

	/**
	 *
	 * @param dbInfo
	 * @param name
	 * @param updateAddedDate
	 * @param updateLastPlayed
	 * @param rootPath
	 * @param updateBPM
	 * @param updatePlayCounter
	 * @param updateTags
	 * @param updateGenre
	 */
	public StatSourceSQL(DbInfo dbInfo, String name, String rootPath,
			boolean updateAddedDate, boolean updateLastPlayed,
			boolean updateBPM, boolean updatePlayCounter,
			boolean updateTags, boolean updateGenre) {
		super(name, rootPath, dbInfo.getLocationOri(), updateAddedDate,
				updateLastPlayed, updateBPM, updatePlayCounter,
				updateTags, updateGenre);
		dbConn = new DbConn(dbInfo);
	}

	/**
	 *
	 * @param files
	 * @return
	 */
	@Override
	public boolean getStatistics(ArrayList<FileInfo> files) {
		ResultSet rs = null;
		try {
			FileInfo myFileInfo;
			rs = this.stSelectFileStatistics.executeQuery();
			while (rs.next()) {
				myFileInfo = getFileStatistics(rs);
				files.add(myFileInfo);
			}
			return true;
		} catch (SQLException ex) {
			Popup.error(ex);
			Jamuz.getLogger().log(Level.SEVERE, "getStatistics", ex);  //NOI18N
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				Jamuz.getLogger().warning("Failed to close ResultSet");
			}

		}
	}

	/**
	 *
	 * @param rs
	 * @return
	 */
	protected FileInfo getFileStatistics(ResultSet rs) {
		try {
			String strfullPath = dbConn.getStringValue(rs, "fullPath");  //NOI18N
			String relativeFullPath = strfullPath.substring(getRootPath().length());
			int rating = rs.getInt("rating");  //NOI18N
			String lastPlayed = dbConn
					.getStringValue(rs, "lastplayed", "1970-01-01 00:00:00");  //NOI18N
			String addedDate = dbConn
					.getStringValue(rs, "addedDate", "1970-01-01 00:00:00");  //NOI18N
			int playCounter = rs.getInt("playCounter");  //NOI18N

			return new FileInfo(-1, -1, relativeFullPath, rating, lastPlayed,
					addedDate, playCounter, this.getName(), 0, Float.valueOf(0),
					"", "", "", "");
		} catch (SQLException ex) {
			Popup.error("getStatistics", ex);  //NOI18N
			return null;
		}
	}

	/**
	 * Update statistics
	 *
	 * @param files
	 * @return
	 */
	@Override
	public int[] updateFileStatistics(ArrayList<? extends FileInfo> files) {
		try {
			dbConn.getConnection().setAutoCommit(false);
			for (FileInfo file : files) {
				setUpdateStatisticsParameters(file);
			}
			long startTime = System.currentTimeMillis();
			int[] results = this.stUpdateFileStatistics.executeBatch();
			dbConn.getConnection().commit();
			long endTime = System.currentTimeMillis();
			Jamuz.getLogger().log(Level.FINEST, "updateStatistics // {0} // Total execution time: {1}ms", new Object[]{results.length, endTime - startTime});   //NOI18N
			dbConn.getConnection().setAutoCommit(true);
			return results;
		} catch (SQLException ex) {
			Popup.error(ex);
			return new int[0];
		}
	}

	/**
	 *
	 * @param file
	 * @throws SQLException
	 */
	abstract protected void setUpdateStatisticsParameters(FileInfo file) throws SQLException;

	// Some audio players do not store path in current OS style
	// Exemple: Mixxx store paths in linux style even running on Windows
	// Since rootPath must use the good separator, we can use it to do conversions
	/**
	 *
	 * @param path
	 * @return
	 */
	protected String getPath(String path) {
		if (this.getRootPath().contains("/")) { //NOI18N
			path = FilenameUtils.separatorsToUnix(path);
		} else {
			path = FilenameUtils.separatorsToWindows(path);
		}
		return path;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public boolean check() {
		return this.dbConn.getInfo().check();
	}

	/**
	 *
	 * @param locationWork
	 * @return
	 */
	@Override
	public boolean getSource(String locationWork) {
		return this.dbConn.getInfo().copyDB(true, locationWork);
	}

	/**
	 *
	 * @param locationWork
	 * @return
	 */
	@Override
	public boolean sendSource(String locationWork) {
		return this.dbConn.getInfo().copyDB(false, locationWork);
	}

	/**
	 *
	 * @param locationWork
	 * @return
	 */
	@Override
	public boolean backupSource(String locationWork) {
		return this.dbConn.getInfo().backupDB(locationWork);
	}

	/**
	 *
	 * @return
	 */
	@Override
	public boolean tearDown() {
		this.dbConn.disconnect();
		return true;
	}

}
