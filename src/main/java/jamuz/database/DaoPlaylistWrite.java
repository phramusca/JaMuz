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
import jamuz.Playlist;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 *
 * @author raph
 */
public class DaoPlaylistWrite {

    private final DbConn dbConn;

    /**
     *
     * @param dbConn
     */
    public DaoPlaylistWrite(DbConn dbConn) {
        this.dbConn = dbConn;
    }

    /**
     * Inserts an empty playlist
     *
     * @param playlist
     * @return
     */
    public boolean insert(Playlist playlist) {
        try {
            PreparedStatement stInsertPlaylist = dbConn.connection.prepareStatement(
                    "INSERT INTO playlist "
                    + "(name, limitDo, limitValue, limitUnit, type, match, random, hidden, destExt) " // NOI18N
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"); // NOI18N
            stInsertPlaylist.setString(1, playlist.getName());
            stInsertPlaylist.setBoolean(2, playlist.isLimit());
            stInsertPlaylist.setInt(3, playlist.getLimitValue());
            stInsertPlaylist.setString(4, playlist.getLimitUnit().name());
            stInsertPlaylist.setString(5, playlist.getType().name());
            stInsertPlaylist.setString(6, playlist.getMatch().name());
            stInsertPlaylist.setBoolean(7, playlist.isRandom());
            stInsertPlaylist.setBoolean(8, playlist.isHidden());
            stInsertPlaylist.setString(9, playlist.getDestExt());

            int nbRowsAffected = stInsertPlaylist.executeUpdate();
            if (nbRowsAffected == 1) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "stInsertPlaylist, playlist=\"{0}\" "
                        + "# row(s) affected: +{1}",
                        new Object[]{playlist, nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("insertPlaylist(" + playlist + ")", ex); // NOI18N
            return false;
        }
    }

    /**
     * Updates a playlist
     *
     * @param playlist
     * @return
     */
    public boolean update(Playlist playlist) {
        try {
            PreparedStatement stUpdatePlaylist = dbConn.connection.prepareStatement(
                    "UPDATE playlist "
                    + "SET limitDo=?, limitValue=?, limitUnit=?, random=?, "
                    + "type=?, match=?, name=?, hidden=?, destExt=? " // NOI18N
                    + "WHERE idPlaylist=?"); // NOI18N
            stUpdatePlaylist.setBoolean(1, playlist.isLimit());
            stUpdatePlaylist.setDouble(2, playlist.getLimitValue());
            stUpdatePlaylist.setString(3, playlist.getLimitUnit().name());
            stUpdatePlaylist.setBoolean(4, playlist.isRandom());
            stUpdatePlaylist.setString(5, playlist.getType().name());
            stUpdatePlaylist.setString(6, playlist.getMatch().name());
            stUpdatePlaylist.setString(7, playlist.getName());
            stUpdatePlaylist.setBoolean(8, playlist.isHidden());
            stUpdatePlaylist.setString(9, playlist.getDestExt());
            stUpdatePlaylist.setInt(10, playlist.getId());
            int nbRowsAffected = stUpdatePlaylist.executeUpdate();
            if (nbRowsAffected == 1) {
                PreparedStatement stDeletePlaylistFilters = dbConn.connection
                        .prepareStatement("DELETE FROM playlistFilter WHERE idPlaylist=?"); // NOI18N
                // Delete all filters for this playlist (before inserting new ones)
                stDeletePlaylistFilters.setInt(1, playlist.getId());
                stDeletePlaylistFilters.executeUpdate(); // Can have no filter, not checking numberOfRowsAffected

                PreparedStatement stDeletePlaylistOrders = dbConn.connection
                        .prepareStatement("DELETE FROM playlistOrder WHERE idPlaylist=?"); // NOI18N
                stDeletePlaylistOrders.setInt(1, playlist.getId());
                stDeletePlaylistOrders.executeUpdate(); // Can have no order, not checking numberOfRowsAffected
                dbConn.connection.setAutoCommit(false);
                PreparedStatement stInsertPlaylistFilter = dbConn.connection
                        .prepareStatement("INSERT INTO playlistFilter "
                                + "(field, operator, value, idPlaylist) " // NOI18N
                                + "VALUES (?, ?, ?, ?)"); // NOI18N
                for (Playlist.Filter filter : playlist.getFilters()) {
                    stInsertPlaylistFilter.setString(1, filter.getFieldName());
                    stInsertPlaylistFilter.setString(2, filter.getOperatorName());
                    stInsertPlaylistFilter.setString(3, filter.getValue());
                    stInsertPlaylistFilter.setInt(4, playlist.getId());
                    stInsertPlaylistFilter.addBatch();
                }
                long startTime = System.currentTimeMillis();
                int[] results = stInsertPlaylistFilter.executeBatch();
                dbConn.connection.commit();
                long endTime = System.currentTimeMillis();
                Jamuz.getLogger().log(Level.FINEST, "stInsertPlaylistFilter // {0} "
                        + "// Total execution time: {1}ms",
                        new Object[]{results.length, endTime - startTime}); // NOI18N
                // Check results
                for (int i = 0; i < results.length; i++) {
                    if (results[i] != 1) {
                        return false;
                    }
                }
                PreparedStatement stInsertPlaylistOrder = dbConn.connection
                        .prepareStatement("INSERT INTO playlistOrder "
                                + "(desc, field, idPlaylist) " // NOI18N
                                + "VALUES (?, ?, ?)"); // NOI18N
                for (Playlist.Order order : playlist.getOrders()) {
                    stInsertPlaylistOrder.setBoolean(1, order.isDesc());
                    stInsertPlaylistOrder.setString(2, order.getFieldName());
                    stInsertPlaylistOrder.setInt(3, playlist.getId());
                    stInsertPlaylistOrder.addBatch();
                }
                startTime = System.currentTimeMillis();
                results = stInsertPlaylistOrder.executeBatch();
                dbConn.connection.commit();
                endTime = System.currentTimeMillis();
                Jamuz.getLogger().log(Level.FINEST, "stInsertPlaylistOrder // {0} "
                        + "// Total execution time: {1}ms",
                        new Object[]{results.length, endTime - startTime}); // NOI18N
                // Check results
                for (int i = 0; i < results.length; i++) {
                    if (results[i] != 1) {
                        return false;
                    }
                }
                // TODO DB TEST: Remove the line below if rollback and finally tests are
                // successfull
                dbConn.connection.setAutoCommit(true);
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "stUpdatePlaylist, playlist={0}, "
                        + "# row(s) affected: +{1}",
                        new Object[]{playlist.toString(),
                            nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("updatePlaylist(" + playlist.toString() + ")", ex); // NOI18N
            // TODO DB TEST the rollback block below and apply to other if successfull
            // try {
            // dbConn.connection.rollback();
            // } catch (SQLException ex1) {
            // Jamuz.getLogger().log(Level.SEVERE, null, ex1);
            // }
            return false;
        }
        // TODO DB TEST the finally block below and apply to other if successfull
        // finally {
        // try {
        // dbConn.connection.setAutoCommit(true);
        // } catch (SQLException ex1) {
        // Jamuz.getLogger().log(Level.SEVERE, null, ex1);
        // }
        // }
    }

    /**
     * Deletes a playlist
     *
     * @param id
     * @return
     */
    public boolean delete(int id) {
        try {
            PreparedStatement stDeletePlaylist = dbConn.connection.prepareStatement("DELETE FROM playlist "
                    + "WHERE idPlaylist=? " // NOI18N
                    + "AND idPlaylist NOT IN (SELECT idPlaylist FROM device WHERE idPlaylist IS NOT NULL) "
                    + "AND idPlaylist NOT IN (SELECT value FROM playlistFilter WHERE field='PLAYLIST')"); // NOI18N

            stDeletePlaylist.setInt(1, id);
            int nbRowsAffected = stDeletePlaylist.executeUpdate();
            if (nbRowsAffected > 0) {
                return true;
            } else {
                Popup.warning("Playlist is linked to a sync device or another playlist so cannot delete it."); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("deletePlaylist(" + id + ")", ex); // NOI18N
            return false;
        }
    }
}
