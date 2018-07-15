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

import jamuz.FileInfo;
import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.gui.PanelMain;
import jamuz.process.merge.ICallBackMerge;
import jamuz.process.merge.ProcessMerge;
import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import jamuz.utils.Popup;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author raph
 */
public class Server {

	/**
	 *
	 */
	public static ServerSocket serverSocket = null;
	private int port;
	private HandleLogin handleLogin;
	private final ICallBackServer callback;
	
	//FIXME ****** Verify that client info is well synchronized b/w these 2 below:
	private final TableModelRemote tableModel; //contains clients info from database
	private final Map<String, Client> clientMap; //contains connected clients
	
	/**
	 *
	 * @param port
	 * @param callback
	 */
	public Server(int port, ICallBackServer callback) {
		this.port = port;
		clientMap = new HashMap();
		tableModel = new TableModelRemote();
		tableModel.setColumnNames();
		this.callback = callback;
	}
	
	/**
	 *
	 * @return
	 */
	public boolean connect() {
		try {
            //TODO: Secure connexion
            //http://www.java2s.com/Code/Java/Network-Protocol/SecureCommunicationwithJSSE.htm
            
			//Create the server socket
			serverSocket = new ServerSocket(port);

			//Start login handling thread
			handleLogin = new HandleLogin(serverSocket);
			handleLogin.start();
			
			return true;
		} catch (IOException ex) {
			Popup.error("Cannot start JaMuz Remote Server", ex);
			return false;
		}
	}

	public void setPort(int port) {
		this.port = port;
	}

	/**
	 *
	 */
	public void close() {
		try {
			handleLogin.abort();
//			handleLogin.join(); //cannot join as serverSocket.accept(); blocks
			serverSocket.close();
		} catch (IOException ex) {
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	void closeClients() {
		for(Client client : clientMap.values()) {
            closeClient(client.getInfo().getLogin());
        }
	}

	class HandleLogin extends ProcessAbstract {

		public HandleLogin(ServerSocket ss){
			super("Thread.Server.HandleLogin");
		}

		@Override
		public void run() {
			try {
				while(true){
					checkAbort();
					//TODO: Use a security manager ...
					Socket socket = serverSocket.accept(); //Blocks until connection made
					checkAbort();
                    CallBackReception callBackReceptionLocal = new CallBackReception();
					Client client = new Client(socket, callBackReceptionLocal);
					checkAbort();
					client.login();
				}
			} catch (IOException | InterruptedException ex) {
//				Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
    class CallBackReception implements ICallBackReception {
        @Override
        public void received(String login, String msg) {
            if(clientMap.containsKey(login)) {
				if(msg.startsWith("JSON_")) {
					String json = msg.substring(5);
					JSONObject jsonObject;
					try {
						jsonObject = (JSONObject) new JSONParser().parse(json);
						String type = (String) jsonObject.get("type");
						int idFile;
						switch(type) {
							case "requestTags":
								setStatus(login, "Sending tags");
								sendTags(login);
								break;
							case "requestGenres":
								setStatus(login, "Sending genres");
								sendGenres(login);
								break;
							case "requestNewFiles":
								sendFilesToGet(login);
								break;
							case "requestFile":
								idFile = (int) (long) jsonObject.get("idFile");
								sendFile(login, idFile);
								break;
							case "ackFileSReception":
								setStatus(login, "Received list of files to ack");
								Device device = Jamuz.getMachine().getDevice(login);
								if(device!=null) {
									JSONArray list = new JSONArray();
									JSONArray idFiles = (JSONArray) jsonObject.get("idFiles");
									FileInfoInt file;
									ArrayList<FileInfoInt> toInsertInDeviceFiles
											 = new ArrayList<>();
									for(int i=0; i<idFiles.size(); i++) {
										idFile = (int) (long) idFiles.get(i);
										file = Jamuz.getDb().getFile(idFile);
										toInsertInDeviceFiles.add(file);
									}
									setStatus(login, "Inserting into device file list");
									ArrayList<FileInfoInt> inserted= Jamuz.getDb().
											insertDeviceFiles(toInsertInDeviceFiles, device.getId());
									//FIXME ***** StatSource will not be found here as not the same machine
									StatSource source = Jamuz.getMachine().getStatSource(login);
									if(source!=null && Jamuz.getDb()
											.setPreviousPlayCounter(inserted, source.getId())) {
										for (FileInfoInt ins : inserted) {
											list.add(ins.toMap());
										}
										
									}//FIXME: else { Manage potential error => Send STOP to remote with erro msg }
									setStatus(login, "Sending list of ack. files");
									JSONObject obj = new JSONObject();
									obj.put("type", "insertDeviceFileSAck");
									obj.put("filesAcked", list);
									send(login, obj);
								} else {
									setStatus(login, "Should not happen (idDevice not found) or you're stuck");
								}
								break;
							case "FilesToMerge":
								setStatus(login, "Received files to merge");
								ArrayList<FileInfo> newTracks = new ArrayList<>();
								JSONArray files = (JSONArray) jsonObject.get("files");
								for(int i=0; i<files.size(); i++) {
									JSONObject obj = (JSONObject) files.get(i);
									FileInfo file = new FileInfo(login, obj);
									newTracks.add(file);
								}
								LinkedHashMap <Integer, StatSource> statSources = new LinkedHashMap<>();
								Jamuz.getDb().getStatSources(statSources, login);
								List<StatSource> sources = new ArrayList();
								if(statSources.values().iterator().hasNext()) {
									sources.add(statSources.values().iterator().next());
									setStatus(login, "Starting merge");
									new ProcessMerge("Thread.Server.ProcessMerge."+login, 
										sources, false, false, newTracks, 
											tableModel.getClient(login).getProgressBar(), 
											new CallBackMerge(login))
									.start();
								} else {
									setStatus(login, "No stat source found !");
								}
								break;
						}
					} catch (ParseException ex) {
						Logger.getLogger(PanelMain.class.getName()).log(Level.SEVERE, null, ex);
					}				
				} else {
					callback.received(login, msg);
				}
            }
        }
		
		@Override
		public void connected(Client client) {
			if(!tableModel.contains(client.getInfo().getLogin())) {
				//Creates a new machine, device and statSource
				//and store the client
				StringBuilder zText = new StringBuilder ();
				if(Jamuz.getDb().isMachine(client.getInfo().getLogin(), zText)) {
					Device device = new Device(-1, 
							client.getInfo().getLogin(), 
							"source", "remote://"+client.getInfo().getLogin(), 
							client.getInfo().getIdPlaylist(), 
							client.getInfo().getLogin());
					if(Jamuz.getDb().setDevice(device)) {
						LinkedHashMap <Integer, Device> devices = new LinkedHashMap<>();
						Jamuz.getDb().getDevices(devices, client.getInfo().getLogin());
						int idDevice = devices.values().iterator().next().getId();
						client.getInfo().setIdDevice(idDevice);
						StatSource statSource = new StatSource(
							-1, client.getInfo().getLogin(), 6, 
							"remote://"+client.getInfo().getLogin(), "MySqlUser", "MySqlPwd", 
							client.getInfo().getRootPath(), 
							client.getInfo().getLogin(), 
							idDevice, false, "");
						if(Jamuz.getDb().setStatSource(statSource)) {
							LinkedHashMap <Integer, StatSource> statSources = new LinkedHashMap<>();
							Jamuz.getDb().getStatSources(statSources, client.getInfo().getLogin());
							int idStatSource = statSources.values().iterator().next().getId();
							client.getInfo().setIdStatSource(idStatSource);
							if(Jamuz.getDb().setClientInfo(client.getInfo())) {
								ClientInfo clientInfo = Jamuz.getDb().getClient(client.getInfo().getLogin());
								client.getInfo().setId(clientInfo.getId());
								tableModel.add(client.getInfo());
							}
						}
					}
				}
			}
			if(!clientMap.containsKey(client.getInfo().getLogin())
					&& tableModel.contains(client.getInfo().getLogin())
						&& tableModel.getClient(client.getInfo().getLogin()).isEnabled()) {
				ClientInfo clientInfoModel = tableModel.getClient(client.getInfo().getLogin());
				if(client.getInfo().isRemoteConnected()) {
					clientInfoModel.setRemoteConnected(true);
					callback.connectedRemote(client.getInfo().getLogin());
				} 
				if(client.getInfo().isSyncConnected()) {
					clientInfoModel.setSyncConnected(true);
					clientInfoModel.setStatus("Connected");
				}
                clientMap.put(client.getInfo().getLogin(), client);
				client.send("MSG_CONNECTED");
            } else {
				//FIXME: ***** Make this disconnect client AND NOT RECONNECT
				client.send("MSG_ERROR_CONNECTION"); 
			}
			tableModel.fireTableDataChanged();
		}

		@Override
		public void disconnected(ClientInfo clientInfo) {
			if(clientMap.containsKey(clientInfo.getLogin())) {
				clientMap.get(clientInfo.getLogin()).close();
				clientMap.remove(clientInfo.getLogin());
			}
			if(tableModel.contains(clientInfo.getLogin())) {
				ClientInfo clientInfoModel = tableModel.getClient(clientInfo.getLogin());
				if(clientInfo.isRemoteConnected()) {
					clientInfoModel.setRemoteConnected(false);
				} 
				if(clientInfo.isSyncConnected()) {
					clientInfoModel.setSyncConnected(false);
					clientInfoModel.setStatus("Disconnected");
				}
				tableModel.fireTableDataChanged();
			} 
		}
    }
	
	class CallBackMerge implements ICallBackMerge {
		private final String login;

		public CallBackMerge(String login) {
			this.login = login;
		}
		
		@Override
		public void completed(ArrayList<FileInfo> errorList, 
				ArrayList<FileInfo> completedList, String popupMsg, 
				String mergeReport) {
            Jamuz.getLogger().info(popupMsg);
			setStatus(login, popupMsg);
            //TODO: Read options again (only to read lastMergeDate !! Still needed ? here and elsewhere)
            //TODO MERGE Use listeners !!
            PanelMain.setOptions(); 
		}

		@Override
		public void refresh() {
			tableModel.fireTableDataChanged();
		}
	}
	
	private void setStatus(String login, String status) {
		if(tableModel.contains(login)) {
			ClientInfo clientInfo = tableModel.getClient(login);
			clientInfo.setStatus(status);
			tableModel.fireTableDataChanged();
		}
	}
	
	private Map<String, Client> getRemoteClients() {
		return clientMap.entrySet().stream()
			.filter((client) -> client.getValue().getInfo().isRemoteConnected())
			.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
	}
	
	private Map<String, Client> getDataClients() {
		return clientMap.entrySet().stream()
			.filter((client) -> client.getValue().getInfo().isSyncConnected())
			.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
	}
	
	/**
	 *
	 * @param login
	 * @param displayedFile
	 * @param maxWidth
	 */
    public void sendCover(String login, FileInfoInt displayedFile, int maxWidth) {
		if(clientMap.containsKey(login)) {
			clientMap.get(login).sendCover(displayedFile, maxWidth);
		}
		
	}
	
	public void sendFile(String login, int id) {
		FileInfoInt fileInfoInt = Jamuz.getDb().getFile(id);
		setStatus(login, "Sending file: "+fileInfoInt.getRelativeFullPath());
		if(!sendFile(login, fileInfoInt)) {
			//FIXME LOW SYNC Happens (still ?) when file not found
			// Need to mark as deleted in db 
			// AND somehow remove it from filesToKeep
			//and filesToGet in remote
			Popup.error("Cannot send missing file \""
					+fileInfoInt.getFullPath().getAbsolutePath()+"\"");
		}
	}
	
	private boolean sendFile(String login, FileInfoInt fileInfoInt) {
		if(clientMap.containsKey(login)) {
			return clientMap.get(login).sendFile(fileInfoInt);
		}
		return true;
	}
    
	private void sendFilesToGet(String login) {
		File file = Jamuz.getFile(login, "data", "devices");
		if(file.exists()) {
			try {
				setStatus(login, "Sending new list of files to retrieve");
				String json = new String(Files.readAllBytes(file.toPath()));
				send(login, "JSON_"+json);
				file.delete();
			} catch (IOException ex) {
				Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else {
			setStatus(login, "Sync will start soon");
			JSONObject obj = new JSONObject();
			obj.put("type", "StartSync");
			send(login, obj);
		}
	}

	private void sendGenres(String login) {
		JSONArray list = new JSONArray();
		for(String genre : Jamuz.getGenres()) {
			list.add(genre);
		}
		JSONObject obj = new JSONObject();
		obj.put("type", "genres");
		obj.put("genres", list);
		send(login, obj);
	}

	private void sendTags(String login) {
		JSONArray list = new JSONArray();
		for(String tag : Jamuz.getTags()) {
			list.add(tag);
		}
		JSONObject obj = new JSONObject();
		obj.put("type", "tags");
		obj.put("tags", list);
		send(login, obj);
	}
	
	/**
     * Sends a message to all clients
	 * @param jsonAsMap
	 * @param isRemote
     */
    public void send(Map jsonAsMap, boolean isRemote) {
		Map<String, Client> clientsToSend = isRemote?
				getRemoteClients():getDataClients();
        for(Client client : clientsToSend.values()) {
            client.send(jsonAsMap);
        }
	}
	
	public boolean isConnected(String login) {
		return clientMap.containsKey(login);
	}
	
	public boolean send(String login, JSONObject obj) {
		if(clientMap.containsKey(login)) {
			clientMap.get(login).send(obj);
			return true;
		}
		return false;
	}
	
	private boolean send(String login, String msg) {
		if(clientMap.containsKey(login)) {
			clientMap.get(login).send(msg);
			return true;
		}
		return false;
	}
	
	/**
	 *
	 * @param login
	 */
	public void closeClient(String login) {
        if(clientMap.containsKey(login)) {
            clientMap.get(login).close();
            clientMap.remove(login);
        }
        else {
            callback.received(login, " has been disconnected.");
        }
	}
	
	/**
	 *
	 * @return
	 */
	public Collection<String> getClients() {
        return clientMap.keySet();
    }

	public TableModelRemote getTableModel() {
		return tableModel;
	}
	
	public void fillClients() {
		tableModel.clear();
		LinkedHashMap<Integer, ClientInfo> clients = new LinkedHashMap<>();
		Jamuz.getDb().getClients(clients);
		for(ClientInfo clientInfo : clients.values()) {
			tableModel.add(clientInfo);
		}
	}
}