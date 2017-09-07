/*
 * Copyright (C) 2012 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import jamuz.process.sync.Device;
import jamuz.process.merge.StatSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Options class
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public final class Machine {

	private ArrayList<Option> options; //TODO: Use an HashMap instead
	private HashMap <Integer, StatSource> statSources;
	private HashMap <Integer, Device> devices;
    private final String name;
    private String description;
    
	/**
	 * Creates Options for given machine name
     * @param machineName
	 */
	public Machine(String machineName) {
        this.name = machineName;
        this.description = "";
	}
	
	/**
	 * Read options and stat sources for given machine name
	 * @return
	 */
	public boolean read() {
		options= new ArrayList<>();
		statSources = new HashMap<>();
		devices = new HashMap<>();
        StringBuilder zText = new StringBuilder ();
		if(Jamuz.getDb().isMachine(this.name, zText)) {
            this.description=zText.toString();
			if(!Jamuz.getDb().getOptions(options, this.name)) {
				return false;
			}
			if(!Jamuz.getDb().getStatSources(statSources, this.name)) {
				return false;
			}
			return Jamuz.getDb().getDevices(devices, this.name);
		}
		else {
			return false;
		}
	}
	
	/**
	 * Get option by index
	 * @param index
	 * @return
	 */
	public Option getOption(int index){
		return options.get(index);
	}
	
	/**
	 *
	 * @param id
	 * @return
	 */
	public Option getOption(String id){    
		for (Option myOption : options) {
			if (myOption.getId().equals(id)) {
				return myOption;
			}
		}
		return null; 
	}
	
	/**
	 * Get option by value
	 * @param id
	 * @return
	 */
	public String getOptionValue(String id){    
		return getOption(id).getValue();
	}
	
	/**
	 * Return options list
	 * @return
	 */
	public ArrayList<Option> getOptions() {
		return options;
	}

	/**
	 * Return Stat sources list
	 * @return
	 */
	public Collection<StatSource> getStatSources() {
		return statSources.values();
	}
	
	/**
	 * Return linked stat source
	 * @param id
	 * @return
	 */
	public StatSource getStatSource(int id) {
		if(id>0) {
			return this.statSources.get(id);
		}
		else {
			return null;
		}
	}

	/**
	 * Return list of devices as Collection
	 * @return
	 */
	public Collection<Device> getDevices() {
		return devices.values();
	}
	
    /**
	 * Return requested device
	 * @param id
	 * @return
	 */
	public Device getDevice(int id) {
		if(id>0) {
			return this.devices.get(id);
		}
		else {
			return new Device();
		}
	}

	/**
	 * Return requested remote device id
	 * @param login
	 * @return
	 */
	public int getDeviceId(String login) {
		List<Device> candidates = 
				devices.values().stream()
				.filter(device -> device.getDestination().equals("remote://"+login))
				.collect(Collectors.toList());
		if(candidates.size()==1) {
			return candidates.get(0).getId();
		}
		return -1;
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
	 * @return
	 */
	public String getDescription() {
        return description;
    }
    
    
}
