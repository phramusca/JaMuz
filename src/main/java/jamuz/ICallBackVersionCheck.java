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

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public interface ICallBackVersionCheck {

	public void onNewVersion(AppVersion appVersion);

    public void onCheck(AppVersion appVersion, String msg);
	
	public void onCheckResult(AppVersion appVersion, String msg);

    public void onUnzipCount(AppVersion appVersion);

    public void onUnzipStart();

    public void onUnzipProgress(AppVersion appVersion, String filename, int percentComplete);

    public void onDownloadRequest(AppVersion assetFile);

    public void onDownloadStart();
    
    public void onDownloadProgress(AppVersion appVersion, int progress);
}
