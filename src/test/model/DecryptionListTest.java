package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static model.EncryptionList.encryptedStringList;
import static org.junit.jupiter.api.Assertions.*;

public class DecryptionListTest {
    public static List<byte[]> encryptedCiphers = new ArrayList<>();
    public static List<String> dataNames = new ArrayList<>();
    Encryption encryption1;
    DecryptionList decryptionList;

    @BeforeEach
    public void runBefore() {
        EncryptionList encryptionList = new EncryptionList();
        encryptedCiphers = EncryptionList.getEncryptedCiphers();
        dataNames = EncryptionList.getDataNames();
        encryption1 = new Encryption();
        decryptionList = new DecryptionList();
    }

    @Test
    public void testGetDataNameIndex() throws Exception {
        String pass1, pass2;
        byte [] ciphertext1, ciphertext2;
        String data1, data2, data3;

        pass1 = "password2";
        data1 = "Chrome2";
        ciphertext1 = encryption1.passEncryption(pass1, data1);

        pass2 = "password33";
        data2 = "Chrome33";
        ciphertext2 = encryption1.passEncryption(pass2, data2);

        assertEquals(0, decryptionList.getDataNameIndex("Chrome2"));
        assertEquals(1, decryptionList.getDataNameIndex("Chrome33"));
        assertEquals(-1, decryptionList.getDataNameIndex("helloWorld"));
    }

    @Test
    public void testGetDecryptedStringAtIndex() throws Exception {
        String pass1, pass2;
        byte [] ciphertext1, ciphertext2;
        String data1, data2, data3;

        pass1 = "password2";
        data1 = "Chrome2";
        ciphertext1 = encryption1.passEncryption(pass1, data1);

        pass2 = "password33";
        data2 = "Chrome33";
        ciphertext2 = encryption1.passEncryption(pass2, data2);

        assertEquals(pass1, decryptionList.getDecryptedStringAtIndex(0));
        assertEquals(pass2, decryptionList.getDecryptedStringAtIndex(1));
    }

    @AfterEach
    public void resetEncryptionList() {
        EncryptionList.getDataNames().clear();
        EncryptionList.getEncryptedCiphers().clear();
    }
}