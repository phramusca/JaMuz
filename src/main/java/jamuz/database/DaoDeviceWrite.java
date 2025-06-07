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
import jamuz.process.sync.Device;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 *
 * @author raph
 */
public class DaoDeviceWrite {

    private final DbConn dbConn;

    /**
     *
     * @param dbConn
     */
    public DaoDeviceWrite(DbConn dbConn) {
        this.dbConn = dbConn;
    }

    /**
     * Inserts or updates a device in the database.
     *
     * @param device the device information to be inserted or updated
     * @return true if the operation is successful, false otherwise
     */
    public boolean insertOrUpdate(Device device) {
        synchronized (dbConn) {
            try {
                if (device.getId() > -1) {
                    return updateDevice(device);
                } else {
                    return insertDevice(device);
                }
            } catch (SQLException ex) {
                Popup.error("setDevice(" + device.toString() + ")", ex);
                return false;
            }
        }
    }

    private boolean updateDevice(Device device) throws SQLException {
        synchronized (dbConn) {
            try (PreparedStatement stUpdateDevice = dbConn.connection.prepareStatement(
                    "UPDATE device SET name=?, source=?, destination=?, idPlaylist=? WHERE idDevice=?")) {

                stUpdateDevice.setString(1, device.getName());
                stUpdateDevice.setString(2, device.getSource());
                stUpdateDevice.setString(3, device.getDestination());
                stUpdateDevice.setInt(4, (device.getIdPlaylist() > 0) ? device.getIdPlaylist() : java.sql.Types.INTEGER);
                stUpdateDevice.setInt(5, device.getId());

                int nbRowsAffected = stUpdateDevice.executeUpdate();
                if (nbRowsAffected > 0) {
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "stUpdateDevice, myStatSource={0} # row(s) affected: +{1}",
                            new Object[]{device.toString(), nbRowsAffected});
                    return false;
                }
            }
        }
    }

    private boolean insertDevice(Device device) throws SQLException {
        synchronized (dbConn) {
            try (PreparedStatement stInsertDevice = dbConn.connection.prepareStatement(
                    "INSERT INTO device (name, source, destination, idMachine, idPlaylist) "
                    + "VALUES (?, ?, ?, (SELECT idMachine FROM machine WHERE name=?), ?)")) {

                stInsertDevice.setString(1, device.getName());
                stInsertDevice.setString(2, device.getSource());
                stInsertDevice.setString(3, device.getDestination());
                stInsertDevice.setString(4, device.getMachineName());
                stInsertDevice.setInt(5, (device.getIdPlaylist() > 0) ? device.getIdPlaylist() : java.sql.Types.INTEGER);

                int nbRowsAffected = stInsertDevice.executeUpdate();
                if (nbRowsAffected > 0) {
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "stInsertDevice, myStatSource={0} # row(s) affected: +{1}",
                            new Object[]{device.toString(), nbRowsAffected});
                    return false;
                }
            }
        }
    }

    /**
     * Deletes a device from the database.
     *
     * @param id the ID of the device to be deleted
     * @return true if the operation is successful, false otherwise
     */
    public boolean delete(int id) {
        synchronized (dbConn) {
            try (PreparedStatement stDeleteDevice = dbConn.connection.prepareStatement(
                    "DELETE FROM device WHERE idDevice=?")) {

                stDeleteDevice.setInt(1, id);
                int nbRowsAffected = stDeleteDevice.executeUpdate();
                if (nbRowsAffected > 0) {
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "stDeleteDevice, id={0} # row(s) affected: +{1}",
                            new Object[]{id, nbRowsAffected});
                    return false;
                }
            } catch (SQLException ex) {
                // FIXME Z OPTIONS Happens when the device is linked to a stat source =>
                // => Popup this nicely to the user!
                // Instead of:
                // java.sql.SQLException: [SQLITE_CONSTRAINT]
                // Abort due to constraint violation (foreign key constraint failed)
                Popup.error("deleteDevice(" + id + ")", ex);
                return false;
            }
        }
    }

}
