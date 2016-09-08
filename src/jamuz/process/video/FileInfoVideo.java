/*
 * Copyright (C) 2015 raph
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
import java.util.ArrayList;
import java.util.List;
import jamuz.utils.Popup;
import jamuz.utils.StringManager;

/**
 *
 * @author raph
 */
public class FileInfoVideo extends FileInfo {

    private int seasonNumber=0;
    private int episodeNumber=0;

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }
    
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
    public FileInfoVideo(int idFile, int idPath, String relativeFullPath, int rating, String lastPlayed, String addedDate, int playCounter, 
            StreamDetails streamDetails, int seasonNumber, int episodeNumber) {
        super(idFile, idPath, relativeFullPath, rating, lastPlayed, addedDate, playCounter, "", 0, 0, "");
        this.streamDetails = streamDetails;
        this.seasonNumber = seasonNumber;
        this.episodeNumber = episodeNumber;
    }

    public FileInfoVideo() {
        super("");
    }
    
    /**
     *
     */
    private StreamDetails streamDetails = new StreamDetails();

//    public StreamDetails getStreamDetails() {
//        return streamDetails;
//    }

    public boolean isHD() {
        //TODO: Why is this function called so may times for the same movie ??
        if(streamDetails.video.size()>=1) {
            StreamDetails.VideoStream videoStream = streamDetails.video.get(0);
            long size = videoStream.height*videoStream.width;
            return size >= 1200*700; //(some may be slightly lower than 1280*720)
        }
//        else if(streamDetails.video.size()>1) {
        //This happened in "Made In France" (h264 and jpeg !!). VLC shows a "Piste 2" video which displays the same.
        //Though we cannot popup an error each time
//            Popup.warning("HD issue");
//        }
        return false;
    }
    
    public String getVideoStreamDetails() {
        if(streamDetails.video.size()>=1) {
            //TODO: Color if for HTML
            StreamDetails.VideoStream videoStream = streamDetails.video.get(0);
            long size = videoStream.height*videoStream.width;
            String display;
            if(size >= 1900*1000) { // 1080 : 1 920 × 1 080 //(some may be slightly lower)
                display= "HD 1080";
            } 
            else if(size >= 1200*700) { // 720  : 1 280 × 720
                display= "HD 720";
            } 
            else {
                display= "SD";
            } 
            display += " - " + StringManager.secondsToHHMM(videoStream.duration);
            
            //TODO: Offer as an option
//            display += " [" + videoStream.codec + " " + videoStream.width + "x" + videoStream.height + "] "; //NOI18N
            return display;
        }
//        else if(streamDetails.video.size()>1) {
        // Refer to isHD() : same problem
//            return "More than one video stream";
//        }
        else {
            return "";
        }
    }
    
    public String getAudioStreamDetails() {
        String display="";
        if(streamDetails.audio.size()>0) {
            for(FileInfoVideo.StreamDetails.AudioStream audioStream : streamDetails.audio) {
                //TODO: Offer this has an option
//                newName += " [" + audioStream.codec + " " + "(" + audioStream.channels + ")"; //NOI18N
                if(!audioStream.language.startsWith("{")) {
                    display += " [" + audioStream.language.toUpperCase()+"]"; //NOI18N
                }
//                newName += "]"; //NOI18N
            }
        }
        return display;
    }
    
    public String getSubtitlesStreamDetails() {
        String display="";
        if(streamDetails.subtitles.size()>0) {
            for(String language : streamDetails.subtitles) {
                display += " [ST " + language.toUpperCase() + "]"; //NOI18N
            }
        }
        return display;
    }
    
    /**
     *
     */
    public static class StreamDetails {
        protected final List<AudioStream> audio;
        protected final List<VideoStream> video;
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
            protected final String codec;
            protected final int channels;
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
            protected final String codec;
            protected final double aspect;
            protected final int width;
            protected final int height;
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
    
}
