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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * @author raph
 */
public class AppVersionCheck {

    private final ICallBackVersionCheck callBackVersionCheck;
    private final AppVersion appVersion;
    private boolean includePreRelease = false;
    private ScheduledExecutorService scheduler;

    /**
     *
     * @param callBackVersionCheck
     */
    public AppVersionCheck(ICallBackVersionCheck callBackVersionCheck) {
        this.callBackVersionCheck = callBackVersionCheck;
        String version = Main.class.getPackage().getImplementationVersion();
        String currentVersion = "v0.7.0";// + version; //FIXME ! SET IT BACK !!!!
        appVersion = new AppVersion(currentVersion, "Unknown");
        callBackVersionCheck.onCheck(appVersion, "");
    }

    public void start(boolean includePreRelease) {
        this.includePreRelease = includePreRelease;
        new Thread() {
            @Override
            public void run() {
                if (scheduler != null && !scheduler.isShutdown()) {
                    try {
                        scheduler.shutdown();
                        scheduler.awaitTermination(30, TimeUnit.MINUTES);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                callBackVersionCheck.onCheck(appVersion, "Initiating ...");
                scheduler = Executors.newSingleThreadScheduledExecutor();
                long initialDelay = 0; // Delay before the first run (0 for immediate execution)
                long period = 24 * 60 * 60; // 24 hours in seconds
                scheduler.scheduleAtFixedRate(() -> {
                    checkNewVersion();
                }, initialDelay, period, TimeUnit.SECONDS);
            }
        }.start();
    }

    private void checkNewVersion() {
        try {
            callBackVersionCheck.onCheck(appVersion, "Checking for new version ...");
            OkHttpClient client = new OkHttpClient();
            JsonArray assets = new JsonArray(0);
            if (includePreRelease) {
                Request request = new Request.Builder()
                        .url("https://api.github.com/repos/phramusca/jamuz/releases?per_page=1&page=1")
                        .build();
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                Gson gson = new Gson();
                JsonArray releasesArray = gson.fromJson(responseBody, JsonArray.class);
                if (releasesArray.size() > 0) {
                    JsonObject latestRelease = releasesArray.get(0).getAsJsonObject();
                    String latestVersion = latestRelease.get("tag_name").getAsString();
                    assets = latestRelease.getAsJsonArray("assets");
                    appVersion.setLatestVersion(latestVersion);
                }
            } else {
                Request request = new Request.Builder()
                        .url("https://api.github.com/repos/phramusca/jamuz/releases/latest")
                        .build();
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                Gson gson = new Gson();
                JsonObject releaseData = gson.fromJson(responseBody, JsonObject.class);
                String latestVersion = releaseData.get("tag_name").getAsString();
                assets = releaseData.getAsJsonArray("assets");
                appVersion.setLatestVersion(latestVersion);
            }

            if (appVersion.isNewVersion()) {
                if (!assets.isJsonNull() && assets.size() > 0) {
                    callBackVersionCheck.onCheck(appVersion, "Getting new version ...");
                    String downloadURL = assets.get(0).getAsJsonObject().get("browser_download_url").getAsString();
                    String assetName = assets.get(0).getAsJsonObject().get("name").getAsString();
                    File assetFile = Jamuz.getFile(assetName, "data", "cache", "system", "update", appVersion.getLatestVersion());
                    int size = assets.get(0).getAsJsonObject().get("size").getAsInt();
                    appVersion.setAsset(assetFile, size);
                    if (appVersion.isAssetValid()) {
                        if (!appVersion.isUnzippedAssetValid()) {
                            appVersion.unzipAsset(callBackVersionCheck);
                        }
                        if (appVersion.isUnzippedAssetValid()) {
                            callBackVersionCheck.onNewVersion(appVersion);
                        } else {
                            callBackVersionCheck.onCheckResult(appVersion, "Unzip failed.");
                        }
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
                            appVersion.unzipAsset(callBackVersionCheck);
                            if (appVersion.isUnzippedAssetValid()) {
                                callBackVersionCheck.onNewVersion(appVersion);
                            } else {
                                callBackVersionCheck.onCheckResult(appVersion, "Unzip failed.");
                            }
                        } else {
                            appVersion.getAssetFile().delete();
                            callBackVersionCheck.onCheckResult(appVersion, "Error: " + downloadResponse.message());
                        }
                    }
                } else {
                    callBackVersionCheck.onCheckResult(appVersion, "No asset found in release!");
                }
            } else {
                callBackVersionCheck.onCheckResult(appVersion, "You are running the latest version.");
            }
        } catch (IOException ex) {
            appVersion.getAssetFile().delete();
            callBackVersionCheck.onCheckResult(appVersion, ex.getLocalizedMessage());
        }
    }

    public AppVersion getAppVersion() {
        return appVersion;
    }
}
