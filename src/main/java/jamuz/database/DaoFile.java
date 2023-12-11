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
import jamuz.utils.DateTime;
import jamuz.utils.Popup;
import jamuz.utils.StringManager;
import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

/**
 *
 * @author raph
 */
public class DaoFile {

    private final DbConn dbConn;

    /**
     *
     * @param dbConn
     */
    public DaoFile(DbConn dbConn) {
        this.dbConn = dbConn;
    }

    /**
     * Inserts a file (tags)
     *
     * @param fileInfo
     * @param key
     * @return
     */
    public boolean insert(FileInfoInt fileInfo, int[] key) {
        try {
            PreparedStatement stInsertFileTag = dbConn.connection.prepareStatement("INSERT INTO file (name, idPath, "
                    + "format, title, artist, album, albumArtist, genre, discNo, trackNo, year, comment, " // NOI18N
                    + "length, bitRate, size, modifDate, trackTotal, discTotal, BPM, nbCovers, "
                    + "rating, lastPlayed, playCounter, addedDate, coverHash, trackGain, albumGain) " // NOI18N
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "0, \"1970-01-01 00:00:00\", 0, datetime('now'), ?, ?, ?)"); // NOI18N
            stInsertFileTag.setString(1, fileInfo.getFilename());
            stInsertFileTag.setInt(2, fileInfo.getIdPath());
            stInsertFileTag.setString(3, fileInfo.getFormat());
            stInsertFileTag.setString(4, fileInfo.getTitle());
            stInsertFileTag.setString(5, fileInfo.getArtist());
            stInsertFileTag.setString(6, fileInfo.getAlbum());
            stInsertFileTag.setString(7, fileInfo.getAlbumArtist());
            stInsertFileTag.setString(8, fileInfo.getGenre());
            stInsertFileTag.setInt(9, fileInfo.getDiscNo());
            stInsertFileTag.setInt(10, fileInfo.getTrackNo());
            stInsertFileTag.setString(11, fileInfo.getYear());
            stInsertFileTag.setString(12, fileInfo.getComment());
            stInsertFileTag.setInt(13, fileInfo.getLength());
            stInsertFileTag.setString(14, fileInfo.getBitRate());
            stInsertFileTag.setLong(15, fileInfo.getSize());
            stInsertFileTag.setString(16, fileInfo.getFormattedModifDate());
            stInsertFileTag.setInt(17, fileInfo.getTrackTotal());
            stInsertFileTag.setInt(18, fileInfo.getDiscTotal());
            stInsertFileTag.setFloat(19, fileInfo.getBPM());
            stInsertFileTag.setInt(20, fileInfo.getNbCovers());
            stInsertFileTag.setString(21, fileInfo.getCoverHash());
            ReplayGain.GainValues gainValues = fileInfo.getReplayGain(false);
            stInsertFileTag.setFloat(22, gainValues.getTrackGain());
            stInsertFileTag.setFloat(23, gainValues.getAlbumGain());
            int nbRowsAffected = stInsertFileTag.executeUpdate();

            if (nbRowsAffected == 1) {
                ResultSet keys = stInsertFileTag.getGeneratedKeys();
                keys.next();
                key[0] = keys.getInt(1);
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "insertTags, fileInfo={0} # row(s) affected: +{1}",
                        new Object[]{fileInfo.toString(), nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("insertTags(" + fileInfo.toString() + ")", ex); // NOI18N
            return false;
        }
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
     * Updates a file (tags)
     *
     * @param fileInfo
     * @return
     */
    public boolean update(FileInfoInt fileInfo) {
        try {
            PreparedStatement stUpdateFileTag = dbConn.connection.prepareStatement(
                    "UPDATE file "
                    + "SET format=?, title=?, artist=?, album=?, albumArtist=?, "
                    + "genre=?, discNo=?, " // NOI18N
                    + "trackNo=?, year=?, comment=?, " // NOI18N
                    + "length=?, bitRate=?, size=?, modifDate=?, trackTotal=?, "
                    + "discTotal=?, BPM=?, "
                    + "nbCovers=?, coverHash=?, trackGain=?, albumGain=? " // NOI18N
                    + "WHERE idPath=? AND idFile=?"); // NOI18N
            stUpdateFileTag.setString(1, fileInfo.getFormat());
            stUpdateFileTag.setString(2, fileInfo.getTitle());
            stUpdateFileTag.setString(3, fileInfo.getArtist());
            stUpdateFileTag.setString(4, fileInfo.getAlbum());
            stUpdateFileTag.setString(5, fileInfo.getAlbumArtist());
            stUpdateFileTag.setString(6, fileInfo.getGenre());
            stUpdateFileTag.setInt(7, fileInfo.getDiscNo());
            stUpdateFileTag.setInt(8, fileInfo.getTrackNo());
            stUpdateFileTag.setString(9, fileInfo.getYear());
            stUpdateFileTag.setString(10, fileInfo.getComment());
            stUpdateFileTag.setInt(11, fileInfo.getLength());
            stUpdateFileTag.setString(12, fileInfo.getBitRate());
            stUpdateFileTag.setLong(13, fileInfo.getSize());
            stUpdateFileTag.setString(14, fileInfo.getFormattedModifDate());
            stUpdateFileTag.setInt(15, fileInfo.getTrackTotal());
            stUpdateFileTag.setInt(16, fileInfo.getDiscTotal());
            stUpdateFileTag.setFloat(17, fileInfo.getBPM());
            stUpdateFileTag.setInt(18, fileInfo.getNbCovers());
            stUpdateFileTag.setString(19, fileInfo.getCoverHash());
            ReplayGain.GainValues gainValues = fileInfo.getReplayGain(false);
            stUpdateFileTag.setFloat(20, gainValues.getTrackGain());
            stUpdateFileTag.setFloat(21, gainValues.getAlbumGain());
            // WHERE:
            stUpdateFileTag.setInt(22, fileInfo.getIdPath());
            stUpdateFileTag.setInt(23, fileInfo.getIdFile());
            int nbRowsAffected = stUpdateFileTag.executeUpdate();
            if (nbRowsAffected == 1) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "updateTags, fileInfo={0} # row(s) affected: +{1}",
                        new Object[]{fileInfo.toString(), nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("updateTags(" + fileInfo.toString() + ")", ex); // NOI18N
            return false;
        }
    }

    /**
     * Updates a file (tags)
     *
     * @param file
     * @return
     */
    public boolean updateLastPlayedAndCounter(FileInfoInt file) {
        try {
            PreparedStatement stUpdateFileLastPlayedAndCounter = dbConn.connection.prepareStatement("UPDATE file "
                    + "SET lastplayed=?, playCounter=? "
                    + "WHERE idFile=?");

            stUpdateFileLastPlayedAndCounter.setString(1, DateTime.getCurrentUtcSql());
            stUpdateFileLastPlayedAndCounter.setInt(2, file.getPlayCounter() + 1);
            stUpdateFileLastPlayedAndCounter.setInt(3, file.getIdFile());
            int nbRowsAffected = stUpdateFileLastPlayedAndCounter.executeUpdate();
            if (nbRowsAffected == 1) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE,
                        "stUpdateFileLastPlayedAndCounter, fileInfo={0} # row(s) affected: +{1}",
                        new Object[]{file.toString(), nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("updateLastPlayedAndCounter(" + file.toString() + ")", ex); // NOI18N
            return false;
        }
    }

    /**
     * Update rating
     *
     * @param fileInfo
     * @return
     */
    public boolean updateRating(FileInfoInt fileInfo) {
        try {
            PreparedStatement stUpdateFileRating = dbConn.connection.prepareStatement(
                    "UPDATE file set rating=?, "
                    + "ratingModifDate=datetime('now') "
                    + "WHERE idFile=?"); // NOI18N
            stUpdateFileRating.setInt(1, fileInfo.getRating());
            stUpdateFileRating.setInt(2, fileInfo.getIdFile());
            int nbRowsAffected = stUpdateFileRating.executeUpdate();
            if (nbRowsAffected == 1) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "stUpdateFileRating, fileInfo={0} # row(s) affected: +{1}",
                        new Object[]{fileInfo.toString(), nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("updateRating(" + fileInfo.toString() + ")", ex); // NOI18N
            return false;
        }
    }
/**
	 * Update genre
	 *
	 * @param fileInfo
	 * @return
	 */
	public boolean updateFileGenre(FileInfoInt fileInfo) {
		try {
			PreparedStatement stUpdateFileGenre = dbConn.connection.prepareStatement(
					"UPDATE file set genre=?, "
					+ "genreModifDate=datetime('now') "
					+ "WHERE idFile=?"); // NOI18N
			stUpdateFileGenre.setString(1, fileInfo.getGenre());
			stUpdateFileGenre.setInt(2, fileInfo.getIdFile());
			int nbRowsAffected = stUpdateFileGenre.executeUpdate();
			if (nbRowsAffected == 1) {
				return true;
			} else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateFileGenre, "
						+ "fileInfo={0} # row(s) affected: +{1}",
						new Object[]{fileInfo.toString(), nbRowsAffected}); // NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateGenre(" + fileInfo.toString() + ")", ex); // NOI18N
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
    public boolean updateIdPath(int idPath, int newIdPath) {
        try {
            PreparedStatement stUpdateIdPathInFile = dbConn.connection.prepareStatement(
                    "UPDATE file "
                    + "SET idPath=? " // NOI18N
                    + "WHERE idPath=?"); // NOI18N
            stUpdateIdPathInFile.setInt(1, newIdPath);
            stUpdateIdPathInFile.setInt(2, idPath);
            int nbRowsAffected = stUpdateIdPathInFile.executeUpdate();
            if (nbRowsAffected > 0) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "setIdPath, idPath={0}, newIdPath={1} "
                        + "# row(s) affected: +{2}", new Object[]{idPath, newIdPath, nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("setIdPath(" + idPath + ", " + newIdPath + ")", ex); // NOI18N
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
    public boolean updateModifDate(int idFile, Date modifDate,
            String name) {
        try {
            PreparedStatement stUpdateFileModifDate = dbConn.connection.prepareStatement(
                    "UPDATE file "
                    + "SET name=?, modifDate=? " // NOI18N
                    + "WHERE idFile=?"); // NOI18N

            stUpdateFileModifDate.setString(1, name);
            stUpdateFileModifDate.setString(2, DateTime.formatUTCtoSqlUTC(modifDate));
            stUpdateFileModifDate.setInt(3, idFile);

            // Note that we need to scan files (even for check) to get idFile otherwise the
            // following will fail
            int nbRowsAffected = stUpdateFileModifDate.executeUpdate();
            if (nbRowsAffected == 1) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "stUpdateFile, idFile={0} # "
                        + "row(s) affected: +{1}", new Object[]{idFile, nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("updateFileModifDate(" + idFile + ", \"" + modifDate.toString() + "\", \"" + name + "\")", ex); // NOI18N
            return false;
        }
    }

    /**
     *
     * @param idFile
     * @return
     */
    public boolean setSaved(int idFile) {
        try {
            PreparedStatement stUpdateSavedFile = dbConn.connection
                    .prepareStatement("UPDATE file SET saved=1 WHERE idFile=?"); // NOI18N

            stUpdateSavedFile.setInt(1, idFile);
            int nbRowsAffected = stUpdateSavedFile.executeUpdate();
            if (nbRowsAffected == 1) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "setFileSaved, idFile={0} # row(s) affected: +{1}",
                        new Object[]{idFile, nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("setFileSaved(" + idFile + ")", ex); // NOI18N
            return false;
        }
    }

    /**
     * Delete a file.
     *
     * @param idFile
     * @return
     */
    public boolean delete(int idFile) {
        try {
            PreparedStatement stDeleteFile = dbConn.connection.prepareStatement(
                    "DELETE FROM file WHERE idFile=?"); // NOI18N
            stDeleteFile.setInt(1, idFile);
            int nbRowsAffected = stDeleteFile.executeUpdate();
            if (nbRowsAffected == 1) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "deleteFile, idFile={0} "
                        + "# row(s) affected: +{1}", new Object[]{idFile, nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("deleteFile(" + idFile + ")", ex); // NOI18N
            return false;
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
