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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author raph
 */
public class SlskdSearchResponse {
	public int fileCount;
	public List<SlskdSearchFile> files;
	public boolean hasFreeUploadSlot;
	public int lockedFileCount;
	public List<SlskdSearchFile> lockedFiles;
	public int queueLength;
	public int token;
	public double uploadSpeed;
	public String username;	
	
	private List<SlskdSearchFile> filteredFiles;
	private String date = DateTime.getCurrentLocal(DateTime.DateTimeFormat.HUMAN);

	//FIXME !!!!!! use existing option
	private static final List<String> ALLOWED_EXTENSIONS 
			= Collections.unmodifiableList(
					Arrays.asList("mp3", "flac"));
	
	public String getDate() {
		return date;
	}
	
	public void filterFiles() {
		filteredFiles = files.stream().filter(file -> 
				ALLOWED_EXTENSIONS.contains(
						FilenameUtils.getExtension(file.filename)))
				.collect(Collectors.toList());
	}

	public List<SlskdSearchFile> getFilteredFiles() {
		return filteredFiles;
	}
	
	public double getBitrate() {
		if(getFilteredFiles()!=null & !getFilteredFiles().isEmpty()) {
			return Math.round(getFilteredFiles().stream()
                .mapToDouble(file -> file.bitRate)
                .average()
                .orElse(0.0));
		} else {
			return 0.0;
		}
	}

	public double getSize() {
		if(getFilteredFiles()!=null & !getFilteredFiles().isEmpty()) {
			double meanSize = getFilteredFiles().stream()
				.mapToDouble(file -> file.size)
				.sum();
			return meanSize;
//			//FIXME !!!!!! Check displayed matches slskd, and think of row sorting before changing
//			return StringManager.humanReadableByteCount(meanSize, false);
		}
		return 0;
	}

	//FIXME ! Split by path: one line by path (and we can then also remove the path in download table)
	public String getPath() {
		List<String> paths = getPaths();
		String path = !paths.isEmpty()?"["+paths.size()+"] "+paths.get(0):"N/A";
		return path;
	}
	
	public List<String> getPaths() {
		List<String> paths = getFilteredFiles().stream()
                .map(file -> FilenameUtils.getFullPathNoEndSeparator(file.filename))
                .distinct()
                .collect(Collectors.toList());
		return paths;
	}

	public double getSpeed() {
		return uploadSpeed;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("SlskdSearchResponse{");
		sb.append("fileCount=").append(fileCount);
		sb.append(", hasFreeUploadSlot=").append(hasFreeUploadSlot);
		sb.append(", lockedFileCount=").append(lockedFileCount);
		sb.append(", queueLength=").append(queueLength);
		sb.append(", uploadSpeed=").append(uploadSpeed);
		sb.append(", username=").append(username);
		sb.append('}');
		return sb.toString();
	}
}
