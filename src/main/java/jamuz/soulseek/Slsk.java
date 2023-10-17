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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Slsk {
	
	private final SlskdClient slskdClient;
//FIXME !!! Manage exceptions and method returns		
    /**
	 * Wrapper for Soulseek CLI (https://github.com/aeyoll/soulseek-cli)
     *
	 * @throws java.io.IOException
	 * @throws jamuz.soulseek.SlskdClient.ServerException
     */  
    public Slsk() throws IOException, SlskdClient.ServerException {
		slskdClient = new SlskdClient();
    }

	public List<SlskdSearchResponse> search(String query, String destination) {
		try {
            SlskdSearchResult search = slskdClient.search(query);
            while(!search.isComplete) {
                search = slskdClient.getSearch(search.id);
                Thread.sleep(1000);
            }

            List<SlskdSearchResponse> searchResponses = slskdClient.getSearchResponses(search.id);

            // Sort, filter (authorized extensions), and group by path
            Map<String, List<SlskdSearchFile>> pathToFileMap = new HashMap<>();
            Map<String, SlskdSearchResponse> pathToResponseMap = new HashMap<>();

            for (SlskdSearchResponse searchResponse : searchResponses) {
                searchResponse.filterAndSortFiles();
                if (!searchResponse.getFiles().isEmpty()) {
                    for (SlskdSearchFile file : searchResponse.getFiles()) {
                        String path = file.getPath();
                        if (pathToFileMap.containsKey(path)) {
                            pathToFileMap.get(path).add(file);
                        } else {
                            List<SlskdSearchFile> fileList = new ArrayList<>();
                            fileList.add(file);
                            pathToFileMap.put(path, fileList);
                            pathToResponseMap.put(path, searchResponse.cloneWithoutFiles());
                        }
                    }
                }
            }

            List<SlskdSearchResponse> groupedResponses = new ArrayList<>();
            for (Map.Entry<String, List<SlskdSearchFile>> entry : pathToFileMap.entrySet()) {
                SlskdSearchResponse response = pathToResponseMap.get(entry.getKey());
                response.fileCount = entry.getValue().size();
                response.files = entry.getValue();
                groupedResponses.add(response);
            }

            //FIXME !!! Remove search from server
            return groupedResponses;	
		} catch (IOException ex) {
			Logger.getLogger(Slsk.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SlskdClient.ServerException ex) {
			Logger.getLogger(Slsk.class.getName()).log(Level.SEVERE, null, ex);
		} catch (InterruptedException ex) {
			Logger.getLogger(Slsk.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	
	SlskdDownloadUser getDownloads(SlskdSearchResponse searchResponse) {
		try {
			return slskdClient.getDownloads(searchResponse);
		} catch (IOException ex) {
			Logger.getLogger(Slsk.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SlskdClient.ServerException ex) {
			Logger.getLogger(Slsk.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	
	boolean download(SlskdSearchResponse searchResponse) {
		try {
			return slskdClient.download(searchResponse);
		} catch (IOException ex) {
			Logger.getLogger(Slsk.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SlskdClient.ServerException ex) {
			Logger.getLogger(Slsk.class.getName()).log(Level.SEVERE, null, ex);
		}
		return false;
	}
}