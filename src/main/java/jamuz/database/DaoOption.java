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
import jamuz.Option;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author raph
 */
public class DaoOption {

    private final DbConn dbConn;
    private final DaoOptionWrite daoOptionWrite;

    /**
     *
     * @param dbConn
     */
    public DaoOption(DbConn dbConn) {
        this.dbConn = dbConn;
        this.daoOptionWrite = new DaoOptionWrite(dbConn);

    }

    /**
     * This is to reach writing operations (insert, update, delete)
     *
     * @return
     */
    public DaoOptionWrite lock() {
        return daoOptionWrite;
    }

    /**
     * Get options for given machine
     *
     * @param myOptions
     * @param machineName
     * @return
     */
    public boolean get(ArrayList<Option> myOptions, String machineName) {
        ResultSet rs = null;
        try {
            PreparedStatement stSelectOptions = dbConn.connection.prepareStatement(
                    "SELECT O.idMachine, OT.name, O.value, O.idOptionType, OT.type "
                    + "FROM option O, optiontype OT, machine M " // NOI18N
                    + "WHERE O.idMachine=M.idMachine "
                    + "AND O.idOptionType=OT.idOptionType "
                    + "AND M.name=?");
            stSelectOptions.setString(1, machineName);
            rs = stSelectOptions.executeQuery();
            while (rs.next()) {
                myOptions.add(new Option(
                        dbConn.getStringValue(rs, "name"),
                        dbConn.getStringValue(rs, "value"),
                        rs.getInt("idMachine"),
                        rs.getInt("idOptionType"),
                        dbConn.getStringValue(rs, "type"))); // NOI18N
            }

            if (myOptions.size() <= 0) {
                Popup.warning(Inter.get("Error.NoOption") + " \"" + machineName + "\"."); // NOI18N //NOI18N
                return false;
            }

            return true;
        } catch (SQLException ex) {
            Popup.error("getOptions(\"" + machineName + "\")", ex); // NOI18N
            return false;
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

}
