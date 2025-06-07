/*
 * Copyright (C) 2016 phramusca <phramusca@gmail.com>
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
package jamuz.utils;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class Encryption {
	
	private static final String ALGO = "AES";
	
//	/**
//	 *
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		String clear = "MaChaineAvegrer";
//		System.out.println("clear: "+clear);
//		String encrypted = encrypt(clear, "NOTeBrrhzrtestSecretK");
//		System.out.println("encrypted: "+encrypted);
//		String decrypted = decrypt(encrypted, "NOTeBrrhzrtestSecretK");
//		System.out.println("decrypted: "+decrypted);
//		String decryptedWrong = decrypt(encrypted, "fgv");
//		System.out.println("decryptedWrong: "+decryptedWrong);
//	}
	
	/**
	 *
	 * @param Data
	 * @param secret
	 * @return
	 */
	public static String encrypt(String Data, String secret) {
		try {
			Key key = generateKey(secret);
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.ENCRYPT_MODE, key);
			byte[] encVal = c.doFinal(Data.getBytes());
			Base64 encoder = new Base64();
			String encryptedValue = encoder.encodeToString(encVal);
			return encryptedValue;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | 
				InvalidKeyException | IllegalBlockSizeException | 
				BadPaddingException ex) {
			return "";
		}
    }

	/**
	 *
	 * @param encryptedData
	 * @param secret
	 * @return
	 */
	public static String decrypt(String encryptedData, String secret) {
		try {
			Key key = generateKey(secret);
			Cipher c = Cipher.getInstance(ALGO);
			c.init(Cipher.DECRYPT_MODE, key);
			Base64 decoder = new Base64();
			byte[] decordedValue = decoder.decode(encryptedData);
			byte[] decValue = c.doFinal(decordedValue);
			String decryptedValue = new String(decValue);
			return decryptedValue;
		} catch (InvalidKeyException | NoSuchAlgorithmException | 
				NoSuchPaddingException | IllegalBlockSizeException | 
				BadPaddingException ex) {
			return "";
		}
    }
    private static Key generateKey(String secret) 
			throws NoSuchAlgorithmException {
		
		byte[] key = secret.getBytes(StandardCharsets.UTF_8);
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		key = sha.digest(key);
		key = Arrays.copyOf(key, 16); // use only first 128 bit
		return new SecretKeySpec(key, ALGO);
	}
}
