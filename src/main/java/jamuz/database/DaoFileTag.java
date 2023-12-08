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

import jamuz.FileInfo;
import jamuz.Jamuz;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoFileTag {

	private final DbConn dbConn;
	private final DaoTag daoTag;

	/**
	 *
	 * @param dbConn
	 * @param daoTag
	 */
	public DaoFileTag(DbConn dbConn, DaoTag daoTag) {
		this.dbConn = dbConn;
		this.daoTag = daoTag;
	}

	/**
	 *
	 * @param tags
	 * @param idFile
	 * @return
	 */
	public boolean get(ArrayList<String> tags, int idFile) {
		try {
			PreparedStatement stSelectTags = dbConn.connection.prepareStatement(
					"SELECT value FROM tag T JOIN tagFile F ON T.id=F.idTag "
							+ "WHERE F.idFile=?"); // NOI18N
			stSelectTags.setInt(1, idFile);
			ResultSet rs = stSelectTags.executeQuery();
			while (rs.next()) {
				tags.add(dbConn.getStringValue(rs, "value"));
			}
			return true;
		} catch (SQLException ex) {
			Popup.error("getTags(" + idFile + ")", ex); // NOI18N
			return false;
		}
	}
	
	public synchronized boolean updateModifDate(String newTag) {
		try {
			String sql = """
                UPDATE file SET tagsModifDate=datetime('now') WHERE idFile=(SELECT TF.idFile
                FROM tag T
                JOIN tagfile TF ON TF.idTag=T.id
                WHERE T.value=?)""";
			PreparedStatement stUpdateTagsModifDate = dbConn.getConnection().prepareStatement(sql); // NOI18N
			stUpdateTagsModifDate.setString(1, newTag);
			int nbRowsAffected = stUpdateTagsModifDate.executeUpdate();
			if (nbRowsAffected >= 0) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateTagsModifDate, "
						+ "newTag={0} # row(s) affected: +{1}",
						new Object[]{newTag, nbRowsAffected}); // NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateTagsModifDate(" + newTag + ")", ex); // NOI18N
			return false;
		}
	}

	/**
	 * Update tagsModifDate
	 *
	 * @param fileInfo
	 * @return
	 */
	public synchronized boolean updateModifDate(FileInfo fileInfo) {
		try {
			PreparedStatement stUpdateTagsModifDate = dbConn.getConnection().prepareStatement(
					"UPDATE file SET tagsModifDate=datetime('now') "
					+ "WHERE idFile=?"); // NOI18N
			stUpdateTagsModifDate.setInt(1, fileInfo.getIdFile());
			int nbRowsAffected = stUpdateTagsModifDate.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateTagsModifDate, "
						+ "fileInfo={0} # row(s) affected: +{1}",
						new Object[]{fileInfo.toString(), nbRowsAffected}); // NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateTagsModifDate(" + fileInfo.toString() + ")", ex); // NOI18N
			return false;
		}
	}
	
	/**
	 *
	 * @param files
	 * @param results
	 * @return
	 */
	public synchronized int[] update(ArrayList<? extends FileInfo> files, int[] results) {
		int i = 0;
		for (FileInfo fileInfo : files) {
			if (fileInfo.getTags() != null) {
				// FIXME Z MERGE Update tags and date in the same transaction
				// so it can be rolled back and probably faster
				if (!update(fileInfo.getTags(), fileInfo.getIdFile())) {
					if (results != null) {
						results[i] = 0;
					}
				}
				if (!updateModifDate(fileInfo)) {
					if (results != null) {
						results[i] = 0;
					}
				}
			}
			i++;
		}
		return results;
	}

	private synchronized boolean update(ArrayList<String> tags, int idFile) {
		if (!delete(idFile)) {
			return false;
		}
		return insert(tags, idFile);
	}

	private synchronized boolean insert(ArrayList<String> tags, int idFile) {
		try {
			if (!tags.isEmpty()) {
				dbConn.getConnection().setAutoCommit(false);
				int[] results;
				PreparedStatement stInsertTagFile = dbConn.getConnection()
						.prepareStatement(
								"INSERT OR IGNORE INTO tagFile "
										+ "(idFile, idTag) " // NOI18N
										+ "VALUES (?, (SELECT id FROM tag WHERE value=?))"); // NOI18N
				for (String tag : tags) {
					if (daoTag.insertIfMissing(tag)) { // TODO: get id instead of using tag name
						stInsertTagFile.setInt(1, idFile);
						stInsertTagFile.setString(2, tag);
						stInsertTagFile.addBatch();
					}
				}
				long startTime = System.currentTimeMillis();
				results = stInsertTagFile.executeBatch();
				dbConn.getConnection().commit();
				long endTime = System.currentTimeMillis();
				Jamuz.getLogger().log(Level.FINEST, "insertTagFiles UPDATE // {0} "
						+ "// Total execution time: {1}ms",
						new Object[] { results.length, endTime - startTime }); // NOI18N

				// Analyse results
				int result;
				for (int i = 0; i < results.length; i++) {
					result = results[i];
					if (result < 0) {
						Jamuz.getLogger().log(Level.SEVERE, "insertTagFiles, "
								+ "idFile={0} result={2}",
								new Object[] { idFile, result }); // NOI18N
					}
				}
				dbConn.getConnection().setAutoCommit(true);
			}
			return true;
		} catch (SQLException ex) {
			Popup.error("insertTagFiles(" + idFile + ")", ex); // NOI18N
			return false;
		}
	}

	private synchronized boolean delete(int idFile) {
		try {
			PreparedStatement stDeleteTagFiles = dbConn.getConnection()
					.prepareStatement(
							"DELETE FROM tagFile WHERE idFile=?"); // NOI18N
			stDeleteTagFiles.setInt(1, idFile);
			long startTime = System.currentTimeMillis();
			int result = stDeleteTagFiles.executeUpdate();
			long endTime = System.currentTimeMillis();
			Jamuz.getLogger().log(Level.FINEST, "stDeleteTagFiles DELETE "
					+ "// Total execution time: {0}ms",
					new Object[] { endTime - startTime }); // NOI18N

			if (result < 0) {
				Jamuz.getLogger().log(Level.SEVERE, "stDeleteTagFiles, "
						+ "idFile={0}, result={1}",
						new Object[] { idFile, result }); // NOI18N
			}

			return true;

		} catch (SQLException ex) {
			Popup.error("deleteTagFiles()", ex); // NOI18N
			return false;
		}
	}
}