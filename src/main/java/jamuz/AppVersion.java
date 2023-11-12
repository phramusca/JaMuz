package jamuz;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //FIXME !! Test with a real update on a running jar
    public void update(ICallBackVersionUpdate callBackVersionUpdate) throws IOException {
        String assetFolderName = "JaMuz";
        File source = Jamuz.getFile("", "data", "cache", "system", "update", assetFolderName);
        File folder = Jamuz.getFile("", "data", "cache", "system", "update", assetFolderName, "data", "system", "update");
        final File[] filteredFiles = filterAndSortUpdateFiles(folder);
        int numberOfChanges = 0;

        // Count the number of changes
        callBackVersionUpdate.onSetup();
        for (File file : filteredFiles) {
            Path path = Paths.get(file.getAbsolutePath());
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (!line.trim().startsWith("//")) {
                    numberOfChanges++;
                }
            }
        }

        // Now iterate again to apply changes
        int currentChange = 0;
        for (File file : filteredFiles) {
            Path path = Paths.get(file.getAbsolutePath());
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (!line.trim().startsWith("//")) {
                    String[] split = line.split(",");
                    String action = split[0];
                    String actionFile = split[1];
                    File destination = Jamuz.getFile("");
                    File destinationFile = new File(FilenameUtils.concat(destination.getAbsolutePath(), actionFile));
                    performAction(action, actionFile, source, destinationFile);
                    currentChange++;
                    int progress = (int) Math.round((double) currentChange / numberOfChanges * 100.0);
                    callBackVersionUpdate.onFileUpdated(progress);
                }
            }
        }
        
        FileUtils.deleteQuietly(source);
        assetFile.delete();
    }

    private void performAction(String action, String actionFile, File source, File destinationFile) throws IOException {
        switch (action) {
            case "rm":
                FileUtils.deleteQuietly(destinationFile); //file or folder
                break;
            case "cp":
                copyFileOrFolder(actionFile, source, destinationFile, false);
                break;
            case "cpo":
                copyFileOrFolder(actionFile, source, destinationFile, true);
                break;
            default:
                throw new AssertionError();
        }
    }

    private void copyFileOrFolder(String actionFile, File source, File destinationFile, boolean force) throws IOException {
        File sourceFile = new File(FilenameUtils.concat(source.getAbsolutePath(), actionFile));
        if (sourceFile.exists() && (force || !destinationFile.exists())) {
            if (sourceFile.isDirectory()) {
                FileUtils.copyDirectory(sourceFile, destinationFile);
            } else {
                FileUtils.copyFile(sourceFile, destinationFile);
            }
        } else if (!sourceFile.exists()) {
            throw new IOException("Source file does not exist: " + sourceFile.getAbsolutePath());
        }
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
        return true; //FIXME ! Remove this
//        if (!currentVersion.equals("vnull")) {
//            return compareVersionStrings(latestVersion, currentVersion) > 0;
//        }
//        return false;
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
}
