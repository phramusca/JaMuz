/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jamuz.remote;

import jamuz.FileInfoInt;
import jamuz.IconBufferCover;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

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
	
	public ServerClient(Socket socket, ICallBackReception callback, ICallBackAuthentication callBackAuth) {
		this.socket = socket;
        this.address = socket.getRemoteSocketAddress().toString();
        address = address.split(":")[0].substring(1);
		this.callback = callback;
		this.callBackAuth = callBackAuth;
	}
	
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
	
	public void close() {
		try {
//            emission.abort();
            reception.abort();
			socket.close();
		} catch (IOException ex) {
//			Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void send(String msg) {
//		emission.send(msg);
        printWriter.println(msg+"\n");
        printWriter.flush();
	}
    
    public boolean sendCover(FileInfoInt displayedFile) {
        send("SENDING_COVER");
        try {
            BufferedImage image = displayedFile.getCoverImage();
            int maxWidth=250;
            int newWidth = image.getWidth()>maxWidth?maxWidth:image.getWidth();
            ImageIO.write(IconBufferCover.toBufferedImage(displayedFile.getCoverImage().getScaledInstance(newWidth, -1, java.awt.Image.SCALE_SMOOTH)),"png", outputStream);
            outputStream.flush();
			return true;
        } catch (IOException ex) {
			//TODO: Check if that happens often (does not seem)
			//Note: tried adding a retry sending cover
			//during tests, cover sending always returned true
			//even if cover not received in android
			//so the problem can only be solved in remote android app
			//Therefore 
            //FIXME LOW: Cover not received in JaMuz Android Remote
			Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
			return false;
        }
    }

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

                if(isValid(login, pass)){
                    send("MSG_CONNECTED");

                    //Starting reception thread
                    reception = new Reception(bufferedReader, callback, login);
                    reception.start();

                    //Notify client is authenticated
                    callBackAuth.authenticated(login, ServerClient.this);
                }
                else {
                    send("MSG_ERROR_CONNECTION");
                }
			} catch (IOException ex) {
				Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		private boolean isValid(String login, String pass) {
			
			//TODO: Use a better authentication & think of moving jamuz to multi-user somehow
			return (login.equals("tata") && pass.equals("tata"));
//			boolean connexion = false;
//			try {
//				Scanner sc = new Scanner(new File("zero.txt"));
//				while(sc.hasNext()){
//					if(sc.nextLine().equals(login+" "+pass)){
//						connexion=true;
//						break;
//					}
//				 }
//			} catch (FileNotFoundException ex) {	
//                Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, "Le fichier \"zero.txt\" n'existe pas !");
//			}
//			return connexion;
		}
	}
}
