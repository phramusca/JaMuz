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
	
	//FIXME !!! display proper dates accordingly (to state ?)
		public String requestedAt="null";
		public String enqueuedAt="null";
	public String startedAt="null";
		public String endedAt="null";
	
	public double percentComplete;
	public double averageSpeed;
	
	//FIXME !!! Display those below
		public int bytesTransferred;
		public int bytesRemaining;
		public String elapsedTime="null";
		public String remainingTime="null";

	SlskFile(SlskdSearchFile file, String username) {
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
	}

	public String getMoreInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("SlskdFile{");
		sb.append("id=").append(id);
		sb.append(", direction=").append(direction);
		sb.append(", startOffset=").append(startOffset);
		sb.append(", requestedAt=").append(requestedAt);
		sb.append(", enqueuedAt=").append(enqueuedAt);
		sb.append(", startedAt=").append(startedAt);
		sb.append(", endedAt=").append(endedAt);
		sb.append(", percentComplete=").append(percentComplete);
		sb.append(", averageSpeed=").append(averageSpeed);
		sb.append('}');
		return sb.toString();
	}

	void update(SlskdDownloadFile filteredFile) {
		this.averageSpeed=filteredFile.averageSpeed;
		this.bytesRemaining=filteredFile.bytesRemaining;
		this.bytesTransferred=filteredFile.bytesTransferred;
		this.direction=filteredFile.direction;
		this.elapsedTime=filteredFile.elapsedTime;
		this.endedAt=filteredFile.endedAt;
		this.enqueuedAt=filteredFile.enqueuedAt;
//		this.filename=filteredFile.filename;
		this.id=filteredFile.id;
		this.percentComplete=filteredFile.percentComplete;
		this.remainingTime=filteredFile.remainingTime;
		this.requestedAt=filteredFile.requestedAt;
//		this.size=filteredFile.size;
		this.startOffset=filteredFile.startOffset;
		this.startedAt=filteredFile.startedAt;
		this.state=filteredFile.state;
//		this.username=filteredFile.username;
	}

	public String getKey() {
		return "[" + size + "]" + filename;
	}
}
