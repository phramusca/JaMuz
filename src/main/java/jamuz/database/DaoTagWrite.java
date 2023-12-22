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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

/**
 *
 * @author raph
 */
public class DaoTagWrite {

    private final DbConn dbConn;

    /**
     *
     * @param dbConn
     */
    public DaoTagWrite(DbConn dbConn) {
        this.dbConn = dbConn;
    }

    /**
     * Inserts a tag
     *
     * @param tag the tag value to be inserted
     * @return true if insertion is successful, false otherwise
     */
    public boolean insert(String tag) {
        synchronized (dbConn) {
            try (PreparedStatement stInsertTag = dbConn.connection.prepareStatement(
                    "INSERT OR IGNORE INTO tag (value) VALUES (?) ")) {
                stInsertTag.setString(1, tag);
                int nbRowsAffected = stInsertTag.executeUpdate();
                if (nbRowsAffected == 1) {
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "stInsertTag, tag=\"{0}\" "
                            + "# row(s) affected: +{1}", new Object[]{tag, nbRowsAffected});
                    return false;
                }
            } catch (SQLException ex) {
                Popup.error("insertTag(" + tag + ")", ex);
                return false;
            }
        }
    }

    /**
     * Inserts a tag if it doesn't already exist
     *
     * @param tag the tag value to be inserted
     * @return true if the tag already exists or insertion is successful, false
     * otherwise
     */
    boolean insertIfMissing(String tag) {
        synchronized (dbConn) {
            try (PreparedStatement stSelectTag = dbConn.getConnection().prepareStatement(
                    "SELECT COUNT(*) FROM tag WHERE value=?"); ResultSet rs = stSelectTag.executeQuery()) {

                if (rs.next() && rs.getInt(1) > 0) {
                    return true; // Tag already exists
                } else {
                    return insert(tag);
                }
            } catch (SQLException ex) {
                Popup.error("isTag(" + tag + ")", ex);
                return false;
            }
        }
    }

    /**
     * Updates a tag in the tag table
     *
     * @param oldTag the old tag value to be updated
     * @param newTag the new tag value
     * @return true if the update is successful, false otherwise
     */
    public boolean update(String oldTag, String newTag) {
        synchronized (dbConn) {
            try (PreparedStatement stUpdateTag = dbConn.connection.prepareStatement(
                    "UPDATE tag SET value=? WHERE value=?")) {
                stUpdateTag.setString(1, newTag);
                stUpdateTag.setString(2, oldTag);
                int nbRowsAffected = stUpdateTag.executeUpdate();
                if (nbRowsAffected == 1) {
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "stUpdateTag, oldTag={0}, "
                            + "newTag={1} # row(s) affected: +{2}",
                            new Object[]{oldTag, newTag, nbRowsAffected});
                    return false;
                }
            } catch (SQLException ex) {
                Popup.error("updateTag(" + oldTag + ", " + newTag + ")", ex);
                return false;
            }
        }
    }

    /**
     * Deletes a tag from the tag table
     *
     * @param tag the tag value to be deleted
     * @return true if the deletion is successful, false otherwise
     */
    public boolean delete(String tag) {
        synchronized (dbConn) {
            try (PreparedStatement stDeleteTag = dbConn.connection.prepareStatement(
                    """
                DELETE FROM tag
                WHERE id=(
                SELECT id FROM tag
                LEFT JOIN tagfile ON tag.id=tagfile.idTag
                WHERE value=? AND idFile IS NULL
                )""")) {
                stDeleteTag.setString(1, tag);
                int nbRowsAffected = stDeleteTag.executeUpdate();
                return nbRowsAffected > 0;
            } catch (SQLException ex) {
                Popup.error("deleteTag(" + tag + ")", ex);
                return false;
            }
        }
    }

}
