/*
 * Copyright (C) 2011 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import jamuz.Playlist.Field;
import jamuz.Playlist.Filter;
import jamuz.Playlist.Operator;
import jamuz.Playlist.Order;
import jamuz.gui.swing.ListElement;
import jamuz.process.check.DuplicateInfo;
import jamuz.process.check.FolderInfo;
import jamuz.process.check.FolderInfo.CheckedFlag;
import jamuz.process.check.FolderInfoResult;
import jamuz.process.check.ReplayGain.GainValues;
import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import jamuz.remote.ClientInfo;
import jamuz.utils.DateTime;
import jamuz.utils.Inter;
import jamuz.utils.OS;
import jamuz.utils.Popup;
import jamuz.utils.StringManager;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Creates a new dbConn.connection to JaMuz database
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DbConnJaMuz extends StatSourceSQL {

	//TODO DB: How to log SQL generated queries ?
	//http://code.google.com/p/log4jdbc/
	//FIXME ZZZZZZ REVIEW code review database methods:
	//Check that nbRowsAffected is properly checked (==1 or >0 depending)
	//Check that all functions return a boolean and that this one is used
	//Check that batches are used whenever possible and needed
	//http://stackoverflow.com/questions/2467125/reusing-a-preparedstatement-multiple-times
	//Check that try/catch/return are all OK
	// - popup if exception (as it should not happen)
	//FIXME ZZZZZZ REVIEW: Internationalization
	// ( !! Test at each step !! )
	// 1 - Use BundleScanner to remove uneeded entries
	// 2 - Use NetBeans editor to merge duplicate entries if applicable
	// 3 - Use NetBeans internationalization tool
	// 4 - Run and manually find missing translations (en / fre)
	// <editor-fold defaultstate="collapsed" desc="Setup">
	private PreparedStatement stSelectFilesStats4Source;
	private PreparedStatement stSelectFilesStats4SourceAndDevice;

	/**
	 * Creates a database dbConn.connection.
	 *
	 * @param dbInfo
	 */
	public DbConnJaMuz(DbInfo dbInfo) {
		super(dbInfo, "JaMuz", "", true, true, true, true, false, true);
	}

	/**
	 * Prepare the predefined SQL statements
	 *
	 * @param isRemote
	 * @return
	 */
	@Override
	public boolean setUp(boolean isRemote) {
		try {
			String fullPath;
			if(isRemote) {
				// We do not care to get original fullPath when merging with remote
				// Moreover it avoids an issue writing metadata, that is still present for other merge types, see below
				fullPath = "(P.strPath || F.name)";
			} else {
				this.dbConn.connect();
				//TODO: Pb writing metadata after a change of fullPath (for instance when mp3 replaced by flac)
				// => Include both original path (for merge comparison) and current path on JaMuz side (for file metadata updates)
				// => No need to have isRemote in setUp()
				fullPath = "D.oriRelativeFullPath";
			}
			
			stSelectFilesStats4SourceAndDevice = dbConn.getConnnection().prepareStatement(
					"SELECT "
					+ "F.idFile, F.idPath, " + fullPath + " AS fullPath, "
					+ "F.rating, F.lastplayed, F.addedDate, F.playCounter, F.BPM, " //NOI18N
					+ "C.playcounter AS previousPlayCounter, " //NOI18N
					+ "F.ratingModifDate, F.tagsModifDate, F.genre, F.genreModifDate  " //NOI18N
					+ "FROM file F "
					+ "JOIN path P ON F.idPath=P.idPath " //NOI18N
					+ "JOIN devicefile D ON D.idFile=F.idFile "
					+ "LEFT OUTER JOIN (SELECT * FROM playcounter WHERE idStatSource=?) C "
					+ "ON F.idFile=C.idFile " //NOI18N //NOI18N
					+ "WHERE D.idDevice=? AND D.status!='INFO'");
			stSelectFilesStats4Source = dbConn.getConnnection().prepareStatement(
					"SELECT "
					+ "F.idFile, F.idPath, (P.strPath || F.name) AS fullPath, "
					+ "F.rating, F.lastplayed, F.addedDate, F.playCounter, F.BPM, " //NOI18N
					+ "C.playcounter AS previousPlayCounter, " //NOI18N
					+ "F.ratingModifDate, F.tagsModifDate, F.genre, F.genreModifDate " //NOI18N
					+ "FROM file F "
					+ "JOIN path P ON F.idPath=P.idPath " //NOI18N
					+ "LEFT OUTER JOIN (SELECT * FROM playcounter WHERE idStatSource=?) C "
					+ "ON F.idFile=C.idFile ");

			this.stSelectFileStatistics = this.stSelectFilesStats4Source; //by default, but not to be called directly 

			this.stUpdateFileStatistics = dbConn.connection.prepareStatement(
					"UPDATE file "
					+ "SET rating=?, bpm=?, lastplayed=?, addedDate=?, "
					+ "playCounter=?, ratingModifDate=?, genreModifDate=?, genre=? "
					+ "WHERE idFile=?");

			return true;
		} catch (SQLException ex) {
			//Proper error handling. We should not have such an error unless above code changes
			Popup.error("setUp", ex);   //NOI18N
			return false;
		}
	}

	/**
	 *
	 * @return
	 */
	@Override
	public boolean tearDown() {
//        this.dbConn.disconnect();
		//Never disconnecting from application database, no need (really ?)
		return true;
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Genre">
	/**
	 * Updates genre in genre table
	 *
	 * @param oldGenre
	 * @param newGenre
	 * @return
	 */
	public synchronized boolean updateGenre(String oldGenre, String newGenre) {
		try {
			PreparedStatement stUpdateGenre = dbConn.connection.prepareStatement(
					"UPDATE genre SET value=? WHERE value=?");   //NOI18N
			stUpdateGenre.setString(1, newGenre);
			stUpdateGenre.setString(2, oldGenre);
			int nbRowsAffected = stUpdateGenre.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateGenre, oldGenre={0}, "
						+ "newGenre={1} # row(s) affected: +{2}",
						new Object[]{oldGenre, newGenre, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateGenre(" + oldGenre + ", " + newGenre + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Deletes genre from genre table
	 *
	 * @param genre
	 * @return
	 */
	public synchronized boolean deleteGenre(String genre) {
		try {
			PreparedStatement stDeleteGenre = dbConn.connection.prepareStatement(
					"DELETE FROM genre WHERE value=?");   //NOI18N
			stDeleteGenre.setString(1, genre);
			int nbRowsAffected = stDeleteGenre.executeUpdate();
			if (nbRowsAffected > 0) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stDeleteGenre, "
						+ "genre={0} # row(s) affected: +{1}",
						new Object[]{genre, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("deleteGenre(" + genre + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Inserts a genre
	 *
	 * @param genre
	 * @return
	 */
	public synchronized boolean insertGenre(String genre) {
		try {
			if (checkGenre(genre)) {
				return false;
			}
			PreparedStatement stInsertGenre = dbConn.connection.prepareStatement(
					"INSERT INTO genre (value) VALUES (?)");   //NOI18N
			stInsertGenre.setString(1, genre);
			int nbRowsAffected = stInsertGenre.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stInsertGenre, genre=\"{0}\" "
						+ "# row(s) affected: +{1}", new Object[]{genre, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Jamuz.getLogger().log(Level.SEVERE, "stInsertGenre, genre=\"{0}\" "
					+ "Exception: {1}", new Object[]{genre, ex.toString()});   //NOI18N
			return false;
		}
	}

	/**
	 * Update genre
	 *
	 * @param fileInfo
	 * @return
	 */
	public synchronized boolean updateFileGenre(FileInfoInt fileInfo) {
		try {
			PreparedStatement stUpdateFileGenre = dbConn.connection.prepareStatement(
					"UPDATE file set genre=?, "
					+ "genreModifDate=datetime('now') "
					+ "WHERE idFile=?");  //NOI18N
			stUpdateFileGenre.setString(1, fileInfo.genre);
			stUpdateFileGenre.setInt(2, fileInfo.idFile);
			int nbRowsAffected = stUpdateFileGenre.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateFileGenre, "
						+ "fileInfo={0} # row(s) affected: +{1}",
						new Object[]{fileInfo.toString(), nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateGenre(" + fileInfo.toString() + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Checks if genre is in supported list
	 *
	 * @param genre
	 * @return
	 */
	public boolean checkGenre(String genre) {
		ResultSet rs = null;
		try {
			PreparedStatement stCheckGenre = dbConn.connection.prepareStatement(
					"SELECT COUNT(*) FROM genre WHERE value=?");   //NOI18N
			stCheckGenre.setString(1, genre);
			rs = stCheckGenre.executeQuery();
			return rs.getInt(1) > 0;
		} catch (SQLException ex) {
			Popup.error("checkGenre(" + genre + ")", ex);   //NOI18N
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
	 *
	 * @param myListModel
	 */
	public void getGenreListModel(DefaultListModel myListModel) {
		getListModel(myListModel, "SELECT value FROM genre ORDER BY value", "value");
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Tag">
	/**
	 * Updates tag in tag table
	 *
	 * @param oldTag
	 * @param newTag
	 * @return
	 */
	public synchronized boolean updateTag(String oldTag, String newTag) {
		try {
			PreparedStatement stUpdateTag = dbConn.connection.prepareStatement(
					"UPDATE tag SET value=? WHERE value=?");   //NOI18N
			stUpdateTag.setString(1, newTag);
			stUpdateTag.setString(2, oldTag);
			int nbRowsAffected = stUpdateTag.executeUpdate();
			if (nbRowsAffected == 1) {
				return updateFileTagsModifDate(newTag);
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateTag, oldTag={0}, "
						+ "newTag={1} # row(s) affected: +{2}",
						new Object[]{oldTag, newTag, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateTag(" + oldTag + ", " + newTag + ")", ex);   //NOI18N
			return false;
		}
	}

	private synchronized boolean updateFileTagsModifDate(String newTag) {
		try {
			PreparedStatement stUpdateTagsModifDate = dbConn.getConnnection().prepareStatement(
					"UPDATE file SET tagsModifDate=datetime('now') "
					+ "WHERE idFile=(SELECT TF.idFile\n"
					+ "FROM tag T \n"
					+ "JOIN tagfile TF ON TF.idTag=T.id \n"
					+ "WHERE T.value=?)");  //NOI18N
			stUpdateTagsModifDate.setString(1, newTag);
			int nbRowsAffected = stUpdateTagsModifDate.executeUpdate();
			if (nbRowsAffected >= 0) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateTagsModifDate, "
						+ "newTag={0} # row(s) affected: +{1}",
						new Object[]{newTag, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateTagsModifDate(" + newTag + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Inserts a tag
	 *
	 * @param tag
	 * @return
	 */
	public synchronized boolean insertTag(String tag) {
		try {
			PreparedStatement stInsertTag = dbConn.connection.prepareStatement(
					"INSERT OR IGNORE INTO tag (value) VALUES (?) ");   //NOI18N
			stInsertTag.setString(1, tag);
			int nbRowsAffected = stInsertTag.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stInsertTag, tag=\"{0}\" "
						+ "# row(s) affected: +{1}", new Object[]{tag, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("insertTag(" + tag + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Deletes tag from tag table
	 *
	 * @param tag
	 * @return
	 */
	public synchronized boolean deleteTag(String tag) {
		try {
			PreparedStatement stDeleteTag = dbConn.connection.prepareStatement(
					"DELETE FROM tag \n"
					+ "WHERE id=(\n"
					+ "SELECT id FROM tag \n"
					+ "LEFT JOIN tagfile ON tag.id=tagfile.idTag \n"
					+ "WHERE value=? AND idFile IS NULL\n"
					+ ")");
			stDeleteTag.setString(1, tag);
			int nbRowsAffected = stDeleteTag.executeUpdate();
			return nbRowsAffected > 0;
		} catch (SQLException ex) {
			Popup.error("deleteTag(" + tag + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Get list of tags
	 *
	 * @return
	 */
	public ArrayList<String> getTags() {
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
			Popup.error("getTags()", ex);   //NOI18N
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
	 *
	 * @param myListModel
	 */
	public void getTagListModel(DefaultListModel myListModel) {
		getListModel(myListModel, "SELECT value FROM tag ORDER BY value", "value");
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Machine">
	/**
	 * Updates genre in genre table
	 *
	 * @param idMachine
	 * @param description
	 * @return
	 */
	public synchronized boolean updateMachine(int idMachine, String description) {
		try {
			PreparedStatement stUpdateMachine = dbConn.connection.prepareStatement(
					"UPDATE machine SET description=? WHERE idMachine=?");   //NOI18N
			stUpdateMachine.setString(1, description);
			stUpdateMachine.setInt(2, idMachine);
			int nbRowsAffected = stUpdateMachine.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateMachine, idMachine={0}, "
						+ "description={1} # row(s) affected: +{2}",
						new Object[]{idMachine, description, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateGenre(" + idMachine + ", \"" + description + "\")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Deletes a machine
	 *
	 * @param machineName
	 * @return
	 */
	public synchronized boolean deleteMachine(String machineName) {
		try {
			PreparedStatement stDeleteMachine = dbConn.connection.prepareStatement("DELETE FROM machine WHERE name=?");  //NOI18N
			stDeleteMachine.setString(1, machineName);
			int nbRowsAffected = stDeleteMachine.executeUpdate();
			if (nbRowsAffected > 0) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stDeleteMachine(\"{0}\"). # row(s) affected: {1}", new Object[]{machineName, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("deleteMachine(" + machineName + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Fill option list
	 *
	 * @param myListModel
	 */
	public void getMachineListModel(DefaultListModel myListModel) {
		getListModel(myListModel,
				"SELECT name, description "
				+ "FROM machine "
				+ "WHERE hidden=0 "
				+ "ORDER BY name", "name");
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Option">
	/**
	 *
	 * @param selOptions
	 * @return
	 */
	public synchronized boolean updateOptions(Machine selOptions) {
		try {
			dbConn.connection.setAutoCommit(false);

			PreparedStatement stUpdateOptions = dbConn.connection.prepareStatement(
					"UPDATE option SET value=? "
					+ "WHERE idMachine=? AND idOptionType=?");     //NOI18N    

			for (Option option : selOptions.getOptions()) {
				if (option.getType().equals("path")
						&& !option.getValue().isBlank()) {   //NOI18N
					option.setValue(FilenameUtils.normalizeNoEndSeparator(option.getValue().trim())
							+ File.separator);
				}

				stUpdateOptions.setString(1, option.getValue());
				stUpdateOptions.setInt(2, option.getIdMachine());
				stUpdateOptions.setInt(3, option.getIdOptionType());

				stUpdateOptions.addBatch();
			}
			long startTime = System.currentTimeMillis();
			int[] results = stUpdateOptions.executeBatch();
			dbConn.connection.commit();
			long endTime = System.currentTimeMillis();
			Jamuz.getLogger().log(Level.FINEST, "setOptions // {0} // Total execution time: {1}ms",
					new Object[]{results.length, endTime - startTime});   //NOI18N
			//Check results
			int result;
			for (int i = 0; i < results.length; i++) {
				result = results[i];
				if (result != 1) {
					return false;
				}
			}
			dbConn.connection.setAutoCommit(true);
			return true;
		} catch (SQLException ex) {
			Popup.error(ex);
			return false;
		}
	}

	/**
	 * Set option value (update)
	 *
	 * @param myOption
	 * @param value
	 * @return
	 */
	public synchronized boolean updateOption(Option myOption, String value) {
		try {
			if (myOption.getType().equals("path")) {   //NOI18N
				value = FilenameUtils.normalizeNoEndSeparator(value.trim()) + File.separator;
			}
			PreparedStatement stUpdateOption = dbConn.connection.prepareStatement("UPDATE option SET value=? "
					+ "WHERE idMachine=? AND idOptionType=?");     //NOI18N    
			stUpdateOption.setString(1, value);
			stUpdateOption.setInt(2, myOption.getIdMachine());
			stUpdateOption.setInt(3, myOption.getIdOptionType());

			int nbRowsAffected = stUpdateOption.executeUpdate();
			if (nbRowsAffected > 0) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateOption, value={0}, idMachine={1}, "
						+ "idMachidOptionTypeine={2} # row(s) affected: +{3}", //NOI18N
						new Object[]{value, myOption.getIdMachine(), myOption.getIdOptionType(), nbRowsAffected});
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("setOption(" + myOption.toString() + "," + value + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Get options for given machine
	 *
	 * @param myOptions
	 * @param machineName
	 * @return
	 */
	public boolean getOptions(ArrayList<Option> myOptions, String machineName) {
		ResultSet rs = null;
		try {
			PreparedStatement stSelectOptions = dbConn.connection.prepareStatement(
					"SELECT O.idMachine, OT.name, O.value, O.idOptionType, OT.type "
					+ "FROM option O, optiontype OT, machine M " //NOI18N
					+ "WHERE O.idMachine=M.idMachine "
					+ "AND O.idOptionType=OT.idOptionType "
					+ "AND M.name=?");
			stSelectOptions.setString(1, machineName);
			rs = stSelectOptions.executeQuery();
			while (rs.next()) {
				myOptions.add(new Option(
						dbConn.getStringValue(rs, "name"),
						dbConn.getStringValue(rs, "value"),
						rs.getInt("idMachine"),
						rs.getInt("idOptionType"),
						dbConn.getStringValue(rs, "type")));   //NOI18N
			}

			if (myOptions.size() <= 0) {
				Popup.warning(Inter.get("Error.NoOption") + " \"" + machineName + "\".");   //NOI18N //NOI18N
				return false;
			}

			return true;
		} catch (SQLException ex) {
			Popup.error("getOptions(\"" + machineName + "\")", ex);   //NOI18N
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

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Machine & Option">
	/**
	 * Check if given machine is listed or insert with default options if not
	 * yet listed.
	 *
	 * @param hostname
	 * @param description
	 * @param hidden
	 * @return
	 */
	public boolean isMachine(String hostname, StringBuilder description, boolean hidden) {
		ResultSet rs = null;
		ResultSet keys = null;
		try {
			PreparedStatement stSelectMachine = dbConn.connection.prepareStatement(
					"SELECT COUNT(*), description FROM machine "
					+ "WHERE name=?");   //NOI18N
			stSelectMachine.setString(1, hostname);
			rs = stSelectMachine.executeQuery();
			if (rs.getInt(1) > 0) {
				description.append(dbConn.getStringValue(rs, "description"));
				return true;
			} else {
				//Insert a new machine
				PreparedStatement stInsertMachine = dbConn.connection.prepareStatement(
						"INSERT INTO machine (name, hidden) VALUES (?, ?)");   //NOI18N
				stInsertMachine.setString(1, hostname);
				stInsertMachine.setBoolean(2, hidden);
				int nbRowsAffected = stInsertMachine.executeUpdate();
				if (nbRowsAffected == 1) {
					keys = stInsertMachine.getGeneratedKeys();
					keys.next();
					int idMachine = keys.getInt(1);
					rs.close();
					//Insert default options
					PreparedStatement stSelectOptionType = dbConn.connection.prepareStatement(
							"SELECT idOptionType, name, `default` "
							+ "FROM optiontype");     //NOI18N
					rs = stSelectOptionType.executeQuery();
					PreparedStatement stInsertOption = dbConn.connection.prepareStatement(
							"INSERT INTO option ('idMachine', 'idOptionType', "
							+ "'value') VALUES (?, ?, ?)");     //NOI18N
					while (rs.next()) {
						stInsertOption.setInt(1, idMachine);
						stInsertOption.setInt(2, rs.getInt("idOptionType"));   //NOI18N
						stInsertOption.setString(3, dbConn.getStringValue(rs, "default"));   //NOI18N
						nbRowsAffected = stInsertOption.executeUpdate();
						if (nbRowsAffected != 1) {
							Jamuz.getLogger().log(Level.SEVERE, "stInsertOption, "
									+ "idMachine={0}, idOptionType={1}, default=\"{2}\" "
									+ "# row(s) affected: +{1}",
									new Object[]{idMachine, rs.getInt("idOptionType"),
										dbConn.getStringValue(rs, "default"), nbRowsAffected});   //NOI18N
							return false;
						}
					}
					return true;
				} else {
					Jamuz.getLogger().log(Level.SEVERE, "stInsertMachine, "
							+ "hostname=\"{0}\" # row(s) affected: +{1}",
							new Object[]{hostname, nbRowsAffected});   //NOI18N
					return false;
				}
			}
		} catch (SQLException ex) {
			Popup.error("isMachine(" + hostname + ")", ex);   //NOI18N
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

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Playlist">
	/**
	 * Updates a playlist
	 *
	 * @param playlist
	 * @return
	 */
	public synchronized boolean updatePlaylist(Playlist playlist) {
		try {
			PreparedStatement stUpdatePlaylist = dbConn.connection.prepareStatement(
					"UPDATE playlist "
					+ "SET limitDo=?, limitValue=?, limitUnit=?, random=?, "
					+ "type=?, match=?, name=?, hidden=?, destExt=? " //NOI18N
					+ "WHERE idPlaylist=?");  //NOI18N
			stUpdatePlaylist.setBoolean(1, playlist.isLimit());
			stUpdatePlaylist.setDouble(2, playlist.getLimitValue());
			stUpdatePlaylist.setString(3, playlist.getLimitUnit().name());
			stUpdatePlaylist.setBoolean(4, playlist.isRandom());
			stUpdatePlaylist.setString(5, playlist.getType().name());
			stUpdatePlaylist.setString(6, playlist.getMatch().name());
			stUpdatePlaylist.setString(7, playlist.getName());
			stUpdatePlaylist.setBoolean(8, playlist.isHidden());
			stUpdatePlaylist.setString(9, playlist.getDestExt());
			stUpdatePlaylist.setInt(10, playlist.getId());
			int nbRowsAffected = stUpdatePlaylist.executeUpdate();
			if (nbRowsAffected == 1) {
				PreparedStatement stDeletePlaylistFilters = dbConn.connection.
						prepareStatement("DELETE FROM playlistFilter WHERE idPlaylist=?");  //NOI18N
				//Delete all filters for this playlist (before inserting new ones)
				stDeletePlaylistFilters.setInt(1, playlist.getId());
				stDeletePlaylistFilters.executeUpdate(); //Can have no filter, not checking numberOfRowsAffected

				PreparedStatement stDeletePlaylistOrders = dbConn.connection.
						prepareStatement("DELETE FROM playlistOrder WHERE idPlaylist=?");  //NOI18N
				stDeletePlaylistOrders.setInt(1, playlist.getId());
				stDeletePlaylistOrders.executeUpdate(); //Can have no order, not checking numberOfRowsAffected
				dbConn.connection.setAutoCommit(false);
				PreparedStatement stInsertPlaylistFilter = dbConn.connection.
						prepareStatement("INSERT INTO playlistFilter "
								+ "(field, operator, value, idPlaylist) " //NOI18N
								+ "VALUES (?, ?, ?, ?)");  //NOI18N
				for (Filter filter : playlist.getFilters()) {
					stInsertPlaylistFilter.setString(1, filter.getFieldName());
					stInsertPlaylistFilter.setString(2, filter.getOperatorName());
					stInsertPlaylistFilter.setString(3, filter.getValue());
					stInsertPlaylistFilter.setInt(4, playlist.getId());
					stInsertPlaylistFilter.addBatch();
				}
				long startTime = System.currentTimeMillis();
				int[] results = stInsertPlaylistFilter.executeBatch();
				dbConn.connection.commit();
				long endTime = System.currentTimeMillis();
				Jamuz.getLogger().log(Level.FINEST, "stInsertPlaylistFilter // {0} "
						+ "// Total execution time: {1}ms",
						new Object[]{results.length, endTime - startTime});    //NOI18N
				//Check results
				for (int i = 0; i < results.length; i++) {
					if (results[i] != 1) {
						return false;
					}
				}
				PreparedStatement stInsertPlaylistOrder = dbConn.connection.
						prepareStatement("INSERT INTO playlistOrder "
								+ "(desc, field, idPlaylist) " //NOI18N
								+ "VALUES (?, ?, ?)");  //NOI18N
				for (Order order : playlist.getOrders()) {
					stInsertPlaylistOrder.setBoolean(1, order.isDesc());
					stInsertPlaylistOrder.setString(2, order.getFieldName());
					stInsertPlaylistOrder.setInt(3, playlist.getId());
					stInsertPlaylistOrder.addBatch();
				}
				startTime = System.currentTimeMillis();
				results = stInsertPlaylistOrder.executeBatch();
				dbConn.connection.commit();
				endTime = System.currentTimeMillis();
				Jamuz.getLogger().log(Level.FINEST, "stInsertPlaylistOrder // {0} "
						+ "// Total execution time: {1}ms",
						new Object[]{results.length, endTime - startTime});    //NOI18N
				//Check results
				for (int i = 0; i < results.length; i++) {
					if (results[i] != 1) {
						return false;
					}
				}
				//TODO DB TEST: Remove the line below if rollback and finally tests are successfull
				dbConn.connection.setAutoCommit(true);
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdatePlaylist, playlist={0}, "
						+ "# row(s) affected: +{1}", new Object[]{playlist.toString(),
							nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updatePlaylist(" + playlist.toString() + ")", ex);   //NOI18N
			//TODO DB TEST the rollback block below and apply to other if successfull
//			try {
//				dbConn.connection.rollback();
//			} catch (SQLException ex1) {
//				Jamuz.getLogger().log(Level.SEVERE, null, ex1);
//			}
			return false;
		}
		//TODO DB TEST the finally block below and apply to other if successfull
//		finally {
//			try {
//				dbConn.connection.setAutoCommit(true);
//			} catch (SQLException ex1) {
//				Jamuz.getLogger().log(Level.SEVERE, null, ex1);
//			}
//		}
	}

	/**
	 * Inserts an empty playlist
	 *
	 * @param playlist
	 * @return
	 */
	public synchronized boolean insertPlaylist(Playlist playlist) {
		try {
			PreparedStatement stInsertPlaylist = dbConn.connection.prepareStatement(
					"INSERT INTO playlist "
					+ "(name, limitDo, limitValue, limitUnit, type, match, random, hidden, destExt) " //NOI18N
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");   //NOI18N
			stInsertPlaylist.setString(1, playlist.getName());
			stInsertPlaylist.setBoolean(2, playlist.isLimit());
			stInsertPlaylist.setInt(3, playlist.getLimitValue());
			stInsertPlaylist.setString(4, playlist.getLimitUnit().name());
			stInsertPlaylist.setString(5, playlist.getType().name());
			stInsertPlaylist.setString(6, playlist.getMatch().name());
			stInsertPlaylist.setBoolean(7, playlist.isRandom());
			stInsertPlaylist.setBoolean(8, playlist.isHidden());
			stInsertPlaylist.setString(9, playlist.getDestExt());

			int nbRowsAffected = stInsertPlaylist.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stInsertPlaylist, playlist=\"{0}\" "
						+ "# row(s) affected: +{1}",
						new Object[]{playlist, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("insertPlaylist(" + playlist + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Deletes a playlist
	 *
	 * @param id
	 * @return
	 */
	public synchronized boolean deletePlaylist(int id) {
		try {
			PreparedStatement stDeletePlaylist = dbConn.connection.prepareStatement("DELETE FROM playlist "
					+ "WHERE idPlaylist=? " //NOI18N
					+ "AND idPlaylist NOT IN (SELECT idPlaylist FROM device WHERE idPlaylist IS NOT NULL) "
					+ "AND idPlaylist NOT IN (SELECT value FROM playlistFilter WHERE field='PLAYLIST')");    //NOI18N

			stDeletePlaylist.setInt(1, id);
			int nbRowsAffected = stDeletePlaylist.executeUpdate();
			if (nbRowsAffected > 0) {
				return true;
			} else {
				Popup.warning("Playlist is linked to a sync device or another playlist so cannot delete it.");  //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("deletePlaylist(" + id + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Get list of playlists
	 *
	 * @param playlists
	 * @return
	 */
	public boolean getPlaylists(HashMap<Integer, Playlist> playlists) {
		try {
			PreparedStatement stSelectPlaylists = dbConn.connection.prepareStatement("SELECT idPlaylist, name, limitDo, "
					+ "limitValue, limitUnit, random, hidden, type, match, destExt FROM playlist");    //NOI18N
			ResultSet rs = stSelectPlaylists.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("idPlaylist");  //NOI18N
				String playlistName = dbConn.getStringValue(rs, "name");  //NOI18N
				boolean limit = rs.getBoolean("limitDo");  //NOI18N
				int limitValue = rs.getInt("limitValue");  //NOI18N
				Playlist.LimitUnit limitUnit = Playlist.LimitUnit.valueOf(dbConn.getStringValue(rs, "limitUnit"));  //NOI18N
				boolean random = rs.getBoolean("random");  //NOI18N
				boolean hidden = rs.getBoolean("hidden");
				String destExt = rs.getString("destExt");
				Playlist.Type type = Playlist.Type.valueOf(dbConn.getStringValue(rs, "type"));  //NOI18N
				Playlist.Match match = Playlist.Match.valueOf(dbConn.getStringValue(rs, "match"));  //NOI18N
				Playlist playlist = new Playlist(id, playlistName, limit, limitValue, limitUnit, random, type, match, hidden, destExt);

				//Get the filters
				PreparedStatement stSelectPlaylistFilters = dbConn.connection.prepareStatement("SELECT idPlaylistFilter, field, operator, value "
						+ "FROM playlistFilter " //NOI18N
						+ "WHERE idPlaylist=?");  //NOI18N
				stSelectPlaylistFilters.setInt(1, id);
				ResultSet rsFilters = stSelectPlaylistFilters.executeQuery();
				while (rsFilters.next()) {
					int idPlaylistFilter = rsFilters.getInt("idPlaylistFilter");  //NOI18N
					String field = dbConn.getStringValue(rsFilters, "field");  //NOI18N
					String operator = dbConn.getStringValue(rsFilters, "operator");  //NOI18N
					String value = dbConn.getStringValue(rsFilters, "value");  //NOI18N
					playlist.addFilter(new Filter(idPlaylistFilter, Field.valueOf(field),
							Operator.valueOf(operator), value));
				}

				//Get the orders
				PreparedStatement stSelectPlaylistOrders = dbConn.connection.prepareStatement("SELECT idPlaylistOrder, desc, field "
						+ "FROM playlistOrder " //NOI18N
						+ "WHERE idPlaylist=?");  //NOI18N
				stSelectPlaylistOrders.setInt(1, id);
				ResultSet rsOrders = stSelectPlaylistOrders.executeQuery();
				while (rsOrders.next()) {
					int idPlaylistOrder = rsOrders.getInt("idPlaylistOrder");  //NOI18N
					boolean desc = rsOrders.getBoolean("desc");  //NOI18N
					String field = dbConn.getStringValue(rsOrders, "field");  //NOI18N
					playlist.addOrder(new Order(idPlaylistOrder, Field.valueOf(field), desc));
				}

				//Add playlist to hashmap
				playlists.put(id, playlist);
			}
			return true;
		} catch (SQLException ex) {
			Popup.error("getPlaylists", ex);   //NOI18N
			return false;
		}
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="StatSource">
	/**
	 *
	 * @param login
	 * @return
	 */
	public StatSource getStatSource(String login) {
		LinkedHashMap<Integer, StatSource> statSources = new LinkedHashMap<>();
		getStatSources(statSources, login, true);
		return statSources.values().iterator().next();
	}

	/**
	 * Return list of database sources for given machine
	 *
	 * @param statSources
	 * @param hostname
	 * @param hidden
	 * @return
	 */
	public boolean getStatSources(LinkedHashMap<Integer, StatSource> statSources,
			String hostname, boolean hidden) {
		ResultSet rs = null;
		try {
			PreparedStatement stSelectStatSources = dbConn.connection.prepareStatement(
					"SELECT S.idStatSource, S.name AS sourceName, "
					+ "S.idStatement, S.location, S.rootPath, "
					+ "S.idDevice, S.selected, M.name AS machineName \n"
					+ ", S.lastMergeDate " //NOI18N
					+ "FROM statsource S \n"
					+ "JOIN machine M ON M.idMachine=S.idMachine \n" //NOI18N
					+ "WHERE M.name=? "
					+ "ORDER BY S.name");  //NOI18N
			stSelectStatSources.setString(1, hostname);
			rs = stSelectStatSources.executeQuery();
			while (rs.next()) {
				StatSource statSource = getStatSource(rs, hidden);
				statSources.put(statSource.getId(), statSource);
			}
			return true;
		} catch (SQLException ex) {
			Popup.error("getStatSources(" + hostname + ")", ex);   //NOI18N
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

	private StatSource getStatSource(ResultSet rs, boolean hidden) throws SQLException {
		int idStatSource = rs.getInt("idStatSource");  //NOI18N
		int idStatement = rs.getInt("idStatement");  //NOI18N
		String statSourceLocation = dbConn.getStringValue(rs, "location");  //NOI18N
		String machineName = dbConn.getStringValue(rs, "machineName");  //NOI18N
		String lastMergeDate = dbConn.getStringValue(rs, "lastMergeDate", "1970-01-01 00:00:00");
		int idDevice = rs.getInt("idDevice");  //NOI18N
		boolean isSelected = rs.getBoolean("selected");  //NOI18N
		return new StatSource(
				idStatSource,
				dbConn.getStringValue(rs, "sourceName"),
				idStatement,
				statSourceLocation, "", "",
				dbConn.getStringValue(rs, "rootPath"),
				machineName,
				idDevice,
				isSelected, lastMergeDate, hidden);  //NOI18N
	}

	/**
	 * Sets a Stat Source
	 *
	 * @param statSource
	 * @return
	 */
	public synchronized boolean insertOrUpdateStatSource(StatSource statSource) {
		try {
			if (statSource.getId() > -1) {
				PreparedStatement stUpdateStatSource = dbConn.connection.
						prepareStatement("UPDATE statsource SET location=?, " //NOI18N
								+ "rootPath=?, " //NOI18N
								+ "name=?, " //NOI18N
								+ "idStatement=?, " //NOI18N
								+ "idDevice=?, selected=? " //NOI18N
								+ "WHERE idStatSource=?");   //NOI18N

				stUpdateStatSource.setString(1, statSource.getSource().getLocation());
				stUpdateStatSource.setString(2, statSource.getSource().getRootPath());
				stUpdateStatSource.setString(3, statSource.getSource().getName());
				stUpdateStatSource.setInt(4, statSource.getIdStatement());
				if (statSource.getIdDevice() > 0) {
					stUpdateStatSource.setInt(5, statSource.getIdDevice());
				} else {
					stUpdateStatSource.setNull(5, java.sql.Types.INTEGER);
				}
				stUpdateStatSource.setBoolean(6, statSource.isIsSelected());
				stUpdateStatSource.setInt(7, statSource.getId());

				int nbRowsAffected = stUpdateStatSource.executeUpdate();
				if (nbRowsAffected > 0) {
					return true;
				} else {
					Jamuz.getLogger().log(Level.SEVERE, "stUpdateStatSource, myStatSource={0} # row(s) affected: +{1}", new Object[]{statSource.toString(), nbRowsAffected});   //NOI18N
					return false;
				}
			} else {
				PreparedStatement stInsertStatSource = dbConn.connection.prepareStatement("INSERT INTO statsource "
						+ "(location, idStatement, rootPath, idMachine, name, idDevice, selected) " //NOI18N
						+ "VALUES (?, ?, ?, (SELECT idMachine FROM machine WHERE name=?), ?, ?, ?)");   //NOI18N

				stInsertStatSource.setString(1, statSource.getSource().getLocation());
				stInsertStatSource.setInt(2, statSource.getIdStatement());
				stInsertStatSource.setString(3, statSource.getSource().getRootPath());
				stInsertStatSource.setString(4, statSource.getMachineName());
				stInsertStatSource.setString(5, statSource.getSource().getName());
				if (statSource.getIdDevice() > 0) {
					stInsertStatSource.setInt(6, statSource.getIdDevice());
				} else {
					stInsertStatSource.setNull(6, java.sql.Types.INTEGER);
				}
				stInsertStatSource.setBoolean(7, statSource.isIsSelected());

				int nbRowsAffected = stInsertStatSource.executeUpdate();
				if (nbRowsAffected > 0) {
					return true;
				} else {
					Jamuz.getLogger().log(Level.SEVERE, "stInsertStatSource, myStatSource={0} # row(s) affected: +{1}", new Object[]{statSource.toString(), nbRowsAffected});   //NOI18N
					return false;
				}
			}
		} catch (SQLException ex) {
			Popup.error("setStatSource(" + statSource.toString() + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Deletes a Stat Source
	 *
	 * @param id
	 * @return
	 */
	public synchronized boolean deleteStatSource(int id) {
		try {
			PreparedStatement stDeleteStatSource = dbConn.connection.prepareStatement(
					"DELETE FROM statsource WHERE idStatSource=?");   //NOI18N
			stDeleteStatSource.setInt(1, id);
			int nbRowsAffected = stDeleteStatSource.executeUpdate();
			if (nbRowsAffected > 0) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stDeleteStatSource, id={0} # row(s) affected: +{1}", new Object[]{id, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("deleteStatSource(" + id + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 *
	 * @param idStatSource
	 * @return
	 */
	public synchronized String updateStatSourceLastMergeDate(int idStatSource) {
		ResultSet rs = null;
		try {
			PreparedStatement stUpdateStatSourceLastMergeDate = dbConn.connection.prepareStatement("UPDATE statsource "
					+ "SET lastMergeDate=datetime('now') WHERE idStatSource=?");
			stUpdateStatSourceLastMergeDate.setInt(1, idStatSource);
			int nbRowsAffected = stUpdateStatSourceLastMergeDate.executeUpdate();
			if (nbRowsAffected > 0) {
				PreparedStatement stGetLastMergeDate = dbConn.connection.prepareStatement("SELECT lastMergeDate FROM statsource  WHERE idStatSource=?");
				stGetLastMergeDate.setInt(1, idStatSource);
				rs = stGetLastMergeDate.executeQuery();
				String temp = dbConn.getStringValue(rs, "lastMergeDate", "1970-01-01 00:00:00");
				return temp;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateStatSourceLastMergeDate, # row(s) affected: +{0}", new Object[]{nbRowsAffected});   //NOI18N
			}
		} catch (SQLException ex) {
			Popup.error("updateLastMergeDate(" + idStatSource + ")", ex);   //NOI18N
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException ex) {
				Jamuz.getLogger().warning("Failed to close ResultSet");
			}
		}
		return "1970-01-01 00:00:00";
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="PlayCounter">
	/**
	 * Sets previous playCounter in file table
	 *
	 * @param files
	 * @param idStatSource
	 * @return
	 */
	public synchronized boolean updatePreviousPlayCounter(ArrayList<? super FileInfoInt> files, int idStatSource) {
		try {
			int[] results;
			PreparedStatement stUpdatePlayCounter = dbConn.connection.prepareStatement(
					"UPDATE playcounter SET playCounter=? "
					+ "WHERE idFile=? AND idStatSource=?");     //NOI18N
			//First try to update values
			dbConn.connection.setAutoCommit(false);
			for (Iterator<? super FileInfoInt> it = files.iterator(); it.hasNext();) {
				FileInfo file = (FileInfo) it.next();
				stUpdatePlayCounter.setInt(1, file.playCounter);
				stUpdatePlayCounter.setInt(2, file.idFile);
				stUpdatePlayCounter.setInt(3, idStatSource);
				stUpdatePlayCounter.addBatch();
			}
			long startTime = System.currentTimeMillis();
			results = stUpdatePlayCounter.executeBatch();
			dbConn.connection.commit();
			long endTime = System.currentTimeMillis();
			Jamuz.getLogger().log(Level.FINEST, "setPreviousPlayCounter UPDATE // {0} // Total execution time: {1}ms", new Object[]{results.length, endTime - startTime});    //NOI18N

			//If update failed, try to insert values
			int result;
			FileInfo file;
			boolean doInsertBatch = false;
			PreparedStatement stInsertPlayCounter = dbConn.connection.
					prepareStatement("INSERT OR IGNORE INTO playcounter "
							+ "(idFile, idStatSource, playCounter) " //NOI18N
							+ "VALUES (?, ?, ?)");   //NOI18N
			for (int i = 0; i < results.length; i++) {
				result = results[i];
				if (result != 1) {
					file = (FileInfo) files.get(i);
					stInsertPlayCounter.setInt(1, file.idFile);
					stInsertPlayCounter.setInt(2, idStatSource);
					stInsertPlayCounter.setInt(3, file.playCounter);
					stInsertPlayCounter.addBatch();
					doInsertBatch = true;
				}
			}
			if (doInsertBatch) {
				startTime = System.currentTimeMillis();
				results = stInsertPlayCounter.executeBatch();
				dbConn.connection.commit();
				endTime = System.currentTimeMillis();
				Jamuz.getLogger().log(Level.FINEST, "setPreviousPlayCounter "
						+ "INSERT // {0} // Total execution time: {1}ms",
						new Object[]{results.length, endTime - startTime});    //NOI18N
				//Check results
				for (int i = 0; i < results.length; i++) {
					result = results[i];
					if (result != 1) {
						return false;
					}
				}
			}
			dbConn.connection.setAutoCommit(true);
			return true;

		} catch (SQLException ex) {
			Popup.error("setPreviousPlayCounter(" + idStatSource + ")", ex);   //NOI18N
			return false;
		}
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="DeviceFile">
	/**
	 * Insert in deviceFile table
	 *
	 * @param files
	 * @param idDevice
	 * @return
	 */
	public synchronized ArrayList<FileInfoInt> insertOrUpdateDeviceFiles(ArrayList<FileInfoInt> files, int idDevice) {
		ArrayList<FileInfoInt> insertedOrUpdated = new ArrayList<>();
		try {
			if (files.size() > 0) {
				long startTime = System.currentTimeMillis();
				dbConn.connection.setAutoCommit(false);
				int[] results;
				//FIXME Z Use this ON CONFLICT syntax for other insertOrUpdateXXX methods, if applicable
				PreparedStatement stInsertDeviceFile = dbConn.connection.prepareStatement(
						"INSERT INTO deviceFile "
						+ " (idFile, idDevice, oriRelativeFullPath, status) " //NOI18N
						+ " VALUES (?, ?, ?, \"NEW\") "
						+ " ON CONFLICT(idFile, idDevice) DO UPDATE SET status=?, oriRelativeFullPath=?"); //NOI18N
				for (FileInfoInt file : files) {
					stInsertDeviceFile.setInt(1, file.idFile);
					stInsertDeviceFile.setInt(2, idDevice);
					stInsertDeviceFile.setString(3, file.relativeFullPath);
					stInsertDeviceFile.setString(4, file.status.name());
					stInsertDeviceFile.setString(5, file.relativeFullPath);
					stInsertDeviceFile.addBatch();
				}
				results = stInsertDeviceFile.executeBatch();
				dbConn.connection.commit();
				//Check results
				int result;
				for (int i = 0; i < results.length; i++) {
					result = results[i];
					if (result > 0) {
						insertedOrUpdated.add(files.get(i));
					}
				}
				dbConn.connection.setAutoCommit(true);
				long endTime = System.currentTimeMillis();
				Jamuz.getLogger().log(Level.FINEST, "insertOrUpdateDeviceFiles // {0} // Total execution time: {1}ms", new Object[]{results.length, endTime - startTime});    //NOI18N
			}

			return insertedOrUpdated;
		} catch (SQLException ex) {
			Popup.error("insertDeviceFile(" + idDevice + ")", ex);   //NOI18N
			return insertedOrUpdated;
		}
	}

	/**
	 * Insert in deviceFile table
	 *
	 * @param files
	 * @param idDevice
	 * @return
	 */
	public synchronized ArrayList<FileInfoInt> insertOrIgnoreDeviceFiles(ArrayList<FileInfoInt> files, int idDevice) {
		ArrayList<FileInfoInt> inserted = new ArrayList<>();
		try {
			if (files.size() > 0) {
				long startTime = System.currentTimeMillis();
				dbConn.connection.setAutoCommit(false);
				int[] results;
				PreparedStatement stInsertDeviceFile = dbConn.connection.prepareStatement(
						"INSERT OR IGNORE INTO deviceFile "
						+ "(idFile, idDevice, oriRelativeFullPath, status) " //NOI18N
						+ "VALUES (?, ?, ?, \"NEW\")");   //NOI18N
				for (FileInfoInt file : files) {
					stInsertDeviceFile.setInt(1, file.idFile);
					stInsertDeviceFile.setInt(2, idDevice);
					stInsertDeviceFile.setString(3, file.relativeFullPath);
					stInsertDeviceFile.addBatch();
				}
				results = stInsertDeviceFile.executeBatch();
				dbConn.connection.commit();
				//Check results
				int result;
				for (int i = 0; i < results.length; i++) {
					result = results[i];
					if (result > 0) {
						inserted.add(files.get(i));
					}
				}
				dbConn.connection.setAutoCommit(true);
				long endTime = System.currentTimeMillis();
				Jamuz.getLogger().log(Level.FINEST, "insertDeviceFiles // {0} // Total execution time: {1}ms", new Object[]{results.length, endTime - startTime});    //NOI18N
			}

			return inserted;
		} catch (SQLException ex) {
			Popup.error("insertDeviceFile(" + idDevice + ")", ex);   //NOI18N
			return inserted;
		}
	}

	/**
	 * Insert in deviceFile table
	 *
	 * @param file
	 * @param idDevice
	 * @return
	 */
	public synchronized boolean insertOrIgnoreDeviceFile(int idDevice, FileInfoInt file) {
		try {
			int result;
			PreparedStatement stInsertDeviceFile
					= dbConn.connection.prepareStatement(
							"INSERT OR IGNORE INTO deviceFile "
							+ "(idFile, idDevice, oriRelativeFullPath) "
							+ "VALUES (?, ?, ?)");   //NOI18N

			stInsertDeviceFile.setInt(1, file.idFile);
			stInsertDeviceFile.setInt(2, idDevice);
			stInsertDeviceFile.setString(3, file.relativeFullPath);

			long startTime = System.currentTimeMillis();
			result = stInsertDeviceFile.executeUpdate();
			long endTime = System.currentTimeMillis();
			Jamuz.getLogger().log(Level.FINEST, "insertDeviceFile UPDATE // {0} "
					+ "// Total execution time: {1}ms",
					new Object[]{result, endTime - startTime});    //NOI18N

			if (result < 0) {
				Jamuz.getLogger().log(Level.SEVERE, "insertDeviceFile, "
						+ "idFile={0}, idDevice={1}, result={2}",
						new Object[]{file.getIdFile(), idDevice, result});   //NOI18N
				return false;
			}
			return true;
		} catch (SQLException ex) {
			Popup.error("insertDeviceFile(" + idDevice + ", " + file.getIdFile() + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 *
	 * @param idDevice
	 * @return
	 */
	public synchronized boolean deleteDeviceFiles(int idDevice) {
		try {
			PreparedStatement stDeleteDeviceFiles = dbConn.connection.prepareStatement(
					"DELETE FROM deviceFile WHERE idDevice=?");  //NOI18N
			stDeleteDeviceFiles.setInt(1, idDevice);
			long startTime = System.currentTimeMillis();
			int result = stDeleteDeviceFiles.executeUpdate();
			long endTime = System.currentTimeMillis();
			Jamuz.getLogger().log(Level.FINEST, "stDeleteDeviceFile DELETE "
					+ "// Total execution time: {0}ms",
					new Object[]{endTime - startTime});    //NOI18N
			if (result < 0) {
				Jamuz.getLogger().log(Level.SEVERE,
						"stDeleteDeviceFile, idDevice={0}, result={1}",
						new Object[]{idDevice, result});   //NOI18N
			}
			return true;
		} catch (SQLException ex) {
			Popup.error("deleteDeviceFiles()", ex);   //NOI18N
			return false;
		}
	}

	/**
	 *
	 * @param files
	 */
	public synchronized void insertOrUpdateFilesTranslated(ArrayList<FileInfoInt> files) {
		try {
			if (files.size() > 0) {
				long startTime = System.currentTimeMillis();
				dbConn.connection.setAutoCommit(false);
				int[] results;
				PreparedStatement preparedStatement = dbConn.connection.prepareStatement(
						"INSERT INTO fileTranscoded "
						+ " (idFile, ext, bitRate, format, length, size, trackGain, albumGain, modifDate) " //NOI18N
						+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) "
						+ " ON CONFLICT(idFile, ext) DO UPDATE SET bitRate=?, format=?, length=?, size=?, trackGain=?, albumGain=?, modifDate=?"); //NOI18N
				for (FileInfoInt file : files) {
					//Insert
					preparedStatement.setInt(1, file.idFile);
					preparedStatement.setString(2, file.getExt());
					preparedStatement.setString(3, file.getBitRate());
					preparedStatement.setString(4, file.getFormat());
					preparedStatement.setInt(5, file.getLength());
					preparedStatement.setLong(6, file.getSize());
					GainValues gainValues = file.getReplayGain(false);
					preparedStatement.setFloat(7, gainValues.getTrackGain());
					preparedStatement.setFloat(8, gainValues.getAlbumGain());
					preparedStatement.setString(9, file.getFormattedModifDate());
					//Update
					preparedStatement.setString(10, file.getBitRate());
					preparedStatement.setString(11, file.getFormat());
					preparedStatement.setInt(12, file.getLength());
					preparedStatement.setLong(13, file.getSize());
					preparedStatement.setFloat(14, gainValues.getTrackGain());
					preparedStatement.setFloat(15, gainValues.getAlbumGain());
					preparedStatement.setString(16, file.getFormattedModifDate());
					preparedStatement.addBatch();
				}
				results = preparedStatement.executeBatch();
				dbConn.connection.commit();
				dbConn.connection.setAutoCommit(true);
				long endTime = System.currentTimeMillis();
				Jamuz.getLogger().log(Level.FINEST, "insertOrUpdateDeviceFilesTranslated // {0} // Total execution time: {1}ms", new Object[]{results.length, endTime - startTime});    //NOI18N
			}
		} catch (SQLException ex) {
			Popup.error("insertOrUpdateDeviceFilesTranslated(ArrayList<FileInfoInt> files)", ex);   //NOI18N
		}
	}

	/**
	 *
	 */
	public enum SyncStatus {

		/**
		 *
		 */
		NEW,
		/**
		 *
		 */
		INFO
	}

	/**
	 * Resets the check flag to UNCHECKED on path table for given checked flag
	 *
	 * @param status
	 * @param idFile
	 * @param idDevice
	 * @return
	 */
	public synchronized boolean updateDeviceFileStatus(SyncStatus status, int idFile, int idDevice) {
		try {
			PreparedStatement stUpdateCheckedFlagReset
					= dbConn.connection.prepareStatement(
							"UPDATE deviceFile SET status=? "
							+ "WHERE idFile=? AND idDevice=?");   //NOI18N
			stUpdateCheckedFlagReset.setString(1, status.name());
			stUpdateCheckedFlagReset.setInt(2, idFile);
			stUpdateCheckedFlagReset.setInt(3, idDevice);
			int nbRowsAffected = stUpdateCheckedFlagReset.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "setDeviceFileStatus, idFile={0} # row(s) affected: +{1}", new Object[]{idFile, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Jamuz.getLogger().log(Level.SEVERE, "setDeviceFileStatus, idFile={0} : {1}", new Object[]{idFile, ex});   //NOI18N
			return false;
		}
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Device">
	/**
	 * Deletes a device
	 *
	 * @param id
	 * @return
	 */
	public synchronized boolean deleteDevice(int id) {
		try {
			PreparedStatement stDeleteDevice = dbConn.connection.prepareStatement(
					"DELETE FROM device WHERE idDevice=?");  //NOI18N
			stDeleteDevice.setInt(1, id);
			int nbRowsAffected = stDeleteDevice.executeUpdate();
			if (nbRowsAffected > 0) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE,
						"stDeleteDevice, id={0} # row(s) affected: +{1}",
						new Object[]{id, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			//FIXME Z OPTIONS Happens when the device is linked to a stat source => 
			// => Popup this nicely to user !
			//instead of:
			//java.sql.SQLException: [SQLITE_CONSTRAINT]  
			//Abort due to constraint violation (foreign key constraint failed)
			Popup.error("deleteDevice(" + id + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 *
	 * @param login
	 * @return
	 */
	public Device getDevice(String login) {
		LinkedHashMap<Integer, Device> devices = new LinkedHashMap<>();
		getDevices(devices, login, true);
		return devices.values().iterator().next();
	}

	/**
	 * Get list of devices
	 *
	 * @param devices
	 * @param hostname
	 * @param hidden
	 * @return
	 */
	public boolean getDevices(LinkedHashMap<Integer, Device> devices,
			String hostname, boolean hidden) {
		ResultSet rs = null;
		try {
			PreparedStatement stSelectDevices = dbConn.connection.prepareStatement(
					"SELECT idDevice AS deviceId, source, destination, idPlaylist, D.name AS deviceName "
					+ "FROM device D "
					+ "JOIN machine M "
					+ "ON M.idMachine=D.idMachine " //NOI18N
					+ "WHERE M.name=? "
					+ "ORDER BY D.name");  //NOI18N
			stSelectDevices.setString(1, hostname);
			rs = stSelectDevices.executeQuery();
			while (rs.next()) {
				Device device = getDevice(rs, hostname, hidden);
				devices.put(device.getId(), device);   //NOI18N
			}
			return true;
		} catch (SQLException ex) {
			Popup.error("getDevices", ex);   //NOI18N
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

	private Device getDevice(ResultSet rs, String hostname, boolean hidden) throws SQLException {
		int idDevice = rs.getInt("deviceId");  //NOI18N
		return new Device(idDevice,
				dbConn.getStringValue(rs, "deviceName"), //NOI18N
				dbConn.getStringValue(rs, "source"),
				dbConn.getStringValue(rs, "destination"),
				rs.getInt("idPlaylist"), hostname, hidden);
	}

	/**
	 * Inserts or update a device
	 *
	 * @param device
	 * @return
	 */
	public synchronized boolean updateDevice(Device device) {
		try {
			if (device.getId() > -1) {
				PreparedStatement stUpdateDevice = dbConn.connection.prepareStatement(
						"UPDATE device SET name=?, source=?,"
						+ "destination=?, idPlaylist=? WHERE idDevice=?");    //NOI18N
				stUpdateDevice.setString(1, device.getName());
				stUpdateDevice.setString(2, device.getSource());
				stUpdateDevice.setString(3, device.getDestination());
				if (device.getIdPlaylist() > 0) {
					stUpdateDevice.setInt(4, device.getIdPlaylist());
				} else {
					stUpdateDevice.setNull(4, java.sql.Types.INTEGER);
				}
				stUpdateDevice.setInt(5, device.getId());

				int nbRowsAffected = stUpdateDevice.executeUpdate();
				if (nbRowsAffected > 0) {
					return true;
				} else {
					Jamuz.getLogger().log(Level.SEVERE, "stUpdateDevice, "
							+ "myStatSource={0} # row(s) affected: +{1}",
							new Object[]{device.toString(), nbRowsAffected});   //NOI18N
					return false;
				}
			} else {
				PreparedStatement stInsertDevice = dbConn.connection.prepareStatement(
						"INSERT INTO device "
						+ "(name, source, destination, "
						+ "idMachine, idPlaylist) VALUES (?, ?, ?, "
						+ "(SELECT idMachine FROM machine WHERE name=?), ?)");    //NOI18N
				stInsertDevice.setString(1, device.getName());
				stInsertDevice.setString(2, device.getSource());
				stInsertDevice.setString(3, device.getDestination());
				stInsertDevice.setString(4, device.getMachineName());
				if (device.getIdPlaylist() > 0) {
					stInsertDevice.setInt(5, device.getIdPlaylist());
				} else {
					stInsertDevice.setNull(5, java.sql.Types.INTEGER);
				}

				int nbRowsAffected = stInsertDevice.executeUpdate();
				if (nbRowsAffected > 0) {
					return true;
				} else {
					Jamuz.getLogger().log(Level.SEVERE, "stInsertDevice, "
							+ "myStatSource={0} # row(s) affected: +{1}",
							new Object[]{device.toString(), nbRowsAffected});   //NOI18N
					return false;
				}
			}
		} catch (SQLException ex) {
			Popup.error("setDevice(" + device.toString() + ")", ex);   //NOI18N
			return false;
		}
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Client">
	/**
	 * Inserts or update a device
	 *
	 * @param clientInfo
	 * @return
	 */
	public synchronized boolean updateClient(ClientInfo clientInfo) {
		try {
			if (clientInfo.getId() > -1) {

				PreparedStatement stUpdateClient = dbConn.connection.prepareStatement(
						"UPDATE client SET login=?, pwd=?, name=?, enabled=? "
						+ "WHERE idClient=?");    //NOI18N
				stUpdateClient.setString(1, clientInfo.getLogin());
				stUpdateClient.setString(2, clientInfo.getPwd());
				stUpdateClient.setString(3, clientInfo.getName());
				stUpdateClient.setBoolean(4, clientInfo.isEnabled());
				stUpdateClient.setInt(5, clientInfo.getId());

				int nbRowsAffected = stUpdateClient.executeUpdate();
				if (nbRowsAffected > 0) {
					updateDevice(clientInfo.getDevice());
					insertOrUpdateStatSource(clientInfo.getStatSource());
					return true;
				} else {
					Jamuz.getLogger().log(Level.SEVERE,
							"stUpdateClient, myStatSource={0} # row(s) affected: +{1}",
							new Object[]{clientInfo.toString(), nbRowsAffected});   //NOI18N
					return false;
				}
			} else {
				PreparedStatement stInsertClient = dbConn.connection.prepareStatement(
						"INSERT INTO client "
						+ "(login, pwd, name, "
						+ "idDevice, idStatSource, enabled) "
						+ "VALUES (?, ?, ?, ?, ?, ?)");    //NOI18N
				stInsertClient.setString(1, clientInfo.getLogin());
				stInsertClient.setString(2, clientInfo.getPwd());
				stInsertClient.setString(3, clientInfo.getName());
				if (clientInfo.getDevice() != null) {
					stInsertClient.setInt(4, clientInfo.getDevice().getId());
				} else {
					stInsertClient.setNull(4, java.sql.Types.INTEGER);
				}
				if (clientInfo.getStatSource() != null) {
					stInsertClient.setInt(5, clientInfo.getStatSource().getId());
				} else {
					stInsertClient.setNull(5, java.sql.Types.INTEGER);
				}
				stInsertClient.setBoolean(6, clientInfo.isEnabled());

				int nbRowsAffected = stInsertClient.executeUpdate();
				if (nbRowsAffected > 0) {
					return true;
				} else {
					Jamuz.getLogger().log(Level.SEVERE,
							"stInsertClient, myStatSource={0} # row(s) affected: +{1}",
							new Object[]{clientInfo.toString(), nbRowsAffected});   //NOI18N
					return false;
				}
			}
		} catch (SQLException ex) {
			Popup.error("setClientInfo(" + clientInfo.toString() + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 *
	 * @param login
	 * @return
	 */
	public ClientInfo getClient(String login) {
		LinkedHashMap<Integer, ClientInfo> clients = new LinkedHashMap<>();
		getClients(clients, login);
		return clients.values().iterator().next();
	}

	/**
	 * Get list of clients
	 *
	 * @param clients
	 * @return
	 */
	public boolean getClients(LinkedHashMap<Integer, ClientInfo> clients) {
		return getClients(clients, "");
	}

	/**
	 * Get list of clients
	 *
	 * @param clients
	 * @param login
	 * @return
	 */
	private boolean getClients(LinkedHashMap<Integer, ClientInfo> clients, String login) {
		ResultSet rs = null;
		try {
			PreparedStatement stSelectClients = dbConn.connection.prepareStatement(
					"SELECT idClient, login, pwd, C.name AS clientName, enabled ,\n"
					+ "D.idDevice AS deviceId, source, destination, idPlaylist, D.name AS deviceName,\n"
					+ "S.idStatSource, S.name AS sourceName, S.idStatement, S.location, "
					+ "S.rootPath, S.idDevice, "
					+ "S.selected, login AS machineName , S.lastMergeDate\n"
					+ "FROM client C \n"
					+ "JOIN device D ON D.idDevice=C.idDevice\n"
					+ "JOIN statSource S ON S.idStatSource=C.idStatSource"
					+ (login.isBlank() ? login : " WHERE login=? "));  //NOI18N
			if (!login.isBlank()) {
				stSelectClients.setString(1, login);
			}
			rs = stSelectClients.executeQuery();
			while (rs.next()) {
				int idClient = rs.getInt("idClient");  //NOI18N
				Device device = getDevice(rs, login, true);
				StatSource statSource = getStatSource(rs, true);
				clients.put(idClient,
						new ClientInfo(idClient,
								dbConn.getStringValue(rs, "login"),
								dbConn.getStringValue(rs, "clientName"),
								dbConn.getStringValue(rs, "pwd"),
								device, statSource,
								rs.getBoolean("enabled")
						));
			}
			return true;
		} catch (SQLException ex) {
			Popup.error("getClients", ex);   //NOI18N
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

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="File">
	/**
	 *
	 * @param sql
	 * @return
	 */
	public Integer getFilesCount(String sql) {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = dbConn.connection.createStatement();
			rs = st.executeQuery(sql);
			return rs.getInt(1);

		} catch (SQLException ex) {
			Popup.error("getIdFileMax()", ex);   //NOI18N
			return null;
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

	/**
	 * Gets MIN or MAX year from audio files
	 *
	 * @param maxOrMin
	 * @return
	 */
	public double getYear(String maxOrMin) {
		Statement st = null;
		ResultSet rs = null;
		try {
			st = dbConn.connection.createStatement();
			//FIXME ZZ PanelSelect better validate year (but regex is not available by default :( )
			rs = st.executeQuery("SELECT " + maxOrMin + "(year) FROM file "
					+ "WHERE year GLOB '[0-9][0-9][0-9][0-9]' AND length(year)=4");
			//To exclude wrong entries (not YYYY format)
			return rs.getDouble(1);

		} catch (SQLException ex) {
			Popup.error("getYear(" + maxOrMin + ")", ex);   //NOI18N
			return -1.0;
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

	/**
	 * Get statistics on given table (path or file)
	 *
	 * @param field
	 * @param value
	 * @param table
	 * @param label
	 * @param color
	 * @param selRatings
	 * @return
	 */
	public StatItem getStatItem(String field, String value, String table,
			String label, Color color, boolean[] selRatings) {
		String sql;
		Statement st = null;
		ResultSet rs = null;
		try {
			value = value.replaceAll("\"", "%");   //NOI18N
			sql = "SELECT COUNT(*), COUNT(DISTINCT path.idPath), SUM(size), "
					+ "\nSUM(length), avg(rating) "
					+ "\nFROM file JOIN path ON path.idPath=file.idPath ";
			if (value.contains("IN (")) {  //NOI18N
				sql += " \nWHERE " + table + "." + field + " " + value;   //NOI18N
			} else if (value.startsWith(">")) {   //NOI18N
				sql += " \nWHERE " + table + "." + field + value + "";   //NOI18N
			} else if (value.contains("%")) {   //NOI18N
				sql += " \nWHERE " + table + "." + field + " LIKE \"" + value + "\"";   //NOI18N
			} else {
				sql += " \nWHERE " + table + "." + field + "='" + value + "'";   //NOI18N
			}
			if (selRatings != null) {
				sql += " \nAND file.rating IN " + getCSVlist(selRatings);
			}
			st = dbConn.connection.createStatement();
			rs = st.executeQuery(sql);
			return new StatItem(label, value, rs.getLong(1), rs.getLong(2),
					rs.getLong(3), rs.getLong(4), rs.getDouble(5), color);

		} catch (SQLException ex) {
			Popup.error("getStatItem(" + field + "," + value + ")", ex);   //NOI18N
			return new StatItem(label, value, -1, -1, -1, -1, -1, Color.BLACK);
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

	/**
	 * Inserts a file (tags)
	 *
	 * @param fileInfo
	 * @param key
	 * @return
	 */
	public synchronized boolean insert(FileInfoInt fileInfo, int[] key) {
		try {
			PreparedStatement stInsertFileTag = dbConn.connection.prepareStatement("INSERT INTO file (name, idPath, "
					+ "format, title, artist, album, albumArtist, genre, discNo, trackNo, year, comment, " //NOI18N
					+ "length, bitRate, size, modifDate, trackTotal, discTotal, BPM, nbCovers, "
					+ "rating, lastPlayed, playCounter, addedDate, coverHash, trackGain, albumGain) " //NOI18N
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ "0, \"1970-01-01 00:00:00\", 0, datetime('now'), ?, ?, ?)");      //NOI18N
			stInsertFileTag.setString(1, fileInfo.getFilename());
			stInsertFileTag.setInt(2, fileInfo.getIdPath());
			stInsertFileTag.setString(3, fileInfo.getFormat());
			stInsertFileTag.setString(4, fileInfo.title);
			stInsertFileTag.setString(5, fileInfo.getArtist());
			stInsertFileTag.setString(6, fileInfo.getAlbum());
			stInsertFileTag.setString(7, fileInfo.getAlbumArtist());
			stInsertFileTag.setString(8, fileInfo.getGenre());
			stInsertFileTag.setInt(9, fileInfo.discNo);
			stInsertFileTag.setInt(10, fileInfo.trackNo);
			stInsertFileTag.setString(11, fileInfo.getYear());
			stInsertFileTag.setString(12, fileInfo.getComment());
			stInsertFileTag.setInt(13, fileInfo.length);
			stInsertFileTag.setString(14, fileInfo.getBitRate());
			stInsertFileTag.setLong(15, fileInfo.size);
			stInsertFileTag.setString(16, fileInfo.getFormattedModifDate());
			stInsertFileTag.setInt(17, fileInfo.trackTotal);
			stInsertFileTag.setInt(18, fileInfo.discTotal);
			stInsertFileTag.setFloat(19, fileInfo.getBPM());
			stInsertFileTag.setInt(20, fileInfo.nbCovers);
			stInsertFileTag.setString(21, fileInfo.getCoverHash());
			GainValues gainValues = fileInfo.getReplayGain(false);
			stInsertFileTag.setFloat(22, gainValues.getTrackGain());
			stInsertFileTag.setFloat(23, gainValues.getAlbumGain());
			int nbRowsAffected = stInsertFileTag.executeUpdate();

			if (nbRowsAffected == 1) {
				ResultSet keys = stInsertFileTag.getGeneratedKeys();
				keys.next();
				key[0] = keys.getInt(1);
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "insertTags, fileInfo={0} # row(s) affected: +{1}", new Object[]{fileInfo.toString(), nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("insertTags(" + fileInfo.toString() + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Updates a file (tags)
	 *
	 * @param fileInfo
	 * @return
	 */
	public synchronized boolean updateFile(FileInfoInt fileInfo) {
		try {
			PreparedStatement stUpdateFileTag = dbConn.connection.prepareStatement(
					"UPDATE file "
					+ "SET format=?, title=?, artist=?, album=?, albumArtist=?, "
					+ "genre=?, discNo=?, " //NOI18N
					+ "trackNo=?, year=?, comment=?, " //NOI18N
					+ "length=?, bitRate=?, size=?, modifDate=?, trackTotal=?, "
					+ "discTotal=?, BPM=?, "
					+ "nbCovers=?, coverHash=?, trackGain=?, albumGain=? " //NOI18N
					+ "WHERE idPath=? AND idFile=?");   //NOI18N
			stUpdateFileTag.setString(1, fileInfo.getFormat());
			stUpdateFileTag.setString(2, fileInfo.title);
			stUpdateFileTag.setString(3, fileInfo.getArtist());
			stUpdateFileTag.setString(4, fileInfo.getAlbum());
			stUpdateFileTag.setString(5, fileInfo.getAlbumArtist());
			stUpdateFileTag.setString(6, fileInfo.getGenre());
			stUpdateFileTag.setInt(7, fileInfo.discNo);
			stUpdateFileTag.setInt(8, fileInfo.trackNo);
			stUpdateFileTag.setString(9, fileInfo.getYear());
			stUpdateFileTag.setString(10, fileInfo.getComment());
			stUpdateFileTag.setInt(11, fileInfo.length);
			stUpdateFileTag.setString(12, fileInfo.getBitRate());
			stUpdateFileTag.setLong(13, fileInfo.size);
			stUpdateFileTag.setString(14, fileInfo.getFormattedModifDate());
			stUpdateFileTag.setInt(15, fileInfo.trackTotal);
			stUpdateFileTag.setInt(16, fileInfo.discTotal);
			stUpdateFileTag.setFloat(17, fileInfo.getBPM());
			stUpdateFileTag.setInt(18, fileInfo.nbCovers);
			stUpdateFileTag.setString(19, fileInfo.getCoverHash());
			GainValues gainValues = fileInfo.getReplayGain(false);
			stUpdateFileTag.setFloat(20, gainValues.getTrackGain());
			stUpdateFileTag.setFloat(21, gainValues.getAlbumGain());
			//WHERE:
			stUpdateFileTag.setInt(22, fileInfo.getIdPath());
			stUpdateFileTag.setInt(23, fileInfo.idFile);
			int nbRowsAffected = stUpdateFileTag.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "updateTags, fileInfo={0} # row(s) affected: +{1}", new Object[]{fileInfo.toString(), nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateTags(" + fileInfo.toString() + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Updates a file (tags)
	 *
	 * @param file
	 * @return
	 */
	public synchronized boolean updateFileLastPlayedAndCounter(FileInfoInt file) {
		try {
			PreparedStatement stUpdateFileLastPlayedAndCounter
					= dbConn.connection.prepareStatement("UPDATE file "
							+ "SET lastplayed=?, playCounter=? "
							+ "WHERE idFile=?");

			stUpdateFileLastPlayedAndCounter.setString(1, DateTime.getCurrentUtcSql());
			stUpdateFileLastPlayedAndCounter.setInt(2, file.playCounter + 1);
			stUpdateFileLastPlayedAndCounter.setInt(3, file.idFile);
			int nbRowsAffected = stUpdateFileLastPlayedAndCounter.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateFileLastPlayedAndCounter, fileInfo={0} # row(s) affected: +{1}", new Object[]{file.toString(), nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateLastPlayedAndCounter(" + file.toString() + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Update rating
	 *
	 * @param fileInfo
	 * @return
	 */
	public synchronized boolean updateFileRating(FileInfoInt fileInfo) {
		try {
			PreparedStatement stUpdateFileRating = dbConn.connection.prepareStatement(
					"UPDATE file set rating=?, "
					+ "ratingModifDate=datetime('now') "
					+ "WHERE idFile=?");  //NOI18N
			stUpdateFileRating.setInt(1, fileInfo.rating);
			stUpdateFileRating.setInt(2, fileInfo.idFile);
			int nbRowsAffected = stUpdateFileRating.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateFileRating, fileInfo={0} # row(s) affected: +{1}", new Object[]{fileInfo.toString(), nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateRating(" + fileInfo.toString() + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Updates all files with idPath to newIdPath
	 *
	 * @param idPath
	 * @param newIdPath
	 * @return
	 */
	public synchronized boolean updateFileIdPath(int idPath, int newIdPath) {
		try {
			PreparedStatement stUpdateIdPathInFile = dbConn.connection.prepareStatement(
					"UPDATE file "
					+ "SET idPath=? " //NOI18N
					+ "WHERE idPath=?");   //NOI18N
			stUpdateIdPathInFile.setInt(1, newIdPath);
			stUpdateIdPathInFile.setInt(2, idPath);
			int nbRowsAffected = stUpdateIdPathInFile.executeUpdate();
			if (nbRowsAffected > 0) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "setIdPath, idPath={0}, newIdPath={1} "
						+ "# row(s) affected: +{2}", new Object[]{idPath, newIdPath, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("setIdPath(" + idPath + ", " + newIdPath + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Updates a file (name, modifDate)
	 *
	 * @param idFile
	 * @param modifDate
	 * @param name
	 * @return
	 */
	public synchronized boolean updateFileModifDate(int idFile, Date modifDate,
			String name) {
		try {
			PreparedStatement stUpdateFileModifDate = dbConn.connection.prepareStatement(
					"UPDATE file "
					+ "SET name=?, modifDate=? " //NOI18N
					+ "WHERE idFile=?");   //NOI18N

			stUpdateFileModifDate.setString(1, name);
			stUpdateFileModifDate.setString(2, DateTime.formatUTCtoSqlUTC(modifDate));
			stUpdateFileModifDate.setInt(3, idFile);

			//Note that we need to scan files (even for check) to get idFile otherwise the following will fail
			int nbRowsAffected = stUpdateFileModifDate.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateFile, idFile={0} # "
						+ "row(s) affected: +{1}", new Object[]{idFile, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateFileModifDate(" + idFile + ", \"" + modifDate.toString() + "\", \"" + name + "\")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Delete a file.
	 *
	 * @param idFile
	 * @return
	 */
	public synchronized boolean deleteFile(int idFile) {
		try {
			PreparedStatement stDeleteFile = dbConn.connection.prepareStatement(
					"DELETE FROM file WHERE idFile=?");   //NOI18N
			stDeleteFile.setInt(1, idFile);
			int nbRowsAffected = stDeleteFile.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "deleteFile, idFile={0} "
						+ "# row(s) affected: +{1}", new Object[]{idFile, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("deleteFile(" + idFile + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 *
	 * @param idFile
	 * @return
	 */
	public synchronized boolean setFileSaved(int idFile) {
		try {
			PreparedStatement stUpdateSavedFile = dbConn.connection.prepareStatement("UPDATE file SET saved=1 WHERE idFile=?");   //NOI18N

			stUpdateSavedFile.setInt(1, idFile);
			int nbRowsAffected = stUpdateSavedFile.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "setFileSaved, idFile={0} # row(s) affected: +{1}", new Object[]{idFile, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("setFileSaved(" + idFile + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Get statistics for merge process
	 *
	 * @param files
	 * @param statSource
	 * @return
	 */
	public boolean getStatistics(ArrayList<FileInfo> files, StatSource statSource) {
		try {
			if (statSource.getIdDevice() > 0) {
				this.stSelectFileStatistics = stSelectFilesStats4SourceAndDevice;
				this.stSelectFileStatistics.setInt(1, statSource.getId());
				this.stSelectFileStatistics.setInt(2, statSource.getIdDevice());
			} else {
				this.stSelectFileStatistics = stSelectFilesStats4Source;
				this.stSelectFileStatistics.setInt(1, statSource.getId());
			}
			return getStatistics(files);

		} catch (SQLException ex) {
			Popup.error(ex);
			Jamuz.getLogger().log(Level.SEVERE, "getStatistics: " + statSource, ex);  //NOI18N
			return false;
		}

	}

	/**
	 * Return a FileInfo from given ResultSet
	 *
	 * @param rs
	 * @return
	 */
	@Override
	protected FileInfo getFileStatistics(ResultSet rs) {
		try {
			//JaMuz database does not store rootPath in database, only relative one
			String relativeFullPath = dbConn.getStringValue(rs, "fullPath");  //NOI18N
			int rating = rs.getInt("rating");  //NOI18N
			String lastPlayed = dbConn.getStringValue(rs, "lastplayed", "1970-01-01 00:00:00");  //NOI18N
			String addedDate = dbConn.getStringValue(rs, "addedDate", "1970-01-01 00:00:00");  //NOI18N
			int playCounter = rs.getInt("playCounter");  //NOI18N
			float bpm = rs.getFloat("bpm");
			int previousPlayCounter = rs.getInt("previousPlayCounter");  //NOI18N
			String ratingModifDate = dbConn.getStringValue(rs, "ratingModifDate", "1970-01-01 00:00:00");  //NOI18N
			String tagsModifDate = dbConn.getStringValue(rs, "tagsModifDate", "1970-01-01 00:00:00");  //NOI18N
			String genreModifDate = dbConn.getStringValue(rs, "genreModifDate", "1970-01-01 00:00:00");  //NOI18N
			int idFile = rs.getInt("idFile");  //NOI18N
			int idPath = rs.getInt("idPath");  //NOI18N
			String genre = dbConn.getStringValue(rs, "genre");  //NOI18N

			return new FileInfo(idFile, idPath, relativeFullPath, rating,
					lastPlayed, addedDate, playCounter, this.getName(),
					previousPlayCounter, bpm, genre, ratingModifDate, tagsModifDate,
					genreModifDate);
		} catch (SQLException ex) {
			Popup.error("getStats", ex);  //NOI18N
			return null;
		}
	}

	/**
	 * Set update statistics parameters
	 *
	 * @param file
	 * @throws SQLException
	 */
	@Override
	protected synchronized void setUpdateStatisticsParameters(FileInfo file) throws SQLException {
		this.stUpdateFileStatistics.setInt(1, file.rating);
		this.stUpdateFileStatistics.setFloat(2, file.getBPM());
		this.stUpdateFileStatistics.setString(3, file.getFormattedLastPlayed());
		this.stUpdateFileStatistics.setString(4, file.getFormattedAddedDate());
		this.stUpdateFileStatistics.setInt(5, file.playCounter);
		this.stUpdateFileStatistics.setString(6,
				file.updateRatingModifDate
						? DateTime.getCurrentUtcSql()
						: file.getFormattedRatingModifDate());
		this.stUpdateFileStatistics.setString(7,
				file.updateGenreModifDate
						? DateTime.getCurrentUtcSql()
						: file.getFormattedGenreModifDate());
		this.stUpdateFileStatistics.setString(8, file.getGenre());
		this.stUpdateFileStatistics.setInt(9, file.idFile);
		this.stUpdateFileStatistics.addBatch();
	}

	/**
	 *
	 * @param files
	 * @return
	 */
	@Override
	public int[] updateFileStatistics(ArrayList<? extends FileInfo> files) {
		int[] results = super.updateFileStatistics(files);
		return updateFileTags(files, results);
	}
	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Path">
	/**
	 * Gets folders having given checked flag
	 *
	 * @param folders
	 * @param checkedFlag
	 * @return
	 */
	public boolean getFolders(ConcurrentHashMap<String, FolderInfo> folders,
			CheckedFlag checkedFlag) {
		return this.getFolders(folders, "WHERE checked="
				+ checkedFlag.getValue());//NOI18N
	}

	/**
	 * Return given folder for check (as a list to match check process)
	 *
	 * @param folders
	 * @param idPath
	 * @return
	 */
	public boolean getFolder(ConcurrentHashMap<String, FolderInfo> folders,
			int idPath) {
		return this.getFolders(folders, "WHERE idPath=" + idPath);   //NOI18N
	}

	/**
	 * Return given folder
	 *
	 * @param idPath
	 * @return
	 */
	public FolderInfo getFolder(int idPath) {
		ConcurrentHashMap<String, FolderInfo> folders = new ConcurrentHashMap<>();
		if (getFolder(folders, idPath)) { //NOI18N
			return folders.elements().nextElement(); //.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Get folders for scan
	 *
	 * @param folders
	 * @return
	 */
	public boolean getFolders(ConcurrentHashMap<String, FolderInfo> folders) {
		return getFolders(folders, "");
	}

	private boolean getFolders(ConcurrentHashMap<String, FolderInfo> folders,
			String sqlWhere) {
		folders.clear();
		String sql = "SELECT idPath, strPath, modifDate, checked "
				+ "FROM path " + sqlWhere;    //NOI18N
		Statement st = null;
		ResultSet rs = null;
		try {
			st = dbConn.connection.createStatement();
			rs = st.executeQuery(sql);
			String path;
			while (rs.next()) {
				Date dbModifDate = DateTime.parseSqlUtc(
						dbConn.getStringValue(rs, "modifDate"));   //NOI18N
				path = FilenameUtils.separatorsToSystem(
						dbConn.getStringValue(rs, "strPath"));   //NOI18N
				folders.put(path, new FolderInfo(rs.getInt("idPath"),
						path, dbModifDate,
						CheckedFlag.values()[rs.getInt("checked")]));   //NOI18N
			}
			return true;
		} catch (SQLException ex) {
			Popup.error("getFolderInfoList(" + sqlWhere + ")", ex);   //NOI18N
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
				if (st != null) {
					st.close();
				}
			} catch (SQLException ex) {
				Jamuz.getLogger().warning("Failed to close Statement");
			}
		}
	}

	/**
	 * Checks if path exists in database
	 *
	 * @param path
	 * @return
	 */
	public int getIdPath(String path) {
		ResultSet rs = null;
		try {
			path = FilenameUtils.separatorsToUnix(path);

			PreparedStatement stSelectPath = dbConn.connection.prepareStatement(
					"SELECT idPath FROM path WHERE strPath=? ORDER BY idPath");   //NOI18N
			stSelectPath.setString(1, path);
			rs = stSelectPath.executeQuery();
			if (rs.next()) { //Check if we have a result, so we can move to this one
				return rs.getInt(1);
			} else {
				return -1;
			}
		} catch (SQLException ex) {
			Popup.error("isPathExists(" + path + ")", ex);   //NOI18N
			return -1;
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
	 * Insert a path in database
	 *
	 * @param relativePath
	 * @param modifDate
	 * @param checkedFlag
	 * @param mbId
	 * @param key
	 * @return
	 */
	public synchronized boolean insertPath(String relativePath, Date modifDate,
			CheckedFlag checkedFlag, String mbId, int[] key) {
		try {
			//Only inserting in Linux style in database
			relativePath = FilenameUtils.separatorsToUnix(relativePath);
			PreparedStatement stInsertPath = dbConn.getConnnection().prepareStatement(
					"INSERT INTO path "
					+ "(strPath, modifDate, checked, mbId) " //NOI18N
					+ "VALUES (?, ?, ?, ?)");   //NOI18N

			stInsertPath.setString(1, relativePath);
			stInsertPath.setString(2, DateTime.formatUTCtoSqlUTC(modifDate));
			stInsertPath.setInt(3, checkedFlag.getValue());
			stInsertPath.setString(4, mbId);
			int nbRowsAffected = stInsertPath.executeUpdate();
			if (nbRowsAffected == 1) {
				ResultSet keys = stInsertPath.getGeneratedKeys();
				keys.next();
				key[0] = keys.getInt(1);
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stInsertPath, relativePath=\"{0}\" "
						+ "# row(s) affected: +{1}",
						new Object[]{relativePath, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("insertPath(" + relativePath + ", " + modifDate.toString() + ")",
					 ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Update a path in database
	 *
	 * @param idPath
	 * @param modifDate
	 * @param checkedFlag
	 * @param path
	 * @param mbId
	 * @return
	 */
	public synchronized boolean updatePath(int idPath, Date modifDate,
			CheckedFlag checkedFlag, String path, String mbId) {
		try {
			PreparedStatement stUpdatePath = dbConn.connection.prepareStatement(
					"UPDATE path "
					+ "SET modifDate=?, checked=?, strPath=?, mbId=? " //NOI18N
					+ "WHERE idPath=?");   //NOI18N
			stUpdatePath.setString(1, DateTime.formatUTCtoSqlUTC(modifDate));
			stUpdatePath.setInt(2, checkedFlag.getValue());
			path = FilenameUtils.separatorsToUnix(path);
			stUpdatePath.setString(3, path);
			stUpdatePath.setString(4, mbId);
			stUpdatePath.setInt(5, idPath);
			int nbRowsAffected = stUpdatePath.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdatePath, idPath={0} "
						+ "# row(s) affected: +{1}", new Object[]{idPath, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updatePath(" + idPath + ", " + modifDate.toString() + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Sets a path as checked
	 *
	 * @param idPath
	 * @param checkedFlag
	 * @return
	 */
	public synchronized boolean updatePathChecked(int idPath, CheckedFlag checkedFlag) {
		try {
			PreparedStatement stUpdateCheckedFlag = dbConn.connection.prepareStatement("UPDATE path set checked=? WHERE idPath=?");   //NOI18N

			stUpdateCheckedFlag.setInt(1, checkedFlag.getValue());
			stUpdateCheckedFlag.setInt(2, idPath);
			int nbRowsAffected = stUpdateCheckedFlag.executeUpdate();
			if (nbRowsAffected > 0) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateCheckedFlag, idPath={0}, checkedFlag={1} # row(s) affected: +{2}", new Object[]{idPath, checkedFlag, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("setCheckedFlag(" + idPath + "," + checkedFlag + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Resets the check flag to UNCHECKED on path table for given checked flag
	 *
	 * @param checkedFlag
	 * @return
	 */
	public synchronized boolean setCheckedFlagReset(CheckedFlag checkedFlag) {
		try {
			PreparedStatement stUpdateCheckedFlagReset
					= dbConn.connection.prepareStatement(
							"UPDATE path SET checked=0 "
							+ "WHERE checked=?");   //NOI18N
			stUpdateCheckedFlagReset.setInt(1, checkedFlag.getValue());
			stUpdateCheckedFlagReset.executeUpdate();
			//we can have no rows affected if library is empty so not checking it
			return true;
		} catch (SQLException ex) {
			Popup.error("setCheckedFlagReset()", ex);   //NOI18N
			return false;
		}
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="File & Path">
	/**
	 * Get number of given "field" (genre, year, artist, album) for stats
	 * display
	 *
	 * @param stats
	 * @param field
	 * @param selRatings
	 */
	public void getSelectionList4Stats(ArrayList<StatItem> stats, String field, boolean[] selRatings) {
		Statement st = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT COUNT(*), COUNT(DISTINCT P.idPath), SUM(size), "
					+ " \nSUM(length), avg(rating)," + field + " "
					+ " \nFROM file F JOIN path P ON P.idPath=F.idPath "
					+ " \nWHERE F.rating IN " + getCSVlist(selRatings)
					+ " \nGROUP BY " + field + " ORDER BY " + field;   //NOI18N //NOI18N

			st = dbConn.connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				stats.add(new StatItem(dbConn.getStringValue(rs, field),
						dbConn.getStringValue(rs, field),
						rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getLong(4),
						rs.getDouble(5), null));
			}
		} catch (SQLException ex) {
			Popup.error("getSelectionList4Stats(" + field + ")", ex);   //NOI18N
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

	/**
	 *
	 * @param stats
	 */
	public void getPercentRatedForStats(ArrayList<StatItem> stats) {
		Statement st = null;
		ResultSet rs = null;
		try {
			//Note Using "percent" as "%" is replaced by "-" because of decades. Now also replacing "percent" by "%"
			String sql = "SELECT count(*), COUNT(DISTINCT idPath), SUM(size), SUM(length), round(avg(albumRating),1 ) as [rating] , T.range as [percentRated] \n"
					+ "FROM (\n"
					+ "SELECT albumRating, size, length, P.idPath,\n"
					+ "	case  \n"
					+ "    when percentRated <  10						  then '0 -> 9 percent'\n"
					+ "    when percentRated >= 10 and percentRated < 25  then '10 -> 24 percent'\n"
					+ "    when percentRated >= 25 and percentRated < 50  then '25 -> 49 percent'\n"
					+ "    when percentRated >= 50 and percentRated < 75  then '50 -> 74 percent'\n"
					+ "    when percentRated >= 75 and percentRated < 100 then '75 -> 99 percent'\n"
					+ "    when percentRated == 100					      then 'x 100 percent x'\n"
					+ "    else 'kes' end as range\n"
					+ "  FROM file F "
					+ "  JOIN ( SELECT path.*, ifnull(round(((sum(case when rating > 0 then rating end))/(sum(case when rating > 0 then 1.0 end))), 1), 0) AS albumRating,\n"
					+ "			ifnull((sum(case when rating > 0 then 1.0 end) / count(*)*100), 0) AS percentRated\n"
					+ "		FROM path JOIN file ON path.idPath=file.idPath GROUP BY path.idPath ) \n"
					+ "P ON F.idPath=P.idPath ) T \n"
					+ "GROUP BY T.range ";

			st = dbConn.connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				stats.add(new StatItem(dbConn.getStringValue(rs, "percentRated"),
						dbConn.getStringValue(rs, "percentRated"),
						rs.getLong(1), rs.getLong(2), rs.getLong(3),
						rs.getLong(4), rs.getDouble(5), null));
			}
		} catch (SQLException ex) {
			Popup.error("getPercentRatedForStats()", ex);   //NOI18N
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

	/**
	 * Get files matching given genre, artist, album, ratings and checkedFlag
	 *
	 * @param myFileInfoList
	 * @param selGenre
	 * @param selArtist
	 * @param selAlbum
	 * @param selRatings
	 * @param selCheckedFlag
	 * @param yearFrom
	 * @param yearTo
	 * @param bpmFrom
	 * @param bpmTo
	 * @param copyRight
	 * @return
	 */
	public boolean getFiles(ArrayList<FileInfoInt> myFileInfoList, String selGenre,
			String selArtist, String selAlbum,
			boolean[] selRatings, boolean[] selCheckedFlag, int yearFrom, int yearTo,
			float bpmFrom, float bpmTo, int copyRight) {

		String sql = "SELECT F.*, P.strPath, P.checked, P.copyRight, 0 AS albumRating, "
				+ "0 AS percentRated, 'INFO' AS status, P.mbId AS pathMbId, P.modifDate AS pathModifDate, P.mbId AS pathMbId, P.modifDate AS pathModifDate " //NOI18N
				+ getSqlWHERE(selGenre, selArtist, selAlbum, selRatings,
						selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo, copyRight);

		return getFiles(myFileInfoList, sql);
	}

	/**
	 *
	 * @param idFile
	 * @param destExt
	 * @return
	 */
	public FileInfoInt getFile(int idFile, String destExt) {
		ArrayList<FileInfoInt> myFileInfoList = new ArrayList<>();
		String sql = "SELECT F.idFile, F.idPath, F.name, F.rating, "
				+ "F.lastPlayed, F.playCounter, F.addedDate, F.artist, "
				+ "F.album, F.albumArtist, F.title, F.trackNo, F.trackTotal, \n"
				+ "F.discNo, F.discTotal, F.genre, F.year, F.BPM, F.comment, "
				+ "F.nbCovers, F.coverHash, F.ratingModifDate, "
				+ "F.tagsModifDate, F.genreModifDate, F.saved, \n"
				+ "ifnull(T.bitRate, F.bitRate) AS bitRate, \n"
				+ "ifnull(T.format, F.format) AS format, \n"
				+ "ifnull(T.length, F.length) AS length, \n"
				+ "ifnull(T.size, F.size) AS size, \n"
				+ "ifnull(T.trackGain, F.trackGain) AS trackGain, \n"
				+ "ifnull(T.albumGain, F.albumGain) AS albumGain, \n"
				+ "ifnull(T.modifDate, F.modifDate) AS modifDate, T.ext, \n"
				+ "P.strPath, P.checked, P.copyRight, 0 AS albumRating, 0 AS percentRated, "
				+ "'INFO' AS status, P.mbId AS pathMbId, P.modifDate AS pathModifDate \n"
				+ "FROM file F \n"
				+ "LEFT JOIN fileTranscoded T ON T.idFile=F.idFile AND T.ext=\"" + destExt + "\" \n"
				+ "JOIN path P ON F.idPath=P.idPath "
				+ "WHERE F.idFile=" + idFile;
		getFiles(myFileInfoList, sql);
		return myFileInfoList.get(0);
	}

	/**
	 * Return list of files
	 *
	 * @param myFileInfoList
	 * @param sql
	 * @return
	 */
	public boolean getFiles(ArrayList<FileInfoInt> myFileInfoList, String sql) {
		return getFiles(myFileInfoList, sql, Jamuz.getMachine()
				.getOptionValue("location.library"));
	}

	/**
	 * Return list of files
	 *
	 * @param myFileInfoList
	 * @param sql
	 * @param rootPath
	 * @return
	 */
	public boolean getFiles(ArrayList<FileInfoInt> myFileInfoList, String sql,
			String rootPath) {

		int idFile;
		int idPath;
		String relativePath;
		String filename;
		int length;
		String format;
		String bitRate;
		int size;
		float BPM;
		String album;
		String albumArtist;
		String artist;
		String comment;
		int discNo;
		int discTotal;
		String genre;
		int nbCovers;
		String coverHash;
		String title;
		int trackNo;
		int trackTotal;
		String year;
		int playCounter;
		int rating;
		String addedDate;
		String lastPlayed;
		String modifDate;
		CheckedFlag checkedFlag;
		FolderInfo.CopyRight copyRight;
		double albumRating;
		int percentRated;
		SyncStatus status;
		String pathModifDate;
		String pathMbid;
		GainValues replayGain;

		myFileInfoList.clear();
		Statement st = null;
		ResultSet rs = null;
		long startTime = System.currentTimeMillis();
		try {
			//Execute query

			st = dbConn.connection.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				idFile = rs.getInt("idFile");   //NOI18N
				idPath = rs.getInt("idPath");   //NOI18N
				checkedFlag = CheckedFlag.values()[rs.getInt("checked")];  //NOI18N
				copyRight = FolderInfo.CopyRight.values()[rs.getInt("copyRight")];

				//Path can be empty if file is on root folder
				relativePath = dbConn.getStringValue(rs, "strPath");   //NOI18N

				filename = dbConn.getStringValue(rs, "name");   //NOI18N
				length = rs.getInt("length");   //NOI18N
				format = dbConn.getStringValue(rs, "format");   //NOI18N
				bitRate = dbConn.getStringValue(rs, "bitRate");   //NOI18N
				size = rs.getInt("size");   //NOI18N
				BPM = rs.getFloat("BPM");   //NOI18N
				album = dbConn.getStringValue(rs, "album");   //NOI18N
				albumArtist = dbConn.getStringValue(rs, "albumArtist");   //NOI18N
				artist = dbConn.getStringValue(rs, "artist");   //NOI18N
				comment = dbConn.getStringValue(rs, "comment");   //NOI18N
				discNo = rs.getInt("discNo");   //NOI18N
				discTotal = rs.getInt("discTotal");   //NOI18N
				genre = dbConn.getStringValue(rs, "genre");   //NOI18N
				nbCovers = rs.getInt("nbCovers");   //NOI18N
				coverHash = dbConn.getStringValue(rs, "coverHash");  //NOI18N
				title = dbConn.getStringValue(rs, "title");   //NOI18N
				trackNo = rs.getInt("trackNo");   //NOI18N
				trackTotal = rs.getInt("trackTotal");   //NOI18N
				year = dbConn.getStringValue(rs, "year");   //NOI18N
				playCounter = rs.getInt("playCounter");   //NOI18N
				rating = rs.getInt("rating");   //NOI18N
				addedDate = dbConn.getStringValue(rs, "addedDate");   //NOI18N
				lastPlayed = dbConn.getStringValue(rs, "lastPlayed");   //NOI18N
				modifDate = dbConn.getStringValue(rs, "modifDate");   //NOI18N
				albumRating = rs.getDouble("albumRating");
				percentRated = rs.getInt("percentRated");
				status = SyncStatus.valueOf(dbConn.getStringValue(rs, "status", "INFO"));
				pathModifDate = dbConn.getStringValue(rs, "pathModifDate");
				pathMbid = dbConn.getStringValue(rs, "pathMbid");
				replayGain = new GainValues(rs.getFloat("trackGain"), rs.getFloat("albumGain"));

				myFileInfoList.add(
						new FileInfoInt(idFile, idPath, relativePath, filename,
								length, format, bitRate, size, BPM, album,
								albumArtist, artist, comment, discNo, discTotal,
								genre, nbCovers, title, trackNo, trackTotal,
								year, playCounter, rating, addedDate,
								lastPlayed, modifDate, coverHash,
								checkedFlag, copyRight, albumRating,
								percentRated, rootPath, status, pathModifDate,
								pathMbid, replayGain)
				);
			}
			return true;
		} catch (SQLException ex) {
			Popup.error("getFileInfoList()", ex);   //NOI18N
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
				if (st != null) {
					st.close();
				}
			} catch (SQLException ex) {
				Jamuz.getLogger().warning("Failed to close Statement");
			}
			long endTime = System.currentTimeMillis();
			Jamuz.getLogger().log(Level.FINEST, "getFiles // Total execution time: {0}ms", new Object[]{endTime - startTime});    //NOI18N
		}
	}

	/**
	 *
	 * @param selGenre
	 * @param selArtist
	 * @param selAlbum
	 * @param selRatings
	 * @param selCheckedFlag
	 * @param yearFrom
	 * @param yearTo
	 * @param bpmFrom
	 * @param bpmTo
	 * @param copyRight
	 * @return
	 */
	public String getFilesStats(String selGenre, String selArtist, String selAlbum,
			boolean[] selRatings, boolean[] selCheckedFlag, int yearFrom, int yearTo,
			float bpmFrom, float bpmTo, int copyRight) {

		String sql = "SELECT COUNT(*) AS nbFiles, SUM(F.size) AS totalSize, "
				+ "SUM(F.length) AS totalLength " //NOI18N
				+ getSqlWHERE(selGenre, selArtist, selAlbum, selRatings,
						selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo,
						copyRight);

		return getFilesStats(sql);

//        getFiles(myFileInfoList, sql);
	}

	/**
	 *
	 * @param sql
	 * @return
	 */
	public String getFilesStats(String sql) {

		Statement st = null;
		ResultSet rs = null;
		int nbFiles;
		long totalSize;
		long totalLength;
		try {
			//Execute query
			st = dbConn.connection.createStatement();
			rs = st.executeQuery(sql);
			nbFiles = rs.getInt("nbFiles");   //NOI18N
			totalSize = rs.getLong("totalSize");   //NOI18N
			totalLength = rs.getLong("totalLength");   //NOI18N
			return nbFiles + " file(s)"
					+ " ; " + StringManager.humanReadableSeconds(totalLength)
					+ " ; " + StringManager.humanReadableByteCount(totalSize, false);
		} catch (SQLException ex) {
			Popup.error("getFileInfoList()", ex);   //NOI18N
			return "";
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

	/**
	 * Fill list of genre, artist or album
	 *
	 * @param myListModel
	 * @param field
	 * @param selArtist
	 * @param selGenre
	 * @param selAlbum
	 * @param selRatings
	 * @param selCheckedFlag
	 * @param yearFrom
	 * @param yearTo
	 * @param bpmFrom
	 * @param bpmTo
	 * @param copyRight
	 * @param sqlOrder
	 */
	public void fillSelectorList(DefaultListModel myListModel, String field,
			String selGenre, String selArtist, String selAlbum, boolean[] selRatings,
			boolean[] selCheckedFlag, int yearFrom, int yearTo, float bpmFrom,
			float bpmTo, int copyRight, String sqlOrder) {

		boolean[] allRatings = new boolean[6];
		Arrays.fill(allRatings, Boolean.TRUE);
		String sql;
		switch (field) {
			case "album":  //NOI18N
				sql = "SELECT checked, strPath, name, coverHash, album, artist, albumArtist, year, "
						+ "ifnull(round(((sum(case when rating > 0 then rating end))/(sum(case when rating > 0 then 1.0 end))), 1), 0) AS albumRating, "
						+ //NOI18N
						"ifnull((sum(case when rating > 0 then 1.0 end) / count(*)*100), 0) AS percentRated\n" //NOI18N
						//FIXME Z PanelSelect SELECT BY idPath (as grouped)
						//ex: Album "Charango" is either from Morcheeba or Yannick Noah
						//BUT files from both album are displayed in both cases
						//(WAS seen as only one album in Select tab, before group by idPath)
						+ getSqlWHERE(selGenre, selArtist, selAlbum, allRatings, selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo, copyRight);
				sql += " GROUP BY P.idPath ";
				sql += " HAVING (sum(case when rating IN " + getCSVlist(selRatings) + " then 1 end))>0 ";
				sql += " ORDER BY " + sqlOrder;
				break;
			case "artist":  //NOI18N
				sql = "SELECT albumArtist, strPath, name, coverHash, COUNT(F.idFile) AS nbFiles, COUNT(DISTINCT F.idPath) AS nbPaths, 'albumArtist' AS source, "
						+ "ifnull(round(((sum(case when rating > 0 then rating end))/(sum(case when rating > 0 then 1.0 end))), 1), 0) AS albumRating, "
						+ //NOI18N
						"ifnull((sum(case when rating > 0 then 1.0 end) / count(*)*100), 0) AS percentRated\n" //NOI18N
						+ getSqlWHERE(selGenre, selArtist, selAlbum, allRatings, selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo, copyRight) //NOI18N
						+ " GROUP BY albumArtist HAVING (sum(case when rating IN " + getCSVlist(selRatings) + " then 1 end))>0 ";  //NOI18N
				sql += " UNION ";  //NOI18N
				sql += "SELECT artist, strPath, name, coverHash, COUNT(F.idFile) AS nbFiles, COUNT(DISTINCT F.idPath) AS nbPaths, 'artist', " //NOI18N
						+ "ifnull(round(((sum(case when rating > 0 then rating end))/(sum(case when rating > 0 then 1.0 end))), 1), 0) AS albumRating, "
						+ //NOI18N
						"ifnull((sum(case when rating > 0 then 1.0 end) / count(*)*100), 0) AS percentRated\n" //NOI18N
						+ getSqlWHERE(selGenre, selArtist, selAlbum, allRatings, selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo, copyRight)
						+ " AND albumArtist='' GROUP BY " + field + " HAVING (sum(case when rating IN " + getCSVlist(selRatings) + " then 1 end))>0 "
						+ "ORDER BY " + sqlOrder;  //NOI18N
				myListModel.addElement(new ListElement("%", field));   //NOI18N
				field = "albumArtist"; //As known as this in recordset                //NOI18N
				break;
			default:
				sql = "SELECT " + field + " " //NOI18N
						+ getSqlWHERE(selGenre, selArtist, selAlbum, selRatings, selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo, copyRight)
						+ " GROUP BY " + field + " ORDER BY " + field;   //NOI18N //NOI18N
				myListModel.addElement("%");   //NOI18N
				break;
		}
		getListModel(myListModel, sql, field);

		if (field.equals("album") && myListModel.size() > 1) {
			myListModel.insertElementAt(new ListElement("%", field), 0);   //NOI18N
		}
	}

	private String getSqlWHERE(String selGenre, String selArtist, String selAlbum,
			boolean[] selRatings, boolean[] selCheckedFlag,
			int yearFrom, int yearTo, float bpmFrom, float bpmTo, int copyRight) {
		String sql = " \nFROM file F "
				+ //NOI18N
				" \nINNER JOIN `path` P ON P.idPath=F.idPath "
				+ //NOI18N
				" \nWHERE F.rating IN " + getCSVlist(selRatings)
				+ " \nAND P.checked IN " + getCSVlist(selCheckedFlag) //NOI18N
				//FIXME Z PanelSelect Check year valid and offer "allow invalid" as an option
				//https://stackoverflow.com/questions/5071601/how-do-i-use-regex-in-a-sqlite-query
				//				else if(yearList.get(0).matches("\\d{4}")) {  //NOI18N
				//					results.get("year").value=yearList.get(0);  //NOI18N
				//				}
				+ " \nAND ((F.year>=" + yearFrom + " AND F.year<=" + yearTo + ") OR length(F.year)!=4)" //NOI18N //NOI18N
				+ " \nAND F.BPM>=" + bpmFrom + " AND F.BPM<=" + bpmTo;    //NOI18N //NOI18N

		if (!selGenre.equals("%")) {  //NOI18N
			sql += " \nAND genre=\"" + selGenre + "\""; //NOI18N
		}
		if (!selArtist.equals("%")) {  //NOI18N
			sql += " \nAND (artist=\"" + escapeDoubleQuote(selArtist) + "\" "
					+ "OR albumArtist=\"" + escapeDoubleQuote(selArtist) + "\")";  //NOI18N
		}
		if (!selAlbum.equals("%")) {  //NOI18N
			sql += " \nAND album=\"" + escapeDoubleQuote(selAlbum) + "\""; //NOI18N
		}
		if (copyRight >= 0) {
			sql += " \nAND copyRight=" + copyRight + " ";//NOI18N;
		}

		sql += " ";  //NOI18N
		return sql;
	}

	private String escapeDoubleQuote(String text) {
		return text.replaceAll("\"", "\"\"");
	}

	/**
	 * Deletes a path.
	 *
	 * @param idPath
	 * @return
	 */
	public synchronized boolean deletePath(int idPath) {
		try {
			PreparedStatement stDeletedPath = dbConn.connection.prepareStatement(
					"DELETE FROM path WHERE idPath=?");   //NOI18N
			stDeletedPath.setInt(1, idPath);
			int nbRowsAffected = stDeletedPath.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stDeletedFiles, idPath={0} "
						+ "# row(s) affected: +{1}", new Object[]{idPath, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("deletePath(" + idPath + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Get given folder's files for scan/check
	 *
	 * @param files
	 * @param idPath
	 * @return
	 */
	public boolean getFiles(ArrayList<FileInfoInt> files, int idPath) {
		String sql = "SELECT F.*, P.strPath, P.checked, P.copyRight, "
				+ "0 AS albumRating, 0 AS percentRated, 'INFO' AS status, P.mbId AS pathMbId, P.modifDate AS pathModifDate "
				+ "FROM file F, path P "
				+ "WHERE F.idPath=P.idPath ";    //NOI18N
		if (idPath > -1) {
			sql += " AND P.idPath=" + idPath;   //NOI18N
		}
		return getFiles(files, sql);
	}

	/**
	 * Check if a similar album exist in JaMuz database
	 *
	 * @param myList
	 * @param album
	 * @param idPath
	 * @return
	 */
	public boolean checkAlbumSimilar(ArrayList<DuplicateInfo> myList,
			String album, int idPath) {
		if (!album.isBlank()) {
			try {

				PreparedStatement stSelectAlbumSimilar = dbConn.connection.prepareStatement(
						SELECT_DUPLICATE
						+ " WHERE album LIKE ? AND P.idPath!=?"
						+ GROUP_DUPLICATE);     //NOI18N
				stSelectAlbumSimilar.setString(1, "%" + album + "%");   //NOI18N
				stSelectAlbumSimilar.setInt(2, idPath);

				getDuplicates(myList, stSelectAlbumSimilar, 1);
				return true;
			} catch (SQLException ex) {
				Popup.error("checkSimilarAlbum(" + album + ")", ex);   //NOI18N
			}
		}
		return false;
	}

	private static final String SELECT_DUPLICATE
			= "SELECT album, albumArtist, checked, discNo, discTotal, "
			+ " ifnull(round(((sum(case when rating > 0 then rating end))"
			+ "/(sum(case when rating > 0 then 1.0 end))), 1), 0) AS albumRating, "
			+ " P.idPath, P.strPath, P.modifDate"
			+ " FROM path P JOIN file F ON F.idPath=P.idPath ";
	private static final String GROUP_DUPLICATE
			= " GROUP BY P.idPath, discNo";   //NOI18N

	/**
	 * Check if an exact album exists in JaMuz database
	 *
	 * @param myList
	 * @param album
	 * @param idPath
	 * @return
	 */
	public boolean checkAlbumExact(ArrayList<DuplicateInfo> myList, String album,
			int idPath) {
		if (!album.isBlank()) {
			try {
				PreparedStatement stSelectAlbumExact = dbConn.connection.prepareStatement(
						SELECT_DUPLICATE
						+ " WHERE album = ? AND P.idPath!=?"
						+ GROUP_DUPLICATE);     //NOI18N

				stSelectAlbumExact.setString(1, album);
				stSelectAlbumExact.setInt(2, idPath);
				getDuplicates(myList, stSelectAlbumExact, 1);
				return true;
			} catch (SQLException ex) {
				Popup.error("checkExactAlbum(" + album + ")", ex);   //NOI18N
			}
		}
		return false;
	}

	/**
	 * Check if a duplicate mbId exists in JaMuz database
	 *
	 * @param myList
	 * @param mbId
	 * @return
	 */
	public boolean checkAlbumDuplicate(
			ArrayList<DuplicateInfo> myList,
			String mbId) {
		if (mbId != null && !mbId.isBlank()) {    //NOI18N
			try {
				PreparedStatement stSelectDuplicates
						= dbConn.connection.prepareStatement(
								SELECT_DUPLICATE
								+ " WHERE mbId LIKE ? " //NOI18N
								+ " AND P.idPath!=? "
								+ GROUP_DUPLICATE);     //NOI18N

				stSelectDuplicates.setString(1, mbId);
				getDuplicates(myList, stSelectDuplicates, 2);
				return true;
			} catch (SQLException ex) {
				Popup.error("checkDuplicate(" + mbId + ")", ex);   //NOI18N
			}
		}
		return false;
	}

	/**
	 * Check if a duplicate artist/album with discNo/discTotal couple exists in
	 * JaMuz database
	 *
	 * @param myList
	 * @param albumArtist
	 * @param album
	 * @param idPath
	 * @param discNo
	 * @param discTotal
	 * @return
	 */
	public boolean checkAlbumDuplicate(ArrayList<DuplicateInfo> myList,
			String albumArtist, String album, int idPath, int discNo, int discTotal) {

		if (!albumArtist.isBlank() && !album.isBlank()) {    //NOI18N
			try {
				PreparedStatement stSelectDuplicates = dbConn.connection.prepareStatement(
						SELECT_DUPLICATE
						+ " WHERE albumArtist LIKE ? " //NOI18N
						+ " AND album LIKE ? "
						+ " AND P.idPath!=? "
						+ " AND discNo=? "
						+ " AND discTotal=? "
						+ GROUP_DUPLICATE);     //NOI18N

				stSelectDuplicates.setString(1, albumArtist);
				stSelectDuplicates.setString(2, album);
				stSelectDuplicates.setInt(3, idPath);
				stSelectDuplicates.setInt(4, discNo);
				stSelectDuplicates.setInt(5, discTotal);
				getDuplicates(myList, stSelectDuplicates, 2);
				return true;
			} catch (SQLException ex) {
				Popup.error("checkDuplicate(" + albumArtist + "," + album + ")", ex);   //NOI18N
			}
		}
		return false;
	}

	/**
	 * Check if a duplicate artist/album couple exists in JaMuz database
	 *
	 * @param myList
	 * @param albumArtist
	 * @param album
	 * @param idPath
	 * @return
	 */
	public boolean checkAlbumDuplicate(ArrayList<DuplicateInfo> myList,
			String albumArtist, String album, int idPath) {

		if (!albumArtist.isBlank() && !album.isBlank()) {    //NOI18N
			try {
				PreparedStatement stSelectDuplicates = dbConn.connection.prepareStatement(
						SELECT_DUPLICATE
						+ " WHERE albumArtist LIKE ? " //NOI18N
						+ " AND album LIKE ? AND P.idPath!=? "
						+ GROUP_DUPLICATE);     //NOI18N

				stSelectDuplicates.setString(1, albumArtist);
				stSelectDuplicates.setString(2, album);
				stSelectDuplicates.setInt(3, idPath);
				getDuplicates(myList, stSelectDuplicates, 1);
				return true;
			} catch (SQLException ex) {
				Popup.error("checkDuplicate(" + albumArtist + "," + album + ")", ex);   //NOI18N
			}
		}
		return false;
	}

	/**
	 * Return DuplicateInfo (for duplicate check)
	 *
	 * @param myFileInfoList
	 * @param selGenre
	 * @param selArtist
	 * @param selAlbum
	 */
	private void getDuplicates(ArrayList<DuplicateInfo> myList, PreparedStatement st,
			int errorlevel) {
		ResultSet rs = null;
		try {
			rs = st.executeQuery();
			String album;
			String albumArtist;
			double albumRating;
			CheckedFlag checkedFlag;
			int discNo;
			int discTotal;
			while (rs.next()) {
				album = dbConn.getStringValue(rs, "album");
				albumArtist = dbConn.getStringValue(rs, "albumArtist");
				albumRating = rs.getDouble("albumRating");
				checkedFlag = CheckedFlag.values()[rs.getInt("checked")];
				discNo = rs.getInt("discNo");
				discTotal = rs.getInt("discTotal");

				Date dbModifDate = DateTime.parseSqlUtc(
						dbConn.getStringValue(rs, "modifDate"));   //NOI18N
				String path = FilenameUtils.separatorsToSystem(
						dbConn.getStringValue(rs, "strPath"));   //NOI18N

				FolderInfo folderInfo = new FolderInfo(rs.getInt("idPath"),
						path, dbModifDate, CheckedFlag.values()[rs.getInt("checked")]);

				myList.add(new DuplicateInfo(album, albumArtist, albumRating, checkedFlag,
						errorlevel, discNo, discTotal, folderInfo));
			}
		} catch (SQLException ex) {
			Popup.error("getDuplicates(...)", ex);   //NOI18N
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
	 * Updates copyRight in path table
	 *
	 * @param idPath
	 * @param copyRight
	 * @return
	 */
	public synchronized boolean updatePathCopyRight(int idPath, int copyRight) {
		try {
			PreparedStatement stUpdateCopyRight = dbConn.connection.prepareStatement(
					"UPDATE path "
					+ "SET copyRight=? WHERE idPath=?");     //NOI18N
			stUpdateCopyRight.setInt(1, copyRight);
			stUpdateCopyRight.setInt(2, idPath);
			int nbRowsAffected = stUpdateCopyRight.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateCopyRight, idPath={0}, "
						+ "copyRight={1} # row(s) affected: +{2}",
						new Object[]{idPath, copyRight, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateCopyRight(" + idPath + ", " + copyRight + ")", ex);   //NOI18N
			return false;
		}
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="TagFile & File">
	/**
	 *
	 * @param files
	 * @param results
	 * @return
	 */
	public synchronized int[] updateFileTags(ArrayList<? extends FileInfo> files, int[] results) {
		int i = 0;
		for (FileInfo fileInfo : files) {
			if (fileInfo.getTags() != null) {
				//FIXME Z MERGE Update tags and date in the same transaction 
				//so it can be rolled back and probably faster
				if (!updateFileTags(fileInfo.getTags(), fileInfo.getIdFile())) {
					if (results != null) {
						results[i] = 0;
					}
				}
				if (!updateFileTagsModifDate(fileInfo)) {
					if (results != null) {
						results[i] = 0;
					}
				}
			}
			i++;
		}
		return results;
	}

	private synchronized boolean updateFileTags(ArrayList<String> tags, int idFile) {
		if (!deleteFileTags(idFile)) {
			return false;
		}
		return insertFileTags(tags, idFile);
	}

	private synchronized boolean insertFileTags(ArrayList<String> tags, int idFile) {
		try {
			if (tags.size() > 0) {
				dbConn.getConnnection().setAutoCommit(false);
				int[] results;
				PreparedStatement stInsertTagFile = dbConn.getConnnection()
						.prepareStatement(
								"INSERT OR IGNORE INTO tagFile "
								+ "(idFile, idTag) " //NOI18N
								+ "VALUES (?, (SELECT id FROM tag WHERE value=?))");   //NOI18N
				for (String tag : tags) {
					if (isTag(tag)) { //TODO: get id instead of using tag name
						stInsertTagFile.setInt(1, idFile);
						stInsertTagFile.setString(2, tag);
						stInsertTagFile.addBatch();
					}
				}
				long startTime = System.currentTimeMillis();
				results = stInsertTagFile.executeBatch();
				dbConn.getConnnection().commit();
				long endTime = System.currentTimeMillis();
				Jamuz.getLogger().log(Level.FINEST, "insertTagFiles UPDATE // {0} "
						+ "// Total execution time: {1}ms",
						new Object[]{results.length, endTime - startTime});    //NOI18N

				//Analyse results
				int result;
				for (int i = 0; i < results.length; i++) {
					result = results[i];
					if (result < 0) {
						Jamuz.getLogger().log(Level.SEVERE, "insertTagFiles, "
								+ "idFile={0} result={2}",
								new Object[]{idFile, result});   //NOI18N
					}
				}
				dbConn.getConnnection().setAutoCommit(true);
			}
			return true;
		} catch (SQLException ex) {
			Popup.error("insertTagFiles(" + idFile + ")", ex);   //NOI18N
			return false;
		}
	}

	private boolean isTag(String tag) {
		ResultSet rs = null;
		ResultSet keys = null;
		try {
			PreparedStatement stSelectMachine
					= dbConn.getConnnection().prepareStatement(
							"SELECT COUNT(*) FROM tag WHERE value=?");   //NOI18N
			stSelectMachine.setString(1, tag);
			rs = stSelectMachine.executeQuery();
			if (rs.getInt(1) > 0) {
				return true;
			} else {
				return insertTag(tag);
			}
		} catch (SQLException ex) {
			Popup.error("isTag(" + tag + ")", ex);   //NOI18N
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

	private synchronized boolean deleteFileTags(int idFile) {
		try {
			PreparedStatement stDeleteTagFiles = dbConn.getConnnection()
					.prepareStatement(
							"DELETE FROM tagFile WHERE idFile=?");  //NOI18N
			stDeleteTagFiles.setInt(1, idFile);
			long startTime = System.currentTimeMillis();
			int result = stDeleteTagFiles.executeUpdate();
			long endTime = System.currentTimeMillis();
			Jamuz.getLogger().log(Level.FINEST, "stDeleteTagFiles DELETE "
					+ "// Total execution time: {0}ms",
					new Object[]{endTime - startTime});    //NOI18N

			if (result < 0) {
				Jamuz.getLogger().log(Level.SEVERE, "stDeleteTagFiles, "
						+ "idFile={0}, result={1}",
						new Object[]{idFile, result});   //NOI18N
			}

			return true;

		} catch (SQLException ex) {
			Popup.error("deleteTagFiles()", ex);   //NOI18N
			return false;
		}
	}

	/**
	 * Update tagsModifDate
	 *
	 * @param fileInfo
	 * @return
	 */
	private synchronized boolean updateFileTagsModifDate(FileInfo fileInfo) {
		try {
			PreparedStatement stUpdateTagsModifDate = dbConn.getConnnection().prepareStatement(
					"UPDATE file SET tagsModifDate=datetime('now') "
					+ "WHERE idFile=?");  //NOI18N
			stUpdateTagsModifDate.setInt(1, fileInfo.getIdFile());
			int nbRowsAffected = stUpdateTagsModifDate.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateTagsModifDate, "
						+ "fileInfo={0} # row(s) affected: +{1}",
						new Object[]{fileInfo.toString(), nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateTagsModifDate(" + fileInfo.toString() + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 *
	 * @param tags
	 * @param idFile
	 * @return
	 */
	public boolean getTags(ArrayList<String> tags, int idFile) {
		try {
			PreparedStatement stSelectTags = dbConn.connection.prepareStatement(
					"SELECT value FROM tag T JOIN tagFile F ON T.id=F.idTag "
					+ "WHERE F.idFile=?");    //NOI18N
			stSelectTags.setInt(1, idFile);
			ResultSet rs = stSelectTags.executeQuery();
			while (rs.next()) {
				tags.add(dbConn.getStringValue(rs, "value"));
			}
			return true;
		} catch (SQLException ex) {
			Popup.error("getTags(" + idFile + ")", ex);   //NOI18N
			return false;
		}
	}

	/**
	 *
	 * @param tags
	 * @param file
	 * @return
	 */
	@Override
	public boolean getTags(ArrayList<String> tags, FileInfo file) {
		return getTags(tags, file.getIdFile());
	}

	// </editor-fold>
	
	// <editor-fold defaultstate="collapsed" desc="Misc">
	private void getListModel(DefaultListModel myListModel, String sql, String field) {
		ResultSet rs = null;
		Statement st = null;
		try {
			st = dbConn.connection.createStatement();
			rs = st.executeQuery(sql);
			Object elementToAdd;
			String rating;
			int percentRated;
			while (rs.next()) {
				elementToAdd = dbConn.getStringValue(rs, field);
				switch (field) {
					case "album":  //NOI18N
						int checked = rs.getInt("checked");  //NOI18N
						String album = (String) elementToAdd;
						String albumArtist = dbConn.getStringValue(rs, "albumArtist") + "<BR/>";  //NOI18N
						String year = dbConn.getStringValue(rs, "year");  //NOI18N
						if (albumArtist.isBlank()) {  //NOI18N
							albumArtist = dbConn.getStringValue(rs, "artist");  //NOI18N
						}

						percentRated = rs.getInt("percentRated");  //NOI18N
						rating = "[" + rs.getDouble("albumRating") + "]";// + "/5]";
						if (percentRated != 100) {
							int errorLevel = 2;
							if (percentRated > 50 && percentRated < 100) {
								errorLevel = 1;
							}
							rating = FolderInfoResult.colorField(rating, errorLevel, false);
						}
						if (checked > 0) {
							album = FolderInfoResult.colorField(album, (3 - checked), false);
						}
						ListElement albumElement = makeListElement(elementToAdd, rs);
						albumElement.setDisplay("<html>" + year + " <b>" + album + "</b> " + rating + "<BR/>" + albumArtist + "</html>");  //NOI18N
						elementToAdd = albumElement;
						break;
					case "albumArtist":  //NOI18N

						String source = dbConn.getStringValue(rs, "source");  //NOI18N

						String artist = (String) elementToAdd;
						if (source.equals("albumArtist")) {  //NOI18N
							artist = "<b>" + artist + "</b>";  //NOI18N
						}
						int nbFiles = rs.getInt("nbFiles");  //NOI18N
						int nbPaths = rs.getInt("nbPaths");  //NOI18N
						percentRated = rs.getInt("percentRated");  //NOI18N
						rating = " [" + rs.getDouble("albumRating") + "]";// + "/5]";
						if (percentRated != 100) {
							int errorLevel = 2;
							if (percentRated > 50 && percentRated < 100) {
								errorLevel = 1;
							}
							rating = FolderInfoResult.colorField(rating,
									errorLevel, false);
						}
						artist = "<html>" + artist + rating
								+ "<BR/>" + nbPaths + " "
								+ Inter.get("Tag.Album").toLowerCase(Locale.getDefault())
								+ "(s), " + nbFiles + " "
								+ Inter.get("Label.File").toLowerCase(Locale.getDefault())
								+ "(s)</html>";  //NOI18N
						ListElement artistElement = makeListElement(elementToAdd, rs);
						artistElement.setDisplay(artist);
						elementToAdd = artistElement;
						break;
					case "name": //that is for machine
						String name = (String) elementToAdd;
						elementToAdd = new ListElement(name, "<html>"
								+ "<b>" + name + "</b><BR/>"
								+ "<i>" + dbConn.getStringValue(rs, "description") + "</i>"
								+ "</html>");
					default:
						break;
				}
				myListModel.addElement(elementToAdd);
			}
		} catch (SQLException ex) {
			Popup.error("fillList(\"" + field + "\")", ex);   //NOI18N
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

	private ListElement makeListElement(Object elementToAdd, ResultSet rs) {
		FileInfoInt file = new FileInfoInt(dbConn.getStringValue(rs, "strPath")
				+ dbConn.getStringValue(rs, "name"), //NOI18N
				Jamuz.getMachine().getOptionValue("location.library"));   //NOI18N
		file.setCoverHash(dbConn.getStringValue(rs, "coverHash"));  //NOI18N
		file.nbCovers = 1;
		file.albumArtist = dbConn.getStringValue(rs, "albumArtist");  //NOI18N
		ListElement listElement = new ListElement((String) elementToAdd, file);
		return listElement;
	}

	/**
	 */
	public class StatItem {

		private final long countFile;
		private final long countPath;
		private long size = -1;
		private long length = -1;
		private double rating = -1;
		private float percentage = -1;
		private String label;
		private final String value;
		private final Color color;

		/**
		 *
		 * @param label
		 * @param value
		 * @param countFile
		 * @param size
		 * @param countPath
		 * @param length
		 * @param rating
		 * @param color
		 */
		public StatItem(String label, String value, long countFile,
				long countPath, long size, long length, double rating,
				Color color) {
			this.value = value;
			this.countFile = countFile;
			this.countPath = countPath;
			this.size = size;
			this.length = length;
			this.rating = rating;
			this.label = label;
			this.color = color;
		}

		/**
		 *
		 * @return
		 */
		public long getCountFile() {
			return countFile;
		}

		/**
		 *
		 * @return
		 */
		public long getCountPath() {
			return countPath;
		}

		/**
		 *
		 * @return
		 */
		public long getSize() {
			return size;
		}

		/**
		 *
		 * @return
		 */
		public long getLength() {
			return length;
		}

		/**
		 *
		 * @return
		 */
		public double getRating() {
			return rating;
		}

		/**
		 *
		 * @return
		 */
		public String getLabel() {
			return label;
		}

		/**
		 *
		 * @return
		 */
		public String getLabelForChart() {
			String newLabel = label;
			newLabel = newLabel.replaceAll("%", "-");
			newLabel = newLabel.replaceAll("percent", "%");
			return newLabel;
//            return label.replaceAll("%", "-").replaceAll("percent", "%");
		}

		/**
		 *
		 * @return
		 */
		public String getValue() {
			return value;
		}

		/**
		 *
		 * @return
		 */
		public float getPercentage() {
			return percentage;
		}

		/**
		 *
		 * @param percentage
		 */
		public void setPercentage(float percentage) {
			this.percentage = percentage;
		}

		/**
		 *
		 * @param label
		 */
		public void setLabel(String label) {
			this.label = label;
		}

		/**
		 *
		 * @return
		 */
		public Color getColor() {
			return color;
		}
	}

	private String getCSVlist(boolean[] values) {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		for (int i = 0; i < values.length; i++) {
			if (values[i]) {
				builder.append(i).append(",");
			}
		}
		builder.deleteCharAt(builder.length() - 1).append(") ");
		return builder.toString();
	}

	// </editor-fold>
	// <editor-fold defaultstate="collapsed" desc="Schema">
	
	boolean updateSchema(int requestedVersion) {
		ArrayList<Version> versions = new ArrayList<>();
		if(!getVersionHistory(versions)) {
			return false;
		}
		if(versions.size()<1) {
			return updateVersion(0, requestedVersion);
		}
		else {
			Optional<Version> currentVersion = versions.stream().filter((t) -> {
				return t.getUpgradeEnd().after(new Date(0));
			}).findFirst();
			if(!currentVersion.isPresent()) {
				return updateVersion(0, requestedVersion);
			} else {
				if(currentVersion.get().getVersion()==requestedVersion) {
					return true;
				}
				return updateVersion(currentVersion.get().getVersion(), requestedVersion);
			}
		}
	}
	
	private boolean updateVersion(int oldVersion, int newVersion) {
		Popup.info("Database requires an upgrade from version " + oldVersion + " to " + newVersion + ".\n\nDo not worry, a backup will be made.\nNo output is expected, just wait a little.\n\nClick OK to proceed.");
		getDbConn().info.backupDB(FilenameUtils.getFullPath(getDbConn().info.getLocationWork()));	
		for (int currentVersion = oldVersion; currentVersion < newVersion; currentVersion++) {
			int nextVersion = currentVersion + 1;
			if(oldVersion!=0 && !insertVersionHistory(nextVersion)) {
				return false;
			}
			if(!updateSchemaToNewVersion(nextVersion)) {
				return false;
			}
			if(oldVersion==0) {
				if(!insertVersionHistory(nextVersion)) {
					return false;
				}
			}
			if(!updateVersionHistory(nextVersion)) {
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
							+ "VALUES (?, datetime('now')) ");  //NOI18N
			stInsertVersionHistory.setInt(1,version);
			int nbRowsAffected = stInsertVersionHistory.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stInsertVersionHistory, fileInfo={0} # row(s) affected: +{1}", new Object[]{version, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("insertVersionHistory(" + version + ")", ex);   //NOI18N
			return false;
		}
	}
	
	private synchronized boolean updateVersionHistory(int version) {
		try {
			PreparedStatement stUpdateVersionHistory = dbConn.connection.prepareStatement(
					"UPDATE versionHistory SET upgradeEnd=datetime('now') "
					+ "WHERE version=?");  //NOI18N
			stUpdateVersionHistory.setInt(1,version);
			int nbRowsAffected = stUpdateVersionHistory.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateVersionHistory, fileInfo={0} # row(s) affected: +{1}", new Object[]{version, nbRowsAffected});   //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateVersionHistory(" + version + ")", ex);   //NOI18N
			return false;
		}
	}
	
	private boolean getVersionHistory(ArrayList<Version> versions) {
		Pair existsTableVersionHistory = existsTableVersionHistory();
		if(!(Boolean)existsTableVersionHistory.getKey()) {
			//Method did not succeeded.
			return false;
		}
		if(!(Boolean)existsTableVersionHistory.getValue()) {
			//Table does not exists.
			versions.add(new Version(0, new Date(0), new Date(0)));
			return true;
		}
		ResultSet rs = null;
		try {
			PreparedStatement stSelectVersionHistory = dbConn.connection.prepareStatement(
					"SELECT version, upgradeStart, upgradeEnd \n"
					+ "FROM versionHistory \n"
					+ "ORDER BY version DESC \n");
			rs = stSelectVersionHistory.executeQuery();
			while (rs.next()) {
				int version = rs.getInt("version");  //NOI18N		
				Date upgradeStart = DateTime.parseSqlUtc(rs.getString("upgradeStart"));
				Date upgradeEnd = DateTime.parseSqlUtc(rs.getString("upgradeEnd"));
				versions.add(new Version(version, upgradeStart, upgradeEnd));
			}
			return true;
		} catch (SQLException ex) {
			Popup.error("getVersionHistory", ex);   //NOI18N
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
			Popup.error("existsTableVersionHistory()", ex);   //NOI18N
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
	// </editor-fold>
}