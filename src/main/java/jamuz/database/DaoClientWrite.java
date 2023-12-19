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
import jamuz.remote.ClientInfo;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 *
 * @author raph
 */
public class DaoClientWrite {

    private final DbConn dbConn;
    private final DaoDevice daoDevice;
    private final DaoStatSource daoStatSource;
    private PreparedStatement stUpdateClient;
    private PreparedStatement stInsertClient;

    /**
     *
     * @param dbConn
     * @param daoDevice
     * @param daoStatSource
     */
    public DaoClientWrite(DbConn dbConn, DaoDevice daoDevice, DaoStatSource daoStatSource) {
        this.dbConn = dbConn;
        this.daoDevice = daoDevice;
        this.daoStatSource = daoStatSource;
    }

    /**
     * Inserts or update a device
     *
     * @param clientInfo
     * @return
     */
    public boolean insertOrUpdate(ClientInfo clientInfo) {
        synchronized (dbConn) {
            try {
                if (clientInfo.getId() > -1) {

                    stUpdateClient = dbConn.connection.prepareStatement(
                            "UPDATE client SET login=?, pwd=?, name=?, enabled=? "
                            + "WHERE idClient=?"); // NOI18N
                    stUpdateClient.setString(1, clientInfo.getLogin());
                    stUpdateClient.setString(2, clientInfo.getPwd());
                    stUpdateClient.setString(3, clientInfo.getName());
                    stUpdateClient.setBoolean(4, clientInfo.isEnabled());
                    stUpdateClient.setInt(5, clientInfo.getId());

                    int nbRowsAffected = stUpdateClient.executeUpdate();
                    if (nbRowsAffected > 0) {
                        daoDevice.lock().insertOrUpdate(clientInfo.getDevice());
                        daoStatSource.insertOrUpdate(clientInfo.getStatSource());
                        return true;
                    } else {
                        Jamuz.getLogger().log(Level.SEVERE,
                                "stUpdateClient, myStatSource={0} # row(s) affected: +{1}",
                                new Object[]{clientInfo.toString(), nbRowsAffected}); // NOI18N
                        return false;
                    }
                } else {
                    stInsertClient = dbConn.connection.prepareStatement(
                            "INSERT INTO client "
                            + "(login, pwd, name, "
                            + "idDevice, idStatSource, enabled) "
                            + "VALUES (?, ?, ?, ?, ?, ?)"); // NOI18N
                    stInsertClient.setString(1, clientInfo.getLogin());
                    stInsertClient.setString(2, clientInfo.getPwd());
                    stInsertClient.setString(3, clientInfo.getName() + "-" + clientInfo.getLogin().substring(0, 5));
                    if (clientInfo.getDevice() != null) {
                        stInsertClient.setInt(4, clientInfo.getDevice().getId());
                    } else {
                        stInsertClient.setNull(4, java.sql.Types.INTEGER);
                    }
                    if (clientInfo.getStatSource() != null) {
                        stInsertClient.setInt(5, clientInfo.getStatSource().getId());
                    } else {
                        stInsertClient.setNull(5, java.sql.Types.INTEGER);
                    }
                    stInsertClient.setBoolean(6, clientInfo.isEnabled());

                    int nbRowsAffected = stInsertClient.executeUpdate();
                    if (nbRowsAffected > 0) {
                        return true;
                    } else {
                        Jamuz.getLogger().log(Level.SEVERE,
                                "stInsertClient, myStatSource={0} # row(s) affected: +{1}",
                                new Object[]{clientInfo.toString(), nbRowsAffected}); // NOI18N
                        return false;
                    }
                }
            } catch (SQLException ex) {
                Popup.error("setClientInfo(" + clientInfo.toString() + ")", ex); // NOI18N
                return false;
            }
        }
    }
}
