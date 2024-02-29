/*
 * Copyright (C) 2017 phramusca <phramusca@gmail.com>
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

//FIXME ! Do not popup errors when on server
// => either send errors to client and/or log
// => incl. SQL errors: see repercussions elsewhere in code

import express.Express;
import express.http.Status;
import io.javalin.http.sse.SseClient;
import jamuz.FileInfo;
import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.player.MPlaybackListener;
import jamuz.player.Mplayer;
import jamuz.process.sync.SyncStatus;
import jamuz.process.check.Location;
import jamuz.process.merge.ICallBackMerge;
import jamuz.process.merge.ProcessMerge;
import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import jamuz.process.sync.ICallBackSync;
import jamuz.process.sync.ProcessSync;
import jamuz.utils.Popup;
import jamuz.utils.ProcessAbstract;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class Server {

	/**
	 *
	 */
	private Express app;

	/**
	 *
	 */
	public static ServerSocket serverSocket = null;
	private int port;
	private HandleLogin handleLogin;
	private final ICallBackServer callback;
	
	private final TableModelRemote tableModel; //contains clients info from database
	private final Map<String, SocketClient> clientMap; //contains connected clients
	
    private static final Mplayer mplayer= new Mplayer();
    Queue<SseClient> clients = new ConcurrentLinkedQueue<>();
    
	/**
	 *
	 * @param port
	 * @param callback
	 */
	public Server(int port, ICallBackServer callback) {
		this.port = port;
		clientMap = new ConcurrentHashMap();
		tableModel = new TableModelRemote();
		tableModel.setColumnNames();
		this.callback = callback;
        
        MPlaybackListener mPlaybackListener = new MPlaybackListener() {
			@Override
			public void volumeChanged(float volume) {
				if(volume>=0) {
					volume=5*Math.round(volume/5);
                    Jamuz.getLogger().log(Level.INFO, "volume: {0}", (float)volume);
				}
			}

			@Override
			public void playbackFinished() {
                Jamuz.getLogger().info("Server playback finished");
			}

			@Override
			public void positionChanged(int position, int length) {
				Jamuz.getLogger().log(Level.INFO, "position: {0}", position);
                
                for (SseClient client : clients) {
                    client.sendEvent("positionChanged", String.valueOf(position), "id");
                }
			}
		};
		
		mplayer.addListener(mPlaybackListener);
	}
	
	/**
	 *
	 * @return
	 */
	public boolean connect() {
		try {
            //TODO: Secure connections
            //http://www.java2s.com/Code/Java/Network-Protocol/SecureCommunicationwithJSSE.htm

			//Socket connection, for the remote
			//Create the server socket
			serverSocket = new ServerSocket(port);
			//Start login handling thread
			handleLogin = new HandleLogin(serverSocket);
			handleLogin.start();
			
			//Start REST Server Express, for Sync process
			app = new Express();

            
//https://medium.com/@anugrahasb1997/implementing-server-sent-events-sse-in-android-with-okhttp-eventsource-226dc9b2599d
            app.sse("/sse", client -> {
//                client.keepAlive();
                client.onClose(() -> clients.remove(client));
                clients.add(client);
                
//                client.s
            });
            //TODO: Switch to https://github.com/square/okhttp/tree/master/okhttp-sse when ready
            
            
            app.get("/play", (req, res) -> {
                String get = res.get("idFile");
                FileInfoInt file = Jamuz.getDb().file().getFile(Integer.parseInt(get), "");
                if(mplayer!=null) {
                    mplayer.stop();
                }
//                mplayer.addListener(new MPlaybackListener() {
//                    @Override
//                    public void volumeChanged(float volume) {
//                        System.out.println("volume changed:" + volume);
//                    }
//
//                    @Override
//                    public void playbackFinished() {
//                        System.out.println("playback stopped");
//                    }
//
//                    @Override
//                    public void positionChanged(int position, int length) {
//                        System.out.println("poistion changed: " + position);
//                        for (SseClient client : clients) {
//                            client.sendEvent(String.valueOf(position));
//                        }
//                    }
//                });
                mplayer.play(file.getFullPath().getAbsolutePath(), false);
                res.sendStatus(Status._200.getCode());
            });
            
//			app.use((req, res) -> {
//				String login=req.get("login");
//                
//				if(!tableModel.contains(login)) {
//					String password=req.get("password");
//					String rootPath=req.get("rootPath");
//					String model=req.get("model");
//					boolean enableNewClients = Boolean.parseBoolean(Jamuz.getOptions().get("server.enable.new.clients", "false"));
//					ClientInfo info = new ClientInfo(login, password, rootPath, model, enableNewClients);
//					createClient(info);
//				}
//				if(!tableModel.contains(login) || !tableModel.getClient(login).isEnabled()) {
//					res.sendStatus(Status._401.getCode());
//				} else {
//					String apiVersion=req.get("api-version");
//					if(!apiVersion.equals("2.0")) {
//						res.status(Status._301.getCode()); // 301 Moved Permanently
//						JSONArray list = new JSONArray();
//						list.add("2.0");
//						JSONObject obj = new JSONObject();
//						obj.put("supported-versions", list);
//						res.send(obj.toJSONString());
//					}	
//				}
//			});
			
			app.get("/connect", (req, res) -> {
				res.sendStatus(Status._200.getCode());
			});
								
			app.get("/download", (req, res) -> {
				String login=req.get("login");
				Device device = tableModel.getClient(login).getDevice();
				String destExt = device.getPlaylist().getDestExt();
				int idFile = Integer.parseInt(req.query("id"));
				FileInfoInt fileInfoInt = Jamuz.getDb().file().getFile(idFile, destExt); 
				File file = fileInfoInt.getFullPath();
				if(!destExt.isBlank() && !fileInfoInt.getExt().equals(destExt)) {
					Location location = new Location("location.transcoded");
					if(!location.check()) {
						res.sendStatus(Status._410.getCode());
					}
					String destPath = location.getValue();
					file = fileInfoInt.getTranscodedFile(destExt, destPath);
					if(!file.exists() || !file.isFile()) {
						res.sendStatus(Status._410.getCode());
					}
				}
				if(file.exists() && file.isFile()) {
					String msg=" #"+fileInfoInt.getIdFile()+" ("+file.length()+"o) "+file.getAbsolutePath();
					System.out.println("Sending"+msg);
					res.download(file.toPath());
					System.out.println("Sent"+msg);
					ArrayList<FileInfoInt> insert = new ArrayList<>();
					fileInfoInt.setStatus(SyncStatus.NEW);
					insert.add(fileInfoInt);
					Jamuz.getDb().deviceFile().lock().insertOrUpdate(insert, device.getId());
				} else {
					res.sendStatus(Status._404.getCode());
				}		
			});
			
			app.get("/tags", (req, res) -> {
				String login=req.get("login");	
				setStatus(login, "Sending tags");
				JSONArray list = new JSONArray();
				for(String tag : Jamuz.getTags()) {
					list.add(tag);
				}
				JSONObject obj = new JSONObject();
				obj.put("type", "tags");
				obj.put("tags", list);
				res.send(obj.toJSONString());
			});
			
			app.get("/genres", (req, res) -> {
				String login=req.get("login");
				setStatus(login, "Sending genres");
				JSONArray list = new JSONArray();
				for(String genre : Jamuz.getGenres()) {
					list.add(genre);
				}
				JSONObject obj = new JSONObject();
				obj.put("type", "genres");
				obj.put("genres", list);
				res.send(obj.toJSONString());
			});

			//Merge statistics
			app.post("/files", (req, res) -> {
				try {
					String login=req.get("login");
					String body = req.body().toString(); //getBody();
					JSONObject jsonObject = (JSONObject) new JSONParser().parse(body);
					setStatus(login, "Received files to merge");
					ArrayList<FileInfo> newTracks = new ArrayList<>();
					JSONArray files = (JSONArray) jsonObject.get("files");
					for(int i=0; i<files.size(); i++) {
						JSONObject obj = (JSONObject) files.get(i);
						FileInfo file = new FileInfo(login, obj);
						newTracks.add(file);
					}
					List<StatSource> sources = new ArrayList();
					sources.add(tableModel.getClient(login).getStatSource());
					setStatus(login, "Starting merge");
					Device device = tableModel.getClient(login).getDevice();
					String destExt = device.getPlaylist().getDestExt();
					new ProcessMerge("Thread.Server.ProcessMerge."+login,
							sources, false, false, newTracks,
							tableModel.getClient(login).getProgressBar(),
							new ICallBackMerge() {
								@Override
								public void completed(ArrayList<FileInfo> errorList, ArrayList<FileInfo> mergeListDbSelected, String popupMsg, String mergeReport) {
									Jamuz.getLogger().info(popupMsg);
									setStatus(login, popupMsg);
									JSONObject obj = new JSONObject();
									obj.put("type", "mergeListDbSelected");
									JSONArray jsonArray = new JSONArray();
									for (int i=0; i < mergeListDbSelected.size(); i++) {
										FileInfo fileInfo = mergeListDbSelected.get(i);
										if(!destExt.isBlank() && !fileInfo.getExt().equals(destExt)) {
											//Note: No need to get info from fileTranscoded table since only stats are updated on remote
											fileInfo.setExt(destExt);
										}
										jsonArray.add(fileInfo.toMap());
									}
									obj.put("files", jsonArray); 
									res.send(obj.toJSONString());
								}

								@Override
								public void refresh() {
									tableModel.fireTableDataChanged();
								}
					}).start();
				} catch (ParseException ex) {
					Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
					res.sendStatus(Status._500.getCode()); //FIXME Z Return proper error
				}
			});
			
			app.get("/refresh", (req, res) -> { 
				String login=req.get("login"); 
				Device device = tableModel.getClient(login).getDevice(); 
				ProcessSync processSync = new ProcessSync("Server.ProcessSync",  
						device, tableModel.getClient(login).getProgressBar(),  
						new ICallBackSync() { 
					@Override 
					public void refresh() { 
						//TODO: Refresh only concerned cell (progressBar of given login) 
						getTableModel().fireTableDataChanged(); 
					} 
 
					@Override 
					public void enable() {
						res.sendStatus(Status._200.getCode());
					}
 
					@Override 
					public void enableButton(boolean enable) {} 
 
					@Override 
					public void addRow(String file, int idIcon) {} 
 
					@Override 
					public void addRow(String file, String msg) {} 
						}); 
                processSync.start();
				
			});
			
			app.get("/files/:status", (req, res) -> { 
				String login=req.get("login"); 
				Device device = tableModel.getClient(login).getDevice(); 
				String destExt = device.getPlaylist().getDestExt(); 
				SyncStatus status = SyncStatus.valueOf(req.params("status")); 
				boolean getCount = Boolean.parseBoolean(req.query("getCount")); 
				
				String limit=""; 
				if(!getCount) { 
					int idFrom = Integer.parseInt(req.query("idFrom")); 
					int nbFilesInBatch = Integer.parseInt(req.query("nbFilesInBatch")); 
					limit=" LIMIT "+idFrom+", "+nbFilesInBatch; 
				}
				 
				setStatus(login, "Sending "+(getCount?"count":"list") + " of "+status.name().toUpperCase()+" files ("+limit+" )"); 
				if(getCount) { 
					res.send(Jamuz.getDb().file().getFilesCount(status, device, limit, destExt).toString()); 
				} else { 
					res.send(getFiles(status, device, limit, destExt)); 
				} 
				setStatus(login, "Sent "+(getCount?"count":"list") + " of "+status.name().toUpperCase()+" files ("+limit+" )"); 
			});
			
			app.listen(port+1); // port is already used by remote
			
			return true;
		} catch (IOException ex) {
			Popup.error("Cannot start JaMuz Remote Server", ex);
			return false;
		}
	}
	
	private String getFiles(SyncStatus status, Device device, String limit, String destExt) {
		ArrayList<FileInfoInt> filesToSend = new ArrayList<>();
		Jamuz.getDb().file().getFiles(filesToSend, status, device, limit, destExt);
		Map jsonAsMap = new HashMap();
		JSONArray jSONArray = new JSONArray();
		for (FileInfoInt fileInfo : filesToSend) {
			fileInfo.getTags();
			if(!destExt.isBlank() && !fileInfo.getExt().equals(destExt)) {
				fileInfo.setExt(destExt);
			}
			jSONArray.add(fileInfo.toMap());
		}
		jsonAsMap.put("files", jSONArray);		
		return JSONValue.toJSONString(jsonAsMap);
	}

	private String getBody(InputStream stream) throws IOException {
		StringWriter writer = new StringWriter();
		IOUtils.copy(stream, writer, StandardCharsets.UTF_8.name());
		return writer.toString();
	}
	
	/**
	 *
	 * @param port
	 */
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
			
			app.stop();
		} catch (IOException ex) {
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	void closeClients() {
		for(SocketClient client : clientMap.values()) {
            closeClient(client.getClientId());
        }
	}

	int getPort() {
		return port;
	}

	class HandleLogin extends ProcessAbstract {

		HandleLogin(ServerSocket ss){
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
					SocketClient client = new SocketClient(socket, callBackReceptionLocal);
					checkAbort();
					client.login();
				}
			} catch (IOException | InterruptedException ex) {
				Logger.getLogger(Server.class.getName()).log(Level.WARNING, null, ex);
			}
		}
	}
	
    class CallBackReception implements ICallBackReception {
        @Override
        public void received(String clientId, String login, String msg) {
            if(clientMap.containsKey(clientId)) {
				callback.received(clientId, msg);
            }
        }
		
		@Override
		public void connected(SocketClient client) {
			if(!tableModel.contains(client.getInfo().getLogin())) {
				createClient(client.getInfo());
			}
			if(!clientMap.containsKey(client.getClientId())
					&& tableModel.contains(client.getInfo().getLogin())
						&& tableModel.getClient(client.getInfo().getLogin()).isEnabled()
					//TODO: Hash passwords at saving
							&& tableModel.getClient(client.getInfo().getLogin()).getPwd() 
									.equals(client.getInfo().getPwd())) {
				ClientInfo clientInfoModel = tableModel.getClient(client.getInfo().getLogin());
				clientMap.put(client.getClientId(), client);
				client.send("MSG_CONNECTED");
				if(client.getInfo().isConnected()) {
					clientInfoModel.setConnected(true);
					callback.connectedRemote(client.getClientId());
				}
            } else {
				//FIXME Z SERVER Make this disconnect client AND NOT RECONNECT ("Authentication failed." in android notif)
				client.send("MSG_ERROR_CONNECTION"); 
			}
			tableModel.fireTableDataChanged();
		}

		@Override
		public void disconnected(ClientInfo clientInfo, String clientId) {
			if(clientMap.containsKey(clientId)) {
				clientMap.get(clientId).close();
				clientMap.remove(clientId);
			}
			if(tableModel.contains(clientInfo.getLogin())) {
				ClientInfo clientInfoModel = tableModel.getClient(clientInfo.getLogin());
				clientInfoModel.setConnected(false);
				tableModel.fireTableDataChanged();
			} 
		}
    }
	
	private void createClient(ClientInfo clientInfo) {
		//Creates a new machine, device and statSource
		//and store the client
		StringBuilder zText = new StringBuilder ();
		if(Jamuz.getDb().machine().lock().getOrInsert(clientInfo.getLogin(), zText, true)) {
			Device device = new Device(-1, 
					clientInfo.getLogin(), 
					"source", clientInfo.getLogin(), 
					-1, 
					clientInfo.getLogin(), true);
			if(Jamuz.getDb().device().lock().insertOrUpdate(device)) {
				Device deviceWithId = Jamuz.getDb().device().get(clientInfo.getLogin());
				clientInfo.setDevice(deviceWithId);
				StatSource statSource = new StatSource(
					-1, clientInfo.getLogin(), 6, 
					clientInfo.getLogin(), "MySqlUser", "MySqlPwd", 
					clientInfo.getRootPath(), 
					clientInfo.getLogin(), 
					deviceWithId.getId(), false, "", true);
				if(Jamuz.getDb().statSource().lock().insertOrUpdate(statSource)) {
					StatSource statSourceWithId = Jamuz.getDb().statSource().get(clientInfo.getLogin());
					clientInfo.setStatSource(statSourceWithId);
					if(Jamuz.getDb().client().lock().insertOrUpdate(clientInfo)) {
						ClientInfo clientInfoUpdated = Jamuz.getDb().client().get(clientInfo.getLogin());
						clientInfo.setId(clientInfoUpdated.getId());
						tableModel.add(clientInfoUpdated);
					}
				}
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
	
	private Map<String, SocketClient> getRemoteClients() {
		return clientMap.entrySet().stream()
			.filter((client) -> client.getValue().getInfo().isConnected())
			.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
	}
	
	/**
	 *
	 * @param clientId
	 * @param displayedFile
	 * @param maxWidth
	 */
    public void sendCover(String clientId, FileInfoInt displayedFile, int maxWidth) {
		if(clientMap.containsKey(clientId)) {
			clientMap.get(clientId).sendCover(displayedFile, maxWidth);
		}
		
	}
	
	/**
	 *
	 * @param clientId
	 * @param login
	 * @param idFile
	 * @param destExt
	 */
	public void sendFile(String clientId, String login, int idFile, String destExt) {
		FileInfoInt fileInfoInt = Jamuz.getDb().file().getFile(idFile, destExt);
		setStatus(login, "Sending file: "+fileInfoInt.getRelativeFullPath());
		if(!sendFile(clientId, fileInfoInt)) {
			Popup.error("Cannot send missing file \""
					+fileInfoInt.getFullPath().getAbsolutePath()+"\"");
		}
	}
	
	private boolean sendFile(String clientId, FileInfoInt fileInfoInt) {
		if(clientMap.containsKey(clientId)) {
			return clientMap.get(clientId).sendFile(fileInfoInt);
		}
		return true;
	}
    
	
	/**
     * Sends a message to all remote clients
	 * @param jsonAsMap
     */
    public void send(Map jsonAsMap) {
        for(SocketClient client : getRemoteClients().values()) {
            client.send(jsonAsMap);
        }
	}
	
	/**
	 *
	 * @param clientId
	 * @return
	 */
	public boolean isConnected(String clientId) {
		return clientMap.containsKey(clientId);
	}
	
	/**
	 *
	 * @param clientId
	 * @param obj
	 * @return
	 */
	public boolean send(String clientId, JSONObject obj) {
		if(clientMap.containsKey(clientId)) {
			clientMap.get(clientId).send(obj);
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
	 * @param clientId
	 */
	public void closeClient(String clientId) {
        if(clientMap.containsKey(clientId)) {
            clientMap.get(clientId).close();
            clientMap.remove(clientId);
        }
        else {
            callback.received(clientId, " has been disconnected.");
        }
	}

	/**
	 *
	 * @return
	 */
	public TableModelRemote getTableModel() {
		return tableModel;
	}
	
	/**
	 *
	 */
	public void fillClients() {
		tableModel.clear();
		LinkedHashMap<Integer, ClientInfo> clientsDb = new LinkedHashMap<>();
		Jamuz.getDb().client().get(clientsDb);
		for(ClientInfo clientInfo : clientsDb.values()) {
			tableModel.add(clientInfo);
		}
	}
}