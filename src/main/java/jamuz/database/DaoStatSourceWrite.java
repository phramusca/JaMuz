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
        try {
            if (statSource.getId() > -1) {
                PreparedStatement stUpdateStatSource = dbConn.connection
                        .prepareStatement("UPDATE statsource SET location=?, " // NOI18N
                                + "rootPath=?, " // NOI18N
                                + "name=?, " // NOI18N
                                + "idStatement=?, " // NOI18N
                                + "idDevice=?, selected=? " // NOI18N
                                + "WHERE idStatSource=?"); // NOI18N

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
                            new Object[]{statSource.toString(), nbRowsAffected}); // NOI18N
                    return false;
                }
            } else {
                PreparedStatement stInsertStatSource = dbConn.connection.prepareStatement("INSERT INTO statsource "
                        + "(location, idStatement, rootPath, idMachine, name, idDevice, selected) " // NOI18N
                        + "VALUES (?, ?, ?, (SELECT idMachine FROM machine WHERE name=?), ?, ?, ?)"); // NOI18N

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
                            new Object[]{statSource.toString(), nbRowsAffected}); // NOI18N
                    return false;
                }
            }
        } catch (SQLException ex) {
            Popup.error("setStatSource(" + statSource.toString() + ")", ex); // NOI18N
            return false;
        }
    }

    /**
     *
     * @param idStatSource
     * @return
     */
    public String updateLastMergeDate(int idStatSource) {
        ResultSet rs = null;
        try {
            PreparedStatement stUpdateStatSourceLastMergeDate = dbConn.connection.prepareStatement("UPDATE statsource "
                    + "SET lastMergeDate=datetime('now') WHERE idStatSource=?");
            stUpdateStatSourceLastMergeDate.setInt(1, idStatSource);
            int nbRowsAffected = stUpdateStatSourceLastMergeDate.executeUpdate();
            if (nbRowsAffected > 0) {
                PreparedStatement stGetLastMergeDate = dbConn.connection
                        .prepareStatement("SELECT lastMergeDate FROM statsource  WHERE idStatSource=?");
                stGetLastMergeDate.setInt(1, idStatSource);
                rs = stGetLastMergeDate.executeQuery();
                String temp = dbConn.getStringValue(rs, "lastMergeDate", "1970-01-01 00:00:00");
                return temp;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "stUpdateStatSourceLastMergeDate, # row(s) affected: +{0}",
                        new Object[]{nbRowsAffected}); // NOI18N
            }
        } catch (SQLException ex) {
            Popup.error("updateLastMergeDate(" + idStatSource + ")", ex); // NOI18N
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Jamuz.getLogger().warning("Failed to close ResultSet");
            }
        }
        return "1970-01-01 00:00:00";
    }

    /**
     * Deletes a Stat Source
     *
     * @param id
     * @return
     */
    public boolean delete(int id) {
        try {
            PreparedStatement stDeleteStatSource = dbConn.connection.prepareStatement(
                    "DELETE FROM statsource WHERE idStatSource=?"); // NOI18N
            stDeleteStatSource.setInt(1, id);
            int nbRowsAffected = stDeleteStatSource.executeUpdate();
            if (nbRowsAffected > 0) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "stDeleteStatSource, id={0} # row(s) affected: +{1}",
                        new Object[]{id, nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("deleteStatSource(" + id + ")", ex); // NOI18N
            return false;
        }
    }
}
