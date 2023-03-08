package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;
import ui.EncryptionApp;
import java.util.List;

public class Progress implements Writable {

    /**

     The Progress class is responsible for storing the progress of the encryption and decryption process.
     It contains static fields to store the list of data names, username, and password entered by the user.
     This class implements the Writable interface, allowing it to be serialized and saved to a JSON file.
     The class provides several methods to retrieve information about the progress, including the number of data names,
     the number of encrypted passwords, and the list of data names.
     Additionally, the class contains two private helper methods to convert the list of decrypted passwords and
     data names to JSON format for serialization.
     */

    static List<String> dataNames;
    static String username;
    static String password;

//  EFFECTS: Initializes the static dataNames field with the list of data names retrieved from the EncryptionList class.
//  Initializes the static username and password fields with the user's entered username and password retrieved from the
//  EncryptionApp class.
    public Progress() {
        dataNames = EncryptionList.getDataNames();
        username = EncryptionApp.getUsername();
        password = EncryptionApp.getPassword();
    }

//  EFFECTS: Converts the current Progress object to a JSON object.
//  Includes the encrypted passwords and data names as JSON arrays.
//  Includes the username and password as strings.
//  Returns a JSON object containing all of the above fields.
    @Override
    public JSONObject toJson() throws Exception {
        JSONObject json = new JSONObject();
        json.put("passes", passToJson());
        json.put("dataNames", dataNamesToJson());
        json.put("username", username);
        json.put("password", password);
        return json;
    }

//  EFFECTS: Creates a new instance of the DecryptionList class.
//  Retrieves the list of decrypted passwords from the DecryptionList object.
//  Converts the list of decrypted passwords to a JSON array.
//  Returns the resulting JSON array.
    private JSONArray passToJson() throws Exception {
        DecryptionList decryptionList = new DecryptionList();
        JSONArray jsonArray = new JSONArray();
        List<String> decStr = decryptionList.getDecryptedStrings();
        for (String pass : decStr) {
            jsonArray.put(pass);
        }
        return jsonArray;
    }

//  EFFECTS: Converts the dataNames list to a JSON array.
//  Returns the resulting JSON array.
    private JSONArray dataNamesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (String name : dataNames) {
            jsonArray.put(name);
        }
        return jsonArray;
    }

    //EFFECTS: Returns the username string
    public String getUsername() {
        return username;
    }

    //EFFECTS: Returns the password string
    public String getPassword() {
        return password;
    }

    //EFFECTS: Returns the size of dataNames list
    public int numDataNames() {
        return dataNames.size();
    }

    //EFFECTS: Retrieves the number of encrypted passwords from the EncryptionList class.
    //Returns the number of encrypted passwords as an integer value.
    public int numPasses() {
        return EncryptionList.getEncryptedCiphers().size();
    }

    //EFFECTS: Returns the list of string of dataNames
    public List<String> getDataNames() {
        return dataNames;
    }
}
