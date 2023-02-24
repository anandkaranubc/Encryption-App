package model;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class Encryption {
    private static final KeyPair pair;
    static Cipher cipher;
    byte[] cipherText;

    static {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(2048);
            pair = keyPairGen.generateKeyPair();
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Encryption", e);
        }
    }

    public byte[] passEncryption(String pass, String dataName) throws Exception {
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