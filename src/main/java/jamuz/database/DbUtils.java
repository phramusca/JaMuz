/*
 * Copyright (C) 2023 raph
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
package jamuz.database;

/**
 *
 * @author raph
 */
public class DbUtils {

    public static String getCSVlist(boolean[] values) {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for (int i = 0; i < values.length; i++) {
            if (values[i]) {
                builder.append(i).append(",");
            }
        }
        builder.deleteCharAt(builder.length() - 1).append(") ");
        return builder.toString();
    }
    
    public static String getSqlWHERE(String selGenre, String selArtist, String selAlbum,
			boolean[] selRatings, boolean[] selCheckedFlag,
			int yearFrom, int yearTo, float bpmFrom, float bpmTo, int copyRight) {
		String sql = " \nFROM file F "
				+ // NOI18N
				" \nINNER JOIN `path` P ON P.idPath=F.idPath "
				+ // NOI18N
				" \nWHERE F.rating IN " + getCSVlist(selRatings)
				+ " \nAND P.checked IN " + getCSVlist(selCheckedFlag) // NOI18N
				// FIXME Z PanelSelect Check year valid and offer "allow invalid" as an option
				// https://stackoverflow.com/questions/5071601/how-do-i-use-regex-in-a-sqlite-query
				// else if(yearList.get(0).matches("\\d{4}")) { //NOI18N
				// results.get("year").value=yearList.get(0); //NOI18N
				// }
				+ " \nAND ((F.year>=" + yearFrom + " AND F.year<=" + yearTo + ") OR length(F.year)!=4)" // NOI18N
				// //NOI18N
				+ " \nAND F.BPM>=" + bpmFrom + " AND F.BPM<=" + bpmTo; // NOI18N //NOI18N

		if (!selGenre.equals("%")) { // NOI18N
			sql += " \nAND genre=\"" + selGenre + "\""; // NOI18N
		}
		if (!selArtist.equals("%")) { // NOI18N
			sql += " \nAND (artist=\"" + escapeDoubleQuote(selArtist) + "\" "
					+ "OR albumArtist=\"" + escapeDoubleQuote(selArtist) + "\")"; // NOI18N
		}
		if (!selAlbum.equals("%")) { // NOI18N
			sql += " \nAND album=\"" + escapeDoubleQuote(selAlbum) + "\""; // NOI18N
		}
		if (copyRight >= 0) {
			sql += " \nAND copyRight=" + copyRight + " ";// NOI18N;
		}

		sql += " "; // NOI18N
		return sql;
	}
    
    private static String escapeDoubleQuote(String text) {
		return text.replaceAll("\"", "\"\"");
	}
}
