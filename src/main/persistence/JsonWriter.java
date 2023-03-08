package persistence;

import model.Progress;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class JsonWriter {

    /**
     The JsonWriter class represents a writer that writes progress data in JSON format to a file.
     It takes in a destination file and provides methods to open, write, and close the writer.
     The class utilizes the Progress class to get the data to be written and converts it into a JSONObject before
     writing it to the file. The class uses a PrintWriter object to write the JSON data to the file and provides
     a method to save the JSON string to the file. If the destination file cannot be opened for writing, the class
     throws a FileNotFoundException.

     */

    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of progress to file
    public void write(Progress progress) throws Exception {
        JSONObject json = progress.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
