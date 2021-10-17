
/* libFLAC - Free Lossless Audio Codec library
* Copyright (C) 2000,2001,2002,2003 Josh Coalson
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Library General Public
* License as published by the Free Software Foundation; either
* version 2 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Library General Public License for more details.
*
* You should have received a copy of the GNU Library General Public
* License along with this library; if not, write to the
* Free Software Foundation, Inc., 59 Temple Place - Suite 330,
* Boston, MA 02111-1307, USA.
*/

package jamuz.player;

import jamuz.utils.Inter;
import jamuz.utils.Popup;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;
import org.kc7bfi.jflac.FLACDecoder;
import org.kc7bfi.jflac.PCMProcessor;
import org.kc7bfi.jflac.metadata.StreamInfo;
import org.kc7bfi.jflac.util.ByteData;

/**
* Play a FLAC file application.
* @author kc7bfi
*/
public class PlayerFlac implements PCMProcessor, LineListener, Runnable {
    private AudioFormat fmt;
    private DataLine.Info info;
    private SourceDataLine line;
    private final List listeners;
    private Thread playerThread;
    private String filePath;
    private FLACDecoder decoder;
    
    /**
     * FLAC player
     */
    public PlayerFlac() {
        this.listeners = new ArrayList();
    }
    
    private void play() {
        this.playerThread = new Thread(this, "Thread.PlayerFLAC.play");  //NOI18N
        this.playerThread.start();
    }
    
	/**
	 *
	 */
	public void stop() { 
        if (decoder != null) {
            decoder.removePCMProcessor(this);
            decoder=null;
        }
        //TODO PLAYER FLAC: better Manage Stop (stops sometimes), 
		//TODO PLAYER FLAC: better Manage "setPosition", ideally as for MP3
	}
    
	/**
	 *
	 * @param filePath
	 * @return
	 */
	public String play(String filePath) {
        try
        {
			this.stop();

			this.filePath = filePath;

			this.play();
            AudioFile audioFile = AudioFileIO.read(new File(filePath));
			String lyrics = audioFile.getTag().getFirst(FieldKey.LYRICS);
			
			return lyrics;
        }
        catch (IOException | CannotReadException | TagException | ReadOnlyFileException | InvalidAudioFrameException ex)
        {
			Popup.error(Inter.get("Error.Play")+" \""+filePath+"\"", ex);  //NOI18N
			return "";  //NOI18N
        } 
    }
    
    /**
     * add a listener
     * @param listener
     */
    public void addListener (LineListener listener)
    {
        listeners.add(listener);
    }
    
    /**
    * Decode and play an input FLAC file.
    * @param inFileName The input FLAC file name
    */
    private void decode(String inFileName) {
        FileInputStream is = null;
        try {
            is = new FileInputStream(inFileName);
            decoder = new FLACDecoder(is);
            decoder.addPCMProcessor(this);
            addListener(this);
            decoder.decode();
            
            line.drain();
            line.close();
            // We're going to clear out the list of listeners as well, so that everytime through
            // things are basically at the same starting point.
            listeners.clear();
        } catch (IOException ex) {
            Popup.error(ex);
        } finally {
//            this.stop();
        }
    }
    
    /**
    * Process the StreamInfo block.
    * @param streamInfo the StreamInfo block
    * @see org.kc7bfi.jflac.PCMProcessor#processStreamInfo(org.kc7bfi.jflac.metadata.StreamInfo)
    */
    @Override
    public void processStreamInfo(StreamInfo streamInfo) {
        try {
            fmt = streamInfo.getAudioFormat();
            info = new DataLine.Info(SourceDataLine.class, fmt, AudioSystem.NOT_SPECIFIED);
            line = (SourceDataLine) AudioSystem.getLine(info);
            // Add the listeners to the line at this point, it's the only
            // way to get the events triggered.
            int size = listeners.size();
            for (int index = 0; index < size; index++) {
				line.addLineListener((LineListener) listeners.get(index));
			}
            
            line.open(fmt, AudioSystem.NOT_SPECIFIED);
            line.start();
        } catch (LineUnavailableException e) {
            Popup.error(e);
        }
    }
    
    /**
    * Process the decoded PCM bytes.
    * @param pcm The decoded PCM data
    * @see org.kc7bfi.jflac.PCMProcessor#processPCM(org.kc7bfi.jflac.util.ByteSpace)
    */
    @Override
    public void processPCM(ByteData pcm) {
        line.write(pcm.getData(), 0, pcm.getLen());
    }
    
    /**
     *
     * @param listener
     */
    public void removeListener (LineListener listener)
    {
        listeners.remove(listener);
    }

	/**
	 *
	 */
	@Override
    public void run() {
        this.decode(filePath);
    }

	/**
	 *
	 * @param event
	 */
	@Override
    public void update(LineEvent event) {
        //TODO PLAYER FLAC: How to display progress ??
        
        //This is called on "Open" and "Start" events only
//        PanelMain.dispMP3progress((int) event.getFramePosition());
//        PanelLyrics.setPosition(positionStart*1000+device.getPosition(), length);
    }
}
