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

import java.io.IOException;
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
public class SlskClient {
	
	private static final String BASE_URL = "http://localhost:5030/api/v0"; // No trailing slash !!
	
	private String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoic2xza2QiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1laWRlbnRpZmllciI6IjU3OWJlYjQxLWYzYjQtNGRkYS1iYzZjLTQ5YmRkMWQ0MmJlYiIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvcm9sZSI6IkFkbWluaXN0cmF0b3IiLCJuYW1lIjoic2xza2QiLCJpYXQiOiIxNjk2NjE4OTUxIiwibmJmIjoxNjk2NjE4OTUxLCJleHAiOjE2OTcyMjM3NTEsImlzcyI6InNsc2tkIn0.s9RBrEoe43epTO1y3uU3kaVgdr3R1A6llA6FAZZ0KsU";
	
	public String search(String queryText) throws IOException, ServerException {
		OkHttpClient client = new OkHttpClient.Builder()
//                    .readTimeout(Math.min(Math.max(tracks.size(), 30), 600), TimeUnit.SECONDS) //Between 30s and 10min
                    .build();
		
		JSONObject obj = new JSONObject();
		obj.put("searchText", queryText);
//		JSONArray filesToMerge = new JSONArray();
//		for (Track track : tracks) {
//			track.getTags(true);
//			filesToMerge.put(track.toJSONObject());
//		}
//		obj.put("files", filesToMerge); //NON-NLS
		
		HttpUrl.Builder urlBuilder = getUrlBuilder("searches"); //NON-NLS
		Request request = getRequestBuilder(urlBuilder) //NON-NLS
                    .post(RequestBody.create(obj.toString(), MediaType.parse("application/json; charset=utf-8"))).build(); //NON-NLS
		String body = getBodyString(request, client);
		
		return body;
	}
	
	
	public HttpUrl.Builder getUrlBuilder(String url) {
        return Objects.requireNonNull(HttpUrl.parse(BASE_URL + "/" + url)).newBuilder(); //NON-NLS
    }

    public Request.Builder getRequestBuilder(HttpUrl.Builder urlBuilder) {
        return new Request.Builder()
				.addHeader("Authorization", "Bearer " + TOKEN)
                .url(urlBuilder.build());
    }

    public String getBodyString(String url, OkHttpClient client) throws IOException, ServerException {
        HttpUrl.Builder urlBuilder = getUrlBuilder(url);
        return getBodyString(urlBuilder, client);
    }

    public String getBodyString(HttpUrl.Builder urlBuilder, OkHttpClient client) throws IOException, ServerException {
        return getBody(urlBuilder, client).string();
    }

    public ResponseBody getBody(HttpUrl.Builder urlBuilder, OkHttpClient client) throws IOException, ServerException {
        Request request = getRequestBuilder(urlBuilder).build();
        return getBody(request, client);
    }

    public String getBodyString(Request request, OkHttpClient client) throws IOException, ServerException {
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
