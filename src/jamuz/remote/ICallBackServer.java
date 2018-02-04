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
	 * @param login
	 * @param msg
	 */
	public void received(String login, String msg);
	
	public void connectedRemote(String login);
	public void disconnectedRemote(String login);
	public void connectedSync(String login);
	public void disconnectedSync(String login);
	
}
