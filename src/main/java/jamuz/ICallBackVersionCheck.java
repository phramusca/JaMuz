/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jamuz;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public interface ICallBackVersionCheck {

	public void onNewVersion(AppVersion appVersion);

    public void onCheck(AppVersion appVersion, String msg);

    public void onUnzipCount(AppVersion appVersion);

    public void onUnzipStart();

    public void onUnzipProgress(String name, int percentComplete);

    public void onDownloadRequest(AppVersion assetFile);

    public void onDownloadStart();
    
    public void onDownloadProgress(AppVersion appVersion, int progress);
}
