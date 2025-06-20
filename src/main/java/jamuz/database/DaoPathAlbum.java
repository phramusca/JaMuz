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

import jamuz.process.check.DuplicateInfo;
import jamuz.process.check.FolderInfo;
import jamuz.utils.DateTime;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author raph
 */
public class DaoPathAlbum {

    private final DbConn dbConn;
    private final DaoPathAlbumWrite albumWrite;

    private static final String SELECT_DUPLICATE = "SELECT album, albumArtist, checked, discNo, discTotal, "
            + " ifnull(round(((sum(case when rating > 0 then rating end))"
            + "/(sum(case when rating > 0 then 1.0 end))), 1), 0) AS albumRating, "
            + " P.idPath, P.strPath, P.modifDate"
            + " FROM path P JOIN file F ON F.idPath=P.idPath ";
    private static final String GROUP_DUPLICATE = " GROUP BY P.idPath, discNo"; // NOI18N

    /**
     *
     * @param dbConn
     */
    public DaoPathAlbum(DbConn dbConn) {
        this.dbConn = dbConn;
        this.albumWrite = new DaoPathAlbumWrite(dbConn);

    }

    /**
     * This is to reach writing operations (insert, update, delete)
     *
     * @return
     */
    public DaoPathAlbumWrite lock() {
        return albumWrite;
    }

    /**
     * Check if a similar album exists in JaMuz database
     *
     * @param myList
     * @param album
     * @param idPath
     * @return
     */
    public boolean checkSimilar(ArrayList<DuplicateInfo> myList, String album, int idPath) {
        if (!album.isBlank()) {
            try (PreparedStatement stSelectAlbumSimilar = dbConn.connection.prepareStatement(
                    SELECT_DUPLICATE
                    + " WHERE album LIKE ? AND P.idPath!=?"
                    + GROUP_DUPLICATE)) {

                stSelectAlbumSimilar.setString(1, "%" + album + "%");
                stSelectAlbumSimilar.setInt(2, idPath);

                getDuplicates(myList, stSelectAlbumSimilar, 1);
                return true;
            } catch (SQLException ex) {
                Popup.error("checkSimilarAlbum(" + album + ")", ex);
            }
        }
        return false;
    }

    /**
     * Check if an exact album exists in JaMuz database
     *
     * @param myList
     * @param album
     * @param idPath
     * @return
     */
    public boolean checkExact(ArrayList<DuplicateInfo> myList, String album, int idPath) {
        if (!album.isBlank()) {
            try (PreparedStatement stSelectAlbumExact = dbConn.connection.prepareStatement(
                    SELECT_DUPLICATE
                    + " WHERE album = ? AND P.idPath!=?"
                    + GROUP_DUPLICATE)) {

                stSelectAlbumExact.setString(1, album);
                stSelectAlbumExact.setInt(2, idPath);
                getDuplicates(myList, stSelectAlbumExact, 1);
                return true;
            } catch (SQLException ex) {
                Popup.error("checkExactAlbum(" + album + ")", ex);
            }
        }
        return false;
    }

    /**
     * Check if a duplicate mbId exists in JaMuz database
     *
     * @param myList
     * @param mbId
     * @return
     */
    public boolean checkDuplicate(ArrayList<DuplicateInfo> myList, String mbId) {
        if (mbId != null && !mbId.isBlank()) {
            try (PreparedStatement stSelectDuplicates = dbConn.connection.prepareStatement(
                    SELECT_DUPLICATE
                    + " WHERE mbId LIKE ? "
                    + " AND P.idPath!=? "
                    + GROUP_DUPLICATE)) {

                stSelectDuplicates.setString(1, mbId);
                getDuplicates(myList, stSelectDuplicates, 2);
                return true;
            } catch (SQLException ex) {
                Popup.error("checkDuplicate(" + mbId + ")", ex);
            }
        }
        return false;
    }

    /**
     * Check if a duplicate artist/album with discNo/discTotal couple exists in
     * JaMuz database
     *
     * @param myList
     * @param albumArtist
     * @param album
     * @param idPath
     * @param discNo
     * @param discTotal
     * @return
     */
    public boolean checkDuplicate(ArrayList<DuplicateInfo> myList,
            String albumArtist, String album, int idPath, int discNo, int discTotal) {

        if (!albumArtist.isBlank() && !album.isBlank()) {
            try (PreparedStatement stSelectDuplicates = dbConn.connection.prepareStatement(
                    SELECT_DUPLICATE
                    + " WHERE albumArtist LIKE ? "
                    + " AND album LIKE ? "
                    + " AND P.idPath!=? "
                    + " AND discNo=? "
                    + " AND discTotal=? "
                    + GROUP_DUPLICATE)) {

                stSelectDuplicates.setString(1, albumArtist);
                stSelectDuplicates.setString(2, album);
                stSelectDuplicates.setInt(3, idPath);
                stSelectDuplicates.setInt(4, discNo);
                stSelectDuplicates.setInt(5, discTotal);
                getDuplicates(myList, stSelectDuplicates, 2);
                return true;
            } catch (SQLException ex) {
                Popup.error("checkDuplicate(" + albumArtist + "," + album + ")", ex);
            }
        }
        return false;
    }

    /**
     * Check if a duplicate artist/album couple exists in JaMuz database
     *
     * @param myList
     * @param albumArtist
     * @param album
     * @param idPath
     * @return
     */
    public boolean checkDuplicate(ArrayList<DuplicateInfo> myList,
            String albumArtist, String album, int idPath) {

        if (!albumArtist.isBlank() && !album.isBlank()) {
            try (PreparedStatement stSelectDuplicates = dbConn.connection.prepareStatement(
                    SELECT_DUPLICATE
                    + " WHERE albumArtist LIKE ? "
                    + " AND album LIKE ? AND P.idPath!=? "
                    + GROUP_DUPLICATE)) {

                stSelectDuplicates.setString(1, albumArtist);
                stSelectDuplicates.setString(2, album);
                stSelectDuplicates.setInt(3, idPath);
                getDuplicates(myList, stSelectDuplicates, 1);
                return true;
            } catch (SQLException ex) {
                Popup.error("checkDuplicate(" + albumArtist + "," + album + ")", ex);
            }
        }
        return false;
    }

    /**
     * Return DuplicateInfo (for duplicate check)
     *
     * @param myList
     * @param st
     * @param errorlevel
     */
    private void getDuplicates(ArrayList<DuplicateInfo> myList, PreparedStatement st,
            int errorlevel) {
        try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                String album = dbConn.getStringValue(rs, "album");
                String albumArtist = dbConn.getStringValue(rs, "albumArtist");
                double albumRating = rs.getDouble("albumRating");
                FolderInfo.CheckedFlag checkedFlag = FolderInfo.CheckedFlag.values()[rs.getInt("checked")];
                int discNo = rs.getInt("discNo");
                int discTotal = rs.getInt("discTotal");

                Date dbModifDate = DateTime.parseSqlUtc(dbConn.getStringValue(rs, "modifDate"));
                String path = FilenameUtils.separatorsToSystem(dbConn.getStringValue(rs, "strPath"));

                FolderInfo folderInfo = new FolderInfo(rs.getInt("idPath"),
                        path, dbModifDate, FolderInfo.CheckedFlag.values()[rs.getInt("checked")]);

                myList.add(new DuplicateInfo(album, albumArtist, albumRating, checkedFlag,
                        errorlevel, discNo, discTotal, folderInfo));
            }
        } catch (SQLException ex) {
            Popup.error("getDuplicates(...)", ex);
        }
    }

}
