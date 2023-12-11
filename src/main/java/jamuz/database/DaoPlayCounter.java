/*
 * Copyright (C) 2023 raph
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
package jamuz.database;

import jamuz.FileInfo;
import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;

/**
 *
 * @author raph
 */
public class DaoPlayCounter {
    private final DbConn dbConn;

	/**
	 *
	 * @param dbConn
	 */
	public DaoPlayCounter(DbConn dbConn) {
		this.dbConn = dbConn;
	}
    
    /**
	 * Sets previous playCounter in playcounter table
	 *
	 * @param files
	 * @param idStatSource
	 * @return
	 */
	public boolean update(ArrayList<? super FileInfoInt> files, int idStatSource) {
		try {
			int[] results;
			PreparedStatement stUpdatePlayCounter = dbConn.connection.prepareStatement(
					"UPDATE playcounter SET playCounter=? "
					+ "WHERE idFile=? AND idStatSource=?"); // NOI18N
			// First try to update values
			dbConn.connection.setAutoCommit(false);
			for (Iterator<? super FileInfoInt> it = files.iterator(); it.hasNext();) {
				FileInfo file = (FileInfo) it.next();
				stUpdatePlayCounter.setInt(1, file.getPlayCounter());
				stUpdatePlayCounter.setInt(2, file.getIdFile());
				stUpdatePlayCounter.setInt(3, idStatSource);
				stUpdatePlayCounter.addBatch();
			}
			long startTime = System.currentTimeMillis();
			results = stUpdatePlayCounter.executeBatch();
			dbConn.connection.commit();
			long endTime = System.currentTimeMillis();
			Jamuz.getLogger().log(Level.FINEST, "setPreviousPlayCounter UPDATE // {0} // Total execution time: {1}ms",
					new Object[]{results.length, endTime - startTime}); // NOI18N

			// If update failed, try to insert values
			int result;
			FileInfo file;
			boolean doInsertBatch = false;
			PreparedStatement stInsertPlayCounter = dbConn.connection
					.prepareStatement("INSERT OR IGNORE INTO playcounter "
							+ "(idFile, idStatSource, playCounter) " // NOI18N
							+ "VALUES (?, ?, ?)"); // NOI18N
			for (int i = 0; i < results.length; i++) {
				result = results[i];
				if (result != 1) {
					file = (FileInfo) files.get(i);
					stInsertPlayCounter.setInt(1, file.getIdFile());
					stInsertPlayCounter.setInt(2, idStatSource);
					stInsertPlayCounter.setInt(3, file.getPlayCounter());
					stInsertPlayCounter.addBatch();
					doInsertBatch = true;
				}
			}
			if (doInsertBatch) {
				startTime = System.currentTimeMillis();
				results = stInsertPlayCounter.executeBatch();
				dbConn.connection.commit();
				endTime = System.currentTimeMillis();
				Jamuz.getLogger().log(Level.FINEST, "setPreviousPlayCounter "
						+ "INSERT // {0} // Total execution time: {1}ms",
						new Object[]{results.length, endTime - startTime}); // NOI18N
				// Check results
				for (int i = 0; i < results.length; i++) {
					result = results[i];
					if (result != 1) {
						return false;
					}
				}
			}
			dbConn.connection.setAutoCommit(true);
			return true;

		} catch (SQLException ex) {
			Popup.error("setPreviousPlayCounter(" + idStatSource + ")", ex); // NOI18N
			return false;
		}
	}
}
