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
import jamuz.utils.DateTime;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ClientInfo {
	private int id;
	private final String login;
	private final String rootPath;
	private String pwd;
	private String name;
	private int idPlaylist;
	private int idDevice;
	private int idStatSource;
	private boolean enabled;
	private boolean remoteConnected;
	private boolean syncConnected;
	private String status="";
	private final ProgressBar progressBar;

	//TODO: Manage rights
//	private boolean allowRating;
//	private boolean allowControl;
//	private boolean allow...;

	public ClientInfo(String login, String rootPath, String pwd) {
		this.progressBar = new ProgressBar();
		this.login = login;
		this.rootPath = rootPath;
		this.pwd = pwd;
		this.id=-1;
		this.name = "";
		this.idPlaylist = -1;
		this.idDevice = -1;
		this.idStatSource = -1;
		this.enabled = false;
		
	}
	
	public ClientInfo(int id, String login, String rootPath, String name, 
			String pwd, int idPlaylist, int idDevice, int idStatSource,
			boolean enabled) {
		this(login, rootPath, pwd);
		this.id = id;
		this.name = name;
		this.idPlaylist = idPlaylist;
		this.idDevice = idDevice;
		this.idStatSource = idStatSource;
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

	public int getIdPlaylist() {
        return idPlaylist;
    }

	public void setIdPlaylist(int idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

	public Playlist getPlaylist() {
		return Jamuz.getPlaylist(this.idPlaylist);
	}
    
    @Override
	public String toString() {
		return this.name + " (" + this.getPlaylist().toString() + ")"; //NOI18N //NOI18N //NOI18N
	}

	public String getRootPath() {
		return rootPath;
	}

	public int getIdDevice() {
		return idDevice;
	}

	public int getIdStatSource() {
		return idStatSource;
	}

	void setIdDevice(int idDevice) {
		this.idDevice=idDevice;
	}

	void setIdStatSource(int idStatSource) {
		this.idStatSource=idStatSource;
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
