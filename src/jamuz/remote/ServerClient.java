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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author phramusca
 */
public class ServerClient {
	private final Socket socket;
	private BufferedReader bufferedReader;
//	private Emission emission;
	private Reception reception;
	private final ICallBackReception callback;
	private final ICallBackAuthentication callBackAuth;
	private String login = "UNKNOWN";
    private String address; //FIXME: Use the address: do not maintain a permanent socket connection but open a new one each time (try if uses less battery)
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
    
//    public void sendFile(String filename) {
////        emission.sendFile(filename);
//        try {
//            BufferedInputStream bis;
//
//            File myFile = new File (filename);
//            bis = new BufferedInputStream(new FileInputStream(myFile));
//
//            byte[] bytes = new byte[1024];
//            int count;
//            while ((count = bis.read(bytes)) > 0)
//            {
//              outputStream.write(bytes, 0, count);
//            }
//            outputStream.flush();
//        }
//        catch (FileNotFoundException ex) {
//            Logger.getLogger(Emission.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(Emission.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
    void sendCover(FileInfoInt displayedFile) {
        send("SENDING_COVER");
        try {
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
            BufferedImage image = displayedFile.getCoverImage();
            int maxWidth=250;
            int newWidth = image.getWidth()>maxWidth?maxWidth:image.getWidth();
            ImageIO.write(IconBufferCover.toBufferedImage(displayedFile.getCoverImage().getScaledInstance(newWidth, -1, java.awt.Image.SCALE_SMOOTH)),"png", outputStream);
//            InputStream fis = new ByteArrayInputStream(os.toByteArray());
//            copyStream(fis, outputStream);
            outputStream.flush();
        } catch (IOException ex) {
            Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
            //FIXME: Retry
//            javax.imageio.IIOException: I/O error writing PNG file!
//	at com.sun.imageio.plugins.png.PNGImageWriter.write(PNGImageWriter.java:1168)
//	at javax.imageio.ImageWriter.write(ImageWriter.java:615)
//	at javax.imageio.ImageIO.doWrite(ImageIO.java:1612)
//	at javax.imageio.ImageIO.write(ImageIO.java:1578)
//	at jamuz.remote.ServerClient.sendCover(ServerClient.java:115)
//	at jamuz.remote.Server.sendCover(Server.java:129)
//	at jamuz.gui.PanelMain.sendToClients(PanelMain.java:2051)
//	at jamuz.gui.PanelMain.access$4000(PanelMain.java:90)
//	at jamuz.gui.PanelMain$CallBackAuthentication.authenticated(PanelMain.java:1917)
//	at jamuz.remote.Server$CallBackAuthentication.authenticated(Server.java:111)
//	at jamuz.remote.ServerClient$Authentication.run(ServerClient.java:160)
//Caused by: java.net.SocketException: Socket closed
//	at java.net.SocketOutputStream.socketWrite(SocketOutputStream.java:121)
//	at java.net.SocketOutputStream.write(SocketOutputStream.java:159)
//	at javax.imageio.stream.FileCacheImageOutputStream.flushBefore(FileCacheImageOutputStream.java:255)
//	at com.sun.imageio.plugins.png.ChunkStream.finish(PNGImageWriter.java:140)
//	at com.sun.imageio.plugins.png.PNGImageWriter.write_IHDR(PNGImageWriter.java:401)
//	at com.sun.imageio.plugins.png.PNGImageWriter.write(PNGImageWriter.java:1135)
//	... 10 more

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
			boolean connexion = false;
			try {
                //FIXME: Use a better authentication & think of moving jamuz to multi-user somehow
				Scanner sc = new Scanner(new File("zero.txt"));
				while(sc.hasNext()){
					if(sc.nextLine().equals(login+" "+pass)){
						connexion=true;
						break;
					}
				 }
			} catch (FileNotFoundException ex) {	
                Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, "Le fichier \"zero.txt\" n'existe pas !");
			}
			return connexion;
		}
	}
}
