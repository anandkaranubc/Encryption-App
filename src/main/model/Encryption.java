package model;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Scanner;

public class Encryption {
    private static KeyPair pair;
    static Cipher cipher;
    byte[] cipherText;

    private Scanner sc = new Scanner(System.in);

    static {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(2048);
            pair = keyPairGen.generateKeyPair();
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        } catch (Exception e) {
            e.printStackTrace();
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

    private String getDataName(String dataName) {
        return dataName;
    }

    public static KeyPair getPair() {
        return pair;
    }
}
