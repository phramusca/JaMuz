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

import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.process.check.ReplayGain;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author raph
 */
public class DaoFileTranscoded {

    private final DbConn dbConn;

    /**
     *
     * @param dbConn
     */
    public DaoFileTranscoded(DbConn dbConn) {
        this.dbConn = dbConn;
    }
    
    	/**
	 *
	 * @param files
	 */
	public void insertOrUpdate(ArrayList<FileInfoInt> files) {
		try {
			if (!files.isEmpty()) {
				long startTime = System.currentTimeMillis();
				dbConn.connection.setAutoCommit(false);
				int[] results;
				PreparedStatement preparedStatement = dbConn.connection.prepareStatement(
						"INSERT INTO fileTranscoded "
						+ " (idFile, ext, bitRate, format, length, size, trackGain, albumGain, modifDate) " // NOI18N
						+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) "
						+ " ON CONFLICT(idFile, ext) DO UPDATE SET bitRate=?, format=?, length=?, size=?, trackGain=?, albumGain=?, modifDate=?"); // NOI18N
				for (FileInfoInt file : files) {
					// Insert
					preparedStatement.setInt(1, file.getIdFile());
					preparedStatement.setString(2, file.getExt());
					preparedStatement.setString(3, file.getBitRate());
					preparedStatement.setString(4, file.getFormat());
					preparedStatement.setInt(5, file.getLength());
					preparedStatement.setLong(6, file.getSize());
					ReplayGain.GainValues gainValues = file.getReplayGain(false);
					preparedStatement.setFloat(7, gainValues.getTrackGain());
					preparedStatement.setFloat(8, gainValues.getAlbumGain());
					preparedStatement.setString(9, file.getFormattedModifDate());
					// Update
					preparedStatement.setString(10, file.getBitRate());
					preparedStatement.setString(11, file.getFormat());
					preparedStatement.setInt(12, file.getLength());
					preparedStatement.setLong(13, file.getSize());
					preparedStatement.setFloat(14, gainValues.getTrackGain());
					preparedStatement.setFloat(15, gainValues.getAlbumGain());
					preparedStatement.setString(16, file.getFormattedModifDate());
					preparedStatement.addBatch();
				}
				results = preparedStatement.executeBatch();
				dbConn.connection.commit();
				dbConn.connection.setAutoCommit(true);
				long endTime = System.currentTimeMillis();
				Jamuz.getLogger().log(Level.FINEST,
						"insertOrUpdateDeviceFilesTranslated // {0} // Total execution time: {1}ms",
						new Object[]{results.length, endTime - startTime}); // NOI18N
			}
		} catch (SQLException ex) {
			Popup.error("insertOrUpdateDeviceFilesTranslated(ArrayList<FileInfoInt> files)", ex); // NOI18N
		}
	}
}
