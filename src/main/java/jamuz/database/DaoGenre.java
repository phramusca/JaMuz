package jamuz.database;

import jamuz.Jamuz;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jamuz.utils.Popup;

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

}
