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

package jamuz.soulseek;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.TypeToken;
import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.utils.Benchmark;
import jamuz.utils.OS;
import jamuz.utils.Popup;
import jamuz.utils.StringManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;

/**
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Soulseek {
	
	private final ICallBackSoulseek callback;
	private OutputStream StdIn;
	private boolean downloadStarted;
	private Process process;
	private Benchmark benchmark;
	private SoulseekDownload folderBeingDownloaded;
	private boolean enableSearch;
	private String destination;
	
	//FIXME Soulseek: Doc installation:
	// - Uninstall node with apt
	//		- sudo apt remove nodejs
	// - Install node with nvm
	//		- nvm install node
	// - Create symbolic link
	//		- sudo ln -s "$(which node)" /usr/bin/node
	// - Install soulseek cli
	//		- npm install -g soulseek-cli
		
    /**
	 * Wrapper for Soulseek CLI (https://github.com/aeyoll/soulseek-cli)
     *
	 * @param callBackSoulseek
     */  
    public Soulseek(ICallBackSoulseek callBackSoulseek) {
		this.callback = callBackSoulseek;
    }

	//FIXME Soulseek ! Use login command
	public boolean login() {
		return process(Command.login, "", Mode.mp3);
	}
	
	//TODO Soulseek: Use query command ?
	public boolean query(String query) {
		return process(Command.query, query, Mode.flac);
	}
	
	public boolean download(String query, String destination, Mode mode) {
		this.destination = destination;
		return process(Command.download, query, mode);
	}
	
	boolean sendSelection(SoulseekResult result, String query) {
		try {
			folderBeingDownloaded = new SoulseekDownload(query, result.getNbOfFiles(), result.getPath(), result.getUsername(), destination);
			StdIn.write((result.getKey()+"\n").getBytes());
			downloadStarted = true;
			benchmark = new Benchmark(result.getNbOfFiles());
			StdIn.flush();
			return true;
		} catch (IOException ex) {
			Logger.getLogger(Soulseek.class.getName()).log(Level.SEVERE, null, ex);
			callback.error(ex.getMessage());
			return false;
		}
	}

	public SoulseekDownload getFolderBeingDownloaded() {
		return folderBeingDownloaded;
	}

	boolean cancelling = false;
	
	void cancel() {
		cancelling = true;
		process.destroyForcibly();
	}
	
	public enum Mode {
		mp3,
		flac
	}
	
	private enum Command {
		login,
		query,
		download
	}
	
	/**
	 * @return  
	 */
	private boolean process(Command command, String query, Mode mode) {
		List<String> cmdArray = new ArrayList<>();
		if(OS.isWindows()) {
			//FIXME WINDOWS Suppport soulseek
			return false;
			//cmdArray.add("data\\soulseek.exe");
		}
		else {
			//TODO: Test if it works in MacOS for instance
//			cmdArray.add("soulseek");		
			cmdArray.add("node");
			cmdArray.add("/home/raph/Documents/04-Creations/Dev/Repos/soulseek-cli/cli.js");	
		}
		cmdArray.add(command.name());
		switch(command) {
			case login:
				break;
			case query:
				cmdArray.add("--mode");
				cmdArray.add(mode.name());
				cmdArray.add(query);
				break;
			case download:
				cmdArray.add("--non-interactive");
				cmdArray.add("--mode");
				cmdArray.add(mode.name());
				cmdArray.add(query);
				cmdArray.add("--destination");
				cmdArray.add(destination);
				break;
			default:
				throw new AssertionError(command.name());
		}
		
		Runtime runtime = Runtime.getRuntime();
		try {
			String[] stockArr = new String[cmdArray.size()];
			stockArr = cmdArray.toArray(stockArr);
			process = runtime.exec(stockArr, null, new File(destination));
			StdIn = process.getOutputStream();
			downloadStarted = false;
			enableSearch = true;
			StringBuilder jsonRes = new StringBuilder();
			
			// Reading Input Stream
			Thread readInputThread = new Thread("Thread.Soulseek.process.soulseek.InputStream") {
				@Override
				public void run() {
					BufferedReader inputReader=null;
					try {
						inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
						String line;
						int resultsCount = 0;
						int downloadId = 0;
						List<SoulseekResult> downloads = new ArrayList<>();
						boolean downloadNotified = false;
						
						while((line = inputReader.readLine()) != null) {
							Jamuz.getLogger().finest(line);
							System.out.println(line);
							if(!downloadStarted) {
								if(line.startsWith("Displaying ")) {
									callback.progress(line);
									resultsCount = getResultsCount(line);
									System.out.println(resultsCount + " ::: " + line);
								} else if(line.equals("Choose a folder to download")) {
									final Gson gson = new Gson();
									Type mapType = new TypeToken<Map<String, SoulseekResultFolder>>(){}.getType();
									Map<String, SoulseekResultFolder> fromJson = gson.fromJson(jsonRes.toString(), mapType);
									callback.enableDownload(fromJson);
								} else if(line.equals("Nothing found")) {
									callback.progress(line);
									if(mode.equals(Mode.flac)) {
										enableSearch = false;
										callback.noFlacFound();
									}
									cancel();
								} else if(resultsCount > 0) {
									jsonRes.append(line);
								} else {
									checkError(line);
								}
							} else {
								if(line.contains("Starting download of")) {
									downloadNotified = true;
								} else if(line.endsWith("downloaded.")) {
									callback.completed();
								} else if(downloadNotified) {
									SoulseekResult soulseekDownloading = getSoulseekDownloading(line, downloadId);
									if(soulseekDownloading!=null) {
										downloads.add(soulseekDownloading);
										downloadId++;
										callback.addResult(soulseekDownloading, "");
									} else {
										SoulseekResult soulseekAlreadyDownloaded = getSoulseekAlreadyDownloaded(line, 
											FilenameUtils.concat(destination, folderBeingDownloaded.getPath()), downloadId);
										if(soulseekAlreadyDownloaded!=null) {
											downloadId++;
											String get = benchmark.get();
											callback.addResult(soulseekAlreadyDownloaded, get);
										} else {
											SoulseekResult soulseekDownloaded = getSoulseekDownloaded(line);
											if(soulseekDownloaded!=null) {
												String path = soulseekDownloaded.getPath();
												File file = new File(path);
												if(file.exists()) {
													FileInfoInt fileInfoInt = new FileInfoInt(path, "");
													fileInfoInt.readMetadata(false);
													soulseekDownloaded.setBitrate(fileInfoInt.getFormat()+" @ "+fileInfoInt.getBitRate()+" kb/s");
													soulseekDownloaded.setSize(StringManager.secondsToMMSS(fileInfoInt.getLength())+" | "+StringManager.humanReadableByteCount(fileInfoInt.getSize(), false));
												}
												String get = benchmark.get();
												soulseekDownloaded.setSpeed(get);
												int rowToReplace = downloads.get(soulseekDownloaded.getId()-1).getId();
												soulseekDownloaded.setId(rowToReplace);
												folderBeingDownloaded.fileDownloaded();
												callback.replaceResult(soulseekDownloaded, rowToReplace, get);
											} else {
												checkError(line);
											}
										}
									}
								}
							}
						}
					} catch(IOException ex) {
						Jamuz.getLogger().log(Level.SEVERE, "", ex);  //NOI18N
						if(cancelling) {
							callback.progress(ex.getMessage());
						} else {
							callback.error(ex.getMessage());
						}
					} finally {
						if(inputReader!=null) {
							try {
								inputReader.close();
							} catch (IOException ex) {
								Logger.getLogger(Soulseek.class.getName()).log(Level.SEVERE, null, ex);
							}
						}
					}
				}
			};
			readInputThread.start();

			// Reading Error Stream
			Thread readErrorThread = new Thread("Thread.Soulseek.process.soulseek.ErrorStream") {
				@Override
				public void run() {
					BufferedReader errorReader=null;
					try {
						errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
						String line;
						while((line = errorReader.readLine()) != null) {
							
							Pattern patterner = Pattern.compile("^(.*)Error: (.*)$", Pattern.CASE_INSENSITIVE);
							Matcher matcher = patterner.matcher(line);
							boolean matchFound = matcher.find();
							if(matchFound) {
								callback.error(line);
								cancel();
							}
							callback.progress("!! ERROR !! " + line);
							Jamuz.getLogger().finest(line);
						}
					} catch(IOException ex) {
						Jamuz.getLogger().log(Level.SEVERE, "", ex);  //NOI18N
						if(cancelling) {
							callback.progress(ex.getMessage());
						} else {
							callback.error(ex.getMessage());
						}
					} finally {
						if(errorReader!=null) {
							try {
								errorReader.close();
							} catch (IOException ex) {
								Logger.getLogger(Soulseek.class.getName()).log(Level.SEVERE, null, ex);
							}
						}
					}
				}
			};
			readErrorThread.start();
			
			process.waitFor();
			readInputThread.join();
			readErrorThread.join();
			return true;

		} catch (IOException | InterruptedException ex) {
			Popup.error(ex);
			return false;
		} finally {
			if(enableSearch) {
				callback.enableSearch();
			}
		}
	}
	
	private void checkError(String line) {
		if(line.startsWith("Error: ")) {
			callback.error(line);
			cancel();
		} else {
			callback.progress(line);
		}
	}
	
	private int getResultId(String line) {
		return getRegexFirstCount(line, "^  ([0-9]+)\\) ");
	}
	
	private int getResultsCount(String line) {
		return getRegexFirstCount(line, "^Displaying ([0-9]+) search results");
	}
	
	private int getRegexFirstCount(String line, String pattern) {
		Pattern patterner = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = patterner.matcher(line);
		boolean matchFound = matcher.find();
		if(matchFound) {
			String group = matcher.group(1);
			return Integer.parseInt(group);
		}
		return -1;
	}
	
	//filename.mp3|flac [already downloaded: skipping]
	private SoulseekResult getSoulseekAlreadyDownloaded(String line, String destination, int downloadId) {
		Pattern patterner = Pattern.compile("^(.*) \\[already downloaded: skipping]$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = patterner.matcher(line);
		boolean matchFound = matcher.find();
		if(matchFound) {
			SoulseekResult soulseekDownload = new SoulseekResult(downloadId,
					SoulseekResult.Status.Received,
					FilenameUtils.concat(destination, matcher.group(1)));
			return soulseekDownload;
		}
		return null;
	}
	
	//filename.mp3|flac [downloading...]
	private SoulseekResult getSoulseekDownloading(String line, int downloadId) {
		Pattern patterner = Pattern.compile("^(.*) \\[downloading\\.\\.\\.\\]$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = patterner.matcher(line);
		boolean matchFound = matcher.find();
		if(matchFound) {
			SoulseekResult soulseekDownload = new SoulseekResult(downloadId,
					SoulseekResult.Status.Downloading,
					matcher.group(1));
			return soulseekDownload;
		}
		return null;
	}
	
	//(2/24) Received: /path/to/folder/filename.mp3|flac
	private SoulseekResult getSoulseekDownloaded(String line) {
		Pattern patterner = Pattern.compile("^\\(([0-9]+)\\/([0-9]+)\\) Received: (.*)$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = patterner.matcher(line);
		boolean matchFound = matcher.find();
		if(matchFound) {
			SoulseekResult soulseekDownload = new SoulseekResult(
					Integer.parseInt(matcher.group(1)), 
					SoulseekResult.Status.Received,
					matcher.group(3));
			return soulseekDownload;
		}
		return null;
	}
}