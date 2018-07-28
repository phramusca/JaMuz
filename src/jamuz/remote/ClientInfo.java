/*
 * Copyright (C) 2017 phramusca ( https://github.com/phramusca/JaMuz/ )
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
package jamuz.remote;

import jamuz.Jamuz;
import jamuz.Playlist;
import jamuz.gui.swing.ProgressBar;
import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import jamuz.utils.DateTime;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ClientInfo {
	private int id;
	private final String login;
	private String pwd;
	private String name;
	private Device device;
	private StatSource statSource;
	private boolean enabled;
	private boolean remoteConnected;
	private boolean syncConnected;
	private String status="";
	private final ProgressBar progressBar;
	private final String rootPath;

	//TODO: Manage rights
//	private boolean allowRating;
//	private boolean allowControl;
//	private boolean allow...;

	public ClientInfo(String login, String pwd, String rootPath) {
		this.progressBar = new ProgressBar();
		this.login = login;
		this.pwd = pwd;
		this.id=-1;
		this.name = "";
		this.enabled = false;
		this.rootPath = rootPath;
	}
	
	public ClientInfo(int id, String login, String name, 
			String pwd, Device device, StatSource statSource,
			boolean enabled) {
		this(login, pwd, statSource.getSource().getRootPath());
		this.id = id;
		this.name = name;
		this.device = device;
		this.statSource = statSource;
		this.enabled = enabled;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRemoteConnected() {
		return remoteConnected;
	}

	public void setRemoteConnected(boolean remoteConnected) {
		this.remoteConnected = remoteConnected;
	}

	public boolean isSyncConnected() {
		return syncConnected;
	}

	public void setSyncConnected(boolean syncConnected) {
		this.syncConnected = syncConnected;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = DateTime.getCurrentLocal(DateTime.DateTimeFormat.HUMAN)+" | "+status;
	}
	
	public ProgressBar getProgressBar() {
		return progressBar;
	}
	
	public String getLogin() {
        return login;
    }
	
	public String getPwd() {
		return pwd;
	}

	void setPwd(String pwd) {
		this.pwd=pwd;
	}

	public Playlist getPlaylist() {
		return Jamuz.getPlaylist(this.device.getIdPlaylist());
	}
    
    @Override
	public String toString() {
		return this.name + " (" + this.getPlaylist().toString() + ")"; //NOI18N //NOI18N //NOI18N
	}

	public String getRootPath() {
		return rootPath;
	}
	
	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public StatSource getStatSource() {
		return statSource;
	}

	public void setStatSource(StatSource statSource) {
		this.statSource = statSource;
	}
	
	public void enable(boolean enable) {
		this.enabled=enable;
	}

	public boolean isEnabled() {
		return enabled;
	}

	void setId(int id) {
		this.id=id;
	}
}