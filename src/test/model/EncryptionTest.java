package model;

import model.Encryption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EncryptionTest {

    private static KeyPair pair;
    public static Cipher cipher;
    byte[] cipherText;
    Encryption encryption;

    @BeforeEach
    public void runBefore() throws Exception {
        pair = Encryption.getPair();
        cipher = Encryption.cipher;
        encryption = new Encryption();
    }

    @Test
    public void testInvalidAlgorithm() {
        Assertions.assertThrows(Exception.class, () -> {
            Cipher cipher = Cipher.getInstance("INVALID_ALGORITHM");
        });
    }

    @Test
    public void testPassEncryption() throws Exception {
        String pass = "password";
        String dataName = "Chrome";

        byte[] encrypted = encryption.passEncryption(pass, dataName);
        assertNotNull(encrypted);

        // check that the decryption of the encrypted password matches the original password
        Encryption.cipher.init(Cipher.DECRYPT_MODE, Encryption.getPair().getPrivate());
        byte[] decrypted = Encryption.cipher.doFinal(encrypted);
        assertEquals(pass, new String(decrypted));
    }
}
