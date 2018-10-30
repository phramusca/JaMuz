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
import jamuz.Jamuz;
import jamuz.gui.PanelOptions;
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
public class StatSourceGuayadeque extends StatSourceSQL {

	/**
	 *
	 * @param dbInfo
	 * @param name
	 * @param rootPath
	 */
	public StatSourceGuayadeque(DbInfo dbInfo, String name, String rootPath) {
        super(dbInfo, name, rootPath, true, true, false, true, true, false);
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
            
            this.stUpdateFileStatistics = dbConn.getConnnection().prepareStatement(
					"UPDATE songs SET song_rating=?, "
						+ "song_lastplay=strftime('%s',?), "
						+ "song_addedtime=strftime('%s',?), "
						+ "song_playcount=? "
					+ " WHERE song_path=? AND song_filename=?");  //NOI18N

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
		try {
            PreparedStatement stSelectPlaylists = dbConn.getConnnection().prepareStatement(
                    "SELECT tag_name FROM tags T "
							+ " JOIN settags ST ON T.tag_id=ST.settag_tagid "
							+ " JOIN songs S ON ST.settag_songid=S.song_id "
							+ " WHERE song_path=? AND song_filename=? ");    //NOI18N
			stSelectPlaylists.setString(1, this.getRootPath()+getPath(file.getRelativePath())); 
			stSelectPlaylists.setString(2, file.getFilename());
            ResultSet rs = stSelectPlaylists.executeQuery();
            while (rs.next()) {
                tags.add(dbConn.getStringValue(rs, "tag_name"));
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
					"DELETE FROM settags "
							+ "WHERE settag_songid=("
								+ "	SELECT song_id FROM songs "
								+ " WHERE song_path=? AND song_filename=? ) ");  //NOI18N
            stDeleteTagFiles.setString(1, getRootPath()+getPath(file.getRelativePath())); 
			stDeleteTagFiles.setString(2, file.getFilename());
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
	
    private boolean isTag(String tag) {
        ResultSet rs=null;
        ResultSet keys=null;
        try {
            PreparedStatement stSelectMachine = dbConn.getConnnection().prepareStatement(
					"SELECT COUNT(*), tag_name FROM tags "
							+ "WHERE tag_name=?");   //NOI18N
            stSelectMachine.setString(1, tag);
            rs = stSelectMachine.executeQuery();
            if (rs.getInt(1) > 0) {
                return true;
            } else {
                //Insert a new tag
                PreparedStatement stInsertMachine = dbConn.getConnnection().prepareStatement(
						"INSERT INTO tags (tag_name) VALUES (?)");   //NOI18N
                stInsertMachine.setString(1, tag);
                int nbRowsAffected = stInsertMachine.executeUpdate();
                if (nbRowsAffected == 1) {
//                    keys = stInsertMachine.getGeneratedKeys();
//                    keys.next();
//                    int tag_id = keys.getInt(1);
                    rs.close();
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "stInsertMachine, "
							+ "hostname=\"{0}\" # row(s) affected: +{1}", 
							new Object[]{tag, nbRowsAffected});   //NOI18N
                    return false;
                }
            }
        } catch (SQLException ex) {
            Popup.error("isTag(" + tag + ")", ex);   //NOI18N
            return false;
        }
        finally {
            try {
                if (rs!=null) rs.close();
            } catch (SQLException ex) {
                Jamuz.getLogger().warning("Failed to close ResultSet");
            }
            
            try {
                if (keys!=null) keys.close();
            } catch (SQLException ex) {
                Jamuz.getLogger().warning("Failed to close ResultSet");
            }
            
        }
    }

	private boolean insertTagFiles(ArrayList<String> tags, FileInfo file) {
        try {
            if (tags.size() > 0) {
                dbConn.getConnnection().setAutoCommit(false);
                int[] results;
                PreparedStatement stInsertTagFile = dbConn.getConnnection()
						.prepareStatement(
					"INSERT  INTO settags "
                    + "(settag_songid, settag_tagid) "    //NOI18N
                    + "VALUES ((SELECT song_id FROM songs WHERE song_path=? AND song_filename=?), "
							+ "(SELECT tag_id FROM tags WHERE tag_name=?))");   //NOI18N
                for (String tag : tags) {
					if(isTag(tag)) { //TODO: get id instead of using tag name
						stInsertTagFile.setString(1, getRootPath()+getPath(file.getRelativePath())); 
						stInsertTagFile.setString(2, file.getFilename());
						stInsertTagFile.setString(3, tag);
						stInsertTagFile.addBatch();
					}
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
