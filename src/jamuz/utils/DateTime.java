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

package jamuz.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * DateTime formatting class
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DateTime {
	/**
	 * Supported dateTime formats
	 */
	public enum DateTimeFormat {
		/**
		 * SQL dateTime format ("yyyy-MM-dd HH:mm:ss")
		 */
		SQL("yyyy-MM-dd HH:mm:ss"),
		/**
		 * Human dateTime format ("dd/MM/yyyy HH:mm:ss")
		 */
		HUMAN("dd/MM/yyyy HH:mm:ss"),
		/**
		 * File dateTime format ("yyyy-MM-dd--HH-mm-ss")
		 */
		FILE("yyyy-MM-dd--HH-mm-ss") ;
    
        private final String pattern;
		private DateTimeFormat(String display) {
			this.pattern = display;
		}

		/**
		 *
		 * @return
		 */
		public String getPattern() {
			return pattern;
		}
    }
	
    /**
	 * Format UTC dateTime to custom format
	 * @param date
	 * @param format
     * @param toLocal
	 * @return
	 */
	public static String formatUTC(Date date, String format, boolean toLocal) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        if(!toLocal) {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return simpleDateFormat.format(date);
	}
    
	/**
	 * Format UTC dateTime to desired format
	 * @param date
	 * @param format
     * @param toLocal
	 * @return
	 */
	public static String formatUTC(Date date, DateTimeFormat format, boolean toLocal) {
        return formatUTC(date, format.getPattern(), toLocal);
	}

	/**
	 * Format UTC dateTime to UTC dateTime in SQL format
	 * @param date
	 * @return
	 */
	public static String formatUTCtoSqlUTC(Date date) {
		return formatUTC(date, DateTimeFormat.SQL, false);
	}
 
    /**
     * Format UTC dateTime to local dateTime in SQL format
     * @param date
     * @return
     */
    public static String formatUTCtoSqlLocal(Date date) {
        return formatUTC(date, DateTimeFormat.SQL, true);
    }
    
	/**
	 * Get current local dateTime in desired format
	 * @param format
	 * @return
	 */
	public static String getCurrentLocal(DateTimeFormat format) {
		return formatUTC(new Date(), format, true);
	}
	
    /**
     * Get current UTC dateTime in SQL format.
     * @return
     */
    public static String getCurrentUtcSql() {
        return formatUTC(new Date(), DateTimeFormat.SQL, false);
    }
	
	/**
	 * Parse date given as string according to desired format
	 * @param date
	 * @param format
	 * @return
	 */
	private static Date parseUTC(String date, DateTimeFormat format) {
		if(date.isBlank()) {
            return new Date(0); 
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format.getPattern());
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return simpleDateFormat.parse(date);
            
        } catch (NumberFormatException | ParseException | ArrayIndexOutOfBoundsException ex) {
            //        Exception in thread "Thread-8" java.lang.ArrayIndexOutOfBoundsException: 53
//	at sun.util.calendar.BaseCalendar.getCalendarDateFromFixedDate(BaseCalendar.java:454)
//	at java.util.GregorianCalendar.computeFields(GregorianCalendar.java:2333)
//	at java.util.GregorianCalendar.computeTime(GregorianCalendar.java:2753)
//	at java.util.Calendar.updateTime(Calendar.java:2606)
//	at java.util.Calendar.getTimeInMillis(Calendar.java:1118)
//	at java.util.Calendar.getTime(Calendar.java:1091)
//	at java.text.SimpleDateFormat.parse(SimpleDateFormat.java:1473)
//	at java.text.DateFormat.parse(DateFormat.java:355)
//	at utils.DateTime.parse(DateTime.java:129)
			return new Date(0);
		}
	}
	
	/**
	 * Parse SQL formatted UTC dateTime 
	 * @param date
	 * @return
	 */
	public static Date parseSqlUtc(String date) {
        return parseUTC(date, DateTimeFormat.SQL);
	}
}
