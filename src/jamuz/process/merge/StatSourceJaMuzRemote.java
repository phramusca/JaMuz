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
public class StatSourceJaMuzRemote extends StatSourceSQL {

	/**
	 *
	 * @param dbInfo
	 * @param name
	 * @param rootPath
	 */
	public StatSourceJaMuzRemote(DbInfo dbInfo, String name, String rootPath) {
        super(dbInfo, name, rootPath, true, true, false, true, true);
    }

    @Override
    public boolean setUp() {
        try {
            this.dbConn.connect();
            
            this.stSelectFileStatistics = dbConn.getConnnection().prepareStatement(
					"SELECT path AS fullPath, rating, playCounter, lastplayed, addedDate "
							+ "FROM tracks ORDER BY path");
            
            this.stUpdateFileStatistics = dbConn.getConnnection().prepareStatement(
					"UPDATE tracks SET rating=?, playCounter=?, lastplayed=?, addedDate=? WHERE path=?");  //NOI18N

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
        this.stUpdateFileStatistics.setString(5, this.getRootPath()+getPath(file.getRelativeFullPath()));
        this.stUpdateFileStatistics.addBatch();
    }

	@Override
	public boolean getTags(ArrayList<String> tags, FileInfo file) {
		//FIXME TAGS get tags from JaMuz Remote
        ResultSet rs=null;
        try {
            PreparedStatement st = dbConn.getConnnection().prepareStatement(
				"SELECT value FROM tag T " +
                "JOIN tagFile F ON T.id=F.idTag " +
                "WHERE F.idFile=?");
			st.setInt(1, file.getIdFile());
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
