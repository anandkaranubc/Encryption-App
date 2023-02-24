package model;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EncryptionList {
    private static List<byte[]> encryptedCiphers = new ArrayList<>();
    private static List<String> dataNames = new ArrayList<>();

    public static void addEncryptedCiphers(byte[] cipherText) {
        encryptedCiphers.add(cipherText);
    }

    public static void addDataName(String dataName) {
        dataNames.add(dataName);
    }

    public static List<String> encryptedStringList() {
        List<String> encryptedStrings = new ArrayList<>();
        for (int i = 0; i < encryptedCiphers.size(); i++) {
            encryptedStrings.add(new String(encryptedCiphers.get(i), StandardCharsets.UTF_8));
        }
        return encryptedStrings;
    }

    public static void printEncryptedStrings() {
        List<String> encryptedStringsList = encryptedStringList();
        for (int i = 0; i < encryptedStringsList.size(); i++) {
            System.out.println(dataNames.get(i) + ": " + encryptedStringsList.get(i));
        }
    }

    public static List<byte []> getEncryptedCiphers() {
        return encryptedCiphers;
    }

    public static List<String> getDataNames() {
        return dataNames;
    }
}