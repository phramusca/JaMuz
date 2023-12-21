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
public class DaoTag {

    private final DbConn dbConn;
    private final DaoTagWrite daoTagWrite;

    /**
     *
     * @param dbConn
     */
    public DaoTag(DbConn dbConn) {
        this.dbConn = dbConn;
        this.daoTagWrite = new DaoTagWrite(dbConn);
    }

    /**
     * This is to reach writing operations (insert, update, delete)
     *
     * @return
     */
    public DaoTagWrite lock() {
        return daoTagWrite;
    }

    /**
     * Get list of tags.
     *
     * @return ArrayList of tags.
     */
    public ArrayList<String> get() {
        ArrayList<String> tags = new ArrayList<>();

        try (PreparedStatement stSelectTags = dbConn.connection.prepareStatement("SELECT id, value FROM tag ORDER BY value"); ResultSet rs = stSelectTags.executeQuery()) {
            while (rs.next()) {
                tags.add(dbConn.getStringValue(rs, "value"));
            }
        } catch (SQLException ex) {
            Popup.error("getTags()", ex); // NOI18N
        }

        return tags;
    }

}
