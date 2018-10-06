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

package test.helpers;

import jamuz.Jamuz;
import jamuz.gui.swing.ProgressBar;
import jamuz.process.sync.Device;
import jamuz.process.merge.ProcessMerge;
import jamuz.process.sync.ProcessSync;
import jamuz.process.check.ProcessCheck;
import jamuz.process.merge.StatSource;
import jamuz.process.check.PanelCheck;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
//TODO: This class should be in ProcessCheck & others (or as is in code package ?)
// (which should not BE a process but include a process ...)
public class TestProcessHelper {

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
//        processCheck.doActions.join();
//		Thread.sleep(2000);
		do {
			Thread.sleep(2000);
		} while(processCheck.actionQueue.size()>0);
		processCheck.stopActions();
    }

	/**
	 *
	 * @throws InterruptedException
	 */
	public static void merge() throws InterruptedException {
        List dbIndexes = new ArrayList();
        for(StatSource statSource : Jamuz.getMachine().getStatSources()) {
			dbIndexes.add(statSource.getId());
        }
        startProcessMerge(dbIndexes, false, false);
		
        //FIXME TEST Also test simulate and forceJamuz parameters
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
    
	//Cannot use more nbScan & nbAnalysis for testing as we need to keep fixed idPath
	//TODO: Support nbScan>1 & nbAnalysis>1 to check if still OK with more threads
    private final static int NB_SCAN=1; 
	private final static int NB_ANALYSIS=1;
    private static ProcessCheck processCheck;
    private static void startProcessCheck(boolean enableDoActions, ProcessCheck.CheckType checkType, int idPath) throws InterruptedException {

        PanelCheck.enableCheck(false);
        PanelCheck.enableRowSorter(false);
        PanelCheck.stopActions(enableDoActions);
        
        processCheck = new ProcessCheck();
        
        //Starting process finally
        PanelCheck.tableModelActionQueue.clear();
		PanelCheck.setThreadPanels(checkType);
        processCheck.startCheck(checkType, idPath, NB_ANALYSIS, NB_SCAN);

		do {
			Thread.sleep(2000);
		} while(processCheck.isCheckAlive());

		
		//Note: cannot use the following as causing Concurrent modification exceptions
	
//      processCheck.doBrowse.join();
//		for(ProcessCheck.DoScan doScan : processCheck.doScanList) {
//            if(doScan!=null) {
//                doScan.join();
//            }
//        }
//		for(ProcessCheck.DoAnalyze doAnalyze : processCheck.doAnalyzeList) {
//			if(doAnalyze!=null) {
//                doAnalyze.join();
//            }
//        }
//        if(processCheck.doActions!=null) { 
//			processCheck.doActions.join(); 
//		}
    }
    
	//TODO: Now that using callbacks here, can we not launch gui ?
	public static ProcessMerge processMerge;
    private static void startProcessMerge(List<StatSource> dbIndexes, 
			boolean simulate, boolean forceJaMuz) throws InterruptedException {
        processMerge = new ProcessMerge("Thread.ProcessHelper.startProcessMerge", 
				dbIndexes, simulate, forceJaMuz, null, 
				new ProgressBar(), null);
        processMerge.start();
        processMerge.join();
    }

    private static void startProcessSync(Device device) throws InterruptedException {
        ProcessSync processSync = new ProcessSync("Thread.ProcessHelper.startProcessSync"
				, device, new ProgressBar(), null);
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
