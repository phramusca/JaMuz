/*
 * Copyright (C) 2015 phramusca ( https://github.com/phramusca/JaMuz/ )
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

package jamuz;

import jamuz.process.sync.Device;
import jamuz.process.merge.ProcessMerge;
import jamuz.process.sync.ProcessSync;
import jamuz.process.check.ProcessCheck;
import jamuz.process.merge.StatSource;
import jamuz.process.merge.PanelMerge;
import jamuz.process.check.PanelCheck;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
//TODO: This class should be in ProcessCheck & others (or as is in code package ?)
// (which should not BE a process but include a process ...)
public class ProcessHelper {

	/**
	 *
	 * @throws InterruptedException
	 */
	public static void scanNewFolder() throws InterruptedException {
        startProcessCheck(true, ProcessCheck.CheckType.CHECK_NEW, -1);
    }

	/**
	 *
	 * @throws InterruptedException
	 */
	public static void checkLibrary() throws InterruptedException {
        startProcessCheck(true, ProcessCheck.CheckType.CHECK_DB, -1);
    }
    
	/**
	 *
	 * @throws InterruptedException
	 */
	public static void scanLibraryQuick() throws InterruptedException {
        startProcessCheck(false, ProcessCheck.CheckType.SCAN_QUICK, -1);
    }
    
	/**
	 *
	 * @throws InterruptedException
	 */
	public static void applyChanges() throws InterruptedException {
        processCheck.startActions(true, true, true);
        processCheck.doActions.join();
    }

	/**
	 *
	 * @throws InterruptedException
	 */
	public static void merge() throws InterruptedException {
        List dbIndexes = new ArrayList();
        for(StatSource statSource : Jamuz.getMachine().getStatSources()) {
//            dbIndexes.add(new Integer(statSource.getId()));
			dbIndexes.add(statSource.getId());
        }
        startProcessMerge(dbIndexes, false, false);
		//FIXME TEST: Click OK button in merge results popup 
		//OR disable popup (after each merge)
		
        //FIXME TEST: Also test simulate and forceJamuz parameters
    }
    
	/**
	 *
	 * @throws InterruptedException
	 */
	public static void sync() throws InterruptedException {
        for(Device device : Jamuz.getMachine().getDevices()) {
            startProcessSync(device);
        }
    }
    private final static int nbAnalysis=4;
	//Cannot use more nbScan for testing as we need to keep fixed idPath
	//TODO: Support nbScan>1 to check if still OK with more threads
    private final static int nbScan=1; 
    private static ProcessCheck processCheck;
    private static void startProcessCheck(boolean enableDoActions, ProcessCheck.CheckType checkType, int idPath) throws InterruptedException {

        PanelCheck.enableCheck(false);
        PanelCheck.enableRowSorter(false);
        PanelCheck.stopActions(enableDoActions);
        
        processCheck = new ProcessCheck();
        
        //Starting process finally
        PanelCheck.tableModelCheck.clear();
		PanelCheck.setThreadPanels(checkType);
        processCheck.startCheck(checkType, idPath, nbAnalysis, nbScan);

        processCheck.doBrowse.join();
		//FIXME TEST Concurrent modification : why and how to fix this ?
		//Workaround: breakpoint and manually wait
		for(ProcessCheck.DoScan doScan : processCheck.doScanList) {
            if(doScan!=null) {
                doScan.join();
            }
        }
		for(ProcessCheck.DoAnalyze doAnalyze : processCheck.doAnalyzeList) {
			if(doAnalyze!=null) {
                doAnalyze.join();
            }
        }
        if(processCheck.doActions!=null) { 
			processCheck.doActions.join(); 
		}
    }
    
	/**
	 *
	 */
	public static ProcessMerge processMerge;
    private static void startProcessMerge(List<Integer> dbIndexes, boolean simulate, boolean forceJaMuz) throws InterruptedException {
        processMerge = new ProcessMerge("Thread.ProcessHelper.startProcessMerge", dbIndexes, simulate, forceJaMuz);
        PanelMerge.enableMerge(false);
        processMerge.start();
        processMerge.join();
        PanelMerge.enableMerge(true);
    }

    private static void startProcessSync(Device device) throws InterruptedException {
        ProcessSync processSync = new ProcessSync("Thread.ProcessHelper.startProcessSync", device);
        processSync.start();
        processSync.join();
    }
        
//    private void setAction (FolderInfo folderInfo, Action action) throws InterruptedException, Exception {
//        if(action.equals(Action.OK)) {
//            throw new Exception("Cannot set action to OK in test. must be done auto");
//        }
//        folderInfo.action=action;
//        PanelCheck.refreshTable();
//    }
}
