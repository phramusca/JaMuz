/*-
 * Copyright © 2009 Diamond Light Source Ltd.
 *
 * This file is part of GDA.
 *
 * GDA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License version 3 as published by the Free
 * Software Foundation.
 *
 * GDA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along
 * with GDA. If not, see <http://www.gnu.org/licenses/>.
 */

package jamuz.player;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A video stream player which is actually an interface to the famous MPlayer.
 */
public class JMPlayer {

	private static Logger logger = LoggerFactory.getLogger(JMPlayer.class.getName());

	/** A thread that reads from an input stream and outputs to another line by line. */
	private static class LineRedirecter extends Thread {
		/** The input stream to read from. */
		private InputStream in;
		/** The output stream to write to. */
		private OutputStream out;
		/** The prefix used to prefix the lines when outputting to the logger. */
		private String prefix;

		/**
		 * @param in
		 *            the input stream to read from.
		 * @param out
		 *            the output stream to write to.
		 * @param prefix
		 *            the prefix used to prefix the lines when outputting to the logger.
		 */
		LineRedirecter(InputStream in, OutputStream out, String prefix) {
			this.in = in;
			this.out = out;
			this.prefix = prefix;
		}

		@Override
		public void run() {
			try {
				// creates the decorating reader and writer
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				PrintStream printStream = new PrintStream(out);
				String line;

				// read line by line
				while ((line = reader.readLine()) != null) {
					logger.info((prefix != null ? prefix : "") + line);
					printStream.println(line);
				}
			} catch (IOException exc) {
				logger.warn("An error has occured while grabbing lines", exc);
			}
		}

	}

	/** The path to the MPlayer executable. */
	private String mplayerPath = "mplayer";
	/** Options passed to MPlayer. */
	private String mplayerOptions = "-quiet -demuxer lavf -x 640 -y 576 -slave"; // " -idle"; //

	/** The process corresponding to MPlayer. */
	private Process mplayerProcess;
	/** The standard input for MPlayer where you can send commands. */
	private PrintStream mplayerIn;
	/** A combined reader for the the standard output and error of MPlayer. Used to read MPlayer responses. */
	private BufferedReader mplayerOutErr;

	/**
	 * Constructor
	 */
	public JMPlayer() {
	}

	/** @return the path to the MPlayer executable. */
	public String getMPlayerPath() {
		return mplayerPath;
	}

	/**
	 * Sets the path to the MPlayer executable.
	 * 
	 * @param mplayerPath
	 *            the new MPlayer path; this will be actually efective after {@link #close() closing} the currently
	 *            running player.
	 */
	public void setMPlayerPath(String mplayerPath) {
		this.mplayerPath = mplayerPath;
	}

	/**
	 * @param file
	 * @throws IOException
	 */
	public void open(File file) throws IOException {
		String path = file.getAbsolutePath().replace('\\', '/');
		if (mplayerProcess == null) {
			// start MPlayer as an external process
			String command = "\"" + mplayerPath + "\" " + mplayerOptions + " \"" + path + "\"";
			logger.info("Starting MPlayer process: " + command);
			mplayerProcess = Runtime.getRuntime().exec(command);

			// create the piped streams where to redirect the standard output and error of MPlayer
			// specify a bigger pipesize
			PipedInputStream readFrom = new PipedInputStream(1024 * 1024);
			PipedOutputStream writeTo = new PipedOutputStream(readFrom);
			mplayerOutErr = new BufferedReader(new InputStreamReader(readFrom));

			// create the threads to redirect the standard output and error of MPlayer
			new LineRedirecter(mplayerProcess.getInputStream(), writeTo, "MPlayer says: ").start();
			new LineRedirecter(mplayerProcess.getErrorStream(), writeTo, "MPlayer encountered an error: ").start();

			// the standard input of MPlayer
			mplayerIn = new PrintStream(mplayerProcess.getOutputStream());
		} else {
			execute("loadfile \"" + path + "\" 0");
		}
		// wait to start playing
		waitForAnswer("Starting playback...");
		logger.info("Started playing file " + path);
	}

	/**
	 * @throws IOException
	 */
	public void open() throws IOException {
		String path = "http://i11-webcam1.diamond.ac.uk/mjpg/video.mjpg";
		if (mplayerProcess == null) {
			// start MPlayer as an external process
			String command = mplayerPath + " " + mplayerOptions + " " + path;
			logger.info("Starting MPlayer process: " + command);
			mplayerProcess = Runtime.getRuntime().exec(command);

			// create the piped streams where to redirect the standard output and error of MPlayer
			// specify a bigger pipesize
			PipedInputStream readFrom = new PipedInputStream(1024 * 1024);
			PipedOutputStream writeTo = new PipedOutputStream(readFrom);
			mplayerOutErr = new BufferedReader(new InputStreamReader(readFrom));

			// create the threads to redirect the standard output and error of MPlayer
			new LineRedirecter(mplayerProcess.getInputStream(), writeTo, "MPlayer says: ").start();
			new LineRedirecter(mplayerProcess.getErrorStream(), writeTo, "MPlayer encountered an error: ").start();

			// the standard input of MPlayer
			mplayerIn = new PrintStream(mplayerProcess.getOutputStream());
		} else {
			execute("loadfile \"" + path + "\" 0");
		}
		// wait to start playing
		waitForAnswer("Starting playback...");
		logger.info("Started playing file " + path);
	}

	/**
	 * 
	 */
	public void close() {
		if (mplayerProcess != null) {
			execute("quit");
			try {
				mplayerProcess.waitFor();
			} catch (InterruptedException e) {
			}
			mplayerProcess = null;
		}
	}

	/**
	 * @return playing file
	 */
	public File getPlayingFile() {
		String path = getProperty("path");
		return path == null ? null : new File(path);
	}

	/**
	 * 
	 */
	public void togglePlay() {
		execute("pause");
	}

	/**
	 * @return are we playing
	 */
	public boolean isPlaying() {
		return mplayerProcess != null;
	}

	/**
	 * @return time position
	 */
	public long getTimePosition() {
		return getPropertyAsLong("time_pos");
	}

	/**
	 * @param seconds
	 */
	public void setTimePosition(long seconds) {
		setProperty("time_pos", seconds);
	}

	/**
	 * @return total time
	 */
	public long getTotalTime() {
		return getPropertyAsLong("length");
	}

	/**
	 * @return volume
	 */
	public float getVolume() {
		return getPropertyAsFloat("volume");
	}

	/**
	 * @param volume
	 */
	public void setVolume(float volume) {
		setProperty("volume", volume);
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	protected String getProperty(String name) {
		if (name == null || mplayerProcess == null) {
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
	 * @param name
	 * @return
	 */
	protected long getPropertyAsLong(String name) {
		try {
			return Long.parseLong(getProperty(name));
		} catch (NumberFormatException exc) {
		} catch (NullPointerException exc) {
		}
		return 0;
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	protected float getPropertyAsFloat(String name) {
		try {
			return Float.parseFloat(getProperty(name));
		} catch (NumberFormatException exc) {
		} catch (NullPointerException exc) {
		}
		return 0f;
	}

	/**
	 *
	 * @param name
	 * @param value
	 */
	protected void setProperty(String name, String value) {
		execute("set_property " + name + " " + value);
	}

	/**
	 *
	 * @param name
	 * @param value
	 */
	protected void setProperty(String name, long value) {
		execute("set_property " + name + " " + value);
	}

	/**
	 *
	 * @param name
	 * @param value
	 */
	protected void setProperty(String name, float value) {
		execute("set_property " + name + " " + value);
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
		if (mplayerProcess != null) {
			logger.info("Send to MPlayer the command \"" + command + "\" and expecting "
					+ (expected != null ? "\"" + expected + "\"" : "no answer"));
			mplayerIn.print(command);
			mplayerIn.print("\n");
			mplayerIn.flush();
			logger.info("Command sent");
			if (expected != null) {
				String response = waitForAnswer(expected);
				logger.info("MPlayer command response: " + response);
				return response;
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
				while ((line = mplayerOutErr.readLine()) != null) {
					logger.info("Reading line: " + line);
					if (line.startsWith(expected)) {
						return line;
					}
				}
			} catch (IOException e) {
			}
		}
		return line;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		JMPlayer jmPlayer = new JMPlayer();

		// open a video file
		// jmPlayer.open(new File("video.avi"));
		jmPlayer.open();
		// skip 2 minutes
		// jmPlayer.setTimePosition(120);
		// set volume to 90%
		// jmPlayer.setVolume(90);
	}
}