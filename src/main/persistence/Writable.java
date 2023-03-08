package persistence;

import org.json.JSONObject;

public interface Writable {

    /**
     The Writable interface defines a contract for objects that can be written to JSON format.
     It requires implementing classes to provide a method that returns a JSONObject representation of the object.
     This interface is used in conjunction with the JsonWriter class to serialize objects to a file in JSON format.
     */

    // EFFECTS: returns this as JSON object
    JSONObject toJson() throws Exception;
}
