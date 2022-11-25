package jamuz.remote;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import jamuz.utils.ProcessAbstract;
import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author raph
 */
public class SocketReception  extends ProcessAbstract {

	private final BufferedReader bufferedReader;
	private final ICallBackReception callback;
	private final SocketClient client;
	
	/**
	 *
	 * @param bufferedReader
	 * @param callback
	 * @param client
	 */
	public SocketReception(BufferedReader bufferedReader, ICallBackReception callback, SocketClient client){
		super("Thread.Common.Reception");
		this.callback = callback; 
		this.bufferedReader = bufferedReader;
		this.client = client;
	}
	
	/**
	 *
	 */
	@Override
	public void run() {
		try {
			while(true){
				checkAbort();
				String msg = bufferedReader.readLine();
                if(msg!=null) {
                    callback.received(client.getClientId(), client.getInfo().getLogin(), msg);
                }
                else {
                    callback.disconnected(client.getInfo(), client.getClientId());
					return;
                }
			}
		} catch (InterruptedException ex) {
        } catch(IOException ex) {
            callback.received(client.getClientId(), client.getInfo().getLogin(), 
					"MSG_ERROR: ".concat(ex.toString()));
		} finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
            }
        }
	}
}