/*
 * Copyright (C) 2012 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import jamuz.utils.ProcessAbstract;
import jamuz.process.check.FolderInfo.CheckedFlag;
import jamuz.Jamuz;
import jamuz.utils.Popup;
import jamuz.utils.Inter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.commons.io.FilenameUtils;
import jamuz.gui.swing.ProgressBar;
import static jamuz.process.check.PanelCheck.enableRowSorter;
import jamuz.utils.Benchmark;
import jamuz.utils.StringManager;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Check process class
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
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
   * Location class
   */   	
    public class Location {
      private final String optionId;
      private final String value;

      /**
       * get value
       * @return
       */
      public String getValue() {
          return value;
      }

      /**
       * create a new location
       * @param optionId
       */
      public Location(String optionId) {
          this.optionId = optionId;
          this.value = Jamuz.getMachine().getOptionValue(this.optionId);
      }

      /**
       * check if location exist
       * @return
       */
      public boolean check() {
          File checked = new File(this.value);
          if(!checked.exists()) {
              Popup.warning(java.text.MessageFormat.format(Inter.get("Error.Check.SourcePathNotFound"), this.value, Inter.get("Options.Title."+this.optionId)));  //NOI18N
              return false;
          }
          return true;
      }
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
            //TODO:  Combien de threads va le plus vite => faire stats
            //Voir stats dans DoScan-Stats-Threading.ods => appliquer nouveau calcul: utiliser un Benchmark global to replace all but mean in below display
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
//        String msg="";
        if(ellapsedTime>0) {
            this.partialTimesAnalysis.add(ellapsedTime);
            int nbAnalyzed = partialTimesAnalysis.size();
            long meanTmp = (Benchmark.mean(this.partialTimesAnalysis)/1000);
            long meanEllpsedSeconds = meanTmp<= 0?1:meanTmp;
            String mean = StringManager.humanReadableSeconds(meanEllpsedSeconds);
            //TODO:  Combien de threads va le plus vite => faire stats
            //Voir stats dans DoScan-Stats-Threading.ods => appliquer nouveau calcul: utiliser un Benchmark global to replace all but mean in below display
            benchAnalysis.setSize(nbAnalyzed+analysisQueue.size());
            msgAnalysis="("+nbAnalyzed+ " x " + mean + "). " + benchAnalysis.get();
//            PanelCheck.progressBarAnalysisSize.progress(msgAnalysis, analysisQueue.size());
        }
//        else {
//            PanelCheck.progressBarAnalysisSize.setValue(analysisQueue.size());
//        }
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
//                boolean scan = (!(checkType.equals(CheckType.CHECK_NEW) || checkType.equals(CheckType.SCAN_DELETED)));
				boolean analyze=(!(checkType.equals(CheckType.SCAN_QUICK) || checkType.equals(CheckType.SCAN_FULL) || checkType.equals(CheckType.SCAN_DELETED)));
                
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
            //Verifying we have a path and not a file
            if (path.isDirectory()) {
                File[] files = path.listFiles();
                this.checkAbort();
                if (files != null) {
                    nbFilesInRootCount+=files.length;
                    if(PanelCheck.progressBarFolders.getMaximum()<nbFilesInRootCount) {
                        PanelCheck.progressBarFolders.setMaximum(nbFilesInRootCount);
                    }
                    if(files.length<=0) {
                        if(!FilenameUtils.equalsNormalizedOnSystem(rootLocation.value, path.getAbsolutePath()+File.separator)) {
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
                        FolderInfo folder = new FolderInfo(path.getAbsolutePath()+File.separator, rootLocation.value);
                        int nbFiles = folder.nbFiles;
                        //Add the list of files to the FolderInfo list
                        if(nbFiles>0) {
                            folder.setScanType(scanType);
                            scanQueue.put(folder);
                            displayScanQueue(0);
                        }
                    }
                } 
            }
        }

        private boolean scan() throws InterruptedException {
            //Get list of folders from library deleted included
            if(!Jamuz.getDb().getFolders(foldersDb, true)) {
                return false;
            }
            checkAbort();
            //Get list of folders from filesystem
            sendFoldersFSToScanQueue(new File(rootLocation.value), ScanType.SCAN);
            return true;
        }

		private boolean scanDeleted() throws InterruptedException {
			//FIXME: Issue when after a renaming (OK, OK - Warning of multiple CDs albums) 
			// two strPath in db happen to end up with same value
			// In that case, only one of each is selected, so others are not available for deletion (this run)
			// since foldersDb does not allow duplicate keys (being strPath for scan purposes)
			// => Use only one idPath when, while renaming,
			// it appears that another strPath already exists
			// and mark others (at least current one) as deleted
			
            //Get list of folders from library deleted included
            if(!Jamuz.getDb().getFolders(foldersDb, false)) {
                return false;
            }
            return sendFoldersDbToScanQueue(ScanType.SCAN_DELETED);
		}
		
        private void scanNew() throws InterruptedException {
            sendFoldersFSToScanQueue(new File(rootLocation.value), ScanType.CHECK_NEW);
        }
        
        private boolean scanDbUnchecked() throws InterruptedException {
            if(!Jamuz.getDb().getFolders(foldersDb, CheckedFlag.UNCHECKED)) {
                return false;
            }
            sendFoldersDbToScanQueue(ScanType.SCAN);
            return true;
        }

        private boolean scanFolder(int idPath) throws InterruptedException {
            if(!Jamuz.getDb().getFolder(foldersDb, idPath)) {
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
		CHECK_NEW
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
                        Jamuz.getLogger().log(Level.FINEST, "DoScan("+progressBarId+").scanDeleted({0})", folder.getRelativePath());
                        folder.scanDeleted(progressBar);
                        break;
                    case SCAN:
                        Jamuz.getLogger().log(Level.FINEST, "DoScan("+progressBarId+").scanAndBrowse({0})", folder.getRelativePath());
                        scanAndBrowse(folder, progressBar);
                        break;
                    case CHECK_NEW:
                        Jamuz.getLogger().log(Level.FINEST, "DoScan("+progressBarId+"): folderInfo.browse({0})", folder.getRelativePath());
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
            
        private boolean scanAndBrowse(FolderInfo folderFS, ProgressBar progressBar) throws InterruptedException {
            this.checkAbort();
            progressBar.reset();
            boolean scanFiles=true;
            if(foldersDb.containsKey(folderFS.getRelativePath())) {
                //Folder found in database, updating it
                FolderInfo folderDb = foldersDb.get(folderFS.getRelativePath());
                folderFS.idPath=folderDb.idPath;
                
                if(folderDb.isDeleted() || full || !folderFS.getModifDate().equals(folderDb.getModifDate())) {
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
                if(folderFS.browse(false, true, progressBar)) {
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
            try {
                this.checkAbort();
                folder.analyseMatch(0, PanelCheck.progressBarListAnalysisDequeue.get(progressBarId)); //Analyse first match
				
				//Analyse match tracks
                this.checkAbort();
                folder.analyseMatchTracks();

                //Select appropriate action
                if(folder.isValid()) { //ie: no KO result
                    if(folder.getMatches().size()>0) {
                        folder.setMbId(folder.getMatches().get(0).getId());
                    }
                    if(folder.isWarning()) {
                        folder.action=Action.WARNING;
                    }
                    else {
                        folder.action=Action.OK;
                    }
                }
                else { //At least one result is KO
                    List<ReleaseMatch> matches = folder.getMatches();
                    if(folder.getFilesAudio().size()<=0 
                            && folder.getFilesConvertible().size()<=0
                            && folder.getFilesOther().size()<=0) {
                        //Delete if it only remains images
                        folder.action=Action.DEL;
                    }
                    else if(matches==null) {
                        //This happens in case of connexion error
                        //=> Need to retry or check connexion
                        folder.action=Action.MANUAL;
                    }
                    else if(matches.size()<=0) {
                        //can be empty:
                        // - if search not performed, if artist and album are empty
                        // - if no matches found (but searched for)
                        folder.action=Action.KO;
                    }
                    else {
                        ReleaseMatch match=matches.get(0);
                        if(match.getDuplicates().size()>0) {
                            folder.action=Action.KO;
                        }
                        else if(match.getScore()<100) {
                            folder.action=Action.KO;
                        }
                        else {
							//Analyzing results
							int nbFailed=0;
							String resultFailed="";
                            for(Map.Entry<String, FolderInfoResult> entry : folder.getResults().entrySet()) {
								if(entry.getValue().isNotValid()) {
									nbFailed++;
									resultFailed = entry.getKey();
								}
							}
							if(nbFailed>1) {
								//More than one not valid => Need manual review
								folder.action=Action.MANUAL;
							}
							else {
								//There is only one item wrong. For some we can select proper action
								switch(resultFailed) {
									case "hasID3v1":
										folder.action=Action.SAVE;
										break;
									case "cover":
                                        if(folder.getFilesImage().size()>0 || folder.getCoverList().size()>0 || folder.getFirstCoverFromTags()!=null) {
                                            //There is a cover somewhere (tag, file or found) so need user to choose from
                                            //Well, folder.getCoverList() only contains a list of information. 
                                            //Covers are only searched when opening DialogCheck, but at least there is hope :)
                                            folder.action=Action.MANUAL;
                                        }
                                        else {
                                            //No image files, no covers found and no covers in tag: only a warning (as it is the only KO)
                                            folder.action=Action.WARNING;
                                        }
										break;
									case "bitRate":
										//TODO: SET KO or delete : make it an option 
                                        //as well as bitrate levels and ideally add options for other criterias as well)
										folder.action=Action.KO;
										break;
									default:
										//For all other cases, need manual review
										folder.action=Action.MANUAL;
										break;
								}
							}
							
                        }
                        //TODO: Actions Set: After action Save tags done, scan and analyze again to select new action
						//=> Problem if all DoScan have stopped meantime ...
                    }
                }
            } catch (CloneNotSupportedException ex) {
                //Should never happen since FileInfoDisplay implements Cloneable
                Jamuz.getLogger().log(Level.SEVERE, "analyseMatch()", ex); //NOI18N
            } finally {
                waitActionQueue(PanelCheck.progressBarListAnalysisDequeue.get(progressBarId));
                
                PanelCheck.tableModelActionQueue.addRow(folder);
                enableRowSorter(PanelCheck.tableModelActionQueue.getRowCount()>0);
                PanelCheck.progressBarListAnalysisDequeue.get(progressBarId).reset();
                PanelCheck.addToActionQueue(folder);
            }
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
                         
                        if(folderInfo.doAction(PanelCheck.progressActionsDequeue)) {                           
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
         * Analysis in progress (default value is not changed either auto or manually)
         */
        ANALYZING("...", 0), //NOI18N

		/**
		 *
		 */

        MANUAL("", -1),
        
        /**
         *  OK
         */
        OK(Inter.get("Check.OK"), 4), //NOI18N

        /**
         * Warning 
         */
        WARNING(Inter.get("Check.OK.Warning"), 1), //NOI18N

        /**
         * KO
         */
        KO(Inter.get("Check.KO"), 6), //NOI18N

		/**
		 *
		 */

        KO_LIBRARY(Inter.get("Check.KO.Library"), 5),        
        /**
         * Delete
         */
        DEL(Inter.get("Label.Delete"), 2), //NOI18N

        /**
         * Save tags
         */
        SAVE(Inter.get("Button.Save"), 3); //NOI18N
		
		private final String display;
        private final int order;
		private Action(String display, int order) {
			this.display = display;
            this.order = order;
		}
		@Override
		public String toString() {
			return display;
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
	 *
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
		CHECK_FOLDER
    }
}