package model;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class Encryption {
    private static KeyPair pair;
    static Cipher cipher;
    byte[] cipherText;
    private int count = 0;

    static void initializeKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        pair = keyPairGen.generateKeyPair();
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    }

    public byte[] passEncryption(String pass, String dataName) throws Exception {

        if (count == 0) {
            initializeKeyPair();
            count++;
        }

        cipher.init(Cipher.ENCRYPT_MODE, pair.getPublic());

        EncryptionList.addDataName(dataName);

        byte[] input = pass.getBytes();
        cipher.update(input);
        cipherText = cipher.doFinal();
        EncryptionList.addEncryptedCiphers(cipherText);

        return cipherText;
    }

    public static KeyPair getPair() {
        return pair;
    }
}
