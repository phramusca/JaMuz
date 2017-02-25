package jamuz.remote;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raph
 */
public class Emission extends ProcessAbstract {

	private final PrintWriter printWriter;
	private final BlockingQueue<String> outQueue;
    private final OutputStream os;
	
	/**
	 *
	 * @param os
	 */
	public Emission(OutputStream os) {
		super("Thread.Common.Emission");
		this.printWriter = new PrintWriter(os);
		outQueue = new LinkedBlockingQueue<>();
        this.os = os;
	}

	/**
	 *
	 * @param msg
	 * @return
	 */
	public boolean send(String msg) {
		try {
			outQueue.put(msg);
			return true;
		} catch (InterruptedException ex) {
//			Logger.getLogger(Emission.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
	}
    
	/**
	 *
	 * @param file
	 */
	public void sendFile(String file) {
        try {
            BufferedInputStream bis;
            
            File myFile = new File (file);
          
//            byte[] mybytearray = new byte[(int) myFile.length()];
            bis = new BufferedInputStream(new FileInputStream(myFile));
//            bis.read(mybytearray, 0, mybytearray.length);
//            os.write(mybytearray, 0, mybytearray.length);
            
            byte[] mybytearray = new byte[1024];
            int count;
            while ((count = bis.read(mybytearray)) > 0)
            {
              os.write(mybytearray, 0, count);
            }
            
            os.flush();
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(Emission.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Emission.class.getName()).log(Level.SEVERE, null, ex);
        }        finally {
//          if (bis != null) bis.close();
//          if (os != null) os.close();
//          if (sock!=null) sock.close();
        }
    }
	
	@Override
	public void run() {
		try {
			String msg;
			while ((msg = outQueue.take())!=null) {
				checkAbort();
				printWriter.println(msg);
				printWriter.flush();
			}
		} catch (InterruptedException ex) {
//			Logger.getLogger(Emission.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}