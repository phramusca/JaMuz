/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jamuz.process.merge;

import jamuz.FileInfo;
import java.util.ArrayList;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public interface ICallBackMerge {

	public void completed(ArrayList<FileInfo> errorList, 
				ArrayList<FileInfo> completedList, String popupMsg, 
				String mergeReport);
	public void refresh();
}
