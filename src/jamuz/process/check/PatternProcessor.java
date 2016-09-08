/*
 * Copyright (C) 2016 raph
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

import jamuz.utils.Inter;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author raph
 */
public class PatternProcessor {
     
    public static Map<String, String> getMap(String path, String pattern) {
        return extract(path, pattern);
    }
  
    public static String toString(String path, String pattern) {
 
        Map<String, String> extracted = extract(path, pattern);
        
        StringBuilder sb = new StringBuilder();
        //FIXME: Sort JList by extracted.size() and select the one having most (as for covers)
        sb.append("<html>".concat(String.valueOf(extracted.size())).concat(" | "));
        Integer counter=0;
        appendValue(sb, "%z", Inter.get("Tag.AlbumArtist"), counter, extracted); //album artist);
        appendValue(sb, "%b", Inter.get("Tag.Album"), counter, extracted);//album
        
        appendValue(sb, "%n", Inter.get("Tag.TrackNo"), counter, extracted); //track#);
        appendValue(sb, "%l", Inter.get("Tag.Artist"), counter, extracted); //# of tracks);
        
        appendValue(sb, "%d", Inter.get("Tag.DiscNo"), counter, extracted); //disc#);
        appendValue(sb, "%x", Inter.get("Tag.Artist"), counter, extracted); //# of discs);
        
        appendValue(sb, "%a", Inter.get("Tag.Artist"), counter, extracted); //artist
        appendValue(sb, "%t", Inter.get("Tag.Title"), counter, extracted); //title);
        
        appendValue(sb, "%y", Inter.get("Tag.Year"), counter, extracted); //year);
        
        appendValue(sb, "%c", Inter.get("Tag.Comment"), counter, extracted); //comment);
        sb.append("</html>");
        return sb.toString();
    }
    
    private static void appendValue(StringBuilder sb, String key, String label, Integer counter, Map<String, String> extracted) {
        if(extracted.containsKey(key)) {
            String value = extracted.get(key);
            if(!value.equals("")) {
                sb.append("".concat(label).concat(" : \"<b>").concat(value).concat("</b>\""));
                counter++;
            }
            else {
                sb.append(key.concat(" : {Empty}"));
            }
            sb.append(" ");
        }
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
