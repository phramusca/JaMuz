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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class PatternProcessor {

	/**
	 * Gets Map<Identifier, Value> of extracted items
	 * @param path
	 * @param pattern
	 * @return
	 */
	public static Map<String, String> getMap(String path, String pattern) {
        
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
		String nextChars;
        for(int i = 0; i < pattern.length(); i++)
        {
            Character c = pattern.charAt(i);
            if(c == '%') {
                if(!sb.toString().equals("")) {
                    params.add(sb.toString());
				}
                sb = new StringBuilder();
				if((i+1)>=pattern.length()) {
					break;
				}
				if((i+5)<pattern.length() && pattern.substring(i+1, i+5).contains("::")) {
					nextChars=pattern.substring(i+1, i+5);
					i+=3;
				} else {
					nextChars=String.valueOf(pattern.charAt(i+1));
				}
                params.add("%"+nextChars);
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
		String paramBefore;
        String analyzedText = path;
        for(int i = 0; i < params.size(); i++) {
            param=params.get(i);
            if(!param.startsWith("%")) {
                posParam = analyzedText.indexOf(param);
                if(posParam<0) return extracted;
                before = analyzedText.substring(0, posParam);
                analyzedText = analyzedText.substring(posParam+param.length());
				if(i>0) {
					paramBefore = params.get(i-1);
					if(paramBefore.contains("::")) {
						switch(paramBefore.substring(4, 5)) {
							case "U":
								before=before.toUpperCase();
								break;
							case "l":
								before=before.toLowerCase();
								break;
							case "c":
								before=WordUtils.capitalize(before);
								break;
							case "C":
								before=WordUtils.capitalizeFully(before);
								break;
							case "u":
								before=WordUtils.uncapitalize(before);
								break;
							case "s":
								before=WordUtils.swapCase(before);
								break;
						}
						paramBefore=paramBefore.substring(0, 2);
					}
					extracted.put(paramBefore, before);
				}
            }
        }
        return extracted;
    }

}