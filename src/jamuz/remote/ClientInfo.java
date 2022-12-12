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
	private String status="";
	private final ProgressBar progressBar;
	private final String rootPath;
	private boolean isConnected;

	//TODO: Manage rights
//	private boolean allowRating;
//	private boolean allowControl;
//	private boolean allow...;

	/**
	 *
	 * @param login
	 * @param pwd
	 * @param rootPath
	 * @param name
	 */
	public ClientInfo(String login, String pwd, String rootPath, String name) {	
		isConnected=false;
		this.progressBar = new ProgressBar();
		this.login = login;
		this.pwd = pwd;
		this.id=-1;
		this.name = "";
		this.enabled = false;
		this.rootPath = rootPath;
		this.name = name;
	}
	
	/**
	 *
	 * @param id
	 * @param login
	 * @param name
	 * @param pwd
	 * @param device
	 * @param statSource
	 * @param enabled
	 */
	public ClientInfo(int id, String login, String name, 
			String pwd, Device device, StatSource statSource,
			boolean enabled) {
		this(login, pwd, statSource.getSource().getRootPath(), name);
		this.id = id;
		this.device = device;
		this.statSource = statSource;
		this.enabled = enabled;
	}
	
	/**
	 *
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 *
	 * @return
	 */
	public boolean isConnected() {
		return isConnected;
	}
	
	/**
	 *
	 * @param connected
	 */
	public void setConnected(boolean connected) {
		isConnected=connected;
	}

	/**
	 *
	 * @return
	 */
	public String getStatus() {
		return status;
	}

	/**
	 *
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = DateTime.getCurrentLocal(DateTime.DateTimeFormat.HUMAN)+" | "+status;
	}
	
	/**
	 *
	 * @return
	 */
	public ProgressBar getProgressBar() {
		return progressBar;
	}
	
	/**
	 *
	 * @return
	 */
	public String getLogin() {
        return login;
    }
	
	/**
	 *
	 * @return
	 */
	public String getPwd() {
		return pwd;
	}

	void setPwd(String pwd) {
		this.pwd=pwd;
	}

	/**
	 *
	 * @return
	 */
	public Playlist getPlaylist() {
		return Jamuz.getPlaylist(this.device.getIdPlaylist());
	}
    
	/**
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return this.name + " (" + this.getPlaylist().toString() + ")"; //NOI18N //NOI18N //NOI18N
	}

	/**
	 *
	 * @return
	 */
	public String getRootPath() {
		return rootPath;
	}
	
	/**
	 *
	 * @return
	 */
	public Device getDevice() {
		return device;
	}

	/**
	 *
	 * @param device
	 */
	public void setDevice(Device device) {
		this.device = device;
	}

	/**
	 *
	 * @return
	 */
	public StatSource getStatSource() {
		return statSource;
	}

	/**
	 *
	 * @param statSource
	 */
	public void setStatSource(StatSource statSource) {
		this.statSource = statSource;
	}
	
	/**
	 *
	 * @param enabled
	 */
	public void enable(boolean enabled) {
		this.enabled = enabled;
	}

	//FIXME !!! Use this for sync too
	/**
	 *
	 * @return
	 */
	public boolean isEnabled() {
		return enabled;
	}

	void setId(int id) {
		this.id=id;
	}	
}