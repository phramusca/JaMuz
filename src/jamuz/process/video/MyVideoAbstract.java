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

import static jamuz.process.video.PanelVideo.comboRating;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author raph
 */
public abstract class MyVideoAbstract {
        protected boolean isFavorite;
        protected boolean isInWatchList;
        protected VideoRating userRating;
        protected int year;

        public MyVideoAbstract() {
            this.isFavorite=false;
            this.isInWatchList=false;
            this.userRating = new VideoRating(0, "Not Rated");
            this.year = 0;
        }

        abstract public int getId(); 
        
        abstract public String getHomepage();
        
        public boolean isIsFavorite() {
            return isFavorite;
        }

        public boolean isIsInWatchList() {
            return isInWatchList;
        }

        public VideoRating getUserRating() {
            return userRating;
        }

        public void setUserRating(VideoRating userRating) {
            this.userRating = userRating;// userRating;
        }

        public void setIsFavorite(boolean isFavorite) {
            this.isFavorite = isFavorite;
        }

        public void setIsInWatchList(boolean isInWatchList) {
            this.isInWatchList = isInWatchList;
        }

        public int getYear() {
            return year;
        }
        
        /**
         * Gets year from "yyyy-MM-dd" date format.
         * Returns 0 if an error occurs.
         * @param date
         * @return
         */
        public static int getYear(String date) {
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date releaseDate = dateFormat.parse(date);
                Calendar cal = Calendar.getInstance();
                cal.setTime(releaseDate);
                return cal.get(Calendar.YEAR);
            } catch (ParseException ex) {
                return 0;
            }
        }
    }
