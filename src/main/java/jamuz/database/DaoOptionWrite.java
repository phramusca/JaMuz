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
     *
     * @param machine
     * @return
     */
    public boolean update(Machine machine) {
        try {
            dbConn.connection.setAutoCommit(false);

            PreparedStatement stUpdateOptions = dbConn.connection.prepareStatement(
                    "UPDATE option SET value=? "
                    + "WHERE idMachine=? AND idOptionType=?"); // NOI18N

            for (Option option : machine.getOptions()) {
                if (option.getType().equals("path")
                        && !option.getValue().isBlank()) { // NOI18N
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
                    new Object[]{results.length, endTime - startTime}); // NOI18N
            // Check results
            int result;
            for (int i = 0; i < results.length; i++) {
                result = results[i];
                if (result != 1) {
                    return false;
                }
            }
            dbConn.connection.setAutoCommit(true);
            return true;
        } catch (SQLException ex) {
            Popup.error(ex);
            return false;
        }
    }

    /**
     * Set option value (update)
     *
     * @param myOption
     * @param value
     * @return
     */
    public boolean update(Option myOption, String value) {
        try {
            if (myOption.getType().equals("path")) { // NOI18N
                value = FilenameUtils.normalizeNoEndSeparator(value.trim()) + File.separator;
            }
            PreparedStatement stUpdateOption = dbConn.connection.prepareStatement("UPDATE option SET value=? "
                    + "WHERE idMachine=? AND idOptionType=?"); // NOI18N
            stUpdateOption.setString(1, value);
            stUpdateOption.setInt(2, myOption.getIdMachine());
            stUpdateOption.setInt(3, myOption.getIdOptionType());

            int nbRowsAffected = stUpdateOption.executeUpdate();
            if (nbRowsAffected > 0) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "stUpdateOption, value={0}, idMachine={1}, "
                        + "idMachidOptionTypeine={2} # row(s) affected: +{3}", // NOI18N
                        new Object[]{value, myOption.getIdMachine(), myOption.getIdOptionType(), nbRowsAffected});
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("setOption(" + myOption.toString() + "," + value + ")", ex); // NOI18N
            return false;
        }
    }
}
