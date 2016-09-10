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
import jamuz.process.check.FolderInfo;
import jamuz.Jamuz;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import javax.swing.ImageIcon;
import org.apache.commons.io.FilenameUtils;
import jamuz.utils.FileSystem;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import jamuz.utils.SSH;
import jamuz.utils.StringManager;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public abstract class VideoAbstract implements Comparable {

    protected TreeMap<String, FileInfoVideo> files;
    protected MyVideoAbstract myVideo;
    
    private String title;
	private String titleOri;
	private String year;
	protected List<String> genres;
    protected String genreStr;
	private String director;
	private List<String> writers; //TODO: Use this
    private String studio;
    private final String country;
    private String synopsis;
    protected Status status;
    private int imdbIdTop250Ranking; 
    private final String imdbId;
    private int runtime; //TODO: Use this
    private String mppaRating;

    abstract public String getRelativeFullPath();
    abstract public void moveFilesAndSrt(ProcessVideo.PathBuffer buffer, DbConnVideo conn, SSH myConn);
    abstract protected String getVideoSummary();
    abstract public void removeFavorite();
    abstract public void addFavorite();
    abstract public void removeFromWatchList();
    abstract public void addToWatchList();
    abstract public void setRating(VideoRating rating);
    abstract public void setMyVideo();
    abstract public boolean isLocal();
    abstract public boolean isWatched();
    
    protected VideoAbstract(String title, String synopsis,
            int ratingVotes, String writers, String year, 
            String imdbId, int runtime, String mppaRating, int imdbIdTop250Ranking, String genre, String director, 
            String titleOri, String studio, String trailerURL, String fanartURLs, String country) {
        
		this.title = title;
		this.writers = parseSlashList(writers);
		this.year = year;
        this.imdbIdTop250Ranking = imdbIdTop250Ranking;
		this.genres = parseSlashList(genre);
        this.genreStr = genre;
		this.director = director;
		this.titleOri = titleOri;
        this.studio = studio;
		this.trailerURL = trailerURL;
        this.status = new Status();
        this.synopsis = synopsis;
        this.runtime = runtime;
        this.mppaRating = mppaRating;
        if(mppaRating.contains(":")) {
            switch(mppaRating.split(":")[1]) { //TODO: Check if this is still valid for other scrappers as cinepassion ("France:-16" par ex.)
                //TODO: Use this switch to return an icon instead
                //Voir /home/raph/Creations/Dev/NetBeans/JaMuz/Source/JaMuz_DEV/data/Logos_adoptEs_par_le_csa_20020918.jpg
                case "Rated U": 
                    this.mppaRating = Inter.get("Label.GeneAudience"); //NOI18N
                    break;
                default: this.mppaRating = "";
            }
        }
        this.ratingVotes = ratingVotes;
//        this.fanarts = new ArrayList<>();
        this.fanarts = parseURLStringList(fanartURLs);     
        this.imdbId = imdbId;
        this.country = country;
        this.length = new MovieSize((long) 0);
        this.files = new TreeMap<>();
        
	}

    protected String moveFileAndSrt(ProcessVideo.PathBuffer buffer, DbConnVideo conn, SSH myConn, FileInfo fileInfo, String newName) {
        
        //Build new filename
          
        String newFileName = StringManager.removeIllegal(newName) + "." + fileInfo.getExt(); //NOI18N
        
        //TODO: Better Handle files with no info. Ex:
        ///home/raph/Vidéos/Films/Archive/Walt Disney/1937 - Blanche Neige.avi
        //newFileName="{Empty} [0].avi"
        if(newFileName.startsWith("{Empty} [0]")) { //NOI18N
//            fileInfo.setStatus(Inter.get("Msg.Video.NoInfo"));
            return Inter.get("Msg.Video.NoInfo");
        }
        
        //Build new path
        String originalPath = FilenameUtils.concat(conn.rootPath, fileInfo.getRelativePath());
        String newPath = originalPath;
        //FIXME: Manage the archival of movies and tv shows.
        //Does not work now that rootPath is "Vidéos" root folder and not "Films"
//        if(fileInfo.playCounter>0) {
//            FilenameUtils.getFullPathNoEndSeparator(originalPath);
//            newPath = FilenameUtils.concat(conn.rootPath, "Archive")+File.separator;  //NOI18N
//        }
        
        //Get new idPath from a repository for updating database after file move
        int newIdPath = buffer.getId(newPath);
        if(newIdPath<=0) { //Check that if is correct
//            fileInfo.setStatus(Inter.get("Msg.Video.PathNotFound"));
            return Inter.get("Msg.Video.PathNotFound");
        }
        
        //Build destination file
        String destination = FilenameUtils.concat(newPath, newFileName);
        
        //get source file
        String source = FilenameUtils.concat(conn.rootPath, fileInfo.getRelativeFullPath());

        //SRT source and destination
        String sourceSrt=FilenameUtils.concat(FilenameUtils.getFullPath(source), FilenameUtils.getBaseName(source)+".srt");  //NOI18N
        String destinationSrt=FilenameUtils.concat(FilenameUtils.getFullPath(destination), FilenameUtils.getBaseName(destination)+".srt");  //NOI18N
        
        //Only moving if it needs a change
        if(!source.equals(destination)) {
            if(moveFile(myConn, source, destination, sourceSrt, destinationSrt)) {
                //Update database with new file path and name
                if(!conn.updateFile(fileInfo.getIdFile(), newIdPath, newFileName)) {
//                    fileInfo.setStatus(Inter.get("Msg.Video.ErrorUpdatingDb"));
                    //Rename back
                    moveFile(myConn, destination, source, destinationSrt, sourceSrt);
                    //Not checking result as we could end up in an infinite loop
                    //A further XBMC library scan will fix the problem anyway
                    return Inter.get("Msg.Video.ErrorUpdatingDb");
                }
            }
        }
        return "";
    }
    
    protected String getStreamDetails4Filename(FileInfoVideo file) {
        String newName=" ["+file.getVideoStreamDetails()+"]";
        newName+=""+file.getAudioStreamDetails()+"";
        newName+=""+file.getSubtitlesStreamDetails()+"";
        return newName;
    }
    
    private boolean moveFile(SSH myConn, String source, String destination, String sourceSrt, String destinationSrt) {
        //Move (and rename) the file over SSH or locally 
        if(Jamuz.getOptions().get("video.SSH.enabled").equals("true")) {
            //TODO: Check source and destination (H2 over SSH ?)      

            //Try to reConnect if somehow disconnected
            if(!myConn.isConnected()) {
                if(!myConn.connect()) {
                    setStatus(Inter.get("Msg.Video.SSHConnectionIssue"));
                    return false;
                }
            }
            //Send the mv command
            if(!myConn.moveFile(source, destination)) {
                setStatus(Inter.get("Msg.Video.MoveFailed"));
                return false;
            }
            //Copy srt
            myConn.moveFile(sourceSrt, destinationSrt);
            //Not checking as it returns an error if source file does not exist but we are not checking it
            //TODO: Check if source file exist first, but does not seem to be an universal way
        }
        else {
            File sourceFile = new File(source);
            File destinationFile = new File(destination);
            if(!sourceFile.exists()) {
                setStatus(Inter.get("Msg.Video.SourceFileMissing"));
                return false;
            }
            if(destinationFile.exists()) {
                setStatus(Inter.get("Msg.Video.DestinationExist"));
                return false;
            }
            if(!FileSystem.moveFile(sourceFile, destinationFile)) {
                setStatus(Inter.get("Msg.Video.MoveFailed"));
                return false;
            }
            //Copy srt if it exists
            File sourceSrtFile = new File(sourceSrt);
            if(sourceSrtFile.exists()) {
                File destinationSrtFile = new File(destinationSrt);
                if(!FileSystem.moveFile(sourceSrtFile, destinationSrtFile)) {
                    setStatus(Inter.get("Msg.Video.MoveFailed"));
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * set status
     * @param status
     */
    public void setStatus(String status) {
        this.status.set(status);
    }
    
    /**
     * MPPA Rating
     * @return
     */
    public String getMppaRating() {
        return mppaRating;
    }
    public String getRating() {
        return String.valueOf(myVideo.getUserRating());
    }
    private final int ratingVotes; //TODO: Use this
	private String trailerURL; 
    protected List<String> thumbnails;
    protected List<String> fanarts; //TODO: Use this
    private boolean selected;
    private MovieSize length;
    
    /**
     * Video file Status
     */
    public static class Status {
        private String message;
        private boolean isOK;

        /**
         * Get status
         * @return
         */
        public String getMsg() {
            return message;
        }
        
        /**
         * create status
         */
        public Status() {
            this.message = ""; //NOI18N
            this.isOK = true;
        }

        @Override
        public String toString() {
            return (this.isOK) ? Inter.get("Check.OK") : Inter.get("Check.KO"); //NOI18N
        }
        
        /**
         * set status
         * @param status
         */
        protected void set(String status) {
            this.isOK = false;
            this.message += status + " | "; //NOI18N
        }
    }
  
    protected final List<String> parseURLStringList(String string) {
        List<String> URLs = new ArrayList<>();
        if(string!=null) {
            for(String splitted : string.split("preview=\"")) { //NOI18N
                if(splitted.startsWith("http")) { //NOI18N
                    splitted = splitted.substring(0, splitted.indexOf("\">")); //NOI18N
                    URLs.add(splitted);
                }
            }
        }
        return URLs;
    }
    

    
    private List<String> parseSlashList(String string) {
        return Arrays.asList(string.split(" / ")); //NOI18N
    }
    
    /**
     * get titel
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return
     */
    public TreeMap<String, FileInfoVideo> getFiles() {
        return files;
    }

    /**
     * get original title
     * @return
     */
    public String getTitleOri() {
        return titleOri;
    }

    /**
     *
     * @return
     */
    public List<String> getGenres() {
        return genres;
    }
    
    /**
     *
     * @return
     */
    public String getYear() {
        return year;
    }

//    /**
//     * get status
//     * @return
//     */
//    public Status getStatus() {
//        return status;
//    }

    //TODO: Offer user to choose the thumbnail (refer to CoverSelectGUI)

    /**
     * get thumbnail
     * @param readIfNotFound
     * @return
     */
    public ImageIcon getThumbnail(boolean readIfNotFound) {
        ImageIcon icon=null;
        for(String url : this.thumbnails) {
            icon=IconBufferVideo.getCoverIcon(url, readIfNotFound);
            if(icon!=null) {
                return icon;
            }
        }
        return icon;
    }

    /**
     * get trailer url
     * @return
     */
    public String getTrailerURL() {
        return trailerURL;
    }

    /**
     * get synopsis
     * @return
     */
    public String getSynopsis() {
        return "<html>"+synopsis+"</html>"; //NOI18N
//        return synopsis;
    }

    /**
     * get imdb uri
     * @return
     */
    public URI getImdbURI() {
        if(!imdbId.equals("")) { //NOI18N
            try {
                String uri = ("http://www.imdb.com/title/"+imdbId+"/").replaceAll(" ", "+"); //NOI18N
                return new URI(uri);
            } catch (URISyntaxException ex) {
                Popup.error(ex);
            }
        }
        return null;
    }

    /**
     *
     * @return
     */
    public URI getHomepage() {
        if(myVideo.getId()>0) { //NOI18N
            try {
                String uri =(myVideo.getHomepage()).replaceAll(" ", "+"); //NOI18N
                return new URI(uri);
            } catch (NullPointerException | URISyntaxException ex) {
                //Do nothing
            }
        }
        return null;
    }

    /**
     * is selected ?
     * @return
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * set selected
     * @param selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * set length
     * @param length
     */
    public void setLength(long length) {
        this.length = new MovieSize(length);
    }

    /**
     * get length
     * @return
     */
    public MovieSize getLength() {
        return length;
    }
 
    /**
     *
     * @return
     */
    public MyVideoAbstract getMyMovieDb() {
        return myVideo;
    }
    
    abstract public boolean isMovie();

    /**
     *
     * @param myMovieDb
     */
    public void setMyVideo(MyMovieDb myMovieDb) {
        this.myVideo = myMovieDb;
    }
    
    /**
     *
     * @param myTvShow
     */
    public void setMyVideo(MyTvShow myTvShow) {
        this.myVideo = myTvShow;
    }
    
    
    @Override
    public String toString() {
        String display = "<html><b>"+title+"</b>"; //NOI18N
        if(!titleOri.equals(title)) {
            display+= " <i>("+titleOri+")</i>"; //NOI18N
        }
//        display+= " {"+myVideo.getId()+"}";
        display+= "<BR/>";
        if(!status.isOK) {
            display+= "<font color=\"red\"> | "+status.getMsg()+"</font><BR/>"; //NOI18N
        }
        if(!genreStr.equals("")) { //NOI18N
            display+= genreStr + "<BR/>"; //NOI18N
        }
        if(!director.equals("")) { //NOI18N
            display+= " de " + director + "<BR/>"; //NOI18N
        }
        display+= getVideoSummary();
        
        if(!country.equals("")) { //NOI18N
            display+= country + "<BR/>"; //NOI18N
        }
        display+= studio + "<BR/>"; //NOI18N
        if(imdbIdTop250Ranking>0) {
            display+= "IMDB: " + imdbIdTop250Ranking + "/250<BR/>"; //NOI18N
        }
        display += mppaRating +"</html>"; //NOI18N
        return display;
    }

    /**
     *
     * @return
     */
    public boolean isHD() {
        ArrayList<String> areHD = FolderInfo.group(new ArrayList(files.values()), "isHD");
        
        if(areHD.size()==1) {
            if(areHD.get(0).equals("true")) {
                return true;
            }
        }
        return false;
    }
    
    /**
	 * Overring method for sorting by title
	 * @param o
	 * @return
	 */
	@Override
	public int compareTo(Object o) {
		return (this.title.compareTo(((VideoAbstract) o).title));
	}
    
    /**
     *
     */
    public class MovieSize implements Comparable {
        private final Long length;

        /**
         *
         * @param length
         */
        public MovieSize(Long length) {
            this.length = length;
        }

        /**
         *
         * @return
         */
        public Long getLength() {
            return length;
        }

        @Override
        public String toString() {
            return StringManager.humanReadableByteCount(length, false);
        }

        @Override
        public int compareTo(Object o) {
//            if (this.length < ((MovieSize) o).length) return -1;
//            if (this.length > ((MovieSize) o).length) return 1;
//            
//            return 0;
            return this.length.compareTo(((MovieSize) o).length);
        }

    }
}
