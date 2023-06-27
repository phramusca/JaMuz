/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jamuz.soulseek;

import java.util.Map;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public interface ICallBackSlsk {
	
	public void progress(String line);

	public void addResult(TableEntrySlsk file, String progressMsg);

	public void replaceResult(TableEntrySlsk result, int row, String bench);

	public void completed();
		
	public void enableDownload(Map<String, SlskResultFolder> results);

	public void enableSearch();

	public void noFlacFound();

	public void error(String message);
}
