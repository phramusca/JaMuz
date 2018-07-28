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
//FIXME LOW MERGE Problem with Guayadeque: 3 albums keep on merging
// Album inconnu (02,10,2008 18_32_32)/
// Autres/Nuclear Device = [1987, #2] Western Electric [France] ~MP3~/
// Dr Dre D.I.C
// + 1 file beach boys not found

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
public class StatSourceGuayadeque extends StatSourceSQL {

	/**
	 *
	 * @param dbInfo
	 * @param name
	 * @param rootPath
	 */
	public StatSourceGuayadeque(DbInfo dbInfo, String name, String rootPath) {
        super(dbInfo, name, rootPath, true, true, false, true, false, false);
    }

    @Override
    public boolean setUp() {
        try {
            this.dbConn.connect();
            
            this.stSelectFileStatistics = dbConn.getConnnection().prepareStatement(
					"SELECT (song_path || song_filename) AS fullPath, "
							+ "song_rating AS rating, "
                    + "song_playcount AS playCounter,  "
					+ "datetime(song_lastplay, 'unixepoch') AS lastplayed, "
                    + "datetime(song_addedtime, 'unixepoch') AS addedDate,"
							+ "'' AS genre  "
                    + "FROM songs ORDER BY song_path, song_filename");
            
            this.stUpdateFileStatistics = dbConn.getConnnection().prepareStatement("UPDATE songs SET song_rating=?, song_lastplay=strftime('%s',?), "
                    + "song_addedtime=strftime('%s',?), song_playcount=? "
                + "WHERE song_path=? AND song_filename=?");  //NOI18N

             return true;
        } catch (SQLException ex) {
            //Proper error handling. We should not have such an error unless above code changes
            Popup.error("StatSourceGuayadeque, setUp", ex);   //NOI18N
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
        
        this.stUpdateFileStatistics.setInt(1, file.getRating());
        this.stUpdateFileStatistics.setString(2, file.getFormattedLastPlayed());
        this.stUpdateFileStatistics.setString(3, file.getFormattedAddedDate());
        this.stUpdateFileStatistics.setInt(4, file.getPlayCounter());
        this.stUpdateFileStatistics.setString(5, this.getRootPath()+getPath(file.getRelativePath())); 
        this.stUpdateFileStatistics.setString(6, file.getFilename());
        this.stUpdateFileStatistics.addBatch();
    }

	@Override
	public boolean getTags(ArrayList<String> tags, FileInfo file) {
		//FIXME MERGE TAGS get tags from Guayadeque
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


}
