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

import jamuz.FileInfo;
import jamuz.DbInfo;
import jamuz.Jamuz;
import java.sql.SQLException;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class StatSourceJaMuzRemote extends StatSourceJaMuzTags {

	/**
	 *
	 * @param dbInfo
	 * @param name
	 * @param rootPath
	 */
	public StatSourceJaMuzRemote(DbInfo dbInfo, String name, String rootPath) {
        super(dbInfo, name, rootPath, true, true, false, true, true, true);
    }

    @Override
    public boolean setUp() {
        try {
            this.dbConn.connect();
            
            this.stSelectFileStatistics = dbConn.getConnnection().prepareStatement(
					"SELECT id, path AS fullPath, rating, playCounter, "
							+ "lastplayed, addedDate, genre "
							+ "FROM tracks ORDER BY path");
            
            this.stUpdateFileStatistics = dbConn.getConnnection().prepareStatement(
					"UPDATE tracks SET rating=?, playCounter=?, lastplayed=?, "
							+ "addedDate=?, genre=? "
							+ "WHERE path=?");  //NOI18N

             return true;
        } catch (SQLException ex) {
            //Proper error handling. We should not have such an error unless above code changes
            Popup.error("StatSourceJaMuzRemote, setUp", ex);   //NOI18N
            return false;
        }
    }

    /**
     * Set update statistics parameters
     * @param file
     * @throws SQLException
     */
    @Override
    protected void setUpdateStatisticsParameters(FileInfo file) throws SQLException {
        
// %path%       : this.rootPath+getPath(file.relativePath)
// %fullPath%   : this.rootPath+getPath(file.relativeFullPath)
// %filename%   : file.filename
        
		//rating=?, playCounter=?, lastplayed=?, addedDate=? WHERE path=?
        this.stUpdateFileStatistics.setInt(1, file.getRating());
		this.stUpdateFileStatistics.setInt(2, file.getPlayCounter());
        this.stUpdateFileStatistics.setString(3, file.getFormattedLastPlayed());
        this.stUpdateFileStatistics.setString(4, file.getFormattedAddedDate());
		this.stUpdateFileStatistics.setString(5, file.getGenre());
        this.stUpdateFileStatistics.setString(6, this.getRootPath()+getPath(file.getRelativeFullPath()));
        this.stUpdateFileStatistics.addBatch();
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
			String lastPlayed = dbConn
					.getStringValue(rs, "lastplayed", "1970-01-01 00:00:00");  //NOI18N
			String addedDate = dbConn
					.getStringValue(rs, "addedDate", "1970-01-01 00:00:00");  //NOI18N
			int playCounter = rs.getInt("playCounter");  //NOI18N
			String genre = dbConn.getStringValue(rs, "genre");  //NOI18N
			FileInfo fileInfo = new FileInfo(-1, -1, relativeFullPath, 
					rating, lastPlayed, addedDate, playCounter, this.getName(), 
					0, Float.valueOf(0), genre, "", "", "");
			
			fileInfo.setIdFileRemote(rs.getInt("id"));
			return fileInfo;
		} catch (SQLException ex) {
			Popup.error("getStatistics", ex);  //NOI18N
			return null;
		}
	}
	
	@Override
	public int[] setTags(ArrayList<? extends FileInfo> files, int[] results) {
		int i=0;
		for(FileInfo fileInfo : files) {
			if(fileInfo.getTags()!=null) {
				if(!setTags(fileInfo.getTags(), fileInfo.getIdFileRemote())) {
					results[i]=0;
				}
			}
			i++;
		}
		return results;
	}
	
	@Override
	public boolean getTags(ArrayList<String> tags, FileInfo file) {
        ResultSet rs=null;
        try {
            PreparedStatement st = dbConn.getConnnection().prepareStatement(
				"SELECT value FROM tag T " +
                "JOIN tagFile F ON T.id=F.idTag " +
                "WHERE F.idFile=?");
			st.setInt(1, file.getIdFileRemote());
			rs = st.executeQuery();
            while (rs.next()) {
				tags.add(dbConn.getStringValue(rs, "value"));
            }
            return true;
        } catch (SQLException ex) {
            Popup.error(ex);
			Jamuz.getLogger().log(Level.SEVERE, "getTags", ex);  //NOI18N
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

}
