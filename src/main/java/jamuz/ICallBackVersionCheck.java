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

    public void onStartUnzipCount(AppVersion appVersion);

    public void onStartUnzip(int entryCount);

    public void onUnzippedFile(String name);

    public void onDownloading(AppVersion assetFile);
}
