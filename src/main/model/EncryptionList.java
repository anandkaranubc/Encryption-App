package model;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EncryptionList {

    /**
     * The EncryptionList class maintains a list of encrypted ciphers and their corresponding data names.
     *
     * It provides methods for adding encrypted ciphers and data names to the list, and for retrieving them.
     *
     * Usage:
     * - Call addEncryptedCiphers() method to add an encrypted cipher to the list.
     * - Call addDataName() method to add a data name to the list.
     * - Call getEncryptedCiphers() method to get a list of encrypted ciphers.
     * - Call getDataNames() method to get a list of data names.
     * - Call encryptedStringList() method to get a list of encrypted strings converted from byte array using UTF-8
     * charset.
     */

    private static List<byte[]> encryptedCiphers = new ArrayList<>();
    private static List<String> dataNames = new ArrayList<>();

//    Requires: a byte array of cipher text to add to the list of encrypted ciphers.
//    Modifies: this.encryptedCiphers
//    Effects: adds the given byte array of cipher text to the list of encrypted ciphers.
    public static void addEncryptedCiphers(byte[] cipherText) {
        encryptedCiphers.add(cipherText);
    }

//    Requires: a String object representing the name of the data corresponding to the cipher text to add to the list.
//    Modifies: this.dataNames
//    Effects: adds the given data name to the list of data names.
    public static void addDataName(String dataName) {
        dataNames.add(dataName);
    }

//    Effects: returns a list of Strings containing the encrypted ciphers converted from byte arrays using the UTF-8
//    character set.
    public static List<String> encryptedStringList() {
        List<String> encryptedStrings = new ArrayList<>();
        for (byte[] encryptedCipher : encryptedCiphers) {
            encryptedStrings.add(new String(encryptedCipher, StandardCharsets.UTF_8));
        }
        return encryptedStrings;
    }

    public static List<byte []> getEncryptedCiphers() {
        return encryptedCiphers;
    }

    public static List<String> getDataNames() {
        return dataNames;
    }
}