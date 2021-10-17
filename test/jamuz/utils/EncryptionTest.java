/*
 * Copyright (C) 2019 phramusca ( https://github.com/phramusca/ )
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

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class EncryptionTest {
	
	/**
	 *
	 */
	public EncryptionTest() {
	}
	
	/**
	 *
	 */
	@BeforeClass
	public static void setUpClass() {
	}
	
	/**
	 *
	 */
	@AfterClass
	public static void tearDownClass() {
	}
	
	/**
	 *
	 */
	@Before
	public void setUp() {
	}
	
	/**
	 *
	 */
	@After
	public void tearDown() {
	}

	/**
	 * Test of encrypt method, of class Encryption.
	 */
	@Test
	public void testEncryptDecrypt() {
		System.out.println("encrypt & decrypt");
		String Data = "Oh the beautifull message I want to encrypt";
		String secret = "Shush, don't tell my secret to anyone";
		String encryptedData = Encryption.encrypt(Data, secret);
		String result = Encryption.decrypt(encryptedData, secret);
		assertEquals(Data, result);
	}
	
	/**
	 * Test of encrypt method, of class Encryption.
	 */
	@Test
	public void testEncrypt() {
		System.out.println("encrypt");
		//Refer to testEncryptDecrypt() above
	}

	/**
	 * Test of decrypt method, of class Encryption.
	 */
	@Test
	public void testDecrypt() {
		System.out.println("decrypt");
		//Refer to testEncryptDecrypt() above
	}
	
}
