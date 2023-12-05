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
import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author phramusca <phramusca@gmail.com>
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