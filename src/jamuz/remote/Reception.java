package jamuz.remote;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author raph
 */
public class Reception  extends ProcessAbstract {

	private final BufferedReader bufferedReader;
	private final ICallBackReception callback;
	private final String login;
	
	/**
	 *
	 * @param bufferedReader
	 * @param callback
	 * @param login
	 */
	public Reception(BufferedReader bufferedReader, ICallBackReception callback, String login){
		super("Thread.Common.Reception");
		this.callback = callback; 
		this.bufferedReader = bufferedReader;
		this.login = login;
	}
	
	@Override
	public void run() {
		try {
			while(true){
				checkAbort();
				String msg = bufferedReader.readLine();
                if(msg!=null) {
                    callback.received(login, msg);
                }
                else {
                    callback.received(login, "MSG_NULL");
                }
			}
		} catch (InterruptedException ex) {
        } catch(IOException ex) {
            callback.received(login, "MSG_ERROR: ".concat(ex.toString()));
		} finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
            }
        }
	}
}