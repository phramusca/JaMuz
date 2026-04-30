/*
 * Copyright (C) 2012 phramusca <phramusca@gmail.com>
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
import jamuz.utils.Inter;
import jamuz.utils.OS;
import jamuz.utils.Popup;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
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
	// By default, let the OS choose the audio output (system default)
	private AudioCard audioCard= new AudioCard("Default (system)", "");
	// If true, we will apply lastPosition once mplayer is ready (see startMplayer()).
	private boolean resumeRequested = false;
	/** Wall time when current mplayer process was started (for failure heuristics). */
	private long mplayerStartedAtMs;
	/** Set from stderr when ALSA/AO init fails; MPlayer sometimes still exits with code 0. */
	private final AtomicBoolean aoOpenFailed = new AtomicBoolean(false);
	/**
	 * True after exit code ≠ 0, AO init failure, or start exception — used so preview UI can
	 * switch output and call play() again while {@link #isPlaying()} is already false.
	 */
	private volatile boolean lastPlaybackAttemptFailed = false;
	private final EventListenerList listeners = new EventListenerList();

	/** MPlayer stderr lines that mean playback did not get a working audio device. */
	private static boolean isAoFatalStderrLine(String line) {
		if (line == null || line.isEmpty()) {
			return false;
		}
		// Keep substrings specific enough to avoid false positives from unrelated messages.
		return line.contains("Could not open/initialize audio device")
				|| line.contains("Failed to initialize audio driver")
				|| line.contains("Playback open error:")
				|| line.contains("Unknown error 524")
				|| line.contains("open '/dev/snd/pcm") && line.contains("failed (-524)");
	}

	/**
	 * After a failed open or decode, preview can change ALSA device and restart without a running process.
	 */
	public boolean isLastPlaybackAttemptFailed() {
		return lastPlaybackAttemptFailed;
	}

	/**
	 * Forget current preview file / failure state (e.g. user clicked preview stop).
	 */
	public void discardPlaybackSession() {
		synchronized (lockPlayer) {
			lastPlaybackAttemptFailed = false;
			filePath = null;
		}
	}
	
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

		/**
		 *
		 * @return
		 */
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
	 * @return current file path being played, or null if none
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @return true if a playback process is running
	 */
	public boolean isPlaying() {
		return process != null && process.isAlive();
	}

	/**
	 * Waits for the playback thread to finish (process exited and device released).
	 * Use after stop() before starting a new playback on another device to avoid "Device or resource busy".
	 *
	 * @param timeoutMs maximum time to wait
	 * @return true if the thread ended within the timeout
	 */
	public boolean waitForPlaybackToEnd(long timeoutMs) {
		Thread t = playerThread;
		if (t == null || t == Thread.currentThread()) {
			return true;
		}
		try {
			t.join(timeoutMs);
			return !t.isAlive();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return false;
		}
	}

	/**
	 * Sets the position to resume from at next play(..., true).
	 */
	public void setResumePosition(int seconds) {
		this.lastPosition = seconds;
	}

	/**
	 * @return last resume position (in seconds) stored from the previous pause/seek.
	 */
	public int getLastPosition() {
		return lastPosition;
	}
	
	/**
	 *
	 * @return
	 */
	public ArrayList<AudioCard> getAudioCards() {
		ArrayList<AudioCard> audioCards = new ArrayList<>();

		if(OS.isWindows()) {
			//FIXME WINDOWS: list audio devices for preview (e.g. mplayer -ao help / dsound)
			return audioCards;
		}

		// Use "aplay -l" to list physical PLAYBACK devices; each line gives (card, device) => hw=X.Y
		// Works on Mint (Nvidia + Generic), Raspberry (vc4hdmi0/1, IQaudIODAC), and with USB cards
		List<String> cmdArray = new ArrayList<>();
		cmdArray.add("aplay");
		cmdArray.add("-l");
		Runtime runtime = Runtime.getRuntime();
		try {
			Process processSoundCards = runtime.exec(cmdArray.toArray(new String[0]));
			Thread readInputThread = new Thread("Thread.Mplayer.getAudioCards") {
				@Override
				public void run() {
					try (BufferedReader reader = new BufferedReader(
							new InputStreamReader(processSoundCards.getInputStream(), "UTF-8"))) {
						String line;
						while((line = reader.readLine()) != null) {
							addAudioCardFromAplayL(audioCards, line);
						}
					} catch(IOException ex) {
						Jamuz.getLogger().log(Level.SEVERE, "", ex);  //NOI18N
					}
				}
			};
			readInputThread.start();
			processSoundCards.waitFor();
			readInputThread.join();
		} catch (IOException | InterruptedException ex) {
			Jamuz.getLogger().log(Level.SEVERE, null, ex);
		}
		return audioCards;
	}

	/**
	 * Raw {@code device=hw=} often triggers ALSA "Unable to set format: Invalid argument" on HDMI/USB.
	 * {@code plughw} adds conversion; we also normalize any legacy / odd casing from stored options.
	 */
	private static String normalizeAlsaAo(String ao) {
		if (ao == null || ao.isEmpty()) {
			return ao;
		}
		String t = ao.trim();
		// Case-insensitive: DB or hand-edited values may not match startsWith("alsa:device=hw=")
		return t.replaceAll("(?i)device=hw=", "device=plughw=");
	}

	/** Pattern: … first "N :" = card index, optional "[Label]", comma, then "M :" = device index, rest = description. Language-agnostic (line may start with "carte "/"card "/etc.). */
	private static final Pattern APLAY_L_LINE = Pattern.compile(
		"^.*?(\\d+)\\s*:\\s*(?:[^\\[]*\\[([^\\]]+)\\])?[^,]*,.*?(\\d+)\\s*:\\s*(.+)$");

	/**
	 * Parse one line from "aplay -l" and add a playback device as AudioCard if applicable.
	 * Structure (any locale): "… cardIndex : … [CardLabel], … deviceIndex : DeviceDescription"
	 * We only rely on digits, colons, brackets and comma — no "carte"/"device" etc.
	 */
	private void addAudioCardFromAplayL(List<AudioCard> audioCards, String line) {
		if(line == null || line.trim().isEmpty()) {
			return;
		}
		Matcher m = APLAY_L_LINE.matcher(line.trim());
		if(!m.matches()) {
			return;
		}
		int cardIdx = Integer.parseInt(m.group(1));
		String cardLabel = m.group(2);
		if(cardLabel == null || cardLabel.isEmpty()) {
			cardLabel = "Card " + cardIdx;
		} else {
			cardLabel = cardLabel.trim();
		}
		int devIdx = Integer.parseInt(m.group(3));
		String deviceDesc = m.group(4).trim();
		// Keep " [xxx]" in display name when it adds info (e.g. "HDMI 0 [PL3494WQ]" for the connected display)
		int br = deviceDesc.indexOf(" [");
		int brEnd = br > 0 ? deviceDesc.indexOf(']', br) : -1;
		if(br > 0 && brEnd > br) {
			String beforeBracket = deviceDesc.substring(0, br).trim();
			String inBracket = deviceDesc.substring(br + 2, brEnd).trim();
			// Strip bracket part only if it's redundant (same as or contained in the name before)
			if(inBracket.equalsIgnoreCase(beforeBracket) || beforeBracket.contains(inBracket)) {
				deviceDesc = beforeBracket;
			}
			// else keep full "Name [Short]" (e.g. "HDMI 0 [PL3494WQ]")
		}
		if(deviceDesc.isEmpty()) {
			deviceDesc = "Device " + devIdx;
		}
		String displayName = cardLabel + " - " + deviceDesc;
		// plughw adds ALSA plug (rate/format conversion). Raw "hw=" often fails on HDMI
		// (no matching sample rate) or shows no sound while aplay -l lists the device.
		String value = "alsa:device=plughw=" + cardIdx + "." + devIdx;
		audioCards.add(new AudioCard(displayName, value));
	}
	
	/**
	 *
	 * @return
	 */
	public boolean startMplayer() {
		//Build mplayer command array
		List<String> cmdArray = new ArrayList<>();
		if(OS.isWindows()) {
			cmdArray.add(Jamuz.getFile("mplayer.exe", "data", "system", "bin").getAbsolutePath());
//			cmdArray.add("-ao");
//			cmdArray.add("dsound:device=1");
		}
		else {
			//TODO: Test if it works in MacOS for instance
			cmdArray.add("mplayer");
			// When resuming a preview/main playback, start directly at lastPosition.
			// This avoids a "start then seek" behavior and is more deterministic than set_property time_pos.
			boolean doSeek = resumeRequested && lastPosition > 0;
			resumeRequested = false;
			if(doSeek) {
				cmdArray.add("-ss");
				cmdArray.add(Integer.toString(lastPosition));
			}
			// If an explicit audio output is set (preview, specific ALSA device, etc.),
			// pass it to mplayer. Otherwise, let mplayer / the OS choose the default.
			boolean explicitLinuxAo = false;
			if(audioCard!=null && audioCard.getValue()!=null && !audioCard.getValue().isEmpty()) {
				String ao = normalizeAlsaAo(audioCard.getValue());
				Jamuz.getLogger().log(Level.INFO, "MPlayer explicit -ao (after normalize): {0}", ao);
				cmdArray.add("-ao");
				cmdArray.add(ao);
				explicitLinuxAo = !OS.isWindows();
			}
			// Software volume when a specific ALSA device is set (HDMI/USB often have no hw mixer).
			// Leave default output unchanged so PulseAudio/PipeWire keep their usual volume path.
			if (explicitLinuxAo) {
				cmdArray.add("-softvol");
				// Helps when the device rejects the decoder's sample format (common HDMI / Pi).
				cmdArray.add("-af");
				cmdArray.add("format=s16le");
			}
			// -novideo    
			//   Ne pas jouer/encoder la vidéo. 
			//   Dans bien des cas, cela ne fonctionnera pas, utilisez à la place -vc null -vo null.
			cmdArray.add("-novideo");
			cmdArray.add("-vc");
			cmdArray.add("null");
			cmdArray.add("-vo");
			cmdArray.add("null");
		}
		
		cmdArray.add("-slave");
		cmdArray.add("-quiet");

		cmdArray.add(filePath);

		Runtime runtime = Runtime.getRuntime();
		try {
			//Start process
			String[] stockArr = new String[cmdArray.size()];
			stockArr = cmdArray.toArray(stockArr);
			mplayerStartedAtMs = System.currentTimeMillis();
			aoOpenFailed.set(false);
			process = runtime.exec(stockArr);

			writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			// Reading Error Stream
			final Process procRef = process;
			Thread readErrorThread = new Thread("Thread.Mplayer.process.mplayer.ErrorStream") {
				@Override
				public void run() {
					try {
						errorReader = new BufferedReader(new InputStreamReader(procRef.getErrorStream()));
						String line;
						while((line = errorReader.readLine()) != null) {
							if (isAoFatalStderrLine(line)) {
								aoOpenFailed.set(true);
							}
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
								Logger.getLogger(Mplayer.class.getName()).log(Level.SEVERE, null, ex);
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
			// Drain stderr before deciding: AO errors can arrive just before EOF on the error stream.
			try {
				readErrorThread.join(3000);
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}
			final long playedMs = System.currentTimeMillis() - mplayerStartedAtMs;
			int exitCode = 0;
			try {
				exitCode = process.exitValue();
			} catch (IllegalThreadStateException ignored) {
			}
			final boolean aoFailed = aoOpenFailed.get();
			synchronized(lockPlayer) {

				if(positionUpdater!=null) {
					positionUpdater.cancel();
					positionUpdater.purge();
				}
				process = null;
				// Do not chain "next track" if mplayer failed (bad -ao, missing file, etc.).
				// MPlayer sometimes exits 0 after "no sound" / ALSA open failure — use stderr too.
				if (goNext && (exitCode != 0 || aoFailed)) {
					goNext = false;
					Jamuz.getLogger().log(Level.WARNING,
							"MPlayer ended with exit {0}, aoFailed={1}, after {2} ms — not advancing queue",
							new Object[]{exitCode, aoFailed, playedMs});
				}
				if(goNext) {
					firePlaybackFinished();
				}
				lastPlaybackAttemptFailed = (exitCode != 0 || aoFailed);
				lockPlayer.notify();
			}

			return true;

		} catch (IOException | InterruptedException ex) {
			synchronized (lockPlayer) {
				lastPlaybackAttemptFailed = true;
			}
			Jamuz.getLogger().log(Level.SEVERE, null, ex);
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
				lastPlaybackAttemptFailed = false;

				this.filePath = filePath;
				this.resumeRequested = resume;

				this.play();

				AudioFile audioFile = AudioFileIO.read(new File(filePath));
				String lyrics = audioFile.getTag().getFirst(FieldKey.LYRICS);
				length=audioFile.getAudioHeader().getTrackLength();

				//Resume to previous position
				if(!resume) {
					lastPosition=0;
				}

				return lyrics;
			}
			catch (IOException | CannotReadException | TagException | ReadOnlyFileException 
					| InterruptedException | InvalidAudioFrameException ex)
			{
				Jamuz.getLogger().log(Level.SEVERE, Inter.get("Error.Play")+" \""+filePath+"\"", ex);  //NOI18N
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
	private synchronized void execute(String command) {
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
	private synchronized String execute(String command, String expected) {
		if (process != null && process.isAlive()) {
			try {
				writer.write(command);
				writer.write("\n");
				writer.flush();
				if (expected != null) {
					String response = waitForAnswer(expected);
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
	public synchronized double getPosition() {
		return getPropertyAsDouble("time_pos");
	}
	
	/**
	 *
	 * @param name
	 * @return
	 */
	protected synchronized double getPropertyAsDouble(String name) {
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
	protected synchronized float getPropertyAsFloat(String name) {
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
	protected synchronized String getProperty(String name) {
		if (name == null || process == null || !process.isAlive()) {
			return null;
		}
		String s = "ANS_" + name + "=";
		String x = execute("get_property " + name, s);
		if (x == null) {
			return null;
		}
		if (!x.startsWith(s)) {
			return null;
		}
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
	public synchronized float getVolume() {
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

	/**
	 *
	 */
	@Override
    public void run() {
        this.startMplayer();
    }
}
