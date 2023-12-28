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
import jamuz.process.check.FolderInfo;
import jamuz.process.check.ReplayGain;
import jamuz.process.sync.Device;
import jamuz.process.sync.SyncStatus;
import jamuz.utils.Popup;
import jamuz.utils.StringManager;
import java.awt.Color;
import java.sql.PreparedStatement;
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
     * Gets MIN or MAX year from audio files.
     *
     * @param maxOrMin
     * @return
     */
    public double getYear(String maxOrMin) {
        String query = "SELECT " + maxOrMin + "(year) FROM file WHERE year GLOB '[0-9][0-9][0-9][0-9]' AND length(year) = 4";

        try (PreparedStatement ps = dbConn.connection.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                // FIXME ZZ PanelSelect better validate year (but regex is not available by
                // default :( )
                // To exclude wrong entries (not YYYY format)
                return rs.getDouble(1);
            }
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
            sql = """
              SELECT COUNT(*), COUNT(DISTINCT path.idPath), SUM(size), 
              SUM(length), avg(rating) 
              FROM file JOIN path ON path.idPath=file.idPath """;

            if (value.startsWith(">")) {
                sql += " \nWHERE " + table + "." + field + value;
            } else if (value.contains("%")) {
                sql += " \nWHERE " + table + "." + field + " LIKE ?";
            } else {
                sql += " \nWHERE " + table + "." + field + "=?";
            }

            if (selRatings != null) {
                sql += " \nAND file.rating IN " + getCSVlist(selRatings);
            }

            try (PreparedStatement ps = dbConn.connection.prepareStatement(sql)) {
                if (value.contains("%")) {
                    ps.setString(1, value);
                } else if (!value.startsWith(">")) {
                    ps.setString(1, value);
                }
                if (selRatings != null) {
                    setCSVlist(ps, selRatings, 2);
                }
                try (ResultSet rs = ps.executeQuery()) {
                    return new StatItem(label, value, rs.getLong(1), rs.getLong(2),
                            rs.getLong(3), rs.getLong(4), rs.getDouble(5), color);
                }
            }
        } catch (SQLException ex) {
            Popup.error("getStatItem(" + field + "," + value + ")", ex);
            return new StatItem(label, value, -1, -1, -1, -1, -1, Color.BLACK);
        }
    }

    /**
     * Get the number of the given "field" (genre, year, artist, album) for stats display.
     *
     * @param stats
     * @param field
     * @param selRatings
     */
    public void getSelectionList4Stats(ArrayList<StatItem> stats, String field, boolean[] selRatings) {
        try {
            String sql = """
                     SELECT COUNT(*), COUNT(DISTINCT P.idPath), SUM(size),  
                     SUM(length), avg(rating), """ + field + """  
                      FROM file F JOIN path P ON P.idPath=F.idPath  
                     WHERE F.rating IN """ + getCSVlist(selRatings)
                    + " \nGROUP BY " + field + " ORDER BY " + field; //NOI18N

            try (PreparedStatement ps = prepareStatement4Stats(selRatings, field, sql); ResultSet rs = ps.executeQuery()) {
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

    private PreparedStatement prepareStatement4Stats(boolean[] selRatings, String field, String sql) throws SQLException {
        PreparedStatement ps = dbConn.connection.prepareStatement(sql);
        setCSVlist(ps, selRatings, 1);
        return ps;
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
            String sql = """
                         SELECT count(*), COUNT(DISTINCT idPath), SUM(size), SUM(length), round(avg(albumRating),1 ) as [rating] , T.range as [percentRated] 
                         FROM (
                            SELECT albumRating, size, length, P.idPath,
                                case  
                                    when percentRated <  10                        then '0 -> 9 percent'
                                    when percentRated >= 10 and percentRated < 25  then '10 -> 24 percent'
                                    when percentRated >= 25 and percentRated < 50  then '25 -> 49 percent'
                                    when percentRated >= 50 and percentRated < 75  then '50 -> 74 percent'
                                    when percentRated >= 75 and percentRated < 100 then '75 -> 99 percent'
                                    when percentRated == 100                       then 'x 100 percent x'
                                    else 'kes' end as range
                            FROM file F   
                            JOIN ( SELECT path.*, ifnull(round(((sum(case when rating > 0 then rating end))/(sum(case when rating > 0 then 1.0 end))), 1), 0) AS albumRating,
                                ifnull((sum(case when rating > 0 then 1.0 end) / count(*)*100), 0) AS percentRated
                                FROM path JOIN file ON path.idPath=file.idPath GROUP BY path.idPath ) P ON F.idPath=P.idPath ) T 
                         GROUP BY T.range """;

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
     * Get files matching given genre, artist, album, ratings, and checkedFlag
     *
     * @param files
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
    public boolean getFiles(ArrayList<FileInfoInt> files, String selGenre,
            String selArtist, String selAlbum,
            boolean[] selRatings, boolean[] selCheckedFlag, int yearFrom, int yearTo,
            float bpmFrom, float bpmTo, int copyRight) {

        String sqlSelect = "SELECT F.*, P.strPath, P.checked, P.copyRight, 0 AS albumRating, "
                + "0 AS percentRated, 'INFO' AS status, P.mbId AS pathMbId, P.modifDate AS pathModifDate, P.mbId AS pathMbId, P.modifDate AS pathModifDate "; // NOI18N
        String sql = getSqlWhere(sqlSelect, selGenre, selArtist, selAlbum, selRatings, selCheckedFlag, copyRight);
        try (PreparedStatement ps = prepareStatement(sql, selGenre, selArtist, selAlbum, selRatings,
                selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo, copyRight)) {
            return getFiles(files, ps);
        } catch (SQLException ex) {
            Popup.error("getFiles()", ex);
            return false;
        }
    }

    String getSqlWhere(String sqlSelect, String selGenre, String selArtist, String selAlbum,
            boolean[] selRatings, boolean[] selCheckedFlag, int copyRight) {
        String sql = sqlSelect + " FROM file F " // NOI18N
                + " \nINNER JOIN `path` P ON P.idPath=F.idPath " // NOI18N
                + " \nWHERE F.rating IN " + getCSVlist(selRatings) // NOI18N
                + " \nAND P.checked IN " + getCSVlist(selCheckedFlag) // NOI18N
                // FIXME Z PanelSelect Check year valid and offer "allow invalid" as an option
                // https://stackoverflow.com/questions/5071601/how-do-i-use-regex-in-a-sqlite-query
                // else if(yearList.get(0).matches("\\d{4}")) { //NOI18N
                // results.get("year").value=yearList.get(0); //NOI18N
                // }
                + " \nAND ((F.year>=? AND F.year<=?) OR length(F.year)!=4)" // NOI18N
                + " \nAND F.BPM>=? AND F.BPM<=?"; // NOI18N //NOI18N

        if (!selGenre.equals("%")) { // NOI18N
            sql += " \nAND genre=?"; // NOI18N
        }
        if (!selArtist.equals("%")) { // NOI18N
            sql += " \nAND (artist=? OR albumArtist=?)"; // NOI18N
        }
        if (!selAlbum.equals("%")) { // NOI18N
            sql += " \nAND album=?"; // NOI18N
        }
        if (copyRight >= 0) {
            sql += " \nAND copyRight=?";// NOI18N;
        }
        return sql;
    }

    private PreparedStatement prepareStatement(String sql, String selGenre, String selArtist, String selAlbum,
            boolean[] selRatings, boolean[] selCheckedFlag,
            int yearFrom, int yearTo, float bpmFrom, float bpmTo, int copyRight) throws SQLException {

        PreparedStatement ps = dbConn.connection.prepareStatement(sql);
        prepareStatement4SqlWhere(1, ps, selGenre, selArtist, selAlbum, selRatings, selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo, copyRight);
        return ps;
    }

    int prepareStatement4SqlWhere(int paramIndex, PreparedStatement ps, String selGenre, String selArtist, String selAlbum,
            boolean[] selRatings, boolean[] selCheckedFlag,
            int yearFrom, int yearTo, float bpmFrom, float bpmTo, int copyRight) throws SQLException {

        paramIndex = setCSVlist(ps, selRatings, paramIndex);
        paramIndex = setCSVlist(ps, selCheckedFlag, paramIndex);

        ps.setInt(paramIndex++, yearFrom);
        ps.setInt(paramIndex++, yearTo);
        ps.setFloat(paramIndex++, bpmFrom);
        ps.setFloat(paramIndex++, bpmTo);

        if (!selGenre.equals("%")) { // NOI18N
            ps.setString(paramIndex++, selGenre);
        }
        if (!selArtist.equals("%")) { // NOI18N
            ps.setString(paramIndex++, selArtist); // artist
            ps.setString(paramIndex++, selArtist); // albumArtist
        }
        if (!selAlbum.equals("%")) { // NOI18N
            ps.setString(paramIndex++, selAlbum);
        }
        if (copyRight >= 0) {
            ps.setInt(paramIndex++, copyRight);
        }
        return paramIndex;
    }

    int setCSVlist(PreparedStatement ps, boolean[] values, int paramIndex) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            if (values[i]) {
                ps.setInt(paramIndex++, i);
            }
        }
        return paramIndex;
    }

    String getCSVlist(boolean[] values) {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for (int i = 0; i < values.length; i++) {
            if (values[i]) {
                builder.append("?").append(",");
            }
        }
        builder.deleteCharAt(builder.length() - 1).append(") ");
        return builder.toString();
    }

    private String escapeDoubleQuote(String text) {
        return text.replaceAll("\"", "\"\"");
    }

    /**
     * Returns a file for server to upload to client
     *
     * @param idFile
     * @param destExt
     * @return
     */
    public FileInfoInt getFile(int idFile, String destExt) {
        ArrayList<FileInfoInt> myFileInfoList = new ArrayList<>();
        String sql = """
                     SELECT F.idFile, F.idPath, F.name, F.rating, F.lastPlayed, F.playCounter, F.addedDate, F.artist, F.album, F.albumArtist, F.title, F.trackNo, F.trackTotal, 
                     F.discNo, F.discTotal, F.genre, F.year, F.BPM, F.comment, F.nbCovers, F.coverHash, F.ratingModifDate, F.tagsModifDate, F.genreModifDate, F.saved, 
                     ifnull(T.bitRate, F.bitRate) AS bitRate, 
                     ifnull(T.format, F.format) AS format, 
                     ifnull(T.length, F.length) AS length, 
                     ifnull(T.size, F.size) AS size, 
                     ifnull(T.trackGain, F.trackGain) AS trackGain, 
                     ifnull(T.albumGain, F.albumGain) AS albumGain, 
                     ifnull(T.modifDate, F.modifDate) AS modifDate, T.ext, 
                     P.strPath, P.checked, P.copyRight, 0 AS albumRating, 0 AS percentRated, 'INFO' AS status, P.mbId AS pathMbId, P.modifDate AS pathModifDate 
                     FROM file F 
                     LEFT JOIN fileTranscoded T ON T.idFile=F.idFile AND T.ext=? 
                     JOIN path P ON F.idPath=P.idPath WHERE F.idFile=?""";

        try (PreparedStatement ps = dbConn.connection.prepareStatement(sql)) {
            ps.setString(1, destExt);
            ps.setInt(2, idFile);

            getFiles(myFileInfoList, ps);
            return myFileInfoList.isEmpty() ? null : myFileInfoList.get(0);
        } catch (SQLException ex) {
            Popup.error("getFile()", ex);
            return null;
        }
    }

    /**
     * Returns list of files for remote sync process
     *
     * @param files
     * @param status
     * @param device
     * @param limit
     * @param destExt
     * @return ArrayList of FileInfoInt
     */
    public boolean getFiles(ArrayList<FileInfoInt> files, SyncStatus status, Device device, String limit, String destExt) {
        try (PreparedStatement ps = prepareStatement(status, device, limit, destExt, false)) {
            if (getFiles(files, ps)) {
                return true;
            }
        } catch (SQLException ex) {
            Popup.error("getFiles()", ex);
        }

        return false;
    }

    /**
     * Returns number of files for remote sync process
     *
     * @param status
     * @param device
     * @param destExt
     * @param limit
     * @return
     */
    public Integer getFilesCount(SyncStatus status, Device device, String destExt, String limit) {
        try (PreparedStatement ps = prepareStatement(status, device, limit, destExt, true)) {
            return getFilesCount(ps);
        } catch (SQLException ex) {
            Popup.error("getFilesCount()", ex);
        }
        return -1;
    }

    private PreparedStatement prepareStatement(SyncStatus status, Device device, String limit, String destExt, boolean getCount) throws SQLException {
        String sql = "SELECT " + (getCount ? " COUNT(F.idFile) "
                : """
                   F.idFile, F.idPath, F.name, F.rating, F.lastPlayed, F.playCounter, F.addedDate, F.artist, F.album, F.albumArtist, F.title, F.trackNo, F.trackTotal, 
                  F.discNo, F.discTotal, F.genre, F.year, F.BPM, F.comment, F.nbCovers, F.coverHash, F.ratingModifDate, F.tagsModifDate, F.genreModifDate, F.saved, 
                  ifnull(T.bitRate, F.bitRate) AS bitRate, 
                  ifnull(T.format, F.format) AS format, 
                  ifnull(T.length, F.length) AS length, 
                  ifnull(T.size, F.size) AS size, 
                  ifnull(T.trackGain, F.trackGain) AS trackGain, 
                  ifnull(T.albumGain, F.albumGain) AS albumGain, 
                  ifnull(T.modifDate, F.modifDate) AS modifDate, T.ext, 
                  P.strPath, P.checked, P.copyRight, 0 AS albumRating, 0 AS percentRated, P.mbId AS pathMbId, P.modifDate AS pathModifDate, """
                + (status.equals(SyncStatus.INFO) ? "'INFO' AS status" : "DF.status") + " \n")
                + "FROM file F \n"
                + "LEFT JOIN fileTranscoded T ON T.idFile=F.idFile AND T.ext=? \n"
                + "LEFT JOIN deviceFile DF ON DF.idFile=F.idFile AND DF.idDevice=? \n"
                + "JOIN path P ON F.idPath=P.idPath \n"
                + "WHERE " + (status.equals(SyncStatus.INFO) ? "(DF.status is null OR DF.status=\"INFO\")" : "DF.idDevice=? AND DF.status=?") + " \n"
                + "ORDER BY F.idFile "
                + limit;
        PreparedStatement ps = dbConn.connection.prepareStatement(sql);
        ps.setString(1, destExt);
        ps.setInt(2, device.getId());
        if (!status.equals(SyncStatus.INFO)) {
            ps.setInt(3, device.getId());
            ps.setString(4, status.toString());
        }
        return ps;
    }

    /**
     * Return files list matching given sql where from playlist
     *
     * @param files
     * @param destExt
     * @param sqlWhere
     * @return
     */
    public boolean getFiles(ArrayList<FileInfoInt> files, String destExt, String sqlWhere) {
        String sql = """
                     SELECT F.idFile, F.idPath, F.name, F.rating, F.lastPlayed, F.playCounter, F.addedDate, F.artist, F.album, F.albumArtist, F.title, F.trackNo, F.trackTotal, 
                     F.discNo, F.discTotal, F.genre, F.year, F.BPM, F.comment, F.nbCovers, F.coverHash, F.ratingModifDate, F.tagsModifDate, F.genreModifDate, F.saved, 
                     ifnull(T.bitRate, F.bitRate) AS bitRate, 
                     ifnull(T.format, F.format) AS format, 
                     ifnull(T.length, F.length) AS length, 
                     ifnull(T.size, F.size) AS size, 
                     ifnull(T.trackGain, F.trackGain) AS trackGain, 
                     ifnull(T.albumGain, F.albumGain) AS albumGain, 
                     ifnull(T.modifDate, F.modifDate) AS modifDate, T.ext, 
                     P.strPath, P.checked, P.copyRight, P.albumRating, P.percentRated, 'INFO' AS status, P.mbId AS pathMbId, P.modifDate AS pathModifDate 
                     FROM file F 
                     LEFT JOIN fileTranscoded T ON T.idFile=F.idFile AND T.ext=? 
                     JOIN (
                     \t\tSELECT path.*, ifnull(round(((sum(case when rating > 0 then rating end))/(sum(case when rating > 0 then 1.0 end))), 1), 0) AS albumRating, 
                     \t\tifnull((sum(case when rating > 0 then 1.0 end) / count(*)*100), 0) AS percentRated
                     \t\tFROM path JOIN file ON path.idPath=file.idPath GROUP BY path.idPath 
                     \t) P ON F.idPath=P.idPath """;

        try (PreparedStatement ps = dbConn.connection.prepareStatement(sql)) {
            ps.setString(1, destExt);
            if (getFiles(files, ps)) {
                return true;
            }
        } catch (SQLException ex) {
            Popup.error("getFiles()", ex);
        }

        return false;
    }

    /**
     * Return raw list of files, for CompareDB
     *
     * @param files
     * @return
     */
    public boolean getFiles(ArrayList<FileInfoInt> files) {
        String sql = "SELECT F.*, P.strPath, P.checked, P.copyRight, 0 AS albumRating, 0 AS percentRated, 'INFO' AS status, P.mbId AS pathMbId, P.modifDate AS pathModifDate "
                + " FROM file F JOIN path P ON F.idPath=P.idPath ";

        try (PreparedStatement ps = dbConn.connection.prepareStatement(sql)) {
            if (getFiles(files, ps)) {
                return true;
            }
        } catch (SQLException ex) {
            Popup.error("getFiles()", ex);
        }

        return false;
    }

    /**
     * Get files where saved is 0
     *
     * @param files
     * @return ArrayList of FileInfoInt
     */
    public boolean getFilesNotSaved(ArrayList<FileInfoInt> files) {
        String sql = "SELECT F.*, P.strPath, P.checked, P.copyRight, 0 AS albumRating, "
                + "0 AS percentRated, 'INFO' AS status, P.mbId AS pathMbId, P.modifDate AS pathModifDate "
                + "FROM file F JOIN path P ON F.idPath=P.idPath WHERE saved=0";

        try (PreparedStatement ps = dbConn.connection.prepareStatement(sql)) {
            if (getFiles(files, ps)) {
                return true;
            }
        } catch (SQLException ex) {
            Popup.error("getFiles()", ex);
        }

        return false;
    }

    /**
     * Get files for given device for sync process
     *
     * @param files
     * @param device
     * @return
     */
    public boolean getFiles(ArrayList<FileInfoInt> files, Device device) {
        String sql = "SELECT DF.status, F.*, P.strPath, P.checked, P.copyRight, "
                + " 0 AS albumRating, 0 AS percentRated, P.mbId AS pathMbId, "
                + " P.modifDate AS pathModifDate "
                + " FROM deviceFile DF "
                + " JOIN file F ON DF.idFile=F.idFile "
                + " JOIN path P ON F.idPath=P.idPath "
                + " AND DF.idDevice=? "
                + " ORDER BY idFile ";
        try (PreparedStatement ps = dbConn.connection.prepareStatement(sql)) {
            ps.setInt(1, device.getId());
            return getFiles(files, ps);
        } catch (SQLException ex) {
            Popup.error("getFiles()", ex); // Handle or log the exception appropriately
            return false;
        }
    }

    /**
     * Get given folder's files
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
            sql += " AND P.idPath=?";
        }

        try (PreparedStatement ps = dbConn.connection.prepareStatement(sql)) {
            if (idPath > -1) {
                ps.setInt(1, idPath);
            }

            return getFiles(files, ps);
        } catch (SQLException ex) {
            Popup.error("getFiles()", ex); // Handle or log the exception appropriately
            return false;
        }
    }

    private boolean getFiles(ArrayList<FileInfoInt> files, PreparedStatement ps) {
        return getFiles(files, ps, locationLibrary);
    }

    private boolean getFiles(ArrayList<FileInfoInt> files, PreparedStatement ps, String rootPath) {
        files.clear();
        try (ResultSet rs = ps.executeQuery()) {

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

                files.add(new FileInfoInt(idFile, idPath, relativePath, filename,
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

    //TODO: Move to higher level, and use it 
    private Integer getFilesCount(PreparedStatement ps) {
        try (ResultSet rs = ps.executeQuery()) {
            return rs.getInt(1);
        } catch (SQLException ex) {
            Popup.error("getIdFileMax()", ex);
            return null;
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

        String sqlSelect = "SELECT COUNT(*) AS nbFiles, SUM(F.size) AS totalSize, SUM(F.length) AS totalLength "; // NOI18N
        String sql = getSqlWhere(sqlSelect, selGenre, selArtist, selAlbum, selRatings, selCheckedFlag, copyRight);
        try (PreparedStatement ps = prepareStatement(sql, selGenre, selArtist, selAlbum, selRatings,
                selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo, copyRight)) {
            return getFilesStats(ps);
        } catch (SQLException ex) {
            Popup.error("getFilesStats()", ex);
            return "";
        }
    }

    /**
     * Retrieve file statistics based on the provided SQL query.
     *
     * @param sql The SQL query to execute.
     * @return A string representation of file statistics.
     */
    private String getFilesStats(PreparedStatement ps) {
        try (ResultSet rs = ps.executeQuery()) {

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
