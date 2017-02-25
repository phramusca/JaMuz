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

package jamuz.player;

import jamuz.gui.PanelMain;
import jamuz.gui.PanelLyrics;
import jamuz.utils.Popup;
import jamuz.utils.Inter;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.advanced.*;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;

/**
 * JLayer MP3 player
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class PlayerMP3 extends PlaybackListener implements Runnable
{
    private String filePath;
    private AdvancedPlayer player;
    private Thread playerThread;    
	
	private int length;
	private double frameLength;
	private int frameStart=0;
	private int frameEnd=0;
	
	private AudioDevice device;
	private Updater positionUpdater;
	/**
	 * Lock position slider update when it is manually changed
	 */
	public boolean positionLock=false;
	private int positionStart=0;
    private int lastPosition=0;
	private boolean goNext=true;

	private double getSamplesPerFrame(String format) {
		
		//INFO: frames length
		// http://www.mp3-converter.com/mp3codec/frames.htm
		
		//Parse format
		String version = format.substring(format.indexOf("-")+1, format.indexOf("-")+2);  //NOI18N
		String layer = format.substring(format.length()-1, format.length());
		
        if (version.equals("1")) {   // MPEG Version 1  //NOI18N
			switch (layer) {
				case "1":  //NOI18N
					return 384;    // Layer1
				case "2":  //NOI18N
					return 1152;   // Layer2
				case "3":  //NOI18N
					return 1152;    // Layer3
			}
        }
		if (version.equals("2")) {	// MPEG Version 2 & 2.5  //NOI18N
			switch (layer) {
				case "1":  //NOI18N
					return 384;  // Layer1
				case "2":  //NOI18N
					return 1152;   // Layer2
				case "3":  //NOI18N
					return 576;     // Layer3
			}
        }
		//The typical MP3 (an MPEG Layer III, version 1) has 1152 samples per frame
		//So using this as default if format parsing did not work as expected
		return 1152;
    }
	
    /**
	 * Plays MP3 file
	 * @param filePath
     * @param resume
	 * @return
	 */
	public String play(String filePath, boolean resume) {
        try
        {
			this.stop();
			
			this.frameStart=0;
			this.positionStart=0;
			this.filePath = filePath;
			
			//TODO: Read header information and store in db the usual way to avoid reading again
			//header here. Warning: may cause issues if for some reason the file
			//has changed since last scan.
			
			//Getting MP3 information from header
			MP3File myMP3File = (MP3File)AudioFileIO.read(new File(filePath));
			AudioHeader myHeader = myMP3File.getAudioHeader();
			
			length=myHeader.getTrackLength();
			//Frame length (in ms) = (samples per frame / sample rate (in hz)) * 1000
			frameLength = getSamplesPerFrame(myHeader.getFormat()) / (double) myHeader.getSampleRateAsNumber();
			double nbFrames = (length*1000/frameLength);
			frameEnd = (int) nbFrames;
			//The typical MP3 (an MPEG Layer III, version 1) has 1152 samples per frame 
			//and the sample rate is (commonly) 44100 hz. 
			//Notice the frame length (time) does not depend on the bitrate

            //Resume to previous position
            if(!resume) lastPosition=0;
            this.setPosition(lastPosition/1000);
            
			String lyrics = myMP3File.getID3v2Tag().getFirst(FieldKey.LYRICS);
			
			return lyrics;
        }
        catch (IOException | CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException ex)
        {
			Popup.error(Inter.get("Error.Play")+" \""+filePath+"\"", ex);  //NOI18N
			return "";  //NOI18N
        } 
    }
	
	private void play() {
		try {
			this.goNext=true;
			
			//TODO: Move the following to play(String filePath)
			//Warning: not that easy ... !
			FileInputStream fis = new FileInputStream(this.filePath);
			BufferedInputStream bis = new BufferedInputStream(fis);
			this.device = new JavaSoundAudioDevice();
			this.player = new AdvancedPlayer(bis, device);
			this.player.setPlayBackListener(this);		
			this.playerThread = new Thread(this, "Thread.PlayerMP3.play");  //NOI18N
			//End of move TODO
			this.playerThread.start();
		} catch (FileNotFoundException | JavaLayerException ex) {
			Popup.error(Inter.get("Error.Play")+" \""+filePath+"\"", ex);  //NOI18N
		}
	}
	
	/**
	 *
	 */
	public void pause() {
		if(device!=null) {
			this.lastPosition=positionStart*1000+device.getPosition();
			stop();
		}
    }
    
	/**
	 * Stops playback
	 */
	public void stop() { 
		this.goNext=false;
		
		if (player != null) {
			player.stop();
			player=null;
		}
	}

	/**
	 * Forward by given number of seconds
	 * @param seconds
	 */
	public void forward(int seconds) {
		this.setPosition((positionStart*1000+device.getPosition())+(seconds*1000));
	}
	
	/**
	 * Rewind by given number of seconds
	 * @param seconds
	 */
	public void rewind(int seconds) {
		this.setPosition((positionStart*1000+device.getPosition())-(seconds*1000));
	}
	
	/**
	 * Moves to given position in seconds
	 * @param seconds
	 */
	public void setPosition(int seconds) {
		//Stopping any previous play
		this.stop();
		//Change start position
		positionStart=seconds;
		frameStart = (int) (positionStart/frameLength);
		//Play from that position
		this.play();
	}

	/**
	 * When playback starts, starts position updater
	 * @param playbackEvent
	 */
	@Override
    public void playbackStarted(PlaybackEvent playbackEvent)
    {
		positionUpdater = new Updater();
		positionUpdater.start();
    }

	/**
	 * When playback is finished, reset position updater and plays next as requested
	 * @param playbackEvent
	 */
	@Override
    public void playbackFinished(PlaybackEvent playbackEvent)
    {
		player=null;
		positionUpdater.cancel();
		positionUpdater.purge();
		if(goNext) {
//			PanelMain.next();
		}
    }    

    // Runnable members
	@Override
    public void run()
    {
        try
        {
			player.play(frameStart, frameEnd - frameStart);
        }
        catch (javazoom.jl.decoder.JavaLayerException ex)
        {
            //No real issues as audio continues playing
            //Happens when forwarding too fast for example
//            Popup.error(ex);
        }
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
//					PanelMain.dispMP3progress(positionStart*1000+device.getPosition());
                    PanelLyrics.setPosition(positionStart*1000+device.getPosition(), length);
				}
			}
		}
	}
	
}
