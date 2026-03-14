package jamuz.database;

import jamuz.Jamuz;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class DaoGenre {

    private final DbConn dbConn;
    private final DaoGenreWrite daoGenreWrite;

    public DaoGenre(DbConn dbConn) {
        this.dbConn = dbConn;
        this.daoGenreWrite = new DaoGenreWrite(dbConn, this);
    }

    /**
     * This is to reach writing operations (insert, update, delete)
     *
     * @return
     */
    public DaoGenreWrite lock() {
        return daoGenreWrite;
    }

    /**
     * Checks if genre is in the supported list.
     *
     * @param genre
     * @return true if the genre exists in the table
     * @throws RuntimeException if a DB error occurs (caller in UI should catch and show Popup)
     */
    public boolean isSupported(String genre) {
        try (PreparedStatement stCheckGenre = dbConn.connection.prepareStatement(
                "SELECT COUNT(*) FROM genre WHERE value=?")) {
            stCheckGenre.setString(1, genre);
            try (ResultSet rs = stCheckGenre.executeQuery()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            Jamuz.getLogger().log(Level.SEVERE, "checkGenre(" + genre + ")", ex);
            throw new RuntimeException(ex);
        }
    }

}
