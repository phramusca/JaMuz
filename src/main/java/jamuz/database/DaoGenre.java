package jamuz.database;

import jamuz.Jamuz;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import jamuz.utils.Popup;

public class DaoGenre {

    private final DbConn dbConn;

    public DaoGenre(DbConn dbConn) {
        this.dbConn = dbConn;
    }

	/**
     * Inserts a genre
     *
     * @param genre
     * @return
     */
    public synchronized boolean insert(String genre) {
        try {
            if (isSupported(genre)) {
                return false;
            }
            PreparedStatement stInsertGenre = dbConn.connection.prepareStatement(
                    "INSERT INTO genre (value) VALUES (?)"); // NOI18N
            stInsertGenre.setString(1, genre);
            int nbRowsAffected = stInsertGenre.executeUpdate();
            if (nbRowsAffected == 1) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "stInsertGenre, genre=\"{0}\" "
                        + "# row(s) affected: +{1}", new Object[] { genre, nbRowsAffected }); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Jamuz.getLogger().log(Level.SEVERE, "stInsertGenre, genre=\"{0}\" "
                    + "Exception: {1}", new Object[] { genre, ex.toString() }); // NOI18N
            return false;
        }
    }
	
	/**
     * Checks if genre is in supported list
     *
     * @param genre
     * @return
     */
    public boolean isSupported(String genre) {
        ResultSet rs = null;
        try {
            PreparedStatement stCheckGenre = dbConn.connection.prepareStatement(
                    "SELECT COUNT(*) FROM genre WHERE value=?"); // NOI18N
            stCheckGenre.setString(1, genre);
            rs = stCheckGenre.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException ex) {
            Popup.error("checkGenre(" + genre + ")", ex); // NOI18N
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

    /**
     * Updates genre in genre table
     *
     * @param oldGenre
     * @param newGenre
     * @return
     */
    public synchronized boolean update(String oldGenre, String newGenre) {
        try {
            PreparedStatement stUpdateGenre = dbConn.connection.prepareStatement(
                    "UPDATE genre SET value=? WHERE value=?"); // NOI18N
            stUpdateGenre.setString(1, newGenre);
            stUpdateGenre.setString(2, oldGenre);
            int nbRowsAffected = stUpdateGenre.executeUpdate();
            if (nbRowsAffected == 1) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "stUpdateGenre, oldGenre={0}, "
                        + "newGenre={1} # row(s) affected: +{2}",
                        new Object[] { oldGenre, newGenre, nbRowsAffected }); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("updateGenre(" + oldGenre + ", " + newGenre + ")", ex); // NOI18N
            return false;
        }
    }

    /**
     * Deletes genre from genre table
     *
     * @param genre
     * @return
     */
    public synchronized boolean delete(String genre) {
        try {
            PreparedStatement stDeleteGenre = dbConn.connection.prepareStatement(
                    "DELETE FROM genre WHERE value=?"); // NOI18N
            stDeleteGenre.setString(1, genre);
            int nbRowsAffected = stDeleteGenre.executeUpdate();
            if (nbRowsAffected > 0) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "stDeleteGenre, "
                        + "genre={0} # row(s) affected: +{1}",
                        new Object[] { genre, nbRowsAffected }); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("deleteGenre(" + genre + ")", ex); // NOI18N
            return false;
        }
    }

}