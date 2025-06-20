/*
 * Copyright (C) 2013 phramusca <phramusca@gmail.com>
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

import com.google.common.collect.Lists;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.tv.TvEpisode;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;
import jamuz.process.video.ProcessVideo.PathBuffer;
import jamuz.utils.Inter;
import jamuz.utils.SSH;
import jamuz.utils.StringManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Video file information class
 * @author phramusca <phramusca@gmail.com>
 */
public class VideoTvShow extends VideoAbstract {

    /**
     * Create a new TV Show information instance
     * @param title
     * @param synopsis
     * @param ratingVotes
     * @param rating
     * @param year
     * @param thumbnails
     * @param mppaRating
     * @param genre
     * @param titleOri
     * @param studio
     * @param trailerURL
     * @param fanartURLs
     * @param idPath
     * @param path
     */
    public VideoTvShow(String title, String synopsis, int ratingVotes, int rating, String year,
                            String thumbnails, String mppaRating, String genre, String titleOri, 
                            String studio, String trailerURL, String fanartURLs,  int idPath, String path) {
        super(title, synopsis,
            ratingVotes, "", year, 
            "", -1, mppaRating, -1, genre, "", 
            titleOri, studio, trailerURL, fanartURLs, "");
        this.thumbnails = parseURLStringList(thumbnails, "<thumb aspect=\"poster\">", "</thumb>");
        myVideo = new MyTvShow(new TvSeries());
    }

    /**
     * TheMovieDb only TV Show
     * @param myTvShow
     */
    public VideoTvShow(MyTvShow myTvShow) {
        super(myTvShow.getSerie().getName(), 
                StringManager.getNullableText(myTvShow.getSerie().getOverview()), 
                Math.round(myTvShow.getSerie().getVoteAverage()), 
                "", 
                String.valueOf(myTvShow.getYear()), 
                "", 
                0, 
                "", 
                0, 
                "", "", 
                StringManager.getNullableText(myTvShow.getSerie().getOriginalName()), 
                "", "", null, "");
//        this.files.put("", new FileInfoVideo());
		this.myVideo = myTvShow;
        
        this.status = new Status();
        this.status.set("No local files. From theMovieDb.org");
        TvSeries serie = myTvShow.getSerie();
        this.genres = new ArrayList<>();
        this.genreStr = "";
        if(serie.getGenres()!=null) {
            for(Genre genre : serie.getGenres()) {
                this.genres.add(genre.getName());
                genreStr+=" / "+genre.getName();
            }
        }
        this.thumbnails = new ArrayList<>();
		this.fanarts = new ArrayList<>();
        setThumbnails(myTvShow);
//        this.ratingVotes = Math.round(serie.getVoteAverage());
        serie.getPopularity(); //TODO: Use this
        serie.getVoteCount(); //TODO: Use thos
        
	}
	
	private void setThumbnails(MyTvShow myTvShow) {
		this.thumbnails.add("https://image.tmdb.org/t/p/w396"+myTvShow.getSerie().getPosterPath()); 
        this.fanarts.add("https://image.tmdb.org/t/p/w396"+myTvShow.getSerie().getBackdropPath()); 
	}
    
	/**
	 *
	 * @return
	 */
	@Override
    protected String getVideoSummary() {
        String display="";
        int nbWatched=0;
        int nbHD=0;
        for(FileInfoVideo fileInfoVideo : files.values()) {
            if(fileInfoVideo.getPlayCounter()>0) {
                nbWatched+=1;
            }
            //TODO: Distinct HD 1080 et 720
            if(fileInfoVideo.isHD()) {
                nbHD+=1;
            }
        }
        display += "<u>Episodes</u>: "+files.size()+": "+nbWatched+" "
				+Inter.get("Label.Watched")+", "+nbHD+" HD<BR/>";

        //List missing episodes (from last watched)
        display += "<u>Missing</u>:";
        List<String> missing = getMissing();
        if(missing != null) {
            //Output the first 10 missing items
            if(missing.size()>0) {
                display += "<font color=red>";
                int limit = missing.size();
                if(limit>10) {
					limit=10;
				}
                for(int i=0; i<limit; i++) {
                    display += " "+missing.get(missing.size()-i-1);
                }
                if(missing.size()>10) {
                    display += " ...";
                }
                display += "</font>";
            }
            else {
                //Or the serie satus if all found 
                display += ((MyTvShow) myVideo).getSerie().getStatus();
            }
        }
        else {
            display += "<font color=red>";
            display += " "+Inter.get("Msg.Video.NoInfo");    
            display += "</font>";

        }
        display += "<BR/>";
        return display;
    }
        
	/**
	 *
	 * @param nbSeasonToKeep
	 * @param nbEpisodeToKeep
	 * @param keepCanceled
	 * @return
	 */
	@Override
	public ArrayList<FileInfoVideo> getFilesToCleanup(
			int nbSeasonToKeep, 
			int nbEpisodeToKeep,
			boolean keepEnded, boolean keepCanceled) {
		nbEpisodeToKeep=nbSeasonToKeep>0?-1:nbEpisodeToKeep;
		MyTvShow myTvShow = (MyTvShow) myVideo;
		
		String tvShowStatus = ((MyTvShow) myVideo).getSerie().getStatus();
		ArrayList<FileInfoVideo> filesToCleanup = new ArrayList<>();
		if(tvShowStatus==null) {
			return filesToCleanup;
		}
		String ended="Ended";
		if((!keepEnded && tvShowStatus.equals(ended)
				&& getMissing().isEmpty())
			|| !keepCanceled && tvShowStatus.equals("Canceled"))
		{
			int nbSeasons=myTvShow.getSerie().getNumberOfSeasons(); 
			int nbEpisodesInLastSeason = myTvShow.getSerie().getSeasons().get(nbSeasons-1).getEpisodes().size(); 
			for(FileInfoVideo fileInfoVideo : files.values()) {
				if(fileInfoVideo.getPlayCounter()>0) {
					File file = fileInfoVideo.getVideoFile();
					if(file.exists()) {
						fileInfoVideo.setSourceName(tvShowStatus 
						+" [missed "+getMissing().size() 
						+" -> S"+nbSeasons 
						+"E"+nbEpisodesInLastSeason 
						+"]"); 
						filesToCleanup.add(fileInfoVideo);
					}
				}
			}
			return filesToCleanup;
		}
		
        List<TvSeason> seasons = myTvShow.getSerie().getSeasons();
        if(seasons != null && files.size()>0) {
			int currentSeason=-1;
			int lastEpisode=-1;
			int seasonumber;
			int episodeNumber;
            for(TvSeason season : Lists.reverse(seasons)) {
				seasonumber=season.getSeasonNumber();
                for(TvEpisode episode :  Lists.reverse(season.getEpisodes())) {
					episodeNumber=episode.getEpisodeNumber();
                    String key = "S"+seasonumber+"E"+episodeNumber;
                    if(files.containsKey(key)) {
						FileInfoVideo fileInfoVideo = files.get(key);
                        if(fileInfoVideo.getPlayCounter()>0) {
							if(currentSeason<0) {
								currentSeason=seasonumber;
								lastEpisode=episodeNumber;
							}
							File file = fileInfoVideo.getVideoFile();
							
							if(file.exists()) {
								if(seasonumber<(currentSeason-nbSeasonToKeep)) {
									fileInfoVideo.setSourceName(
											"< S"+(currentSeason-nbSeasonToKeep) 
											+" (last:S"+currentSeason+")"); 
									
									filesToCleanup.add(fileInfoVideo);
								} else if(
										nbEpisodeToKeep>-1
										&& seasonumber==currentSeason
										&& episodeNumber<=(lastEpisode-nbEpisodeToKeep)) {
									fileInfoVideo.setSourceName(
											"<= S"+currentSeason+"E"+(lastEpisode-nbEpisodeToKeep) 
											+" (last:S"+currentSeason+"E"+lastEpisode+")"); 
									filesToCleanup.add(fileInfoVideo);
								}
							}
                        }
                    }
                }
            }
        }
        return filesToCleanup;
	}
	
    private List<String> getMissing() {
        MyTvShow myTvShow = (MyTvShow) myVideo;
        List<TvSeason> seasons = myTvShow.getSerie().getSeasons();
        List<String> missing = null;
        if(seasons != null) {
            missing = new ArrayList<>();
            for(TvSeason season : seasons) {
                for(TvEpisode episode : season.getEpisodes()) {
                    String key = "S"+season.getSeasonNumber()+"E"+episode.getEpisodeNumber();
                    if(!files.containsKey(key)) {
                        missing.add(key);
                    }
                    else {
                        FileInfoVideo fileInfoVideo = files.get(key);
                        if(fileInfoVideo.getPlayCounter()>0) {
                            //This was one watched, forget previous one, supposely watched too
                            missing.clear();
                        }
                    }
                }
            }
        }
        return missing;
    }

    /**
     *
     * @return
     */
    @Override
    public String getRelativeFullPath() {
        return files.size()>0?files.firstEntry().getValue().getRelativePath():"";
    }

    /**
     *
     * @param buffer
     * @param conn
     * @param myConn
     */
    @Override
    public void moveFilesAndSrt(PathBuffer buffer, DbConnVideo conn, SSH myConn) {

        String serieName = getTitle();
        if(!getTitleOri().isBlank() && !getTitle().equals(getTitleOri())) {
            serieName+=" ["+getTitleOri()+"]"; //NOI18N
        }
		
        for(FileInfoVideo file : files.values()) {
            
            String newName = serieName + " "+file.getFormattedEpisodeNumber();

            //Not showing year as serie first release year, not episode year
            //TODO: Add episode year to filename
            //TODO: Rename folder including serie year
//            newName += " [" + getYear() + "]"; 
            
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
		//Note: same as isLocal but on purpose
        List<String> missing = getMissing();
        return missing==null?false:missing.size()<=0;
    }

	/**
	 *
	 * @return
	 */
	@Override
    public boolean isLocal() {
        List<String> missing = getMissing();
        return missing==null?false:missing.size()<=0;
    }

	/**
	 *
	 * @param search
	 */
	@Override
    public void setMyVideo(boolean search) {
        MyTvShow myTvShow = ProcessVideo.themovieDb.getTv(getTitle(), getYearInt(), search);
        if(myTvShow!=null) {
            setMyVideo(myTvShow);
			setThumbnails(myTvShow);
        }
        else if(search) {
            myTvShow = ProcessVideo.themovieDb.searchFirstTv(getTitle(), getYearInt());
        }
		if(myTvShow==null) {
			myTvShow = new MyTvShow(new TvSeries());
		}
		setMyVideo(myTvShow);
		setThumbnails(myTvShow);
    }
    
    /**
     *
     * @param rating
     */
    @Override
    public void setRating(VideoRating rating) {
        myVideo.setUserRating(rating);
        ProcessVideo.themovieDb.setRatingTV(myVideo.getId(), rating.getRating());
    }
    
    /**
     *
     */
    @Override
    public void addToWatchList() {
        myVideo.setIsInWatchList(true);
        ProcessVideo.themovieDb.addToWatchListTV(myVideo.getId());
    }
    
    /**
     *
     */
    @Override
    public void removeFromWatchList() {
        myVideo.setIsInWatchList(false);
        ProcessVideo.themovieDb.removeFromWatchListTV(myVideo.getId());
    }
    
    /**
     *
     */
    @Override
    public void addFavorite() {
        myVideo.setIsFavorite(true);
        ProcessVideo.themovieDb.addFavoriteTV(myVideo.getId());
    }
    
    /**
     *
     */
    @Override
    public void removeFavorite() {
        myVideo.setIsFavorite(false);
        ProcessVideo.themovieDb.removeFavoriteTV(myVideo.getId());
    }

	/**
	 *
	 * @return
	 */
	@Override
    public boolean isMovie() {
        return false;
    }
}