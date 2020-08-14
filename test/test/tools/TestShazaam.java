/*
 * Copyright (C) 2020 phramusca ( https://github.com/phramusca/ )
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
package test.tools;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import jamuz.Keys;
import jamuz.utils.Popup;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class TestShazaam {
	
	public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024]; // Adjust if you want
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1)
        {
            output.write(buffer, 0, bytesRead);
        }
    }
	
	/**
	 * Main program.
	 * @param args
	 */
	public static void main(String[] args) {
		try {

			Keys keys = new Keys("/jamuz/keys.properties");
			if(!keys.read()) {
				Popup.error("Missing keys.properties file from jar package.");
				return;
			}
			
			//The raw sound data must be 44100Hz, 1 channel (Mono), signed 16 bit PCM little endian.
			File source;
			File target;
			String basePath="/home/raph/Bureau/TEST_SHAZAM/";
			String filename="01 Bad.mp3";
			source = new File(FilenameUtils.concat(basePath, filename));
			target = new File(FilenameUtils.concat(basePath, FilenameUtils.getBaseName(filename)+".raw"));  //NOI18N
			AudioAttributes audio = new AudioAttributes();
//			audio.setBitRate(192000); //What is required value for shazam ?
			audio.setChannels(1);
			audio.setSamplingRate(44100);
			EncodingAttributes attrs = new EncodingAttributes();
			audio.setCodec("pcm_s16le");  //NOI18N
			attrs.setFormat("s16le");  //NOI18N
			attrs.setOffset(30f); //TODO: half duration
			attrs.setDuration(3f); //3-5 seconds => 500KB max
			attrs.setAudioAttributes(audio);
			Encoder encoder = new Encoder();
			encoder.encode(source, target, attrs);
			
			//ffmpeg -i input.mp3 -f s16le -acodec pcm_s16le -t 3 -ac 1 -ar 44100 -ss 30 output.raw
//			target = new File("/home/raph/Bureau/TEST_SHAZAM/output.raw");
			
			//	Encrypted base64 string of byte[] that generated
			//	from raw data less than 500KB (3-5 seconds sample are good enough for detection).
			//	The raw sound data must be 44100Hz, 1 channel (Mono), signed 16 bit PCM little endian.
			//	Other types of media are NOT supported, such as : mp3, wav, etc…
			//	or need to be converted to uncompressed raw data.
			//	If the result is empty, your request data must be in wrong format in most case.

			//Convert wav file to byte[]
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(target));
			copyStream(in, out);
			byte[] audioBytes = out.toByteArray();
			
			//Encode to base64
			String encoded = Base64.getEncoder().encodeToString(audioBytes);
			System.out.println("Base64: "+encoded);
			
			//Call shazam.p.rapidapi.com
			OkHttpClient client = new OkHttpClient();
			MediaType mediaType = MediaType.parse("text/plain");
//			RequestBody body = RequestBody.create(mediaType, "\""+encoded+"\"");
			RequestBody body = RequestBody.create(mediaType, encoded);
			Request request = new Request.Builder()
				.url("https://shazam.p.rapidapi.com/songs/detect")
				.post(body)
				.addHeader("x-rapidapi-host", "shazam.p.rapidapi.com")
				.addHeader("x-rapidapi-key", keys.get("RapidAPI"))
				.addHeader("content-type", "text/plain")
				.addHeader("accept", "text/plain")
				.build();
			Response response = client.newCall(request).execute();
			if(response.isSuccessful()) {
				System.out.println("Result: "+response.body().string());
			} else {
				System.out.println("FAILED: "+response.message());
			}
			
		} catch (IllegalArgumentException | IOException | EncoderException ex) {
			Logger.getLogger(TestShazaam.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}