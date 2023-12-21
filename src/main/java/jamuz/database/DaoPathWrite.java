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

import jamuz.Jamuz;
import jamuz.process.check.FolderInfo;
import jamuz.utils.DateTime;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import org.apache.commons.io.FilenameUtils;

//FIXME Z Only log in Dao classes and throw exceptions (need to evaluate the consequences first) and update tests to check (no) exception is thrown
/**
 *
 * @author raph
 */
public class DaoPathWrite {

    private final DbConn dbConn;

    /**
     *
     * @param dbConn
     */
    public DaoPathWrite(DbConn dbConn) {
        this.dbConn = dbConn;
    }

    /**
     * Insert a path in the database.
     *
     * @param relativePath the relative path to be inserted
     * @param modifDate the modification date
     * @param checkedFlag the checked flag
     * @param mbId the mbId
     * @param key an array to store the generated key
     * @return true if the insertion is successful, false otherwise
     */
    public boolean insert(String relativePath, Date modifDate, FolderInfo.CheckedFlag checkedFlag, String mbId, int[] key) {
        synchronized (dbConn) {
            try (PreparedStatement stInsertPath = dbConn.getConnection().prepareStatement(
                    "INSERT INTO path (strPath, modifDate, checked, mbId) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {

                relativePath = FilenameUtils.separatorsToUnix(relativePath);
                stInsertPath.setString(1, relativePath);
                stInsertPath.setString(2, DateTime.formatUTCtoSqlUTC(modifDate));
                stInsertPath.setInt(3, checkedFlag.getValue());
                stInsertPath.setString(4, mbId);

                int nbRowsAffected = stInsertPath.executeUpdate();
                if (nbRowsAffected == 1) {
                    try (ResultSet keys = stInsertPath.getGeneratedKeys()) {
                        if (keys.next()) {
                            key[0] = keys.getInt(1);
                            return true;
                        }
                    }
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "stInsertPath, relativePath=\"{0}\" # row(s) affected: +{1}",
                            new Object[]{relativePath, nbRowsAffected});
                }

            } catch (SQLException ex) {
                Popup.error("insertPath(" + relativePath + ", " + modifDate.toString() + ")", ex);
            }

            return false;
        }
    }

    /**
     * Update a path in the database.
     *
     * @param idPath the ID of the path to be updated
     * @param modifDate the modification date
     * @param checkedFlag the checked flag
     * @param path the path to be updated
     * @param mbId the mbId
     * @return true if the update is successful, false otherwise
     */
    public boolean update(int idPath, Date modifDate, FolderInfo.CheckedFlag checkedFlag, String path, String mbId) {
        synchronized (dbConn) {
            try (PreparedStatement stUpdatePath = dbConn.connection.prepareStatement(
                    "UPDATE path SET modifDate=?, checked=?, strPath=?, mbId=? WHERE idPath=?")) {

                stUpdatePath.setString(1, DateTime.formatUTCtoSqlUTC(modifDate));
                stUpdatePath.setInt(2, checkedFlag.getValue());
                path = FilenameUtils.separatorsToUnix(path);
                stUpdatePath.setString(3, path);
                stUpdatePath.setString(4, mbId);
                stUpdatePath.setInt(5, idPath);

                int nbRowsAffected = stUpdatePath.executeUpdate();
                if (nbRowsAffected == 1) {
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "stUpdatePath, idPath={0} # row(s) affected: +{1}",
                            new Object[]{idPath, nbRowsAffected});
                    return false;
                }

            } catch (SQLException ex) {
                Popup.error("updatePath(" + idPath + ", " + modifDate.toString() + ")", ex);
            }

            return false;
        }
    }

    /**
     * Sets a path as checked in the database.
     *
     * @param idPath the ID of the path to be updated
     * @param checkedFlag the checked flag
     * @return true if the update is successful, false otherwise
     */
    public boolean updateCheckedFlag(int idPath, FolderInfo.CheckedFlag checkedFlag) {
        synchronized (dbConn) {
            try (PreparedStatement stUpdateCheckedFlag = dbConn.connection
                    .prepareStatement("UPDATE path SET checked=? WHERE idPath=?")) {

                stUpdateCheckedFlag.setInt(1, checkedFlag.getValue());
                stUpdateCheckedFlag.setInt(2, idPath);

                int nbRowsAffected = stUpdateCheckedFlag.executeUpdate();
                if (nbRowsAffected > 0) {
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE,
                            "stUpdateCheckedFlag, idPath={0}, checkedFlag={1} # row(s) affected: +{2}",
                            new Object[]{idPath, checkedFlag, nbRowsAffected});
                    return false;
                }

            } catch (SQLException ex) {
                Popup.error("setCheckedFlag(" + idPath + "," + checkedFlag + ")", ex);
            }

            return false;
        }
    }

    /**
     * Resets the check flag to UNCHECKED on the path table for a given checked
     * flag.
     *
     * @param checkedFlag the checked flag
     * @return true if the update is successful, false otherwise
     */
    public boolean updateCheckedFlagReset(FolderInfo.CheckedFlag checkedFlag) {
        synchronized (dbConn) {
            try (PreparedStatement stUpdateCheckedFlagReset = dbConn.connection.prepareStatement(
                    "UPDATE path SET checked=0 WHERE checked=?")) {

                stUpdateCheckedFlagReset.setInt(1, checkedFlag.getValue());
                stUpdateCheckedFlagReset.executeUpdate();
                // We can have no rows affected if the library is empty, so not checking it
                return true;

            } catch (SQLException ex) {
                Popup.error("setCheckedFlagReset()", ex);
            }

            return false;
        }
    }

    /**
     * Updates the copyRight in the path table.
     *
     * @param idPath the ID of the path to be updated
     * @param copyRight the copyRight value
     * @return true if the update is successful, false otherwise
     */
    public boolean updateCopyRight(int idPath, int copyRight) {
        synchronized (dbConn) {
            try (PreparedStatement stUpdateCopyRight = dbConn.connection.prepareStatement(
                    "UPDATE path SET copyRight=? WHERE idPath=?")) {

                stUpdateCopyRight.setInt(1, copyRight);
                stUpdateCopyRight.setInt(2, idPath);

                int nbRowsAffected = stUpdateCopyRight.executeUpdate();
                if (nbRowsAffected == 1) {
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "stUpdateCopyRight, idPath={0}, copyRight={1} # row(s) affected: +{2}",
                            new Object[]{idPath, copyRight, nbRowsAffected});
                    return false;
                }

            } catch (SQLException ex) {
                Popup.error("updateCopyRight(" + idPath + ", " + copyRight + ")", ex);
            }

            return false;
        }
    }

    /**
     * Deletes a path from the database.
     *
     * @param idPath the ID of the path to be deleted
     * @return true if the deletion is successful, false otherwise
     */
    public boolean delete(int idPath) {
        synchronized (dbConn) {
            try (PreparedStatement stDeletedPath = dbConn.connection.prepareStatement(
                    "DELETE FROM path WHERE idPath=?")) {

                stDeletedPath.setInt(1, idPath);

                int nbRowsAffected = stDeletedPath.executeUpdate();
                if (nbRowsAffected == 1) {
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "stDeletedFiles, idPath={0} # row(s) affected: +{1}",
                            new Object[]{idPath, nbRowsAffected});
                    return false;
                }

            } catch (SQLException ex) {
                Popup.error("deletePath(" + idPath + ")", ex);
            }

            return false;
        }
    }

}
