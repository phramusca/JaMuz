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
	private final ICallBackAuthentication callBackAuth;

	/**
	 *
	 * @param port
	 * @param callback
	 * @param callBackAuth
	 */
	public Server(int port, ICallBackReception callback, ICallBackAuthentication callBackAuth) {
		this.port = port;
		clients = new HashMap();
		this.callback = callback;
		this.callBackAuth = callBackAuth;
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
					CallBackAuthentication callBackAuthLocal = new CallBackAuthentication();
					ServerClient client = new ServerClient(socket, callBackReceptionLocal, callBackAuthLocal);
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
                switch(msg) {
                    case "MSG_NULL" :
                        callback.received(login, "MSG_NULL");
                        clients.get(login).close();
                        clients.remove(login);
                        break;
                    default:
                        callback.received(login, msg);
                        break;
                }
            }
        }
    }
    
	class CallBackAuthentication implements ICallBackAuthentication {
		@Override
		public void authenticated(Client login, ServerClient client) {
			if(!clients.containsKey(login.getId())) {
                clients.put(login.getId(), client);
                callBackAuth.authenticated(login, null);
            }
//            else {
//				//TODO: This can happen. why ?
//				//Until this is solved, considering not a problem
//                client.send("MSG_ERROR_ALREADY_CONNECTED");
//                closeClient(login);
//            }
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
		clients.get(login).sendCover(displayedFile, maxWidth);
	}
	
	public void sendFile(String login, FileInfoInt fileInfoInt) {
		clients.get(login).sendFile(login, fileInfoInt);
	}
    
	/**
	 *
	 * @param login
	 * @param msg
	 */
	public void send(String login, String msg) {
		clients.get(login).send(msg);
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




