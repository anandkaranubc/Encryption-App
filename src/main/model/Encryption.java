package model;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.List;

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

//    Modifies: this.pair, this.cipher
//    Effects: Generates a new key pair and cipher for the Encryption object.
    public Encryption() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        pair = keyPairGen.generateKeyPair();
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    }

//    Modifies: this.instance
//    Effects: If instance is null, creates a new instance of the Encryption class, otherwise returns the existing
//    instance.
//    Returns the instance of the Encryption class.
    public static synchronized Encryption getInstance() throws Exception {
        if (instance == null) {
            instance = new Encryption();
        }
        return instance;
    }

    //    Requires: pass and dataName are not null.
//    Modifies: EncryptionList.getDataNames(), EncryptionList.getEncryptedCiphers(), this.cipherText
//    Effects: Initializes the cipher with the public key from the key pair, adds the data name to the EncryptionList,
//             encrypts the password using the cipher, adds the encrypted password to the EncryptionList, and returns
//             the encrypted password as a byte array.
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

    public void passEncryptionInList(List<String> passes, List<String> dataNames) throws Exception {
        for (int i = 0; i < passes.size(); i++) {
            passEncryption(passes.get(i), dataNames.get(i));
        }
    }
}