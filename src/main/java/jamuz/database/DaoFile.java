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
import java.util.logging.Level;

/**
 *
 * @author raph
 */
public class DaoFile {

    private final DbConn dbConn;
    private final DaoFileWrite daoFileWrite;
    private final String locationLibrary;

    /**
     *
     * @param dbConn
     * @param locationLibrary
     */
    public DaoFile(DbConn dbConn, String locationLibrary) {
        this.dbConn = dbConn;
        this.locationLibrary = locationLibrary;
        this.daoFileWrite = new DaoFileWrite(dbConn);
    }

    /**
     * This is to reach writing operations (insert, update, delete) on the file
     * table
     *
     * @return
     */
    public DaoFileWrite lock() {
        return daoFileWrite;
    }

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
            Popup.error("getIdFileMax()", ex); // NOI18N
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
            // FIXME ZZ PanelSelect better validate year (but regex is not available by
            // default :( )
            rs = st.executeQuery("SELECT " + maxOrMin + "(year) FROM file "
                    + "WHERE year GLOB '[0-9][0-9][0-9][0-9]' AND length(year)=4");
            // To exclude wrong entries (not YYYY format)
            return rs.getDouble(1);

        } catch (SQLException ex) {
            Popup.error("getYear(" + maxOrMin + ")", ex); // NOI18N
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
            value = value.replaceAll("\"", "%"); // NOI18N
            sql = "SELECT COUNT(*), COUNT(DISTINCT path.idPath), SUM(size), "
                    + "\nSUM(length), avg(rating) "
                    + "\nFROM file JOIN path ON path.idPath=file.idPath ";
            if (value.contains("IN (")) { // NOI18N
                sql += " \nWHERE " + table + "." + field + " " + value; // NOI18N
            } else if (value.startsWith(">")) { // NOI18N
                sql += " \nWHERE " + table + "." + field + value + ""; // NOI18N
            } else if (value.contains("%")) { // NOI18N
                sql += " \nWHERE " + table + "." + field + " LIKE \"" + value + "\""; // NOI18N
            } else {
                sql += " \nWHERE " + table + "." + field + "='" + value + "'"; // NOI18N
            }
            if (selRatings != null) {
                sql += " \nAND file.rating IN " + getCSVlist(selRatings);
            }
            st = dbConn.connection.createStatement();
            rs = st.executeQuery(sql);
            return new StatItem(label, value, rs.getLong(1), rs.getLong(2),
                    rs.getLong(3), rs.getLong(4), rs.getDouble(5), color);

        } catch (SQLException ex) {
            Popup.error("getStatItem(" + field + "," + value + ")", ex); // NOI18N
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
                    + " \nGROUP BY " + field + " ORDER BY " + field; // NOI18N //NOI18N

            st = dbConn.connection.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                stats.add(new StatItem(dbConn.getStringValue(rs, field),
                        dbConn.getStringValue(rs, field),
                        rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getLong(4),
                        rs.getDouble(5), null));
            }
        } catch (SQLException ex) {
            Popup.error("getSelectionList4Stats(" + field + ")", ex); // NOI18N
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
            // Note Using "percent" as "%" is replaced by "-" because of decades. Now also
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

            st = dbConn.connection.createStatement();
            rs = st.executeQuery(sql);
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
        } catch (SQLException ex) {
            Popup.error("getPercentRatedForStats()", ex); // NOI18N
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
        FolderInfo.CheckedFlag checkedFlag;
        FolderInfo.CopyRight copyRight;
        double albumRating;
        int percentRated;
        SyncStatus status;
        String pathModifDate;
        String pathMbid;
        ReplayGain.GainValues replayGain;

        myFileInfoList.clear();
        Statement st = null;
        ResultSet rs = null;
        long startTime = System.currentTimeMillis();
        try {
            // Execute query

            st = dbConn.connection.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                idFile = rs.getInt("idFile"); // NOI18N
                idPath = rs.getInt("idPath"); // NOI18N
                checkedFlag = FolderInfo.CheckedFlag.values()[rs.getInt("checked")]; // NOI18N
                copyRight = FolderInfo.CopyRight.values()[rs.getInt("copyRight")];

                // Path can be empty if file is on root folder
                relativePath = dbConn.getStringValue(rs, "strPath"); // NOI18N

                filename = dbConn.getStringValue(rs, "name"); // NOI18N
                length = rs.getInt("length"); // NOI18N
                format = dbConn.getStringValue(rs, "format"); // NOI18N
                bitRate = dbConn.getStringValue(rs, "bitRate"); // NOI18N
                size = rs.getInt("size"); // NOI18N
                BPM = rs.getFloat("BPM"); // NOI18N
                album = dbConn.getStringValue(rs, "album"); // NOI18N
                albumArtist = dbConn.getStringValue(rs, "albumArtist"); // NOI18N
                artist = dbConn.getStringValue(rs, "artist"); // NOI18N
                comment = dbConn.getStringValue(rs, "comment"); // NOI18N
                discNo = rs.getInt("discNo"); // NOI18N
                discTotal = rs.getInt("discTotal"); // NOI18N
                genre = dbConn.getStringValue(rs, "genre"); // NOI18N
                nbCovers = rs.getInt("nbCovers"); // NOI18N
                coverHash = dbConn.getStringValue(rs, "coverHash"); // NOI18N
                title = dbConn.getStringValue(rs, "title"); // NOI18N
                trackNo = rs.getInt("trackNo"); // NOI18N
                trackTotal = rs.getInt("trackTotal"); // NOI18N
                year = dbConn.getStringValue(rs, "year"); // NOI18N
                playCounter = rs.getInt("playCounter"); // NOI18N
                rating = rs.getInt("rating"); // NOI18N
                addedDate = dbConn.getStringValue(rs, "addedDate"); // NOI18N
                lastPlayed = dbConn.getStringValue(rs, "lastPlayed"); // NOI18N
                modifDate = dbConn.getStringValue(rs, "modifDate"); // NOI18N
                albumRating = rs.getDouble("albumRating");
                percentRated = rs.getInt("percentRated");
                status = SyncStatus.valueOf(dbConn.getStringValue(rs, "status", "INFO"));
                pathModifDate = dbConn.getStringValue(rs, "pathModifDate");
                pathMbid = dbConn.getStringValue(rs, "pathMbid");
                replayGain = new ReplayGain.GainValues(rs.getFloat("trackGain"), rs.getFloat("albumGain"));

                myFileInfoList.add(
                        new FileInfoInt(idFile, idPath, relativePath, filename,
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
            Jamuz.getLogger().log(Level.FINEST, "getFiles // Total execution time: {0}ms",
                    new Object[]{endTime - startTime}); // NOI18N
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
                + "SUM(F.length) AS totalLength " // NOI18N
                + getSqlWHERE(selGenre, selArtist, selAlbum, selRatings,
                        selCheckedFlag, yearFrom, yearTo, bpmFrom, bpmTo,
                        copyRight);

        return getFilesStats(sql);

        // getFiles(myFileInfoList, sql);
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
            // Execute query
            st = dbConn.connection.createStatement();
            rs = st.executeQuery(sql);
            nbFiles = rs.getInt("nbFiles"); // NOI18N
            totalSize = rs.getLong("totalSize"); // NOI18N
            totalLength = rs.getLong("totalLength"); // NOI18N
            return nbFiles + " file(s)"
                    + " ; " + StringManager.humanReadableSeconds(totalLength)
                    + " ; " + StringManager.humanReadableByteCount(totalSize, false);
        } catch (SQLException ex) {
            Popup.error("getFileInfoList()", ex); // NOI18N
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

}
