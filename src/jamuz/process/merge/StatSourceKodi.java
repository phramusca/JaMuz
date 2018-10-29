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

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class StatSourceKodi extends StatSourceSQL {

	/**
	 *
	 * @param dbInfo
	 * @param name
	 * @param rootPath
	 */
	public StatSourceKodi(DbInfo dbInfo, String name, String rootPath) {
        super(dbInfo, name, rootPath, false, true, false, true, false, false);
    }
    
    @Override
    public boolean setUp() {
        try {
            this.dbConn.connect();

            this.stSelectFileStatistics = dbConn.getConnnection().prepareStatement("SELECT (P.strPath || S.strFileName) AS fullPath, "
                    + "S.rating, S.lastplayed, S.iTimesPlayed AS playCounter, "
					+ "'1970-01-01 00:00:00' AS addedDate, '' AS genre "
                    + "FROM song S, path P WHERE S.idPath=P.idPath "
                    + "ORDER BY P.strPath, S.strFileName");
  
            //Added "strHash!=''" as after moving sync to case sensitive, found some 
            //identical except case strPath, old ones now having strHash=''
            //so "SELECT idPath" returns more than 1 result (as a like st), first one being sadly the wrong old one
            //=> changed LIKE in select idPath into an =, but kept "strHash!=''" as such folders are kind of marked as deleted
            this.stUpdateFileStatistics = dbConn.getConnnection().prepareStatement("UPDATE song SET rating=?, lastplayed=?, iTimesPlayed=? "
                    + "WHERE idPath=(SELECT idPath FROM path WHERE strPath=? AND strHash!='') AND strFileName=?");  //NOI18N

             return true;
        } catch (SQLException ex) {
            //Proper error handling. We should not have such an error unless above code changes
            Popup.error("StatSourceKodi, setUp", ex);   //NOI18N
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
        this.stUpdateFileStatistics.setInt(1, file.getRating());
        this.stUpdateFileStatistics.setString(2, file.getFormattedLastPlayed());
        this.stUpdateFileStatistics.setInt(3, file.getPlayCounter());
        this.stUpdateFileStatistics.setString(4, this.getRootPath()+getPath(file.getRelativePath())); 
        this.stUpdateFileStatistics.setString(5, file.getFilename());
        this.stUpdateFileStatistics.addBatch();
    }

	public String guessRootPath() {
        ResultSet rs = null;
        try {
            PreparedStatement stSelectPath = dbConn.getConnnection().prepareStatement(
					"SELECT strPath FROM path ORDER BY length(strPath) ASC LIMIT 1");   //NOI18N
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
		//FIXME MERGE TAGS get tags from Kodi
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	
}
