package jamuz;

import java.io.File;

/**
 *
 * @author raph
 */
public class AppVersion {
    private String currentVersion;
    private String latestVersion;
    private File assetFile;
    
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
    
    void setAsset(File assetFile) {
        this.assetFile = assetFile;
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
        return "Current: " + currentVersion +". Latest:" + latestVersion + ". ";
    }
}
