/*
 * Copyright (C) 2011 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import java.util.concurrent.TimeUnit;

/**
 * String manager class
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class StringManager {
    /**
	 * Return left portion of a string
	 * @param text
	 * @param length
	 * @return
	 */
	public static String Left(String text, int length)
    {
        return text.substring(0, length);
    }

    /**
	 * Return right portion of a string
	 * @param text
	 * @param length
	 * @return
	 */
	public static String Right(String text, int length)
    {
        return text.substring(text.length() - length, text.length());
    }

    /**
	 * Return portion of a string
	 * @param text
	 * @param start
	 * @param end
	 * @return
	 */
	public static String Mid(String text, int start, int end)
    {
        return text.substring(start, end);
    }

    /**
	 * Return portion of a string
	 * @param text
	 * @param start
	 * @return
	 */
	public static String Mid(String text, int start)
    {
        return text.substring(start, text.length() - start);
    }

    /**
     * Remove illegal characters from path and filename.
     * Includes nearly (no one is perfect) all windows and linux ones.
     * Windows has much more that Linux but removing on both
     * systems for compatibility. Anyway, not that important characters
     * for an audio filename ...
     * @param str
     * @return
     */
    public static String removeIllegal(String str) {
        String pattern = "[\\\\/:\"*?<>|.!]+"; //NOI18N
        return str.replaceAll(pattern, "_"); //NOI18N
    }
    
	/**
	 *
	 * @param text
	 * @return
	 */
	public static String getNullableText(String text) {
        if(text==null) {
            return "null";
        }
        else {
            return text;
        }
    }
    
    /**
     * Convert number of bytes into human readable formatDisplay (Kio, Ko, ...)
     *
     * @param bytes
     * @param si
     * @return
     */
    public static String humanReadableByteCount(long bytes, boolean si) {
        if (bytes < 0) {
            bytes = Math.abs(bytes);
        }

        int unit = si ? 1000 : 1024;
        if (bytes < unit) {
            return bytes + " o"; //NOI18N
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i"); //NOI18N
        return String.format("%.1f %so", bytes / Math.pow(unit, exp), pre); //NOI18N
    }
    
	/**
	 *
	 * @param seconds
	 * @return
	 */
	public static String secondsToMMSS(int seconds) {
        return String.format("%02d:%02d", //NOI18N
                TimeUnit.SECONDS.toMinutes(seconds),
                TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(seconds))
        );
    }
    
	/**
	 *
	 * @param seconds
	 * @return
	 */
	public static String secondsToHHMM(int seconds) {
        return String.format("%02d h %02d", //NOI18N
                TimeUnit.SECONDS.toHours(seconds),
                TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(seconds))
        );
    }
    
	/**
	 *
	 * @param seconds
	 * @return
	 */
	public static String humanReadableSeconds(long seconds) {
        if (seconds <= 0) {
            return "-";
        }

        final long days = TimeUnit.SECONDS.toDays(seconds);
        seconds -= TimeUnit.DAYS.toSeconds(days);
        final long hours = TimeUnit.SECONDS.toHours(seconds);
        seconds -= TimeUnit.HOURS.toSeconds(hours);
        final long minutes = TimeUnit.SECONDS.toMinutes(seconds);
        seconds -= TimeUnit.MINUTES.toSeconds(minutes);
//        final long seconds = TimeUnit.SECONDS.toSeconds(millis);
//        millis -= TimeUnit.SECONDS.toMillis(seconds);

        final StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days);
            sb.append("d ");
        }
        if (hours > 0) {
            sb.append(String.format("%02d", hours));
            sb.append("h ");
        }
        if (minutes > 0) {
            sb.append(String.format("%02d", minutes));
            sb.append("m ");
        }
        if (seconds > 0) {
            sb.append(String.format("%02d", seconds));
            sb.append("s");
        }
//        if ((seconds <= 0) && (millis > 0) && showMS) {
//            sb.append(String.format("%02d", millis));
//            sb.append("ms");
//        }

        return sb.toString();
    }
    
    
    //	public static String humanReadableMilliSeconds(long millis)
//    {
//        long hours = TimeUnit.MILLISECONDS.toHours(millis);
//        millis -= TimeUnit.HOURS.toMillis(hours);
//        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
//        millis -= TimeUnit.MINUTES.toMillis(minutes);
//        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
//
//        return String.format("%02dh %02dm %02ds", hours, minutes, seconds);
//    }
}
