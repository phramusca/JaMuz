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
import jamuz.StatItem;
import static jamuz.database.DbUtils.getCSVlist;
import static jamuz.database.DbUtils.getSqlWHERE;
import jamuz.process.check.FolderInfo;
import jamuz.process.check.ReplayGain;
import jamuz.process.sync.SyncStatus;
import jamuz.utils.Popup;
import jamuz.utils.StringManager;
import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author raph
 */
public class DaoFile {

    private final DbConn dbConn;
    private final DaoFileWrite daoFileWrite;
    private String locationLibrary;

    /**
     *
     * @param dbConn
     */
    public DaoFile(DbConn dbConn) {
        this.dbConn = dbConn;
        this.daoFileWrite = new DaoFileWrite(dbConn);
    }

    public void setLocationLibrary(String locationLibrary) {
        this.locationLibrary = locationLibrary;
    }

    /**
     * This is to reach writing operations (insert, update, delete)
     *
     * @return
     */
    public DaoFileWrite lock() {
        return daoFileWrite;
    }

    /**
     * Get the count of files based on the provided SQL query.
     *
     * @param sql
     * @return
     */
    public Integer getFilesCount(String sql) {
        try (Statement st = dbConn.connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return rs.getInt(1);
        } catch (SQLException ex) {
            Popup.error("getIdFileMax()", ex);
            return null;
        }
    }

    /**
     * Gets MIN or MAX year from audio files.
     *
     * @param maxOrMin
     * @return
     */
    public double getYear(String maxOrMin) {
        try (Statement st = dbConn.connection.createStatement(); ResultSet rs = st.executeQuery("SELECT " + maxOrMin + "(year) FROM file "
                + "WHERE year GLOB '[0-9][0-9][0-9][0-9]' AND length(year)=4")) {
            // FIXME ZZ PanelSelect better validate year (but regex is not available by
            // default :( )
            // To exclude wrong entries (not YYYY format)
            return rs.getDouble(1);
        } catch (SQLException ex) {
            Popup.error("getYear(" + maxOrMin + ")", ex);
            return -1.0;
        }
    }

    /**
     * Get statistics on the given table (path or file).
     *
     * @param field
     * @param value
     * @param table
     * @param label
     * @param color
     * @param selRatings
     * @return
     */
    public StatItem getStatItem(String field, String value, String table, String label, Color color, boolean[] selRatings) {
        String sql;
        try {
            value = value.replaceAll("\"", "%");
            sql = "SELECT COUNT(*), COUNT(DISTINCT path.idPath), SUM(size), "
                    + "\nSUM(length), avg(rating) "
                    + "\nFROM file JOIN path ON path.idPath=file.idPath ";
            if (value.contains("IN (")) {
                sql += " \nWHERE " + table + "." + field + " " + value;
            } else if (value.startsWith(">")) {
                sql += " \nWHERE " + table + "." + field + value + "";
            } else if (value.contains("%")) {
                sql += " \nWHERE " + table + "." + field + " LIKE \"" + value + "\"";
            } else {
                sql += " \nWHERE " + table + "." + field + "='" + value + "'";
            }
            if (selRatings != null) {
                sql += " \nAND file.rating IN " + getCSVlist(selRatings);
            }

            try (Statement st = dbConn.connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                return new StatItem(label, value, rs.getLong(1), rs.getLong(2),
                        rs.getLong(3), rs.getLong(4), rs.getDouble(5), color);
            }
        } catch (SQLException ex) {
            Popup.error("getStatItem(" + field + "," + value + ")", ex);
            return new StatItem(label, value, -1, -1, -1, -1, -1, Color.BLACK);
        }
    }

    /**
     * Get the number of the given "field" (genre, year, artist, album) for
     * stats display.
     *
     * @param stats
     * @param field
     * @param selRatings
     */
    public void getSelectionList4Stats(ArrayList<StatItem> stats, String field, boolean[] selRatings) {
        try {
            String sql = "SELECT COUNT(*), COUNT(DISTINCT P.idPath), SUM(size), "
                    + " \nSUM(length), avg(rating)," + field + " "
                    + " \nFROM file F JOIN path P ON P.idPath=F.idPath "
                    + " \nWHERE F.rating IN " + getCSVlist(selRatings)
                    + " \nGROUP BY " + field + " ORDER BY " + field; // NOI18N //NOI18N

            try (Statement st = dbConn.connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    stats.add(new StatItem(dbConn.getStringValue(rs, field),
                            dbConn.getStringValue(rs, field),
                            rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getLong(4),
                            rs.getDouble(5), null));
                }
            }
        } catch (SQLException ex) {
            Popup.error("getSelectionList4Stats(" + field + ")", ex);
        }
    }

    /**
     * Get the percentage of rated items for stats.
     *
     * @param stats
     */
    public void getPercentRatedForStats(ArrayList<StatItem> stats) {
        try {
            // Note: Using "percent" as "%" is replaced by "-" because of decades. Now also
            // replacing "percent" by "%"
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

            try (Statement st = dbConn.connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    String label = dbConn.getStringValue(rs, "percentRated");
                    Color color = Color.WHITE;
                    switch (label) {
                        case "0 -> 9 percent":
                            color = new Color(233, 76, 18);
                            break;
                        case "10 -> 24 percent":
                            color = new Color(233, 183, 18);
                            break;
                        case "25 -> 49 percent":
                            color = new Color(212, 233, 18);
                            break;
                        case "50 -> 74 percent":
                            color = new Color(153, 255, 153);
                            break;
                        case "75 -> 99 percent":
                            color = new Color(51, 255, 51);
                            break;
                        case "x 100 percent x":
                            color = new Color(0, 195, 0);
                            break;
                    }
                    stats.add(new StatItem(label, label,
                            rs.getLong(1), rs.getLong(2), rs.getLong(3),
                            rs.getLong(4), rs.getDouble(5), color));
                }
            }
        } catch (SQLException ex) {
            Popup.error("getPercentRatedForStats()", ex);
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
                + "0 AS percentRated, 'INFO' AS status, P.mbId AS pathMbId, P.modifDate AS pathModifDate, P.mbId AS pathMbId, P.modifDate AS pathModifDate " // NOI18N
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
        return getFiles(myFileInfoList, sql, locationLibrary);
    }

    /**
     * Return list of files
     *
     * @param myFileInfoList
     * @param sql
     * @param rootPath
     * @return
     */
    public boolean getFiles(ArrayList<FileInfoInt> myFileInfoList, String sql, String rootPath) {
        myFileInfoList.clear();
        try (Statement st = dbConn.connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                int idFile = rs.getInt("idFile"); // NOI18N
                int idPath = rs.getInt("idPath"); // NOI18N
                FolderInfo.CheckedFlag checkedFlag = FolderInfo.CheckedFlag.values()[rs.getInt("checked")]; // NOI18N
                FolderInfo.CopyRight copyRight = FolderInfo.CopyRight.values()[rs.getInt("copyRight")];

                // Path can be empty if file is on root folder
                String relativePath = dbConn.getStringValue(rs, "strPath"); // NOI18N

                String filename = dbConn.getStringValue(rs, "name"); // NOI18N
                int length = rs.getInt("length"); // NOI18N
                String format = dbConn.getStringValue(rs, "format"); // NOI18N
                String bitRate = dbConn.getStringValue(rs, "bitRate"); // NOI18N
                int size = rs.getInt("size"); // NOI18N
                float BPM = rs.getFloat("BPM"); // NOI18N
                String album = dbConn.getStringValue(rs, "album"); // NOI18N
                String albumArtist = dbConn.getStringValue(rs, "albumArtist"); // NOI18N
                String artist = dbConn.getStringValue(rs, "artist"); // NOI18N
                String comment = dbConn.getStringValue(rs, "comment"); // NOI18N
                int discNo = rs.getInt("discNo"); // NOI18N
                int discTotal = rs.getInt("discTotal"); // NOI18N
                String genre = dbConn.getStringValue(rs, "genre"); // NOI18N
                int nbCovers = rs.getInt("nbCovers"); // NOI18N
                String coverHash = dbConn.getStringValue(rs, "coverHash"); // NOI18N
                String title = dbConn.getStringValue(rs, "title"); // NOI18N
                int trackNo = rs.getInt("trackNo"); // NOI18N
                int trackTotal = rs.getInt("trackTotal"); // NOI18N
                String year = dbConn.getStringValue(rs, "year"); // NOI18N
                int playCounter = rs.getInt("playCounter"); // NOI18N
                int rating = rs.getInt("rating"); // NOI18N
                String addedDate = dbConn.getStringValue(rs, "addedDate"); // NOI18N
                String lastPlayed = dbConn.getStringValue(rs, "lastPlayed"); // NOI18N
                String modifDate = dbConn.getStringValue(rs, "modifDate"); // NOI18N
                double albumRating = rs.getDouble("albumRating");
                int percentRated = rs.getInt("percentRated");
                SyncStatus status = SyncStatus.valueOf(dbConn.getStringValue(rs, "status", "INFO"));
                String pathModifDate = dbConn.getStringValue(rs, "pathModifDate");
                String pathMbid = dbConn.getStringValue(rs, "pathMbid");
                ReplayGain.GainValues replayGain = new ReplayGain.GainValues(rs.getFloat("trackGain"), rs.getFloat("albumGain"));

                myFileInfoList.add(new FileInfoInt(idFile, idPath, relativePath, filename,
                        length, format, bitRate, size, BPM, album,
                        albumArtist, artist, comment, discNo, discTotal,
                        genre, nbCovers, title, trackNo, trackTotal,
                        year, playCounter, rating, addedDate,
                        lastPlayed, modifDate, coverHash,
                        checkedFlag, copyRight, albumRating,
                        percentRated, rootPath, status, pathModifDate,
                        pathMbid, replayGain));
            }
            return true;
        } catch (SQLException ex) {
            Popup.error("getFileInfoList()", ex); // NOI18N
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
                + "WHERE F.idPath=P.idPath "; // NOI18N
        if (idPath > -1) {
            sql += " AND P.idPath=" + idPath; // NOI18N
        }
        return getFiles(files, sql);
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
                + "SUM(F.length) AS totalLength " // NOI18N
                + getSqlWHERE(selGenre, selArtist, selAlbum, selRatings,
                        selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo,
                        copyRight);

        return getFilesStats(sql);

        // getFiles(myFileInfoList, sql);
    }

    /**
     * Retrieve file statistics based on the provided SQL query.
     *
     * @param sql The SQL query to execute.
     * @return A string representation of file statistics.
     */
    public String getFilesStats(String sql) {
        try (Statement st = dbConn.connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            int nbFiles = rs.getInt("nbFiles"); // NOI18N
            long totalSize = rs.getLong("totalSize"); // NOI18N
            long totalLength = rs.getLong("totalLength"); // NOI18N

            return String.format("%d file(s); %s; %s",
                    nbFiles,
                    StringManager.humanReadableSeconds(totalLength),
                    StringManager.humanReadableByteCount(totalSize, false));

        } catch (SQLException ex) {
            Popup.error("getFilesStats()", ex); // NOI18N
            return "";
        }
    }

}
