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
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author raph
 */
public class DaoPath {

    private final DbConn dbConn;

    /**
     *
     * @param dbConn
     */
    public DaoPath(DbConn dbConn) {
        this.dbConn = dbConn;
    }

    /**
     * Insert a path in database
     *
     * @param relativePath
     * @param modifDate
     * @param checkedFlag
     * @param mbId
     * @param key
     * @return
     */
    public synchronized boolean insert(String relativePath, Date modifDate,
            FolderInfo.CheckedFlag checkedFlag, String mbId, int[] key) {
        try {
            // Only inserting in Linux style in database
            relativePath = FilenameUtils.separatorsToUnix(relativePath);
            PreparedStatement stInsertPath = dbConn.getConnection().prepareStatement(
                    "INSERT INTO path "
                    + "(strPath, modifDate, checked, mbId) " // NOI18N
                    + "VALUES (?, ?, ?, ?)"); // NOI18N

            stInsertPath.setString(1, relativePath);
            stInsertPath.setString(2, DateTime.formatUTCtoSqlUTC(modifDate));
            stInsertPath.setInt(3, checkedFlag.getValue());
            stInsertPath.setString(4, mbId);
            int nbRowsAffected = stInsertPath.executeUpdate();
            if (nbRowsAffected == 1) {
                ResultSet keys = stInsertPath.getGeneratedKeys();
                keys.next();
                key[0] = keys.getInt(1);
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "stInsertPath, relativePath=\"{0}\" "
                        + "# row(s) affected: +{1}",
                        new Object[]{relativePath, nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("insertPath(" + relativePath + ", " + modifDate.toString() + ")",
                    ex); // NOI18N
            return false;
        }
    }

    /**
     * Gets folders having given checked flag
     *
     * @param folders
     * @param checkedFlag
     * @return
     */
    public boolean get(ConcurrentHashMap<String, FolderInfo> folders,
            FolderInfo.CheckedFlag checkedFlag) {
        return this.get(folders, "WHERE checked="
                + checkedFlag.getValue());// NOI18N
    }

    /**
     * Return given folder for check (as a list to match check process)
     *
     * @param folders
     * @param idPath
     * @return
     */
    public boolean get(ConcurrentHashMap<String, FolderInfo> folders,
            int idPath) {
        return this.get(folders, "WHERE idPath=" + idPath); // NOI18N
    }

    /**
     * Return given folder
     *
     * @param idPath
     * @return
     */
    public FolderInfo get(int idPath) {
        ConcurrentHashMap<String, FolderInfo> folders = new ConcurrentHashMap<>();
        if (DaoPath.this.get(folders, idPath)) { // NOI18N
            return folders.elements().nextElement(); // .get(0);
        } else {
            return null;
        }
    }

    /**
     * Get folders for scan
     *
     * @param folders
     * @return
     */
    public boolean get(ConcurrentHashMap<String, FolderInfo> folders) {
        return DaoPath.this.get(folders, "");
    }

    private boolean get(ConcurrentHashMap<String, FolderInfo> folders,
            String sqlWhere) {
        folders.clear();
        String sql = "SELECT idPath, strPath, modifDate, checked "
                + "FROM path " + sqlWhere; // NOI18N
        Statement st = null;
        ResultSet rs = null;
        try {
            st = dbConn.connection.createStatement();
            rs = st.executeQuery(sql);
            String path;
            while (rs.next()) {
                Date dbModifDate = DateTime.parseSqlUtc(
                        dbConn.getStringValue(rs, "modifDate")); // NOI18N
                path = FilenameUtils.separatorsToSystem(
                        dbConn.getStringValue(rs, "strPath")); // NOI18N
                folders.put(path, new FolderInfo(rs.getInt("idPath"),
                        path, dbModifDate,
                        FolderInfo.CheckedFlag.values()[rs.getInt("checked")])); // NOI18N
            }
            return true;
        } catch (SQLException ex) {
            Popup.error("getFolderInfoList(" + sqlWhere + ")", ex); // NOI18N
            return false;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Jamuz.getLogger().warning("Failed to close ResultSet");
            }
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException ex) {
                Jamuz.getLogger().warning("Failed to close Statement");
            }
        }
    }

    /**
     * Checks if path exists in database
     *
     * @param path
     * @return
     */
    public int getIdPath(String path) {
        ResultSet rs = null;
        try {
            path = FilenameUtils.separatorsToUnix(path);

            PreparedStatement stSelectPath = dbConn.connection.prepareStatement(
                    "SELECT idPath FROM path WHERE strPath=? ORDER BY idPath"); // NOI18N
            stSelectPath.setString(1, path);
            rs = stSelectPath.executeQuery();
            if (rs.next()) { // Check if we have a result, so we can move to this one
                return rs.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            Popup.error("isPathExists(" + path + ")", ex); // NOI18N
            return -1;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Jamuz.getLogger().warning("Failed to close ResultSet");
            }

        }
    }

    /**
     * Update a path in database
     *
     * @param idPath
     * @param modifDate
     * @param checkedFlag
     * @param path
     * @param mbId
     * @return
     */
    public synchronized boolean update(int idPath, Date modifDate,
            FolderInfo.CheckedFlag checkedFlag, String path, String mbId) {
        try {
            PreparedStatement stUpdatePath = dbConn.connection.prepareStatement(
                    "UPDATE path "
                    + "SET modifDate=?, checked=?, strPath=?, mbId=? " // NOI18N
                    + "WHERE idPath=?"); // NOI18N
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
                Jamuz.getLogger().log(Level.SEVERE, "stUpdatePath, idPath={0} "
                        + "# row(s) affected: +{1}", new Object[]{idPath, nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("updatePath(" + idPath + ", " + modifDate.toString() + ")", ex); // NOI18N
            return false;
        }
    }

    /**
     * Sets a path as checked
     *
     * @param idPath
     * @param checkedFlag
     * @return
     */
    public synchronized boolean updateCheckedFlag(int idPath, FolderInfo.CheckedFlag checkedFlag) {
        try {
            PreparedStatement stUpdateCheckedFlag = dbConn.connection
                    .prepareStatement("UPDATE path set checked=? WHERE idPath=?"); // NOI18N

            stUpdateCheckedFlag.setInt(1, checkedFlag.getValue());
            stUpdateCheckedFlag.setInt(2, idPath);
            int nbRowsAffected = stUpdateCheckedFlag.executeUpdate();
            if (nbRowsAffected > 0) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE,
                        "stUpdateCheckedFlag, idPath={0}, checkedFlag={1} # row(s) affected: +{2}",
                        new Object[]{idPath, checkedFlag, nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("setCheckedFlag(" + idPath + "," + checkedFlag + ")", ex); // NOI18N
            return false;
        }
    }

    /**
     * /!\ Resets the check flag to UNCHECKED on path table for given checked
     * flag
     *
     * @param checkedFlag
     * @return
     */
    public synchronized boolean updateCheckedFlagReset(FolderInfo.CheckedFlag checkedFlag) {
        try {
            PreparedStatement stUpdateCheckedFlagReset = dbConn.connection.prepareStatement(
                    "UPDATE path SET checked=0 "
                    + "WHERE checked=?"); // NOI18N
            stUpdateCheckedFlagReset.setInt(1, checkedFlag.getValue());
            stUpdateCheckedFlagReset.executeUpdate();
            // we can have no rows affected if library is empty so not checking it
            return true;
        } catch (SQLException ex) {
            Popup.error("setCheckedFlagReset()", ex); // NOI18N
            return false;
        }
    }

    /**
     * Updates copyRight in path table
     *
     * @param idPath
     * @param copyRight
     * @return
     */
    public synchronized boolean updateCopyRight(int idPath, int copyRight) {
        try {
            PreparedStatement stUpdateCopyRight = dbConn.connection.prepareStatement(
                    "UPDATE path "
                    + "SET copyRight=? WHERE idPath=?"); // NOI18N
            stUpdateCopyRight.setInt(1, copyRight);
            stUpdateCopyRight.setInt(2, idPath);
            int nbRowsAffected = stUpdateCopyRight.executeUpdate();
            if (nbRowsAffected == 1) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "stUpdateCopyRight, idPath={0}, "
                        + "copyRight={1} # row(s) affected: +{2}",
                        new Object[]{idPath, copyRight, nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("updateCopyRight(" + idPath + ", " + copyRight + ")", ex); // NOI18N
            return false;
        }
    }
    
    /**
	 * Deletes a path.
	 *
	 * @param idPath
	 * @return
	 */
	public synchronized boolean delete(int idPath) {
		try {
			PreparedStatement stDeletedPath = dbConn.connection.prepareStatement(
					"DELETE FROM path WHERE idPath=?"); // NOI18N
			stDeletedPath.setInt(1, idPath);
			int nbRowsAffected = stDeletedPath.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stDeletedFiles, idPath={0} "
						+ "# row(s) affected: +{1}", new Object[]{idPath, nbRowsAffected}); // NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("deletePath(" + idPath + ")", ex); // NOI18N
			return false;
		}
	}
}
