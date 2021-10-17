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

package jamuz.process.book;

import jamuz.DbConn;
import jamuz.DbInfo;
import jamuz.Jamuz;
import jamuz.utils.Popup;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Creates a new connection to a Kodi database to get videos information
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DbConnBook extends DbConn {


	/**
	 * Creates a database connection.
	 * @param dbInfo
     * @param rootPath
	 */
	public DbConnBook(DbInfo dbInfo) {
		super(dbInfo);
	}
   
	/**
	 * Gets movies list
	 * @param books
	 * @return
	 */
	public boolean getBooks(List<Book> books) {
		String title; String title_sort; String pubdate; String author_sort; 
        String uuid; String path; String formats;
		String comment; String rating; String language; String author;
		String filenameWithoutExtension; String tags;
		books.clear();
        ResultSet rs = null;
		try {
			PreparedStatement stSelectBooks = connection.prepareStatement(
					"SELECT \n" +
					"REPLACE(GROUP_CONCAT(DISTINCT A.name), ',', ' / ') AS author, \n" +
					"CASE WHEN  COUNT(DISTINCT A.id)>1 THEN \"Various\" ELSE A.sort END AS author_sort, \n" +
					"REPLACE(GROUP_CONCAT(DISTINCT D.format), ',', ' / ') AS formats,\n" +
					"REPLACE(GROUP_CONCAT(DISTINCT T.name), ',', ' / ') AS tags, \n" +
					"B.title, B.sort AS title_sort, \n" +
					"(R.rating/2) AS rating, L.lang_code AS language, \n" +
					"B.pubdate, B.uuid, \n" +
					"B.path, D.name AS filenameWithoutExtension, \n" +
					"C.text AS comment\n" +
					"FROM books B \n" +
					"LEFT OUTER JOIN books_authors_link AL ON B.id=AL.book \n" +
					"LEFT OUTER JOIN authors A ON A.id = AL.author \n" +
					"LEFT OUTER JOIN books_languages_link LL ON B.id=LL.book \n" +
					"LEFT OUTER JOIN languages L ON L.id = LL.lang_code \n" +
					"LEFT OUTER JOIN books_ratings_link RL ON B.id=RL.book \n" +
					"LEFT OUTER JOIN ratings R ON R.id = RL.rating \n" +
					"LEFT OUTER JOIN books_tags_link TL ON B.id=TL.book \n" +
					"LEFT OUTER JOIN tags T ON T.id = TL.tag \n" +
					"LEFT OUTER JOIN comments C ON C.book=B.id \n" +
					"LEFT OUTER JOIN data D ON B.id = D.book \n" +
					"GROUP BY B.id\n" +
					"ORDER BY B.sort");    //NOI18N
			//Execute query
			rs = stSelectBooks.executeQuery();
			while(rs.next()){
                title_sort=getStringValue(rs, "title_sort"); //NOI18N
                pubdate=getStringValue(rs, "pubdate"); //NOI18N
                author_sort=getStringValue(rs, "author_sort"); //NOI18N
				path=getStringValue(rs, "path");  //NOI18N
				title=getStringValue(rs, "title");  //NOI18N
				uuid=getStringValue(rs, "uuid");  //NOI18N
				comment=getStringValue(rs, "comment");  //NOI18N
				rating=getStringValue(rs, "rating");  //NOI18N
				language=getStringValue(rs, "language");  //NOI18N
				author=getStringValue(rs, "author");  //NOI18N
				filenameWithoutExtension=getStringValue(rs, "filenameWithoutExtension");  //NOI18N
				formats=getStringValue(rs, "formats");  //NOI18N
				tags=getStringValue(rs, "tags");  //NOI18N
                Book book = new Book(title, title_sort, pubdate, author_sort, 
						uuid, path, comment, rating, language, author, 
						filenameWithoutExtension, formats, tags);

				books.add(book);
			}
			return true;
		} catch (SQLException ex) {
			Popup.error(ex);
			return false;
		}
        finally {
            try {
                if (rs!=null) {
					rs.close();
				}
            } catch (SQLException ex) {
                Jamuz.getLogger().warning("Failed to close ResultSet");
            }
            
        }
	}
      
//    /**
//     * Update file's idPath and filename
//     * @param idFile
//     * @param newIdPath
//     * @param newFilename
//     * @return
//     */
//    public boolean updateFile(int idFile, int newIdPath, String newFilename) {
//		try {
//			PreparedStatement stUpdateFile = 
//					connection.prepareStatement("UPDATE files SET strFilename=?, idPath=? WHERE idFile=?"); //NOI18N
//			stUpdateFile.setString(1, newFilename);
//            stUpdateFile.setInt(2, newIdPath);
//			stUpdateFile.setInt(3, idFile);
//			int nbRowsAffected = stUpdateFile.executeUpdate();
//			if(nbRowsAffected==1) {
//				return true;
//			}
//			else {
//				Jamuz.getLogger().log(Level.SEVERE, "stUpdateFile, idFile={0}, newFilename={1} # row(s) affected: +{2}", new Object[]{idFile, newFilename, nbRowsAffected});  //NOI18N
//				return false;
//			}
//		} catch (SQLException ex) {
//			Popup.error("updateFile("+idFile+", "+newFilename+")", ex);  //NOI18N
//			return false;
//		}
//	}
}
