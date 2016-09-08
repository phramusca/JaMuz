/*
 * Copyright (C) 2015 raph
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

/**
 *
 * @author raph
 */
public class StatSourceMyTunes extends StatSourceSQL {

    public StatSourceMyTunes(DbInfo dbInfo, String name, String rootPath) {
        super(dbInfo, name, rootPath, true, true, false);
    }

    @Override
    public boolean setUp() {
        try {
            this.dbConn.connect();
            this.stSelectFileStatistics = dbConn.getConnnection().prepareStatement("SELECT path AS fullPath, rating/10 AS rating, local_play_count AS playCounter, "
                    + "datetime(last_play_time, 'unixepoch') AS lastPlayed, datetime(date_added, 'unixepoch') AS addedDate "
                    + "FROM song ORDER BY path"); 
            
            this.stUpdateFileStatistics = dbConn.getConnnection().prepareStatement("UPDATE song SET rating=10*?, local_play_count=?, "
                    + "last_play_time=strftime('%s',?), date_added=strftime('%s',?) "
                    + "WHERE path=?");  //NOI18N

             return true;
        } catch (SQLException ex) {
            //Proper error handling. We should not have such an error unless above code changes
            Popup.error("StatSourceMyTunes, setUp", ex);   //NOI18N
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
        this.stUpdateFileStatistics.setInt(2, file.getPlayCounter());
        this.stUpdateFileStatistics.setString(3, file.getFormattedLastPlayed());
        this.stUpdateFileStatistics.setString(4, file.getFormattedAddedDate());
        this.stUpdateFileStatistics.setString(5, this.getRootPath()+getPath(file.getRelativeFullPath())); 
        this.stUpdateFileStatistics.addBatch();
    }

}
