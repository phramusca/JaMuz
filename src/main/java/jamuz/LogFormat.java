/*
 * Copyright (C) 2012 phramusca ( https://github.com/phramusca/JaMuz/ )
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
package jamuz;

import jamuz.utils.DateTime;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * A Formatter extension for current application
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class LogFormat extends Formatter {

	/**
	 *
	 * @param record
	 * @return
	 */
	@Override
	public String format(LogRecord record) {

		StringBuilder s = new StringBuilder(1000);
		s.append("<tr bgcolor=\"#");  //NOI18N
		if (record.getLevel().equals(Level.SEVERE)) {
			s.append("ff0000");  //NOI18N
		} else if (record.getLevel().equals(Level.WARNING)) {
			s.append("ff8c00");  //NOI18N
		} else if (record.getLevel().equals(Level.INFO)) {
			s.append("ffe7ba");  //NOI18N
		} else if (record.getLevel().equals(Level.CONFIG)) {
			s.append("cdba96");  //NOI18N
		} else if (record.getLevel().equals(Level.FINE)) {
			s.append("9fb6cd");  //NOI18N
		} else if (record.getLevel().equals(Level.FINER)) {
			s.append("b9d3ee");  //NOI18N
		} else if (record.getLevel().equals(Level.FINEST)) {
			s.append("c6e2ff");  //NOI18N
		} else {
			//This should not happen !
			s.append("ff0000");  //NOI18N
		}
		s.append("\">");  //NOI18N

		s.append("<td>").append(record.getLevel()).append("</td>\n");  //NOI18N
		s.append("<td>").append(DateTime.formatUTCtoSqlLocal(new Date(record.getMillis()))).append("</td>\n");  //NOI18N

		//Replace \n to <BR/> for HTML display
		record.setMessage(record.getMessage().replaceAll("\n", "<BR/>"));  //NOI18N

		s.append("<td>").append(formatMessage(record));  //NOI18N
		if (record.getThrown() != null) {
			s.append("<HR>").append(record.getThrown());  //NOI18N
		}
		s.append("</td>\n");  //NOI18N
		s.append("<td>").append(record.getSourceClassName()).append("</td>\n");  //NOI18N
		s.append("<td>").append(record.getSourceMethodName()).append("</td>\n");  //NOI18N

		s.append("<tr>\n");  //NOI18N
		return s.toString();
	}

	/**
	 *
	 * @param h
	 * @return
	 */
	@Override
	public String getHead(Handler h) {
		return "<html>\n<body>\n<table border=1>\n";  //NOI18N
	}

	/**
	 *
	 * @param h
	 * @return
	 */
	@Override
	public String getTail(Handler h) {
		return "</table>\n</body>\n</html>\n";  //NOI18N
	}
}
