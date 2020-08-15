/*
 * Copyright (C) 2020 raph
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
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * @author raph
 */
public class Result {
	String id;
	private List<Recording> recordings = new ArrayList<>();
	String score;
   
	public double getScore() {
		return Double.parseDouble(score);
	}
   
	public AcoustIdResult getFirst() {
		Recording firstRecording = recordings.stream().findFirst().orElseThrow(NoSuchElementException::new);
		AcoustIdResult meta = firstRecording.getMeta();
		meta.setScore(score);
		return meta;
	}	
	
	public AcoustIdResult get(int index) {
		Recording recording = recordings.get(index);
		AcoustIdResult meta = recording.getMeta();
		meta.setScore(score);
		return meta;
	}
}
