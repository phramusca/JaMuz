/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jamuz.remote;

import jamuz.FileInfoInt;
import jamuz.IconBufferCover;
import jamuz.Jamuz;
import jamuz.process.check.DialogScanner;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ServerClient {
	private final Socket socket;
	private BufferedReader bufferedReader;
	private Reception reception;
	private final ICallBackReception callback;
	private String login = "UNKNOWN";
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
	public ServerClient(Socket socket, ICallBackReception callback) {
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
//			Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
			return false;
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
//			Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 *
	 * @param msg
	 */
	public void send(String msg) {
        printWriter.println(msg+"\n");
        printWriter.flush();
	}
	
	public void send(Map jsonAsMap) {
		send("JSON_"+JSONValue.toJSONString(jsonAsMap));
	}
	
	public void send(JSONObject obj) {
		send("JSON_"+obj.toJSONString());
	}
    
	public boolean getDatabase() {
		try {
			DataInputStream dis = new DataInputStream(new BufferedInputStream(inputStream));
			double fileSize = dis.readLong();
			try (FileOutputStream fos = new FileOutputStream(path)) {
				// FIXME MERGE Find best. Make a benchmark (Same on Jamuz Remote)
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
			Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
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
            ImageIO.write(IconBufferCover.toBufferedImage(displayedFile.getCoverImage()
					.getScaledInstance(newWidth, -1, java.awt.Image.SCALE_SMOOTH)),"png", outputStream);
            outputStream.flush();
			return true;
        } catch (IOException ex) {
			Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
			return false;
        }
    }
	
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
	
	public boolean sendDatabase(String path) {
		File file = new File(path);
		if(file.exists()&&file.isFile())
		{
			send("SENDING_DB");
			DataOutputStream dos = new DataOutputStream(
					new BufferedOutputStream(outputStream));	
			System.out.println("Sending : "+file.getAbsolutePath());
			System.out.println("Size : "+file.length());
			try {
				dos.writeLong(file.length());
				return sendFile(file, dos);
			} catch (IOException ex) {
				Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return false;
    }
	
	private boolean sendFile(File file, DataOutputStream dos) {
		try (FileInputStream input = new FileInputStream(file)) {
			int read = 0;
			while ((read = input.read()) != -1) {
				dos.writeByte(read);
			}
			dos.flush();
			System.out.println("File successfully sent!");
			return true;
		} catch (SocketException ex) {
			Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
			close();
			callback.disconnected(login);
		} catch (IOException ex) {
			Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
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
	
	@Override
	public String toString() {
		return login;
	}

	class Authentication extends ProcessAbstract {

		public Authentication() {
			super("Thread.Server.Authentication");
		}

		@Override
		public void run() {
			try {
                send("MSG_ENTER_LOGIN");
                login = bufferedReader.readLine();
                send("MSG_ENTER_PWD");
                String pass = bufferedReader.readLine();
				String name = isValid(login, pass);
                if(!name.equals("")){
                    send("MSG_CONNECTED");
                    //Starting reception thread
                    reception = new Reception(bufferedReader, callback, login);
                    reception.start();
                    //Notify client is authenticated
                    callback.authenticated(new Client(login, name), ServerClient.this);
                }
                else {
                    send("MSG_ERROR_CONNECTION");
                }
			} catch (IOException ex) {
				Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		private String isValid(String login, String pass) {
			//TODO: Use a better authentication & make jamuz to multi-user somehow
			try {
				Scanner sc = new Scanner(Jamuz.getFile("RemoteClients.txt", "data"));
				while(sc.hasNext()){
					String line = sc.nextLine().trim();
					String items[] = line.split("\t");
					if(items[0].trim().equals(login) && pass.equals("tata")){
						return items[1];
					}
				 }
			} catch (FileNotFoundException ex) {	
                Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, "Le fichier \"RemoteClients.txt\" n'existe pas !");
			}
			//Not found, ask user
			String input = JOptionPane.showInputDialog(null, "Enter device pretty name for \""+login+"\"", "");  //NOI18N
			if (input != null) {
				try {
					try (Writer output = new BufferedWriter(new FileWriter(Jamuz.getFile("RemoteClients.txt", "data"), true))) {
						output.append("\n").append(login).append("\t").append(input);
						return input;
					}
				} catch (IOException ex) {
					Logger.getLogger(DialogScanner.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			//=> Not authorized then
			return "";
		}
	}

	public String getLogin() {
		return login;
	}
}
