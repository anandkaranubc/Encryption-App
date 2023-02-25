package model;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class Encryption {

    /**
     * The Encryption class provides encryption functionality using the RSA algorithm.
     *
     * It generates a key pair and initializes a cipher for encrypting data using the public key.
     *
     * This class uses the Singleton pattern to ensure that only one instance of the Encryption class is created.
     *
     * Usage:
     * - Call getInstance() method to get an instance of the Encryption class and if not present, it just creates one.
     * - Call passEncryption() method to encrypt a password string and add it to the EncryptionList.
     * - Call getPair() method to get the generated key pair.
     */

    private static KeyPair pair;
    static Cipher cipher;
    byte[] cipherText;
    static KeyPairGenerator keyPairGen;
    private static Encryption instance;

    public Encryption() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        pair = keyPairGen.generateKeyPair();
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    }

    public static synchronized Encryption getInstance() throws Exception {
        if (instance == null) {
            instance = new Encryption();
        }
        return instance;
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