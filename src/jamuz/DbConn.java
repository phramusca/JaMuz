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

package jamuz;

import jamuz.utils.Popup;
import jamuz.utils.Inter;
import java.sql.*;
import java.util.logging.Level;
import org.sqlite.SQLiteConfig;

/**
 * Used to connect to a database
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DbConn {
    /**
     * The SQL connection
     */
    protected Connection connection;

	/**
	 *
	 * @return
	 */
	public Connection getConnnection() {
        return connection;
    }
    
	/**
	 * Connection information
	 */
	protected DbInfo info;

	/**
	 *
	 * @return
	 */
	public DbInfo getInfo() {
        return info;
    }

	/**
	 * Creates a new SQL database connection
	 * @param inDbInfo
	 */
	public DbConn(DbInfo inDbInfo) {
		this.info = inDbInfo;
	}
	
	/**
	 * Connect database
	 * @return 
	 */
	public boolean connect() {
		try {
            switch (this.info.libType) {
                case Sqlite:
                    //NOI18N
                    Class.forName("org.sqlite.JDBC");  //NOI18N
                    //This is to enforce foreign keys usage
                    SQLiteConfig config = new SQLiteConfig();
                    config.enforceForeignKeys(true);
                    connection = DriverManager.getConnection("jdbc:sqlite:" + this.info.locationWork,config.toProperties()); //NOI18N //NOI18N
                    break;
                case MySQL:
                    //NOI18N
                    Class.forName("com.mysql.jdbc.Driver").newInstance();  //NOI18N
                    connection = DriverManager.getConnection("jdbc:mysql://" + this.info.locationWork, this.info.user, this.info.pwd); //NOI18N //NOI18N
                    break;
                default: 
                    Popup.error(Inter.get("Error.ConnectDatabase")+" \""+this.info.locationWork+"\"  "+" \""+this.info.libType+"\"  ");  //NOI18N
                    return false;
            }
			return true;
		} catch (ClassNotFoundException | SQLException | InstantiationException | IllegalAccessException ex) {
			Popup.error(Inter.get("Error.ConnectDatabase")+" \""+this.info.locationWork+"\"", ex);  //NOI18N
			return false;
		}
	}

	/**
	 * Disconnect database
	 */
	public final void disconnect() {
		try {
			connection.close();
		} catch (SQLException ex) {
			Jamuz.getLogger().log(Level.SEVERE, "DbConn.disconnect()", ex);  //NOI18N
		}
	}

    
	/**
	 * Get string value from database, replaced by given default value if empty or any problem occured
	 * @param rs
	 * @param source
	 * @param defaultValue
	 * @return
	 */
	public String getStringValue(ResultSet rs, String source, String defaultValue) {
		String value = getStringValue(rs, source);
		if(value.startsWith("{")) {  //NOI18N
			value = defaultValue;
		}
		return value;
	}
	
	/**
	 * Get string value from database, replacing empty string with {Empty}
	 * @param rs
	 * @param source
	 * @return
	 */
	public String getStringValue(ResultSet rs, String source) {
		return getStringValue(rs, source, true);
	}

	/**
	 * Get String value from database. 
	 * Returns {NoSource} if source is not empty.
	 * Returns {null} if value is null.
	 * Returns {Empty} if value is empty (if replaceEmpty==true)
	 * Returns {ERROR} if an error occured
	 * @param rs
	 * @param source
	 * @param replaceEmpty
	 * @return
	 */
	public String getStringValue(ResultSet rs, String source, boolean replaceEmpty) {
		try {
			if(source.equals("")) {  //NOI18N
				source = "{NoSource}";  //NOI18N
			}
			else {
				source = rs.getString(source);
				if(source == null) {
					source = "{null}";  //NOI18N
				}
				else if(source.equals("")) {  //NOI18N
					if(replaceEmpty) {
						source = "{Empty}";  //NOI18N
					}
				}
			}
			return source;
		} catch (SQLException ex) {
			Popup.error(ex);
			return "{ERROR}";  //NOI18N
		}
	}
}
