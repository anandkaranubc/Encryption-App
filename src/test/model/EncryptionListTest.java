package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static model.EncryptionList.encryptedStringList;
import static org.junit.jupiter.api.Assertions.*;

public class EncryptionListTest {
    public static List<byte[]> encryptedCiphers = new ArrayList<>();
    public static List<String> dataNames = new ArrayList<>();
    Encryption encryption1;

    @BeforeEach
    public void runBefore() throws Exception {
        EncryptionList encryptionList = new EncryptionList();
        encryptedCiphers = EncryptionList.getEncryptedCiphers();
        dataNames = EncryptionList.getDataNames();
        encryption1 = new Encryption();
    }

    @Test
    public void testAddEncryptedCiphers() throws Exception {
        String pass1, pass2, pass3;
        byte [] ciphertext1, ciphertext2, ciphertext3;
        String data1, data2, data3;
        List<byte[]> encryptedCiphersOrg = new ArrayList<>();

        assertEquals(0, encryptedCiphers.size());

        pass1 = "password2";
        data1 = "Chrome2";
        ciphertext1 = encryption1.passEncryption(pass1, data1);
        encryptedCiphersOrg.add(ciphertext1);
        assertEquals(1, encryptedCiphers.size());
        assertEquals(encryptedCiphersOrg, encryptedCiphers);
        assertEquals(ciphertext1, encryptedCiphers.get(0));

        pass2 = "password2";
        data2 = "Chrome2";
        ciphertext2 = encryption1.passEncryption(pass2, data2);
        encryptedCiphersOrg.add(ciphertext2);

        pass3 = "password3";
        data3 = "Chrome3";
        ciphertext3 = encryption1.passEncryption(pass3, data3);
        encryptedCiphersOrg.add(ciphertext3);

        assertEquals(3, encryptedCiphers.size());
        assertEquals(encryptedCiphersOrg, encryptedCiphers);
        assertEquals(ciphertext2, encryptedCiphers.get(1));
        assertEquals(ciphertext3, encryptedCiphers.get(2));
    }

    @Test
    public void testAddDataName() throws Exception {
        String pass1, pass2, pass3;
        String data1, data2, data3;
        byte [] ciphertext1, ciphertext2, ciphertext3;
        List<String> dataNamesOrg = new ArrayList<>();

        assertEquals(0, dataNamesOrg.size());

        pass1 = "password1";
        data1 = "Chrome1";
        dataNamesOrg.add(data1);
        ciphertext1 = encryption1.passEncryption(pass1, data1);
        assertEquals(1, dataNames.size());
        assertEquals(dataNamesOrg, dataNames);
        assertEquals(data1, dataNames.get(0));

        pass2 = "password2";
        data2 = "Chrome2";
        dataNamesOrg.add(data2);
        ciphertext2 = encryption1.passEncryption(pass2, data2);

        pass3 = "password3";
        data3 = "Chrome3";
        dataNamesOrg.add(data3);
        ciphertext3 = encryption1.passEncryption(pass3, data3);


        assertEquals(3, dataNames.size());
        assertEquals(dataNamesOrg, dataNames);
        assertEquals(data2, dataNames.get(1));
        assertEquals(data3, dataNames.get(2));

    }

    @Test
    public void testEncryptedStringList() throws Exception {
        String pass1, pass2, pass3;
        byte [] ciphertext1, ciphertext2, ciphertext3;
        String data1, data2, data3;
        List<String> encryptedStringsOrg = new ArrayList<>();

        assertEquals(0, EncryptionList.encryptedStringList().size());

        pass1 = "password1";
        data1 = "Chrome1";
        ciphertext1 = encryption1.passEncryption(pass1, data1);
        encryptedStringsOrg.add(new String(ciphertext1, StandardCharsets.UTF_8));
        assertEquals(1, encryptedStringList().size());
        assertEquals(encryptedStringsOrg, encryptedStringList());
        assertEquals(new String(ciphertext1, StandardCharsets.UTF_8), encryptedStringList().get(0));

        pass2 = "password2";
        data2 = "Chrome2";
        ciphertext2 = encryption1.passEncryption(pass2, data2);
        encryptedStringsOrg.add(new String(ciphertext2, StandardCharsets.UTF_8));

        pass3 = "password3";
        data3 = "Chrome3";
        ciphertext3 = encryption1.passEncryption(pass3, data3);
        encryptedStringsOrg.add(new String(ciphertext3, StandardCharsets.UTF_8));

        assertEquals(3, encryptedStringList().size());
        assertEquals(encryptedStringsOrg, encryptedStringList());
        assertEquals(new String(ciphertext2, StandardCharsets.UTF_8), encryptedStringsOrg.get(1));
        assertEquals(new String(ciphertext3, StandardCharsets.UTF_8), encryptedStringsOrg.get(2));
    }

    @AfterEach
    public void resetEncryptionList() {
        EncryptionList.getDataNames().clear();
        EncryptionList.getEncryptedCiphers().clear();
    }
}
