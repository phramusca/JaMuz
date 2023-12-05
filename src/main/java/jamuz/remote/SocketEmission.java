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
 * @author phramusca <phramusca@gmail.com>
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