/*
 * Copyright (C) 2017 phramusca <phramusca@gmail.com>
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
import java.util.Objects;

/**
 *
 * @author phramusca <phramusca@gmail.com>
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
	 * @param enabled
	 */
	public ClientInfo(String login, String pwd, String rootPath, String name, boolean enabled) {	
		isConnected=false;
		this.progressBar = new ProgressBar();
		this.login = login;
		this.pwd = pwd;
		this.id=-1;
		this.name = "";
		this.enabled = false;
		this.rootPath = rootPath;
		this.name = name;
		this.enabled = enabled;
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
		this(login, pwd, statSource.getSource().getRootPath(), name, enabled);
		this.id = id;
		this.device = device;
		this.statSource = statSource;
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
	
    
    //FIXME ! Set this on when sse is connected, for 
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.id;
        hash = 83 * hash + Objects.hashCode(this.login);
        hash = 83 * hash + Objects.hashCode(this.pwd);
        hash = 83 * hash + Objects.hashCode(this.name);
        hash = 83 * hash + Objects.hashCode(this.device);
        hash = 83 * hash + Objects.hashCode(this.statSource);
        hash = 83 * hash + (this.enabled ? 1 : 0);
        hash = 83 * hash + Objects.hashCode(this.status);
        hash = 83 * hash + Objects.hashCode(this.rootPath);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClientInfo other = (ClientInfo) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.enabled != other.enabled) {
            return false;
        }
        if (!Objects.equals(this.login, other.login)) {
            return false;
        }
        if (!Objects.equals(this.pwd, other.pwd)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.rootPath, other.rootPath)) {
            return false;
        }
        if (!Objects.equals(this.device, other.device)) {
            return false;
        }
        return Objects.equals(this.statSource, other.statSource);
    }
    
    
}