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

import jamuz.Playlist;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoPlaylist {

    private final DbConn dbConn;
    private final DaoPlaylistWrite daoPlaylistWrite;

    /**
     *
     * @param dbConn
     */
    public DaoPlaylist(DbConn dbConn) {
        this.dbConn = dbConn;
        this.daoPlaylistWrite = new DaoPlaylistWrite(dbConn);

    }

    /**
     * This is to reach writing operations (insert, update, delete)
     *
     * @return
     */
    public DaoPlaylistWrite lock() {
        return daoPlaylistWrite;
    }

    /**
     * Get list of playlists
     *
     * @param playlists
     * @return
     */
    public boolean get(HashMap<Integer, Playlist> playlists) {
        try {
            PreparedStatement stSelectPlaylists = dbConn.connection
                    .prepareStatement("SELECT idPlaylist, name, limitDo, "
                            + "limitValue, limitUnit, random, hidden, type, match, destExt FROM playlist"); // NOI18N
            ResultSet rs = stSelectPlaylists.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("idPlaylist"); // NOI18N
                String playlistName = dbConn.getStringValue(rs, "name"); // NOI18N
                boolean limit = rs.getBoolean("limitDo"); // NOI18N
                int limitValue = rs.getInt("limitValue"); // NOI18N
                Playlist.LimitUnit limitUnit = Playlist.LimitUnit.valueOf(dbConn.getStringValue(rs, "limitUnit")); // NOI18N
                boolean random = rs.getBoolean("random"); // NOI18N
                boolean hidden = rs.getBoolean("hidden");
                String destExt = rs.getString("destExt");
                Playlist.Type type = Playlist.Type.valueOf(dbConn.getStringValue(rs, "type")); // NOI18N
                Playlist.Match match = Playlist.Match.valueOf(dbConn.getStringValue(rs, "match")); // NOI18N
                Playlist playlist = new Playlist(id, playlistName, limit, limitValue, limitUnit, random, type, match,
                        hidden, destExt);

                // Get the filters
                PreparedStatement stSelectPlaylistFilters = dbConn.connection
                        .prepareStatement("SELECT idPlaylistFilter, field, operator, value "
                                + "FROM playlistFilter " // NOI18N
                                + "WHERE idPlaylist=?"); // NOI18N
                stSelectPlaylistFilters.setInt(1, id);
                ResultSet rsFilters = stSelectPlaylistFilters.executeQuery();
                while (rsFilters.next()) {
                    int idPlaylistFilter = rsFilters.getInt("idPlaylistFilter"); // NOI18N
                    String field = dbConn.getStringValue(rsFilters, "field"); // NOI18N
                    String operator = dbConn.getStringValue(rsFilters, "operator"); // NOI18N
                    String value = dbConn.getStringValue(rsFilters, "value"); // NOI18N
                    playlist.addFilter(new Playlist.Filter(idPlaylistFilter, Playlist.Field.valueOf(field),
                            Playlist.Operator.valueOf(operator), value));
                }

                // Get the orders
                PreparedStatement stSelectPlaylistOrders = dbConn.connection
                        .prepareStatement("SELECT idPlaylistOrder, desc, field "
                                + "FROM playlistOrder " // NOI18N
                                + "WHERE idPlaylist=?"); // NOI18N
                stSelectPlaylistOrders.setInt(1, id);
                ResultSet rsOrders = stSelectPlaylistOrders.executeQuery();
                while (rsOrders.next()) {
                    int idPlaylistOrder = rsOrders.getInt("idPlaylistOrder"); // NOI18N
                    boolean desc = rsOrders.getBoolean("desc"); // NOI18N
                    String field = dbConn.getStringValue(rsOrders, "field"); // NOI18N
                    playlist.addOrder(new Playlist.Order(idPlaylistOrder, Playlist.Field.valueOf(field), desc));
                }

                // Add playlist to hashmap
                playlists.put(id, playlist);
            }
            return true;
        } catch (SQLException ex) {
            Popup.error("getPlaylists", ex); // NOI18N
            return false;
        }
    }

}
