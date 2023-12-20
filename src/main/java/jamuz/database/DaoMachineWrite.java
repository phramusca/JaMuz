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
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

/**
 *
 * @author raph
 */
public class DaoMachineWrite {

    private final DbConn dbConn;

    /**
     *
     * @param dbConn
     */
    public DaoMachineWrite(DbConn dbConn) {
        this.dbConn = dbConn;
    }

    //TODO: Split get and insert
    /**
     * Check if given machine is listed or insert with default options if not
     * yet listed.
     *
     * @param hostname
     * @param description
     * @param hidden
     * @return
     */
    public boolean getOrInsert(String hostname, StringBuilder description, boolean hidden) {
        synchronized (dbConn) {
            try (PreparedStatement stSelectMachine = dbConn.connection.prepareStatement(
                    "SELECT COUNT(*), description FROM machine "
                    + "WHERE name=?")) {

                stSelectMachine.setString(1, hostname);
                try (ResultSet rs = stSelectMachine.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        description.append(dbConn.getStringValue(rs, "description"));
                        return true;
                    } else {
                        // Insert a new machine
                        try (PreparedStatement stInsertMachine = dbConn.connection.prepareStatement(
                                "INSERT INTO machine (name, hidden) VALUES (?, ?)",
                                Statement.RETURN_GENERATED_KEYS)) {

                            stInsertMachine.setString(1, hostname);
                            stInsertMachine.setBoolean(2, hidden);
                            int nbRowsAffected = stInsertMachine.executeUpdate();

                            if (nbRowsAffected == 1) {
                                try (ResultSet keys = stInsertMachine.getGeneratedKeys()) {
                                    if (keys.next()) {
                                        int idMachine = keys.getInt(1);
                                        // Insert default options
                                        try (PreparedStatement stSelectOptionType = dbConn.connection.prepareStatement(
                                                "SELECT idOptionType, name, `default` "
                                                + "FROM optiontype"); ResultSet optionTypeRs = stSelectOptionType.executeQuery()) {

                                            try (PreparedStatement stInsertOption = dbConn.connection.prepareStatement(
                                                    "INSERT INTO option ('idMachine', 'idOptionType', "
                                                    + "'value') VALUES (?, ?, ?)")) {

                                                while (optionTypeRs.next()) {
                                                    stInsertOption.setInt(1, idMachine);
                                                    stInsertOption.setInt(2, optionTypeRs.getInt("idOptionType"));
                                                    stInsertOption.setString(3, dbConn.getStringValue(optionTypeRs, "default"));
                                                    nbRowsAffected = stInsertOption.executeUpdate();

                                                    if (nbRowsAffected != 1) {
                                                        Jamuz.getLogger().log(Level.SEVERE, "stInsertOption, "
                                                                + "idMachine={0}, idOptionType={1}, default=\"{2}\" "
                                                                + "# row(s) affected: +{1}",
                                                                new Object[]{idMachine, optionTypeRs.getInt("idOptionType"),
                                                                    dbConn.getStringValue(optionTypeRs, "default"), nbRowsAffected});
                                                        return false;
                                                    }
                                                }
                                                return true;
                                            }
                                        }
                                    } else {
                                        Jamuz.getLogger().log(Level.SEVERE, "Failed to retrieve generated keys");
                                        return false;
                                    }
                                }
                            } else {
                                Jamuz.getLogger().log(Level.SEVERE, "stInsertMachine, "
                                        + "hostname=\"{0}\" # row(s) affected: +{1}",
                                        new Object[]{hostname, nbRowsAffected});
                                return false;
                            }
                        }
                    }
                }
            } catch (SQLException ex) {
                Popup.error("isMachine(" + hostname + ")", ex);
                return false;
            }
        }
    }

    /**
     * Updates machine in machine table
     *
     * @param idMachine
     * @param description
     * @return
     */
    public boolean update(int idMachine, String description) {
        synchronized (dbConn) {
            try (PreparedStatement stUpdateMachine = dbConn.connection.prepareStatement(
                    "UPDATE machine SET description=? WHERE idMachine=?")) {

                stUpdateMachine.setString(1, description);
                stUpdateMachine.setInt(2, idMachine);
                int nbRowsAffected = stUpdateMachine.executeUpdate();
                if (nbRowsAffected == 1) {
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "stUpdateMachine, idMachine={0}, "
                            + "description={1} # row(s) affected: +{2}",
                            new Object[]{idMachine, description, nbRowsAffected});
                    return false;
                }
            } catch (SQLException ex) {
                Popup.error("updateMachine(" + idMachine + ", \"" + description + "\")", ex);
                return false;
            }
        }
    }

    /**
     * Deletes a machine
     *
     * @param machineName
     * @return
     */
    public boolean delete(String machineName) {
        synchronized (dbConn) {
            try (PreparedStatement stDeleteMachine = dbConn.connection.prepareStatement("DELETE FROM machine WHERE name=?")) {

                stDeleteMachine.setString(1, machineName);
                int nbRowsAffected = stDeleteMachine.executeUpdate();
                if (nbRowsAffected > 0) {
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "stDeleteMachine(\"{0}\"). # row(s) affected: {1}",
                            new Object[]{machineName, nbRowsAffected});
                    return false;
                }
            } catch (SQLException ex) {
                Popup.error("deleteMachine(" + machineName + ")", ex);
                return false;
            }
        }
    }

}
