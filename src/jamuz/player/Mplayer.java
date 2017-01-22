/*
 * Copyright (C) 2012 phramusca ( https://github.com/phramusca/JaMuz/ )
 * 
 *  Helped a LOT by https://beradrian.wordpress.com/tag/mplayer/
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

package jamuz.player;

import jamuz.Jamuz;
import jamuz.gui.PanelLyrics;
import jamuz.gui.PanelMain;
import jamuz.process.check.MP3gain;
import jamuz.utils.Inter;
import jamuz.utils.OS;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jamuz.utils.Popup;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;

/**
* Play any or so audio file using mplayer.
* @author kc7bfi
*/
public class Mplayer implements Runnable {
    private Thread playerThread;
    private String filePath;
	private Process process;
	private int length;
	public boolean positionLock=false;
    private Updater positionUpdater;
	Writer writer;
	BufferedReader inputReader;
	BufferedReader errorReader;
	float volume;
	
    /**
     * Mplayer player
     */
    public Mplayer() {
    }
    
    private void play() {
        this.playerThread = new Thread(this, "Thread.Mplayer.play");  //NOI18N
        this.playerThread.start();
    }
    
	public boolean startMplayer() {
		//Build mplayer command array
		List<String> cmdArray = new ArrayList<>();
		if(OS.isWindows()) {
			//FIXME: Check that on Windows
			cmdArray.add("data\\mplayer.exe");
		}
		else {
			//TODO: Test if it works in MacOS for instance
			cmdArray.add("mplayer");
		}

		//FIXME: Allow this in Select tab to preview a song
		
		//Play on a particular sound card (use "aplay -L" for list of sound cards)
		//http://stackoverflow.com/questions/22438353/java-select-audio-device-and-play-mp3
		//Adapted for mplayer
		//"-ao", "alsa:device=sysdefault=Device"
//		cmdArray.add("-ao");
//		cmdArray.add("alsa:device=sysdefault=Device");

		
		cmdArray.add("-slave");
		cmdArray.add("-quiet");
//		cmdArray.add("-idle");


		cmdArray.add(filePath);

		Runtime runtime = Runtime.getRuntime();
		try {
			//Start process
			String[] stockArr = new String[cmdArray.size()];
			stockArr = cmdArray.toArray(stockArr);
			process = runtime.exec(stockArr);

			writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//			errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			// Reading Input Stream
//			Thread readInputThread = new Thread("Thread.Mplayer.process.mplayer.InputStream") {
//				@Override
//				public void run() {
//					try {
//						inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//						String line;
//						while((line = inputReader.readLine()) != null) {
////							if(!line.trim().equals("")) {  //NOI18N
////								//A:   1.6 (01.6) of 188.0 (03:08.0)  0.3% 
////								if(line.startsWith("A:")) {  //NOI18N
////									String pos = line.substring(2, line.indexOf("("));
////									int posint = Double.valueOf(pos).intValue()*1000;
////									if(!positionLock) {
////										PanelMain.dispMP3progress(posint);
////										PanelLyrics.setPosition(posint, length);
////									}
////								}
////								else {
//									System.out.println("inputReader: "+line);
////								}
////							}
//						}
//					} catch(IOException ex) {
//						Jamuz.getLogger().log(Level.SEVERE, "", ex);  //NOI18N
//						//TODO: return false
//					} finally {
//						if(inputReader!=null) {
//							try {
//								inputReader.close();
//							} catch (IOException ex) {
//								Logger.getLogger(MP3gain.class.getName()).log(Level.SEVERE, null, ex);
//							}
//						}
//					}
//				}
//			};
//			readInputThread.start();

			// Reading Error Stream
			Thread readErrorThread = new Thread("Thread.Mplayer.process.mplayer.ErrorStream") {
				@Override
				public void run() {
					try {
						errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
						String line;
						while((line = errorReader.readLine()) != null) {
//							System.err.println(line);
							System.err.println("errorReader: "+line);
							Jamuz.getLogger().log(Level.SEVERE, line);  //NOI18N
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
			
			process.waitFor(1, TimeUnit.SECONDS);
			
			positionUpdater = new Updater();
			positionUpdater.start();
			
			volume = getVolume();
			
			//Waiting for process
			process.waitFor();
//			readInputThread.join();
			readErrorThread.join();
			Jamuz.getLogger().finest("COMPLETE");  //NOI18N
			return true;

		} catch (IOException | InterruptedException ex) {
			Popup.error(ex);
			return false;
		}
	}
	
	//FIXME: Implement Pause: refer to playerMP3
	//FIXME: when playing stop:
	//	- Stop positionUpdater (before and anyway ignore stream close errors or at least do not popup)
	//	- Play next song
	//FIXME: Stop playing when application is closed !!
    public void stop() { 
        //FIXME: Test
		if(process!=null) {
			try {
				execute("q");
				writer.close();
				process.waitFor(5, TimeUnit.SECONDS);
				if(process.isAlive()) {
					process.destroy();
				}
				process=null;
			} catch (InterruptedException | IOException ex) {
				Popup.error(Inter.get("Error.stop")+" \""+filePath+"\"", ex);  //NOI18N
			} 
		}
		
		if(positionUpdater!=null) {
			positionUpdater.cancel();
			positionUpdater.purge();
		}
	}
    
    public String play(String filePath) {
        try
        {
			this.stop();

			this.filePath = filePath;

			this.play();
            AudioFile audioFile = AudioFileIO.read(new File(filePath));
			String lyrics = audioFile.getTag().getFirst(FieldKey.LYRICS);
			
			length=audioFile.getAudioHeader().getTrackLength();
			
			return lyrics;
        }
        catch (IOException | CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException ex)
        {
			Popup.error(Inter.get("Error.Play")+" \""+filePath+"\"", ex);  //NOI18N
			return "";  //NOI18N
        } 
    }

	/**
	 * Sends a command to MPlayer..
	 * 
	 * @param command
	 *            the command to be sent
	 */
	private void execute(String command) {
		execute(command, null);
	}
	
	/**
	 * Sends a command to MPlayer and waits for an answer.
	 * 
	 * @param command
	 *            the command to be sent
	 * @param expected
	 *            the string with which has to start the line; if null don't wait for an answer
	 * @return the MPlayer answer
	 */
	private String execute(String command, String expected) {
		if (process != null) {
			try {
				System.out.println("Send to MPlayer the command \"" + command + "\" and expecting "
						+ (expected != null ? "\"" + expected + "\"" : "no answer"));
				writer.write(command);
				writer.write("\n");
				writer.flush();
				System.out.println("Command sent");
				if (expected != null) {
					String response = waitForAnswer(expected);
					System.out.println("MPlayer command response: " + response);
					return response;
				}
			} catch (IOException ex) {
				Popup.error("Error.execute \""+command+"\"", ex);  //NOI18N
			}
		}
		return null;
	}
	
	/**
	 * Read from the MPlayer standard output and error a line that starts with the given parameter and return it.
	 * 
	 * @param expected
	 *            the expected starting string for the line
	 * @return the entire line from the standard output or error of MPlayer
	 */
	private String waitForAnswer(String expected) {
		// todo add the possibility to specify more options to be specified
		// todo use regexp matching instead of the beginning of a string
		String line = null;
		if (expected != null) {
			try {
				while ((line = inputReader.readLine()) != null) {
					System.out.println("Reading line: " + line);
					if (line.startsWith(expected)) {
						return line;
					}
				}
			} catch (IOException e) {
			}
		}
		return line;
	}
	
	public void setPosition(int seconds) {
		execute("set_property time_pos"+ " " + seconds);
	}
	
	/**
	 * @return time position
	 */
	public double getPosition() {
		return getPropertyAsDouble("time_pos");
	}
	
	protected double getPropertyAsDouble(String name) {
		try {
			String valueS = getProperty(name);
			double valueL = Double.parseDouble(valueS);
			return valueL;
		} catch (NumberFormatException | NullPointerException exc) {
		}
		return 0;
	}
	
	protected float getPropertyAsFloat(String name) {
		try {
			String valueS = getProperty(name);
			return Float.parseFloat(valueS);
		} catch (NumberFormatException | NullPointerException exc) {
		}
		return 0f;
	}
	
	protected String getProperty(String name) {
		if (name == null || process == null) {
			return null;
		}
		String s = "ANS_" + name + "=";
		String x = execute("get_property " + name, s);
		if (x == null)
			return null;
		if (!x.startsWith(s))
			return null;
		return x.substring(s.length());
	}
	
	public void volumePlus() {
		volume++;
		setVolume();
	}
	
	public void volumeMinus() {
		volume--;
		setVolume();
	}
	
	private void setVolume() {
		execute("set_property volume" + " " + volume);
	}
	
	/**
	 * @return volume
	 */
	public float getVolume() {
		return getPropertyAsFloat("volume");
	}
	
	/**
	 * Position updater class
	 */
	public class Updater extends Timer{
		
		/**
		 * Update period:
		 * 800: too much, 500 looks OK
		 */
		private static final long UPDATE_PERIODE = 500;
		
		/**
		 * Starts position updater
		 */
		public void start() {
			schedule(new displayPosition(), 0, UPDATE_PERIODE);
		}

		private class displayPosition extends TimerTask {
			@Override
			public void run() {
				if(!positionLock) {
					int position = (int) Math.round(getPosition());
					PanelMain.dispMP3progress(position*1000);
					PanelLyrics.setPosition(position*1000, length);
				}
			}
		}
	}

    @Override
    public void run() {
        this.startMplayer();
    }
}
