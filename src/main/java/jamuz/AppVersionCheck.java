/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jamuz;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author raph
 */
public class AppVersionCheck {
    
    private ICallBackVersionCheck callBackVersionCheck;
    private AppVersion appVersion;

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
                    File assetFile = Jamuz.getFile(assetName, "data", "system", "update");
                    appVersion.setAsset(assetFile);
                    if(!assetFile.exists() ) {
                        Request downloadRequest = new Request.Builder()
                            .url(downloadURL)
                            .build();
                        Response downloadResponse = client.newCall(downloadRequest).execute();
                        byte[] zipBytes = downloadResponse.body().bytes();
                        FileUtils.writeByteArrayToFile(assetFile, zipBytes);
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

    public AppVersion getAppVersion() {
        return appVersion;
    }
}
