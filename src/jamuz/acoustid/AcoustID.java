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
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * simple wrapper for AcoustID
 *
 * @author tom
 */
public class AcoustID {
	
	public static AcoustIdResult analyze(String filename, String key) {
		try {	
			ChromaPrint chromaprint = chromaprint(new File(filename), "fpcalc");		
			Result result = lookup(chromaprint, key);
			if(result!=null) {
				AcoustIdResult acoustIdResult = result.getMeta();
				acoustIdResult.setFilename(filename);
				return acoustIdResult;
			}
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
    * do a ChromaPrint lookup and result a musicbrainz id
	 * @param chromaprint
	 * @param key
	 * @return 
	 * @throws java.io.IOException
    */
	public static Result lookup(ChromaPrint chromaprint, String key) throws IOException {
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
		final Results results = gson.fromJson(response.body().string(), Results.class);
		if (results.status.compareTo("ok") == 0) {
		   return results.results.stream().max(Comparator.comparing(Result::getScore)).get();
		}
		return null;
	}
}
