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
import jamuz.gui.swing.ListElement;
import jamuz.process.check.FolderInfoResult;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private final DaoFile daoFile;
    private final boolean[] allRatings = new boolean[6];

    /**
     *
     * @param dbConn
     * @param daoFile
     */
    public DaoListModel(DbConn dbConn, DaoFile daoFile) {
        this.dbConn = dbConn;
        this.daoFile = daoFile;
        Arrays.fill(allRatings, Boolean.TRUE);

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
        try (PreparedStatement ps = dbConn.connection.prepareStatement(sql)) {
            getListModel(myListModel, ps, field);
        } catch (SQLException ex) {
            Popup.error("getFilesStats()", ex);
        }
    }

    private void getListModel(DefaultListModel myListModel, PreparedStatement ps, String field) {

        try (ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object elementToAdd = dbConn.getStringValue(rs, field);
                switch (field) {
                    case "album": // NOI18N
                        elementToAdd = handleAlbumField(rs, elementToAdd);
                        break;
                    case "albumArtist": // NOI18N
                        elementToAdd = handleAlbumArtistField(rs, elementToAdd);
                        break;
                    case "name": // that is for machine
                        elementToAdd = handleNameField(rs, elementToAdd);
                        break;
                    default:
                        break;
                }
                myListModel.addElement(elementToAdd);
            }
        } catch (SQLException ex) {
            Popup.error("fillList(\"" + field + "\")", ex); // NOI18N
        }
    }

    private Object handleAlbumField(ResultSet rs, Object elementToAdd) throws SQLException {
        int checked = rs.getInt("checked"); // NOI18N
        String album = (String) elementToAdd;
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

        ListElement albumElement = makeListElement(elementToAdd, rs);
        albumElement.setDisplay("<html>" + year + " <b>" + album + "</b> " + rating + "<BR/>"
                + albumArtist + "</html>"); // NOI18N
        return albumElement;
    }

    private Object handleAlbumArtistField(ResultSet rs, Object elementToAdd) throws SQLException {
        String source = dbConn.getStringValue(rs, "source"); // NOI18N
        String artist = (String) elementToAdd;

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

        ListElement artistElement = makeListElement(elementToAdd, rs);
        artistElement.setDisplay(artist);
        return artistElement;
    }

    private Object handleNameField(ResultSet rs, Object elementToAdd) throws SQLException {
        String name = (String) elementToAdd;
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
     * Fill list of genre, artist, or album
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

        String sql;
        switch (field) {
            case "album":
                sql = buildAlbumSql(selGenre, selArtist, selAlbum, selRatings,
                        selCheckedFlag, copyRight, sqlOrder);
                break;
            case "artist":
                sql = buildArtistSql(selGenre, selArtist, selAlbum, selRatings,
                        selCheckedFlag, copyRight, sqlOrder);
                myListModel.addElement(new ListElement("%", field)); // NOI18N
                field = "albumArtist"; // As known as this in recordset //NOI18N
                break;
            default:
                sql = buildDefaultSql(field, selGenre, selArtist, selAlbum,
                        selRatings, selCheckedFlag, copyRight);
                myListModel.addElement("%"); // NOI18N
                break;
        }

        try (PreparedStatement ps = prepareStatement4ListModel(sql, selGenre, selArtist, selAlbum, selRatings, selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo, copyRight, field)) {
            getListModel(myListModel, ps, field);
        } catch (SQLException ex) {
            Popup.error("getFilesStats()", ex);
        }

        if (field.equals("album") && myListModel.size() > 1) {
            myListModel.insertElementAt(new ListElement("%", field), 0); // NOI18N
        }
    }

    private PreparedStatement prepareStatement4ListModel(String sql, String selGenre, String selArtist, String selAlbum,
            boolean[] selRatings, boolean[] selCheckedFlag,
            int yearFrom, int yearTo, float bpmFrom, float bpmTo, int copyRight, String field) throws SQLException {

        PreparedStatement ps = dbConn.connection.prepareStatement(sql);
        int paramIndex = 1;

        switch (field) {
            case "album":
                paramIndex = daoFile.prepareStatement4SqlWhere(paramIndex, ps, selGenre, selArtist, selAlbum, allRatings, selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo, copyRight);
                daoFile.setCSVlist(ps, selRatings, paramIndex);
                break;
            case "albumArtist":
                paramIndex = daoFile.prepareStatement4SqlWhere(paramIndex, ps, selGenre, selArtist, selAlbum, allRatings, selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo, copyRight);
                paramIndex = daoFile.setCSVlist(ps, selRatings, paramIndex);
                paramIndex = daoFile.prepareStatement4SqlWhere(paramIndex, ps, selGenre, selArtist, selAlbum, allRatings, selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo, copyRight);
                daoFile.setCSVlist(ps, selRatings, paramIndex);
                break;
            default:
                daoFile.prepareStatement4SqlWhere(paramIndex, ps, selGenre, selArtist, selAlbum, selRatings, selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo, copyRight);
        }
        return ps;
    }

    private String buildAlbumSql(String selGenre, String selArtist, String selAlbum,
            boolean[] selRatings, boolean[] selCheckedFlag, int copyRight, String sqlOrder) {
        String sqlSelect = """
                          SELECT checked, strPath, name, coverHash, album, artist, albumArtist, year,
                          ifnull(round(((sum(case when rating > 0 then rating end))/(sum(case when rating > 0 then 1.0 end))), 1), 0) AS albumRating,
                          ifnull((sum(case when rating > 0 then 1.0 end) / count(*)*100), 0) AS percentRated
                          """;
        String sql = daoFile.getSqlWhere(sqlSelect, selGenre, selArtist, selAlbum, allRatings, selCheckedFlag, copyRight);
        sql += " GROUP BY P.idPath ";
        sql += " HAVING (sum(case when rating IN " + daoFile.getCSVlist(selRatings) + " then 1 end))>0 ";
        sql += " ORDER BY " + sqlOrder;
        return sql;
    }

    private String buildArtistSql(String selGenre, String selArtist, String selAlbum,
            boolean[] selRatings, boolean[] selCheckedFlag, int copyRight, String sqlOrder) {
        String sqlSelectAlbumArtist = """
                           SELECT albumArtist, strPath, name, coverHash, COUNT(F.idFile) AS nbFiles,
                           COUNT(DISTINCT F.idPath) AS nbPaths, 'albumArtist' AS source,
                           ifnull(round(((sum(case when rating > 0 then rating end))/(sum(case when rating > 0 then 1.0 end))), 1), 0) AS albumRating,
                           ifnull((sum(case when rating > 0 then 1.0 end) / count(*)*100), 0) AS percentRated
                           """;
        String sql = daoFile.getSqlWhere(sqlSelectAlbumArtist, selGenre, selArtist, selAlbum, allRatings, selCheckedFlag, copyRight)
                + " GROUP BY albumArtist HAVING (sum(case when rating IN " + daoFile.getCSVlist(selRatings)
                + " then 1 end))>0 ";
        sql += " \nUNION\n ";
        String sqlSelectArtist = """
                                SELECT artist, strPath, name, coverHash, COUNT(F.idFile) AS nbFiles,
                                COUNT(DISTINCT F.idPath) AS nbPaths, 'artist', 
                                ifnull(round(((sum(case when rating > 0 then rating end))/(sum(case when rating > 0 then 1.0 end))), 1), 0) AS albumRating,
                                ifnull((sum(case when rating > 0 then 1.0 end) / count(*)*100), 0) AS percentRated
                                """;
        sql += daoFile.getSqlWhere(sqlSelectArtist, selGenre, selArtist, selAlbum, allRatings, selCheckedFlag, copyRight)
                + " AND albumArtist='' GROUP BY artist HAVING (sum(case when rating IN "
                + daoFile.getCSVlist(selRatings) + " then 1 end))>0 "
                + "ORDER BY " + sqlOrder;
        return sql;
    }

    private String buildDefaultSql(String field, String selGenre, String selArtist,
            String selAlbum, boolean[] selRatings, boolean[] selCheckedFlag, int copyRight) {
        return daoFile.getSqlWhere("SELECT " + field + " ", selGenre, selArtist, selAlbum, selRatings, selCheckedFlag, copyRight)
                + " GROUP BY " + field + " ORDER BY " + field;
    }

}
