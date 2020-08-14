package jamuz.acoustid;

import com.google.gson.Gson;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.client.ClientProtocolException;

/**
 * simple wrapper for AcoustID
 *
 * @author tom
 */
public class AcoustID {
	
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
    * get the highest rated result
    */
   private static Result getBestResult(Results results) {
      if (results.results.size() > 0) {
         Result bestResult = results.results.get(0);
         double currentScore = Double.parseDouble(bestResult.score);
         for (final Result result : results.results) {
            final double score = Double.parseDouble(result.score);
            if (score > currentScore) {
               bestResult = result;
               currentScore = score;
            }
         }
         return bestResult;
      } else {
         return null;
      }
   }

   /**
    * get the Results object from JSON
    */
   private static Results getResults(String json) {
		final Gson gson = new Gson();
		final Results results = gson.fromJson(json, Results.class);
		return results;
   }
   
   /**
    * do a ChromaPrint lookup and result a musicbrainz id
	 * @param chromaprint
	 * @param key
	 * @return 
	 * @throws org.apache.http.client.ClientProtocolException
    */
	public static String lookup(ChromaPrint chromaprint, String key) throws ClientProtocolException, IOException {
		OkHttpClient client = new OkHttpClient();		
		HttpUrl.Builder urlBuilder = HttpUrl.parse("http://api.acoustid.org/v2/lookup").newBuilder();
		urlBuilder.addQueryParameter("client", key);
		urlBuilder.addQueryParameter("meta", "recordingids");
		urlBuilder.addQueryParameter("duration", chromaprint.getDuration());
		urlBuilder.addQueryParameter("fingerprint", chromaprint.getChromaprint());
		String url = urlBuilder.build().toString();
		Request request = new Request.Builder().url(url).build();
		Response response = client.newCall(request).execute();

		final Results results = getResults(response.body().string());
		/*
		 * check status
		 */
		if (results.status.compareTo("ok") == 0) {
		   /*
			* get the best match
			*/
		   final Result bestResult = getBestResult(results);
		   if (null != bestResult) {
			  /*
			   * return the id
			   */
			  if (bestResult.recordings.size() > 0) {
				 return bestResult.recordings.get(0).id;
			  } else {
				 return null;
			  }
		   } else {
			  return null;
		   }
		} else {
		   return null;
		}
   }
}
