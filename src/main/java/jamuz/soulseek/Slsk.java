/*
 * Copyright (C) 2012 phramusca <phramusca@gmail.com>
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

import jamuz.Jamuz;
import jamuz.utils.Popup;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author phramusca <phramusca@gmail.com>
 */
public class Slsk {

    private final SlskdClient slskdClient;
    private final List<String> allowedExtensions;
    private final Base64 encoder = new Base64();

    /**
     * Wrapper for Soulseek CLI (https://github.com/aeyoll/soulseek-cli)
     *
     * @throws java.io.IOException
     * @throws jamuz.soulseek.SlskdClient.ServerException
     */
    public Slsk() throws IOException, SlskdClient.ServerException {
        slskdClient = new SlskdClient();

        allowedExtensions = new ArrayList(
                Arrays.asList(Jamuz.getMachine().getOptionValue("files.audio").split(","))); //NOI18N;
    }

    public List<SlskdSearchResponse> search(String query, ICallBackSearch callBackSearch) {
        try {
            SlskdSearchResult search = slskdClient.search(query);
            while (!search.isComplete) {
                search = slskdClient.getSearch(search.id);
                callBackSearch.searching(search);
                Thread.sleep(1000);
            }

            List<SlskdSearchResponse> searchResponses = slskdClient.getSearchResponses(search.id);

            // Sort, filter (authorized extensions), and group by path
            Map<String, List<SlskdSearchFile>> pathToFileMap = new HashMap<>();
            Map<String, SlskdSearchResponse> pathToResponseMap = new HashMap<>();
            for (SlskdSearchResponse searchResponse : searchResponses) {
                searchResponse.filterAndSortFiles(allowedExtensions);
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
                response.setSearchText(query);
                groupedResponses.add(response);
            }
            slskdClient.deleteSearch(search.id);

            return groupedResponses;
        } catch (IOException | SlskdClient.ServerException ex) {
            Popup.error("search " + query, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Slsk.class.getName()).log(Level.WARNING, null, ex);
        }
        return null;
    }

    SlskdDownloadUser getDownloads(SlskdSearchResponse searchResponse) {
        try {
            return slskdClient.getDownloads(searchResponse);
        } catch (IOException | SlskdClient.ServerException ex) {
            Logger.getLogger(Slsk.class.getName()).log(Level.WARNING, null, ex);
        }
        return null;
    }

    boolean download(SlskdSearchResponse searchResponse) {
        try {
            return slskdClient.postDownloads(searchResponse);
        } catch (IOException | SlskdClient.ServerException ex) {
            Popup.error("download " + searchResponse.getSearchText(), ex);
        }
        return false;
    }

    boolean deleteDirectory(String base64subDir) {
        try {
            return slskdClient.deleteDirectory(base64subDir);
        } catch (IOException ex) {
            Popup.error("deleteDirectory " + base64subDir, ex);
        }
        return false;
    }

    boolean deleteTransfer(SlskdDownloadFile downloadFile) {
        try {
            return slskdClient.deleteTransfer(downloadFile.username, downloadFile.id);
        } catch (IOException ex) {
            Popup.error("deleteTransfer " + downloadFile.filename, ex);
        }
        return false;
    }

    boolean deleteTransfer(String username, SlskdSearchFile downloadFile) {
        try {
            return slskdClient.deleteTransfer(username, downloadFile.id);
        } catch (IOException ex) {
            Popup.error("deleteTransfer " + username + ", " + downloadFile.filename, ex);
        }
        return false;
    }

    boolean deleteFile(SlskdDownloadFile downloadFile) {
        try {
            return deleteFilename(downloadFile.filename, false);
        } catch (IOException ex) {
            Popup.error("deleteFile " + downloadFile.filename, ex);
        }
        return false;
    }

    boolean deleteFile(SlskdSearchFile searchFile) {
        try {
            return deleteFilename(searchFile.filename, searchFile.percentComplete < 100);
        } catch (IOException ex) {
            Popup.error("deleteFile " + searchFile.filename, ex);
        }
        return false;
    }

    private boolean deleteFilename(String filename, boolean incomplete) throws IOException {
        Pair<String, String> directory = getDirectory(filename);
        String subDirectoryName = directory.getLeft();
        String fileOnlyName = directory.getRight();
        String base64File = encoder.encodeToString(FilenameUtils.concat(subDirectoryName, fileOnlyName).getBytes());

        if (incomplete) {
            return slskdClient.deleteIncompleteFile(base64File);
        } else {
            return slskdClient.deleteFile(base64File);
        }
    }

    public Pair<String, String> getDirectory(String filename) {
        String[] split = filename.split("\\\\");
        return Pair.of(split[split.length - 2], split[split.length - 1]);
    }

    boolean rescanShares() {
        try {
            return slskdClient.putShareScan();
        } catch (IOException ex) {
            Logger.getLogger(Slsk.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SlskdClient.ServerException ex) {
            Logger.getLogger(Slsk.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
