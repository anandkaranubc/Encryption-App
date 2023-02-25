package model;

import javax.crypto.Cipher;
import java.security.KeyPair;


public class Decryption {

    /**
     * The Decryption class provides decryption functionality using the RSA algorithm.
     *
     * It uses a key pair generated by the Encryption class and the same cipher used for encryption.
     *
     * Usage:
     * - Create an instance of the Decryption class.
     * - Call passDecryption() method with the cipher text and the key pair generated by the Encryption class to
         decrypt the text.
     */

    Encryption encryption = Encryption.getInstance();
    static Cipher cipher = model.Encryption.cipher;

//  Modifies: this.cipher
//  Effects: Creates a new Decryption object and initializes a cipher for decryption.
    public Decryption() throws Exception {
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    }

//    Requires: the cipherText and pair parameters must not be null.
//    Effects: returns the decrypted password as a string.
    public static String passDecryption(byte[] cipherText, KeyPair pair) throws Exception {
        //Initializing the same cipher for decryption
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());

        //Decrypting the text
        byte[] decipheredText = cipher.doFinal(cipherText);
        return new String(decipheredText);
    }
}
