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
package jamuz.soulseek;

import jamuz.gui.swing.ProgressBar;
import jamuz.utils.DateTime;
import java.util.Date;

/**
 *
 * @author raph
 */
public class SlskFile {
	//Common
	public String filename;
	public int size;
	
	//From SlskdSearchFile
	public int bitDepth;
	public int bitRate;
	public int code;
	public String extension="null";
	public boolean isVariableBitRate;
	public int length;
	public int sampleRate;
	public boolean isLocked;
	
	//From SlskdDownloadFile
		public String id;
	public String username=""; // Also set along with SlskdSearchFile
		public String direction="null";
		public int startOffset;
	public String state="null";
	
	public String requestedAt="null";
	public String enqueuedAt="null";
	public String startedAt="null";
	public String endedAt="null";
	public String searchedAt="null";
	
	String getDate() {
		String date = 
				!endedAt.equals("null")?endedAt
				:!startedAt.equals("null")?startedAt
				:!enqueuedAt.equals("null")?enqueuedAt
				:!requestedAt.equals("null")?requestedAt
				:"--";
		if(date.equals("--")) {
			date = searchedAt;
		} else {
			//Parse and convert to local time
			Date parseUTC = DateTime.parseUTC(removeDotAndAfter(date), DateTime.DateTimeFormat.MS);
			date = DateTime.formatUTC(parseUTC, DateTime.DateTimeFormat.HUMAN, true);
		}
		return date;
	}
	
	private static String removeDotAndAfter(String input) {
		int dotIndex = input.indexOf('.');
		if (dotIndex >= 0) {
			return input.substring(0, dotIndex);
		} else {
			return input;
		}
	}
		
	public double percentComplete;
	public double averageSpeed;
	
	private final ProgressBar progressBar;
	
	/**
	 *
	 * @return
	 */
	public ProgressBar getProgressBar() {
		return progressBar;
	}
	
	//FIXME !! Display bytesTransferred, bytesRemaining, elapsedTime and remainingTime
		public int bytesTransferred;
		public int bytesRemaining;
		public String elapsedTime="null";
		public String remainingTime="null";

	SlskFile(SlskdSearchFile file, String username, String searchedAt) {
		this.bitDepth=file.bitDepth;
		this.bitRate=file.bitRate;
		this.code=file.code;
		this.extension=file.extension;
		this.filename=file.filename;
		this.isLocked=file.isLocked;
		this.isVariableBitRate=file.isVariableBitRate;
		this.length=file.length;
		this.sampleRate=file.sampleRate;
		this.size=file.size;
		this.username=username;
		this.searchedAt=searchedAt;
		this.state="Searched";
		
		this.progressBar = new ProgressBar();
		this.progressBar.setup(100);
		this.progressBar.displayAsPercent();
	}

	void update(SlskdDownloadFile filteredFile) {
//		this.filename=filteredFile.filename;
//		this.size=filteredFile.size;
//		this.username=filteredFile.username;
		this.averageSpeed=filteredFile.averageSpeed;
		this.bytesRemaining=filteredFile.bytesRemaining;
		this.bytesTransferred=filteredFile.bytesTransferred;
		this.direction=filteredFile.direction;
		this.elapsedTime=filteredFile.elapsedTime;
		this.endedAt=filteredFile.endedAt;
		this.enqueuedAt=filteredFile.enqueuedAt;
		this.id=filteredFile.id;
		this.percentComplete=filteredFile.percentComplete;
		
		this.remainingTime=filteredFile.remainingTime;
		this.requestedAt=filteredFile.requestedAt;
		this.startOffset=filteredFile.startOffset;
		this.startedAt=filteredFile.startedAt;
		this.state=filteredFile.state;
		this.progressBar.progress(remainingTime, (int) Math.round(percentComplete));
		
	}

	public String getKey() {
		return "[" + size + "]" + filename;
	}
}
