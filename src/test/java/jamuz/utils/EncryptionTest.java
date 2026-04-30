package jamuz.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EncryptionTest {

    @Test
    void encryptThenDecrypt_returnsOriginal() {
        String data = "Oh the beautifull message I want to encrypt";
        String secret = "Shush, don't tell my secret to anyone";
        String encrypted = Encryption.encrypt(data, secret);
        assertNotEquals(data, encrypted, "Encrypted text should differ from original");
        assertEquals(data, Encryption.decrypt(encrypted, secret));
    }

    @Test
    void encryptWithDifferentSecrets_produceDifferentResults() {
        String data = "same plaintext";
        String enc1 = Encryption.encrypt(data, "secret1");
        String enc2 = Encryption.encrypt(data, "secret2");
        assertNotEquals(enc1, enc2);
    }
}
