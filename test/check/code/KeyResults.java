/*
 * Copyright (C) 2018 phramusca ( https://github.com/phramusca/JaMuz/ )
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
package check.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
class KeyResults {
	List<String> values;
	Map<String, List<Result>> fileResults;

	public KeyResults() {
		values=new ArrayList<>();
		fileResults=new HashMap<>();
	}
	
	public void add(String file, Result result) {
		List<Result> resultList;
		if(fileResults.containsKey(file)) {
			resultList = fileResults.get(file);
		} else {
			resultList = new ArrayList<>();
		}
		resultList.add(result);
		fileResults.put(file, resultList);
	}
}
