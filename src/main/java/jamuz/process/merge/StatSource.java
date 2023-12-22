/*
 * Copyright (C) 2011 phramusca <phramusca@gmail.com>
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

import jamuz.database.DbInfo;
import jamuz.database.DbInfo.LibType;
import jamuz.Jamuz;
import jamuz.database.StatSourceAbstract;
import jamuz.process.sync.Device;
import jamuz.utils.DateTime;
import java.util.Date;
import java.util.Objects;

/**
 * Stat source class
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class StatSource {

	private boolean hidden;

	/**
	 * Used when retrieving StatSourceS from db for given machine
	 *
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
	 *
	 * @param machineName
	 */
	public StatSource(String machineName) {
		this(-1, "", "", "", "", "", machineName, true, 0, 1, "");  //NOI18N
	}

	private StatSource(int id, String name, String location, String user, String pwd, String rootPath,
			String machineName, boolean isSelected, int idDevice, int idStatement, String lastMergeDate) {
		this.id = id;
		this.idStatement = idStatement;
		this.machineName = machineName;
		this.isSelected = isSelected;
		this.idDevice = idDevice;
		this.lastMergeDate = DateTime.parseSqlUtc(lastMergeDate);

		switch (idStatement) {
			case 1: // Guayadeque 	(Linux)
				this.source = new StatSourceGuayadeque(new DbInfo(LibType.Sqlite, location, user, pwd), name, rootPath);
				break;
			case 2: // Kodi 	(Linux/Windows)
				this.source = new StatSourceKodi(new DbInfo(LibType.Sqlite, location, user, pwd), name, rootPath);
				break;
			case 3: // MediaMonkey (Windows)
				this.source = new StatSourceMediaMonkey(new DbInfo(LibType.Sqlite, location, user, pwd), name, rootPath);
				break;
			case 4: // Mixxx 	(Linux/Windows)
				this.source = new StatSourceMixxx(new DbInfo(LibType.Sqlite, location, user, pwd), name, rootPath);
				break;
			case 5: // MyTunes 	(Android)
				this.source = new StatSourceMyTunes(new DbInfo(LibType.Sqlite, location, user, pwd), name, rootPath);
				break;
			case 6: // JaMuz Remote 	(Android)
				this.source = new StatSourceJaMuzRemote(new DbInfo(LibType.Sqlite, location, user, pwd), name, rootPath);
				break;
			default:
				this.source = null;
				break;
		}
	}

	private int id;

	/**
	 *
	 * @return
	 */
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	 *
	 */
	public void updateLastMergeDate() {
		lastMergeDate = DateTime.parseSqlUtc(Jamuz.getDb().statSource().lock().updateLastMergeDate(this.getId()));
	}

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
	 *
	 * @return
	 */
	public Device getDevice() {
		return Jamuz.getMachine().getDevice(this.idDevice);
	}

	/**
	 * Type
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return this.source.getName();
	}

	boolean isHidden() {
		return hidden;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 41 * hash + (this.hidden ? 1 : 0);
		//hash = 41 * hash + this.id;
		hash = 41 * hash + this.idStatement;
		hash = 41 * hash + Objects.hashCode(this.source);
		hash = 41 * hash + Objects.hashCode(this.lastMergeDate);
		hash = 41 * hash + Objects.hashCode(this.machineName);
		hash = 41 * hash + (this.isSelected ? 1 : 0);
		hash = 41 * hash + this.idDevice;
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
		final StatSource other = (StatSource) obj;
		if (this.hidden != other.hidden) {
			return false;
		}
//		if (this.id != other.id) {
//			return false;
//		}
		if (this.idStatement != other.idStatement) {
			return false;
		}
		if (this.isSelected != other.isSelected) {
			return false;
		}
		if (this.idDevice != other.idDevice) {
			return false;
		}
		if (!Objects.equals(this.machineName, other.machineName)) {
			return false;
		}
		if (!Objects.equals(this.source, other.source)) {
			return false;
		}
		return Objects.equals(this.lastMergeDate, other.lastMergeDate);
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

}
