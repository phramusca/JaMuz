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
public class DaoFileTagWrite {

    private final DbConn dbConn;
    private final DaoTag daoTag;
    private final DaoFile daoFile;

    /**
     *
     * @param dbConn
     * @param daoTag
     * @param daoFile
     */
    public DaoFileTagWrite(DbConn dbConn, DaoTag daoTag, DaoFile daoFile) {
        this.dbConn = dbConn;
        this.daoTag = daoTag;
        this.daoFile = daoFile;

    }

 

    /**
     * Update tags and modification date for a list of files
     *
     * @param files
     * @param results
     * @return
     */
    public int[] update(ArrayList<? extends FileInfo> files, int[] results) {
        synchronized (dbConn) {
            int i = 0;
            try {
                dbConn.connection.setAutoCommit(false);

                for (FileInfo fileInfo : files) {
                    if (fileInfo.getTags() != null) {
                        if (!update(fileInfo.getTags(), fileInfo.getIdFile())) {
                            if (results != null) {
                                results[i] = 0;
                            }
                        }
                        if (!daoFile.lock().updateModifDate(fileInfo)) {
                            if (results != null) {
                                results[i] = 0;
                            }
                        }
                    }
                    i++;
                }

                dbConn.connection.commit();
                return results;
            } catch (SQLException ex) {
                try {
                    dbConn.connection.rollback();
                } catch (SQLException rollbackEx) {
                    // Handle rollback exception
                    rollbackEx.printStackTrace();
                }
                // Log or handle the SQL exception
                ex.printStackTrace();
                return results;
            } finally {
                try {
                    dbConn.connection.setAutoCommit(true);
                } catch (SQLException autoCommitEx) {
                    // Handle setAutoCommit exception
                    autoCommitEx.printStackTrace();
                }
            }
        }
    }

    /**
     * Update tags for a file
     *
     * @param tags
     * @param idFile
     * @return
     */
    private boolean update(ArrayList<String> tags, int idFile) {
        synchronized (dbConn) {
            if (!delete(idFile)) {
                return false;
            }
            return insert(tags, idFile);
        }
    }

    /**
     * Insert tags for a file
     *
     * @param tags
     * @param idFile
     * @return
     */
    private boolean insert(ArrayList<String> tags, int idFile) {
        synchronized (dbConn) {
            try (PreparedStatement stInsertTagFile = dbConn.connection.prepareStatement(
                    "INSERT OR IGNORE INTO tagFile (idFile, idTag) VALUES (?, (SELECT id FROM tag WHERE value=?))")) {
                if (!tags.isEmpty()) {
                    dbConn.connection.setAutoCommit(false);
                    int[] results;
                    for (String tag : tags) {
                        if (daoTag.insertIfMissing(tag)) {
                            stInsertTagFile.setInt(1, idFile);
                            stInsertTagFile.setString(2, tag);
                            stInsertTagFile.addBatch();
                        }
                    }
                    long startTime = System.currentTimeMillis();
                    results = stInsertTagFile.executeBatch();
                    dbConn.connection.commit();
                    long endTime = System.currentTimeMillis();
                    Jamuz.getLogger().log(Level.FINEST, "insertTagFiles UPDATE // {0} "
                            + "// Total execution time: {1}ms",
                            new Object[]{results.length, endTime - startTime});

                    // Analyse results
                    int result;
                    for (int i = 0; i < results.length; i++) {
                        result = results[i];
                        if (result < 0) {
                            Jamuz.getLogger().log(Level.SEVERE, "insertTagFiles, "
                                    + "idFile={0} result={2}",
                                    new Object[]{idFile, result});
                        }
                    }
                }
                return true;
            } catch (SQLException ex) {
                Popup.error("insertTagFiles(" + idFile + ")", ex);
                return false;
            }
        }
    }

    /**
     * Delete tags for a file
     *
     * @param idFile
     * @return
     */
    private boolean delete(int idFile) {
        synchronized (dbConn) {
            try (PreparedStatement stDeleteTagFiles = dbConn.connection.prepareStatement(
                    "DELETE FROM tagFile WHERE idFile=?")) {
                stDeleteTagFiles.setInt(1, idFile);
                long startTime = System.currentTimeMillis();
                int result = stDeleteTagFiles.executeUpdate();
                long endTime = System.currentTimeMillis();
                Jamuz.getLogger().log(Level.FINEST, "stDeleteTagFiles DELETE "
                        + "// Total execution time: {0}ms",
                        new Object[]{endTime - startTime});

                if (result < 0) {
                    Jamuz.getLogger().log(Level.SEVERE, "stDeleteTagFiles, "
                            + "idFile={0}, result={1}",
                            new Object[]{idFile, result});
                }

                return true;
            } catch (SQLException ex) {
                Popup.error("deleteTagFiles()", ex);
                return false;
            }
        }
    }

}
