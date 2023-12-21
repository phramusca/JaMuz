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

import jamuz.process.merge.StatSource;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoStatSource {

    private final DbConn dbConn;
    private final DaoStatSourceWrite daoStatSourceWrite;

    /**
     *
     * @param dbConn
     */
    public DaoStatSource(DbConn dbConn) {
        this.dbConn = dbConn;
        this.daoStatSourceWrite = new DaoStatSourceWrite(dbConn);

    }

    /**
     * This is to reach writing operations (insert, update, delete)
     *
     * @return
     */
    public DaoStatSourceWrite lock() {
        return daoStatSourceWrite;
    }

    /**
     *
     * @param login
     * @return
     */
    public StatSource get(String login) {
        LinkedHashMap<Integer, StatSource> statSources = new LinkedHashMap<>();
        DaoStatSource.this.get(statSources, login, true);
        return statSources.values().iterator().next();
    }

    /**
     * Return list of database sources for the given machine.
     *
     * @param statSources LinkedHashMap to store StatSource objects.
     * @param hostname Machine hostname.
     * @param hidden Boolean indicating whether to include hidden sources.
     * @return True if the operation is successful, false otherwise.
     */
    public boolean get(LinkedHashMap<Integer, StatSource> statSources, String hostname, boolean hidden) {
        try (PreparedStatement stSelectStatSources = dbConn.connection.prepareStatement(
                "SELECT S.idStatSource, S.name AS sourceName, S.idStatement, S.location, S.rootPath, S.idDevice, S.selected, M.name AS machineName, S.lastMergeDate "
                + "FROM statsource S "
                + "JOIN machine M ON M.idMachine=S.idMachine "
                + "WHERE M.name=? ORDER BY S.name")) {
            stSelectStatSources.setString(1, hostname);
            try (ResultSet rs = stSelectStatSources.executeQuery()) {
                while (rs.next()) {
                    StatSource statSource = get(rs, hidden);
                    statSources.put(statSource.getId(), statSource);
                }
            }
            return true;
        } catch (SQLException ex) {
            Popup.error("getStatSources(" + hostname + ")", ex); // NOI18N
            return false;
        }
    }

    /**
     * Convert a ResultSet row to a StatSource object.
     *
     * @param rs ResultSet containing the data.
     * @param hidden Boolean indicating whether the source is hidden.
     * @return StatSource object.
     * @throws SQLException If a database access error occurs.
     */
    public StatSource get(ResultSet rs, boolean hidden) throws SQLException {
        int idStatSource = rs.getInt("idStatSource");
        int idStatement = rs.getInt("idStatement");
        String statSourceLocation = dbConn.getStringValue(rs, "location");
        String machineName = dbConn.getStringValue(rs, "machineName");
        String lastMergeDate = dbConn.getStringValue(rs, "lastMergeDate", "1970-01-01 00:00:00");
        int idDevice = rs.getInt("idDevice");
        boolean isSelected = rs.getBoolean("selected");

        return new StatSource(
                idStatSource,
                dbConn.getStringValue(rs, "sourceName"),
                idStatement,
                statSourceLocation, "", "",
                dbConn.getStringValue(rs, "rootPath"),
                machineName,
                idDevice,
                isSelected, lastMergeDate, hidden);
    }

}
