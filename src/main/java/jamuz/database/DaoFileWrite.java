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
import jamuz.process.check.ReplayGain;
import jamuz.utils.DateTime;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;

/**
 *
 * @author raph
 */
public class DaoFileWrite {

    private final DbConn dbConn;
    private PreparedStatement stDeleteFile;
    private PreparedStatement stInsertFileTag;
    private PreparedStatement stUpdateFileTag;
    private PreparedStatement stUpdateFileLastPlayedAndCounter;
    private PreparedStatement stUpdateFileRating;
    private PreparedStatement stUpdateFileGenre;
    private PreparedStatement stUpdateIdPathInFile;
    private PreparedStatement stUpdateFileModifDate;
    private PreparedStatement stUpdateSavedFile;

    /**
     *
     * @param dbConn
     */
    public DaoFileWrite(DbConn dbConn) {
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
        synchronized (dbConn) {
            ResultSet keys = null;
            try {
                if (stInsertFileTag == null) {
                    stInsertFileTag = dbConn.connection.prepareStatement("INSERT INTO file (name, idPath, "
                            + "format, title, artist, album, albumArtist, genre, discNo, trackNo, year, comment, " // NOI18N
                            + "length, bitRate, size, modifDate, trackTotal, discTotal, BPM, nbCovers, "
                            + "rating, lastPlayed, playCounter, addedDate, coverHash, trackGain, albumGain) " // NOI18N
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                            + "0, \"1970-01-01 00:00:00\", 0, datetime('now'), ?, ?, ?)", Statement.RETURN_GENERATED_KEYS); // NOI18N
                }
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
                    keys = stInsertFileTag.getGeneratedKeys();
                    keys.next();
                    key[0] = keys.getInt(1);
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "insertTags, fileInfo={0} # row(s) affected: +{1}", new Object[]{fileInfo.toString(), nbRowsAffected}); // NOI18N
                    return false;
                }
            } catch (SQLException ex) {
                Jamuz.getLogger().log(Level.SEVERE, fileInfo.toString(), ex); // NOI18N
                System.out.println("insert file error " + ex.getLocalizedMessage());
//            Popup.error(fileInfo.toString(), ex);
                return false;
            } finally {
                try {
                    if (keys != null) {
                        keys.close();
                    }
                } catch (SQLException ex) {
                    Jamuz.getLogger().warning("Failed to close ResultSet");
                }
            }
        }
    }

    /**
     * Delete a file.
     *
     * @param idFile
     * @return
     */
    public synchronized boolean delete(int idFile) {
        try {
            if (stDeleteFile == null) {
                stDeleteFile = dbConn.connection.prepareStatement(
                        "DELETE FROM file WHERE idFile=?"); // NOI18N
            }
            stDeleteFile.setInt(1, idFile);
            int nbRowsAffected = stDeleteFile.executeUpdate();
            if (nbRowsAffected == 1) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "stDeleteFile, idFile={0} # row(s) affected: +{1}", new Object[]{idFile, nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("stDeleteFile(" + idFile + ")", ex); // NOI18N
            return false;
        }
    }

    /**
     *
     * @param idFile
     * @return
     */
    public synchronized boolean setSaved(int idFile) {
        try {
            if (stUpdateSavedFile == null) {
                stUpdateSavedFile = dbConn.connection
                        .prepareStatement("UPDATE file SET saved=1 WHERE idFile=?"); // NOI18N
            }
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
     * Updates a file (tags)
     *
     * @param fileInfo
     * @return
     */
    public synchronized boolean update(FileInfoInt fileInfo) {
        try {
            if (stUpdateFileTag == null) {
                stUpdateFileTag = dbConn.connection.prepareStatement(
                        """
                    UPDATE file 
                    SET format=?, title=?, artist=?, album=?, albumArtist=?, 
                    genre=?, discNo=?, 
                    trackNo=?, year=?, comment=?, 
                    length=?, bitRate=?, size=?, modifDate=?, trackTotal=?,
                     discTotal=?, BPM=?, 
                    nbCovers=?, coverHash=?, trackGain=?, albumGain=? 
                    WHERE idPath=? AND idFile=?
                    """
                ); // NOI18N
            }
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
    public synchronized boolean updateLastPlayedAndCounter(FileInfoInt file) {
        try {
            if (stUpdateFileLastPlayedAndCounter == null) {
                stUpdateFileLastPlayedAndCounter = dbConn.connection.prepareStatement("UPDATE file "
                        + "SET lastplayed=?, playCounter=? "
                        + "WHERE idFile=?");
            }
            stUpdateFileLastPlayedAndCounter.setString(1, DateTime.getCurrentUtcSql());
            stUpdateFileLastPlayedAndCounter.setInt(2, file.getPlayCounter() + 1);
            stUpdateFileLastPlayedAndCounter.setInt(3, file.getIdFile());
            int nbRowsAffected = stUpdateFileLastPlayedAndCounter.executeUpdate();
            if (nbRowsAffected == 1) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE,
                        "stUpdateFileLastPlayedAndCounter, fileInfo={0} # row(s) affected: +{1}", new Object[]{file.toString(), nbRowsAffected}); // NOI18N
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
    public synchronized boolean updateRating(FileInfoInt fileInfo) {
        try {
            if (stUpdateFileRating == null) {
                stUpdateFileRating = dbConn.connection.prepareStatement(
                        "UPDATE file set rating=?, "
                        + "ratingModifDate=datetime('now') "
                        + "WHERE idFile=?"); // NOI18N
            }
            stUpdateFileRating.setInt(1, fileInfo.getRating());
            stUpdateFileRating.setInt(2, fileInfo.getIdFile());
            int nbRowsAffected = stUpdateFileRating.executeUpdate();
            if (nbRowsAffected == 1) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "stUpdateFileRating, fileInfo={0} # row(s) affected: +{1}", new Object[]{fileInfo.toString(), nbRowsAffected}); // NOI18N
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
    public synchronized boolean updateFileGenre(FileInfoInt fileInfo) {
        try {
            if (stUpdateFileGenre == null) {
                stUpdateFileGenre = dbConn.connection.prepareStatement(
                        "UPDATE file set genre=?, "
                        + "genreModifDate=datetime('now') "
                        + "WHERE idFile=?"); // NOI18N
            }
            stUpdateFileGenre.setString(1, fileInfo.getGenre());
            stUpdateFileGenre.setInt(2, fileInfo.getIdFile());
            int nbRowsAffected = stUpdateFileGenre.executeUpdate();
            if (nbRowsAffected == 1) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "stUpdateFileGenre, fileInfo={0} # row(s) affected: +{1}", new Object[]{fileInfo.toString(), nbRowsAffected}); // NOI18N
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
    public synchronized boolean updateIdPath(int idPath, int newIdPath) {
        try {
            if (stUpdateIdPathInFile == null) {
                stUpdateIdPathInFile = dbConn.connection.prepareStatement(
                        "UPDATE file "
                        + "SET idPath=? " // NOI18N
                        + "WHERE idPath=?"); // NOI18N
            }
            stUpdateIdPathInFile.setInt(1, newIdPath);
            stUpdateIdPathInFile.setInt(2, idPath);
            int nbRowsAffected = stUpdateIdPathInFile.executeUpdate();
            if (nbRowsAffected > 0) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "updateIdPath, idPath={0}, newIdPath={1} # row(s) affected: +{2}", new Object[]{idPath, newIdPath, nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("updateIdPath(" + idPath + ", " + newIdPath + ")", ex); // NOI18N
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
    public synchronized boolean updateModifDate(int idFile, Date modifDate, String name) {
        try {
            if (stUpdateFileModifDate == null) {
                stUpdateFileModifDate = dbConn.connection.prepareStatement(
                        "UPDATE file "
                        + "SET name=?, modifDate=? " // NOI18N
                        + "WHERE idFile=?"); // NOI18N
            }
            stUpdateFileModifDate.setString(1, name);
            stUpdateFileModifDate.setString(2, DateTime.formatUTCtoSqlUTC(modifDate));
            stUpdateFileModifDate.setInt(3, idFile);

            // Note that we need to scan files (even for check) to get idFile otherwise the
            // following will fail
            int nbRowsAffected = stUpdateFileModifDate.executeUpdate();
            if (nbRowsAffected == 1) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "stUpdateFile, idFile={0} # row(s) affected: +{1}", new Object[]{idFile, nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("updateModifDate(" + idFile + ", \"" + modifDate.toString() + "\", \"" + name + "\")", ex); // NOI18N
            return false;
        }
    }

}
