/*
 * Copyright (C) 2014 phramusca <phramusca@gmail.com>
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

package jamuz.process.sync;

import jamuz.Jamuz;
import jamuz.Playlist;
import jamuz.utils.Inter;
import java.util.Objects;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class Device {

	private int id;
	private boolean hidden;
	private String name;
	private String machineName;
	private int idPlaylist;
	private String source;
	private String destination;
	
	/**
	 * Creates a new Device from database
	 * @param id
	 * @param name
	 * @param source
	 * @param destination
	 * @param idPlaylist
	 * @param machineName
	 * @param hidden
	 */
	public Device(int id, String name, String source, String destination, 
			int idPlaylist, String machineName, boolean hidden) {
		this.id = id;
		this.name = name;
		this.source = source;
		this.destination = destination;
		this.idPlaylist = idPlaylist;
		this.machineName = machineName;
		this.hidden = hidden;
	}
	
	/**
	 * Creates an empty device
	 * @param machineName
	 */
	public Device(String machineName) {
		this(-1, "", "", "", -1, machineName, false);  //NOI18N
	}
	
    /**
	 * This is the default "None" device
	 */
	public Device() {
        this(0, Inter.get("Label.None"), "", "", 0, "", false); //NOI18N
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
	public String getSource() {
        return source;
    }

	/**
	 *
	 * @param source
	 */
	public void setSource(String source) {
        this.source = source;
    }
 
	/**
	 *
	 * @return
	 */
	public String getDestination() {
        return destination;
    }

	/**
	 *
	 * @param destination
	 */
	public void setDestination(String destination) {
        this.destination = destination;
    }

	/**
	 *
	 * @return
	 */
	public int getIdPlaylist() {
        return idPlaylist;
    }

	/**
	 *
	 * @param idPlaylist
	 */
	public void setIdPlaylist(int idPlaylist) {
        this.idPlaylist = idPlaylist;
    }
    
	/**
	 *
	 * @return
	 */
	public String getMachineName() {
        return machineName;
    }

	/**
	 * Return device's associated playlist
	 * @return
	 */
	public Playlist getPlaylist() {
		return Jamuz.getPlaylist(this.idPlaylist);
	}
    
	/**
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return this.name + " (" + this.getPlaylist().toString() + ")"; //NOI18N //NOI18N //NOI18N
	}

	public boolean isHidden() {
		return hidden;
	}

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.id;
        hash = 37 * hash + (this.hidden ? 1 : 0);
        hash = 37 * hash + Objects.hashCode(this.name);
        hash = 37 * hash + Objects.hashCode(this.machineName);
        hash = 37 * hash + this.idPlaylist;
        hash = 37 * hash + Objects.hashCode(this.source);
        hash = 37 * hash + Objects.hashCode(this.destination);
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
        final Device other = (Device) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.hidden != other.hidden) {
            return false;
        }
        if (this.idPlaylist != other.idPlaylist) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.machineName, other.machineName)) {
            return false;
        }
        if (!Objects.equals(this.source, other.source)) {
            return false;
        }
        return Objects.equals(this.destination, other.destination);
    }
    
    
}
