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

import jamuz.utils.DateTime;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoSchema {

    private final DbConn dbConn;
    private final DaoSchemaWrite daoSchemaWrite;

    /**
     *
     * @param dbConn
     */
    public DaoSchema(DbConn dbConn) {
        this.dbConn = dbConn;
        this.daoSchemaWrite = new DaoSchemaWrite(dbConn, this);

    }

    /**
     * This is to reach writing operations (insert, update, delete)
     *
     * @return
     */
    public DaoSchemaWrite lock() {
        return daoSchemaWrite;
    }

    boolean getVersionHistory(ArrayList<DbVersion> versions) {
        Pair<Boolean, Boolean> tableVersionHistoryStatus = existsTableVersionHistory();

        if (!tableVersionHistoryStatus.getKey()) {
            // Method did not succeed.
            return false;
        }

        if (!tableVersionHistoryStatus.getValue()) {
            // Table does not exist.
            versions.add(new DbVersion(0, new Date(0), new Date(0)));
            return true;
        }

        try (PreparedStatement stSelectVersionHistory = dbConn.connection.prepareStatement(
                "SELECT version, upgradeStart, upgradeEnd FROM versionHistory ORDER BY version DESC"); ResultSet rs = stSelectVersionHistory.executeQuery()) {

            while (rs.next()) {
                int version = rs.getInt("version");
                Date upgradeStart = DateTime.parseSqlUtc(rs.getString("upgradeStart"));
                Date upgradeEnd = DateTime.parseSqlUtc(rs.getString("upgradeEnd"));
                versions.add(new DbVersion(version, upgradeStart, upgradeEnd));
            }

            return true;
        } catch (SQLException ex) {
            Popup.error("getVersionHistory", ex);
            return false;
        }
    }

    private Pair<Boolean, Boolean> existsTableVersionHistory() {
        try (Statement st = dbConn.connection.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='versionHistory';")) {

            return new ImmutablePair<>(true, rs.getInt(1) > 0);

        } catch (SQLException ex) {
            Popup.error("existsTableVersionHistory()", ex);
            return new ImmutablePair<>(false, false);
        }
    }

}
