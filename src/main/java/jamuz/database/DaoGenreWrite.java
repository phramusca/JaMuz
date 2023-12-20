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
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 *
 * @author raph
 */
public class DaoGenreWrite {

    private final DbConn dbConn;
    private final DaoGenre daoGenre;

    /**
     *
     * @param dbConn
     * @param daoGenre
     */
    public DaoGenreWrite(DbConn dbConn, DaoGenre daoGenre) {
        this.dbConn = dbConn;
        this.daoGenre = daoGenre;
    }

    /**
     * Inserts a genre
     *
     * @param genre
     * @return
     */
    public boolean insert(String genre) {
        synchronized (dbConn) {
            try {
                if (daoGenre.isSupported(genre)) {
                    return false;
                }
                String sql = "INSERT INTO genre (value) VALUES (?)";
                try (PreparedStatement stInsertGenre = dbConn.connection.prepareStatement(sql)) {
                    stInsertGenre.setString(1, genre);
                    int nbRowsAffected = stInsertGenre.executeUpdate();
                    if (nbRowsAffected == 1) {
                        return true;
                    } else {
                        Jamuz.getLogger().log(Level.SEVERE, "stInsertGenre, genre=\"{0}\" "
                                + "# row(s) affected: +{1}", new Object[]{genre, nbRowsAffected});
                        return false;
                    }
                }
            } catch (SQLException ex) {
                Jamuz.getLogger().log(Level.SEVERE, "stInsertGenre, genre=\"{0}\" "
                        + "Exception: {1}", new Object[]{genre, ex.toString()});
                return false;
            }
        }
    }

    /**
     * Updates genre in genre table
     *
     * @param oldGenre
     * @param newGenre
     * @return
     */
    public boolean update(String oldGenre, String newGenre) {
        synchronized (dbConn) {
            try {
                String sql = "UPDATE genre SET value=? WHERE value=?";
                try (PreparedStatement stUpdateGenre = dbConn.connection.prepareStatement(sql)) {
                    stUpdateGenre.setString(1, newGenre);
                    stUpdateGenre.setString(2, oldGenre);
                    int nbRowsAffected = stUpdateGenre.executeUpdate();
                    if (nbRowsAffected == 1) {
                        return true;
                    } else {
                        Jamuz.getLogger().log(Level.SEVERE, "stUpdateGenre, oldGenre={0}, "
                                + "newGenre={1} # row(s) affected: +{2}",
                                new Object[]{oldGenre, newGenre, nbRowsAffected});
                        return false;
                    }
                }
            } catch (SQLException ex) {
                Popup.error("updateGenre(" + oldGenre + ", " + newGenre + ")", ex);
                return false;
            }
        }
    }

    /**
     * Deletes genre from genre table
     *
     * @param genre
     * @return
     */
    public boolean delete(String genre) {
        synchronized (dbConn) {
            try {
                String sql = "DELETE FROM genre WHERE value=?";
                try (PreparedStatement stDeleteGenre = dbConn.connection.prepareStatement(sql)) {
                    stDeleteGenre.setString(1, genre);
                    int nbRowsAffected = stDeleteGenre.executeUpdate();
                    if (nbRowsAffected > 0) {
                        return true;
                    } else {
                        Jamuz.getLogger().log(Level.SEVERE, "stDeleteGenre, "
                                + "genre={0} # row(s) affected: +{1}",
                                new Object[]{genre, nbRowsAffected});
                        return false;
                    }
                }
            } catch (SQLException ex) {
                Popup.error("deleteGenre(" + genre + ")", ex);
                return false;
            }
        }
    }

}
