/*
 * Copyright (C) 2020 phramusca <phramusca@gmail.com>
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
package jamuz.acoustid;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class Results {
	String status;
	List<Result> results = new ArrayList<>();
	AcoustIdResult bestResult;
	ChromaPrint chromaprint;
	
	/**
	 *
	 * @return
	 */
	public AcoustIdResult getBest() {
		if(bestResult==null) {
			if (status.compareTo("ok") == 0 && results.size()>0) {
				Result best = results.stream().max(Comparator.comparing(Result::getScore)).get();
				try {
					bestResult = best.getFirst();
				} catch(NoSuchElementException ex) {
				}
			}	
		}
		return bestResult;
	}

	/**
	 *
	 * @return
	 */
	public ChromaPrint getChromaprint() {
		return chromaprint;
	}
}