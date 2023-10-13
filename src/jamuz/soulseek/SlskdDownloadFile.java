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
public class SlskdDownloadFile {
	public String filename="null";
	public int size;
	
	public String id="null";
	public String username="null";
	public String direction="null";
	public int startOffset;
	public String state="null";
	public String requestedAt="null";
	public String enqueuedAt="null";
	public String startedAt="null";
	public String endedAt="null";
	public int bytesTransferred;
	public double averageSpeed;
	public int bytesRemaining;
	public String elapsedTime="null";
	public double percentComplete;
	public String remainingTime="null";
	
	public String getKey() {
		return "[" + size + "]" + filename;
	}
}
