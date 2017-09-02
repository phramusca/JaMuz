/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jamuz.remote;

import jamuz.FileInfoInt;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
	private final Map<String, ServerClient> clients;
	private final ICallBackReception callback;
	private static final Object LOCK_REMOTE = new Object();

	/**
	 *
	 * @param port
	 * @param callback
	 */
	public Server(int port, ICallBackReception callback) {
		this.port = port;
		clients = new HashMap();
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
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
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
					ServerClient client = new ServerClient(socket, callBackReceptionLocal);
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
            if(clients.containsKey(login)) {
				if(msg.equals("SENDING_DB")) {
					synchronized(LOCK_REMOTE) {
						clients.get(login).getDatabase();
						Logger.getLogger(Server.class.getName()).log(Level.INFO, "lockRemote.notify()");
						LOCK_REMOTE.notify();
					}
				} else {
					callback.received(login, msg);
				}
            }
        }
		
		@Override
		public void authenticated(Client login, ServerClient client) {
			if(!clients.containsKey(login.getId())) {
                clients.put(login.getId(), client);
                callback.authenticated(login, null);
            }
//            else {
//				//TODO: This can happen. why ?
//				//Until this is solved, considering not a problem
//                client.getDatabase("MSG_ERROR_ALREADY_CONNECTED");
//                closeClient(login);
//            }
		}

		@Override
		public void disconnected(String login) {
			if(clients.containsKey(login)) {
				clients.get(login).close();
				clients.remove(login);
			}
			callback.disconnected(login);
		}
    }

	private Map<String, ServerClient> getRemoteClients() {
		return clients.entrySet().stream()
			.filter((client) -> !client.getValue().getLogin().endsWith("-data"))
			.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
	}
	
	private Map<String, ServerClient> getDataClients() {
		return clients.entrySet().stream()
			.filter((client) -> client.getValue().getLogin().endsWith("-data"))
			.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
	}
	
	/**
	 *
	 * @param login
	 * @param displayedFile
	 * @param maxWidth
	 */
    public void sendCover(String login, FileInfoInt displayedFile, int maxWidth) {
		if(clients.containsKey(login)) {
			clients.get(login).sendCover(displayedFile, maxWidth);
		}
		
	}
	
	public boolean sendFile(String login, FileInfoInt fileInfoInt) {
		if(clients.containsKey(login)) {
			return clients.get(login).sendFile(fileInfoInt);
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
		if(clients.containsKey(login)) {
			synchronized(LOCK_REMOTE) {
				try {
					clients.get(login).setPath(path);
					clients.get(login).send("MSG_SEND_DB");
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
		if(clients.containsKey(login)) {
			return clients.get(login).sendDatabase(path);
		}
		return false;
	}
    
    /**
     * Sends a message to all clients
     * @param msg
	 * @param isRemote
     */
    public void send(String msg, boolean isRemote) {
		Map<String, ServerClient> clientsToSend = isRemote?
				getRemoteClients():getDataClients();
        for(ServerClient client : clientsToSend.values()) {
            client.send(msg);
        }
	}
	
	public boolean isConnected(String login) {
		return clients.containsKey(login);
	}
	
	/**
     * Sends a message to given client
	 * @param login
     * @param msg
	 * @return 
     */
    public boolean send(String login, String msg) {
		if(clients.containsKey(login)) {
			clients.get(login).send(msg);
			return true;
		}
		return false;
	}
	
	/**
	 *
	 * @param login
	 */
	public void closeClient(String login) {
        if(clients.containsKey(login)) {
            clients.get(login).close();
            clients.remove(login);
        }
        else {
            callback.received(login, " has been disconnected.");
        }
	}
}




