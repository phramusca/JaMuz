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
import jamuz.gui.PanelMain;
import jamuz.process.sync.SyncStatus;
import jamuz.process.check.Location;
import jamuz.process.merge.ICallBackMerge;
import jamuz.process.merge.ProcessMerge;
import jamuz.process.merge.StatSource;
import jamuz.process.sync.Device;
import jamuz.process.sync.ICallBackSync;
import jamuz.process.sync.ProcessSync;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class Server {

    //https://github.com/Aarkan1/java-express
    private Express app;
    private int port;
    private final TableModelRemote tableModel; //contains clients info from database
    Queue<SseClient> sseClients = new ConcurrentLinkedQueue<>();
    private final ICallBackServer callBackServer;

    /**
     *
     * @param port
     * @param callBackServer
     */
    public Server(int port, ICallBackServer callBackServer) {
        this.port = port;
        tableModel = new TableModelRemote();
        tableModel.setColumnNames();
        this.callBackServer = callBackServer;
    }

    public void sendSseEvent(String event, String data, String id) {
        for (SseClient sseClient : sseClients) {
            sseClient.sendEvent(event, data, id);
        }
    }

    /**
     *
     * @return
     */
    public boolean connect() {
        app = new Express();

        app.sse("/sse", client -> {
            String login = client.ctx.req.getHeader("login");
            ClientInfo clientInfo = tableModel.getClient(login);
            
            client.onClose(() -> {
                //FIXME ! This is never called, but should be !!
                // Is connection really closed ? check it first
                clientInfo.setConnected(false);
                sseClients.remove(client);
            });
            sseClients.add(client);
            clientInfo.setConnected(true);
        });
        //https://medium.com/@anugrahasb1997/implementing-server-sent-events-sse-in-android-with-okhttp-eventsource-226dc9b2599d

        app.post("/action", (req, res) -> {
            String action = (String) req.body().get("action");
            String value = (String) req.body().getOrDefault("value", "");
            callBackServer.received(action, value);
            res.sendStatus(Status._200.getCode());
        });

        app.use((req, res) -> {
            String login = req.get("login");

            if (!tableModel.contains(login)) {
                String password = req.get("password");
                String rootPath = req.get("rootPath");
                String model = req.get("model");
                boolean enableNewClients = Boolean.parseBoolean(Jamuz.getOptions().get("server.enable.new.clients", "false"));
                ClientInfo info = new ClientInfo(login, password, rootPath, model, enableNewClients);
                createClient(info);
            }
            if (!tableModel.contains(login) || !tableModel.getClient(login).isEnabled()) {
                res.sendStatus(Status._401.getCode());
            } else {
                String apiVersion = req.get("api-version");
                if (!apiVersion.equals("2.0")) {
                    res.status(Status._301.getCode()); // 301 Moved Permanently
                    JSONArray list = new JSONArray();
                    list.add("2.0");
                    JSONObject obj = new JSONObject();
                    obj.put("supported-versions", list);
                    res.send(obj.toJSONString());
                }
            }
        });

        app.get("/connect", (req, res) -> {
            res.sendStatus(Status._200.getCode());
        });

        app.get("/download", (req, res) -> {
            String login = req.get("login");
            Device device = tableModel.getClient(login).getDevice();
            String destExt = device.getPlaylist().getDestExt();
            int idFile = Integer.parseInt(req.query("id"));
            FileInfoInt fileInfoInt = Jamuz.getDb().file().getFile(idFile, destExt);
            File file = fileInfoInt.getFullPath();
            if (!destExt.isBlank() && !fileInfoInt.getExt().equals(destExt)) {
                Location location = new Location("location.transcoded");
                if (!location.check()) {
                    res.sendStatus(Status._410.getCode());
                }
                String destPath = location.getValue();
                file = fileInfoInt.getTranscodedFile(destExt, destPath);
                if (!file.exists() || !file.isFile()) {
                    res.sendStatus(Status._410.getCode());
                }
            }
            if (file.exists() && file.isFile()) {
                String msg = " #" + fileInfoInt.getIdFile() + " (" + file.length() + "o) " + file.getAbsolutePath();
                System.out.println("Sending" + msg);
                res.download(file.toPath());
                System.out.println("Sent" + msg);
                ArrayList<FileInfoInt> insert = new ArrayList<>();
                fileInfoInt.setStatus(SyncStatus.NEW);
                insert.add(fileInfoInt);
                Jamuz.getDb().deviceFile().lock().insertOrUpdate(insert, device.getId());
            } else {
                res.sendStatus(Status._404.getCode());
            }
        });

        app.get("/tags", (req, res) -> {
            String login = req.get("login");
            setStatus(login, "Sending tags");
            JSONArray list = new JSONArray();
            for (String tag : Jamuz.getTags()) {
                list.add(tag);
            }
            JSONObject obj = new JSONObject();
            obj.put("type", "tags");
            obj.put("tags", list);
            res.send(obj.toJSONString());
        });

        app.get("/genres", (req, res) -> {
            String login = req.get("login");
            setStatus(login, "Sending genres");
            JSONArray list = new JSONArray();
            for (String genre : Jamuz.getGenres()) {
                list.add(genre);
            }
            JSONObject obj = new JSONObject();
            obj.put("type", "genres");
            obj.put("genres", list);
            res.send(obj.toJSONString());
        });

        app.get("/playing", ((req, res) -> {
            JSONArray list = new JSONArray();
            for (String playlist : PanelMain.getPlaylists()) {
                list.add(playlist);
            }
            JSONObject obj = new JSONObject();
            obj.put("playlists", list);
            obj.put("selectedPlaylist", PanelMain.getSelectPlaylist());
            
            int idFile = callBackServer.getIdFile();
            obj.put("idFile", idFile);
            res.send(obj.toJSONString());
        }));

        //Merge statistics
        app.post("/files", (req, res) -> {
            try {
                String login = req.get("login");
                setStatus(login, "Received files to merge");
                ArrayList<FileInfo> newTracks = new ArrayList<>();
                
                ArrayList files = (ArrayList) req.body().get("files");
                for (int i = 0; i < files.size(); i++) {
                    LinkedHashMap obj = (LinkedHashMap) files.get(i);
                    FileInfo file = new FileInfo(login, obj);
                    newTracks.add(file);
                }
                List<StatSource> sources = new ArrayList();
                sources.add(tableModel.getClient(login).getStatSource());
                setStatus(login, "Starting merge");
                Device device = tableModel.getClient(login).getDevice();
                String destExt = device.getPlaylist().getDestExt();
                ProcessMerge processMerge = new ProcessMerge("Thread.Server.ProcessMerge." + login,
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
                        for (int i = 0; i < mergeListDbSelected.size(); i++) {
                            FileInfo fileInfo = mergeListDbSelected.get(i);
                            if (!destExt.isBlank() && !fileInfo.getExt().equals(destExt)) {
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
                });
                processMerge.start();
                processMerge.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                res.sendStatus(Status._500.getCode()); //FIXME Z Return proper error
            }
        });

        app.get("/refresh", (req, res) -> {
            String login = req.get("login");
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
                public void enableButton(boolean enable) {
                }

                @Override
                public void addRow(String file, int idIcon) {
                }

                @Override
                public void addRow(String file, String msg) {
                }
            });
            processSync.start();

        });

        app.get("/files/:status", (req, res) -> {
            String login = req.get("login");
            Device device = tableModel.getClient(login).getDevice();
            String destExt = device.getPlaylist().getDestExt();
            SyncStatus status = SyncStatus.valueOf(req.params("status"));
            boolean getCount = Boolean.parseBoolean(req.query("getCount"));

            String limit = "";
            if (!getCount) {
                int idFrom = Integer.parseInt(req.query("idFrom"));
                int nbFilesInBatch = Integer.parseInt(req.query("nbFilesInBatch"));
                limit = " LIMIT " + idFrom + ", " + nbFilesInBatch;
            }

            setStatus(login, "Sending " + (getCount ? "count" : "list") + " of " + status.name().toUpperCase() + " files (" + limit + " )");
            if (getCount) {
                res.send(Jamuz.getDb().file().getFilesCount(status, device, limit, destExt).toString());
            } else {
                res.send(getFiles(status, device, limit, destExt));
            }
            setStatus(login, "Sent " + (getCount ? "count" : "list") + " of " + status.name().toUpperCase() + " files (" + limit + " )");
        });

        app.listen(port);

        return true;
    }

    private String getFiles(SyncStatus status, Device device, String limit, String destExt) {
        ArrayList<FileInfoInt> filesToSend = new ArrayList<>();
        Jamuz.getDb().file().getFiles(filesToSend, status, device, limit, destExt);
        Map jsonAsMap = new HashMap();
        JSONArray jSONArray = new JSONArray();
        for (FileInfoInt fileInfo : filesToSend) {
            fileInfo.getTags();
            if (!destExt.isBlank() && !fileInfo.getExt().equals(destExt)) {
                fileInfo.setExt(destExt);
            }
            jSONArray.add(fileInfo.toMap());
        }
        jsonAsMap.put("files", jSONArray);
        return JSONValue.toJSONString(jsonAsMap);
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
        app.stop();
    }

    int getPort() {
        return port;
    }

    private void createClient(ClientInfo clientInfo) {
        //Creates a new machine, device and statSource
        //and store the client
        StringBuilder zText = new StringBuilder();
        if (Jamuz.getDb().machine().lock().getOrInsert(clientInfo.getLogin(), zText, true)) {
            Device device = new Device(-1,
                    clientInfo.getLogin(),
                    "source", clientInfo.getLogin(),
                    -1,
                    clientInfo.getLogin(), true);
            if (Jamuz.getDb().device().lock().insertOrUpdate(device)) {
                Device deviceWithId = Jamuz.getDb().device().get(clientInfo.getLogin());
                clientInfo.setDevice(deviceWithId);
                StatSource statSource = new StatSource(
                        -1, clientInfo.getLogin(), 6,
                        clientInfo.getLogin(), "MySqlUser", "MySqlPwd",
                        clientInfo.getRootPath(),
                        clientInfo.getLogin(),
                        deviceWithId.getId(), false, "", true);
                if (Jamuz.getDb().statSource().lock().insertOrUpdate(statSource)) {
                    StatSource statSourceWithId = Jamuz.getDb().statSource().get(clientInfo.getLogin());
                    clientInfo.setStatSource(statSourceWithId);
                    if (Jamuz.getDb().client().lock().insertOrUpdate(clientInfo)) {
                        ClientInfo clientInfoUpdated = Jamuz.getDb().client().get(clientInfo.getLogin());
                        clientInfo.setId(clientInfoUpdated.getId());
                        tableModel.add(clientInfoUpdated);
                    }
                }
            }
        }
    }

    private void setStatus(String login, String status) {
        if (tableModel.contains(login)) {
            ClientInfo clientInfo = tableModel.getClient(login);
            clientInfo.setStatus(status);
            tableModel.fireTableDataChanged();
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
        for (ClientInfo clientInfo : clientsDb.values()) {
            tableModel.add(clientInfo);
        }
    }
}
