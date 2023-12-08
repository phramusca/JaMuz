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

import jamuz.Jamuz;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoTag {
	private final DbConn dbConn;

	/**
	 *
	 * @param dbConn
	 */
	public DaoTag(DbConn dbConn) {
        this.dbConn = dbConn;
    }
	
	/**
	 * Inserts a tag
	 *
	 * @param tag
	 * @return
	 */
	public synchronized boolean insert(String tag) {
		try {
			PreparedStatement stInsertTag = dbConn.connection.prepareStatement(
					"INSERT OR IGNORE INTO tag (value) VALUES (?) "); // NOI18N
			stInsertTag.setString(1, tag);
			int nbRowsAffected = stInsertTag.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stInsertTag, tag=\"{0}\" "
						+ "# row(s) affected: +{1}", new Object[] { tag, nbRowsAffected }); // NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("insertTag(" + tag + ")", ex); // NOI18N
			return false;
		}
	}
	
	public boolean insertIfMissing(String tag) {
		ResultSet rs = null;
		ResultSet keys = null;
		try {
			PreparedStatement stSelectMachine = dbConn.getConnection().prepareStatement(
					"SELECT COUNT(*) FROM tag WHERE value=?"); // NOI18N
			stSelectMachine.setString(1, tag);
			rs = stSelectMachine.executeQuery();
			if (rs.getInt(1) > 0) {
				return true;
			} else {
				return insert(tag);
			}
		} catch (SQLException ex) {
			Popup.error("isTag(" + tag + ")", ex); // NOI18N
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				Jamuz.getLogger().warning("Failed to close ResultSet");
			}

			try {
				if (keys != null) {
					keys.close();
				}
			} catch (SQLException ex) {
				Jamuz.getLogger().warning("Failed to close ResultSet");
			}
		}
	}

	/**
	 * Get list of tags
	 *
	 * @return
	 */
	public ArrayList<String> get() {
		Statement st = null;
		ResultSet rs = null;
		ArrayList<String> tags = new ArrayList<>();
		try {
			st = dbConn.connection.createStatement();
			rs = st.executeQuery("SELECT id, value FROM tag ORDER BY value");
			while (rs.next()) {
				tags.add(dbConn.getStringValue(rs, "value"));
			}
		} catch (SQLException ex) {
			Popup.error("getTags()", ex); // NOI18N
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				Jamuz.getLogger().warning("Failed to close ResultSet");
			}
			try {
				if (st != null) {
					st.close();
				}
			} catch (SQLException ex) {
				Jamuz.getLogger().warning("Failed to close Statement");
			}
		}
		return tags;
	}
	
	/**
	 * Updates tag in tag table
	 *
	 * @param oldTag
	 * @param newTag
	 * @return
	 */
	public synchronized boolean update(String oldTag, String newTag) {
		try {
			PreparedStatement stUpdateTag = dbConn.connection.prepareStatement(
					"UPDATE tag SET value=? WHERE value=?"); // NOI18N
			stUpdateTag.setString(1, newTag);
			stUpdateTag.setString(2, oldTag);
			int nbRowsAffected = stUpdateTag.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateTag, oldTag={0}, "
						+ "newTag={1} # row(s) affected: +{2}",
						new Object[] { oldTag, newTag, nbRowsAffected }); // NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateTag(" + oldTag + ", " + newTag + ")", ex); // NOI18N
			return false;
		}
	}
	
	/**
	 * Deletes tag from tag table
	 *
	 * @param tag
	 * @return
	 */
	public synchronized boolean delete(String tag) {
		try {
			String sql = """
                DELETE FROM tag
                WHERE id=(
                SELECT id FROM tag
                LEFT JOIN tagfile ON tag.id=tagfile.idTag
                WHERE value=? AND idFile IS NULL
                )""";
			PreparedStatement stDeleteTag = dbConn.connection.prepareStatement(sql);
			stDeleteTag.setString(1, tag);
			int nbRowsAffected = stDeleteTag.executeUpdate();
			return nbRowsAffected > 0;
		} catch (SQLException ex) {
			Popup.error("deleteTag(" + tag + ")", ex); // NOI18N
			return false;
		}
	}
}
