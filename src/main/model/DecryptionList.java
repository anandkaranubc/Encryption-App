package model;

import java.util.List;

import static model.Decryption.passDecryption;

public class DecryptionList {

    static List<byte []> encryptedCiphers = EncryptionList.getEncryptedCiphers();
    static List<String> dataNames = EncryptionList.getDataNames();

    public static void printDecryptedStrings() throws Exception {
        for (int i = 0; i < encryptedCiphers.size(); i++) {
            byte [] cipher = encryptedCiphers.get(i);
            String password = passDecryption(cipher, model.Encryption.getPair());
            System.out.println(dataNames.get(i) + ": " + password);
        }
    }

    public static void searchDataName(String dataName) throws Exception {
        int index = dataNames.indexOf(dataName);

        if (index == -1) {
            System.out.println("Sorry, data name not found.");
        } else {
            String answer = passDecryption(encryptedCiphers.get(index), model.Encryption.getPair());
            System.out.println("Your password for " + dataName + " is: " + answer);
        }
    }
}
