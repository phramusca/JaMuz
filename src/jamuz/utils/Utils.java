/*
 * Copyright (C) 2017 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Utils {
	
	public static boolean equalLists(List<String> one, List<String> two){     
		if (one == null && two == null){
			return true;
		}

		if((one == null && two != null) 
		  || one != null && two == null
		  || one.size() != two.size()){
			return false;
		}

		//to avoid messing the order of the lists
		one = new ArrayList<>(one); 
		two = new ArrayList<>(two);   

		Collections.sort(one);
		Collections.sort(two);      
		return one.equals(two);
	}
	
	public static int getInteger(String entry) {
        try {
            return Integer.parseInt(entry);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
	
}
