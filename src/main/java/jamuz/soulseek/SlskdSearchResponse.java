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
	private String date;
	private boolean completed;
	private String searchText;
	private boolean queued;
	private final ProgressBar progressBar;
	private TableModelSlskdDownload tableModelDownload;

	public SlskdSearchResponse() {
		this.fileCount = 0;
		this.files = new ArrayList<>();
		this.hasFreeUploadSlot = false;
		this.lockedFileCount = 0;
		this.lockedFiles = new ArrayList<>();
		this.queueLength = 0;
		this.token = 0;
		this.uploadSpeed = 0.0;
		this.username = "";
		this.date = DateTime.getCurrentLocal(DateTime.DateTimeFormat.HUMAN);
		this.completed = false;
		this.searchText = "";
		this.queued = false;
		this.progressBar = new ProgressBar();
		this.progressBar.setMsgMax(500);
		this.progressBar.setupAsPercentage();
		this.tableModelDownload = null;
	}

	// Getters and Setters
	public int getFileCount() {
		return fileCount;
	}

	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}

	public List<SlskdSearchFile> getFiles() {
		return files;
	}

	public void setFiles(List<SlskdSearchFile> files) {
		this.files = files;
	}

	public boolean hasFreeUploadSlot() {
		return hasFreeUploadSlot;
	}

	public void setHasFreeUploadSlot(boolean hasFreeUploadSlot) {
		this.hasFreeUploadSlot = hasFreeUploadSlot;
	}

	public int getLockedFileCount() {
		return lockedFileCount;
	}

	public void setLockedFileCount(int lockedFileCount) {
		this.lockedFileCount = lockedFileCount;
	}

	public List<SlskdSearchFile> getLockedFiles() {
		return lockedFiles;
	}

	public void setLockedFiles(List<SlskdSearchFile> lockedFiles) {
		this.lockedFiles = lockedFiles;
	}

	public int getQueueLength() {
		return queueLength;
	}

	public void setQueueLength(int queueLength) {
		this.queueLength = queueLength;
	}

	public int getToken() {
		return token;
	}

	public void setToken(int token) {
		this.token = token;
	}

	public double getUploadSpeed() {
		return uploadSpeed;
	}

	public void setUploadSpeed(double uploadSpeed) {
		this.uploadSpeed = uploadSpeed;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted() {
		this.completed = true;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public boolean isQueued() {
		return queued;
	}

	public void setQueued() {
		this.queued = true;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public TableModelSlskdDownload getTableModelDownload() {
		return tableModelDownload;
	}

	public void setTableModelDownload(TableModelSlskdDownload tableModelDownload) {
		this.tableModelDownload = tableModelDownload;
	}

	public void filterAndSortFiles(List<String> allowedExtensions) {
		files = files.stream()
			.filter(file -> allowedExtensions.contains(FilenameUtils.getExtension(file.filename)))
			.sorted(Comparator.comparing(SlskdSearchFile::getFilename))
			.collect(Collectors.toList());
	}

	public SlskdSearchResponse cloneWithoutFiles() {
		SlskdSearchResponse clone = new SlskdSearchResponse();
		clone.setHasFreeUploadSlot(this.hasFreeUploadSlot);
		clone.setQueueLength(this.queueLength);
		clone.setToken(this.token);
		clone.setUploadSpeed(this.uploadSpeed);
		clone.setUsername(this.username);
		clone.setDate(this.date);
		return clone;
	}

	public double getBitrate() {
		if (files != null && !files.isEmpty()) {
			return Math.round(files.stream()
				.mapToDouble(file -> file.bitRate)
				.average()
				.orElse(0.0));
		} else {
			return 0.0;
		}
	}

	public double getSize() {
		if (files != null && !files.isEmpty()) {
			double meanSize = files.stream()
				.mapToDouble(file -> file.size)
				.sum();
			return meanSize;
		}
		return 0;
	}

	public String getPath() {
		return files.get(0).getPath();
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

	public void update(String msg, int index) {
		this.progressBar.progress(msg, index);
	}
}
