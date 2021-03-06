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

import express.Express;
import express.utils.Status;
import jamuz.DbConnJaMuz;
import jamuz.FileInfo;
import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.process.merge.ICallBackMerge;
import jamuz.process.merge.ProcessMerge;
import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import jamuz.utils.Popup;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ws.schild.jave.EncoderException;

/**
 *
 * @author raph
 */
public class Server {

	/**
	 *
	 */
	private Express app;
	public static ServerSocket serverSocket = null;
	private int port;
	private HandleLogin handleLogin;
	private final ICallBackServer callback;
	
	private final TableModelRemote tableModel; //contains clients info from database
	private final Map<String, Client> clientMap; //contains connected clients
	
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

			app.use((req, res) -> {
				String login=req.getHeader("login").get(0);
				if(!validateLogin(login)) {
					res.sendStatus(Status._401);
				}
				String apiVersion=req.getHeader("api-version").get(0);
				if(!apiVersion.equals("1.0")) {
					res.setStatus(Status._301); // 301 Moved Permanently
					JSONArray list = new JSONArray();
					list.add("1.0");
					JSONObject obj = new JSONObject();
					obj.put("supported-versions", list);
					res.send(obj.toJSONString());
				}
			});
			
			app.get("/connect", (req, res) -> {
				res.sendStatus(Status._200);
			});
								
			app.get("/download", (req, res) -> {
				String login=req.getHeader("login").get(0);
				Device device = tableModel.getClient(login).getDevice();
				int idFile = Integer.valueOf(req.getQuery("id"));
				FileInfoInt fileInfoInt = Jamuz.getDb().getFile(idFile);
				
				// FIXME !! 0.5.0 Make destExt an option (server or client side or both ?)
				String destExt = "mp3";
				res.setHeader("oriExt", fileInfoInt.getExt());
				res.setHeader("destExt", destExt);
				boolean transcoded=false;
				if(!fileInfoInt.getExt().equals(destExt)) {
					try {
						fileInfoInt.readMetadata(true);
						fileInfoInt.getLyrics();
						//FIXME !! 0.5.0 Replaygain is wrong for transcoded files on remote
						//FIXME !! 0.5.0 Size differs a lot from flac to mp3 so playlist size to export is wrong :(
						File f = Jamuz.getFile("void", "data", "temp", login, "transcoding");
						String destPath = FilenameUtils.getFullPath(f.getAbsolutePath());
						fileInfoInt.transcode(destExt, destPath);
						transcoded=true;
						res.setHeader("size", String.valueOf(fileInfoInt.getFullPath().length()));
					} catch (IllegalArgumentException | EncoderException ex) {
						Jamuz.getLogger().severe(ex.toString());
						res.setStatus(Status._500);
						res.send(ex.getLocalizedMessage());
					}
				}
				
				File file = fileInfoInt.getFullPath();
				if(!fileInfoInt.isDeleted() && file.exists() && file.isFile()) {	
					String msg=" #"+fileInfoInt.getIdFile()+" ("+file.length()+"o) "+file.getAbsolutePath();
					System.out.println("Sending"+msg);
					res.sendAttachment(file.toPath());
					System.out.println("Sent"+msg);
					ArrayList<FileInfoInt> insert = new ArrayList<>();
					fileInfoInt.setStatus(DbConnJaMuz.SyncStatus.NEW);
					insert.add(fileInfoInt);
					Jamuz.getDb().insertOrUpdateDeviceFiles(insert, device.getId());
					if(transcoded) {
						fileInfoInt.getFullPath().delete();
					}
				} else {
					res.sendStatus(Status._404);
				}			
			});
			
			app.get("/tags", (req, res) -> {
				String login=req.getHeader("login").get(0);	
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
				String login=req.getHeader("login").get(0);
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
					String login=req.getHeader("login").get(0);
					String body = getBody(req.getBody());
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
										String destExt = "mp3";
										FileInfo fileInfo = mergeListDbSelected.get(i);
										//FIXME !! 0.5.0 Better handle this for transcoding to mp3
										if(!fileInfo.getExt().equals(destExt)) {
											fileInfo.setPath(FilenameUtils.concat(fileInfo.getRelativePath(), FilenameUtils.getBaseName(fileInfo.getFilename())+"."+destExt));
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
				} catch (IOException | ParseException ex) {
					Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
					res.sendStatus(Status._500); //FIXME Z Return proper error
				}
			});
			
			app.get("/files/new", (req, res) -> {
				String login=req.getHeader("login").get(0);
				Device device = tableModel.getClient(login).getDevice();
				boolean getCount = Boolean.valueOf(req.getQuery("getCount"));
				String limit="";
				if(!getCount) {
					int idFrom = Integer.valueOf(req.getQuery("idFrom"));
					int nbFilesInBatch = Integer.valueOf(req.getQuery("nbFilesInBatch"));
					limit=" LIMIT "+idFrom+", "+nbFilesInBatch;
				}
				String sql = "SELECT "+(getCount?" COUNT(F.idFile) ":" DF.status, F.*, P.strPath, P.checked, P.copyRight, 0 AS albumRating, 0 AS percentRated, P.mbId AS pathMbId, P.modifDate AS pathModifDate ")
				+ " FROM file F "
				+ " JOIN deviceFile DF ON DF.idFile=F.idFile "
				+ " JOIN path P ON F.idPath=P.idPath "
				+ " WHERE F.deleted=0 AND P.deleted=0 "
				+ " AND DF.idDevice="+device.getId()+" AND DF.status=\""+DbConnJaMuz.SyncStatus.NEW+"\""
				+ " ORDER BY F.idFile "
				+ limit;
				setStatus(login, "Sending "+(getCount?"count":"list") + " of NEW files ("+limit+")");
				if(getCount) {
					res.send(Jamuz.getDb().getFilesCount(sql).toString());
				} else {
					res.send(getFiles(sql));
				}
				setStatus(login, "Sent "+(getCount?"count":"list") + " of NEW files ("+limit+")");
			});
			
			app.get("/files/info", (req, res) -> {
				String login=req.getHeader("login").get(0);
				Device device = tableModel.getClient(login).getDevice();
				boolean getCount = Boolean.valueOf(req.getQuery("getCount"));
				String limit="";
				if(!getCount) {
					int idFrom = Integer.valueOf(req.getQuery("idFrom"));
					int nbFilesInBatch = Integer.valueOf(req.getQuery("nbFilesInBatch"));
					limit=" LIMIT "+idFrom+", "+nbFilesInBatch;
				}
				String sql = "SELECT "+(getCount?" COUNT(F.idFile) ":" 'INFO' AS status, F.*, P.strPath, P.checked, P.copyRight, 0 AS albumRating, 0 AS percentRated, P.mbId AS pathMbId, P.modifDate AS pathModifDate ")
				+ " FROM file F "
				+ " JOIN path P ON F.idPath=P.idPath "
				+ " WHERE F.deleted=0 AND P.deleted=0 "
				+ " AND F.idFile NOT IN (SELECT idFile FROM deviceFile WHERE idDevice="+device.getId()+" AND status=\""+DbConnJaMuz.SyncStatus.NEW+"\") "
				+ " ORDER BY F.idFile "
				+ limit;
				setStatus(login, "Sending "+(getCount?"count":"list") + " of INFO files ("+limit+")");
				if(getCount) {
					res.send(Jamuz.getDb().getFilesCount(sql).toString());
				} else {
					res.send(getFiles(sql));
				}
				setStatus(login, "Sending "+(getCount?"count":"list") + " of INFO files ("+limit+")");
			});
			
			app.listen(port+1); // port is already used by remote
			
			return true;
		} catch (IOException ex) {
			Popup.error("Cannot start JaMuz Remote Server", ex);
			return false;
		}
	}
	
	private String getFiles(String sql) {
		ArrayList<FileInfoInt> filesToSend = new ArrayList<>();
		Jamuz.getDb().getFiles(filesToSend, sql);
		Map jsonAsMap = new HashMap();
		JSONArray jSONArray = new JSONArray();
		for (FileInfoInt fileInfo : filesToSend) {
			fileInfo.getTags();
			//FIXME !! 0.5.0 Better handle this for transcoding to mp3
			String destExt = "mp3";
			if(!fileInfo.getExt().equals(destExt)) {
				fileInfo.setPath(FilenameUtils.concat(fileInfo.getRelativePath(), FilenameUtils.getBaseName(fileInfo.getFilename())+"."+destExt));
			}			
			jSONArray.add(fileInfo.toMap());
		}
		jsonAsMap.put("files", jSONArray);		
		return JSONValue.toJSONString(jsonAsMap);
	}
	
	private boolean validateLogin(String login) {
		return tableModel.contains(login) && tableModel.getClient(login).isEnabled();
//		if(tableModel.contains(login)) {
//			if(tableModel.getClient(login).isEnabled()) {
//				return true;
//			}
//		}
//		return false;
	}

	private String getBody(InputStream stream) throws IOException {
		//read body
		StringWriter writer = new StringWriter();
		IOUtils.copy(stream, writer, StandardCharsets.UTF_8.name());
		return writer.toString();
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
			
			app.stop();
		} catch (IOException ex) {
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	void closeClients() {
		for(Client client : clientMap.values()) {
            closeClient(client.getClientId());
        }
	}

	int getPort() {
		return port;
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
		public void connected(Client client) {
			if(!tableModel.contains(client.getInfo().getLogin())) {
				//Creates a new machine, device and statSource
				//and store the client
				StringBuilder zText = new StringBuilder ();
				if(Jamuz.getDb().isMachine(client.getInfo().getLogin(), zText, true)) {
					Device device = new Device(-1, 
							client.getInfo().getLogin(), 
							"source", client.getInfo().getLogin(), 
							-1, 
							client.getInfo().getLogin(), true);
					if(Jamuz.getDb().setDevice(device)) {
						Device deviceWithId = Jamuz.getDb().getDevice(client.getInfo().getLogin());
						client.getInfo().setDevice(deviceWithId);
						StatSource statSource = new StatSource(
							-1, client.getInfo().getLogin(), 6, 
							client.getInfo().getLogin(), "MySqlUser", "MySqlPwd", 
							client.getInfo().getRootPath(), 
							client.getInfo().getLogin(), 
							deviceWithId.getId(), false, "", true);
						if(Jamuz.getDb().setStatSource(statSource)) {
							StatSource statSourceWithId = Jamuz.getDb().getStatSource(client.getInfo().getLogin());
							client.getInfo().setStatSource(statSourceWithId);
							if(Jamuz.getDb().setClientInfo(client.getInfo())) {
								ClientInfo clientInfo = Jamuz.getDb().getClient(client.getInfo().getLogin());
								client.getInfo().setId(clientInfo.getId());
								tableModel.add(clientInfo);
							}
						}
					}
				}
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
	
	private void setStatus(String login, String status) {
		if(tableModel.contains(login)) {
			ClientInfo clientInfo = tableModel.getClient(login);
			clientInfo.setStatus(status);
			tableModel.fireTableDataChanged();
		}
	}
	
	private Map<String, Client> getRemoteClients() {
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
	
	public void sendFile(String clientId, String login, int idFile) {
		FileInfoInt fileInfoInt = Jamuz.getDb().getFile(idFile);
		setStatus(login, "Sending file: "+fileInfoInt.getRelativeFullPath());
		if(!sendFile(clientId, fileInfoInt)) {
			//TODO SYNC Happens (still ?) when file not found
			// Need to mark as deleted in db 
			// AND somehow remove it from filesToKeep
			//and filesToGet in remote
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
        for(Client client : getRemoteClients().values()) {
            client.send(jsonAsMap);
        }
	}
	
	public boolean isConnected(String clientId) {
		return clientMap.containsKey(clientId);
	}
	
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