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
import java.sql.SQLException;
import jamuz.utils.Popup;
import java.util.ArrayList;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class StatSourceMediaMonkey extends StatSourceSQL {

	/**
	 *
	 * @param dbInfo
	 * @param name
	 * @param rootPath
	 */
	public StatSourceMediaMonkey(DbInfo dbInfo, String name, String rootPath) {
        super(dbInfo, name, rootPath, true, true, false, true, false, false); 
        //TODO: MediaMonkey support BPM I think. Need to get a Windows machine to test and update the SQL scripts below
    }
    
    @Override
    public boolean setUp() {
        try {
            this.dbConn.connect();
            //http://www.mediamonkey.com/forum/viewtopic.php?f=2&t=25687&sid=34fcd9835844e407f703630b8b4cc7ab&start=15
            this.stSelectFileStatistics = dbConn.getConnnection().prepareStatement(
					"SELECT SongPath AS fullPath, rating/20 AS rating, playCounter, "
                    + "datetime(LastTimePlayed+2415018.5) AS lastplayed, "
                    + "datetime(DateAdded+2415018.5) AS addedDate, '' AS genre "
                    + "FROM Songs "
                    + "ORDER BY SongPath COLLATE NOCASE"); 
			//FIXME WINDOWS Should not we remove  COLLATE NOCASE as now sync is case sensitive ?	
            //WARNING: Windows is not case-sensitive ... is that a problem ? (maybe for sync)
  
            this.stUpdateFileStatistics = dbConn.getConnnection().prepareStatement(
					"UPDATE Songs SET rating=?*20, "
                    + "LastTimePlayed=(strftime('%J', ?) -2415018.5), "
                    + "DateAdded=(strftime('%J', ?) -2415018.5), "
                    + "playCounter=? "
                    + "WHERE SongPath=? COLLATE NOCASE");  //NOI18N

             return true;
        } catch (SQLException ex) {
            //Proper error handling. We should not have such an error unless above code changes
            Popup.error("StatSourceMediaMonkey, setUp", ex);   //NOI18N
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
        this.stUpdateFileStatistics.setString(3, file.getFormattedAddedDate());
        this.stUpdateFileStatistics.setInt(4, file.getPlayCounter());
        this.stUpdateFileStatistics.setString(5, this.getRootPath()+getPath(file.getRelativeFullPath()));
        this.stUpdateFileStatistics.addBatch();
    }

	@Override
	public boolean getTags(ArrayList<String> tags, FileInfo file) {
		//FIXME WIDOWS MERGE get user tags from MediaMonkey
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
