package model;

import java.util.List;

import static model.Decryption.passDecryption;

public class DecryptionList {

    static List<byte []> encryptedCiphers = EncryptionList.getEncryptedCiphers();
    static List<String> dataNames = EncryptionList.getDataNames();

    public int getDataNameIndex(String dataName) {
        return dataNames.indexOf(dataName);
    }

    public String getDecryptedStringAtIndex(int index) throws Exception {
        return passDecryption(encryptedCiphers.get(index), model.Encryption.getPair());
    }
}
