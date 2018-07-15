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
package jamuz.remote;

import jamuz.gui.swing.TableModelGeneric;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */

public class TableModelRemote extends TableModelGeneric {

    private final Map<String, ClientInfo> clients;
    
    /**
	 * Create the table model
	 */
	public TableModelRemote() {
        clients = new LinkedHashMap<>();
	}
	
	public void setColumnNames() {
        this.setColumnNames(new String [] {
            "Remote",
			"Sync",
			"Name",
			"Sync Status",
			"Merge Status"
        });
		this.fireTableStructureChanged();
	}

	@Override
    public Object getValueAt(int rowIndex, int columnIndex) {
		ClientInfo clientInfo = (new ArrayList<>(clients.values())).get(rowIndex);
        switch (columnIndex) {
            case 0: return clientInfo.isRemoteConnected();
			case 1: return clientInfo.isSyncConnected();
			case 2: return clientInfo.getName();
			case 3: return clientInfo.getStatus();
			case 4: return clientInfo.getProgressBar();
		}
        return null;
    }
	
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

	public void removeClient(String id) {
		if(clients.containsKey(id)) {
			clients.remove(id);
			this.fireTableDataChanged();
//			this.fireIntervalRemoved(this, 0, clients.size());
		}
	}

	public boolean contains(String id) {
		return clients.containsKey(id);
	}
}
