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
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author raph
 */
public class DaoDeviceFileWrite {

    private final DbConn dbConn;

    /**
     *
     * @param dbConn
     */
    public DaoDeviceFileWrite(DbConn dbConn) {
        this.dbConn = dbConn;
    }

    /**
     * Insert deviceFile or update status in deviceFile table. Used for remote sync.
     *
     * @param files
     * @param idDevice
     * @return
     */
    public ArrayList<FileInfoInt> insertOrUpdate(ArrayList<FileInfoInt> files, int idDevice) {
        ArrayList<FileInfoInt> insertedOrUpdated = new ArrayList<>();
        synchronized (dbConn) {
            try {
                if (!files.isEmpty()) {
                    long startTime = System.currentTimeMillis();
                    dbConn.connection.setAutoCommit(false);
                    int[] results;
                    // FIXME Z Use this ON CONFLICT syntax for other insertOrUpdateXXX methods, if
                    // applicable
                    try (PreparedStatement stInsertDeviceFile = dbConn.connection.prepareStatement(
                            "INSERT INTO deviceFile "
                            + " (idFile, idDevice, oriRelativeFullPath, status) " // NOI18N
                            + " VALUES (?, ?, ?, \"NEW\") "
                            + " ON CONFLICT(idFile, idDevice) DO UPDATE SET status=?, oriRelativeFullPath=?")) { // NOI18N
                        for (FileInfoInt file : files) {
                            stInsertDeviceFile.setInt(1, file.getIdFile());
                            stInsertDeviceFile.setInt(2, idDevice);
                            stInsertDeviceFile.setString(3, file.getRelativeFullPath());
                            stInsertDeviceFile.setString(4, file.getStatus().name());
                            stInsertDeviceFile.setString(5, file.getRelativeFullPath());
                            stInsertDeviceFile.addBatch();
                        }
                        results = stInsertDeviceFile.executeBatch();
                    }
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
    }

    /**
     * Insert in deviceFile table. Used in filesystem sync
     *
     * @param files
     * @param idDevice
     * @return
     */
    public ArrayList<FileInfoInt> insertOrIgnore(ArrayList<FileInfoInt> files, int idDevice) {
        ArrayList<FileInfoInt> inserted = new ArrayList<>();
        synchronized (dbConn) {
            try {
                if (!files.isEmpty()) {
                    long startTime = System.currentTimeMillis();
                    dbConn.connection.setAutoCommit(false);
                    int[] results;
                    try (PreparedStatement stInsertDeviceFile = dbConn.connection.prepareStatement(
                            "INSERT OR IGNORE INTO deviceFile "
                            + "(idFile, idDevice, oriRelativeFullPath, status) " // NOI18N
                            + "VALUES (?, ?, ?, \"NEW\")")) { // NOI18N
                        for (FileInfoInt file : files) {
                            stInsertDeviceFile.setInt(1, file.getIdFile());
                            stInsertDeviceFile.setInt(2, idDevice);
                            stInsertDeviceFile.setString(3, file.getRelativeFullPath());
                            stInsertDeviceFile.addBatch();
                        }
                        results = stInsertDeviceFile.executeBatch();
                    }
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
    }

    /**
     * Deletes device files for a given device
     *
     * @param idDevice
     * @return
     */
    public boolean delete(int idDevice) {
        synchronized (dbConn) {
            try (PreparedStatement stDeleteDeviceFiles = dbConn.connection.prepareStatement(
                    "DELETE FROM deviceFile WHERE idDevice=?")) {

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

}
