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

import jamuz.Jamuz;
import jamuz.utils.Popup;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
     * Get list of tags
     *
     * @return
     */
    public ArrayList<String> get() {
        Statement st = null;
        ResultSet rs = null;
        ArrayList<String> tags = new ArrayList<>();
        try {
            st = dbConn.connection.createStatement();
            rs = st.executeQuery("SELECT id, value FROM tag ORDER BY value");
            while (rs.next()) {
                tags.add(dbConn.getStringValue(rs, "value"));
            }
        } catch (SQLException ex) {
            Popup.error("getTags()", ex); // NOI18N
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
        return tags;
    }

}
