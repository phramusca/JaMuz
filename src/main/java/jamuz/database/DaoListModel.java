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

import jamuz.FileInfoInt;
import jamuz.Jamuz;
import static jamuz.database.DbUtils.getCSVlist;
import static jamuz.database.DbUtils.getSqlWHERE;
import jamuz.gui.swing.ListElement;
import jamuz.process.check.FolderInfoResult;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Locale;
import javax.swing.DefaultListModel;

/**
 *
 * @author raph
 */
public class DaoListModel {
    private final DbConn dbConn;

	/**
	 *
	 * @param dbConn
	 */
	public DaoListModel(DbConn dbConn) {
		this.dbConn = dbConn;
	}
    
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
		getListModel(myListModel, sql, "name");
	}
    
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
}
