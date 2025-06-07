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

import jamuz.Jamuz;
import jamuz.gui.swing.ProgressBar;
import jamuz.utils.Inter;
import jamuz.utils.OS;
import jamuz.utils.Popup;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;

/**
 * MP3gain class launches mp3gain executable to compute ReplayGain on MP3 files
 * @author phramusca <phramusca@gmail.com>
 */
public class MP3gain {
	
	private int progressTotal;
	private int progressIndex;
    private final String path;
    private final String relativePath;
	private final ProgressBar progressBar;
	private final boolean recalculate;
	private final boolean trackGain;
	
    /**
     *
	 * @param trackGain
	 * @param recalculate
     * @param rootPath
     * @param relativePath
     * @param progressBar
     */  
    public MP3gain(boolean trackGain, boolean recalculate, String rootPath, String relativePath, ProgressBar progressBar) {
        this.relativePath = relativePath;
        this.path = rootPath + relativePath;
        this.progressBar = progressBar;
		this.recalculate = recalculate;
		this.trackGain=trackGain;
    }

	/**
	 *
	 * @param file
	 */
	public MP3gain(File file) {
        this.path = file.getAbsolutePath();
		this.relativePath = FilenameUtils.getBaseName(path);
        this.progressBar = new ProgressBar();
		this.recalculate = true;
		this.trackGain=true;
	}
	
	/**
	 *
	 * @return
	 */
	public boolean process() {
		File file = new File(path);
		File[] files;
		if (file.isDirectory()) {
			files = file.listFiles();
			if (files == null) {
				Popup.error("No files found for mp3gain in \""+path+"\"");  //NOI18N
				return false;
			}
		} else {
			files = new File[1];
			files[0] = file;
		}
		return process(files);
	}
	
	/**
	 * Compute MP3gain with options:
	 * <p>
	 * -k - automatically lower Track/Album gain to not clip audio <p>
	 * -a - apply Album gain automatically<p>
	 * If recalculate: -s r - force re-calculation (do not read tag info)<p>
	 * 
	 * Note that even if album gain is applied, only the remaining is set in tag.<p>
	 * (http://forums.slimdevices.com/archive/index.php/t-27036.html)<p>
	 * total gain = [(mp3gain undo)*1.5 => applied] + gain in tag (remaining)<p>
	 * 
	 * https://sourceforge.net/projects/mp3gain/<p>
	 * http://wiki.hydrogenaud.io/index.php?title=Replaygain#MP3Gain<p>
	 * @return  
	 */
	private boolean process(File[] files) {
		//Build mp3gain command array
		List<String> cmdArray = new ArrayList<>();
		if(OS.isWindows()) {
			cmdArray.add("data\\system\\bin\\mp3gain.exe");
		}
		else {
			//TODO: Test if it works in MacOS for instance
			cmdArray.add("mp3gain");
		}

		cmdArray.add("-k");  //-k - automatically lower Track/Album gain to not clip audio  //NOI18N
		//If you specify -r and -a, only the second one will work
		//TODO: mp3gain : make album/track an option

		if(trackGain) {
			cmdArray.add("-r");	 //-r - apply Track gain automatically (all files set to equal loudness)
		} else {
			cmdArray.add("-a"); //-a - apply Album gain automatically (files are all from the same  //NOI18N
					  //album: a single gain change is applied to all files, so
					  //their loudness relative to each other remains unchanged,
					  //but the average album loudness is normalized)
		}

//			cmdArray.add("-d"); //-d <n> - modify suggested dB gain by floating-point n
//			cmdArray.add("2.0"); // -d 2.0: makes it 91.0 dB (defaults to 89.0)
//			cmdArray.add(""); // -s i - use ID3v2 tag for MP3 gain info (bugged)
//			cmdArray.add(""); // -s a - use APE tag for MP3 gain info (default)
							// => !!! DO NOT STORE TAG IN ID3 AS IT MESS IT UP !!!!

		if(recalculate) {
			cmdArray.add("-s");// -s r - force re-calculation (do not read tag info)
			cmdArray.add("r");
		}

//TODO: Use -o to retrieve replaygain values and store them in ID3
//Max Amplitude must be divided by 32768

//	File				MP3 gain	dB gain		Max Amplitude	Max global_gain		Min global_gain
//	I:\Musique\Nouveau\APE.mp3	0		0.240000	15637.837632	192			135
//	"Album"				0		0.240000	15637.837632	192			135
//
//	No changes to I:\Musique\Nouveau\APE.mp3 are necessary

		//Add the files in path to command array
		progressIndex=0;
		progressTotal=0;
		for (File myFile : files) {
			String absolutePath=myFile.getAbsolutePath();
			String fileExtension=FilenameUtils.getExtension(absolutePath);
			//TODO: Find a way to get mime type instead of file extension
			if(fileExtension.toLowerCase().equals("mp3")) {  //NOI18N
				cmdArray.add(absolutePath);
				progressTotal+=1;
			}
		}

		if(progressTotal<=0) {
			//No MP3 files to be processed. Exiting
			return false;
		}

		Runtime runtime = Runtime.getRuntime();
		final Process process;

		try {
			progressBar.setupAsPercentage();

			//Start process
			String[] stockArr = new String[cmdArray.size()];
			stockArr = cmdArray.toArray(stockArr);
			process = runtime.exec(stockArr);

			// Reading Input Stream
			Thread readInputThread = new Thread("Thread.MP3gain.process.mp3gain.InputStream") {
				@Override
				public void run() {
					BufferedReader inputReader=null;
					try {
						inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
						String line;
						while((line = inputReader.readLine()) != null) {
//									Jamuz.getLogger().finest(line);
							if(!line.isBlank()) {  //NOI18N
								if(line.startsWith("No changes")) {  //NOI18N
									//No changes to <myFile> are necessary
									progressIndex+=2;
									setProgress(0);
								}
								else if(line.startsWith("Applying mp3 gain")) {  //NOI18N
									//Applying mp3 gain change of 1 to /home/raph/Musique/Archive-Manuel/Armelle/Led Zeppelin/Led Zeppelin IV/03 The Battle of Evermore.mp3...
									// => Ensuite dans ErrorStream:
									// 1% of 7721786 bytes written
									progressIndex+=1;
									setProgress(0);
								}
								//Not testing nor logging other messages
								//as it can be different from one OS to another
								//+parsing is only dedicated to progressbar display
								//No need to deep-analyse process
							}

						}
					} catch(IOException ex) {
						Jamuz.getLogger().log(Level.SEVERE, "", ex);  //NOI18N
						//TODO: return false
					} finally {
						if(inputReader!=null) {
							try {
								inputReader.close();
							} catch (IOException ex) {
								Logger.getLogger(MP3gain.class.getName()).log(Level.SEVERE, null, ex);
							}
						}
					}
				}
			};
			readInputThread.start();

			// Reading Error Stream
			Thread readErrorThread = new Thread("Thread.MP3gain.process.mp3gain.ErrorStream") {
				@Override
				public void run() {
					BufferedReader errorReader=null;
					try {
						errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
						String line;
						int posBegin;
						int posMiddle;
						int posEnd;
						String fileNumber;
						String percent;
						while((line = errorReader.readLine()) != null) {
//                                    Jamuz.getLogger().finest(line);
							if(!line.isBlank()) {  //NOI18N
								if(line.endsWith("analyzed")) {  //NOI18N
									//[1/8]  1% of 7721786 bytes analyzed
									posBegin = line.indexOf('[');  //NOI18N
									posMiddle = line.indexOf('/');  //NOI18N
									posEnd=line.indexOf(']');  //NOI18N
									fileNumber=line.substring(posBegin+1,posMiddle);
									percent=line.substring(posEnd+1, line.indexOf('%')).trim();  //NOI18N

									if(Integer.parseInt(fileNumber)>progressIndex) {
										progressIndex+=1;
										setProgress(0);
									}
									setProgress(Integer.valueOf(percent));
								}
								else if(line.endsWith("written")) {  //NOI18N
									//1% of 7721786 bytes written
									percent=line.substring(0, line.indexOf('%')).trim();  //NOI18N
									setProgress(Integer.valueOf(percent));		
								}
								//Not testing nor logging other messages
								//as it can be different from one OS to another
								//+parsing is only dedicated to progressbar display
								//No need to deep-analyse process
							}
						}
					} catch(IOException ex) {
						Jamuz.getLogger().log(Level.SEVERE, "", ex);  //NOI18N
						//TODO: return false
					} finally {
						if(errorReader!=null) {
							try {
								errorReader.close();
							} catch (IOException ex) {
								Logger.getLogger(MP3gain.class.getName()).log(Level.SEVERE, null, ex);
							}
						}
					}
				}
			};
			readErrorThread.start();

			//Waiting for process, then input and error reading threads to complete
			process.waitFor();
			readInputThread.join();
			readErrorThread.join();

//				Jamuz.getLogger().finest("COMPLETE");  //NOI18N
			return true;

		} catch (IOException | InterruptedException ex) {
			Popup.error(ex);
			return false;
		}
	}
	
	/**
	 *
	 * @return
	 */
	public boolean undo() {
		File pathFile = new File(path);
		File[] files = pathFile.listFiles();
		if (files != null) {
			//Build mp3gain command array
			List<String> cmdArray = new ArrayList<>();
			if(OS.isWindows()) {
				cmdArray.add("data\\system\\bin\\mp3gain.exe");
			}
            else {
                //TODO: Test if it works in MacOS for instance
				cmdArray.add("mp3gain");
			}
			
			cmdArray.add("-u");  //-u - undo changes made (based on stored tag info)

			//Add the files in path to command array
            for (File myFile : files) {
                String absolutePath=myFile.getAbsolutePath();
                String fileExtension=FilenameUtils.getExtension(absolutePath);
                //TODO: Find a way to get mime type instead of file extension
                if(fileExtension.toLowerCase().equals("mp3")) {  //NOI18N
                    cmdArray.add(absolutePath);
                }
            }
			
			Runtime runtime = Runtime.getRuntime();
			final Process process;

			try {
				String[] stockArr = new String[cmdArray.size()];
				stockArr = cmdArray.toArray(stockArr);
				process = runtime.exec(stockArr);
				process.waitFor();
				return true;

			} catch (IOException | InterruptedException ex) {
				Popup.error(ex);
				return false;
			}
		}
		else {
			// This should never happen as this function
			// will be called only if folder contains MP3 files
			Popup.error("No MP3 files found for mp3gain in \""+path+"\"");  //NOI18N
			return false;
		}
	}
	
	/**
	 *
	 * @return
	 */
	public boolean clearTags() {
		File pathFile = new File(path);
		File[] files = pathFile.listFiles();
		if (files != null) {
			//Build mp3gain command array
			List<String> cmdArray = new ArrayList<>();
			if(OS.isWindows()) {
				cmdArray.add("data\\system\\bin\\mp3gain.exe");
			}
            else {
                //TODO: Test if it works in MacOS for instance
				cmdArray.add("mp3gain");
			}
			
			cmdArray.add("-s");  //-s d - delete stored tag info (no other processing)
			cmdArray.add("d");
			
			//Add the files in path to command array
            for (File myFile : files) {
                String absolutePath=myFile.getAbsolutePath();
                String fileExtension=FilenameUtils.getExtension(absolutePath);
                //TODO: Find a way to get mime type instead of file extension
                if(fileExtension.toLowerCase().equals("mp3")) {  //NOI18N
                    cmdArray.add(absolutePath);
                }
            }
			
			Runtime runtime = Runtime.getRuntime();
			final Process process;

			try {
				String[] stockArr = new String[cmdArray.size()];
				stockArr = cmdArray.toArray(stockArr);
				process = runtime.exec(stockArr);
				process.waitFor();
				return true;

			} catch (IOException | InterruptedException ex) {
				Popup.error(ex);
				return false;
			}
		}
		else {
			// This should never happen as this function
			// will be called only if folder contains MP3 files
			Popup.error("No MP3 files found for mp3gain in \""+path+"\"");  //NOI18N
			return false;
		}
	}
	
	private void setProgress(int percent) {
		progressBar.setMaximum(progressTotal*200);
		progressBar.progress(MessageFormat.format(Inter.get("Msg.Check.ReplayGain"), relativePath), progressIndex*100+percent);
	}
}