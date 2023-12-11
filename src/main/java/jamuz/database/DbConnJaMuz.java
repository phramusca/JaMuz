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

import jamuz.FileInfo;
import jamuz.FileInfoInt;
import jamuz.Jamuz;
import static jamuz.database.DbUtils.getCSVlist;
import static jamuz.database.DbUtils.getSqlWHERE;
import jamuz.gui.swing.ListElement;
import jamuz.process.check.FolderInfoResult;
import jamuz.process.merge.StatSource;
import jamuz.utils.DateTime;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Level;
import javax.swing.DefaultListModel;

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
    private final DaoOption daoOption;
    private final DaoPathAlbum daoPathAlbum;

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
        daoPathAlbum = new DaoPathAlbum(dbConn);
        daoPlayCounter = new DaoPlayCounter(dbConn);
        daoOption = new DaoOption(dbConn);
        
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

    public DaoOption option() {
        return daoOption;
    }

    public DaoPathAlbum album() {
        return daoPathAlbum;
    }

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
	public void setUpdateStatisticsParameters(FileInfo file) throws SQLException {
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
    
    @Override
	public boolean getTags(ArrayList<String> tags, FileInfo file) {
		return daoFileTag.get(tags, file.getIdFile());
	}

	// </editor-fold>
}
