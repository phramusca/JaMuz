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
import javax.swing.event.EventListenerList;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;

/**
* Play any or so audio file using mplayer.
* http://beradrian.users.sourceforge.net/articles/JMPlayer.java
*/
public class Mplayer implements Runnable {
    private Thread playerThread;
    private String filePath;
	private Process process;
	private int length;

	/**
	 *
	 */
	public boolean positionLock=false;
    private Updater positionUpdater;
	private Writer writer;
	private BufferedReader inputReader;
	private BufferedReader errorReader;
	private boolean goNext=true;
	private final Object lockPlayer = new Object();
	private int lastPosition=0;
	private AudioCard audioCard= new AudioCard("Default", "default");
	private final EventListenerList listeners = new EventListenerList();
	
    /**
     * Mplayer player
     */
    public Mplayer() {
    }
	
	/**
	 *
	 * @param listener
	 */
	public void addListener(MPlaybackListener listener) {
        listeners.add(MPlaybackListener.class, listener);
    }
	
	/**
	 *
	 * @return
	 */
	public MPlaybackListener[] getListeners() {
        return listeners.getListeners(MPlaybackListener.class);
    }
	
	/**
	 *
	 * @param volume
	 */
	protected void fireVolumeChanged(float volume) {
        for(MPlaybackListener listener : getListeners()) {
			listener.volumeChanged(volume);
		}
    }
	
	/**
	 *
	 */
	protected void firePlaybackFinished() {
        for(MPlaybackListener listener : getListeners()) {
			listener.playbackFinished();
		}
    }
	
	/**
	 *
	 * @param position
	 * @param length
	 */
	protected void firePositionChanged(int position, int length) {
        for(MPlaybackListener listener : getListeners()) {
			listener.positionChanged(position, length);
		}
    }
	
    private void play() {
		this.goNext=true;
        this.playerThread = new Thread(this, "Thread.Mplayer.play");  //NOI18N
        this.playerThread.start();
    }
    
	/**
	 *
	 */
	public class AudioCard {
		private final String name;
		private final String value;

		/**
		 *
		 * @param name
		 * @param value
		 */
		public AudioCard(String name, String value) {
			this.name = name;
			this.value = value;
		}

		/**
		 *
		 * @return
		 */
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return name;
		}
	}
	
	/**
	 *
	 * @param audioCard
	 */
	public void setAudioCard(AudioCard audioCard) {
		this.audioCard = audioCard;
	}
	
	/**
	 *
	 * @return
	 */
	public ArrayList<AudioCard> getAudioCards() {
		ArrayList<AudioCard> audioCards = new ArrayList<>();
		//Build mplayer command array
		List<String> cmdArray = new ArrayList<>();
		if(OS.isWindows()) {
			//FIXME WINDOWS what is "aplay" equivalent to list audio cards in Windows ?
			return audioCards;
		}
		else {
			//TODO: Test if it works in MacOS for instance
			cmdArray.add("aplay");
			cmdArray.add("-L");
		}
		Runtime runtime = Runtime.getRuntime();
		try {
			//Start process
			String[] stockArr = new String[cmdArray.size()];
			stockArr = cmdArray.toArray(stockArr);
			Process processSoundCards = runtime.exec(stockArr);

			// Reading Input Stream
			Thread readInputThread = new Thread("Thread.Mplayer.getAudioCards") {
				@Override
				public void run() {
					BufferedReader iputBufferedReader=null;
					try {
						iputBufferedReader = new BufferedReader(new InputStreamReader(processSoundCards.getInputStream()));
						String line;
						while((line = iputBufferedReader.readLine()) != null) {
							if(line.startsWith("sysdefault:")) {  //NOI18N
								////"-ao", "alsa:device=sysdefault=Device"
								//sysdefault:CARD=PCH
								//sysdefault:CARD=Device
								audioCards.add(
									new AudioCard(
										line.replaceAll("sysdefault:CARD=", ""), 
										"alsa:device="+line.replaceAll(":CARD", "")
									)
								);
							}
						}
					} catch(IOException ex) {
						Jamuz.getLogger().log(Level.SEVERE, "", ex);  //NOI18N
						//TODO: return false
					} finally {
						if(iputBufferedReader!=null) {
							try {
								iputBufferedReader.close();
							} catch (IOException ex) {
								Logger.getLogger(MP3gain.class.getName()).log(Level.SEVERE, null, ex);
							}
						}
					}
				}
			};
			readInputThread.start();

			processSoundCards.waitFor();
			readInputThread.join();
			
		} catch (IOException | InterruptedException ex) {
			Popup.error(ex);
		} 
		return audioCards;
	}
	
	/**
	 *
	 * @return
	 */
	public boolean startMplayer() {
		//Build mplayer command array
		List<String> cmdArray = new ArrayList<>();
		if(OS.isWindows()) {
			cmdArray.add("data\\mplayer.exe");
//			cmdArray.add("-ao");
//			cmdArray.add("dsound:device=1");
		}
		else {
			//TODO: Test if it works in MacOS for instance
			cmdArray.add("mplayer");
			cmdArray.add("-ao");
			cmdArray.add("alsa:device="+audioCard.getValue());
		}
		
		cmdArray.add("-slave");
		cmdArray.add("-quiet");

		cmdArray.add(filePath);

		Runtime runtime = Runtime.getRuntime();
		try {
			//Start process
			String[] stockArr = new String[cmdArray.size()];
			stockArr = cmdArray.toArray(stockArr);
			process = runtime.exec(stockArr);

			writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			// Reading Error Stream
			Thread readErrorThread = new Thread("Thread.Mplayer.process.mplayer.ErrorStream") {
				@Override
				public void run() {
					try {
						errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
						String line;
						while((line = errorReader.readLine()) != null) {
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

			fireVolumeChanged(getVolume());
					
			//Waiting for process
			process.waitFor();
			synchronized(lockPlayer) {

				if(positionUpdater!=null) {
					positionUpdater.cancel();
					positionUpdater.purge();
				}
				process = null;
				if(goNext) {
					firePlaybackFinished();
				}
				lockPlayer.notify();
			}

			return true;

		} catch (IOException | InterruptedException ex) {
			Popup.error(ex);
			return false;
		}
	}
	
	/**
	 *
	 */
	public void pause() {
		if(process!=null) {
			int position = (int) getPosition();
			position = Math.round(position);
			if(position>=0) {
				this.lastPosition=position;
			}
			stop();
		}
    }

	/**
	 *
	 * @return
	 */
	public boolean stop() {
		this.goNext=false;
		if (process != null && process.isAlive()) {
			execute("quit");
			return true;
		}
		return false;
	}

	/**
	 *
	 * @param filePath
	 * @param resume
	 * @return
	 */
	public String play(String filePath, boolean resume) {
		synchronized(lockPlayer) {
			try {
				if(this.stop()) {
					lockPlayer.wait(5000);
				}

				this.filePath = filePath;

				this.play();

				AudioFile audioFile = AudioFileIO.read(new File(filePath));
				String lyrics = audioFile.getTag().getFirst(FieldKey.LYRICS);
				length=audioFile.getAudioHeader().getTrackLength();

				//Resume to previous position
				if(!resume) lastPosition=0;
				this.setPosition(lastPosition);

				return lyrics;
			}
			catch (IOException | CannotReadException | TagException | ReadOnlyFileException 
					| InterruptedException | InvalidAudioFrameException ex)
			{
				Popup.error(Inter.get("Error.Play")+" \""+filePath+"\"", ex);  //NOI18N
				return "";  //NOI18N
			} 
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
		if (process != null && process.isAlive()) {
			try {
//				Jamuz.getLogger().log(Level.FINEST, "Send to MPlayer the command \"{0}\" and expecting {1}", 
//					new Object[]{command, expected != null ? "\"" + expected + "\"" : "no answer"});  //NOI18N
				writer.write(command);
				writer.write("\n");
				writer.flush();
//				Jamuz.getLogger().log(Level.FINEST, "Command sent");  //NOI18N
				if (expected != null) {
					String response = waitForAnswer(expected);
//					Jamuz.getLogger().log(Level.FINEST, "MPlayer command response: {0}", response);  //NOI18N
					return response;
				}
			} catch (IOException ex) {
				Jamuz.getLogger().log(Level.SEVERE, "Error.execute \""+command+"\"", ex);  //NOI18N
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
	private synchronized String waitForAnswer(String expected) {
		String line = null;
		if (expected != null) {
			try {
				while ((line = inputReader.readLine()) != null) {
					if (line.startsWith(expected)) {
						return line;
					}
					if (process == null || !process.isAlive()) {
						return null;
					}
				}
			} catch (IOException e) {
			}
		}
		return line;
	}
	
	/**
	 *
	 * @param seconds
	 */
	public void setPosition(int seconds) {
		execute("set_property time_pos"+ " " + seconds);
	}
	
	/**
	 * @return time position
	 */
	public double getPosition() {
		return getPropertyAsDouble("time_pos");
	}
	
	/**
	 *
	 * @param name
	 * @return
	 */
	protected double getPropertyAsDouble(String name) {
		try {
			String valueS = getProperty(name);
			double valueL = Double.parseDouble(valueS);
			return valueL;
		} catch (NumberFormatException | NullPointerException exc) {
		}
		return -1;
	}
	
	/**
	 *
	 * @param name
	 * @return
	 */
	protected float getPropertyAsFloat(String name) {
		try {
			String valueS = getProperty(name);
			return Float.parseFloat(valueS);
		} catch (NumberFormatException | NullPointerException exc) {
		}
		return -1;
	}
	
	/**
	 *
	 * @param name
	 * @return
	 */
	protected String getProperty(String name) {
		if (name == null || process == null || !process.isAlive()) {
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
	
	/**
	 *
	 * @param volume
	 */
	public void setVolume(float volume) {
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
	public class Updater extends Timer {
		
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
					if(position>=0) {
						firePositionChanged(position, length);
						//TODO: make PanelLyrics.setPosition private and use listener instead
						PanelLyrics.setPosition(position*1000, length);
					}	
				}
			}
		}
	}

    @Override
    public void run() {
        this.startMplayer();
    }
}
