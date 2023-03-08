package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;
import ui.EncryptionApp;
import java.util.List;

public class Progress implements Writable {

    static List<String> dataNames;
    static String username;
    static String password;

    public Progress() {
        dataNames = EncryptionList.getDataNames();
        username = EncryptionApp.getUsername();
        password = EncryptionApp.getPassword();
    }

    @Override
    public JSONObject toJson() throws Exception {
        JSONObject json = new JSONObject();
        json.put("passes", passToJson());
        json.put("dataNames", dataNamesToJson());
        json.put("username", username);
        json.put("password", password);
        return json;
    }

    private JSONArray passToJson() throws Exception {
        DecryptionList decryptionList = new DecryptionList();
        JSONArray jsonArray = new JSONArray();
        for (String pass : decryptionList.getDecryptedStrings()) {
            jsonArray.put(pass);
        }
        return jsonArray;
    }

    private JSONArray dataNamesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (String name : dataNames) {
            jsonArray.put(name);
        }
        return jsonArray;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int numDataNames() {
        return dataNames.size();
    }

    public int numPasses() {
        return EncryptionList.getEncryptedCiphers().size();
    }

    public List<String> getDataNames() {
        return dataNames;
    }

    public void clearDataNames() {
        dataNames.clear();
    }
}
