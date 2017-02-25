/*
 * Copyright (C) 2014 phramusca ( https://github.com/phramusca/JaMuz/ )
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

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Device {
	/**
	 * Device ID
	 */
	private int id;

	/**
	 *
	 * @return
	 */
	public int getId() {
        return id;
    }
    
	/**
	 * Device name
	 */
	private String name;

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
	 * Device source path
	 */
	private String source;

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
	 * Device destination path
	 */
	private String destination;

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
	 * Linked playlist ID
	 */
	private int idPlaylist;

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
	 * Machine name
	 */
	private String machineName;

	/**
	 *
	 * @return
	 */
	public String getMachineName() {
        return machineName;
    }

	/**
	 * Creates a new Device from database
	 * @param id
	 * @param name
	 * @param source
	 * @param destination
	 * @param idPlaylist
	 * @param machineName
	 */
	public Device(int id, String name, String source, String destination, int idPlaylist, String machineName) {
		this.id = id;
		this.name = name;
		this.source = source;
		this.destination = destination;
		this.idPlaylist = idPlaylist;
		this.machineName = machineName;
	}
	
	/**
	 * Creates an empty device
	 * @param machineName
	 */
	public Device(String machineName) {
		this(-1, "", "", "", -1, machineName);  //NOI18N
	}
	
    /**
	 * This is the default "None" device
	 */
	public Device() {
        this(0, Inter.get("Label.None"), "", "", 0, ""); //NOI18N
    }
    
	/**
	 * Return device's associated playlist
	 * @return
	 */
	public Playlist getPlaylist() {
		return Jamuz.getPlaylist(this.idPlaylist);
	}
    
    @Override
	public String toString() {
		return this.name + " (" + this.getPlaylist().toString() + ")"; //NOI18N //NOI18N //NOI18N
	}
}
