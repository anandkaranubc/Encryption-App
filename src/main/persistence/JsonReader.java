package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

import model.Encryption;
import model.EncryptionList;
import model.Progress;
import org.json.*;
import ui.EncryptionApp;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Progress read() throws Exception {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseProgress(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses workroom from JSON object and returns it
    private Progress parseProgress(JSONObject jsonObject) throws Exception {

        List<String> dataNames = listExtractor(jsonObject, "dataNames");
        List<String> passes = listExtractor(jsonObject, "passes");
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");

        Encryption encryption = Encryption.getInstance();
        encryption.passEncryptionInList(passes, dataNames);

        EncryptionList.setVariables(dataNames);
        EncryptionApp.setVariables(username, password);

        Progress progress = new Progress();
        return progress;
    }

    public static List<String> listExtractor(JSONObject json, String key) {
        List<String> dataNames = new ArrayList<>();
        JSONArray dataNamesArray = json.optJSONArray(key);
        if (dataNamesArray != null) {
            for (int i = 0; i < dataNamesArray.length(); i++) {
                String name = dataNamesArray.optString(i);
                if (name != null) {
                    dataNames.add(name);
                }
            }
        }
        return dataNames;
    }


    public static KeyPair parseKeyPairString(String keyPairString) throws Exception {
        // Parse the key pair string to extract the public and private keys
        String[] keyStrings = keyPairString.split("\n");
        byte[] publicKeyBytes = Base64.getDecoder().decode(keyStrings[0].getBytes("UTF-8"));
        byte[] privateKeyBytes = Base64.getDecoder().decode(keyStrings[1].getBytes("UTF-8"));

        // Generate a new KeyPair object from the extracted key material
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
        return new KeyPair(publicKey, privateKey);
    }

}
