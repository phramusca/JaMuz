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

public class Server {
	public static ServerSocket serverSocket = null;
	private final int port;
	private HandleLogin handleLogin;
	private final Map<String, ServerClient> clients;
	private final ICallBackReception callback;
	private final ICallBackAuthentication callBackAuth;

	public Server(int port, ICallBackReception callback, ICallBackAuthentication callBackAuth) {
		this.port = port;
		clients = new HashMap();
		this.callback = callback;
		this.callBackAuth = callBackAuth;
	}
	
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
		public void authenticated(String login, ServerClient client) {
			if(!clients.containsKey(login)) {
                clients.put(login, client);
                callBackAuth.authenticated(login, null);
            }
            else {
                client.send("MSG_ERROR_ALREADY_CONNECTED");
                closeClient(login);
            }
		}
	}
	
//    public void sendFile(String filename) {
//        for(ServerClient client : clients.values()) {
//            client.send("MSG_SENDING_"+filename);
//            client.sendFile(filename);
//        }
//    }
    
    public void sendCover(FileInfoInt displayedFile) {
        for(ServerClient client : clients.values()) {
            client.sendCover(displayedFile);
        }
    }
    
	public void send(String login, String msg) {
		clients.get(login).send(msg);
	}
    
    /**
     * Sends a message to all clients
     * @param msg
     */
    public void send(String msg) {
        for(ServerClient client : clients.values()) {
            client.send(msg);
        }
	}
	
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




