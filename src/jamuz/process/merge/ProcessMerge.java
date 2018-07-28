/*
 * Copyright (C) 2011 phramusca ( https://github.com/phramusca/JaMuz/ )
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

//TODO: Merge Playlists:
//					|	Static							|	Dynamic							|	Export				|
//	MediaMonkey		| Db playlists & playlistsongs		| Db: playlists [QueryData: INI]	| M3U or XSPF			|
//	Guayadeque		| Db: playlists & plsets			| Bd: idem							| M3U, PLS, ASX, XSPF	|
//	Kodi			| File M3U in userdata/playlists	| File XSP (XBOX Smart Playlist)	| None					|
//	Mixxx			| Db: playlist & crates				| Not yet							| None					|


package jamuz.process.merge;

import jamuz.utils.LogText;
import jamuz.StatSourceAbstract;
import jamuz.DbConnJaMuz;
import jamuz.DbInfo;
import jamuz.FileInfo;
import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.gui.swing.ProgressBar;
import jamuz.remote.PanelRemote;
import jamuz.utils.ProcessAbstract;
import jamuz.utils.Popup;
import jamuz.utils.Inter;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Level;
import jamuz.utils.DateTime;
import jamuz.utils.Utils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ProcessMerge extends ProcessAbstract {
	
	//Process information (retrieved from caller)
	private final List<StatSource> sources;
	private final boolean simulate;
	private final boolean forceJaMuz; //If true force statistics from JaMuz db to apply on selected databases
	
	//Selected DB information
	private StatSource selectedStatSource; 
	private DbConnJaMuz dBJaMuz; //Can be a copy if simulation mode
	private ArrayList<FileInfo> statsListDbSelected;
	private final boolean isRemote;
	private ArrayList<FileInfo> mergeListDbSelected;
	
	//JaMuz DB information
	private ArrayList<FileInfo> statsListDbJaMuz;
	private ArrayList<FileInfo> mergeListDbJaMuz;
    private ArrayList<FileInfoInt> mergeListDbJaMuzFileTags;
    
	//LOG files and report
	private boolean doLogText = false;
	private String logSubPath;
	private LogText logDbSelected;
	private LogText logDbJaMuz;
	private LogText logDbNew;
	private String mergeReport="";  //NOI18N
	private int nbFilesNotFoundSelected=0;
	private int nbFilesNotFoundJaMuz=0;
	private int nbFilesErrorSelected=0;
	private int nbFilesErrorJaMuz=0;
	private ArrayList<FileInfo> errorList;
	protected ProgressBar progressBar;
	private ArrayList<FileInfo> completedList;
	private int nbSteps;
	private final ICallBackMerge callback;
	
	/**
	 * Creates a new merge process instance
     * @param name
	 * @param sources 
	 * @param forceJaMuz
	 * @param simulate  
	 * @param files  
	 * @param progressBar  
	 * @param callback  
	 */
	public ProcessMerge(String name, List<StatSource> sources, 
			boolean simulate, boolean forceJaMuz, ArrayList<FileInfo> files,
			ProgressBar progressBar, ICallBackMerge callback) {
        super(name);
		this.sources = sources;
		this.simulate = simulate;
		this.forceJaMuz = forceJaMuz;
		
		//We only log to text files in "debug" mode, at least FINE level
		if(Jamuz.getLogger().getLevel().intValue()<=Level.FINE.intValue()) {
			doLogText=true;
		}
		statsListDbSelected = files;
		isRemote=files!=null;
		this.progressBar = progressBar;
		this.callback = callback;
	}
	
	/**
	 * Starts merge statistics process in a new thread
	 */
    @Override
	public void run() {
        String popupMsg=Inter.get("Msg.MergeComplete");  //NOI18N
        try {
            resetAbort();
            if(sources.size()<=0) {
                Popup.info("You must select at least one source.");
                popupMsg="";
                return;
            }
            mergeReport="";  //NOI18N
            errorList = new ArrayList<>();
            completedList = new ArrayList<>();
            logSubPath = Jamuz.getLogPath() + DateTime.getCurrentLocal(DateTime.DateTimeFormat.FILE) 
					+ "--StatsMerge" + "--" + Jamuz.getMachine().getName() 
					+ File.separator;  
            //TODO: Test and check potential errors while creating (refer to Main.createLog())
            File f = new File(logSubPath);
            f.mkdir();
            if(!mergeMain()) {
				//A popup will already display error in catch clauses below
				//Don't want to popup merge results too, which could be wrong anyway
                popupMsg=""; 
            }
        } catch (InterruptedException ex) {
            popupMsg=Inter.get("Msg.MergeAborted");  //NOI18N
        } catch (CloneNotSupportedException ex) {
            popupMsg="Clone not supported. Should never happen!"; //NOI18N
        }
        finally {
            progressBar.reset();
			progressBar.setString(
					DateTime.getCurrentLocal(DateTime.DateTimeFormat.HUMAN)
					+" | "+popupMsg+" "+completedList.size()
					+" change(s). " + errorList.size() + " error(s).");
			callback.completed(errorList, completedList, popupMsg, mergeReport);
        }
	}

	private boolean mergeMain() throws InterruptedException, CloneNotSupportedException {
		
		//Set number of steps
		getAndSetNbSteps();
		if(isRemote) {
			nbSteps=10;
		}
		progressBar.setup(nbSteps);
        
		checkAbort();
		
		// TODO: do not popup an error when a source is not found, simply disable and
		// warn user only at the end (some sources will be merged if so)
		
		//Check all selected databases
		for (StatSource statSource : sources) {
			progressBar.progress(MessageFormat.format(Inter.get("Msg.Merge.Checking"), 
					statSource.getSource().getName())); //NOI18N
            callback.refresh();
			if(!statSource.getSource().check()) {
				return false;
			}
			checkAbort();
		}
		
		//Get and backup all selected databases
		if(!isRemote) {
			for (StatSource statSource : sources) {
				checkAbort();
				//Retrieve database
				if(!copyDB(statSource.getSource(), true)) {
					return false;
				}
				checkAbort();
				//Backup database
				if(!simulate) {
					if(!backupDB(statSource.getSource())) {
						return false;
					}
				}
			}
		}
		
		//Backup JaMuz database
		checkAbort();
		//Copy database
		if(!copyDB(Jamuz.getDb(), true)) {
			return false;
		}
		//We will use that copy if simulation mode, otherwise use real JamuzDb
		if(simulate) {
            dBJaMuz = new DbConnJaMuz(
					new DbInfo(DbInfo.LibType.Sqlite, 
							Jamuz.getDb().getDbConn().getInfo().getLocationWork(), "", ""));
			//Connect and create Prepared Statements
			if(!dBJaMuz.setUp()) {
				return false;
			}
		}
		else {
			dBJaMuz = Jamuz.getDb();
		}
		
		//Scan and merge all selected databases
        int i=1;
		for (StatSource statSource : sources) {
			selectedStatSource = statSource;
			if(!scanAndMerge("1-"+i)) {  //NOI18N
				return false;
			}
            i++;
		}
		
		//Reverse the selected database list
		Collections.reverse(sources);
		//Remove the first item (which used to be the last before reverse)
		//Keep original for the databases copy back !
		List<StatSource> sourcesReverse = new ArrayList<>(sources);
		sourcesReverse.remove(0);
		checkAbort();
		
		//Scan and merge all selected databases again (without the last one) 
		//  BUT in REVERSE ORDER
		//in order to apply possible modifications retrieved from previously merged databases
        i=1;
        //TODO: No need to do reverse way if no changes made on first run ...
		for (StatSource statSource : sourcesReverse) {
			selectedStatSource = statSource;
			if(!scanAndMerge("2-"+i)) {  //NOI18N
				return false;
			}
            i++;
		}

		//TODO: offer the choice to user: "Apply changes ?"
		//Copy back databases back 
		if(!simulate) {
			if(isRemote) {
				PanelRemote.send(selectedStatSource.getMachineName()+"-sync",
						mergeListDbSelected);
			} else {
				for (StatSource statSource : sources) {
					checkAbort();
					if(!copyDB(statSource.getSource(), false)) {
						return false;
					}
				}
			}
		}
        
		//Too late for a potential abort: job has been done !
		return true;
	}
	
	private boolean scanAndMerge(String run) throws InterruptedException, CloneNotSupportedException {

		nbFilesNotFoundSelected=0;
		nbFilesNotFoundJaMuz=0;
		nbFilesErrorSelected=0;
		nbFilesErrorJaMuz=0;
		
		//Create text LOG files
		if(doLogText) {
            progressBar.progress(Inter.get("Msg.Merge.CreateLogText")); //NOI18N
			callback.refresh();
			if(!createTextLogs(run)) {
				return false;
			}
			checkAbort();
		}
		else {
			nbSteps-=1;
		}
        
		if(!isRemote) {
			//Connect selected database
			progressBar.progress(MessageFormat.format(Inter.get("Msg.Merge.ConnectingDB"), selectedStatSource.getSource().getName())); //NOI18N
			callback.refresh();
			if(!selectedStatSource.getSource().setUp()) {
				return false;
			}
			checkAbort();

			//Get statistics selected from database
			progressBar.progress(MessageFormat.format(Inter.get("Msg.Merge.GettingStats"), selectedStatSource.getSource().getName())); //NOI18N
			callback.refresh();
			statsListDbSelected = new ArrayList<>();
			if(!selectedStatSource.getSource().getStatistics(statsListDbSelected)) {
				return false;
			}
		}
		
		checkAbort();
		
		//Get statistics from JaMuz database
		progressBar.progress(MessageFormat.format(Inter.get("Msg.Merge.GettingStats"), dBJaMuz.getName())); //NOI18N
        callback.refresh();
		statsListDbJaMuz = new ArrayList<>();
		if(!dBJaMuz.getStatistics(statsListDbJaMuz, selectedStatSource)) {
			return false;
		}
		checkAbort();
		
		//Compare both statistics lists

		compareLists(run); //Several checkAbort() inside
		
		//Merge
		if(!merge(run)) {
			return false;
		}
		checkAbort();

		progressBar.progress(MessageFormat.format(Inter.get("Msg.Merge.ClosingConnectionDB"), selectedStatSource.getSource().getName())); //NOI18N
        callback.refresh();
		if(!isRemote) {
			selectedStatSource.getSource().tearDown();
			checkAbort();
		}

		mergeReport+= "<tr>"
            + "<td>"+selectedStatSource.getSource().getName()+"</td>"
            + "<td align=center style=\"border-right:1px solid black;border-bottom:1px solid black;border-left:1px solid black\">";    //NOI18N 
        String msgSelected="";
        msgSelected+=displayStatus((mergeListDbSelected.size()-nbFilesErrorSelected), Inter.get("Label.Updated"));
        msgSelected+=displayStatus(nbFilesNotFoundJaMuz, Inter.get("Label.NotFound"));//Yes, inverted, this is OK
        msgSelected+=displayStatus(nbFilesErrorSelected, Inter.get("Label.Errors"));
        mergeReport+=msgSelected.equals("")?" --- ":msgSelected;
        
        mergeReport+="</td><td align=center style=\"border-right:1px solid black;border-bottom:1px solid black\">";  //NOI18N
        String msgJaMuz="";
        msgJaMuz+=displayStatus((mergeListDbJaMuz.size()-nbFilesErrorJaMuz), Inter.get("Label.Updated"));
        msgJaMuz+=displayStatus(nbFilesNotFoundSelected, Inter.get("Label.NotFound"));//Yes, inverted, this is OK
        msgJaMuz+=displayStatus(nbFilesErrorJaMuz, Inter.get("Label.Updated"));
        mergeReport+=msgJaMuz.equals("")?" --- ":msgJaMuz;
        msgJaMuz+="</td></tr>";  //NOI18N

		return true;
	}

    private String displayStatus(int value, String msg) {
        if(value>0) {
            return value+" "+msg+". ";
        }
        else {
            return "";
        }
    }
	
	private void compareLists(String run) throws InterruptedException, CloneNotSupportedException {

		mergeListDbSelected = new ArrayList<>();
		mergeListDbJaMuz = new ArrayList<>();
        mergeListDbJaMuzFileTags=new ArrayList<>();
                
		FileInfo fileDbSelected; 
		FileInfo fileInfoDbJaMuz;

		progressBar.progress(
				MessageFormat.format(Inter.get("Msg.Merge.ComparingStats"), 
				selectedStatSource.getSource().getName(), 
				dBJaMuz.getName())); 
        callback.refresh();
		for (FileInfo file : statsListDbSelected) {
            //Checking if process got interrupted
            checkAbort();
            fileDbSelected = file;
            //Convert path (potentially in Windows style) to JaMuz linux style
            //No more needed as paths are directly converted to System when read from database
//			String relativeFullPath = FilenameUtils.separatorsToUnix(myFileInfoDbSelected.relativeFullPath);
            
            //Get item from JaMuz DB
            int idSecond = searchInStatsListDbJaMuz(fileDbSelected.getRelativeFullPath());
            if(idSecond>=0) {
                fileInfoDbJaMuz = statsListDbJaMuz.get(idSecond);
                compareStats(run, fileDbSelected,fileInfoDbJaMuz);
                //Removing so that next searches are even faster
                statsListDbJaMuz.remove(idSecond);
            }
            else {
                Jamuz.getLogger().warning(MessageFormat.format(
						Inter.get("Error.Merge.RetrieveInfo"), 
						new Object[] {fileDbSelected.getRelativeFullPath(), 
							dBJaMuz.getName()
						})); 
                //Add to ErrorList
//				myFileInfoDbSelected.sourceName=MessageFormat.format(Inter.get("Error.Merge.NotFound"), 
//new Object[] {dBJaMuz.name, selectedStatSource.source.name});  //NOI18N
                fileDbSelected.setSourceName(run+"-"+dBJaMuz.getName()
						+":\t"+Inter.get("Label.NotFound"));
                errorList.add((FileInfo) fileDbSelected.clone());
                if(fileDbSelected.getRating()>0) {
                    if(doLogText) {
                        addToLog(fileDbSelected.getRating()+","
								+fileDbSelected.getFormattedLastPlayed()+","
								+fileDbSelected.getRelativeFullPath(), ""); //NOI18N
                    }
                }
				nbFilesNotFoundSelected+=1;
			}
        }
		
		//Add remaining from Merge.statsListDbJaMuz to ErrorList
		progressBar.progress(MessageFormat.format(
				Inter.get("Msg.Merge.SetRemainingNotFound"), 
				dBJaMuz.getName())); //NOI18N
        callback.refresh();
		for (FileInfo file : statsListDbJaMuz) {
            fileInfoDbJaMuz = file;
            //Add to ErrorList
            fileInfoDbJaMuz.setSourceName(run+"-"
					+selectedStatSource.getSource().getName()
					+":\t"+Inter.get("Label.NotFound"));
            errorList.add((FileInfo) fileInfoDbJaMuz.clone());
            if(fileInfoDbJaMuz.getRating()>0) {
                if(doLogText) {
                    addToLog("", fileInfoDbJaMuz.getRating()+","
							+fileInfoDbJaMuz.getFormattedLastPlayed()+","
							+fileInfoDbJaMuz.getRelativeFullPath()); 
                }
            }
            nbFilesNotFoundJaMuz+=1;
        }
	}
	
	private void compareStats(String run, FileInfo fileSelectedDb, 
			FileInfo fileJaMuz) 
			throws InterruptedException, CloneNotSupportedException {
		//Checking if process got interrupted
		checkAbort();

		//New is by default the one from JaMuz
		FileInfo fileNew=(FileInfo) fileJaMuz.clone();
		fileNew.setSourceName("new"); //This will be overwritten anyway  //NOI18N
		boolean isOneDbUpdated=false;
		
        //TODO: Replace "force Jamuz" option by a "Select action" option so that user can 
        //select what direction copy info to (1>2; 2>1; and none)
        //=> Allow only one statsource so that below buttons are effective
        //=> 2 buttons (hidden by default, if option not selected) above each statistic ("rating", "Last Played" ...) column:
        //      - "1>2" and "2>1" to force one dB or the other
        //=> An (also hidden) button "Auto" to set back to analyzed options (need to remember those)
        //=> Add 1 line by statsource (with previous values), and the "new" line (as in text logs - to be removed), 
        //      displaying in bold what is different han new on statsources rows and also in bold what value will be
        //      updated in whatever dB in "new" row.
        //=> Eventually, (for rating mainly or only) allow choosing what line will be used in "new" by double clicking on 
        // one or the other dB cell, and restoring automatic choice by double clicking in "new" cell

		if(forceJaMuz) {
			if(selectedStatSource.getSource().isUpdateTags()) {
				//Read tags and set it to new as well
				fileNew.setTags(getTagsJaMuz(fileJaMuz));
			}
		} else { //New is by default the one from JaMuz, so not comparing if forcing JaMuz
			
			//Compare playCounter	
			if(selectedStatSource.getSource().isUpdatePlayCounter()) {
				//Note: previousPlayCounter (for the selected database) is stored on myFileInfoDbJaMuz
				//as retrieved during getStatistics on JaMuzDB
				int playCounterToAdd;
				if(fileJaMuz.getPreviousPlayCounter()<=fileSelectedDb.getPlayCounter()) {
					//New play counter seen on selected database that needs to be added
					if(fileJaMuz.getPreviousPlayCounter()>=0) {
						playCounterToAdd=fileSelectedDb.getPlayCounter()-fileJaMuz.getPreviousPlayCounter();
					}
					else {
						//This happens when no previous playCounter could be retrieved from JaMuzDb
						//Means that selected Db has not yet been merged
						playCounterToAdd=fileSelectedDb.getPlayCounter();
					}
				}
				else {
					//Current playCounter is less than previous playCounter !!
					//This can happen if somehow the statistics have been reset in selected database
					//BUT can also happen if the merge is aborted or fails (for any reason) 
					//while merging and before the selected DB has been copied back
					//=> NOT adding anything. The correct playCounter will be reached later on
					//=> Restarting from current reference, which is JaMuz
					//(we can only miss some play counts IF the reset is made on selected DB. 
					//Better than adding some each time the process is aborted)
					//Do not think -or think well - of updating previousPlayCounter after selected DB is copied back as
					//the problem will be on the if() above, and could cause more problems :
					//		- extra play counts added again and again)
					playCounterToAdd=0;
				}
				fileNew.setPlayCounter(fileJaMuz.getPlayCounter()+playCounterToAdd);
			}
			//Compare lastPlayed, only if required (not for Mixxx as an example)
			if(selectedStatSource.getSource().isUpdateLastPlayed()) {
				if(!fileSelectedDb.getLastPlayed().equals(fileJaMuz.getLastPlayed())) {
					if(fileSelectedDb.getLastPlayed().before(new Date()) 
							&& fileJaMuz.getLastPlayed().before(new Date())) {
						if(fileSelectedDb.getLastPlayed().after(
								fileJaMuz.getLastPlayed())) {
							fileNew.setLastPlayed(fileSelectedDb.getLastPlayed());
						}
						else {
							fileNew.setLastPlayed(fileJaMuz.getLastPlayed());
						}
					}
					else if(fileSelectedDb.getLastPlayed().before(new Date())) {
						fileNew.setLastPlayed(fileSelectedDb.getLastPlayed());
					}
					else if(fileJaMuz.getLastPlayed().before(new Date())) {
						fileNew.setLastPlayed(fileJaMuz.getLastPlayed());
					}
					else {
						//If both are after new Date() (now), but still different
						fileNew.setLastPlayed(new Date());
					}
				}
				else if(fileJaMuz.getLastPlayed().after(new Date())) {
					//If both are equal and after new Date() (now)
					fileNew.setLastPlayed(new Date());
				}
			}
			
			//Compare addedDate, only if required (not for Kodi as an example)
			if(selectedStatSource.getSource().isUpdateAddedDate()) {
				if(!fileSelectedDb.getAddedDate().equals(fileJaMuz.getAddedDate())) {
					if(fileSelectedDb.getAddedDate().after(new Date(0)) 
							&& fileJaMuz.getAddedDate().after(new Date(0))) {
						if(fileSelectedDb.getAddedDate().before(
								fileJaMuz.getAddedDate())) {
							fileNew.setAddedDate(fileSelectedDb.getAddedDate());
						}
						else {
							fileNew.setAddedDate(fileJaMuz.getAddedDate());
						}
					}
					else if(fileSelectedDb.getAddedDate().after(new Date(0))) {
						fileNew.setAddedDate(fileSelectedDb.getAddedDate());
					}
					else if(fileJaMuz.getAddedDate().after(new Date(0))) {
						fileNew.setAddedDate(fileJaMuz.getAddedDate());
					}
					else {
						//If both are before new Date(0), but still different
						fileNew.setAddedDate(new Date(0));
					}
				}
				else if(fileJaMuz.getAddedDate().before(new Date(0))) {
					//If both are equal and before new Date(0)
					fileNew.setAddedDate(new Date(0));
				}
			}
			
			//Comparing rating
			if(fileSelectedDb.getRating()<=0 && fileJaMuz.getRating() <=0) {
				//If both files have rating <=0, set them both to 0
				fileNew.setRating(0);
			}
			else if(fileSelectedDb.getRating()<=0 || fileJaMuz.getRating() <=0) {
				//If only one file is <=0, take the other side
				if(fileSelectedDb.getRating()<=0) {
					fileNew.setRating(fileJaMuz.getRating());
				}
				else {
					fileNew.setRating(fileSelectedDb.getRating());
				}
			}
			else if(fileSelectedDb.getRating()!=fileJaMuz.getRating()) {
				//FIXME TEST include this new behavior in junit tests
				if(fileJaMuz.getRatingModifDate().after(
						selectedStatSource.lastMergeDate)) {
					//It has been modified after last merge on JaMuz
					//Could be the same on selected Db but Preferring JaMuz since we cannot know for sure
					fileNew.setRating(fileJaMuz.getRating());
					
					//*** TIP ***: To force JaMuz for rating only, run this on JaMuz dB:
					//update file set ratingModifDate=datetime('now');
					//=> Rating from Jamuz will then be replicated to all sources, then back to normal :)
				}
				else {
					//May have been updated on other sources as well
					//But taking the first one
					//Then, since ratingModifDate will be updated on JaMuz, this value will be replicated to all
					//other sources.
					
					//TODO: Add sources priority as an option to decide which source is first
					
					fileNew.setRating(fileSelectedDb.getRating());
				}
			}
			if(fileNew.getRating()!=fileJaMuz.getRating() 
					&& fileNew.getRating() > 0) {
				//This will ensure that we only update ratingModifDate
				//if changed on JaMuz to a valid value
				fileNew.setUpdateRatingModifDate(true);
			}
			
			String genre="";
			float BPM=-1;
			
			//Comparing genre
			if(isGenreValid(fileSelectedDb.getGenre()) && !isGenreValid(fileJaMuz.getGenre())) {
				fileNew.setGenre(fileSelectedDb.getGenre());
			}
			else if(!isGenreValid(fileSelectedDb.getGenre()) && isGenreValid(fileJaMuz.getGenre())) {
				fileNew.setGenre(fileJaMuz.getGenre());
			}
			else if(!fileSelectedDb.getGenre().equals(fileJaMuz.getGenre())) {
				//TODO: include this new behavior in junit tests
				if(fileJaMuz.getGenreModifDate().after(
						selectedStatSource.lastMergeDate)) {
					//It has been modified after last merge on JaMuz
					//Could be the same on selected Db but Preferring JaMuz 
					//since we cannot know for sure
					fileNew.setGenre(fileJaMuz.getGenre());
					
					//*** TIP ***: To force JaMuz for genre only, run this on JaMuz dB:
					//update file set genreModifDate=datetime('now');
					//=> Rating from Jamuz will then be replicated to all sources, 
					//then back to normal :)
				}
				else {
					//May have been updated on other sources as well
					//But taking the first one
					//Then, since ratingModifDate will be updated on JaMuz, 
					//this value will be replicated to all other sources.
					
					//TODO: Add sources priority as an option to decide which 
					//source is first
					
					fileNew.setGenre(fileSelectedDb.getGenre());
				}
				
			}
			if(!fileNew.getGenre().equals(fileJaMuz.getGenre()) 
					&& isGenreValid(fileNew.getGenre())) {
				//This will ensure that we only update genreModifDate
				//if changed on JaMuz to a valid value
				fileNew.setUpdateGenreModifDate(true);
				genre=fileNew.getGenre();
			}
			
			//Comparing BPM
			//TODO: Now that merging BPM (and that works), display it on jtable and logs
			if(selectedStatSource.getSource().isUpdateBPM()) {
				if(fileSelectedDb.getBPM()<=0 && fileJaMuz.getBPM() <=0) {
					//If both files have BPM <=0, set them both to 0
					fileNew.setBPM(0);
				}
				else if(fileSelectedDb.getBPM()<=0 || fileJaMuz.getBPM() <=0) {
					//If only one file is <=0, take the other side
					if(fileSelectedDb.getBPM()<=0) {
						fileNew.setBPM(fileJaMuz.getBPM());
					}
					else {
						fileNew.setBPM(fileSelectedDb.getBPM());
						BPM = fileSelectedDb.getBPM();
					}
				}
				else if(fileSelectedDb.getBPM()!=fileJaMuz.getBPM()){
					//If both are >0 but different then taking the one from Mixxx
					if(selectedStatSource.getIdStatement()==4) { //TODO: Mixxx is Badly hard-coded (use an enum instead of int)
						fileNew.setBPM(fileSelectedDb.getBPM());
						BPM = fileSelectedDb.getBPM();
					}
					else {
						fileNew.setBPM(fileJaMuz.getBPM());
					}
				}
			}
			
			if(!genre.equals("") || BPM>=0) {
				//TODO: Better use inheritance !!
				mergeListDbJaMuzFileTags.add(new FileInfoInt(fileJaMuz, 
						BPM, genre));
			}
			
			//Comparing Tags
			//TODO: display it on jtable
			if(selectedStatSource.getSource().isUpdateTags()) {
				ArrayList<String> selectedDbTags;
				if(isRemote) {
					selectedDbTags = fileSelectedDb.getTags();
				}
				else {
					selectedDbTags = getTagsSelected(fileSelectedDb);
				}
				ArrayList<String> jaMuzTags = getTagsJaMuz(fileJaMuz);
				if(!Utils.equalLists(selectedDbTags, jaMuzTags)) {
					if(fileJaMuz.getTagsModifDate().after(
							selectedStatSource.lastMergeDate)) {
						//It has been modified after last merge on JaMuz
						//Could be the same on selected Db but Preferring JaMuz 
						//since we cannot know for sure
						fileNew.setTags(jaMuzTags);
						
						//*** TIP ***: To force JaMuz for user tags only, run this on JaMuz dB:
						//update file set tagsModifDate=datetime('now');
						//=> User tags from Jamuz will then be replicated to all sources, 
						//then back to normal :)
						
					}
					else {
						//May have been updated on other sources as well
						//But taking the first one
						//Then, since tagsModifDate will be updated on JaMuz, 
						//this value will be replicated to all other sources.
						
						//TODO: Add sources priority as an option to decide which 
						//source is first
					
						fileNew.setTags(selectedDbTags);
					}
				} else {
					//Still compared equal but user tags will not be saved
					//even if some other fields are different
					//=> less db usage
					fileJaMuz.setTags(null);
					fileSelectedDb.setTags(null);
					fileNew.setTags(null);
				}
			}
			
			//Comparing new with JaMuz and add it to merge list if different
			if(!fileJaMuz.equalsStats(fileNew)) {
				fileNew.setSourceName(run+"-"+fileJaMuz.getSourceName());
				mergeListDbJaMuz.add((FileInfo) fileNew.clone());
				Jamuz.getLogger().log(Level.FINEST, "Added to \"{0}\" merge list", 
						fileNew.getSourceName());  //NOI18N
				isOneDbUpdated=true;
			}
		}
		
	//Now comparing new with selected
		//fileNew is a clone of fileJaMuz at first
		//In case we do not want to compare lastplayed and/or addedDate, ...
		//we need to update myFileInfoNew with their original values
		if(!selectedStatSource.getSource().isUpdateLastPlayed()) {
			fileNew.setLastPlayed(fileSelectedDb.getLastPlayed());
		}
		if(!selectedStatSource.getSource().isUpdateAddedDate()) {
			fileNew.setAddedDate(fileSelectedDb.getAddedDate());
		}
        if(!selectedStatSource.getSource().isUpdateBPM()) {
            fileNew.setBPM(fileSelectedDb.getBPM());
		}
		if(!selectedStatSource.getSource().isUpdateGenre()) {
            fileNew.setGenre(fileSelectedDb.getGenre());
		}
		if(!selectedStatSource.getSource().isUpdatePlayCounter()) {
			fileNew.setPlayCounter(fileSelectedDb.getPlayCounter());
		}
		
		if(!fileSelectedDb.equalsStats(fileNew)) {
			fileNew.setSourceName(run+"-"+fileSelectedDb.getSourceName());
			//myFileInfoNew is a clone of myFileInfoDbJaMuz at first
			//so path is in linux style whereas on selected, it may be in Windows format
			fileNew.setRelativeFullPath(fileSelectedDb.getRelativeFullPath());
			fileNew.setRelativePath(fileSelectedDb.getRelativePath());
			fileNew.setFilename(fileSelectedDb.getFilename()); //This is not really usefull, though
			mergeListDbSelected.add((FileInfo) fileNew.clone());
			Jamuz.getLogger().log(Level.FINEST, "Added to \"{0}\" merge list", 
					fileNew.getSourceName());  //NOI18N
			isOneDbUpdated=true;
		}

	//Adding to LOGs
		//TODO: Make a single text LOG, instead of the current 3 text files, tab separated (for Excel/LibreCalc viewing)
		//And including all compared files, NOT only when updated somewhere (bigger file but easier for debugging)
		//"File"	"ProcessStep"	"RatingDB1"	"RatingDB2"	"RatingNew"	...
		//"Alain Souchon\(Collection) 1984-2001\04 Portbail.mp3"	"TBD"	"0"	"1"	"1"	...
		if(doLogText && isOneDbUpdated) {
			//Those are N/A on fileNew & fileSelectedDb
			fileNew.setRatingModifDate(fileJaMuz.getRatingModifDate());
			fileSelectedDb.setRatingModifDate(fileJaMuz.getRatingModifDate());
			fileNew.setTagsModifDate(fileJaMuz.getTagsModifDate());
			fileSelectedDb.setTagsModifDate(fileJaMuz.getTagsModifDate());
			fileNew.setGenreModifDate(fileJaMuz.getGenreModifDate());
			fileSelectedDb.setGenreModifDate(fileJaMuz.getGenreModifDate());
			fileSelectedDb.setPreviousPlayCounter(fileJaMuz.getPreviousPlayCounter());
			//Those also if not compared
			if(!selectedStatSource.getSource().isUpdateLastPlayed()) {
				fileNew.setLastPlayed(fileJaMuz.getLastPlayed());
				fileSelectedDb.setLastPlayed(fileJaMuz.getLastPlayed());
			}
			if(!selectedStatSource.getSource().isUpdateAddedDate()) {
				fileNew.setAddedDate(fileJaMuz.getAddedDate());
				fileSelectedDb.setAddedDate(fileJaMuz.getAddedDate());
			}
			if(!selectedStatSource.getSource().isUpdateBPM()) {
				fileNew.setBPM(fileJaMuz.getBPM());
				fileSelectedDb.setBPM(fileJaMuz.getBPM());
			}
			if(!selectedStatSource.getSource().isUpdateGenre()) {
				fileNew.setGenre(fileJaMuz.getGenre());
				fileSelectedDb.setGenre(fileJaMuz.getGenre());
			}
			if(!selectedStatSource.getSource().isUpdatePlayCounter()) {
				fileNew.setPlayCounter(fileJaMuz.getPlayCounter());
				fileSelectedDb.setPlayCounter(fileJaMuz.getPlayCounter());
			}
			addToLog(fileNew, fileSelectedDb, fileJaMuz);
		}
	}

	private ArrayList<String> getTagsSelected(FileInfo fileSelectedDb) {
		ArrayList<String> selectedDbTags = new ArrayList<>();
		selectedStatSource
				.getSource().getTags(selectedDbTags, fileSelectedDb);
		fileSelectedDb.setTags(selectedDbTags);
		return selectedDbTags;
	}
	
	private ArrayList<String> getTagsJaMuz(FileInfo fileJaMuz) {
		ArrayList<String> jaMuzTags = new ArrayList<>();
		dBJaMuz.getTags(jaMuzTags, fileJaMuz);
		fileJaMuz.setTags(jaMuzTags);
		return jaMuzTags;
	}
	
	private void addToLog(FileInfo myFileInfoNew, FileInfo myFileInfoDbSelected, 
			FileInfo myFileInfoDbJaMuz) {
		logDbSelected.add(myFileInfoDbSelected.toString());
		Jamuz.getLogger().log(Level.FINEST, "{0}:<BR/>{1}", 
				new Object[]{myFileInfoDbSelected.getSourceName(), 
					myFileInfoDbSelected.toString()});  //NOI18N
		logDbJaMuz.add(myFileInfoDbJaMuz.toString());
		Jamuz.getLogger().log(Level.FINEST, "{0}:<BR/>{1}", 
				new Object[]{myFileInfoDbJaMuz.getSourceName(), myFileInfoDbJaMuz.toString()});  //NOI18N
		logDbNew.add(myFileInfoNew.toString());
		Jamuz.getLogger().log(Level.FINEST, "NEW:<BR/>{0}", myFileInfoNew.toString());  //NOI18N
	}
    
    private void addToLog(String selected, String jamuz) {
		logDbSelected.add(selected);
		logDbJaMuz.add(jamuz);
//		logDbNew.add("Item missing on some side.");
	}
	
	private boolean copyDB(StatSourceAbstract dbStats, boolean receive) throws InterruptedException {
		//Checking if process got interrupted
		checkAbort();

		if(receive) {
			progressBar.progress(MessageFormat.format(
					Inter.get("Msg.Merge.Retrieving"), dbStats.getName())); //NOI18N
			callback.refresh();
            return dbStats.getSource(logSubPath);
        }
		else  {
            progressBar.progress(MessageFormat.format(
					Inter.get("Msg.Merge.CopyingDBback"), dbStats.getName())); //NOI18N
			callback.refresh();
            return dbStats.sendSource(logSubPath);
        }
	}
	
	private boolean backupDB(StatSourceAbstract dbStats) throws InterruptedException {
		//Checking if process got interrupted
		checkAbort();
        progressBar.progress(MessageFormat.format(
				Inter.get("Msg.Merge.DatabaseBackup"), dbStats.getName())); //NOI18N
        callback.refresh();
		return dbStats.backupSource(logSubPath); 
        //TODO: guayadeque db is big as contains blobs in covers table.
        //Would be good to remove before backup as we do not need those for merge stats anyway
		// => as we cannot modify source nor we want to backup copy (as too big)
		//		then need to create a new db and insert from source
	}
	
	private boolean merge(String run) throws InterruptedException, 
			CloneNotSupportedException {
		int nbFiles;
        ArrayList<FileInfo> filesToUpdatePlayCounter = new ArrayList<>();
        // Processing selected database
        progressBar.progress(MessageFormat.format(
				Inter.get("Msg.Merge.Updating"), 
				selectedStatSource.getSource().getName())); //NOI18N
		callback.refresh();
		nbFiles = mergeListDbSelected.size();
		if(nbFiles>0) {
			checkAbort();
			if(isRemote) {
				for(FileInfo fileInfo : mergeListDbSelected) {
					filesToUpdatePlayCounter.add(fileInfo);
					completedList.add((FileInfo) fileInfo.clone());
				}
				Jamuz.getDb().setTags(mergeListDbSelected, null);
				
			} else {
				int[] results = selectedStatSource
						.getSource()
						.updateStatistics(mergeListDbSelected);
				if(results.length<nbFiles) {
					return false;
				}
				checkAbort();
				//Analyzing results
				int result;
				FileInfo fileInfo;
				for(int i = 0; i < nbFiles; i++) {
					checkAbort();
					result = results[i];
					fileInfo = mergeListDbSelected.get(i);
					if(result==1) {
						filesToUpdatePlayCounter.add(fileInfo);
						completedList.add((FileInfo) fileInfo.clone());
					}
					else {
						//TODO: Insert statistics (AmaroK only for now)
						//					fileInfo.sourceName="!!! ERROR updating "+fileInfo.sourceName;  //NOI18N
						fileInfo.setSourceName(run+"-"+fileInfo.getSourceName()+":\t"
								+"!!! ERROR updating !!!");
						errorList.add((FileInfo) fileInfo.clone());
						nbFilesErrorSelected+=1;
					}
				}
				checkAbort();
			}
		}
		
        //Processing jamuz
		progressBar.progress(MessageFormat.format(Inter.get(
				"Msg.Merge.Updating"), dBJaMuz.getName())); //NOI18N
        callback.refresh();
		nbFiles = mergeListDbJaMuz.size();
		if(nbFiles>0) {
			checkAbort();
			int[] results = dBJaMuz
					.updateStatistics(mergeListDbJaMuz);
			if(results.length<nbFiles) {
				return false;
			}
			checkAbort();
			//Analyzing results
			int result;
			FileInfo fileInfo;
			for(int i = 0; i < nbFiles; i++) {
				checkAbort();
				result = results[i];
				fileInfo = mergeListDbJaMuz.get(i);
				if(result!=1) {
                    fileInfo.setSourceName(run+"-"+fileInfo.getSourceName()+":\t"
							+"!!! ERROR updating !!!");
					errorList.add((FileInfo) fileInfo.clone());
					nbFilesErrorJaMuz+=1;
				}
				else {
					completedList.add((FileInfo) fileInfo.clone());
                    filesToUpdatePlayCounter.add(fileInfo);
				}
			}
			checkAbort();
		}

		if(!simulate) {	
			nbFiles=mergeListDbJaMuzFileTags.size();
			progressBar.progress(Inter.get("Msg.Check.SavingTags")); //NOI18N
			callback.refresh();
			if(nbFiles>0) {
				Iterator<FileInfoInt> i = mergeListDbJaMuzFileTags.iterator();
				while (i.hasNext()) {
					FileInfoInt fileInfoInt = i.next();
					checkAbort();
					//TODO MERGE If aborted, tags will no more be written to file. 
					if(!fileInfoInt.getGenre().equals("") ) {
						fileInfoInt.saveGenreToFileTags();
					} 
					else if(fileInfoInt.getBPM()>=0) {
						fileInfoInt.saveBPMtoFileTags();
					}
					//FIXME MERGE Are we updating modifiedDate (path and file) 
					//and other stats when saving tags (always) ?
					//+ile.setLastModified() too
					checkAbort();
					i.remove();
				}
			}

			if(filesToUpdatePlayCounter.size()>0) {
				//Remove potential duplicates 
	//	        filesToUpdatePlayCounter = new ArrayList(new HashSet(filesToUpdatePlayCounter)); //no order
				filesToUpdatePlayCounter = new ArrayList(
						new LinkedHashSet(filesToUpdatePlayCounter)); //If you need to preserve the order use 'LinkedHashSet'
				//Changing previous play counter if update was successfull
				if(!dBJaMuz.setPreviousPlayCounter(filesToUpdatePlayCounter, 
						selectedStatSource.getId())) {
					return false;
				}
			}

			selectedStatSource.updateLastMergeDate();
		}

		return true;
    }
	
	private boolean createTextLogs(String prefix) {
		
		prefix+="-";  //NOI18N
		
		//Creating LOG only if Level requires it
		if(Level.INFO.intValue()<Jamuz.getLogger().getLevel().intValue()) {
            return true;
        }
        //Create Log for JaMuz database
        logDbJaMuz = new LogText(logSubPath);
        if(!logDbJaMuz.createFile(prefix+"1-"+dBJaMuz.getName() + ".txt")) {
            Popup.error(MessageFormat.format(Inter.get("Error.Merge.CreatingLOG"), 
					new Object[] {prefix+dBJaMuz.getName()+".txt"}));  //NOI18N
            return false;
        }
        //Create Log for NEW info (after comparison)
        logDbNew = new LogText(logSubPath);
        if(!logDbNew.createFile(prefix+"2-"+"NEW" + ".txt")) {  //NOI18N
            Popup.error(MessageFormat.format(Inter.get("Error.Merge.CreatingLOG"), 
					new Object[] {prefix+"NEW.txt"}));  //NOI18N
            return false;
        }
		//Create Log for selected database
        logDbSelected = new LogText(logSubPath);
        String name=selectedStatSource.getSource().getName()
                +" ["+DateTime.formatUTC(selectedStatSource.lastMergeDate, 
						DateTime.DateTimeFormat.FILE, false)+"]";
        if(!logDbSelected.createFile(prefix+"3-"+name + ".txt")) {
            Popup.error(MessageFormat.format(Inter.get("Error.Merge.CreatingLOG"), 
					new Object[] {prefix+name+".txt"}));  //NOI18N
            return false;
        }
        return true;
    }
	
    //TODO: Use a Map instead ...
	private int searchInStatsListDbJaMuz(String relativeFullPath) {
		for(int i = 0; i < statsListDbJaMuz.size(); i++) {
			FileInfo myFileInfo = statsListDbJaMuz.get(i);
            //Searching ignoring case because of windows, but that may not be a good way 
            //TODO: What if it changes to case-sensitive equals() ?
			if(myFileInfo.getRelativeFullPath().equalsIgnoreCase(relativeFullPath)) { 
				return i; 
			}
		}
		return -1;
	}

	public ArrayList<FileInfo> getErrorList() {
        return errorList;
    }

	public ArrayList<FileInfo> getCompletedList() {
        return completedList;
    }
	
	private void getAndSetNbSteps() throws InterruptedException {
		//The number of steps is the same for each call to scanAndMerge
		//but depends if selected database is sqlite or not AND if merge is simulated or not

		nbSteps=1; // Récupération de "JaMuz" ...
		for (int i=0; i<sources.size(); i++) {
			//+1. Create text LOG files
			//+1. Connect DB
			//+1. Get stats sel DB
			//+1. Get stats JaMuz DB
			//+1. Compare sel DB VS JaMuz DB
			//+1. Mark remaining from JaMuz List as not found
			//+1. Update sel DB
			//+1. Update JaMuz DB
            //+1  Update BPM tag in files
			//+1. Close connection
			int nbStepsOneWay=10;
			//Doubling for all dbIndex except the last one which is only done once
			if(i<(sources.size()-1)) {
				nbStepsOneWay*=2;
			}
			nbSteps+=nbStepsOneWay;
			
			//+1 Get selected database
			nbSteps+=1;
			//+1 Check selected database
			nbSteps+=1;

			if(!simulate) {
				//+1 backup database
				nbSteps+=1;
				//+1 Copy database back
				nbSteps+=1;
			}
		}
		checkAbort();
	}

	private boolean isGenreValid(String genre) {
		return !genre.startsWith("{") 
				&& Jamuz.getGenres().contains(genre);
	}
}