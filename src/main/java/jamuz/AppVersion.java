package jamuz;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;

/**
 * AppVersion class for handling application version updates.
 *
 * @author phramusca
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
        if (!currentVersion.equals("vnull")) {
            return compareVersionStrings(latestVersion, currentVersion) > 0;
        }
        return false;
    }

    void setAsset(File assetFile, int size) {
        this.assetFile = assetFile;
        this.assetSize = size;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    //FIXME TEST this one compareVersionStrings
    private static int compareVersionStrings(String version1, String version2) {
        String[] parts1 = version1.split("\\.");
        String[] parts2 = version2.split("\\.");

        int minLength = Math.min(parts1.length, parts2.length);

        for (int i = 0; i < minLength; i++) {
            int v1 = Integer.parseInt(parts1[i].replaceAll("\\D", ""));
            int v2 = Integer.parseInt(parts2[i].replaceAll("\\D", ""));

            if (v1 < v2) {
                return -1; // version1 is lower
            } else if (v1 > v2) {
                return 1; // version1 is higher
            }
        }

        // If all common parts are equal, the longer version is higher
        if (parts1.length < parts2.length) {
            return -1; // version1 is lower
        } else if (parts1.length > parts2.length) {
            return 1; // version1 is higher
        }

        return 0; // versions are equal
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
        long totalBytes = 0;
        try (SevenZFile sevenZFile = new SevenZFile(assetFile)) {
            SevenZArchiveEntry entry;
            while ((entry = sevenZFile.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    totalBytes += entry.getSize();
                }
            }
        }
        callBackVersionCheck.onUnzipStart();
        long totalBytesRead = 0;
        try (SevenZFile sevenZFile = new SevenZFile(assetFile)) {
            SevenZArchiveEntry entry;
            while ((entry = sevenZFile.getNextEntry()) != null) {
                File outputFile = Jamuz.getFile(entry.getName(), "data", "cache", "system", "update", latestVersion);
                if (entry.isDirectory()) {
                    if (!outputFile.exists()) {
                        outputFile.mkdirs();
                    }
                } else {
                    // Create parent directories if they don't exist
                    File parentDir = outputFile.getParentFile();
                    if (!parentDir.exists()) {
                        parentDir.mkdirs();
                    }
                    // Create an output stream for the entry
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
                }
            }
        }
    }
}
