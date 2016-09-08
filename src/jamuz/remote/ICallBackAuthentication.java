/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jamuz.remote;

/**
 *
 * @author phramusca
 */
public interface ICallBackAuthentication {
	public void authenticated(String login, ServerClient client);
}
