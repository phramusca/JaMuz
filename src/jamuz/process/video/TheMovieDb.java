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

import info.movito.themoviedbapi.TmdbAccount;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbAuthentication;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.TmdbTV;
import info.movito.themoviedbapi.TmdbTvSeasons;
import info.movito.themoviedbapi.TvResultsPage;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.config.Account;
import info.movito.themoviedbapi.model.config.TokenSession;
import info.movito.themoviedbapi.model.core.AccountID;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.core.ResponseStatusException;
import info.movito.themoviedbapi.model.core.SessionToken;
import info.movito.themoviedbapi.model.tv.TvSeason;
import info.movito.themoviedbapi.model.tv.TvSeries;
import jamuz.DbInfo;
import jamuz.Jamuz;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import jamuz.utils.Popup;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class TheMovieDb {

    private String language = ""; 
    
    private TmdbApi tmdbApi;
    private AccountID accountId;
    private SessionToken sessionToken;
    private TmdbAccount account;
    
    private Map<Integer, MyMovieDb> myMovies;
    private Map<Integer, MyTvShow> myTvShows;
    
	/**
	 *
	 * @param username
	 * @param password
	 * @param language
	 */
	public TheMovieDb(String username, String password, String language) {
		myMovies = new HashMap<>();
        myTvShows = new HashMap<>();
        //TODO: Not getting info in french !
        //language "fr" (ISO 639-1 code as mentionned in doc (http://docs.themoviedb.apiary.io/#reference/movies/movieidreleasedates)
        // Not better with "fr-FR" as displayed in website
		
		//Configure TheMovieDb
        this.language = language; 
        tmdbApi = new TmdbApi(Jamuz.getKeys().get("TheMovieDb"));
        TmdbAuthentication auth = tmdbApi.getAuthentication();
        TokenSession tokenSession = auth.getSessionLogin(username, password);
        sessionToken = new SessionToken(tokenSession.getSessionId());
        account = tmdbApi.getAccount();
        Account user = account.getAccount(sessionToken);
        accountId = new AccountID(user.getId());
    }
    
	/**
	 *
	 */
	public void getAll() {
		//Get movies and TV shows
        getMovies();
        getTvShows();
	}
	
	/**
	 *
	 */
	public void getAllFromCache() {
		DbConnVideo conn = new DbConnVideo(new DbInfo(DbInfo.LibType.Sqlite, "myMovieDb.db", ".", "."), "");
        conn.connect();
		conn.getTvShowsFromCache(myTvShows);
		conn.getMoviesFromCache(myMovies);
		conn.disconnect();
	}
	
    private void getTvShows() {
        //Get user lists and ratings
        Map<Integer, TvSeries> watchList = getWatchListTv();
        Map<Integer, TvSeries> userRatings = getUserRatingsTv();
        Map<Integer, TvSeries> favorites = getFavoritesTv();
        
        List<Integer> collection = new ArrayList<>();
        collection.addAll(watchList.keySet());
        collection.addAll(favorites.keySet());
        collection.addAll(userRatings.keySet()); 
        //Remove potential duplicates 
        collection = new ArrayList(new HashSet(collection)); //no order
//            fileInfoListTemp = new ArrayList(new LinkedHashSet(fileInfoListTemp)); //If you need to preserve the order use 'LinkedHashSet'

        for(Integer id : collection) {
            MyTvShow myTvShow = null;
            
            if(userRatings.containsKey(id)) {
                if(myTvShow==null) {
                    myTvShow = new MyTvShow(userRatings.get(id));
                }
            }
            
            if(watchList.containsKey(id)) {
                if(myTvShow==null) {
                    myTvShow = new MyTvShow(watchList.get(id));
                }
                myTvShow.isInWatchList=true;
            }
            
            if(favorites.containsKey(id)) {
                if(myTvShow==null) {
                    myTvShow = new MyTvShow(favorites.get(id));
                }
                myTvShow.isFavorite=true;
            }
            
            //TODO: This retrieves more movie info but remove user rating :(
                //+ it counts as another queryrestaa => really need to store info in dB !!
//            if(myMovieDb!=null) {
//                myMovieDb.setMovieDb(getMovie(id));
//            }
            
            myTvShows.put(id, myTvShow);
        }
    }
    
    private void getMovies() {
        //Get user lists and ratings
        Map<Integer, MovieDb> watchList = getWatchList();
        Map<Integer, MovieDb> userRatings = getUserRatings();
        Map<Integer, MovieDb> favorites = getFavorites();
        
        List<Integer> collection = new ArrayList<>();
        collection.addAll(watchList.keySet());
        collection.addAll(favorites.keySet());
        collection.addAll(userRatings.keySet());
        //Remove potential duplicates 
        collection = new ArrayList(new HashSet(collection)); //no order
//            fileInfoListTemp = new ArrayList(new LinkedHashSet(fileInfoListTemp)); //If you need to preserve the order use 'LinkedHashSet'

        for(Integer id : collection) {
            MyMovieDb myMovieDb = null;
            
            if(userRatings.containsKey(id)) {
                if(myMovieDb==null) {
                    myMovieDb = new MyMovieDb(userRatings.get(id));
                }
            }
            
            if(watchList.containsKey(id)) {
                if(myMovieDb==null) {
                    myMovieDb = new MyMovieDb(watchList.get(id));
                }
                myMovieDb.isInWatchList=true;
            }
            
            if(favorites.containsKey(id)) {
                if(myMovieDb==null) {
                    myMovieDb = new MyMovieDb(favorites.get(id));
                }
                myMovieDb.isFavorite=true;
            }
            
            //TODO: This retrieves more movie info but remove user rating :(
                //+ it counts as another queryrestaa => really need to store info in dB !!
//            if(myMovieDb!=null) {
//                myMovieDb.setMovieDb(getMovie(id));
//            }
            
            myMovies.put(id, myMovieDb);
        }
    }
    
	/**
	 *
	 * @param movieId
	 * @param rating
	 */
	public void setRating(int movieId, int rating) {
        doWait();
        account.postMovieRating(sessionToken, movieId, rating);
    }
    
	/**
	 *
	 * @param serieId
	 * @param rating
	 */
	public void setRatingTV(int serieId, int rating) {
        doWait();
        account.postTvSeriesRating(sessionToken, serieId, rating);
    }
    
	/**
	 *
	 * @param movieId
	 */
	public void addFavorite(int movieId) {
        doWait();
        account.addFavorite(sessionToken, accountId, movieId, TmdbAccount.MediaType.MOVIE);
    }
    
	/**
	 *
	 * @param serieId
	 */
	public void addFavoriteTV(int serieId) {
        doWait();
        account.addFavorite(sessionToken, accountId, serieId, TmdbAccount.MediaType.TV);
    }
    
	/**
	 *
	 * @param movieId
	 */
	public void removeFavorite(int movieId) {
        doWait();
        account.removeFavorite(sessionToken, accountId, movieId, TmdbAccount.MediaType.MOVIE);
    }
    
	/**
	 *
	 * @param movieId
	 */
	public void removeFavoriteTV(int movieId) {
        doWait();
        account.removeFavorite(sessionToken, accountId, movieId, TmdbAccount.MediaType.TV);
    }
    
	/**
	 *
	 * @param movieId
	 */
	public void addToWatchList(int movieId) {
        doWait();
        account.addToWatchList(sessionToken, accountId, movieId, TmdbAccount.MediaType.MOVIE);
    }
    
	/**
	 *
	 * @param movieId
	 */
	public void addToWatchListTV(int movieId) {
        doWait();
        account.addToWatchList(sessionToken, accountId, movieId, TmdbAccount.MediaType.TV);
    }
    
	/**
	 *
	 * @param movieId
	 */
	public void removeFromWatchList(int movieId) {
        doWait();
        account.removeFromWatchList(sessionToken, accountId, movieId, TmdbAccount.MediaType.MOVIE);
    }
    
	/**
	 *
	 * @param movieId
	 */
	public void removeFromWatchListTV(int movieId) {
        doWait();
        account.removeFromWatchList(sessionToken, accountId, movieId, TmdbAccount.MediaType.TV);
    }
    
    private MovieDb getMovie(int id) {
        doWait();
        TmdbMovies movies = tmdbApi.getMovies();
        doWait();
        return movies.getMovie(id, language);
    }
    
    private TvSeries getTV(int id) {
        doWait();
        TmdbTV series = tmdbApi.getTvSeries();
        doWait();
        TvSeries tvSeries = series.getSeries(id, language, TmdbTV.TvMethod.external_ids);
        doWait();
        TmdbTvSeasons tmdbTVSeasons = tmdbApi.getTvSeasons();
        List<TvSeason> seasons = new ArrayList<>();
        for(int seasonNumber=1; seasonNumber<=tvSeries.getNumberOfSeasons(); seasonNumber++) {
            doWait();
            TvSeason tvSeason=tmdbTVSeasons.getSeason(id, seasonNumber, language, TmdbTvSeasons.SeasonMethod.external_ids);
            seasons.add(tvSeason);
        }
        tvSeries.setSeasons(seasons);
        return tvSeries;
    }
    
    private int requestCount = 41;
    private long lastQuery = System.currentTimeMillis();
            
    private void doWait() {
        
        long timeSinceLastQuery = System.currentTimeMillis() - this.lastQuery;
        lastQuery=System.currentTimeMillis();
        if(timeSinceLastQuery>10000) {
            requestCount=0;
            return;
        }
        requestCount++;
        if(requestCount>38) {
            try {
                PanelVideo.progressBarTimer.setIndeterminate("TheMovieDb ...");
                Thread.sleep(10000-timeSinceLastQuery);
                PanelVideo.progressBarTimer.reset();
            } catch (InterruptedException ex) {
                Jamuz.getLogger().log(Level.SEVERE, null, ex);
            }
            requestCount = 0;
        }
    }
    
    private Map<Integer, MovieDb> getWatchList() {
        Map<Integer, MovieDb> watchList = new HashMap<>();
        doWait();
        try {
            MovieResultsPage page = account.getWatchListMovies(sessionToken, accountId, 1);
            for(int i=0; i<=page.getTotalPages(); i++) {
                doWait();
                page = account.getWatchListMovies(sessionToken, accountId, i);
                for(MovieDb movie : page.getResults()) {
                    watchList.put(movie.getId(), movie);
                }
            }
        }
        catch(ResponseStatusException ex) {
            popupOrLog(ex);
        }
        return watchList;
    }
    
	private void popupOrLog(ResponseStatusException ex) {
		//FIXME We should retry if a requestCount issue (and other similar cases).
		//We should not have such errors as calls are delayed
		
		if(ex.getResponseStatus().getStatusCode()!=25) {
			Popup.error("Code "+ex.getResponseStatus().getStatusCode()+": "+ex.getResponseStatus().getStatusMessage());
		}
		else {
			Jamuz.getLogger().log(Level.WARNING, "Code {0}: {1}", new Object[]{ex.getResponseStatus().getStatusCode(), ex.getResponseStatus().getStatusMessage()});
		}
	}
	
    private Map<Integer, TvSeries> getWatchListTv() {
        Map<Integer, TvSeries> watchList = new HashMap<>();
        doWait();
        try {
            TvResultsPage page = account.getWatchListSeries(sessionToken, accountId, 1);
            for(int i=0; i<=page.getTotalPages(); i++) {
                doWait();
                page = account.getWatchListSeries(sessionToken, accountId, i);
                for(TvSeries serie : page.getResults()) {
                    watchList.put(serie.getId(), serie);
                }
            }
        }
        catch(ResponseStatusException ex) {
            popupOrLog(ex);
        }
        return watchList;
    }
   
    private Map<Integer, TvSeries> getFavoritesTv() {
        Map<Integer, TvSeries> favorites = new HashMap<>();
        doWait();
        try {
            TvResultsPage page = account.getFavoriteSeries(sessionToken, accountId, 1);
            for(int i=0; i<=page.getTotalPages(); i++) {
                doWait();
                page = account.getFavoriteSeries(sessionToken, accountId, i);
                for(TvSeries serie : page.getResults()) {
                    favorites.put(serie.getId(), serie);
                }
            }
        }
        catch(ResponseStatusException ex) {
            popupOrLog(ex);
        }
        return favorites;
    }
    
    private Map<Integer, MovieDb> getFavorites() {
        Map<Integer, MovieDb> favorites = new HashMap<>();
        doWait();
        try {
            MovieResultsPage page = account.getFavoriteMovies(sessionToken, accountId);
            for(MovieDb movie : page.getResults()) {
                favorites.put(movie.getId(), movie);
            }
        }
        catch(ResponseStatusException ex) {
            popupOrLog(ex);
        }
        return favorites;
    }
    
    private Map<Integer, TvSeries> getUserRatingsTv() {
        Map<Integer, TvSeries> userRatings = new HashMap<>();
        doWait();
        try {
            TvResultsPage page = account.getRatedTvSeries(sessionToken, accountId, 1);
            for(int i=0; i<=page.getTotalPages(); i++) {
                doWait();
                page = account.getRatedTvSeries(sessionToken, accountId, i);
                for(TvSeries serie : page.getResults()) {
                    userRatings.put(serie.getId(), serie);
                }
            }
        }
        catch(ResponseStatusException ex) {
            popupOrLog(ex);
        }
        
        return userRatings;
    }
    
    private Map<Integer, MovieDb> getUserRatings() {
        Map<Integer, MovieDb> userRatings = new HashMap<>();
        doWait();
        try {
            MovieResultsPage page = account.getRatedMovies(sessionToken, accountId, 1);
            for(int i=0; i<=page.getTotalPages(); i++) {
                doWait();
                page = account.getRatedMovies(sessionToken, accountId, i);
                for(MovieDb movie : page.getResults()) {
                    userRatings.put(movie.getId(), movie);
                }
            }
        }
        catch(ResponseStatusException ex) {
            popupOrLog(ex);
        }
        return userRatings;
    }
	
    //TODO: Do we need to try/catch ResponseStatusException in search functions below ?
    private List<MovieDb> search(String name, int year) {
        doWait();
        TmdbSearch search = new TmdbSearch(tmdbApi);
        doWait();
        MovieResultsPage page = search.searchMovie(name, year, language, false, 1);
        return page.getResults();
    }
    
    private List<TvSeries> searchTv(String name, int year) {
        doWait();
        TmdbSearch search = new TmdbSearch(tmdbApi);
        doWait();
        TvResultsPage page = search.searchTv(name, language, 1);
        return page.getResults();
    }
    
	/**
	 *
	 * @param name
	 * @param year
	 * @return
	 */
	public MyMovieDb searchFirst(String name, int year) {
        List<MovieDb> movies = search(name, year);
        
        if(movies.size()>0) {
            //TODO: Manage if >1 (offer user a choice)
            MovieDb movieDb = movies.get(0);
            int movieId = movieDb.getId();
                    
            if(myMovies.containsKey(movieId)) {
                MyMovieDb myMovieDb = myMovies.get(movieId);
                myMovies.remove(movieId);
                return myMovieDb;
            }
            
            return new MyMovieDb(movieDb);
        }
        return null;
    }
    
	/**
	 *
	 * @param name
	 * @param year
	 * @return
	 */
	public MyTvShow searchFirstTv(String name, int year) {
        List<TvSeries> series = searchTv(name, year);
        
        if(series.size()>0) {
            //TODO: Manage if >1 (offer user a choice)
            TvSeries serie = series.get(0);
            int serieId = serie.getId();
                    
            if(myTvShows.containsKey(serieId)) {
                MyTvShow myTvShow = myTvShows.get(serieId);
                myTvShow.setSerie(getTV(serie.getId()));
                myTvShows.remove(serieId);
                return myTvShow;
            }
            serie=getTV(serie.getId());
            return new MyTvShow(serie);
        }
        return null;
    }

	/**
	 *
	 * @return
	 */
	public Map<Integer, MyMovieDb> getMyMovies() {
        return myMovies;
    }

	/**
	 *
	 * @return
	 */
	public Map<Integer, MyTvShow> getMyTvShows() {
        return myTvShows;
    }
    
	/**
	 *
	 * @param name
	 * @param year
	 * @return
	 */
	public MyMovieDb get(String name, int year) {
        for(MyMovieDb myMovieDb : myMovies.values()) {
            String title=myMovieDb.getMovieDb().getTitle()==null?"":myMovieDb.getMovieDb().getTitle();	
            if(title.equals(name)) {
                if(year<=0 || myMovieDb.getYear()==year) {
                    myMovies.remove(myMovieDb.getMovieDb().getId());
                    return myMovieDb;
                }
            }
        }
        return null;
    }
    
	/**
	 *
	 * @param name
	 * @param year
	 * @param search
	 * @return
	 */
	public MyTvShow getTv(String name, int year, boolean search) {
        for(MyTvShow myTvShow : myTvShows.values()) {
            
            if(myTvShow.getSerie().getName().equals(name)) {
                if(year<=0 || myTvShow.getYear()==year) {
                    if(search) { myTvShow.setSerie(getTV(myTvShow.getSerie().getId())); }
                    myTvShows.remove(myTvShow.getSerie().getId());
                    return myTvShow;
                }
            }
        }
        return null;
    }

}
