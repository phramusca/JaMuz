/*
 * Copyright (C) 2023 phramusca <phramusca@gmail.com>
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
import jamuz.gui.SchemaUpgradeProgress;
import jamuz.utils.DateTime;
import jamuz.utils.Inter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca <phramusca@gmail.com>
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
        synchronized (dbConn) {
            ArrayList<DbVersion> versions = new ArrayList<>();
            if (!daoSchema.getVersionHistory(versions)) {
                SchemaUpgradeLog.line("getVersionHistory failed");
                return false;
            }
            if (!versions.isEmpty()) {
                DbVersion latest = versions.get(0);
                if (latest.getVersion() > 0 && !latest.getUpgradeEnd().after(new Date(0))) {
                    Logger.getLogger(DaoSchemaWrite.class.getName()).log(Level.WARNING,
                            "versionHistory: missing upgradeEnd for v{0}, completing now",
                            new Object[]{latest.getVersion()});
                    SchemaUpgradeLog.line("Repair: set upgradeEnd for version " + latest.getVersion());
                    if (!updateVersionHistory(latest.getVersion())) {
                        return false;
                    }
                    versions.clear();
                    if (!daoSchema.getVersionHistory(versions)) {
                        return false;
                    }
                }
            }
            if (versions.size() < 1) {
                return updateVersion(0, requestedVersion);
            } else {
                Optional<DbVersion> currentVersion = versions.stream().filter((t)
                        -> t.getUpgradeEnd().after(new Date(0)))
                        .findFirst();
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
    }

    private boolean updateVersion(int oldVersion, int newVersion) {
        synchronized (dbConn) {
            SchemaUpgradeProgress ui = SchemaUpgradeProgress.showOnEdt();
            try {
                Jamuz.getLogger().log(Level.INFO, MessageFormat.format(Inter.get("SchemaUpgrade.Prompt"), oldVersion, newVersion));
                SchemaUpgradeLog.line("Upgrade confirmed: " + oldVersion + " -> " + newVersion);

                ui.setMessage(Inter.get("SchemaUpgrade.Status.Backup"));
                if (!backupBeforeSchemaUpgrade()) {
                    SchemaUpgradeLog.line("Aborted: backup failed");
                    return false;
                }

                ui.setMessage(Inter.get("SchemaUpgrade.Status.Migration"));
                for (int currentVersion = oldVersion; currentVersion < newVersion; currentVersion++) {
                    int nextVersion = currentVersion + 1;
                    ui.setMessage(MessageFormat.format(Inter.get("SchemaUpgrade.Status.Step"), nextVersion));
                    SchemaUpgradeLog.line("Step: preparing version " + nextVersion);

                    if (oldVersion != 0 && !insertVersionHistory(nextVersion)) {
                        SchemaUpgradeLog.line("insertVersionHistory failed for " + nextVersion);
                        return false;
                    }
                    if (!updateSchemaToNewVersion(nextVersion)) {
                        SchemaUpgradeLog.line("updateSchemaToNewVersion failed for " + nextVersion);
                        Jamuz.getLogger().log(Level.SEVERE, Inter.get("SchemaUpgrade.FailedSeeLog"));
                        return false;
                    }
                    if (oldVersion == 0) {
                        if (!insertVersionHistory(nextVersion)) {
                            SchemaUpgradeLog.line("insertVersionHistory (post-sql) failed for " + nextVersion);
                            return false;
                        }
                    }
                    if (!updateVersionHistory(nextVersion)) {
                        SchemaUpgradeLog.line("updateVersionHistory failed for " + nextVersion);
                        Jamuz.getLogger().log(Level.SEVERE, Inter.get("SchemaUpgrade.FailedSeeLog"));
                        return false;
                    }
                    SchemaUpgradeLog.line("Step completed: version " + nextVersion);
                }
                SchemaUpgradeLog.line("Upgrade finished OK");
                return true;
            } finally {
                ui.dispose();
            }
        }
    }

    private boolean backupBeforeSchemaUpgrade() {
        File logsDir = new File(Jamuz.getLogPath());
        try {
            FileUtils.forceMkdir(logsDir);
        } catch (IOException ex) {
            SchemaUpgradeLog.line("Cannot create logs directory: " + ex.getMessage());
            Jamuz.getLogger().log(Level.SEVERE, MessageFormat.format(Inter.get("Error.SchemaUpgrade.BackupFailed"), ex.getMessage()));
            return false;
        }
        if (dbConn.info.getLibType() != DbInfo.LibType.Sqlite) {
            boolean ok = dbConn.info.backupDB(logsDir.getAbsolutePath());
            if (!ok) {
                SchemaUpgradeLog.line("backupDB returned false (non-SQLite)");
            } else {
                SchemaUpgradeLog.line("backupDB OK (non-SQLite) under " + logsDir.getAbsolutePath());
            }
            return ok;
        }
        String stamp = DateTime.formatUTC(new Date(), DateTime.DateTimeFormat.FILE, true);
        String base = FilenameUtils.getBaseName(dbConn.info.getLocationWork());
        File backupFile = new File(logsDir, base + "_schema_backup_" + stamp + ".db");
        try {
            if (backupFile.exists()) {
                FileUtils.deleteQuietly(backupFile);
            }
            sqliteVacuumIntoBackup(backupFile);
            if (!backupFile.isFile() || backupFile.length() == 0L) {
                throw new IOException("Backup file missing or empty after VACUUM INTO");
            }
            SchemaUpgradeLog.line("Backup OK (VACUUM INTO): " + backupFile.getAbsolutePath());
            return true;
        } catch (Exception ex) {
            SchemaUpgradeLog.line("VACUUM INTO backup failed, trying file copy: " + ex.getMessage());
            try {
                try (Statement st = dbConn.connection.createStatement()) {
                    st.execute("PRAGMA wal_checkpoint(TRUNCATE)");
                }
            } catch (SQLException sqlEx) {
                SchemaUpgradeLog.line("wal_checkpoint: " + sqlEx.getMessage());
            }
            try {
                FileUtils.copyFile(new File(dbConn.info.getLocationWork()), backupFile);
                SchemaUpgradeLog.line("Backup OK (file copy): " + backupFile.getAbsolutePath());
                return true;
            } catch (IOException ioe) {
                SchemaUpgradeLog.line("Backup copy failed: " + ioe.getMessage());
                Jamuz.getLogger().log(Level.SEVERE, MessageFormat.format(Inter.get("Error.SchemaUpgrade.BackupFailed"), ioe.getMessage()));
                return false;
            }
        }
    }

    private void sqliteVacuumIntoBackup(File dest) throws SQLException {
        String path = dest.getAbsolutePath().replace("\\", "/").replace("'", "''");
        try (Statement st = dbConn.connection.createStatement()) {
            st.execute("PRAGMA wal_checkpoint(TRUNCATE)");
        }
        try (Statement st = dbConn.connection.createStatement()) {
            st.execute("VACUUM INTO '" + path + "'");
        }
    }

    /**
     * Runs {@code data/system/sql/&lt;version&gt;.sql} on the same JDBC connection (no external
     * {@code sqlite3} process, so exit codes and errors are visible and logged).
     */
    private boolean updateSchemaToNewVersion(int newVersion) {
        synchronized (dbConn) {
            try {
                File scriptFile = Jamuz.getFile(newVersion + ".sql", "data", "system", "sql");
                if (!scriptFile.isFile()) {
                    SchemaUpgradeLog.line("Migration script missing: " + scriptFile.getAbsolutePath());
                    return false;
                }
                String script = FileUtils.readFileToString(scriptFile, StandardCharsets.UTF_8);
                List<String> statements = splitSqlMigrationStatements(script);
                SchemaUpgradeLog.line("Running " + statements.size() + " SQL statement(s) from " + scriptFile.getName());
                try (Statement st = dbConn.connection.createStatement()) {
                    int n = 0;
                    for (String sql : statements) {
                        n++;
                        SchemaUpgradeLog.line("  [" + n + "/" + statements.size() + "] " + previewSql(sql));
                        st.execute(sql);
                    }
                }
                return true;
            } catch (IOException ex) {
                Logger.getLogger(DbConnJaMuz.class.getName()).log(Level.SEVERE, "updateSchemaToNewVersion read", ex);
                SchemaUpgradeLog.line("IO error: " + ex.getMessage());
                return false;
            } catch (SQLException ex) {
                Logger.getLogger(DbConnJaMuz.class.getName()).log(Level.SEVERE,
                        "updateSchemaToNewVersion(" + newVersion + ")", ex);
                SchemaUpgradeLog.line("SQL error: " + ex.getMessage());
                return false;
            }
        }
    }

    private static String previewSql(String sql) {
        String oneLine = sql.replace('\n', ' ').trim();
        return oneLine.length() > 120 ? oneLine.substring(0, 117) + "..." : oneLine;
    }

    /**
     * Splits a migration file into statements. Scripts must not put semicolons inside string literals.
     */
    private static List<String> splitSqlMigrationStatements(String script) {
        StringBuilder cleaned = new StringBuilder();
        for (String rawLine : script.split("\\R")) {
            String line = rawLine.trim();
            if (line.startsWith("--")) {
                continue;
            }
            int dash = line.indexOf("--");
            if (dash >= 0) {
                line = line.substring(0, dash).trim();
            }
            if (!line.isEmpty()) {
                cleaned.append(line).append('\n');
            }
        }
        List<String> out = new ArrayList<>();
        for (String part : cleaned.toString().split(";")) {
            String s = part.trim();
            if (!s.isEmpty()) {
                out.add(s);
            }
        }
        return out;
    }

    private boolean insertVersionHistory(int version) {
        synchronized (dbConn) {
            try (PreparedStatement stInsertVersionHistory = dbConn.connection.prepareStatement(
                    "INSERT INTO versionHistory (version, upgradeStart) VALUES (?, datetime('now')) ")) {
                stInsertVersionHistory.setInt(1, version);
                int nbRowsAffected = stInsertVersionHistory.executeUpdate();
                if (nbRowsAffected == 1) {
                    SchemaUpgradeLog.line("versionHistory: inserted start for v" + version);
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "stInsertVersionHistory, fileInfo={0} # row(s) affected: +{1}",
                            new Object[]{version, nbRowsAffected});
                    return false;
                }
            } catch (SQLException ex) {
                Jamuz.getLogger().log(Level.SEVERE, "insertVersionHistory(" + version + ")", ex);
                SchemaUpgradeLog.line("insertVersionHistory SQLException: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        }
    }

    private boolean updateVersionHistory(int version) {
        synchronized (dbConn) {
            try (PreparedStatement stUpdateVersionHistory = dbConn.connection.prepareStatement(
                    "UPDATE versionHistory SET upgradeEnd=datetime('now') WHERE version=?")) {
                stUpdateVersionHistory.setInt(1, version);
                int nbRowsAffected = stUpdateVersionHistory.executeUpdate();
                if (nbRowsAffected == 1) {
                    SchemaUpgradeLog.line("versionHistory: set upgradeEnd for v" + version);
                    return true;
                } else {
                    Jamuz.getLogger().log(Level.SEVERE, "stUpdateVersionHistory, fileInfo={0} # row(s) affected: +{1}",
                            new Object[]{version, nbRowsAffected});
                    SchemaUpgradeLog.line("updateVersionHistory: expected 1 row, got " + nbRowsAffected);
                    return false;
                }
            } catch (SQLException ex) {
                Jamuz.getLogger().log(Level.SEVERE, "updateVersionHistory(" + version + ")", ex);
                SchemaUpgradeLog.line("updateVersionHistory SQLException: " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        }
    }

}
