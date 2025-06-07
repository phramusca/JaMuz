/*
 * Copyright (C) 2015 phramusca <phramusca@gmail.com>
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

import info.movito.themoviedbapi.model.tv.TvSeries;
import jamuz.database.DbInfo;
import static jamuz.process.video.PanelVideo.comboRating;
import java.io.Serializable;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class MyTvShow extends MyVideoAbstract implements Serializable {
	private TvSeries serie;

	/**
	 *
	 * @param serie
	 */
	public MyTvShow(TvSeries serie) {
		super();
//            this.userRating = comboRating[Math.round(serie.getUserRating())-1];
		int rating = Math.round(serie.getUserRating());
		this.userRating = rating<=0?new VideoRating(0, "0 - Not Rated"):comboRating[rating-1];
		this.serie = serie;

		if(serie.getFirstAirDate()!=null) {
			this.year = getYear(serie.getFirstAirDate());
//                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  //NOI18N
//                try {
//                    Date releaseDate = dateFormat.parse(serie.getFirstAirDate());
//                    Calendar cal = Calendar.getInstance();
//                    cal.setTime(releaseDate);
//                    this.year = cal.get(Calendar.YEAR);
//                } catch (ParseException ex) {
////                    Popup.error(ex);
//                }
		}
	}

	/**
	 *
	 * @return
	 */
	public TvSeries getSerie() {
		return serie;
	}

	/**
	 *
	 * @param movieDb
	 */
	public void setSerie(TvSeries movieDb) {
		this.serie = movieDb;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public int getId() {
		return this.serie.getId();
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String getHomepage() {
		return this.serie.getHomepage();
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String toString() {
		return this.serie.getName()+" ("+this.serie.getUserRating()+")";
	}

	/**
	 *
	 */
	@Override
	public void setMyVideoInCache() {
		DbConnVideo conn = new DbConnVideo(new DbInfo(DbInfo.LibType.Sqlite, "myMovieDb.db", ".", "."), "");
		conn.connect();
		conn.setTvShowInCache(this);
		conn.disconnect();
	}
}
