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
     * @return true if the insertion is successful, false otherwise
     */
    public boolean insert(Playlist playlist) {
        synchronized (dbConn) {
            try {
                String sql = "INSERT INTO playlist "
                        + "(name, limitDo, limitValue, limitUnit, type, match, random, hidden, destExt) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement stInsertPlaylist = dbConn.connection.prepareStatement(sql)) {
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
                }
            } catch (SQLException ex) {
                Popup.error("insertPlaylist(" + playlist + ")", ex); // NOI18N
                return false;
            }
        }
    }

    /**
     * Updates a playlist
     *
     * @param playlist
     * @return true if the update is successful, false otherwise
     */
    public boolean update(Playlist playlist) {
        synchronized (dbConn) {
            try {
                String updateSql = "UPDATE playlist "
                        + "SET limitDo=?, limitValue=?, limitUnit=?, random=?, "
                        + "type=?, match=?, name=?, hidden=?, destExt=? "
                        + "WHERE idPlaylist=?";

                try (PreparedStatement stUpdatePlaylist = dbConn.connection.prepareStatement(updateSql)) {
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
                        deletePlaylistFilters(playlist.getId());
                        deletePlaylistOrders(playlist.getId());

                        try (PreparedStatement stInsertPlaylistFilter = dbConn.connection.prepareStatement(
                                "INSERT INTO playlistFilter (field, operator, value, idPlaylist) VALUES (?, ?, ?, ?)")) {
                            batchInsertPlaylistFilter(stInsertPlaylistFilter, playlist);
                        }

                        try (PreparedStatement stInsertPlaylistOrder = dbConn.connection.prepareStatement(
                                "INSERT INTO playlistOrder (desc, field, idPlaylist) VALUES (?, ?, ?)")) {
                            batchInsertPlaylistOrder(stInsertPlaylistOrder, playlist);
                        }

                        return true;
                    } else {
                        Jamuz.getLogger().log(Level.SEVERE, "stUpdatePlaylist, playlist={0}, "
                                + "# row(s) affected: +{1}",
                                new Object[]{playlist.toString(), nbRowsAffected});
                        return false;
                    }
                }
            } catch (SQLException ex) {
                Popup.error("updatePlaylist(" + playlist.toString() + ")", ex);
                return false;
            }
        }
    }

    private void deletePlaylistFilters(int idPlaylist) throws SQLException {
        try (PreparedStatement stDeletePlaylistFilters = dbConn.connection
                .prepareStatement("DELETE FROM playlistFilter WHERE idPlaylist=?")) {
            stDeletePlaylistFilters.setInt(1, idPlaylist);
            stDeletePlaylistFilters.executeUpdate();
        }
    }

    private void deletePlaylistOrders(int idPlaylist) throws SQLException {
        try (PreparedStatement stDeletePlaylistOrders = dbConn.connection
                .prepareStatement("DELETE FROM playlistOrder WHERE idPlaylist=?")) {
            stDeletePlaylistOrders.setInt(1, idPlaylist);
            stDeletePlaylistOrders.executeUpdate();
        }
    }

    private void batchInsertPlaylistFilter(PreparedStatement stInsertPlaylistFilter, Playlist playlist) throws SQLException {
        for (Playlist.Filter filter : playlist.getFilters()) {
            stInsertPlaylistFilter.setString(1, filter.getFieldName());
            stInsertPlaylistFilter.setString(2, filter.getOperatorName());
            stInsertPlaylistFilter.setString(3, filter.getValue());
            stInsertPlaylistFilter.setInt(4, playlist.getId());
            stInsertPlaylistFilter.addBatch();
        }

        executeBatchAndCommit(stInsertPlaylistFilter);
    }

    private void batchInsertPlaylistOrder(PreparedStatement stInsertPlaylistOrder, Playlist playlist) throws SQLException {
        for (Playlist.Order order : playlist.getOrders()) {
            stInsertPlaylistOrder.setBoolean(1, order.isDesc());
            stInsertPlaylistOrder.setString(2, order.getFieldName());
            stInsertPlaylistOrder.setInt(3, playlist.getId());
            stInsertPlaylistOrder.addBatch();
        }

        executeBatchAndCommit(stInsertPlaylistOrder);
    }

    private void executeBatchAndCommit(PreparedStatement statement) throws SQLException {
        long startTime = System.currentTimeMillis();
        int[] results = statement.executeBatch();
        dbConn.connection.commit();
        long endTime = System.currentTimeMillis();
        Jamuz.getLogger().log(Level.FINEST, "{0} // {1} // Total execution time: {2}ms",
                new Object[]{statement.toString(), results.length, endTime - startTime});

        // Check results
        for (int result : results) {
            if (result != 1) {
                throw new SQLException("Batch execution failed.");
            }
        }
    }

    /**
     * Deletes a playlist
     *
     * @param id
     * @return true if the playlist is successfully deleted, false otherwise
     */
    public boolean delete(int id) {
        synchronized (dbConn) {
            try {
                String deleteSql = "DELETE FROM playlist "
                        + "WHERE idPlaylist=? "
                        + "AND idPlaylist NOT IN (SELECT idPlaylist FROM device WHERE idPlaylist IS NOT NULL) "
                        + "AND idPlaylist NOT IN (SELECT value FROM playlistFilter WHERE field='PLAYLIST')";

                try (PreparedStatement stDeletePlaylist = dbConn.connection.prepareStatement(deleteSql)) {
                    stDeletePlaylist.setInt(1, id);
                    int nbRowsAffected = stDeletePlaylist.executeUpdate();
                    if (nbRowsAffected > 0) {
                        return true;
                    } else {
                        Popup.warning("Playlist is linked to a sync device or another playlist so cannot delete it.");
                        return false;
                    }
                }
            } catch (SQLException ex) {
                Popup.error("deletePlaylist(" + id + ")", ex);
                return false;
            }
        }
    }

}
