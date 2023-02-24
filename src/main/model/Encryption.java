package model;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class Encryption {
    private static KeyPair pair;
    static Cipher cipher;
    byte[] cipherText;
    static KeyPairGenerator keyPairGen;

    static {
        try {
            initialize("RSA");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Encryption", e);
        }
    }

    public static void initialize(String algorithm) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algorithm);
        keyPairGen.initialize(2048);
        pair = keyPairGen.generateKeyPair();
        cipher = Cipher.getInstance(algorithm + "/ECB/PKCS1Padding");
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