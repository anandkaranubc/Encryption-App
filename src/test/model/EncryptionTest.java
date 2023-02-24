package model;

import model.Encryption;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.util.ArrayList;

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
        encryption = new Encryption();
    }

    @Test
    public void testPassEncryption() throws Exception {
        cipher = Encryption.cipher;
        String pass = "password";
        String dataName = "Chrome";

        byte[] encrypted = encryption.passEncryption(pass, dataName);
        assertNotNull(encrypted);

        // check that the decryption of the encrypted password matches the original password
        Encryption.cipher.init(Cipher.DECRYPT_MODE, Encryption.getPair().getPrivate());
        byte[] decrypted = Encryption.cipher.doFinal(encrypted);
        assertEquals(pass, new String(decrypted));

    }

    @AfterEach
    public void resetEncryptionList() {
        EncryptionList.getDataNames().clear();
        EncryptionList.getEncryptedCiphers().clear();
    }
}
