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
package jamuz.remote;

import jamuz.gui.swing.TableModelGeneric;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */

public class TableModelRemote extends TableModelGeneric {

    private final Map<String, ClientInfo> clients;
    
    /**
	 * Create the table model
	 */
	public TableModelRemote() {
        clients = new LinkedHashMap<>();
	}
	
	/**
	 *
	 */
	public void setColumnNames() {
		//FIXME Z SERVER Progress & status
		// -Inter
		// - split "Status" into "Datetime" and "Status"
		// -use "Status" & "Progress" columns accordingly
        this.setColumnNames(new String [] {
            "Remote",
			"Name",
			"Status",
			"Progress"
        });
		this.fireTableStructureChanged();
	}

	/**
	 *
	 * @param rowIndex
	 * @param columnIndex
	 * @return
	 */
	@Override
    public Object getValueAt(int rowIndex, int columnIndex) {
		ClientInfo clientInfo = (new ArrayList<>(clients.values())).get(rowIndex);
        switch (columnIndex) {
            case 0: return clientInfo.isConnected();
			case 1: return !clientInfo.isEnabled()?"<html><s>"+clientInfo.getName()+"</s></html>":clientInfo.getName();
			case 2: return clientInfo.getStatus();
			case 3: return clientInfo.getProgressBar();
		}
        return null;
    }
	
	/**
	 *
	 * @param row
	 * @param col
	 * @return
	 */
	@Override
    public boolean isCellEditable(int row, int col){
		return false;
    }
    
	/**
	 *
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean isCellEnabled(int row, int col) {
        return true;
    }
    
	/**
	 *
	 * @return
	 */
	public Collection<String> getClients() {
        return clients.keySet();
    }

	/**
	 *
	 * @param login
	 * @return
	 */
	public ClientInfo getClient(String login) {
		return clients.get(login);
	}
	
	/**
     * get list of files
     * @param index
     * @return
     */
    public ClientInfo getClient(int index) {
        return (new ArrayList<>(clients.values())).get(index);
    }

	/**
	 *
	 * @return
	 */
	@Override
    public int getRowCount() {
        return clients.size();
    }
	
    /**
	* Returns given column's data class
    * @param col
     * @return 
    */
    @Override
    public Class getColumnClass(int col){
        //Note: since all data on a given column are all the same
		//we return data class of given column first row
        return this.getValueAt(0, col).getClass();
    }

	/**
     * Clear the table
     */
    public void clear() {
        if(clients.size()>0) {
            clients.clear();
			this.fireTableDataChanged();
//            this.fireIntervalRemoved(this, 0, lastIndex);
        }
    }
	
	/**
     * Add a client to the list
     *
     * @param client
     */
    public void add(ClientInfo client) {
        clients.put(client.getLogin(), client);
		this.fireTableDataChanged();
//		this.fireIntervalAdded(this, clients.size()-1, clients.size()-1);
    }

	/**
	 *
	 * @param login
	 */
	public void removeClient(String login) {
		if(clients.containsKey(login)) {
			clients.remove(login);
			this.fireTableDataChanged();
//			this.fireIntervalRemoved(this, 0, clients.size());
		}
	}

	/**
	 *
	 * @param login
	 * @return
	 */
	public boolean contains(String login) {
		return clients.containsKey(login);
	}
}
