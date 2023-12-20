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

import jamuz.Jamuz;
import jamuz.utils.Popup;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author raph
 */
public class DaoSchemaWrite {

    private final DbConn dbConn;
    private final DaoSchema daoSchema;

    /**
     *
     * @param dbConn
     * @param daoSchema
     */
    public DaoSchemaWrite(DbConn dbConn, DaoSchema daoSchema) {
        this.dbConn = dbConn;
        this.daoSchema = daoSchema;
    }

    public boolean update(int requestedVersion) {
        ArrayList<DbVersion> versions = new ArrayList<>();
        if (!daoSchema.getVersionHistory(versions)) {
            return false;
        }
        if (versions.size() < 1) {
            return updateVersion(0, requestedVersion);
        } else {
            Optional<DbVersion> currentVersion = versions.stream().filter((t) -> {
                return t.getUpgradeEnd().after(new Date(0));
            }).findFirst();
            if (!currentVersion.isPresent()) {
                return updateVersion(0, requestedVersion);
            } else {
                if (currentVersion.get().getVersion() == requestedVersion) {
                    return true;
                }
                return updateVersion(currentVersion.get().getVersion(), requestedVersion);
            }
        }
    }

    private boolean updateVersion(int oldVersion, int newVersion) {
        Popup.info("Database requires an upgrade from version " + oldVersion + " to " + newVersion
                + ".\n\nDo not worry, a backup will be made.\nNo output is expected, just wait a little.\n\nClick OK to proceed.");
        dbConn.info.backupDB(FilenameUtils.getFullPath(dbConn.info.getLocationWork()));
        for (int currentVersion = oldVersion; currentVersion < newVersion; currentVersion++) {
            int nextVersion = currentVersion + 1;
            if (oldVersion != 0 && !insertVersionHistory(nextVersion)) {
                return false;
            }
            if (!updateSchemaToNewVersion(nextVersion)) {
                return false;
            }
            if (oldVersion == 0) {
                if (!insertVersionHistory(nextVersion)) {
                    return false;
                }
            }
            if (!updateVersionHistory(nextVersion)) {
                return false;
            }
        }
        return true;
    }

    private boolean updateSchemaToNewVersion(int newVersion) {
        try {
            File scriptFile = Jamuz.getFile(newVersion + ".sql", "data", "system", "sql");
            String locationWork = this.dbConn.info.getLocationWork();
            ProcessBuilder builder = new ProcessBuilder("sqlite3", locationWork);
            builder.redirectInput(scriptFile);
            Process process = builder.start();
            process.waitFor();
            return true;
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(DbConnJaMuz.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private boolean insertVersionHistory(int version) {
        try {
            PreparedStatement stInsertVersionHistory = dbConn.connection.prepareStatement(
                    "INSERT INTO versionHistory (version, upgradeStart) "
                    + "VALUES (?, datetime('now')) "); // NOI18N
            stInsertVersionHistory.setInt(1, version);
            int nbRowsAffected = stInsertVersionHistory.executeUpdate();
            if (nbRowsAffected == 1) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "stInsertVersionHistory, fileInfo={0} # row(s) affected: +{1}",
                        new Object[]{version, nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("insertVersionHistory(" + version + ")", ex); // NOI18N
            return false;
        }
    }

    private boolean updateVersionHistory(int version) {
        try {
            PreparedStatement stUpdateVersionHistory = dbConn.connection.prepareStatement(
                    "UPDATE versionHistory SET upgradeEnd=datetime('now') "
                    + "WHERE version=?"); // NOI18N
            stUpdateVersionHistory.setInt(1, version);
            int nbRowsAffected = stUpdateVersionHistory.executeUpdate();
            if (nbRowsAffected == 1) {
                return true;
            } else {
                Jamuz.getLogger().log(Level.SEVERE, "stUpdateVersionHistory, fileInfo={0} # row(s) affected: +{1}",
                        new Object[]{version, nbRowsAffected}); // NOI18N
                return false;
            }
        } catch (SQLException ex) {
            Popup.error("updateVersionHistory(" + version + ")", ex); // NOI18N
            return false;
        }
    }
}
