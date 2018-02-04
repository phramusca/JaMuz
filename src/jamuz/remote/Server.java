/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jamuz.remote;

import jamuz.FileInfoInt;
import jamuz.utils.Popup;
import java.io.*;
import java.net.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.json.simple.JSONObject;

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
	private final Map<String, Client> clientMap;
	private final ICallBackServer callback;
	private static final Object LOCK_REMOTE = new Object();

	/**
	 *
	 * @param port
	 * @param callback
	 */
	public Server(int port, ICallBackServer callback) {
		this.port = port;
		clientMap = new HashMap();
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
				if(msg.equals("SENDING_DB")) {
					synchronized(LOCK_REMOTE) {
						clientMap.get(login).getDatabase();
						Logger.getLogger(Server.class.getName()).log(Level.INFO, "lockRemote.notify()");
						LOCK_REMOTE.notify();
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
			if(client.getInfo().isRemoteConnected()) {
				callback.connectedRemote(client.getInfo().getId());
			} 
			if(client.getInfo().isSyncConnected()) {
				callback.connectedSync(client.getInfo().getId());
			}
		}

		@Override
		public void disconnected(ClientInfo clientInfo) {
			if(clientMap.containsKey(clientInfo.getId())) {
				clientMap.get(clientInfo.getId()).close();
				clientMap.remove(clientInfo.getId());
			}
			if(clientInfo.isRemoteConnected()) {
					callback.disconnectedRemote(clientInfo.getId());
				}
				if(clientInfo.isSyncConnected()) {
					callback.disconnectedSync(clientInfo.getId());
				}
		}
    }

	private Map<String, Client> getRemoteClients() {
		return clientMap.entrySet().stream()
			.filter((client) -> !client.getValue().getInfo().isRemoteConnected())
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
	
	public boolean sendFile(String login, FileInfoInt fileInfoInt) {
		if(clientMap.containsKey(login)) {
			return clientMap.get(login).sendFile(fileInfoInt);
		}
		return true;
	}
    
	/**
	 *
	 * @param login
	 * @param path
	 * @return 
	 */
	public boolean getDatabase(String login, String path) {
		if(clientMap.containsKey(login)) {
			synchronized(LOCK_REMOTE) {
				try {
					clientMap.get(login).setPath(path);
					JSONObject obj = new JSONObject();
					obj.put("type", "SEND_DB");
					clientMap.get(login).send( obj);
					Logger.getLogger(Server.class.getName()).log(Level.INFO, "lockRemote.wait()");
					LOCK_REMOTE.wait(10 * 1000); // 10s
					Logger.getLogger(Server.class.getName()).log(Level.INFO, "lockRemote released");
					return true;
				} catch (InterruptedException ex) {
					Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 *
	 * @param login
	 * @param path
	 * @return 
	 */
	public boolean sendDatabase(String login, String path) {
		if(clientMap.containsKey(login)) {
			return clientMap.get(login).sendDatabase(path);
		}
		return false;
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
}




