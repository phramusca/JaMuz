/*
 * Copyright (C) 2013 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;
import jamuz.process.video.FileInfoVideo.StreamDetails;
import jamuz.process.check.FolderInfo;
import jamuz.process.video.ProcessVideo.PathBuffer;
import java.util.ArrayList;
import jamuz.utils.SSH;
import jamuz.utils.StringManager;

/**
 * Video file information class
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class VideoMovie extends VideoAbstract {

    
	/**
	 * Create a new movie information instance
	 * @param title
     * @param synopsis
     * @param synopsis2
     * @param synopsis3
     * @param ratingVotes
	 * @param writers
	 * @param year
     * @param thumbnails
     * @param imdbId
     * @param runtime
     * @param mppaRating
     * @param imdbIdTop250Ranking
     * @param genre
	 * @param director
	 * @param titleOri
     * @param studio
	 * @param trailerURL
     * @param fanartURLs
     * @param country
	 * @param relativeFullPath
	 * @param rating
	 * @param lastPlayed
	 * @param addedDate
	 * @param playCounter
     * @param idFile
     * @param idPath
     * @param streamDetails
	 */
	public VideoMovie(String title, String synopsis, String synopsis2, String synopsis3, 
            int ratingVotes, int rating, String writers, String year, String thumbnails,
            String imdbId, int runtime, String mppaRating, int imdbIdTop250Ranking, String genre, String director, 
            String titleOri, String studio, String trailerURL, String fanartURLs, String country,  
            int idFile, int idPath, String relativeFullPath, int playCounter, String lastPlayed, String addedDate, StreamDetails streamDetails) {
        super(title, synopsis,
            ratingVotes, writers, year, 
            imdbId, runtime, mppaRating, imdbIdTop250Ranking, genre, director, 
            titleOri, studio, trailerURL, fanartURLs, country);
        this.files.put("", new FileInfoVideo(idFile, idPath, relativeFullPath, rating, lastPlayed, addedDate, playCounter, streamDetails, 1, 1));
        this.thumbnails = parseURLStringList(thumbnails);
        myVideo = new MyMovieDb(new MovieDb());
	}
    
    /**
     * TheMovieDb only Movie
     * @param myMovieDb
     */
    public VideoMovie(MyMovieDb myMovieDb) {
        
        super(myMovieDb.getMovieDb().getTitle(), 
                StringManager.getNullableText(myMovieDb.getMovieDb().getOverview()), 
                Math.round(myMovieDb.getMovieDb().getVoteAverage()), 
                "", 
                String.valueOf(myMovieDb.getYear()), 
                myMovieDb.getMovieDb().getImdbID()==null?"":StringManager.getNullableText(myMovieDb.getMovieDb().getImdbID()), 
                myMovieDb.getMovieDb().getRuntime(), 
                myMovieDb.getMovieDb().isAdult()?"Adult":"", 
                0, 
                "", "", 
                StringManager.getNullableText(myMovieDb.getMovieDb().getOriginalTitle()), 
                "", "", null, "");
        
        this.files.put("", new FileInfoVideo());
        
		this.myVideo = myMovieDb;
        
        this.status = new Status();
        this.status.set("No local file. From theMovieDb.org");
        
        MovieDb movieDb = myMovieDb.getMovieDb();

        this.thumbnails = new ArrayList<>();
        this.thumbnails.add("https://image.tmdb.org/t/p/w396"+movieDb.getPosterPath());
        
        this.fanarts = new ArrayList<>();
        this.fanarts.add("https://image.tmdb.org/t/p/w396"+movieDb.getBackdropPath());

		this.genres = new ArrayList<>();
        this.genreStr = "";
        if(movieDb.getGenres()!=null) {
            for(Genre genre : movieDb.getGenres()) {
                this.genres.add(genre.getName());
                genreStr+=" / "+genre.getName();
            }
        }
        
        movieDb.getPopularity(); //TODO: Use this
        movieDb.getVoteCount(); //TODO: Use thos
	}
    
    @Override
    protected String getVideoSummary() {
        
        //Movie only have one file in list
        FileInfoVideo fileInfoVideo = files.firstEntry().getValue();
        String display="Video: "+fileInfoVideo.getVideoStreamDetails() + "<BR/>";
        display+="Audio: "+fileInfoVideo.getAudioStreamDetails()+fileInfoVideo.getSubtitlesStreamDetails()+"<BR/>";
        return display;
    }

    @Override
    public void moveFilesAndSrt(PathBuffer buffer, DbConnVideo conn, SSH myConn) {
        
        //There should be only one file in files, list is for tv shows only
        for(FileInfoVideo file : files.values()) { 
            String newName = getTitle();
            if(!getTitle().equals(getTitleOri())) {
                newName+=" ["+getTitleOri()+"]"; //NOI18N
            }
            newName+=" [" + getYear() + "]"; //NOI18N
            newName += getStreamDetails4Filename(file);
        
            moveFileAndSrt(buffer, conn, myConn, file, newName);
        }
    }
    
    /**
     *
     * @return
     */
    @Override
    public boolean isWatched() {
        ArrayList<String> playCounterList = FolderInfo.group(new ArrayList(this.files.values()), "getPlayCounter");  //NOI18N
        return (!playCounterList.contains("0"));
    }

    @Override
    public boolean isLocal() {
        return getLength().getLength()>0;
    }
   
    @Override
    public void setMyVideo() {
        MyMovieDb myMovieDb = ProcessVideo.themovieDb.get(getTitle(), Integer.parseInt(getYear()));
        if(myMovieDb!=null) {
            setMyVideo(myMovieDb);  
        }
        else {
            myMovieDb = ProcessVideo.themovieDb.searchFirst(getTitle(), Integer.parseInt(getYear()));
            if(myMovieDb==null) {
                myMovieDb = new MyMovieDb(new MovieDb());
            }
            setMyVideo(myMovieDb); 
        }
    }
    
    /**
     *
     * @param rating
     */
    @Override
    public void setRating(VideoRating rating) {
        myVideo.setUserRating(rating);
        ProcessVideo.themovieDb.setRating(myVideo.getId(), rating.getRating());
    }
    
    /**
     *
     */
    @Override
    public void addToWatchList() {
        myVideo.setIsInWatchList(true);
        ProcessVideo.themovieDb.addToWatchList(myVideo.getId());
    }
    
    /**
     *
     */
    @Override
    public void removeFromWatchList() {
        myVideo.setIsInWatchList(false);
        ProcessVideo.themovieDb.removeFromWatchList(myVideo.getId());
    }
    
    /**
     *
     */
    @Override
    public void addFavorite() {
        myVideo.setIsFavorite(true);
        ProcessVideo.themovieDb.addFavorite(myVideo.getId());
    }
    
    /**
     *
     */
    @Override
    public void removeFavorite() {
        myVideo.setIsFavorite(false);
        ProcessVideo.themovieDb.removeFavorite(myVideo.getId());
    }

    @Override
    public String getRelativeFullPath() {
        return files.size()>0?files.firstEntry().getValue().getRelativeFullPath():"";
    }

    @Override
    public boolean isMovie() {
        return true;
    }
} 
