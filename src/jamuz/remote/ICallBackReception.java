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
	 * @param login
	 * @param msg
	 */
	public void received(String login, String msg);
	
	/**
	 *
	 * @param clientInfo
	 */
	public void disconnected(ClientInfo clientInfo);
	
	/**
	 *
	 * @param client
	 */
	public void connected(Client client);
}
