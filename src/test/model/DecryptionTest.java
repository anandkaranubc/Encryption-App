package model;

import model.Decryption;
import model.Encryption;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import java.security.KeyPair;

import static org.junit.jupiter.api.Assertions.*;

public class DecryptionTest {

    private static KeyPair pair;
    public static Cipher cipher;
    byte[] cipherText, cipherText2;
    Decryption decryption;
    Encryption encryption;

    @BeforeEach
    public void runBefore() throws Exception {
        pair = Encryption.getPair();
        cipher = Decryption.cipher;
        decryption = new Decryption();
        encryption = Encryption.getInstance();
    }

    @Test
    public void testPassDecryption() throws Exception {
        String a = "password";
        String b = "pa";
        String dataName = "chrome";
        cipherText = encryption.passEncryption(a, dataName);
        cipherText2 = encryption.passEncryption(b,"chrome2");

        assertEquals(a, Decryption.passDecryption(cipherText, Encryption.getPair()));
        assertEquals(b, Decryption.passDecryption(cipherText2, Encryption.getPair()));
    }

    @AfterEach
    public void resetEncryptionList() {
        EncryptionList.getDataNames().clear();
        EncryptionList.getEncryptedCiphers().clear();
    }
}
