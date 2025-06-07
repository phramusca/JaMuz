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
import jamuz.Machine;
import jamuz.Option;
import jamuz.utils.Popup;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author raph
 */
public class DaoOptionWrite {

    private final DbConn dbConn;

    /**
     *
     * @param dbConn
     */
    public DaoOptionWrite(DbConn dbConn) {
        this.dbConn = dbConn;
    }

    /**
     * Updates options for a machine
     *
     * @param machine
     * @return true if the update is successful, false otherwise
     */
    public boolean update(Machine machine) {
        synchronized (dbConn) {
            try {
                dbConn.connection.setAutoCommit(false);

                try (PreparedStatement stUpdateOptions = dbConn.connection.prepareStatement(
                        "UPDATE option SET value=? "
                        + "WHERE idMachine=? AND idOptionType=?")) {

                    for (Option option : machine.getOptions()) {
                        if (option.getType().equals("path") && !option.getValue().isBlank()) {
                            option.setValue(FilenameUtils.normalizeNoEndSeparator(option.getValue().trim())
                                    + File.separator);
                        }

                        stUpdateOptions.setString(1, option.getValue());
                        stUpdateOptions.setInt(2, option.getIdMachine());
                        stUpdateOptions.setInt(3, option.getIdOptionType());

                        stUpdateOptions.addBatch();
                    }

                    long startTime = System.currentTimeMillis();
                    int[] results = stUpdateOptions.executeBatch();
                    dbConn.connection.commit();
                    long endTime = System.currentTimeMillis();

                    Jamuz.getLogger().log(Level.FINEST, "setOptions // {0} // Total execution time: {1}ms",
                            new Object[]{results.length, endTime - startTime});

                    // Check results
                    for (int result : results) {
                        if (result != 1) {
                            return false;
                        }
                    }

                    dbConn.connection.setAutoCommit(true);
                    return true;
                }
            } catch (SQLException ex) {
                Popup.error(ex);
                return false;
            }
        }
    }

    /**
     * Set option value (update)
     *
     * @param myOption
     * @param value
     * @return true if the update is successful, false otherwise
     */
    public boolean update(Option myOption, String value) {
        synchronized (dbConn) {
            try {
                if (myOption.getType().equals("path")) {
                    value = FilenameUtils.normalizeNoEndSeparator(value.trim()) + File.separator;
                }

                try (PreparedStatement stUpdateOption = dbConn.connection.prepareStatement(
                        "UPDATE option SET value=? WHERE idMachine=? AND idOptionType=?")) {

                    stUpdateOption.setString(1, value);
                    stUpdateOption.setInt(2, myOption.getIdMachine());
                    stUpdateOption.setInt(3, myOption.getIdOptionType());

                    int nbRowsAffected = stUpdateOption.executeUpdate();

                    if (nbRowsAffected > 0) {
                        return true;
                    } else {
                        Jamuz.getLogger().log(Level.SEVERE, "stUpdateOption, value={0}, idMachine={1}, "
                                + "idOptionType={2} # row(s) affected: +{3}",
                                new Object[]{value, myOption.getIdMachine(), myOption.getIdOptionType(), nbRowsAffected});
                        return false;
                    }
                }
            } catch (SQLException ex) {
                Popup.error("setOption(" + myOption.toString() + "," + value + ")", ex);
                return false;
            }
        }
    }

}
