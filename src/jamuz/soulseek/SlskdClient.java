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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.simple.JSONObject;

/**
 *
 * @author raph
 */
public class SlskdClient {
	
	private static final String BASE_URL = "http://localhost:5030/api/v0"; // No trailing slash !!
	
	private String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoic2xza2QiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6IjU3OWJlYjQxLWYzYjQtNGRkYS1iYzZjLTQ5YmRkMWQ0MmJlYiIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvcm9sZSI6IkFkbWluaXN0cmF0b3IiLCJuYW1lIjoic2xza2QiLCJpYXQiOiIxNjk2NjE4OTUxIiwibmJmIjoxNjk2NjE4OTUxLCJleHAiOjE2OTcyMjM3NTEsImlzcyI6InNsc2tkIn0.s9RBrEoe43epTO1y3uU3kaVgdr3R1A6llA6FAZZ0KsU";
	private final OkHttpClient client = new OkHttpClient.Builder().build();
	private final Gson gson = new Gson();

	public SlskdClient() throws IOException, ServerException {
		getToken();
	}
	
	
	
	public void getToken() throws IOException, ServerException {
		HttpUrl.Builder urlBuilder = getUrlBuilder("session"); //NON-NLS
		
		JSONObject obj = new JSONObject();
		obj.put("username", "slskd");
		obj.put("password", "slskd");
		
		Request request = getRequestBuilder(urlBuilder) //NON-NLS
                    .post(RequestBody.create(obj.toString(), MediaType.parse("application/json; charset=utf-8"))).build(); //NON-NLS
		String body = getBodyString(request, client);
		
		
		SlskdTokenResponse fromJson = null;
		if (!body.equals("")) {
			fromJson = gson.fromJson(body, SlskdTokenResponse.class);
		}
		
		TOKEN = fromJson.token;
	}
	
	//TODO: Use this in a refreshToken
	public boolean checkToken() throws IOException, ServerException {
		getBodyString("server", client);
		return true; //TODO: return true or false based on http result
	}
	
	public SlskdSearchResult search(String queryText) throws IOException, ServerException {
		JSONObject obj = new JSONObject();
		obj.put("searchText", queryText);
//		JSONArray filesToMerge = new JSONArray();
//		for (Track track : tracks) {
//			track.getTags(true);
//			filesToMerge.put(track.toJSONObject());
//		}
//		obj.put("files", filesToMerge); //NON-NLS
		
//SearchRequest{
//description:	
//
//A search request.
//id	string($uuid)
//nullable: true
//
//Gets or sets the unique search identifier.
//fileLimit	integer($int32)
//nullable: true
//
//Gets or sets the maximum number of file results to accept before the search is considered complete. (Default = 10,000).
//filterResponses	boolean
//nullable: true
//
//Gets or sets a value indicating whether responses are to be filtered. (Default = true).
//maximumPeerQueueLength	integer($int32)
//nullable: true
//
//Gets or sets the maximum queue depth a peer may have in order for a response to be processed. (Default = 1000000).
//minimumPeerUploadSpeed	integer($int32)
//nullable: true
//
//Gets or sets the minimum upload speed a peer must have in order for a response to be processed. (Default = 0).
//minimumResponseFileCount	integer($int32)
//nullable: true
//
//Gets or sets the minimum number of files a response must contain in order to be processed. (Default = 1).
//responseLimit	integer($int32)
//nullable: true
//
//Gets or sets the maximum number of search results to accept before the search is considered complete. (Default = 100).
//searchText	string
//nullable: true
//
//Gets or sets the search text.
//searchTimeout	integer($int32)
//nullable: true
//
//Gets or sets the search timeout value, in seconds, used to determine when the search is complete. (Default = 15).
//token	integer($int32)
//nullable: true
//
//Gets or sets the search token.
//}


		HttpUrl.Builder urlBuilder = getUrlBuilder("searches"); //NON-NLS
		Request request = getRequestBuilder(urlBuilder) //NON-NLS
                    .post(RequestBody.create(obj.toString(), MediaType.parse("application/json; charset=utf-8"))).build(); //NON-NLS
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
	
	public List<SlskdSearchResponse> getSearchResponses(String id) throws IOException, ServerException, ServerException {
		String bodyString = getBodyString("searches/" + id + "/responses", client);
		
		List<SlskdSearchResponse> fromJson = null;
		if (!bodyString.equals("")) {
			fromJson = gson.fromJson(bodyString, new TypeToken<List<SlskdSearchResponse>>() {}.getType());
		}
		return fromJson;
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
	
	static class ServerException extends Exception {
        public ServerException(String errorMessage) {
            super(errorMessage);
        }
    }
}
