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
package jamuz.gui;

import jamuz.remote.Client;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractListModel;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ListModelRemoteClients extends AbstractListModel {

	private final Map<String, Client> clients;

    /**
     * Create a new queue
     */
    public ListModelRemoteClients() {
        clients = new HashMap<>();
    }

    /**
     * Add a client to the list
     *
     * @param client
     */
    public void add(Client client) {
        clients.put(client.getId(), client);
		this.fireIntervalAdded(this, clients.size()-1, clients.size()-1);
    }

    /**
     * Clear queue
     */
    public void clear() {
        if(clients.size()>0) {
            int lastIndex = clients.size() - 1;
            clients.clear();
            this.fireIntervalRemoved(this, 0, lastIndex);
        }
    }

	/**
	 *
	 * @return
	 */
	public Collection<String> getClients() {
        return clients.keySet();
    }
	
	public Client getClient(String id) {
		return clients.get(id);
	}
	
	public void removeClient(String id) {
		if(clients.containsKey(id)) {
			clients.remove(id);
			this.fireIntervalRemoved(this, 0, clients.size());
		}
	}
	
	public boolean contains(String id) {
		return clients.containsKey(id);
	}

    @Override
    public Object getElementAt(int index) {
        return (new ArrayList(clients.values())).get(index);
    }
	
	@Override
    public int getSize() {
        return clients.size();
    }

}
