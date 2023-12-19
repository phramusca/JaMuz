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
import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import jamuz.remote.ClientInfo;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoClient {

    private final DbConn dbConn;
    private final DaoDevice daoDevice;
    private final DaoStatSource daoStatSource;
    private final DaoClientWrite daoClientWrite;

    /**
     *
     * @param dbConn
     * @param daoDevice
     * @param daoStatSource
     */
    public DaoClient(DbConn dbConn, DaoDevice daoDevice, DaoStatSource daoStatSource) {
        this.dbConn = dbConn;
        this.daoDevice = daoDevice;
        this.daoStatSource = daoStatSource;
        daoClientWrite = new DaoClientWrite(dbConn, daoDevice, daoStatSource);
    }

    /**
     * This is to reach writing operations (insert, update, delete) on the
     * client table
     *
     * @return
     */
    public DaoClientWrite lock() {
        return daoClientWrite;
    }

    /**
     *
     * @param login
     * @return
     */
    public ClientInfo get(String login) {
        LinkedHashMap<Integer, ClientInfo> clients = new LinkedHashMap<>();
        get(clients, login);
        return clients.values().iterator().next();
    }

    /**
     * Get list of clients
     *
     * @param clients
     * @return
     */
    public boolean get(LinkedHashMap<Integer, ClientInfo> clients) {
        return get(clients, "");
    }

    /**
     * Get list of clients
     *
     * @param clients
     * @param login
     * @return
     */
    private boolean get(LinkedHashMap<Integer, ClientInfo> clients, String login) {
        ResultSet rs = null;
        try {
            String sql = """
                SELECT idClient, login, pwd, C.name AS clientName, enabled ,
                D.idDevice AS deviceId, source, destination, idPlaylist, D.name AS deviceName,
                S.idStatSource, S.name AS sourceName, S.idStatement, S.location, S.rootPath, S.idDevice, S.selected, login AS machineName , S.lastMergeDate
                FROM client C
                JOIN device D ON D.idDevice=C.idDevice
                JOIN statSource S ON S.idStatSource=C.idStatSource""";
            PreparedStatement stSelectClients = dbConn.connection.prepareStatement(sql + (login.isBlank() ? login : " WHERE login=? ")); // NOI18N
            if (!login.isBlank()) {
                stSelectClients.setString(1, login);
            }
            rs = stSelectClients.executeQuery();
            while (rs.next()) {
                int idClient = rs.getInt("idClient"); // NOI18N
                Device device = daoDevice.get(rs, login, true);
                StatSource statSource = daoStatSource.get(rs, true);
                clients.put(idClient,
                        new ClientInfo(idClient,
                                dbConn.getStringValue(rs, "login"),
                                dbConn.getStringValue(rs, "clientName"),
                                dbConn.getStringValue(rs, "pwd"),
                                device, statSource,
                                rs.getBoolean("enabled")));
            }
            return true;
        } catch (SQLException ex) {
            Popup.error("getClients", ex); // NOI18N
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
