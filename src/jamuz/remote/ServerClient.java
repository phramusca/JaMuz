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
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ServerClient {
	private final Socket socket;
	private BufferedReader bufferedReader;
//	private Emission emission;
	private Reception reception;
	private final ICallBackReception callback;
	private final ICallBackAuthentication callBackAuth;
	private String login = "UNKNOWN";
    private String address;
    private PrintWriter printWriter;
    private OutputStream outputStream;
	
	/**
	 *
	 * @param socket
	 * @param callback
	 * @param callBackAuth
	 */
	public ServerClient(Socket socket, ICallBackReception callback, ICallBackAuthentication callBackAuth) {
		this.socket = socket;
        this.address = socket.getRemoteSocketAddress().toString();
        address = address.split(":")[0].substring(1);
		this.callback = callback;
		this.callBackAuth = callBackAuth;
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
//			emission = new Emission(socket.getOutputStream());
//			emission.start();
			
			//Authenticate
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
//            emission.abort();
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
        send("SENDING_FILE"+fileInfoInt.getIdFile());
        try {
			File source = new File(FilenameUtils.concat(fileInfoInt.getRootPath(), fileInfoInt.getRelativeFullPath()));
			DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(outputStream));			
			sendFile(fileInfoInt, dos);
			return true;
        } catch (IOException ex) {
			Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
			return false;
        }
    }
	
	public void sendFile(FileInfoInt fileInfoInt, DataOutputStream dos) throws IOException {
		File file = new File(FilenameUtils.concat(fileInfoInt.getRootPath(), fileInfoInt.getRelativeFullPath()));
		if(dos!=null&&file.exists()&&file.isFile())
		{
			try (FileInputStream input = new FileInputStream(file)) {
				dos.writeLong(file.length());
				System.out.println(file.getAbsolutePath());
				int read = 0;
				while ((read = input.read()) != -1)
					dos.writeByte(read);
				dos.flush();
				System.out.println("File successfully sent!");
			}
		}
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
                    callBackAuth.authenticated(new Client(login, name), ServerClient.this);
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
}
