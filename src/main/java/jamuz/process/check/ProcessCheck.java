/*
 * Copyright (C) 2012 phramusca <phramusca@gmail.com>
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

package jamuz.process.check;

//FIXME Z Use CountDownLatch to monitor the end of processes
// https://stackoverflow.com/questions/51317317/java-wait-for-timer-thread-to-complete

import jamuz.Jamuz;
import jamuz.gui.swing.ProgressBar;
import jamuz.process.check.FolderInfo.CheckedFlag;
import static jamuz.process.check.PanelCheck.enableRowSorter;
import jamuz.utils.Benchmark;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import jamuz.utils.ProcessAbstract;
import jamuz.utils.StringManager;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import org.apache.commons.io.FilenameUtils;

//FIXME Z  Pb "Vérifier" FLAC pour effacer commentaires

/**
 * Check process class
 * @author phramusca <phramusca@gmail.com>
 */
public class ProcessCheck {
	//TODO: Set Genre combobox based on ID3v1 genre IDs

    private static Location rootLocation;
    private static Location koLocation;
    private static Location manualLocation;
    private static Location destinationLocation;
    
    //TODO: Would ArrayBlockingQueue be faster ? ( http://examples.javacodegeeks.com/core-java/util/concurrent/linkedblockingqueue/java-util-concurrent-linkedblockingqueue-example/ )
    private BlockingQueue<FolderInfo> scanQueue = new LinkedBlockingQueue<>();
    private BlockingQueue<FolderInfo> analysisQueue = new LinkedBlockingQueue<>();

	/**
	 *
	 */
	public BlockingQueue<FolderInfo> actionQueue = new LinkedBlockingQueue<>();
    private static final FolderInfo LAST_FOLDER = new FolderInfo(); //TODO: Do not display in queues (internal use only)
    private ConcurrentHashMap<String,FolderInfo> foldersDb;
    
    private volatile int nbProcesses=0;

	/**
	 *
	 */
	public DoBrowse doBrowse;

	/**
	 *
	 */
	public List<DoAnalyze> doAnalyzeList=new ArrayList<>();

	/**
	 *
	 */
	public List<DoScan> doScanList=new ArrayList<>();
    
	/**
	 *
	 */
	public DoActions doActions;
    private final Object lockScan = new Object();
    private int maxActionQueueSize=30; //TODO: Make this an option (save value automatically when changed, and do the same for nbSan et nbAnalysis spinners)
	private final ICallBackCheckPanel callback;

	/**
	 *
	 * @param callback
	 */
	public ProcessCheck(ICallBackCheckPanel callback) {
		this.callback = callback;
	}

	/**
	 *
	 * @param maxActionQueueSize
	 */
	public void setMaxActionQueueSize(int maxActionQueueSize) {
        synchronized(lockScan) {
            this.maxActionQueueSize = maxActionQueueSize;
            lockScan.notifyAll();
        }
    }

	/**
	 *
	 * @return
	 */
	public int getMaxActionQueueSize() {
        synchronized(lockScan) {
            return maxActionQueueSize;
        }
    }
    
    /**
    *
    * @return
    */
    public static Location getDestinationLocation() {
        return destinationLocation;
    }

    /**
     *
     * @return
     */
    public static Location getRootLocation() {
       return rootLocation;
   }

    /**
    *
    * @return
    */
    public static Location getKoLocation() {
       return koLocation;
   }

	/**
	 *
	 * @return
	 */
	public static Location getManualLocation() {
       return manualLocation;
   }
    
    /**
	 * Starts check process:
     * Browse > Scan > Analyse > Actions
     * @param checkType
     * @param nbAnalysis
     * @param idPath
     * @param nbScan
	 */
    public void startCheck(CheckType checkType, int idPath, int nbAnalysis, int nbScan) {
        Jamuz.getLogger().log(Level.INFO, "startCheck({0},idPath={1},nbAnalysis={2},nbScan={3})", new Object[]{checkType, idPath, nbAnalysis, nbScan});
		scanQueue = new LinkedBlockingQueue<>();
        analysisQueue = new LinkedBlockingQueue<>();
        actionQueue = new LinkedBlockingQueue<>();
        displayScanQueue(0);
        displayAnalysisQueue(0);
        displayActionQueue();
        partialTimesScan = new ArrayList<>();
		partialTimesAnalysis = new ArrayList<>();
		msgScan="";
		msgAnalysis="";
        doBrowse = new DoBrowse(checkType, idPath, nbAnalysis, nbScan);
        doBrowse.start();
    }
    
	/**
	 *
	 */
	public void stopCheck() {
        doBrowse.abort();
        for(DoScan doScan : doScanList) {
            if(doScan!=null) {
                doScan.abort();
            }
        }
        for(DoAnalyze doAnalyze : doAnalyzeList) {
            if(doAnalyze!=null) {
                doAnalyze.abort();
            }
        }
    }

	/**
	 *
	 * @return
	 */
	public boolean isCheckAlive() {
        return nbProcesses>0;
    }
    
    private void enableCheckPanel() {
        if(!isCheckAlive()) {
            PanelCheck.enableCheck(true);
        }
    }
  
	/**
	 *
	 */
	public void displayActionQueue() {
        PanelCheck.progressActionsSize.setMaximum(PanelCheck.tableModelActionQueue.getRowCount());
        PanelCheck.progressActionsSize.progress("", actionQueue.size());
    }
    
    private void waitActionQueue(ProgressBar progressBar) throws InterruptedException {
        synchronized(lockScan) {
            while(isActionQueueFull()) {
                boolean isStringPainted = progressBar.isStringPainted();
                progressBar.reset();
                progressBar.setStringPainted(true);
                progressBar.setString("Waiting ...");
                lockScan.wait();
                progressBar.reset();
                progressBar.setStringPainted(isStringPainted);
            }
        }
    }
 
    private List<Long> partialTimesScan;   
	private Benchmark benchScan;
    private String msgScan="";
    private synchronized void displayScanQueue(long ellapsedTime) {
        if(scanQueue.size()>PanelCheck.progressBarScanSize.getMaximum()) {
            PanelCheck.progressBarScanSize.setMaximum(scanQueue.size());
        }
        if(ellapsedTime>0) {
            this.partialTimesScan.add(ellapsedTime);
            int nbScanned = partialTimesScan.size();
            long meanTmp = (Benchmark.mean(this.partialTimesScan)/1000);
            long meanEllpsedSeconds = meanTmp<= 0?1:meanTmp;
            String mean = StringManager.humanReadableSeconds(meanEllpsedSeconds);
            benchScan.setSize(nbScanned+scanQueue.size());
            msgScan="("+nbScanned+ " x " + mean + "). " + benchScan.get();
        }
        PanelCheck.progressBarScanSize.progress(msgScan, scanQueue.size());
    }
    
    private List<Long> partialTimesAnalysis;   
	private Benchmark benchAnalysis;
    private String msgAnalysis="";
    private synchronized void displayAnalysisQueue(long ellapsedTime) {
        if(analysisQueue.size()>PanelCheck.progressBarAnalysisSize.getMaximum()) {
            PanelCheck.progressBarAnalysisSize.setMaximum(analysisQueue.size());
        }
        if(ellapsedTime>0) {
            this.partialTimesAnalysis.add(ellapsedTime);
            int nbAnalyzed = partialTimesAnalysis.size();
            long meanTmp = (Benchmark.mean(this.partialTimesAnalysis)/1000);
            long meanEllpsedSeconds = meanTmp<= 0?1:meanTmp;
            String mean = StringManager.humanReadableSeconds(meanEllpsedSeconds);
            benchAnalysis.setSize(nbAnalyzed+analysisQueue.size());
            msgAnalysis="("+nbAnalyzed+ " x " + mean + "). " + benchAnalysis.get();
        }
         PanelCheck.progressBarAnalysisSize.progress(msgAnalysis, analysisQueue.size());
    }
    
	/**
	 *
	 * @param doKO
	 * @param doWarning
	 * @param doManual
	 */
	public void startActions(boolean doKO, boolean doWarning, boolean doManual) {
        
        //Adding back those who are no more in queue
        Iterator<FolderInfo> folders = PanelCheck.tableModelActionQueue.getFolders().iterator();
        while (folders.hasNext()) {
            FolderInfo folderInfo = folders.next();
            if(!actionQueue.contains(folderInfo)) {
                actionQueue.add(folderInfo);
            }
        }
        //Removing potential terminator signals
        while(actionQueue.contains(LAST_FOLDER)) {
            actionQueue.remove(LAST_FOLDER);
        }
        //Displaying the queue
        displayActionQueue();
        //Starting process
        doActions = new DoActions(doKO, doWarning, doManual);
        doActions.start();
    }
    
	/**
	 *
	 */
	public void stopActions() {
        if(doActions!=null) { //Happens when starting check process for the first time
            putLastFolder();
            doActions.abort();
        }
    }
	
	private void putLastFolder() {
		if(doActions!=null) { //Happens when starting check process for the first time
            try {
                //Add a "terminate" signal at queue end
                actionQueue.put(LAST_FOLDER);
            } catch (InterruptedException ex) {
                Jamuz.getLogger().log(Level.SEVERE, null, ex);
            }
        }
	}
    
	/**
	 *
	 */
	public class DoBrowse extends ProcessAbstract {
        
        private final int idPath;
        private final CheckType checkType;
        private int nbFilesInRootCount;
        private final int nbAnalysis;
        private final int nbScan;

        /**
        * Creates a new Check process instance
        * @param checkType
        * @param idPath
         * @param nbAnalysis
         * @param nbScan
        */
       public DoBrowse(CheckType checkType, int idPath, int nbAnalysis, int nbScan) {
            super("Thread.ProcessCheck.DoBrowse");
			this.checkType = checkType;
            this.idPath=idPath;
            this.nbAnalysis = nbAnalysis;
            this.nbScan = nbScan;
        }
        
		/**
		 *
		 */
		@Override
        public void run() {
            nbProcesses++;
            try {
                PanelCheck.progressBarFolders.setIndeterminate(Inter.get("Msg.Check.Scan.Setup")); //NOI18N
                
                nbFilesInRootCount=0;
                foldersDb = new ConcurrentHashMap<>();
                this.resetAbort();

                koLocation = new Location("location.ko"); //NOI18N
                manualLocation = new Location("location.manual"); //NOI18N
                switch(checkType) {
                    case CHECK_NEW:
                        //Check new
                        rootLocation = new Location("location.add"); //NOI18N
                        destinationLocation = new Location("location.ok"); //NOI18N
                        break;
					case TRANSCODE:
						rootLocation = new Location("location.library"); //NOI18N
                        destinationLocation = new Location("location.transcoded"); //NOI18N
						break;
                    default: //All other cases
                        rootLocation = new Location("location.library"); //NOI18N
                        destinationLocation = new Location("location.library"); //NOI18N
                        //Note: These options are only displayed when database is set as master,
                        // BUT not when checking from Select tab ...
                        break;
                }
                
                if(!rootLocation.check()) {
                    return;
                }

                if(!koLocation.check()) {
                    return;
                }

                if(!manualLocation.check()) {
                    return;
                }

                if(!destinationLocation.check()) {
                    return;
                }

                PanelCheck.enableAbortButton();
               
                //Start the processes
                boolean full=(!checkType.equals(CheckType.SCAN_QUICK));
				boolean analyze=(!(checkType.equals(CheckType.SCAN_QUICK) 
						|| checkType.equals(CheckType.SCAN_FULL) 
						|| checkType.equals(CheckType.SCAN_DELETED)
						|| checkType.equals(CheckType.TRANSCODE)));
                
                if(analyze) {
					partialTimesAnalysis.clear();
					benchAnalysis = new Benchmark(analysisQueue.size());
                    doAnalyzeList=new ArrayList<>();
                    DoAnalyze doAnalyze;
                    for(int i=0; i<nbAnalysis; i++) {
                        doAnalyze = new DoAnalyze(i);
                        doAnalyze.start();
                        doAnalyzeList.add(doAnalyze);
                    }
                }
                
                DoScan doScan;
				partialTimesScan.clear();
				benchScan = new Benchmark(scanQueue.size());
                doScanList=new ArrayList<>();
                for(int i=0; i<nbScan; i++) {
                    doScan = new DoScan(full, analyze, i);
                    doScan.start();
                    doScanList.add(doScan);
                }                
                
                switch(checkType) {
                    case CHECK_FOLDER:
                        //Check pathToCheck
                        scanFolder(idPath);
                        break;
                    case CHECK_NEW:
                        scanNew();
                        break;
                    case CHECK_DB:
                        scanDbUnchecked();
                        break;
                    case SCAN_QUICK:
                    case SCAN_FULL:
                        scan();
                        break;
					case SCAN_DELETED:
						scanDeleted();
						break;
					case TRANSCODE:
						transcode();
						break;
                }
            } catch (InterruptedException ex) {
            } 
            finally {
                //Add a "terminate" signal at queue end
                for (Iterator<DoScan> it = doScanList.iterator(); it.hasNext();) {
                    it.next();
                    try {
                        scanQueue.put(LAST_FOLDER);
                    } catch (InterruptedException ex) {
                    }
                }
                PanelCheck.progressBarFolders.reset();
                nbProcesses--;
                enableCheckPanel();
            }
        }
    
        private boolean sendFoldersDbToScanQueue(ScanType scanType) throws InterruptedException {
			checkAbort();
            PanelCheck.progressBarFolders.setup(foldersDb.size());
			//FIXME Z CHECK Set min selected number of Scan and Analysis to 2 (though user can decrease to 1)
			//So that big/huge folders do not block others, assuming there aren't too many
			// => Various Artists/Various Albums is 4000+ and takes far too long
			for(FolderInfo folder : foldersDb.values()) {
				checkAbort();
                folder.setScanType(scanType);
                scanQueue.put(folder);
                displayScanQueue(0);
                PanelCheck.progressBarFolders.progress("");
			}
            PanelCheck.progressBarFolders.reset();
			return true;
		}

        private void sendFoldersFSToScanQueue(File path, ScanType scanType) throws InterruptedException {
            if (path.isDirectory()) {
                File[] files = path.listFiles();
                this.checkAbort();
                if (files != null) {
                    nbFilesInRootCount+=files.length;
                    if(PanelCheck.progressBarFolders.getMaximum()<nbFilesInRootCount) {
                        PanelCheck.progressBarFolders.setMaximum(nbFilesInRootCount);
                    }
                    if(files.length<=0) {
                        if(!FilenameUtils.equalsNormalizedOnSystem(rootLocation.getValue(), path.getAbsolutePath()+File.separator)) {
                            Jamuz.getLogger().log(Level.FINE, "Deleted empty folder \"{0}\"", path.getAbsolutePath());  //NOI18N
                            path.delete();
                        }
                    }
                    else {
                        for (File myFile : files) {
                            this.checkAbort();
                            if (myFile.isDirectory()) {
                                sendFoldersFSToScanQueue(myFile, scanType);
                            }
							PanelCheck.progressBarFolders.progress("");
                        }
                        FolderInfo folder = new FolderInfo(path.getAbsolutePath()+File.separator, rootLocation.getValue());
                        if(folder.nbFiles>0) {
                            folder.setScanType(scanType);
                            scanQueue.put(folder);
                            displayScanQueue(0);
                        }
                    }
                } 
            }
        }

        private boolean scan() throws InterruptedException {
            //Get list of folders from library
            if(!Jamuz.getDb().path().get(foldersDb)) {
                return false;
            }
            checkAbort();
            //Get list of folders from filesystem
            sendFoldersFSToScanQueue(new File(rootLocation.getValue()), ScanType.SCAN);
            return true;
        }

		private boolean scanDeleted() throws InterruptedException {
            //Get list of folders from library
            if(!Jamuz.getDb().path().get(foldersDb)) {
                return false;
            }
            return sendFoldersDbToScanQueue(ScanType.SCAN_DELETED);
		}
		
		private boolean transcode() throws InterruptedException {
            //Get list of folders from library
            if(!Jamuz.getDb().path().get(foldersDb)) {
                return false;
            }
            return sendFoldersDbToScanQueue(ScanType.TRANSCODE);
		}
		
        private void scanNew() throws InterruptedException {
            sendFoldersFSToScanQueue(new File(rootLocation.getValue()), ScanType.CHECK_NEW);
        }
        
        private boolean scanDbUnchecked() throws InterruptedException {
            if(!Jamuz.getDb().path().get(foldersDb, CheckedFlag.UNCHECKED)) {
                return false;
            }
            sendFoldersDbToScanQueue(ScanType.SCAN);
            return true;
        }

        private boolean scanFolder(int idPath) throws InterruptedException {
            if(!Jamuz.getDb().path().get(foldersDb, idPath)) {
                return false;
            }
            sendFoldersDbToScanQueue(ScanType.SCAN);
            return true;
        }
    }

	/**
	 *
	 */
	public enum ScanType {

		/**
		 *
		 */
		SCAN,

		/**
		 *
		 */
		SCAN_DELETED,

		/**
		 *
		 */
		CHECK_NEW,
		
		/**
		 *
		 */
		TRANSCODE
    }
    
	/**
	 *
	 */
	public class DoScan extends ProcessAbstract {

        private final boolean full;
        private final boolean analyze;
        private final int progressBarId;
        
        /**
         *
         * @param full
         * @param analyze
         * @param progressBarId
         */
        public DoScan(boolean full, boolean analyze, int progressBarId) {
            super("Thread.ProcessCheck.DoScan.#"+progressBarId);
            this.full = full;
            this.analyze = analyze;
            this.progressBarId = progressBarId;
        }
        
		/**
		 *
		 */
		@Override
        public void run() {
            try {
				Jamuz.getLogger().log(Level.FINEST, "DoScan({0}).run()", progressBarId);
                nbProcesses++;
                dequeue();
                
            } catch (InterruptedException ex) {
            } 
            finally {
				Jamuz.getLogger().log(Level.FINEST, "DoScan({0}).STOP()", progressBarId);
                if(analyze) {
                    doScanList.remove(this);
                    if(doScanList.size()<1) {
                        for (Iterator<DoAnalyze> it = doAnalyzeList.iterator(); it.hasNext();) {
                            it.next();
                            try {
                                Jamuz.getLogger().log(Level.FINEST, "DoScan({0}): analysisQueue.put(lastFolder)", progressBarId);
                                analysisQueue.put(LAST_FOLDER);
                            } catch (InterruptedException ex) {
                            }
                        }
                    }
                }
                nbProcesses--;
                PanelCheck.progressBarListScanDequeue.get(progressBarId).reset();
                displayScanQueue(0);
                enableCheckPanel();
            }
        }

        private void dequeue() throws InterruptedException {
            FolderInfo folder;
            ProgressBar progressBar=PanelCheck.progressBarListScanDequeue.get(progressBarId);
            while (!(folder = scanQueue.take()).isLast) {
                Jamuz.getLogger().log(Level.FINEST, "DoScan("+progressBarId+").dequeue(): {0}", folder.getRelativePath());
                checkAbort();
                long startTime=System.currentTimeMillis();
                if(!analyze) { //ie: Scan library (quick or full)
                    //We do not want it when scanning, too slow
                    folder.setIsReplayGainDone(true);
                }
                switch(folder.getScanType()) {
                    case SCAN_DELETED:
                        Jamuz.getLogger().log(Level.FINEST, "DoScan("+progressBarId+"): folder.scanDeleted({0})", folder.getRelativePath());
                        folder.scanDeleted(progressBar);
                        break;
					case TRANSCODE:
						Jamuz.getLogger().log(Level.FINEST, "DoScan("+progressBarId+"): folder.transcodeAsNeeded({0})", folder.getRelativePath());
                        folder.transcodeAsNeeded(progressBar);
                        break;
                    case SCAN:
                        Jamuz.getLogger().log(Level.FINEST, "DoScan("+progressBarId+"): scanAndBrowse({0})", folder.getRelativePath());
                        scanAndBrowse(analyze, folder, progressBar);
                        break;
                    case CHECK_NEW:
                        Jamuz.getLogger().log(Level.FINEST, "DoScan("+progressBarId+"): folder.browse({0})", folder.getRelativePath());
                        folder.browse(true, true, progressBar);
                        break;
                }
                waitActionQueue(progressBar);
                if(analyze) {
					Jamuz.getLogger().log(Level.FINEST, "DoScan("+progressBarId+"): analysisQueue.put({0})", folder.getRelativePath());
                    analysisQueue.put(folder);
                    displayAnalysisQueue(0);
                }
                displayScanQueue(System.currentTimeMillis()-startTime);
           }
        }
            
        private boolean scanAndBrowse(boolean recalculateGain, FolderInfo folderFS, ProgressBar progressBar) throws InterruptedException {
            this.checkAbort();
            progressBar.reset();
            boolean scanFiles=true;
            if(foldersDb.containsKey(folderFS.getRelativePath())) {
                //Folder found in database, updating it
                FolderInfo folderDb = foldersDb.get(folderFS.getRelativePath());
                folderFS.idPath=folderDb.idPath;
                
                if(full || !folderFS.getModifDate().equals(folderDb.getModifDate())) {
                    CheckedFlag checkedFlagDb=folderDb.getCheckedFlag();
                    if(!folderFS.getModifDate().equals(folderDb.getModifDate())) {
                        checkedFlagDb=CheckedFlag.UNCHECKED;
                    }
                    if(!folderFS.updateInDb(checkedFlagDb)) {
                        return false;
                    }
                }
                else {
                    //We will not scan inside path IF not a full scan OR folder has not been modified (quick scan mode)
                    scanFiles=false;
                }
            }
            else {
                //Insert folder in database
                if(!folderFS.insertInDb(CheckedFlag.UNCHECKED)) {
                    return false;
                }
            }
            //Scan and update folder's files
            if(scanFiles) {
                //Get list of files from filesystem, reading tags as will be displayed
                if(folderFS.browse(recalculateGain, true, progressBar)) {
                    if(!folderFS.scan(true, progressBar)) {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
            progressBar.reset();
            return true;
        }
    }

	/**
	 *
	 */
	public class DoAnalyze extends ProcessAbstract {
        private final int progressBarId;
        
		/**
		 *
		 * @param progressBarId
		 */
		public DoAnalyze(int progressBarId) {
            super("Thread.ProcessCheck.DoAnalyze.#"+progressBarId);
            this.progressBarId = progressBarId;
        }
        
		/**
		 *
		 */
		@Override
        public void run() {
            try {
				Jamuz.getLogger().log(Level.FINEST, "DoAnalyze({0}).run()", progressBarId);
                nbProcesses++;
                dequeue();
            } catch (InterruptedException ex) {
            }
            finally {
				Jamuz.getLogger().log(Level.FINEST, "DoAnalyze({0}).STOP", progressBarId);
                nbProcesses--;
                PanelCheck.progressBarListAnalysisDequeue.get(progressBarId).reset();
                displayAnalysisQueue(0);
                enableCheckPanel();
            }
        }
        
        private void dequeue() throws InterruptedException {
            FolderInfo folderInfo;
            while (!(folderInfo = analysisQueue.take()).isLast) { 
				Jamuz.getLogger().log(Level.FINEST, "DoAnalyze("+progressBarId+").dequeue(): {0}", folderInfo.getRelativePath());
                checkAbort();
				long startTime=System.currentTimeMillis();
                analyse(folderInfo);
                displayAnalysisQueue(System.currentTimeMillis()-startTime);
           }
        }
        
        private void analyse(FolderInfo folder) throws InterruptedException {

            this.checkAbort();
            try {
                folder.analyse(PanelCheck.progressBarListAnalysisDequeue.get(progressBarId));
            } catch (CloneNotSupportedException ex) {
                Popup.error(ex);
            }
            folder.action=Action.ANALYZING;
            this.checkAbort();
			folder.analyseMatch(0, PanelCheck.progressBarListAnalysisDequeue.get(progressBarId)); //Analyse first match
			this.checkAbort();
			folder.analyseMatchTracks();
			folder.setAction();            
			waitActionQueue(PanelCheck.progressBarListAnalysisDequeue.get(progressBarId));
			PanelCheck.tableModelActionQueue.addRow(folder);
			enableRowSorter(PanelCheck.tableModelActionQueue.getRowCount()>0);
			PanelCheck.progressBarListAnalysisDequeue.get(progressBarId).reset();
			callback.addToQueueAction(folder);
        }
    }
    
    private boolean isActionQueueFull() {
        synchronized(lockScan) {
            return((PanelCheck.tableModelActionQueue.getRowCount()) >= maxActionQueueSize);
        }
    }
    
	/**
	 *
	 */
	public class DoActions extends ProcessAbstract {

        private final boolean doKO; 
        private final boolean doWarning; 
        private final boolean doManual;

		/**
		 *
		 * @param doKO
		 * @param doWarning
		 * @param doManual
		 */
		public DoActions(boolean doKO, boolean doWarning, boolean doManual) {
            super("Thread.ProcessCheck.DoActions");
            this.doKO = doKO;
            this.doWarning = doWarning;
            this.doManual = doManual;
        }
                
		/**
		 *
		 */
		@Override
        public void run() {
            try {
				Jamuz.getLogger().log(Level.FINEST, "DoActions.run(): doKO={0},doWarning={1},nbScan={2})", new Object[]{doKO, doWarning, doManual});
                nbProcesses++;
                dequeue(doKO, doWarning, doManual);
            } catch (InterruptedException ex) {
            }
            finally {
                synchronized(lockScan) {
                    lockScan.notifyAll(); //Releasing lock on scan if any
                }
				Jamuz.getLogger().log(Level.FINEST, "DoActions.STOP");
                nbProcesses--;
                enableCheckPanel();
            }
        }

        private void dequeue(boolean doKO, boolean doWarning, boolean doManual) throws InterruptedException {
            FolderInfo folderInfo;
            while (!(folderInfo = actionQueue.take()).isLast) {

                synchronized(lockScan) {
                    if(!isActionQueueFull()) {
                        lockScan.notifyAll();
                    }
                }
                
                checkAbort();
                    if(!(        (folderInfo.action.equals(Action.KO) && !doKO)
                            || (folderInfo.action.equals(Action.WARNING) && !doWarning)
                            || (folderInfo.action.equals(Action.MANUAL) && !doManual)    )) {
                         
                        if(folderInfo.doAction(PanelCheck.progressActionsDequeue).isPerformed) {                           
                            enableRowSorter(PanelCheck.tableModelActionQueue.getRowCount()>1);
                            PanelCheck.tableModelActionQueue.removeRow(folderInfo);
                            PanelCheck.tableModelActionQueue.fireTableDataChanged();
                        } 
                    }
                displayActionQueue();
           }
        }
    }

    /**
     * Action (move to OK, Delete, save, ...)
     */
    public enum Action {
		/**
		 * Manual
		 */
        MANUAL("", 0, "document_todo.png", Color.WHITE),
        /**
         * Warning 
         */
        WARNING(Inter.get("Check.OK.Warning"), 1, "accept.png", Color.ORANGE), //NOI18N
		/**
         * KO
         */
        KO(Inter.get("Check.KO"), 2, "cancel.png", Color.RED),
		/**
         * Save tags
         */
        SAVE(Inter.get("Button.Save"), 3, "application_form_edit.png", new Color(0,153,143)), 
        /**
         *  OK
         */
        OK(Inter.get("Check.OK"), 4, "accept.png", new Color(0, 128, 0)), //NOI18N
		/**
         * Delete
         */
        DEL(Inter.get("Label.Delete"), 5, "bin.png", Color.DARK_GRAY), //NOI18N
		/**
		 * KO in library
		 */
        KO_LIBRARY(Inter.get("Check.KO"), 6, "document_insert.png", Color.RED),        
		/**
		 * Warning in library
		 */
		WARNING_LIBRARY(Inter.get("Check.OK.Warning"), 7, "document_insert.png", Color.ORANGE),
		/**
         * Analysis in progress (default value is not changed either auto or manually)
         */
        ANALYZING("...", 8, "search_plus.png", Color.LIGHT_GRAY); //NOI18N
		
		private final String display;
        private final int order;
		private final String res;
		private final Color color;
		private Action(String display, int order, String res, Color color) {
			this.display = display;
            this.order = order;
			this.res = res;
			this.color = color;
		}

		/**
		 *
		 * @return
		 */
		@Override
		public String toString() {
			return display;
		}

		/**
		 *
		 * @return
		 */
		public String getRes() {
			return res;
		}

		/**
		 *
		 * @return
		 */
		public Color getColor() {
			return color;
		}

        /**
         *
         * @return
         */
        public int getOrder() {
            return order;
        }

	};
	
	/**
	 * Type of check (Scan quick, Check new,...)
	 */
	public enum CheckType {

		/**
		 *
		 */
		SCAN_QUICK,

		/**
		 *
		 */
		SCAN_FULL,

		/**
		 *
		 */
		SCAN_DELETED,

		/**
		 *
		 */
		CHECK_NEW,

		/**
		 *
		 */
		CHECK_DB,

		/**
		 *
		 */
		CHECK_FOLDER,

		/**
		 *
		 */
		TRANSCODE
    }
}