/*
 * Copyright (C) 2016 phramusca ( https://github.com/phramusca/JaMuz/ )
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
package jamuz.process.check;

import jamuz.FileInfoInt;
import jamuz.utils.Inter;
import jamuz.utils.Utils;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class PatternProcessor {
     
	/**
	 *
	 * @param path
	 * @param pattern
	 * @return
	 */
	public static Map<String, String> getMap(String path, String pattern) {
        return extract(path, pattern);
    }
	
	/**
	 *
	 * @param path
	 * @param pattern
	 * @return
	 */
	public static FileInfoInt get(String path, String pattern) {
 
		Map<String, String> extracted = extract(path, pattern);

		String relativePath="";
		String filename="";
		String album=get("%b", extracted);
		String albumArtist=get("%z", extracted);
		String artist=get("%a", extracted);
		String comment=get("%c", extracted);
		int discNo=Utils.getInteger(get("%d", extracted));
		int discTotal=Utils.getInteger(get("%x", extracted));
		String genre="";
		String title=get("%t", extracted);
		int trackNo=Utils.getInteger(get("%n", extracted));
		int trackTotal=Utils.getInteger(get("%l", extracted));
		String year=get("%y", extracted);
		
		FileInfoInt fileInfoInt = new FileInfoInt(-1, -1, relativePath, 
				filename, -1, "", "", -1, 0, album, 
				albumArtist, artist, comment, discNo, discTotal, genre, 
				-1, title, trackNo, trackTotal, year, -1, -1,
				"", "", "", false, "", 
				FolderInfo.CheckedFlag.UNCHECKED, -1, -1, -1, "");

		//FIXME: Display somehow: String.valueOf(extracted.size())
        return fileInfoInt;
    }
	
	private static String get(String key, Map<String, String> extracted) {
        if(extracted.containsKey(key)) {
            String value = extracted.get(key);
            if(!value.equals("")) {
				return new StringBuilder().append("<html>")
						.append("<b>")
						.append(value)
						.append("</b>").append("</html>")
						.toString();
            }
        }
		return "";
    }

    private static Map<String, String> extract(String path, String pattern) {
        
        //Split by path separator and remove unwanted as of pattern
        int nbSeparatorInPattern = StringUtils.countMatches(pattern, File.separator);
        int nbSeparatorInPath = StringUtils.countMatches(path, File.separator);
        int nbToRemove = nbSeparatorInPath - nbSeparatorInPattern;
        if(nbToRemove>0) {
            String[] split = path.split(File.separator);
            List<String> list = Arrays.asList(split);
            list=list.subList(nbToRemove, list.size());
            path="";
            for(String s : list) {
                path = path.concat(s).concat(File.separator);
            }
        }

        //Extract parameters from pattern
        List<String> params = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < pattern.length(); i++)
        {
            Character c = pattern.charAt(i);
            if(c == '%') {
                if(!sb.toString().equals(""))
                    params.add(sb.toString());
                sb = new StringBuilder();
                params.add("%"+pattern.charAt(i+1));
                i++;
            }
            else {
                sb.append(c);
            }
        }
        if(!sb.toString().equals(""))
            params.add(sb.toString());

        //Extract values from path
        Map<String, String> extracted = new HashMap<>();
        String param;
        int posParam;
        String before;
        String analyzedText = path;
        for(int i = 0; i < params.size(); i++) {
            param=params.get(i);
            if(!param.startsWith("%")) {
                posParam = analyzedText.indexOf(param);
                if(posParam<0) return extracted;
                before = analyzedText.substring(0, posParam);
                analyzedText = analyzedText.substring(posParam+param.length());
                extracted.put(params.get(i-1), before);
            }
        }
        return extracted;
    }

}
