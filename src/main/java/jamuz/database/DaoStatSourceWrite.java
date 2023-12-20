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
import jamuz.process.merge.StatSource;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 *
 * @author raph
 */
public class DaoStatSourceWrite {

    private final DbConn dbConn;

    /**
     *
     * @param dbConn
     */
    public DaoStatSourceWrite(DbConn dbConn) {
        this.dbConn = dbConn;
    }

    /**
     * Sets a Stat Source
     *
     * @param statSource
     * @return
     */
    public boolean insertOrUpdate(StatSource statSource) {
        synchronized (dbConn) {
            try {
                if (statSource.getId() > -1) {
                    try (PreparedStatement stUpdateStatSource = dbConn.connection.prepareStatement(
                            "UPDATE statsource SET location=?, rootPath=?, name=?, idStatement=?, "
                            + "idDevice=?, selected=? WHERE idStatSource=?")) {
                        stUpdateStatSource.setString(1, statSource.getSource().getLocation());
                        stUpdateStatSource.setString(2, statSource.getSource().getRootPath());
                        stUpdateStatSource.setString(3, statSource.getSource().getName());
                        stUpdateStatSource.setInt(4, statSource.getIdStatement());
                        if (statSource.getIdDevice() > 0) {
                            stUpdateStatSource.setInt(5, statSource.getIdDevice());
                        } else {
                            stUpdateStatSource.setNull(5, java.sql.Types.INTEGER);
                        }
                        stUpdateStatSource.setBoolean(6, statSource.isIsSelected());
                        stUpdateStatSource.setInt(7, statSource.getId());

                        int nbRowsAffected = stUpdateStatSource.executeUpdate();
                        if (nbRowsAffected > 0) {
                            return true;
                        } else {
                            Jamuz.getLogger().log(Level.SEVERE, "stUpdateStatSource, myStatSource={0} # row(s) affected: +{1}",
                                    new Object[]{statSource.toString(), nbRowsAffected});
                            return false;
                        }
                    }
                } else {
                    try (PreparedStatement stInsertStatSource = dbConn.connection.prepareStatement(
                            "INSERT INTO statsource (location, idStatement, rootPath, idMachine, name, idDevice, selected) "
                            + "VALUES (?, ?, ?, (SELECT idMachine FROM machine WHERE name=?), ?, ?, ?)")) {
                        stInsertStatSource.setString(1, statSource.getSource().getLocation());
                        stInsertStatSource.setInt(2, statSource.getIdStatement());
                        stInsertStatSource.setString(3, statSource.getSource().getRootPath());
                        stInsertStatSource.setString(4, statSource.getMachineName());
                        stInsertStatSource.setString(5, statSource.getSource().getName());
                        if (statSource.getIdDevice() > 0) {
                            stInsertStatSource.setInt(6, statSource.getIdDevice());
                        } else {
                            stInsertStatSource.setNull(6, java.sql.Types.INTEGER);
                        }
                        stInsertStatSource.setBoolean(7, statSource.isIsSelected());

                        int nbRowsAffected = stInsertStatSource.executeUpdate();
                        if (nbRowsAffected > 0) {
                            return true;
                        } else {
                            Jamuz.getLogger().log(Level.SEVERE, "stInsertStatSource, myStatSource={0} # row(s) affected: +{1}",
                                    new Object[]{statSource.toString(), nbRowsAffected});
                            return false;
                        }
                    }
                }
            } catch (SQLException ex) {
                Popup.error("setStatSource(" + statSource.toString() + ")", ex);
                return false;
            }
        }
    }

    /**
     * Updates the last merge date for a Stat Source
     *
     * @param idStatSource
     * @return The updated last merge date as a string
     */
    public String updateLastMergeDate(int idStatSource) {
        synchronized (dbConn) {
            try {
                int nbRowsAffected = updateLastMergeDateInDatabase(idStatSource);
                if (nbRowsAffected > 0) {
                    return getLastMergeDateFromDatabase(idStatSource);
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "stUpdateStatSourceLastMergeDate, # row(s) affected: +{0}",
                            new Object[]{nbRowsAffected});
                }
            } catch (SQLException ex) {
                Popup.error("updateLastMergeDate(" + idStatSource + ")", ex);
            }
            return "1970-01-01 00:00:00";
        }
    }

    private int updateLastMergeDateInDatabase(int idStatSource) throws SQLException {
        synchronized (dbConn) {
            try (PreparedStatement stUpdateStatSourceLastMergeDate = dbConn.connection.prepareStatement(
                    "UPDATE statsource SET lastMergeDate=datetime('now') WHERE idStatSource=?")) {
                stUpdateStatSourceLastMergeDate.setInt(1, idStatSource);
                return stUpdateStatSourceLastMergeDate.executeUpdate();
            }
        }
    }

    //TODO: Move to DaoStatSource
    private String getLastMergeDateFromDatabase(int idStatSource) throws SQLException {
        synchronized (dbConn) {
            try (PreparedStatement stGetLastMergeDate = dbConn.connection.prepareStatement(
                    "SELECT lastMergeDate FROM statsource WHERE idStatSource=?")) {
                stGetLastMergeDate.setInt(1, idStatSource);
                ResultSet rs = stGetLastMergeDate.executeQuery();
                return dbConn.getStringValue(rs, "lastMergeDate", "1970-01-01 00:00:00");
            }
        }
    }

    /**
     * Deletes a Stat Source
     *
     * @param id
     * @return true if the deletion is successful, false otherwise
     */
    public boolean delete(int id) {
        synchronized (dbConn) {
            try (PreparedStatement stDeleteStatSource = dbConn.connection.prepareStatement(
                    "DELETE FROM statsource WHERE idStatSource=?")) {

                stDeleteStatSource.setInt(1, id);
                int nbRowsAffected = stDeleteStatSource.executeUpdate();

                return nbRowsAffected > 0;
            } catch (SQLException ex) {
                Popup.error("deleteStatSource(" + id + ")", ex);
                return false;
            }
        }
    }

}
