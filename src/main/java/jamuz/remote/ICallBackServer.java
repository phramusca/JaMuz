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
public interface ICallBackServer {

	/**
	 *
	 * @param clientId
	 * @param msg
	 */
	public void received(String clientId, String msg);
	
	/**
	 *
	 * @param clientId
	 */
	public void connectedRemote(String clientId);
	
}
