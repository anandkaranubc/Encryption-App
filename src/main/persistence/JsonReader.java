package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import model.Encryption;
import model.EncryptionList;
import model.Progress;
import org.json.*;
import ui.EncryptionApp;

// Represents a reader that reads progress from JSON data stored in file
public class JsonReader {

    /**

     The JsonReader class represents a reader that reads progress from JSON data stored in a file.
     It provides methods to read a JSON file and parse the progress data from it. The parsed data is used to
     initialize an instance of the Progress class, which is returned by the read method. The class also contains
     a helper method to extract a list of strings from a JSON object. The extracted data is used to initialize
     variables in the Encryption and EncryptionList classes, and the username and password are set in the
     EncryptionApp class. If an error occurs while reading the file, an IOException is thrown.
     */

    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads progress from file and returns it;
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

    // EFFECTS: parses progress from JSON object and returns it
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

    //REQUIRES: A JSON object json to extract data from.
    //A string key representing the key whose associated value is to be extracted from the JSON object.

    //EFFECTS: Initializes a new empty ArrayList of String type to store the extracted data.
    //Attempts to retrieve a JSON array associated with the given key from the input JSON object.
    //If the JSON array is found, iterates over its elements and extracts each element as a string, adding it to the dataNames list.
    //Returns the list of extracted data names, or an empty list if no data is found for the given key.
    public static List<String> listExtractor(JSONObject json, String key) {
        List<String> dataNames = new ArrayList<>();
        JSONArray dataNamesArray = json.optJSONArray(key);
        if (dataNamesArray != null) {
            for (int i = 0; i < dataNamesArray.length(); i++) {
                String name = dataNamesArray.optString(i);
                dataNames.add(name);
            }
        }
        return dataNames;
    }
}
