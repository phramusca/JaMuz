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
package jamuz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

/**
 * AppVersion class for handling application version updates.
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class AppVersion {

    private final String currentVersion;
    private String latestVersion;
    private File assetFile;
    private int assetSize;

    public AppVersion(String currentVersion, String latestVersion) {
        this.currentVersion = currentVersion;
        this.latestVersion = latestVersion;
    }

    public boolean isNewVersion() {
        return !currentVersion.equals("vnull") && compareVersionStrings(latestVersion, currentVersion) > 0;
    }

    void setAsset(File assetFile, int size) {
        if (assetFile == null) {
            throw new IllegalArgumentException("Asset file cannot be null");
        }
        this.assetFile = assetFile;
        this.assetSize = size;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public static int compareVersionStrings(String version1, String version2) {
        String[] parts1 = version1.split("\\.");
        String[] parts2 = version2.split("\\.");

        int minLength = Math.min(parts1.length, parts2.length);

        for (int i = 0; i < minLength; i++) {
            int v1 = Integer.parseInt(parts1[i].replaceAll("\\D", ""));
            int v2 = Integer.parseInt(parts2[i].replaceAll("\\D", ""));

            if (v1 < v2) {
                return -1;
            } else if (v1 > v2) {
                return 1;
            }
        }

        if (parts1.length < parts2.length) {
            return -1;
        } else if (parts1.length > parts2.length) {
            return 1;
        }

        return 0;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public File getAssetFile() {
        return assetFile;
    }

    public int getAssetSize() {
        return assetSize;
    }

    @Override
    public String toString() {
        return "Current: " + currentVersion + ". Latest: " + latestVersion + ". ";
    }

    boolean isAssetValid() {
        return assetFile.exists() && assetFile.length() == assetSize;
    }

    boolean isUnzippedAssetValid() {
        try (SevenZFile sevenZFile = new SevenZFile(assetFile)) {
            SevenZArchiveEntry entry;
            while ((entry = sevenZFile.getNextEntry()) != null) {
                File entryFile = Jamuz.getFile(entry.getName(), "data", "cache", "system", "update", latestVersion);
                if (!entry.isDirectory()) {
                    if (!entryFile.exists() || entryFile.length() != entry.getSize()) {
                        return false;
                    }
                }
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    void unzipAsset(ICallBackVersionCheck callBackVersionCheck) throws IOException {
        callBackVersionCheck.onUnzipCount(this);
        long totalBytes = calculateTotalBytes();
        callBackVersionCheck.onUnzipStart();
        unzipEntries(callBackVersionCheck, totalBytes);
    }

    private long calculateTotalBytes() throws IOException {
        long totalBytes = 0;
        try (SevenZFile sevenZFile = new SevenZFile(assetFile)) {
            SevenZArchiveEntry entry;
            while ((entry = sevenZFile.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    totalBytes += entry.getSize();
                }
            }
        }
        return totalBytes;
    }

    private void unzipEntries(ICallBackVersionCheck callBackVersionCheck, long totalBytes) throws IOException {
        long totalBytesRead = 0;
        try (SevenZFile sevenZFile = new SevenZFile(assetFile)) {
            SevenZArchiveEntry entry;
            while ((entry = sevenZFile.getNextEntry()) != null) {
                File outputFile = Jamuz.getFile(entry.getName(), "data", "cache", "system", "update", latestVersion);
                if (entry.isDirectory()) {
                    createDirectory(outputFile);
                } else {
                    totalBytesRead = writeFile(callBackVersionCheck, totalBytes, totalBytesRead, sevenZFile, entry, outputFile);
                }
            }
        }
    }

    private void createDirectory(File outputFile) {
        if (!outputFile.exists()) {
            outputFile.mkdirs();
        }
    }

    private long writeFile(ICallBackVersionCheck callBackVersionCheck, long totalBytes, long totalBytesRead, SevenZFile sevenZFile, SevenZArchiveEntry entry, File outputFile) throws IOException {
        createParentDirectories(outputFile);
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = sevenZFile.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                int progress = (int) ((totalBytesRead * 100) / totalBytes);
                callBackVersionCheck.onUnzipProgress(this, entry.getName(), progress);
            }
        }
        return totalBytesRead;
    }

    private void createParentDirectories(File outputFile) {
        File parentDir = outputFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
    }
}
