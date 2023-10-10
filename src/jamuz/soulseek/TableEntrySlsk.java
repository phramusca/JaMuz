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

import jamuz.utils.DateTime;

/**
 *
 * @author raph
 */
public class TableEntrySlsk {

	public TableEntrySlsk(SlskdSearchResponse searchResponse) {
		this.searchResponse = searchResponse;
		username = searchResponse.username;
		speed = searchResponse.uploadSpeed;
		path = searchResponse.getPath();
		size = searchResponse.getSize();
		
		fileCount = searchResponse.fileCount;
		hasFreeUploadSlot = searchResponse.hasFreeUploadSlot;
		lockedFileCount = searchResponse.lockedFileCount;
		queueLength = searchResponse.queueLength;
		bitRate = searchResponse.getBitrate();
		
		startOffset=0;
		state="";
		requestedAt="";
		enqueuedAt="";
		startedAt="";
		endedAt="";
		bytesTransferred=0;
		bytesRemaining=0;
		elapsedTime="";
		percentComplete=0;
		remainingTime="";
	}
	
	public TableEntrySlsk(SlskdDownloadFile downloadFile) {
		username = downloadFile.username;
		speed = downloadFile.averageSpeed;
		path = downloadFile.filename;
		size = downloadFile.size;
		
		fileCount = 0;
		hasFreeUploadSlot = false;
		lockedFileCount = 0;
		queueLength = 0;
		bitRate = 0;
		
		startOffset=downloadFile.startOffset;
		state=downloadFile.state;
		requestedAt=downloadFile.requestedAt;
		enqueuedAt=downloadFile.enqueuedAt;
		startedAt=downloadFile.startedAt;
		endedAt=downloadFile.endedAt;
		bytesTransferred=downloadFile.bytesTransferred;
		bytesRemaining=downloadFile.bytesRemaining;
		elapsedTime=downloadFile.elapsedTime;
		percentComplete=downloadFile.percentComplete;
		remainingTime=downloadFile.remainingTime;
	}
	
	private SlskdSearchResponse searchResponse;
	
	private int fileCount;
	private boolean hasFreeUploadSlot;
	private int lockedFileCount;
	private int queueLength;
	private double speed;
	private String username;	
	private String date = DateTime.getCurrentLocal(DateTime.DateTimeFormat.HUMAN);
	private String path;
	private double bitRate;
	private double size;

	private int startOffset;
	private String state;
	private String requestedAt;
	private String enqueuedAt;
	private String startedAt;
	private String endedAt;
	private int bytesTransferred;
	private int bytesRemaining;
	private String elapsedTime;
	private double percentComplete;
	private String remainingTime;

	public SlskdSearchResponse getSearchResponse() {
		return searchResponse;
	}
	
	public int getFileCount() {
		return fileCount;
	}

	public boolean isHasFreeUploadSlot() {
		return hasFreeUploadSlot;
	}

	public int getLockedFileCount() {
		return lockedFileCount;
	}

	public int getQueueLength() {
		return queueLength;
	}

	public double getSpeed() {
		return speed;
	}

	public String getUsername() {
		return username;
	}

	public String getDate() {
		return date;
	}

	public String getPath() {
		return path;
	}

	public double getBitRate() {
		return bitRate;
	}

	public double getSize() {
		return size;
	}

	public int getStartOffset() {
		return startOffset;
	}

	public String getState() {
		return state;
	}

	public String getRequestedAt() {
		return requestedAt;
	}

	public String getEnqueuedAt() {
		return enqueuedAt;
	}

	public String getStartedAt() {
		return startedAt;
	}

	public String getEndedAt() {
		return endedAt;
	}

	public int getBytesTransferred() {
		return bytesTransferred;
	}

	public int getBytesRemaining() {
		return bytesRemaining;
	}

	public String getElapsedTime() {
		return elapsedTime;
	}

	public double getPercentComplete() {
		return percentComplete;
	}

	public String getRemainingTime() {
		return remainingTime;
	}
	
	
}
