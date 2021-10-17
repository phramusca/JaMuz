/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jamuz.process.sync;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public interface ICallBackSync {

	/**
	 *
	 */
	public void refresh();

	/**
	 *
	 */
	public void enable();

	/**
	 *
	 * @param enable
	 */
	public void enableButton(boolean enable);

	/**
	 *
	 * @param file
	 * @param idIcon
	 */
	public void addRow(String file, int idIcon);

	/**
	 *
	 * @param file
	 * @param msg
	 */
	public void addRow(String file, String msg);
}
