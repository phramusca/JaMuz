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

package jamuz.process.merge;

import jamuz.StatSourceSQL;
import jamuz.FileInfo;
import jamuz.DbInfo;
import jamuz.Jamuz;
import java.sql.ResultSet;
import java.sql.SQLException;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class StatSourceMixxx extends StatSourceSQL {

	/**
	 *
	 * @param dbInfo
	 * @param name
	 * @param rootPath
	 */
	public StatSourceMixxx(DbInfo dbInfo, String name, String rootPath) {
        super(dbInfo, name, rootPath, true, false, true, true, true, false);
    }

    @Override
    public boolean setUp() {
        try {
            this.dbConn.connect();
			//FIXME TEST MERGE mixxx does not update well if folder modification 
			//date is not changed
			//So need to make some tests and check that we are always updating date 
			//when modifying files (tags)
            this.stSelectFileStatistics = dbConn.getConnnection().prepareStatement(
					"SELECT L.location AS fullPath, F.rating, "
                    + "F.timesPlayed AS playCounter, "
					+ "'1970-01-01 00:00:00' AS lastplayed, "
                    + "F.datetime_added AS addedDate, F.bpm, '' AS genre "
                    + "FROM library F, track_locations L "
                    + "WHERE F.id=L.id AND mixxx_deleted=0 "
                    + "AND fs_deleted=0 "
                    + "ORDER BY L.location");
            
            this.stUpdateFileStatistics = dbConn.getConnnection().prepareStatement(
					"UPDATE library SET rating=?, bpm=?, "
                    + "datetime_added=?, timesPlayed=? "
                    + "WHERE id=(SELECT id FROM track_locations WHERE location=?)");  //NOI18N

             return true;
        } catch (SQLException ex) {
            //Proper error handling. We should not have such an error unless above code changes
            Popup.error("StatSourceMixxx, setUp", ex);   //NOI18N
            return false;
        }
    }

	/**
	 *
	 * @param rs
	 * @return
	 */
	@Override
    protected FileInfo getStatistics(ResultSet rs) {
        try {
            String strfullPath = dbConn.getStringValue(rs, "fullPath");  //NOI18N
            String relativeFullPath = strfullPath.substring(getRootPath().length());
            int rating = rs.getInt("rating");  //NOI18N
            String lastPlayed = dbConn.getStringValue(rs, "lastplayed", "1970-01-01 00:00:00");  //NOI18N
            String addedDate = dbConn.getStringValue(rs, "addedDate", "1970-01-01 00:00:00");  //NOI18N
            int playCounter = rs.getInt("playCounter");  //NOI18N

             //OVERRIDING BECAUSE OF BPM 
            float bpm = rs.getFloat("bpm");

            return new FileInfo(-1, -1, relativeFullPath, rating, lastPlayed, 
					addedDate, playCounter, this.getName(), 0, bpm, "", "", "", "");
        } catch (SQLException ex) {
            Popup.error("getStats", ex);  //NOI18N
            return null;
        }
    }
    
    /**
     * Set update statistics parameters
     * @param file
     * @throws SQLException
     */
    @Override
    protected void setUpdateStatisticsParameters(FileInfo file) throws SQLException {
        this.stUpdateFileStatistics.setInt(1, file.getRating());
        this.stUpdateFileStatistics.setFloat(2, file.getBPM());
        this.stUpdateFileStatistics.setString(3, file.getFormattedAddedDate());
        this.stUpdateFileStatistics.setInt(4, file.getPlayCounter());
        this.stUpdateFileStatistics.setString(5, this.getRootPath()+getPath(file.getRelativeFullPath())); 
        this.stUpdateFileStatistics.addBatch();
    }
	
	public String guessRootPath() {
        ResultSet rs = null;
        try {
            
            PreparedStatement stSelectPath = dbConn.getConnnection().prepareStatement(
					"SELECT directory FROM directories");   //NOI18N
            rs = stSelectPath.executeQuery();
            if (rs.next()) { //Check if we have a result, so we can move to this one
                return rs.getString(1);
            } else {
                return "";
            }
        } catch (SQLException ex) {
            Popup.error("guessRootPath()", ex);   //NOI18N
            return "";
        }
        finally {
            try {
                if (rs!=null) rs.close();
            } catch (SQLException ex) {
                Jamuz.getLogger().warning("Failed to close ResultSet");
            }
            
        }
    }

	@Override
	public boolean getTags(ArrayList<String> tags, FileInfo file) {
		try {
            PreparedStatement stSelectPlaylists = dbConn.getConnnection().prepareStatement(
                    "SELECT name FROM crates C "
							+ " JOIN crate_tracks CT ON C.id=CT.crate_id "
							+ " JOIN library L ON CT.track_id=L.id "
							+ " WHERE L.id=(SELECT id FROM track_locations WHERE location=?) ");    //NOI18N
			stSelectPlaylists.setString(1, getRootPath()+getPath(file.getRelativeFullPath()));
            ResultSet rs = stSelectPlaylists.executeQuery();
            while (rs.next()) {
                tags.add(dbConn.getStringValue(rs, "name"));
            }
			return true;
        } catch (SQLException ex) {
            Popup.error("getTags("+this.getRootPath()+getPath(file.getRelativePath())+","+file.getFilename()+")", ex);   //NOI18N
			return false;
        }
	}

	@Override
	public int[] updateStatistics(ArrayList<? extends FileInfo> files) {
		int[] results = super.updateStatistics(files);
		return setTags(files, results); 
	}
	
	public synchronized int[] setTags(ArrayList<? extends FileInfo> files, int[] results) {
		int i=0;
		for(FileInfo fileInfo : files) {
			if(fileInfo.getTags()!=null) {
				if(!setTags(fileInfo.getTags(), fileInfo)) {
					if(results!=null) {
						results[i]=0;
					}
				}
			}
			i++;
		}
		return results;
	}
	
	private boolean setTags(ArrayList<String> tags, FileInfo fileInfo) {
		if(!deleteTagFiles(fileInfo)) {
			return false;
		}
		return insertTagFiles(tags, fileInfo);
	}

	/**
	 *
	 * @param idFile
	 * @return
	 */
	private boolean deleteTagFiles(FileInfo file) {
        try {
            PreparedStatement stDeleteTagFiles = dbConn.getConnnection()
					.prepareStatement(
					"DELETE FROM crate_tracks "
							+ "WHERE track_id=(	SELECT id FROM track_locations WHERE location=? ) ");  //NOI18N
            stDeleteTagFiles.setString(1, getRootPath()+getPath(file.getRelativeFullPath()));
            long startTime = System.currentTimeMillis();
            int result = stDeleteTagFiles.executeUpdate();
            long endTime = System.currentTimeMillis();
            Jamuz.getLogger().log(Level.FINEST, "stDeleteTagFiles DELETE "
					+ "// Total execution time: {0}ms", 
					new Object[]{endTime - startTime});    //NOI18N

            if (result < 0) {
                Jamuz.getLogger().log(Level.SEVERE, "stDeleteTagFiles, "
						+ "song_path={0}, song_filename={1}, result={2}", 
						new Object[]{getRootPath()+getPath(file.getRelativePath()), file.getFilename(), result});   //NOI18N
            }
            
            return true;

        } catch (SQLException ex) {
            Popup.error("deleteTagFiles("+this.getRootPath()+getPath(file.getRelativePath())+","+file.getFilename()+")", ex);   //NOI18N
            return false;
        }
    }
	
	private boolean insertTagFiles(ArrayList<String> tags, FileInfo file) {
        try {
            if (tags.size() > 0) {
                dbConn.getConnnection().setAutoCommit(false);
                int[] results;
				//FIXME MERGE When tag name not found, insert it
				// or we end up with tag_name=null in settags table
                PreparedStatement stInsertTagFile = dbConn.getConnnection()
						.prepareStatement(
					"INSERT INTO crate_tracks "
                    + "(track_id, crate_id) "    //NOI18N
                    + "VALUES ((SELECT id FROM track_locations WHERE location=?), "
							+ "(SELECT id FROM crates WHERE name=?))");   //NOI18N
                for (String tag : tags) {
					stInsertTagFile.setString(1, getRootPath()+getPath(file.getRelativeFullPath()));
                    stInsertTagFile.setString(2, tag);
                    stInsertTagFile.addBatch();
                }
                long startTime = System.currentTimeMillis();
                results = stInsertTagFile.executeBatch();
                dbConn.getConnnection().commit();
                long endTime = System.currentTimeMillis();
                Jamuz.getLogger().log(Level.FINEST, "insertTagFiles UPDATE // {0} "
						+ "// Total execution time: {1}ms", 
						new Object[]{results.length, endTime - startTime});    //NOI18N
                //Analyse results
                int result;
                for (int i = 0; i < results.length; i++) {
                    result = results[i];
                    if (result < 0) {
                        Jamuz.getLogger().log(Level.SEVERE, "insertTagFiles, "
						+ "song_path={0}, song_filename={1}, result={2}", 
						new Object[]{getRootPath()+getPath(file.getRelativePath()), file.getFilename(), result});   //NOI18N
                    }
                }
                dbConn.getConnnection().setAutoCommit(true);
            }
            return true;
        } catch (SQLException ex) {
            Popup.error("insertTagFiles("+this.getRootPath()+getPath(file.getRelativePath())+","+file.getFilename()+")", ex);   //NOI18N
            return false;
        }
    }

}
