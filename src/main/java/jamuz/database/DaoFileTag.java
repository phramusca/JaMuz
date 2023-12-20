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
package jamuz.database;

import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoFileTag {

    private final DbConn dbConn;
    private final DaoTag daoTag;
    private final DaoFileTagWrite daoFileTagWrite;

    /**
     *
     * @param dbConn
     * @param daoTag
     * @param daoFile
     */
    public DaoFileTag(DbConn dbConn, DaoTag daoTag, DaoFile daoFile) {
        this.dbConn = dbConn;
        this.daoTag = daoTag;
        this.daoFileTagWrite = new DaoFileTagWrite(dbConn, daoTag, daoFile);
    }

    /**
     * This is to reach writing operations (insert, update, delete) on the tagFile
     * table
     *
     * @return
     */
    public DaoFileTagWrite lock() {
        return daoFileTagWrite;
    }

    /**
     *
     * @param tags
     * @param idFile
     * @return
     */
    public boolean get(ArrayList<String> tags, int idFile) {
        try {
            PreparedStatement stSelectTags = dbConn.connection.prepareStatement(
                    "SELECT value FROM tag T JOIN tagFile F ON T.id=F.idTag "
                    + "WHERE F.idFile=?"); // NOI18N
            stSelectTags.setInt(1, idFile);
            ResultSet rs = stSelectTags.executeQuery();
            while (rs.next()) {
                tags.add(dbConn.getStringValue(rs, "value"));
            }
            return true;
        } catch (SQLException ex) {
            Popup.error("getTags(" + idFile + ")", ex); // NOI18N
            return false;
        }
    }

}
