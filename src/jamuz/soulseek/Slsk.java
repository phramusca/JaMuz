/*
 * Copyright (C) 2012 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Slsk {
	
	private Process process;
	private String destination;
		
    /**
	 * Wrapper for Soulseek CLI (https://github.com/aeyoll/soulseek-cli)
     *
     */  
    public Slsk() {
    }

	public List<SlskdSearchResponse> search(String query, String destination) {
		this.destination = destination;
		return process(query);
	}
	
	boolean sendSelection(SlskdSearchResponse result, String query) {
//		try {
//			folderBeingDownloaded = new SlskDownload(query, result.getNbOfFiles(), result.getPath(), result.getUser(), destination);
//			StdIn.write((result.getKey()+"\n").getBytes());
//			downloadStarted = true;
//			benchmark = new Benchmark(result.getNbOfFiles());
//			StdIn.flush();
			return true;
//		} catch (IOException ex) {
//			Logger.getLogger(Slsk.class.getName()).log(Level.SEVERE, null, ex);
//			callback.error(ex.getMessage());
//			return false;
//		}
	}

	boolean cancelling = false;
	
	void cancel() {
		cancelling = true;
		if(process!=null) {
			process.destroyForcibly();
		}
	}
	
	/**
	 * @return  
	 */
	private List<SlskdSearchResponse> process(String query) {
		try {
				SlskdClient slskdClient = new SlskdClient();
				SlskdSearchResult search = slskdClient.search(query);
				
				while(!search.isComplete) {
					search = slskdClient.getSearch(search.id);
					Thread.sleep(1000);
				}
				
				List<SlskdSearchResponse> searchResponses = slskdClient.getSearchResponses(search.id);
				return searchResponses;	
		} catch (IOException ex) {
			Logger.getLogger(Slsk.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SlskdClient.ServerException ex) {
			Logger.getLogger(Slsk.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InterruptedException ex) {
			Logger.getLogger(Slsk.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
}