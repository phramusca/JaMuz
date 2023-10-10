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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author raph
 */
public class SlskdSearchResponse {
	public int fileCount;
	public List<SlskdFile> files;
	public boolean hasFreeUploadSlot;
	public int lockedFileCount;
	public List<SlskdFile> lockedFiles;
	public int queueLength;
	public int token;
	public int uploadSpeed;
	public String username;	
	
	private String date = DateTime.getCurrentLocal(DateTime.DateTimeFormat.HUMAN);
	
	public String getDate() {
		return date;
	}

	public double getBitrate() {
		if(files!=null & !files.isEmpty()) {
			return Math.round(files.stream()
                .mapToDouble(file -> file.bitRate)
                .average()
                .orElse(0.0));
		} else {
			return 0.0;
		}
	}

//	public String getSize() {
//		if(files!=null & !files.isEmpty()) {
//			double meanSize = files.stream()
//                .mapToDouble(file -> file.size)
//                .sum();
//			//FIXME ! Check displayed matches slskd
//			return StringManager.humanReadableByteCount(meanSize, false);
//		} else {
//			return "0";
//		}
//	}
	
	public double getSize() {
//		try {
			if(files!=null & !files.isEmpty()) {
				double meanSize = files.stream()
					.mapToDouble(file -> file.size)
					.sum();
				return meanSize;
			}
//		} catch (Exception ex) {
//			Logger.getLogger(Slsk.class.getName()).log(Level.SEVERE, null, ex);
//		}
		return 0;
	}

	public String getPath() {
		List<String> paths = files.stream()
                .map(file -> FilenameUtils.getFullPathNoEndSeparator(file.filename))
                .distinct()
                .collect(Collectors.toList());
		
		String path = !paths.isEmpty()?paths.get(0):"N/A";
			
		return path;
	}

	public int getSpeed() {
		return uploadSpeed;
	}
	
}
