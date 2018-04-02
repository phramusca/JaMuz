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
import jamuz.FileInfoInt;
import jamuz.Jamuz;
import java.sql.SQLException;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public abstract class StatSourceJaMuzTags extends StatSourceSQL {

	/**
	 *
	 * @param dbInfo
	 * @param name
	 * @param rootPath
	 * @param updateAddedDate
	 * @param updateLastPlayed
	 * @param updateBPM
	 * @param updatePlayCounter
	 * @param updateTags
	 */
	public StatSourceJaMuzTags(DbInfo dbInfo, String name, String rootPath, 
            boolean updateAddedDate, boolean updateLastPlayed, boolean updateBPM, 
			boolean updatePlayCounter, boolean updateTags, boolean updateGenre) {
        super(dbInfo, name, rootPath, updateAddedDate, updateLastPlayed, 
				updateBPM, updatePlayCounter, updateTags, updateGenre);
    }

	/**
	 * Update statistics
	 * @param files
	 * @return
	 */
    @Override
	public int[] updateStatistics(ArrayList<? extends FileInfo> files) {
		//FIXME LOW: Include setTags in upupdateStatistics so:
		// - it is faster
		// - it will be easier to merge tags with other stat sources
		int[] results = super.updateStatistics(files);
		return setTags(files, results);
	}

	abstract public int[] setTags(ArrayList<? extends FileInfo> files, int[] results);
	
	public boolean setTags(ArrayList<String> tags, int idFile) {
		if(!deleteTagFiles(idFile)) {
			return false;
		}
		return insertTagFiles(tags, idFile);
	}

	/**
	 *
	 * @param idFile
	 * @return
	 */
	private boolean deleteTagFiles(int idFile) {
        try {
            PreparedStatement stDeleteTagFiles = dbConn.getConnnection()
					.prepareStatement(
					"DELETE FROM tagFile WHERE idFile=?");  //NOI18N
            stDeleteTagFiles.setInt(1, idFile);
            long startTime = System.currentTimeMillis();
            int result = stDeleteTagFiles.executeUpdate();
            long endTime = System.currentTimeMillis();
            Jamuz.getLogger().log(Level.FINEST, "stDeleteTagFiles DELETE "
					+ "// Total execution time: {0}ms", 
					new Object[]{endTime - startTime});    //NOI18N

            if (result < 0) {
                Jamuz.getLogger().log(Level.SEVERE, "stDeleteTagFiles, "
						+ "idFile={0}, result={1}", 
						new Object[]{idFile, result});   //NOI18N
            }
            
            return true;

        } catch (SQLException ex) {
            Popup.error("deleteTagFiles()", ex);   //NOI18N
            return false;
        }
    }
	
	private boolean insertTagFiles(ArrayList<String> tags, int idFile) {
        try {
            if (tags.size() > 0) {
                dbConn.getConnnection().setAutoCommit(false);
                int[] results;
                PreparedStatement stInsertTagFile = dbConn.getConnnection()
						.prepareStatement(
					"INSERT OR IGNORE INTO tagFile "
                    + "(idFile, idTag) "    //NOI18N
                    + "VALUES (?, (SELECT id FROM tag WHERE value=?))");   //NOI18N
                for (String tag : tags) {
                    stInsertTagFile.setInt(1, idFile);
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
								+ "idFile={0} result={2}", 
								new Object[]{idFile, result});   //NOI18N
                    }
                }
                dbConn.getConnnection().setAutoCommit(true);
            }
            return true;
        } catch (SQLException ex) {
            Popup.error("insertTagFiles(" + idFile + ")", ex);   //NOI18N
            return false;
        }
    }
}
