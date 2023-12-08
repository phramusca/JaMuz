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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoMachine {
	private final DbConn dbConn;

	/**
	 *
	 * @param dbConn
	 */
	public DaoMachine(DbConn dbConn) {
        this.dbConn = dbConn;
    }
	
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
		ResultSet rs = null;
		ResultSet keys = null;
		try {
			PreparedStatement stSelectMachine = dbConn.connection.prepareStatement(
					"SELECT COUNT(*), description FROM machine "
					+ "WHERE name=?"); // NOI18N
			stSelectMachine.setString(1, hostname);
			rs = stSelectMachine.executeQuery();
			if (rs.getInt(1) > 0) {
				description.append(dbConn.getStringValue(rs, "description"));
				return true;
			} else {
				// Insert a new machine
				PreparedStatement stInsertMachine = dbConn.connection.prepareStatement(
						"INSERT INTO machine (name, hidden) VALUES (?, ?)"); // NOI18N
				stInsertMachine.setString(1, hostname);
				stInsertMachine.setBoolean(2, hidden);
				int nbRowsAffected = stInsertMachine.executeUpdate();
				if (nbRowsAffected == 1) {
					keys = stInsertMachine.getGeneratedKeys();
					keys.next();
					int idMachine = keys.getInt(1);
					rs.close();
					// Insert default options
					PreparedStatement stSelectOptionType = dbConn.connection.prepareStatement(
							"SELECT idOptionType, name, `default` "
							+ "FROM optiontype"); // NOI18N
					rs = stSelectOptionType.executeQuery();
					PreparedStatement stInsertOption = dbConn.connection.prepareStatement(
							"INSERT INTO option ('idMachine', 'idOptionType', "
							+ "'value') VALUES (?, ?, ?)"); // NOI18N
					while (rs.next()) {
						stInsertOption.setInt(1, idMachine);
						stInsertOption.setInt(2, rs.getInt("idOptionType")); // NOI18N
						stInsertOption.setString(3, dbConn.getStringValue(rs, "default")); // NOI18N
						nbRowsAffected = stInsertOption.executeUpdate();
						if (nbRowsAffected != 1) {
							Jamuz.getLogger().log(Level.SEVERE, "stInsertOption, "
									+ "idMachine={0}, idOptionType={1}, default=\"{2}\" "
									+ "# row(s) affected: +{1}",
									new Object[]{idMachine, rs.getInt("idOptionType"),
										dbConn.getStringValue(rs, "default"), nbRowsAffected}); // NOI18N
							return false;
						}
					}
					return true;
				} else {
					Jamuz.getLogger().log(Level.SEVERE, "stInsertMachine, "
							+ "hostname=\"{0}\" # row(s) affected: +{1}",
							new Object[]{hostname, nbRowsAffected}); // NOI18N
					return false;
				}
			}
		} catch (SQLException ex) {
			Popup.error("isMachine(" + hostname + ")", ex); // NOI18N
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				Jamuz.getLogger().warning("Failed to close ResultSet");
			}

			try {
				if (keys != null) {
					keys.close();
				}
			} catch (SQLException ex) {
				Jamuz.getLogger().warning("Failed to close ResultSet");
			}

		}
	}
    
	/**
	 * Updates genre in genre table
	 *
	 * @param idMachine
	 * @param description
	 * @return
	 */
	public synchronized boolean update(int idMachine, String description) {
		try {
			PreparedStatement stUpdateMachine = dbConn.connection.prepareStatement(
					"UPDATE machine SET description=? WHERE idMachine=?"); // NOI18N
			stUpdateMachine.setString(1, description);
			stUpdateMachine.setInt(2, idMachine);
			int nbRowsAffected = stUpdateMachine.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateMachine, idMachine={0}, "
						+ "description={1} # row(s) affected: +{2}",
						new Object[] { idMachine, description, nbRowsAffected }); // NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateGenre(" + idMachine + ", \"" + description + "\")", ex); // NOI18N
			return false;
		}
	}

	/**
	 * Deletes a machine
	 *
	 * @param machineName
	 * @return
	 */
	public synchronized boolean delete(String machineName) {
		try {
			PreparedStatement stDeleteMachine = dbConn.connection.prepareStatement("DELETE FROM machine WHERE name=?"); // NOI18N
			stDeleteMachine.setString(1, machineName);
			int nbRowsAffected = stDeleteMachine.executeUpdate();
			if (nbRowsAffected > 0) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stDeleteMachine(\"{0}\"). # row(s) affected: {1}",
						new Object[] { machineName, nbRowsAffected }); // NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("deleteMachine(" + machineName + ")", ex); // NOI18N
			return false;
		}
	}
}
