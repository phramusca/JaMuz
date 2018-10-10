/*
 * Copyright (C) 2017 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import com.beaglebuddy.ape.APETag;
import com.beaglebuddy.mp3.MP3;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.AbstractTagFrameBody;
import org.jaudiotagger.tag.id3.framebody.FrameBodyTXXX;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class ReplayGain {

	//http://wiki.hydrogenaud.io/index.php?title=ReplayGain_2.0_specification#Metadata_format
	public static GainValues read(File path, String ext) {
		GainValues gainValues=new GainValues();
		switch(ext) {
			case "mp3":
				//First try reading from APE tags (default mp3gain storage)
				gainValues=readReplayGainFromAPE(path);
				if(!gainValues.isValid()) {
					//If not found, read from ID3 (written by some other tool, foobar2000 maybe ?)
					gainValues=readReplayGainFromID3(path);
				}
				break;
			case "flac":
				//http://www.bobulous.org.uk/misc/Replay-Gain-in-Linux.html#flac-and-metaflac
				gainValues=readReplayGainFromFlac(path);
				break;
			case "ogg":
				//http://www.bobulous.org.uk/misc/Replay-Gain-in-Linux.html#vorbisgain
				//TODO: Support ReplayGain for Ogg Vorbis
			default:
				//Not supported
		}
		return gainValues;
	}
	
	public static class GainValues {
		private float albumGain=Float.NaN;
		private float trackGain=Float.NaN;
		
		public float trackPeak; //Not used
		public float albumPeak; //Not used
		public String MP3GAIN_MINMAX; //mp3gain only, Not used
		public String MP3GAIN_ALBUM_MINMAX; //mp3gain only, Not used
		public String MP3GAIN_UNDO; //mp3gain only, Not used
		public String REPLAYGAIN_REFERENCE_LOUDNESS; //Flac only, Not used

		@Override
		public String toString() {
			return "albumGain=" + albumGain + ", trackGain=" + trackGain;
		}
		
		public boolean isValid() {
			return !Float.isNaN(albumGain) && !Float.isNaN(trackGain);
		}

		public float getAlbumGain() {
			return albumGain;
		}

		public float getTrackGain() {
			return trackGain;
		}
	}
	
	private static GainValues readReplayGainFromFlac(File path) {
		GainValues gv = new GainValues();
		try {
			org.jaudiotagger.audio.AudioFile f = AudioFileIO.read(path);
			FlacTag tag = (FlacTag) f.getTag();
			VorbisCommentTag vcTag = tag.getVorbisCommentTag();

			gv.REPLAYGAIN_REFERENCE_LOUDNESS = vcTag.getFirst("REPLAYGAIN_REFERENCE_LOUDNESS");
			gv.trackGain = getFloatFromString(vcTag.getFirst("REPLAYGAIN_TRACK_GAIN"));
			gv.albumGain = getFloatFromString(vcTag.getFirst("REPLAYGAIN_ALBUM_GAIN"));
			gv.albumPeak = getFloatFromString(vcTag.getFirst("REPLAYGAIN_ALBUM_PEAK"));
			gv.trackPeak = getFloatFromString(vcTag.getFirst("REPLAYGAIN_TRACK_PEAK"));
		} catch (CannotReadException | IOException | TagException | 
				ReadOnlyFileException | InvalidAudioFrameException ex) {
			Logger.getLogger(ReplayGain.class.getName()).log(Level.SEVERE, null, ex);
		}
		return gv;
	}
	
	//http://www.beaglebuddy.com/
	private static GainValues readReplayGainFromAPE(File path) {
		GainValues gv = new GainValues();
		try {
			MP3 mp3 = new MP3(path);
			if (mp3.hasAPETag()) {
//				mp3 contains an APEv2 tag
//				MP3GAIN_MINMAX : 052,209
//				MP3GAIN_ALBUM_MINMAX : 052,209
//				MP3GAIN_UNDO : +001,+001,N
//				REPLAYGAIN_TRACK_GAIN : +0.920000 dB
//				REPLAYGAIN_TRACK_PEAK : 0.860053
//				REPLAYGAIN_ALBUM_GAIN : +0.400000 dB
//				REPLAYGAIN_ALBUM_PEAK : 0.899413
				APETag apeTag = mp3.getAPETag();
//				System.out.println("mp3 contains an " + apeTag.getVersionString() + " tag");
//				System.out.println(apeTag);
				apeTag.getItems().stream().filter((item) -> (item.isValueText())).forEach((item) -> {
					switch (item.getKey().toUpperCase()) {
						case "REPLAYGAIN_TRACK_GAIN":
							gv.trackGain = getFloatFromString(item.getTextValue());
							break;
						case "REPLAYGAIN_ALBUM_GAIN":
							gv.albumGain = getFloatFromString(item.getTextValue());
							break;
						case "REPLAYGAIN_ALBUM_PEAK":
							gv.albumPeak = getFloatFromString(item.getTextValue());
							break;
						case "REPLAYGAIN_TRACK_PEAK":
							gv.trackPeak = getFloatFromString(item.getTextValue());
							break;
						case "MP3GAIN_MINMAX":
							gv.MP3GAIN_MINMAX = item.getTextValue();
							break;
						case "MP3GAIN_ALBUM_MINMAX":
							gv.MP3GAIN_ALBUM_MINMAX = item.getTextValue();
							break;
						case "MP3GAIN_UNDO":
							gv.MP3GAIN_UNDO = item.getTextValue();
							break;
						default:
							break;
					}
				});
			}
		}
		catch (IOException 
				| NegativeArraySizeException ex) {
			Logger.getLogger(ReplayGain.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
		}
		return gv;
    }

	private static float getFloatFromString(String dbFloat) {
		float rg_float = Float.NaN;
		try {
			String nums = dbFloat.replaceAll("[^0-9.-]","");
			rg_float = Float.parseFloat(nums);
		} catch(Exception ex) {
		}
		return rg_float;
	}

	//TODO: Check if jaudiotagger > 2.2.6 supports REPLAYGAIN tags as generic fields
	//https://bitbucket.org/ijabz/jaudiotagger/issues/37/add-generic-support-for-reading-writing
	
	private static GainValues readReplayGainFromID3(File path) {
		GainValues gv = new GainValues();
		try {
			MP3File mp3file = (MP3File)AudioFileIO.read(path);
			AbstractID3v2Tag v2tag = mp3file.getID3v2Tag();
			
			Iterator i = v2tag.getFrameOfType("TXXX");
			while (i.hasNext()) {
				Object obj = i.next();
				if (obj instanceof AbstractID3v2Frame) {
					AbstractTagFrameBody af = ((AbstractID3v2Frame) obj).getBody();
					if (af instanceof FrameBodyTXXX) {
						FrameBodyTXXX fb = (FrameBodyTXXX) af;
						switch (fb.getDescription().toUpperCase()) {
							case "REPLAYGAIN_TRACK_GAIN":
								gv.trackGain = getFloatFromString(fb.getTextWithoutTrailingNulls());
								break;
							case "REPLAYGAIN_ALBUM_GAIN":
								gv.albumGain = getFloatFromString(fb.getTextWithoutTrailingNulls());
								break;
							case "REPLAYGAIN_ALBUM_PEAK":
								gv.albumPeak = getFloatFromString(fb.getTextWithoutTrailingNulls());
								break;
							case "REPLAYGAIN_TRACK_PEAK":
								gv.trackPeak = getFloatFromString(fb.getTextWithoutTrailingNulls());
								break;
							case "MP3GAIN_MINMAX":
								gv.MP3GAIN_MINMAX = fb.getTextWithoutTrailingNulls();
								break;
							case "MP3GAIN_ALBUM_MINMAX":
								gv.MP3GAIN_ALBUM_MINMAX = fb.getTextWithoutTrailingNulls();
								break;
							case "MP3GAIN_UNDO":
								gv.MP3GAIN_UNDO = fb.getTextWithoutTrailingNulls();
								break;
							default:
								break;
						}
					}
				}
			}
		} catch (CannotReadException 
				| IOException 
				| TagException 
				| ReadOnlyFileException 
				| InvalidAudioFrameException ex) {
			Logger.getLogger(ReplayGain.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
		}
		return gv;
	}

}
