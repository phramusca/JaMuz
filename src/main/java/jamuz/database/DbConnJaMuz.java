/*
 * Copyright (C) 2011 phramusca <phramusca@gmail.com>
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

import jamuz.StatItem;
import jamuz.process.sync.SyncStatus;
import jamuz.FileInfo;
import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.Machine;
import jamuz.Option;
import static jamuz.database.DbUtils.getCSVlist;
import static jamuz.database.DbUtils.getSqlWHERE;
import jamuz.gui.swing.ListElement;
import jamuz.process.check.DuplicateInfo;
import jamuz.process.check.FolderInfo;
import jamuz.process.check.FolderInfo.CheckedFlag;
import jamuz.process.check.FolderInfoResult;
import jamuz.process.check.ReplayGain.GainValues;
import jamuz.process.merge.StatSource;
import jamuz.utils.DateTime;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import jamuz.utils.StringManager;
import java.awt.Color;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import javax.swing.DefaultListModel;
import org.apache.commons.io.FilenameUtils;

/**
 * Creates a new dbConn.connection to JaMuz database
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DbConnJaMuz extends StatSourceSQL {

	// TODO DB: How to log SQL generated queries ?
	// http://code.google.com/p/log4jdbc/
	// FIXME ZZZZZZ REVIEW code review database methods:
	// Check that nbRowsAffected is properly checked (==1 or >0 depending)
	// Check that all functions return a boolean and that this one is used
	// Check that batches are used whenever possible and needed
	// http://stackoverflow.com/questions/2467125/reusing-a-preparedstatement-multiple-times
	// Check that try/catch/return are all OK
	// - popup if exception (as it should not happen)
	// FIXME ZZZZZZ REVIEW: Internationalization
	// ( !! Test at each step !! )
	// 1 - Use BundleScanner to remove uneeded entries
	// 2 - Use NetBeans editor to merge duplicate entries if applicable
	// 3 - Use NetBeans internationalization tool
	// 4 - Run and manually find missing translations (en / fre)

	private PreparedStatement stSelectFilesStats4Source;
	private PreparedStatement stSelectFilesStats4SourceAndDevice;

	private final DaoGenre daoGenre;
	private final DaoTag daoTag;
	private final DaoFileTag daoFileTag;
	private final DaoMachine daoMachine;
	private final DaoPlaylist daoPlaylist;
	private final DaoDevice daoDevice;
	private final DaoClient daoClient;
	private final DaoStatSource daoStatSource;
	private final DaoSchema daoSchema;
	private final DaoDeviceFile daoDeviceFile;
    private final DaoFile daoFile;
    private final DaoFileTranscoded daoFileTranscoded;
    private final DaoPath daoPath;
    private final DaoPlayCounter daoPlayCounter;

	/**
	 * Creates a database dbConn.connection.
	 *
	 * @param dbInfo
	 */
	public DbConnJaMuz(DbInfo dbInfo) {
		super(dbInfo, "JaMuz", "", true, true, true, true, false, true);
		daoGenre = new DaoGenre(dbConn);
		daoTag = new DaoTag(dbConn);
		daoFileTag = new DaoFileTag(dbConn, daoTag);
		daoMachine = new DaoMachine(dbConn);
		daoPlaylist = new DaoPlaylist(dbConn);
		daoDevice = new DaoDevice(dbConn);
		daoStatSource = new DaoStatSource(dbConn);
		daoClient = new DaoClient(dbConn, daoDevice, daoStatSource);
		daoSchema = new DaoSchema(dbConn);
		daoDeviceFile = new DaoDeviceFile(dbConn);
        daoFile = new DaoFile(dbConn);
        daoFileTranscoded = new DaoFileTranscoded(dbConn);
        daoPath = new DaoPath(dbConn);
        daoPlayCounter = new DaoPlayCounter(dbConn);
	}

	public DaoGenre genre() {
		return daoGenre;
	}

	public DaoTag tag() {
		return daoTag;
	}

	public DaoFileTag fileTag() {
		return daoFileTag;
	}

	public DaoMachine machine() {
		return daoMachine;
	}

	public DaoPlaylist playlist() {
		return daoPlaylist;
	}

	public DaoDevice device() {
		return daoDevice;
	}

	public DaoDeviceFile deviceFile() {
		return daoDeviceFile;
	}
	
	public DaoClient client() {
		return daoClient;
	}

	public DaoStatSource statSource() {
		return daoStatSource;
	}
	
	public DaoSchema schema() {
		return daoSchema;
	}
    
    public DaoFile file() {
        return daoFile;
    }
    
    public DaoFileTranscoded fileTranscoded() {
        return daoFileTranscoded;
    }

    public DaoPath path() {
        return daoPath;
    }
    
    public DaoPlayCounter playCounter() {
        return daoPlayCounter;
    }
	

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
					+ "WHERE idMachine=? AND idOptionType=?"); // NOI18N

			for (Option option : selOptions.getOptions()) {
				if (option.getType().equals("path")
						&& !option.getValue().isBlank()) { // NOI18N
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
					new Object[]{results.length, endTime - startTime}); // NOI18N
			// Check results
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
			if (myOption.getType().equals("path")) { // NOI18N
				value = FilenameUtils.normalizeNoEndSeparator(value.trim()) + File.separator;
			}
			PreparedStatement stUpdateOption = dbConn.connection.prepareStatement("UPDATE option SET value=? "
					+ "WHERE idMachine=? AND idOptionType=?"); // NOI18N
			stUpdateOption.setString(1, value);
			stUpdateOption.setInt(2, myOption.getIdMachine());
			stUpdateOption.setInt(3, myOption.getIdOptionType());

			int nbRowsAffected = stUpdateOption.executeUpdate();
			if (nbRowsAffected > 0) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateOption, value={0}, idMachine={1}, "
						+ "idMachidOptionTypeine={2} # row(s) affected: +{3}", // NOI18N
						new Object[]{value, myOption.getIdMachine(), myOption.getIdOptionType(), nbRowsAffected});
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("setOption(" + myOption.toString() + "," + value + ")", ex); // NOI18N
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
					+ "FROM option O, optiontype OT, machine M " // NOI18N
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
						dbConn.getStringValue(rs, "type"))); // NOI18N
			}

			if (myOptions.size() <= 0) {
				Popup.warning(Inter.get("Error.NoOption") + " \"" + machineName + "\"."); // NOI18N //NOI18N
				return false;
			}

			return true;
		} catch (SQLException ex) {
			Popup.error("getOptions(\"" + machineName + "\")", ex); // NOI18N
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
					+ "WHERE name=?"); // NOI18N
			stSelectMachine.setString(1, hostname);
			rs = stSelectMachine.executeQuery();
			if (rs.getInt(1) > 0) {
				description.append(dbConn.getStringValue(rs, "description"));
				return true;
			} else {
				// Insert a new machine
				PreparedStatement stInsertMachine = dbConn.connection.prepareStatement(
						"INSERT INTO machine (name, hidden) VALUES (?, ?)"); // NOI18N
				stInsertMachine.setString(1, hostname);
				stInsertMachine.setBoolean(2, hidden);
				int nbRowsAffected = stInsertMachine.executeUpdate();
				if (nbRowsAffected == 1) {
					keys = stInsertMachine.getGeneratedKeys();
					keys.next();
					int idMachine = keys.getInt(1);
					rs.close();
					// Insert default options
					PreparedStatement stSelectOptionType = dbConn.connection.prepareStatement(
							"SELECT idOptionType, name, `default` "
							+ "FROM optiontype"); // NOI18N
					rs = stSelectOptionType.executeQuery();
					PreparedStatement stInsertOption = dbConn.connection.prepareStatement(
							"INSERT INTO option ('idMachine', 'idOptionType', "
							+ "'value') VALUES (?, ?, ?)"); // NOI18N
					while (rs.next()) {
						stInsertOption.setInt(1, idMachine);
						stInsertOption.setInt(2, rs.getInt("idOptionType")); // NOI18N
						stInsertOption.setString(3, dbConn.getStringValue(rs, "default")); // NOI18N
						nbRowsAffected = stInsertOption.executeUpdate();
						if (nbRowsAffected != 1) {
							Jamuz.getLogger().log(Level.SEVERE, "stInsertOption, "
									+ "idMachine={0}, idOptionType={1}, default=\"{2}\" "
									+ "# row(s) affected: +{1}",
									new Object[]{idMachine, rs.getInt("idOptionType"),
										dbConn.getStringValue(rs, "default"), nbRowsAffected}); // NOI18N
							return false;
						}
					}
					return true;
				} else {
					Jamuz.getLogger().log(Level.SEVERE, "stInsertMachine, "
							+ "hostname=\"{0}\" # row(s) affected: +{1}",
							new Object[]{hostname, nbRowsAffected}); // NOI18N
					return false;
				}
			}
		} catch (SQLException ex) {
			Popup.error("isMachine(" + hostname + ")", ex); // NOI18N
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

    // <editor-fold defaultstate="collapsed" desc="File & Path">
	

	

	/**
	 * Deletes a path.
	 *
	 * @param idPath
	 * @return
	 */
	public synchronized boolean deletePath(int idPath) {
		try {
			PreparedStatement stDeletedPath = dbConn.connection.prepareStatement(
					"DELETE FROM path WHERE idPath=?"); // NOI18N
			stDeletedPath.setInt(1, idPath);
			int nbRowsAffected = stDeletedPath.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stDeletedFiles, idPath={0} "
						+ "# row(s) affected: +{1}", new Object[]{idPath, nbRowsAffected}); // NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("deletePath(" + idPath + ")", ex); // NOI18N
			return false;
		}
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
						+ GROUP_DUPLICATE); // NOI18N
				stSelectAlbumSimilar.setString(1, "%" + album + "%"); // NOI18N
				stSelectAlbumSimilar.setInt(2, idPath);

				getDuplicates(myList, stSelectAlbumSimilar, 1);
				return true;
			} catch (SQLException ex) {
				Popup.error("checkSimilarAlbum(" + album + ")", ex); // NOI18N
			}
		}
		return false;
	}

	private static final String SELECT_DUPLICATE = "SELECT album, albumArtist, checked, discNo, discTotal, "
			+ " ifnull(round(((sum(case when rating > 0 then rating end))"
			+ "/(sum(case when rating > 0 then 1.0 end))), 1), 0) AS albumRating, "
			+ " P.idPath, P.strPath, P.modifDate"
			+ " FROM path P JOIN file F ON F.idPath=P.idPath ";
	private static final String GROUP_DUPLICATE = " GROUP BY P.idPath, discNo"; // NOI18N

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
						+ GROUP_DUPLICATE); // NOI18N

				stSelectAlbumExact.setString(1, album);
				stSelectAlbumExact.setInt(2, idPath);
				getDuplicates(myList, stSelectAlbumExact, 1);
				return true;
			} catch (SQLException ex) {
				Popup.error("checkExactAlbum(" + album + ")", ex); // NOI18N
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
		if (mbId != null && !mbId.isBlank()) { // NOI18N
			try {
				PreparedStatement stSelectDuplicates = dbConn.connection.prepareStatement(
						SELECT_DUPLICATE
						+ " WHERE mbId LIKE ? " // NOI18N
						+ " AND P.idPath!=? "
						+ GROUP_DUPLICATE); // NOI18N

				stSelectDuplicates.setString(1, mbId);
				getDuplicates(myList, stSelectDuplicates, 2);
				return true;
			} catch (SQLException ex) {
				Popup.error("checkDuplicate(" + mbId + ")", ex); // NOI18N
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

		if (!albumArtist.isBlank() && !album.isBlank()) { // NOI18N
			try {
				PreparedStatement stSelectDuplicates = dbConn.connection.prepareStatement(
						SELECT_DUPLICATE
						+ " WHERE albumArtist LIKE ? " // NOI18N
						+ " AND album LIKE ? "
						+ " AND P.idPath!=? "
						+ " AND discNo=? "
						+ " AND discTotal=? "
						+ GROUP_DUPLICATE); // NOI18N

				stSelectDuplicates.setString(1, albumArtist);
				stSelectDuplicates.setString(2, album);
				stSelectDuplicates.setInt(3, idPath);
				stSelectDuplicates.setInt(4, discNo);
				stSelectDuplicates.setInt(5, discTotal);
				getDuplicates(myList, stSelectDuplicates, 2);
				return true;
			} catch (SQLException ex) {
				Popup.error("checkDuplicate(" + albumArtist + "," + album + ")", ex); // NOI18N
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

		if (!albumArtist.isBlank() && !album.isBlank()) { // NOI18N
			try {
				PreparedStatement stSelectDuplicates = dbConn.connection.prepareStatement(
						SELECT_DUPLICATE
						+ " WHERE albumArtist LIKE ? " // NOI18N
						+ " AND album LIKE ? AND P.idPath!=? "
						+ GROUP_DUPLICATE); // NOI18N

				stSelectDuplicates.setString(1, albumArtist);
				stSelectDuplicates.setString(2, album);
				stSelectDuplicates.setInt(3, idPath);
				getDuplicates(myList, stSelectDuplicates, 1);
				return true;
			} catch (SQLException ex) {
				Popup.error("checkDuplicate(" + albumArtist + "," + album + ")", ex); // NOI18N
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
						dbConn.getStringValue(rs, "modifDate")); // NOI18N
				String path = FilenameUtils.separatorsToSystem(
						dbConn.getStringValue(rs, "strPath")); // NOI18N

				FolderInfo folderInfo = new FolderInfo(rs.getInt("idPath"),
						path, dbModifDate, CheckedFlag.values()[rs.getInt("checked")]);

				myList.add(new DuplicateInfo(album, albumArtist, albumRating, checkedFlag,
						errorlevel, discNo, discTotal, folderInfo));
			}
		} catch (SQLException ex) {
			Popup.error("getDuplicates(...)", ex); // NOI18N
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
					+ "SET copyRight=? WHERE idPath=?"); // NOI18N
			stUpdateCopyRight.setInt(1, copyRight);
			stUpdateCopyRight.setInt(2, idPath);
			int nbRowsAffected = stUpdateCopyRight.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateCopyRight, idPath={0}, "
						+ "copyRight={1} # row(s) affected: +{2}",
						new Object[]{idPath, copyRight, nbRowsAffected}); // NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateCopyRight(" + idPath + ", " + copyRight + ")", ex); // NOI18N
			return false;
		}
	}

	// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="TagFile & File">
	/**
	 *
	 * @param tags
	 * @param file
	 * @return
	 */
	@Override
	public boolean getTags(ArrayList<String> tags, FileInfo file) {
		return daoFileTag.get(tags, file.getIdFile());
	}

	// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Misc">
    
    /**
	 *
	 * @param myListModel
	 */
	public void getGenreListModel(DefaultListModel myListModel) {
		getListModel(myListModel, "SELECT value FROM genre ORDER BY value", "value");
	}

	/**
	 *
	 * @param myListModel
	 */
	public void getTagListModel(DefaultListModel myListModel) {
		getListModel(myListModel, "SELECT value FROM tag ORDER BY value", "value");
	}

	/**
	 * Fill option list
	 *
	 * @param myListModel
	 */
	public void getMachineListModel(DefaultListModel myListModel) {
		String sql = """
               SELECT name, description 
               FROM machine 
               WHERE hidden=0 
               ORDER BY name""";
		getListModel(myListModel, sql,
				"name");
	}
    
	public void getListModel(DefaultListModel myListModel, String sql, String field) {
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
					case "album": // NOI18N
						int checked = rs.getInt("checked"); // NOI18N
						String album = (String) elementToAdd;
						String albumArtist = dbConn.getStringValue(rs, "albumArtist") + "<BR/>"; // NOI18N
						String year = dbConn.getStringValue(rs, "year"); // NOI18N
						if (albumArtist.isBlank()) { // NOI18N
							albumArtist = dbConn.getStringValue(rs, "artist"); // NOI18N
						}

						percentRated = rs.getInt("percentRated"); // NOI18N
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
						albumElement.setDisplay("<html>" + year + " <b>" + album + "</b> " + rating + "<BR/>"
								+ albumArtist + "</html>"); // NOI18N
						elementToAdd = albumElement;
						break;
					case "albumArtist": // NOI18N

						String source = dbConn.getStringValue(rs, "source"); // NOI18N

						String artist = (String) elementToAdd;
						if (source.equals("albumArtist")) { // NOI18N
							artist = "<b>" + artist + "</b>"; // NOI18N
						}
						int nbFiles = rs.getInt("nbFiles"); // NOI18N
						int nbPaths = rs.getInt("nbPaths"); // NOI18N
						percentRated = rs.getInt("percentRated"); // NOI18N
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
								+ "(s)</html>"; // NOI18N
						ListElement artistElement = makeListElement(elementToAdd, rs);
						artistElement.setDisplay(artist);
						elementToAdd = artistElement;
						break;
					case "name": // that is for machine
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
			Popup.error("fillList(\"" + field + "\")", ex); // NOI18N
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
				+ dbConn.getStringValue(rs, "name"), // NOI18N
				Jamuz.getMachine().getOptionValue("location.library")); // NOI18N
		file.setCoverHash(dbConn.getStringValue(rs, "coverHash")); // NOI18N
		file.setNbCovers(1);
		file.setAlbumArtist(dbConn.getStringValue(rs, "albumArtist")); // NOI18N
		ListElement listElement = new ListElement((String) elementToAdd, file);
		return listElement;
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
			case "album": // NOI18N
				sql = "SELECT checked, strPath, name, coverHash, album, artist, albumArtist, year, "
						+ "ifnull(round(((sum(case when rating > 0 then rating end))/(sum(case when rating > 0 then 1.0 end))), 1), 0) AS albumRating, "
						+ // NOI18N
						"ifnull((sum(case when rating > 0 then 1.0 end) / count(*)*100), 0) AS percentRated\n" // NOI18N
						// FIXME Z PanelSelect SELECT BY idPath (as grouped)
						// ex: Album "Charango" is either from Morcheeba or Yannick Noah
						// BUT files from both album are displayed in both cases
						// (WAS seen as only one album in Select tab, before group by idPath)
						+ getSqlWHERE(selGenre, selArtist, selAlbum, allRatings, selCheckedFlag, yearFrom, yearTo,
								bpmFrom, bpmTo, copyRight);
				sql += " GROUP BY P.idPath ";
				sql += " HAVING (sum(case when rating IN " + getCSVlist(selRatings) + " then 1 end))>0 ";
				sql += " ORDER BY " + sqlOrder;
				break;
			case "artist": // NOI18N
				sql = "SELECT albumArtist, strPath, name, coverHash, COUNT(F.idFile) AS nbFiles, COUNT(DISTINCT F.idPath) AS nbPaths, 'albumArtist' AS source, "
						+ "ifnull(round(((sum(case when rating > 0 then rating end))/(sum(case when rating > 0 then 1.0 end))), 1), 0) AS albumRating, "
						+ // NOI18N
						"ifnull((sum(case when rating > 0 then 1.0 end) / count(*)*100), 0) AS percentRated\n" // NOI18N
						+ getSqlWHERE(selGenre, selArtist, selAlbum, allRatings, selCheckedFlag, yearFrom, yearTo,
								bpmFrom, bpmTo, copyRight) // NOI18N
						+ " GROUP BY albumArtist HAVING (sum(case when rating IN " + getCSVlist(selRatings)
						+ " then 1 end))>0 "; // NOI18N
				sql += " UNION "; // NOI18N
				sql += "SELECT artist, strPath, name, coverHash, COUNT(F.idFile) AS nbFiles, COUNT(DISTINCT F.idPath) AS nbPaths, 'artist', " // NOI18N
						+ "ifnull(round(((sum(case when rating > 0 then rating end))/(sum(case when rating > 0 then 1.0 end))), 1), 0) AS albumRating, "
						+ // NOI18N
						"ifnull((sum(case when rating > 0 then 1.0 end) / count(*)*100), 0) AS percentRated\n" // NOI18N
						+ getSqlWHERE(selGenre, selArtist, selAlbum, allRatings, selCheckedFlag, yearFrom, yearTo,
								bpmFrom, bpmTo, copyRight)
						+ " AND albumArtist='' GROUP BY " + field + " HAVING (sum(case when rating IN "
						+ getCSVlist(selRatings) + " then 1 end))>0 "
						+ "ORDER BY " + sqlOrder; // NOI18N
				myListModel.addElement(new ListElement("%", field)); // NOI18N
				field = "albumArtist"; // As known as this in recordset //NOI18N
				break;
			default:
				sql = "SELECT " + field + " " // NOI18N
						+ getSqlWHERE(selGenre, selArtist, selAlbum, selRatings, selCheckedFlag, yearFrom, yearTo,
								bpmFrom, bpmTo, copyRight)
						+ " GROUP BY " + field + " ORDER BY " + field; // NOI18N //NOI18N
				myListModel.addElement("%"); // NOI18N
				break;
		}
		getListModel(myListModel, sql, field);

		if (field.equals("album") && myListModel.size() > 1) {
			myListModel.insertElementAt(new ListElement("%", field), 0); // NOI18N
		}
	}

	// </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="For Merge Process">
	
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
			Jamuz.getLogger().log(Level.SEVERE, "getStatistics: " + statSource, ex); // NOI18N
			return false;
		}

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
			if (isRemote) {
				// We do not care to get original fullPath when merging with remote
				// Moreover it avoids an issue writing metadata, that is still present for other
				// merge types, see below
				fullPath = "(P.strPath || F.name)";
			} else {
				this.dbConn.connect();
				// TODO: Pb writing metadata after a change of fullPath (for instance when mp3
				// replaced by flac)
				// => Include both original path (for merge comparison) and current path on
				// JaMuz side (for file metadata updates)
				// => No need to have isRemote in setUp()
				fullPath = "D.oriRelativeFullPath";
			}

			stSelectFilesStats4SourceAndDevice = dbConn.getConnection().prepareStatement(
					"SELECT "
					+ "F.idFile, F.idPath, " + fullPath + " AS fullPath, "
					+ "F.rating, F.lastplayed, F.addedDate, F.playCounter, F.BPM, " // NOI18N
					+ "C.playcounter AS previousPlayCounter, " // NOI18N
					+ "F.ratingModifDate, F.tagsModifDate, F.genre, F.genreModifDate  " // NOI18N
					+ "FROM file F "
					+ "JOIN path P ON F.idPath=P.idPath " // NOI18N
					+ "JOIN devicefile D ON D.idFile=F.idFile "
					+ "LEFT OUTER JOIN (SELECT * FROM playcounter WHERE idStatSource=?) C "
					+ "ON F.idFile=C.idFile " // NOI18N //NOI18N
					+ "WHERE D.idDevice=? AND D.status!='INFO'");
			stSelectFilesStats4Source = dbConn.getConnection().prepareStatement(
					"SELECT "
					+ "F.idFile, F.idPath, (P.strPath || F.name) AS fullPath, "
					+ "F.rating, F.lastplayed, F.addedDate, F.playCounter, F.BPM, " // NOI18N
					+ "C.playcounter AS previousPlayCounter, " // NOI18N
					+ "F.ratingModifDate, F.tagsModifDate, F.genre, F.genreModifDate " // NOI18N
					+ "FROM file F "
					+ "JOIN path P ON F.idPath=P.idPath " // NOI18N
					+ "LEFT OUTER JOIN (SELECT * FROM playcounter WHERE idStatSource=?) C "
					+ "ON F.idFile=C.idFile ");

			this.stSelectFileStatistics = this.stSelectFilesStats4Source; // by default, but not to be called directly

			this.stUpdateFileStatistics = dbConn.connection.prepareStatement(
					"UPDATE file "
					+ "SET rating=?, bpm=?, lastplayed=?, addedDate=?, "
					+ "playCounter=?, ratingModifDate=?, genreModifDate=?, genre=? "
					+ "WHERE idFile=?");

			return true;
		} catch (SQLException ex) {
			// Proper error handling. We should not have such an error unless above code
			// changes
			Popup.error("setUp", ex); // NOI18N
			return false;
		}
	}

	/**
	 *
	 * @return
	 */
	@Override
	public boolean tearDown() {
		// this.dbConn.disconnect();
		// Never disconnecting from application database, no need (really ?)
		return true;
	}

	/**
	 * Return a FileInfo from given ResultSet
	 *
	 * @param rs
	 * @return
	 */
	@Override
	public FileInfo getFileStatistics(ResultSet rs) {
		try {
			// JaMuz database does not store rootPath in database, only relative one
			String relativeFullPath = dbConn.getStringValue(rs, "fullPath"); // NOI18N
			int rating = rs.getInt("rating"); // NOI18N
			String lastPlayed = dbConn.getStringValue(rs, "lastplayed", "1970-01-01 00:00:00"); // NOI18N
			String addedDate = dbConn.getStringValue(rs, "addedDate", "1970-01-01 00:00:00"); // NOI18N
			int playCounter = rs.getInt("playCounter"); // NOI18N
			float bpm = rs.getFloat("bpm");
			int previousPlayCounter = rs.getInt("previousPlayCounter"); // NOI18N
			String ratingModifDate = dbConn.getStringValue(rs, "ratingModifDate", "1970-01-01 00:00:00"); // NOI18N
			String tagsModifDate = dbConn.getStringValue(rs, "tagsModifDate", "1970-01-01 00:00:00"); // NOI18N
			String genreModifDate = dbConn.getStringValue(rs, "genreModifDate", "1970-01-01 00:00:00"); // NOI18N
			int idFile = rs.getInt("idFile"); // NOI18N
			int idPath = rs.getInt("idPath"); // NOI18N
			String genre = dbConn.getStringValue(rs, "genre"); // NOI18N

			return new FileInfo(idFile, idPath, relativeFullPath, rating,
					lastPlayed, addedDate, playCounter, this.getName(),
					previousPlayCounter, bpm, genre, ratingModifDate, tagsModifDate,
					genreModifDate);
		} catch (SQLException ex) {
			Popup.error("getStats", ex); // NOI18N
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
	public synchronized void setUpdateStatisticsParameters(FileInfo file) throws SQLException {
		this.stUpdateFileStatistics.setInt(1, file.getRating());
		this.stUpdateFileStatistics.setFloat(2, file.getBPM());
		this.stUpdateFileStatistics.setString(3, file.getFormattedLastPlayed());
		this.stUpdateFileStatistics.setString(4, file.getFormattedAddedDate());
		this.stUpdateFileStatistics.setInt(5, file.getPlayCounter());
		this.stUpdateFileStatistics.setString(6,
				file.isUpdateRatingModifDate()
						? DateTime.getCurrentUtcSql()
						: file.getFormattedRatingModifDate());
		this.stUpdateFileStatistics.setString(7,
				file.isUpdateRatingModifDate()
						? DateTime.getCurrentUtcSql()
						: file.getFormattedGenreModifDate());
		this.stUpdateFileStatistics.setString(8, file.getGenre());
		this.stUpdateFileStatistics.setInt(9, file.getIdFile());
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
		return daoFileTag.update(files, results);
	}

	// </editor-fold>
}
