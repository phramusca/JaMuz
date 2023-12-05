/*
 * Copyright (C) 2023 phramusca <phramusca@gmail.com>
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
import jamuz.utils.StringManager;
import java.util.Date;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class SlskdSearchFile {
	public String filename;
	public int size;
	public int bitDepth;
	public int bitRate;
	public int code;
	public String extension;
	public boolean isVariableBitRate;
	public int length;
	public int sampleRate;
	public boolean isLocked;	

    public String id;
    public String direction="null";
    public int startOffset;
	public String state="null";
	public String requestedAt="null";
	public String enqueuedAt="null";
	public String startedAt="null";
	public String endedAt="null";
	public String searchedAt="null";
    public double percentComplete;
	public double averageSpeed;
//	@Expose(deserialize = false, serialize = false)
	private transient ProgressBar progressBar = new ProgressBar();
    public int bytesTransferred;
    public int bytesRemaining;
    public String elapsedTime="null";
    public String remainingTime="null";

    public SlskdSearchFile() {
		this.state="Searched";
        this.progressBar.setMsgMax(500);
		this.progressBar.setupAsPercentage();
        this.progressBar.setString(state);
    }
    
    void update(SlskdDownloadFile filteredFile) {
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
        
        String msg = state + " (" +
            StringManager.humanReadableByteCount(bytesTransferred, true) + " / " +
                StringManager.humanReadableByteCount(bytesRemaining, true) + ")";
        
        if(!elapsedTime.equals("null") && !remainingTime.equals("null")) {
            msg = msg + " [" + removeDotAndAfter(elapsedTime)+" @ "+StringManager.humanReadableByteCount(averageSpeed, true)+"/s / "+removeDotAndAfter(remainingTime)+"]";
        }
        
		this.progressBar.progress(msg, (int) Math.round(percentComplete));
	}
    
	public ProgressBar getProgressBar() {
		return progressBar;
	}
    
    public String getFilename() {
        return filename;
    }

    String getPath() {
        return FilenameUtils.getFullPathNoEndSeparator(filename);
    }
    
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
			date = convertDate(date);
		}
		return date;
	}
	
    private String convertDate(String date) {
        Date parseUTC = DateTime.parseUTC(removeDotAndAfter(date), DateTime.DateTimeFormat.MS);
        return DateTime.formatUTC(parseUTC, DateTime.DateTimeFormat.HUMAN, true);
    }
    
	private static String removeDotAndAfter(String input) {
		int dotIndex = input.indexOf('.');
		if (dotIndex >= 0) {
			return input.substring(0, dotIndex);
		} else {
			return input;
		}
	}
    
    public String getKey() {
		return "[" + size + "]" + filename;
	}
}
