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
    private String locationLibrary;

    /**
     *
     * @param dbConn
     */
    public DaoListModel(DbConn dbConn) {
        this.dbConn = dbConn;
    }

    public void setLocationLibrary(String locationLibrary) {
        this.locationLibrary = locationLibrary;
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
        try (Statement st = dbConn.connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Object elementToAdd = null;
                switch (field) {
                    case "album": // NOI18N
                        elementToAdd = handleAlbumField(rs);
                        break;
                    case "albumArtist": // NOI18N
                        elementToAdd = handleAlbumArtistField(rs);
                        break;
                    case "name": // that is for machine
                        elementToAdd = handleNameField(rs);
                        break;
                    default:
                        break;
                }
                if (elementToAdd != null) {
                    myListModel.addElement(elementToAdd);
                }
            }
        } catch (SQLException ex) {
            Popup.error("fillList(\"" + field + "\")", ex); // NOI18N
        }
    }

    private Object handleAlbumField(ResultSet rs) throws SQLException {
        int checked = rs.getInt("checked"); // NOI18N
        String album = dbConn.getStringValue(rs, "album"); // NOI18N
        String albumArtist = dbConn.getStringValue(rs, "albumArtist") + "<BR/>"; // NOI18N
        String year = dbConn.getStringValue(rs, "year"); // NOI18N

        if (albumArtist.isBlank()) { // NOI18N
            albumArtist = dbConn.getStringValue(rs, "artist"); // NOI18N
        }

        int percentRated = rs.getInt("percentRated"); // NOI18N
        String rating = "[" + rs.getDouble("albumRating") + "]";// + "/5]";

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

        ListElement albumElement = makeListElement(album, rs);
        albumElement.setDisplay("<html>" + year + " <b>" + album + "</b> " + rating + "<BR/>"
                + albumArtist + "</html>"); // NOI18N
        return albumElement;
    }

    private Object handleAlbumArtistField(ResultSet rs) throws SQLException {
        String source = dbConn.getStringValue(rs, "source"); // NOI18N
        String artist = dbConn.getStringValue(rs, "albumArtist");

        if (source.equals("albumArtist")) { // NOI18N
            artist = "<b>" + artist + "</b>"; // NOI18N
        }

        int nbFiles = rs.getInt("nbFiles"); // NOI18N
        int nbPaths = rs.getInt("nbPaths"); // NOI18N
        int percentRated = rs.getInt("percentRated"); // NOI18N
        String rating = " [" + rs.getDouble("albumRating") + "]";// + "/5]";

        if (percentRated != 100) {
            int errorLevel = 2;
            if (percentRated > 50 && percentRated < 100) {
                errorLevel = 1;
            }
            rating = FolderInfoResult.colorField(rating, errorLevel, false);
        }

        artist = "<html>" + artist + rating
                + "<BR/>" + nbPaths + " "
                + Inter.get("Tag.Album").toLowerCase(Locale.getDefault())
                + "(s), " + nbFiles + " "
                + Inter.get("Label.File").toLowerCase(Locale.getDefault())
                + "(s)</html>"; // NOI18N

        ListElement artistElement = makeListElement(artist, rs);
        artistElement.setDisplay(artist);
        return artistElement;
    }

    private Object handleNameField(ResultSet rs) throws SQLException {
        String name = dbConn.getStringValue(rs, "name");
        return new ListElement(name, "<html>"
                + "<b>" + name + "</b><BR/>"
                + "<i>" + dbConn.getStringValue(rs, "description") + "</i>"
                + "</html>");
    }

    private ListElement makeListElement(Object elementToAdd, ResultSet rs) throws SQLException {
        FileInfoInt file = new FileInfoInt(dbConn.getStringValue(rs, "strPath")
                + dbConn.getStringValue(rs, "name"), locationLibrary); // NOI18N
        file.setCoverHash(dbConn.getStringValue(rs, "coverHash")); // NOI18N
        file.setNbCovers(1);
        file.setAlbumArtist(dbConn.getStringValue(rs, "albumArtist")); // NOI18N
        return new ListElement((String) elementToAdd, file);
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
