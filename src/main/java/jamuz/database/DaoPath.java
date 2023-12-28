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

import jamuz.process.check.FolderInfo;
import jamuz.utils.DateTime;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author raph
 */
public class DaoPath {

    private final DbConn dbConn;
    private final DaoPathWrite daoPathWrite;

    /**
     *
     * @param dbConn
     */
    public DaoPath(DbConn dbConn) {
        this.dbConn = dbConn;
        daoPathWrite = new DaoPathWrite(dbConn);
    }

    /**
     * This is to reach writing operations (insert, update, delete)
     *
     * @return
     */
    public DaoPathWrite lock() {
        return daoPathWrite;
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
        return get(folders, "WHERE checked="
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
        return get(folders, "WHERE idPath=" + idPath); // NOI18N
    }

    /**
     * Return given folder
     *
     * @param idPath
     * @return
     */
    public FolderInfo get(int idPath) {
        ConcurrentHashMap<String, FolderInfo> folders = new ConcurrentHashMap<>();
        if (get(folders, idPath)) { // NOI18N
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
        return get(folders, "");
    }

    private boolean get(ConcurrentHashMap<String, FolderInfo> folders, String sqlWhere) {
        folders.clear();
        String sql = "SELECT idPath, strPath, modifDate, checked FROM path " + sqlWhere; // NOI18N
        try (Statement st = dbConn.connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            String path;
            while (rs.next()) {
                Date dbModifDate = DateTime.parseSqlUtc(dbConn.getStringValue(rs, "modifDate")); // NOI18N
                path = FilenameUtils.separatorsToSystem(dbConn.getStringValue(rs, "strPath")); // NOI18N
                folders.put(path, new FolderInfo(rs.getInt("idPath"), path, dbModifDate,
                        FolderInfo.CheckedFlag.values()[rs.getInt("checked")])); // NOI18N
            }
            return true;
        } catch (SQLException ex) {
            Popup.error("getFolderInfoList(" + sqlWhere + ")", ex); // NOI18N
            return false;
        }
    }

    /**
     * Checks if path exists in the database
     *
     * @param path
     * @return
     */
    public int getIdPath(String path) {
        path = FilenameUtils.separatorsToUnix(path);

        try (PreparedStatement stSelectPath = dbConn.connection.prepareStatement(
                "SELECT idPath FROM path WHERE strPath=? ORDER BY idPath")) {
            stSelectPath.setString(1, path);

            try (ResultSet rs = stSelectPath.executeQuery()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        } catch (SQLException ex) {
            Popup.error("isPathExists(" + path + ")", ex); // NOI18N
            return -1;
        }
    }

}