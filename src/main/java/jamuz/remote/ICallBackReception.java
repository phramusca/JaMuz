/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jamuz.remote;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public interface ICallBackReception {

	/**
	 *
	 * @param clientId
	 * @param login
	 * @param msg
	 */
	public void received(String clientId, String login, String msg);
	
	/**
	 *
	 * @param clientInfo
	 * @param clientId
	 */
	public void disconnected(ClientInfo clientInfo, String clientId);
	
	/**
	 *
	 * @param client
	 */
	public void connected(SocketClient client);
}
