/*
 * Copyright (C) 2023 phramusca <phramusca@gmail.com>
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
package jamuz;

import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoDeviceFile {

	private final DbConn dbConn;

	/**
	 *
	 * @param dbConn
	 */
	public DaoDeviceFile(DbConn dbConn) {
		this.dbConn = dbConn;
	}
	
	/**
	 * Insert or update in deviceFile table
	 *
	 * @param files
	 * @param idDevice
	 * @return
	 */
	public synchronized ArrayList<FileInfoInt> insertOrUpdate(ArrayList<FileInfoInt> files, int idDevice) {
		ArrayList<FileInfoInt> insertedOrUpdated = new ArrayList<>();
		try {
			if (!files.isEmpty()) {
				long startTime = System.currentTimeMillis();
				dbConn.connection.setAutoCommit(false);
				int[] results;
				// FIXME Z Use this ON CONFLICT syntax for other insertOrUpdateXXX methods, if
				// applicable
				PreparedStatement stInsertDeviceFile = dbConn.connection.prepareStatement(
						"INSERT INTO deviceFile "
						+ " (idFile, idDevice, oriRelativeFullPath, status) " // NOI18N
						+ " VALUES (?, ?, ?, \"NEW\") "
						+ " ON CONFLICT(idFile, idDevice) DO UPDATE SET status=?, oriRelativeFullPath=?"); // NOI18N
				for (FileInfoInt file : files) {
					stInsertDeviceFile.setInt(1, file.idFile);
					stInsertDeviceFile.setInt(2, idDevice);
					stInsertDeviceFile.setString(3, file.relativeFullPath);
					stInsertDeviceFile.setString(4, file.status.name());
					stInsertDeviceFile.setString(5, file.relativeFullPath);
					stInsertDeviceFile.addBatch();
				}
				results = stInsertDeviceFile.executeBatch();
				dbConn.connection.commit();
				// Check results
				int result;
				for (int i = 0; i < results.length; i++) {
					result = results[i];
					if (result > 0) {
						insertedOrUpdated.add(files.get(i));
					}
				}
				dbConn.connection.setAutoCommit(true);
				long endTime = System.currentTimeMillis();
				Jamuz.getLogger().log(Level.FINEST, "insertOrUpdateDeviceFiles // {0} // Total execution time: {1}ms",
						new Object[]{results.length, endTime - startTime}); // NOI18N
			}

			return insertedOrUpdated;
		} catch (SQLException ex) {
			Popup.error("insertDeviceFile(" + idDevice + ")", ex); // NOI18N
			return insertedOrUpdated;
		}
	}

	/**
	 * Insert in deviceFile table
	 *
	 * @param files
	 * @param idDevice
	 * @return
	 */
	public synchronized ArrayList<FileInfoInt> insertOrIgnore(ArrayList<FileInfoInt> files, int idDevice) {
		ArrayList<FileInfoInt> inserted = new ArrayList<>();
		try {
			if (!files.isEmpty()) {
				long startTime = System.currentTimeMillis();
				dbConn.connection.setAutoCommit(false);
				int[] results;
				PreparedStatement stInsertDeviceFile = dbConn.connection.prepareStatement(
						"INSERT OR IGNORE INTO deviceFile "
						+ "(idFile, idDevice, oriRelativeFullPath, status) " // NOI18N
						+ "VALUES (?, ?, ?, \"NEW\")"); // NOI18N
				for (FileInfoInt file : files) {
					stInsertDeviceFile.setInt(1, file.idFile);
					stInsertDeviceFile.setInt(2, idDevice);
					stInsertDeviceFile.setString(3, file.relativeFullPath);
					stInsertDeviceFile.addBatch();
				}
				results = stInsertDeviceFile.executeBatch();
				dbConn.connection.commit();
				// Check results
				int result;
				for (int i = 0; i < results.length; i++) {
					result = results[i];
					if (result > 0) {
						inserted.add(files.get(i));
					}
				}
				dbConn.connection.setAutoCommit(true);
				long endTime = System.currentTimeMillis();
				Jamuz.getLogger().log(Level.FINEST, "insertDeviceFiles // {0} // Total execution time: {1}ms",
						new Object[]{results.length, endTime - startTime}); // NOI18N
			}

			return inserted;
		} catch (SQLException ex) {
			Popup.error("insertDeviceFile(" + idDevice + ")", ex); // NOI18N
			return inserted;
		}
	}

	/**
	 * Insert in deviceFile table
	 *
	 * @param file
	 * @param idDevice
	 * @return
	 */
	public synchronized boolean insertOrIgnore(int idDevice, FileInfoInt file) {
		try {
			int result;
			PreparedStatement stInsertDeviceFile = dbConn.connection.prepareStatement(
					"INSERT OR IGNORE INTO deviceFile "
					+ "(idFile, idDevice, oriRelativeFullPath) "
					+ "VALUES (?, ?, ?)"); // NOI18N

			stInsertDeviceFile.setInt(1, file.idFile);
			stInsertDeviceFile.setInt(2, idDevice);
			stInsertDeviceFile.setString(3, file.relativeFullPath);

			long startTime = System.currentTimeMillis();
			result = stInsertDeviceFile.executeUpdate();
			long endTime = System.currentTimeMillis();
			Jamuz.getLogger().log(Level.FINEST, "insertDeviceFile UPDATE // {0} "
					+ "// Total execution time: {1}ms",
					new Object[]{result, endTime - startTime}); // NOI18N

			if (result < 0) {
				Jamuz.getLogger().log(Level.SEVERE, "insertDeviceFile, "
						+ "idFile={0}, idDevice={1}, result={2}",
						new Object[]{file.getIdFile(), idDevice, result}); // NOI18N
				return false;
			}
			return true;
		} catch (SQLException ex) {
			Popup.error("insertDeviceFile(" + idDevice + ", " + file.getIdFile() + ")", ex); // NOI18N
			return false;
		}
	}

	/**
	 *
	 * @param idDevice
	 * @return
	 */
	public synchronized boolean delete(int idDevice) {
		try {
			PreparedStatement stDeleteDeviceFiles = dbConn.connection.prepareStatement(
					"DELETE FROM deviceFile WHERE idDevice=?"); // NOI18N
			stDeleteDeviceFiles.setInt(1, idDevice);
			long startTime = System.currentTimeMillis();
			int result = stDeleteDeviceFiles.executeUpdate();
			long endTime = System.currentTimeMillis();
			Jamuz.getLogger().log(Level.FINEST, "stDeleteDeviceFile DELETE "
					+ "// Total execution time: {0}ms",
					new Object[]{endTime - startTime}); // NOI18N
			if (result < 0) {
				Jamuz.getLogger().log(Level.SEVERE,
						"stDeleteDeviceFile, idDevice={0}, result={1}",
						new Object[]{idDevice, result}); // NOI18N
			}
			return true;
		} catch (SQLException ex) {
			Popup.error("deleteDeviceFiles()", ex); // NOI18N
			return false;
		}
	}
}
