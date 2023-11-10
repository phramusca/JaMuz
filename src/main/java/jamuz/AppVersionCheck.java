/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jamuz;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author raph
 */
public class AppVersionCheck {
    
    private final ICallBackVersionCheck callBackVersionCheck;
    private final AppVersion appVersion;

    public AppVersionCheck(ICallBackVersionCheck callBackVersionCheck) {
        this.callBackVersionCheck = callBackVersionCheck;
        String version = Main.class.getPackage().getImplementationVersion();
        String currentVersion = "v"+version;
        currentVersion = "v0.5.61"; //FIXME ! Remove when done with tests
        appVersion = new AppVersion(currentVersion, "Unknown");
        callBackVersionCheck.onCheck(appVersion, "Checking version ...");
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        long initialDelay = 0; // Delay before the first run (0 for immediate execution)
        long period = 24 * 60 * 60; // 24 hours in seconds
        scheduler.scheduleAtFixedRate(this::checkNewVersion, initialDelay, period, TimeUnit.SECONDS);
    }
    
//    public void checkVersions() {
//        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
//        long initialDelay = 0; // Delay before the first run (0 for immediate execution)
//        long period = 24 * 60 * 60; // 24 hours in seconds
//        scheduler.scheduleAtFixedRate(this::checkNewVersion, initialDelay, period, TimeUnit.SECONDS);
//    }
    
    private void checkNewVersion() {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.github.com/repos/phramusca/jamuz/releases/latest")
                    .build();
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            Gson gson = new Gson();
            JsonObject releaseData = gson.fromJson(responseBody, JsonObject.class);
            String latestVersion = releaseData.get("tag_name").getAsString();
            JsonArray assets = releaseData.getAsJsonArray("assets");
            appVersion.setLatestVersion(latestVersion);
            if(appVersion.isNewVersion()) {
                if (!assets.isJsonNull() && assets.size() > 0) {
                    String downloadURL = assets.get(0).getAsJsonObject().get("browser_download_url").getAsString();
                    String assetName = assets.get(0).getAsJsonObject().get("name").getAsString();
                    File assetFile = Jamuz.getFile(assetName, "cache", "system", "update");
                    appVersion.setAsset(assetFile);
                    if(!assetFile.exists() ) {
                        callBackVersionCheck.onDownloading(appVersion);
                        Request downloadRequest = new Request.Builder()
                            .url(downloadURL)
                            .build();
                        Response downloadResponse = client.newCall(downloadRequest).execute();
                        byte[] zipBytes = downloadResponse.body().bytes();
                        FileUtils.writeByteArrayToFile(assetFile, zipBytes);
                        unzipAsset(appVersion);
                    }
                    callBackVersionCheck.onNewVersion(appVersion);
                } else {
                   callBackVersionCheck.onCheck(appVersion, "No asset found in release!");
                }
            } else {
                callBackVersionCheck.onCheck(appVersion, "You are running the latest version.");
            }
        } catch (IOException ex) {
            callBackVersionCheck.onCheck(appVersion, ex.getLocalizedMessage());
        }
    }

    private void unzipAsset(AppVersion appVersion) {
        callBackVersionCheck.onStartUnzipCount(appVersion);
        int entryCount = 0;
        try (SevenZFile sevenZFile = new SevenZFile(appVersion.getAssetFile())) {
            while ((sevenZFile.getNextEntry()) != null) {
                entryCount++;
            }
        } catch (IOException ex) {
            Logger.getLogger(AppVersionCheck.class.getName()).log(Level.SEVERE, null, ex);
        }

        callBackVersionCheck.onStartUnzip(entryCount);
        try (SevenZFile sevenZFile = new SevenZFile(appVersion.getAssetFile())) {
            SevenZArchiveEntry entry;
            while ((entry = sevenZFile.getNextEntry()) != null) {
                File outputFile = Jamuz.getFile(entry.getName(), "cache", "system", "update");
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
                        }
                    }
                }
                callBackVersionCheck.onUnzippedFile(entry.getName());
            }
        } catch (IOException ex) {
            Logger.getLogger(AppVersionCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public AppVersion getAppVersion() {
        return appVersion;
    }
}
