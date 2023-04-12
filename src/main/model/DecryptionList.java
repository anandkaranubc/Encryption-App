package model;

import java.util.ArrayList;
import java.util.List;

import static model.Decryption.passDecryption;

public class DecryptionList {

    /**
     * The DecryptionList class provides functionality to decrypt the cipher text stored in the EncryptionList.
     *
     * It uses the Decryption class to decrypt the cipher text and provides methods to retrieve the decrypted string.
     *
     * Usage:
     * - Create an instance of the DecryptionList class.
     * - Call getDataNameIndex() method with the data name to get the index of the corresponding cipher text in the
         list.
     * - Call getDecryptedStringAtIndex() method with the index of the cipher text to get the decrypted string.
     */

    static List<byte []> encryptedCiphers = EncryptionList.getEncryptedCiphers();
    static List<String> dataNames = EncryptionList.getDataNames();

//    Requires: dataName is a non-null String.
//    Effects: Returns an integer that represents the index of the cipher text in encryptedCiphers list.
//    Returns -1 if the dataName is not present in dataNames list.
    public int getDataNameIndex(String dataName) {
        dataNames = EncryptionList.getDataNames();
        EventLog.getInstance().logEvent(new Event("Searched for password of " + dataName));
        return dataNames.indexOf(dataName);
    }

//    Requires: index is a non-negative integer that is less than the size of encryptedCiphers.
//    Effects: Returns a String that represents the decrypted text of the cipher text at the given index in
//    the encryptedCiphers list.
    public String getDecryptedStringAtIndex(int index) throws Exception {
        return passDecryption(encryptedCiphers.get(index), model.Encryption.getPair());
    }

//    Effects: Returns a list of all the decrypted passwords present in the program which
//    were added by the user
    public List<String> getDecryptedStrings() throws Exception {
        List<String> decryptedStrings = new ArrayList<>();
        for (int i = 0; i < encryptedCiphers.size(); i++) {
            decryptedStrings.add(getDecryptedStringAtIndex(i));
        }
        EventLog.getInstance().logEvent(new Event("Added all the decrypted passwords to the Decryption List "
                + "with their respective data names"));
        return decryptedStrings;
    }
}
