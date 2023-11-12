/* //FIXME ZZZ Check templates ...
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
import java.io.InputStream;
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
        String currentVersion = "v" + version;
        appVersion = new AppVersion(currentVersion, "Unknown");
        callBackVersionCheck.onCheck(appVersion, "Checking version ...");
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        long initialDelay = 0; // Delay before the first run (0 for immediate execution)
        long period = 24 * 60 * 60; // 24 hours in seconds
        scheduler.scheduleAtFixedRate(this::checkNewVersion, initialDelay, period, TimeUnit.SECONDS);
    }

    //TODO: Move scheduler startup out of constructor !
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
            if (appVersion.isNewVersion()) {
                if (!assets.isJsonNull() && assets.size() > 0) {
                    String downloadURL = assets.get(0).getAsJsonObject().get("browser_download_url").getAsString();
                    String assetName = assets.get(0).getAsJsonObject().get("name").getAsString();
                    File assetFile = Jamuz.getFile(assetName, "data", "cache", "system", "update");
                    appVersion.setAsset(assetFile);
                    if (assetFile.exists()) {
                        callBackVersionCheck.onNewVersion(appVersion);
                    } else {
                        callBackVersionCheck.onDownloadRequest(appVersion);
                        Request downloadRequest = new Request.Builder()
                                .url(downloadURL)
                                .build();
                        Response downloadResponse = client.newCall(downloadRequest).execute();
                        if (downloadResponse.isSuccessful()) {
                            long fileSize = downloadResponse.body().contentLength();
                            long bytesRead = 0;
                            int bufferSize = 8 * 1024; // 8 KB buffer, you can adjust this based on your needs
                            byte[] buffer = new byte[bufferSize];
                            InputStream inputStream = downloadResponse.body().byteStream();
                            FileOutputStream outputStream = new FileOutputStream(assetFile);
                            int read;
                            callBackVersionCheck.onDownloadStart();
                            while ((read = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, read);
                                bytesRead += read;
                                int progress = (int) ((bytesRead * 100) / fileSize);
                                callBackVersionCheck.onDownloadProgress(appVersion, progress);
                            }

                            unzipAsset(appVersion);
                            callBackVersionCheck.onNewVersion(appVersion);
                        } else {
                            callBackVersionCheck.onCheck(appVersion, "Error: " + downloadResponse.message());
                        }
                    }
                } else {
                    callBackVersionCheck.onCheck(appVersion, "No asset found in release!");
                }
            } else {
                callBackVersionCheck.onCheck(appVersion, "You are running the latest version.");
            }
        } catch (IOException ex) {
            callBackVersionCheck.onCheck(appVersion, ex.getLocalizedMessage());
            //FIXME ! Clean data/cache/system/update
        }
    }

    private void unzipAsset(AppVersion appVersion) {
        callBackVersionCheck.onUnzipCount(appVersion);
        int entryCount = 0;
        try (SevenZFile sevenZFile = new SevenZFile(appVersion.getAssetFile())) {
            while ((sevenZFile.getNextEntry()) != null) {
                entryCount++;
            }
        } catch (IOException ex) {
            Logger.getLogger(AppVersionCheck.class.getName()).log(Level.SEVERE, null, ex);
        }

        callBackVersionCheck.onUnzipStart(entryCount);
        try (SevenZFile sevenZFile = new SevenZFile(appVersion.getAssetFile())) {
            SevenZArchiveEntry entry;
            while ((entry = sevenZFile.getNextEntry()) != null) {
                File outputFile = Jamuz.getFile(entry.getName(), "data", "cache", "system", "update");
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
                callBackVersionCheck.onUnzipProgress(entry.getName());
            }
        } catch (IOException ex) {
            Logger.getLogger(AppVersionCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public AppVersion getAppVersion() {
        return appVersion;
    }
}
