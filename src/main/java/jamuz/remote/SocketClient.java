/*
 * Copyright (C) 2023 phramusca <phramusca@gmail.com>
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

import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.utils.ImageUtils;
import jamuz.utils.ProcessAbstract;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class SocketClient {
	private final Socket socket;
	private BufferedReader bufferedReader;
	private SocketReception reception;
	private final ICallBackReception callback;
	private ClientInfo info;
	private int canal;
    private String address;
    private PrintWriter printWriter;
    private OutputStream outputStream;
	private InputStream inputStream;
	private String path;
	
	/**
	 * Set the value of locationWork
	 *
	 * @param locationWork new value of locationWork
	 */
	public void setPath(String locationWork) {
		this.path = locationWork;
	}
	
	/**
	 *
	 * @param socket
	 * @param callback
	 */
	public SocketClient(Socket socket, ICallBackReception callback) {
		this.socket = socket;
        this.address = socket.getRemoteSocketAddress().toString();
        address = address.split(":")[0].substring(1);
		this.callback = callback;
	}
	
	/**
	 *
	 * @return
	 */
	public boolean login() {
		try {
			//Starting emission thread
			this.printWriter = new PrintWriter(socket.getOutputStream());
            this.outputStream = socket.getOutputStream();
			this.inputStream = socket.getInputStream();
			//Authenticate
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			Authentication authentication = new Authentication();
			authentication.start();
			
			return true;
		} catch (IOException ex) {
//			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
	}
	
	class Authentication extends ProcessAbstract {

		Authentication() {
			super("Thread.Server.Authentication");
		}

		@Override
		public void run() {
			try {
                send("MSG_AUTHENTICATE");
				String json = bufferedReader.readLine();		
				JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);
				String login =  (String) jsonObject.get("login");
				String password = (String) jsonObject.get("password");
				int newCanal = (int) (long) jsonObject.get("canal");
				String appId = (String) jsonObject.get("appId");
				String rootPath = (String) jsonObject.get("rootPath");
				String model = (String) jsonObject.get("model");
				reception = new SocketReception(bufferedReader, callback, SocketClient.this);
				reception.start();
				boolean enableNewClients = Boolean.parseBoolean(Jamuz.getOptions().get("server.enable.new.clients", "false"));
				info = new ClientInfo(login+"-"+appId, password, rootPath, model, enableNewClients);
				info.setConnected(true);
				canal = newCanal;
				callback.connected(SocketClient.this);
			} catch (IOException | ParseException ex) {
				Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
	
	/**
	 *
	 */
	public void close() {
		try {
			reception.abort();
			socket.close();
		} catch (IOException ex) {
//			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 *
	 * @param msg
	 */
	public void send(String msg) {
		Logger.getLogger(SocketClient.class.getName()).log(Level.FINE, 
				"SEND to {0} : {1}", new Object[]{info==null?"null":info.getId(), msg});
        printWriter.println(msg+"\n");
        printWriter.flush();
	}
	
	/**
	 *
	 * @param jsonAsMap
	 */
	public void send(Map jsonAsMap) {
		send("JSON_"+JSONValue.toJSONString(jsonAsMap));
	}
	
	/**
	 *
	 * @param obj
	 */
	public void send(JSONObject obj) {
		send("JSON_"+obj.toJSONString());
	}
    
	/**
	 *
	 * @return
	 */
	public boolean getDatabase() {
		try {
			DataInputStream dis = new DataInputStream(new BufferedInputStream(inputStream));
			double fileSize = dis.readLong();
			try (FileOutputStream fos = new FileOutputStream(path)) {
				// TODO MERGE Find best. Make a benchmark (Same on Jamuz Remote)
				//https://stackoverflow.com/questions/8748960/how-do-you-decide-what-byte-size-to-use-for-inputstream-read
				byte[] buf = new byte[8192];
				int bytesRead;
				while (fileSize > 0 && (bytesRead = dis.read(buf, 0, (int) Math.min(buf.length, fileSize))) != -1) {
					fos.write(buf, 0, bytesRead);
					fileSize -= bytesRead;
				}
			}
			return true;
		} catch (IOException ex) {
			Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
	}
	
	/**
	 *
	 * @param displayedFile
	 * @param maxWidth
	 * @return
	 */
	public boolean sendCover(FileInfoInt displayedFile, int maxWidth) {
        send("SENDING_COVER");
        try {
            BufferedImage image = displayedFile.getCoverImage();
            int newWidth = image.getWidth()>maxWidth?maxWidth:image.getWidth();
            ImageIO.write(ImageUtils.toBufferedImage(displayedFile.getCoverImage()
					.getScaledInstance(newWidth, -1, java.awt.Image.SCALE_SMOOTH)),"png", outputStream);
            outputStream.flush();
			return true;
        } catch (IOException ex) {
			Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
			return false;
        }
    }
	
	/**
	 *
	 * @param fileInfoInt
	 * @return
	 */
	public boolean sendFile(FileInfoInt fileInfoInt) {
		File file = fileInfoInt.getFullPath();
		if(file.exists()&&file.isFile()) {
			send("SENDING_FILE"+fileInfoInt.toJson());
			DataOutputStream dos = new DataOutputStream(
					new BufferedOutputStream(outputStream));	
			System.out.println("Sending : "+fileInfoInt.getIdFile()
					+" "+file.getAbsolutePath());
			System.out.println("Size : "+file.length());
			sendFile(file, dos);
			return true;
		}
		return false;
    }

	private boolean sendFile(File file, DataOutputStream dos) {
		try (FileInputStream input = new FileInputStream(file)) {
			int read;
			while ((read = input.read()) != -1) {
				dos.writeByte(read);
			}
			dos.flush();
			System.out.println("File successfully sent!");
			return true;
		} catch (SocketException ex) {
			Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
			close();
			callback.disconnected(info, getClientId());
		} catch (IOException ex) {
			Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}

	/**
	 *
	 * @param input
	 * @param output
	 * @throws IOException
	 */
	public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024]; // Adjust if you want
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1)
        {
            output.write(buffer, 0, bytesRead);
        }
    }
	
	/**
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return getClientId();
	}

	/**
	 *
	 * @return
	 */
	public ClientInfo getInfo() {
		return info;
	}
	
	/**
	 *
	 * @return
	 */
	public String getClientId() {
		return info.getLogin()+"-"+(canal);
	}
}
