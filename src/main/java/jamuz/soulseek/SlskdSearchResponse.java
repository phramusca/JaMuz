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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class SlskdSearchResponse {
	private int fileCount;
	private List<SlskdSearchFile> files;
	private boolean hasFreeUploadSlot;
	private int lockedFileCount;
	private List<SlskdSearchFile> lockedFiles;
	private int queueLength;
	private int token;
	private double uploadSpeed;
	private String username;	
	private String date = DateTime.getCurrentLocal(DateTime.DateTimeFormat.HUMAN);
    private boolean completed;
    private String searchText;
    private boolean queued;
    
    private final transient ProgressBar progressBar = new ProgressBar();
    private transient TableModelSlskdDownload tableModelDownload;

    public SlskdSearchResponse() {
        this.progressBar.setMsgMax(500);
		this.progressBar.setupAsPercentage();
    }
    
    boolean isQueued() {
        return queued;
    }

    void setQueued() {
        queued = true;
    }
   
    String getSearchText() {
        return searchText;
    }
    
    void setSearchText(String query) {
        this.searchText = query;
    }

    public void filterAndSortFiles(List<String> allowedExtensions) {
        files = files.stream()
            .filter(file -> allowedExtensions.contains(FilenameUtils.getExtension(file.filename)))
            .sorted(Comparator.comparing(SlskdSearchFile::getFilename))
            .collect(Collectors.toList());
}
   
	public List<SlskdSearchFile> getFiles() {
		return files;
	}
    
    public SlskdSearchResponse cloneWithoutFiles() {
        SlskdSearchResponse clone = new SlskdSearchResponse();
         // Clear files
        clone.fileCount = 0;
        clone.files = new ArrayList<>();
        clone.lockedFileCount = 0;
        clone.lockedFiles = new ArrayList<>();

        clone.hasFreeUploadSlot = this.hasFreeUploadSlot;
        clone.queueLength = this.queueLength;
        clone.token = this.token;
        clone.uploadSpeed = this.uploadSpeed;
        clone.username = this.username;
        clone.date = this.date;
        return clone;
    }

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

	public double getSize() {
		if(files!=null & !files.isEmpty()) {
			double meanSize = files.stream()
				.mapToDouble(file -> file.size)
				.sum();
			return meanSize;
			//TODO !!!!!! and think of row sorting before changing to return StringManager.humanReadableByteCount(meanSize, false);
		}
		return 0;
	}

	public String getPath() {
		// Since files have been grouped by path, if any, there is one and only one
		return files.get(0).getPath();
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

    void setCompleted() {
        this.completed=true;
    }    

    public boolean isCompleted() {
        return completed;
    }

    public TableModelSlskdDownload getTableModel() {
        if (tableModelDownload == null) {
            tableModelDownload = new TableModelSlskdDownload(this);
            for (SlskdSearchFile file : files) {
                tableModelDownload.addRow(file);
            }
        }
        return tableModelDownload;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    void update(String msg, int index) {
        this.progressBar.progress(msg, index);
    }

    public int getQueueLength() {
        return queueLength;
    }

    public boolean hasFreeUploadSlot() {
        return hasFreeUploadSlot;
    }

    public String getUsername() {
        return username;
    }

    public void setFiles(List<SlskdSearchFile> files) {
        this.files = files;
    }

    public int setFileCount(int size) {
        return fileCount;
    }

    
}
