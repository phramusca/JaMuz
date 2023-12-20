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

import jamuz.FileInfo;
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

    /**
     *
     * @param dbConn
     */
    public DaoFileWrite(DbConn dbConn) {
        this.dbConn = dbConn;
    }

    /**
     * Inserts file (tags) into the database.
     *
     * @param fileInfo the file information
     * @param key an array to store the generated key
     * @return true if the insertion is successful, false otherwise
     */
    public boolean insert(FileInfoInt fileInfo, int[] key) {
        synchronized (dbConn) {
            try (PreparedStatement stInsertFileTag = dbConn.connection.prepareStatement("INSERT INTO file (name, idPath, "
                    + "format, title, artist, album, albumArtist, genre, discNo, trackNo, year, comment, " // NOI18N
                    + "length, bitRate, size, modifDate, trackTotal, discTotal, BPM, nbCovers, "
                    + "rating, lastPlayed, playCounter, addedDate, coverHash, trackGain, albumGain) " // NOI18N
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "0, \"1970-01-01 00:00:00\", 0, datetime('now'), ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {

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
                    try (ResultSet keys = stInsertFileTag.getGeneratedKeys()) {
                        if (keys.next()) {
                            key[0] = keys.getInt(1);
                            return true;
                        }
                    }
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "insertTags, fileInfo={0} # row(s) affected: +{1}",
                            new Object[]{fileInfo.toString(), nbRowsAffected});
                }

            } catch (SQLException ex) {
                Popup.error(fileInfo.toString(), ex);
            }

            return false;
        }
    }

    /**
     * Deletes a file from the database.
     *
     * @param idFile the ID of the file to be deleted
     * @return true if the deletion is successful, false otherwise
     */
    public boolean delete(int idFile) {
        synchronized (dbConn) {
            try (PreparedStatement stDeleteFile = dbConn.connection.prepareStatement(
                    "DELETE FROM file WHERE idFile=?")) {

                stDeleteFile.setInt(1, idFile);

                int nbRowsAffected = stDeleteFile.executeUpdate();
                if (nbRowsAffected == 1) {
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "stDeleteFile, idFile={0} # row(s) affected: +{1}",
                            new Object[]{idFile, nbRowsAffected});
                    return false;
                }

            } catch (SQLException ex) {
                Popup.error("stDeleteFile(" + idFile + ")", ex);
            }

            return false;
        }
    }

    /**
     * Sets the saved flag for a file in the database.
     *
     * @param idFile the ID of the file to be updated
     * @return true if the update is successful, false otherwise
     */
    public boolean setSaved(int idFile) {
        synchronized (dbConn) {
            try (PreparedStatement stUpdateSavedFile = dbConn.connection.prepareStatement(
                    "UPDATE file SET saved=1 WHERE idFile=?")) {

                stUpdateSavedFile.setInt(1, idFile);

                int nbRowsAffected = stUpdateSavedFile.executeUpdate();
                if (nbRowsAffected == 1) {
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "setFileSaved, idFile={0} # row(s) affected: +{1}",
                            new Object[]{idFile, nbRowsAffected});
                    return false;
                }

            } catch (SQLException ex) {
                Popup.error("setFileSaved(" + idFile + ")", ex);
            }

            return false;
        }
    }

    /**
     * Updates file tags in the database.
     *
     * @param fileInfo the file information to be updated
     * @return true if the update is successful, false otherwise
     */
    public boolean update(FileInfoInt fileInfo) {
        synchronized (dbConn) {
            try (PreparedStatement stUpdateFileTag = dbConn.connection.prepareStatement(
                    """
                UPDATE file
                SET format=?, title=?, artist=?, album=?, albumArtist=?,
                genre=?, discNo=?,
                trackNo=?, year=?, comment=?,
                length=?, bitRate=?, size=?, modifDate=?, trackTotal=?,
                discTotal=?, BPM=?,
                nbCovers=?, coverHash=?, trackGain=?, albumGain=?
                WHERE idPath=? AND idFile=?
                """)) {

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
                            new Object[]{fileInfo.toString(), nbRowsAffected});
                }

            } catch (SQLException ex) {
                Popup.error("updateTags(" + fileInfo.toString() + ")", ex);
            }

            return false;
        }
    }

    /**
     * Updates the last played timestamp and play counter for a file in the
     * database.
     *
     * @param fileInfo the file information to be updated
     * @return true if the update is successful, false otherwise
     */
    public boolean updateLastPlayedAndCounter(FileInfoInt fileInfo) {
        synchronized (dbConn) {
            try (PreparedStatement stUpdateFileLastPlayedAndCounter = dbConn.connection.prepareStatement(
                    "UPDATE file SET lastplayed=?, playCounter=? WHERE idFile=?")) {

                stUpdateFileLastPlayedAndCounter.setString(1, DateTime.getCurrentUtcSql());
                stUpdateFileLastPlayedAndCounter.setInt(2, fileInfo.getPlayCounter() + 1);
                stUpdateFileLastPlayedAndCounter.setInt(3, fileInfo.getIdFile());

                int nbRowsAffected = stUpdateFileLastPlayedAndCounter.executeUpdate();
                if (nbRowsAffected == 1) {
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE,
                            "stUpdateFileLastPlayedAndCounter, fileInfo={0} # row(s) affected: +{1}",
                            new Object[]{fileInfo.toString(), nbRowsAffected});
                }

            } catch (SQLException ex) {
                Popup.error("updateLastPlayedAndCounter(" + fileInfo.toString() + ")", ex);
            }

            return false;
        }
    }

    /**
     * Updates the rating for a file in the database.
     *
     * @param fileInfo the file information to be updated
     * @return true if the update is successful, false otherwise
     */
    public boolean updateRating(FileInfoInt fileInfo) {
        synchronized (dbConn) {
            try (PreparedStatement stUpdateFileRating = dbConn.connection.prepareStatement(
                    "UPDATE file SET rating=?, ratingModifDate=datetime('now') WHERE idFile=?")) {

                stUpdateFileRating.setInt(1, fileInfo.getRating());
                stUpdateFileRating.setInt(2, fileInfo.getIdFile());

                int nbRowsAffected = stUpdateFileRating.executeUpdate();
                if (nbRowsAffected == 1) {
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE,
                            "stUpdateFileRating, fileInfo={0} # row(s) affected: +{1}",
                            new Object[]{fileInfo.toString(), nbRowsAffected});
                }

            } catch (SQLException ex) {
                Popup.error("updateRating(" + fileInfo.toString() + ")", ex);
            }

            return false;
        }
    }

    /**
     * Updates the genre for a file in the database.
     *
     * @param fileInfo the file information to be updated
     * @return true if the update is successful, false otherwise
     */
    public boolean updateFileGenre(FileInfoInt fileInfo) {
        synchronized (dbConn) {
            try (PreparedStatement stUpdateFileGenre = dbConn.connection.prepareStatement(
                    "UPDATE file SET genre=?, genreModifDate=datetime('now') WHERE idFile=?")) {

                stUpdateFileGenre.setString(1, fileInfo.getGenre());
                stUpdateFileGenre.setInt(2, fileInfo.getIdFile());

                int nbRowsAffected = stUpdateFileGenre.executeUpdate();
                if (nbRowsAffected == 1) {
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE,
                            "stUpdateFileGenre, fileInfo={0} # row(s) affected: +{1}",
                            new Object[]{fileInfo.toString(), nbRowsAffected});
                }

            } catch (SQLException ex) {
                Popup.error("updateGenre(" + fileInfo.toString() + ")", ex);
            }

            return false;
        }
    }

    /**
     * Updates the idPath for all files with the given idPath to a new idPath in
     * the database.
     *
     * @param idPath the current idPath to be updated
     * @param newIdPath the new idPath to set
     * @return true if the update is successful, false otherwise
     */
    public boolean updateIdPath(int idPath, int newIdPath) {
        synchronized (dbConn) {
            try (PreparedStatement stUpdateIdPathInFile = dbConn.connection.prepareStatement(
                    "UPDATE file SET idPath=? WHERE idPath=?")) {

                stUpdateIdPathInFile.setInt(1, newIdPath);
                stUpdateIdPathInFile.setInt(2, idPath);

                int nbRowsAffected = stUpdateIdPathInFile.executeUpdate();
                if (nbRowsAffected > 0) {
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE,
                            "updateIdPath, idPath={0}, newIdPath={1} # row(s) affected: +{2}",
                            new Object[]{idPath, newIdPath, nbRowsAffected});
                }

            } catch (SQLException ex) {
                Popup.error("updateIdPath(" + idPath + ", " + newIdPath + ")", ex);
            }

            return false;
        }
    }

    /**
     * Updates the modification date and name for a file in the database.
     *
     * @param idFile the ID of the file to be updated
     * @param modifDate the new modification date
     * @param name the new name
     * @return true if the update is successful, false otherwise
     */
    public boolean updateModifDate(int idFile, Date modifDate, String name) {
        synchronized (dbConn) {
            try (PreparedStatement stUpdateFileModifDate = dbConn.connection.prepareStatement(
                    "UPDATE file SET name=?, modifDate=? WHERE idFile=?")) {

                stUpdateFileModifDate.setString(1, name);
                stUpdateFileModifDate.setString(2, DateTime.formatUTCtoSqlUTC(modifDate));
                stUpdateFileModifDate.setInt(3, idFile);

                int nbRowsAffected = stUpdateFileModifDate.executeUpdate();
                if (nbRowsAffected == 1) {
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE,
                            "stUpdateFile, idFile={0} # row(s) affected: +{1}",
                            new Object[]{idFile, nbRowsAffected});
                }

            } catch (SQLException ex) {
                Popup.error("updateModifDate(" + idFile + ", \"" + modifDate.toString() + "\", \"" + name + "\")", ex);
            }

            return false;
        }
    }
    
       /**
     * Updates the modification date of files based on a new tag value
     *
     * @param newTag
     * @return
     */
    public boolean updateModifDate(String newTag) {
        synchronized (dbConn) {
            try {
                String sql = """
                UPDATE file SET tagsModifDate=datetime('now') WHERE idFile=(SELECT TF.idFile
                FROM tag T
                JOIN tagfile TF ON TF.idTag=T.id
                WHERE T.value=?)""";
                try (PreparedStatement stUpdateTagsModifDate = dbConn.getConnection().prepareStatement(sql)) {

                    stUpdateTagsModifDate.setString(1, newTag);
                    int nbRowsAffected = stUpdateTagsModifDate.executeUpdate();
                    if (nbRowsAffected >= 0) {
                        return true;
                    } else {
                        Jamuz.getLogger().log(Level.SEVERE, "stUpdateTagsModifDate, "
                                + "newTag={0} # row(s) affected: +{1}",
                                new Object[]{newTag, nbRowsAffected});
                        return false;
                    }
                }
            } catch (SQLException ex) {
                Popup.error("updateTagsModifDate(" + newTag + ")", ex);
                return false;
            }
        }
    }

    /**
     * Update modification date for a file
     *
     * @param fileInfo
     * @return
     */
    public boolean updateModifDate(FileInfo fileInfo) {
        synchronized (dbConn) {
            try (PreparedStatement stUpdateTagsModifDate = dbConn.connection.prepareStatement(
                    "UPDATE file SET tagsModifDate=datetime('now') WHERE idFile=?")) {
                stUpdateTagsModifDate.setInt(1, fileInfo.getIdFile());
                int nbRowsAffected = stUpdateTagsModifDate.executeUpdate();
                if (nbRowsAffected == 1) {
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "stUpdateTagsModifDate, "
                            + "fileInfo={0} # row(s) affected: +{1}",
                            new Object[]{fileInfo.toString(), nbRowsAffected});
                    return false;
                }
            } catch (SQLException ex) {
                Popup.error("updateTagsModifDate(" + fileInfo.toString() + ")", ex);
                return false;
            }
        }
    }

}
