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
import jamuz.process.sync.Device;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoDevice {

    private final DbConn dbConn;
    private final DaoDeviceWrite daoDeviceWrite;

    /**
     *
     * @param dbConn
     */
    public DaoDevice(DbConn dbConn) {
        this.dbConn = dbConn;
        daoDeviceWrite = new DaoDeviceWrite(dbConn);
    }

    /**
     * This is to reach writing operations (insert, update, delete)
     *
     * @return
     */
    public DaoDeviceWrite lock() {
        return daoDeviceWrite;
    }

    /**
     *
     * @param login
     * @return
     */
    public Device get(String login) {
        LinkedHashMap<Integer, Device> devices = new LinkedHashMap<>();
        DaoDevice.this.get(devices, login, true);
        return devices.values().iterator().next();
    }

    /**
     * Get list of devices
     *
     * @param devices
     * @param hostname
     * @param hidden
     * @return
     */
    public boolean get(LinkedHashMap<Integer, Device> devices,
            String hostname, boolean hidden) {
        ResultSet rs = null;
        try {
            PreparedStatement stSelectDevices = dbConn.connection.prepareStatement(
                    "SELECT idDevice AS deviceId, source, destination, idPlaylist, D.name AS deviceName "
                    + "FROM device D "
                    + "JOIN machine M "
                    + "ON M.idMachine=D.idMachine " // NOI18N
                    + "WHERE M.name=? "
                    + "ORDER BY D.name"); // NOI18N
            stSelectDevices.setString(1, hostname);
            rs = stSelectDevices.executeQuery();
            while (rs.next()) {
                Device device = get(rs, hostname, hidden);
                devices.put(device.getId(), device); // NOI18N
            }
            return true;
        } catch (SQLException ex) {
            Popup.error("getDevices", ex); // NOI18N
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

    public Device get(ResultSet rs, String hostname, boolean hidden) throws SQLException {
        int idDevice = rs.getInt("deviceId"); // NOI18N
        return new Device(idDevice,
                dbConn.getStringValue(rs, "deviceName"), // NOI18N
                dbConn.getStringValue(rs, "source"),
                dbConn.getStringValue(rs, "destination"),
                rs.getInt("idPlaylist"), hostname, hidden);
    }

}
