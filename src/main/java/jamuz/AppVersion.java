package jamuz;

import jamuz.utils.FileSystem;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca
 */
public class AppVersion {

    private final String currentVersion;
    private String latestVersion;
    private File assetFile;

    public AppVersion(String currentVersion, String latestVersion) {
        this.currentVersion = currentVersion;
        this.latestVersion = latestVersion;
    }

    public void update() throws IOException {
        String assetFolderName = FilenameUtils.getBaseName(assetFile.getAbsolutePath());
        File source = Jamuz.getFile("", "cache", "system", "update", assetFolderName);
        File folder = Jamuz.getFile("", "cache", "system", "update", assetFolderName, "data", "system", "update");
        final File[] filteredFiles = filterAndSortUpdateFiles(folder);
        for (File file : filteredFiles) {
            Path path = Paths.get(file.getAbsolutePath());
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                String[] split = line.split(",");
                String action = split[0];
                String actionFile = split[1];
                File destination = Jamuz.getFile("");
                File destinationFile = new File(FilenameUtils.concat(destination.getAbsolutePath(), actionFile));
                switch (action) {
                    case "rm":
                        //FIXME ! Can't delete a folder (lib in example) with File
                        // => either support rm folder
                        // Or rm file by file (but folder would remain :( )
                        destinationFile.delete();
                        break;
                    case "cp":
                        File sourceFile = new File(FilenameUtils.concat(source.getAbsolutePath(), actionFile));
                        //FIXME ! Support (if not already) copying folder (restore removed methods)
                        FileSystem.copyFile(sourceFile, destinationFile);
                    default:
                        throw new AssertionError();
                }
            }
        }
    }
    
    private void copyFilesToTarget(AppVersion appVersion, String... targetSubfolders) {
        String assetFolderName = FilenameUtils.getBaseName(appVersion.getAssetFile().getAbsolutePath());
        String[] assetPath = concatenateDirectoryNames(new String[] {"cache", "system", "update", assetFolderName}, targetSubfolders);
        File[] files = Jamuz.getFiles(assetPath);
        for (File fileUpdate : files) {
            File fileCurrent = Jamuz.getFile(FilenameUtils.getName(fileUpdate.getAbsolutePath()), targetSubfolders);
            if (!fileCurrent.exists()) {
                try {
                    FileUtils.copyFile(fileUpdate, fileCurrent);
                } catch (IOException ex) {
                    Logger.getLogger(AppVersion.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    private void copyFileToTarget(AppVersion appVersion, String fileName, boolean force) {
        String assetFolderName = FilenameUtils.getBaseName(appVersion.getAssetFile().getAbsolutePath());
        File fileUpdate = Jamuz.getFile(fileName, "cache", "system", "update", assetFolderName);
        File fileCurrent = Jamuz.getFile(FilenameUtils.getName(fileUpdate.getAbsolutePath()));
        if (force || !fileCurrent.exists()) {
            try {
                FileUtils.copyFile(fileUpdate, fileCurrent);
            } catch (IOException ex) {
                Logger.getLogger(AppVersion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private String[] concatenateDirectoryNames(String[] firstNames, String[] secondNames) {
        String[] combinedNames = new String[firstNames.length + secondNames.length];

        System.arraycopy(firstNames, 0, combinedNames, 0, firstNames.length);
        System.arraycopy(secondNames, 0, combinedNames, firstNames.length, secondNames.length);

        return combinedNames;
    }

    private File[] filterAndSortUpdateFiles(File folder) {
        // Define a pattern for the file names, assuming they are in the format "previousVersion--nextVersion.csv"
        Pattern pattern = Pattern.compile("v\\d+\\.\\d+\\.\\d+->v\\d+\\.\\d+\\.\\d+.csv");

        File[] allFiles = folder.listFiles();
        List<File> updateFiles = new ArrayList<>();

        for (File file : allFiles) {
            Matcher matcher = pattern.matcher(file.getName());
            if (matcher.matches()) {
                updateFiles.add(file);
            }
        }

        // Sort the files to ensure they are in the correct order
        updateFiles.sort((file1, file2) -> {
            String version1 = file1.getName().split("->")[0];
            String version2 = file2.getName().split("->")[0];
            return compareVersionStrings(version1, version2);
        });

        // Filter files that start with currentVersion and subsequent files
        List<File> filteredFiles = new ArrayList<>();
        boolean startAdding = false;

        for (File file : updateFiles) {
            if (startAdding || file.getName().startsWith(currentVersion)) {
                filteredFiles.add(file);
                startAdding = true;
            }
        }

        return filteredFiles.toArray(File[]::new);
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
        return "Current: " + currentVersion + ". Latest: " + latestVersion + ". ";
    }
}
