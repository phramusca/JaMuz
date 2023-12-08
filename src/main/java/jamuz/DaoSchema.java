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
package jamuz;

import jamuz.utils.DateTime;
import jamuz.utils.Popup;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoSchema {
	
	private final DbConn dbConn;

	/**
	 *
	 * @param dbConn
	 */
	public DaoSchema(DbConn dbConn) {
		this.dbConn = dbConn;
	}
	
	boolean update(int requestedVersion) {
		ArrayList<DbVersion> versions = new ArrayList<>();
		if (!getVersionHistory(versions)) {
			return false;
		}
		if (versions.size() < 1) {
			return updateVersion(0, requestedVersion);
		} else {
			Optional<DbVersion> currentVersion = versions.stream().filter((t) -> {
				return t.getUpgradeEnd().after(new Date(0));
			}).findFirst();
			if (!currentVersion.isPresent()) {
				return updateVersion(0, requestedVersion);
			} else {
				if (currentVersion.get().getVersion() == requestedVersion) {
					return true;
				}
				return updateVersion(currentVersion.get().getVersion(), requestedVersion);
			}
		}
	}

	private boolean updateVersion(int oldVersion, int newVersion) {
		Popup.info("Database requires an upgrade from version " + oldVersion + " to " + newVersion
				+ ".\n\nDo not worry, a backup will be made.\nNo output is expected, just wait a little.\n\nClick OK to proceed.");
		dbConn.info.backupDB(FilenameUtils.getFullPath(dbConn.info.getLocationWork()));
		for (int currentVersion = oldVersion; currentVersion < newVersion; currentVersion++) {
			int nextVersion = currentVersion + 1;
			if (oldVersion != 0 && !insertVersionHistory(nextVersion)) {
				return false;
			}
			if (!updateSchemaToNewVersion(nextVersion)) {
				return false;
			}
			if (oldVersion == 0) {
				if (!insertVersionHistory(nextVersion)) {
					return false;
				}
			}
			if (!updateVersionHistory(nextVersion)) {
				return false;
			}
		}
		return true;
	}

	private synchronized boolean updateSchemaToNewVersion(int newVersion) {
		try {
			File scriptFile = Jamuz.getFile(newVersion + ".sql", "data", "system", "sql");
			String locationWork = this.dbConn.info.getLocationWork();
			ProcessBuilder builder = new ProcessBuilder("sqlite3", locationWork);
			builder.redirectInput(scriptFile);
			Process process = builder.start();
			process.waitFor();
			return true;
		} catch (IOException | InterruptedException ex) {
			Logger.getLogger(DbConnJaMuz.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	private synchronized boolean insertVersionHistory(int version) {
		try {
			PreparedStatement stInsertVersionHistory = dbConn.connection.prepareStatement(
					"INSERT INTO versionHistory (version, upgradeStart) "
					+ "VALUES (?, datetime('now')) "); // NOI18N
			stInsertVersionHistory.setInt(1, version);
			int nbRowsAffected = stInsertVersionHistory.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stInsertVersionHistory, fileInfo={0} # row(s) affected: +{1}",
						new Object[]{version, nbRowsAffected}); // NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("insertVersionHistory(" + version + ")", ex); // NOI18N
			return false;
		}
	}

	private synchronized boolean updateVersionHistory(int version) {
		try {
			PreparedStatement stUpdateVersionHistory = dbConn.connection.prepareStatement(
					"UPDATE versionHistory SET upgradeEnd=datetime('now') "
					+ "WHERE version=?"); // NOI18N
			stUpdateVersionHistory.setInt(1, version);
			int nbRowsAffected = stUpdateVersionHistory.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateVersionHistory, fileInfo={0} # row(s) affected: +{1}",
						new Object[]{version, nbRowsAffected}); // NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateVersionHistory(" + version + ")", ex); // NOI18N
			return false;
		}
	}

	private boolean getVersionHistory(ArrayList<DbVersion> versions) {
		Pair existsTableVersionHistory = existsTableVersionHistory();
		if (!(Boolean) existsTableVersionHistory.getKey()) {
			// Method did not succeeded.
			return false;
		}
		if (!(Boolean) existsTableVersionHistory.getValue()) {
			// Table does not exists.
			versions.add(new DbVersion(0, new Date(0), new Date(0)));
			return true;
		}
		ResultSet rs = null;
		try {
			String sql = """
                SELECT version, upgradeStart, upgradeEnd
                FROM versionHistory
                ORDER BY version DESC
                """;
			PreparedStatement stSelectVersionHistory = dbConn.connection.prepareStatement(sql);
			rs = stSelectVersionHistory.executeQuery();
			while (rs.next()) {
				int version = rs.getInt("version"); // NOI18N
				Date upgradeStart = DateTime.parseSqlUtc(rs.getString("upgradeStart"));
				Date upgradeEnd = DateTime.parseSqlUtc(rs.getString("upgradeEnd"));
				versions.add(new DbVersion(version, upgradeStart, upgradeEnd));
			}
			return true;
		} catch (SQLException ex) {
			Popup.error("getVersionHistory", ex); // NOI18N
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

	private Pair existsTableVersionHistory() {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = dbConn.connection.createStatement();
			rs = st.executeQuery("SELECT COUNT(*) FROM sqlite_master WHERE type='table' AND name='versionHistory';");
			return new ImmutablePair(true, rs.getInt(1) > 0);
		} catch (SQLException ex) {
			Popup.error("existsTableVersionHistory()", ex); // NOI18N
			return new ImmutablePair(false, false);
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
	}
}
