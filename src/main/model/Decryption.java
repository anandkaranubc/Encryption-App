package model;

import javax.crypto.Cipher;
import java.security.KeyPair;


public class Decryption {
    Encryption encryption = Encryption.getInstance();
    static Cipher cipher = model.Encryption.cipher;

    public Decryption() throws Exception {
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    }

    public static String passDecryption(byte[] cipherText, KeyPair pair) throws Exception {
        //Initializing the same cipher for decryption
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());

        //Decrypting the text
        byte[] decipheredText = cipher.doFinal(cipherText);
        return new String(decipheredText);
    }
}
