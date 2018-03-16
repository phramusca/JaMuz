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

import jamuz.process.video.FileInfoVideo.StreamDetails;
import jamuz.Jamuz;
import jamuz.DbConn;
import jamuz.DbInfo;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import jamuz.utils.StringManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.SerializationUtils;

/**
 * Creates a new connection to a Kodi database to get videos information
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DbConnVideo extends DbConn {

	/**
	 *
	 */
	protected final String rootPath;
	
	/**
	 * Creates a database connection.
	 * @param dbInfo
     * @param rootPath
	 */
	public DbConnVideo(DbInfo dbInfo, String rootPath) {
		super(dbInfo);
        this.rootPath = rootPath;
	}

    /**
     * Get idPath having strPath as path
     * @param strPath
     * @return
     */
    public int getIdPath(String strPath) {
        ResultSet rs=null;
        try {
			PreparedStatement stSelectPath = 
					connection.prepareStatement("SELECT idPath FROM path WHERE strPath=?"); //NOI18N
            stSelectPath.setString(1, strPath);
            rs = stSelectPath.executeQuery();
            if (rs.next()) { //Check if we have a result, so we can move to this one
                return rs.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            Popup.error("getIdPath(\""+strPath+"\"", ex);  //NOI18N
			return -1;
        }
        finally {
            try {
                if (rs!=null) rs.close();
            } catch (SQLException ex) {
                Jamuz.getLogger().warning("Failed to close ResultSet");
            }
        }
    }
    
	/**
	 * Gets movies list
	 * @param myFileInfoList
     * @param rootPath
	 * @return
	 */
	public boolean getMovies(List<VideoAbstract> myFileInfoList, String rootPath) {
		String title; String synopsis; String synopsis2; String synopsis3; 
        int ratingVotes; int rating; String writers; String year; String thumbnails;
        String imdbId; int runtime; String mppaRating; int imdbIdTop250Ranking; String genre; String director; 
        String titleOri; String studio; String trailerURL; String fanartURLs; String country;
        int idFile; int idPath; String path; String filename; String relativeFullPath; String fullPath;
        int playCounter; String lastPlayed; String addedDate;
		myFileInfoList.clear();
        ResultSet rs = null;
		try {
			PreparedStatement stSelectAllMovies = connection.prepareStatement("SELECT "
                    + "M.c00 AS title, " //NOI18N
                    + "M.c01 AS synopsis, "
                    + "M.c02 AS synopsis2, " //NOI18N
                    + "M.c03 AS synopsis3, "
                    + "M.c04 AS ratingVotes, " //NOI18N
                    + "M.c05 AS rating, "
                    + "M.c06 AS writers, " //NOI18N
					+ "M.c07 as year, "
                    + "M.c08 AS thumbnails, " //NOI18N
                    + "M.c09 AS imdbId, "
                    + "M.c11 as runtime, " //NOI18N
                    + "M.c12 AS mppaRating, "
                    + "M.c13 AS imdbIdTop250Ranking, " //NOI18N
                    + "M.c14 AS genre, "
                    + "M.c15 AS director, " //NOI18N
					+ "M.c16 AS titleOri, "
                    + "M.c18 AS studio, " //NOI18N
                    + "M.c19 AS trailerURL, "
                    + "M.c20 AS fanartURLs, " //NOI18N
                    + "M.c21 AS country, "
					+ "F.idFile, F.strFilename, F.playCount, F.lastPlayed, F.dateAdded, " //NOI18N
                    + "P.idPath, P.strPath "   //NOI18N
					+ "FROM ( movie M "
                    + "INNER JOIN path P ON M.idFile=F.idFile) " //NOI18N
					+ "INNER JOIN files F ON F.idPath=P.idPath "
					+ "WHERE strPath LIKE ?"
                    + "ORDER BY M.c00");    //NOI18N 
			stSelectAllMovies.setString(1, rootPath+"%");
			//Execute query
			rs = stSelectAllMovies.executeQuery();
			while(rs.next()){
                synopsis=getStringValue(rs, "synopsis"); //NOI18N
                synopsis2=getStringValue(rs, "synopsis2"); //NOI18N
                synopsis3=getStringValue(rs, "synopsis3"); //NOI18N
                mppaRating=getStringValue(rs, "mppaRating"); //NOI18N
				idFile=rs.getInt("idFile");  //NOI18N
				idPath=rs.getInt("idPath");  //NOI18N
                runtime=rs.getInt("runtime");  //NOI18N
                ratingVotes=rs.getInt("ratingVotes");  //NOI18N
				path=getStringValue(rs, "strPath");  //NOI18N
				filename=getStringValue(rs, "strFilename");  //NOI18N
                fullPath=path+filename;
                relativeFullPath=fullPath.substring(rootPath.length());
				playCounter=rs.getInt("playCount");  //NOI18N
                imdbIdTop250Ranking=rs.getInt("imdbIdTop250Ranking");  //NOI18N
				lastPlayed=getStringValue(rs, "lastPlayed");  //NOI18N
				imdbId=getStringValue(rs, "imdbId");  //NOI18N
				title=getStringValue(rs, "title");  //NOI18N
				writers=getStringValue(rs, "writers");  //NOI18N
				year=getStringValue(rs, "year", "");  //NOI18N
				thumbnails=getStringValue(rs, "thumbnails");  //NOI18N
				director=getStringValue(rs, "director");  //NOI18N
				titleOri=getStringValue(rs, "titleOri", "");  //NOI18N
				trailerURL=getStringValue(rs, "trailerURL");  //NOI18N
                genre=getStringValue(rs, "genre"); //NOI18N
                studio=getStringValue(rs, "studio"); //NOI18N
				rating=rs.getInt("rating");  //NOI18N
                fanartURLs=getStringValue(rs, "fanartURLs"); //NOI18N
                country=getStringValue(rs, "country"); //NOI18N
                addedDate=getStringValue(rs, "dateAdded"); //NOI18N
				
                VideoMovie video = new VideoMovie(title, synopsis, synopsis2, 
						synopsis3, ratingVotes, rating, writers, year,
                        thumbnails, imdbId, runtime, mppaRating, imdbIdTop250Ranking, 
						genre, director, titleOri, studio, trailerURL, fanartURLs, 
						country, idFile, idPath, relativeFullPath, 
                        playCounter, lastPlayed, addedDate, getStreamDetails(idFile));

				myFileInfoList.add(video);
			}
			return true;
		} catch (SQLException ex) {
			Popup.error(ex);
			return false;
		}
        finally {
            try {
                if (rs!=null) rs.close();
            } catch (SQLException ex) {
                Jamuz.getLogger().warning("Failed to close ResultSet");
            }
            
        }
	}
    
    private StreamDetails getStreamDetails(int idFile) throws SQLException {
        StreamDetails streamDetails= new StreamDetails();
        int iStreamType;
        ResultSet rsStream = null;
        //Get stream details
        try {
			PreparedStatement stSelectStreamdetails = connection.prepareStatement(
					"SELECT iStreamType, strVideoCodec, fVideoAspect, "
							+ "iVideoWidth, iVideoHeight, iVideoDuration, "
							+ "strAudioCodec, iAudioChannels, strAudioLanguage, "
							+ "strSubtitleLanguage " 
                    + "FROM streamdetails WHERE idFile=?");
            stSelectStreamdetails.setInt(1, idFile);
            rsStream = stSelectStreamdetails.executeQuery();
            while(rsStream.next()){
                iStreamType=rsStream.getInt("iStreamType");
                switch(iStreamType) {
                    case 0: //Video
                        streamDetails.addVideoStream(
                                getStringValue(rsStream, "strVideoCodec"), 
                                rsStream.getDouble("fVideoAspect"), 
                                rsStream.getInt("iVideoWidth"), 
                                rsStream.getInt("iVideoHeight"), 
                                rsStream.getInt("iVideoDuration"));
                        break;
                    case 1: //Audio
                        streamDetails.addAudioStream(
                                getStringValue(rsStream, "strAudioCodec"), 
                                rsStream.getInt("iAudioChannels"), 
                                getStringValue(rsStream, "strAudioLanguage"));
                        break;
                    case 2: //Subtitle
                        streamDetails.addSubtitle(getStringValue(rsStream, "strSubtitleLanguage"));
                        break;
                }
            }
            return streamDetails;
        } catch (SQLException ex) {
			Popup.error(ex);
			return streamDetails;
		}
        finally {
            try {
                if (rsStream!=null) rsStream.close();
            } catch (SQLException ex) {
                Jamuz.getLogger().warning("Failed to close ResultSet");
            }
            
        }
        
    }
    
    /**
	 * Gets TV shows list
	 * @param myFileInfoList
	 * @return
	 */
	public boolean getTvShows(List<VideoAbstract> myFileInfoList) {
		String title; String synopsis;
        int ratingVotes; int rating; String year; String thumbnails; String mppaRating; String genre;
        String titleOri; String studio; String trailerURL; String fanartURLs; int idPath; String path; int idShow;
        ResultSet rs = null;
        ResultSet rsEpisodes = null;
		try {
			PreparedStatement stSelectAllTvShows = connection.prepareStatement("SELECT T.idShow, "
                    + "T.c00 AS title, " //NOI18N
                    + "T.c01 AS synopsis, "
                    + "T.c02 AS status, " //NOI18N //TODO
                    + "T.c03 AS ratingVotes, "
                    + "T.c04 AS rating, " //NOI18N
                    + "T.c05 AS year, "
                    + "T.c06 AS thumbnails, " //NOI18N
                    + "T.c08 AS genre, " //NOI18N
                    + "T.c09 AS titleOri, "
                    + "T.c10 AS episodeguideURL, " //TODO
                    + "T.c11 as fanartURLs, " //NOI18N
                    + "T.c13 AS mppaRating, " //NOI18N
                    + "T.c14 AS studio, "
                    + "P.idPath, P.strPath "   //NOI18N
					+ "FROM ( tvshow T "
                    + "INNER JOIN tvshowlinkpath L ON L.idShow=T.idShow "
                    + "INNER JOIN path P ON L.idPath=P.idPath) " //NOI18N
                    + "ORDER BY T.c00");    //NOI18N 
			PreparedStatement stSelectTvShowEpisodes = connection.prepareStatement("SELECT "
                    + "E.idFile, E.c18 AS originalRelativeFullPath, "
                    + "P.strPath, F.strFilename, E.c03 AS rating, E.c12 AS seasonNumber, "
                    + "E.c13 AS episodeNumber, "
                    + "F.playCount AS playCounter, F.lastPlayed, F.dateAdded AS addedDate " 
                    + "FROM episode E " 
                    + "JOIN files F ON F.idFile=E.idFile "
                    + "JOIN path P on P.idPath=F.idPath  " 
                    + "WHERE E.idShow=?");
			//Execute query
			rs = stSelectAllTvShows.executeQuery();
			while(rs.next()){
                synopsis=getStringValue(rs, "synopsis"); //NOI18N
                mppaRating=getStringValue(rs, "mppaRating"); //NOI18N
				idPath=rs.getInt("idPath");  //NOI18N
                ratingVotes=rs.getInt("ratingVotes");  //NOI18N
				path=getStringValue(rs, "strPath");  //NOI18N
				title=getStringValue(rs, "title");  //NOI18N
                year=getStringValue(rs, "year", "");
                if(year.length()>4) {
                    year=StringManager.Left(year, 4); ;  //NOI18N
                }
				thumbnails=getStringValue(rs, "thumbnails");  //NOI18N
				titleOri=getStringValue(rs, "titleOri", "");  //NOI18N
				trailerURL=getStringValue(rs, "episodeguideURL");  //NOI18N
                genre=getStringValue(rs, "genre"); //NOI18N
                studio=getStringValue(rs, "studio"); //NOI18N
				rating=rs.getInt("rating");  //NOI18N
                fanartURLs=getStringValue(rs, "fanartURLs"); //NOI18N
                idShow = rs.getInt("idShow");
                
                VideoTvShow fileInfoVideo = new VideoTvShow(title, synopsis, ratingVotes, rating, year,
                            thumbnails, mppaRating, genre, titleOri, 
                            studio, trailerURL, fanartURLs, idPath, path );
                
                stSelectTvShowEpisodes.setInt(1, idShow);
                rsEpisodes = stSelectTvShowEpisodes.executeQuery();
                while(rsEpisodes.next()){
                    rating = rsEpisodes.getInt("rating");  //NOI18N
                    String lastPlayed = getStringValue(rsEpisodes, "lastplayed", "1970-01-01 00:00:00");  //NOI18N
                    String addedDate = getStringValue(rsEpisodes, "addedDate", "1970-01-01 00:00:00");  //NOI18N
                    int playCounter = rsEpisodes.getInt("playCounter");  //NOI18N
                    
                    //TODO: This might be useful ...
//                    String originalRelativeFullPath = getStringValue(rsEpisodes, "originalRelativeFullPath");
                    
                    String fullPath = getStringValue(rsEpisodes, "strPath");
                    fullPath += getStringValue(rsEpisodes, "strFilename");                    
                    
                    String relativeFullPath=fullPath.substring(rootPath.length());
                    int seasonNumber = rsEpisodes.getInt("seasonNumber");  //NOI18N
                    int episodeNumber = rsEpisodes.getInt("episodeNumber");  //NOI18N
                
                    int idFile = rsEpisodes.getInt("idFile");  //NOI18N
            
                    fileInfoVideo.getFiles().put("S"+seasonNumber+"E"+episodeNumber,
                        new FileInfoVideo(
                            idFile, 
                            idPath, 
                            relativeFullPath, 
                            rating, 
                            lastPlayed, 
                            addedDate, 
                            playCounter, getStreamDetails(idFile), seasonNumber, episodeNumber
                        )
                    );
                }
                
                myFileInfoList.add(fileInfoVideo);
			}
			return true;
		} catch (SQLException ex) {
			Popup.error(ex);
			return false;
		}
        finally {
            try {
                if (rs!=null) rs.close();
                if (rsEpisodes!=null) rsEpisodes.close();
            } catch (SQLException ex) {
                Jamuz.getLogger().warning("Failed to close ResultSet");
            }
            
        }
	}
    
	/**
	 *
	 * @param myTvShows
	 * @return
	 */
	public boolean getTvShowsFromCache(Map<Integer, MyTvShow> myTvShows) {
		return getVideosFromCache(myTvShows, "tvshow", "idTvShow");
	}

	/**
	 *
	 * @param myTvShows
	 * @return
	 */
	public boolean setTvShowsInCache(Map<Integer, MyTvShow> myTvShows) {
		return setVideosInCache(myTvShows, "tvshow", "idTvShow");
	}
	
	/**
	 *
	 * @param myTvShow
	 * @return
	 */
	public boolean setTvShowInCache(MyTvShow myTvShow) {
		Map<Integer, MyTvShow> myTvShows = new HashMap<>();
		myTvShows.put(myTvShow.getSerie().getId(), myTvShow);
		return setTvShowsInCache(myTvShows);
	}
	
	/**
	 *
	 * @param myMovies
	 * @return
	 */
	public boolean getMoviesFromCache(Map<Integer, MyMovieDb> myMovies) {
		return getVideosFromCache(myMovies, "movie", "idMovie");
	}

	/**
	 *
	 * @param myMovies
	 * @return
	 */
	public boolean setMoviesInCache(Map<Integer, MyMovieDb> myMovies) {
		return setVideosInCache(myMovies, "movie", "idMovie");
	}
	
	/**
	 *
	 * @param myMovie
	 * @return
	 */
	public boolean setMovieInCache(MyMovieDb myMovie) {
		Map<Integer, MyMovieDb> myMovies = new HashMap<>();
		myMovies.put(myMovie.getMovieDb().getId(), myMovie);
		return setMoviesInCache(myMovies);
	}
	
	private boolean getVideosFromCache(Map<Integer, ? extends MyVideoAbstract> myVideos, String table, String idName) {
		//Get bytes from db
		Map<Integer, byte[]> objects = new HashMap<>();
		if(!getBytesFromCache(objects, table, idName)) {
			return false;
		}
		//Deserialize
		for(Entry<Integer, byte[]> entry : objects.entrySet()) {
			myVideos.put(entry.getKey(), SerializationUtils.deserialize(entry.getValue()));
		}
		return true;
	}
	
	private boolean setVideosInCache(Map<Integer, ? extends MyVideoAbstract> myVideos, String table, String idName) {
		Map<Integer, byte[]> objects = new HashMap<>();
		for(Entry<Integer, ? extends MyVideoAbstract> entry : myVideos.entrySet()) {
			objects.put(entry.getKey(), SerializationUtils.serialize(entry.getValue()));
		}
		return setBytesInCache(objects, table, idName);
	}
	
	//TODO: Move to a common library

	/**
	 *
	 * @param objects
	 * @param table
	 * @param idName
	 * @return
	 */
	public boolean getBytesFromCache(Map<Integer, byte[]> objects, String table, String idName) {
        ResultSet rs = null;
		byte[] bytes;
		int id;
		try {
			PreparedStatement stSelectAllTvShowsFromCache = null;
			stSelectAllTvShowsFromCache = connection.prepareStatement("SELECT "+idName+", obj FROM "+table+"");    //NOI18N 
			rs = stSelectAllTvShowsFromCache.executeQuery();
			while(rs.next()){
				id=rs.getInt(idName);
				bytes=rs.getBytes("obj");
				objects.put(id, bytes);
			}
			return true;
		} catch (SQLException ex) {
			Popup.error(ex);
			return false;
		}
        finally {
            try {
                if (rs!=null) rs.close();
            } catch (SQLException ex) {
                Jamuz.getLogger().warning("getBytesFromCache: Failed to close ResultSet");
            }
            
        }
	}
	
	//TODO: Move to a common library

	/**
	 *
	 * @param objects
	 * @param table
	 * @param idName
	 * @return
	 */
	public boolean setBytesInCache(Map<Integer, byte[]> objects, String table, String idName) {
        ResultSet rs = null;
		try {
			connection.setAutoCommit(false);
			int[] results;
			PreparedStatement stInsertInCache = connection.prepareStatement(
					"INSERT OR REPLACE INTO "+table+" ("+idName+", obj) VALUES (?, ?)"); //NOI18N 
			for (Entry<Integer, byte[]> entry : objects.entrySet()) {
				stInsertInCache.setInt(1, entry.getKey());
				stInsertInCache.setBytes(2, entry.getValue());
				stInsertInCache.addBatch();
			}
            long startTime = System.currentTimeMillis();
			results = stInsertInCache.executeBatch();
			connection.commit();
			long endTime = System.currentTimeMillis();
			Jamuz.getLogger().log(Level.FINEST, 
					"setObjectInCache UPDATE done in {0}ms. Total execution time: {1}ms", 
					new Object[]{results.length, endTime - startTime});    //NOI18N

			//Analyse results
			//TODO: Get more info to display => add a Map<Integer, EntrySet>. Do that for other similar cases
			int result;
			for (int i = 0; i < results.length; i++) {
				result = results[i];
				if (result < 0) {
					Jamuz.getLogger().log(Level.SEVERE, "setObjectInCache in table {1}, result={0}", new Object[]{result, table});   //NOI18N
				}
			}
			connection.setAutoCommit(true);
			return true;
		} catch (SQLException ex) {
			Popup.error(ex);
			return false;
		}
        finally {
            try {
                if (rs!=null) rs.close();
            } catch (SQLException ex) {
                Jamuz.getLogger().warning("setObjectInCache: Failed to close ResultSet");
            }
        }
	}
	
    /**
     * Update file's idPath and filename
     * @param idFile
     * @param newIdPath
     * @param newFilename
     * @return
     */
    public boolean updateFile(int idFile, int newIdPath, String newFilename) {
		try {
			PreparedStatement stUpdateFile = 
					connection.prepareStatement("UPDATE files SET strFilename=?, "
							+ "idPath=? WHERE idFile=?"); //NOI18N
			stUpdateFile.setString(1, newFilename);
            stUpdateFile.setInt(2, newIdPath);
			stUpdateFile.setInt(3, idFile);
			int nbRowsAffected = stUpdateFile.executeUpdate();
			if(nbRowsAffected==1) {
				return true;
			}
			else {
				Jamuz.getLogger().log(Level.SEVERE, "stUpdateFile, idFile={0}, "
						+ "newFilename={1} # row(s) affected: +{2}", 
						new Object[]{idFile, newFilename, nbRowsAffected});  //NOI18N
				return false;
			}
		} catch (SQLException ex) {
			Popup.error("updateFile("+idFile+", "+newFilename+")", ex);  //NOI18N
			return false;
		}
	}
}
