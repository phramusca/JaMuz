/*
 * Copyright (C) 2026 phramusca / contributors
 *
 * Text log for database schema upgrades (before main HTML log exists).
 */
package jamuz.database;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Append-only log under {@code logs/schema_upgrade.log}.
 */
public final class SchemaUpgradeLog {

    private static final Logger LOGGER = Logger.getLogger(SchemaUpgradeLog.class.getName());
    private static final Object LOCK = new Object();
    private static volatile BufferedWriter writer;

    private SchemaUpgradeLog() {
    }

    /**
     * Prepare log directory and open (or append to) the log file.
     *
     * @param logDirectory e.g. {@code Jamuz.getLogPath()}
     */
    public static void init(String logDirectory) {
        synchronized (LOCK) {
            closeQuietly();
            if (logDirectory == null || logDirectory.isBlank()) {
                return;
            }
            try {
                Path dir = Path.of(logDirectory);
                Files.createDirectories(dir);
                Path logFile = dir.resolve("schema_upgrade.log");
                writer = Files.newBufferedWriter(logFile, StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                writer.write("==== " + Instant.now() + " schema upgrade session ====\n");
                writer.flush();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, "SchemaUpgradeLog.init", ex);
                writer = null;
            }
        }
    }

    /**
     * Append one line (with newline). Safe if init failed (falls back to java.util.logging).
     */
    public static void line(String message) {
        synchronized (LOCK) {
            if (writer != null) {
                try {
                    writer.write(Instant.now() + " " + message + "\n");
                    writer.flush();
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, "SchemaUpgradeLog.line", ex);
                }
            } else {
                LOGGER.info("[schema_upgrade] " + message);
            }
        }
    }

    public static void closeQuietly() {
        synchronized (LOCK) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                    LOGGER.log(Level.FINE, "SchemaUpgradeLog.close", ex);
                }
                writer = null;
            }
        }
    }
}
