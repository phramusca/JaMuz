/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jamuz.soulseek;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public interface ICallBackSoulseek {
	
	public void progress(String line);

	public void addResult(SoulseekResult file, String progressMsg);

	public void appendResult(String path, int row);

	public void replaceResult(SoulseekResult result, int row, String bench);

	public void completed();
		
	public void enableDownload();

	public void enableSearch();

	public void noFlacFound();

	public void error(String message);
}
