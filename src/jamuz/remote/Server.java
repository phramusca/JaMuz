/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jamuz.remote;

import jamuz.FileInfo;
import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.gui.PanelMain;
import jamuz.process.merge.PanelMerge;
import jamuz.utils.Popup;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
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
	private final int port;
	private HandleLogin handleLogin;
	private final ICallBackServer callback;
	private final TableModelRemote tableModel;
	private final Map<String, Client> clientMap;
	
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
            closeClient(client.getInfo().getId());
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
							case "requestFile":
								idFile = (int) (long) jsonObject.get("idFile");
								setStatus(login, "Sending file: "+idFile);
								sendFile(login, idFile);
								break;
							case "ackFileReception":
								boolean requestNextFile = (boolean) jsonObject.get("requestNextFile");
								idFile = (int) (long) jsonObject.get("idFile");
								//Send back ack to client
								if(requestNextFile) { //Not needed for now in this case
									FileInfoInt file = Jamuz.getDb().getFile(idFile);
									int idDevice = Jamuz.getMachine().getDeviceId(login);
									JSONObject obj = new JSONObject();
									obj.put("type", "insertDeviceFileAck");
									obj.put("requestNextFile", requestNextFile);
									setStatus(login, "Inserting file "+idFile+" "+file.getRelativeFullPath());
									if(idDevice>=0 && Jamuz.getDb().insertDeviceFile(idDevice, file)) {
										obj.put("status", "OK");
									} else {
										obj.put("status", "KO");
									}
									obj.put("file", file.toMap());
									setStatus(login, "Sending ack for file "+idFile+" "+file.getRelativeFullPath());
									send(login, obj);
								}
								break;
							case "FilesToMerge":
								ArrayList<FileInfo> newTracks = new ArrayList<>();
								JSONArray files = (JSONArray) jsonObject.get("files");
								for(int i=0; i<files.size(); i++) {
									JSONObject obj = (JSONObject) files.get(i);
									FileInfo file = new FileInfo(login, obj);
									newTracks.add(file);
								}
								PanelMerge.merge(login, newTracks);
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
		public void authenticated(Client client) {
			if(!clientMap.containsKey(client.getInfo().getId())) {
                clientMap.put(client.getInfo().getId(), client);				
            }
//            else {
//				//TODO: This can happen. why ?
//				//Until this is solved, considering not a problem
//                client.getDatabase("MSG_ERROR_ALREADY_CONNECTED");
//                closeClient(login);
//            }
			if(tableModel.contains(client.getInfo().getId())) {
				ClientInfo clientInfoModel = tableModel.getClient(client.getInfo().getId());
				if(client.getInfo().isRemoteConnected()) {
					clientInfoModel.setRemoteConnected(true);
					callback.connectedRemote(client.getInfo().getId());
				} 
				if(client.getInfo().isSyncConnected()) {
					clientInfoModel.setSyncConnected(true);
					clientInfoModel.setStatus("Connected");
					callback.connectedSync(client.getInfo().getId());
				}
			} else {
				tableModel.add(client.getInfo());
			}
			tableModel.fireTableDataChanged();
		}

		@Override
		public void disconnected(ClientInfo clientInfo) {
			if(clientMap.containsKey(clientInfo.getId())) {
				clientMap.get(clientInfo.getId()).close();
				clientMap.remove(clientInfo.getId());
			}
			if(tableModel.contains(clientInfo.getId())) {
				ClientInfo clientInfoModel = tableModel.getClient(clientInfo.getId());
				if(clientInfo.isRemoteConnected()) {
					clientInfoModel.setRemoteConnected(false);
					callback.disconnectedRemote(clientInfo.getId());
				} 
				if(clientInfo.isSyncConnected()) {
					clientInfoModel.setSyncConnected(false);
					clientInfoModel.setStatus("Disconnected");
					callback.disconnectedSync(clientInfo.getId());
				}
				tableModel.fireTableDataChanged();
			} 
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
		if(!sendFile(login, fileInfoInt)) {
			//FIXME SYNC Happens when file not found
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
	
	public boolean send(String login, Map jsonAsMap) {
		if(clientMap.containsKey(login)) {
			clientMap.get(login).send(jsonAsMap);
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
		try {
			tableModel.clear();
			Scanner sc = new Scanner(Jamuz.getFile("RemoteClients.txt", "data"));
			while(sc.hasNext()){
				String line = sc.nextLine().trim();
				String items[] = line.split("\t");
				String login = items[0].trim();
				String name = items[1].trim();
				tableModel.add(new ClientInfo(login, name));
			}
		} catch (FileNotFoundException ex) {
			Logger.getLogger(PanelRemote.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}




