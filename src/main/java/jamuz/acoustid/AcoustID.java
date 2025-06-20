/*
 * Copyright (C) 2020 phramusca <phramusca@gmail.com>
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
package jamuz.acoustid;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * simple wrapper for AcoustID
 *
 * @author tom (https://github.com/teverett/musicbrainztagger/tree/master/src/main/java/com/khubla/musicbrainztagger/acoustid)
 */
public class AcoustID {
	
	//FIXME WINDOWS Support fpcalc on windows (as for mp3gain). Get exe here: https://acoustid.org/chromaprint

	/**
	 *
	 * @param filename
	 * @param key
	 * @return
	 */
	public static Results analyze(String filename, String key) {
		try {	
			ChromaPrint chromaprint = chromaprint(new File(filename), "fpcalc");		
			return lookup(chromaprint, key);
		} catch (IOException ex) {
			Logger.getLogger(AcoustID.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
	
	/**
    * Chromaprint the file passed in
	 * @param file
	 * @param fpcalc
	 * @return 
	 * @throws java.io.IOException
    */
	public static ChromaPrint chromaprint(File file, String fpcalc) throws IOException {
		final ProcessBuilder processBuilder = new ProcessBuilder(fpcalc, null);
		processBuilder.redirectErrorStream(true);
		processBuilder.command().set(1, file.getAbsolutePath());
		final Process fpcalcProc = processBuilder.start();
		final BufferedReader br = new BufferedReader(new InputStreamReader(fpcalcProc.getInputStream()));
		String line;
		String chromaprint = null;
		String duration = null;
		while ((line = br.readLine()) != null) {
			if (line.startsWith("FINGERPRINT=")) {
				chromaprint = line.substring("FINGERPRINT=".length());
			} else if (line.startsWith("DURATION=")) {
				duration = line.substring("DURATION=".length());
			}
		}
		return new ChromaPrint(chromaprint, duration);
	}

	/**
	 * do a ChromaPrint lookup
	 * @param chromaprint
	 * @param key
	 * @return 
	 * @throws java.io.IOException
    */
	public static Results lookup(ChromaPrint chromaprint, String key) throws IOException {
		OkHttpClient client = new OkHttpClient();		
		HttpUrl.Builder urlBuilder = HttpUrl.parse("http://api.acoustid.org/v2/lookup").newBuilder();
		urlBuilder.addQueryParameter("client", key);
		urlBuilder.addQueryParameter("meta", "recordingids");
		urlBuilder.addQueryParameter("duration", chromaprint.getDuration());
		urlBuilder.addQueryParameter("fingerprint", chromaprint.getChromaprint());
		String url = urlBuilder.build().toString();
		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();
		final Gson gson = new Gson();
		Results fromJson = gson.fromJson(response.body().string(), Results.class);
		fromJson.chromaprint=chromaprint;
		return fromJson;
	}
}
