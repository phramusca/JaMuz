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
package jamuz;

import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import jamuz.remote.ClientInfo;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.logging.Level;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoClient {

	private final DbConn dbConn;
	private final DaoDevice daoDevice;
	private final DaoStatSource daoStatSource;

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
	
	/**
	 * Inserts or update a device
	 *
	 * @param clientInfo
	 * @return
	 */
	public synchronized boolean update(ClientInfo clientInfo) {
		try {
			if (clientInfo.getId() > -1) {

				PreparedStatement stUpdateClient = dbConn.connection.prepareStatement(
						"UPDATE client SET login=?, pwd=?, name=?, enabled=? "
						+ "WHERE idClient=?"); // NOI18N
				stUpdateClient.setString(1, clientInfo.getLogin());
				stUpdateClient.setString(2, clientInfo.getPwd());
				stUpdateClient.setString(3, clientInfo.getName());
				stUpdateClient.setBoolean(4, clientInfo.isEnabled());
				stUpdateClient.setInt(5, clientInfo.getId());

				int nbRowsAffected = stUpdateClient.executeUpdate();
				if (nbRowsAffected > 0) {
					daoDevice.update(clientInfo.getDevice());
					daoStatSource.insertOrUpdate(clientInfo.getStatSource());
					return true;
				} else {
					Jamuz.getLogger().log(Level.SEVERE,
							"stUpdateClient, myStatSource={0} # row(s) affected: +{1}",
							new Object[]{clientInfo.toString(), nbRowsAffected}); // NOI18N
					return false;
				}
			} else {
				PreparedStatement stInsertClient = dbConn.connection.prepareStatement(
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
