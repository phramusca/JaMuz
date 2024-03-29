package jamuz.remote;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import jamuz.utils.ProcessAbstract;
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
public class SocketEmission extends ProcessAbstract {

	private final PrintWriter printWriter;
	private final BlockingQueue<String> outQueue;
    private final OutputStream os;
	
	/**
	 *
	 * @param os
	 */
	public SocketEmission(OutputStream os) {
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
            bis = new BufferedInputStream(new FileInputStream(myFile));
            byte[] mybytearray = new byte[1024];
            int count;
            while ((count = bis.read(mybytearray)) > 0)
            {
              os.write(mybytearray, 0, count);
            }
            os.flush();
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(SocketEmission.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SocketEmission.class.getName()).log(Level.SEVERE, null, ex);
        }        finally {
        }
    }
	
	/**
	 *
	 */
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