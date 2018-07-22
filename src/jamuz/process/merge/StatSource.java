/*
 * Copyright (C) 2011 phramusca ( https://github.com/phramusca/JaMuz/ )
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

package jamuz.process.merge;

import jamuz.StatSourceAbstract;
import jamuz.process.sync.Device;
import jamuz.Jamuz;
import jamuz.DbInfo;
import jamuz.DbInfo.LibType;
import java.util.Date;
import jamuz.utils.DateTime;

/**
 * Stat source class
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class StatSource {

	private boolean hidden;

	/**
	 * Used when retrieving StatSourceS from db for given machine 
	 * @param id 
	 * @param name 
	 * @param idStatement 
	 * @param location
	 * @param user
	 * @param pwd
	 * @param rootPath
	 * @param machineName  
	 * @param idDevice
	 * @param isSelected  
     * @param lastMergeDate  
	 * @param hidden  
	 */
	public StatSource(int id, String name, int idStatement, String location, 
			String user, String pwd, String rootPath, String machineName, 
			int idDevice, boolean isSelected, String lastMergeDate, boolean hidden) {
		
		this(id, name, location, user, pwd, rootPath, machineName, 
				isSelected, idDevice, idStatement, lastMergeDate);
		this.hidden = hidden;
	}
    
    /**
	 * Used when creating a new empty StatSource
	 * @param machineName
	 */
	public StatSource(String machineName) {
		this(-1, "", "", "", "", "", machineName, true, 0, 1, "");  //NOI18N
	}

	/**
	 *
	 */
	public void updateLastMergeDate() {
        //TODO: No need to update lastMerge date during merge (really ?), do them all at the end of 
        //merge process and only if not a simulation and merge OK
        //Then, read options again 
        //(better: Generate String date = DateUtil.getFormattedCurrentSql and update
        //both database (statource table for merged ids) and StatSource instances with this value)
        Jamuz.getDb().updateLastMergeDate(this.getId());
    }
    
	private StatSource(int id, String name, String location, String user, String pwd, String rootPath, 
			String machineName, boolean isSelected, int idDevice, int idStatement, String lastMergeDate) {
		this.id=id;
        this.idStatement = idStatement;
        this.machineName = machineName;
		this.isSelected = isSelected;
        this.idDevice = idDevice;
        this.lastMergeDate=DateTime.parseSqlUtc(lastMergeDate);
        
        switch (idStatement) {
            case 1: // Guayadeque 	(Linux)
                this.source = new StatSourceGuayadeque(new DbInfo(LibType.Sqlite, location, user, pwd, false), name, rootPath);
                break;
            case 2: // Kodi 	(Linux/Windows)
                this.source = new StatSourceKodi(new DbInfo(LibType.Sqlite, location, user, pwd, false), name, rootPath); 
                break;
            case 3: // MediaMonkey (Windows)
                this.source = new StatSourceMediaMonkey(new DbInfo(LibType.Sqlite, location, user, pwd, false), name, rootPath); 
                break;
            case 4: // Mixxx 	(Linux/Windows)
                this.source = new StatSourceMixxx(new DbInfo(LibType.Sqlite, location, user, pwd, false), name, rootPath); 
                break;
            case 5: // MyTunes 	(Android)
                this.source = new StatSourceMyTunes(new DbInfo(LibType.Sqlite, location, user, pwd, false), name, rootPath); 
                break;
			case 6: // JaMuz Remote 	(Android)
                this.source = new StatSourceJaMuzRemote(new DbInfo(LibType.Sqlite, location, user, pwd, true), name, rootPath); 
                break;
            default:
                this.source = null;
                break;
        }
	}

	private final int id;

	/**
	 *
	 * @return
	 */
	public int getId() {
        return id;
    }
    private int idStatement; //TODO: Replace by an enum: refer to checkedFlag usage
    
	/**
	 *
	 * @return
	 */
	public int getIdStatement() {
        return idStatement;
    }

	/**
	 *
	 * @param idStatement
	 */
	public void setIdStatement(int idStatement) {
        this.idStatement = idStatement;
    }
    
    private StatSourceAbstract source;

	/**
	 *
	 * @return
	 */
	public StatSourceAbstract getSource() {
        return source;
    }
    
    /**
     * Last Merge Date
     */
    protected Date lastMergeDate;

	/**
	 * Machine name
	 */
	private final String machineName;

	/**
	 *
	 * @return
	 */
	public String getMachineName() {
        return machineName;
    }
    
	/**
	 * Is selected by default in GUI ?
	 */
	private boolean isSelected;

	/**
	 *
	 * @return
	 */
	public boolean isIsSelected() {
        return isSelected;
    }

	/**
	 *
	 * @param isSelected
	 */
	public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
	
	/**
	 * Device ID
	 */
	private int idDevice;

	/**
	 *
	 * @return
	 */
	public int getIdDevice() {
        return idDevice;
    }

	/**
	 *
	 * @param idDevice
	 */
	public void setIdDevice(int idDevice) {
        this.idDevice = idDevice;
    }
	
	/**
	 * Gets device
	 * @return
	 */
	public Device getDevice() {
		return Jamuz.getMachine().getDevice(this.idDevice);
	}

	/**
	 * Type
     * @return 
	 */
	@Override
	public String toString() {
		return this.source.getName();
	}

	boolean isHidden() {
		return hidden;
	}

}
