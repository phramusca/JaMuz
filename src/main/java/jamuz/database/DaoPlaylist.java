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
        String selectPlaylistsSql = "SELECT idPlaylist, name, limitDo, limitValue, limitUnit, random, hidden, type, match, destExt FROM playlist";
        try (PreparedStatement stSelectPlaylists = dbConn.connection.prepareStatement(selectPlaylistsSql); ResultSet rs = stSelectPlaylists.executeQuery()) {

            while (rs.next()) {
                Playlist playlist = buildPlaylistFromResultSet(rs);
                playlists.put(playlist.getId(), playlist);
            }

            return true;
        } catch (SQLException ex) {
            Popup.error("getPlaylists", ex);
            return false;
        }
    }

    private Playlist buildPlaylistFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("idPlaylist");
        String playlistName = dbConn.getStringValue(rs, "name");
        boolean limit = rs.getBoolean("limitDo");
        int limitValue = rs.getInt("limitValue");
        Playlist.LimitUnit limitUnit = Playlist.LimitUnit.valueOf(dbConn.getStringValue(rs, "limitUnit"));
        boolean random = rs.getBoolean("random");
        boolean hidden = rs.getBoolean("hidden");
        String destExt = rs.getString("destExt");
        Playlist.Type type = Playlist.Type.valueOf(dbConn.getStringValue(rs, "type"));
        Playlist.Match match = Playlist.Match.valueOf(dbConn.getStringValue(rs, "match"));
        Playlist playlist = new Playlist(id, playlistName, limit, limitValue, limitUnit, random, type, match, hidden, destExt);

        fetchPlaylistFilters(playlist);
        fetchPlaylistOrders(playlist);

        return playlist;
    }

    private void fetchPlaylistFilters(Playlist playlist) throws SQLException {
        String selectFiltersSql = "SELECT idPlaylistFilter, field, operator, value FROM playlistFilter WHERE idPlaylist=?";
        try (PreparedStatement stSelectPlaylistFilters = dbConn.connection.prepareStatement(selectFiltersSql)) {
            stSelectPlaylistFilters.setInt(1, playlist.getId());
            ResultSet rsFilters = stSelectPlaylistFilters.executeQuery();

            while (rsFilters.next()) {
                int idPlaylistFilter = rsFilters.getInt("idPlaylistFilter");
                String field = dbConn.getStringValue(rsFilters, "field");
                String operator = dbConn.getStringValue(rsFilters, "operator");
                String value = dbConn.getStringValue(rsFilters, "value");
                playlist.addFilter(new Playlist.Filter(idPlaylistFilter, Playlist.Field.valueOf(field),
                        Playlist.Operator.valueOf(operator), value));
            }
        }
    }

    private void fetchPlaylistOrders(Playlist playlist) throws SQLException {
        String selectOrdersSql = "SELECT idPlaylistOrder, desc, field FROM playlistOrder WHERE idPlaylist=?";
        try (PreparedStatement stSelectPlaylistOrders = dbConn.connection.prepareStatement(selectOrdersSql)) {
            stSelectPlaylistOrders.setInt(1, playlist.getId());
            ResultSet rsOrders = stSelectPlaylistOrders.executeQuery();

            while (rsOrders.next()) {
                int idPlaylistOrder = rsOrders.getInt("idPlaylistOrder");
                boolean desc = rsOrders.getBoolean("desc");
                String field = dbConn.getStringValue(rsOrders, "field");
                playlist.addOrder(new Playlist.Order(idPlaylistOrder, Playlist.Field.valueOf(field), desc));
            }
        }
    }

}
