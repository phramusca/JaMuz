/*
 * Copyright (C) 2015 phramusca ( https://github.com/phramusca/JaMuz/ )
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
package jamuz.process.video;

import jamuz.FileInfo;
import jamuz.Jamuz;
import java.util.ArrayList;
import java.util.List;
import jamuz.utils.StringManager;
import java.io.File;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class FileInfoVideo extends FileInfo {

    private int seasonNumber=0;
    private int episodeNumber=0;
	private Quality quality = Quality.UNKNOWN;
	private String duration;
	private String subtitlesStreamDetails="";
	private String audioStreamDetails="";

    /**
     *
     * @param idFile
     * @param idPath
     * @param relativeFullPath
     * @param rating
     * @param lastPlayed
     * @param addedDate
     * @param playCounter
     * @param streamDetails
     * @param seasonNumber
     * @param episodeNumber
     */
    public FileInfoVideo(int idFile, int idPath, String relativeFullPath, 
			int rating, String lastPlayed, String addedDate, int playCounter, 
            StreamDetails streamDetails, int seasonNumber, int episodeNumber) {
        super(idFile, idPath, relativeFullPath, rating, lastPlayed, addedDate, 
				playCounter, "", 0, 0, "genre", "", "", "");
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
		
		if(streamDetails.video.size()>0) {
            //TODO: Color if for HTML
            StreamDetails.VideoStream videoStream = streamDetails.video.get(0);
            long size = videoStream.width*videoStream.height;
			
			//TODO: Height can be less than max if ratio not 16:9
			//Is it specified by aspect ?
			
            if(size >= 1900*1000) { // 1080 : 1 920 × 1 080
                quality = Quality.HD1080;
            } 
            else if(size >= 1200*700) { // 720  : 1 280 × 720
                quality = Quality.HD720;
            } 
            else {
                quality= Quality.SD;
            }
			duration = StringManager.secondsToHHMM(videoStream.duration);
			
			for(String language : streamDetails.subtitles) {
                subtitlesStreamDetails += " [ST " + language.toUpperCase() + "]"; //NOI18N
            }
			
			for(FileInfoVideo.StreamDetails.AudioStream audioStream : streamDetails.audio) {
                //TODO: Offer this has an option
//                newName += " [" + audioStream.codec + " " + "(" + audioStream.channels + ")"; //NOI18N
                if(!audioStream.language.startsWith("{")) {
                    audioStreamDetails += " [" + audioStream.language.toUpperCase()+"]"; //NOI18N
                }
//                newName += "]"; //NOI18N
            }
        }
    }

	/**
	 *
	 */
	public FileInfoVideo() {
        super("");
    }
    
	/**
	 *
	 * @return
	 */

    public boolean isHD() {
		return quality == Quality.HD1080
			|| quality == Quality.HD720;
    }
    
	/**
	 *
	 * @return
	 */
	public String getVideoStreamDetails() {
		//TODO: Offer as an option
//		display += " [" + videoStream.codec + " " + videoStream.width + "x" + videoStream.height + "] "; //NOI18N

		if(!quality.equals(Quality.UNKNOWN) && duration!=null) {
			return quality + " - " + duration;
		} else if(quality.equals(Quality.UNKNOWN) && duration!=null) {
			return duration;
		} else if(!quality.equals(Quality.UNKNOWN) && duration==null) {
			return quality.toString();
		} else {
			return "";
		}
    }

	/**
	 *
	 * @return
	 */
	public String getAudioStreamDetails() {
        return audioStreamDetails;
    }
    
	/**
	 *
	 * @return
	 */
	public String getSubtitlesStreamDetails() {
        return subtitlesStreamDetails;
    }

	File getVideoFile() {
		return new File(FilenameUtils.concat(
					Boolean.parseBoolean(Jamuz.getOptions().get("video.library.remote"))?
							Jamuz.getOptions().get("video.location.library")
							:Jamuz.getOptions().get("video.rootPath"), 
					relativeFullPath));
	}
    
	public enum Quality {
		HD1080("HD 1080"), //NOI18N
		HD720("HD 720"), //NOI18N
		SD("SD"),
		UNKNOWN(""); //NOI18N
        
        private final String display;
		
        private Quality(String display) {
            this.display = display;
		}

        @Override
		public String toString() {
			return display;
		}
	}
	
    /**
     *
     */
    public static class StreamDetails {

		/**
		 *
		 */
		protected final List<AudioStream> audio;

		/**
		 *
		 */
		protected final List<VideoStream> video;

		/**
		 *
		 */
		protected final List<String> subtitles;

        /**
         *
         */
        public StreamDetails() {
            this.audio = new ArrayList<>();
            this.video = new ArrayList<>();
            this.subtitles = new ArrayList<>();
        }
        
        /**
         *
         * @param codec
         * @param channels
         * @param language
         */
        public void addAudioStream(String codec, int channels, String language) {
            this.audio.add(new AudioStream(codec, channels, language));
        }
        
        /**
         *
         * @param codec
         * @param aspect
         * @param width
         * @param height
         * @param duration
         */
        public void addVideoStream(String codec, double aspect, int width, int height, int duration) {
            this.video.add(new VideoStream(codec, aspect, width, height, duration));
        }
        
        /**
         *
         * @param language
         */
        public void addSubtitle(String language) {
            if(!language.startsWith("{")) {
                this.subtitles.add(language);
            }
        }

        /**
         *
         * @return
         */
        public List<AudioStream> getAudio() {
            return audio;
        }

        /**
         *
         * @return
         */
        public List<VideoStream> getVideo() {
            return video;
        }

        /**
         *
         * @return
         */
        public List<String> getSubtitles() {
            return subtitles;
        }

        /**
         *
         */
        public class AudioStream {

			/**
			 *
			 */
			protected final String codec;

			/**
			 *
			 */
			protected final int channels;
  
			/**
			 *
			 */
			protected final String language;  

            /**
             *
             * @param codec
             * @param channels
             * @param language
             */
            public AudioStream(String codec, int channels, String language) {
                this.codec = codec;
                this.channels = channels;
                this.language = language;
            }
        }
        
        /**
         *
         */
        public class VideoStream {

			/**
			 *
			 */
			protected final String codec;

			/**
			 *
			 */
			protected final double aspect;

			/**
			 *
			 */
			protected final int width;

			/**
			 *
			 */
			protected final int height;

			/**
			 *
			 */
			protected final int duration;

            /**
             *
             * @param codec
             * @param aspect
             * @param width
             * @param height
             * @param duration
             */
            public VideoStream(String codec, double aspect, int width, int height, int duration) {
                this.codec = codec;
                this.aspect = aspect;
                this.width = width;
                this.height = height;
                this.duration = duration;
            }
        }
    }
	
	/**
	 *
	 * @return
	 */
	public int getSeasonNumber() {
        return seasonNumber;
    }

	/**
	 *
	 * @return
	 */
	public int getEpisodeNumber() {
        return episodeNumber;
    }
}