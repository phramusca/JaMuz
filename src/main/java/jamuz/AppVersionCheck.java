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
 * @author phramusca <phramusca@gmail.com>
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
        String currentVersion = "v" + version;
        appVersion = new AppVersion(currentVersion, "Unknown");
        callBackVersionCheck.onCheck(appVersion, "");
    }

    public void start(boolean includePreRelease) {
        this.includePreRelease = includePreRelease;
        new Thread() {
            @Override
            public void run() {
                if (scheduler != null && !scheduler.isShutdown()) {
                    shutdownScheduler();
                }
                callBackVersionCheck.onCheck(appVersion, "Initiating ...");
                scheduler = Executors.newSingleThreadScheduledExecutor();
                long initialDelay = 0; // Delay before the first run (0 for immediate execution)
                long period = 24 * 60 * 60; // 24 hours in seconds
                scheduler.scheduleAtFixedRate(() -> checkNewVersion(), initialDelay, period, TimeUnit.SECONDS);
            }
        }.start();
    }

    void shutdownScheduler() {
        try {
            scheduler.shutdown();
            scheduler.awaitTermination(30, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void checkNewVersion() {
        try {
            callBackVersionCheck.onCheck(appVersion, "Checking for new version ...");
            OkHttpClient client = new OkHttpClient();
            JsonArray assets = fetchReleaseAssets(client);
            if (appVersion.isNewVersion()) {
                handleNewVersion(client, assets);
            } else {
                callBackVersionCheck.onCheckResult(appVersion, "You are running the latest version.");
            }
        } catch (IOException ex) {
            appVersion.getAssetFile().delete();
            String errorMessage = ex.getLocalizedMessage();
            if (errorMessage == null || errorMessage.isEmpty()) {
                errorMessage = ex.getClass().getName() + ": " + ex.getMessage();
            }
            callBackVersionCheck.onCheckResult(appVersion, errorMessage);
        }
    }

    private JsonArray fetchReleaseAssets(OkHttpClient client) throws IOException {
        Request request = new Request.Builder()
                .url(includePreRelease ? "https://api.github.com/repos/phramusca/jamuz/releases?per_page=1&page=1" : "https://api.github.com/repos/phramusca/jamuz/releases/latest")
                .build();
        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();
        Gson gson = new Gson();
        if (includePreRelease) {
            JsonArray releasesArray = gson.fromJson(responseBody, JsonArray.class);
            if (releasesArray.size() > 0) {
                JsonObject latestRelease = releasesArray.get(0).getAsJsonObject();
                appVersion.setLatestVersion(latestRelease.get("tag_name").getAsString());
                return latestRelease.getAsJsonArray("assets");
            }
        } else {
            JsonObject releaseData = gson.fromJson(responseBody, JsonObject.class);
            appVersion.setLatestVersion(releaseData.get("tag_name").getAsString());
            return releaseData.getAsJsonArray("assets");
        }
        return new JsonArray(0);
    }

    private void handleNewVersion(OkHttpClient client, JsonArray assets) throws IOException {
        if (!assets.isJsonNull() && assets.size() > 0) {
            callBackVersionCheck.onCheck(appVersion, "Getting new version ...");
            String downloadURL = assets.get(0).getAsJsonObject().get("browser_download_url").getAsString();
            String assetName = assets.get(0).getAsJsonObject().get("name").getAsString();
            File assetFile = Jamuz.getFile(assetName, "data", "cache", "system", "update", appVersion.getLatestVersion());
            int size = assets.get(0).getAsJsonObject().get("size").getAsInt();
            appVersion.setAsset(assetFile, size);
            if (appVersion.isAssetValid()) {
                processValidAsset();
            } else {
                downloadAndProcessAsset(client, downloadURL, assetFile);
            }
        } else {
            callBackVersionCheck.onCheckResult(appVersion, "No asset found in release!");
        }
    }

    private void processValidAsset() throws IOException {
        if (!appVersion.isUnzippedAssetValid()) {
            appVersion.unzipAsset(callBackVersionCheck);
        }
        if (appVersion.isUnzippedAssetValid()) {
            callBackVersionCheck.onNewVersion(appVersion);
        } else {
            callBackVersionCheck.onCheckResult(appVersion, "Unzip failed.");
        }
    }

    private void downloadAndProcessAsset(OkHttpClient client, String downloadURL, File assetFile) throws IOException {
        callBackVersionCheck.onDownloadRequest(appVersion);
        Request downloadRequest = new Request.Builder().url(downloadURL).build();
        Response downloadResponse = client.newCall(downloadRequest).execute();
        if (downloadResponse.isSuccessful()) {
            downloadAsset(downloadResponse, assetFile);
            processValidAsset();
        } else {
            assetFile.delete();
            callBackVersionCheck.onCheckResult(appVersion, "Error: " + downloadResponse.message());
        }
    }

    private void downloadAsset(Response downloadResponse, File assetFile) throws IOException {
        long fileSize = downloadResponse.body().contentLength();
        long bytesRead = 0;
        int bufferSize = 8 * 1024; // 8 KB buffer
        byte[] buffer = new byte[bufferSize];
        InputStream inputStream = downloadResponse.body().byteStream();
        try (FileOutputStream outputStream = new FileOutputStream(assetFile)) {
            int read;
            callBackVersionCheck.onDownloadStart();
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
                bytesRead += read;
                int progress = (int) ((bytesRead * 100) / fileSize);
                callBackVersionCheck.onDownloadProgress(appVersion, progress);
            }
        }
    }

    public AppVersion getAppVersion() {
        return appVersion;
    }
}
