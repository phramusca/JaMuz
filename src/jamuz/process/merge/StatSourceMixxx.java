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
import java.sql.ResultSet;
import java.sql.SQLException;
import jamuz.utils.Popup;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class StatSourceMixxx extends StatSourceSQL {

    public StatSourceMixxx(DbInfo dbInfo, String name, String rootPath) {
        super(dbInfo, name, rootPath, true, false, true);
    }

    @Override
    public boolean setUp() {
        try {
            this.dbConn.connect();

            this.stSelectFileStatistics = dbConn.getConnnection().prepareStatement("SELECT L.location AS fullPath, F.rating, "
                    + "F.timesPlayed AS playCounter, '1970-01-01 00:00:00' AS lastplayed, "
                    + "F.datetime_added AS addedDate, F.bpm "
                    + "FROM library F, track_locations L "
                    + "WHERE F.id=L.id AND mixxx_deleted=0 "
                    + "AND fs_deleted=0 "
                    + "ORDER BY L.location");
            
            this.stUpdateFileStatistics = dbConn.getConnnection().prepareStatement("UPDATE library SET rating=?, bpm=?, "
                    + "datetime_added=?, timesPlayed=? "
                    + "WHERE id=(SELECT id FROM track_locations WHERE location=?)");  //NOI18N

             return true;
        } catch (SQLException ex) {
            //Proper error handling. We should not have such an error unless above code changes
            Popup.error("StatSourceMixxx, setUp", ex);   //NOI18N
            return false;
        }
    }

    @Override
    protected FileInfo getStats(ResultSet rs) {
        try {
            String strfullPath = dbConn.getStringValue(rs, "fullPath");  //NOI18N
            String relativeFullPath = strfullPath.substring(getRootPath().length());
            int rating = rs.getInt("rating");  //NOI18N
            String lastPlayed = dbConn.getStringValue(rs, "lastplayed", "1970-01-01 00:00:00");  //NOI18N
            String addedDate = dbConn.getStringValue(rs, "addedDate", "1970-01-01 00:00:00");  //NOI18N
            int playCounter = rs.getInt("playCounter");  //NOI18N

             //OVERRIDING BECAUSE OF BPM 
            float bpm = rs.getFloat("bpm");

            return new FileInfo(-1, -1, relativeFullPath, rating, lastPlayed, addedDate, playCounter, this.getName(), 0, bpm, "");
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
}
