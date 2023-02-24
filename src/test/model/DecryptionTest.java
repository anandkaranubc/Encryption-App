package model;

import model.Decryption;
import model.Encryption;
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
        encryption = new Encryption();
    }

    @Test
    public void testPassDecryption() throws Exception {
        String a = "password";
        String b = "pa";
        String dataName = "chrome";
        cipherText = encryption.passEncryption(a, dataName);
        cipherText2 = encryption.passEncryption(b,"chrome2");

        assertEquals(a, Decryption.passDecryption(cipherText, pair));
        assertEquals(b, Decryption.passDecryption(cipherText2, pair));
    }
}
