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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Wrapper for http://localhost:5030/swagger/index.html
 * @author phramusca <phramusca@gmail.com>
 */
public class SlskdClient {
	
	private static final String BASE_URL = "http://localhost:5030/api/v0"; // No trailing slash !!
	
	private String TOKEN = "";
	private final OkHttpClient client = new OkHttpClient.Builder().build();
	private final Gson gson = new Gson();

	public SlskdClient() throws IOException, ServerException {
		getToken();
	}

	public final void getToken() throws IOException, ServerException {
		HttpUrl.Builder urlBuilder = getUrlBuilder("session"); //NON-NLS
		
		HashMap<String, String> credentials = new HashMap<>();
		credentials.put("username", "slskd");
		credentials.put("password", "slskd");
		
		String json = gson.toJson(credentials);
		
		Request request = getRequestBuilder(urlBuilder) //NON-NLS
                    .post(RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"))).build(); //NON-NLS
		String body = getBodyString(request, client);

		SlskdTokenResponse fromJson = null;
		if (!body.equals("")) {
			fromJson = gson.fromJson(body, SlskdTokenResponse.class);
		}
		
		if (fromJson != null) {
			TOKEN = fromJson.token();
		} else {
			throw new ServerException("Failed to retrieve token from response");
		}
	}
	
	//TODO: Use this in a refreshToken
	public boolean checkToken() throws IOException, ServerException {
		getBodyString("server", client);
		return true; //TODO: return true or false based on http result
	}
	
	public boolean postDownloads(SlskdSearchResponse searchResponse) throws IOException, ServerException {
		List<HashMap<String, Object>> jsonArray = searchResponse.getFiles().stream()
			.filter(file -> file.percentComplete < 100)
			.map(file -> {
				HashMap<String, Object> map = new HashMap<>();
				map.put("filename", file.filename);
				map.put("size", file.size);
				return map;
			})
			.toList();

		String json = gson.toJson(jsonArray);

		OkHttpClient timeoutClient = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
		
		HttpUrl.Builder urlBuilder = getUrlBuilder("transfers/downloads/" + searchResponse.getUsername()); //NON-NLS
		Request request = getRequestBuilder(urlBuilder) //NON-NLS
				.post(RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"))).build(); //NON-NLS
        
        Response response = timeoutClient.newCall(request).execute();
		return response.isSuccessful();
	}
	
    public boolean putShareScan() throws IOException, ServerException {
		HttpUrl.Builder urlBuilder = getUrlBuilder("shares"); //NON-NLS
		Request request = getRequestBuilder(urlBuilder).put(RequestBody.create("", null)).build(); //NON-NLS
        
        Response response = client.newCall(request).execute();
		return response.isSuccessful();
	}
    
	public SlskdDownloadUser getDownloads(SlskdSearchResponse searchResponse) throws IOException, ServerException {
        String username = URLEncoder.encode(searchResponse.getUsername(), StandardCharsets.UTF_8.toString()); //FIXME Z Use this elsewhere ? Does it work ?
        String url = "transfers/downloads/" + username;
		String bodyString = getBodyString(url, client);
		
		SlskdDownloadUser fromJson = null;
		if (!bodyString.equals("")) {
			fromJson = gson.fromJson(bodyString, SlskdDownloadUser.class);
		}
		return fromJson;
	}
	
	public SlskdSearchResult search(String queryText) throws IOException, ServerException {
		HashMap<String, Object> searchParams = new HashMap<>();
		
		// searchParams"xxx-x--x-x-x-x");						//  string($uuid)	//the unique search identifier.
		// searchParams.put("fileLimit", 10000);				//  integer($int32)	//the maximum number of file results to accept before the search is considered complete. (Default = 10,000).
		// searchParams.put("filterResponses", true);			//  boolean			//a value indicating whether responses are to be filtered. (Default = true).
		// searchParams.put("maximumPeerQueueLength", 1000000);	//  integer($int32) //the maximum queue depth a peer may have in order for a response to be processed. (Default = 1000000).
		// searchParams.put("minimumPeerUploadSpeed", 0);		//	integer($int32) //the minimum upload speed a peer must have in order for a response to be processed. (Default = 0).
		// searchParams.put("minimumResponseFileCount", 1);		//	integer($int32)	//the minimum number of files a response must contain in order to be processed. (Default = 1).
		// searchParams.put("responseLimit", 5);				//	integer($int32)	//the maximum number of search results to accept before the search is considered complete. (Default = 100).
		searchParams.put("searchText", queryText);			//	string			//the search text.
		// searchParams.put("searchTimeout", 15);				//	integer($int32)	//the search timeout value, in seconds, used to determine when the search is complete. (Default = 15).
		// searchParams.put("token", 0);						//	integer($int32)	//the search token.

		String json = gson.toJson(searchParams);
				
		HttpUrl.Builder urlBuilder = getUrlBuilder("searches"); //NON-NLS
		Request request = getRequestBuilder(urlBuilder) //NON-NLS
                    .post(RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"))).build(); //NON-NLS
		String body = getBodyString(request, client);
		
		
		SlskdSearchResult fromJson = null;
		if (!body.equals("")) {
			fromJson = gson.fromJson(body, SlskdSearchResult.class);
		}
		return fromJson;
	}
	
	public SlskdSearchResult getSearch(String id) throws IOException, ServerException {
		String bodyString = getBodyString("searches/" + id, client);
		
		SlskdSearchResult fromJson = null;
		if (!bodyString.equals("")) {
			fromJson = gson.fromJson(bodyString, SlskdSearchResult.class);
		}
		return fromJson;
	}
	
	public List<SlskdSearchResponse> getSearchResponses(String id) throws IOException, ServerException {
		String bodyString = getBodyString("searches/" + id + "/responses", client);
		
		List<SlskdSearchResponse> fromJson = null;
		if (!bodyString.equals("")) {
			fromJson = gson.fromJson(bodyString, new TypeToken<List<SlskdSearchResponse>>() {}.getType());
		}
		return fromJson;
	}
	
    public boolean deleteSearch(String id) throws IOException {
        HttpUrl.Builder urlBuilder = getUrlBuilder("searches/" + id); //NON-NLS
		Request request = getRequestBuilder(urlBuilder).delete().build();
        Response response = client.newCall(request).execute();
		return response.isSuccessful();
    }
    
    public boolean deleteTransfer(String username, String id) throws IOException {
        HttpUrl.Builder urlBuilder = getUrlBuilder("transfers/downloads/" + username + "/" + id + "?remove=true"); //NON-NLS
		Request request = getRequestBuilder(urlBuilder).delete().build();
        Response response = client.newCall(request).execute();
		return response.isSuccessful();
    }
    
    public boolean deleteDirectory(String base64subDir) throws IOException {
        HttpUrl.Builder urlBuilder = getUrlBuilder("files/downloads/directories/" + base64subDir); //NON-NLS
		Request request = getRequestBuilder(urlBuilder).delete().build();
        Response response = client.newCall(request).execute();
		return response.isSuccessful();
    }
	
    public boolean deleteFile(String base64File) throws IOException {
        HttpUrl.Builder urlBuilder = getUrlBuilder("files/downloads/files/" + base64File); //NON-NLS
		Request request = getRequestBuilder(urlBuilder).delete().build();
        Response response = client.newCall(request).execute();
		return response.isSuccessful();
    }
    
    public boolean deleteIncompleteFile(String base64File) throws IOException {
        HttpUrl.Builder urlBuilder = getUrlBuilder("files/incomplete/files/" + base64File); //NON-NLS
		Request request = getRequestBuilder(urlBuilder).delete().build();
        Response response = client.newCall(request).execute();
		return response.isSuccessful();
    }
    
	private HttpUrl.Builder getUrlBuilder(String url) {
        return Objects.requireNonNull(HttpUrl.parse(BASE_URL + "/" + url)).newBuilder(); //NON-NLS
    }

    private Request.Builder getRequestBuilder(HttpUrl.Builder urlBuilder) {
        return new Request.Builder()
				.addHeader("Authorization", "Bearer " + TOKEN)
                .url(urlBuilder.build());
    }

    private String getBodyString(String url, OkHttpClient client) throws IOException, ServerException {
        HttpUrl.Builder urlBuilder = getUrlBuilder(url);
        return getBodyString(urlBuilder, client);
    }

    private String getBodyString(HttpUrl.Builder urlBuilder, OkHttpClient client) throws IOException, ServerException {
        return getBody(urlBuilder, client).string();
    }

    private ResponseBody getBody(HttpUrl.Builder urlBuilder, OkHttpClient client) throws IOException, ServerException {
        Request request = getRequestBuilder(urlBuilder).build();
        return getBody(request, client);
    }

    private String getBodyString(Request request, OkHttpClient client) throws IOException, ServerException {
        return getBody(request, client).string();
    }

    private ResponseBody getBody(Request request, OkHttpClient client) throws IOException, ServerException {
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
//            if (response.code() == 301) {
//                throw new ServerException(request.header("api-version") + " not supported. " + Objects.requireNonNull(response.body()).string()); //NON-NLS
//            }
            throw new ServerException(response.code() + ": " + response.message());
        }
        return response.body();
    }

	public static class ServerException extends Exception {
        public ServerException(String errorMessage) {
            super(errorMessage);
        }
    }
}
