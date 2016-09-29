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

import info.movito.themoviedbapi.model.MovieDb;
import jamuz.DbInfo;
import static jamuz.process.video.PanelVideo.comboRating;
import java.io.Serializable;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class MyMovieDb extends MyVideoAbstract implements Serializable {
        private MovieDb movieDb;

        public MyMovieDb(MovieDb movieDb) {
            super();
            int rating = Math.round(movieDb.getUserRating());
            this.userRating = rating<=0?new VideoRating(0, "0 - Not Rated"):comboRating[rating-1];
            this.movieDb = movieDb;
            
            if(movieDb.getReleaseDate()!=null) {
                this.year = getYear(movieDb.getReleaseDate());
//                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  //NOI18N
//                try {
//                    Date releaseDate = dateFormat.parse(movieDb.getReleaseDate());
//                    Calendar cal = Calendar.getInstance();
//                    cal.setTime(releaseDate);
//                    this.year = cal.get(Calendar.YEAR);
//                } catch (ParseException ex) {
////                    Popup.error(ex);
//                }
            }
        }

        public MovieDb getMovieDb() {
            return movieDb;
        }

        public void setMovieDb(MovieDb movieDb) {
            this.movieDb = movieDb;
        }

        @Override
        public int getId() {
            return this.movieDb.getId();
        }

        @Override
        public String getHomepage() {
            return this.movieDb.getHomepage(); //TODO: Lookup if not already done to get the homepage
        }
		
		@Override
		public void setMyVideoInCache() {
			DbConnVideo conn = new DbConnVideo(new DbInfo("sqlite", "myMovieDb.db", ".", "."), "");
			conn.connect();
			conn.setMovieInCache(this);
			conn.disconnect();
		}
    }   
